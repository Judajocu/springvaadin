package com.springvaadin.service;

import com.springvaadin.repository.EventRepository;
import com.springvaadin.model.CustomEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List<CustomEvent> findAll() {
        return eventRepository.findAll();
    }

    public List<CustomEvent> findAllByStartAndEndDates(Date start, Date end) {
        return eventRepository.findAllByStartAndEnd(start, end);
    }

    public List<CustomEvent> findBetween(Date startDate, Date endDate) {
        return eventRepository.findByDatesBetween(startDate, endDate);
    }

    @Transactional
    public CustomEvent registerEvent(String caption, String description, boolean isAllDay, Date start, Date end) {
        return eventRepository.save(new CustomEvent(caption, description, isAllDay, start, end));
    }

    @Transactional
    public boolean delete(CustomEvent customEvent) {
        eventRepository.delete(customEvent);
        return true;
    }

}