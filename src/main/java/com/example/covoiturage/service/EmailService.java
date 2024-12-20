package com.example.covoiturage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;
    //@Value("${emails.sender_name}")
    //private String senderName;

    public void sendConfirmationEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("PathMates <" + senderEmail + ">");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            javaMailSender.send(message);
            System.out.println("Email sent correctly to: " + to);
        } catch (MailException e) {
            // Log the error
            System.err.println("Failed to send email to: " + to);
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            // Handle any other exceptions
            System.err.println("An unexpected error occurred while sending email to: " + to);
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
