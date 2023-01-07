package com.yydds.hackathonkakee.classes;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class Hackathon {
    private String name, shortDesc, longDesc, mode, iconUri, venue, organizerID;
    private int prizePool, maxTeamMembers;
    private ArrayList<String> participantsID, teamsID, participantsWithTeam;
    private Timestamp startDateTS, endDateTS;

    public Hackathon(String name, String shortDesc, String longDesc, String mode, String venue, String organizerID, String iconUri, Timestamp startDateTS, Timestamp endDateTS, int prizePool, int maxTeamMembers, ArrayList<String> participantsID, ArrayList<String> participantsWithTeam, ArrayList<String> teamsID) {
        this.name = name;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
        this.mode = mode;
        this.venue = venue;
        this.prizePool = prizePool;
        this.maxTeamMembers = maxTeamMembers;
        this.participantsID = participantsID;
        this.teamsID = teamsID;
        this.participantsWithTeam = participantsWithTeam;
        this.iconUri = iconUri;
        this.organizerID = organizerID;
        this.startDateTS = startDateTS;
        this.endDateTS = endDateTS;
    }

    public Hackathon() {
    }

    public ArrayList<String> getParticipantsID() {
        return participantsID;
    }

    public void setParticipantsID(ArrayList<String> participantsID) {
        this.participantsID = participantsID;
    }

    public ArrayList<String> getTeamsID() {
        return teamsID;
    }

    public void setTeamsID(ArrayList<String> teamsID) {
        this.teamsID = teamsID;
    }

    public String getOrganizerID() {
        return organizerID;
    }

    public void setOrganizerID(String organizerID) {
        this.organizerID = organizerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public int getPrizePool() {
        return prizePool;
    }

    public void setPrizePool(int prizePool) {
        this.prizePool = prizePool;
    }

    public int getMaxTeamMembers() {
        return maxTeamMembers;
    }

    public void setMaxTeamMembers(int maxTeamMembers) {
        this.maxTeamMembers = maxTeamMembers;
    }

    public Timestamp getStartDateTS() {
        return startDateTS;
    }

    public void setStartDateTS(Timestamp startDateTS) {
        this.startDateTS = startDateTS;
    }

    public Timestamp getEndDateTS() {
        return endDateTS;
    }

    public void setEndDateTS(Timestamp endDateTS) {
        this.endDateTS = endDateTS;
    }

    public ArrayList<String> getParticipantsWithTeam() {
        return participantsWithTeam;
    }

    public void setParticipantsWithTeam(ArrayList<String> participantsWithTeam) {
        this.participantsWithTeam = participantsWithTeam;
    }
}
