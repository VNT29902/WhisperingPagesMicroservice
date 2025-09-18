package com.example.ProductService.Controller;

import com.example.ProductService.BookDto.*;
import com.example.ProductService.Response.PromotionCheckResponse;
import com.example.ProductService.Response.PromotionListResponse;
import com.example.ProductService.Response.PromotionSaveResponse;
import com.example.ProductService.Service.BookService;
import com.example.ProductService.Service.PromotionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/products")

public class BookController {

    private final BookService bookService;
    private final PromotionService promotionService;

    public BookController(BookService bookService, PromotionService promotionService) {
        this.bookService = bookService;
        this.promotionService = promotionService;
    }


    // ===== CRUD / Query =====

    @PostMapping
    public ResponseEntity<List<BookResponseDto>> createBooks(@Valid @RequestBody List<BookDto> bookDtos) {
        List<BookResponseDto> created = bookService.createBooks(bookDtos);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/upload-excel")
    public ResponseEntity<List<BookResponseDto>> uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            List<BookResponseDto> created = bookService.importFromExcel(file);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseAndPromotions> getBookById(@PathVariable String id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }


    @GetMapping("/search")
    public ResponseEntity<Page<BookResponseAndPromotions>> searchBooks(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<BookResponseAndPromotions> result = bookService.searchByKeyword(keyword, pageable);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/admin/search")
    public ResponseEntity<Page<BookResponseAndPromotions>> searchBooksAdminPage(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<BookResponseAndPromotions> result =
                bookService.searchBooksAdmin(keyword, category, minPrice, maxPrice, pageable);

        return ResponseEntity.ok(result);
    }


    @PatchMapping("/{id}/price")
    public ResponseEntity<BookResponseDto> updateBookPrice(@PathVariable String id, @Valid @RequestBody UpdatePriceRequest request) {
        return ResponseEntity.ok(bookService.updateBookPrice(id, request.getPrice()));
    }


    @PatchMapping("/{id}/stock")
    public ResponseEntity<BookResponseDto> updateBookStock(@PathVariable String id, @Valid @RequestBody UpdateStockRequest request) {
        return ResponseEntity.ok(bookService.updateBookStock(id, request.getStock()));
    }


    @GetMapping
    public ResponseEntity<Page<BookResponseAndPromotions>> getByCategory(
            @RequestParam String category,
            @RequestParam(defaultValue = "12") int limit,
            @RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, limit);
        Page<BookResponseAndPromotions> result = bookService.getBookCategory(category, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/best-selling")
    public ResponseEntity<List<BookResponseAndPromotions>> getBestSelling(@RequestParam(required = false) String from, @RequestParam(required = false) String to, @RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "0") int minSales) {

        return ResponseEntity.ok(bookService.getBestSelling(from, to, limit, minSales));
    }


    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDto> updateBook(@PathVariable String id, @Valid @RequestBody BookDto bookDto) {
        return ResponseEntity.ok(bookService.updateBook(id, bookDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable String id) {
        return bookService.deleteBook(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<BookResponseDto>> getBooks(@RequestParam(required = false) String category, @RequestParam(required = false) String author) {
        List<BookResponseDto> books = bookService.findBooksByFilters(category, author);
        return books.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build() : ResponseEntity.ok(books);
    }

    @GetMapping("/{id}/price")
    public ResponseEntity<BigDecimal> getPriceByBookId(@PathVariable String id) {
        BigDecimal price = bookService.getBookById(id).getBookResponseDto().getPrice();
        if (price == null) throw new IllegalStateException("❌ Price of book is not updated");
        return ResponseEntity.ok(price);
    }

    @GetMapping("/latest")
    public ResponseEntity<List<BookResponseAndPromotions>> getLatestBooks() {
        return ResponseEntity.ok(bookService.getLatestBooks());
    }


    /**
     * Trừ tồn thật (COD hoặc sau khi thanh toán online thành công)
     */
    @PostMapping("/{id}/confirm")
    public ResponseEntity<String> confirmStock(@PathVariable String id, @RequestParam int quantity) {
        try {
            bookService.confirmStock(id, quantity); // trước đây là placeOrder
            return ResponseEntity.ok("✅ Confirmed stock");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("❌ Error: " + e.getMessage());
        }
    }

    /**
     * Giữ hàng tạm thời cho thanh toán online (TTL tính bằng giây)
     */
    @PostMapping("/{id}/reserve")
    public ResponseEntity<String> reserveStock(@PathVariable String id, @RequestParam int quantity, @RequestParam int ttlSeconds) {
        try {
            bookService.reserveStock(id, quantity, ttlSeconds);
            return ResponseEntity.ok("✅ Reserved stock");
        } catch (Exception e) {
            // hết hàng hoặc tranh chấp
            return ResponseEntity.status(HttpStatus.CONFLICT).body("❌ Error: " + e.getMessage());
        }
    }

    /**
     * Trả lại hàng đã reserve (khi hủy/timeout)
     */
    @PutMapping("/{id}/release")
    public ResponseEntity<String> releaseStock(@PathVariable String id, @RequestParam int quantity) {
        try {
            bookService.releaseStock(id, quantity);
            return ResponseEntity.ok("✅ Stock released");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("❌ Error: " + e.getMessage());
        }
    }

    /**
     * (Tuỳ chọn) Kiểm tra tồn đủ không – nên dùng cho debug hoặc UI pre-check
     */
    @GetMapping("/{id}/checkStock")
    public ResponseEntity<String> checkStock(@PathVariable String id, @RequestParam int quantity) {
        try {
            bookService.checkStock(id, quantity);
            return ResponseEntity.ok("✅ Stock is enough");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("❌ Error: " + e.getMessage());
        }
    }

    @GetMapping("/promotions")
    public ResponseEntity<Page<PromotionListResponse>> getPromotions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Boolean active
    ) {
        return ResponseEntity.ok(promotionService.getPromotions(page, size, active));
    }

    @DeleteMapping("/promotions/{id}")
    public ResponseEntity<Void> deletePromotion(@PathVariable Long id) {
        promotionService.deletePromotionsById(id);

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/promotions/{id}")
    public ResponseEntity<PromotionSaveResponse> getPromotionById(@PathVariable Long id) {
        PromotionSaveResponse response = promotionService.getPromotionById(id);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }



    @PostMapping("/promotions/{id}")
    public ResponseEntity<PromotionSaveResponse> updatePromotion(@PathVariable Long id, @RequestBody PromotionUpdateRequest request) {
        PromotionSaveResponse response = promotionService.updatePromotion(id, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/promotions")
    public ResponseEntity<PromotionSaveResponse> createPromotion(@RequestBody PromotionSaveRequest request) {
        PromotionSaveResponse response = promotionService.savePromotion(request);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/promotions/check")
    public ResponseEntity<PromotionCheckResponse> checkPromotion(@RequestBody PromotionCheckRequest request) {
        return ResponseEntity.ok(promotionService.getBestPromotionForProduct(request));
    }

    @PostMapping("promotions/{id}/status")
    public ResponseEntity<String> updatePromotionStatus(@PathVariable Long id, @RequestBody PromotionStatusUpdateRequest request) {

        promotionService.updatePromotionStatus(id, request.getIsActive());
        String msg = "Promotion " + id + " has been " + (request.getIsActive() ? "activated" : "deactivated");
        return ResponseEntity.ok(msg);
    }


}
