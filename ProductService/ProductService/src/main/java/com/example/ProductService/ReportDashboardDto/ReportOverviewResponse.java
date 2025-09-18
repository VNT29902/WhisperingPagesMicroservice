package com.example.ProductService.ReportDashboardDto;

import java.time.LocalDateTime;

public class ReportOverviewResponse {
    private int totalSales;
    private BestSellingBook bestSellingBook;
    private String topCategory;
    private LocalDateTime lastUpdatedAt;


    public ReportOverviewResponse() {
    }

    // nested DTO
    public static class BestSellingBook {
        private String bookId;
        private String title;
        private Long salesCount;


        public BestSellingBook(String bookId, String title, Long salesCount) {
            this.bookId = bookId;
            this.title = title;
            this.salesCount = salesCount;
        }

        public BestSellingBook() {
        }

        // getters & setters
        public String getBookId() { return bookId; }
        public void setBookId(String bookId) { this.bookId = bookId; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public Long getSalesCount() {
            return salesCount;
        }

        public void setSalesCount(Long salesCount) {
            this.salesCount = salesCount;
        }
    }

    // getters & setters
    public int getTotalSales() { return totalSales; }
    public void setTotalSales(int totalSales) { this.totalSales = totalSales; }

    public BestSellingBook getBestSellingBook() { return bestSellingBook; }
    public void setBestSellingBook(BestSellingBook bestSellingBook) { this.bestSellingBook = bestSellingBook; }

    public String getTopCategory() { return topCategory; }
    public void setTopCategory(String topCategory) { this.topCategory = topCategory; }

    public LocalDateTime getLastUpdatedAt() { return lastUpdatedAt; }
    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) { this.lastUpdatedAt = lastUpdatedAt; }
}

