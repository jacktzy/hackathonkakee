package com.yydds.hackathonkakee.classes;

public class Team {
    private int no;
    private String teamName;
    private String[] teamMember;

    public Team(int no, String teamName, String[] teamMember) {
        this.no = no;
        this.teamName = teamName;
        this.teamMember = teamMember;
    }

    public int getNo() {
        return no;
    }

    public String getTeamName() {
        return teamName;
    }

    public String[] getTeamMember() {
        return teamMember;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setTeamMember(String[] teamMember) {
        this.teamMember = teamMember;
    }
}
