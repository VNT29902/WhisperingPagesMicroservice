import { CommonModule } from '@angular/common';
import { FooterComponent } from '../footer/footer.component';
import { HeaderComponent } from './../header/header.component';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ChangePasswordRequest } from '../models/change-password.model';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-change-password',
  imports: [HeaderComponent,FooterComponent,CommonModule,ReactiveFormsModule],
  templateUrl: './change-password.component.html',
  styleUrl: './change-password.component.css'
})
export class ChangePasswordComponent {
  changePasswordForm: FormGroup;
  submitted = false;
  successMessage = '';
  errorMessage = '';
  username = localStorage.getItem('userName') || ''; // hoặc lấy từ AuthService nếu có

  constructor(
    private fb: FormBuilder,
    private accountService: AuthService
  ) {
    this.changePasswordForm = this.fb.group({
      currentPassword: ['', [Validators.required, Validators.minLength(8)]],
      newPassword: ['', [Validators.required, Validators.minLength(8)]],
      confirmPassword: ['', [Validators.required]]
    }, { validators: this.passwordMatchValidator });
  }

  passwordMatchValidator(form: FormGroup) {
    const newPass = form.get('newPassword')?.value;
    const confirm = form.get('confirmPassword')?.value;
    return newPass === confirm ? null : { mismatch: true };
  }

  onSubmit() {
    this.submitted = true;

    if (this.changePasswordForm.invalid) {
      return;
    }

    const request: ChangePasswordRequest = {
      currentPassword: this.changePasswordForm.value.currentPassword,
      newPassword: this.changePasswordForm.value.newPassword,
      confirmPassword: this.changePasswordForm.value.confirmPassword
    };

    this.accountService.changePassword(this.username, request).subscribe({
      next: (res) => {
        this.successMessage = res;
        this.errorMessage = '';
        this.changePasswordForm.reset();
        this.submitted = false;
      },
      error: (err) => {
        this.errorMessage = err.error || '❌ Đổi mật khẩu thất bại';
        this.successMessage = '';
      }
    });
  }
}
