import { routes } from './../../app.routes';
import { Component, OnInit } from '@angular/core';
import { PromotionListResponse } from '../../models/promotion-list-response.model';
import { BookService } from '../../services/book.service';
import { PageResponse } from '../../models/page-response.model';
import { PromotionStatusUpdateRequest } from '../../models/promotion-status-update-request.model';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { PromotionSaveResponse } from '../../models/promotion-save-response.model';

@Component({
  selector: 'app-promotions',
  standalone: true,
  imports: [FormsModule,RouterLink,CommonModule],
  templateUrl: './promotions.component.html',
  styleUrl: './promotions.component.css'
})
export class DashBoardPromotionsComponent implements OnInit {

promotions: PromotionListResponse[] = [];

allPromotions: PromotionListResponse[] = [];

  page = 0;
  pageSize = 5;
  totalPages = 1;

  activeFilter: string = '';
keyword: string = '';
selectedMonth: string = ''; 
   constructor(
    private bookService: BookService,
        // ✅ inject Router ở đây
  ) {}

  ngOnInit(): void {
    this.loadPromotions();
  }

  loadPromotions(): void {
  const activeParam =
    this.activeFilter === ''
      ? undefined
      : this.activeFilter === 'true'
      ? true
      : false;

  this.bookService
    .getPromotions(this.page, this.pageSize, activeParam)
    .subscribe({
      next: (res: PageResponse<PromotionListResponse>) => {
        // ✅ lưu full data cho filter client
        this.allPromotions = res.content;
        this.promotions = [...res.content];

        this.totalPages = res.totalPages;
      },
      error: (err) => console.error('❌ Failed to load promotions:', err),
    });
}


 applyFilters() {
  let result = [...this.allPromotions];

  // search by name or target
  if (this.keyword) {
    const kw = this.keyword.toLowerCase();
    result = result.filter(
      (p) =>
        p.name.toLowerCase().includes(kw) ||
        (p.targetValue && p.targetValue.toLowerCase().includes(kw))
    );
  }

  // filter theo tháng
  if (this.selectedMonth) {
    const [year, month] = this.selectedMonth.split('-').map(Number);
    result = result.filter((p) => {
      const promoDate = new Date(p.startDate);
      return (
        promoDate.getFullYear() === year && promoDate.getMonth() + 1 === month
      );
    });
  }

  // status filter
  if (this.activeFilter !== '') {
    const active = this.activeFilter === 'true';
    result = result.filter((p) => p.active === active);
  }

  // ✅ reset page về 0 khi filter
  this.page = 0;
  this.totalPages = Math.ceil(result.length / this.pageSize);
  const start = this.page * this.pageSize;
  this.promotions = result.slice(start, start + this.pageSize);
}



resetFilters() {
  this.keyword = '';
  this.selectedMonth = '';
  this.activeFilter = '';
  this.page = 0;
  this.promotions = [...this.allPromotions];
  this.totalPages = Math.ceil(this.promotions.length / this.pageSize);
}




  // Pagination
  prevPage() {
    if (this.page > 0) {
      this.page--;
      this.loadPromotions();
    }
  }

  nextPage() {
    if (this.page < this.totalPages - 1) {
      this.page++;
      this.loadPromotions();
    }
  }

  goToPage(index: number) {
    this.page = index;
    this.loadPromotions();
  }

  onFilterChange() {
    this.page = 0;
    this.loadPromotions();
  }

deleteSuccessMessage = '';

confirmDelete(promo: PromotionListResponse): void {
  if (confirm(`Bạn có chắc muốn xóa promotion : ${promo.name}?`)) {
    this.bookService.deletePromotion(promo.id).subscribe({
      next: () => {
        this.deleteSuccessMessage = 'Promotion deleted successfully!';
        this.loadPromotions();
        setTimeout(() => this.deleteSuccessMessage = '', 2000); // ẩn sau 2s
      }
    });
  }
}

confirmChangeStatus(promo: PromotionListResponse) {
  const action = promo.active ? 'ngưng hoạt động' : 'kích hoạt';
  const ok = confirm(
    `Bạn có chắc muốn ${action} promotion "${promo.name}" không?`
  );
  if (ok) {
    this.changeStatus(promo);
  }
}

changeStatus(promo: PromotionListResponse) {
  const req = { isActive: !promo.active }; // BE cần isActive trong request body

  this.bookService.updatePromotionStatus(promo.id, req).subscribe({
    next: () => {
      promo.active = !promo.active; // ✅ update đúng property
      alert(
        `✅ Promotion "${promo.name}" đã được ${
          promo.active ? 'kích hoạt' : 'ngưng hoạt động'
        }`
      );
    },
    error: (err) => {
      console.error('❌ Failed to update status:', err);
      alert('Cập nhật trạng thái thất bại!');
    },
  });
}




}
