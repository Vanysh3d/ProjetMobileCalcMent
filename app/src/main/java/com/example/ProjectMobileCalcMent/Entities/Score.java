package com.example.ProjectMobileCalcMent.Entities;

public class Score extends BaseEntity {
    public String username;
    public int score;

    public Score(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
