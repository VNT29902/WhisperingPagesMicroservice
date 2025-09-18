package com.example.ProductService.Service;

import com.example.ProductService.ReportDashboardDto.*;

import java.util.List;

public interface ReportService {
    ReportOverviewResponse getOverview(int year, Integer month);
    List<MonthlySalesResponse> getMonthlySales(int year);
    List<WeeklySalesResponse> getWeeklySales(int year, int month);
    List<TopBookResponse> getTopBooks(int year, Integer month, int limit);
    List<SalesDetailResponse> getSalesDetail(int year, Integer month);
    List<CategorySalesResponse> getSalesByCategory(int year, Integer month);
}