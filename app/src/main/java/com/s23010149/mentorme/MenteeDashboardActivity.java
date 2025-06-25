package com.s23010149.mentorme;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;

public class MenteeDashboardActivity extends AppCompatActivity {
    private RecyclerView rvMentorProfiles;
    private MentorProfileAdapter adapter;
    private List<MentorProfileActivity> mentorList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mentee_dashboard);

        rvMentorProfiles = findViewById(R.id.rvMentorProfiles);
        rvMentorProfiles.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MentorProfileAdapter(this, mentorList);
        rvMentorProfiles.setAdapter(adapter);

        // Add menu button click handler here
        ImageButton btnDropdownMenu = findViewById(R.id.btnDropdownMenu);
        btnDropdownMenu.setOnClickListener(v -> {
            Intent intent = new Intent(MenteeDashboardActivity.this, Appointmentactivity.class);
            startActivity(intent);
        });

        fetchMentors();
    }

    private void fetchMentors() {
        DatabaseReference mentorsRef = FirebaseDatabase.getInstance().getReference("mentors");
        mentorsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mentorList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    MentorProfileActivity profile = ds.getValue(MentorProfileActivity.class);
                    if (profile != null) {
                        mentorList.add(profile);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MenteeDashboardActivity.this, "Failed to load mentors", Toast.LENGTH_SHORT).show();
            }
        });
    }
}