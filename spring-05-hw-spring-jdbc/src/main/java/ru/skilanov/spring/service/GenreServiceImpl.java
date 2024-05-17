package ru.skilanov.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skilanov.spring.models.Genre;
import ru.skilanov.spring.repositories.GenreRepository;

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

    @Override
    public Genre insert(String name) {
        var genre = new Genre(0, name);
        return genreRepository.save(genre);
    }

    @Override
    public Genre update(long id, String name) {
        var genre = new Genre(id, name);
        return genreRepository.save(genre);
    }

    @Override
    public void deleteById(long id) {
        genreRepository.deleteById(id);
    }
}
