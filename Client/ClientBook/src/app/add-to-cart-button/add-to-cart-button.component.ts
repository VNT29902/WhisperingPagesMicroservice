import { Component, Input, OnInit } from '@angular/core';
import { CartService } from '../services/cart.service';
import { Router } from '@angular/router';
import { CartItemRequest } from '../models/cart-item-request.model';

@Component({
  selector: 'app-add-to-cart-button',
  standalone: true, // ✅ nếu bạn dùng standalone components
  imports: [],
  templateUrl: './add-to-cart-button.component.html',
  styleUrls: ['./add-to-cart-button.component.css'], // ✅ sửa lại styleUrls
})
export class AddToCartButtonComponent implements OnInit {
  @Input() productId!: string;
  @Input() quantity: number = 1; // default 1
  @Input() title!: string;
  @Input() image!: string;
  @Input() price!: number;

  constructor(private cartService: CartService, private router: Router) {}

  ngOnInit(): void {
    console.log('🛒 AddToCartButtonComponent input received:');
    console.log({
      productId: this.productId,
      image: this.image,
      title: this.title,
      price: this.price,
    });
  }

  addToCart() {
    const userName = localStorage.getItem('userName');

    if (!userName) {
      // ❌ chưa login → chuyển tới trang login
      this.router.navigate(['/login']);
      return;
    }

    // ✅ đã login → thêm vào giỏ
    const item: CartItemRequest = {
      productId: this.productId,
      quantity: this.quantity,
      title: this.title,
      image: this.image,
      price: this.price,
    };

    this.cartService.addToCart(item).subscribe({
      next: () => this.router.navigate(['/cart']),
      error: (err) => console.error('❌ Add to cart failed:', err),
    });
  }
}
