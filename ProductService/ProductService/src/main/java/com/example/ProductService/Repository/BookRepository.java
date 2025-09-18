package com.example.ProductService.Repository;

import com.example.ProductService.Entity.Book;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {

    boolean existsByTitleAndAuthorAndCategory(String title, String author, String category);
    boolean existsByCategory(String category);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({
            @QueryHint(name = "javax.persistence.lock.timeout", value = "5000")  // 5 giây timeout
    })
    @Query("SELECT b FROM Book b WHERE b.id = :id")
    Optional<Book> findByIdForUpdate(@Param("id") String id);  // changed UUID -> String

    @Query("""
    SELECT b FROM Book b
    WHERE (:keyword IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')))
      AND (:category IS NULL OR b.category = :category)
      AND (:minPrice IS NULL OR b.price >= :minPrice)
      AND (:maxPrice IS NULL OR b.price <= :maxPrice)
""")
    Page<Book> searchBooksAdmin(
            @Param("keyword") String keyword,
            @Param("category") String category,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable);



    Page<Book> findByCategory(String category, Pageable pageable);

    List<Book> findByCategory(String category);

    List<Book> findByAuthor(String author);
    List<Book> findByCategoryAndAuthor(String category, String author);

    List<Book> findTop3ByOrderByCreatedAtDesc();

    List<Book> findTop5ByOrderBySalesCountDesc();

    List<Book> findTop10ByOrderByCreatedAtDesc();






}
