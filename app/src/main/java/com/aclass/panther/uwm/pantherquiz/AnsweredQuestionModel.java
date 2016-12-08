package com.aclass.panther.uwm.pantherquiz;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by Asmamaw on 10/27/16.
 */

public class AnsweredQuestionModel {
    private String question;
    private String answer;
    private String studentAnswer; // student's selection

    private HashMap<String, String> choices;

    public AnsweredQuestionModel(String question, String answer, String studentAnswer, HashMap<String, String> choices) {
        Log.i("INFO", "Inside AnsweredQuestionModel class");
        this.question = question;
        this.answer = answer;
        this.choices = choices;
        this.studentAnswer = studentAnswer;
    }

    public AnsweredQuestionModel() {
    }


    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }


    public String getStudentAnswer() {
        return studentAnswer;
    }

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public HashMap<String, String> getChoices() {
        return choices;
    }


    public void setChoices(HashMap<String, String> choices) {
        this.choices = choices;
    }

    @Override
    public String toString() {
        return
                "question='" + question + '\'' +
                        ", answer='" + answer + '\'' +
                        ", choices=" + choices + '\'' +
                        ", studAns=" + studentAnswer;

    }
}


