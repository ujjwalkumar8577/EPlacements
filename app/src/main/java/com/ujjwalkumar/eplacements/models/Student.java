package com.ujjwalkumar.eplacements.models;

public class Student {
    String name, regNo, degreeCourse, status;

    public Student(String name, String regNo, String degreeCourse, String status) {
        this.name = name;
        this.regNo = regNo;
        this.degreeCourse = degreeCourse;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getDegreeCourse() {
        return degreeCourse;
    }

    public void setDegreeCourse(String degreeCourse) {
        this.degreeCourse = degreeCourse;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
