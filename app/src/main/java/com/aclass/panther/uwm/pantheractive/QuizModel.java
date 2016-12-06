package com.aclass.panther.uwm.pantheractive;

/**
 * Created by Asmamaw on 10/27/16.
 */

public class QuizModel {
    private String quizId;
    private String quizName;
    private String startTime;
    private String endTime;
    private String challenge;
    private String isExpired;
    private String isTaken;
    private String isAvailable;


    public QuizModel() {

    }

    public QuizModel(String quizId, String quizName, String startTime,
                     String endTime, String challenge, String isExpired,
                     String isTaken, String isAvailable) {
        this.quizId = quizId;
        this.quizName = quizName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.challenge = challenge;
        this.isExpired = isExpired;
        this.isTaken = isTaken;
        this.isAvailable = isAvailable;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }

    public String getIsExpired() {
        return isExpired;
    }

    public void setIsExpired(String isExpired) {
        this.isExpired = isExpired;
    }

    public String getIsTaken() {
        return isTaken;
    }

    public void setIsTaken(String isTaken) {
        this.isTaken = isTaken;
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable;
    }

    @Override
    public String toString() {
        return
                quizName;

    }
}


