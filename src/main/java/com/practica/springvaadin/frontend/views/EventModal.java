package com.practica.springvaadin.frontend.views;

import com.practica.springvaadin.Models.CustomEvent;
import com.practica.springvaadin.Services.EventService;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.nio.ch.SelectorImpl;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@UIScope
@SpringUI
public class EventModal extends FormLayout {

    @Autowired
    EventService eventService;

    DateField start = new PopupDateField("Fecha de inicio");
    DateField end = new PopupDateField("Fecha de temrino");

    TextField subject = new TextField("Asunto");
    TextArea description = new TextArea("Contenido");
    Button submit = new Button("Agregar");
    Button cancel = new Button("Cancelar");

    public EventModal(Date startD, Date endD)
    {
        start.setValue(startD);
        end.setValue(endD);

    }

    public EventModal()
    {
        start.setValue(new Date());
        end.setValue(new Date());

    }
    private void setup()
    {
        setSizeUndefined();
        setMargin(true);
        setSpacing(true);
        start.setResolution(Resolution.MINUTE);
        end.setResolution(Resolution.MINUTE);
         submit.addClickListener(new Button.ClickListener() {
             @Override
             public void buttonClick(Button.ClickEvent event) {
                 CustomEvent customEvent = new CustomEvent(subject.getValue(),description.getValue(),false, start.getValue(), end.getValue());
                 try {
                     SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                     eventService.registerEvent(customEvent.getCaption(),customEvent.getDescription(), false, format.parse(customEvent.getStart().toString()), format.parse(customEvent.getEnd().toString()));
                 }
                 catch (Exception e)
                 {
                     e.printStackTrace();
                 }
                 Mainview.calendar.addEvent(customEvent);
                 ((Window) getParent()).close();
             }
         });
         submit.setClickShortcut(ShortcutAction.KeyCode.ENTER);

         cancel.addClickListener(new Button.ClickListener() {
             @Override
             public void buttonClick(Button.ClickEvent event) {
                 ((Window) getParent()).close();
             }
         });

         HorizontalLayout buttons = new HorizontalLayout(submit,cancel);
         buttons.setSpacing(true);

         start.setCaption("Fecha inicio");
         end.setCaption("Fecha termino");
         subject.setCaption("Asunto:");
         description.setCaption("Contenido");

         addComponents(subject, description, start, end);
    }

    public void setDates(Date startD, Date endD)
    {
        start.setValue(startD);
        end.setValue(endD);
    }
}
