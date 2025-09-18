export interface OrderItemDTO {
  productId: string;
  quantity: number;
  price: number;
  title: string;
  image: string;
}

export interface OrderItemResponse {
  id: string;
  productId: string;
  title: string;
  image: string;
  quantity: number;
  price: number; 
  addedAt: string; 
  orderId: string;
}

export interface Order {
  id: string;
  createdAt: string;
  status: string;
  totalAmount: number;
  items: OrderItemResponse[];
}
