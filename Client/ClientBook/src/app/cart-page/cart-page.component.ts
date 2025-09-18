import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { CartService } from '../services/cart.service';
import { CartResponse } from '../models/cart-response.model';
import { AuthService } from '../services/auth.service';
import { CartItemDTO } from '../models/cart-item-dto.model';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';


@Component({
  selector: 'app-cart-page',
  standalone: true,
  imports: [HeaderComponent,FooterComponent,CommonModule],
  templateUrl: './cart-page.component.html',
  styleUrl: './cart-page.component.css'
})
export class CartPageComponent implements OnInit {
  cartData!: CartResponse;

  constructor(
    private cartService: CartService,
    private authService: AuthService,
     private router: Router
  ) {}

  ngOnInit(): void {
  
    this.cartService.getCart().subscribe({
      next: (res) => {
        this.cartData = res;
      },
      error: (err) => {
        console.error('❌ Lỗi lấy giỏ hàng:', err);
      }
    });
  }

  removeItem(item: CartItemDTO): void {
    this.cartService.removeItem( item.productId).subscribe(() => {
      this.cartData.items = this.cartData.items.filter(i => i.productId !== item.productId);
      this.recalculateTotal();
    });
  }

  increaseQuantity(item: CartItemDTO): void {
    this.cartService.updateItemQuantity( item.productId, item.quantity + 1).subscribe(() => {
      item.quantity++;
      this.recalculateTotal();
    });
  }

  decreaseQuantity(item: CartItemDTO): void {
  if (item.quantity > 1) {
    this.cartService.updateItemQuantity(item.productId, item.quantity - 1).subscribe(() => {
      item.quantity--;
      this.recalculateTotal();
    });
  } else if (item.quantity === 1) {
    this.removeItem(item);
  }
}


  recalculateTotal(): void {
    this.cartData.totalAmount = this.cartData.items.reduce(
      (sum, item) => sum + item.price * item.quantity, 0
    );
  }

 checkout(): void {
  if (!this.cartData || !this.cartData.items?.length) return;
  this.router.navigate(['/account/checkout'], { state: { cart: this.cartData } });
}

  onImgError(event: Event) {
  (event.target as HTMLImageElement).src = 'assets/images/default.jpg';
}

}

