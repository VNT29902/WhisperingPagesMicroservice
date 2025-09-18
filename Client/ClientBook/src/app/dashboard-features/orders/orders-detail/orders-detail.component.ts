import { Component, OnInit } from '@angular/core';
import { ShippingInfoResponse } from '../../../models/shipping.model';
import { ActivatedRoute, RouterLink, RouterModule } from '@angular/router';
import { OrderService } from '../../../services/orders.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { OrderWithItemsResponse } from '../../../models/orders-purchase';

@Component({
  selector: 'app-orders-detail',
  standalone: true,
  imports: [CommonModule,RouterModule],
  templateUrl: './orders-detail.component.html',
  styleUrl: './orders-detail.component.css'
})
export class OrdersDetailComponent implements OnInit {
  orderId!: string;
  order?: OrderWithItemsResponse;
  shipping?: ShippingInfoResponse;

  constructor(
    private route: ActivatedRoute,
    private orderService: OrderService
  ) {}

ngOnInit(): void {
  this.orderId = this.route.snapshot.paramMap.get('id')!;
  this.loadOrder();
  this.loadShipping(); // 👈 gọi thêm
}


loadOrder() {
  this.orderService.getOrderWithItemsById(this.orderId).subscribe({
    next: (res) => this.order = res,
    error: (err) => console.error('❌ Failed to load order:', err),
  });
}


  loadShipping() {
    this.orderService.getShippingInfoDashboard(this.orderId).subscribe({
      next: (res) => (this.shipping = res),
      error: (err) => console.error('❌ Failed to load shipping info:', err),
    });
  }
}
