package com.example.ProductService.ReportDashboardDto;

public class CategorySalesResponse {
    private String category;
    private Long salesCount;

    public CategorySalesResponse(String category, Long salesCount) {
        this.category = category;
        this.salesCount = salesCount;
    }

    public CategorySalesResponse() {
    }

    // getters & setters
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Long getSalesCount() { return salesCount; }
    public void setSalesCount(Long salesCount) { this.salesCount = salesCount; }
}
