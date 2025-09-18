import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { tap } from 'rxjs/operators';

import { Book } from '../models/book.model'; // interface Book
import { environment } from '../../environments/environment';
import { Bookdetail } from '../models/bookdetail.model';
import { BookAndPromotion } from '../models/bookAndPromotion.model';
import { PageResponse } from '../models/page-response.model';
import { PromotionSaveRequest } from '../models/promotion-save-request.model';
import { PromotionSaveResponse } from '../models/promotion-save-response.model';
import { PromotionListResponse } from '../models/promotion-list-response.model';
import { PromotionUpdateRequest } from '../models/promotion-update-request.model';
import { PromotionStatusUpdateRequest } from '../models/promotion-status-update-request.model';

@Injectable({
  providedIn: 'root',
})
export class BookService {
  private apiUrl = `${environment.apiUrl}/api/products`;

  private _latestCache: BookAndPromotion[] | null = null;
  private _categoryCache = new Map<string, PageResponse<BookAndPromotion>>();

  constructor(private http: HttpClient) {}

  // 🟢 POST /api/products
  createBooks(bookDtos: Book[]): Observable<Book[]> {
    return this.http.post<Book[]>(this.apiUrl, bookDtos);
  }

  uploadExcel(file: File): Observable<Book[]> {
    const formData = new FormData();
    formData.append('file', file);

    return this.http.post<Book[]>(`${this.apiUrl}/upload-excel`, formData);
  }

  // 🔵 GET /api/products/{id}
  getBookById(id: string): Observable<BookAndPromotion> {
    return this.http.get<BookAndPromotion>(`${this.apiUrl}/${id}`);
  }

  // Get best selling has 5 product
  getBestSelling(): Observable<BookAndPromotion[]> {
    return this.http.get<BookAndPromotion[]>(`${this.apiUrl}/best-selling`);
  }

  // 🔵 GET /api/products?category=...
  getBooksByCategory(
    category: string = 'all',
    limit: number = 12,
    page: number = 0
  ): Observable<PageResponse<BookAndPromotion>> {
    const key = `cat=${category}&limit=${limit}&page=${page}`;

    if (this._categoryCache.has(key)) {
      return of(
        this._categoryCache.get(key)! as PageResponse<BookAndPromotion>
      );
    }

    const params = new HttpParams()
      .set('category', category)
      .set('limit', limit.toString())
      .set('page', page.toString());

    return this.http
      .get<PageResponse<BookAndPromotion>>(this.apiUrl, { params })
      .pipe(tap((data) => this._categoryCache.set(key, data)));
  }

  // 🔄 PUT /api/products/{id}
  updateBook(id: string, bookDto: Book): Observable<Book> {
    return this.http.put<Book>(`${this.apiUrl}/${id}`, bookDto);
  }

  // ❌ DELETE /api/products/{id}
  deleteBook(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // 🟢 POST /api/products/{id}/order?quantity=...
  placeOrder(id: string, quantity: number): Observable<string> {
    const params = new HttpParams().set('quantity', quantity.toString());
    return this.http.post(`${this.apiUrl}/${id}/order`, null, {
      responseType: 'text',
      params,
    });
  }

  // 🔎 GET /api/products/{id}/checkStock?quantity=...
  checkStock(id: string, quantity: number): Observable<string> {
    const params = new HttpParams().set('quantity', quantity.toString());
    return this.http.get(`${this.apiUrl}/${id}/checkStock`, {
      responseType: 'text',
      params,
    });
  }

  // 🔄 PUT /api/products/{id}/release?quantity=...
  releaseStock(id: string, quantity: number): Observable<string> {
    const params = new HttpParams().set('quantity', quantity.toString());
    return this.http.put(`${this.apiUrl}/${id}/release`, null, {
      responseType: 'text',
      params,
    });
  }

  // 🔍 GET /api/products?category=...&author=...
  searchBooks(
    keyword: string,
    page: number = 0,
    size: number = 10
  ): Observable<PageResponse<BookAndPromotion>> {
    let params = new HttpParams()
      .set('keyword', keyword)
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<PageResponse<BookAndPromotion>>(
      `${this.apiUrl}/search`,
      { params }
    );
  }

  //Get/api/searchAdmin

  searchBooksAdmin(
    keyword: string = '',
    category: string = '',
    minPrice?: number,
    maxPrice?: number,
    page: number = 0,
    size: number = 10
  ): Observable<PageResponse<BookAndPromotion>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (keyword) {
      params = params.set('keyword', keyword);
    }
    if (category) {
      params = params.set('category', category);
    }
    if (minPrice !== undefined) {
      params = params.set('minPrice', minPrice.toString());
    }
    if (maxPrice !== undefined) {
      params = params.set('maxPrice', maxPrice.toString());
    }

    return this.http.get<PageResponse<BookAndPromotion>>(
      `${this.apiUrl}/admin/search`,
      { params }
    );
  }

  // 💵 GET /api/products/getPrice/{id}
  getPriceByBookId(id: string): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/getPrice/${id}`);
  }
  getLatestBooks(): Observable<BookAndPromotion[]> {
    if (this._latestCache) return of(this._latestCache);

    return this.http
      .get<BookAndPromotion[]>(`${this.apiUrl}/latest`)
      .pipe(tap((data) => (this._latestCache = data)));
  }

  clearAllCache() {
    this._latestCache = null;
    this._categoryCache.clear();
  }

  // promotion

  // 1. Create promotion
  createPromotion(
    req: PromotionSaveRequest
  ): Observable<PromotionSaveResponse> {
    return this.http.post<PromotionSaveResponse>(`${this.apiUrl}/promotions`, req);
  }

 getPromotions(
  page: number = 0,
  size: number = 10,
  active?: boolean
): Observable<PageResponse<PromotionListResponse>> {
  let params = new HttpParams()
    .set('page', page.toString())
    .set('size', size.toString());

  if (active !== undefined) {
    params = params.set('active', active.toString());
  }

  return this.http.get<PageResponse<PromotionListResponse>>(
    `${this.apiUrl}/promotions`, // ✅ thêm /promotions
    { params }
  );
}

  deletePromotion(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/promotions/${id}`);
  }


  updatePromotion(
    id: number,
    req: PromotionUpdateRequest
  ): Observable<PromotionSaveResponse> {
    return this.http.post<PromotionSaveResponse>(`${this.apiUrl}/promotions/${id}`, req);
  }

 updatePromotionStatus(
  id: number,
  req: PromotionStatusUpdateRequest
): Observable<string> {
  return this.http.post(`${this.apiUrl}/promotions/${id}/status`, req, {
    responseType: 'text'  // 👈 để Angular không parse JSON
  });
}

getPromotionById(id: number): Observable<PromotionSaveResponse> {
  return this.http.get<PromotionSaveResponse>(`${this.apiUrl}/promotions/${id}`);
}


}
