USE product_db_flyway;

-- Fahasa-style categorized seed data for local development/demo.
-- Compatible with current ProductService schema.

INSERT INTO books (id, title, author, image, price, category, stock, description, created_at, updated_at, sales_count)
VALUES
('FH-VH-0001','Nhà Giả Kim','Paulo Coelho','https://dummyimage.com/400x600/e2e8f0/1e293b&text=nha-gia-kim',89000,'van-hoc',120,'Tiểu thuyết truyền cảm hứng nổi tiếng toàn cầu.',NOW(),NOW(),0),
('FH-VH-0002','Tuổi Trẻ Đáng Giá Bao Nhiêu','Rosie Nguyễn','https://dummyimage.com/400x600/e2e8f0/1e293b&text=tuoi-tre-dang-gia-bao-nhieu',105000,'van-hoc',140,'Sách truyền cảm hứng cho người trẻ về học tập và trải nghiệm.',NOW(),NOW(),0),
('FH-VH-0003','Đời Ngắn Đừng Ngủ Dài','Robin Sharma','https://dummyimage.com/400x600/e2e8f0/1e293b&text=doi-ngan-dung-ngu-dai',98000,'van-hoc',95,'Những ghi chép giúp thay đổi tư duy và hành động.',NOW(),NOW(),0),

('FH-KD-0001','Dám Bị Ghét','Ichiro Kishimi','https://dummyimage.com/400x600/e2e8f0/1e293b&text=dam-bi-ghet',128000,'kinh-doanh',100,'Tư duy tâm lý ứng dụng để sống tự do và hạnh phúc.',NOW(),NOW(),0),
('FH-KD-0002','Cha Giàu Cha Nghèo','Robert T. Kiyosaki','https://dummyimage.com/400x600/e2e8f0/1e293b&text=cha-giau-cha-ngheo',119000,'kinh-doanh',170,'Kiến thức tài chính cá nhân kinh điển.',NOW(),NOW(),0),
('FH-KD-0003','Từ Tốt Đến Vĩ Đại','Jim Collins','https://dummyimage.com/400x600/e2e8f0/1e293b&text=tu-tot-den-vi-dai',159000,'kinh-doanh',88,'Nghiên cứu về doanh nghiệp phát triển bền vững.',NOW(),NOW(),0),
('FH-KD-0004','Zero To One','Peter Thiel','https://dummyimage.com/400x600/e2e8f0/1e293b&text=zero-to-one',149000,'kinh-doanh',76,'Góc nhìn xây dựng sản phẩm đột phá và startup.',NOW(),NOW(),0),

('FH-TN-0001','Dế Mèn Phiêu Lưu Ký','Tô Hoài','https://dummyimage.com/400x600/e2e8f0/1e293b&text=de-men-phieu-luu-ky',69000,'thieu-nhi',200,'Tác phẩm thiếu nhi kinh điển của văn học Việt Nam.',NOW(),NOW(),0),
('FH-TN-0002','Harry Potter Và Hòn Đá Phù Thủy','J.K. Rowling','https://dummyimage.com/400x600/e2e8f0/1e293b&text=harry-potter-1',179000,'thieu-nhi',130,'Khởi đầu hành trình phép thuật hấp dẫn.',NOW(),NOW(),0),
('FH-TN-0003','Không Gia Đình','Hector Malot','https://dummyimage.com/400x600/e2e8f0/1e293b&text=khong-gia-dinh',99000,'thieu-nhi',110,'Câu chuyện nhân văn về nghị lực và tình người.',NOW(),NOW(),0),

('FH-MG-0001','One Piece Tập 1','Eiichiro Oda','https://dummyimage.com/400x600/e2e8f0/1e293b&text=one-piece-1',25000,'manga-comic',220,'Khởi đầu cuộc phiêu lưu tìm kho báu One Piece.',NOW(),NOW(),0),
('FH-MG-0002','Doraemon Tập 1','Fujiko F. Fujio','https://dummyimage.com/400x600/e2e8f0/1e293b&text=doraemon-1',22000,'manga-comic',260,'Mèo máy Doraemon và những bảo bối kỳ diệu.',NOW(),NOW(),0),
('FH-MG-0003','Conan Tập 1','Gosho Aoyama','https://dummyimage.com/400x600/e2e8f0/1e293b&text=conan-1',28000,'manga-comic',210,'Thám tử lừng danh Conan mở đầu các vụ án hấp dẫn.',NOW(),NOW(),0),

('FH-KN-0001','Đắc Nhân Tâm','Dale Carnegie','https://dummyimage.com/400x600/e2e8f0/1e293b&text=dac-nhan-tam',130000,'tam-ly-ky-nang',180,'Nghệ thuật giao tiếp và ứng xử kinh điển.',NOW(),NOW(),0),
('FH-KN-0002','Atomic Habits','James Clear','https://dummyimage.com/400x600/e2e8f0/1e293b&text=atomic-habits',189000,'tam-ly-ky-nang',160,'Phương pháp xây dựng thói quen bền vững.',NOW(),NOW(),0),
('FH-KN-0003','Tư Duy Nhanh Và Chậm','Daniel Kahneman','https://dummyimage.com/400x600/e2e8f0/1e293b&text=tu-duy-nhanh-va-cham',199000,'tam-ly-ky-nang',92,'Khoa học hành vi và tư duy ra quyết định.',NOW(),NOW(),0),

('FH-NN-0001','English Grammar In Use','Raymond Murphy','https://dummyimage.com/400x600/e2e8f0/1e293b&text=english-grammar-in-use',220000,'ngoai-ngu',70,'Ngữ pháp tiếng Anh tự học phổ biến.',NOW(),NOW(),0),
('FH-NN-0002','5500 Từ Vựng Tiếng Anh','The Windy','https://dummyimage.com/400x600/e2e8f0/1e293b&text=5500-tu-vung-tieng-anh',145000,'ngoai-ngu',95,'Bộ từ vựng thông dụng cho giao tiếp và thi cử.',NOW(),NOW(),0),

('FH-LS-0001','Sapiens: Lược Sử Loài Người','Yuval Noah Harari','https://dummyimage.com/400x600/e2e8f0/1e293b&text=sapiens',219000,'lich-su',80,'Lịch sử phát triển của loài người từ quá khứ đến hiện tại.',NOW(),NOW(),0),
('FH-LS-0002','Lược Sử Thời Gian','Stephen Hawking','https://dummyimage.com/400x600/e2e8f0/1e293b&text=luoc-su-thoi-gian',175000,'khoa-hoc',78,'Nhập môn vũ trụ học dành cho độc giả phổ thông.',NOW(),NOW(),0),
('FH-LS-0003','Vũ Trụ Trong Vỏ Hạt Dẻ','Stephen Hawking','https://dummyimage.com/400x600/e2e8f0/1e293b&text=vu-tru-trong-vo-hat-de',169000,'khoa-hoc',65,'Các khái niệm vật lý hiện đại được giải thích dễ hiểu.',NOW(),NOW(),0),

('FH-CN-0001','Clean Code','Robert C. Martin','https://dummyimage.com/400x600/e2e8f0/1e293b&text=clean-code',259000,'cong-nghe',90,'Nguyên tắc viết code sạch và dễ bảo trì.',NOW(),NOW(),0),
('FH-CN-0002','Designing Data-Intensive Applications','Martin Kleppmann','https://dummyimage.com/400x600/e2e8f0/1e293b&text=ddia',399000,'cong-nghe',55,'Kiến trúc hệ thống dữ liệu hiện đại.',NOW(),NOW(),0),
('FH-CN-0003','Grokking Algorithms','Aditya Bhargava','https://dummyimage.com/400x600/e2e8f0/1e293b&text=grokking-algorithms',235000,'cong-nghe',73,'Minh họa thuật toán trực quan, dễ tiếp cận.' ,NOW(),NOW(),0)
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
('FH-VH-0001','NHA GIA KIM','PAULO COELHO'),
('FH-VH-0002','TUOI TRE DANG GIA BAO NHIEU','ROSIE NGUYEN'),
('FH-VH-0003','DOI NGAN DUNG NGU DAI','ROBIN SHARMA'),
('FH-KD-0001','DAM BI GHET','ICHIRO KISHIMI'),
('FH-KD-0002','CHA GIAU CHA NGHEO','ROBERT T KIYOSAKI'),
('FH-KD-0003','TU TOT DEN VI DAI','JIM COLLINS'),
('FH-KD-0004','ZERO TO ONE','PETER THIEL'),
('FH-TN-0001','DE MEN PHIEU LUU KY','TO HOAI'),
('FH-TN-0002','HARRY POTTER VA HON DA PHU THUY','JK ROWLING'),
('FH-TN-0003','KHONG GIA DINH','HECTOR MALOT'),
('FH-MG-0001','ONE PIECE TAP 1','EIICHIRO ODA'),
('FH-MG-0002','DORAEMON TAP 1','FUJIKO F FUJIO'),
('FH-MG-0003','CONAN TAP 1','GOSHO AOYAMA'),
('FH-KN-0001','DAC NHAN TAM','DALE CARNEGIE'),
('FH-KN-0002','ATOMIC HABITS','JAMES CLEAR'),
('FH-KN-0003','TU DUY NHANH VA CHAM','DANIEL KAHNEMAN'),
('FH-NN-0001','ENGLISH GRAMMAR IN USE','RAYMOND MURPHY'),
('FH-NN-0002','5500 TU VUNG TIENG ANH','THE WINDY'),
('FH-LS-0001','SAPIENS LUOC SU LOAI NGUOI','YUVAL NOAH HARARI'),
('FH-LS-0002','LUOC SU THOI GIAN','STEPHEN HAWKING'),
('FH-LS-0003','VU TRU TRONG VO HAT DE','STEPHEN HAWKING'),
('FH-CN-0001','CLEAN CODE','ROBERT C MARTIN'),
('FH-CN-0002','DESIGNING DATA INTENSIVE APPLICATIONS','MARTIN KLEPPMANN'),
('FH-CN-0003','GROKKING ALGORITHMS','ADITYA BHARGAVA')
ON DUPLICATE KEY UPDATE
  title_no_accent = VALUES(title_no_accent),
  author_no_accent = VALUES(author_no_accent),
  updated_at = CURRENT_TIMESTAMP;
