package ru.skilanov.spring.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skilanov.spring.dto.AuthorDto;
import ru.skilanov.spring.exception.EntityNotFoundException;
import ru.skilanov.spring.mapper.AuthorMapper;
import ru.skilanov.spring.models.Author;
import ru.skilanov.spring.repositories.AuthorRepository;
import ru.skilanov.spring.service.api.AuthorService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    private final AuthorMapper mapper;

    @Transactional(readOnly = true)
    @Override
    public Optional<AuthorDto> findById(long id) {
        return authorRepository.findById(id)
                .map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    @Override
    public List<AuthorDto> findAll() {
        return authorRepository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public AuthorDto create(String fullName) {
        var author = Author.builder()
                .fullName(fullName)
                .build();
        var createdAuthor = authorRepository.save(author);
        return mapper.toDto(createdAuthor);
    }

    @Transactional
    @Override
    public AuthorDto update(long id, String fullName) {
        var author = Author.builder()
                .id(id)
                .fullName(fullName)
                .build();
        var updatedAuthor = authorRepository.save(author);
        return mapper.toDto(updatedAuthor);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        Author author = authorRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        authorRepository.delete(author);
    }
}
