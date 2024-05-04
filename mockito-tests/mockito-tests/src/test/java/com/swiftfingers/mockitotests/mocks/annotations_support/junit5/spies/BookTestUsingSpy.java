package com.swiftfingers.mockitotests.mocks.annotations_support.junit5.spies;

import com.swiftfingers.mockitotests.entity.Book;
import com.swiftfingers.mockitotests.service.BookManager;
import com.swiftfingers.mockitotests.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookTestUsingSpy {

}
