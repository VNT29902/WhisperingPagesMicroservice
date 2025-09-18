package com.example.ProductService.BookDto;

import java.math.BigDecimal;

public class BookResponseAndPromotions {

    private BigDecimal promotionPercent;

    private BookResponseDto bookResponseDto;


    public BookResponseAndPromotions(BigDecimal promotionPercent, BookResponseDto bookResponseDto) {
        this.promotionPercent = promotionPercent;
        this.bookResponseDto = bookResponseDto;
    }

    public BigDecimal getPromotionPercent() {
        return promotionPercent;
    }

    public void setPromotionPercent(BigDecimal promotionPercent) {
        this.promotionPercent = promotionPercent;
    }

    public BookResponseDto getBookResponseDto() {
        return bookResponseDto;
    }

    public void setBookResponseDto(BookResponseDto bookResponseDto) {
        this.bookResponseDto = bookResponseDto;
    }
}
