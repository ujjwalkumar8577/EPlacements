package com.ujjwalkumar.eplacements.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Notice {
    String title, content;
    Long timestamp;

    public Notice(String title, String content, Long timestamp) {
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
