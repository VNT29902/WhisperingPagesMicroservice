import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { CartResponse } from '../models/cart-response.model';
import { CartItemRequest } from '../models/cart-item-request.model';
import { CartItemDTO } from '../models/cart-item-dto.model';
import { ProductOfCart } from '../models/add-to-cart.model';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private apiUrl = `${environment.apiUrl}/api/cart`;

  constructor(private http: HttpClient) {}

  // ✅ Lấy giỏ hàng
  getCart(): Observable<CartResponse> {
    return this.http.get<CartResponse>(`${this.apiUrl}`);
  }

  // ✅ Thêm sản phẩm vào giỏ
  addToCart(item: CartItemRequest): Observable<string> {
    return this.http.post(`${this.apiUrl}/items`, item, {
      responseType: 'text'
    });
  }

  // ✅ Cập nhật số lượng sản phẩm
  updateItemQuantity(productId: string, quantity: number): Observable<string> {
    const params = new HttpParams().set('quantity', quantity.toString());
    return this.http.put(`${this.apiUrl}/item/${productId}`, null, {
      params,
      responseType: 'text'
    });
  }

  // ✅ Xoá 1 sản phẩm
  removeItem(productId: string): Observable<string> {
    return this.http.delete(`${this.apiUrl}/item/${productId}`, {
      responseType: 'text'
    });
  }

  // ✅ Xoá toàn bộ giỏ
  clearCart(): Observable<string> {
    return this.http.delete(`${this.apiUrl}`, {
      responseType: 'text'
    });
  }

  // ✅ Thanh toán
  checkout(items: ProductOfCart[]): Observable<string> {
    return this.http.post(`${this.apiUrl}/checkout`, items, {
      responseType: 'text'
    });
  }

  // ✅ Xoá nhiều sản phẩm sau khi đặt hàng thành công
  deleteItemsAfterOrder(items: CartItemDTO[]): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/me/items`, {
      body: items
    });
  }
}
