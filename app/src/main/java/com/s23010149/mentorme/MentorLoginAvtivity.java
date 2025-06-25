package com.s23010149.mentorme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MentorLoginAvtivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mentor_login); // Use your actual layout name

        emailEditText = findViewById(R.id.editTextTextEmailAddress2);
        passwordEditText = findViewById(R.id.editTextTextPassword2);
        loginButton = findViewById(R.id.button3);

        db = FirebaseFirestore.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MentorLoginAvtivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Query Firestore for user with given email, password, and role "As Mentor"
                db.collection("users")
                        .whereEqualTo("email", email)
                        .whereEqualTo("password", password)
                        .whereEqualTo("role", "As Mentor")
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                    // Mentor login successful
                                    Toast.makeText(MentorLoginAvtivity.this, "Login successful as Mentor!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MentorLoginAvtivity.this, MentorDashboardActivity.class);
                                    startActivity(intent);
                                    finish(); // Optional: close the login screen
                                } else {
                                    Toast.makeText(MentorLoginAvtivity.this, "Login failed. Not a Mentor or incorrect credentials.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(MentorLoginAvtivity.this, "Login error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}