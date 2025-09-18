import { Component, OnInit } from '@angular/core';
import { FooterComponent } from '../footer/footer.component';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { LocationService, Province, Ward } from '../services/location.service';
import { HeaderComponent } from '../header/header.component';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-address',
  standalone: true,
  imports: [
    HeaderComponent,
    FooterComponent,
    CommonModule,
    ReactiveFormsModule,
  ],
  templateUrl: './address.component.html',
  styleUrl: './address.component.css',
})
export class AddressComponent implements OnInit {
  showForm = false;
  mode: 'create' | 'edit' = 'create';
  editingAddressId: string | null = null;

  provinces: Province[] = [];
  wards: Ward[] = [];

  addressForm!: FormGroup;
  address: any = null;

  provincesLoaded = false;
  wardsLoaded = false;
   updateMessage: string = ''; 

  constructor(
    private fb: FormBuilder,
    private locationService: LocationService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.addressForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      phone: ['', Validators.required],
      note: [''],
      street: ['', Validators.required],
      country: ['Vietnam'],
      province: ['', Validators.required],
      ward: ['', Validators.required],
      zipCode: [''],
    });

    this.loadProvinces();

    const userName = localStorage.getItem('userName') || '';
    this.loadShippingAddress(userName);

    this.addressForm.get('province')?.valueChanges.subscribe((provinceName) => {
      this.onProvinceChange(provinceName);
    });
  }

  loadProvinces() {
    this.locationService.getProvinces().subscribe((res) => {
      this.provinces = res.data;
      this.provincesLoaded = true;

      if (this.provinces.length > 0) {
        const firstProvince = this.provinces[0];
        this.addressForm.patchValue({ province: firstProvince.full_name });
        this.loadWardsByProvinceName(firstProvince.full_name);
      }
    });
  }

  loadWardsByProvinceName(provinceName: string) {
    const selectedProvince = this.provinces.find(
      (p) => p.full_name === provinceName
    );

    if (selectedProvince) {
      this.wardsLoaded = false;

      this.locationService
        .getWardsByProvinceId(selectedProvince.id)
        .subscribe((res) => {
          this.wards = res.data;
          this.wardsLoaded = true;
          this.addressForm.patchValue({ ward: '' });
        });
    }
  }

  onProvinceChange(provinceName: string) {
    this.loadWardsByProvinceName(provinceName);
  }

  loadShippingAddress(userName: string) {
    this.userService.getShippingAddresses(userName).subscribe({
      next: (addresses) => {
        if (addresses.length > 0) {
          const defaultAddress =
            addresses.find((addr) => addr.isDefault) || addresses[0];

          this.address = {
            id: defaultAddress.id, // ✅ Ghi nhớ id để cập nhật
            fullName: `${defaultAddress.recipientFirstName} ${defaultAddress.recipientLastName}`,
            phone: defaultAddress.phoneNumber,
            company: defaultAddress.note || '',
            street: defaultAddress.street,
            ward: defaultAddress.ward,
            province: defaultAddress.province,
            country: 'Vietnam',
            zipCode: '',
          };
        }
      },
      error: () => {
        this.address = null;
      },
    });
  }

  openEditAddress() {
    if (!this.provincesLoaded || !this.wardsLoaded) {
      console.warn('⏳ Dữ liệu chưa sẵn sàng để mở form.');
      return;
    }

    if (this.address) {
      const [firstName, ...rest] = this.address.fullName.trim().split(' ');
      const lastName = rest.join(' ');

      this.addressForm.patchValue({
        firstName,
        lastName,
        phone: this.address.phone,
        note: this.address.company,
        street: this.address.street,
        province: this.address.province,
        ward: this.address.ward,
        country: 'Vietnam',
        zipCode: '',
      });

      this.editingAddressId = this.address.id;
      this.mode = 'edit';
      this.showForm = true;
    }
  }

  openCreateAddress() {
    this.addressForm.reset({
      country: 'Vietnam',
    });
    this.editingAddressId = null;
    this.mode = 'create';
    this.showForm = true;
  }

  cancel() {
    this.showForm = false;
    this.editingAddressId = null;
    this.mode = 'create';
  }

  submitAddress() {
    console.log('🟢 submitAddress chạy');

    if (this.addressForm.valid) {
      const formValue = this.addressForm.value;
      const userName = localStorage.getItem('userName') || '';

      const request = {
        recipientFirstName: formValue.firstName,
        recipientLastName: formValue.lastName,
        phoneNumber: formValue.phone,
        street: formValue.street,
        province: formValue.province,
        ward: formValue.ward,
        email:formValue.email,
        note: formValue.note || '',
        isDefault: true,
      };

      if (this.mode === 'edit' && this.editingAddressId) {
        console.log('🔄 Gọi update địa chỉ với id:', this.editingAddressId);

        this.userService
          .updateShippingAddress(this.editingAddressId, userName, request)
          .subscribe({
            next: () => {
              console.log('✅ Đã cập nhật địa chỉ');
              this.afterSubmit(userName);
            },
            error: (err) => {
              console.error('❌ Lỗi cập nhật địa chỉ:', err);
            },
          });
      } else {
        console.log('➕ Gọi create địa chỉ');

        this.userService.createShippingAddress(userName, request).subscribe({
          next: () => {
            console.log('✅ Đã thêm địa chỉ mới');
            this.afterSubmit(userName);
          },
          error: (err) => {
            console.error('❌ Lỗi thêm địa chỉ:', err);
          },
        });
      }
    } else {
      console.warn('❌ Form không hợp lệ');
      console.table(this.addressForm.value);
    }
  }

  afterSubmit(userName: string) {
    this.loadShippingAddress(userName);
    this.showForm = false;
    this.editingAddressId = null;
    this.mode = 'create';
  }

  deleteAddress() {
  const userName = localStorage.getItem('userName') || '';
  const addressId = this.address?.id;

  if (!addressId) {
    console.warn('❌ Không tìm thấy ID địa chỉ để xoá');
    return;
  }

  const confirmed = confirm('Bạn có chắc muốn xoá địa chỉ này không?');
  if (!confirmed) return;

  this.userService.deleteShippingAddress(userName, addressId).subscribe({
    next: () => {
      console.log('🗑️ Đã xoá địa chỉ');
      this.address = null;
      this.updateMessage = '✅ Địa chỉ đã được xoá thành công!';
    },
    error: (err) => {
      console.error('❌ Lỗi khi xoá địa chỉ:', err);
    }
  });
}

}
