package com.swiftfingers.mockitotests.mocks.annotations_support.junit5.argument_captor;

import com.swiftfingers.mockitotests.entity.Book;
import com.swiftfingers.mockitotests.repository.BookRepository;
import com.swiftfingers.mockitotests.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)  //used in JUnit 5
public class TestBookArgumentCaptor {

    @InjectMocks //used for the class under test (BookService depends on BookRepository)
    private BookService bookService;

    @Mock //used for a mock dependency
    private BookRepository bookRepository;

    @Captor
    private ArgumentCaptor<Book> bookArgumentCaptor;

    @Test
    public void verifySaveArgument_WhenAddBookCalled() {
        Book book1 = new Book(1L, "Mockito In Action", new Date(), 500.0, true);
       // ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class); you can also do this without using annotations @Captor
        bookService.addBook(book1);
        verify(bookRepository).save(bookArgumentCaptor.capture()); //verify that bookRepository.save(book) is actually called with book argument
    }
}
