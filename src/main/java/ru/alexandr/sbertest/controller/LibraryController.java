package ru.alexandr.sbertest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.alexandr.sbertest.model.Book;
import ru.alexandr.sbertest.model.LegacySubscription;
import ru.alexandr.sbertest.service.LibraryService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class LibraryController {
    private final LibraryService libraryService;

    @Operation(summary = "Импорт старых подписок", description = "Позволяет импортировать список старых подписок в систему.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Импорт выполнен успешно"),
            @ApiResponse(responseCode = "400", description = "Некорректный формат входных данных"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/importLegacySubs")
    public ResponseEntity<Void> importLegacy(@RequestBody @Valid List<LegacySubscription> dtos) {
        libraryService.saveLegacySubscription(dtos);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/books")
    public Page<Book> getBooks(Pageable pageable) {
        return libraryService.getBooks(pageable);
    }

    // Ручка для получения книги по ID
    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable UUID id) {
        Optional<Book> book = libraryService.getBookById(id);
        return book.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
