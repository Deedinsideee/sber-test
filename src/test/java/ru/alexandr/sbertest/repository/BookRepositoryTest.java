package ru.alexandr.sbertest.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.alexandr.sbertest.model.Book;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.alexandr.sbertest.BaseTestClass.createBookWithNames;

@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void findByNameAndAuthorIn() {
        String title1 = "The Hobbit";
        String author1 = "Tolkien";

        String title2 = "1984";
        String author2 = "Orwell";

        String title3 = "The Hobbit"; // Другая книга с тем же названием, она не найдется
        String author3 = "Doe";

        Book book1 = createBookWithNames(title1, author1);
        Book book2 = createBookWithNames(title2, author2);
        Book book3 = createBookWithNames(title3, author3);

        bookRepository.saveAll(List.of(book1, book2, book3));

        // Условие поиска
        Set<String> titles = Set.of(title1, title2);
        Set<String> authors = Set.of(author1, author2);

        // Выполнение запроса
        List<Book> result = bookRepository.findByNameAndAuthorIn(titles, authors);

        // Проверка результатов
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(b -> b.getTitle().equals(title1) && b.getAuthor().equals(author1)));
        assertTrue(result.stream().anyMatch(b -> b.getTitle().equals(title2) && b.getAuthor().equals(author2)));
    }
}



