package com.example.ProductService.Repository;

import com.example.ProductService.Entity.BookSalesStat;
import com.example.ProductService.ReportDashboardDto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookSalesStatRepository extends JpaRepository<BookSalesStat, Long> {

    // ✅ Tìm thống kê theo bookId + year + month + week
    @Query("SELECT s FROM BookSalesStat s " +
            "WHERE s.book.id = :bookId AND s.year = :year AND s.month = :month AND s.week = :week")
    Optional<BookSalesStat> findByBookIdAndYearAndMonthAndWeek(
            @Param("bookId") String bookId,
            @Param("year") int year,
            @Param("month") int month,
            @Param("week") int week);

    // ✅ Best selling books trong khoảng tháng
    @Query("""
        SELECT s.book
        FROM BookSalesStat s
        WHERE (s.year > :fromYear OR (s.year = :fromYear AND s.month >= :fromMonth))
          AND (s.year < :toYear OR (s.year = :toYear AND s.month <= :toMonth))
        GROUP BY s.book
        HAVING SUM(s.salesCount) >= :minSales
        ORDER BY SUM(s.salesCount) DESC
        """)
    List<com.example.ProductService.Entity.Book> findBestSellingBooksByMonthRangeAsBook(
            @Param("fromYear") int fromYear,
            @Param("fromMonth") int fromMonth,
            @Param("toYear") int toYear,
            @Param("toMonth") int toMonth,
            @Param("minSales") int minSales,
            Pageable pageable);

    // ✅ Tổng doanh số theo năm
    @Query("SELECT SUM(s.salesCount) FROM BookSalesStat s WHERE s.year = :year")
    Integer getTotalSalesByYear(@Param("year") int year);

    @Query("SELECT SUM(s.salesCount) FROM BookSalesStat s WHERE s.year = :year AND s.month = :month")
    Integer getTotalSalesByMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT MAX(s.updatedAt) FROM BookSalesStat s WHERE s.year = :year AND s.month = :month")
    LocalDateTime getLastUpdatedAtByMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT new com.example.ProductService.ReportDashboardDto.TopBookResponse(s.book.id, s.book.title, SUM(s.salesCount)) " +
            "FROM BookSalesStat s WHERE s.year = :year AND s.month = :month " +
            "GROUP BY s.book.id, s.book.title ORDER BY SUM(s.salesCount) DESC")
    List<TopBookResponse> getTopBooksByMonth(@Param("year") int year, @Param("month") int month, Pageable pageable);

    // ✅ Doanh số theo tháng
    @Query("SELECT new com.example.ProductService.ReportDashboardDto.MonthlySalesResponse(s.month, SUM(s.salesCount)) " +
            "FROM BookSalesStat s WHERE s.year = :year GROUP BY s.month ORDER BY s.month ASC")
    List<MonthlySalesResponse> getMonthlySales(@Param("year") int year);

    // ✅ Doanh số theo tuần
    @Query("SELECT new com.example.ProductService.ReportDashboardDto.WeeklySalesResponse(s.week, SUM(s.salesCount)) " +
            "FROM BookSalesStat s WHERE s.year = :year AND s.month = :month GROUP BY s.week ORDER BY s.week ASC")
    List<WeeklySalesResponse> getWeeklySales(@Param("year") int year, @Param("month") int month);

    // ✅ Top books theo năm (join sang Book để lấy title)
    @Query("SELECT new com.example.ProductService.ReportDashboardDto.TopBookResponse(s.book.id, s.book.title, SUM(s.salesCount)) " +
            "FROM BookSalesStat s WHERE s.year = :year " +
            "GROUP BY s.book.id, s.book.title ORDER BY SUM(s.salesCount) DESC")
    List<TopBookResponse> getTopBooksByYear(@Param("year") int year, Pageable pageable);

    @Query("SELECT new com.example.ProductService.ReportDashboardDto.TopBookResponse(s.book.id, s.book.title, SUM(s.salesCount)) " +
            "FROM BookSalesStat s WHERE s.year = :year AND s.month = :month " +
            "GROUP BY s.book.id, s.book.title ORDER BY SUM(s.salesCount) DESC")
    List<TopBookResponse> getTopBooksByYearAndMonth(@Param("year") int year,
                                                    @Param("month") int month,
                                                    Pageable pageable);


    // ✅ Chi tiết sales (year, month, week, count)
    @Query("SELECT new com.example.ProductService.ReportDashboardDto.SalesDetailResponse(s.book.id, s.book.title, s.year, s.month, s.week, s.salesCount, s.updatedAt) " +
            "FROM BookSalesStat s " +
            "WHERE s.year = :year AND (:month IS NULL OR s.month = :month)")
    List<SalesDetailResponse> getSalesDetail(@Param("year") int year, @Param("month") Integer month);

    // ✅ Doanh số theo category (join sang Book)
    @Query("SELECT new com.example.ProductService.ReportDashboardDto.CategorySalesResponse(s.book.category, SUM(s.salesCount)) " +
            "FROM BookSalesStat s WHERE s.year = :year " +
            "GROUP BY s.book.category ORDER BY SUM(s.salesCount) DESC")
    List<CategorySalesResponse> getSalesByCategory(@Param("year") int year);

    @Query("SELECT new com.example.ProductService.ReportDashboardDto.CategorySalesResponse(s.book.category, SUM(s.salesCount)) " +
            "FROM BookSalesStat s WHERE s.year = :year AND s.month = :month " +
            "GROUP BY s.book.category ORDER BY SUM(s.salesCount) DESC")
    List<CategorySalesResponse> getSalesByCategoryByMonth(@Param("year") int year,
                                                          @Param("month") int month);


    // ✅ Lần update cuối
    @Query("SELECT MAX(s.updatedAt) FROM BookSalesStat s WHERE s.year = :year")
    LocalDateTime getLastUpdatedAt(@Param("year") int year);
}
