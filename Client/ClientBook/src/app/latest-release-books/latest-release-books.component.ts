import { Component, OnInit } from '@angular/core';
import { BookService } from '../services/book.service';
import { BookAndPromotion } from '../models/bookAndPromotion.model';
import { CommonModule } from '@angular/common';
import { AddToCartButtonComponent } from '../add-to-cart-button/add-to-cart-button.component';
import { RouterModule } from '@angular/router';
import { HOME_LATEST_FALLBACK } from '../mock/home-books.mock';

@Component({
  selector: 'app-latest-release-books',
  standalone: true,
  imports: [CommonModule, AddToCartButtonComponent, RouterModule],
  templateUrl: './latest-release-books.component.html',
  styleUrl: './latest-release-books.component.css',
})
export class LatestReleaseBooksComponent implements OnInit {
  newReleaseBooks: BookAndPromotion[] = [];

  constructor(private bookService: BookService) {}

  ngOnInit(): void {
    this.bookService.getLatestBooks().subscribe({
      next: (books) => {
        this.newReleaseBooks = books?.length ? books : HOME_LATEST_FALLBACK;
      },
      error: (err) => {
        console.error('❌ Lỗi khi lấy sách mới phát hành:', err);
        this.newReleaseBooks = HOME_LATEST_FALLBACK;
      },
    });
  }

  showFullTitle(title: string) {
    alert(title);
  }

  getStockWidth(book: BookAndPromotion): number {
    const dto = book.bookResponseDto;
    if (dto.stock === 0) return 100;
    if (dto.stock < 10) return 80;
    if (dto.saleStock > dto.stock) return 50;
    return (dto.saleStock / dto.stock) * 100;
  }
}
