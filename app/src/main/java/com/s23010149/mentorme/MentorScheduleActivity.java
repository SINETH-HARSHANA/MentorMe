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

public class MentorScheduleActivity extends AppCompatActivity {

    private RecyclerView rvSchedule;
    private TextView tvEmptySchedule;
    private FirebaseFirestore db;
    private List<AppointmentModel> confirmedList;
    private MentorScheduleAdapter adapter;
    private String mentorEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_schedule);

        rvSchedule = findViewById(R.id.rvSchedule);
        tvEmptySchedule = findViewById(R.id.tvEmptySchedule);

        db = FirebaseFirestore.getInstance();
        mentorEmail = getIntent().getStringExtra("email");
        if (mentorEmail == null) mentorEmail = "";

        confirmedList = new ArrayList<>();
        adapter = new MentorScheduleAdapter(confirmedList);

        rvSchedule.setLayoutManager(new LinearLayoutManager(this));
        rvSchedule.setAdapter(adapter);

        loadConfirmedAppointments();
    }

    private void loadConfirmedAppointments() {
        db.collection("appointments")
                .whereEqualTo("mentorEmail", mentorEmail)
                .whereEqualTo("status", "confirmed")
                .get()
                .addOnSuccessListener(snaps -> {
                    confirmedList.clear();
                    for (QueryDocumentSnapshot d : snaps) {
                        confirmedList.add(new AppointmentModel(
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
                    tvEmptySchedule.setVisibility(confirmedList.isEmpty() ? View.VISIBLE : View.GONE);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}