package ru.skilanov.spring.listener;

import lombok.RequiredArgsConstructor;
import org.bson.Document;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import ru.skilanov.spring.models.Book;
import ru.skilanov.spring.repositories.CommentRepository;

@RequiredArgsConstructor
@Component
public class BookCascadeDeleteListener extends AbstractMongoEventListener<Book> {

    private final CommentRepository commentRepository;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Book> event) {
        super.onBeforeDelete(event);
        Document source = event.getSource();
        String bookId = source.get("_id").toString();
        commentRepository.deleteAllByBookId(bookId);
    }
}