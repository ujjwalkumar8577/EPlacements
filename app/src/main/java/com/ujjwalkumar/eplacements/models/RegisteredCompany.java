package com.ujjwalkumar.eplacements.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RegisteredCompany {
    String id, name, profile;
    double ctc;
    long deadline;

    public RegisteredCompany(String id, String name, String profile, double ctc, long deadline) {
        this.id = id;
        this.name = name;
        this.profile = profile;
        this.ctc = ctc;
        this.deadline = deadline;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public double getCtc() {
        return ctc;
    }

    public void setCtc(double ctc) {
        this.ctc = ctc;
    }

    public long getDeadline() {
        return deadline;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

    public String getTimeString() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(deadline);
        return "Registered on : " + new SimpleDateFormat("dd/MM/yyyy hh:mm a").format(cal.getTime());
    }
}
