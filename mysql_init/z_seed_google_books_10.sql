USE product_db_flyway;

-- Google Books sample (10 records)
INSERT INTO books (id, title, author, image, price, category, stock, description, created_at, updated_at, sales_count)
VALUES
('GB-ZYTCALFPJGYC','The Google Story','David A. Vise','https://books.google.com/books/content?id=zyTCAlFPjgYC&printsec=frontcover&img=1&zoom=1',106000,'kinh-doanh',51,'Inside the growth of Google.',NOW(),NOW(),0),
('GB-UW2QDWAAQBAJ','Atomic Habits','James Clear','https://books.google.com/books/content?id=uW2qDwAAQBAJ&printsec=frontcover&img=1&zoom=1',113000,'tam-ly-ky-nang',52,'An easy proven way to build good habits.',NOW(),NOW(),0),
('GB-7HLYDWAAQBAJ','Sapiens','Yuval Noah Harari','https://books.google.com/books/content?id=7HlyDwAAQBAJ&printsec=frontcover&img=1&zoom=1',120000,'lich-su',53,'A brief history of humankind.',NOW(),NOW(),0),
('GB-FFCJDQAAQBAJ','Clean Code','Robert C. Martin','https://books.google.com/books/content?id=fFCjDQAAQBAJ&printsec=frontcover&img=1&zoom=1',127000,'cong-nghe',54,'A handbook of agile software craftsmanship.',NOW(),NOW(),0),
('GB-S1GVAAAAYAAJ','The Time Machine','H. G. Wells','https://books.google.com/books/content?id=s1gVAAAAYAAJ&printsec=frontcover&img=1&zoom=1',134000,'van-hoc',55,'Classic science fiction novel.',NOW(),NOW(),0),
('GB-QZ2XDWAAQBAJ','Thinking, Fast and Slow','Daniel Kahneman','https://books.google.com/books/content?id=qz2xDwAAQBAJ&printsec=frontcover&img=1&zoom=1',141000,'tam-ly-ky-nang',56,'Two systems that drive the way we think.',NOW(),NOW(),0),
('GB-NNQQDWAAQBAJ','Deep Work','Cal Newport','https://books.google.com/books/content?id=NnQqDwAAQBAJ&printsec=frontcover&img=1&zoom=1',148000,'tam-ly-ky-nang',57,'Rules for focused success in a distracted world.',NOW(),NOW(),0),
('GB-T6O0EAAAQBAJ','The Pragmatic Programmer','David Thomas, Andrew Hunt','https://books.google.com/books/content?id=t6o0EAAAQBAJ&printsec=frontcover&img=1&zoom=1',155000,'cong-nghe',58,'Your journey to mastery.',NOW(),NOW(),0),
('GB-XGGZEAAAQBAJ','Dune','Frank Herbert','https://books.google.com/books/content?id=xGgzEAAAQBAJ&printsec=frontcover&img=1&zoom=1',162000,'van-hoc',59,'Epic science fiction classic.',NOW(),NOW(),0),
('GB-U8E8DWAAQBAJ','Educated','Tara Westover','https://books.google.com/books/content?id=u8e8DwAAQBAJ&printsec=frontcover&img=1&zoom=1',169000,'van-hoc',60,'A memoir by Tara Westover.',NOW(),NOW(),0)
ON DUPLICATE KEY UPDATE
  title = VALUES(title),
  author = VALUES(author),
  image = VALUES(image),
  price = VALUES(price),
  category = VALUES(category),
  stock = VALUES(stock),
  description = VALUES(description),
  updated_at = VALUES(updated_at);

INSERT INTO book_search_index (book_id, title_no_accent, author_no_accent)
VALUES
('GB-ZYTCALFPJGYC','THE GOOGLE STORY','DAVID A  VISE'),
('GB-UW2QDWAAQBAJ','ATOMIC HABITS','JAMES CLEAR'),
('GB-7HLYDWAAQBAJ','SAPIENS','YUVAL NOAH HARARI'),
('GB-FFCJDQAAQBAJ','CLEAN CODE','ROBERT C  MARTIN'),
('GB-S1GVAAAAYAAJ','THE TIME MACHINE','H  G  WELLS'),
('GB-QZ2XDWAAQBAJ','THINKING  FAST AND SLOW','DANIEL KAHNEMAN'),
('GB-NNQQDWAAQBAJ','DEEP WORK','CAL NEWPORT'),
('GB-T6O0EAAAQBAJ','THE PRAGMATIC PROGRAMMER','DAVID THOMAS  ANDREW HUNT'),
('GB-XGGZEAAAQBAJ','DUNE','FRANK HERBERT'),
('GB-U8E8DWAAQBAJ','EDUCATED','TARA WESTOVER')
ON DUPLICATE KEY UPDATE
  title_no_accent = VALUES(title_no_accent),
  author_no_accent = VALUES(author_no_accent),
  updated_at = CURRENT_TIMESTAMP;
