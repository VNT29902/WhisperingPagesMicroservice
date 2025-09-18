import { UserService } from './../../../services/user.service';
import { Component, OnInit } from '@angular/core';
import { UserResponse } from '../../../models/user-response.model';
import { ShippingAddressResponse } from '../../../models/shipping-address-response.model';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-detail',
  standalone: true,
  imports: [CommonModule,RouterLink],
  templateUrl: './user-detail.component.html',
  styleUrl: './user-detail.component.css'
})
export class UserDetailComponent implements OnInit {
  username!: string;
  user?: UserResponse;
  shippingAddresses: ShippingAddressResponse[] = [];

  constructor(
    private route: ActivatedRoute,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.username = this.route.snapshot.paramMap.get('username')!;
    this.loadUser();
    this.loadShippingAddresses();
  }

  loadUser() {
    this.userService.getUserByUsername(this.username).subscribe({
      next: (res) => (this.user = res),
      error: (err) => console.error(err)
    });
  }

  loadShippingAddresses() {
    this.userService.getShippingAddresses(this.username).subscribe({
      next: (res) => (this.shippingAddresses = res),
      error: (err) => console.error(err)
    });
  }
}
