package ru.skilanov.spring.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.skilanov.spring.models.Author;
import ru.skilanov.spring.models.Book;
import ru.skilanov.spring.models.Comment;
import ru.skilanov.spring.models.Genre;
import ru.skilanov.spring.repositories.AuthorRepository;
import ru.skilanov.spring.repositories.BookRepository;
import ru.skilanov.spring.repositories.CommentRepository;
import ru.skilanov.spring.repositories.GenreRepository;

@ChangeLog
public class LibraryChangeLog {
    @ChangeSet(order = "001", id = "dropDb", author = "kilanov", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "fillData", author = "kilanov")
    public void fillData(AuthorRepository authorRepository, GenreRepository genreRepository,
                         BookRepository bookRepository, CommentRepository commentRepository) {
        Author tolstoy = authorRepository.save(new Author("1", "Tolstoy"));
        Author gogol = authorRepository.save(new Author("2", "Author_2"));

        Genre drama = genreRepository.save(new Genre("1", "Drama"));
        Genre horror = genreRepository.save(new Genre("2", "Horror"));


        Book book1 = bookRepository.save(new Book("1", "voina i mir", tolstoy, drama));
        Book book2 = bookRepository.save(new Book("2", "vii", gogol, horror));

        commentRepository.save(new Comment("1", "awesome", book1));
        commentRepository.save(new Comment("2", "interesting", book2));

    }
}
