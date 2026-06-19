package com.s23010149.mentorme;

public class RequestModel {
    private String mentorName;
    private String subject;
    private String message;
    private String status;

    public RequestModel() {}

    public RequestModel(String mentorName, String subject, String message, String status) {
        this.mentorName = mentorName;
        this.subject = subject;
        this.message = message;
        this.status = status;
    }

    public String getMentorName() {
        return mentorName;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public void setMentorName(String mentorName) {
        this.mentorName = mentorName;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}