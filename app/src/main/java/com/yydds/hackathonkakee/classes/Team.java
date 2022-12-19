package com.yydds.hackathonkakee.classes;

public class Team {
    private int ranking;
    private String teamName, hackathonID, leaderID, teamDescription, teamVisibility, leaderContact;
    private String[] participantsID;

    public Team(int no, String teamName, String[] teamMember) {
        this.ranking = no;
        this.teamName = teamName;
        this.participantsID = teamMember;
    }

    public int getRanking() {
        return ranking;
    }

    public String getTeamName() {
        return teamName;
    }

    public String[] getParticipantsID() {
        return participantsID;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setParticipantsID(String[] participantsID) {
        this.participantsID = participantsID;
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
}
