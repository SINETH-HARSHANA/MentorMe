package com.s23010149.mentorme;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MentorRequestsActivity extends AppCompatActivity {

    private RecyclerView rvRequests;
    private TextView tvEmpty;
    private FirebaseFirestore db;
    private List<AppointmentModel> requestList;
    private MentorRequestsAdapter adapter;
    private String mentorEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_requests);

        rvRequests = findViewById(R.id.rvRequests);
        tvEmpty = findViewById(R.id.tvEmptyRequests);

        db = FirebaseFirestore.getInstance();
        mentorEmail = getIntent().getStringExtra("email");
        if (mentorEmail == null) mentorEmail = "";

        requestList = new ArrayList<>();
        adapter = new MentorRequestsAdapter(requestList, this::confirmBooking);

        rvRequests.setLayoutManager(new LinearLayoutManager(this));
        rvRequests.setAdapter(adapter);

        loadPendingRequests();
    }

    private void loadPendingRequests() {
        db.collection("appointments")
                .whereEqualTo("mentorEmail", mentorEmail)
                .whereEqualTo("status", "booked")
                .get()
                .addOnSuccessListener(snaps -> {
                    requestList.clear();
                    for (QueryDocumentSnapshot d : snaps) {
                        requestList.add(new AppointmentModel(
                                d.getId(),
                                d.getString("menteeName"),
                                d.getString("menteeEmail"),
                                d.getString("date"),
                                d.getString("time"),
                                d.getString("notes"),
                                d.getString("status")
                        ));
                    }
                    adapter.notifyDataSetChanged();
                    tvEmpty.setVisibility(requestList.isEmpty() ? View.VISIBLE : View.GONE);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void confirmBooking(AppointmentModel a) {
        db.collection("appointments").document(a.getId())
                .update("status", "confirmed")
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Booking confirmed", Toast.LENGTH_SHORT).show();
                    loadPendingRequests();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Confirm failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}