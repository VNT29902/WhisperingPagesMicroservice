import { Component } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { Router, RouterModule } from '@angular/router';
import { LoginInfo } from '../models/auth-login.model';
import { AuthService } from '../services/auth.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AccountService } from '../services/account.service';

@Component({
  selector: 'app-login-page',
  imports: [HeaderComponent, FooterComponent, RouterModule, ReactiveFormsModule, CommonModule, FormsModule],
  standalone: true,
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.css'
})
export class LoginPageComponent {

  loginInfo: LoginInfo = {
    emailOrPhone: '',
    password: '',
    deviceId:'',

  };

  constructor(private authService: AuthService, private router: Router,
    private accountService: AccountService
  ) { }

  onLogin(): void {
  // ✅ luôn đảm bảo deviceId tồn tại
  this.loginInfo.deviceId = this.ensureDeviceId();

  this.authService.loginUser(this.loginInfo).subscribe({
    next: (res) => {
      localStorage.setItem('accessToken', res.accessToken);
      localStorage.setItem('refreshToken', res.refreshToken);
      localStorage.setItem('userName', res.userName);
      localStorage.setItem('role',res.role)

      this.authService.loginSuccess();
 this.router.navigate(['/']);
  
    },
    error: () => {
      alert('Đăng nhập thất bại');
    },
  });
}


loginWithGoogle(): void {
  const deviceId = this.ensureDeviceId();
  window.location.href = `http://localhost:8086/oauth2/authorization/google?state=${deviceId}`;
}



private ensureDeviceId(): string {
  let deviceId = localStorage.getItem('deviceId');
  if (!deviceId) {
    const ua = navigator.userAgent;
    if (/tablet|ipad|playbook|silk/i.test(ua)) {
      deviceId = 'tablet';
    } else if (/Mobile|iP(hone|od)|Android|BlackBerry|IEMobile|Silk-Accelerated|(hpw|web)OS|Opera M(obi|ini)/.test(ua)) {
      deviceId = 'mobile';
    } else {
      deviceId = 'desktop';
    }
    localStorage.setItem('deviceId', deviceId);
  }
  return deviceId;
}


}
