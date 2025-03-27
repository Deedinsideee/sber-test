package ru.alexandr.sbertest.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.alexandr.sbertest.dtos.SubscriptionByUsernameDto;
import ru.alexandr.sbertest.mapper.LibraryMapper;
import ru.alexandr.sbertest.model.Subscription;
import ru.alexandr.sbertest.repository.SubscriptionBookRepository;
import ru.alexandr.sbertest.repository.SubscriptionRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static ru.alexandr.sbertest.BaseTestClass.createSubscription;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceTest {
    @InjectMocks
    private SubscriptionService service;

    @Mock
    private LibraryMapper libraryMapper;
    @Mock
    private NotificationService notificationService;
    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private SubscriptionBookRepository subscriptionBookRepository;


    @Test
    void findSubByNameReturnDto() {
        String fullName = "Иванов Иван";
        Subscription subscription = createSubscription(fullName);
        SubscriptionByUsernameDto expectedDto = new SubscriptionByUsernameDto();
        expectedDto.setActive(Boolean.TRUE);
        expectedDto.setUserFullName("Иванов Иван");

        when(subscriptionRepository.findByUserFullNameLike(fullName))
                .thenReturn(Optional.of(subscription));
        when(libraryMapper.toSubscriptionDto(subscription)).thenReturn(expectedDto);

        SubscriptionByUsernameDto actualDto = service.findSubByNameSurName(fullName);

        assertNotNull(actualDto);
        assertEquals(expectedDto.getUserFullName(), actualDto.getUserFullName());

        verify(subscriptionRepository, times(1)).findByUserFullNameLike(fullName);
        verify(libraryMapper, times(1)).toSubscriptionDto(subscription);
    }

    @Test
    void findSubByNameThrowEx() {
        // Подготовка данных
        String fullName = "Петр Петров";

        when(subscriptionRepository.findByUserFullNameLike(fullName))
                .thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.findSubByNameSurName(fullName));

        verify(subscriptionRepository, times(1)).findByUserFullNameLike(fullName);
        verifyNoInteractions(libraryMapper);
    }


    @Test
    void checkOverdueSubscriptionsSendNotification() {
        Subscription sub1 = createSubscription("1");
        Subscription sub2 = createSubscription("2");
        List<Subscription> overdueSubscriptions = List.of(sub1, sub2);
        when(subscriptionBookRepository.findOverdueSubscriptions(any())).thenReturn(overdueSubscriptions);

        service.checkOverdueSubscriptions();

        verify(notificationService, times(1)).sendNotification(sub1);
        verify(notificationService, times(1)).sendNotification(sub2);
    }

    @Test
    void checkOverdueSubscriptions_NotSendNotifications() {
        when(subscriptionBookRepository.findOverdueSubscriptions(any())).thenReturn(Collections.emptyList());

        service.checkOverdueSubscriptions();

        verify(notificationService, never()).sendNotification(any());
    }

    @Test
    void checkOverdueSubscriptionsWithCorrectDate() {
        LocalDate expectedCutoffDate = LocalDate.now().minusDays(20);
        when(subscriptionBookRepository.findOverdueSubscriptions(any())).thenReturn(Collections.emptyList());

        service.checkOverdueSubscriptions();

        ArgumentCaptor<LocalDate> dateCaptor = ArgumentCaptor.forClass(LocalDate.class);
        verify(subscriptionBookRepository).findOverdueSubscriptions(dateCaptor.capture());

        assertEquals(expectedCutoffDate, dateCaptor.getValue());
    }

}
