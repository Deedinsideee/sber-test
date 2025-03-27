package ru.alexandr.sbertest.service;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alexandr.sbertest.model.Book;
import ru.alexandr.sbertest.model.LegacySubscription;
import ru.alexandr.sbertest.model.Subscription;
import ru.alexandr.sbertest.model.SubscriptionBook;
import ru.alexandr.sbertest.repository.BookRepository;
import ru.alexandr.sbertest.repository.LegacySubscriptionRepository;
import ru.alexandr.sbertest.repository.SubscriptionBookRepository;
import ru.alexandr.sbertest.repository.SubscriptionRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LibraryService {

    private final BookRepository bookRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final LegacySubscriptionRepository legacySubscriptionRepository;
    private final SubscriptionBookRepository subscriptionBookRepository;

    @Value("${batch.size.processing:500}")
    private Integer batchSizeProcessing;


    public Page<Book> getBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Optional<Book> getBookById(UUID id) {
        return bookRepository.findById(id);
    }

    public void saveLegacySubscription(List<LegacySubscription> dto) {

        legacySubscriptionRepository.saveAll(dto);
    }

    @Scheduled(cron = "${batch.size.processing.schedule}")
    @Transactional
    public void importSubscriptions() {
        log.info("Запущена задача по сохранению подписок и книг");
        List<LegacySubscription> dtos = legacySubscriptionRepository.findFirstUnprocessed(batchSizeProcessing);
        if (dtos == null || dtos.isEmpty()) {
            log.info("Не найдено данных для сохранения");
            return;
        }
        Set<String> usernames = dtos.stream().map(LegacySubscription::getUsername).collect(Collectors.toSet());

        Map<String, Subscription> existingSubscriptions = subscriptionRepository.findByUsernameIn(usernames)
                .stream().collect(Collectors.toMap(Subscription::getUsername, sub -> sub));

        Set<String> names = dtos.stream().map(LegacySubscription::getBookName).collect(Collectors.toSet());
        Set<String> authors = dtos.stream().map(LegacySubscription::getBookAuthor).collect(Collectors.toSet());

        Map<String, Book> existingBooks = bookRepository.findByNameAndAuthorIn(names, authors)
                .stream().collect(Collectors.toMap(book -> book.getTitle() + "|" + book.getAuthor(), book -> book));

        List<SubscriptionBook> newSubscriptionBooks = new ArrayList<>();

        for (LegacySubscription dto : dtos) {
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
        for (LegacySubscription dto : dtos) {
            dto.setProcessed(true);
        }
    }

    private void createNewSubscriptionBooks(Subscription subscription, Book book, List<SubscriptionBook> newSubscriptionBooks) {
        SubscriptionBook subscriptionBook = SubscriptionBook.builder()
                .subscription(subscription)
                .book(book)
                .issueDate(LocalDate.now())
                .build();

        subscription.getBooks().add(subscriptionBook);
        book.getSubscriptions().add(subscriptionBook);
        newSubscriptionBooks.add(subscriptionBook);
    }

    private Subscription createSubscription(LegacySubscription dto, String username) {
        return Subscription.builder()
                .username(username)
                .userFullName(dto.getUserFullName())
                .active(dto.getUserActive())
                .books(new ArrayList<>())
                .build();
    }

    private Book createBook(LegacySubscription dto) {
        return Book.builder()
                .title(dto.getBookName())
                .author(dto.getBookAuthor())
                .publishedDate(LocalDate.now())
                .subscriptions(new ArrayList<>())
                .build();
    }

}
