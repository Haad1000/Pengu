package com.example.pengu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONObject;
import org.w3c.dom.Text;
import org.json.JSONException;
import java.util.HashMap;
import java.util.Map;
import java.util.HashMap;
import java.util.Map;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;

public class StudentLogIn extends AppCompatActivity implements View.OnClickListener {
    private EditText passwordEdt, mailEdt;
    private Button postDataBtn, getButton;
    private TextView OpenNewPage;
    private TextView responseTv,msgRes;
    // private static final String URL_STRING_REQ = "https://96c6c1cf-3d2c-4913-9105-dbe0363fac99.mock.pstmn.io/users";
    private static final String URL_STRING_REQ = "http://coms-309-037.class.las.iastate.edu:8080/login";
    //Mock Login.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_log_in);
        //initialize variables
        mailEdt = findViewById(R.id.TextMail);
        passwordEdt = findViewById(R.id.TextPassword);
        postDataBtn = findViewById(R.id.button);
        msgRes = findViewById(R.id.msgResponse);
        //OpenNewPage= findViewById(R.id.textViewOpenNewPage);


        //textViewOpenNewPage.setOnClickListener(this);
        postDataBtn.setOnClickListener(this);
        //getButton.setOnClickListener(this);


        // adding on click listener to our button.

        TextView textViewOpenNewPage = findViewById(R.id.textViewOpenNewPage);
        textViewOpenNewPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentLogIn.this, StudentSignIn.class);
                startActivity(intent);
            }
        });

        postDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postRequest();
            }
        });

/*
            @Override
            public void onClick(View v) {
                // validating if the text field is empty or not.
                if (mailEdt.getText().toString().isEmpty() && passwordEdt.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter both the values", Toast.LENGTH_SHORT).show();
                    return;// Toast is used for displaying short timed messages on the screen
                }
                // calling a method to post the data and passing our name and job.
                //postDataUsingVolley(mailEdt.getText().toString(), passwordEdt.getText().toString());
                makeString();
            }
*/
    }

    @Override
    public void onClick(View v) {

    }

    private void postRequest() {
        String email = mailEdt.getText().toString().trim();
        String password = passwordEdt.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(StudentLogIn.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject postBody = new JSONObject();
        try {
            postBody.put("emailId", email);
            postBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST,
                URL_STRING_REQ,
                postBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {Log.d("Volley Response", response.toString());
                        try {
                            // Check if the response contains the expected data
                            if (response.has("token") && response.has("role")) {
                                String role = response.getString("role");

                                String message = response.getString("token");
                                String userId = response.getString("userId");


                                SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("AuthToken", message);
                                editor.putString("userId", userId);
                                editor.apply();

                                // Show the message in the TextView or Toast
                                //msgRes.setText("Role: " + role + "\nMessage: " + message);
                                Log.d("Login Info", "Role: " + role + ", Token: " + message);


                                //  Use the role to determine what to do next
                                switch (role) {
                                    case "ADMIN":
                                        Intent adminIntent = new Intent(StudentLogIn.this, AdminDashBoard.class);
                                        startActivity(adminIntent);
                                        break;
                                    case "STUDENT":
                                        Intent studentIntent = new Intent(StudentLogIn.this, StudentDashboard.class);
                                        startActivity(studentIntent);
                                        break;
                                    default:
                                        Toast.makeText(StudentLogIn.this, "Unexpected role: " + role, Toast.LENGTH_LONG).show();
                                        break;
                                }
                            } else {
                                msgRes.setText("Response does not contain email, name, or role");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            msgRes.setText("Response parsing error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log error details
                        if (error.networkResponse != null) {
                            Log.e("Network Response Code", String.valueOf(error.networkResponse.statusCode));
                            Log.e("Network Response Data", new String(error.networkResponse.data));
                        } else if (error.getCause() != null) {
                            Log.e("Volley Error Cause", error.getCause().toString());
                        }

                        Log.e("Volley Error", error.toString());

                        // Check if the error is an instance of a more specific subclass
                        if (error instanceof NoConnectionError) {
                            Log.e("Volley", "NoConnectionError - Could not connect to the server");
                        } else if (error instanceof AuthFailureError) {
                            Log.e("Volley", "AuthFailureError - Authentication failure while performing the request");
                        } else if (error instanceof ServerError) {
                            Log.e("Volley", "ServerError - Server responded with an error response");
                        } // Add other error types if needed

                        Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Add any headers if needed
                return headers;
            }
        };


        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
    }

    /**
     * Making string request
     **/



    //@Override
    // public void onClick(View v) {

    // }
}
