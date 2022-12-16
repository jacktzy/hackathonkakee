package com.yydds.hackathonkakee.classes;

import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Participant {
    private String name, email, profilePicUrl, resumeUrl, gender, phoneNumber, institutionName, fieldMajor, levelOfEducation, interestField, interestJobPos;
    private ArrayList<String> participatedHackathonId;
    private double GPA;
    private Timestamp birthDate;


    //Constructor used when creating new participant account
    public Participant(String name, String email) throws ParseException {
        this.birthDate =  new Timestamp(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1900"));

        this.name = name;
        this.email = email;
        this.profilePicUrl = this.resumeUrl = this.gender = this.phoneNumber = this.institutionName = this.fieldMajor = this.levelOfEducation = this.interestField = this.interestJobPos = "";
        this.participatedHackathonId = new ArrayList<>();
        this.GPA = 0.0;
    }

    //Constructor used when updating participant profile


    public Participant(String name, String profilePicUrl, String resumeUrl, String gender, String phoneNumber, String institutionName, String fieldMajor, String levelOfEducation, String interestField, String interestJobPos, double GPA, Timestamp birthDate) {
        this.name = name;
        this.profilePicUrl = profilePicUrl;
        this.resumeUrl = resumeUrl;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.institutionName = institutionName;
        this.fieldMajor = fieldMajor;
        this.levelOfEducation = levelOfEducation;
        this.interestField = interestField;
        this.interestJobPos = interestJobPos;
        this.GPA = GPA;
        this.birthDate = birthDate;
    }

    public Participant() {
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

    public ArrayList<String> getParticipatedHackathonId() {
        return participatedHackathonId;
    }

    public void setParticipatedHackathonId(ArrayList<String> participatedHackathonId) {
        this.participatedHackathonId = participatedHackathonId;
    }

    public double getGPA() {
        return GPA;
    }

    public void setGPA(double GPA) {
        this.GPA = GPA;
    }

    public String getInterestField() {
        return interestField;
    }

    public void setInterestField(String interestField) {
        this.interestField = interestField;
    }

    public String getInterestJobPos() {
        return interestJobPos;
    }

    public void setInterestJobPos(String interestJobPos) {
        this.interestJobPos = interestJobPos;
    }

    public Timestamp getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Timestamp birthDate) {
        this.birthDate = birthDate;
    }
}
