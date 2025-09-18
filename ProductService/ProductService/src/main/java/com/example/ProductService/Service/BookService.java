package com.example.ProductService.Service;

import com.example.ProductService.BookDto.BookDto;
import com.example.ProductService.BookDto.BookResponseAndPromotions;
import com.example.ProductService.BookDto.PromotionCheckRequest;
import com.example.ProductService.Entity.BookSalesStat;
import com.example.ProductService.Entity.BookSearchIndex;
import com.example.ProductService.Mapper.BookMapper;
import com.example.ProductService.BookDto.BookResponseDto;
import com.example.ProductService.Entity.Book;
import com.example.ProductService.GenKeyBookId.CodeGenerator;
import com.example.ProductService.Migrate.BookSearchIndexMigrator;
import com.example.ProductService.Repository.BookRepository;
import com.example.ProductService.Repository.BookSalesStatRepository;
import com.example.ProductService.Repository.BookSearchIndexRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class BookService {


    private final BookRepository bookRepository;

    private final BookSalesStatRepository bookSalesStatRepository;

    private final PromotionService promotionService;

    private final BookSearchIndexRepository bookSearchIndexRepository;

    private final BookSearchIndexMigrator bookSearchIndexMigrator;


    public BookService(BookRepository bookRepository, BookSalesStatRepository bookSalesStatRepository, PromotionService promotionService, BookSearchIndexRepository bookSearchIndexRepository, BookSearchIndexMigrator bookSearchIndexMigrator) {
        this.bookRepository = bookRepository;
        this.bookSalesStatRepository = bookSalesStatRepository;

        this.promotionService = promotionService;
        this.bookSearchIndexRepository = bookSearchIndexRepository;
        this.bookSearchIndexMigrator = bookSearchIndexMigrator;
    }


    public List<BookResponseDto> createBooks(List<BookDto> dtoList) {

        if (dtoList == null || dtoList.isEmpty()) {
            throw new IllegalArgumentException("❌ Danh sách sách gửi lên đang trống.");
        }

        List<Book> booksToSave = new ArrayList<>();

        for (BookDto dto : dtoList) {
            boolean exists = bookRepository.existsByTitleAndAuthorAndCategory(dto.getTitle(), dto.getAuthor(), dto.getCategory());

            if (exists) {
                throw new IllegalArgumentException("📚 Sách '" + dto.getTitle() + "' đã tồn tại trong hệ thống!");
            }

            String code;
            do {
                code = CodeGenerator.generateCode("BK-");
            } while (bookRepository.existsById(code));

            Book book = BookMapper.toEntity(dto);
            book.setId(code);
            book.setSalesCount(0);
            book.setCreatedAt(LocalDateTime.now());
            book.setUpdatedAt(LocalDateTime.now());
            booksToSave.add(book);
        }

        List<Book> savedBooks = bookRepository.saveAll(booksToSave);

        for (Book book : savedBooks) {
            BookSearchIndex index = new BookSearchIndex();
            index.setBookId(book.getId());
            index.setTitleNoAccent(bookSearchIndexMigrator.removeAccents(book.getTitle()));
            index.setAuthorNoAccent(bookSearchIndexMigrator.removeAccents(book.getAuthor()));
            bookSearchIndexRepository.save(index);
        }

        return BookMapper.toDtoList(savedBooks);

    }

    public List<BookResponseDto> importFromExcel(MultipartFile file) throws IOException {
        List<BookDto> dtoList = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            // Bỏ dòng header (i=0), đọc từ i=1
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                BookDto dto = new BookDto();
                dto.setTitle(row.getCell(0).getStringCellValue());
                dto.setAuthor(row.getCell(1).getStringCellValue());
                dto.setPrice(BigDecimal.valueOf(row.getCell(2).getNumericCellValue()));
                dto.setCategory(row.getCell(3).getStringCellValue());
                dto.setImage(row.getCell(4).getStringCellValue());
                dto.setSaleStock((int) row.getCell(5).getNumericCellValue());
                dto.setStock((int) row.getCell(6).getNumericCellValue());
                dto.setDescription(row.getCell(7).getStringCellValue());

                dtoList.add(dto);
            }
        }

        // 🟢 Tái sử dụng createBooks cho toàn bộ list DTO đọc được
        return createBooks(dtoList);
    }


    public Page<BookResponseAndPromotions> searchByKeyword(String keyword, Pageable pageable) {
        String normalizedKeyword = bookSearchIndexMigrator.removeAccents(keyword).toLowerCase();

        // Repository trả về Page<String>
        Page<String> bookIdsPage = bookSearchIndexRepository.searchBookIds(normalizedKeyword, pageable);

        List<Book> books = bookRepository.findAllById(bookIdsPage.getContent());

        List<BookResponseAndPromotions> content = books.stream().map(book -> {
            BookResponseDto dto = BookMapper.toDto(book);

            var promotionBook = promotionService.getBestPromotionForProduct(new PromotionCheckRequest(book.getId(), book.getCategory()));

            BigDecimal promo = promotionBook != null ? promotionBook.getDiscountPercent() : BigDecimal.ZERO;

            return new BookResponseAndPromotions(promo, dto);
        }).toList();

        return new PageImpl<>(content, pageable, bookIdsPage.getTotalElements());
    }

    public Page<BookResponseAndPromotions> searchBooksAdmin(String keyword, String category, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {

        Page<Book> booksPage = bookRepository.searchBooksAdmin(keyword, category, minPrice, maxPrice, pageable);

        List<BookResponseAndPromotions> content = booksPage.getContent().stream().map(book -> {
            BookResponseDto dto = BookMapper.toDto(book);

            var promotionBook = promotionService.getBestPromotionForProduct(new PromotionCheckRequest(book.getId(), book.getCategory()));

            BigDecimal promo = promotionBook != null ? promotionBook.getDiscountPercent() : BigDecimal.ZERO;

            return new BookResponseAndPromotions(promo, dto);
        }).toList();

        return new PageImpl<>(content, pageable, booksPage.getTotalElements());
    }


    public BookResponseAndPromotions getBookById(String id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "❌ Book not found"));
        BookResponseDto bookResponseDto = BookMapper.toDto(book);
        var promotionBook = promotionService.getBestPromotionForProduct(new PromotionCheckRequest(book.getId(), book.getCategory()));
        return new BookResponseAndPromotions(promotionBook.getDiscountPercent(), bookResponseDto);

    }


    public Page<BookResponseAndPromotions> getBookCategory(String category, Pageable pageable) {
        Page<Book> page;

        if ("all".equalsIgnoreCase(category)) {
            page = bookRepository.findAll(pageable);
        } else {
            if (!bookRepository.existsByCategory(category)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "❌ Book not found");
            }
            page = bookRepository.findByCategory(category, pageable);
        }

        // ✅ Dùng map() có sẵn của Page<T> thay vì stream().toList()
        return page.map(book -> {
            var promo = promotionService.getBestPromotionForProduct(new PromotionCheckRequest(book.getId(), book.getCategory()));
            return new BookResponseAndPromotions(promo.getDiscountPercent(), BookMapper.toDto(book));
        });
    }

    public List<BookResponseAndPromotions> getBestSelling(String from, String to, int limit, int minSales) {
        // parse input thời gian
        LocalDate fromDate = (from != null) ? LocalDate.parse(from) : LocalDate.now().withDayOfMonth(1);
        LocalDate toDate = (to != null) ? LocalDate.parse(to) : LocalDate.now();

        int fromYear = fromDate.getYear();
        int fromMonth = fromDate.getMonthValue();
        int toYear = toDate.getYear();
        int toMonth = toDate.getMonthValue();

        // query thẳng ra list<Book>
        List<Book> books = bookSalesStatRepository.findBestSellingBooksByMonthRangeAsBook(
                fromYear, fromMonth, toYear, toMonth, minSales, PageRequest.of(0, limit));

        return books.stream()
                .map(book -> {
                    var promo = promotionService.getBestPromotionForProduct(
                            new PromotionCheckRequest(book.getId(), book.getCategory()));
                    BigDecimal discount = promo != null ? promo.getDiscountPercent() : BigDecimal.ZERO;
                    return new BookResponseAndPromotions(discount, BookMapper.toDto(book));
                })
                .toList();
    }



    // update

    public BookResponseDto updateBook(String id, BookDto dto) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "❌ Book ID not found"));

        book.setTitle(dto.getTitle());
        book.setSalesCount(dto.getSaleStock());
        book.setAuthor(dto.getAuthor());
        book.setPrice(dto.getPrice());
        book.setCategory(dto.getCategory());
        book.setImage(dto.getImage());
        book.setStock(dto.getStock());
        book.setDescription(dto.getDescription());
        book.setUpdatedAt(LocalDateTime.now());

        return BookMapper.toDto(bookRepository.save(book));
    }

    public BookResponseDto updateBookPrice(String id, BigDecimal price) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "❌ Book ID not found"));

        book.setPrice(price);
        book.setUpdatedAt(LocalDateTime.now());

        return BookMapper.toDto(bookRepository.save(book));
    }

    public BookResponseDto updateBookStock(String id, int stock) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "❌ Book ID not found"));

        book.setStock(stock);
        book.setUpdatedAt(LocalDateTime.now());

        return BookMapper.toDto(bookRepository.save(book));
    }


    public boolean deleteBook(String id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    public void placeOrder(String productId, int quantity) {
        Book book = bookRepository.findByIdForUpdate(productId).orElseThrow(() -> new IllegalArgumentException("❌ Book does not exist"));

        if (book.getStock() < quantity) {
            throw new IllegalArgumentException("❌ Not enough stock available");
        }

        // Giảm tồn kho
        book.setStock(book.getStock() - quantity);

        // Tăng số lượng bán ra
        int currentSales = book.getSalesCount(); // không cần check null

        book.setSalesCount(currentSales + quantity);

        book.setUpdatedAt(LocalDateTime.now());
        bookRepository.save(book);
    }


    @Transactional
    public void releaseStock(String productId, int quantity) {
        Book book = bookRepository.findByIdForUpdate(productId).orElseThrow(() -> new RuntimeException("❌ Book not found"));

        book.setStock(book.getStock() + quantity);
        book.setUpdatedAt(LocalDateTime.now());
        bookRepository.save(book);
    }

    @Transactional
    public void checkStock(String productId, int quantity) {
        Book book = bookRepository.findByIdForUpdate(productId).orElseThrow(() -> new IllegalArgumentException("❌ Book does not exist"));

        if (book.getStock() < quantity) {
            throw new IllegalArgumentException("❌ Not enough stock available");
        }


    }

    public List<BookResponseDto> findBooksByFilters(String category, String author) {
        List<Book> books;

        if (category != null && author != null) {
            books = bookRepository.findByCategoryAndAuthor(category, author);
        } else if (category != null) {
            books = bookRepository.findByCategory(category);
        } else if (author != null) {
            books = bookRepository.findByAuthor(author);
        } else {
            books = bookRepository.findAll();
        }

        return books.stream().map(BookMapper::toDto).toList();
    }

    public List<BookResponseAndPromotions> getLatestBooks() {
        List<Book> books = bookRepository.findTop10ByOrderByCreatedAtDesc();

        return books.stream().map(book -> {
            var promo = promotionService.getBestPromotionForProduct(new PromotionCheckRequest(book.getId(), book.getCategory()));
            BigDecimal discount = promo != null ? promo.getDiscountPercent() : BigDecimal.ZERO;

            return new BookResponseAndPromotions(discount, BookMapper.toDto(book));
        }).toList();
    }


    @Transactional
    public void confirmStock(String bookId, int quantity) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "❌ Book not found"));

        if (book.getStock() < quantity) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "⚠️ Not enough stock");
        }

        // 🟢 Giảm tồn kho
        book.setStock(book.getStock() - quantity);

        // 🟢 Cộng dồn all-time salesCount
        book.setSalesCount(book.getSalesCount() + quantity);

        book.setUpdatedAt(LocalDateTime.now());
        bookRepository.save(book);

        // 🟢 Cập nhật thống kê theo tuần & tháng
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int week = now.get(java.time.temporal.WeekFields.ISO.weekOfWeekBasedYear());

        // Lấy hoặc tạo mới record trong book_sales_stats
        BookSalesStat stat = bookSalesStatRepository.findByBookIdAndYearAndMonthAndWeek(bookId, year, month, week).orElseGet(() -> {
            Book bookById = bookRepository.findById(bookId).orElseThrow();
            BookSalesStat s = new BookSalesStat();
            s.setBook(bookById);
            s.setYear(year);
            s.setMonth(month);
            s.setWeek(week);
            s.setSalesCount(0);
            return s;
        });

        stat.setSalesCount(stat.getSalesCount() + quantity);
        stat.setUpdatedAt(LocalDateTime.now());

        bookSalesStatRepository.save(stat);
    }


    /**
     * Giữ hàng tạm thời trong Redis với TTL (giây).
     * Khi hết TTL, một job hoặc Redis key expiry listener sẽ trả lại hàng.
     */
    @Transactional
    public void reserveStock(String bookId, int quantity, int ttlSeconds) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Book not found"));

        if (book.getStock() < quantity) {
            throw new ResponseStatusException(CONFLICT, "Not enough stock to reserve");
        }

        int newStock = book.getStock() - quantity;

        book.setStock(newStock);
        bookRepository.save(book);


    }


}

