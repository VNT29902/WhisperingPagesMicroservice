export interface CouponValidationResponse {
  code: string | null;
  discountAmount: number;
  discountType?: 'PERCENTAGE' | 'FIXED_AMOUNT';
  discountValue?: number;
  maxDiscountAmount?: number | null;
  message: string;
}
