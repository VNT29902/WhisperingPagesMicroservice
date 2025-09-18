import { OrderItemResponse } from "./order.model";
import { OrderStatus } from "./orders-status";

export interface OrderWithItemsResponse {
  orderId: string;
  status: OrderStatus;
  userName: string,
  createdAt: string; // LocalDateTime bên Java trả về dạng ISO string
  totalAmount: number;
  items: OrderItemResponse[];
}