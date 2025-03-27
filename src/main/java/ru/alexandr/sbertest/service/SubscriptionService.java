package ru.alexandr.sbertest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.alexandr.sbertest.dtos.SubscriptionByUsernameDto;
import ru.alexandr.sbertest.mapper.LibraryMapper;
import ru.alexandr.sbertest.model.Subscription;
import ru.alexandr.sbertest.repository.SubscriptionBookRepository;
import ru.alexandr.sbertest.repository.SubscriptionRepository;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final LibraryMapper libraryMapper;
    private final NotificationService notificationService;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionBookRepository subscriptionBookRepository;

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
}
