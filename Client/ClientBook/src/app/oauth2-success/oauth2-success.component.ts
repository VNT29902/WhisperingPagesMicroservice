import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AccountService } from '../services/account.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-oauth2-success',
  standalone: true,
  imports: [],
  templateUrl: './oauth2-success.component.html',
  styleUrl: './oauth2-success.component.css'
})
export class OauthSuccessComponent implements OnInit {
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private accountService: AccountService,
    private authService: AuthService
  ) {}

ngOnInit(): void {
  this.route.queryParams.subscribe(params => {
    const accessToken = params['accessToken'];
    const refreshToken = params['refreshToken'];
    const userName = params['userName'];
    const role = params['role'];   // ✅ lấy role từ URL

    if (accessToken) {
      localStorage.setItem('accessToken', accessToken);
      localStorage.setItem('refreshToken', refreshToken);
      localStorage.setItem('userName', userName);
      localStorage.setItem('role', role);

      this.authService.loginSuccess();

      // ✅ redirect sau khi login
      if (role === 'ROLE_ADMIN') {
        this.router.navigate(['/dashboard']);
      } else {
        this.router.navigate(['/']);
      }

    } else {
      this.router.navigate(['/login']);
    }
  });
}

}
