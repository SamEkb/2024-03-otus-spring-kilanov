package ru.skilanov.spring.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookPagesController {

    @GetMapping("/")
    public String listBooksPage() {
        return "list";
    }

    @GetMapping("/add")
    public String addBookPage() {
        return "add";
    }
}
