package com.swiftfingers.mockitotests.repository;

import com.swiftfingers.mockitotests.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

//Used as a mock Repository to test with Mockito
public interface BookRepository {
    List<Book> findNewBooks(int days);

    Book save(Book book);
    Book findBookByBookId(Long id);
    Book findBookById(String bookId);
    Book findBookByTitleAndPublishedDate(String title, Date date);
    Book findBookByTitleAndPriceAndIsDigital(String title, int price, boolean isDigital);
    void saveAllBooks(List<Book> books);

    Optional<Book> findById(Long id);

    void delete(Book book);

    void deleteById(Long id);
    List<Book> findAll();
}
