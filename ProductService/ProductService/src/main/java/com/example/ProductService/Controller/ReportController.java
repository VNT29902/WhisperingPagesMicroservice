package com.example.ProductService.Controller;


import com.example.ProductService.ReportDashboardDto.*;
import com.example.ProductService.Service.ReportService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // 1. KPI Overview
    @GetMapping("/overview")
    public ReportOverviewResponse getOverview(
            @RequestParam int year,
            @RequestParam(required = false) Integer month

    ) {
        return reportService.getOverview(year, month);
    }

    // 2. Sales per month
    @GetMapping("/sales/monthly")
    public List<MonthlySalesResponse> getMonthlySales(@RequestParam int year) {
        return reportService.getMonthlySales(year);
    }

    // 3. Sales per week
    @GetMapping("/sales/weekly")
    public List<WeeklySalesResponse> getWeeklySales(
            @RequestParam int year,
            @RequestParam int month
    ) {
        return reportService.getWeeklySales(year, month);
    }

    // 4. Top N books
    @GetMapping("/sales/top-books")
    public List<TopBookResponse> getTopBooks(
            @RequestParam int year,
            @RequestParam(required = false) Integer month,
            @RequestParam(defaultValue = "5") int limit
    ) {
        return reportService.getTopBooks(year, month, limit);
    }

    // 5. Sales detail table
    @GetMapping("/sales/detail")
    public List<SalesDetailResponse> getSalesDetail(
            @RequestParam int year,
            @RequestParam(required = false) Integer month
    ) {
        return reportService.getSalesDetail(year, month);
    }

    // 6. Sales by category (optional)
    @GetMapping("/sales/by-category")
    public List<CategorySalesResponse> getSalesByCategory(
            @RequestParam int year,
            @RequestParam(required = false) Integer month
    ) {
        return reportService.getSalesByCategory(year, month);
    }
}
