package com.practica.springvaadin.frontend.views;

import com.practica.springvaadin.Services.AccessService;
import com.practica.springvaadin.Services.EventService;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

@SpringUI(path = "/calendar")
@Theme("valo")
public class Mainview extends UI {

    @Autowired
    private AccessService accessService;
    @Autowired
    public EventService eventService;

    @Autowired
    private EventModal eventModal;
    private SenderModal senderModal;

    private VerticalLayout verticalLayout = new VerticalLayout();
    public static Calendar calendar = new Calendar();

    @Autowired
    public Mainview(EventModal event)
    {
        this.eventModal = event;
    }

    @Override
    protected void init(VaadinRequest request) {

    }
}
