package ru.skilanov.spring.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skilanov.spring.exception.EntityNotFoundException;
import ru.skilanov.spring.models.Genre;
import ru.skilanov.spring.repositories.api.GenreRepository;
import ru.skilanov.spring.service.api.GenreService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    public Optional<Genre> findById(long id) {
        return genreRepository.findById(id);
    }

    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Transactional
    @Override
    public Genre insert(String name) {
        var genre = Genre.builder()
                .name(name)
                .build();
        return genreRepository.save(genre);
    }

    @Transactional
    @Override
    public Genre update(long id, String name) {
        var genre = new Genre(id, name);
        return genreRepository.save(genre);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        var genre = genreRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        genreRepository.delete(genre);
    }
}
