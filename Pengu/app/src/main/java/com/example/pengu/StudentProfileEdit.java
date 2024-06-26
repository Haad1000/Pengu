package com.example.pengu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentProfileEdit extends AppCompatActivity {
    private String BASE_BIO_GET_URL = "http://coms-309-037.class.las.iastate.edu:8080/getBio/";
    private String BASE_BIO_PUT_URL = "http://coms-309-037.class.las.iastate.edu:8080/updateBio/";
    private String BASE_PROFILE_GET_URL = "http://coms-309-037.class.las.iastate.edu:8080/images/user/";
    private String BASE_PROFILE_PUT_URL = "http://coms-309-037.class.las.iastate.edu:8080/images/update/";
    private String Bio_Get_Url, Bio_Put_Url, Pfp_Get_Url, Pfp_Put_Url, User_Id, authToken;
    private EditText profileBio;
    private ImageButton backBtn;
    private Button updateButton;
    private BottomNavigationView bottomNavigationView;
    private CircleImageView pfp;

    private ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile_edit);

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE);
        authToken = sharedPreferences.getString("AuthToken", "");
        User_Id = sharedPreferences.getString("userId","");
        Log.e("The user Token is: ", authToken);
        Log.e("The user is is: ", User_Id);

        bottomNavigationView = findViewById(R.id.student_navbar);

        bottomNavigationView.setSelectedItemId(R.id.stuProfile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.stuDashboard) {
                    Intent intent = new Intent(StudentProfileEdit.this, ViewSubjectsActivity.class);
                    startActivity(intent);
                    return true;
                }
                else if (item.getItemId() == R.id.stuSchedule) {
                    Intent intent = new Intent(StudentProfileEdit.this, StudentDashboard.class);
                    startActivity(intent);
                    return true;
                }
                else if (item.getItemId() == R.id.stuInbox) {
                    Intent intent = new Intent(StudentProfileEdit.this, InboxActivity.class);
                    startActivity(intent);
                    return true;
                }
                else if (item.getItemId() == R.id.stuProfile) {
                    Intent intent = new Intent(StudentProfileEdit.this, StudentProfile.class);
                    startActivity(intent);
                    return true;
                }
                else {
                    return true;
                }
            }
        });

        profileBio = findViewById(R.id.editUserBio);
        pfp = findViewById(R.id.editStuPfp);
        backBtn = findViewById(R.id.goBackToStuProfile);
        updateButton = findViewById(R.id.updateProfileBtn);

        Bio_Get_Url = BASE_BIO_GET_URL + User_Id;
        Pfp_Get_Url = BASE_PROFILE_GET_URL + User_Id;
        Bio_Put_Url = BASE_BIO_PUT_URL + authToken;
        Pfp_Put_Url = BASE_PROFILE_PUT_URL + authToken;

        fetch_Bio(Bio_Get_Url);
        fetchProfilePic(Pfp_Get_Url);

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if (o.getResultCode() == RESULT_OK) {
                            Intent data = o.getData();
                            if (data != null) {
                                Uri imageUri = data.getData();
                                try {
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                                    pfp.setImageBitmap(bitmap);
                                }
                                catch (IOException e) {
                                    Log.e("IMG select problem",e.toString());
                                }
                            }
                        }
                    }
                }
        );

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentProfileEdit.this, StudentProfile.class);
                startActivity(intent);
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBio(Bio_Put_Url);
                updatePfp(Pfp_Put_Url);
            }
        });

        pfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryLauncher.launch(intent);
            }
        });
    }

    private void fetch_Bio(String url) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Bio Response",response.toString());
                    profileBio.setText(response);
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

    private void updateBio(String url) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("bio", profileBio.getText());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("On Bio Responce", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("On Bio Error", error.toString());
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

//    private void updatePfp(String url) {
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//    }

    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream); // Compress to JPEG with 100% quality
        return stream.toByteArray();
    }


    private void updatePfp(String url) {
        // Get the image from the CircleImageView
        Bitmap bitmap = ((BitmapDrawable) pfp.getDrawable()).getBitmap();

        // Convert the bitmap to byte array
        byte[] imageData = convertBitmapToByteArray(bitmap);

        // Create the MultipartRequest to upload the image
        MultipartRequest multipartRequest = new MultipartRequest(
                Request.Method.PUT,
                url,
                imageData, // The byte array representing the image
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle success
                        Log.d("Image Upload Success", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Log.e("Image Upload Error", error.toString());
                    }
                }
        );

        // Add the request to the request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(multipartRequest);
    }


}