CREATE DATABASE  IF NOT EXISTS `orders_db_flyway` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `orders_db_flyway`;
-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: orders_db_flyway
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
INSERT INTO `flyway_schema_history` VALUES (1,'1','create order tables','SQL','V1__create_order_tables.sql',496170055,'root','2025-09-03 01:23:33',29,1),(2,'2','create orders item','SQL','V2__create_orders_item.sql',-150798798,'root','2025-09-03 01:23:33',46,1),(3,'3','create shipping info table','SQL','V3__create_shipping_info_table.sql',-1436830425,'root','2025-09-03 01:23:33',43,1);
/*!40000 ALTER TABLE `flyway_schema_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_items` (
  `id` varchar(50) NOT NULL,
  `order_id` varchar(50) NOT NULL,
  `product_id` varchar(50) NOT NULL,
  `title` varchar(255) NOT NULL,
  `image` varchar(255) NOT NULL,
  `quantity` int NOT NULL,
  `price` decimal(19,2) NOT NULL,
  `added_at` timestamp NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_order` (`order_id`),
  CONSTRAINT `fk_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
INSERT INTO `order_items` VALUES ('ORD-6E2BZFWT','ORD-DDM7SYKA','BK-AX5K8C','KHOA HỌC VỀ LỐI SỐNG','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/khoahocveloisonge1702267101454.jpg?v=1705552510080',1,95000.00,'2025-09-09 19:16:35'),('ORD-9QCA4LPX','ORD-DDM7SYKA','BK-CVNAWN','LƯỢC SỬ TÔN GIÁO','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/luocsutongiao01-4d24170a-0d06-427f-b136-f1086d5461ad.jpg?v=1732263668327',1,216000.00,'2025-09-09 19:16:35'),('ORD-BU34XCRQ','ORD-FIUCNTGG','BK-CVNAWN','LƯỢC SỬ TÔN GIÁO','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/luocsutongiao01-4d24170a-0d06-427f-b136-f1086d5461ad.jpg?v=1732263668327',1,216000.00,'2025-09-07 00:20:24'),('ORD-CSPTEDCG','ORD-3SKESYDA','BK-2QN6EW','LƯỢC SỬ TÔN GIÁO','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/luocsutongiao01-4d24170a-0d06-427f-b136-f1086d5461ad.jpg?v=1732263668327',1,199000.00,'2025-09-02 19:39:04'),('ORD-EMQYJGEB','ORD-3SKESYDA','BK-BMNKHS','DOANH NGHIỆP CỐNG HIẾN','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/doanh-nghiep-cong-hien-01.jpg?v=1722825490417',1,210000.00,'2025-09-02 19:39:04'),('ORD-HAHKXXRW','ORD-MRXMXGF6','BK-E4J8KA','Phong cách NVIDIA (The NVIDIA Way)','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/phong-cach-nvidia-bia-ao-01.jpg?v=1750646609997',1,90000.00,'2025-09-07 00:36:20'),('ORD-KMP00R2B','ORD-FIUCNTGG','BK-AT4F84','DANH NHÂN VIỆT NAM','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/danhnhane1699525531491.jpg?v=1705552512687',1,230000.00,'2025-09-07 00:20:24'),('ORD-MIOVUN0E','ORD-ERXD0E1Q','BK-4LAU8N','LƯỢC SỬ TRIẾT HỌC','https://bizweb.dktcdn.net/thumb/large/100/363/455/products/luocsutriethoc01.jpg?v=1710306251673',1,120000.00,'2025-09-02 19:34:56'),('ORD-NK4F1CMG','ORD-3SKESYDA','BK-RKNNT8','50 Ý TƯỞNG TRIẾT HỌC','https://bizweb.dktcdn.net/100/363/455/products/50ytuongtriethoc01.jpg?v=1705552118277',1,350000.00,'2025-09-02 19:39:04'),('ORD-PGKIOHR3','ORD-MRXMXGF6','BK-CDDQ57','CON ĐƯỜNG TỈNH THỨC','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/con-duong-tinh-thuc-01.jpg?v=1744625216473',1,150000.00,'2025-09-07 00:36:20'),('ORD-PO047FOH','ORD-FIUCNTGG','BK-B3QVR5','CÂU CHUYỆN NGHỆ THUẬT','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/cau-chuyen-nghe-thuat-the-illustrated-story-of-art-01.jpg?v=1722589847287',1,140000.00,'2025-09-07 00:20:24'),('ORD-US55UNV8','ORD-MRXMXGF6','BK-AT4F84','DANH NHÂN VIỆT NAM','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/danhnhane1699525531491.jpg?v=1705552512687',1,230000.00,'2025-09-07 00:36:20'),('ORD-USODKKZE','ORD-ZYN7H9SY','BK-AT4F84','DANH NHÂN VIỆT NAM','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/danhnhane1699525531491.jpg?v=1705552512687',1,230000.00,'2025-09-09 19:15:12'),('ORD-VMGATNOQ','ORD-FIUCNTGG','BK-AX5K8C','KHOA HỌC VỀ LỐI SỐNG','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/khoahocveloisonge1702267101454.jpg?v=1705552510080',1,95000.00,'2025-09-07 00:20:24'),('ORD-WXOFEE7I','ORD-5J45KYT0','BK-AX5K8C','KHOA HỌC VỀ LỐI SỐNG','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/khoahocveloisonge1702267101454.jpg?v=1705552510080',1,95000.00,'2025-09-07 06:16:22'),('ORD-YDWHWF7O','ORD-5J45KYT0','BK-B3QVR5','CÂU CHUYỆN NGHỆ THUẬT','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/cau-chuyen-nghe-thuat-the-illustrated-story-of-art-01.jpg?v=1722589847287',2,140000.00,'2025-09-07 06:16:22');
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` varchar(50) NOT NULL,
  `user_name` varchar(100) NOT NULL,
  `status` varchar(30) NOT NULL,
  `payment_method` varchar(30) NOT NULL,
  `total_amount` decimal(19,2) NOT NULL,
  `created_at` timestamp NOT NULL,
  `expires_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES ('ORD-3SKESYDA','whispering','CONFIRMED','COD',799000.00,'2025-09-02 19:39:04',NULL),('ORD-5J45KYT0','vincenzo','CONFIRMED','COD',415000.00,'2025-09-07 06:16:22',NULL),('ORD-DDM7SYKA','iceking29902','CONFIRMED','COD',351000.00,'2025-09-09 19:16:35',NULL),('ORD-ERXD0E1Q','whispering','CONFIRMED','COD',160000.00,'2025-09-02 19:34:56',NULL),('ORD-FIUCNTGG','vincenzo','CONFIRMED','COD',721000.00,'2025-09-07 00:20:24',NULL),('ORD-MRXMXGF6','vincenzo','CONFIRMED','COD',510000.00,'2025-09-07 00:36:20',NULL),('ORD-ZYN7H9SY','iceking29902','CONFIRMED','COD',270000.00,'2025-09-09 19:15:12',NULL);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shipping_info`
--

DROP TABLE IF EXISTS `shipping_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shipping_info` (
  `id` varchar(50) NOT NULL,
  `recipient_first_name` varchar(100) NOT NULL,
  `recipient_last_name` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `province` varchar(100) NOT NULL,
  `ward` varchar(100) NOT NULL,
  `street` varchar(200) NOT NULL,
  `note` text,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `order_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_id` (`order_id`),
  CONSTRAINT `fk_order_shipping` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shipping_info`
--

LOCK TABLES `shipping_info` WRITE;
/*!40000 ALTER TABLE `shipping_info` DISABLE KEYS */;
INSERT INTO `shipping_info` VALUES ('SHP-5VPSOFLW','Võ Ngọc','Thạnh','vincenzo@gmail.com','03335555555','Thành phố Hồ Chí Minh','Xã Bà Điểm','','giao hàng vào buổi sáng','2025-09-07 13:16:22','ORD-5J45KYT0'),('SHP-85952GEZ','Nguyen Van','A','whispering@gmail.com','0901234567','Thành phố Hồ Chí Minh','Phường Hiệp Bình','123 Lê Lợi','Giao buổi sáng','2025-09-03 02:39:04','ORD-3SKESYDA'),('SHP-8AQFZ3E4','Văn Ân','Nguyễn','iceking29902@gmail.com','0369125775','Thành phố Hồ Chí Minh','Phường Bảy Hiền','Đường số 37','giao hàng buổi trưa','2025-09-10 02:15:12','ORD-ZYN7H9SY'),('SHP-RXNHOHIV','Võ Văn','Vinh','vincenzo@gmail.com','0377897534','Tỉnh Khánh Hòa','','đường số 34','chỉ giao hàng vào buổi sáng','2025-09-07 07:20:25','ORD-FIUCNTGG'),('SHP-UB1CDV3U','Võ Văn','Vinh','vincenzo@gmail.com','0233444523','Thành phố Hồ Chí Minh','Phường Chánh Phú Hòa','đường số 34','giao hàng buổi sáng','2025-09-07 07:36:20','ORD-MRXMXGF6'),('SHP-VPTQFEXD','Văn Ân','Nguyễn','iceking29902@gmail.com','0369125775','Thành phố Hồ Chí Minh','Phường Bảy Hiền','Đường số 37','giao hàng buổi trưa','2025-09-10 02:16:35','ORD-DDM7SYKA'),('SHP-WXJJ92XN','Nguyen Van','A','whispering@gmail.com','0901234567','Thành phố Hồ Chí Minh','Phường Hiệp Bình','123 Lê Lợi','Giao buổi sáng','2025-09-03 02:34:56','ORD-ERXD0E1Q');
/*!40000 ALTER TABLE `shipping_info` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-17 14:23:46
