package com.yydds.hackathonkakee.classes;

import com.google.firebase.Timestamp;

public class News {
    private String title, content, hackathonID, imageUri;
    private Timestamp timestamp;

    public News(String title, String content, String hackathonID, Timestamp timestamp) {
        this.title = title;
        this.content = content;
        this.hackathonID = hackathonID;
        this.timestamp = timestamp;
    }

    public News() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}
