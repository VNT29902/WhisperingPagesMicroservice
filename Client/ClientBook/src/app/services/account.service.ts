import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { AccountProfile } from '../models/account.model';

@Injectable({ providedIn: 'root' })
export class AccountService {
  private apiUrl = `${environment.apiUrl}/account/me`;
  private profileSubject = new BehaviorSubject<AccountProfile | null>(null);
  profile$ = this.profileSubject.asObservable();

  constructor(
    private http: HttpClient,
    @Inject(PLATFORM_ID) private platformId: Object // 👈 inject để check browser
  ) {
   
  }



  setProfile(profile: AccountProfile) {
    this.profileSubject.next(profile);
  }

  clearProfile() {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem('userName');
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
    }
   
  }

 

  
}
