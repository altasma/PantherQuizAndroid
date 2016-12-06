package com.aclass.panther.uwm.pantheractive;

/**
 * Created by Asmamaw on 10/27/16.
 */

public class ClassRoomModel {
    private String id;
    private String department;
    private String name;
    private String school;
    private String term;
    private String year;

    public ClassRoomModel() {

    }


    public ClassRoomModel(String id, String department, String name, String school, String term, String year) {
        this.id = id;
        this.department = department;
        this.name = name;
        this.school = school;
        this.term = term;
        this.year = year;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return
//                "id='" + id + '\'' +
//                ", department='" + department + '\'' +
//                ", name='" + name + '\'' +
//                ", school='" + school + '\'' +
//                ", term='" + term + '\'' +
//                ", year='" + year ;
                name;

    }

    public String print() {
        return id + " " + name;
    }
}


