import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { BookService } from '../services/book.service';
import { BookAndPromotion } from '../models/bookAndPromotion.model';
import { AddToCartButtonComponent } from '../add-to-cart-button/add-to-cart-button.component';
import { RouterModule } from '@angular/router';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-product-list-page',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    AddToCartButtonComponent,
    HeaderComponent,
    FooterComponent,
    FormsModule,
  ],
  templateUrl: './product-list-pages.component.html',
  styleUrls: ['./product-list-pages.component.css'],
})
export class ProductListPageComponent implements OnInit {
  books: BookAndPromotion[] = []; // dữ liệu hiển thị sau khi filter
  allBooks: BookAndPromotion[] = []; // dữ liệu gốc từ API
  category: string = 'all';
  page: number = 0;
  limit: number = 12;
  pageSize: number = 12;
  totalProducts:number = 0; 
  type: string | null = null;
  categories = [
    { key: 'all', label: 'Tất cả sách' },
    { key: 'kinh-doanh', label: 'Kinh doanh' },
    { key: 'triet-hoc', label: 'Triết học' },
    { key: 'nghe-thuat', label: 'Nghệ thuật' },
    { key: 'lich-su', label: 'Lịch sử' },
    { key: 'khoa-hoc', label: 'Khoa học' },
    { key: 'best-selling', label: 'Best Selling' },
    { key: 'latest', label: 'Mới Phát Hành' },
  ];

  priceRanges = [
    { label: '0đ - 150,000đ', min: 0, max: 150000 },
    { label: '150,000đ - 300,000đ', min: 150000, max: 300000 },
    { label: '300,000đ - 500,000đ', min: 300000, max: 500000 },
    { label: '500,000đ - 700,000đ', min: 500000, max: 700000 },
    { label: '700,000đ - Trở Lên', min: 700000, max: null },
  ];
  selectedPriceRange: any = null;

  selectedCategory: string = 'all';
  minPrice: number | null = null;
  maxPrice: number | null = null;
  sortOption: string = 'default';

  constructor(
    private bookService: BookService,
    private route: ActivatedRoute
  ) {}

ngOnInit(): void {
  window.scrollTo(0, 0);

  this.route.paramMap.subscribe(params => {
    if (this.route.snapshot.url.some(seg => seg.path === 'latest')) {
      this.type = 'latest';
      this.selectedCategory = 'latest';
      this.loadLatestBooks();
    } else if (this.route.snapshot.url.some(seg => seg.path === 'best-selling')) {
      this.type = 'best-selling';
      this.selectedCategory = 'best-selling';
      this.loadBestSelling();
    } else if (this.route.snapshot.url.some(seg => seg.path === 'category')) {
      this.type = 'category';
      this.category = params.get('category') || 'all';
      this.selectedCategory = this.category;
      this.loadBooksByCategory(this.category, this.page);
    } else {
      this.type = 'all';
      this.selectedCategory = 'all';
      this.loadBooksByCategory('all', this.page);
    }
  });
}




 loadBooksByCategory(category: string, page: number): void {
  this.bookService.getBooksByCategory(category, this.limit, page).subscribe({
    next: (res) => {
      this.allBooks = res.content;          // ✅ lấy mảng book
      this.books = [...this.allBooks];      // copy để filter
      this.totalProducts = res.totalElements; // ✅ cập nhật số product để tính pagination
    },
    error: (err) => console.error('❌ Lỗi khi lấy sách theo category:', err),
  });
}


  loadLatestBooks(): void {
    this.bookService.getLatestBooks().subscribe({
      next: (res) => {
        this.allBooks = res;
        this.books = [...this.allBooks];
      },
      error: (err) => console.error('❌ Lỗi khi lấy sách mới:', err),
    });
  }

  loadBestSelling(): void {
    this.bookService.getBestSelling().subscribe({
      next: (res) => {
        this.allBooks = res;
        this.books = [...this.allBooks];
      },
      error: (err) => console.error('❌ Lỗi lấy sách bán chạy:', err),
    });
  }

get totalPages(): number {
  return Math.max(1, Math.ceil(this.totalProducts / this.pageSize));
}


  goToPage(page: number): void {
  // ✅ chặn nếu < 0 hoặc vượt quá tổng số trang
  if (page < 0 || page >= this.totalPages) return;

  this.page = page;

  if (this.type === 'category') {
    this.loadBooksByCategory(this.category, this.page);
  } else if (this.type === 'all') {
    this.loadBooksByCategory('all', this.page);
  }
}

  onFilterChange() {
    let filtered = [...this.allBooks];

    // 👉 Nếu là best-selling category thì bỏ lọc theo category
    if (
     this.selectedCategory !== 'all' &&
  this.selectedCategory !== 'best-selling' &&
  this.selectedCategory !== 'latest'
    ) {
      filtered = filtered.filter(
        (bp) => bp.bookResponseDto.category === this.selectedCategory
      );
    }

    // filter giá
    if (this.selectedPriceRange) {
      const r = this.selectedPriceRange;
      filtered = filtered.filter(
        (bp) =>
          (r.min === null || bp.bookResponseDto.price >= r.min) &&
          (r.max === null || bp.bookResponseDto.price <= r.max)
      );
    }

    // sort
    if (this.sortOption === 'bestseller') {
      filtered.sort(
        (a, b) => b.bookResponseDto.saleStock - a.bookResponseDto.saleStock
      );
    } else if (this.sortOption === 'priceAsc') {
      filtered.sort(
        (a, b) => a.bookResponseDto.price - b.bookResponseDto.price
      );
    } else if (this.sortOption === 'priceDesc') {
      filtered.sort(
        (a, b) => b.bookResponseDto.price - a.bookResponseDto.price
      );
    } else if (this.sortOption === 'newest') {
      filtered.sort(
        (a, b) =>
          new Date(b.bookResponseDto.createdAt).getTime() -
          new Date(a.bookResponseDto.createdAt).getTime()
      );
    }

    this.books = filtered;
  }

 changeCategory(cat: string) {
  this.selectedCategory = cat;
  this.page = 0; // reset về trang 1

  if (cat === 'all') {
    this.type = 'all';
    this.loadBooksByCategory('all', this.page);
  } else if (cat === 'best-selling') {
    this.type = 'best-selling';
    this.loadBestSelling();
  } else if (cat === 'latest') {
    this.type = 'latest';
    this.loadLatestBooks();
  } else {
    this.type = 'category';
    this.category = cat;
    this.loadBooksByCategory(cat, this.page);
  }
}



  getStockWidth(bp: BookAndPromotion): number {
    const total = bp.bookResponseDto.stock + bp.bookResponseDto.saleStock;
    if (total === 0) return 0;
    return (bp.bookResponseDto.saleStock / total) * 100;
  }

  togglePriceRange(range: any) {
    // Nếu click lại cùng filter đang chọn → reset
    if (this.selectedPriceRange === range) {
      this.selectedPriceRange = null;
    } else {
      this.selectedPriceRange = range;
    }
    this.onFilterChange();
  }

  getCategoryLabel(key: string): string {
  const cat = this.categories.find(c => c.key === key);
  return cat ? cat.label : 'Tất cả sản phẩm';
}

getCurrentTitle(): string {
  if (this.type === 'latest') return 'Mới Phát Hành';
  if (this.type === 'best-selling') return 'Best Selling';
  return this.getCategoryLabel(this.category); // dùng cho all / category
}


}
