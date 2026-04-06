import { OrderItemDTO } from "./order.model";
import { ShippingAddressRequest } from "./shipping-address-request.model";

export interface CreateOrderRequest {
  paymentMethod: PaymentMethod;
  shippingAddress: ShippingAddressRequest;
  items: OrderItemDTO[];
  couponCode?: string | null;
}

export type PaymentMethod = 'COD' | 'MOMO';