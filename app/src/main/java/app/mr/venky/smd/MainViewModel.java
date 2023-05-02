package app.mr.venky.smd;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

public class MainViewModel extends AndroidViewModel {

    private FirebaseFirestore firestore;


    public MainViewModel(@NonNull Application application) {
        super(application);
        firestore = FirebaseFirestore.getInstance();
    }

    

}
