import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { CommonModule } from '@angular/common';

import { OrderService } from '../services/orders.service';
import { OrderWithItemsResponse } from '../models/orders-purchase';

@Component({
  selector: 'app-purchase',
  standalone: true,
  imports: [HeaderComponent, FooterComponent, CommonModule],
  templateUrl: './purchase.component.html',
  styleUrl: './purchase.component.css',
})
export class PurchaseComponent implements OnInit {
  orders: OrderWithItemsResponse[] = [];

  userName: string = 'demoUser'; // TODO: thay bằng AuthService khi login

  constructor(private orderService: OrderService) {}
  ngOnInit(): void {
    this.orderService.getOrdersByUserName(this.userName).subscribe({
      next: (res) => {
        this.orders = res.sort(
          (a, b) =>
            new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
        ); // 🔥 sắp xếp từ mới đến cũ
      },
      error: (err) => console.error('❌ Failed to load orders:', err),
    });
  }

  getTotalAmount(order: OrderWithItemsResponse): number {
    return order.items.reduce((s, it) => s + Number(it.price) * it.quantity, 0);
  }

  getGrandTotal(order: OrderWithItemsResponse): number {
    return this.getTotalAmount(order) + 40000;
  }
}
