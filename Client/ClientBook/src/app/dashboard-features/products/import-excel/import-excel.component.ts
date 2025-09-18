import { Component } from '@angular/core';
import { BookService } from '../../../services/book.service';

@Component({
  selector: 'app-import-excel',
  imports: [],
  templateUrl: './import-excel.component.html',
  styleUrl: './import-excel.component.css'
})
export class ImportExcelComponent {

selectedFile?: File;

  constructor(private bookService: BookService) {}

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  upload() {
    if (this.selectedFile) {
      this.bookService.uploadExcel(this.selectedFile).subscribe({
        next: (res) => alert(`✅ Import thành công ${res.length} sản phẩm!`),
        error: () => alert('❌ Import thất bại!')
      });
    }
  }
}
