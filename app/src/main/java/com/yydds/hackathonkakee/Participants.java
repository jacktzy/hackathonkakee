package com.yydds.hackathonkakee;

public class Participants {
    private int no;
    private String name, team;

    public Participants(int no, String name, String team) {
        this.no = no;
        this.name = name;
        this.team = team;
    }

    public int getNo() {
        return no;
    }

    public String getName() {
        return name;
    }

    public String getTeam() {
        return team;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeam(String team) {
        this.team = team;
    }
}
