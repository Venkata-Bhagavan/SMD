package app.mr.venky.smd.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;
import java.util.Objects;

import app.mr.venky.smd.HistoryAdapter;
import app.mr.venky.smd.MainActivity;
import app.mr.venky.smd.MainViewModel;
import app.mr.venky.smd.R;
import app.mr.venky.smd.databinding.FragmentHistoryBinding;
import app.mr.venky.smd.databinding.FragmentProfileBinding;
import app.mr.venky.smd.objects.SmdObject;


public class HistoryFragment extends BottomSheetDialogFragment {

    private FragmentHistoryBinding binding;

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    public HistoryFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        if (MainActivity.history != null) {

        HistoryAdapter adapter = new HistoryAdapter(MainActivity.history, new HistoryAdapter.OnClickListener() {
            @Override
            public void onItemClick(HistoryAdapter.ViewHolder holder, int position) {
                Toast.makeText(view.getContext(), "Clicked on item " + position, Toast.LENGTH_SHORT).show();
            }
            @Override
            public boolean onItemLongClick(HistoryAdapter.ViewHolder holder, int position) {
                return false;
            }
        });
        binding.historyList.setLayoutManager(manager);
        binding.historyList.setAdapter(adapter);
        }

    }
}