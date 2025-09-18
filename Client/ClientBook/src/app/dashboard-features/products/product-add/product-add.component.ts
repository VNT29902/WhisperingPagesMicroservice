import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Book } from '../../../models/book.model';
import { BookService } from '../../../services/book.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-product-add',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './product-add.component.html',
  styleUrl: './product-add.component.css'
})
export class ProductAddComponent {

  product: Book = {
    id: '',
    title: '',
    author: '',
    price: 0,
    category: '',
    image: '',
    saleStock: 0,
    stock: 0,
    description: '',
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  constructor(
    private bookService: BookService,
    private router: Router
  ) {}

  save(): void {
    // Bọc product vào mảng để gọi service
    this.bookService.createBooks([this.product]).subscribe({
      next: () => {
        alert('Thêm sản phẩm thành công!');
        this.router.navigate(['/dashboard/products']);
      },
      error: (err) => {
        console.error('Error creating product:', err);
        alert('Có lỗi khi thêm sản phẩm');
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/dashboard/products']);
  }
}


