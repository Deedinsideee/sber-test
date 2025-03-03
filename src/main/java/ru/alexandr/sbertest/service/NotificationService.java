package ru.alexandr.sbertest.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.alexandr.sbertest.model.Subscription;

//Сервис заглушка для отправки уведомлений
@Service
@Slf4j
public class NotificationService {

    public void sendNotification(Subscription subscription) {
        log.info("Уведомление пользователю: {} о просроченной книге", subscription.getUserFullName());
    }
}
