package ru.skilanov.spring.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skilanov.spring.dto.GenreDto;
import ru.skilanov.spring.exception.NotFoundException;
import ru.skilanov.spring.mapper.GenreMapper;
import ru.skilanov.spring.models.Genre;
import ru.skilanov.spring.repositories.GenreRepository;
import ru.skilanov.spring.service.api.GenreService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    private final GenreMapper mapper;

    @Transactional(readOnly = true)
    @Override
    public GenreDto findById(long id) {
        return genreRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    @Override
    public List<GenreDto> findAll() {
        return genreRepository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public GenreDto create(String name) {
        var genre = Genre.builder()
                .name(name)
                .build();
        var savedGenre = genreRepository.save(genre);

        return mapper.toDto(savedGenre);
    }

    @Transactional
    @Override
    public GenreDto update(long id, String name) {
        var genre = genreRepository.findById(id).orElseThrow(NotFoundException::new);
        genre.setName(name);
        var updatedGenre = genreRepository.save(genre);

        return mapper.toDto(updatedGenre);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        genreRepository.deleteById(id);
    }
}
