package com.swiftfingers.mockitotests.service;

import com.swiftfingers.mockitotests.entity.Book;

public class BookManager {

    private BookService bookService;

    public BookManager(BookService bookService) {
        this.bookService = bookService;
    }

    public BookManager () {}

    public double applyDiscountOnBook(String bookId, int discountRate) {
        Book book = bookService.findBook(bookId); // We need to Mock
        double discountedPrice = bookService.getAppliedDiscount(book, discountRate);
        return discountedPrice;
    }
}
