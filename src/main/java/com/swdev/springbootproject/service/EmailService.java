package com.swdev.springbootproject.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class EmailService {
  private final JavaMailSender javaMailSender;

  @Value("${spring.mail.username}")
  private String mailFrom;

  public EmailService(JavaMailSender javaMailSender) {
    this.javaMailSender = javaMailSender;
  }

  public void sendVerificationEmail(String email, String verificationToken) {
    String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    String subject = "Verify Your Email Address";
    String confirmationUrl = baseUrl + "/verify?token=" + verificationToken;

    String message =
        "Hi there, \n\n"
            + "Welcome to Certified Bangers! Thanks for signing up.\n\n"
            + "To complete your registration, please verify your email address and confirm your email address by clicking the link below:\n\n"
            + confirmationUrl
            + "\n\n"
            + "This link will stay active for 30 minutes.\n\n"
            + "The Certified Bangers Team";

    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setTo(email);
    mailMessage.setSubject(subject);
    mailMessage.setText(message);
    mailMessage.setFrom(mailFrom);

    javaMailSender.send(mailMessage);
  }
}
