package ru.alexandr.sbertest.dtos;

import lombok.Data;

@Data
public class OldSubscriptionDto {
    private String username;
    private String userFullName;
    private Boolean userActive;
    private String bookName;
    private String bookAuthor;
}
