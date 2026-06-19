package com.s23010149.mentorme;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MenteeProfileActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPhone, etUniversity, etBio;
    private Button btnSaveProfile;

    private FirebaseFirestore db;
    private String prefilledEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentee_profile);

        // Bind views
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etUniversity = findViewById(R.id.etUniversity);
        etBio = findViewById(R.id.etBio);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);

        db = FirebaseFirestore.getInstance();

        // Get optional email from intent (if passed from login)
        prefilledEmail = getIntent().getStringExtra("email");
        if (prefilledEmail == null) prefilledEmail = "";

        // Allow user to edit their own email
        etEmail.setEnabled(true);
        etEmail.setFocusable(true);
        etEmail.setFocusableInTouchMode(true);

        if (!TextUtils.isEmpty(prefilledEmail)) {
            etEmail.setText(prefilledEmail);
            loadProfileByEmail(prefilledEmail);
        }

        btnSaveProfile.setOnClickListener(v -> saveProfile());
    }

    private void loadProfileByEmail(String email) {
        db.collection("users")
                .whereEqualTo("email", email)
                .whereEqualTo("role", "mentee")
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot doc = querySnapshot.getDocuments().get(0);
                        etName.setText(safe(doc.getString("name")));
                        etPhone.setText(safe(doc.getString("phone")));
                        etUniversity.setText(safe(doc.getString("university")));
                        etBio.setText(safe(doc.getString("bio")));
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load profile: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void saveProfile() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim(); // user's own entered email
        String phone = etPhone.getText().toString().trim();
        String university = etUniversity.getText().toString().trim();
        String bio = etBio.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Name is required");
            etName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Valid email is required");
            etEmail.requestFocus();
            return;
        }

        Map<String, Object> profile = new HashMap<>();
        profile.put("name", name);
        profile.put("email", email);
        profile.put("phone", phone);
        profile.put("university", university);
        profile.put("bio", bio);
        profile.put("role", "mentee");

        // Upsert by email+role (if exists update, else create)
        db.collection("users")
                .whereEqualTo("email", email)
                .whereEqualTo("role", "mentee")
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        String docId = querySnapshot.getDocuments().get(0).getId();
                        db.collection("users").document(docId)
                                .set(profile) // full replace (can use update too)
                                .addOnSuccessListener(unused ->
                                        Toast.makeText(this, "Profile saved successfully", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e ->
                                        Toast.makeText(this, "Save failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    } else {
                        db.collection("users")
                                .add(profile)
                                .addOnSuccessListener(ref ->
                                        Toast.makeText(this, "Profile saved successfully", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e ->
                                        Toast.makeText(this, "Save failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}