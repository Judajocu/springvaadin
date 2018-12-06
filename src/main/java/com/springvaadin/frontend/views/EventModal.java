package com.springvaadin.frontend.views;

import com.springvaadin.service.EventService;
import com.springvaadin.model.CustomEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@UIScope
@SpringUI
public class EventModal extends FormLayout {

    @Autowired
    EventService eventService;

    DateField start = new PopupDateField("Fecha de inicio");
    DateField end = new PopupDateField("Fecha de termino");

    TextField subject = new TextField("Asunto");
    TextArea contenido = new TextArea("Contenido");

    Button submit = new Button("Agregar");
    Button cancel = new Button("Cancelar");

    public EventModal(Date startDate, Date endDate) {
        start.setValue(startDate);
        end.setValue(endDate);
        setup();
    }

    public EventModal() {
        start.setValue(new Date());
        end.setValue(new Date());
        setup();

    }

    private void setup() {

        setSizeUndefined();
        setMargin(true);
        setSpacing(true);
        start.setResolution(Resolution.MINUTE);
        end.setResolution(Resolution.MINUTE);

        submit.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                CustomEvent e = new CustomEvent(subject.getValue(), contenido.getValue(), false, start.getValue(), end.getValue());
                try {
                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                    eventService.registerEvent(e.getCaption(), e.getDescription(), false, sdf1.parse(e.getStart().toString()), sdf1.parse(e.getEnd().toString()));

                } catch (Exception exp) {

                }
                MainView.calendar.addEvent(e);

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

        HorizontalLayout buttons = new HorizontalLayout(submit, cancel);
        buttons.setSpacing(true);
        cancel.setClickShortcut(ShortcutAction.KeyCode.X);

        start.setCaption("Inicio:");
        end.setCaption("Fin:");
        subject.setCaption("Asunto:");
        contenido.setCaption("Contenido:");

        addComponents(subject, contenido, start, end, buttons);
    }

    public void setDates(Date startDate, Date endDate) {
        start.setValue(startDate);
        end.setValue(endDate);
    }


}
