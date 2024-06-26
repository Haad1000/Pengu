package com.example.pengu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.widget.ImageView;
import java.util.ArrayList;
import android.util.Log;

import android.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;

public class AdminSubjectAdapter extends RecyclerView.Adapter<AdminSubjectAdapter.ViewHolder> {

    private List<AdminSubject> subjectList;
    private AdminSubjectsActivity activity;

    public AdminSubjectAdapter(List<AdminSubject> subjectList, AdminSubjectsActivity activity) {
        this.subjectList = subjectList;
        this.activity = activity;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_subject_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AdminSubject subject = subjectList.get(position);
        holder.subjectNameTextView.setText(subject.getName()); // Use the subject instance

        holder.editSubjectImageView.setOnClickListener(view -> {
            AdminSubject subjectToEdit = subjectList.get(holder.getAdapterPosition());
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
            builder.setTitle("Edit Subject");

            final EditText input = new EditText(holder.itemView.getContext());
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setText(subjectToEdit.getName());
            builder.setView(input);

            builder.setPositiveButton("OK", (dialog, which) -> {
                String newName = input.getText().toString();
                subjectToEdit.setName(newName); // Set the new name on your subject object
                activity.updateSubjectOnServer(subjectToEdit, holder.getAdapterPosition());
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder.show();
        });

        holder.deleteSubjectImageView.setOnClickListener(view -> {
            int subjectId = subjectList.get(holder.getAdapterPosition()).getID();
            activity.deleteSubjectFromServer(subjectId, holder.getAdapterPosition());
        });
    }
    @Override
    public int getItemCount() {
        return subjectList.size();
    }
    public void addSubject(AdminSubject AdminSubject) {
        // Add the subject to your data set
        subjectList.add(AdminSubject);
        // Notify the adapter that an item was inserted at the last position
        notifyItemInserted(subjectList.size() - 1);
    }


    public void deleteSubject(int position) {
        if (position >= 0 && position < subjectList.size()) {
            subjectList.remove(position);
            notifyItemRemoved(position);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView subjectNameTextView;
        ImageView editSubjectImageView, deleteSubjectImageView;

        ViewHolder(View itemView) {
            super(itemView);
            subjectNameTextView = itemView.findViewById(R.id.subjectNameTextView);
            editSubjectImageView = itemView.findViewById(R.id.editSubjectImageView);
            deleteSubjectImageView = itemView.findViewById(R.id.deleteSubjectImageView);
        }
    }
    public void onDeleteIconClicked(AdminSubject subject, int position) {
        if (activity != null) {
            activity.deleteSubjectFromServer(subject.getID(), position);
        }
    }
}
