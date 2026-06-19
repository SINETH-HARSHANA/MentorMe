package com.s23010149.mentorme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateProfileMentorActivity extends AppCompatActivity {

    // Declare UI Elements
    private ImageView imgProfilePhoto;
    private Button btnUploadPhoto, btnSelectLocation, btnSaveProfile;
    private EditText etMentorName, etMentoringSide, etAvailableHours, etContact, etDescription;
    private ProgressBar progressBar;

    // Initialize Firestore
    private FirebaseFirestore db;

    // Store selected location coordinates
    private double selectedLatitude = 0.0;
    private double selectedLongitude = 0.0;

    // Activity Result Launcher for location picker
    private final ActivityResultLauncher<Intent> locationPickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedLatitude = result.getData().getDoubleExtra("selected_lat", 0.0);
                    selectedLongitude = result.getData().getDoubleExtra("selected_lng", 0.0);

                    // Update button text to show selected location
                    btnSelectLocation.setText("Lat: " + String.format("%.4f", selectedLatitude) +
                            ", Lng: " + String.format("%.4f", selectedLongitude));

                    Toast.makeText(this, "Location selected: " + selectedLatitude + ", " + selectedLongitude,
                            Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_profile_mentor);

        // Initialize Firestore instance
        db = FirebaseFirestore.getInstance();

        // 1. Bind Views to XML IDs
        imgProfilePhoto = findViewById(R.id.imgProfilePhoto);
        btnUploadPhoto = findViewById(R.id.btnUploadPhoto);
        btnSelectLocation = findViewById(R.id.btnSelectLocation);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);

        etMentorName = findViewById(R.id.etMentorName);
        etMentoringSide = findViewById(R.id.etMentoringSide);
        etAvailableHours = findViewById(R.id.etAvailableHours);
        etContact = findViewById(R.id.etContact);
        etDescription = findViewById(R.id.etDescription);

        progressBar = findViewById(R.id.progressBar);

        // 2. Upload Photo Button Click
        btnUploadPhoto.setOnClickListener(v -> {
            // Placeholder: Add your Image Picker logic here
            Toast.makeText(this, "Upload Photo clicked", Toast.LENGTH_SHORT).show();
        });

        // 3. Select Location Button Click
        btnSelectLocation.setOnClickListener(v -> {
            Intent intent = new Intent(CreateProfileMentorActivity.this, LocationSelectorActivity.class);
            locationPickerLauncher.launch(intent);
        });

        // 4. Save Profile Button Click
        btnSaveProfile.setOnClickListener(v -> saveMentorData());
    }

    private void saveMentorData() {
        // Get data from fields
        String name = etMentorName.getText().toString().trim();
        String side = etMentoringSide.getText().toString().trim();
        String hours = etAvailableHours.getText().toString().trim();
        String contact = etContact.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        // Simple Validation
        if (TextUtils.isEmpty(name)) {
            etMentorName.setError("Name is required");
            return;
        }
        if (TextUtils.isEmpty(contact)) {
            etContact.setError("Contact info is required");
            return;
        }

        if (selectedLatitude == 0.0 && selectedLongitude == 0.0) {
            Toast.makeText(this, "Please select a location", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading indicator and disable button
        progressBar.setVisibility(View.VISIBLE);
        btnSaveProfile.setEnabled(false);

        // Create a Map to store in Firestore
        Map<String, Object> mentorProfile = new HashMap<>();
        mentorProfile.put("name", name);
        mentorProfile.put("mentoringSide", side);
        mentorProfile.put("availableHours", hours);
        mentorProfile.put("contact", contact);
        mentorProfile.put("description", description);
        mentorProfile.put("latitude", selectedLatitude);
        mentorProfile.put("longitude", selectedLongitude);
        mentorProfile.put("timestamp", com.google.firebase.Timestamp.now());

        // Save to Firestore collection "mentors"
        db.collection("mentors")
                .add(mentorProfile)
                .addOnSuccessListener(documentReference -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(CreateProfileMentorActivity.this, "Profile Saved Successfully!", Toast.LENGTH_LONG).show();
                    finish(); // Closes this activity and returns to the previous screen
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    btnSaveProfile.setEnabled(true);
                    Toast.makeText(CreateProfileMentorActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}