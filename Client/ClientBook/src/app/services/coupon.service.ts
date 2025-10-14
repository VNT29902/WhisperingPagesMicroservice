import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { CouponValidationResponse } from '../models/coupon.model';

@Injectable({
  providedIn: 'root',
})
export class CouponService {
  private apiUrl = `${environment.apiUrl}/api/coupons`;

  constructor(private http: HttpClient) {}

  validateCoupon(
    userName: string,
    code: string,
    subtotal: number
  ): Observable<CouponValidationResponse> {
    const headers = new HttpHeaders().set('X-User-Name', userName);
    return this.http.post<CouponValidationResponse>(
      `${this.apiUrl}/validate`,
      { code, subtotal },
      { headers }
    );
  }
}
