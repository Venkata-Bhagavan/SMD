package app.mr.venky.smd;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.Date;
import java.util.List;

import app.mr.venky.smd.databinding.HistorySmallViewBinding;
import app.mr.venky.smd.objects.SmdObject;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private List<SmdObject> objects;
    private HistoryAdapter.OnClickListener listener;
    private Context context;

    public HistoryAdapter(List<SmdObject> objects, OnClickListener listener) {
        this.objects = objects;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ViewHolder(HistorySmallViewBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {
        SmdObject object = objects.get(position);
        Glide.with(context).load(Uri.parse(object.getImage())).into(holder.binding.objectImage);
        holder.binding.objectName.setText("Found " + object.getName() + " with " + object.getAccuracy() + "% accuracy.");
        Date date = object.getTimestamp().toDate();
        holder.binding.objectTimestamp.setText(date.toString());

        holder.itemView.setOnClickListener(view -> {
            this.listener.onItemClick(holder, position);
        });
        holder.itemView.setOnLongClickListener(view -> listener.onItemLongClick(holder, position));
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        HistorySmallViewBinding binding;

        public ViewHolder(@NonNull HistorySmallViewBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public interface OnClickListener {
        void onItemClick(HistoryAdapter.ViewHolder holder, int position);

        boolean onItemLongClick(HistoryAdapter.ViewHolder holder, int position);
    }
}
