package ru.skilanov.spring.repositories.impl;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.skilanov.spring.models.Book;
import ru.skilanov.spring.repositories.api.BookRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
@RequiredArgsConstructor
public class BookRepositoryJpa implements BookRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<Book> findById(long id) {
        EntityGraph<?> graph = entityManager.getEntityGraph("bookWithAuthorAndGenreGraph");
        return Optional.ofNullable(entityManager.find(
                        Book.class,
                        id,
                        Collections.singletonMap("javax.persistence.fetchgraph", graph)
                )
        );
    }

    @Override
    public List<Book> findAll() {
        EntityGraph<?> graph = entityManager.getEntityGraph("bookWithAuthorAndGenreGraph");
        TypedQuery<Book> query = entityManager.createQuery("select distinct b from Book as b", Book.class);
        query.setHint(FETCH.getKey(), graph);

        return query.getResultList();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            entityManager.persist(book);
            return book;
        }
        return entityManager.merge(book);
    }

    @Override
    public void delete(long id) {
        Book book = entityManager.find(Book.class, id);
        if (book != null) {
            entityManager.remove(book);
        }
    }
}
