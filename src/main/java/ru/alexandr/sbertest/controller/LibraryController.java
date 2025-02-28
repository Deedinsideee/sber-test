package ru.alexandr.sbertest.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.alexandr.sbertest.dtos.OldSubscriptionDto;
import ru.alexandr.sbertest.dtos.SubscriptionByUsernameDto;
import ru.alexandr.sbertest.dtos.UserFullNameRequest;
import ru.alexandr.sbertest.service.LibraryService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@AllArgsConstructor
public class LibraryController {
    private final LibraryService libraryService;

    @PostMapping("/findByUserFullName")
    public SubscriptionByUsernameDto findByUserFullName(@RequestBody UserFullNameRequest userFullName) {
        return libraryService.findSubByNameSurName(userFullName.getUserFullName());
    }

    @PostMapping("/importLegacySubs")
    public void importLegacy(@RequestBody List<OldSubscriptionDto> dtos){
        libraryService.importSubscriptions(dtos);
    }
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleSubscriptionNotFound(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Данный пользователь не найден");
    }

}
