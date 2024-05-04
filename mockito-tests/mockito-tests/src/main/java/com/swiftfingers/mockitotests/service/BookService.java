package com.swiftfingers.mockitotests.service;

import com.swiftfingers.mockitotests.entity.Book;
import com.swiftfingers.mockitotests.entity.BookRequest;
import com.swiftfingers.mockitotests.repository.BookRepository;
import com.swiftfingers.mockitotests.util.DatabaseReadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
//@Service
public class BookService {

  private final BookRepository bookRepository;

  public BookService (BookRepository bookRepository) {
      this.bookRepository = bookRepository;
  }

  public Book addBook (Book book) {
      return bookRepository.save(book);
  }

  public Optional<Book> updateBook (Book request) {
      if (Objects.isNull(request)) return Optional.empty();

      if (request.getBookId() == null)
          return Optional.empty();
      Book book = bookRepository.findById(request.getBookId()).orElseThrow(() -> new RuntimeException("Book not found"));

      if (book.getPrice() == request.getPrice()) return Optional.empty();
      book.setPrice(request.getPrice());
      book.setTitle(request.getTitle());
      book.setPublishedDate(request.getPublishedDate());

      return Optional.of(bookRepository.save(book));
  }

    public Book updatePrice (Long bookId, double updatedPrice) {
        if (bookId == null)
            return null;
        Book book = bookRepository.findBookByBookId(bookId);

        //if (book.getPrice() == updatedPrice) return null;

        book.setPrice(updatedPrice);

        return bookRepository.save(book);
    }

  public void deleteBookById (Long id) {
      bookRepository.deleteById(id);
  }

  public void deleteBook (Book book) {
      if (book.getPrice() > 500.0) return;
      bookRepository.delete(book);
  }

  public Book getBookByTitleAndPublishedDate(String title, Date localDate) {
        return bookRepository.findBookByTitleAndPublishedDate(title, localDate);
  }

  public Book getBookByTitleAndPriceAndIsDigital(String title, int price, boolean isDigital) {
        return bookRepository.findBookByTitleAndPriceAndIsDigital(title, price, isDigital);
  }


  public List<Book> findAllBooks () {
      return bookRepository.findAll();
  }

  public Book findBookById (Long id) {
      return bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
  }

    public List<Book> getNewBooksWithAppliedDiscount(int discountRate, int days){
        List<Book> newBooks = bookRepository.findNewBooks(days);
        for(Book book : newBooks){
            double price = book.getPrice();
            double newPrice = price - (discountRate * price / 100);
            book.setPrice(newPrice);
        }

        return newBooks;
    }

    public double calculateTotalCost(List<Long> bookIds) {
        double total = 0;
        for(Long bookId : bookIds){
            Book book = bookRepository.findBookByBookId(bookId);
            total = total + book.getPrice();
        }
        return total;
    }

    public double getTotalPriceOfBooks() {
        List<Book> books = null;
        try {
            books = bookRepository.findAll();
        } catch (NullPointerException e) {
            // log exception
            throw new DatabaseReadException("Unable to read from database due to - " + e.getMessage());
        }
        double totalPrice = 0;
        for(Book book : books){
            totalPrice = totalPrice + book.getPrice();
        }
        return totalPrice;
    }


    //used for spy tests
    public Book findBook(String bookId) {
        throw new RuntimeException("Method not implemented");
    }

    public double getAppliedDiscount(Book book, int discountRate) {
        double price = book.getPrice();
        double newPrice = price - (price * discountRate / 100);
        return newPrice;
    }

    public void addBooks(List<Book> books) {
        bookRepository.saveAllBooks(books);
    }




}
