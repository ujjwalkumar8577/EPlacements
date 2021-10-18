package com.ujjwalkumar.eplacements.models;

public class RegisteredCompany {
    String id, name, profile, ctc, timestamp;

    public RegisteredCompany(String id, String name, String profile, String ctc, String timestamp) {
        this.id = id;
        this.name = name;
        this.profile = profile;
        this.ctc = ctc;
        this.timestamp = "Registered on: " + timestamp;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
