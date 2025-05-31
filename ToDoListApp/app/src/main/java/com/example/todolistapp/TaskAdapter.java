package com.example.todolistapp;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private final List<Task> taskList;
    private final OnDeleteClickListener deleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(Task task);
    }

    public TaskAdapter(List<Task> taskList, OnDeleteClickListener deleteClickListener) {
        this.taskList = taskList;
        this.deleteClickListener = deleteClickListener;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView dueDateText;
        public CheckBox checkBox;
        public ImageButton deleteButton;

        public TaskViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.task_title);
            dueDateText = view.findViewById(R.id.task_due_date); // add this TextView to your layout
            checkBox = view.findViewById(R.id.task_checkbox);
            deleteButton = view.findViewById(R.id.delete_button);
        }
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.title.setText(task.getTitle());

        // Display due date in yyyy-MM-dd format
        holder.dueDateText.setText(task.getDueDate().toString());

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(task.isCompleted());

        holder.title.setPaintFlags(task.isCompleted()
                ? holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
                : holder.title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setCompleted(isChecked);
            notifyItemChanged(holder.getBindingAdapterPosition());
        });

        holder.deleteButton.setOnClickListener(v -> deleteClickListener.onDeleteClick(task));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void addTask(Task task) {
        // Insert in the correct position based on dueDate
        int insertIndex = 0;
        for (int i = 0; i < taskList.size(); i++) {
            if (task.getDueDate().isBefore(taskList.get(i).getDueDate())) {
                break;
            }
            insertIndex++;
        }
        taskList.add(insertIndex, task);
        notifyItemInserted(insertIndex);
    }

    public void removeTask(Task task) {
        int position = taskList.indexOf(task);
        if (position != -1) {
            taskList.remove(position);
            notifyItemRemoved(position);
        }
    }
}
