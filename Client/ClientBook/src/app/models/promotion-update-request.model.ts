export interface PromotionUpdateRequest {
  name: string;
  discountType: string;   // hoặc enum DiscountType nếu anh dùng enum
  discountValue: number;
  targetType: string;     // hoặc enum TargetType
  targetValue: string;
  startDate: string;      // ISO string (yyyy-MM-ddTHH:mm:ss)
  endDate: string;        // ISO string
  isActive: boolean;
}
