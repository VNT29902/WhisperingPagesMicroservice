import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { LoginInfo } from '../models/auth-login.model';
import { RegisterInfo } from '../models/auth-register.model';
import { loginInfoResponse } from '../models/auth-login-response.model';
import { UserInfoResponse } from '../models/auth-info-response.model';
import { ChangePasswordRequest } from '../models/change-password.model';
import { PageResponse } from '../models/page-response.model';
import { UserGetAllResponse } from '../models/user-all-response.model';

export interface RefreshDto {
  refreshToken: string;
}

export interface TokenResponseDto {
  accessToken: string;
  refreshToken: string;
  userName: string;
  role: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/api/auth`;
  private loggedInSubject = new BehaviorSubject<boolean>(false);
  public loggedIn$ = this.loggedInSubject.asObservable();

  private refreshInterval: any;
  private readonly REFRESH_INTERVAL_MS = 14 * 60 * 1000;

  constructor(private http: HttpClient) {
    if (typeof window !== 'undefined') {
      const hasToken = !!localStorage.getItem('accessToken');
      this.loggedInSubject.next(hasToken);
    }
  }

  registerUser(regisInfo: RegisterInfo): Observable<string> {
    return this.http.post(`${this.apiUrl}/register`, regisInfo, {
      responseType: 'text',
    });
  }

  loginUser(loginInfo: LoginInfo): Observable<loginInfoResponse> {
    return this.http.post<loginInfoResponse>(`${this.apiUrl}/login`, loginInfo);
  }

  getUserInfo(userName: string): Observable<UserInfoResponse> {
    return this.http.get<UserInfoResponse>(`${this.apiUrl}`, {
      params: { userName },
    });
  }

  changeStatus(userId: number): Observable<UserGetAllResponse> {
  return this.http.patch<UserGetAllResponse>(`${this.apiUrl}/${userId}/status`, {});
}


  getAllUsers(page: number = 0, size: number = 10): Observable<PageResponse<UserGetAllResponse>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<PageResponse<UserGetAllResponse>>(`${this.apiUrl}/all`, { params });
  }

 refreshToken(userName: string, refreshDto: RefreshDto): Observable<TokenResponseDto> {
  const params = new HttpParams().set('userName', userName);

  return this.http.post<TokenResponseDto>(
    `${this.apiUrl}/refreshToken`,
    refreshDto,
    { params }
  ).pipe(
    tap((response: TokenResponseDto) => {
      // ✅ Cập nhật token mới vào localStorage
      localStorage.setItem('accessToken', response.accessToken);
      localStorage.setItem('refreshToken', response.refreshToken);
      localStorage.setItem('userName', response.userName);
      localStorage.setItem('role', response.role);
    })
  );
}


  changePassword(username: string, request: ChangePasswordRequest): Observable<string> {
    const params = new HttpParams().set('username', username);

    return this.http.post(`${this.apiUrl}/change-password`, request, {
      params,
      responseType: 'text'
    });
  }

  startRefreshTokenLoop(): void {
    if (this.refreshInterval) {
      clearInterval(this.refreshInterval);
    }

    this.refreshInterval = setInterval(() => {
      this.refreshAccessToken();
    }, this.REFRESH_INTERVAL_MS);
  }

  private refreshAccessToken(): void {
    const refreshToken = localStorage.getItem('refreshToken');
    const userName = localStorage.getItem('userName');
    if (!refreshToken || !userName) return;

    const refreshDto: RefreshDto = { refreshToken };

    this.refreshToken(userName, refreshDto).subscribe({
      next: (res: TokenResponseDto) => {
        localStorage.setItem('accessToken', res.accessToken);
        localStorage.setItem('refreshToken', res.refreshToken);
        localStorage.setItem('userName', res.userName);
      },
      error: (err) => {
        console.error('❌ Refresh token failed:', err);
        this.logout(); // optional: auto logout on failure
      }
    });
  }

  stopRefreshTokenLoop(): void {
    if (this.refreshInterval) {
      clearInterval(this.refreshInterval);
      this.refreshInterval = null;
    }
  }

  loginSuccess(): void {
    this.loggedInSubject.next(true);
    this.startRefreshTokenLoop();
  }

   logoutSession(refreshDto: RefreshDto): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/logout`, refreshDto);
  }

  // ✅ Logout toàn bộ session của user
  logoutAll(refreshDto: RefreshDto): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/logout/all`, refreshDto);
  }

  logout(): void {
  localStorage.removeItem('accessToken');
  localStorage.removeItem('refreshToken');
  localStorage.removeItem('userName');
  localStorage.removeItem('role'); 

  this.stopRefreshTokenLoop();
  this.loggedInSubject.next(false);
}


  isLoggedIn(): boolean {
    return typeof window !== 'undefined' && !!localStorage.getItem('accessToken');
  }


resetPassword(username: string, newPassword: string): Observable<{ message: string }> {
  return this.http.post<{ message: string }>(
    `${this.apiUrl}/reset-password`,
    { resetPass: newPassword },
    { params: { username } }
  );
}




}
