package com.springvaadin.frontend.views;

import com.springvaadin.service.AccessService;
import com.springvaadin.service.EventService;
import com.springvaadin.model.CustomEvent;
import com.springvaadin.model.User;
import com.vaadin.annotations.Theme;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.components.calendar.CalendarComponentEvents;
import com.vaadin.ui.components.calendar.event.EditableCalendarEvent;
import com.vaadin.ui.components.calendar.handler.BasicEventMoveHandler;
import com.vaadin.ui.components.calendar.handler.BasicEventResizeHandler;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.PersistenceException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

@SpringUI(path = "/calendar")
@Theme("valo")
public class MainView extends UI {

    @Autowired
    private AccessService accessService;
    @Autowired
    public EventService eventService;


    @Autowired
    private EventModal eventModal;
    private SenderModal SenderModal;


    private VerticalLayout vertical = new VerticalLayout();
    public static Calendar calendar = new Calendar();

    @Autowired
    public MainView(EventModal eventMod) {
        this.eventModal = eventMod;
    }

    @Override
    protected void init(VaadinRequest request) {

        if (accessService.fetchAllRegisteredUser().isEmpty())
            getUI().getPage().setLocation("/");
        else if (!accessService.fetchAllRegisteredUser().get(0).isLoggedIn())
            getUI().getPage().setLocation("/");
        else {
            setup();
            head();
            addCalendar();
            eventModal = new EventModal();
            SenderModal = new SenderModal(accessService.fetchAllRegisteredUser().get(0).getEmail());
            form();
        }
    }

    public void setup() {
        Page.getCurrent().setTitle("Vaadin-Spring Vaadin-Calendar");

        vertical = new VerticalLayout();
        vertical.setSpacing(true);
        vertical.setMargin(true);
        vertical.setSizeFull();
        vertical.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        setContent(vertical);

    }

    private void form() {

        HorizontalLayout horizontal = new HorizontalLayout();

        horizontal.setSpacing(true);
        horizontal.setMargin(true);

        Button submit = new Button("Agregar evento");
        Button send = new Button("Enviar email");
        Button logOut = new Button("LogOut");
        Button viewInfo = new Button("Información usuario");

        send.addStyleName(ValoTheme.BUTTON_PRIMARY);
        send.setIcon(FontAwesome.ENVELOPE);
        submit.addStyleName(ValoTheme.BUTTON_PRIMARY);
        submit.setIcon(FontAwesome.PLUS);
        logOut.addStyleName(ValoTheme.BUTTON_PRIMARY);
        logOut.setIcon(FontAwesome.XING);
        ;
        viewInfo.addStyleName(ValoTheme.BUTTON_PRIMARY);
        viewInfo.setIcon(FontAwesome.ARCHIVE);

        if (!eventService.findAll().isEmpty())
            setUpButtonModalView(submit, "Agregar evento nuevo (" + eventService.findAll().size() + ")", eventModal);
        else
            setUpButtonModalView(submit, "Agregar evento nuevo", eventModal);
        setUpButtonModalView(send, "Enviar email", SenderModal);

        horizontal.addComponents(submit, send, logOut, viewInfo);

        logOut.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    User user = accessService.fetchAllRegisteredUser().get(0);
                    user.setLoggedIn(false);
                    accessService.editUser(user);
                } catch (PersistenceException ex) {
                    //
                } catch (NullPointerException ex) {
                    //
                } catch (Exception ex) {
                    //
                }
                getUI().getPage().setLocation("/");
            }
        });

        viewInfo.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getUI().getPage().setLocation("/userInfo");
            }
        });

        vertical.addComponent(horizontal);

        submit.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    }

    public void head() {
        Label header = new Label("KLK Calendar");
        header.addStyleName(ValoTheme.LABEL_H1);
        header.setSizeUndefined();
        vertical.addComponent(header);
    }

    public void addCalendar() {

        Button submit = new Button("Agregar");
        submit.setIcon(FontAwesome.PLUS);
        submit.setStyleName(ValoTheme.BUTTON_PRIMARY);

        calendar.setFirstDayOfWeek(1);
        calendar.setTimeFormat(Calendar.TimeFormat.Format12H);

        calendar.setWeeklyCaptionFormat("yyyy-MM-dd");
        calendar.setFirstVisibleDayOfWeek(1);
        calendar.setLastVisibleDayOfWeek(7);
        calendar.setSizeFull();
        calendar.setHeight("100%");


        calendar.setHandler(new CalendarComponentEvents.EventClickHandler() {
            @Override
            public void eventClick(CalendarComponentEvents.EventClick event) {
                CustomEvent e = (CustomEvent) event.getCalendarEvent();

                new Notification("Evento clickeado: " + e.getCaption(), e.getDescription()).show(Page.getCurrent());
            }
        });


        calendar.setHandler(new BasicEventMoveHandler() {
            private java.util.Calendar javaCalendar;

            public void eventMove(CalendarComponentEvents.MoveEvent event) {
                javaCalendar = event.getComponent().getInternalCalendar();
                super.eventMove(event);
            }

            protected void setDates(EditableCalendarEvent event,
                                    Date start, Date end) {
                CustomEvent e = (CustomEvent) event;
                e.setStart(start);
                e.setEnd(end);
                eventService.registerEvent(e.getCaption(), e.getDescription(), e.isAllDay(), e.getStart(), e.getEnd());
            }
        });

        calendar.setHandler(new BasicEventResizeHandler() {
            protected void setDates(EditableCalendarEvent event,
                                    Date start, Date end) {
                CustomEvent e = (CustomEvent) event;
                e.setStart(start);
                e.setEnd(end);
                eventService.registerEvent(e.getCaption(), e.getDescription(), e.isAllDay(), e.getStart(), e.getEnd());
            }
        });
        calendar.setHandler(new CalendarComponentEvents.RangeSelectHandler() {
            @Override
            public void rangeSelect(CalendarComponentEvents.RangeSelectEvent event) {
                eventModal.setDates(event.getStart(), event.getEnd());
                openModalView("Agregar evento nuevo", eventModal);
            }
        });


        calendar.setLocale(Locale.US);
        calendar.setStartDate(new GregorianCalendar().getTime());
        GregorianCalendar calEnd = new GregorianCalendar();
        calEnd.set(java.util.Calendar.DATE, 1);
        calEnd.roll(java.util.Calendar.DATE, -1);
        calendar.setEndDate(calEnd.getTime());
        calendar.setTimeFormat(Calendar.TimeFormat.Format12H);
        calendar.setFirstVisibleDayOfWeek(1);
        calendar.setLastVisibleDayOfWeek(7);
        calendar.setFirstVisibleHourOfDay(6);
        calendar.setLastVisibleHourOfDay(20);
        calendar.setSizeFull();

        vertical.addComponent(calendar);
        vertical.setExpandRatio(calendar, 1.0f);

    }

    private void openModalView(String title, FormLayout form) {
        Window modalView = new Window(title);
        modalView.center();
        modalView.setResizable(false);
        modalView.setModal(true);
        modalView.setClosable(true);
        modalView.setDraggable(false);
        modalView.setContent(form);

        addWindow(modalView);
    }

    private void setUpButtonModalView(Button button, String title, FormLayout form) {
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                openModalView(title, form);
            }
        });
    }

}
