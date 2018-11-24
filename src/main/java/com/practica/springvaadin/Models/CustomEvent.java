package com.practica.springvaadin.Models;

import java.util.Date;

public class CustomEvent {

    private int id;
    private String name;
    private String description;
    private Date start;
    private Date ends;
    private boolean entireday;
    private boolean notification;

    public CustomEvent(int id, String name, String description, Date start, Date ends, boolean entireday, boolean notification) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.start = start;
        this.ends = ends;
        this.entireday = entireday;
        this.notification = notification;
    }

    public CustomEvent() {
        this.id = id;
        this.name = name;
        this.description = description;
        this.start = start;
        this.ends = ends;
        this.entireday = entireday;
        this.notification = notification;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnds() {
        return ends;
    }

    public void setEnds(Date ends) {
        this.ends = ends;
    }

    public boolean isEntireday() {
        return entireday;
    }

    public void setEntireday(boolean entireday) {
        this.entireday = entireday;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }
}
