package com.s23010149.mentorme;

public class MentorModel {
    private String name;
    private String email;
    private String expertise;

    public MentorModel(String name, String email, String expertise) {
        this.name = name;
        this.email = email;
        this.expertise = expertise;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getExpertise() { return expertise; }
}