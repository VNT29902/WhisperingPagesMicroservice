import { routes } from './../app.routes';
import { Component } from '@angular/core';
import { BookAndPromotion } from '../models/bookAndPromotion.model';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { BookService } from '../services/book.service';
import { CurrencyPipe, NgIf } from '@angular/common';
import { FooterComponent } from "../footer/footer.component";
import { HeaderComponent } from '../header/header.component';
import { AddToCartButtonComponent } from '../add-to-cart-button/add-to-cart-button.component';

@Component({
  selector: 'app-book-detail',
  imports: [NgIf, CurrencyPipe, FooterComponent,HeaderComponent,FooterComponent,AddToCartButtonComponent

  ],
  templateUrl: './book-detail.component.html',
  styleUrl: './book-detail.component.css'
})
export class BookDetailComponent {

  bookAndPromotion!: BookAndPromotion;
loading: boolean = true;
error: string | null = null;

constructor(
  private route: ActivatedRoute,
  private bookService: BookService,
  private router: Router
) {}

ngOnInit(): void {
  // 👇 Subscribe để bắt id mỗi khi route param thay đổi
  this.route.paramMap.subscribe(params => {
    const bookId = params.get('id');
    if (bookId) {
      this.fetchBookDetail(bookId);
    } else {
      this.error = 'Không tìm thấy ID sách!';
      this.loading = false;
    }
  });
}

fetchBookDetail(id: string): void {
  this.loading = true;
  this.error = null;

  this.bookService.getBookById(id).subscribe({
    next: (data) => {
      this.bookAndPromotion = data;
      this.loading = false;
    },
    error: (err) => {
      this.error = 'Không thể tải chi tiết sách. Vui lòng thử lại!';
      console.error(err);
      this.loading = false;
    }
  });
}

buyNow(b: BookAndPromotion) {
  const finalPrice = b.bookResponseDto.price * (1 - b.promotionPercent / 100);

  const checkoutData = {
    items: [
      {
        productId: b.bookResponseDto.id,
        title: b.bookResponseDto.title,
        image: b.bookResponseDto.image,
        price: finalPrice,
        quantity: 1
      }
    ],
    totalAmount: finalPrice
  };

  this.router.navigate(['/account/checkout'], { state: { cart: checkoutData } });
}
}


