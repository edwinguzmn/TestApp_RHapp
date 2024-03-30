package com.apck.proyectfx.model;

public class LogEvents {
    public String text1;
    public String username_LogEvent;
    public String text2;
    public String date_LogEvent;

    public LogEvents(String text1, String username_LogEvent, String text2, String date_LogEvent) {
        this.text1 = text1;
        this.username_LogEvent = username_LogEvent;
        this.text2 = text2;
        this.date_LogEvent = date_LogEvent;
    }

    public LogEvents(){

    }

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getusername_LogEvent() {
        return username_LogEvent;
    }

    public void setusername_LogEvent(String username_LogEvent) {
        this.username_LogEvent = username_LogEvent;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public String getdate_LogEvent() {
        return date_LogEvent;
    }

    public void setdate_LogEvent(String date_LogEvent) {
        date_LogEvent = date_LogEvent;
    }
}
