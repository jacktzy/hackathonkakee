package com.yydds.hackathonkakee.classes;

import java.util.ArrayList;

public class Team {
    private int ranking;
    private String teamName, hackathonID, leaderID, teamDescription, teamVisibility, leaderContact;
    private ArrayList<String> membersName;

    //constructor for create new team
    public Team(String teamName, String hackathonID, String leaderID, String teamDescription, String teamVisibility, String leaderContact, ArrayList<String> membersName) {
        this.teamName = teamName;
        this.hackathonID = hackathonID;
        this.leaderID = leaderID;
        this.teamDescription = teamDescription;
        this.teamVisibility = teamVisibility;
        this.leaderContact = leaderContact;
        this.membersName = membersName;
        this.ranking = 0;
    }

    public Team() {
    }

    public int getRanking() {
        return ranking;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getHackathonID() {
        return hackathonID;
    }

    public void setHackathonID(String hackathonID) {
        this.hackathonID = hackathonID;
    }

    public String getLeaderID() {
        return leaderID;
    }

    public void setLeaderID(String leaderID) {
        this.leaderID = leaderID;
    }

    public String getTeamDescription() {
        return teamDescription;
    }

    public void setTeamDescription(String teamDescription) {
        this.teamDescription = teamDescription;
    }

    public String getTeamVisibility() {
        return teamVisibility;
    }

    public void setTeamVisibility(String teamVisibility) {
        this.teamVisibility = teamVisibility;
    }

    public String getLeaderContact() {
        return leaderContact;
    }

    public void setLeaderContact(String leaderContact) {
        this.leaderContact = leaderContact;
    }

    public ArrayList<String> getMembersName() {
        return membersName;
    }

    public void setMembersName(ArrayList<String> membersName) {
        this.membersName = membersName;
    }
}
