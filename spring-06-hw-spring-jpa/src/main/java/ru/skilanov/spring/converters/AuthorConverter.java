package ru.skilanov.spring.converters;

import org.springframework.stereotype.Component;
import ru.skilanov.spring.dto.AuthorDto;

@Component
public class AuthorConverter {
    public String authorToString(AuthorDto author) {
        return "Id: %d, FullName: %s".formatted(author.getId(), author.getFullName());
    }
}
