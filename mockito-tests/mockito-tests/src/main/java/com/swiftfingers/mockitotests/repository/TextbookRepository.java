package com.swiftfingers.mockitotests.repository;

import com.swiftfingers.mockitotests.entity.Textbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TextbookRepository extends JpaRepository<Textbook, Long> {
    List<Textbook> findByTitle(String title);

    // Custom Query
    @Query("SELECT b FROM Textbook b WHERE b.publishDate > :date")
    List<Textbook> findByPublishedDateAfter(@Param("date") LocalDate date);
}
