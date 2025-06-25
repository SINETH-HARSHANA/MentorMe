package com.s23010149.mentorme;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.*;
import android.view.View;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateProfileMentorActivity extends AppCompatActivity {

    private static final int PICK_LOCATION_REQUEST = 1001;

    private ImageView imgProfilePhoto;
    private Button btnUploadPhoto, btnSelectLocation, btnSaveProfile;
    private EditText etMentorName, etMentoringSide, etAvailableHours, etContact, etDescription;
    private Uri profilePhotoUri;
    private String uploadedPhotoUrl = "";

    private FirebaseAuth mAuth;
    private DatabaseReference mentorsRef;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private ProgressBar progressBar;

    // Hold selected location
    private double selectedLatitude = 0.0;
    private double selectedLongitude = 0.0;

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<String[]> requestCameraPermissionLauncher;
    private Uri cameraImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_profile_mentor);

        // Init Firebase
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        mentorsRef = FirebaseDatabase.getInstance().getReference("mentors");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        imgProfilePhoto = findViewById(R.id.imgProfilePhoto);
        btnUploadPhoto = findViewById(R.id.btnUploadPhoto);
        btnSelectLocation = findViewById(R.id.btnSelectLocation);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        etMentorName = findViewById(R.id.etMentorName);
        etMentoringSide = findViewById(R.id.etMentoringSide);
        etAvailableHours = findViewById(R.id.etAvailableHours);
        etContact = findViewById(R.id.etContact);
        etDescription = findViewById(R.id.etDescription);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        // Register gallery picker
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null && result.getData().getData() != null) {
                        profilePhotoUri = result.getData().getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), profilePhotoUri);
                            imgProfilePhoto.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        // Register camera launcher
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        if (cameraImageUri != null) {
                            profilePhotoUri = cameraImageUri;
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), profilePhotoUri);
                                imgProfilePhoto.setImageBitmap(bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );

        // Register camera permission launcher
        requestCameraPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    Boolean cameraGranted = result.getOrDefault(Manifest.permission.CAMERA, false);
                    Boolean storageGranted = result.getOrDefault(Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
                    if (cameraGranted && storageGranted) {
                        openCamera();
                    } else {
                        Toast.makeText(this, "Camera permission is required to take a photo.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        btnUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a dialog to choose Camera or Gallery
                String[] options = {"Camera", "Gallery"};
                new AlertDialog.Builder(CreateProfileMentorActivity.this)
                        .setTitle("Choose Photo")
                        .setItems(options, (dialog, which) -> {
                            if (which == 0) {
                                // Camera selected
                                checkCameraPermissionAndOpenCamera();
                            } else {
                                // Gallery selected
                                openImagePicker();
                            }
                        })
                        .show();
            }
        });

        btnSelectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateProfileMentorActivity.this, PickLocationActivity.class);
                startActivityForResult(intent, PICK_LOCATION_REQUEST);
            }
        });

        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileToFirebase();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        imagePickerLauncher.launch(Intent.createChooser(intent, "Select Profile Photo"));
    }

    private void checkCameraPermissionAndOpenCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
            boolean storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    || Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
            if (cameraPermission && storagePermission) {
                openCamera();
            } else {
                requestCameraPermissionLauncher.launch(new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                });
            }
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        cameraImageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
        cameraLauncher.launch(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_LOCATION_REQUEST && resultCode == RESULT_OK && data != null) {
            double lat = data.getDoubleExtra("latitude", 0.0);
            double lng = data.getDoubleExtra("longitude", 0.0);
            selectedLatitude = lat;
            selectedLongitude = lng;
            Toast.makeText(this, "Selected: " + lat + ", " + lng, Toast.LENGTH_SHORT).show();
        }
    }

    private void saveProfileToFirebase() {
        final String name = etMentorName.getText().toString().trim();
        final String mentoringSide = etMentoringSide.getText().toString().trim();
        final String availableHours = etAvailableHours.getText().toString().trim();
        final String contact = etContact.getText().toString().trim();
        final String description = etDescription.getText().toString().trim();

        if (name.isEmpty() || mentoringSide.isEmpty() || availableHours.isEmpty() ||
                contact.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedLatitude == 0.0 && selectedLongitude == 0.0) {
            Toast.makeText(this, "Please select your location", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        if (profilePhotoUri != null) {
            final StorageReference ref = storageReference.child("mentor_profile_images/" + UUID.randomUUID());
            ref.putFile(profilePhotoUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    uploadedPhotoUrl = uri.toString();
                                    uploadProfile(name, mentoringSide, availableHours, contact, description, uploadedPhotoUrl);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(CreateProfileMentorActivity.this, "Failed to upload photo: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("ProfileUpload", "Photo upload error", e);
                        }
                    });
        } else {
            uploadProfile(name, mentoringSide, availableHours, contact, description, uploadedPhotoUrl);
        }
    }

    private void uploadProfile(String name, String mentoringSide, String availableHours, String contact, String description, String photoUrl) {
        FirebaseUser user = mAuth.getCurrentUser();
        String uid;
        if (user != null) {
            uid = user.getUid();
        } else {
            uid = mentorsRef.push().getKey();
        }

        if (uid == null) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "User authentication failed. Please log in again.", Toast.LENGTH_LONG).show();
            return;
        }

        Map<String, Object> mentorMap = new HashMap<>();
        mentorMap.put("name", name);
        mentorMap.put("mentoringSide", mentoringSide);
        mentorMap.put("availableHours", availableHours);
        mentorMap.put("contact", contact);
        mentorMap.put("description", description);
        mentorMap.put("photoUrl", photoUrl);
        mentorMap.put("latitude", selectedLatitude);
        mentorMap.put("longitude", selectedLongitude);

        mentorsRef.child(uid).setValue(mentorMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(CreateProfileMentorActivity.this, "Profile saved successfully!", Toast.LENGTH_SHORT).show();
                        finish(); // Go back or redirect as needed
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(CreateProfileMentorActivity.this, "Failed to save profile: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("ProfileUpload", "Database write error", e);
                    }
                });
    }
}