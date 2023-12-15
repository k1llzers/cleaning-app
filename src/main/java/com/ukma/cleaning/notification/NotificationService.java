package com.ukma.cleaning.notification;

import com.ukma.cleaning.order.OrderEntity;

public interface NotificationService {
    Long create(OrderEntity order);

    void sendNotifications();
}
