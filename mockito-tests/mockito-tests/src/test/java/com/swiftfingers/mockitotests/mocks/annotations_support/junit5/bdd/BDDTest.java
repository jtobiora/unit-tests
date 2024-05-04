package com.swiftfingers.mockitotests.mocks.annotations_support.junit5.bdd;

import com.swiftfingers.mockitotests.entity.Book;
import com.swiftfingers.mockitotests.repository.BookRepository;
import com.swiftfingers.mockitotests.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)  //used in JUnit 5
//@RunWith(MockitoJUnitRunner.class) //used in JUnit 4
public class BDDTest {
    @InjectMocks //used for the class under test (BookService depends on BookRepository)
    private BookService bookService;

    @Mock //used for a mock dependency
    private BookRepository bookRepository;
    @Test
    public void testStubbingUsingBDDMockitoStyle () {
        Book book1 = new Book(1L, "Mockito In Action", new Date(), 500.0, true);
        Book book2 = new Book(2L, "JUnit 5 In Action", new Date(),400.0, false);

        List<Book> newBooks = Arrays.asList(book1, book2);

        when(bookRepository.findNewBooks(7)).thenReturn(newBooks);

        List<Book> newBooksWithAppliedDiscount = bookService.getNewBooksWithAppliedDiscount(10, 7);

        assertEquals(2, newBooksWithAppliedDiscount.size());
        assertEquals(450, newBooksWithAppliedDiscount.get(0).getPrice());
        assertEquals(360, newBooksWithAppliedDiscount.get(1).getPrice());

    }

    @Test
    @DisplayName("Given - New books, When - Get new books with applied discount method is called, Then - New books with applied discount is returned")
    public void test_Given_NewBooks_When_GetNewBooksWithAppliedDiscountIsCalled_Then_NewBooksWithAppliedDiscountIsReturned() {
        // Arrange - Given
        Book book1 = new Book(1L, "Mockito In Action", new Date(), 500, false);
        Book book2 = new Book(2L, "JUnit 5 In Action", new Date(), 400, true);

        List<Book> newBooks = new ArrayList<>();
        newBooks.add(book1);
        newBooks.add(book2);

        given(bookRepository.findNewBooks(7)).willReturn(newBooks);

        // Act - When
        List<Book> newBooksWithAppliedDiscount = bookService.getNewBooksWithAppliedDiscount(10, 7);

        // Assert - Then
//		assertEquals(2, newBooksWithAppliedDiscount.size());
//		assertEquals(450, newBooksWithAppliedDiscount.get(0).getPrice());
//		assertEquals(360, newBooksWithAppliedDiscount.get(1).getPrice());

        // AssertJ - BDDAssertions
        then(newBooksWithAppliedDiscount).isNotNull();
        then(newBooksWithAppliedDiscount.size()).isEqualTo(2);
        then(newBooksWithAppliedDiscount.get(0).getPrice()).isEqualTo(450);
    }


   /*
   * Show Behaviour Verification Using BDD Style
   * */
   @Test
   public void test_Given_ABook_When_UpdatePriceIsCalledWithNewPrice_Then_BookPriceIsUpdated() {
       // Given - Arrange
       Book book = new Book(1L, "Mockito In Action", new Date(), 500, false);
       given(bookRepository.findBookByBookId(1L)).willReturn(book);

       // When - Act
       bookService.updatePrice(1L, 500);

       // Then - Assert/Verify
       BDDMockito.then(bookRepository).should().save(book);
   }
}
