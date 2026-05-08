package com.example.Wime_java.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.internet.MimeMessage;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendMassEmail(List<String> toEmails, String subject, String messageBody) throws Exception {
        for (String to : toEmails) {
            sendEmail(to, subject, messageBody);
        }
    }

        public void sendPasswordRecoveryEmail(String to, String subject, String messageBody, String actionUrl) throws Exception {
        sendEmail(to, subject, messageBody, actionUrl, "Cambiar contraseña");
    }

    private void sendEmail(String to, String subject, String messageBody) throws Exception {

        sendEmail(to, subject, messageBody, "http://localhost:8080/", "Abrir en Wime");
    }

    private void sendEmail(String to, String subject, String messageBody, String actionUrl, String actionText) throws Exception {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        Context context = new Context();
        context.setVariable("titulo", subject);
        context.setVariable("mensaje", messageBody);

        context.setVariable("actionUrl", actionUrl);
        context.setVariable("actionText", actionText);

        String htmlContent = templateEngine.process("email-template", context);


        helper.setText(htmlContent, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom("scrumm516@gmail.com");
        
        //ClassPathResource logo = new ClassPathResource("../static/images/wimeLogo.png");
        //helper.addInline("wimeLogo", logo);

        mailSender.send(mimeMessage);
        System.out.println("📨 Enviado a: " + to);
    }
}
