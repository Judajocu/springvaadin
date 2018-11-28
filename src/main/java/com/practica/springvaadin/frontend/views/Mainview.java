package com.practica.springvaadin.frontend.views;

import com.practica.springvaadin.Services.AccessService;
import com.practica.springvaadin.Services.EventService;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;

@SpringUI(path = "/calendar")
@Theme("valo")
public class Mainview extends UI {

    @Autowired
    private AccessService accessService;
    @Autowired
    public EventService eventService;



    @Override
    protected void init(VaadinRequest request) {

    }
}
