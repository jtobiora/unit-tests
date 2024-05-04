package com.swiftfingers.mockitotests.mocks.annotations_support.junit5.stubbing;

import com.swiftfingers.mockitotests.entity.Book;
import com.swiftfingers.mockitotests.repository.BookRepository;
import com.swiftfingers.mockitotests.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)  //used in JUnit 5
//@RunWith(MockitoJUnitRunner.class) //used in JUnit 4
public class BookApplicationTestsUsingAnnotations {

    @InjectMocks //used for the class user test (BookService depends on BookRepository)
    private BookService bookService;

    @Mock //used for a mock dependency
    private BookRepository bookRepository;

    @Test
    public void demoTestsUsingAnnotationsSupport () {
        Book book1 = new Book(1L, "Mockito In Action", new Date(), 500.0, false);
        Book book2 = new Book(2L, "JUnit 5 In Action", new Date(),400.0, false);

        List<Book> newBooks = Arrays.asList(book1, book2);

        when(bookRepository.findNewBooks(7)).thenReturn(newBooks);

        List<Book> newBooksWithAppliedDiscount = bookService.getNewBooksWithAppliedDiscount(10, 7);

        assertEquals(2, newBooksWithAppliedDiscount.size());
        assertEquals(450, newBooksWithAppliedDiscount.get(0).getPrice());
        assertEquals(360, newBooksWithAppliedDiscount.get(1).getPrice());

    }

    @Test
    public void testCalculateTotalCostOfBooks() {
        List<Long> bookIds = new ArrayList<>();
        bookIds.add(1L);
        bookIds.add(2L);

        Book book1 = new Book(1L, "Mockito In Action", new Date(), 500.0, false);
        Book book2 = new Book(2L, "JUnit 5 In Action", new Date(),400.0, true);

		when(bookRepository.findBookByBookId(1L)).thenReturn(book1); //when the repository layer is called with book id 1 then return book 1
        when(bookRepository.findBookByBookId(2L)).thenReturn(book2);

             //You can also use doReturn to achieve same thing above
//		doReturn(book1).when(bookRepository).findBookByBookId(1L);
//		doReturn(book2).when(bookRepository).findBookByBookId(2L);

        double actualCost = bookService.calculateTotalCost(bookIds);
        assertEquals(900.0, actualCost);
    }

    @Test
    public void testCalculateTotalCostOfBooks_stubSameMethodTwice() {
        List<Long> bookIds = new ArrayList<>();
        bookIds.add(1L);
        bookIds.add(1L);

        Book book1 = new Book(1L, "Mockito In Action", new Date(), 500.0, true);
        Book book2 = new Book(2L, "JUnit 5 In Action", new Date(),400.0, true);

        when(bookRepository.findBookByBookId(1L))
                .thenReturn(book1)
                .thenReturn(book1);

        double actualCost = bookService.calculateTotalCost(bookIds);
        assertEquals(1000.0, actualCost);
    }

    @Test
    public void stubbingVoidMethod_testDeleteAction() {
            Book book1 = new Book(1L, "Mockito In Action", new Date(), 500.0, true);
            doNothing().when(bookRepository).deleteById(book1.getBookId());
            bookService.deleteBookById(book1.getBookId());
    }
}

