package ru.alexandr.sbertest.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookDto {

    private String title;


    private String author;

    private LocalDate publishedDate;
}
