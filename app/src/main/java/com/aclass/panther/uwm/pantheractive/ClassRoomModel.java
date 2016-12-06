package com.aclass.panther.uwm.pantheractive;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by Asmamaw on 10/27/16.
 */

public class ClassRoomModel {
    private String question;
    private String answer;
    //    private String[] choices;
    // private ArrayList<String> choices;
    private HashMap<String, String> choices;

    public ClassRoomModel(String question, String answer, HashMap<String, String> choices) {
        Log.i("INFO", "Inside QuestionMOdel class");
        this.question = question;
        this.answer = answer;
        this.choices = choices;
    }

    public ClassRoomModel() {
    }


    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

//    public void setChoices(String[] choices) {
//        this.choices = choices;
//    }


    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

//    public String[] getChoices() {
//        return choices;
//    }

//    public ArrayList<String> getChoices() {
//        return choices;
//    }
//
//    public void setChoices(ArrayList<String> choices) {
//        Log.i("INFO", "Inside QuestionMOdel class setChoices method");
//
//        this.choices = choices;
//    }

    public HashMap<String, String> getChoices() {
        return choices;
    }


    public void setChoices(HashMap<String, String> choices) {
        this.choices = choices;
    }

    @Override
    public String toString() {
        return "QuestionModel{" +
                "question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", choices=" + choices +
                '}';
    }
}


