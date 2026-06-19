package com.s23010149.mentorme;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MenteeDashboardActivity extends AppCompatActivity {

    private ImageButton btnDropdownMenu;
    private Button btnNearbyMentors;
    private RecyclerView rvMentorProfiles;

    private FirebaseFirestore db;
    private MentorAdapter mentorAdapter;
    private List<MentorModel> mentorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mentee_dashboard);
        // make sure your xml file name is activity_mentee_dashboard.xml

        btnDropdownMenu = findViewById(R.id.btnDropdownMenu);
        btnNearbyMentors = findViewById(R.id.btnNearbyMentors);
        rvMentorProfiles = findViewById(R.id.rvMentorProfiles);

        db = FirebaseFirestore.getInstance();
        mentorList = new ArrayList<>();

        // RecyclerView setup
        rvMentorProfiles.setLayoutManager(new LinearLayoutManager(this));
        mentorAdapter = new MentorAdapter(mentorList);
        rvMentorProfiles.setAdapter(mentorAdapter);

        // Load mentors from Firestore
        loadMentors();

        // Nearby mentors button click (open map activity)
        btnNearbyMentors.setOnClickListener(v -> {
            // Replace NearbyMentorsMapActivity.class with your real map activity
            Intent intent = new Intent(MenteeDashboardActivity.this,FindNearbyMentorsActivity.class);
            startActivity(intent);
        });

        // Dropdown menu click
        btnDropdownMenu.setOnClickListener(v -> showPopupMenu());

        Button btnSeeMentees = findViewById(R.id.btnSeeMentees);
        Button btnBookAppointment = findViewById(R.id.btnBookAppointment);
        Button btnJoinMentee = findViewById(R.id.btnJoinMentee);

        btnSeeMentees.setOnClickListener(v ->
                startActivity(new Intent(MenteeDashboardActivity.this, SeeMenteesActivity.class)));

        btnBookAppointment.setOnClickListener(v ->
                startActivity(new Intent(MenteeDashboardActivity.this, BookAppointmentActivity.class)));

        btnJoinMentee.setOnClickListener(v ->
                startActivity(new Intent(MenteeDashboardActivity.this, JoinMenteeActivity.class)));
    }

    private void loadMentors() {
        // Assumes mentors are in "users" collection with role = "mentor"
        db.collection("users")
                .whereEqualTo("role", "mentor")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    mentorList.clear();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String name = doc.getString("name");
                        String email = doc.getString("email");
                        String expertise = doc.getString("expertise");

                        MentorModel mentor = new MentorModel(
                                name != null ? name : "No Name",
                                email != null ? email : "No Email",
                                expertise != null ? expertise : "Not specified"
                        );
                        mentorList.add(mentor);
                    }

                    mentorAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(MenteeDashboardActivity.this, "Failed to load mentors: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    private void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(this, btnDropdownMenu);

        // Add menu items programmatically
        popupMenu.getMenu().add(0, 1, 0, "Profile");
        popupMenu.getMenu().add(0, 2, 1, "My Requests");
        popupMenu.getMenu().add(0, 3, 2, "Logout");

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == 1) {
                    // Open profile activity
                    startActivity(new Intent(MenteeDashboardActivity.this, MenteeProfileActivity.class));
                    return true;
                } else if (id == 2) {
                    // Open mentee requests activity
                    startActivity(new Intent(MenteeDashboardActivity.this, MyRequestsActivity.class));
                    return true;
                } else if (id == 3) {
                    // Logout (if using FirebaseAuth, sign out there)
                    Intent intent = new Intent(MenteeDashboardActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    return true;
                }

                return false;
            }
        });

        popupMenu.show();
    }
}