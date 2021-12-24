package com.ujjwalkumar.eplacements.models;

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
}
