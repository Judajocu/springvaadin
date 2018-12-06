package com.springvaadin.model;

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
    private Long id;
    @Column
    @Size(min = 3, max = 50)
    private String caption;
    private String description;
    private String styleName;
    private boolean isAllDay;
    private boolean notified;
    @DateTimeFormat
    private Date start;
    @DateTimeFormat
    private Date end;

    @Transient
    private List<EventChangeListener> listeners = new ArrayList<EventChangeListener>();

    public CustomEvent() {
    }

    public CustomEvent(String caption, String description, boolean isAllDay, Date start, Date end) {
        this.setCaption(caption);
        this.setDescription(description);
        this.setAllDay(isAllDay);
        this.setNotified(false);
        this.setStart(start);
        this.setEnd(end);
    }

    @Override
    public void addEventChangeListener(EventChangeListener listener) {
        getListeners().add(listener);
    }

    @Override
    public void removeEventChangeListener(EventChangeListener listener) {
        getListeners().remove(listener);
    }

    @Override
    public void setCaption(String caption) {
        this.caption = caption;
        fireEventChange();
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
        fireEventChange();
    }

    @Override
    public void setEnd(Date end) {
        this.end = end;
        fireEventChange();
    }

    @Override
    public void setStart(Date start) {
        this.start = start;
    }

    @Override
    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    @Override
    public void setAllDay(boolean isAllDay) {
        this.isAllDay = isAllDay;
        fireEventChange();
    }

    @Override
    public Date getStart() {
        return start;
    }

    @Override
    public Date getEnd() {
        return end;
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getStyleName() {
        return styleName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean isAllDay() {
        return isAllDay;
    }

    public long getId() {
        return id;
    }


    protected void fireEventChange() {
        EventChangeEvent event = new EventChangeEvent(this);

        for (EventChangeListener listener : getListeners()) {
            listener.eventChange(event);
        }
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public List<EventChangeListener> getListeners() {
        return listeners;
    }

    public void setListeners(List<EventChangeListener> listeners) {
        this.listeners = listeners;
    }
}
