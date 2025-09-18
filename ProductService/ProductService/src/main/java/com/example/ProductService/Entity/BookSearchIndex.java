package com.example.ProductService.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_search_index")
public class BookSearchIndex {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_id", nullable = false, length = 20)
    private String bookId;

    @Column(name = "title_no_accent", nullable = false, length = 255)
    private String titleNoAccent;

    @Column(name = "author_no_accent", nullable = false, length = 255)
    private String authorNoAccent;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // ✅ Optional: liên kết với Book (nếu muốn join nhanh)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", insertable = false, updatable = false)
    private Book book;

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getTitleNoAccent() {
        return titleNoAccent;
    }

    public void setTitleNoAccent(String titleNoAccent) {
        this.titleNoAccent = titleNoAccent;
    }

    public String getAuthorNoAccent() {
        return authorNoAccent;
    }

    public void setAuthorNoAccent(String authorNoAccent) {
        this.authorNoAccent = authorNoAccent;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
