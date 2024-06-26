package com.example.pengu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherProfile extends AppCompatActivity {
    private String BASE_URL = "ws://coms-309-037.class.las.iastate.edu:8080/review/";
    private String BASE_BIO_URL = "http://coms-309-037.class.las.iastate.edu:8080/getBio/";
    private String BASE_PROFILE_URL = "http://coms-309-037.class.las.iastate.edu:8080/images/user/";
    private String BioUrl, PfpUrl;
    private Button continueButton, bookBtn;
    private ImageButton optBtn, backBtn;
    private TextView bioBox, nameBox;
    private int teachID, subjID;
    private EditText usernameEtx, profnameEtx;
    private CircleImageView pfp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile);

        //need a reportTeacherActivity.class and getTeacherRating.class and

        optBtn = findViewById(R.id.optionButton);
        backBtn = findViewById(R.id.backButton);
        bookBtn = findViewById(R.id.bookButton);
        pfp = findViewById(R.id.teachpfp);
        bioBox = findViewById(R.id.aboutMeBox);
        nameBox = findViewById(R.id.name);

        teachID = getIntent().getIntExtra("teacherID", -1);
        subjID = getIntent().getIntExtra("subjectID", -1);

        BioUrl = BASE_BIO_URL + teachID;
        PfpUrl = BASE_PROFILE_URL + teachID;

        fetch_Bio(BioUrl);
        fetchProfilePic(PfpUrl);

        optBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherProfile.this, reportTeacherActivity.class);
                intent.putExtra("teacherID", teachID);
                startActivity(intent);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherProfile.this, StudentDashboard.class);
                startActivity(intent);
            }
        });

        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherProfile.this, reportTeacherActivity.class);
                intent.putExtra("teacherID", teachID);
                intent.putExtra("subjectID", subjID);
                startActivity(intent);
            }
        });




//        continueButton = findViewById(R.id.continueButton);
//
//        continueButton.setOnClickListener(view -> {
//            try {
//                String userName = usernameEtx.getText().toString() + "@email.com";
//                String teacherName = profnameEtx.getText().toString() + "@email.com";
//                String serverUrl = BASE_URL + userName + "/" + teacherName;
//
//                // Log the serverUrl for debugging purposes
//                Log.d("StudentReview", "Server URL: " + serverUrl);
//
//                WebSocketManager.getInstance().connectWebSocket(serverUrl);
//                //WebSocketManager.getInstance().setWebSocketListener(TeacherProfile.this);
//
//                // If this line is reached, the button is being clicked
//                Log.d("StudentReview", "Connect button clicked");
//            } catch (Exception e) {
//                Log.e("StudentReview", "Error in connectBtn click listener", e);
//            }
//            Intent intent = new Intent(this, StudentReview.class);
//            startActivity(intent);
//        });
    }

    private void fetch_Bio(String url) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Bio Response",response.toString());
                    bioBox.setText(response);
                }
                catch(Exception e) {
                    Log.e("Bio here", "Error parsing user info: " + e.getMessage());
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Another Bio Here", "Error fetching user info: " + error.getMessage());
                    }
                }
        );

        requestQueue.add(stringRequest);
    }

    private void fetchProfilePic(String url) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //
        ImageRequest imageRequest = new ImageRequest(
                url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        pfp.setImageBitmap(response);
                    }
                },
                0, // Width, set to 0 to get the original width
                0, // Height, set to 0 to get the original height
                ImageView.ScaleType.FIT_XY, // ScaleType
                Bitmap.Config.RGB_565, // Bitmap config

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors here
                        Log.e("Volley Error", error.toString());
                    }
                }
        );
        requestQueue.add(imageRequest);
    }
}