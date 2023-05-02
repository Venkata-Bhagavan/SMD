package app.mr.venky.smd;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import app.mr.venky.smd.objects.SmdObject;

public class MainViewModel extends AndroidViewModel {

    private FirebaseFirestore db;

    private MutableLiveData<List<SmdObject>> smdObjects = new MutableLiveData<>();

    public LiveData<List<SmdObject>> getSmdObjects() {
        return smdObjects;
    }

    public void setSmdObjects(List<SmdObject> smdObjects) {
        this.smdObjects.setValue(smdObjects);
    }

    public MainViewModel(@NonNull Application application) {
        super(application);
        db = FirebaseFirestore.getInstance();
        loadData();
    }

    protected void loadData() {
        db.collection("smd")
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
                });
    }

}
