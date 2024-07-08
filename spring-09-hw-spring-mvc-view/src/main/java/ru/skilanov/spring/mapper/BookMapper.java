package ru.skilanov.spring.mapper;

import org.mapstruct.Mapper;
import ru.skilanov.spring.dto.BookDto;
import ru.skilanov.spring.dto.request.BookUpdateDto;
import ru.skilanov.spring.models.Book;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDto toDto(Book book);

    Book toEntity(BookDto dto);

    BookDto toBookUpdateDto(BookUpdateDto dto);
}
