package com.s23010149.mentorme;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MentorDashboardActivity extends AppCompatActivity {

    private Button btnViewRequests, btnMySchedule, btnLogout, btnProfileSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mentor_dashboard); // <-- your XML file name

        btnViewRequests = findViewById(R.id.btnViewRequests);
        btnMySchedule = findViewById(R.id.btnMySchedule);
        btnLogout = findViewById(R.id.btnLogout);
        btnProfileSettings = findViewById(R.id.button5);

        // Profile Settings -> create_profile_mentor activity
        btnProfileSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MentorDashboardActivity.this, CreateProfileMentorActivity.class);
            startActivity(intent);
        });

        // Optional: other buttons
        btnViewRequests.setOnClickListener(v -> {
            // startActivity(new Intent(this, MentorRequestsActivity.class));
        });

        btnMySchedule.setOnClickListener(v -> {
            // startActivity(new Intent(this, MentorScheduleActivity.class));
        });

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(MentorDashboardActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}