import { OrderItemDTO } from "./order.model";
import { ShippingAddressRequest } from "./shipping-address-request.model";

export interface CreateOrderRequest {
  paymentMethod: PaymentMethod;
  shippingAddress: ShippingAddressRequest;
  items: OrderItemDTO[];
}

export type PaymentMethod = 'COD' | 'MOMO';