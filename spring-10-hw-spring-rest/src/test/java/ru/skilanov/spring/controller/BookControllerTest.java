package ru.skilanov.spring.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.skilanov.spring.dto.AuthorDto;
import ru.skilanov.spring.dto.BookDto;
import ru.skilanov.spring.dto.GenreDto;
import ru.skilanov.spring.dto.request.BookCreateDto;
import ru.skilanov.spring.dto.request.BookUpdateDto;
import ru.skilanov.spring.exception.NotFoundException;
import ru.skilanov.spring.service.api.BookService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Books controller test")
@WebMvcTest(BookController.class)
public class BookControllerTest {

    public static final String BOOK_TITLE_KARENINA = "Anna Karenina";

    public static final Long ONE = 1L;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objMapper;

    @MockBean
    private BookService bookService;

    @Test
    void whenGetBookThenItReturns() throws Exception {
        var book = BookDto.builder()
                .id(ONE)
                .title(BOOK_TITLE_KARENINA)
                .genre(new GenreDto(ONE, ""))
                .author(new AuthorDto(ONE, ""))
                .build();

        when(bookService.findById(ONE)).thenReturn(book);

        MvcResult result = mvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(objMapper.readValue(result.getResponse().getContentAsString(), BookDto.class))
                .isEqualTo(book);
    }

    @Test
    void whenGetAllBooksThenTheyReturn() throws Exception {
        var book = BookDto.builder()
                .id(ONE)
                .title(BOOK_TITLE_KARENINA)
                .genre(new GenreDto(ONE, ""))
                .author(new AuthorDto(ONE, ""))
                .build();
        List<BookDto> books = List.of(book);
        when(bookService.findAll()).thenReturn(books);

        MvcResult result = mvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(objMapper
                .readValue(result.getResponse().getContentAsByteArray(), new TypeReference<List<BookDto>>() {}))
                .hasSize(1).containsExactly(books.toArray(BookDto[]::new));
    }

    @Test
    void whenAddBookThenItCreated() throws Exception {
        var bookToSave = BookCreateDto.builder()
                .title(BOOK_TITLE_KARENINA)
                .genreId(ONE)
                .authorId(ONE)
                .build();

        var createdBook = BookDto.builder()
                .id(ONE)
                .title(BOOK_TITLE_KARENINA)
                .genre(new GenreDto(ONE, ""))
                .author(new AuthorDto(ONE, ""))
                .build();

        when(bookService.create(bookToSave)).thenReturn(createdBook);

        mvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(bookToSave)))
                .andExpect(status().isCreated());
    }

    @Test
    void whenEditBookThenItEdited() throws Exception {
        var bookToUpdate = BookUpdateDto.builder()
                .id(ONE)
                .title(BOOK_TITLE_KARENINA)
                .genreId(ONE)
                .authorId(ONE)
                .build();

        mvc.perform(patch("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(bookToUpdate)))
                .andExpect(status().isOk());
    }

    @Test
    void whenDeleteBookThenItDeleted() throws Exception {
        doNothing().when(bookService).deleteById(ONE);

        mvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenBookDoesntExistThenItReturnsNotFound() throws Exception {
        when(bookService.findById(11)).thenThrow(new NotFoundException());

        mvc.perform(get("/api/books/11"))
                .andExpect(status().isNotFound());
    }
}
