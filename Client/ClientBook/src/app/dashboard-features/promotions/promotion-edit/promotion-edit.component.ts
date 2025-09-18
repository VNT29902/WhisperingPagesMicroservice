import { Component } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { BookService } from '../../../services/book.service';
import { PromotionUpdateRequest } from '../../../models/promotion-update-request.model';
import { CommonModule } from '@angular/common';
import { PromotionSaveResponse } from '../../../models/promotion-save-response.model';

@Component({
  selector: 'app-promotion-edit',
  standalone: true,
  imports: [FormsModule,ReactiveFormsModule,CommonModule],
  templateUrl: './promotion-edit.component.html',
  styleUrl: './promotion-edit.component.css'
})
export class PromotionEditComponent {

form!: FormGroup;
  promoId!: number;
  loading = false;

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private bookService: BookService,
    public router: Router
  ) {}

  ngOnInit(): void {
    this.promoId = Number(this.route.snapshot.paramMap.get('id'));
    this.buildForm();
    this.loadPromotion();
  }

  buildForm() {
    this.form = this.fb.group({
      name: ['', Validators.required],
      discountType: ['PERCENT', Validators.required],
      discountValue: [0, [Validators.required, Validators.min(1)]],
      targetType: ['', Validators.required],
      targetValue: [''],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      active: [true]
    });
  }

  loadPromotion() {
    this.loading = true;
    this.bookService.getPromotionById(this.promoId).subscribe({
      next: (promo: PromotionSaveResponse) => {
        this.form.patchValue({
          ...promo,
          startDate: promo.startDate?.split('T')[0],
          endDate: promo.endDate?.split('T')[0]
        });
        this.loading = false;
      },
      error: (err) => {
        console.error('❌ Failed to load promotion:', err);
        this.loading = false;
      }
    });
  }

  save() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const req: PromotionUpdateRequest = this.form.value;
    this.bookService.updatePromotion(this.promoId, req).subscribe({
      next: () => {
        alert('✅ Promotion updated successfully');
        this.router.navigate(['/dashboard/promotions']);
      },
      error: (err) => {
        console.error('❌ Update failed:', err);
        alert('Cập nhật thất bại!');
      }
    });
  }
}
