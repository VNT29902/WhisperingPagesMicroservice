import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

// === Interfaces (matching DTO ở server) ===
export interface ReportOverviewResponse {
  totalSales: number;
  bestSellingBook: {
    bookId: string;
    title: string;
    salesCount: number;
  };
  topCategory: string;
  lastUpdatedAt: string;
}

export interface MonthlySalesResponse {
  month: number;
  salesCount: number;
}

export interface WeeklySalesResponse {
  week: number;
  salesCount: number;
}

export interface TopBookResponse {
  bookId: string;
  title: string;
  salesCount: number;
}

export interface SalesDetailResponse {
  bookId: string;
  title: string;
  year: number;
  month: number;
  week: number;
  salesCount: number;
  updatedAt: string;
}

export interface CategorySalesResponse {
  category: string;
  salesCount: number;
}

@Injectable({
  providedIn: 'root'
})
export class ReportsService {
 private apiUrl = `${environment.apiUrl}/api/reports`;

  constructor(private http: HttpClient) {}

  // 1. KPI Overview
  getOverview(year: number, month?: number, week?: number): Observable<ReportOverviewResponse> {
    let params = new HttpParams().set('year', year.toString());
    if (month !== undefined) params = params.set('month', month.toString());
    if (week !== undefined) params = params.set('week', week.toString());

    return this.http.get<ReportOverviewResponse>(`${this.apiUrl}/overview`, { params });
  }

  // 2. Sales per month
  getMonthlySales(year: number): Observable<MonthlySalesResponse[]> {
    const params = new HttpParams().set('year', year.toString());
    return this.http.get<MonthlySalesResponse[]>(`${this.apiUrl}/sales/monthly`, { params });
  }

  // 3. Sales per week
  getWeeklySales(year: number, month: number): Observable<WeeklySalesResponse[]> {
    const params = new HttpParams()
      .set('year', year.toString())
      .set('month', month.toString());

    return this.http.get<WeeklySalesResponse[]>(`${this.apiUrl}/sales/weekly`, { params });
  }

  // 4. Top N books
  getTopBooks(year: number, month?: number, limit: number = 5): Observable<TopBookResponse[]> {
    let params = new HttpParams()
      .set('year', year.toString())
      .set('limit', limit.toString());

    if (month !== undefined) params = params.set('month', month.toString());

    return this.http.get<TopBookResponse[]>(`${this.apiUrl}/sales/top-books`, { params });
  }

  // 5. Sales detail table
  getSalesDetail(year: number, month?: number): Observable<SalesDetailResponse[]> {
    let params = new HttpParams().set('year', year.toString());
    if (month !== undefined) params = params.set('month', month.toString());

    return this.http.get<SalesDetailResponse[]>(`${this.apiUrl}/sales/detail`, { params });
  }

  // 6. Sales by category
  getSalesByCategory(year: number, month?: number): Observable<CategorySalesResponse[]> {
    let params = new HttpParams().set('year', year.toString());
    if (month !== undefined) params = params.set('month', month.toString());

    return this.http.get<CategorySalesResponse[]>(`${this.apiUrl}/sales/by-category`, { params });
  }
}
