package com.yydds.hackathonkakee.classes;

import java.util.LinkedList;

public class Participant {
    private String name, email, profilePicUrl, resumeUrl, gender, phoneNumber, institutionName, fieldMajor, levelOfEducation;
    private LinkedList<String> participatedHackathonId;
    private double GPA;

    public Participant(String name, String email) {
        this.name = name;
        this.email = email;
        this.profilePicUrl = this.resumeUrl = this.gender = this.phoneNumber = this.institutionName = this.fieldMajor = this.levelOfEducation
                = "";
        this.participatedHackathonId = new LinkedList<>();
        this.GPA = 0.0;
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

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
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

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getFieldMajor() {
        return fieldMajor;
    }

    public void setFieldMajor(String fieldMajor) {
        this.fieldMajor = fieldMajor;
    }

    public String getLevelOfEducation() {
        return levelOfEducation;
    }

    public void setLevelOfEducation(String levelOfEducation) {
        this.levelOfEducation = levelOfEducation;
    }

    public LinkedList<String> getParticipatedHackathonId() {
        return participatedHackathonId;
    }

    public void setParticipatedHackathonId(LinkedList<String> participatedHackathonId) {
        this.participatedHackathonId = participatedHackathonId;
    }

    public double getGPA() {
        return GPA;
    }

    public void setGPA(double GPA) {
        this.GPA = GPA;
    }
}
