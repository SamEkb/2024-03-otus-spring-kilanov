package ru.skilanov.spring.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skilanov.spring.exception.EntityNotFoundException;
import ru.skilanov.spring.models.Author;
import ru.skilanov.spring.repositories.api.AuthorRepository;
import ru.skilanov.spring.service.api.AuthorService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    public Optional<Author> findById(long id) {
        return authorRepository.findById(id);
    }

    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public Author insert(String fullName) {
        var author = Author.builder()
                .fullName(fullName)
                .build();
        return authorRepository.save(author);
    }

    @Override
    public Author update(long id, String fullName) {
        var author = new Author(id, fullName);
        return authorRepository.save(author);
    }

    @Override
    public void deleteById(long id) {
        var author = authorRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        authorRepository.delete(author);
    }
}
