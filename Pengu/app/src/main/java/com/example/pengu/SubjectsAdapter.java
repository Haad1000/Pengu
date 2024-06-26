package com.example.pengu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.util.Log;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Text;
import android.util.Log;

// If you're using Volley for network requests
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;


import androidx.recyclerview.widget.LinearLayoutManager;
import com.android.volley.Response;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class SubjectsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_ADD = 1;
    private List<Subjects> subjectsList;
    private actualTeacherProfile activity;

    public SubjectsAdapter(List<Subjects> subjectsList, actualTeacherProfile activity) {
        this.subjectsList = subjectsList;
        this.activity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == subjectsList.size()) ? VIEW_TYPE_ADD : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ADD) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_add_item, parent, false);
            return new AddViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_item, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            Subjects subject = subjectsList.get(position);
            ((ViewHolder) holder).bind(subject);
        }

        // No binding needed for AddViewHolder
    }

    @Override
    public int getItemCount() {
        return subjectsList.size() + 1; // +1 for the add button
    }
//////////////////////////////////////////////////////
public class AddViewHolder extends RecyclerView.ViewHolder {
    public AddViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(view -> showBottomSheetDialog(view.getContext()));
    }

    private void showBottomSheetDialog(Context context) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_subjects, null);
        RecyclerView subjectsListRecyclerView = bottomSheetView.findViewById(R.id.subjectsListRecyclerView);
        subjectsListRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        BottomSheetSubjectsAdapter bottomSheetAdapter = new BottomSheetSubjectsAdapter(subjectsList, context);
        subjectsListRecyclerView.setAdapter(bottomSheetAdapter);
        bottomSheetAdapter.fetchSubjects(context);

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }
}


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView subjectTextView;
        private final ImageView deleteImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            subjectTextView = itemView.findViewById(R.id.subjectNameTextView);
            deleteImageView = itemView.findViewById(R.id.deleteButton);
            deleteImageView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    int subjectId = subjectsList.get(position).getSubjectID();
                    activity.deleteSubject(subjectId, position);
                }
            });
        }

        public void bind(Subjects subject) {
            subjectTextView.setText(subject.getSubjectName());
        }
    }


}
