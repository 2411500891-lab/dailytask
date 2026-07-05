package com.example.dailytask;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dailytaskplus.R;
import com.example.dailytaskplus.model.Task;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private Context context;
    private List<Task> list;
    private Listener listener;

    public interface Listener {
        void onToggle(Task t);
        void onDelete(Task t);
    }

    public TaskAdapter(Context context, List<Task> list, Listener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_task, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task t = list.get(position);
        holder.tvTitle.setText(t.title);
        holder.tvDesc.setText(t.description);
        holder.tvCategory.setText(t.category != null ? t.category : "Umum");
        
        // Priority color indicator
        int priorityColor = context.getResources().getColor(R.color.medium);
        if ("High".equalsIgnoreCase(t.priority)) priorityColor = context.getResources().getColor(R.color.high);
        else if ("Low".equalsIgnoreCase(t.priority)) priorityColor = context.getResources().getColor(R.color.low);
        holder.viewPriority.getBackground().setTint(priorityColor);

        // Visual feedback for completed tasks
        if (t.is_done == 1) {
            holder.cbDone.setChecked(true);
            holder.tvTitle.setPaintFlags(holder.tvTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvTitle.setTextColor(context.getResources().getColor(R.color.text_muted));
            holder.tvTitle.setAlpha(0.6f);
            holder.tvDesc.setAlpha(0.5f);
            holder.tvCategory.setAlpha(0.5f);
        } else {
            holder.cbDone.setChecked(false);
            holder.tvTitle.setPaintFlags(holder.tvTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.tvTitle.setTextColor(context.getResources().getColor(R.color.text_dark));
            holder.tvTitle.setAlpha(1.0f);
            holder.tvDesc.setAlpha(1.0f);
            holder.tvCategory.setAlpha(1.0f);
        }

        holder.cbDone.setOnClickListener(v -> {
            t.is_done = holder.cbDone.isChecked() ? 1 : 0;
            listener.onToggle(t);
        });

        // Memudahkan klik: klik area teks juga akan mencentang
        holder.layoutText.setOnClickListener(v -> {
            holder.cbDone.setChecked(!holder.cbDone.isChecked());
            t.is_done = holder.cbDone.isChecked() ? 1 : 0;
            listener.onToggle(t);
        });

        holder.btnDelete.setOnClickListener(v -> listener.onDelete(t));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc, tvCategory;
        CheckBox cbDone;
        ImageButton btnDelete;
        View layoutText, viewPriority;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            cbDone = itemView.findViewById(R.id.cbDone);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            layoutText = itemView.findViewById(R.id.layoutText);
            viewPriority = itemView.findViewById(R.id.viewPriority);
        }
    }
}
