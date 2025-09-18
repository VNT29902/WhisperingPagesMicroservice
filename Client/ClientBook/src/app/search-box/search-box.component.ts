import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { BookService } from '../services/book.service';
import { BookAndPromotion } from '../models/bookAndPromotion.model';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-search-box',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterLink],
  templateUrl: './search-box.component.html',
  styleUrls: ['./search-box.component.css']
})
export class SearchBoxComponent {
  keyword: string = '';
  suggestions: BookAndPromotion[] = [];   // ✅ luôn khởi tạo

  constructor(private bookService: BookService, private router: Router) {}

 onInput() {
  if (!this.keyword.trim()) {
    this.suggestions = [];
    return;
  }

 this.bookService.searchBooks(this.keyword, 0, 6).subscribe({
  next: (data) => {
    this.suggestions = (data?.content ?? []).slice(0, 6); // 👈 chỉ giữ 6 sản phẩm
  },
  error: (err) => console.error(err)
});

}


 onSearch() {
  if (!this.keyword.trim()) return;

  // 👉 điều hướng đến search page với query param
  this.router.navigate(['/products/search-product'], { 
    queryParams: { keyword: this.keyword } 
  });

  this.suggestions = []; // clear dropdown khi điều hướng
}

  clearSearch(): void {
    this.keyword = '';
    this.suggestions = [];
  }

  
}
