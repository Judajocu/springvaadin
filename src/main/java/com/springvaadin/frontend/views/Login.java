package com.springvaadin.frontend.views;

import com.springvaadin.service.AccessService;
import com.springvaadin.model.User;
import com.vaadin.annotations.Theme;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.PersistenceException;

@SpringComponent
@SpringUI(path = "/")
@Theme("valo")
public class Login extends UI {

    @Autowired
    private AccessService accessService;

    private VerticalLayout vertical = new VerticalLayout();

    @Override
    protected void init(VaadinRequest request) {
        if (!accessService.fetchAllRegisteredUser().isEmpty() && accessService.fetchAllRegisteredUser().get(0).isLoggedIn())
            getUI().getPage().setLocation("/calendar");
        else {
            setup();
            head();
            form();
        }
    }

    public void setup() {
        Page.getCurrent().setTitle("Login");

        vertical = new VerticalLayout();
        vertical.setSpacing(true);
        vertical.setMargin(true);
        vertical.setSizeFull();
        vertical.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        setContent(vertical);

    }

    private void form() {

        TextField email = new TextField("Email");

        PasswordField pass = new PasswordField("Clave");

        TextField name = new TextField("Nombre");

        TextField lastName = new TextField("Apellido");

        Button submit = accessService.fetchAllRegisteredUser().isEmpty() ? new Button("Registar") : new Button("Login");

        submit.addStyleName(ValoTheme.BUTTON_PRIMARY);
        submit.setIcon(FontAwesome.PLUS);

        if (accessService.fetchAllRegisteredUser().isEmpty())
            vertical.addComponents(email, name, lastName, pass, submit);
        else
            vertical.addComponents(email, pass, submit);

        submit.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (accessService.fetchAllRegisteredUser().isEmpty()) {
                    try {
                        accessService.registerUser(email.getValue(), name.getValue(), lastName.getValue(), pass.getValue());
                        getUI().getPage().setLocation("/");
                    } catch (PersistenceException exp) {
                        //
                    } catch (NullPointerException exp) {
                        //
                    } catch (Exception exp) {
                        //
                    }
                } else {
                    if (accessService.validateUserCredentials(email.getValue(), pass.getValue())) {
                        try {
                            User user = accessService.fetchAllRegisteredUser().get(0);

                            user.setLoggedIn(true);

                            accessService.editUser(user);

                            getUI().getPage().setLocation("/calendar");
                        } catch (PersistenceException exp) {
                            //
                        } catch (NullPointerException exp) {
                            //
                        } catch (Exception exp) {
                            //
                        }
                    } else
                        getUI().getPage().setLocation("/");
                }
            }
        });

        submit.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    }

    public void head() {
        Label header = accessService.fetchAllRegisteredUser().isEmpty() ? new Label("Registre una cuenta") : new Label("Login");
        header.addStyleName(ValoTheme.LABEL_H3);
        header.setSizeUndefined();
        vertical.addComponent(header);
    }
}
