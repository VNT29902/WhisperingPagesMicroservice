import { Component, OnInit } from '@angular/core';
import { BookService } from '../services/book.service';
import { BookAndPromotion } from '../models/bookAndPromotion.model';
import { CommonModule } from '@angular/common';
import { AddToCartButtonComponent } from '../add-to-cart-button/add-to-cart-button.component';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-bestselling',
  imports: [CommonModule,AddToCartButtonComponent,RouterModule],
  templateUrl: './bestselling.component.html',
  styleUrl: './bestselling.component.css'
})
export class BestsellingComponent implements OnInit {
bestSellingBooks: BookAndPromotion[] = [];

  constructor(private bookService: BookService) {}

  ngOnInit(): void {
    this.bookService.getBestSelling().subscribe({
      next: (data) => {
        this.bestSellingBooks = data;
      },
      error: (err) => {
        console.error('❌ Lỗi khi load sách bán chạy:', err);
      }
    });
  }

  getStockWidth(bp: BookAndPromotion): number {
    if (!bp.bookResponseDto || bp.bookResponseDto.stock <= 0) return 0;
    const sold = bp.bookResponseDto.saleStock || 0;
    const total = bp.bookResponseDto.stock + sold;
    return Math.min((sold / total) * 100, 100);
  }

  showFullTitle(title: string): void {
    alert(title);
  }
}