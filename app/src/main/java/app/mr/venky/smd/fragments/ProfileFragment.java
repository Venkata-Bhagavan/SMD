package app.mr.venky.smd.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import app.mr.venky.smd.LauncherActivity;
import app.mr.venky.smd.databinding.FragmentProfileBinding;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     ProfileFragment.newInstance().show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class ProfileFragment extends BottomSheetDialogFragment {

    private FragmentProfileBinding binding;

    // TODO: Customize parameters
    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            binding.profileName.setText(user.getDisplayName());
            binding.profileEmail.setText(user.getEmail());
            Glide.with(view).load(user.getPhotoUrl()).into(binding.prifileImage);
            binding.signout.setOnClickListener(view1 -> {
                // signing out from firebase
                auth.signOut();

         /*       signing out from google
                  (if this not performed, on clicking signin it will login with
                  same google account and not ask for choosing account)*/
                GoogleSignInOptions gso = new GoogleSignInOptions.
                        Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                        build();
                GoogleSignInClient signInClient = GoogleSignIn.getClient(view.getContext(), gso);
                signInClient.signOut();

                // navigating to Launcher activity
                Intent intent = new Intent(getContext(), LauncherActivity.class);
                startActivity(intent);
                if (getActivity() != null) getActivity().finish();
            });
        }

    }

    private void signOut(FirebaseAuth auth, Context context) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}