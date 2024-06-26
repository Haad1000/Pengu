//package com.example.pengu;
//
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.util.Log;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonArrayRequest;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class S_ViewMyRatingsActivity extends AppCompatActivity {
//    private RecyclerView ratingView;
//    private VolleySingleton volleySingleton;
//    private RequestQueue requestQueue;
//    private rateAdpter rateAdapter;
//    private List<JSONObject> reportList;
//    private String url = "http://coms-309-037.class.las.iastate.edu:8080/getAllStudentRatings/";
//    private String thisurl, authToken;
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_view_all_ratings_by_student);
//
//        ratingView = findViewById(R.id.ratingsRecyclerView);
//        ratingView.setLayoutManager(new LinearLayoutManager(this));
//
//        volleySingleton = VolleySingleton.getInstance(this);
//        requestQueue = volleySingleton.getRequestQueue();
//
//        reportList = new ArrayList<>();
//
//        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE);
//        authToken = sharedPreferences.getString("AuthToken", "");
//        Log.e("The user Token is: ", authToken);
//
//        thisurl = url + authToken;
//
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, thisurl, null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        for (int i = 0; i < response.length(); i++) {
//                            Log.e("what is in here", response.toString());
//
//                            try {
//                                JSONObject rateObject = response.getJSONObject(i);
//                                String rateId = rateObject.getString("id");
//                                reportList.add(rateObject);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        //Adapter stuff here
//                        rateAdapter = new rateAdpter(S_ViewMyRatingsActivity.this, reportList);
//                        ratingView.setAdapter(rateAdapter);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("nvm got error", error.getMessage());
//                        error.printStackTrace();
//                    }
//                });
//        volleySingleton.addToRequestQueue(jsonArrayRequest);
//    }
//}
