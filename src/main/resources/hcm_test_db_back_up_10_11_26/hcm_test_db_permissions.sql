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
-- Table structure for table `permissions`
--

DROP TABLE IF EXISTS `permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permissions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permissions`
--

LOCK TABLES `permissions` WRITE;
/*!40000 ALTER TABLE `permissions` DISABLE KEYS */;
INSERT INTO `permissions` VALUES (1,'Can view users','VIEW_USERS'),(2,'Can add new users','CREATE_USERS'),(3,'Can update users','UPDATE_USERS'),(4,'Can archive users','ARCHIVE_USERS'),(5,'Can change password users','CHANGE_PASSWORD_USERS'),(6,'Can logout users','LOGOUT_USERS'),(7,'Can view employees','VIEW_EMPLOYEES'),(8,'Can view profile','VIEW_PROFILE'),(9,'Can add new employees','CREATE_EMPLOYEES'),(10,'Can update employees','UPDATE_EMPLOYEES'),(11,'Can archive employees','ARCHIVE_EMPLOYEES'),(12,'Can unarchived or restore employees','UNARCHIVED_EMPLOYEES'),(13,'Can view applicants - recruitment module','VIEW_APPLICANTS'),(14,'Can view applications - recruitment module','VIEW_APPLICATIONS'),(15,'Can update applications - recruitment module','UPDATE_APPLICATIONS'),(16,'Can approve applications - recruitment module','APPROVE_APPLICATIONS'),(17,'Can reject applications - recruitment module','REJECT_APPLICATIONS'),(18,'Can change application status to initial qualifications - recruitment module','INITIAL_QUALIFICATION'),(19,'Can change application status to first inteview - recruitment module','FIRST_INTERVIEW'),(20,'Can change application status to second inteview - recruitment module','SECOND_INTERVIEW'),(21,'Can change application status to contract proposal -  recruitment module','CONTRACT_PROPOSAL'),(22,'Can change application status to contract signed -  recruitment module','CONTRACT_SIGNED'),(23,'Can change application status to hire applicant -  recruitment module','HIRE_APPLICANTS'),(24,'Can view departments','VIEW_DEPARTMENTS'),(25,'Can add departments','CREATE_DEPARTMENTS'),(26,'Can update departments','UPDATE_DEPARTMENTS'),(27,'Can archive departments','ARCHIVE_DEPARTMENTS'),(28,'Can unarchive/restore departments','UNARCHIVED_DEPARTMENTS'),(29,'Can view jobs','VIEW_JOBS'),(30,'Can add jobs','CREATE_JOBS'),(31,'Can update jobs','UPDATE_JOBS'),(32,'Can change status of jobs to open','OPEN_JOBS'),(33,'Can change status of jobs to close','CLOSE_JOBS'),(34,'Can change status of jobs to filled','FILL_JOBS'),(35,'Can archive jobs','ARCHIVE_JOBS'),(36,'Can unarchived/restore jobs','UNARCHIVED_JOBS'),(37,'Can view leaves','VIEW_LEAVES'),(38,'Can create leaves','CREATE_LEAVES'),(39,'Can cancel leaves','CANCEL_LEAVES'),(40,'Can view my leaves','VIEW_MY_LEAVES'),(41,'Can update leaves','UPDATE_LEAVES'),(42,'Can approve leaves','APPROVE_LEAVES'),(43,'Can reject leaves','REJECT_LEAVES'),(44,'Can archive leaves','ARCHIVE_LEAVES'),(45,'Can unarchived/restore leaves','UNARCHIVED_LEAVES'),(46,'Can view hcm dashboard','VIEW_DASHBOARD'),(47,'Can view attendances','VIEW_ATTENDANCES'),(48,'Can view my attendances','VIEW_MY_ATTENDANCES'),(49,'Can check in attendances','CHECK_IN_ATTENDANCES'),(50,'Can check out attendances','CHECK_OUT_ATTENDANCES'),(51,'Applicants can view open jobs','VIEW_OPEN_JOBS'),(52,'Applicants can apply jobs','APPLY_JOBS'),(53,'Applicants can view applied jobs','VIEW_APPLIED_JOBS'),(54,'Applicants can withdraw applied jobs','WITHDRAW_APPLIED_JOBS'),(55,'Applicants can viwe its profile','MY_APPLICANT_PROFILE'),(56,'Applicants can update its profile','UPDATE_APPLICANT_PROFILE'),(57,'Applicants can upload resume ','UPLOAD_RESUME'),(58,'Applicants can delete its own account','DELETE_ACCOUNT');
/*!40000 ALTER TABLE `permissions` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-11 22:05:21
