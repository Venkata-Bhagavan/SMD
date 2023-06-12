package app.mr.venky.smd.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.VibrationAttributes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.FirebaseDatabase;

import app.mr.venky.smd.R;
import app.mr.venky.smd.databinding.FragmentSettingsBinding;


public class SettingsFragment extends BottomSheetDialogFragment {

    private final String TAG = "SettingsFragment";
    private FragmentSettingsBinding binding;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.getReference("settings").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "onViewCreated: " + task.getResult().getValue());

                SettingValues values = task.getResult().getValue(SettingValues.class);
                if (values != null) {
                    binding.alarmMode.setChecked(values.alarm_mode);
                    binding.highAlertTime.setText(String.valueOf(values.getHigh_priority_time()));
                    binding.lowAlertTime.setText(String.valueOf(values.getLow_priority_time()));


                    binding.save.setOnClickListener(view1 -> {
                        values.setAlarm_mode(binding.alarmMode.isChecked());
                        values.setHigh_priority_time(Integer.parseInt(binding.highAlertTime.getText().toString()));
                        values.setLow_priority_time(Integer.parseInt(binding.lowAlertTime.getText().toString()));

                        database.getReference("settings").setValue(values).addOnCompleteListener(task1 -> {
                            Toast.makeText(view.getContext(), task1.isSuccessful()? "Saved Successfully": "Failed to Save", Toast.LENGTH_SHORT).show();
                        });
                    });

                }
/*                try {
                    long low_time = (long) task.getResult().child("low_priority_time").getValue();
                    long high_time = (long) task.getResult().child("high_priority_time").getValue();
                    boolean alarm_mode = (boolean) task.getResult().child("alarm_mode").getValue();

                    binding.lowAlertTime.setText(String.valueOf(low_time));
                    binding.highAlertTime.setText(String.valueOf(high_time));
                    binding.alarmMode.setChecked(alarm_mode);
                } catch (Exception e) {
                    Log.e(TAG, "onViewCreated: "+e );
                }*/

            } else {
                Toast.makeText(getContext(), "Failed to Load Settings", Toast.LENGTH_SHORT).show();
            }
        });
    }

    static class SettingValues {
        private boolean alarm_mode;
        private long low_priority_time;
        private long high_priority_time;
        private long status;

        public SettingValues() {
        }

        public SettingValues(boolean alarm_mode, long low_priority_time, long high_priority_time, long status) {
            this.alarm_mode = alarm_mode;
            this.low_priority_time = low_priority_time;
            this.high_priority_time = high_priority_time;
            this.status = status;
        }

        public boolean isAlarm_mode() {
            return alarm_mode ;
        }

        public void setAlarm_mode(boolean alarm_mode) {
            this.alarm_mode = alarm_mode;
        }

        public long getLow_priority_time() {
            return low_priority_time;
        }

        public void setLow_priority_time(long low_priority_time) {
            this.low_priority_time = low_priority_time;
        }

        public long getHigh_priority_time() {
            return high_priority_time;
        }

        public void setHigh_priority_time(long high_priority_time) {
            this.high_priority_time = high_priority_time;
        }

        public long getStatus() {
            return status;
        }

        public void setStatus(long status) {
            this.status = status;
        }
    }

}