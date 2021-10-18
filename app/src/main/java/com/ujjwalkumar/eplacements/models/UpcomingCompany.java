package com.ujjwalkumar.eplacements.models;

public class UpcomingCompany {
    String id, name, profile, ctc, deadline;

    public UpcomingCompany(String id, String name, String profile, String ctc, String deadline) {
        this.id = id;
        this.name = name;
        this.profile = profile;
        this.ctc = ctc;
        this.deadline = "Deadline: " + deadline;
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

    public String getCtc() {
        return ctc;
    }

    public void setCtc(String ctc) {
        this.ctc = ctc;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}
