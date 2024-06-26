package com.example.pengu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import android.widget.Button;
import org.json.JSONArray;

import android.util.Log;

import com.android.volley.toolbox.JsonObjectRequest;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.android.volley.toolbox.StringRequest;
import android.content.SharedPreferences;


import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminBookingActivity extends AppCompatActivity {
    private Map<Integer, String> subjectIdToNameMap = new HashMap<>();
    private RecyclerView subjectsRecyclerView;
    private BookingAdapter adapter;
    private List<BookingInfo> bookingList = new ArrayList<>(); // Initialized as an empty list
    private Button addSubjectButton;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_booking);

        bottomNavigationView = findViewById(R.id.admin_navbar);

        bottomNavigationView.setSelectedItemId(R.id.admBooking);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.admSubjects) {
                    Intent intent = new Intent(AdminBookingActivity.this, AdminSubjectsActivity.class);
                    startActivity(intent);
                    return true;
                }
                else if (item.getItemId() == R.id.admBooking) {
                    //Currently here right now
                    return true;
                }
                else if (item.getItemId() == R.id.admReport) {
                    Intent intent = new Intent(AdminBookingActivity.this, AdminDashBoard.class);
                    startActivity(intent);
                    return true;
                }
                else if (item.getItemId() == R.id.admUsers) {
                    Intent intent = new Intent(AdminBookingActivity.this, AdminAllUsers.class);
                    startActivity(intent);
                    return true;
                }
                else {
                    return true;
                }
            }
        });

        subjectsRecyclerView = findViewById(R.id.subjectsRecyclerView);
        // LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2); // Use a span count of 2 for two columns
        subjectsRecyclerView.setLayoutManager(layoutManager);
        addSubjectButton = findViewById(R.id.addSubjectButton);

        addSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateBookingDialog();

            }
        });

        adapter = new BookingAdapter(this, bookingList, subjectIdToNameMap, new BookingAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                // Handle the delete action
                int bookingId = bookingList.get(position).getId();
                deleteBooking(bookingId);
            }

            @Override
            public void onEditClick(int position) {
                showEditBookingDialog(bookingList.get(position), position); // Implement this method
            }

            @Override
            public void onClick() {
                // Implement this method if needed
            }
        });

        subjectsRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        fetchSubjectNames();

        fetchBookingInfo(); // Fetch the booking data directly
    }

    private void fetchBookingInfo() {
        String url = "http://coms-309-037.class.las.iastate.edu:8080/printBookingInfo";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    bookingList.clear(); // Clear the list before adding new data
                    parseBookingJson(response);
                    adapter.notifyDataSetChanged(); // Notify the adapter to refresh the view
                },
                error -> {
                    // Handle error
                    error.printStackTrace();
                }
        );

        VolleySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    private void parseBookingJson(JSONArray response) {
        try {

            for (int i = 0; i < response.length(); i++) {
                JSONObject bookingJson = response.getJSONObject(i);
                int id = bookingJson.getInt("id");

                // Check if the "subject" field is null
                int subjectID;
                if (!bookingJson.isNull("subject")) {
                    subjectID = bookingJson.getJSONObject("subject").getInt("subjectID");
                } else {
                    subjectID = -1; // or any other suitable value for null subject
                }
                Log.d("parseBookingJson", "This is parseBookinhJson");

                int teacherID = bookingJson.getJSONObject("teacher").getInt("id");
                int studentID = bookingJson.getJSONObject("student").getInt("id");
                String teacherName = bookingJson.getJSONObject("teacher").getString("name");
                String studentName = bookingJson.getJSONObject("student").getString("name");
                String date = bookingJson.getString("date");
                String startTime = bookingJson.getString("startTime");
                String endTime = bookingJson.getString("endTime");

                bookingList.add(new BookingInfo(id, subjectID, teacherID, studentID, teacherName, studentName, date, startTime, endTime));
            }
            adapter.notifyDataSetChanged();
            Log.d("AdminBookingActivity", "Number of bookings: " + bookingList.size());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void fetchSubjectNames() {
        String url = "http://coms-309-037.class.las.iastate.edu:8080/printSubject";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject subjectObject = response.getJSONObject(i);
                            int id = subjectObject.getInt("subjectID");
                            String name = subjectObject.getString("subjectName");
                            subjectIdToNameMap.put(id, name);
                        }
                        // Log the contents of subjectIdToNameMap to check it
                        for (Map.Entry<Integer, String> entry : subjectIdToNameMap.entrySet()) {
                            Log.d("SubjectMap", "ID: " + entry.getKey() + " Name: " + entry.getValue());
                        }

                        // Continue with the rest of your logic, such as fetching bookings
                        //fetchBookingInfo();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // Handle error
                    }
                },
                error -> {
                    // Handle error
                    error.printStackTrace();
                });

        VolleySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    public void deleteBooking(int bookingId) {
        // The bookingId is appended to the URL path
        String url = "http://coms-309-037.class.las.iastate.edu:8080/deleteBooking/" + bookingId;

        // Create a StringRequest for the DELETE request without a request body
        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, url,
                response -> {
                    // Handle the successful deletion
                    Log.d("DeleteBooking", "Booking deleted successfully.");
                    // Update your local data and UI accordingly
                    removeFromBookingListById(bookingId);
                },
                error -> {
                    // Handle any errors
                    Log.e("DeleteBooking", "Error deleting booking: " + error.toString());
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Bearer YOUR_AUTH_TOKEN_HERE"); // Replace with actual token if needed
                return headers;
            }
        };

        // Add the request to the Volley request queue
        VolleySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(deleteRequest);
    }

    private void removeFromBookingListById(int bookingId) {
        // Loop through the bookingList and remove the booking with the given id
        Iterator<BookingInfo> iterator = bookingList.iterator();
        while (iterator.hasNext()) {
            BookingInfo booking = iterator.next();
            if (booking.getId() == bookingId) {
                // Remove the current item from the iterator and the list
                iterator.remove();
                // Notify the adapter of the item removed
                adapter.notifyDataSetChanged();
                return;
            }
        }
    }

    public void showEditBookingDialog(BookingInfo booking, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_booking, null);
        builder.setView(dialogView);

        // Find the EditText views in your dialog layout
        EditText editDate = dialogView.findViewById(R.id.editDate);
        EditText editStartTime = dialogView.findViewById(R.id.editStartTime);
        EditText editEndTime = dialogView.findViewById(R.id.editEndTime);
        // Initialize EditText with current booking info
        editDate.setText(booking.getDate());
        editStartTime.setText(booking.getStartTime());
        editEndTime.setText(booking.getEndTime());

        // ... setup other EditText views for different attributes as needed

        // When "Save" is clicked...
        builder.setPositiveButton("Save", (dialog, which) -> {
            // Collect updated values from EditText views
            String newDate = editDate.getText().toString();
            String newStartTime = editStartTime.getText().toString();
            String newEndTime = editEndTime.getText().toString();

            // ... collect other updated values

            // Update the booking object with new values
            booking.setDate(newDate);
            booking.setStartTime(newStartTime);
            booking.setEndTime(newEndTime);

            // ... update other attributes of the booking object

            // Now, pass the updated booking object to updateBookingOnServer
            updateBookingOnServer(booking, position);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void updateBookingOnServer(BookingInfo updatedBooking, int position) {
        Log.d("updateBookingServer", "this is updateBookingServer");
        String url = "http://coms-309-037.class.las.iastate.edu:8080/updateBookingInfo";
        JSONObject updateParams = new JSONObject();
        try {
            updateParams.put("id", updatedBooking.getId());
            updateParams.put("subject", new JSONObject().put("subjectID", updatedBooking.getSubjectID()));
            updateParams.put("teacher", new JSONObject().put("id", updatedBooking.getTeacherID()));
            updateParams.put("student", new JSONObject().put("id", updatedBooking.getStudentID()));
            updateParams.put("date", updatedBooking.getDate());
            updateParams.put("startTime", updatedBooking.getStartTime());
            updateParams.put("endTime", updatedBooking.getEndTime());

            // Add other fields as necessary
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, updateParams,
                response -> {
                    // Handle successful update
                    bookingList.set(position, updatedBooking); // Update the list
                    adapter.notifyItemChanged(position); // Notify the adapter of the change
                },
                error -> {
                    // Handle error
                    Log.e("UpdateBooking", "Error: " + error.toString());
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                // Add other headers if necessary, such as Authorization
                return headers;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    public void showCreateBookingDialog() {
        Log.d("AdminBookingActivity", "showCreateBookingDialog called.");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_create_booking, null);
        builder.setView(dialogView);

       // EditText editSubjectID = dialogView.findViewById(R.id.editSubjectID);
        EditText editSubjectName = dialogView.findViewById(R.id.editSubjectName);
        EditText editTeacherID = dialogView.findViewById(R.id.editTeacherID);
        EditText editStudentID = dialogView.findViewById(R.id.editStudentID);
        EditText editDate = dialogView.findViewById(R.id.editDate);
        EditText editStartTime = dialogView.findViewById(R.id.editStartTime);
        EditText editEndTime = dialogView.findViewById(R.id.editEndTime);


        // ... other EditTexts for the booking information


        builder.setPositiveButton("Create", (dialog, which) -> {


            try {
                Log.d("showCreateBookingDialog", "Start creating booking");
                String subjectName = editSubjectName.getText().toString();
                Integer subjectId = null;
                // Look for the subject ID based on the name entered by the user
                for (Map.Entry<Integer, String> entry : subjectIdToNameMap.entrySet()) {
                    if (entry.getValue().equalsIgnoreCase(subjectName)) {
                        subjectId = entry.getKey();
                        break;
                    }
                }

                if (subjectId == null) {
                    Log.e("showCreateBookingDialog", "Subject ID not found for name: " + subjectName);
                    Toast.makeText(this, "Subject name is invalid", Toast.LENGTH_LONG).show();
                    return; // Exit the method if subject ID wasn't found
                }
                Log.d("showCreateBookingDialog", "Subject ID for " + subjectName + " is " + subjectId);
               // int subjectId = Integer.parseInt(editSubjectID.getText().toString());
                int teacherId = Integer.parseInt(editTeacherID.getText().toString());
                int studentId = Integer.parseInt(editStudentID.getText().toString());


                JSONObject bookingDetails = new JSONObject();

                //bookingDetails.put("subject", new JSONObject().put("subjectID", subjectId));
                bookingDetails.put("teacher", new JSONObject().put("id", teacherId));
                bookingDetails.put("student", new JSONObject().put("id", studentId));
                bookingDetails.put("date", editDate.getText().toString());

                bookingDetails.put("startTime", editStartTime.getText().toString());
                bookingDetails.put("endTime", editEndTime.getText().toString());
                bookingDetails.put("subject", new JSONObject().put("subjectID", subjectId));
                //bookingDetails.put("endTime","2024/03/28");

                Log.d("showCreateBookingDialog", "this is the end create booking should be called");


                createBooking(bookingDetails);

            } catch (JSONException e) {
                Log.e("showCreateBookingDialog", "JSONException: " + e.getMessage());
            } catch (NumberFormatException e) {
                Log.e("showCreateBookingDialog", "NumberFormatException: " + e.getMessage());
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

public void createBooking(JSONObject bookingDetails) {
    Log.d("CreateBooking", "Attempting to create a booking...");

    SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE);
    String token = sharedPreferences.getString("AuthToken", "");
    if (token.isEmpty()) {
        Log.e("CreateBooking", "Auth token is missing.");
        return;
    }

    String url = "http://coms-309-037.class.las.iastate.edu:8080/createBooking/" + token;

    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.POST,
            url,
            bookingDetails,
            response -> {
                try {
                    String message = response.getString("message");
                    if ("success".equalsIgnoreCase(message)) {
                        Log.d("CreateBooking", "Booking created successfully.");
                        // Refresh the page here
                        fetchBookingInfo(); // Re-fetch the booking info to update the list
                    } else {
                        Log.e("CreateBooking", "Booking creation failed: " + message);
                    }
                } catch (JSONException e) {
                    Log.e("CreateBooking", "JSON parsing error: " + e.getMessage(), e);
                }
            },
            error -> {
                if (error.networkResponse != null) {
                    String statusCode = String.valueOf(error.networkResponse.statusCode);
                    String responseData = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    Log.e("CreateBooking", "Error: StatusCode: " + statusCode + ", Data: " + responseData);
                } else {
                    Log.e("CreateBooking", "Error creating booking: " + error.getMessage(), error);
                }
            }) {
        @Override
        public Map<String, String> getHeaders() {
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json; charset=utf-8");
            headers.put("Authorization", "Bearer " + token);
            return headers;
        }
    };

    VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
}

}
