package ru.skilanov.spring.service.api;


import ru.skilanov.spring.dto.AuthorDto;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    Optional<AuthorDto> findById(String id);

    List<AuthorDto> findAll();

    AuthorDto create(String fullName);

    AuthorDto update(String id, String fullName);

    void deleteById(String id);
}
