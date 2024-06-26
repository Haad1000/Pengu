package com.example.pengu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.List;
import java.util.ArrayList;
import android.widget.Toast;
import com.example.pengu.Review;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import org.json.JSONObject;

import org.json.JSONException;
import org.java_websocket.handshake.ServerHandshake;
import org.w3c.dom.Text;
import android.util.Log;

public class StudentReview extends AppCompatActivity implements WebSocketListener {
//    private String BASE_URL = "ws://10.0.2.2:8080/chat/";
    // private String BASE_URL = "ws://coms-309-037.class.las.iastate.edu:8080/review/";

    private Button connectBtn, sendBtn,backMainBtn;
    private EditText usernameEtx, msgEtx, profnameEtx;//msgEtx is where you write your message
    // usernameEtx should be read on its own with the login
    private TextView msgTv;//this is where the chat appear
    private RecyclerView reviewsRecyclerView;
    private ReviewsAdapter reviewsAdapter;
    private List<Review> reviewList = new ArrayList<>();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_review);

        // connectBtn = (Button) findViewById(R.id.button3);
        sendBtn = (Button) findViewById(R.id.submitReviewButton);
        //usernameEtx = (EditText) findViewById(R.id.et1);
        // profnameEtx = (EditText) findViewById(R.id.et2);
        msgEtx = (EditText) findViewById(R.id.reviewEditText);
        // msgTv = (TextView) findViewById(R.id.tx1);

        // Initialize RecyclerView
        reviewsRecyclerView = findViewById(R.id.reviewsRecyclerView);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewsAdapter = new ReviewsAdapter(reviewList);
        reviewsRecyclerView.setAdapter(reviewsAdapter);

        /* connect button listener */
//        connectBtn.setOnClickListener(view -> {
//            try {
//                String userName = usernameEtx.getText().toString() + "@email.com";
//                String teacherName = profnameEtx.getText().toString() + "@email.com";
//                String serverUrl = BASE_URL + userName + "/" + teacherName;
//
//                // Log the serverUrl for debugging purposes
//                Log.d("StudentReview", "Server URL: " + serverUrl);
//
//                WebSocketManager.getInstance().connectWebSocket(serverUrl);
//                WebSocketManager.getInstance().setWebSocketListener(StudentReview.this);
//
//                // If this line is reached, the button is being clicked
//                Log.d("StudentReview", "Connect button clicked");
//            } catch (Exception e) {
//                Log.e("StudentReview", "Error in connectBtn click listener", e);
//            }
//        });
        WebSocketManager.getInstance().setWebSocketListener(StudentReview.this);

//        // Set the WebSocketListener to this activity
//        WebSocketManager.getInstance().setWebSocketListener(this);
//
//        // Automatically connect to the WebSocket server when the activity starts
//        WebSocketManager.getInstance().connectWebSocket("ws://10.0.2.2:8080/chat/");
        sendBtn.setOnClickListener(v -> {
            String messageToSend = msgEtx.getText().toString();
            if (!messageToSend.isEmpty()) {
                WebSocketManager.getInstance().sendMessage(messageToSend);
                msgEtx.setText("");
            }
        });

        /* back button listener */
//        backMainBtn.setOnClickListener(view -> {
//            // got to chat activity
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//        });

//        sendBtn.setOnClickListener(v -> {
//            String message = msgEtx.getText().toString();
//            if (!message.isEmpty()) {
//                WebSocketManager.getInstance().sendMessage(message);
//                msgEtx.setText(""); // Clear the input field after sending the message
//            } else {
//                Toast.makeText(this, "Please enter a review before sending.", Toast.LENGTH_SHORT).show();
//            }
//        });
        reviewList.add(new Review("User1", "Test review 1"));
        reviewList.add(new Review("User2", "Test review 2"));
        reviewList.add(new Review("User3", "Test review 3"));
        reviewsAdapter.notifyDataSetChanged();



    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        Log.d("StudentReview", "WebSocket connected");
        runOnUiThread(() -> {
            Toast.makeText(this, "Connected to WebSocket server", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onWebSocketMessage(String message) {
        // Assuming " has Joined the Chat" is the part of the message you want to filter out
        if (!message.contains("has Joined the Chat")) {
            // Split the incoming message to extract the username and message
            String[] parts = message.split(":", 2);
            if (parts.length == 2) {
                String username = parts[0].trim();
                String reviewText = parts[1].trim();

                // Create a new Review object and add it to the list
                Review newReview = new Review(username, reviewText);

                runOnUiThread(() -> {
                    reviewList.add(newReview);
                    reviewsAdapter.notifyDataSetChanged();
                });
            }
        } else {
            // Log or handle user join messages differently if needed
            Log.d("WebSocket", "User join message received and not displayed: " + message);
        }
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        Log.d("StudentReview", "WebSocket closed");
        // Handle the closing of the WebSocket connection
    }

    @Override
    public void onWebSocketError(Exception ex) {
        Log.d("StudentReview", "WebSocket error: " + ex.getMessage());
        // Handle any WebSocket errors
    }

    // Don't forget to disconnect from the WebSocket when the activity is destroyed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        WebSocketManager.getInstance().disconnectWebSocket();
    }
}