package com.aclass.panther.uwm.pantherquiz;

import java.util.HashMap;

/**
 * Created by Asmamaw on 10/27/16.
 */

public class QuestionModel {
    private String question;
    private String answer;

    private HashMap<String, String> choices;

    public QuestionModel(String question, String answer, HashMap<String, String> choices) {
        this.question = question;
        this.answer = answer;
        this.choices = choices;
    }

    public QuestionModel() {
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
                        ", choices=" + choices;

    }
}


