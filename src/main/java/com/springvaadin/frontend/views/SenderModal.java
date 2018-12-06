package com.springvaadin.frontend.views;

import com.sendgrid.*;
import com.vaadin.event.ShortcutAction;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@UIScope
@SpringUI
public class SenderModal extends FormLayout {

    TextField From = new TextField("De:");
    TextField To = new TextField("Para:");
    TextField Subject = new TextField("Asunto");
    TextArea description = new TextArea("Contenido");

    Button submit = new Button("Enviar");
    Button cancelBtn = new Button("Cancel");


    public SenderModal(String email) {
        To.setValue(email);
        setup();

    }

    private void setup() {

        setSizeUndefined();
        setMargin(true);
        setSpacing(true);

        submit.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Email from = new Email(From.getValue());
                String subject = Subject.getValue();
                Email to = new Email(To.getValue());
                Content content = new Content("text/plain", description.getValue());
                Mail mail = new Mail(from, subject, to, content);

                SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_ KEY"));
                Request request = new Request();
                try {
                    request.method = Method.POST;
                    request.endpoint = "mail/send";
                    request.body = mail.build();
                    Response response = sg.api(request);
                    System.out.println(response.statusCode);
                    System.out.println(response.body);
                    System.out.println(response.headers);
                } catch (IOException ex) {
                    //throw ex;
                }
                ((Window) getParent()).close();
            }
        });
        submit.setClickShortcut(ShortcutAction.KeyCode.ENTER);


        cancelBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                ((Window) getParent()).close();
            }
        });
        cancelBtn.setClickShortcut(ShortcutAction.KeyCode.X);

        HorizontalLayout buttons = new HorizontalLayout(submit, cancelBtn);
        buttons.setSpacing(true);

        From.setCaption("De:");
        From.setValue("test@test.com");
        To.setCaption("Para:");

        Subject.setCaption("Asunto:");
        description.setCaption("Contenido:");

        addComponents(From, To, Subject, description, buttons);
    }
}
