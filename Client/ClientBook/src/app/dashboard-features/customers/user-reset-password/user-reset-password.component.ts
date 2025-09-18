import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-user-reset-password',
  standalone: true,
  imports: [FormsModule, CommonModule,RouterLink],
  templateUrl: './user-reset-password.component.html',
  styleUrls: ['./user-reset-password.component.css'] // 👈 sửa lại styleUrls
})
export class UserResetPasswordComponent implements OnInit {
  username!: string;
  newPassword: string = '';
  confirmPassword: string = '';
  showPassword: boolean = false;
  showConfirmPassword: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.username = this.route.snapshot.paramMap.get('username')!;
  }

  onResetPassword() {
    if (!this.newPassword || !this.confirmPassword) {
      alert('❌ Vui lòng nhập đầy đủ mật khẩu');
      return;
    }

    if (this.newPassword !== this.confirmPassword) {
      alert('❌ Mật khẩu xác nhận không khớp');
      return;
    }

    this.authService.resetPassword(this.username, this.newPassword).subscribe({
      next: (res: any) => {
        // Nếu backend trả JSON { message: "..." }
        alert(res.message ?? '✅ Reset thành công');
        this.router.navigate(['/dashboard/users']); // quay lại list
      },
      error: (err) => {
        console.error(err);
        alert('❌ Reset mật khẩu thất bại');
      }
    });
  }
}
