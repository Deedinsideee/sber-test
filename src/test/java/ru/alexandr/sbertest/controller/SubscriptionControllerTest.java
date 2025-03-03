package ru.alexandr.sbertest.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.alexandr.sbertest.dtos.UserFullNameRequest;
import ru.alexandr.sbertest.service.SubscriptionService;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SubscriptionControllerTest {

    @Mock
    private SubscriptionService service;

    @InjectMocks
    private SubscriptionController subscriptionController;

    @Test
    void findByUserFullNam() {
        UserFullNameRequest request = new UserFullNameRequest();
        request.setUserFullName("Тестовый Пользователь");

        assertDoesNotThrow(() -> subscriptionController.findByUserFullName(request));

        verify(service, times(1)).findSubByNameSurName("Тестовый Пользователь");
    }

    @Test
    void checkOverdueSubscriptions() {
        assertDoesNotThrow(() -> subscriptionController.findByUserFullName());

        verify(service, times(1)).checkOverdueSubscriptions();
    }

    @Test
    void handleSubscriptionNotFound_ShouldReturnBadRequestMessage() {
        NoSuchElementException exception = new NoSuchElementException();

        String response = subscriptionController.handleSubscriptionNotFound(exception).getBody();

        assertEquals("Данный пользователь не найден", response);
    }
}
