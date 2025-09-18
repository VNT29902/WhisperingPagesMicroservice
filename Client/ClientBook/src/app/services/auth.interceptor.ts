import {
  HttpInterceptorFn,
  HttpErrorResponse,
  HttpRequest,
  HttpHandlerFn
} from '@angular/common/http';
import { inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { AuthService } from './auth.service';
import { BehaviorSubject, throwError } from 'rxjs';
import { catchError, filter, switchMap, take } from 'rxjs/operators';

let isRefreshing = false;
const tokenSubject = new BehaviorSubject<string | null>(null);

// Hàm lấy token an toàn
function getFromLocalStorage(key: string): string | null {
  try {
    if (typeof localStorage !== 'undefined') {
      return localStorage.getItem(key);
    }
  } catch {}
  return null;
}

// Hàm lưu token an toàn
function setToLocalStorage(key: string, value: string) {
  try {
    if (typeof localStorage !== 'undefined') {
      localStorage.setItem(key, value);
      console.log(`✅ Saved ${key}`);
    }
  } catch (e) {
    console.error(`❌ Failed to save ${key}:`, e);
  }
}

function addAuthHeader(req: HttpRequest<any>, token?: string) {
  return token ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } }) : req;
}

function isAuthEndpoint(url: string): boolean {
  return url.includes('/api/auth/login')
      || url.includes('/api/auth/register')
      || url.includes('/api/auth/refreshToken');
}

export const AuthInterceptor: HttpInterceptorFn = (req, next: HttpHandlerFn) => {
  const authService = inject(AuthService);
  const platformId = inject(PLATFORM_ID);
  const isBrowser = isPlatformBrowser(platformId);

  const accessToken = isBrowser ? getFromLocalStorage('accessToken') : null;
  const apiRequest = req.url.startsWith('http://localhost:8085'); // hoặc base API của bạn

  // Nếu chưa đăng nhập → không gắn token, vẫn cho request qua
  const authReq = (accessToken && apiRequest && !isAuthEndpoint(req.url))
    ? addAuthHeader(req, accessToken)
    : req;

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      const unauthorized = error.status === 401 || error.status === 403;

      // Chỉ refresh khi: đang ở browser, API nội bộ, không phải endpoint auth, và đã đăng nhập
      if (isBrowser && unauthorized && apiRequest && !isAuthEndpoint(req.url) && accessToken) {
        if (isRefreshing) {
          return tokenSubject.pipe(
            filter((t): t is string => !!t),
            take(1),
            switchMap((newToken) => next(addAuthHeader(req, newToken)))
          );
        }

        isRefreshing = true;
        tokenSubject.next(null);

        const refreshToken = getFromLocalStorage('refreshToken');
        const userName = getFromLocalStorage('userName');

        if (!refreshToken || !userName) {
          isRefreshing = false;
          authService.logout();
          return throwError(() => error);
        }

        return authService.refreshToken(userName, { refreshToken }).pipe(
  switchMap((res) => {
    if (res.accessToken) setToLocalStorage('accessToken', res.accessToken);
    if (res.refreshToken) setToLocalStorage('refreshToken', res.refreshToken);
    if (res.userName) setToLocalStorage('userName', res.userName);
    if (res.role) setToLocalStorage('role', res.role); // ✅ thêm role

    isRefreshing = false;
    tokenSubject.next(res.accessToken);

    return next(addAuthHeader(req, res.accessToken));
  }),
  catchError((refreshErr) => {
    isRefreshing = false;
    authService.logout(); // ✅ clear token
    return throwError(() => refreshErr);
  })
);
      }

      // Các lỗi khác giữ nguyên
      return throwError(() => error);
    })
  );
};
