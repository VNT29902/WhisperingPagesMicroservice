package com.example.ProductService.BookDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Bag;

import java.math.BigDecimal;

public class BookDto {
    @NotBlank
    private String title;

    @NotBlank
    private String author;

    @NotNull
    private BigDecimal price;

    @NotBlank
    private String category;

    @NotBlank
    private String image;

    @Min(0)
    private int saleStock;

    @Min(0)
    private int stock;

    private String description;

    public BookDto() {
    }

    public int getSaleStock() {
        return saleStock;
    }

    public void setSaleStock(int saleStock) {
        this.saleStock = saleStock;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
