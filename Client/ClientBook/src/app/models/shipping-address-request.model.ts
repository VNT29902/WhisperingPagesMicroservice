export interface ShippingAddressRequest {
  recipientFirstName: string;
  recipientLastName: string;
  phoneNumber: string;
  province: string;
  ward: string;
  street: string;
  note?: string;
  email: string;
 
}
