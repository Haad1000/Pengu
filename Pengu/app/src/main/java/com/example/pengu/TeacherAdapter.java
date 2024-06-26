package com.example.pengu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.TeacherViewHolder> {

    private List<Teacher> teacherList;
    private OnItemClickListener listener;

    public TeacherAdapter(List<Teacher> teacherList, OnItemClickListener listener) {
        this.teacherList = teacherList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new TeacherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherViewHolder holder, int position) {
        Teacher teacher = teacherList.get(position);
        holder.nameTextView.setText(teacher.getName());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(teacher));
    }

    @Override
    public int getItemCount() {
        return teacherList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Teacher teacher);
    }

    static class TeacherViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;

        public TeacherViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.userName);
        }
    }
}
