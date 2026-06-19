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
import java.util.Locale;

public class SeeMenteesActivity extends AppCompatActivity {

    private RecyclerView rvMentors;
    private TextView tvEmpty;

    private FirebaseFirestore db;
    private MentorCardsAdapter adapter;
    private List<MentorCardModel> mentorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_mentees);

        rvMentors = findViewById(R.id.rvMentors);
        tvEmpty = findViewById(R.id.tvEmptyMentors);

        db = FirebaseFirestore.getInstance();
        mentorList = new ArrayList<>();

        rvMentors.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MentorCardsAdapter(mentorList);
        rvMentors.setAdapter(adapter);

        loadAvailableMentors();
    }

    private void loadAvailableMentors() {
        // If you save mentor profiles in "mentors" collection:
        db.collection("mentors")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    mentorList.clear();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String name = doc.getString("name");
                        String mentoringSide = doc.getString("mentoringSide");
                        String contact = doc.getString("contact");

                        Double lat = doc.getDouble("latitude");
                        Double lng = doc.getDouble("longitude");

                        String locationText = "Not selected";
                        if (lat != null && lng != null) {
                            locationText = String.format(Locale.getDefault(), "%.5f, %.5f", lat, lng);
                        }

                        mentorList.add(new MentorCardModel(
                                name != null ? name : "No Name",
                                mentoringSide != null ? mentoringSide : "Not specified",
                                contact != null ? contact : "Not provided",
                                locationText
                        ));
                    }

                    adapter.notifyDataSetChanged();
                    tvEmpty.setVisibility(mentorList.isEmpty() ? View.VISIBLE : View.GONE);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load mentors: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}