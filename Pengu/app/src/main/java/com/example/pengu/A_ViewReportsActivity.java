package com.example.pengu;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class A_ViewReportsActivity extends AppCompatActivity {

    private RecyclerView ongoingReportsView, doneReportsView;
    private ReportAdptr ongoingAdptr;
    private ReportAdpterAnother doneAdptr;

    private List<JSONObject> ongoingReportList, doneReportList;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private String url = "http://coms-309-037.class.las.iastate.edu:8080/getAllReports";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_reports);


        ongoingReportsView = findViewById(R.id.ongoingReportsRecyclerView);
        doneReportsView = findViewById(R.id.doneReportsRecyclerView);

        ongoingReportsView.setLayoutManager(new LinearLayoutManager(this));
        doneReportsView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize VolleySingleton
        volleySingleton = VolleySingleton.getInstance(this);
        requestQueue = volleySingleton.getRequestQueue();

        // Initialize the list to hold the teacher users
        ongoingReportList = new ArrayList<>();
        doneReportList = new ArrayList<>();



        // Make Volley GET request to fetch users from the backend API
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Parse the JSON response
                        for (int i = 0; i < response.length(); i++) {
                            Log.e("what is in here", response.toString());
                            try {
                                JSONObject reportObject = response.getJSONObject(i);
                                String status = reportObject.getString("status");
                                if ("ONGOING".equals(status)) {
                                    ongoingReportList.add(reportObject);
                                }
                                else if ("DONE".equals(status)) {
                                    doneReportList.add(reportObject);

                                }else if ("DONE".equals(status)) {
                                    doneReportList.add(reportObject);
                                }
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }

                            ongoingAdptr = new ReportAdptr(A_ViewReportsActivity.this, ongoingReportList);
                            ongoingReportsView.setAdapter(ongoingAdptr);

                            doneAdptr = new ReportAdpterAnother(A_ViewReportsActivity.this, doneReportList);
                            doneReportsView.setAdapter(doneAdptr);


                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("nvm got error", error.toString());
                        error.printStackTrace();
                    }
                });

        // Add the request to the RequestQueue
        volleySingleton.addToRequestQueue(jsonArrayRequest);
    }
}
