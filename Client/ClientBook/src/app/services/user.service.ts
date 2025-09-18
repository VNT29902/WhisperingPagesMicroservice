import { ShippingAddressResponse } from './../models/shipping-address-response.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserCreateRequest } from '../models/user-create-request.model';
import { UserResponse } from '../models/user-response.model';
import { ShippingAddressRequest } from '../models/shipping-address-request.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiUrl = `${environment.apiUrl}/api/users`;

  constructor(private http: HttpClient) {}

  // ===== USER =====

  createUser(request: UserCreateRequest): Observable<UserResponse> {
    return this.http.post<UserResponse>(this.apiUrl, request);
  }

  getUserById(id: string): Observable<UserResponse> {
    return this.http.get<UserResponse>(`${this.apiUrl}/${id}`);
  }

  getUserByUsername(userName: string): Observable<UserResponse> {
    const headers = new HttpHeaders({ 'X-User-Name': userName });
    return this.http.get<UserResponse>(this.apiUrl, { headers });
  }

  updateUser(id: string, request: UserCreateRequest): Observable<UserResponse> {
    return this.http.put<UserResponse>(`${this.apiUrl}/${id}`, request);
  }

  deleteUser(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // ===== SHIPPING ADDRESS =====

  getShippingAddresses(
    userName: string
  ): Observable<ShippingAddressResponse[]> {
    const headers = new HttpHeaders({ 'X-User-Name': userName });
    return this.http.get<ShippingAddressResponse[]>(`${this.apiUrl}/shipping`, {
      headers,
    });
  }

  createShippingAddress(
    userName: string,
    request: ShippingAddressRequest
  ): Observable<ShippingAddressResponse> {
    const headers = new HttpHeaders({ 'X-User-Name': userName });
    return this.http.post<ShippingAddressResponse>(
      `${this.apiUrl}/shipping`,
      request,
      { headers }
    );
  }

  updateShippingAddress(
    id: string,
    userName: string,
    request: ShippingAddressRequest
  ): Observable<ShippingAddressResponse> {
    const headers = new HttpHeaders({ 'X-User-Name': userName });
    return this.http.put<ShippingAddressResponse>(
      `${this.apiUrl}/shipping/${id}`,
      request,
      { headers }
    );
  }

  deleteShippingAddress(userName: string, addressId: string): Observable<void> {
    const headers = new HttpHeaders({ 'X-User-Name': userName });
    return this.http.delete<void>(`${this.apiUrl}/shipping/${addressId}`, {
      headers,
    });
  }
}
