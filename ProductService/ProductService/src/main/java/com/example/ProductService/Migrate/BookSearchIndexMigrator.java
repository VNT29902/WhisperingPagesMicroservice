package com.example.ProductService.Migrate;

import com.example.ProductService.Entity.Book;
import com.example.ProductService.Entity.BookSearchIndex;
import com.example.ProductService.Repository.BookRepository;
import com.example.ProductService.Repository.BookSearchIndexRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class BookSearchIndexMigrator implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final BookSearchIndexRepository searchIndexRepository;

    public BookSearchIndexMigrator(BookRepository bookRepository, BookSearchIndexRepository searchIndexRepository) {
        this.bookRepository = bookRepository;
        this.searchIndexRepository = searchIndexRepository;
    }

    @Override
    public void run(String... args) {
        List<Book> books = bookRepository.findAll();
        for (Book book : books) {
            boolean exists = searchIndexRepository.existsByBookId(book.getId());
            if (!exists) {
                BookSearchIndex index = new BookSearchIndex();
                index.setBookId(book.getId());
                index.setTitleNoAccent(removeAccents(book.getTitle()));
                index.setAuthorNoAccent(removeAccents(book.getAuthor()));
                index.setUpdatedAt(LocalDateTime.now());
                searchIndexRepository.save(index);
            }
        }
    }

    public String removeAccents(String input) {
        if (input == null) return null;
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
    }
}
