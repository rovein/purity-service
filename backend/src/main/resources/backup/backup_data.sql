-- MySQL dump 10.13  Distrib 8.0.23, for Linux (x86_64)
--
-- Host: d3y0lbg7abxmbuoi.chr7pe7iynqr.eu-west-1.rds.amazonaws.com    Database: nxakhcexfus69jy4
-- ------------------------------------------------------
-- Server version	8.0.20

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
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '';

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
INSERT INTO `address` VALUES (1,'Харків','Україна','10','string','string','Бакуліна'),(2,'Харків','Україна','35','string','string','Перемоги'),(3,'Харків','Україна','78','0','0','Пушкіна'),(4,'Харків','Україна','28','0','0','Перемоги'),(5,'Харків','Україна','28','0','0','Перемоги'),(6,'Харків','Україна','10','string','string','Бакуліна'),(7,'Харків','Україна','35','string','string','Перемоги');
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (1,'admin@gmail.com','$2a$10$ecGZcqzz2.BW884wo/6REuUyL/68oo4dJA66FliY.EYjPg5llaXZy','Rick','Sanchez',1);
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `cleaning_provider`
--

LOCK TABLES `cleaning_provider` WRITE;
/*!40000 ALTER TABLE `cleaning_provider` DISABLE KEYS */;
INSERT INTO `cleaning_provider` VALUES (2,'cleaning_provider@gmail.com','$2a$10$EMIOb8T3a7.dbidk5Mibaul/ygJocnRi0qfPuoRx2XQtmE9KVVNfm',NULL,'Cleaning_Provider','+380567843612',3,6),(7,'cleaning_provider1@gmail.com','$2a$10$fISpsEWlYmj8ybUkTkP0V.n8lIaFWGesI5NbTsI7E4i3FNfsZo64.',NULL,'ТОВ \"Надавач1\"','+380675446754',3,5);
/*!40000 ALTER TABLE `cleaning_provider` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `contract`
--

LOCK TABLES `contract` WRITE;
/*!40000 ALTER TABLE `contract` DISABLE KEYS */;
INSERT INTO `contract` VALUES (1,'2021-03-27 12:34:38.802000',1000,5,4),(2,'2021-03-28 12:34:38.802000',1000,5,4),(3,'2021-03-29 22:34:36.943000',1250,10,4),(4,'2021-03-25 09:50:37.114000',1250,10,4);
/*!40000 ALTER TABLE `contract` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (11),(11),(11),(11),(11);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `placement`
--

LOCK TABLES `placement` WRITE;
/*!40000 ALTER TABLE `placement` DISABLE KEYS */;
INSERT INTO `placement` VALUES (5,40,1,'2021-03-18 12:31:15.970000','Офіс',3,3),(10,50,1,'2021-03-24 22:32:45.019000','Склад',4,3);
/*!40000 ALTER TABLE `placement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `placement_owner`
--

LOCK TABLES `placement_owner` WRITE;
/*!40000 ALTER TABLE `placement_owner` DISABLE KEYS */;
INSERT INTO `placement_owner` VALUES (3,'placement_owner@gmail.com','$2a$10$dZLg6RntX/dUcS3qzTgrpeqE0fWZrGjMdXED9pkHmOtzP7By5LgJ.',NULL,'Placement_Owner','+380784538645',2,7),(6,'placement_owner1@gmail.com','$2a$10$/Lh/9dm2MZBVmSPZgXftcO1pSCc1y4XHKWbEJSi.EPCrDX9x3SS2q','2021-03-24 21:20:58.996000','ТОВ \"Приміщення\"','+380567844512',2,3);
/*!40000 ALTER TABLE `placement_owner` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `provider_service`
--

LOCK TABLES `provider_service` WRITE;
/*!40000 ALTER TABLE `provider_service` DISABLE KEYS */;
INSERT INTO `provider_service` VALUES (4,'Вологе прибирання приміщення',200,20,'Вологе прибирання',25,'Офіс',2);
/*!40000 ALTER TABLE `provider_service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'ADMIN'),(2,'PLACEMENT_OWNER'),(3,'CLEANING_PROVIDER');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `smart_device`
--

LOCK TABLES `smart_device` WRITE;
/*!40000 ALTER TABLE `smart_device` DISABLE KEYS */;
INSERT INTO `smart_device` VALUES (5,0.8,0,0,0,'string',0),(10,0,0,0,0,'null',0);
/*!40000 ALTER TABLE `smart_device` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-03-25 11:53:40
