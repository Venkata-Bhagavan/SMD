package app.mr.venky.smd;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.NotificationTarget;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessaging";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            // Handle the payload data
            String name = remoteMessage.getData().get("name");
            String accuracy = remoteMessage.getData().get("accuracy");
            String timestamp = remoteMessage.getData().get("timestamp");
            String imageUrl = remoteMessage.getData().get("image");

            String title = "Security Alert";
            String message = "Found " + name + (accuracy != null ? " with " + accuracy + " accuracy." : ".") + "\n" + timestamp;


            // Load the image using Glide
            Bitmap imageBitmap = null;
            if (imageUrl != null) {
                try {
                    RequestOptions requestOptions = new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .dontTransform();

                    FutureTarget<Bitmap> futureTarget = Glide.with(this)
                            .asBitmap()
                            .load(imageUrl)
                            .apply(requestOptions)
                            .submit();

                    imageBitmap = futureTarget.get();
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(TAG, "Failed to load image from URL: " + imageUrl, e);
                }
            }



            // Create a notification
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "your_channel_id")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            // If image loading was successful, set the large icon
            if (imageBitmap != null) {
                notificationBuilder.setLargeIcon(imageBitmap);
            }

         /*   // If an image URL is provided, load the image using Glide
            if (imageUrl != null) {
                Bitmap bitmap = getBitmapFromUrl(imageUrl);
                if (bitmap != null) {
                    NotificationTarget notificationTarget = new NotificationTarget(
                            this,
                            R.id.notification_image,
                            notificationBuilder.bigContentView,
                            notificationBuilder.getNotification(),
                            1
                    );
                    Glide.with(getApplicationContext())
                            .asBitmap()
                            .load(imageUrl)
                            .into(notificationTarget);
                }
            }*/

            // Display the notification
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String channelId = "Sem_4_project";
                    CharSequence channelName = "Sem 4 Project";
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
                    notificationManager.createNotificationChannel(channel);
                }
                notificationManager.notify(0, notificationBuilder.build());
            }
        }
    }

/*    private Bitmap getBitmapFromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            Log.e(TAG, "Failed to get bitmap from URL: " + imageUrl, e);
            return null;
        }
    }*/
}
