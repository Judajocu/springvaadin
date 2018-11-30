package com.practica.springvaadin.frontend.views;

import com.practica.springvaadin.Models.User;
import com.practica.springvaadin.Services.AccessService;
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

@SpringUI(path = "/")
@Theme("valo")
public class Login extends UI {

    @Autowired
    private AccessService accessService;

    private VerticalLayout verticalLayout = new VerticalLayout();

    @Override
    protected void init(VaadinRequest request) {
        if (!accessService.fetchAllRegisteredUser().isEmpty() && accessService.fetchAllRegisteredUser().get(0).isLooged())
        {
            getUI().getPage().setLocation("/calendar");
        }
        else
        {
            setup();
            Head();
            Form();
        }
    }

    public void setup()
    {
        Page.getCurrent().setTitle("Login");

        verticalLayout = new VerticalLayout();
        verticalLayout.setSpacing(true);
        verticalLayout.setMargin(true);
        verticalLayout.setSizeFull();
        verticalLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        setContent(verticalLayout);
    }

    private void Form()
    {
        TextField email = new TextField("Email");
        PasswordField pass = new PasswordField("Clave");
        TextField name = new TextField("Nombre");
        TextField lastname = new TextField("Apellido");
        Button submit = accessService.fetchAllRegisteredUser().isEmpty() ? new Button("Registrar") : new Button("Login");

        submit.addStyleName(ValoTheme.BUTTON_PRIMARY);
        submit.setIcon(FontAwesome.PLUS);

        if (accessService.fetchAllRegisteredUser().isEmpty())
            verticalLayout.addComponents(email,pass,name,lastname, submit);
        else
            verticalLayout.addComponents(email,pass,submit);

        submit.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (accessService.fetchAllRegisteredUser().isEmpty())
                {
                    try {
                        accessService.registerUser(email.getValue(), pass.getValue(), name.getValue(), pass.getValue());
                        getUI().getPage().setLocation("/");
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
                }
                else
                {
                    if (accessService.ValidateUser(email.getValue(), pass.getValue()))
                    {
                        try {
                            User user = accessService.fetchAllRegisteredUser().get(0);
                            user.setLooged(true);
                            accessService.editUser(user);
                            getUI().getPage().setLocation("calendar");
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
                    }
                    else
                        getUI().getPage().setLocation("/");
                }
            }
        });

        submit.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    }

    public void Head()
    {
        Label head = accessService.fetchAllRegisteredUser().isEmpty() ? new Label("Registre una cuenta de usuario") : new Label("Acceda a su cuenta");
        head.addStyleName(ValoTheme.LABEL_H3);
        head.setSizeUndefined();
        verticalLayout.addComponent(head);
    }
}
