package com.example.pengu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class InboxActivity extends AppCompatActivity {
    Button joinBtn;
    String URL, tokenAuthThing;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        bottomNavigationView = findViewById(R.id.student_navbar);

        bottomNavigationView.setSelectedItemId(R.id.stuInbox);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.stuDashboard) {
                    Intent intent = new Intent(InboxActivity.this, ViewSubjectsActivity.class);
                    startActivity(intent);
                    return true;
                }
                else if (item.getItemId() == R.id.stuSchedule) {
                    Intent intent = new Intent(InboxActivity.this, StudentDashboard.class);
                    startActivity(intent);
                    return true;
                }
                else if (item.getItemId() == R.id.stuInbox) {
                    //Currently here
                    return true;
                }
                else if (item.getItemId() == R.id.stuProfile) {
                    Intent intent = new Intent(InboxActivity.this, StudentProfile.class);
                    startActivity(intent);
                    return true;
                }
                else {
                    return true;
                }
            }
        });

        joinBtn = findViewById(R.id.joinChatButton);

        URL = "ws://coms-309-037.class.las.iastate.edu:8080/chat/";

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE);
        tokenAuthThing = sharedPreferences.getString("AuthToken", "");
        Log.e("The user Token is: ", tokenAuthThing);

        joinBtn.setOnClickListener(view -> {
            String serverUrl = URL + tokenAuthThing;
            Log.e("ServerUrl", serverUrl);

            WebSocketManager1.getInstance().connectWebSocket(serverUrl);

            Intent intent = new Intent(this, MessageActivity.class);
            startActivity(intent);
        });

    }
}
