import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-banner',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './banner.component.html',
  styleUrls: ['./banner.component.css']
})
export class BannerComponent {
  banners: string[] = [
    'assets/images/banner1.webp',
    'assets/images/banner2.webp',
    'assets/images/banner4.webp'
  ];

  currentSlide = 0;

  nextSlide(): void {
    this.currentSlide = (this.currentSlide + 1) % this.banners.length;
  }

  prevSlide(): void {
    this.currentSlide =
      (this.currentSlide - 1 + this.banners.length) % this.banners.length;
  }
}
