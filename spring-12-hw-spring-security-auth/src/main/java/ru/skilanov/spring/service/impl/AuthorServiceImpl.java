package ru.skilanov.spring.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skilanov.spring.dto.AuthorDto;
import ru.skilanov.spring.exception.NotFoundException;
import ru.skilanov.spring.mapper.AuthorMapper;
import ru.skilanov.spring.models.Author;
import ru.skilanov.spring.repositories.AuthorRepository;
import ru.skilanov.spring.service.api.AuthorService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    private final AuthorMapper mapper;

    @Transactional(readOnly = true)
    @Override
    public AuthorDto findById(long id) {
        return authorRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(NotFoundException::new);
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
        var foundAuthor = authorRepository.findById(id).orElseThrow(NotFoundException::new);
        foundAuthor.setFullName(fullName);
        var updatedAuthor = authorRepository.save(foundAuthor);
        return mapper.toDto(updatedAuthor);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        authorRepository.deleteById(id);
    }
}
