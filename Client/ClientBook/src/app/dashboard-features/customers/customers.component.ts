import { AuthService } from './../../services/auth.service';
import { Component } from '@angular/core';
import { UserResponse } from '../../models/user-response.model';
import { UserService } from '../../services/user.service';
import { PageResponse } from '../../models/page-response.model';
import { UserGetAllResponse } from '../../models/user-all-response.model';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UserResetPasswordComponent } from './user-reset-password/user-reset-password.component';

@Component({
  selector: 'app-customers',
  standalone: true,
  imports: [RouterLink,FormsModule,CommonModule],
  templateUrl: './customers.component.html',
  styleUrl: './customers.component.css'
})
export class DashBoardCustomersComponent {

users: UserGetAllResponse[] = [];
  allUsers: UserGetAllResponse[] = []; // dữ liệu gốc từ API


  keyword = '';
  activeFilter = '';
  page = 0;
  pageSize = 10;
  totalPages = 0;
  deleteSuccessMessage = '';

  constructor(private userService: UserService,
    private authService: AuthService



  ) {}

  ngOnInit(): void {
    this.loadUsers();
  }

 loadUsers() {
    this.authService.getAllUsers(this.page, this.pageSize).subscribe((res: PageResponse<UserGetAllResponse>) => {
      this.allUsers = res.content;
      this.applyFilters(); // filter ngay sau khi load
      this.totalPages = res.totalPages;
    });
  }
 applyFilters() {
  let filtered = [...this.allUsers];

  // ✅ Filter theo keyword: username | email | phone
  if (this.keyword.trim() !== '') {
    const kw = this.keyword.toLowerCase();
    filtered = filtered.filter(
      (u) =>
        (u.username && u.username.toLowerCase().includes(kw)) ||  // chứa trong username
        (u.email && u.email.toLowerCase().includes(kw)) ||        // chứa trong email
        (u.phone && u.phone.toLowerCase().includes(kw))           // chứa trong phone
    );
  }

  // ✅ Filter theo active status
  if (this.activeFilter === 'true') {
    filtered = filtered.filter((u) => u.enabled);
  } else if (this.activeFilter === 'false') {
    filtered = filtered.filter((u) => !u.enabled);
  }

  this.users = filtered;
}

 resetFilters() {
    this.keyword = '';
    this.activeFilter = '';
    this.applyFilters();
  }


  confirmChangeStatus(user: UserGetAllResponse) {
    if (confirm(`Do you want to ${user.enabled ? 'deactivate' : 'activate'} this user?`)) {
      this.authService.changeStatus(user.id).subscribe((updated) => {
        user.enabled = updated.enabled;
        this.applyFilters(); // update lại filter
      });
    }
  }


  confirmDelete(user: UserResponse) {
    if (confirm('Are you sure you want to delete this user?')) {
      // TODO: gọi API delete
      this.deleteSuccessMessage = 'User deleted successfully';
      this.loadUsers();
    }
  }

  prevPage() {
    if (this.page > 0) {
      this.page--;
      this.loadUsers();
    }
  }

  nextPage() {
    if (this.page < this.totalPages - 1) {
      this.page++;
      this.loadUsers();
    }
  }

  goToPage(i: number) {
    this.page = i;
    this.loadUsers();
  }
}
