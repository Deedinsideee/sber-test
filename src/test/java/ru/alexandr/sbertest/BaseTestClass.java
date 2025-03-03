package ru.alexandr.sbertest;

import ru.alexandr.sbertest.model.Book;
import ru.alexandr.sbertest.model.Subscription;

import java.util.ArrayList;

public class BaseTestClass {

    public static Subscription createSubscription(String username) {
        return Subscription.builder()
                .username(username)
                .userFullName("Mock username")
                .active(true)
                .books(new ArrayList<>())
                .build();
    }

    public static Book createBook() {
        return Book.builder()
                .title("Test Book")
                .author("Test Author")
                .subscriptions(new ArrayList<>())
                .build();
    }

    public static Book createBookWithNames(String title, String author) {
        return Book.builder()
                .title(title)
                .author(author)
                .subscriptions(new ArrayList<>())
                .build();
    }
}
