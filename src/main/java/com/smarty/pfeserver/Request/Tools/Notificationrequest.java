package com.smarty.pfeserver.Request.Tools;

public class Notificationrequest {
    private Note note;
    private String token;

    public Notificationrequest() {
    }

    public Notificationrequest(Note note, String token) {
        this.note = note;
        this.token = token;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
