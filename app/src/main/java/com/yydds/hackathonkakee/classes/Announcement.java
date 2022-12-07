package com.yydds.hackathonkakee.classes;

public class Announcement {
    private String date, detail;

    public Announcement(String date, String detail) {
        this.date = date;
        this.detail = detail;
    }

    public String getDate() {
        return date;
    }

    public String getDetail() {
        return detail;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
