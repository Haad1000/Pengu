package com.example.pengu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

    private List<Subject> subjectList;
    private OnItemClickListener listener;

    public SubjectAdapter(List<Subject> subjectList, OnItemClickListener listener) {
        this.subjectList = subjectList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subject, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        Subject subject = subjectList.get(position);
        holder.subjectName.setText(subject.getSubjectName());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(subject));
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    static class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView subjectName;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.userName);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Subject subject);
    }
}
