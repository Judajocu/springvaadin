package com.practica.springvaadin.frontend.views;

import com.practica.springvaadin.Models.User;
import com.practica.springvaadin.Services.AccessService;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import sun.security.util.PendingException;

import javax.persistence.PersistenceException;

public class Userview extends UI {

    @Autowired
    private AccessService accessService;

    private VerticalLayout verticalLayout;
    private HorizontalLayout horizontalLayout;
    private FormLayout editcredentials;
    private FormLayout editGralInfo;
    private VerticalLayout infoLayout;

    private User user;
    @Override
    protected void init(VaadinRequest request) {
        if(accessService.fetchAllRegisteredUser().isEmpty())
        {
            getUI().getPage().setLocation("/");
        }
        else if (!accessService.fetchAllRegisteredUser().isEmpty())
        {
            getUI().getPage().setLocation("/");
        }
        else
        {
            user = accessService.fetchAllRegisteredUser().get(0);

            setup();
            addHeader();
            userInfo();
            infoForm();
            PassForm();
            formatLayout();
        }
    }

    private void setup()
    {
        Page.getCurrent().setTitle("Información Personal");

        verticalLayout = new VerticalLayout();
        horizontalLayout = new HorizontalLayout();
        verticalLayout.setSpacing(true);
        verticalLayout.setMargin(true);
        horizontalLayout.setSpacing(true);
        horizontalLayout.setMargin(true);
        verticalLayout.setSizeFull();
        verticalLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        horizontalLayout.setSizeFull();
        horizontalLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        setContent(verticalLayout);
    }

    private void addHeader()
    {
        HorizontalLayout layout = new HorizontalLayout();

        layout.setSpacing(true);
        layout.setMargin(true);

        Label head = new Label("Perfil de "+user.getfullName());
        head.addStyleName(ValoTheme.LABEL_H2);
        head.setSizeUndefined();

        Button logOut = new Button("LogOut");
        Button calendar = new Button("Calendario");

        logOut.addStyleName(ValoTheme.BUTTON_PRIMARY);
        calendar.addStyleName(ValoTheme.BUTTON_PRIMARY);

        logOut.setIcon(FontAwesome.XING);
        calendar.setIcon(FontAwesome.XING);

        layout.addComponents(head, logOut, calendar);

        logOut.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    User user = accessService.fetchAllRegisteredUser().get(0);

                    user.setLooged(false);

                    accessService.editUser(user);
                } catch (PersistenceException e)
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

        calendar.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getUI().getPage().setLocation("/calendar");
            }
        });

        verticalLayout.addComponent(layout);
    }

    private void userInfo()
    {
        infoLayout = new VerticalLayout();

        Label email = new Label("Correo: "+ user.getEmail());
        Label name = new Label("Nombre: " + user.getfullName());

        email.addStyleName(ValoTheme.LABEL_H4);
        name.addStyleName(ValoTheme.LABEL_H4);

        infoLayout.addComponents(email, name);

        horizontalLayout.addComponent(infoLayout);
    }

    private void PassForm()
    {
        editcredentials = new FormLayout();

        Label title = new Label("Cambio de clave");
        title.addStyleName(ValoTheme.LABEL_H3);

        PasswordField previousPass = new PasswordField("Clave antigua:");
        PasswordField newPass = new PasswordField("Clave nueva:");
        PasswordField confirmPass = new PasswordField("Confirmar clave nueva:");
        Button submit = new Button("Enviar");

        submit.addStyleName(ValoTheme.BUTTON_PRIMARY);
        submit.setIcon(FontAwesome.PLUS);

        editcredentials.addComponents(previousPass, newPass, confirmPass, submit);

        submit.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (previousPass.getValue().equals(user.getPassword()))
                {
                    if (newPass.getValue().equals(confirmPass.getValue()))
                    {
                        try {
                            user.setPassword(newPass.getValue());
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
                    } else
                        getUI().getPage().setLocation("/userInfo");
                } else
                    getUI().getPage().setLocation("/userInfo");
            }
        });

        submit.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        horizontalLayout.addComponent(editcredentials);
    }

    private void infoForm()
    {
        editGralInfo = new FormLayout();

        Label title = new Label("Cambio de información");
        title.addStyleName(ValoTheme.LABEL_H3);

        TextField newEmail = new TextField("Email nuevo");
        TextField newName = new TextField("Nombre nuevo");
        TextField newLastname = new TextField("Apellido nuevo");
        PasswordField pass = new PasswordField("Clave:");
        Button submit = new Button("Enviar");

        submit.addStyleName(ValoTheme.BUTTON_PRIMARY);
        submit.setIcon(FontAwesome.PLUS);

        editGralInfo.addComponents(newEmail, newName, newLastname, pass, submit);
        submit.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (pass.getValue().equals(user.getPassword()))
                {
                    try {
                        if (!newEmail.getValue().equals(""))
                        {
                            user.setEmail(newEmail.getValue());
                        }
                        if (!newName.getValue().equals(""))
                        {
                            user.setFirstname(newName.getValue());
                        }
                        if (!newLastname.getValue().equals(""))
                        {
                            user.setLastname(newLastname.getValue());
                        }
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

                    getUI().getPage().setLocation("/userInfo");
                }else
                    getUI().getPage().setLocation("/userInfo");
            }
        });

        submit.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        horizontalLayout.addComponent(editGralInfo);
    }

    private void formatLayout()
    {
        verticalLayout.addComponent(horizontalLayout);
    }
}
