package com.s23010149.mentorme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MentorDashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mentor_dashboard);

        Button btnProfileSettings = findViewById(R.id.button5);
        btnProfileSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MentorDashboardActivity.this, CreateProfileMentorActivity.class);
                startActivity(intent);
            }
        });


    }
}