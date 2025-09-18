package com.example.ProductService.Mapper;

import com.example.ProductService.BookDto.BookDto;
import com.example.ProductService.BookDto.BookResponseDto;
import com.example.ProductService.Entity.Book;

import java.util.List;

public class BookMapper {

    public static Book toEntity(BookDto dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setPrice(dto.getPrice());
        book.setCategory(dto.getCategory());
        book.setImage(dto.getImage());
        book.setStock(dto.getStock());
        book.setDescription(dto.getDescription());
        return book;
    }

    public static BookResponseDto toDto(Book book) {
        BookResponseDto dto = new BookResponseDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setSaleStock(book.getSalesCount());
        dto.setPrice(book.getPrice());
        dto.setCategory(book.getCategory());
        dto.setImage(book.getImage());
        dto.setStock(book.getStock());
        dto.setDescription(book.getDescription());
        dto.setCreatedAt(book.getCreatedAt());
        dto.setUpdatedAt(book.getUpdatedAt());
        return dto;
    }



    public static List<Book> toEntityList(List<BookDto> dtoList) {
        return dtoList.stream()
                .map(BookMapper::toEntity)
                .toList();
    }

    public static List<BookResponseDto> toDtoList(List<Book> bookList) {
        return bookList.stream()
                .map(BookMapper::toDto)
                .toList();
    }

}
