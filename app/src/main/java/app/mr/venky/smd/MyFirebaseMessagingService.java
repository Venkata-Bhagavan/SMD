package app.mr.venky.smd;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;
import java.util.concurrent.ExecutionException;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessaging";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "onMessageReceived: " + remoteMessage.getData());
            // Handle the payload data
            String message = remoteMessage.getData().get("message");
            String imageUrl = remoteMessage.getData().get("image");
            String title = "Security Alert";

            notify(title, message, imageUrl);
        }
    }

    private void notify(String title, String body, String imageUrl) {

        Log.i(TAG, "notify: called");

//        Building a Pending intent. we can use this to navigate to required activity on notification click.
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

//        This is for setting a picture in notification
        NotificationCompat.BigPictureStyle bigPicture = null;
        try {
            bigPicture = new NotificationCompat.BigPictureStyle()
                    .setBigContentTitle(title)
                    .setSummaryText(body)
                    .bigPicture(Glide.with(this).asBitmap().load(imageUrl).submit().get());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

//        Building a notification.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Sem_4_project")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setStyle(bigPicture) // even if style was null it was not a problem
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        // Display the notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelId = "Sem_4_project";
                CharSequence channelName = "Sem 4 Project";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
                notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(new Random().nextInt(1000), builder.build());
        }
    }
}
