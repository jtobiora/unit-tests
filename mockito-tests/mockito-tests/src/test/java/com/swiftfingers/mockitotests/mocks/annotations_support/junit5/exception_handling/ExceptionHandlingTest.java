package com.swiftfingers.mockitotests.mocks.annotations_support.junit5.exception_handling;

import com.swiftfingers.mockitotests.repository.BookRepository;
import com.swiftfingers.mockitotests.service.BookService;
import com.swiftfingers.mockitotests.util.DatabaseReadException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)  //used in JUnit 5
public class ExceptionHandlingTest {
    @InjectMocks //used for the class under test (BookService depends on BookRepository)
    private BookService bookService;

    @Mock //used for a mock dependency
    private BookRepository bookRepository;

    @Test
    public void testTotalPriceOfBooks() throws NullPointerException {
        when(bookRepository.findAll()).thenThrow(NullPointerException.class);
        assertThrows(DatabaseReadException.class, () -> bookService.getTotalPriceOfBooks());
    }
}
