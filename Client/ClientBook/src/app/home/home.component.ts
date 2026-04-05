import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { BannerComponent } from '../banner/banner.component';

import { BestsellingComponent } from '../bestselling/bestselling.component';
import { PopularbookComponent } from '../popularbook/popularbook.component';
import { AboutPageComponent } from '../about-page/about-page.component';
import { FooterComponent } from '../footer/footer.component';
import { BookAndPromotion } from '../models/bookAndPromotion.model';
import { BookService } from '../services/book.service';
import { forkJoin } from 'rxjs';
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
    BannerComponent,
  ],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  latestBooks: BookAndPromotion[] = [];
  bestsellingBooks: BookAndPromotion[] = [];
  categoryBooks: BookAndPromotion[] = [];

  featuredBooks: BookAndPromotion[] = [];
  loadFailed = false;

  constructor(private bookService: BookService) {}

  ngOnInit(): void {
    forkJoin({
      latest: this.bookService.getLatestBooks(),
      bestselling: this.bookService.getBestSelling(),
      category: this.bookService.getBooksByCategory('all', 12, 0),
    }).subscribe({
      next: (result) => {
        this.latestBooks = result.latest || [];
        this.bestsellingBooks = result.bestselling || [];
        this.categoryBooks = result.category?.content || [];
        this.featuredBooks = this.categoryBooks.slice(0, 6);
        this.loadFailed = false;
      },
      error: (err) => {
        console.error('❌ Lỗi khi lấy dữ liệu trang chủ:', err);
        this.featuredBooks = [];
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
