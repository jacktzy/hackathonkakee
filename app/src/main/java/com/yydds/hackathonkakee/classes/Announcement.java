package com.yydds.hackathonkakee.classes;

import com.google.firebase.Timestamp;

public class Announcement {
    private String title, content, hackathonID;
    private Timestamp timestamp;

    public Announcement(String title, String content, String hackathonID, Timestamp timestamp) {
        this.title = title;
        this.content = content;
        this.hackathonID = hackathonID;
        this.timestamp = timestamp;
    }

    public Announcement() {
    }

    public String getHackathonID() {
        return hackathonID;
    }

    public void setHackathonID(String hackathonID) {
        this.hackathonID = hackathonID;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
