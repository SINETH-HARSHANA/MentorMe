package com.s23010149.mentorme;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookAppointmentActivity extends AppCompatActivity {

    private Spinner spMentees;
    private EditText etDate, etTime, etNotes;
    private Button btnConfirmAppointment;

    private FirebaseFirestore db;

    // mentee dropdown display list + email map
    private final List<String> menteeDisplayList = new ArrayList<>();
    private final Map<String, String> menteeNameToEmail = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        spMentees = findViewById(R.id.spMentees);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        etNotes = findViewById(R.id.etNotes);
        btnConfirmAppointment = findViewById(R.id.btnConfirmAppointment);

        db = FirebaseFirestore.getInstance();

        loadMenteesIntoDropdown();

        btnConfirmAppointment.setOnClickListener(v -> saveAppointment());
    }

    private void loadMenteesIntoDropdown() {
        db.collection("users")
                .whereEqualTo("role", "mentee")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    menteeDisplayList.clear();
                    menteeNameToEmail.clear();

                    menteeDisplayList.add("Select a mentee");

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String name = doc.getString("name");
                        String email = doc.getString("email");

                        if (name == null || name.trim().isEmpty()) name = "Unnamed Mentee";
                        if (email == null) email = "";

                        // avoid duplicate names by appending email
                        String display = name;
                        if (menteeNameToEmail.containsKey(display)) {
                            display = name + " (" + email + ")";
                        }

                        menteeDisplayList.add(display);
                        menteeNameToEmail.put(display, email);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            BookAppointmentActivity.this,
                            android.R.layout.simple_spinner_item,
                            menteeDisplayList
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spMentees.setAdapter(adapter);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load mentees: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void saveAppointment() {
        String selectedMenteeDisplay = String.valueOf(spMentees.getSelectedItem());
        String date = etDate.getText().toString().trim();
        String time = etTime.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();

        if (TextUtils.isEmpty(selectedMenteeDisplay) || "Select a mentee".equals(selectedMenteeDisplay)) {
            Toast.makeText(this, "Please select a mentee", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(date)) {
            etDate.setError("Date is required");
            etDate.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(time)) {
            etTime.setError("Time is required");
            etTime.requestFocus();
            return;
        }

        String menteeEmail = menteeNameToEmail.get(selectedMenteeDisplay);
        if (menteeEmail == null) menteeEmail = "";

        // Optional: get mentor email passed from previous screen
        String mentorEmail = getIntent().getStringExtra("email");
        if (mentorEmail == null) mentorEmail = "";

        Map<String, Object> appointment = new HashMap<>();
        appointment.put("menteeName", selectedMenteeDisplay);
        appointment.put("menteeEmail", menteeEmail);
        appointment.put("mentorEmail", mentorEmail);
        appointment.put("date", date);     // e.g. 2026-06-20
        appointment.put("time", time);     // e.g. 14:30
        appointment.put("notes", notes);
        appointment.put("status", "booked");
        appointment.put("createdAt", Timestamp.now());

        // "appointments table" in Firestore = collection named appointments
        db.collection("appointments")
                .add(appointment)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Appointment booked successfully", Toast.LENGTH_SHORT).show();
                    clearForm();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to book appointment: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void clearForm() {
        spMentees.setSelection(0);
        etDate.setText("");
        etTime.setText("");
        etNotes.setText("");
    }
}