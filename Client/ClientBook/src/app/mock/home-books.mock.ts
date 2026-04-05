import { BookAndPromotion } from '../models/bookAndPromotion.model';

const now = new Date();

export const HOME_FEATURED_BOOKS: BookAndPromotion[] = [
  {
    promotionPercent: 20,
    bookResponseDto: {
      id: 'demo-atomic-habits',
      title: 'Atomic Habits',
      author: 'James Clear',
      price: 189000,
      category: 'Kỹ năng sống',
      image: 'assets/images/default.png',
      saleStock: 360,
      stock: 520,
      description: 'Xây dựng thói quen tốt và loại bỏ thói quen xấu bằng các bước nhỏ.',
      createdAt: now,
      updatedAt: now,
    },
  },
  {
    promotionPercent: 15,
    bookResponseDto: {
      id: 'demo-1984',
      title: '1984',
      author: 'George Orwell',
      price: 129000,
      category: 'Tiểu thuyết kinh điển',
      image: 'assets/images/default.png',
      saleStock: 250,
      stock: 410,
      description: 'Tác phẩm kinh điển về xã hội toàn trị và quyền tự do cá nhân.',
      createdAt: now,
      updatedAt: now,
    },
  },
  {
    promotionPercent: 25,
    bookResponseDto: {
      id: 'demo-dac-nhan-tam',
      title: 'Đắc Nhân Tâm',
      author: 'Dale Carnegie',
      price: 99000,
      category: 'Kỹ năng giao tiếp',
      image: 'assets/images/default.png',
      saleStock: 480,
      stock: 620,
      description: 'Cuốn sách kinh điển về nghệ thuật ứng xử và thuyết phục.',
      createdAt: now,
      updatedAt: now,
    },
  },
  {
    promotionPercent: 10,
    bookResponseDto: {
      id: 'demo-sapiens',
      title: 'Sapiens: Lược Sử Loài Người',
      author: 'Yuval Noah Harari',
      price: 219000,
      category: 'Lịch sử - Khoa học',
      image: 'assets/images/default.png',
      saleStock: 190,
      stock: 320,
      description: 'Hành trình phát triển của loài người từ quá khứ đến hiện đại.',
      createdAt: now,
      updatedAt: now,
    },
  },
  {
    promotionPercent: 30,
    bookResponseDto: {
      id: 'demo-think-fast',
      title: 'Tư Duy Nhanh Và Chậm',
      author: 'Daniel Kahneman',
      price: 199000,
      category: 'Tâm lý học',
      image: 'assets/images/default.png',
      saleStock: 175,
      stock: 300,
      description: 'Khám phá 2 hệ thống tư duy ảnh hưởng đến mọi quyết định của chúng ta.',
      createdAt: now,
      updatedAt: now,
    },
  },
  {
    promotionPercent: 18,
    bookResponseDto: {
      id: 'demo-clean-code',
      title: 'Clean Code',
      author: 'Robert C. Martin',
      price: 259000,
      category: 'Công nghệ',
      image: 'assets/images/default.png',
      saleStock: 142,
      stock: 280,
      description: 'Hướng dẫn viết mã nguồn sạch, dễ bảo trì và chuyên nghiệp.',
      createdAt: now,
      updatedAt: now,
    },
  },
];

export const HOME_BESTSELLER_FALLBACK = HOME_FEATURED_BOOKS.slice(0, 5);
export const HOME_LATEST_FALLBACK = HOME_FEATURED_BOOKS.slice(1, 6);
export const HOME_CATEGORY_FALLBACK = HOME_FEATURED_BOOKS.slice(0, 8);
