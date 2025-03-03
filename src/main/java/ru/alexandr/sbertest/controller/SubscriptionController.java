package ru.alexandr.sbertest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.alexandr.sbertest.dtos.SubscriptionByUsernameDto;
import ru.alexandr.sbertest.dtos.UserFullNameRequest;
import ru.alexandr.sbertest.service.SubscriptionService;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService service;

    @PostMapping("/findByUserFullName")
    public SubscriptionByUsernameDto findByUserFullName(@RequestBody UserFullNameRequest userFullName) {
        return service.findSubByNameSurName(userFullName.getUserFullName());
    }

    //Для теста просроченных подписок
    @GetMapping("/get")
    public void findByUserFullName() {
        service.checkOverdueSubscriptions();
    }


    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleSubscriptionNotFound(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Данный пользователь не найден");
    }
}
