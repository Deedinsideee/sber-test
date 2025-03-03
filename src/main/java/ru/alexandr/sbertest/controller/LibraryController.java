package ru.alexandr.sbertest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.alexandr.sbertest.dtos.OldSubscriptionDto;
import ru.alexandr.sbertest.service.LibraryService;

import java.util.List;

@RestController
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
    public ResponseEntity<Void> importLegacy(@RequestBody @Valid List<OldSubscriptionDto> dtos) {
        libraryService.importSubscriptions(dtos);
        return ResponseEntity.ok().build();
    }


}
