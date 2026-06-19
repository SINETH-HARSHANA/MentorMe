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

public class MyRequestsActivity extends AppCompatActivity {

    private RecyclerView rvMyRequests;
    private TextView tvEmptyRequests;

    private FirebaseFirestore db;
    private MyRequestsAdapter adapter;
    private List<RequestModel> requestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requests);

        rvMyRequests = findViewById(R.id.rvMyRequests);
        tvEmptyRequests = findViewById(R.id.tvEmptyRequests);

        db = FirebaseFirestore.getInstance();
        requestList = new ArrayList<>();

        rvMyRequests.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRequestsAdapter(requestList);
        rvMyRequests.setAdapter(adapter);

        // email should be passed from previous activity
        String menteeEmail = getIntent().getStringExtra("email");

        if (menteeEmail == null || menteeEmail.trim().isEmpty()) {
            tvEmptyRequests.setVisibility(View.VISIBLE);
            tvEmptyRequests.setText("No user email received.");
            return;
        }

        loadMyRequests(menteeEmail);
    }

    private void loadMyRequests(String menteeEmail) {
        // Firestore collection: mentor_requests
        // Fields expected: menteeEmail, mentorName, subject, message, status
        db.collection("mentor_requests")
                .whereEqualTo("menteeEmail", menteeEmail)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    requestList.clear();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String mentorName = doc.getString("mentorName");
                        String subject = doc.getString("subject");
                        String message = doc.getString("message");
                        String status = doc.getString("status");

                        requestList.add(new RequestModel(
                                mentorName != null ? mentorName : "Unknown",
                                subject != null ? subject : "-",
                                message != null ? message : "-",
                                status != null ? status : "Pending"
                        ));
                    }

                    adapter.notifyDataSetChanged();

                    if (requestList.isEmpty()) {
                        tvEmptyRequests.setVisibility(View.VISIBLE);
                    } else {
                        tvEmptyRequests.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(MyRequestsActivity.this,
                                "Failed to load requests: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show()
                );
    }
}