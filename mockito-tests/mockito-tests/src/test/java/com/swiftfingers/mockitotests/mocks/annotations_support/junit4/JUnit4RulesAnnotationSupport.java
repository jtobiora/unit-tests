package com.swiftfingers.mockitotests.mocks.annotations_support.junit4;

import com.swiftfingers.mockitotests.entity.Book;
import com.swiftfingers.mockitotests.repository.BookRepository;
import com.swiftfingers.mockitotests.service.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class JUnit4RulesAnnotationSupport {

    @InjectMocks //used for the class user test (BookService depends on BookRepository)
    private BookService bookService;

    @Mock //used for a mock dependency
    private BookRepository bookRepository;


    //Another way to use init mocks in JUNIT 4
//    @Rule
//    public MockitoRule mockitoRule = MockitoJUnit.rule();


//    @Test
//    public void demoTestsUsingAnnotationsSupport () {
//        Book book1 = new Book(1L, "Mockito In Action", new Date(), 500.0);
//        Book book2 = new Book(2L, "JUnit 5 In Action", new Date(),400.0);
//
//        List<Book> newBooks = Arrays.asList(book1, book2);
//
//        when(bookRepository.findNewBooks(7)).thenReturn(newBooks);
//
//        List<Book> newBooksWithAppliedDiscount = bookService.getNewBooksWithAppliedDiscount(10, 7);
//
//        assertEquals(2, newBooksWithAppliedDiscount.size());
//        assertEquals(450, newBooksWithAppliedDiscount.get(0).getPrice());
//        assertEquals(360, newBooksWithAppliedDiscount.get(1).getPrice());
//
//    }
}
