CREATE DATABASE  IF NOT EXISTS `cart_db_flyway` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `cart_db_flyway`;
-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: cart_db_flyway
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
-- Table structure for table `cart_items`
--

DROP TABLE IF EXISTS `cart_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cart_id` varchar(36) NOT NULL,
  `product_id` varchar(20) NOT NULL,
  `image` varchar(200) NOT NULL,
  `title` varchar(200) NOT NULL,
  `quantity` int NOT NULL,
  `price` decimal(19,2) NOT NULL,
  `added_at` timestamp NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_cart_item_cart` (`cart_id`),
  CONSTRAINT `fk_cart_item_cart` FOREIGN KEY (`cart_id`) REFERENCES `carts` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_items`
--

LOCK TABLES `cart_items` WRITE;
/*!40000 ALTER TABLE `cart_items` DISABLE KEYS */;
INSERT INTO `cart_items` VALUES (41,'CAR-Y6WLXNBZ','BK-548USQ','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/baotangkhaidinh01.jpg?v=1696822783657','BẢO TÀNG KHẢI ĐỊNH',2,146000.00,'2025-09-02 20:53:42'),(49,'CAR-Y6WLXNBZ','BK-AX5K8C','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/khoahocveloisonge1702267101454.jpg?v=1705552510080','KHOA HỌC VỀ LỐI SỐNG',2,95000.00,'2025-09-07 06:14:00'),(50,'CAR-Y6WLXNBZ','BK-CVNAWN','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/luocsutongiao01-4d24170a-0d06-427f-b136-f1086d5461ad.jpg?v=1732263668327','LƯỢC SỬ TÔN GIÁO',1,216000.00,'2025-09-07 06:14:01'),(53,'CAR-Y6WLXNBZ','BK-CDDQ57','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/con-duong-tinh-thuc-01.jpg?v=1744625216473','CON ĐƯỜNG TỈNH THỨC',1,150000.00,'2025-09-09 18:56:33'),(58,'CAR-VDDSJFUB','BK-AT4F84','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/danhnhane1699525531491.jpg?v=1705552512687','DANH NHÂN VIỆT NAM',1,230000.00,'2025-09-09 19:54:54'),(59,'CAR-VDDSJFUB','BK-CVNAWN','https://bizweb.dktcdn.net/thumb/1024x1024/100/363/455/products/luocsutongiao01-4d24170a-0d06-427f-b136-f1086d5461ad.jpg?v=1732263668327','LƯỢC SỬ TÔN GIÁO',1,216000.00,'2025-09-09 19:54:57');
/*!40000 ALTER TABLE `cart_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carts`
--

DROP TABLE IF EXISTS `carts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carts` (
  `id` varchar(36) NOT NULL,
  `user_name` varchar(100) DEFAULT NULL,
  `guest_id` binary(16) DEFAULT NULL,
  `created_at` timestamp NOT NULL,
  `updated_at` timestamp NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carts`
--

LOCK TABLES `carts` WRITE;
/*!40000 ALTER TABLE `carts` DISABLE KEYS */;
INSERT INTO `carts` VALUES ('CAR-QKPMSNXW','iceking29902',NULL,'2025-09-09 19:14:54','2025-09-09 19:16:35'),('CAR-VDDSJFUB','vincenzo',NULL,'2025-09-07 00:19:10','2025-09-09 19:54:57'),('CAR-Y6WLXNBZ','whispering',NULL,'2025-08-18 23:53:51','2025-09-09 18:56:33');
/*!40000 ALTER TABLE `carts` ENABLE KEYS */;
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
INSERT INTO `flyway_schema_history` VALUES (1,'1','create cart','SQL','V1__create_cart.sql',489323161,'root','2025-08-19 06:53:21',93,1),(2,'2','create cart item','SQL','V2__create_cart_item.sql',-700907561,'root','2025-08-19 06:53:22',104,1);
/*!40000 ALTER TABLE `flyway_schema_history` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-17 14:22:53
