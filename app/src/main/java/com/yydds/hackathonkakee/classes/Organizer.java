package com.yydds.hackathonkakee.classes;

import java.util.LinkedList;

public class Organizer {
    private String name, email;
    private LinkedList<String> hostedHackathon;

    public Organizer(String name, String email) {
        this.name = name;
        this.email = email;
        this.hostedHackathon = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LinkedList<String> getHostedHackathon() {
        return hostedHackathon;
    }

    public void setHostedHackathon(LinkedList<String> hostedHackathon) {
        this.hostedHackathon = hostedHackathon;
    }
}
