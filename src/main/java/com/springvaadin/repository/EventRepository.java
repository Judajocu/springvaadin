package com.springvaadin.repository;

import com.springvaadin.model.CustomEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;


public interface EventRepository extends JpaRepository<CustomEvent, Long> {
    List<CustomEvent> findAllByStartAndEnd(Date start, Date end);

    @Query("select e from CustomEvent e " +
            "where e.start between ?1 and ?2 and e.end between ?1 and ?2")
    List<CustomEvent> findByDatesBetween(Date startDate, Date endDate);
}
