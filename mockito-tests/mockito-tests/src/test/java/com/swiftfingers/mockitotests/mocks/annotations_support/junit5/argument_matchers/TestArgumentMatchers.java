package com.swiftfingers.mockitotests.mocks.annotations_support.junit5.argument_matchers;
import com.swiftfingers.mockitotests.entity.Book;
import com.swiftfingers.mockitotests.repository.BookRepository;
import com.swiftfingers.mockitotests.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestArgumentMatchers {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Test
    public void testUpdatePrice() {
        Book book1 = new Book(1L, "Mockito In Action", new Date(), 500.0, true);
        Book book2 = new Book(2L, "Mockito In Action Version 2", new Date(), 600.0, true);
        when(bookRepository.findBookByBookId(any(Long.class))).thenReturn(book1);
        bookService.updatePrice(1L, 500);
        verify(bookRepository).save(book1);
    }

    // Argument Matchers should be provided for all arguments
    // Argument Matchers can't be used outside stubbing/verification i.e when calling the method to test
    @Test
    public void testInvalidUseOfArgumentMatchers() {
        Book book = new Book(1L, "Mockito In Action", new Date(), 500.0,true);
        when(bookRepository.findBookByTitleAndPublishedDate(eq("Mockito In Action"), any())).thenReturn(book);
       // Book actualBook = bookService.getBookByTitleAndPublishedDate(eq("Mockito In Action"), any()); //invalid use case
        Book actualBook = bookService.getBookByTitleAndPublishedDate("Mockito In Action", new Date());
        assertEquals("Mockito In Action", actualBook.getTitle());
    }

    @Test
    public void testSpecificTypeOfArgumentMatchers() {
        Book book = new Book(1L, "Mockito In Action", new Date(), 400.0, true);
        when(bookRepository.findBookByTitleAndPriceAndIsDigital(anyString(), anyInt(), anyBoolean())).thenReturn(book);
        Book actualBook = bookService.getBookByTitleAndPriceAndIsDigital("Mockito In Action", 600, true);
        assertEquals("Mockito In Action", actualBook.getTitle());
    }

    @Test
    public void testCollectionTypeArgumentMatchers() {
        List<Book> books = new ArrayList<>();
        Book book = new Book(1L, "Mockito In Action", new Date(), 400.0, true);
        books.add(book);
        bookService.addBooks(books);
        verify(bookRepository).saveAllBooks(anyList()); // anySet, anyMap, anyCollection
    }

    @Test
    public void testStringTypeArgumentMatchers() {
        Book book = new Book(1L, "Mockito In Action", new Date(), 400.0, true);
        when(bookRepository.findBookByTitleAndPriceAndIsDigital(contains("Action"), anyInt(), anyBoolean())).thenReturn(book);
        Book actualBook = bookService.getBookByTitleAndPriceAndIsDigital("JUnit 5 In Action", 600, true);
        assertEquals("Mockito In Action", actualBook.getTitle());
    }
}
