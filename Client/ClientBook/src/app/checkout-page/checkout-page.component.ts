// checkout-page.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ReactiveFormsModule,
  FormBuilder,
  Validators,
  FormGroup,
} from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { firstValueFrom, of } from 'rxjs';
import { map, distinctUntilChanged, switchMap, tap } from 'rxjs/operators';

import { UserService } from '../services/user.service';
import { ShippingAddressResponse } from '../models/shipping-address-response.model';
import { LocationService, Province, Ward } from '../services/location.service';
import { CartResponse } from '../models/cart-response.model';
import { CartService } from '../services/cart.service';
import { OrderService } from '../services/orders.service';
import { CreateOrderRequest } from '../models/create.orders.request';
import { CouponService } from '../services/coupon.service';
import { CouponValidationResponse } from '../models/coupon.model';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';

@Component({
  selector: 'app-checkout-page',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink,
    HeaderComponent,
    FooterComponent,
  ],
  templateUrl: './checkout-page.component.html',
  styleUrls: ['./checkout-page.component.css'],
})
export class CheckoutPageComponent implements OnInit {
  form!: FormGroup;

  // address data
  addresses: ShippingAddressResponse[] = [];
  loading = false;

  // location data
  provinces: Province[] = [];
  wards: Ward[] = [];
  provincesLoaded = false;
  wardsLoaded = true; // để ward select không bị disable ngay từ đầu
  cart: CartResponse | null = null;
  couponMessage = '';
  couponError = '';
  appliedCouponCode: string | null = null;
  appliedDiscount = 0;
  couponLoading = false;

  constructor(
    private fb: FormBuilder,
    private shippingService: UserService,
    private locationService: LocationService,
    private router: Router,
    private cartService: CartService,
    private orderService: OrderService,
    private couponService: CouponService
  ) {}

  async ngOnInit(): Promise<void> {
    this.buildForm();

    const nav = this.router.getCurrentNavigation();
    this.cart =
      (nav?.extras?.state as any)?.cart ??
      (window.history.state as any)?.cart ?? // khi quay lại/forward
      null;

    // Fallback khi F5 hoặc vào trực tiếp
    if (!this.cart) {
      try {
        this.cart = await firstValueFrom(this.cartService.getCart());
      } catch {
        this.cart = { items: [], totalAmount: 0 } as any;
      }
    }

    // 1) Email từ localStorage
    const userName = localStorage.getItem('userName') ?? '';
    if (userName) this.form.patchValue({ email: `${userName}@gmail.com` });

    // 2) Load provinces trước
    await this.ensureProvincesLoaded();

    // 3) Set up reactive: province thay đổi -> load wards
    this.setupProvinceWatcher();

    // 4) Lấy địa chỉ user và bind mặc định
    try {
      this.loading = true;
      const res = await firstValueFrom(
        this.shippingService.getShippingAddresses(userName)
      );
      this.addresses = res ?? [];

      if (!this.addresses.length) {
        this.clearAddressFields();
        return;
      }

      const addr = this.addresses.find((a) => a.isDefault) ?? this.addresses[0];
      await this.bindInitialAddress(addr);
    } catch (err) {
      console.error('getShippingAddresses failed:', err);
      this.clearAddressFields();
    } finally {
      this.loading = false;
    }
  }

  private buildForm(): void {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      fullName: [''],
      phone: [''],
      street: [''],
      province: [''], // ✅ province luôn enable
      ward: [''], // ✅ ward luôn enable
      note: [''],
      paymentMethod: ['COD', Validators.required],
      couponCode: [''],
    });
  }


  // Ghép text “Sổ địa chỉ”
  get displayAddress(): string {
    const v = this.form?.value ?? {};
    return [v.fullName, v.street, v.ward, v.province]
      .map((s: string) => (s || '').trim())
      .filter(Boolean)
      .join(', ');
  }

  private setupProvinceWatcher(): void {
    this.form
      .get('province')!
      .valueChanges.pipe(
        distinctUntilChanged(),
        tap(() => {
          this.wardsLoaded = false;
          this.wards = [];
          this.form.patchValue({ ward: '' });
        }),
        switchMap((provinceName: string) =>
          provinceName
            ? this.loadWardsByProvinceName$(provinceName)
            : of<Ward[]>([])
        ),
        tap(() => (this.wardsLoaded = true))
      )
      .subscribe({
        next: (list) => (this.wards = list),
        error: () => {
          this.wards = [];
          this.wardsLoaded = true;
        },
      });
  }

  private async bindInitialAddress(
    addr: ShippingAddressResponse
  ): Promise<void> {
    this.form.patchValue({
      fullName: `${addr.recipientFirstName ?? ''} ${
        addr.recipientLastName ?? ''
      }`.trim(),
      phone: addr.phoneNumber ?? '',
      street: addr.street ?? '',
      note: addr.note ?? '',
      province: addr.province ?? '',
    });

    const provinceName = addr.province ?? '';
    if (provinceName) {
      try {
        this.wardsLoaded = false;
        this.wards = await firstValueFrom(
          this.loadWardsByProvinceName$(provinceName)
        );

        const wardFromDb = (addr.ward ?? '').trim();
        const exists = this.wards.some((w) => w.full_name === wardFromDb);
        this.form.patchValue({ ward: exists ? wardFromDb : '' });
      } catch (e) {
        this.wards = [];
        this.form.patchValue({ ward: '' });
      } finally {
        this.wardsLoaded = true;
      }
    } else {
      this.wards = [];
      this.form.patchValue({ ward: '' });
    }
  }




  private async ensureProvincesLoaded() {
    if (this.provincesLoaded && this.provinces.length) return;
    const res = await firstValueFrom(this.locationService.getProvinces());
    this.provinces = res?.data ?? [];
    this.provincesLoaded = true;
    this.form.get('province')!.enable();
  }

  private loadWardsByProvinceName$(provinceName: string) {
    const selected = this.provinces.find((p) => p.full_name === provinceName);
    if (!selected) return of<Ward[]>([]);
    return this.locationService
      .getWardsByProvinceId(selected.id)
      .pipe(map((r) => r?.data ?? []));
  }

  private clearAddressFields() {
    this.form.patchValue({
      fullName: '',
      phone: '',
      street: '',
      province: '',
      ward: '',
      note: '',
    });
    this.wards = [];
    this.wardsLoaded = true;
  }
  // get cart

  get subtotal() {
    return (this.cart?.items ?? []).reduce(
      (s, i) => s + i.price * i.quantity,
      0
    );
  }
  shippingFee = 40000;
  get discountAmount() {
    return this.appliedDiscount;
  }
  get total() {
    const total = this.subtotal + this.shippingFee - this.discountAmount;
    return total > 0 ? total : 0;
  }

  onImgError(event: Event) {
    (event.target as HTMLImageElement).src = 'assets/images/default.jpg';
  }

  // Button Orders

  private resetCouponState() {
    this.appliedCouponCode = null;
    this.appliedDiscount = 0;
    this.couponMessage = '';
    this.couponError = '';
  }

  private translateCouponError(code: string): string {
    const normalized = code?.toUpperCase?.() ?? '';
    const messages: Record<string, string> = {
      INVALID_CODE: 'Mã giảm giá không tồn tại hoặc đã hết hiệu lực.',
      COUPON_INACTIVE: 'Mã giảm giá hiện không khả dụng.',
      COUPON_NOT_STARTED: 'Mã giảm giá này chưa bắt đầu hiệu lực.',
      COUPON_EXPIRED: 'Mã giảm giá đã hết hạn sử dụng.',
      MAX_REDEMPTIONS_REACHED: 'Mã giảm giá đã được sử dụng tối đa.',
      CUSTOMER_USAGE_EXCEEDED: 'Bạn đã sử dụng mã giảm giá này đủ số lần cho phép.',
      MIN_SUBTOTAL_NOT_MET: 'Giá trị đơn hàng chưa đạt mức tối thiểu để áp dụng mã.',
      NO_DISCOUNT_AVAILABLE: 'Không có ưu đãi phù hợp với đơn hàng hiện tại.',
    };
    return messages[normalized] ?? code;
  }

  async applyCoupon() {
    const code = (this.form.get('couponCode')?.value ?? '').trim();
    this.couponError = '';
    this.couponMessage = '';

    if (!code) {
      this.resetCouponState();
      return;
    }

    const userName = localStorage.getItem('userName') ?? 'guest';
    this.couponLoading = true;
    try {
      const response: CouponValidationResponse = await firstValueFrom(
        this.couponService.validateCoupon(userName, code, this.subtotal)
      );
      this.appliedCouponCode = response.code ?? code.toUpperCase();
      this.appliedDiscount = Number(response.discountAmount ?? 0);
      this.couponMessage = response.message ?? 'Đã áp dụng mã giảm giá';
      this.couponError = '';
      this.form.patchValue({ couponCode: this.appliedCouponCode });
    } catch (err: any) {
      this.resetCouponState();
      const errorMessage = err?.error?.message || err?.error || 'Mã giảm giá không hợp lệ';
      this.couponError =
        typeof errorMessage === 'string'
          ? this.translateCouponError(errorMessage)
          : 'Mã giảm giá không hợp lệ';
    } finally {
      this.couponLoading = false;
    }
  }

  removeCoupon() {
    this.resetCouponState();
    this.form.patchValue({ couponCode: '' });
  }

  private splitFullName(fullName: string) {
    const parts = (fullName || '').trim().split(/\s+/);
    const last = parts.pop() || '';
    return { recipientFirstName: parts.join(' '), recipientLastName: last };
  }

  async placeOrder() {
    if (!this.form.valid || !this.cart?.items?.length) {
      this.form.markAllAsTouched();
      return;
    }

    const f = this.form.getRawValue();
    const { recipientFirstName, recipientLastName } = this.splitFullName(
      f.fullName
    );
    const userName = localStorage.getItem('userName') ?? 'guest';

    // 🔹 KHỚP CHUẨN CreateOrderRequest (lưu ý phoneNumber + isDefault)
   const req = {
  paymentMethod: f.paymentMethod, // 'COD' | 'MOMO'
  shippingAddress: {
    recipientFirstName,
    recipientLastName,
    phoneNumber: f.phone,
    email: f.email,
    province: f.province,
    ward: f.ward,
    street: f.street,
    note: f.note,
  },
  items: (this.cart?.items ?? []).map((it) => ({
    productId: it.productId,
    title: it.title,
    image: it.image,
    quantity: Number(it.quantity),
    price: Number(it.price),
  })),
  couponCode: this.appliedCouponCode,
} satisfies CreateOrderRequest;


    try {
      const order = await firstValueFrom(
        this.orderService.createOrder(userName, req)
      );
      alert(`✅ Đặt hàng thành công! Mã đơn: ${order.orderId}`);
      this.router.navigate(['/']); // route về HomeComponent
    } catch (e: any) {
      console.error('Create order failed', e);
      alert(e?.error ?? '❌ Tạo đơn thất bại');
    }
  }
}
