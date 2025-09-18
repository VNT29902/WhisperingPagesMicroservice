CREATE DATABASE  IF NOT EXISTS `auth_db_flyway` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `auth_db_flyway`;
-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: auth_db_flyway
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
INSERT INTO `flyway_schema_history` VALUES (1,'1','create users table','SQL','V1__create_users_table.sql',-299532513,'root','2025-07-26 15:17:52',24,1),(2,'2','create roles table.sql','SQL','V2__create_roles_table.sql.sql',-1737434961,'root','2025-07-26 15:17:52',24,1),(3,'3','create user roles table','SQL','V3__create_user_roles_table.sql',-1907730593,'root','2025-07-26 15:17:52',41,1),(4,'4','create refresh tokens table','SQL','V4__create_refresh_tokens_table.sql',-2070676200,'root','2025-07-26 15:17:53',46,1),(5,'5','update users table','SQL','V5__update_users_table.sql',-82693014,'root','2025-07-26 15:17:53',138,1),(6,'6','alter refresh token add sectionid','SQL','V6__alter_refresh_token_add_sectionid.sql',1212732401,'root','2025-09-11 15:36:42',42,0);
/*!40000 ALTER TABLE `flyway_schema_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `refresh_tokens`
--

DROP TABLE IF EXISTS `refresh_tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `refresh_tokens` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `token` varchar(255) NOT NULL,
  `expiry_date` timestamp NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `revoked` tinyint(1) NOT NULL DEFAULT '0',
  `user_id` bigint NOT NULL,
  `session_id` varchar(36) NOT NULL DEFAULT 'MIGRATION_TEMP',
  `device_id` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `token` (`token`),
  KEY `fk_refresh_token_user` (`user_id`),
  CONSTRAINT `fk_refresh_token_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=652 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `refresh_tokens`
--

LOCK TABLES `refresh_tokens` WRITE;
/*!40000 ALTER TABLE `refresh_tokens` DISABLE KEYS */;
INSERT INTO `refresh_tokens` VALUES (626,'c4e87c8c-d938-452a-aaa9-9b205ec14744','2025-09-18 22:08:03','2025-09-11 22:08:03',0,1,'c3d5fe3c-d454-434e-9e34-455498a556dc','desktop'),(627,'7ffe0c91-b6dd-4037-890c-be3308d9c520','2025-09-18 22:14:43','2025-09-11 22:14:43',0,1,'71e272e7-75e8-4655-a127-2186aca751ae','desktop'),(628,'fda6b79e-f621-4d69-8477-022c8e9b446d','2025-09-18 22:29:14','2025-09-11 22:29:14',1,2,'892af8dc-39d8-48a8-8bd3-f201b0659f33','desktop'),(629,'95de2c58-62fb-48e4-8219-b66543faf7b7','2025-09-18 22:33:53','2025-09-11 22:33:53',1,2,'903c78be-0370-45cf-adb2-05808430068f','desktop'),(630,'4cf5f17e-642a-4759-bc86-31d51382b8b7','2025-09-18 22:35:53','2025-09-11 22:35:53',1,2,'a67983e0-e467-433b-9b52-03297e07b9f5','desktop'),(631,'5c87520d-0d73-44e8-b3bc-5132b55ad7c5','2025-09-18 22:40:40','2025-09-11 22:40:40',1,2,'f7399a8e-416b-4b97-896a-276e3798a161','desktop'),(632,'5c956947-2623-48b2-a65c-4556ccf32073','2025-09-18 22:40:40','2025-09-11 22:40:40',1,2,'fda84a63-1f8d-4ee1-9389-3b15a65cb7d9','desktop'),(633,'6061f272-d0fc-4744-8485-412a8b3f3b56','2025-09-18 22:53:49','2025-09-11 22:53:49',1,2,'5b76e159-c575-4f5c-ba7e-c162be56f51c','desktop'),(634,'fb8e0b59-d6f9-407b-a2cd-d8b15748b861','2025-09-18 22:54:22','2025-09-11 22:54:22',1,2,'5dae31e0-e130-4979-86d2-d05761cbc23b','desktop'),(635,'73a8975e-baad-49dc-882c-24118911a466','2025-09-18 23:10:46','2025-09-11 23:10:46',1,2,'0dbf4e05-c1be-4b84-8367-656a4333a4a6','desktop'),(636,'741afaee-d6a1-4d9d-b19b-fa57205bab61','2025-09-19 00:11:56','2025-09-12 00:11:56',1,2,'d42bb45a-4497-4e5f-b988-811d01f8acb9','desktop'),(637,'314e5a33-8c2e-4487-9e38-5cbd6f6b12c8','2025-09-19 00:21:51','2025-09-12 00:21:51',1,2,'ec41dd5c-d0ba-45d0-b96c-5f9e89d57ce8','desktop'),(638,'149c338a-06a4-46d6-bccd-530f21501476','2025-09-19 00:22:45','2025-09-12 00:22:45',1,2,'da621e21-148c-4e7c-9524-142be27169e4','desktop'),(639,'7eb36d4b-32ae-4f58-bc1c-60c7efc926a5','2025-09-19 00:26:23','2025-09-12 00:26:23',1,2,'98fa46b2-537d-4329-8830-ae94a317e0ec','desktop'),(640,'34cd671f-7d27-487f-8bc0-a8f91614010f','2025-09-19 00:30:40','2025-09-12 00:30:40',1,2,'fda35c90-6384-4d67-be45-263608297251','desktop'),(641,'12af1ab6-cfe1-4869-8a65-daa4d9566bdd','2025-09-19 00:51:43','2025-09-12 00:51:43',1,2,'5b444b4f-a58d-45a8-bb1e-c918fde19fc5','desktop'),(642,'f2733248-b653-4cb7-b6b5-8bdcaa20bea0','2025-09-19 01:08:14','2025-09-12 01:08:14',1,2,'dcb8e64f-dbeb-45ee-ab12-cefd248fc06f','desktop'),(643,'5cb3fd04-751a-4f1b-8b8b-1a4a4cbbd799','2025-09-19 01:08:20','2025-09-12 01:08:20',1,2,'06b6f428-8035-44ba-9af5-f85f1cb9b6c5','desktop'),(644,'27a23b57-7455-4464-b2fb-c7ea08c6a92a','2025-09-19 01:08:30','2025-09-12 01:08:30',1,2,'262e7e8f-e0a7-4e8e-93e2-e0b2bdf13160','desktop'),(645,'48c310b2-88d2-4cab-8551-feed2ab13af7','2025-09-19 01:08:36','2025-09-12 01:08:36',1,2,'f042755a-e81c-4edb-8958-94fda981bb81','desktop'),(646,'b3e9c880-9b53-4cf1-8aeb-6f1c3dcbdb67','2025-09-19 01:31:18','2025-09-12 01:31:18',1,2,'89fd261b-a06d-49db-b38b-2436cf8bdf4f','desktop'),(647,'8c79ca75-2c1f-4551-8e26-26148c592d42','2025-09-19 01:32:52','2025-09-12 01:32:52',1,2,'c8685d01-af18-4242-b227-86376ce25053','desktop'),(648,'b06d1be9-affa-4d16-9f40-0a9aa3d245f1','2025-09-19 01:34:37','2025-09-12 01:34:37',1,2,'23b2dca8-0b55-4155-af5c-bd5f8be48b20','desktop'),(649,'a0c95702-78a0-4c87-b262-eeec7fa3324b','2025-09-19 01:49:58','2025-09-12 01:49:58',1,2,'39736fbc-0846-4390-ada3-fc8c14651951','desktop'),(650,'cdfbe239-99c8-4ec1-b4ee-d2b838d43d44','2025-09-19 01:50:43','2025-09-12 01:50:43',1,2,'e17c7e68-6304-4c8d-9828-9073f6e8b724','desktop'),(651,'040764e0-b86c-422a-95b0-0daa6c44fde8','2025-09-19 01:52:33','2025-09-12 01:52:33',1,2,'ade203d3-5d3c-4935-8527-8b817ebee2a0','desktop');
/*!40000 ALTER TABLE `refresh_tokens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (2,'ROLE_ADMIN'),(1,'ROLE_USER');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_roles` (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `user_roles_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `user_roles_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_roles`
--

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;
INSERT INTO `user_roles` VALUES (2,1),(3,1),(1,2);
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT '1',
  `provider` varchar(20) DEFAULT 'LOCAL',
  `provider_id` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'whispering','whispering@gmail.com','Vincent','Vo','$2a$10$6Gpc9y0BFZ88YrWeZa86dOub8/ISwiKz1DOTaplaw90XNwDKHquTq',1,'LOCAL',NULL,'0369123667'),(2,'vincenzo','vincenzo@gmail.com','Vincenzo','Le','$2a$10$9HsEomWlkCYI.y.RsReB2.164zW4iyRZp.hy4n8Th3e9RLCBOOgK.',1,'LOCAL',NULL,'0365223887'),(3,'iceking29902','iceking29902@gmail.com',NULL,NULL,NULL,1,'GOOGLE','102466502418065564812',NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-17 13:51:54
