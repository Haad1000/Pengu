package com.example.pengu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class BottomSheetSubjectsAdapter extends RecyclerView.Adapter<BottomSheetSubjectsAdapter.ViewHolder> {
    private List<Subjects> subjectsList;
    private Context context;
    private OnSubjectsUpdatedListener updateListener;
    public BottomSheetSubjectsAdapter(List<Subjects> subjectsList, Context context, OnSubjectsUpdatedListener listener) {
        this.subjectsList = subjectsList;
        this.context = context;
        this.updateListener = listener; // This line is crucial
    }


    public BottomSheetSubjectsAdapter(List<Subjects> subjectsList, Context context) {
        this.subjectsList = subjectsList;
        this.context = context; // Set the context
    }
    public void fetchSubjects(Context context) {
        String url = "http://coms-309-037.class.las.iastate.edu:8080/printSubject";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        subjectsList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject subjectObject = response.getJSONObject(i);
                                Subjects subject = new Subjects(
                                        subjectObject.getInt("subjectID"),
                                        subjectObject.getString("subjectName")
                                );
                                subjectsList.add(subject);
                            } catch (JSONException e) {
                                Log.e("JSON Parsing Error", "Error: " + e.getMessage());
                            }
                        }
                        notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Error fetching subjects: " + error.getMessage());
                    }
                }
        );
        VolleySingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_subject_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Subjects subject = subjectsList.get(position);
        holder.bind(subject);
    }

    @Override
    public int getItemCount() {
        return subjectsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView subjectTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            subjectTextView = itemView.findViewById(R.id.subjectNameTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Subjects subject = subjectsList.get(position);
                        int subjectId = subject.getSubjectID();


                        // Retrieve the teacherId from SharedPreferences using itemView.getContext()
                        SharedPreferences sharedPreferences = itemView.getContext().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
                        String teacherIdString = sharedPreferences.getString("userId", "");
                        int teacherId = 4; // Default or fallback teacher ID
                        try {
                            teacherId = Integer.parseInt(teacherIdString); // Parse the ID if possible
                        } catch (NumberFormatException e) {
                            Toast.makeText(itemView.getContext(), "Invalid Teacher ID. Please log in again.", Toast.LENGTH_LONG).show();
                            return; // Exit if the teacherId is not valid
                        }

                        // Now use the subjectId and teacherId to make your PUT request
                        updateTeacherSubjects(subjectId, teacherId, itemView.getContext(), () -> {
                            // Callback to refresh the list after PUT completes
                            fetchSubjects(itemView.getContext());
                            //dismissBottomSheet();// Assume fetchSubjects() re-fetches the list and updates the adapter
                        });
                    }
                }


            });

        }


        public void bind(Subjects subject) {
            subjectTextView.setText(subject.getSubjectName());
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Subjects clickedSubject = subjectsList.get(position);

                    SharedPreferences sharedPreferences = itemView.getContext().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
                    String teacherIdString = sharedPreferences.getString("userId", "");
                    int teacherId = 4; // Default or fallback teacher ID
                    try {
                        teacherId = Integer.parseInt(teacherIdString); // Parse the ID if possible
                    } catch (NumberFormatException e) {
                        Toast.makeText(itemView.getContext(), "Invalid Teacher ID. Please log in again.", Toast.LENGTH_LONG).show();
                        return; // Exit if the teacherId is not valid
                    }
//
                    updateTeacherSubjects(clickedSubject.getSubjectID(), teacherId, context, () -> {

                        //fetchSubjects(context); // Refresh data after update
                    });
                }
            });
        }

    }


//private void updateTeacherSubjects(int subjectId, int teacherId, Context context) {
//    String url = "http://coms-309-037.class.las.iastate.edu:8080/subjects/" + teacherId + "/" + subjectId;
//
//    StringRequest stringRequest = new StringRequest(
//            Request.Method.PUT,
//            url,
//            response -> {
//                // If this part runs, the response is successfully retrieved as a string
//                // Log the response and show it in a toast
//                Log.d("PUT Response", response);
//                Toast.makeText(context, "Response: " + response, Toast.LENGTH_LONG).show();
//            },
//            error -> {
//                // Handle error
//                Log.e("Volley Error", "Error: " + error.toString());
//                Toast.makeText(context, "Error: " + error.toString(), Toast.LENGTH_LONG).show();
//            }
//    ) {
//        @Override
//        public Map<String, String> getHeaders() throws AuthFailureError {
//            HashMap<String, String> headers = new HashMap<>();
//            // Add your headers here
//            return headers;
//        }
//    };
//
//    VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
//}
    private void updateTeacherSubjects(int subjectId, int teacherId, Context context, Runnable onCompletion) {
        String url = "http://coms-309-037.class.las.iastate.edu:8080/subjects/" + teacherId + "/" + subjectId;

        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT,
                url,
                response -> {
                    Log.d("PUT Response", response);
                    Toast.makeText(context, "Subject updated successfully!", Toast.LENGTH_SHORT).show();
                    onCompletion.run(); // This will call fetchSubjects after the update
                    //fetchSubjects(context);
                    updateListener.onSubjectsUpdated();
                    navigateToTeacherDashboard(context);


                },
                error -> {
                    Log.e("Volley Error", "Error: " + error.toString());
                    Toast.makeText(context, "Error: " + error.toString(), Toast.LENGTH_LONG).show();
                    onCompletion.run(); // Optionally call onCompletion even if there's an error
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                // Add any required headers here
                return headers;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }
    private void navigateToTeacherDashboard(Context context) {
        if (context instanceof Activity) {
            Intent intent = new Intent(context, TeacherDashboard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            ((Activity) context).finish(); // Close the current activity
        }
    }
    public interface OnSubjectsUpdatedListener {////
        void onSubjectsUpdated();
    }
   



}





