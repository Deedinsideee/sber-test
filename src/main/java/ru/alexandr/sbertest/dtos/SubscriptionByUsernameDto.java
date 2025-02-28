package ru.alexandr.sbertest.dtos;


import lombok.Data;

import java.util.List;

@Data
public class SubscriptionByUsernameDto {
    private String username;
    private String userFullName;
    private Boolean active;

    List<BookDto> books;

}
