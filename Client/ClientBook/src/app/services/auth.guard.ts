import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(private router: Router) {}

  canActivate(): boolean | UrlTree {
    const token = localStorage.getItem('accessToken');
    const role = localStorage.getItem('role');

    if (token && role === 'ROLE_ADMIN') {
      return true; // ✅ Cho vào dashboard
    }

    if (!token) {
      return this.router.parseUrl('/login'); // ❌ Chưa login
    }

    return this.router.parseUrl('/'); // ❌ Login nhưng không phải admin
  }
}
