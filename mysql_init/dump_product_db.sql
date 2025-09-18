CREATE DATABASE  IF NOT EXISTS `product_db_flyway` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `product_db_flyway`;
-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: product_db_flyway
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `book_sales_stats`
--

DROP TABLE IF EXISTS `book_sales_stats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book_sales_stats` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `book_id` varchar(20) NOT NULL,
  `year` int NOT NULL,
  `month` int NOT NULL,
  `week` int NOT NULL,
  `sales_count` int NOT NULL DEFAULT '0',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_book_sales_book` (`book_id`),
  CONSTRAINT `fk_book_sales_book` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book_sales_stats`
--

LOCK TABLES `book_sales_stats` WRITE;
/*!40000 ALTER TABLE `book_sales_stats` DISABLE KEYS */;
INSERT INTO `book_sales_stats` VALUES (1,'BK-AT4F84',2025,9,36,2,'2025-09-07 00:36:20'),(2,'BK-AX5K8C',2025,9,36,2,'2025-09-07 06:16:22'),(3,'BK-B3QVR5',2025,9,36,3,'2025-09-07 06:16:22'),(4,'BK-CVNAWN',2025,9,36,1,'2025-09-07 00:20:26'),(5,'BK-E4J8KA',2025,9,36,1,'2025-09-07 00:36:20'),(6,'BK-CDDQ57',2025,9,36,1,'2025-09-07 00:36:20'),(27,'BK-78RJPJ',2024,1,3,120,'2025-09-07 08:36:17'),(28,'BK-78RJPJ',2024,2,6,80,'2025-09-07 08:36:17'),(29,'BK-AT4F84',2024,3,10,95,'2025-09-07 08:36:17'),(30,'BK-AT4F84',2024,4,14,135,'2025-09-07 08:36:17'),(31,'BK-AX5K8C',2024,5,20,210,'2025-09-07 08:36:17'),(32,'BK-AX5K8C',2023,12,50,175,'2025-09-07 08:36:17'),(33,'BK-B2LN66',2024,6,24,142,'2025-09-07 08:36:17'),(34,'BK-B2LN66',2024,7,28,98,'2025-09-07 08:36:17'),(35,'BK-B3QVR5',2024,8,33,310,'2025-09-07 08:36:17'),(36,'BK-B3QVR5',2023,11,46,188,'2025-09-07 08:36:17'),(37,'BK-CDDQ57',2024,9,37,200,'2025-09-07 08:36:17'),(38,'BK-CDDQ57',2024,10,40,165,'2025-09-07 08:36:17'),(39,'BK-CVNAWN',2024,11,45,92,'2025-09-07 08:36:17'),(40,'BK-CVNAWN',2024,12,49,130,'2025-09-07 08:36:17'),(41,'BK-E4J8KA',2023,10,42,155,'2025-09-07 08:36:17'),(42,'BK-E4J8KA',2024,1,4,260,'2025-09-07 08:36:17'),(43,'BK-AT4F84',2025,9,37,1,'2025-09-09 19:15:13'),(44,'BK-AX5K8C',2025,9,37,1,'2025-09-09 19:16:35'),(45,'BK-CVNAWN',2025,9,37,1,'2025-09-09 19:16:35');
/*!40000 ALTER TABLE `book_sales_stats` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `book_search_index`
--

DROP TABLE IF EXISTS `book_search_index`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book_search_index` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `book_id` varchar(20) NOT NULL,
  `title_no_accent` varchar(255) NOT NULL,
  `author_no_accent` varchar(255) NOT NULL,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_book` (`book_id`),
  KEY `idx_book_search_title` (`title_no_accent`),
  KEY `idx_book_search_author` (`author_no_accent`),
  CONSTRAINT `fk_book` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book_search_index`
--

LOCK TABLES `book_search_index` WRITE;
/*!40000 ALTER TABLE `book_search_index` DISABLE KEYS */;
INSERT INTO `book_search_index` VALUES (1,'BK-WZAQNZ','TRIET HOC VE SU HAM MUON','frederic lenoir',NULL),(2,'BK-CDDQ57','CON ĐUONG TINH THUC','ha vinh tho',NULL),(3,'BK-S7B6G2','SU AN UI CUA TRIET HOC','alain de botton',NULL),(4,'BK-G3TDHF','CHINH PHUC HANH PHUC','bertrand russell',NULL),(5,'BK-KW85H7','LUOC SU HOA KY','james west',NULL),(6,'BK-CVNAWN','LUOC SU TON GIAO','richard holloway',NULL),(7,'BK-78RJPJ','VAN MINH VIET NAM','nguyen van huyen',NULL),(8,'BK-AT4F84','DANH NHAN VIET NAM','doan ke thien',NULL),(9,'BK-ERUMR9','Đac Nhan Tam','Dale Carnegie',NULL),(10,'BK-B2LN66','Tu Tot Đen Vi Đai','Jim Collins',NULL),(11,'BK-N6PTRN','BA CHU AI','parmy olson',NULL),(12,'BK-F9AH35','DOANH NGHIEP CONG HIEN','philip kotler',NULL),(13,'BK-E4J8KA','Phong cach NVIDIA (The NVIDIA Way)','tae kim',NULL),(14,'BK-E5SA59','THIET KE HE THONG HOC MAY','huyen chip',NULL),(15,'BK-HUNB6D','LAM SAO ĐE…?','randall munroe',NULL),(16,'BK-AX5K8C','KHOA HOC VE LOI SONG','stuart farrimond',NULL),(17,'BK-MM6GWS','HUONG DAN VIET VE PHIM','timothy corrigan',NULL),(18,'BK-B3QVR5','CAU CHUYEN NGHE THUAT','Tenzin Gyatso – Dalai Lama',NULL),(19,'BK-NX8M82','NGHE THUAT HUE','Eckhart Tolle',NULL),(20,'BK-QHSUHC','BAO TANG KHAI ĐINH','Tuyen tap co đo Hue',NULL);
/*!40000 ALTER TABLE `book_search_index` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `books`
--

DROP TABLE IF EXISTS `books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `books` (
  `id` varchar(20) NOT NULL,
  `title` varchar(255) NOT NULL,
  `author` varchar(255) NOT NULL,
  `image` varchar(255) NOT NULL,
  `price` decimal(15,2) NOT NULL,
  `category` varchar(100) NOT NULL,
  `stock` int NOT NULL,
  `description` text,
  `created_at` timestamp NOT NULL,
  `updated_at` timestamp NOT NULL,
  `sales_count` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books`
--

LOCK TABLES `books` WRITE;
/*!40000 ALTER TABLE `books` DISABLE KEYS */;
INSERT INTO `books` VALUES ('BK-78RJPJ','VĂN MINH VIỆT NAM','nguyễn văn huyên','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/van-minh-viet-nam-01.jpg?v=1699961156847',191000.00,'lich-su',10,'Cuốn sách này có một đời sống riêng đặc biệt. Bản thảo của nó được hoàn thành năm 1939 bằng tiếng Pháp với tựa đề La civilisation annamite. Nhưng phải chờ hơn 5 năm sau, năm 1944, nó mới được xuất bản tại Hà Nội. Đó là 5 năm kiểm duyệt gắt gao của chính quyền thuộc địa, và cũng là 5 năm kháng cự bền bỉ của tác giả để bảo vệ cho quan điểm của mình. Và có những trang bản thảo đã không được xuất bản.','2025-09-06 23:10:22','2025-09-06 23:10:22',0),('BK-AT4F84','DANH NHÂN VIỆT NAM','doãn kế thiện','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/danhnhane1699525531491.jpg?v=1705552512687',230000.00,'lich-su',9,'Lịch sử Việt Nam trải hàng nghìn năm đã sản sinh nên biết bao danh nhân lỗi lạc. Họ là những danh nhân về học vấn, chính trị, quân sự hoặc kỹ thuật,… nhưng tất cả đều kế tiếp nhau gom tài góp sức làm cho tổ quốc ngày một vẻ vang.','2025-09-06 23:10:22','2025-09-09 19:15:13',3),('BK-AX5K8C','KHOA HỌC VỀ LỐI SỐNG','stuart farrimond','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/khoahocveloisonge1702267101454.jpg?v=1705552510080',95000.00,'khoa-hoc',47,'Nhiều hoạt động mà chúng ta coi là đương nhiên trên thực tế đang làm suy yếu sức khỏe của chúng ta. Trong cuốn sách mang tính đột phá này, những niềm tin lâu đời đã bị khoa học hiện đại đánh đổ khi tiến sĩ Stuart Farrimond, với con mắt khoa học của mình, phân tích một ngày điển hình trong cuộc sống của chúng ta. Tôi nên đánh răng trước hay sau khi ăn sáng? Làm cách nào chống lại tình trạng uể oải vào giữa buổi chiều? Tôi ăn uống lành mạnh, vậy tại sao tôi không thể kiểm soát cân nặng của mình? Tất cả những câu hỏi này và nhiều câu hỏi khác sẽ được giải đáp.','2025-09-06 23:10:22','2025-09-09 19:16:35',3),('BK-B2LN66','Từ Tốt Đến Vĩ Đại','Jim Collins','https://example.com/images/good_to_great.jpg',180000.00,'kinh-doanh',17,'Phân tích cách các công ty chuyển từ tốt sang vĩ đại.','2025-09-06 23:10:22','2025-09-06 23:10:22',0),('BK-B3QVR5','CÂU CHUYỆN NGHỆ THUẬT','Tenzin Gyatso – Dalai Lama','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/cau-chuyen-nghe-thuat-the-illustrated-story-of-art-01.jpg?v=1722589847287',140000.00,'nghe-thuat',16,'Mạch phát triển của các trào lưu quan trọng nhất từ thời Phục hưng đến hiện đại, các họa sĩ tiêu biểu cùng những cách tân, biến hóa về thủ pháp của họ để thấy rằng có đôi khi, chỉ một thiên tài thôi – một Giotto, một Leonardo, một Picasso – cũng đủ để mở ra cả chân trời nghệ thuật.','2025-09-06 23:10:22','2025-09-07 06:16:22',3),('BK-CDDQ57','CON ĐƯỜNG TỈNH THỨC','hà vĩnh thọ','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/con-duong-tinh-thuc-01.jpg?v=1744625216473',150000.00,'triet-hoc',29,'Trong một thế giới với những tiến bộ vượt bậc của công nghệ cũng như đời sống ngày càng phức tạp thì hành trình tìm kiếm sự an ổn trong tâm hồn luôn là mưu cầu phổ quát. Những thách thức, cơ hội, và biến động trong thế kỷ 21 cho ta thấy việc nương tựa vào minh triết của tiền nhân để định vị những nhiễu nhương của hiện tại và những bất trắc của tương lai là vô cùng quan trọng.','2025-09-06 23:10:22','2025-09-07 00:36:20',1),('BK-CVNAWN','LƯỢC SỬ TÔN GIÁO','richard holloway','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/luocsutongiao01-4d24170a-0d06-427f-b136-f1086d5461ad.jpg?v=1732263668327',216000.00,'lich-su',20,'Hơn bảy tỷ người trên thế giới có thể viết một thứ gì đó khác chữ “Không” vào mục Tôn giáo trong hồ sơ của mình. Một số sinh ra đã theo một tôn giáo được chọn sẵn; số khác có thể tự lựa chọn theo sở thích, theo định hướng, theo đám đông...','2025-09-06 23:10:22','2025-09-09 19:16:35',2),('BK-E4J8KA','Phong cách NVIDIA (The NVIDIA Way)','tae kim','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/phong-cach-nvidia-bia-ao-01.jpg?v=1750646609997',90000.00,'khoa-hoc',24,'Trong bối cảnh bùng nổ của làn sóng Trí tuệ Nhân tạo (AI), Nvidia hiện đang khẳng định vị thế trung tâm của mình trong ngành công nghệ: con chip của họ là nền tảng của những siêu máy tính huấn luyện AI mạnh nhất thế giới. Thậm chí đã có thời điểm, Nvidia còn vượt mặt cả Microsoft để trở thành công ty có giá trị vốn hóa thị trường lớn nhất thế giới.','2025-09-06 23:10:22','2025-09-07 00:36:20',1),('BK-E5SA59','THIẾT KẾ HỆ THỐNG HỌC MÁY','huyền chip','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/thiet-ke-he-thong-hoc-may-01.jpg?v=1748402370840',214000.00,'khoa-hoc',30,'Các hệ thống học máy (ML) vừa phức tạp vừa độc đáo. Phức tạp vì chúng cấu thành từ nhiều thành phần khác nhau và cần tới nhiều bên liên quan khác nhau. Độc đáo vì chúng phụ thuộc vào dữ liệu, với dữ liệu có thể thay đổi rất lớn tùy vào từng trường hợp sử dụng cụ thể. Trong cuốn sách này, bạn sẽ học được một cách tiếp cận toàn diện để thiết kế các hệ thống học máy đáng tin cậy, có khả năng mở rộng, dễ bảo trì và thích ứng với các môi trường và yêu cầu kinh doanh thay đổi.','2025-09-06 23:10:22','2025-09-06 23:10:22',0),('BK-ERUMR9','Đắc Nhân Tâm','Dale Carnegie','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/dacnhantam03.jpg?v=1705552096050',130000.00,'kinh-doanh',40,'Đắc nhân tâm, nghĩa là sống sao cho đẹp lòng người, nhằm tạo dựng một cuộc sống an vui trong đời tư, thiện chí và đầy tinh thần hợp tác trong công việc. Lẽ thường sẽ chẳng bao giờ có một chiếc chìa khóa vạn năng mở ra mọi thành công. Bạn phải thông minh, đắc lực, nhiệt huyết và dấn thân. Nhưng Dale Carnegie sẽ nói rằng còn một điều cốt yếu nữa mà ta phải học: kỹ năng thu phục lòng người.','2025-09-06 23:10:22','2025-09-06 23:10:22',0),('BK-F9AH35','DOANH NGHIỆP CỐNG HIẾN','philip kotler','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/doanh-nghiep-cong-hien-01.jpg?v=1722825490417',210000.00,'kinh-doanh',35,'Trong những năm vừa qua, việc thực hiện trách nhiệm xã hội doanh nghiệp đã chuyển từ “làm được thì tốt” thành “phải làm” đối với mọi doanh nghiệp. Nhiều bằng chứng nghiên cứu và thực tiễn cho thấy muốn được tiếng hay – làm ngay việc thiện không chỉ là một sáo ngữ, và việc xây dựng các sáng kiến marketing vì cộng đồng hiệu quả có thể giúp kiến tạo một thế giới tốt đẹp hơn và gia tăng lợi nhuận cho doanh nghiệp.','2025-09-06 23:10:22','2025-09-06 23:10:22',0),('BK-G3TDHF','CHINH PHỤC HẠNH PHÚC','bertrand russell','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/chinhphuchanhphuc01e1719216190.jpg?v=1719216280550',140000.00,'triet-hoc',15,'Chừng nào còn khỏe mạnh và đủ ăn thì con vật còn hạnh phúc. Nhưng có vẻ điều ấy không đúng lắm với con người. Thời nay dường như ai ai cũng mang nặng một mối u sầu nào đó. Nhà toán học kiêm triết gia Russell cho rằng những mối u sầu hiện nay hầu hết chẳng có nguyên nhân cao siêu như ta tưởng.','2025-09-06 23:10:22','2025-09-06 23:10:22',0),('BK-HUNB6D','LÀM SAO ĐỂ…?','randall munroe','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/lamsaodeloikhuyenkhoahoclathuo.jpg?v=1718618628150',85000.00,'khoa-hoc',45,'LÀM SAO ĐỂ…?  là cuốn sách vô cùng giải trí và hài hước, mang đậm phong cách đặc trưng của Randall Munroe, tác giả của webcomic nổi tiếng xkcd và các bestseller như Nếu... thì?','2025-09-06 23:10:22','2025-09-06 23:10:22',0),('BK-KW85H7','LƯỢC SỬ HOA KỲ','james west','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/luocsuhoaky01e1720516593920.jpg?v=1720516918677',204000.00,'lich-su',18,'Lược sử Hoa Kỳ sẽ kể cho bạn nghe câu chuyện lịch sử trải suốt 500 năm, mô tả cách quốc gia này mở rộng và phát triển trên một châu lục đa sắc tộc, và giải thích cách những sắc tộc ấy hợp nhất dưới lá cờ của Tự do và Bình đẳng.','2025-09-06 23:10:22','2025-09-06 23:10:22',0),('BK-MM6GWS','HƯỚNG DẪN VIẾT VỀ PHIM','timothy corrigan','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/huong-dan-viet-ve-phim-01.jpg?v=1724040665327',168000.00,'nghe-thuat',21,'Nếu như các bộ phim cung cấp cho chúng ta thông tin về nhiều lĩnh vực của cuộc sống thì chúng ta cũng có thể thưởng thức chúng theo nhiều cách, không loại trừ cả việc thử suy nghĩ, giải thích, và viết về những trải nghiệm khi xem chúng. Chúng ta đến rạp chiếu bóng bởi nhiều lý do: để suy nghĩ hay để không phải suy nghĩ, để chăm chú vào các bộ phim, hay để viết về chúng. Chúng ta có thể đi xem một bộ phim giống như ta ăn một chiếc kẹo bông; và cũng có thể tìm đến một bộ phim khi mà chiếc kẹo đó trở thành món ăn tinh thần cho ta','2025-09-06 23:10:22','2025-09-06 23:10:22',0),('BK-N6PTRN','BÁ CHỦ AI','parmy olson','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/ba-chu-ai-02.jpg?v=1739505975113',161000.00,'kinh-doanh',28,'Bá chủ AI kể lại câu chuyện gay cấn về các chặng đường phát triển của AI: từ một ý tưởng viển vông không mấy ai tin, tới một dự án xa vời gọi vốn từ các tỷ phú tò mò, cho đến khi tạo tiếng vang với chương trình AlphaGo có thể đánh bại các nhà vô địch cờ vây thế giới. Và mới nhất là sự kiện ChatGPT đã tạo nên một làn sóng mà người ta đã so sánh với sự xuất hiện của điện thoại thông minh.','2025-09-06 23:10:22','2025-09-06 23:10:22',0),('BK-NX8M82','NGHỆ THUẬT HUẾ','Eckhart Tolle','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/nghethuathuehop02.jpg?v=1705552577237',255000.00,'nghe-thuat',27,'Với những bài phân tích tỉ mỉ cùng hơn 200 phụ bản sinh động, Nghệ thuật Huế là một nguồn tư liệu quý giá, lột tả các đặc tính của nền mỹ thuật bản địa vốn có nội dung phong phú cùng hệ thống biểu tượng, nhưng vẫn hạn chế về năng lực tả thực bởi các ràng buộc về quy ước trong văn hóa và nghe-thuat truyền thống, cũng như bởi tư duy khuôn mẫu của những nghệ nhân không một lần dám bước ra ngoài lệ thường.','2025-09-06 23:10:22','2025-09-06 23:10:22',0),('BK-QHSUHC','BẢO TÀNG KHẢI ĐỊNH','Tuyển tập cố đô Huế','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/baotangkhaidinh01.jpg?v=1696822783657',146000.00,'nghe-thuat',33,'Bảo tàng Khải Định vốn là một điện cũ có tên là Điện Long An. Sau triều đình giao cho Quốc Tử Giám dùng làm nơi lưu trữ tài liệu, được gọi là Tân Thơ Viện. Đến thời vua Duy Tân, Tân Thơ Viện được giao cho Hội những người bạn cố đô Huế để làm trụ sở. Năm 1923, dưới thời Khải Định, Tân Thơ Viện được tổ chức thành một cơ quan có tên là Cổ Học Viện, chuyên lưu trữ và bảo tồn những hiện vật xưa nay trên khắp An Nam. ','2025-09-06 23:10:22','2025-09-06 23:10:22',0),('BK-S7B6G2','SỰ AN ỦI CỦA TRIẾT HỌC','alain de botton','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/suanuitriethoc01-8b3e5c36-50b5-4eb4-8c04-9b17c21b31c2.jpg?v=1736215302887',110000.00,'triet-hoc',20,'Chúng ta sống ngày này qua ngày khác cùng những nỗi bận tâm: không tiền, thất tinh, thiếu thốn vật chất và tinh thần, lo lắng, sợ thất bại và áp lực phải hành xử theo chuẩn mực...Nỗi bận tâm nhỏ khiến ta dằn vặt. Nỗi bận tâm lớn có thể hủy hoại cả đời người. Alain de Botton viết về cách mà bộ óc vĩ đại nhất trong lịch sử triết học bàn về những nỗi bận tâm, niềm đau khổ trong cuộc sống thường ngày ấy. Khi không được ưa thích, khi chịu áp lực phải hành xử theo chuẩn mực..., ta có thể tìm đến Socrates. Khi không có tiền, ta có thể hỏi ý kiến Epicurus. Còn khi thất tình, ta hoàn toàn có thể chia sẻ với Schopenhauer.','2025-09-06 23:10:22','2025-09-06 23:10:22',0),('BK-WZAQNZ','TRIẾT HỌC VỀ SỰ HAM MUỐN','frédéric lenoir','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/triet-hoc-ve-su-ham-muon-01.jpg?v=1746605425953',106000.00,'triet-hoc',25,'Từ xưa, con người đã luôn tìm cách thấu hiểu và kiểm soát ham muốn, vì nó vừa là động lực sống thúc đẩy con người sáng tạo, yêu thương và vượt qua chính mình, vừa là nguồn cơn dẫn đến mọi thói hư tật xấu, hủy hoại cuộc sống của chúng ta. Hiện nay, trong cuộc sống, ham muốn của chúng ta gặp rất nhiều giới hạn, thậm chí bị giam cầm trong nhứng nhu cầu sơ cấp của não bộ và trong chủ nghĩa tiêu dùng luôn thúc đẩy mỗi người tiêu thụ ngày một nhiều hơn.','2025-09-06 23:10:22','2025-09-06 23:10:22',0);
/*!40000 ALTER TABLE `books` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flyway_schema_history`
--

DROP TABLE IF EXISTS `flyway_schema_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flyway_schema_history` (
  `installed_rank` int NOT NULL,
  `version` varchar(50) DEFAULT NULL,
  `description` varchar(200) NOT NULL,
  `type` varchar(20) NOT NULL,
  `script` varchar(1000) NOT NULL,
  `checksum` int DEFAULT NULL,
  `installed_by` varchar(100) NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`),
  KEY `flyway_schema_history_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flyway_schema_history`
--

LOCK TABLES `flyway_schema_history` WRITE;
/*!40000 ALTER TABLE `flyway_schema_history` DISABLE KEYS */;
INSERT INTO `flyway_schema_history` VALUES (1,'1','create books update','SQL','V1__create_books_update.sql',1180860652,'root','2025-09-07 05:51:30',31,1),(2,'2','add sales count to books','SQL','V2__add_sales_count_to_books.sql',1464386571,'root','2025-09-07 05:51:30',49,1),(3,'3','create sale promotion','SQL','V3__create_sale_promotion.sql',-205214613,'root','2025-09-07 05:51:30',27,1),(4,'4','create book sales stats','SQL','V4__create_book_sales_stats.sql',97458479,'root','2025-09-07 05:51:30',56,1),(5,'5','create book search','SQL','V5__create_book_search.sql',1692587445,'root','2025-09-07 05:51:30',88,1);
/*!40000 ALTER TABLE `flyway_schema_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `promotions`
--

DROP TABLE IF EXISTS `promotions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `promotions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `discount_type` varchar(20) NOT NULL,
  `discount_value` decimal(10,2) NOT NULL,
  `target_type` varchar(20) NOT NULL,
  `target_value` varchar(255) DEFAULT NULL,
  `start_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `promotions`
--

LOCK TABLES `promotions` WRITE;
/*!40000 ALTER TABLE `promotions` DISABLE KEYS */;
INSERT INTO `promotions` VALUES (1,'Giảm giá tất cả các sách 15%','PERCENT',15.00,'GLOBAL','ALL','2025-09-07 00:00:00','2025-09-29 00:00:00',1,'2025-09-07 06:14:06');
/*!40000 ALTER TABLE `promotions` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-17 13:53:49
