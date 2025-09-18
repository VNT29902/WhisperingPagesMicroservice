import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Order, OrderItemResponse } from '../../models/order.model';
import { ActivatedRoute, RouterLink, RouterModule } from '@angular/router';
import { OrderWithItemsResponse } from '../../models/orders-purchase';
import { OrderService } from '../../services/orders.service';
import { PageResponse } from '../../models/page-response.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-orders',
  standalone: true,
  imports: [FormsModule, RouterModule, CommonModule, RouterLink],
  templateUrl: './orders.component.html',
  styleUrl: './orders.component.css',
})
export class DashBoardOrdersComponent {
  orders: OrderWithItemsResponse[] = [];
  allOrders: OrderWithItemsResponse[] = []; // ✅ danh sách gốc
  itemsCache: { [orderId: string]: OrderItemResponse[] } = {};
  expanded: { [orderId: string]: boolean } = {};

  page = 0;
  pageSize = 5;
  totalPages = 1;

  keyword = '';
  selectedStatus = '';
  dateFrom?: string;
  dateTo?: string;
  minTotal?: number;
  maxTotal?: number;

  constructor(private orderService: OrderService) {}

  ngOnInit() {
    this.loadOrders();
    this.fetchAllOrders();
  }

  loadOrders() {
    this.orderService.getAllOrders(this.page, this.pageSize).subscribe({
      next: (res: PageResponse<OrderWithItemsResponse>) => {
        this.orders = res.content;
        this.totalPages = res.totalPages;
      },
      error: (err) => console.error('❌ Failed to load orders:', err),
    });
  }

  toggleItems(order: OrderWithItemsResponse) {
    const orderId = order.orderId;
    if (this.expanded[orderId]) {
      // nếu đang mở thì đóng lại
      this.expanded[orderId] = false;
    } else {
      // mở ra → nếu chưa cache thì cache items
      if (!this.itemsCache[orderId]) {
        this.itemsCache[orderId] = order.items;
      }
      this.expanded[orderId] = true;
    }
  }

  prevPage() {
    if (this.page > 0) {
      this.page--;
      this.loadOrders();
    }
  }

  nextPage() {
    if (this.page < this.totalPages - 1) {
      this.page++;
      this.loadOrders();
    }
  }

  goToPage(index: number) {
    this.page = index;
    this.loadOrders();
  }

  onFilterChange() {
    this.page = 0;
    this.applyFilters();
  }

  fetchAllOrders() {
    // lấy pageSize rất lớn để fetch hết
    this.orderService.getAllOrders(0, 9999).subscribe({
      next: (res: PageResponse<OrderWithItemsResponse>) => {
        this.allOrders = res.content;
        this.applyFilters();
      },
      error: (err) => console.error('❌ Failed to load orders:', err),
    });
  }

  applyFilters() {
    let result = [...this.allOrders];

    // search by orderId or userName
    if (this.keyword) {
      const kw = this.keyword.toLowerCase();
      result = result.filter(
        (o) =>
          o.orderId.toLowerCase().includes(kw) ||
          o.userName.toLowerCase().includes(kw)
      );
    }

    // status
    if (this.selectedStatus) {
      result = result.filter((o) => o.status === this.selectedStatus);
    }

    // date filter
    if (this.dateFrom) {
      result = result.filter(
        (o) => new Date(o.createdAt) >= new Date(this.dateFrom!)
      );
    }
    if (this.dateTo) {
      result = result.filter(
        (o) => new Date(o.createdAt) <= new Date(this.dateTo!)
      );
    }

    // total amount range
    if (this.minTotal) {
      result = result.filter((o) => o.totalAmount >= this.minTotal!);
    }
    if (this.maxTotal) {
      result = result.filter((o) => o.totalAmount <= this.maxTotal!);
    }

    // pagination
    this.totalPages = Math.ceil(result.length / this.pageSize);
    const start = this.page * this.pageSize;
    this.orders = result.slice(start, start + this.pageSize);
  }

  resetFilters() {
  this.keyword = '';
  this.selectedStatus = '';
  this.dateFrom = undefined;
  this.dateTo = undefined;
  this.minTotal = undefined;
  this.maxTotal = undefined;

  this.page = 0;
  this.fetchAllOrders(); // gọi lại API lấy full list
}

}
