package app.mr.venky.smd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Date;

import app.mr.venky.smd.databinding.ActivityMainBinding;
import app.mr.venky.smd.objects.SmdObject;

public class MainActivity extends AppCompatActivity {

    private boolean enabled = true;
    private ActivityMainBinding binding;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // status in toolbar
        binding.toolbar.status.setOnClickListener(view -> {
            enabled = !enabled;
            binding.toolbar.status.setText(enabled ? "Enabled" : "Disabled");
            binding.toolbar.statusImg.setImageResource(enabled ? R.drawable.enable_anim : R.drawable.disabled_anim);
            animateStatus(); // need to call this animation so that animation vector work.
        });


        recentCard();
        historyCard();
        moreCard();

    }

    private void recentCard() {
        viewModel.getSmdObjects().observe(this, smdObjects -> {
            if (!smdObjects.isEmpty()) {
                SmdObject object = smdObjects.get(0);
                binding.recentCard.recentObject.setText("Found " + object.getName() + " with " + object.getAccuracy() + "% accuracy.");
                Date date = object.getTimestamp().toDate();
                binding.recentCard.recentTime.setText(date.toString());
                Glide.with(this).load(Uri.parse(object.getImage())).into(binding.recentCard.recentImage);
            }
        });
    }

    private void historyCard() {
//        binding.historyCard.recyclerView
    }


    @Override
    protected void onStart() {
        super.onStart();
        animateStatus(); // need to call this animation so that animation vector work.
    }

    private void moreCard() {
        // settings
        binding.moreCard.settings.setOnClickListener(view -> {
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
        });

        // about
        binding.moreCard.about.setOnClickListener(view -> {
            Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
        });

        // profile
        binding.moreCard.profile.setOnClickListener(view -> {
            Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
        });
    }

    private void animateStatus() {
        AnimationDrawable anim = (AnimationDrawable) binding.toolbar.statusImg.getDrawable();
        if (anim != null) anim.start();
    }

}