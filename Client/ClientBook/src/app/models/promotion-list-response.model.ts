export interface PromotionListResponse {
  id: number;
  name: string;
  discountType: string;
  discountValue: number;
  targetType: string;
  targetValue: string;
  startDate: string;
  endDate: string;
  active: boolean;
}
