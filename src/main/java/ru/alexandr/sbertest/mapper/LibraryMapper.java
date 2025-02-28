package ru.alexandr.sbertest.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.alexandr.sbertest.dtos.BookDto;
import ru.alexandr.sbertest.dtos.SubscriptionByUsernameDto;
import ru.alexandr.sbertest.model.Book;
import ru.alexandr.sbertest.model.Subscription;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LibraryMapper {


    SubscriptionByUsernameDto toSubscriptionDto(Subscription from);


    List<BookDto> toBookDtos(List<Book> books);

    BookDto toBookDto(Book books);

    @AfterMapping
    default void mapBooksToDto(@MappingTarget SubscriptionByUsernameDto dto, Subscription from) {
        dto.setBooks(toBookDtos(from.getBooks()));
    }

}
