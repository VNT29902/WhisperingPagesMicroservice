import { Component, OnDestroy, OnInit } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { BestsellingComponent } from '../bestselling/bestselling.component';
import { PopularbookComponent } from '../popularbook/popularbook.component';
import { AboutPageComponent } from '../about-page/about-page.component';
import { FooterComponent } from '../footer/footer.component';
import { BookAndPromotion } from '../models/bookAndPromotion.model';
import { BookService } from '../services/book.service';
import { forkJoin } from 'rxjs';
import { catchError, map, of } from 'rxjs';
import { LatestReleaseBooksComponent } from '../latest-release-books/latest-release-books.component';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

type PromoItem = {
  image: string;
  label?: string;
  link: string;
};

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    HeaderComponent,
    LatestReleaseBooksComponent,
    BestsellingComponent,
    PopularbookComponent,
    AboutPageComponent,
    FooterComponent,
  ],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit, OnDestroy {
  latestBooks: BookAndPromotion[] = [];
  bestsellingBooks: BookAndPromotion[] = [];
  categoryBooks: BookAndPromotion[] = [];

  featuredBooks: BookAndPromotion[] = [];
  partialLoadFailed = false;
  loadFailed = false;
  currentHeroSlide = 0;
  private heroIntervalId: ReturnType<typeof setInterval> | null = null;

  readonly heroSlides: PromoItem[] = [
    { image: 'https://cdn1.fahasa.com/media/magentothem/banner7/trangCTT4_1504_840x320.png', link: '/products' },
    { image: 'https://cdn1.fahasa.com/media/magentothem/banner7/AzVietNamt4_resize_840x320.png', link: '/products' },
    { image: 'https://cdn1.fahasa.com/media/magentothem/banner7/TrangUuDaiT3_Resize_840x320.png', link: '/products' },
    { image: 'https://cdn1.fahasa.com/media/magentothem/banner7/LDP_SachThamKhao_840x320.png', link: '/products' },
    { image: 'https://cdn1.fahasa.com/media/magentothem/banner7/megabook_t4_840x320.png', link: '/products' },
  ];

  readonly rightHeroPromos: PromoItem[] = [
    { image: 'https://cdn1.fahasa.com/media/wysiwyg/Thang-04-2026/Fahasa_392x156.jpg', link: '/products' },
    { image: 'https://cdn1.fahasa.com/media/magentothem/banner7/TrangUuDaiT3_Resize_840x320.png', link: '/products' },
  ];


  readonly featuredCategories: string[] = [
    'Fiction',
    'Non-Fiction',
    'Children',
    'Education',
    'Art & Design',
    'Rare Finds',
  ];

  readonly iconCategories: string[] = [
    'Sách tiếng Việt',
    'Sách ngoại văn',
    'Văn phòng phẩm',
    'Quà tặng',
    'Đồ chơi',
    'Manga/Light novel',
  ];

  constructor(private bookService: BookService) {}

  ngOnInit(): void {
    this.startHeroAutoplay();

    forkJoin({
      latest: this.bookService.getLatestBooks().pipe(
        map((data) => ({ data: data || [], failed: false })),
        catchError((err) => {
          console.error('❌ Lỗi latest books:', err);
          return of({ data: [] as BookAndPromotion[], failed: true });
        })
      ),
      bestselling: this.bookService.getBestSelling().pipe(
        map((data) => ({ data: data || [], failed: false })),
        catchError((err) => {
          console.error('❌ Lỗi best selling books:', err);
          return of({ data: [] as BookAndPromotion[], failed: true });
        })
      ),
      category: this.bookService.getBooksByCategory('all', 12, 0).pipe(
        map((res) => ({ data: res?.content || [], failed: false })),
        catchError((err) => {
          console.error('❌ Lỗi category books:', err);
          return of({ data: [] as BookAndPromotion[], failed: true });
        })
      ),
    }).subscribe({
      next: (result) => {
        this.latestBooks = result.latest.data;
        this.bestsellingBooks = result.bestselling.data;
        this.categoryBooks = result.category.data;
        this.featuredBooks = this.categoryBooks.slice(0, 6);
        const failedCount = [result.latest.failed, result.bestselling.failed, result.category.failed].filter(Boolean).length;
        this.partialLoadFailed = failedCount > 0 && failedCount < 3;
        this.loadFailed = failedCount === 3;
      },
      error: (err) => {
        console.error('❌ Lỗi khi lấy dữ liệu trang chủ:', err);
        this.featuredBooks = [];
        this.partialLoadFailed = false;
        this.loadFailed = true;
      },
    });
  }

  ngOnDestroy(): void {
    if (this.heroIntervalId) {
      clearInterval(this.heroIntervalId);
      this.heroIntervalId = null;
    }
  }

  nextHeroSlide(): void {
    this.currentHeroSlide = (this.currentHeroSlide + 1) % this.heroSlides.length;
  }

  previousHeroSlide(): void {
    this.currentHeroSlide = (this.currentHeroSlide - 1 + this.heroSlides.length) % this.heroSlides.length;
  }

  goToHeroSlide(index: number): void {
    this.currentHeroSlide = index;
  }

  private startHeroAutoplay(): void {
    this.heroIntervalId = setInterval(() => {
      this.nextHeroSlide();
    }, 4500);
  }

  getDiscountPrice(book: BookAndPromotion): number {
    const price = Number(book.bookResponseDto.price || 0);
    const discount = Number(book.promotionPercent || 0);
    return Math.max(price - (price * discount) / 100, 0);
  }
}
