export interface ShippingAddressResponse {
  id:string;
  recipientFirstName: string;
  recipientLastName: string;
  phoneNumber: string;
  province: string;
  ward: string;
  street: string;
  note?: string;
  isDefault: boolean;

}
