package ru.alexandr.sbertest.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import ru.alexandr.sbertest.dtos.BookDto;
import ru.alexandr.sbertest.dtos.SubscriptionByUsernameDto;
import ru.alexandr.sbertest.model.Book;
import ru.alexandr.sbertest.model.Subscription;
import ru.alexandr.sbertest.model.SubscriptionBook;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.alexandr.sbertest.BaseTestClass.createBookWithNames;
import static ru.alexandr.sbertest.BaseTestClass.createSubscription;

public class LibraryMapperTest {
    @InjectMocks
    private LibraryMapper libraryMapper = Mappers.getMapper(LibraryMapper.class);


    @Test
    void toSubscriptionDto() {
        String username = "test_user";
        Subscription subscription = createSubscription(username);

        SubscriptionByUsernameDto dto = libraryMapper.toSubscriptionDto(subscription);

        assertNotNull(dto);
        assertEquals(username, dto.getUsername());
        assertTrue(dto.getActive());
    }

    @Test
    void toBookDto() {
        String authorName = "Test Author";
        String bookTitle = "Test Book";
        Book book = createBookWithNames(bookTitle, authorName);

        BookDto dto = libraryMapper.toBookDto(book);

        assertNotNull(dto);
        assertEquals(bookTitle, dto.getTitle());
        assertEquals(authorName, dto.getAuthor());
    }

    @Test
    void toBookDtos() {
        String authorName = "Author 1";
        String bookTitle = "Book 1";
        Book book1 = createBookWithNames(bookTitle, authorName);

        String authorName2 = "Author 2";
        String bookTitle2 = "Book 2";
        Book book2 = createBookWithNames(bookTitle2, authorName2);


        List<BookDto> dtos = libraryMapper.toBookDtos(List.of(book1, book2));

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals(bookTitle, dtos.get(0).getTitle());
        assertEquals(authorName, dtos.get(0).getAuthor());
        assertEquals(bookTitle2, dtos.get(1).getTitle());
        assertEquals(authorName2, dtos.get(1).getAuthor());
    }

    @Test
    void mapWitSubAndBooks() {
        String username = "test_user";
        Subscription subscription = createSubscription(username);
        String authorName = "Test Author";
        String bookTitle = "Test Book";
        Book book = createBookWithNames(bookTitle, authorName);

        String authorName2 = "Author 2";
        String bookTitle2 = "Book 2";
        Book book2 = createBookWithNames(bookTitle2, authorName2);

        SubscriptionBook subscriptionBook = new SubscriptionBook();
        subscriptionBook.setBook(book);


        SubscriptionBook subscriptionBook2 = new SubscriptionBook();
        subscriptionBook2.setBook(book2);
        subscription.setBooks(List.of(subscriptionBook, subscriptionBook2));

        SubscriptionByUsernameDto dto = libraryMapper.toSubscriptionDto(subscription);

        assertNotNull(dto);
        assertNotNull(dto.getBooks());
        assertEquals(2, dto.getBooks().size());
        assertEquals(bookTitle, dto.getBooks().get(0).getTitle());
        assertEquals(authorName, dto.getBooks().get(0).getAuthor());
        assertEquals(bookTitle2, dto.getBooks().get(1).getTitle());
        assertEquals(authorName2, dto.getBooks().get(1).getAuthor());
    }
}
