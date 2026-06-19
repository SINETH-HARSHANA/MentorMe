package com.s23010149.mentorme;

public class MentorCardModel {
    private String name;
    private String mentoringSide;
    private String contact;
    private String locationText;

    public MentorCardModel(String name, String mentoringSide, String contact, String locationText) {
        this.name = name;
        this.mentoringSide = mentoringSide;
        this.contact = contact;
        this.locationText = locationText;
    }

    public String getName() { return name; }
    public String getMentoringSide() { return mentoringSide; }
    public String getContact() { return contact; }
    public String getLocationText() { return locationText; }
}