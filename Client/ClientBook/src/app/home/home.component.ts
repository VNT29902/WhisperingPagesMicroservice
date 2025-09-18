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

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
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

  constructor(private bookService: BookService) {}

  ngOnInit(): void {
  forkJoin({
    latest: this.bookService.getLatestBooks(),        // giả sử trả về BookAndPromotion[]
    bestselling: this.bookService.getBestSelling(),   // giả sử trả về BookAndPromotion[]
    category: this.bookService.getBooksByCategory('all', 10, 0) // trả về PageResponse
  }).subscribe({
    next: (result) => {
      this.latestBooks = result.latest;
      this.bestsellingBooks = result.bestselling;
      this.categoryBooks = result.category.content;  // ✅ lấy mảng từ content
    },
    error: (err) => console.error('❌ Lỗi khi lấy dữ liệu:', err),
  });
}

}
