package com.aclass.panther.uwm.pantherquiz;

/**
 * Created by Asmamaw on 10/26/16.
 */
public class Question {
    private int questionNumber;
    private String questionText;
    private int numOfChoices;
    private String[] choices;

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void setNumOfChoices(int numOfChoices) {
        this.numOfChoices = numOfChoices;
    }

    public void setChoices(String[] choices) {
        this.choices = choices;
    }

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public void setCorectAnswer(String corectAnswer) {
        this.corectAnswer = corectAnswer;
    }

    public int getQuestionNumber() {

        return questionNumber;
    }

    public String getQuestionText() {
        return questionText;
    }

    public int getNumOfChoices() {
        return numOfChoices;
    }

    public String[] getChoices() {
        return choices;
    }

    public String getStudentAnswer() {
        return studentAnswer;
    }

    public String getCorectAnswer() {
        return corectAnswer;
    }

    public Question(String questionText, int numOfChoices, String[] choices, String corectAnswer) {

        this.questionText = questionText;
        this.numOfChoices = numOfChoices;
        this.choices = choices;
        this.corectAnswer = corectAnswer;
    }

    public Question() {

    }

    private String studentAnswer;
    private String corectAnswer;

}
