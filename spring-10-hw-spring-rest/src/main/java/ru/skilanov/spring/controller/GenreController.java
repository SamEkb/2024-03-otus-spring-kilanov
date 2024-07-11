package ru.skilanov.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skilanov.spring.dto.GenreDto;
import ru.skilanov.spring.service.api.GenreService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/api/genres")
    public List<GenreDto> getAllBooks() {
        return genreService.findAll();
    }
}
