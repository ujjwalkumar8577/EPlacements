package com.ujjwalkumar.eplacements.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Experience {
    String year, reg_no, student_name, company_name, job_profile, job_location, desc;
    int rating;
    long timestamp;

    public Experience(String year, String reg_no, String student_name, String company_name, String job_profile, String job_location, String desc, int rating, long timestamp) {
        this.year = year;
        this.reg_no = reg_no;
        this.student_name = student_name;
        this.company_name = company_name;
        this.job_profile = job_profile;
        this.job_location = job_location;
        this.desc = desc;
        this.rating = rating;
        this.timestamp = timestamp;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getReg_no() {
        return reg_no;
    }

    public void setReg_no(String reg_no) {
        this.reg_no = reg_no;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getJob_profile() {
        return job_profile;
    }

    public void setJob_profile(String job_profile) {
        this.job_profile = job_profile;
    }

    public String getJob_location() {
        return job_location;
    }

    public void setJob_location(String job_location) {
        this.job_location = job_location;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimeString() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
    }
}
