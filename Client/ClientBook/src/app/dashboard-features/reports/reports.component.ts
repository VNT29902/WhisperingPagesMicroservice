import { Component, Inject, OnInit, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgxEchartsModule } from 'ngx-echarts';
import { EChartsOption } from 'echarts';
import { forkJoin } from 'rxjs';

import {
  ReportsService,
  ReportOverviewResponse,
  MonthlySalesResponse,
  WeeklySalesResponse,
  TopBookResponse,
  SalesDetailResponse,
  CategorySalesResponse,
} from '../../services/reports.service';

@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [CommonModule, FormsModule, NgxEchartsModule],
  templateUrl: './reports.component.html',
  styleUrl: './reports.component.css',
})
export class ReportsComponent implements OnInit {
  year: number = new Date().getFullYear();
  month?: number;
  isBrowser = false;

  // Pagination
  currentPage = 1;
  pageSize = 10;
  pagedSalesDetail: SalesDetailResponse[] = [];

  overview?: ReportOverviewResponse;
  monthlySales: MonthlySalesResponse[] = [];
  weeklySales: WeeklySalesResponse[] = [];
  topBooks: TopBookResponse[] = [];
  salesDetail: SalesDetailResponse[] = [];
  salesByCategory: CategorySalesResponse[] = [];

  loading = true;

  // Chart options (base + merge)
  baseMonthlyOptions: EChartsOption = {
    xAxis: { type: 'category' },
    yAxis: { type: 'value' },
    series: [{ type: 'bar', data: [] }],
  };
  baseWeeklyOptions: EChartsOption = {
    xAxis: { type: 'category' },
    yAxis: { type: 'value' },
    series: [{ type: 'line', data: [] }],
  };
  baseTopBooksOptions: EChartsOption = {
    xAxis: { type: 'category' },
    yAxis: { type: 'value' },
    series: [{ type: 'bar', data: [] }],
  };
  baseCategoryOptions: EChartsOption = {
    tooltip: { trigger: 'item' },
    series: [{ type: 'pie', radius: '60%', data: [] }],
  };

  monthlyChartOptions: EChartsOption = {};
  weeklyChartOptions: EChartsOption = {};
  topBooksChartOptions: EChartsOption = {};
  categoryChartOptions: EChartsOption = {};

  constructor(
    private reportsService: ReportsService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    this.isBrowser = isPlatformBrowser(this.platformId);
  }

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.loading = true;

    const requests = {
      overview: this.reportsService.getOverview(this.year, this.month),
      monthly: this.reportsService.getMonthlySales(this.year),
      topBooks: this.reportsService.getTopBooks(this.year, this.month, 5),
      salesDetail: this.reportsService.getSalesDetail(this.year, this.month),
      salesByCategory: this.reportsService.getSalesByCategory(this.year, this.month),
      ...(this.month
        ? { weekly: this.reportsService.getWeeklySales(this.year, this.month) }
        : {}),
    };

    forkJoin(requests).subscribe(
      (res: {
        overview: ReportOverviewResponse;
        monthly: MonthlySalesResponse[];
        topBooks: TopBookResponse[];
        salesDetail: SalesDetailResponse[];
        salesByCategory: CategorySalesResponse[];
        weekly?: WeeklySalesResponse[];
      }) => {
        // reset dữ liệu
        this.overview = res.overview ?? undefined;
        this.monthlySales = res.monthly ?? [];
        this.topBooks = res.topBooks ?? [];
        this.salesDetail = res.salesDetail ?? [];
        this.salesByCategory = res.salesByCategory ?? [];
        this.weeklySales = res.weekly ?? [];

        // reset về page 1
        this.currentPage = 1;
        this.updatePagedSalesDetail();

        // cập nhật chart
        this.setMonthlyChart();
        this.setTopBooksChart();
        this.setCategoryChart();
        this.setWeeklyChart();

        this.loading = false;
      }
    );
  }

  applyFilters(): void {
    if (!this.year || this.year < 2000 || this.year > 2100) {
      alert('Vui lòng nhập năm hợp lệ');
      return;
    }
    if (this.month && (this.month < 1 || this.month > 12)) {
      alert('Vui lòng nhập tháng hợp lệ');
      return;
    }
    this.loadData();
  }

  resetFilters(): void {
    this.year = new Date().getFullYear();
    this.month = undefined;
    this.loadData();
  }

  // Pagination
  private updatePagedSalesDetail() {
    const start = (this.currentPage - 1) * this.pageSize;
    const end = start + this.pageSize;
    this.pagedSalesDetail = this.salesDetail.slice(start, end);
  }

  goToPage(page: number) {
    if (page < 1 || page > this.totalPages) return;
    this.currentPage = page;
    this.updatePagedSalesDetail();
  }

  get totalPages(): number {
    return Math.ceil(this.salesDetail.length / this.pageSize);
  }

  // Charts
  private setMonthlyChart() {
    this.monthlyChartOptions = {
      xAxis: { data: this.monthlySales.map((m) => `M${m.month}`) },
      series: [
        {
          type: 'bar',
          data: this.monthlySales.map((m) => m.salesCount),
          color: '#42a5f5',
        },
      ],
    };
  }

  private setWeeklyChart() {
    this.weeklyChartOptions = {
      xAxis: { data: this.weeklySales.map((w) => `W${w.week}`) },
      series: [
        {
          type: 'line',
          data: this.weeklySales.map((w) => w.salesCount),
          smooth: true,
          color: '#66bb6a',
        },
      ],
    };
  }

  private setTopBooksChart() {
    this.topBooksChartOptions = {
      tooltip: {
        trigger: 'axis',
        axisPointer: { type: 'shadow' },
        formatter: (params: any) => {
          const item = params[0];
          return `${item.name}<br/>Sales: ${item.value}`;
        },
      },
      xAxis: {
        type: 'category',
        data: this.topBooks.map((b) => b.title),
        axisLabel: {
          interval: 0,
          rotate: 20,
          fontSize: 12,
        },
      },
      yAxis: { type: 'value' },
      series: [
        {
          name: 'Sales Count',
          type: 'bar',
          data: this.topBooks.map((b) => b.salesCount),
          itemStyle: { color: '#ffa726' },
        },
      ],
    };
  }

  private setCategoryChart() {
    this.categoryChartOptions = {
      tooltip: { trigger: 'item' },
      series: [
        {
          type: 'pie',
          radius: '60%',
          data: this.salesByCategory.map((c) => ({
            value: c.salesCount,
            name: c.category,
          })),
        },
      ],
    };
  }
}
