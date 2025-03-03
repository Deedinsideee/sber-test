package ru.alexandr.sbertest.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.alexandr.sbertest.dtos.OldSubscriptionDto;
import ru.alexandr.sbertest.model.Book;
import ru.alexandr.sbertest.model.LegacySubscription;
import ru.alexandr.sbertest.model.Subscription;
import ru.alexandr.sbertest.model.SubscriptionBook;
import ru.alexandr.sbertest.repository.BookRepository;
import ru.alexandr.sbertest.repository.LegacySubscriptionRepository;
import ru.alexandr.sbertest.repository.SubscriptionBookRepository;
import ru.alexandr.sbertest.repository.SubscriptionRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.alexandr.sbertest.BaseTestClass.createBook;
import static ru.alexandr.sbertest.BaseTestClass.createSubscription;

@ExtendWith(MockitoExtension.class)
public class LibraryServiceTest {
    @InjectMocks
    private LibraryService service;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private LegacySubscriptionRepository legacySubscriptionRepository;
    @Mock
    private SubscriptionBookRepository subscriptionBookRepository;


    @Test
    void importSubscriptionsSuccess() {
        LegacySubscription dto = createLegacySubscription();

        List<LegacySubscription> dtos = List.of(dto);
        when(legacySubscriptionRepository.findFirstUnprocessed(any())).thenReturn(dtos);
        when(subscriptionRepository.findByUsernameIn(anySet())).thenReturn(Collections.emptyList());
        when(bookRepository.findByNameAndAuthorIn(anySet(), anySet())).thenReturn(Collections.emptyList());

        service.importSubscriptions();

        ArgumentCaptor<List<SubscriptionBook>> subscriptionBookCaptor = ArgumentCaptor.forClass(List.class);
        verify(subscriptionBookRepository).saveAll(subscriptionBookCaptor.capture());
        assertEquals(1, subscriptionBookCaptor.getValue().size());
    }

    @Test
    void importSubscriptionsExistingSubscriptionAndNoBook() {
        LegacySubscription dto = createLegacySubscription();

        List<LegacySubscription> dtos = List.of(dto);

        Subscription existingSubscription = createSubscription("user1");

        when(legacySubscriptionRepository.findFirstUnprocessed(any())).thenReturn(dtos);
        when(subscriptionRepository.findByUsernameIn(anySet())).thenReturn(List.of(existingSubscription));
        when(bookRepository.findByNameAndAuthorIn(anySet(), anySet())).thenReturn(Collections.emptyList());

        service.importSubscriptions();


        ArgumentCaptor<List<SubscriptionBook>> subscriptionBookCaptor = ArgumentCaptor.forClass(List.class);
        verify(subscriptionBookRepository).saveAll(subscriptionBookCaptor.capture());
        assertEquals(1, subscriptionBookCaptor.getValue().size());
    }

    @Test
    void importSubscriptionsExistingBookAndNoBookSubscription() {
        LegacySubscription dto = createLegacySubscription();

        List<LegacySubscription> dtos = List.of(dto);

        Book existingBook = createBook();

        when(legacySubscriptionRepository.findFirstUnprocessed(any())).thenReturn(dtos);
        when(subscriptionRepository.findByUsernameIn(anySet())).thenReturn(Collections.emptyList());
        when(bookRepository.findByNameAndAuthorIn(anySet(), anySet())).thenReturn(List.of(existingBook));

        service.importSubscriptions();


        ArgumentCaptor<List<SubscriptionBook>> subscriptionBookCaptor = ArgumentCaptor.forClass(List.class);
        verify(subscriptionBookRepository).saveAll(subscriptionBookCaptor.capture());
        assertEquals(1, subscriptionBookCaptor.getValue().size());
    }


    @Test
    void importSubscriptionsSubAlreadyExist() {
        LegacySubscription dto = createLegacySubscription();

        List<LegacySubscription> dtos = List.of(dto);

        Subscription existingSubscription = createSubscription("user1");

        Book existingBook = createBook();

        SubscriptionBook existingSubscriptionBook = new SubscriptionBook();
        existingSubscriptionBook.setSubscription(existingSubscription);
        existingSubscriptionBook.setBook(existingBook);

        existingSubscription.setBooks(new ArrayList<>(List.of(existingSubscriptionBook)));

        when(legacySubscriptionRepository.findFirstUnprocessed(any())).thenReturn(dtos);
        when(subscriptionRepository.findByUsernameIn(anySet())).thenReturn(List.of(existingSubscription));
        when(bookRepository.findByNameAndAuthorIn(anySet(), anySet())).thenReturn(List.of(existingBook));

        service.importSubscriptions();

        verify(subscriptionBookRepository, never()).saveAll(anyList());
    }


    private LegacySubscription createLegacySubscription() {
        LegacySubscription dto = new LegacySubscription();
        dto.setUsername("user1");
        dto.setUserFullName("Test User");
        dto.setUserActive(true);
        dto.setBookName("Test Book");
        dto.setBookAuthor("Test Author");
        return dto;
    }


}
