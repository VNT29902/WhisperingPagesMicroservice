export interface PromotionSaveRequest {
  name: string;
  discountType: string;
  discountValue: number;
  targetType: string;
  targetValue: string;
  startDate: string;
  endDate: string;
}
