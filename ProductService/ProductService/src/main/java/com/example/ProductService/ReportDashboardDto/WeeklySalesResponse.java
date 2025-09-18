package com.example.ProductService.ReportDashboardDto;

public class WeeklySalesResponse {
    private int week;
    private Long salesCount;

    public WeeklySalesResponse(int week, Long salesCount) {
        this.week = week;
        this.salesCount = salesCount;
    }

    // getters & setters
    public int getWeek() { return week; }
    public void setWeek(int week) { this.week = week; }

    public Long getSalesCount() { return salesCount; }
    public void setSalesCount(Long salesCount) { this.salesCount = salesCount; }
}
