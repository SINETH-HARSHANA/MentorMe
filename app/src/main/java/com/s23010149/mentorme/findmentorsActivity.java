package com.s23010149.mentorme;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class findmentorsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker selectedMarker;
    private LatLng selectedLatLng;
    private Button btnConfirmLocation;

    // Mock mentor data structure
    static class Mentor {
        String name;
        LatLng location;

        Mentor(String name, LatLng location) {
            this.name = name;
            this.location = location;
        }
    }

    // Replace this with your actual mentor data fetching logic
    private List<Mentor> getNearbyMentors() {
        List<Mentor> mentors = new ArrayList<>();
        mentors.add(new Mentor("Mentor A", new LatLng(7.8900, 80.8000)));
        mentors.add(new Mentor("Mentor B", new LatLng(7.8500, 80.7500)));
        mentors.add(new Mentor("Mentor C", new LatLng(7.8700, 80.7700)));
        return mentors;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_nerby_mentors);

        btnConfirmLocation = findViewById(R.id.btnConfirmLocation);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        btnConfirmLocation.setOnClickListener(v -> {
            if (selectedLatLng != null) {
                String msg = "Selected Location: " + selectedLatLng.latitude + ", " + selectedLatLng.longitude;
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Please select a location on the map.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Default location (center of Sri Lanka as example)
        LatLng defaultLatLng = new LatLng(7.8731, 80.7718);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLatLng, 7.0f));

        // Show mentor markers
        List<Mentor> mentors = getNearbyMentors();
        for (Mentor mentor : mentors) {
            mMap.addMarker(new MarkerOptions()
                    .position(mentor.location)
                    .title(mentor.name)
                    .snippet("Tap to view mentor profile"));
        }

        // Listen for map taps to select a location
        mMap.setOnMapClickListener(latLng -> {
            if (selectedMarker != null) selectedMarker.remove();
            selectedLatLng = latLng;
            selectedMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));
        });

        // (Optional) Listen for marker taps
        mMap.setOnMarkerClickListener(marker -> {
            if (!"Selected Location".equals(marker.getTitle())) {
                Toast.makeText(this, "Mentor: " + marker.getTitle(), Toast.LENGTH_SHORT).show();
                // TODO: You could show a detailed mentor profile or dialog here
                return true; // consume event
            }
            return false; // let default behavior occur
        });
    }
}