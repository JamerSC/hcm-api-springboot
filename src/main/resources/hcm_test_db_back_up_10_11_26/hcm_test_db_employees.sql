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
-- Table structure for table `employees`
--

DROP TABLE IF EXISTS `employees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employees` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `hired_date` date DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `salary` double DEFAULT NULL,
  `created_at` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `updated_at` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `created_by_user_id` bigint DEFAULT NULL,
  `updated_by_user_id` bigint DEFAULT NULL,
  `job_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKda7l61mwq7ianepuu0iww39v4` (`created_by_user_id`),
  KEY `FK5mh7fdhm8iliiog1hvvgkgre3` (`updated_by_user_id`),
  KEY `FKnpqyu6u0fdh2rmqtoue23gxb4` (`job_id`),
  CONSTRAINT `FK5mh7fdhm8iliiog1hvvgkgre3` FOREIGN KEY (`updated_by_user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKda7l61mwq7ianepuu0iww39v4` FOREIGN KEY (`created_by_user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKnpqyu6u0fdh2rmqtoue23gxb4` FOREIGN KEY (`job_id`) REFERENCES `jobs` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employees`
--

LOCK TABLES `employees` WRITE;
/*!40000 ALTER TABLE `employees` DISABLE KEYS */;
INSERT INTO `employees` VALUES (1,'john.doe@mail.com','John','2024-01-01','Doe',30000,'2025-12-21 16:36:11.128640','2025-10-18 11:33:25.571000',1,1,1),(2,'mary@mail.com','Mary','2024-06-01','Public',30000,'2025-08-11 11:13:03.574000','2025-08-11 11:13:03.574000',1,1,2),(3,'susan@mail.com','Susan','2025-01-01','Roses',30000,'2025-08-11 11:13:03.574000','2025-08-11 11:13:03.574000',1,1,2),(5,'shelby@mail.com','Tommy','2024-03-01','Shelby',40000,'2025-08-11 11:13:03.574000','2025-08-11 11:13:03.574000',1,1,2),(7,'luna@mail.com','Juan','2024-03-01','Luna',40000,'2025-08-11 11:13:03.574000','2025-08-11 11:13:03.574000',1,1,2),(8,'jose@mail.com','Jose','2024-03-01','Dela Cruz',40000,'2025-08-11 11:13:03.574000','2025-08-11 11:13:03.574000',1,1,2),(9,'shark@mail.com','Sonny','2025-01-01','Shark',100,'2025-08-11 11:13:03.574000','2025-08-11 11:13:03.574000',1,1,2),(15,'luffy@mail.com','Luffy','2025-08-01','Monkey D.',0,'2025-08-11 11:13:03.574000','2025-08-11 11:13:03.574000',1,1,2),(16,'mary@mail.com','Mary','2025-09-09','Public',NULL,'2025-09-09 12:10:09.863000','2025-09-09 12:10:09.863000',1,1,2),(17,'user@example.com','Example','2025-11-26','User',10000,'2025-11-25 23:04:39.315000','2025-11-25 23:05:46.400000',1,1,9),(18,'mary@mail.com','Mary','2025-11-27','Public',NULL,'2025-11-27 11:32:53.537000','2025-11-27 11:32:53.537000',1,1,2);
/*!40000 ALTER TABLE `employees` ENABLE KEYS */;
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
