package com.practica.springvaadin.Models;

import com.vaadin.ui.components.calendar.event.CalendarEvent;
import com.vaadin.ui.components.calendar.event.EditableCalendarEvent;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.persistence.*;
import javax.validation.constraints.Size;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@ApplicationScope
@Table(name = "event")
public class CustomEvent implements Serializable, CalendarEvent, EditableCalendarEvent, CalendarEvent.EventChangeNotifier {

    @Id
    @GeneratedValue
    private int id;
    @Column
    @Size(min = 5, max = 100)
    private String caption;
    private String description;
    private String styleName;
    @DateTimeFormat
    private Date end;
    @DateTimeFormat
    private Date start;

    private boolean isallDay;
    private boolean notified;

    @Transient
    private List<EventChangeListener> listeners = new ArrayList<EventChangeListener>();

    public CustomEvent(String caption, String description, Date end, Date start, String styleName, boolean isallDay) {
        this.caption = caption;
        this.description = description;
        this.end = end;
        this.start = start;
        this.styleName = styleName;
        this.isallDay = isallDay;
        this.setNotified(false);
    }

    public CustomEvent(String caption, String description, boolean isAllDay, Date start, Date end) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void setCaption(String caption) {

    }

    @Override
    public void setDescription(String description) {

    }

    @Override
    public void setEnd(Date end) {

    }

    @Override
    public void setStart(Date start) {

    }

    @Override
    public void setStyleName(String styleName) {

    }

    @Override
    public void setAllDay(boolean isAllDay) {

    }

    @Override
    public Date getStart() {
        return null;
    }

    @Override
    public Date getEnd() {
        return null;
    }

    @Override
    public String getCaption() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getStyleName() {
        return null;
    }

    @Override
    public boolean isAllDay() {
        return false;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    @Override
    public void addEventChangeListener(EventChangeListener listener) {

    }

    @Override
    public void removeEventChangeListener(EventChangeListener listener) {

    }
}
