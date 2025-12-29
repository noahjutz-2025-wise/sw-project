package com.swdev.springbootproject.service;

import com.swdev.springbootproject.entity.CbUser;
import com.swdev.springbootproject.entity.EmailVerification;
import com.swdev.springbootproject.repository.EmailVerificationRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class EmailService {
  private final JavaMailSender javaMailSender;
  private final EmailVerificationRepository emailVerificationRepository;

  @Value("${spring.mail.username:#{null}}")
  private String mailFrom;

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

  public void sendVerificationEmail(CbUser cbUser) {
    String token = UUID.randomUUID().toString();
    EmailVerification emailVerification =
        new EmailVerification(token, LocalDateTime.now().plusMinutes(30), cbUser);
    emailVerificationRepository.save(emailVerification);
    sendVerificationEmail(cbUser.getEmail(), token);
  }
}
