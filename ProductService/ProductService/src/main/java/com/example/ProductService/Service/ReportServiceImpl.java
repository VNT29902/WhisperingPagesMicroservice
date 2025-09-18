package com.example.ProductService.Service;

import com.example.ProductService.ReportDashboardDto.*;
import com.example.ProductService.Repository.BookSalesStatRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private final BookSalesStatRepository repository;

    public ReportServiceImpl(BookSalesStatRepository repository) {
        this.repository = repository;
    }

    @Override
    public ReportOverviewResponse getOverview(int year, Integer month) {
        ReportOverviewResponse res = new ReportOverviewResponse();

        // ✅ Tổng doanh số
        Integer totalSales;
        if (month != null) {
            totalSales = repository.getTotalSalesByMonth(year, month);
        } else {
            totalSales = repository.getTotalSalesByYear(year);
        }
        res.setTotalSales(totalSales != null ? totalSales : 0);

        // ✅ Last update
        LocalDateTime lastUpdated;
        if (month != null) {
            lastUpdated = repository.getLastUpdatedAtByMonth(year, month);
        } else {
            lastUpdated = repository.getLastUpdatedAt(year);
        }
        res.setLastUpdatedAt(lastUpdated);

        // ✅ Best-selling book
        List<TopBookResponse> topBooks;
        if (month != null) {
            topBooks = repository.getTopBooksByMonth(year, month, PageRequest.of(0, 1));
        } else {
            topBooks = repository.getTopBooksByYear(year, PageRequest.of(0, 1));
        }

        if (!topBooks.isEmpty()) {
            TopBookResponse top = topBooks.get(0);
            ReportOverviewResponse.BestSellingBook best = new ReportOverviewResponse.BestSellingBook();
            best.setBookId(top.getBookId());
            best.setTitle(top.getTitle());
            best.setSalesCount(top.getSalesCount());
            res.setBestSellingBook(best);
        }

        // ✅ Top category
        List<CategorySalesResponse> categories;
        if (month != null) {
            categories = repository.getSalesByCategoryByMonth(year, month);
        } else {
            categories = repository.getSalesByCategory(year);
        }

        if (!categories.isEmpty()) {
            res.setTopCategory(categories.get(0).getCategory());
        } else {
            res.setTopCategory("N/A");
        }

        return res;
    }


    @Override
    public List<MonthlySalesResponse> getMonthlySales(int year) {
        return repository.getMonthlySales(year);
    }

    @Override
    public List<WeeklySalesResponse> getWeeklySales(int year, int month) {
        return repository.getWeeklySales(year, month);
    }

    @Override
    public List<TopBookResponse> getTopBooks(int year, Integer month, int limit) {
        if (month != null) {
            return repository.getTopBooksByYearAndMonth(year, month, PageRequest.of(0, limit));
        } else {
            return repository.getTopBooksByYear(year, PageRequest.of(0, limit));
        }
    }


    @Override
    public List<SalesDetailResponse> getSalesDetail(int year, Integer month) {
        return repository.getSalesDetail(year, month);
    }

    @Override
    public List<CategorySalesResponse> getSalesByCategory(int year, Integer month) {
        if (month != null) {
            return repository.getSalesByCategoryByMonth(year, month);
        } else {
            return repository.getSalesByCategory(year);
        }
    }

}
