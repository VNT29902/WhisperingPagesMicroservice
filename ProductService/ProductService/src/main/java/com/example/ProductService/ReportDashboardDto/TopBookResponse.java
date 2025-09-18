package com.example.ProductService.ReportDashboardDto;

public class TopBookResponse {
    private String bookId;
    private String title;
    private Long salesCount; // đổi từ int → Long

    public TopBookResponse(String bookId, String title, Long salesCount) {
        this.bookId = bookId;
        this.title = title;
        this.salesCount = salesCount;
    }

    // getters & setters



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