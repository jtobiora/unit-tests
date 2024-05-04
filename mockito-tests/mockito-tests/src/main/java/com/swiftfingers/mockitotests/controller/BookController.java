package com.swiftfingers.mockitotests.controller;

import com.swiftfingers.mockitotests.entity.Book;
import com.swiftfingers.mockitotests.entity.BookRequest;
import com.swiftfingers.mockitotests.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController (BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/add")
    public ResponseEntity addBooks (@RequestBody BookRequest request) {
        Book book = Book.builder().price(request.getPrice()).title(request.getTitle()).publishedDate(request.getPublishedDate()).build();
        return ResponseEntity.ok(bookService.addBook(book));
    }

    @GetMapping("/findAll")
    public ResponseEntity findAllBooks () {
       return ResponseEntity.ok(bookService.findAllBooks());

    }

    @GetMapping("/{id}")
    public ResponseEntity findBookById (@PathVariable Long id) {
              return ResponseEntity.ok(bookService.findBookById(id));
    }

    @PutMapping("/update")
    public ResponseEntity updateBook (@RequestBody Book book) {
        return ResponseEntity.ok(bookService.updateBook(book));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity updateBook (@PathVariable Long id) {
        bookService.deleteBookById(id);
        return ResponseEntity.ok("Book deleted!");
    }
}
