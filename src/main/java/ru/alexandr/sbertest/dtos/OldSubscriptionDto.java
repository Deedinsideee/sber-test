package ru.alexandr.sbertest.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OldSubscriptionDto {

    @NotBlank(message = "Username не должен быть пустым")
    private String username;

    @NotBlank(message = "UserFullName не должен быть пустым")
    private String userFullName;

    @NotNull(message = "UserActive не должен быть null")
    private Boolean userActive;

    @NotBlank(message = "BookName не должен быть пустым")
    private String bookName;

    @NotBlank(message = "BookAuthor не должен быть пустым")
    private String bookAuthor;
}
