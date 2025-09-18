package com.example.ProductService.ReportDashboardDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SalesDetailResponse {
    private String bookId;
    private String title;
    private int year;
    private int month;
    private int week;
    private int salesCount;
    private LocalDateTime updatedAt;


    public SalesDetailResponse(String bookId, String title, int year, int month, int week, int salesCount, LocalDateTime updatedAt) {
        this.bookId = bookId;
        this.title = title;
        this.year = year;
        this.month = month;
        this.week = week;
        this.salesCount = salesCount;
        this.updatedAt = updatedAt;
    }

    // getters & setters
    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }

    public int getWeek() { return week; }
    public void setWeek(int week) { this.week = week; }

    public int getSalesCount() { return salesCount; }
    public void setSalesCount(int salesCount) { this.salesCount = salesCount; }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}