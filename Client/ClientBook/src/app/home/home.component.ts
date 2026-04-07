import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { BestsellingComponent } from '../bestselling/bestselling.component';
import { PopularbookComponent } from '../popularbook/popularbook.component';
import { AboutPageComponent } from '../about-page/about-page.component';
import { FooterComponent } from '../footer/footer.component';
import { BookAndPromotion } from '../models/bookAndPromotion.model';
import { BookService } from '../services/book.service';
import { forkJoin } from 'rxjs';
import { catchError, map, of } from 'rxjs';
import { LatestReleaseBooksComponent } from '../latest-release-books/latest-release-books.component';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    HeaderComponent,
    LatestReleaseBooksComponent,
    BestsellingComponent,
    PopularbookComponent,
    AboutPageComponent,
    FooterComponent,
  ],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  latestBooks: BookAndPromotion[] = [];
  bestsellingBooks: BookAndPromotion[] = [];
  categoryBooks: BookAndPromotion[] = [];

  featuredBooks: BookAndPromotion[] = [];
  partialLoadFailed = false;
  loadFailed = false;

  constructor(private bookService: BookService) {}

  ngOnInit(): void {
    forkJoin({
      latest: this.bookService.getLatestBooks().pipe(
        map((data) => ({ data: data || [], failed: false })),
        catchError((err) => {
          console.error('❌ Lỗi latest books:', err);
          return of({ data: [] as BookAndPromotion[], failed: true });
        })
      ),
      bestselling: this.bookService.getBestSelling().pipe(
        map((data) => ({ data: data || [], failed: false })),
        catchError((err) => {
          console.error('❌ Lỗi best selling books:', err);
          return of({ data: [] as BookAndPromotion[], failed: true });
        })
      ),
      category: this.bookService.getBooksByCategory('all', 12, 0).pipe(
        map((res) => ({ data: res?.content || [], failed: false })),
        catchError((err) => {
          console.error('❌ Lỗi category books:', err);
          return of({ data: [] as BookAndPromotion[], failed: true });
        })
      ),
    }).subscribe({
      next: (result) => {
        this.latestBooks = result.latest.data;
        this.bestsellingBooks = result.bestselling.data;
        this.categoryBooks = result.category.data;
        this.featuredBooks = this.categoryBooks.slice(0, 6);
        const failedCount = [result.latest.failed, result.bestselling.failed, result.category.failed].filter(Boolean).length;
        this.partialLoadFailed = failedCount > 0 && failedCount < 3;
        this.loadFailed = failedCount === 3;
      },
      error: (err) => {
        console.error('❌ Lỗi khi lấy dữ liệu trang chủ:', err);
        this.featuredBooks = [];
        this.partialLoadFailed = false;
        this.loadFailed = true;
      },
    });
  }

  getDiscountPrice(book: BookAndPromotion): number {
    const price = Number(book.bookResponseDto.price || 0);
    const discount = Number(book.promotionPercent || 0);
    return Math.max(price - (price * discount) / 100, 0);
  }
}
