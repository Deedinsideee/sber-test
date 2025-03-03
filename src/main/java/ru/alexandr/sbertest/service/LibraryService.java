package ru.alexandr.sbertest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alexandr.sbertest.dtos.OldSubscriptionDto;
import ru.alexandr.sbertest.model.Book;
import ru.alexandr.sbertest.model.Subscription;
import ru.alexandr.sbertest.model.SubscriptionBook;
import ru.alexandr.sbertest.repository.BookRepository;
import ru.alexandr.sbertest.repository.SubscriptionBookRepository;
import ru.alexandr.sbertest.repository.SubscriptionRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LibraryService {

    private final BookRepository bookRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionBookRepository subscriptionBookRepository;


    @Transactional
    public void importSubscriptions(List<OldSubscriptionDto> dtos) {
        Set<String> usernames = dtos.stream().map(OldSubscriptionDto::getUsername).collect(Collectors.toSet());

        Map<String, Subscription> existingSubscriptions = subscriptionRepository.findByUsernameIn(usernames)
                .stream().collect(Collectors.toMap(Subscription::getUsername, sub -> sub));

        Set<String> names = dtos.stream().map(OldSubscriptionDto::getBookName).collect(Collectors.toSet());
        Set<String> authors = dtos.stream().map(OldSubscriptionDto::getBookAuthor).collect(Collectors.toSet());

        Map<String, Book> existingBooks = bookRepository.findByNameAndAuthorIn(names, authors)
                .stream().collect(Collectors.toMap(book -> book.getTitle() + "|" + book.getAuthor(), book -> book));

        List<SubscriptionBook> newSubscriptionBooks = new ArrayList<>();

        for (OldSubscriptionDto dto : dtos) {
            Subscription subscription = existingSubscriptions.computeIfAbsent(dto.getUsername(), username -> createSubscription(dto, username));

            String bookKey = dto.getBookName() + "|" + dto.getBookAuthor();
            Book book = existingBooks.computeIfAbsent(bookKey, key -> createBook(dto));
            boolean alreadyExists = subscription.getBooks().stream()
                    .anyMatch(sb -> sb.getBook().equals(book));
            if (!alreadyExists) {
                createNewSubscriptionBooks(subscription, book, newSubscriptionBooks);
            }
        }

        if (!newSubscriptionBooks.isEmpty()) {
            subscriptionBookRepository.saveAll(newSubscriptionBooks);
        }
    }

    private void createNewSubscriptionBooks(Subscription subscription, Book book, List<SubscriptionBook> newSubscriptionBooks) {
        SubscriptionBook subscriptionBook = new SubscriptionBook();
        subscriptionBook.setSubscription(subscription);
        subscriptionBook.setBook(book);
        subscriptionBook.setIssueDate(LocalDate.now());
        subscription.getBooks().add(subscriptionBook);
        book.getSubscriptions().add(subscriptionBook);

        newSubscriptionBooks.add(subscriptionBook);
    }

    private Subscription createSubscription(OldSubscriptionDto dto, String username) {
        Subscription newSub = new Subscription();
        newSub.setUsername(username);
        newSub.setUserFullName(dto.getUserFullName());
        newSub.setActive(dto.getUserActive());
        newSub.setBooks(new ArrayList<>());

        return newSub;
    }

    private Book createBook(OldSubscriptionDto dto) {
        Book newBook = new Book();
        newBook.setTitle(dto.getBookName());
        newBook.setAuthor(dto.getBookAuthor());
        newBook.setPublishedDate(LocalDate.now());

        return newBook;
    }


}
