package ru.alexandr.sbertest.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.alexandr.sbertest.model.Subscription;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.alexandr.sbertest.BaseTestClass.createSubscription;

@DataJpaTest
public class SubscriptionRepositoryTest {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Test
    void findByUserFullNameLike() {
        String fullName = "Иван Иванов";
        Subscription subscription = createSubscription(fullName);
        subscription.setUserFullName(fullName);
        subscriptionRepository.save(subscription);

        Optional<Subscription> foundSubscription = subscriptionRepository.findByUserFullNameLike("иван иванов");

        assertTrue(foundSubscription.isPresent());
        assertEquals(fullName, foundSubscription.get().getUserFullName());
    }

    @Test
    void findByUserFullNameLikeEmpty() {
        Optional<Subscription> foundSubscription = subscriptionRepository.findByUserFullNameLike("Петр Петров");

        assertFalse(foundSubscription.isPresent());
    }

    @Test
    void findByUsernameIn() {
        String username1 = "ivan_ivanov";
        Subscription sub1 = createSubscription(username1);

        String username2 = "petr_petrov";
        Subscription sub2 = createSubscription(username2);


        subscriptionRepository.saveAll(List.of(sub1, sub2));

        Set<String> usernames = Set.of("ivan_ivanov", "petr_petrov");
        List<Subscription> foundSubscriptions = subscriptionRepository.findByUsernameIn(usernames);

        assertEquals(2, foundSubscriptions.size());
        assertTrue(foundSubscriptions.stream().anyMatch(s -> s.getUsername().equals(username1)));
        assertTrue(foundSubscriptions.stream().anyMatch(s -> s.getUsername().equals(username2)));
    }

    @Test
    void findByUsernameInEmpty() {
        Set<String> usernames = Set.of("non");
        List<Subscription> foundSubscriptions = subscriptionRepository.findByUsernameIn(usernames);

        assertTrue(foundSubscriptions.isEmpty());
    }
}
