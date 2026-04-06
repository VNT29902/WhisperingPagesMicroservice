import { Component, OnInit } from '@angular/core';
import { BookService } from '../services/book.service';
import { CommonModule } from '@angular/common';
import { AddToCartButtonComponent } from '../add-to-cart-button/add-to-cart-button.component';
import { BookAndPromotion } from '../models/bookAndPromotion.model';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-popularbook',
  standalone: true,
  imports: [CommonModule, AddToCartButtonComponent, RouterLink],
  templateUrl: './popularbook.component.html',
  styleUrls: ['./popularbook.component.css'],
})
export class PopularbookComponent implements OnInit {
  genres: string[] = ['Tất cả thể loại', 'Khoa Học', 'Triết Học', 'Nghệ Thuật', 'Kinh Doanh', 'Lịch Sử'];
  genreMap: { [key: string]: string } = {
    'Tất cả thể loại': 'all',
    'Khoa Học': 'khoa-hoc',
    'Triết Học': 'triet-hoc',
    'Nghệ Thuật': 'nghe-thuat',
    'Kinh Doanh': 'kinh-doanh',
    'Lịch Sử': 'lich-su',
  };

  selectedGenre: string = 'Tất cả thể loại';
  books: BookAndPromotion[] = [];

  private cache: Map<string, BookAndPromotion[]> = new Map();

  constructor(private bookService: BookService) {}

  ngOnInit(): void {
    this.selectGenre(this.selectedGenre);
  }

  selectGenre(genre: string): void {
    this.selectedGenre = genre;
    const category = this.genreMap[genre];
    const key = `${category || 'all'}-8-0`;

    if (this.cache.has(key)) {
      this.books = this.cache.get(key)!;
      return;
    }

    this.bookService.getBooksByCategory(category, 8, 0).subscribe({
      next: (res) => {
        this.books = res?.content || [];
        this.cache.set(key, this.books);
      },
      error: (err) => {
        console.error('❌ Lỗi khi lấy sách:', err);
        this.books = [];
        this.cache.set(key, this.books);
      },
    });
  }

  showFullTitle(title: string) {
    alert(title);
  }

  getStockWidth(bp: BookAndPromotion): number {
    const book = bp.bookResponseDto;
    if (book.stock === 0) return 100;
    if (book.stock < 10) return 80;
    if (book.saleStock > book.stock) return 50;
    return (book.saleStock / book.stock) * 100;
  }

  onImageError(event: Event): void {
    const target = event.target as HTMLImageElement | null;
    if (target) {
      target.src = 'assets/images/default.png';
    }
  }
}
