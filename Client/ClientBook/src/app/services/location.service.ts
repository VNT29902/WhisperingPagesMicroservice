import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LocationService {
  private provinceUrl = 'https://esgoo.net/api-tinhthanh-new/1/0.htm';
  private wardUrl = 'https://esgoo.net/api-tinhthanh-new/2/'; // + provinceId + .htm

  constructor(private http: HttpClient) {}

  getProvinces(): Observable<any> {
    return this.http.get(this.provinceUrl);
  }

  getWardsByProvinceId(provinceId: string): Observable<any> {
    return this.http.get(`${this.wardUrl}${provinceId}.htm`);
  }
}

export interface Province {
  id: string;
  full_name: string;
}

export interface Ward {
  id: string;
  full_name: string;
}

