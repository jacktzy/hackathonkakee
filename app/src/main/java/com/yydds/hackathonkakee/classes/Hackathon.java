package com.yydds.hackathonkakee.classes;

import java.util.LinkedList;

public class Hackathon {
    private String name, shortDesc, longDesc, mode, imageUrl, venue, organizerID;
    private int prizePool, maxTeamMembers;
    private LinkedList<String> participantsID, teamsID;

    //TODO finalize constructor (now only testing stage)


    public Hackathon(String name, String shortDesc, String longDesc, String mode, String venue, String organizerID, int prizePool, int maxTeamMembers) {
        this.name = name;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
        this.mode = mode;
        this.venue = venue;
        this.prizePool = prizePool;
        this.maxTeamMembers = maxTeamMembers;
        this.participantsID = new LinkedList<>();
        this.teamsID = new LinkedList<>();
        this.imageUrl = "";
        this.organizerID = organizerID;
    }

    public LinkedList<String> getTeamsID() {
        return teamsID;
    }

    public void setTeamsID(LinkedList<String> teamsID) {
        this.teamsID = teamsID;
    }

    public LinkedList<String> getParticipantsID() {
        return participantsID;
    }

    public String getOrganizerID() {
        return organizerID;
    }

    public void setOrganizerID(String organizerID) {
        this.organizerID = organizerID;
    }

    public void setParticipantsID(LinkedList<String> participantsID) {
        this.participantsID = participantsID;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
}
