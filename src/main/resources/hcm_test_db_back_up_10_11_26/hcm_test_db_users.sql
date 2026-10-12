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
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `created_at` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `updated_at` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `created_by_user_id` bigint DEFAULT NULL,
  `updated_by_user_id` bigint DEFAULT NULL,
  `employee_id` bigint DEFAULT NULL,
  `active` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`),
  UNIQUE KEY `UKd1s31g1a7ilra77m65xmka3ei` (`employee_id`),
  KEY `FKpcsj08dbah77s3pprdt49kleh` (`created_by_user_id`),
  KEY `FKipwrqx5wmkmad7otmwv8yh9gn` (`updated_by_user_id`),
  CONSTRAINT `FK6p2ib82uai0pj9yk1iassppgq` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`),
  CONSTRAINT `FKipwrqx5wmkmad7otmwv8yh9gn` FOREIGN KEY (`updated_by_user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKpcsj08dbah77s3pprdt49kleh` FOREIGN KEY (`created_by_user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin@mail.com','Super','Admin','$2a$10$JguKTlN/QDdj4HZ.nxO6feooZ6ZsLf0neE7Fj9kBMITQ//afjP3zK','admin123@','2025-12-21 16:38:25.675399','2025-12-25 17:05:15.270269',1,1,1,_binary ''),(2,'mary@mail.com','Mary','Public','$2a$10$JguKTlN/QDdj4HZ.nxO6feooZ6ZsLf0neE7Fj9kBMITQ//afjP3zK','mary123@','2025-08-03 08:29:22.849000','2025-12-25 17:05:15.276007',1,1,NULL,_binary ''),(4,'chan@mail.com','Jackie','Chan','$2a$10$JguKTlN/QDdj4HZ.nxO6feooZ6ZsLf0neE7Fj9kBMITQ//afjP3zK','chan123@','2025-08-05 12:40:44.981000','2025-12-25 17:05:15.277753',1,1,NULL,_binary ''),(5,'brock@mail.com','Brock','Lesnar','$2a$10$JguKTlN/QDdj4HZ.nxO6feooZ6ZsLf0neE7Fj9kBMITQ//afjP3zK','brock123@','2025-08-05 12:41:31.829000','2025-12-25 17:05:15.276851',1,1,NULL,_binary ''),(6,'keanu@mail.com','Keanu','Reeves','$2a$10$JguKTlN/QDdj4HZ.nxO6feooZ6ZsLf0neE7Fj9kBMITQ//afjP3zK','keanu123@','2025-08-05 12:42:03.796000','2025-12-25 17:05:15.278250',1,1,NULL,_binary ''),(8,'marypublic@mail.com','Mary','Public','$2a$10$JguKTlN/QDdj4HZ.nxO6feooZ6ZsLf0neE7Fj9kBMITQ//afjP3zK','mary123$','2025-09-10 12:38:39.570000','2025-09-10 12:38:39.570000',1,1,2,_binary ''),(9,'jamer.gomezlaw@gmail.com','Jamer','Catalla','$2a$10$JguKTlN/QDdj4HZ.nxO6feooZ6ZsLf0neE7Fj9kBMITQ//afjP3zK','jamer123@','2025-09-13 12:42:20.654000','2025-12-25 17:05:15.278578',1,1,NULL,_binary ''),(10,'jamer@example.com','Jamer','Catalla','$2a$10$HxUcZCpbLIVci/9g8lw.MOSPZC/ugSRS2rQCAUFfxNiW4tLTV5Pci','jamer@mail.com','2025-11-30 09:05:24.364000','2025-12-25 17:05:15.278958',1,1,NULL,_binary '');
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

-- Dump completed on 2026-10-11 22:05:20
