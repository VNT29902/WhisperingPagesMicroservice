import { UUID } from "crypto";

export interface Book {
    id: string;
    title: string;
    author: string;
    price: number;
    category: string;
    image: string;
    saleStock: number;
    stock: number;
    description: string;
    createdAt: Date;
    updatedAt: Date;
  
  }
  