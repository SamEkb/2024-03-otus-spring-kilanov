package ru.skilanov.spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.skilanov.spring.dto.AuthorDto;
import ru.skilanov.spring.dto.BookDto;
import ru.skilanov.spring.dto.GenreDto;
import ru.skilanov.spring.service.api.AuthorService;
import ru.skilanov.spring.service.api.BookService;
import ru.skilanov.spring.service.api.GenreService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@DisplayName("Books controller test")
@WebMvcTest(BookController.class)
public class BookControllerTest {

    public static final String BOOK_TITLE_KARENINA = "Anna Karenina";

    public static final long ONE = 1;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @Test
    void whenGetAllBooksThenReturnsRightView() throws Exception {
        this.mvc
                .perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("list"))
                .andExpect(model().attributeExists("books"));
    }

    @Test
    void whenAddBookThenReturnsRightView() throws Exception {
        this.mvc
                .perform(get("/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("create"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attributeExists("book"));
    }

    @Test
    void whenAddBookThenItCreated() throws Exception {
        BookDto book1 = BookDto.builder()
                .title(BOOK_TITLE_KARENINA)
                .genre(new GenreDto(ONE, ""))
                .author(new AuthorDto(ONE, ""))
                .build();
        this.mvc
                .perform(post("/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(book1))

                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    void whenEditBookThenReturnsRightView() throws Exception {
        BookDto book1 = BookDto.builder()
                .title(BOOK_TITLE_KARENINA)
                .genre(new GenreDto(ONE, ""))
                .author(new AuthorDto(ONE, ""))
                .build();

        when(bookService.findById(ONE)).thenReturn(book1);
        this.mvc
                .perform(get("/edit")
                        .param("id", "1")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("edit"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attributeExists("book"));
    }

    @Test
    void whenEditBookThenItEdited() throws Exception {
        BookDto book1 = BookDto.builder()
                .title(BOOK_TITLE_KARENINA)
                .genre(new GenreDto(ONE, ""))
                .author(new AuthorDto(ONE, ""))
                .build();

        this.mvc
                .perform(post("/edit")
                        .content(mapper.writeValueAsString(book1))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    void whenDeleteBookThenItDeleted() throws Exception {
        this.mvc
                .perform(post("/delete")
                        .param("id", "1")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }
}
