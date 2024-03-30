package com.apck.proyectfx.model;

import androidx.annotation.NonNull;

public class Event_model {

    private String event_date;
    private String event_hourStart;
    private String event_hourFinish;
    private String event_name;
    private String event_desc;
    private String eventid;
    private String month;
    private String day;

    public Event_model(String event_date, String event_hourStart, String event_hourFinish, String event_name, String event_desc, String eventid, String month, String day) {
        this.event_date = event_date;
        this.event_hourStart = event_hourStart;
        this.event_hourFinish = event_hourFinish;
        this.event_name = event_name;
        this.event_desc = event_desc;
        this.eventid = eventid;
        this.month = month;
        this.day = day;
    }

    public Event_model(){

    }

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public String getEvent_hourStart() {
        return event_hourStart;
    }

    public void setEvent_hourStart(String event_hourStart) {
        this.event_hourStart = event_hourStart;
    }

    public String getEvent_hourFinish() {
        return event_hourFinish;
    }

    public void setEvent_hourFinish(String event_hourFinish) {
        this.event_hourFinish = event_hourFinish;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_desc() {
        return event_desc;
    }

    public void setEvent_desc(String event_desc) {
        this.event_desc = event_desc;
    }

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
