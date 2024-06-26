package com.example.pengu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class StudentSignIn extends AppCompatActivity implements View.OnClickListener {
    private EditText editPassword, editPasswordSure, editName,editEmail,editRole;
    private Button postDataBtn;
    private static final String URL_STRING_REQ = "http://coms-309-037.class.las.iastate.edu:8080/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_sign_in);
        editName = findViewById(R.id.TextName);
        //editRole = findViewById(R.id.TextRole);
        editEmail = findViewById(R.id.TextMail);
        editPassword = findViewById(R.id.TextPassword);
        // editPasswordSure = findViewById(R.id.TextPasswordSure);
        //OpenNewPage= findViewById(R.id.textViewOpenNewPage);
        postDataBtn = findViewById(R.id.button);



        //editRole.setText("STUDENT");
        //textViewOpenNewPage.setOnClickListener(this);
        postDataBtn.setOnClickListener(this);

        TextView OpenLogIN = findViewById(R.id.textViewOpenLoginPage);
        OpenLogIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentSignIn.this, StudentLogIn.class);
                startActivity(intent);
            }
        });
        postDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postRequest();
            }
        });

    }

    @Override
    public void onClick(View v) {

    }
    private void postRequest() {
        String name = editName.getText().toString().trim();
        //String role = editRole.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        // String passwordSure = editPasswordSure.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty()|| password.isEmpty()) {
            Toast.makeText(StudentSignIn.this, "Please enter all the information", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject postBody = new JSONObject();
        try {
            postBody.put("name", name);
            postBody.put("role"," STUDENT");
            postBody.put("emailId", email);//
            postBody.put("password", password);
            postBody.put("isActive", 0);
            //postBody.put("passwordSure", passwordSure);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST,
                URL_STRING_REQ,
                postBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", "Response: " + response.toString());
                        // Handle the response
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Error: " + error.toString());
                        if (error.networkResponse != null) {
                            String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                            Log.e("Network Response Body", responseBody);
                        }
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };


        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
