import { ProductOfCart } from './add-to-cart.model';
import { CartItemDTO } from './cart-item-dto.model';

export interface CartResponse {
  items: ProductOfCart[];
  totalAmount: number;
}
