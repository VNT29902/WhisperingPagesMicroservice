import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { BookService } from '../../../services/book.service';
import { PromotionSaveRequest } from '../../../models/promotion-save-request.model';
import { PromotionSaveResponse } from '../../../models/promotion-save-response.model';

@Component({
  selector: 'app-promotion-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './promotion-create.component.html',
  styleUrls: ['./promotion-create.component.css'],
})
export class PromotionCreateComponent {
  form!: FormGroup;
  showSuccessAlert = false; // bật/tắt bảng alert

  constructor(
    private fb: FormBuilder,
    private promoService: BookService,
    public router: Router
  ) {}

    categories: { key: string; label: string }[] = [
    { key: 'triet-hoc', label: 'Triết học' },
    { key: 'khoa-hoc', label: 'Khoa học' },
    { key: 'lich-su', label: 'Lịch sử' },
    { key: 'nghe-thuat', label: 'Nghệ thuật' },
    { key: 'kinh-doanh', label: 'Kinh doanh' },
  ];

  ngOnInit(): void {
    this.form = this.fb.group({
      name: ['', Validators.required],
      discountType: ['PERCENT', Validators.required],
      discountValue: [0, [Validators.required, Validators.min(1)]],
      targetType: ['GLOBAL', Validators.required],
      targetValue: ['ALL', Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      active: [true],
    });

    this.form.get('targetType')?.valueChanges.subscribe((val) => {
      if (val === 'GLOBAL') {
        this.form.get('targetValue')?.setValue('ALL');
      } else {
        this.form.get('targetValue')?.reset('');
      }
    });
  }

 save(): void {
  if (this.form.invalid) return;

  const req: PromotionSaveRequest = {
    name: this.form.value.name,
    discountType: this.form.value.discountType,
    discountValue: this.form.value.discountValue,
    targetType: this.form.value.targetType,
    targetValue: this.form.value.targetValue,
    startDate: this.form.value.startDate,
    endDate: this.form.value.endDate,
  };

  this.promoService.createPromotion(req).subscribe({
    next: (res: PromotionSaveResponse) => {
      console.log('Promotion created:', res);
      this.showSuccessAlert = true; // bật alert

      // tự động redirect sau 2 giây
      setTimeout(() => {
        this.router.navigate(['/dashboard/promotions']);
      }, 2000);
    },
    error: (err) => {
      console.error('Error creating promotion:', err);
    },
  });
}


  redirectToList(): void {
    this.router.navigate(['/dashboard/promotions']);
  }
}
