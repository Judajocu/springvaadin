package com.practica.springvaadin.frontend.views;

import com.sendgrid.*;
import com.vaadin.event.ShortcutAction;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.stereotype.Component;

@Component
@UIScope
@SpringUI
public class SenderModal extends FormLayout {

    TextField from = new TextField("De parte:");
    TextField to = new TextField("A:");
    TextField caption = new TextField("Asunto");
    TextArea description = new TextArea("Contenido");
    Button submit = new Button("Enviar");
    Button cancel = new Button("Cancel");

    public SenderModal(String email)
    {
        to.setValue(email);
        setup();
    }

    private void setup()
    {
        setSizeUndefined();
        setMargin(true);
        setSpacing(true);

        submit.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Email Efrom = new Email(from.getValue());
                String subject = caption.getValue();
                Email Eto = new Email(to.getValue());
                Content content = new Content("text/plain", description.getValue());
                Mail mail = new Mail(Efrom,subject,Eto,content);

                SendGrid sendGrid = new SendGrid(System.getenv("SENDGRID_API_KEY"));
                Request request = new Request();
                try {
                    request.method = Method.POST;
                    request.endpoint = "mail/send";
                    request.body = mail.build();
                    Response response = sendGrid.api(request);
                    System.out.println(response.statusCode);
                    System.out.println(response.body);
                    System.out.println(response.headers);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
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

        from.setCaption("De parte:");
        from.setValue("test@test.com");
        to.setCaption("A:");
        caption.setCaption("Asunto:");
        description.setCaption("Contenido");

        addComponents(from,to,caption,description,buttons);
    }
}
