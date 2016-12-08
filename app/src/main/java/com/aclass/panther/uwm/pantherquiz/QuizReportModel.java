package com.aclass.panther.uwm.pantherquiz;

/**
 * Created by Asmamaw on 10/27/16.
 */

public class QuizReportModel {
    private String quizName;
    private String quizId;
    private String score;
    private String classAverage;
    private String quizKey;

    public QuizReportModel() {

    }

    public QuizReportModel(String quizName, String quizId, String score, String classAverage, String quizKey) {
        this.quizName = quizName;
        this.quizId = quizId;
        this.score = score;
        this.classAverage = classAverage;
        this.quizKey = quizKey;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getClassAverage() {
        return classAverage;
    }

    public void setClassAverage(String classAverage) {
        this.classAverage = classAverage;
    }

    public String getQuizKey() {
        return quizKey;
    }

    public void setQuizKey(String quizKey) {
        this.quizKey = quizKey;
    }

    @Override
    public String toString() {
        return "QuizReportModel{" +
                "quizName='" + quizName + '\'' +
                ", quizId='" + quizId + '\'' +
                ", score='" + score + '\'' +
                ", classAverage='" + classAverage + '\'' +
                '}';
    }
}


