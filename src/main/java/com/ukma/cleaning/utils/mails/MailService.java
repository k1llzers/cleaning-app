package com.ukma.cleaning.utils.mails;

import com.ukma.cleaning.order.OrderEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;

    public void sendOrderCreationMail(OrderEntity order) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(order.getClient().getEmail());
        mailMessage.setSubject("Your order");
        mailMessage.setText("Thank you for your order with number " + order.getId()
                + ". Our administrator will verify it. You can check status of your order, in our website "
                + ", orders tab.\n We hope you're having a great day!\n\n"
                + "Best regards,\n"
                + "The Spring Boot Cleaning Team");
        try {
            javaMailSender.send(mailMessage);
        } catch (MailException e) {
            log.error("Can`t send email for user: " + order.getClient().getId()
                    + ", for order with id: " + order.getId());
        }
    }

    public void sendOrderNotificationForUser(OrderEntity order) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(order.getClient().getEmail());
        mailMessage.setSubject("Notification");
        mailMessage.setText("Good morning, our team preparing to start your order " + order.getId()
                + ". Wait for our team at: " + order.getOrderTime().toLocalTime().format(DateTimeFormatter.ISO_LOCAL_TIME)
                + ".\n We hope you're having a great day!\n\n"
                + "Best regards,\n"
                + "The Spring Boot Cleaning Team");
        try {
            javaMailSender.send(mailMessage);
        } catch (MailException e) {
            log.error("Can`t send email for user: " + order.getClient().getId()
                    + ", for order with id: " + order.getId());
        }
    }
}
