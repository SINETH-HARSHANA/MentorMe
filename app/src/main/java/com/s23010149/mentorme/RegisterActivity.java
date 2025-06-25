package com.s23010149.mentorme;

import android.os.Bundle;
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
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page); // Use your registration XML filename



    Button registerButton = findViewById(R.id.button2);
    registerButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Collect data from EditTexts
            String name = ((EditText) findViewById(R.id.editTextText)).getText().toString();
            String email = ((EditText) findViewById(R.id.editTextTextEmailAddress)).getText().toString();
            String phone = ((EditText) findViewById(R.id.editTextPhone)).getText().toString();
            String password = ((EditText) findViewById(R.id.editTextTextPassword)).getText().toString();


            RadioGroup radioGroup = findViewById(R.id.radioGroupRole);
            int selectedId = radioGroup.getCheckedRadioButtonId();
            RadioButton selectedRadioButton = findViewById(selectedId);
            String role = selectedRadioButton.getText().toString();


            Map<String, Object> user = new HashMap<>();
            user.put("name", name);
            user.put("email", email);
            user.put("phone", phone);
            user.put("password", password); // Note: Don't store plain passwords in production!
            user.put("role", role);

            FirebaseFirestore db = FirebaseFirestore.getInstance();


            db.collection("users")
                    .add(user)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(RegisterActivity.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                        finish(); // Optional: close the registration screen
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(RegisterActivity.this, "Registration Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
            }
        });
    }
}




