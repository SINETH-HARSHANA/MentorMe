package com.s23010149.mentorme;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister, btnLoginAsMentor;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // View binding (IDs from your XML)
        etEmail = findViewById(R.id.editTextTextEmailAddress2);
        etPassword = findViewById(R.id.editTextTextPassword2);
        btnLogin = findViewById(R.id.button3);       // Login
        btnRegister = findViewById(R.id.button4);    // Register
        btnLoginAsMentor = findViewById(R.id.button); // Login as Mentor

        db = FirebaseFirestore.getInstance();

        // Register -> RegisterActivity
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Login as Mentor -> MentorLoginActivity (direct navigation as requested)
        btnLoginAsMentor.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MentorLoginActivity.class);
            startActivity(intent);
        });

        // Login -> validate email/password from Firestore, then go to MenteeDashboardActivity
        btnLogin.setOnClickListener(v -> loginMentee());
    }

    private void loginMentee() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Input validation
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email");
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        // Firestore check: collection "users"
        // each document should have fields: email, password, role (optional)
        db.collection("users")
                .whereEqualTo("email", email)
                .whereEqualTo("password", password)
                .limit(1)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            // Login success -> go to mentee dashboard
                            Intent intent = new Intent(MainActivity.this, MenteeDashboardActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}