package ru.alexandr.sbertest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.alexandr.sbertest.dtos.ErrorResponse;
import ru.alexandr.sbertest.dtos.SubscriptionByUsernameDto;
import ru.alexandr.sbertest.dtos.UserFullNameRequest;
import ru.alexandr.sbertest.service.SubscriptionService;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService service;

    @Operation(summary = "Поиск подписки по полному имени пользователя",
            description = "Возвращает информацию о подписке по переданному имени и фамилии")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ",
                    content = @Content(schema = @Schema(implementation = SubscriptionByUsernameDto.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/findByUserFullName")
    public ResponseEntity<SubscriptionByUsernameDto> findByUserFullName(@RequestBody UserFullNameRequest userFullName) {
        SubscriptionByUsernameDto response = service.findSubByNameSurName(userFullName.getUserFullName());
        return ResponseEntity.ok(response);
    }


    @GetMapping("/pay")
    public ResponseEntity<Void> findByUserFullName(@RequestParam UUID subscriptionId,
                                                   @RequestParam BigDecimal amount) {
        service.paySubscription(subscriptionId, amount);
        return ResponseEntity.ok().build();
    }


    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleSubscriptionNotFound(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Данный пользователь не найден"));
    }

}
