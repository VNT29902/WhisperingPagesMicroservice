import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { BookService } from '../services/book.service';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { BookAndPromotion } from '../models/bookAndPromotion.model';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-search-page',
  standalone: true,
  imports: [
    CommonModule,
    HeaderComponent,
    FooterComponent,
    RouterLink,
    FormsModule,
  ],
  templateUrl: './search-page.component.html',
  styleUrl: './search-page.component.css',
})
export class SearchPageComponent {
  keyword: string = '';
  allBooks: BookAndPromotion[] = []; // dữ liệu gốc từ API
  books: BookAndPromotion[] = []; // dữ liệu sau khi filter
  page = 0;
  totalPages = 1;

 priceRanges = [
  { label: '0đ - 150,000đ', value: '0-150000' },
  { label: '150,000đ - 300,000đ', value: '150000-300000' },
  { label: '300,000đ - 500,000đ', value: '300000-500000' },
  { label: '500,000đ - 700,000đ', value: '500000-700000' },
  { label: '700,000đ - Trở lên', value: '700000-' }
];

  selectedPriceRange: any = null;
  sortOption: string = 'newest';

  constructor(
    private route: ActivatedRoute,
    private bookService: BookService
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe((params) => {
      this.keyword = params['keyword'] || '';
      this.page = +params['page'] || 0;
      this.fetchData();
    });
  }

  fetchData() {
  this.bookService.searchBooks(this.keyword, this.page, 50).subscribe({
    next: (res) => {
      this.allBooks = res.content;   // dữ liệu gốc
      this.totalPages = res.totalPages;
      this.applyFilters();           // áp filter ở FE
    },
    error: (err) => console.error(err)
  });
}

applyFilters() {
  let filtered = [...this.allBooks];

  // ✅ lọc theo giá
  if (this.selectedPriceRange) {
    const [minStr, maxStr] = this.selectedPriceRange.value.split('-');
    const min = +minStr;
    const max = maxStr ? +maxStr : Number.MAX_SAFE_INTEGER;

    filtered = filtered.filter(b => {
      const price = b.bookResponseDto.price;
      return price >= min && price <= max;
    });
  }

  // ✅ sắp xếp
  switch (this.sortOption) {
    case 'priceAsc':
      filtered.sort((a, b) => a.bookResponseDto.price - b.bookResponseDto.price);
      break;
    case 'priceDesc':
      filtered.sort((a, b) => b.bookResponseDto.price - a.bookResponseDto.price);
      break;
    case 'newest':
      filtered.sort((a, b) =>
        new Date(b.bookResponseDto.createdAt).getTime() -
        new Date(a.bookResponseDto.createdAt).getTime()
      );
      break;
    case 'bestseller':
      filtered.sort((a, b) => b.bookResponseDto.saleStock - a.bookResponseDto.saleStock);
      break;
  }

  this.books = filtered;
}



  goToPage(p: number) {
    if (p < 0 || p >= this.totalPages) return;
    this.page = p;
    this.fetchData();
  }
 togglePriceRange(range: any) {
  if (this.selectedPriceRange === range) {
    this.selectedPriceRange = null;   // bỏ filter nếu click lại
  } else {
    this.selectedPriceRange = range;
  }
  this.applyFilters();
}


 onFilterChange() {
  this.applyFilters();
}

}
