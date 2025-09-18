import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Order, OrderItemDTO, OrderItemResponse } from '../models/order.model';
import {
  ShippingInfoResponse,
  ShippingInfoDto,
} from '../models/shipping.model';
import { CreateOrderRequest } from '../models/create.orders.request';
import { OrderWithItemsResponse } from '../models/orders-purchase';
import { PageResponse } from '../models/page-response.model';
import { OrderResponse } from '../models/order-response.model';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  private apiUrl = `${environment.apiUrl}/api/orders`;

  constructor(private http: HttpClient) {}

  // ✅ Tạo đơn hàng
  createOrder(
    userName: string,
    req: CreateOrderRequest
  ): Observable<OrderResponse> {
    const headers = new HttpHeaders().set('X-User-Name', userName);
    return this.http.post<OrderResponse>(`${this.apiUrl}`, req, { headers });
  }

  getOrdersByUserName(userName: string) {
    const headers = new HttpHeaders().set('X-User-Name', userName);
    return this.http.get<OrderWithItemsResponse[]>(`${this.apiUrl}`, {
      headers,
    });
  }

  getOrderWithItemsById(orderId: string): Observable<OrderWithItemsResponse> {
  return this.http.get<OrderWithItemsResponse>(`${this.apiUrl}/${orderId}`);
}


  getAllOrders(
    page: number = 0,
    size: number = 10
  ): Observable<PageResponse<OrderWithItemsResponse>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<PageResponse<OrderWithItemsResponse>>(
      `${this.apiUrl}/admin`,
      { params }
    );
  }

  // ✅ Xoá tất cả order của user
  deleteOrdersByUserName(userName: string): Observable<string> {
    const params = new HttpParams().set('userName', userName);
    return this.http.delete<string>(`${this.apiUrl}`, { params });
  }

  // ✅ Đánh dấu đơn hàng là đã thanh toán
  markOrderAsPaid(orderId: string): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${orderId}/mark-paid`, {});
  }

  // ✅ Thêm thông tin giao hàng
  addShippingInfo(
    orderId: string,
    shippingInfo: ShippingInfoDto
  ): Observable<ShippingInfoResponse> {
    return this.http.post<ShippingInfoResponse>(
      `${this.apiUrl}/add/${orderId}`,
      shippingInfo
    );
  }

  // ✅ Lấy thông tin giao hàng theo orderId
  getShippingInfo(orderId: string): Observable<ShippingInfoResponse> {
    return this.http.get<ShippingInfoResponse>(
      `${this.apiUrl}/order/${orderId}`
    );
  }

  getShippingInfoDashboard(orderId: string): Observable<ShippingInfoResponse> {
    return this.http.get<ShippingInfoResponse>(
      `${this.apiUrl}/${orderId}/shipping-info`
    );
  }

}
