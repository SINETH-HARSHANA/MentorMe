package com.s23010149.mentorme;

public class AppointmentModel {
    private String id;
    private String menteeName;
    private String menteeEmail;
    private String date;
    private String time;
    private String notes;
    private String status;

    public AppointmentModel(String id, String menteeName, String menteeEmail, String date, String time, String notes, String status) {
        this.id = id;
        this.menteeName = menteeName;
        this.menteeEmail = menteeEmail;
        this.date = date;
        this.time = time;
        this.notes = notes;
        this.status = status;
    }

    public String getId() { return id; }
    public String getMenteeName() { return menteeName; }
    public String getMenteeEmail() { return menteeEmail; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getNotes() { return notes; }
    public String getStatus() { return status; }
}