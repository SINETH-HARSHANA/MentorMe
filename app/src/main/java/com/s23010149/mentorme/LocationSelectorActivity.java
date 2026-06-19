package com.s23010149.mentorme;

import android.content.Intent;
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
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationSelectorActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button btnConfirmLocation;
    private LatLng selectedLatLng = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_selector);

        btnConfirmLocation = findViewById(R.id.btnConfirmLocation);

        // Get map fragment
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Confirm button click -> return selected lat/lng
        btnConfirmLocation.setOnClickListener(v -> {
            if (selectedLatLng == null) {
                Toast.makeText(this, "Please tap a location on the map first", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra("selected_lat", selectedLatLng.latitude);
            resultIntent.putExtra("selected_lng", selectedLatLng.longitude);

            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Default location: Colombo, Sri Lanka
        LatLng colombo = new LatLng(6.9271, 79.8612);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(colombo, 12f));

        // Map click listener -> place marker at tapped location
        mMap.setOnMapClickListener(latLng -> {
            selectedLatLng = latLng;

            // Clear previous markers
            mMap.clear();

            // Add new marker at selected location
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Selected Location")
                    .snippet("Lat: " + latLng.latitude + ", Lng: " + latLng.longitude));

            Toast.makeText(LocationSelectorActivity.this,
                    "Location: " + latLng.latitude + ", " + latLng.longitude,
                    Toast.LENGTH_SHORT).show();
        });
    }
}