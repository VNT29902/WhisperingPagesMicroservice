import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DeviceService {
  private readonly STORAGE_KEY = 'deviceId';

  constructor() {
    // khi service khởi tạo thì đảm bảo đã có deviceId
    if (!localStorage.getItem(this.STORAGE_KEY)) {
      localStorage.setItem(this.STORAGE_KEY, this.detectDeviceType());
    }
  }

  // ✅ hàm detect
  private detectDeviceType(): string {
    const ua = navigator.userAgent;
    if (/tablet|ipad|playbook|silk/i.test(ua)) return 'tablet';
    if (/Mobile|iP(hone|od)|Android|BlackBerry|IEMobile|Silk-Accelerated|(hpw|web)OS|Opera M(obi|ini)/.test(ua)) {
      return 'mobile';
    }
    return 'desktop';
  }

  // ✅ lấy deviceId (luôn từ localStorage)
  getDeviceId(): string {
    return localStorage.getItem(this.STORAGE_KEY) || this.detectDeviceType();
  }
}
