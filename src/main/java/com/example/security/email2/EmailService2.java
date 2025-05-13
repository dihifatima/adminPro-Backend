package com.example.security.email2;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailService2 {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    public void sendEmail(String nom, String email, String sujet) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            // Utiliser sujet au lieu de message pour être cohérent
            Context context = new Context();
            context.setVariable("contact", new ContactInfo(nom, email, sujet));

            // Utiliser l'énumération corrigée
            String htmlContent = templateEngine.process(EmailTemplateName2.EnvoyerContact.getName(), context);

            helper.setFrom(email);
            helper.setTo("fatifaty715@gmail.com");
            helper.setSubject("Nouveau message de " + nom);
            helper.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'e-mail : " + e.getMessage());
        }
    }

    // Mettre à jour la classe interne pour utiliser sujet
    public record ContactInfo(String nom, String email, String message) {}

}