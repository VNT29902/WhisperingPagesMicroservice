export interface ShippingInfoDto {
  recipientName: string;
  phoneNumber: string;
  province: string;
  district: string;
  ward: string;
  street: string;
  note?: string;
}

export interface ShippingInfoResponse  {

  id: string;
  fullName:string;
  email: string;
  phone:string;
  address: string;
  status:string;
  paymentMethod:string;
}
