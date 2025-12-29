package com.plateforme.dons.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendActivationEmail(String toEmail, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Activation de votre compte Plateforme-Dons");
        message.setText("Bienvenue ! Veuillez cliquer sur le lien ci-dessous pour activer votre compte :\n" +
                "http://localhost:8080/api/auth/activate?token=" + token);
        message.setFrom("noreply@plateforme-dons.com");

        mailSender.send(message);
    }

    @Async
    public void sendNotificationEmail(String toEmail, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(content);
        message.setFrom("noreply@plateforme-dons.com");

        mailSender.send(message);
    }
}
