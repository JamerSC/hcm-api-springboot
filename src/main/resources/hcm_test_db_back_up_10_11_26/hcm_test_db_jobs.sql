CREATE DATABASE  IF NOT EXISTS `hcm_test_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `hcm_test_db`;
-- MySQL dump 10.13  Distrib 8.0.31, for macos12 (x86_64)
--
-- Host: 127.0.0.1    Database: hcm_test_db
-- ------------------------------------------------------
-- Server version	8.0.26

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
-- Table structure for table `jobs`
--

DROP TABLE IF EXISTS `jobs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `jobs` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` text NOT NULL,
  `location` varchar(255) DEFAULT NULL,
  `posted_date` date DEFAULT NULL,
  `requirements` varchar(255) NOT NULL,
  `status` enum('CLOSED','CREATED','FILLED','ON_HOLD','OPEN') NOT NULL,
  `title` varchar(255) NOT NULL,
  `created_by_user_id` bigint DEFAULT NULL,
  `department_id` bigint DEFAULT NULL,
  `posted_by_user_id` bigint DEFAULT NULL,
  `created_at` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `updated_at` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `updated_by_user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdkxkvwics1m1weva7kq8dhwf4` (`created_by_user_id`),
  KEY `FKt27140un7et9j2mqcevbh4shk` (`department_id`),
  KEY `FK6qscrbh8nm8lkow3ak9om1t9o` (`posted_by_user_id`),
  KEY `FKjrgoxyyguah38lg5vu7jkqd4k` (`updated_by_user_id`),
  CONSTRAINT `FK6qscrbh8nm8lkow3ak9om1t9o` FOREIGN KEY (`posted_by_user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKdkxkvwics1m1weva7kq8dhwf4` FOREIGN KEY (`created_by_user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKjrgoxyyguah38lg5vu7jkqd4k` FOREIGN KEY (`updated_by_user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKt27140un7et9j2mqcevbh4shk` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jobs`
--

LOCK TABLES `jobs` WRITE;
/*!40000 ALTER TABLE `jobs` DISABLE KEYS */;
INSERT INTO `jobs` VALUES (1,'Administrator Head','Manila','2025-08-13','BC','FILLED','Administrator',1,1,NULL,'2025-08-13 12:53:52.845000','2025-08-13 12:53:52.845000',1),(2,'Assist on admin works','Manila','2025-08-14','BC','OPEN','Admin Junior',1,1,1,'2025-08-14 12:26:11.484000','2025-08-26 11:27:23.720000',1),(3,'Assist on HR works','Manila','2025-08-15','BC','CREATED','HR Staff',2,4,NULL,'2025-08-16 11:33:31.336000','2025-08-16 11:33:31.336000',2),(4,'Assist on IT works','Manila','2025-08-15','BC','CREATED','IT Staff',2,2,NULL,'2025-08-16 11:34:11.196000','2025-08-16 11:34:11.196000',2),(6,'Implement quality assurance','Manila','2025-08-15','BC','OPEN','QA Engineer',1,2,1,'2025-09-16 12:02:00.055000','2025-09-16 12:02:53.398000',1),(7,'Implement quality assurance','Manila','2025-08-15','BC','OPEN','Software Engineer',1,2,1,'2025-09-16 12:02:12.546000','2025-09-16 12:02:59.692000',1),(8,'Focus on client requirements & deliverables','Manila','2025-08-15','BC','OPEN','Business Dev Officer',1,2,1,'2025-10-13 10:47:52.148000','2025-10-13 10:48:24.268000',1),(9,'Focus on client requirements & deliverables','Manila','2025-08-15','Resume & Diploman','CREATED','Test Developer',1,2,NULL,'2025-10-17 12:46:33.141000','2025-10-17 12:48:08.995000',1);
/*!40000 ALTER TABLE `jobs` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-11 22:05:20
