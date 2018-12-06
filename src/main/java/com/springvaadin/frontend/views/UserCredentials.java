package com.springvaadin.frontend.views;

import com.springvaadin.service.AccessService;
import com.springvaadin.model.User;
import com.vaadin.annotations.Theme;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.PersistenceException;

@SpringUI(path = "/userInfo")
@Theme("valo")
public class UserCredentials extends UI {

    @Autowired
    private AccessService accessService;

    private VerticalLayout vertical;
    private HorizontalLayout horizontal;
    private FormLayout editPassword;
    private FormLayout editCredential;
    private VerticalLayout info;

    private User user;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        if (accessService.fetchAllRegisteredUser().isEmpty())
            getUI().getPage().setLocation("/");
        else if (!accessService.fetchAllRegisteredUser().get(0).isLoggedIn())
            getUI().getPage().setLocation("/");
        else {
            user = accessService.fetchAllRegisteredUser().get(0);

            setup();
            head();
            info();
            infoform();
            passform();
            format();
        }
    }

    private void setup() {
        Page.getCurrent().setTitle("Información de usuario");

        vertical = new VerticalLayout();
        horizontal = new HorizontalLayout();
        vertical.setSpacing(true);
        vertical.setMargin(true);
        horizontal.setSpacing(true);
        horizontal.setMargin(true);
        vertical.setSizeFull();
        vertical.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        horizontal.setSizeFull();
        horizontal.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        setContent(vertical);
    }

    private void head() {
        HorizontalLayout spacingLayout = new HorizontalLayout();

        spacingLayout.setSpacing(true);
        spacingLayout.setMargin(true);

        Label header = new Label("Perfil de" + user.getName() + " " + user.getLastname());
        header.addStyleName(ValoTheme.LABEL_H3);
        header.setSizeUndefined();

        Button logOut = new Button("LogOut");
        Button calendar = new Button("Calendario");

        logOut.addStyleName(ValoTheme.BUTTON_PRIMARY);
        logOut.setIcon(FontAwesome.XING);

        calendar.addStyleName(ValoTheme.BUTTON_PRIMARY);
        calendar.setIcon(FontAwesome.CALENDAR);

        spacingLayout.addComponents(header, logOut, calendar);

        logOut.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    User user = accessService.fetchAllRegisteredUser().get(0);

                    user.setLoggedIn(false);

                    accessService.editUser(user);
                } catch (PersistenceException exp) {
                    //
                } catch (NullPointerException exp) {
                    //
                } catch (Exception exp) {
                    //
                }

                getUI().getPage().setLocation("/");
            }
        });

        calendar.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getUI().getPage().setLocation("/calendar");
            }
        });

        vertical.addComponent(spacingLayout);
    }

    private void info() {
        info = new VerticalLayout();

        Label email = new Label("Email: " + user.getEmail());
        email.addStyleName(ValoTheme.LABEL_H4);
        Label name = new Label("Nombre: " + user.getName() + " " + user.getLastname());
        name.addStyleName(ValoTheme.LABEL_H4);
        info.addComponents(email, name);

        horizontal.addComponent(info);
    }

    private void passform() {
        editPassword = new FormLayout();

        Label title = new Label("Cambiar clave");
        title.addStyleName(ValoTheme.LABEL_H3);

        PasswordField oldPassword = new PasswordField("Clave:");
        PasswordField newPassword = new PasswordField("Clave nueva:");
        PasswordField confirm = new PasswordField("Confirmar clave nueva: ");
        Button submit = new Button("Aceptar");

        submit.addStyleName(ValoTheme.BUTTON_PRIMARY);
        submit.setIcon(FontAwesome.PLUS);

        editPassword.addComponents(oldPassword, newPassword, confirm, submit);

        submit.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (oldPassword.getValue().equals(user.getPassword()))
                    if (newPassword.getValue().equals(confirm.getValue())) {
                        try {
                            user.setPassword(newPassword.getValue());
                            accessService.editUser(user);
                        } catch (PersistenceException exp) {
                            //
                        } catch (NullPointerException exp) {
                            //
                        } catch (Exception exp) {
                            //
                        }

                        getUI().getPage().setLocation("/");
                    } else
                        getUI().getPage().setLocation("/userInfo"); // TODO: add error exceptions
                else
                    getUI().getPage().setLocation("/userInfo"); // TODO: add error exceptions
            }
        });

        submit.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        horizontal.addComponent(editPassword);
    }

    private void infoform() {
        editCredential = new FormLayout();

        Label title = new Label("Información básica");
        title.addStyleName(ValoTheme.LABEL_H3);

        TextField newEmail = new TextField("Email nuevo:");
        TextField newFirstName = new TextField("Nuevo nombre:");
        TextField newLastName = new TextField("Nuevo apellido: ");
        PasswordField password = new PasswordField("Clave:");
        Button submit = new Button("Aceptar");

        submit.addStyleName(ValoTheme.BUTTON_PRIMARY);
        submit.setIcon(FontAwesome.PLUS);

        editCredential.addComponents(newEmail, newFirstName, newLastName, password, submit);

        submit.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (password.getValue().equals(user.getPassword())) {
                    try {
                        if (!newEmail.getValue().equals(""))
                            user.setEmail(newEmail.getValue());

                        if (!newFirstName.getValue().equals(""))
                            user.setName(newFirstName.getValue());

                        if (!newLastName.getValue().equals(""))
                            user.setLastname(newLastName.getValue());

                        accessService.editUser(user);
                    } catch (PersistenceException exp) {
                        //
                    } catch (NullPointerException exp) {
                        //
                    } catch (Exception exp) {
                        //
                    }

                    getUI().getPage().setLocation("/userInfo");
                } else
                    getUI().getPage().setLocation("/userInfo"); // TODO: add error exceptions
            }
        });

        submit.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        horizontal.addComponent(editCredential);
    }

    private void format() {
        vertical.addComponent(horizontal);
    }
}
