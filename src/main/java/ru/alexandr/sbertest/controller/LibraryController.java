package ru.alexandr.sbertest.controller;

import lombok.RequiredArgsConstructor;
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


    @PostMapping("/importLegacySubs")
    public void importLegacy(@RequestBody List<OldSubscriptionDto> dtos) {
        libraryService.importSubscriptions(dtos);
    }


}
