import { Component, NgModule, OnInit } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { AuthService } from '../services/auth.service';
import { UserInfoResponse } from '../models/auth-info-response.model';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-user-pages',
  imports: [
    HeaderComponent,
    FooterComponent,
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
  ],
  standalone: true,
  templateUrl: './user-pages.component.html',
  styleUrl: './user-pages.component.css',
})
export class UserPagesComponent implements OnInit {
  userInfo!: UserInfoResponse;
  userForm: FormGroup;
  showCreateUserForm = false;

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private authService: AuthService
  ) {
    this.userForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', Validators.required],
      userName: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    const userName = localStorage.getItem('userName') || '';
    if (userName) {
      this.userService.getUserByUsername(userName).subscribe({
        next: (res) => (this.userInfo = res),
        error: (err) => console.error('❌ Lỗi lấy user info:', err),
      });
    }
  }

openCreateUser() {
  this.showCreateUserForm = true;

  const email = this.userInfo?.email || '';
  const username = email.includes('@') ? email.split('@')[0] : '';

  this.userForm.patchValue({
    firstName: this.userInfo?.firstName || '',
    lastName: this.userInfo?.lastName || '',
    phone: this.userInfo?.phone || '',
    email: email,
    userName: username
  });
}



  cancelCreateUser() {
    this.showCreateUserForm = false;
  }

  submitCreateUser() {
    if (this.userForm.valid) {
      this.userService.createUser(this.userForm.value).subscribe({
        next: (res) => {
          alert('Tạo user thành công!');
          this.showCreateUserForm = false;
        },
        error: (err) => {
          console.error(err);
          alert('Có lỗi xảy ra!');
        },
      });
    }
  }
}
