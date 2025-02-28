package ru.alexandr.sbertest.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alexandr.sbertest.dtos.OldSubscriptionDto;
import ru.alexandr.sbertest.dtos.SubscriptionByUsernameDto;
import ru.alexandr.sbertest.mapper.LibraryMapper;
import ru.alexandr.sbertest.model.Book;
import ru.alexandr.sbertest.model.Subscription;
import ru.alexandr.sbertest.repository.BookRepository;
import ru.alexandr.sbertest.repository.SubscriptionRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LibraryService {
    private final SubscriptionRepository subscriptionRepository;
    private final BookRepository bookRepository;
    private final LibraryMapper libraryMapper;


    public SubscriptionByUsernameDto findSubByNameSurName(String name) {
        return libraryMapper.toSubscriptionDto(subscriptionRepository.findByUserFullNameLike(name).orElseThrow());
    }


    @Transactional
    public void importSubscriptions(List<OldSubscriptionDto> dtos) {
        Set<String> usernames = dtos.stream().map(OldSubscriptionDto::getUsername).collect(Collectors.toSet());

        // Загружаем существующих подписчиков и книги
        Map<String, Subscription> existingSubscriptions = subscriptionRepository.findByUsernameIn(usernames)
                .stream().collect(Collectors.toMap(Subscription::getUsername, sub -> sub));

        Set<String> names = dtos.stream().map(OldSubscriptionDto::getBookName).collect(Collectors.toSet());
        Set<String> authors = dtos.stream().map(OldSubscriptionDto::getBookAuthor).collect(Collectors.toSet());

        Map<String, Book> existingBooks = bookRepository.findByNameAndAuthorIn(names, authors)
                .stream().collect(Collectors.toMap(book -> book.getTitle() + "|" + book.getAuthor(), book -> book));

        List<Subscription> newSubscriptions = new ArrayList<>();
        List<Book> newBooks = new ArrayList<>();

        for (OldSubscriptionDto dto : dtos) {
            Subscription subscription = existingSubscriptions.computeIfAbsent(dto.getUsername(), username -> {
                Subscription newSub = new Subscription();
                newSub.setUsername(username);
                newSub.setUserFullName(dto.getUserFullName());
                newSub.setActive(dto.getUserActive());
                newSub.setBooks(new ArrayList<>());
                newSubscriptions.add(newSub);
                return newSub;
            });

            String bookKey = dto.getBookName() + "|" + dto.getBookAuthor();
            Book book = existingBooks.computeIfAbsent(bookKey, key -> {
                Book newBook = new Book();
                newBook.setTitle(dto.getBookName());
                newBook.setAuthor(dto.getBookAuthor());
                newBook.setPublishedDate(LocalDate.now());
                newBooks.add(newBook);
                return newBook;
            });

            if (!subscription.getBooks().contains(book)) {
                subscription.getBooks().add(book);
                book.getSubscriptions().add(subscription);
            }
        }

        if (!newBooks.isEmpty()) {
            bookRepository.saveAll(newBooks);
        }
        if (!newSubscriptions.isEmpty()) {
            subscriptionRepository.saveAll(newSubscriptions);
        }
    }






}
