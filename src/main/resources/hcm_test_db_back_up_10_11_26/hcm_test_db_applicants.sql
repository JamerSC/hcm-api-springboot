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
-- Table structure for table `applicants`
--

DROP TABLE IF EXISTS `applicants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `applicants` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `cv_file_path` varchar(255) DEFAULT NULL,
  `education_level` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `user_id` bigint NOT NULL,
  `created_at` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `updated_at` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `created_by_user_id` bigint DEFAULT NULL,
  `updated_by_user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK8dcjpq7ywwkgrsen3kr8yfj57` (`user_id`),
  KEY `FK4vgriwqkodaekis5bs47dn5ee` (`created_by_user_id`),
  KEY `FK951f3uxlkf1ecur0kcqnsrlx4` (`updated_by_user_id`),
  CONSTRAINT `FK4vgriwqkodaekis5bs47dn5ee` FOREIGN KEY (`created_by_user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK951f3uxlkf1ecur0kcqnsrlx4` FOREIGN KEY (`updated_by_user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKs48x9ywgx7l6mg1egokmys0gp` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `applicants`
--

LOCK TABLES `applicants` WRITE;
/*!40000 ALTER TABLE `applicants` DISABLE KEYS */;
INSERT INTO `applicants` VALUES (1,'Paco, Manila','2-661071dc-5f5a-4bdd-b482-2d7a48129904.pdf','Masters Degree','Mary','Public','09XXXXXX999',2,'2025-12-21 16:24:24.470914','2025-10-20 11:10:58.871000',NULL,2),(3,NULL,NULL,NULL,'Jackie','Chan',NULL,4,'2025-12-21 16:24:24.470914','2025-12-21 16:24:24.470914',NULL,NULL),(4,NULL,NULL,NULL,'Brock','Lesnar',NULL,5,'2025-12-21 16:24:24.470914','2025-12-21 16:24:24.470914',NULL,NULL),(5,NULL,NULL,NULL,'Keanu','Reeves',NULL,6,'2025-12-21 16:24:24.470914','2025-12-21 16:24:24.470914',NULL,NULL),(6,NULL,NULL,NULL,'Jamer','Catalla',NULL,9,'2025-09-13 12:42:20.702000','2025-09-13 12:42:20.702000',NULL,NULL),(7,NULL,NULL,NULL,'Jamer','Catalla',NULL,10,'2025-11-30 09:05:24.430000','2025-11-30 09:05:24.430000',NULL,NULL);
/*!40000 ALTER TABLE `applicants` ENABLE KEYS */;
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
