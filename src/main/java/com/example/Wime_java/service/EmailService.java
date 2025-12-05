package com.example.Wime_java.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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

    private void sendEmail(String to, String subject, String messageBody) throws Exception {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        Context context = new Context();
        context.setVariable("titulo", subject);
        context.setVariable("mensaje", messageBody);

        String htmlContent = templateEngine.process("email-template", context);


        helper.setText(htmlContent, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom("mixagg6@gmail.com");
        
        //ClassPathResource logo = new ClassPathResource("../static/images/wimeLogo.png");
        //helper.addInline("wimeLogo", logo);

        mailSender.send(mimeMessage);
        System.out.println("📨 Enviado a: " + to);
    }
}
