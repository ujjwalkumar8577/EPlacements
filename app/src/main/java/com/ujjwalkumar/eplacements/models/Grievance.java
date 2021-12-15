package com.ujjwalkumar.eplacements.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Grievance {
    String id, reg_no, name, email, message, status;
    Long timestamp;

    public Grievance(String id, String reg_no, String name, String email, String message, String status, Long timestamp) {
        this.id = id;
        this.reg_no = reg_no;
        this.name = name;
        this.email = email;
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReg_no() {
        return reg_no;
    }

    public void setReg_no(String reg_no) {
        this.reg_no = reg_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimeString() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return new SimpleDateFormat("dd/MM/yyyy hh:mm a").format(cal.getTime());
    }
}
