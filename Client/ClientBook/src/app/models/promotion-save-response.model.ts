export interface PromotionSaveResponse {
  id: number;
  name: string;
  targetType: string;
  targetValue: string;
  discountPercent: number;
  startDate: string;
  endDate: string;
  isActive: boolean;
}
