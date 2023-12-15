package com.ukma.cleaning.notification;

import com.ukma.cleaning.order.OrderEntity;
import com.ukma.cleaning.utils.mails.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository repository;
    private final MailService mailService;

    @Override
    public Long create(OrderEntity order) {
        NotificationEntity notification = new NotificationEntity();
        notification.setOrder(order);
        return repository.save(notification).getId();
    }

    @Override
    public void sendNotifications() {
        List<NotificationEntity> notificationsToSend = repository.findAll();
        notificationsToSend.stream()
                .map(NotificationEntity::getOrder)
                .forEach(mailService::sendOrderCreationMail);
        repository.deleteAll(notificationsToSend);
    }
}
