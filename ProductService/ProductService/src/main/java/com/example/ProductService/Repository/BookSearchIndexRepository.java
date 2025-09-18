package com.example.ProductService.Repository;

import com.example.ProductService.Entity.BookSearchIndex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookSearchIndexRepository extends JpaRepository<BookSearchIndex, Long> {

    @Query("""
    SELECT s.bookId
    FROM BookSearchIndex s
    WHERE s.titleNoAccent LIKE %:keyword%
       OR s.authorNoAccent LIKE %:keyword%
    """)
    Page<String> searchBookIds(@Param("keyword") String keyword, Pageable pageable);


    @Query("""
        SELECT COUNT(s)
        FROM BookSearchIndex s
        WHERE s.titleNoAccent LIKE %:keyword%
           OR s.authorNoAccent LIKE %:keyword%
        """)
    long countByKeyword(@Param("keyword") String keyword);

    boolean existsByBookId(String id);
}


