package com.practica.springvaadin.frontend.views;

import com.practica.springvaadin.Models.CustomEvent;
import com.practica.springvaadin.Models.User;
import com.practica.springvaadin.Services.AccessService;
import com.practica.springvaadin.Services.EventService;
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
        if (accessService.fetchAllRegisteredUser().isEmpty())
        {
            getUI().getPage().setLocation("/");
        }
        else if (!accessService.fetchAllRegisteredUser().get(0).isLooged())
        {
            getUI().getPage().setLocation("/");
        }
        else
        {
            setup();
            Head();
            Calendar();
            eventModal = new EventModal();
            senderModal = new SenderModal(accessService.fetchAllRegisteredUser().get(0).getEmail());
            Form();
        }
    }

    public void setup()
    {
        Page.getCurrent().setTitle("Vaadin Calendario");

        verticalLayout = new VerticalLayout();
        verticalLayout.setSpacing(true);
        verticalLayout.setMargin(true);
        verticalLayout.setSizeFull();
        verticalLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        setContent(verticalLayout);
    }

    private void Form()
    {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        horizontalLayout.setSpacing(true);
        horizontalLayout.setMargin(true);

        Button nEvent = new Button("Agregar evento");
        Button email = new Button("Enviar email");
        Button logout = new Button("Log out");
        Button info = new Button("Información de usuario");

        email.addStyleName(ValoTheme.BUTTON_PRIMARY);
        email.setIcon(FontAwesome.ENVELOPE_O);
        nEvent.addStyleName(ValoTheme.BUTTON_PRIMARY);
        nEvent.setIcon(FontAwesome.PLUS);
        logout.addStyleName(ValoTheme.BUTTON_PRIMARY);
        logout.setIcon(FontAwesome.XING);
        info.addStyleName(ValoTheme.BUTTON_PRIMARY);
        info.setIcon(FontAwesome.NAVICON);

        if (!eventService.findAll().isEmpty())
        {
            setUpButtonModelView(nEvent, "Agregar evento (" + eventService.findAll().size() + ")", eventModal);
        }
        else
        {
            setUpButtonModelView(nEvent, "Agregar evento", eventModal);
        }
        setUpButtonModelView(email, "Enviar email", senderModal);
        horizontalLayout.addComponents(nEvent, email, logout, info);

        logout.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    User user = accessService.fetchAllRegisteredUser().get(0);
                    user.setLooged(false);
                    accessService.editUser(user);
                }
                catch (PersistenceException e)
                {
                    e.printStackTrace();
                }
                catch (NullPointerException e)
                {
                    e.printStackTrace();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                getUI().getPage().setLocation("/");
            }
        });

        info.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getUI().getPage().setLocation("/userInfo");
            }
        });

        verticalLayout.addComponent(horizontalLayout);

        nEvent.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    }

    public void Head()
    {
        Label header = new Label("KLK Calendar");
        header.addStyleName(ValoTheme.LABEL_HUGE);
        header.setSizeUndefined();
        verticalLayout.addComponent(header);
    }

    public void Calendar()
    {
        Button addB = new Button("Agregar");
        addB.setIcon(FontAwesome.PLUS);
        addB.setStyleName(ValoTheme.BUTTON_PRIMARY);

        calendar.setFirstDayOfWeek(1);
        calendar.setTimeFormat(Calendar.TimeFormat.Format12H);
        calendar.setWeeklyCaptionFormat("dd-MM-yyyy");
        calendar.setLastVisibleDayOfWeek(7);
        calendar.setFirstVisibleDayOfWeek(1);
        calendar.setSizeFull();
        calendar.setHeight("100%");

        calendar.setHandler(new CalendarComponentEvents.EventClickHandler() {
            @Override
            public void eventClick(CalendarComponentEvents.EventClick event) {
                CustomEvent customEvent = (CustomEvent) event.getCalendarEvent();

                new Notification("Event clicked: "+ customEvent.getCaption(), customEvent.getDescription()).show(Page.getCurrent());
            }
        });

        calendar.setHandler(new BasicEventMoveHandler()
        {
            private java.util.Calendar JCalendar;

            public void eventMove(CalendarComponentEvents.MoveEvent moveEvent)
            {
                JCalendar = moveEvent.getComponent().getInternalCalendar();
                super.eventMove(moveEvent);
            }

            protected void setDates(EditableCalendarEvent calendarEvent, Date startD, Date endD)
            {
                CustomEvent customEvent = (CustomEvent) calendarEvent;
                customEvent.setStart(startD);
                customEvent.setEnd(endD);
                eventService.registerEvent(customEvent.getCaption(), customEvent.getDescription(), customEvent.isAllDay(), customEvent.getStart(), customEvent.getEnd());
            }
        });

        calendar.setHandler(new BasicEventResizeHandler()
        {
            protected void setDates(EditableCalendarEvent calendarEvent, Date startD, Date endD)
            {
                CustomEvent customEvent = (CustomEvent) calendarEvent;
                customEvent.setStart(startD);
                customEvent.setEnd(endD);
                eventService.registerEvent(customEvent.getCaption(), customEvent.getDescription(), customEvent.isAllDay(), customEvent.getStart(), customEvent.getEnd());
            }
        });
        calendar.setHandler(new CalendarComponentEvents.RangeSelectHandler() {
            @Override
            public void rangeSelect(CalendarComponentEvents.RangeSelectEvent event) {
                eventModal.setDates(event.getStart(), event.getEnd());
                openModalView("Agregar un evento nuevo", eventModal);
            }
        });
         calendar.setLocale(Locale.US);
         calendar.setStartDate(new GregorianCalendar().getTime());
         GregorianCalendar GCalendar = new GregorianCalendar();
         GCalendar.set(java.util.Calendar.DATE, 1);
         GCalendar.roll(java.util.Calendar.DATE, -1);
         calendar.setEndDate(GCalendar.getTime());
         calendar.setTimeFormat(Calendar.TimeFormat.Format12H);
         calendar.setFirstVisibleDayOfWeek(1);
         calendar.setLastVisibleDayOfWeek(7);
         calendar.setFirstVisibleHourOfDay(8);
         calendar.setLastVisibleHourOfDay(22);
         calendar.setSizeFull();

         verticalLayout.addComponent(calendar);
         verticalLayout.setExpandRatio(calendar, 1.0f);
    }

    private void openModalView(String title, FormLayout formLayout)
    {
        Window modalView = new Window(title);
        modalView.center();
        modalView.setResizable(false);
        modalView.setModal(true);
        modalView.setClosable(true);
        modalView.setDraggable(true);
        modalView.setContent(formLayout);

        addWindow(modalView);
    }

    private void setUpButtonModelView(Button button, String title, FormLayout formLayout)
    {
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                openModalView(title,formLayout);
            }
        });
    }
}
