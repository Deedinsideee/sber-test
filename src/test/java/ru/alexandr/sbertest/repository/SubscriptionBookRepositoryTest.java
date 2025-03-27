package ru.alexandr.sbertest.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.alexandr.sbertest.model.Book;
import ru.alexandr.sbertest.model.Subscription;
import ru.alexandr.sbertest.model.SubscriptionBook;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.alexandr.sbertest.BaseTestClass.createBook;
import static ru.alexandr.sbertest.BaseTestClass.createSubscription;

@DataJpaTest
public class SubscriptionBookRepositoryTest {

    @Autowired
    private SubscriptionBookRepository subscriptionBookRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private BookRepository bookRepository;


    @Test
    void findOverdueSubscriptions() {
        LocalDate cutoffDate = LocalDate.now();

        Subscription subscription = createSubscription("test");
        subscriptionRepository.save(subscription);

        Book book = createBook();
        bookRepository.save(book);

        SubscriptionBook overdueSubscription = new SubscriptionBook();
        overdueSubscription.setSubscription(subscription);
        overdueSubscription.setBook(book);
        overdueSubscription.setIssueDate(cutoffDate.minusDays(10)); // выдана 10 дней назад
        overdueSubscription.setReturnDate(null);
        subscriptionBookRepository.save(overdueSubscription);

        List<Subscription> overdueSubscriptions = subscriptionBookRepository.findOverdueSubscriptions(cutoffDate);

        assertFalse(overdueSubscriptions.isEmpty());
        assertEquals(1, overdueSubscriptions.size());
        assertEquals(subscription.getId(), overdueSubscriptions.get(0).getId());
    }


    @Test
    void findOverdueSubscriptionsReturnNull() {
        LocalDate cutoffDate = LocalDate.now().minusDays(10);

        Subscription subscription = createSubscription("test");
        subscriptionRepository.save(subscription);

        Book book = createBook();
        bookRepository.save(book);

        SubscriptionBook overdueSubscription = new SubscriptionBook();
        overdueSubscription.setSubscription(subscription);
        overdueSubscription.setBook(book);
        overdueSubscription.setIssueDate(LocalDate.now()); // выдана сегодня
        overdueSubscription.setReturnDate(null);
        subscriptionBookRepository.save(overdueSubscription);

        List<Subscription> overdueSubscriptions = subscriptionBookRepository.findOverdueSubscriptions(cutoffDate);

        assertTrue(overdueSubscriptions.isEmpty());
        assertEquals(0, overdueSubscriptions.size());
    }

}
