package com.example.pengu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView teacherText, studentText;
    private ImageView teacherPic, studentPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        teacherText = findViewById(R.id.teacherTextView);
        studentText = findViewById(R.id.studentTextView);

        teacherPic = findViewById(R.id.teacherImageView);
        studentPic = findViewById(R.id.studentImageView);

        teacherText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start the TeacherLogin Activity
                Intent intent = new Intent(MainActivity.this, TeacherLogin.class);
                startActivity(intent);
            }
        });

        teacherPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start the TeacherLogin Activity
                Intent intent = new Intent(MainActivity.this, TeacherLogin.class);
                startActivity(intent);
            }
        });

        studentText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start the TeacherLogin Activity
                Intent intent = new Intent(MainActivity.this, StudentLogIn.class);
                startActivity(intent);
            }
        });

        studentPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start the TeacherLogin Activity
                Intent intent = new Intent(MainActivity.this, StudentLogIn.class);
                startActivity(intent);
            }
        });
    }
}