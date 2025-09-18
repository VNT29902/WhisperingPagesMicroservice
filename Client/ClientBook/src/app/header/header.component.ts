import { AccountService } from './../services/account.service';
import { BookService } from './../services/book.service';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { CommonModule } from '@angular/common';
import { BookAndPromotion } from '../models/bookAndPromotion.model';
import { SearchBoxComponent } from '../search-box/search-box.component';
import { FormsModule } from '@angular/forms';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterModule, CommonModule, SearchBoxComponent, FormsModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent implements OnInit, OnDestroy {
  loggedIn = false;
  isAdmin = false;   // ✅ Khai báo đúng boolean, mặc định false

  showAccountMenu = false;
  showMobileMenu = false;
  showMobileSearch = false;
  showSideMenu = false;

  keyword: string = '';
  results: BookAndPromotion[] = [];

  private sub?: Subscription;
  private profileSub?: Subscription;

  constructor(
    private authService: AuthService,
    private router: Router,
    private bookService: BookService,
    private accountService: AccountService
  ) {}

ngOnInit(): void {
  // Theo dõi login
  this.sub = this.authService.loggedIn$.subscribe((isLoggedIn) => {
    this.loggedIn = isLoggedIn;

    if (isLoggedIn) {
      const role = localStorage.getItem('role');
      this.isAdmin = role === 'ROLE_ADMIN';   
    } else {
      this.isAdmin = false; 
    }
  });
}


  ngOnDestroy(): void {
    this.sub?.unsubscribe();
    this.profileSub?.unsubscribe();
  }

  toggleAccountMenu(): void {
    this.showAccountMenu = !this.showAccountMenu;
  }

  toggleMobileMenu(): void {
    this.showMobileMenu = !this.showMobileMenu;
  }

  toggleMobileSearch(): void {
    this.showMobileSearch = !this.showMobileSearch;
    if (!this.showMobileSearch) {
      this.results = [];
    }
  }

  toggleSideMenu(): void {
    this.showSideMenu = !this.showSideMenu;
  }

onLogout(): void {
  const refreshToken = localStorage.getItem('refreshToken');
  if (refreshToken) {
    this.authService.logoutSession({ refreshToken }).subscribe({
      next: () => {
        this.authService.logout();
        this.router.navigate(['/login']);
      },
      error: () => {
        this.authService.logout(); // fallback vẫn logout client
        this.router.navigate(['/login']);
      }
    });
  } else {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}





  onSearch(): void {
    const kw = this.keyword.trim();
    if (!kw) {
      this.results = [];
      return;
    }

    this.bookService.searchBooks(kw, 0, 5).subscribe({
      next: (data) => {
        this.results = data.content ?? [];
      },
      error: (err) => console.error('Search failed:', err),
    });
  }

  goToBook(id: string): void {
    this.router.navigate(['/books', id]);
    this.results = [];
    this.showMobileSearch = false;
  }

  onLogoutAll(): void {
  const refreshToken = localStorage.getItem('refreshToken');
  if (!refreshToken) {
    this.authService.logout();
    this.router.navigate(['/login']);
    return;
  }

  const dto = { refreshToken };

  this.authService.logoutAll(dto).subscribe({
    next: () => {
      // ✅ clear hết token client
      this.authService.logout();
      this.router.navigate(['/login']);
      window.location.reload();
    },
    error: (err) => {
      console.error('❌ Logout all failed:', err);
      // vẫn clear client để tránh user kẹt trong trạng thái login
      this.authService.logout();
      this.router.navigate(['/login']);
    }
  });
}

}
