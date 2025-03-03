package ru.alexandr.sbertest;

import ru.alexandr.sbertest.model.Book;
import ru.alexandr.sbertest.model.Subscription;

import java.util.ArrayList;

public class BaseTestClass {

    public static Subscription createSubscription(String username) {
        Subscription newSub = new Subscription();
        newSub.setUsername(username);
        newSub.setUserFullName("Mock username");
        newSub.setActive(Boolean.TRUE);
        newSub.setBooks(new ArrayList<>());

        return newSub;
    }

    public static Book createBook() {
        Book existingBook = new Book();
        existingBook.setTitle("Test Book");
        existingBook.setAuthor("Test Author");
        return existingBook;
    }

    public static Book createBookWithNames(String title, String author) {
        Book existingBook = new Book();
        existingBook.setTitle(title);
        existingBook.setAuthor(author);
        return existingBook;
    }
}
