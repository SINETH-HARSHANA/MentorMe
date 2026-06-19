package com.s23010149.mentorme;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    // Declare UI elements
    private EditText etName, etEmail, etPhone, etPassword;
    private RadioGroup rgRole;
    private Button btnRegister;

    // Initialize Firestore
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

        // Initialize Firestore instance
        db = FirebaseFirestore.getInstance();

        // 1. Link Java variables to XML IDs
        etName = findViewById(R.id.editTextText);
        etEmail = findViewById(R.id.editTextTextEmailAddress);
        etPhone = findViewById(R.id.editTextPhone);
        etPassword = findViewById(R.id.editTextTextPassword);
        rgRole = findViewById(R.id.radioGroupRole);
        btnRegister = findViewById(R.id.button2);

        // 2. Register Button Click Logic
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processRegistration();
            }
        });
    }

    private void processRegistration() {
        // Collect text from EditTexts
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Check which RadioButton is selected
        int selectedRoleId = rgRole.getCheckedRadioButtonId();

        // --- Input Validation ---
        if (TextUtils.isEmpty(name)) {
            etName.setError("Name is required");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            return;
        }
        if (selectedRoleId == -1) {
            Toast.makeText(this, "Please select a role (Mentor or Mentee)", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the actual text of the selected role ("As Mentor" or "As Mentee")
        RadioButton selectedRadioButton = findViewById(selectedRoleId);
        String role = selectedRadioButton.getText().toString();

        // --- Save to Firebase Firestore ---

        // Create a Map for the data
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("email", email);
        userData.put("phone", phone);
        userData.put("password", password); // Security Tip: Use Firebase Auth for real password handling
        userData.put("role", role);

        btnRegister.setEnabled(false); // Disable button to prevent double clicks

        db.collection("users")
                .add(userData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(RegisterActivity.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Close the screen and return to Login
                })
                .addOnFailureListener(e -> {
                    btnRegister.setEnabled(true);
                    Toast.makeText(RegisterActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}