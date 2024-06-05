package ru.skilanov.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {

    private long id;

    private String title;

    private AuthorDto author;

    private GenreDto genre;
}
