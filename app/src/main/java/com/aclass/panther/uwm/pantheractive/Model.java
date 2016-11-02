package com.aclass.panther.uwm.pantheractive;

/**
 * Created by Asmamaw on 9/15/16.
 */
public class Model{
    String name;
    int value; /* 0 -&gt; checkbox disable, 1 -&gt; checkbox enable */
    String textEdit;

    Model(String name, int value, String textEdit){
        this.name = name;
        this.value = value;
        this.textEdit = textEdit;
    }
    public String getName(){
        return this.name;
    }
    public int getValue(){
        return this.value;
    }

    public String getTextEdit() {
        return textEdit;
    }

    public void setTextEdit(String textEdit) {
        this.textEdit = textEdit;
    }
}