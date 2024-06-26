package com.example.pengu;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.Context;
import android.util.Log;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.content.BroadcastReceiver;
import android.widget.Toast;

import org.java_websocket.handshake.ServerHandshake;

public class MessageActivity extends AppCompatActivity implements WebSocketListener {

    private ImageButton backBtn, optionBtn, sendBtn;
    private TextView chatLog;
    private EditText sendText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        backBtn = findViewById(R.id.backButton);
        optionBtn = findViewById(R.id.optionButton);
        sendBtn = findViewById(R.id.sendButton);

        chatLog = findViewById(R.id.textLog);

        sendText = findViewById(R.id.enterText);

        WebSocketManager1.getInstance().setWebSocketListener(MessageActivity.this);

        sendText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.e("EditorAction","Started");
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    sendBtn.performClick();
                    Log.e("EditorAction","Completed");
                }
                Log.e("EditorAction", "Failed");
                return false;
            }
        });

        sendBtn.setOnClickListener(v -> {
            try {
                WebSocketManager1.getInstance().sendMessage(sendText.getText().toString());
                sendText.setText("");
            } catch (Exception e) {
                Log.d("ExceptionSendMessage", e.getMessage().toString());
            }
        });

        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, InboxActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onWebSocketMessage(String message) {
        runOnUiThread(() -> {
            String s = chatLog.getText().toString();
            chatLog.setText(s + "\n"+message);
        });
    }


//    @Override
//    public void onWebSocketClose(int code, String reason, boolean remote) {
//        String closedBy = remote ? "server" : "local";
//        runOnUiThread(() -> {
//            String s = chatLog.getText().toString();
//            chatLog.setText(s + "---\nconnection closed by " + closedBy + "\nreason: " + reason);
//        });
//    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        String closedBy = remote ? "server" : "local";
        runOnUiThread(() -> {
            String s = chatLog.getText().toString();
            chatLog.setText(s + "---\nconnection closed by " + closedBy + "\nreason: " + reason);
        });
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {}

//    @Override
//    public void onWebSocketError(Exception ex) {}

    @Override
    public void onWebSocketError(Exception ex) {
        runOnUiThread(() -> {
            // Handle WebSocket error
            Log.e("WebSocket", "Error occurred in WebSocket communication: " + ex.getMessage(), ex);
            // Optionally, you can display a toast or show an error message to the user
            Toast.makeText(this, "WebSocket error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}
