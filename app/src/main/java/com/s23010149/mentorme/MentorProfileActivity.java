package com.s23010149.mentorme;

public class MentorProfileActivity {
    public String name, mentoringSide, availableHours, contact, description, photoUrl;
    public double latitude, longitude;

    public MentorProfileActivity() {} // Needed for Firebase

    public MentorProfileActivity(String name, String mentoringSide, String availableHours, String contact, String description, String photoUrl, double latitude, double longitude) {
        this.name = name;
        this.mentoringSide = mentoringSide;
        this.availableHours = availableHours;
        this.contact = contact;
        this.description = description;
        this.photoUrl = photoUrl;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}