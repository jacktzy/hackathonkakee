package com.yydds.hackathonkakee.classes;

import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Organizer {
    private String name, email, gender, phoneNumber, jobPosition, employer, profilePicUrl;
    private Timestamp birthDate;
    private ArrayList<String> hostedHackathon;

    public Organizer(String name, String email) throws ParseException {
        this.name = name;
        this.email = email;
        this.hostedHackathon = new ArrayList<>();
        this.gender = this.phoneNumber = this.jobPosition = this.employer = this.profilePicUrl = "";
        this.birthDate =  new Timestamp(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1900"));
    }

    public void updateProfile (String name, String gender, String phoneNumber, String jobPosition, String employer, String profilePicUrl, Timestamp birthDate) {
        this.name = name;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.jobPosition = jobPosition;
        this.employer = employer;
        this.profilePicUrl = profilePicUrl;
        this.birthDate = birthDate;
    }

    public Organizer() {
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

    public ArrayList<String> getHostedHackathon() {
        return hostedHackathon;
    }

    public void setHostedHackathon(ArrayList<String> hostedHackathon) {
        this.hostedHackathon = hostedHackathon;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public Timestamp getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Timestamp birthDate) {
        this.birthDate = birthDate;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }
}
