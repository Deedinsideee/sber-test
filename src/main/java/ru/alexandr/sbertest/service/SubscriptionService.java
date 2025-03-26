package ru.alexandr.sbertest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alexandr.sbertest.dtos.SubscriptionByUsernameDto;
import ru.alexandr.sbertest.mapper.LibraryMapper;
import ru.alexandr.sbertest.model.Subscription;
import ru.alexandr.sbertest.model.SubscriptionPayment;
import ru.alexandr.sbertest.repository.SubscriptionBookRepository;
import ru.alexandr.sbertest.repository.SubscriptionPaymentRepository;
import ru.alexandr.sbertest.repository.SubscriptionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final LibraryMapper libraryMapper;
    private final NotificationService notificationService;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionBookRepository subscriptionBookRepository;
    private final SubscriptionPaymentRepository subscriptionPaymentRepository;

    public SubscriptionByUsernameDto findSubByNameSurName(String name) {
        return libraryMapper.toSubscriptionDto(subscriptionRepository.findByUserFullNameLike(name).orElseThrow());
    }

    @Scheduled(cron = "${notification.schedule}")
    public void checkOverdueSubscriptions() {
        LocalDate cutoffDate = LocalDate.now().minusDays(20);
        List<Subscription> overdueSubscriptions = subscriptionBookRepository.findOverdueSubscriptions(cutoffDate);
        if (!overdueSubscriptions.isEmpty()) {
            overdueSubscriptions.forEach(notificationService::sendNotification);
        }
    }

    public void paySubscription(UUID subscriptionId, BigDecimal amount) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        SubscriptionPayment payment = new SubscriptionPayment();
        payment.setSubscription(subscription);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setAmount(amount);
        payment.setStatus("PAID");  // Статус оплаты

        subscription.setNextPaymentDate(LocalDateTime.now().plusMonths(1));

        subscriptionPaymentRepository.save(payment);

        // Обновляем статус подписки на оплаченный
        subscription.setActive(true);
        subscriptionRepository.save(subscription);
    }

}
