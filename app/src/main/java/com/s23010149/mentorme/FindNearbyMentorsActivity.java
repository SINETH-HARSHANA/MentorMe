package com.s23010149.mentorme;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class FindNearbyMentorsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseFirestore db;
    private LatLngBounds.Builder boundsBuilder;
    private boolean hasAtLeastOneMarker = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_nerby_mentors);
        // XML must contain fragment with id: map

        db = FirebaseFirestore.getInstance();
        boundsBuilder = new LatLngBounds.Builder();

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Toast.makeText(this, "Map fragment not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Default location (Colombo) until mentors are loaded
        LatLng defaultLocation = new LatLng(6.9271, 79.8612);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f));

        loadMentorLocationsFromFirestore();
    }

    private void loadMentorLocationsFromFirestore() {
        // If you saved profiles in "mentors" collection:
        // fields expected: name, latitude, longitude
        db.collection("mentors")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    mMap.clear();
                    hasAtLeastOneMarker = false;
                    boundsBuilder = new LatLngBounds.Builder();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String mentorName = doc.getString("name");
                        Double latitude = doc.getDouble("latitude");
                        Double longitude = doc.getDouble("longitude");
                        String side = doc.getString("mentoringSide"); // optional

                        if (latitude != null && longitude != null) {
                            LatLng mentorLocation = new LatLng(latitude, longitude);

                            String title = (mentorName != null && !mentorName.isEmpty()) ? mentorName : "Mentor";
                            String snippet = (side != null && !side.isEmpty()) ? "Expertise: " + side : "Available Mentor";

                            mMap.addMarker(new MarkerOptions()
                                    .position(mentorLocation)
                                    .title(title)
                                    .snippet(snippet));

                            boundsBuilder.include(mentorLocation);
                            hasAtLeastOneMarker = true;
                        }
                    }

                    if (hasAtLeastOneMarker) {
                        // Fit camera to all mentor markers
                        int padding = 120;
                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), padding));
                    } else {
                        Toast.makeText(this, "No mentor locations found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load mentor locations: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }
}