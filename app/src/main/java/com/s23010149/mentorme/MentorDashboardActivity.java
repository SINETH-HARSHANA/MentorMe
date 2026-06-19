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
        setContentView(R.layout.mentor_dashboard);

        btnViewRequests = findViewById(R.id.btnViewRequests);
        btnMySchedule = findViewById(R.id.btnMySchedule);
        btnLogout = findViewById(R.id.btnLogout);
        btnProfileSettings = findViewById(R.id.button5);

        String mentorEmail = getIntent().getStringExtra("email");
        if (mentorEmail == null) mentorEmail = "";

        btnProfileSettings.setOnClickListener(v ->
                startActivity(new Intent(MentorDashboardActivity.this, CreateProfileMentorActivity.class)));

        String finalMentorEmail = mentorEmail;

        btnViewRequests.setOnClickListener(v -> {
            Intent i = new Intent(MentorDashboardActivity.this, MentorRequestsActivity.class);
            i.putExtra("email", finalMentorEmail);
            startActivity(i);
        });

        btnMySchedule.setOnClickListener(v -> {
            Intent i = new Intent(MentorDashboardActivity.this, MentorScheduleActivity.class);
            i.putExtra("email", finalMentorEmail);
            startActivity(i);
        });

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(MentorDashboardActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}