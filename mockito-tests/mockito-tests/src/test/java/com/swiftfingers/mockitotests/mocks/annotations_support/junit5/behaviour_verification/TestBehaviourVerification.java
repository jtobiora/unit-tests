package com.swiftfingers.mockitotests.mocks.annotations_support.junit5.behaviour_verification;

import com.swiftfingers.mockitotests.entity.Book;
import com.swiftfingers.mockitotests.entity.BookRequest;
import com.swiftfingers.mockitotests.repository.BookRepository;
import com.swiftfingers.mockitotests.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.*;


/**
 * Behaviour verification helps us in verifying that certain mock method was called by the System Under Test
 * It uses the verify method. The verify method checks that a given method was called once and only
 * once with given arguments
 */
@ExtendWith(MockitoExtension.class)  //used in JUnit 5
//@RunWith(MockitoJUnitRunner.class) //used in JUnit 4
public class TestBehaviourVerification {

    @InjectMocks //used for the class user test (BookService depends on BookRepository)
    private BookService bookService;

    @Mock //used for a mock dependency
    private BookRepository bookRepository;

    //IN this test we want to verify that bookRepository.delete is called when bookService.deleteBookById is invoked
    @Test
    public void verifyThatDeleteIsCalled_TestShouldPass() {
        Book book1 = new Book(1L, "Mockito In Action", new Date(), 500.0, false);
        bookService.deleteBook(book1);
        verify(bookRepository).delete(book1); //verify that bookRepository.delete(book) is actually called
    }

    //Test should fail because the bookRepository.delete method was not called.
    @Test
    public void verifyThatDeleteIsCalled_TestShouldFail () {
        Book book1 = new Book(1L, "Mockito In Action", new Date(), 400.0, true);
        bookService.deleteBook(book1);
        verify(bookRepository, times(0)).delete(book1); //verify that bookRepository.delete(book) is actually called
    }

    @Test
    public void verifyNoInteractionWithMock_testShouldFail () {
        BookRequest request = new BookRequest(1L, "Mockito In Action", 400.0, new Date());
        bookService.updateBook(null);
        verifyNoInteractions(bookRepository); //verify that bookRepository is not interacted with
    }

    @Test
    public void testInteractionsWithMockObject_verifyThatNotOnlyFindByIdWasCalledOnMockObject() {
        Book book = new Book(1L, "Mockito In Action", new Date(), 500.0, false);
        when(bookRepository.findById(book.getBookId())).thenReturn(Optional.of(book));
        bookService.updateBook(book);
        verify(bookRepository).findById(1L);
        verifyNoMoreInteractions(bookRepository); //verify that bookRepository was used to call ONLY findById method and no more
    }

    @Test
    public void testAllInteractionsWithMockObject_verifyThatSaveWasAlsoCalledOnMockObjectAfterFindById() {
        Book book = new Book(1L, "Mockito In Action", new Date(), 500.0, true);
        when(bookRepository.findBookByBookId(book.getBookId())).thenReturn(book);
        bookService.updatePrice(1L, 600.0);
        verify(bookRepository).findBookByBookId(1L);
        verify(bookRepository).save(book);
        verifyNoMoreInteractions(bookRepository); //verify that bookRepository was used to call both methods 'findBookByBookId' and 'save' and no more
    }

    @Test
    public void testOrderOfInteractionsWithMockObject_testShouldFailBecauseOrderOfInvocationOnMockObjectIsWrong() {
        Book book = new Book(1L, "Mockito In Action", new Date(), 500.0, true);
        when(bookRepository.findBookByBookId(1L)).thenReturn(book);
        bookService.updatePrice(1L, 500);

        //Note that 'save' was called before 'findBookByBookId' in bookService using the mock (bookRepository)
        //So the order of interaction below is incorrect. Test should fail
        InOrder inOrder = Mockito.inOrder(bookRepository);
        inOrder.verify(bookRepository).save(book);
        inOrder.verify(bookRepository).findBookByBookId(1L);
    }

    @Test
    public void testSaveBookWithBookRequestWithGreaterPrice3() {
        //BookRequest bookRequest = new BookRequest("Mockito In Action", 600, LocalDate.now());
        Book book = new Book(1L, "Mockito In Action", new Date(), 500.0, true);
        bookService.addBook(book);
        bookService.addBook(book);
        bookService.addBook(book);
//		verify(bookRepository, times(2)).save(book);//verify that 'save' was called exactly two times
//		verify(bookRepository, atLeast(4)).save(book);//verify that 'save' was called at least four times
//		verify(bookRepository, atMost(2)).save(book);//verify that 'save' was called at most two times
//		verify(bookRepository, atMostOnce()).save(book);//verify that 'save' was called at most once
        verify(bookRepository, atLeastOnce()).save(book);//verify that 'save' was called at least once
    }
}
