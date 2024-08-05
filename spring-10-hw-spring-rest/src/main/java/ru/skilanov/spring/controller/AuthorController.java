package ru.skilanov.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skilanov.spring.dto.AuthorDto;
import ru.skilanov.spring.service.api.AuthorService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/api/authors")
    public List<AuthorDto> getAllBooks() {
        return authorService.findAll();
    }
}
