package ru.alexandr.sbertest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.alexandr.sbertest.model.Book;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {

    @Query("SELECT b FROM Book b WHERE b.title IN :names AND b.author IN :authors")
    List<Book> findByNameAndAuthorIn(Set<String> names, Set<String> authors);

}
