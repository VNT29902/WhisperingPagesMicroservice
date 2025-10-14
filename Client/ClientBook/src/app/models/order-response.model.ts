export interface OrderResponse {
  orderId: string;
  subtotalAmount: number;
  shippingFee: number;
  discountAmount: number;
  totalAmount: number;
  status: string;
  createdAt: string;
  couponCode?: string | null;
}
