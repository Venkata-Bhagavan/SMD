package app.mr.venky.smd;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import app.mr.venky.smd.databinding.ActivityLauncherBinding;

public class LauncherActivity extends AppCompatActivity {

    private static final String TAG = "LauncherActivity";
    private ActivityLauncherBinding binding;
    private FirebaseAuth mAuth;

    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;
    private ActivityResultLauncher<Intent> gResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLauncherBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        //google SignIn
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //new method of OnActivityResult..
        gResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
            handleGoogleSignInResult(task);

        });

        binding.loginGSignin.setOnClickListener(view1 -> {
//            if (NetworkUtil.isConnected(this)) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                gResult.launch(signInIntent);
//            }else Toast.makeText(this, "Check your Network", Toast.LENGTH_SHORT).show();
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            if (account != null)
                firebaseAuthWithGoogle(account.getIdToken());


        } catch (ApiException e) {

            //todo: check if there was an internet connection because
            // this error also got when there is no internet connection.
            Log.w("signInResult failed", e.toString());

        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.loginGSignin.setVisibility(View.INVISIBLE);
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.loginGSignin.setVisibility(View.VISIBLE);

                    if (task.isSuccessful()) {

                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                        if (Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getAdditionalUserInfo()).isNewUser()) {
                            //todo: what to do when new user sign in..
                            Toast.makeText(this, "welcome " + (user != null ? user.getDisplayName() : ""), Toast.LENGTH_SHORT).show();
                        } else {
                            //todo: what to do when old user sign in..
                            Toast.makeText(this, "welcome back " + (user != null ? user.getDisplayName() : "again"), Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "SignIn Failed", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                }).addOnFailureListener(e -> {
                    if (e instanceof FirebaseAuthInvalidUserException) {
                        Toast.makeText(this, "your account is disabled", Toast.LENGTH_SHORT).show();
                        GoogleSignIn.getClient(this, gso).signOut();
                    }
                });
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            subscribeToFCMTopic();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private void subscribeToFCMTopic() {
        // Subscribe to the "smd" topic
        FirebaseMessaging.getInstance().subscribeToTopic("smd")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Subscribed to topic: smd");
                    } else {
                        Log.e(TAG, "Failed to subscribe to topic: smd", task.getException());
                    }
                });
    }
}