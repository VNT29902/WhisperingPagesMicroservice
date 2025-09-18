package com.example.ProductService.ReportDashboardDto;

public class MonthlySalesResponse {
    private int month;
    private Long salesCount; // đổi từ int → Long

    public MonthlySalesResponse(int month, Long salesCount) {
        this.month = month;
        this.salesCount = salesCount;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public Long getSalesCount() {
        return salesCount;
    }

    public void setSalesCount(Long salesCount) {
        this.salesCount = salesCount;
    }

    // getters & setters
}
