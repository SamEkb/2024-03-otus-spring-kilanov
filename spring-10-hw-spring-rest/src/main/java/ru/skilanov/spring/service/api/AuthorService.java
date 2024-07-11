package ru.skilanov.spring.service.api;


import ru.skilanov.spring.dto.AuthorDto;

import java.util.List;

public interface AuthorService {
    AuthorDto findById(long id);

    List<AuthorDto> findAll();

    AuthorDto create(String fullName);

    AuthorDto update(long id, String fullName);

    void deleteById(long id);
}
