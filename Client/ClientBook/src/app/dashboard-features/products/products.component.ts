import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { BookAndPromotion } from '../../models/bookAndPromotion.model';
import { BookService } from '../../services/book.service';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink, RouterModule } from '@angular/router';

@Component({
  selector: 'app-products',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, RouterModule],
  templateUrl: './products.component.html',
  styleUrl: './products.component.css'
})
export class DashBoardProductsComponent {
  books: BookAndPromotion[] = [];
  keyword = '';
  selectedCategory = '';
  selectedPriceRange = '';
  minPrice?: number;
  maxPrice?: number;
  page = 0;
  totalPages = 1;

constructor(
  private bookService: BookService,
  public route: ActivatedRoute
) {}


  ngOnInit() {
    this.loadBooks();
  }

  onFilterChange() {
    if (this.selectedPriceRange) {
      const [min, max] = this.selectedPriceRange.split('-').map(v => +v);
      this.minPrice = min;
      this.maxPrice = max;
    } else {
      this.minPrice = undefined;
      this.maxPrice = undefined;
    }
    this.page = 0; 
    this.loadBooks();
  }

  loadBooks() {
    this.bookService.searchBooksAdmin(
      this.keyword,
      this.selectedCategory,
      this.minPrice,
      this.maxPrice,
      this.page,
      10
    ).subscribe(res => {
      this.books = res.content;
      this.totalPages = res.totalPages || 1;
    });
  }

  goToPage(i: number) {
    this.page = i;
    this.loadBooks();
  }

  prevPage() {
    if (this.page > 0) {
      this.page--;
      this.loadBooks();
    }
  }

  nextPage() {
    if (this.page < this.totalPages - 1) {
      this.page++;
      this.loadBooks();
    }
  }

  deleteBook(id: string) {
    if (confirm(`Bạn có chắc muốn xóa product với ID: ${id} không?`)) {
      this.bookService.deleteBook(id).subscribe(() => {
        this.books = this.books.filter(b => b.bookResponseDto.id !== id);
      });
    }
  }
}
