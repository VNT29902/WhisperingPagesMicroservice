package com.example.ProductService.BookDto;

import jakarta.validation.constraints.Min;

public class UpdateStockRequest {
    @Min(0)
    private int stock;

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}

