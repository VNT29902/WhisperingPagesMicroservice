import { Component } from '@angular/core';
import { BookAndPromotion } from '../../../models/bookAndPromotion.model';
import { ActivatedRoute, Router } from '@angular/router';
import { BookService } from '../../../services/book.service';
import { Book } from '../../../models/book.model';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-product-edit',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './product-edit.component.html',
  styleUrl: './product-edit.component.css'
})
export class ProductEditComponent {

  product!: Book;  // dùng interface Book
  isLoading = true;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private bookService: BookService
  ) {}

ngOnInit(): void {
  const id = this.route.snapshot.paramMap.get('id');
  if (id) {
    this.bookService.getBookById(id).subscribe({
      next: (res) => {
        // res là BookAndPromotion
        this.product = res.bookResponseDto; // chỉ lấy Book để binding form
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading product:', err);
        this.isLoading = false;
      }
    });
  }
}

saveEdit(): void {
  if (!this.product) return;

  this.bookService.updateBook(this.product.id, this.product).subscribe({
    next: () => {
      alert('Cập nhật sản phẩm thành công!');
      this.router.navigate(['/dashboard/products']);
    },
    error: (err) => {
      console.error('Error updating product:', err);
      alert('Có lỗi xảy ra khi lưu sản phẩm');
    }
  });
}


  cancelEdit(): void {
    this.router.navigate(['/dashboard/products']);
  }
}
