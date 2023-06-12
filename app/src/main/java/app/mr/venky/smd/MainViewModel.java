package app.mr.venky.smd;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import app.mr.venky.smd.objects.SmdObject;

public class MainViewModel extends AndroidViewModel {


    private static final String TAG = "MainViewModel";

    private FirebaseFirestore db;

    private MutableLiveData<List<SmdObject>> smdObjects = new MutableLiveData<>();

    public MutableLiveData<Integer> status = new MutableLiveData<>(-1);

    public LiveData<List<SmdObject>> getSmdObjects() {
        return smdObjects;
    }
    public void setSmdObjects(List<SmdObject> smdObjects) {
        this.smdObjects.setValue(smdObjects);
    }

    public LiveData<Integer> getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status.setValue(status);
    }

    public MainViewModel(@NonNull Application application) {
        super(application);
        db = FirebaseFirestore.getInstance();
        loadData();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        Log.d("TAG FCM TOKEN", token);
                    }
                });

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        db.getReference("settings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    long status = (long) snapshot.child("status").getValue();
                    setStatus((int) status);
                } catch (Exception e) {
                    Log.e(TAG, "onDataChange: long"+ e );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    protected void loadData() {
    /*    db.collection("smd")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<SmdObject> smdList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                SmdObject smdObject = document.toObject(SmdObject.class);
                                smdList.add(smdObject);
                            }
                            setSmdObjects(smdList);
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });*/

        db.collection("smd")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            Log.e("TAG", "onEvent: "+error );
                        }

                        List<SmdObject> smdList = new ArrayList<>();
                        if (value != null)
                            for (QueryDocumentSnapshot doc : value) {
                                SmdObject smdObject = doc.toObject(SmdObject.class);
                                smdList.add(smdObject);
                            }
                        setSmdObjects(smdList);
                    }
                });
    }

}
