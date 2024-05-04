package com.swiftfingers.mockitotests.mocks.annotations_support.junit5.jpaunit_test;

import com.swiftfingers.mockitotests.entity.Textbook;
import com.swiftfingers.mockitotests.repository.TextbookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @DataJpaTest
 * 1. It scans the `@Entity` classes and Spring Data JPA repositories.
 * 2. Set the `spring.jpa.show-sql` property to true and enable the SQL queries logging.
 * 3. Default, JPA test data are transactional and roll back at the end of each test;
 * it means we do not need to clean up saved or modified table data after each test.
 * 4. Replace the application DataSource, run and configure the embedded database on classpath.
 */
@DataJpaTest
public class TextbookRepoTests {
    // Alternative for EntityManager
    // Optional in this case, we can use bookRepository to do the same stuff
    @Autowired
    private TestEntityManager testEM;

    @Autowired
    private TextbookRepository bookRepository;

    @Test
    public void testSave() {

        Textbook b1 = new Textbook("Book A",
                BigDecimal.valueOf(9.99),
                LocalDate.of(2023, 8, 31));

        //testEM.persistAndFlush(b1); the same
        bookRepository.save(b1);

        Long savedBookID = b1.getId();

        Textbook book = bookRepository.findById(savedBookID).orElseThrow();
        // Textbook book = testEM.find(Textbook.class, savedBookID);

        assertEquals(savedBookID, book.getId());
        assertEquals("Book A", book.getTitle());
        assertEquals(BigDecimal.valueOf(9.99), book.getPrice());
        assertEquals(LocalDate.of(2023, 8, 31), book.getPublishDate());


    }

    @Test
    public void testUpdate() {

        Textbook b1 = new Textbook("Book A",
                BigDecimal.valueOf(9.99),
                LocalDate.of(2023, 8, 31));

        //testEM.persistAndFlush(b1);
        bookRepository.save(b1);

        // update price from 9.99 to 19.99
        b1.setPrice(BigDecimal.valueOf(19.99));

        // update
        bookRepository.save(b1);

        List<Textbook> result = bookRepository.findByTitle("Book A");

        assertEquals(1, result.size());

        Textbook book = result.get(0);
        assertNotNull(book.getId());
        assertTrue(book.getId() > 0);

        assertEquals("Book A", book.getTitle());
        assertEquals(BigDecimal.valueOf(19.99), book.getPrice());
        assertEquals(LocalDate.of(2023, 8, 31), book.getPublishDate());


    }

    @Test
    public void testFindByTitle() {

        Textbook b1 = new Textbook("Book A",
                BigDecimal.valueOf(9.99),
                LocalDate.of(2023, 8, 31));
        bookRepository.save(b1);

        List<Textbook> result = bookRepository.findByTitle("Book A");

        assertEquals(1, result.size());
        Textbook book = result.get(0);
        assertNotNull(book.getId());
        assertTrue(book.getId() > 0);

        assertEquals("Book A", book.getTitle());
        assertEquals(BigDecimal.valueOf(9.99), book.getPrice());
        assertEquals(LocalDate.of(2023, 8, 31), book.getPublishDate());

    }

    @Test
    public void testFindAll() {

        Textbook b1 = new Textbook("Book A",
                BigDecimal.valueOf(9.99),
                LocalDate.of(2023, 8, 31));
        Textbook b2 = new Textbook("Book B",
                BigDecimal.valueOf(19.99),
                LocalDate.of(2023, 7, 31));
        Textbook b3 = new Textbook("Book C",
                BigDecimal.valueOf(29.99),
                LocalDate.of(2023, 6, 10));
        Textbook b4 = new Textbook("Book D",
                BigDecimal.valueOf(39.99),
                LocalDate.of(2023, 5, 5));

        bookRepository.saveAll(List.of(b1, b2, b3, b4));

        List<Textbook> result = bookRepository.findAll();
        assertEquals(4, result.size());

    }

    @Test
    public void testFindByPublishedDateAfter() {

        Textbook b1 = new Textbook("Book A",
                BigDecimal.valueOf(9.99),
                LocalDate.of(2023, 8, 31));
        Textbook b2 = new Textbook("Book B",
                BigDecimal.valueOf(19.99),
                LocalDate.of(2023, 7, 31));
        Textbook b3 = new Textbook("Book C",
                BigDecimal.valueOf(29.99),
                LocalDate.of(2023, 6, 10));
        Textbook b4 = new Textbook("Book D",
                BigDecimal.valueOf(39.99),
                LocalDate.of(2023, 5, 5));

        bookRepository.saveAll(List.of(b1, b2, b3, b4));

        List<Textbook> result = bookRepository.findByPublishedDateAfter(
                LocalDate.of(2023, 7, 1));

        // b1 and b2
        assertEquals(2, result.size());

    }

    @Test
    public void testDeleteById() {

        Textbook b1 = new Textbook("Book A",
                BigDecimal.valueOf(9.99),
                LocalDate.of(2023, 8, 31));
        bookRepository.save(b1);

        Long savedBookID = b1.getId();

        // Book book = bookRepository.findById(savedBookID).orElseThrow();
        // Book book = testEM.find(Book.class, savedBookID);

        bookRepository.deleteById(savedBookID);

        Optional<Textbook> result = bookRepository.findById(savedBookID);
        assertTrue(result.isEmpty());

    }
}
