package com.swiftfingers.mockitotests.mocks;

import com.swiftfingers.mockitotests.entity.Book;
import com.swiftfingers.mockitotests.repository.BookRepository;
import com.swiftfingers.mockitotests.service.BookService;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookApplicationTestsWithoutAnnotations {

    @Test
    public void shouldReturnNumberofBooks_testShouldFail () {
        BookRepository bookRepository = mock(BookRepository.class); //one way to mock a stubbed class
        BookService bookService = new BookService(bookRepository);

        Book book = Book.builder().bookId(1L).title("Accounting for Beginners").price(2500.0).publishedDate(new Date()).build();
        List<Book> books = Arrays.asList(book);

        when(bookRepository.findAll()).thenReturn(books);

        List<Book> booksFound = bookService.findAllBooks();
        assertEquals(2, booksFound.size()); //test will fail because number returned is 1
    }
}
