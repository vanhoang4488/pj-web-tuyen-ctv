-- MySQL dump 10.13  Distrib 8.1.0, for Win64 (x86_64)
--
-- Host: localhost    Database: ctvdatabase
-- ------------------------------------------------------
-- Server version	8.1.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `blogs`
--

DROP TABLE IF EXISTS `blogs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blogs` (
  `id` bigint NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `blog_key` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `content` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `major_img_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `rate` double DEFAULT NULL,
  `thumbnail` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `views` int DEFAULT NULL,
  `author_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKt8g0udj2fq40771g38t2t011n` (`author_id`),
  CONSTRAINT `FKt8g0udj2fq40771g38t2t011n` FOREIGN KEY (`author_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blogs`
--

LOCK TABLES `blogs` WRITE;
/*!40000 ALTER TABLE `blogs` DISABLE KEYS */;
INSERT INTO `blogs` VALUES (44444445,'2023-12-04 00:00:00','2023-12-04 00:00:00','first_blog','day la blog dau tien',NULL,5,'day la thumbnail cua blog dau tien','day la title cua blog dau tien',1,4444);
/*!40000 ALTER TABLE `blogs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comments` (
  `id` bigint NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `comment` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `comment_level` int DEFAULT NULL,
  `blog_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `parent_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9aakob3a7aghrm94k9kmbrjqd` (`blog_id`),
  KEY `FK8omq0tc18jd43bu5tjh6jvraq` (`user_id`),
  KEY `FKlri30okf66phtcgbe5pok7cc0` (`parent_id`),
  CONSTRAINT `FK8omq0tc18jd43bu5tjh6jvraq` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK9aakob3a7aghrm94k9kmbrjqd` FOREIGN KEY (`blog_id`) REFERENCES `blogs` (`id`),
  CONSTRAINT `FKlri30okf66phtcgbe5pok7cc0` FOREIGN KEY (`parent_id`) REFERENCES `comments` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments`
--

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
INSERT INTO `comments` VALUES (1234408665096,'2023-12-04 22:20:00','2023-12-04 22:20:00','comment1',0,44444445,8888,NULL),(1325929836408,'2023-12-05 23:09:40','2023-12-05 23:09:41','comment2',0,44444445,8888,NULL);
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comments_childen_comments`
--

DROP TABLE IF EXISTS `comments_childen_comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comments_childen_comments` (
  `comment_entity_id` bigint NOT NULL,
  `childen_comments_id` bigint NOT NULL,
  PRIMARY KEY (`comment_entity_id`,`childen_comments_id`),
  UNIQUE KEY `UK_b147h3xwx8wrkaaav5x0945q7` (`childen_comments_id`),
  CONSTRAINT `FK4m5jwyo9v26y7l713r1wwgg8k` FOREIGN KEY (`comment_entity_id`) REFERENCES `comments` (`id`),
  CONSTRAINT `FKkvjpa61ha125lb0e4bsuctr54` FOREIGN KEY (`childen_comments_id`) REFERENCES `comments` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments_childen_comments`
--

LOCK TABLES `comments_childen_comments` WRITE;
/*!40000 ALTER TABLE `comments_childen_comments` DISABLE KEYS */;
/*!40000 ALTER TABLE `comments_childen_comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `id` bigint NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `args` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `content` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_read` int DEFAULT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `source_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcg2172m06os5oa9siem1qaa22` (`source_id`),
  CONSTRAINT `FKcg2172m06os5oa9siem1qaa22` FOREIGN KEY (`source_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
INSERT INTO `notifications` VALUES (20231204,'2023-12-04 00:00:00','2023-12-04 00:00:00','{\'userIds\':[4444,44444444,44448888]}','thong bao dau tien',0,'title cua thong bao dau tien',8888),(20231205,'2023-12-05 00:00:00','2023-12-05 00:00:00','{\'userIds\':[4444,44444444,44448888]}','thong bao lan thu 16',0,'title cua thong bao lan thu 16',8888),(20231206,'2023-12-06 00:00:00','2023-12-06 00:00:00','[4444,44444444,44448888]','thong bao lan thu 44',0,'title thong bao lan thu 44',8888),(2023120402,'2023-12-04 00:00:00','2023-12-04 00:00:00','{\'userIds\':[4444,44444444,44448888]}','thong bao lan thu 2',0,'title cua thong bao lan thu 2',8888),(2023120403,'2023-12-04 00:00:00','2023-12-04 00:00:00','{\'userIds\':[4444,44444444,44448888]}','thong bao lan 3',0,'title cua thong bao lan 3',8888),(2023120404,'2023-12-04 00:00:00','2023-12-04 00:00:00','{\'userIds\':[4444,44444444,44448888]}','thong bao lan thu 4',0,'title cua thong bao lan thu 4',8888),(2023120405,'2023-12-04 00:00:00','2023-12-04 00:00:00','{\'userIds\':[4444,44444444,44448888]}','thong bao lan thu 5',0,'title cua thong bao lan thu 5',8888),(2023120406,'2023-12-04 00:00:00','2023-12-04 00:00:00','{\'userIds\':[4444,44444444,44448888]}','thong bao lan thu 6',0,'title cua thong bao lan thu 6',8888),(2023120407,'2023-12-04 00:00:00','2023-12-04 00:00:00','{\'userIds\':[4444,44444444,44448888]}','thong bao lan thu 7',0,'title cua thong bao lan thu 7',8888),(2023120408,'2023-12-04 00:00:00','2023-12-04 00:00:00','{\'userIds\':[4444,44444444,44448888]}','thong bao lan thu 8',0,'title cua thong bao lan thu 8',8888),(2023120409,'2023-12-04 00:00:00','2023-12-04 00:00:00','{\'userIds\':[4444,44444444,44448888]}','thong bao lan thu 9',0,'title cua thong bao lan thu 9',8888),(2023120410,'2023-12-04 00:00:00','2023-12-04 00:00:00','{\'userIds\':[4444,44444444,44448888]}','thong bao lan thu 10',0,'title cua thong bao lan thu 10',8888),(2023120411,'2023-12-04 00:00:00','2023-12-04 00:00:00','{\'userIds\':[4444,44444444,44448888]}','thong bao lan thu 11',0,'title cua thong bao lan thu 11',8888),(2023120412,'2023-12-04 00:00:00','2023-12-04 00:00:00','{\'userIds\':[4444,44444444,44448888]}','thong bao lan thu 12',0,'title cua thong bao lan thu 12',8888),(2023120413,'2023-12-04 00:00:00','2023-12-04 00:00:00','{\'userIds\':[4444,44444444,44448888]}','thong bao lan thu 13',0,'title cua thong bao lan thu 13',8888),(2023120414,'2023-12-04 00:00:00','2023-12-04 00:00:00','{\'userIds\':[4444,44444444,44448888]}','thong bao lan thu 14',0,'title cua thong bao lan thu 14',8888),(2023120415,'2023-12-04 00:00:00','2023-12-04 00:00:00','{\'userIds\':[4444,44444444,44448888]}','thong bao lan thu 15',0,'title cua thong bao lan thu 15',8888),(2023120502,'2023-12-05 00:00:00','2023-12-05 00:00:00','{\'userIds\':[4444,44444444,44448888]}','thong bao lan thu 17',0,'title cua thong bao lan thu 17',8888),(2023120503,'2023-12-05 00:00:00','2023-12-05 00:00:00','{\'userIds\':[4444,44444444,44448888]}','thong bao lan thu 18',0,'title cua thong bao lan thu 18',8888),(2023120504,'2023-12-05 00:00:00','2023-12-05 00:00:00','{\'userIds\':[4444,44444444,44448888]}','thong bao lan thu 19',0,'title cua thong bao lan thu 19',8888),(2023120505,'2023-12-05 00:00:00','2023-12-05 00:00:00','{\'userIds\':[4444,44444444,44448888]}','thong bao lan thu 20',0,'title cua thong bao lan thu 20',8888),(2023120506,'2023-12-05 00:00:00','2023-12-05 00:00:00','{\'userIds\':[4444,44444444,44448888]}','thong bao lan thu 21',0,'title cua thong bao lan thu 21',8888),(2023120507,'2023-12-05 00:00:00','2023-12-05 00:00:00','{\'userIds\':[4444,44444444,44448888]}','thong bao lan thu 22',0,'title cua thong bao lan thu 22',8888),(2023120508,'2023-12-05 00:00:00','2023-12-05 00:00:00','{\'userIds\':[4444,44444444,44448888]}','thong bao lan thu 23',0,'title cua thong bao lan thu 23',8888),(2023120509,'2023-12-05 00:00:00','2023-12-05 00:00:00','{\'userIds\':[4444,44444444,44448888]}','thong bao lan thu 24',0,'title cua thong bao lan thu 24',8888),(2023120510,'2023-12-05 00:00:00','2023-12-05 00:00:00','{\'userIds\':[4444,44444444,44448888]}','thong bao lan thu 25',0,'title cua thong bao lan thu 25',8888),(2023120511,'2023-12-05 00:00:00','2023-12-05 00:00:00','{\'userIds\':[4444,44444444,44448888]}','thong bao lan thu 26',0,'title cua thong bao lan thu 26',8888),(2023120512,'2023-12-05 00:00:00','2023-12-05 00:00:00','{\"userIds\":[4444,44444444,44448888]}','thong bao lan thu 27',0,'title cua thong bao lan thu 27',8888),(2023120513,'2023-12-05 00:00:00','2023-12-05 00:00:00','{\"userIds\":[4444,44444444,44448888]}','thong bao lan thu 28',0,'title cua thong bao lan thu 28',8888),(2023120514,'2023-12-05 00:00:00','2023-12-05 00:00:00','{\"userIds\":[4444,44444444,44448888]}','thong bao lan thu 29',0,'title cua thong bao lan thu 29',8888),(2023120515,'2023-12-05 00:00:00','2023-12-05 00:00:00','{[4444,44444444,44448888]}','thong bao lan thu 30',0,'title cua thong bao lan thu 30',8888),(2023120516,'2023-12-05 00:00:00','2023-12-05 00:00:00','{[4444,44444444,44448888]}','thong bao lan thu 31',0,'title cua thong bao lan thu 31',8888),(2023120517,'2023-12-05 00:00:00','2023-12-05 00:00:00','{[4444,44444444,44448888]}','thong bao lan thu 32',0,'title cua thong bao lan thu 32',8888),(2023120518,'2023-12-05 00:00:00','2023-12-05 00:00:00','[4444,44444444,44448888]','thong bao lan thu 33',0,'title cua thong bao lan thu 33',8888),(2023120519,'2023-12-05 00:00:00','2023-12-05 00:00:00','[4444,44444444,44448888]','thong bao lan thu 34',0,'title cua thong bao lan thu 34',8888),(2023120520,'2023-12-05 00:00:00','2023-12-05 00:00:00','[4444,44444444,44448888]','thong bao lan thu 35',0,'title cua thong bao lan thu 35',8888),(2023120521,'2023-12-05 00:00:00','2023-12-05 00:00:00','[4444,44444444,44448888]','thong bao lan thu 36',0,'title cua thong bao lan thu 36',8888),(2023120522,'2023-12-05 00:00:00','2023-12-05 00:00:00','[4444,44444444,44448888]','thong bao lan thu 37',0,'title cua thong bao lan thu 37',8888),(2023120523,'2023-12-05 00:00:00','2023-12-05 00:00:00','[4444,44444444,44448888]','thong bao lan thu 38',0,'title cua thong bao lan thu 38',8888),(2023120524,'2023-12-05 00:00:00','2023-12-05 00:00:00','[4444,44444444,44448888]','thong bao lan thu 39',0,'title cua thong bao lan thu 39',8888),(2023120540,'2023-12-05 00:00:00','2023-12-05 00:00:00','[4444,44444444,44448888]','thong bao lan thu 40',0,'title cua thong bao lan thu 40',8888),(2023120541,'2023-12-05 00:00:00','2023-12-05 00:00:00','[4444,44444444,44448888]','thong bao lan thu 41',0,'title cua thong bao lan thu 41',8888),(2023120542,'2023-12-05 00:00:00','2023-12-05 00:00:00','[4444,44444444,44448888]','thong bao lan thu 42',0,'title cua thong bao lan thu 42',8888),(2023120543,'2023-12-05 00:00:00','2023-12-05 00:00:00','[4444,44444444,44448888]','thong bao lan thu 43',0,'title cua thong bao lan thu 43',8888),(2023120544,'2023-12-05 00:00:00','2023-12-05 00:00:00','[4444,44444444,44448888]','thong bao lan thu 44',0,'title cua thong bao lan thu 44',8888),(2023120602,'2023-12-06 00:00:00','2023-12-06 00:00:00','[4444,44444444,44448888]','thong bao lan thu 46',0,'title thong bao lan thu 46',8888),(2023120603,'2023-12-06 00:00:00','2023-12-06 00:00:00','[4444,44444444,44448888]','thong bao lan thu 47',0,'title thong bao lan thu 47',8888);
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_notifications`
--

DROP TABLE IF EXISTS `user_notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_notifications` (
  `id` bigint NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `not_read_total` int DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9f86wonnl11hos1cuf5fibutl` (`user_id`),
  CONSTRAINT `FK9f86wonnl11hos1cuf5fibutl` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_notifications`
--

LOCK TABLES `user_notifications` WRITE;
/*!40000 ALTER TABLE `user_notifications` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `birth_day` datetime DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `full_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `gender` int DEFAULT NULL,
  `login_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (4444,'2023-12-01 00:00:00','2023-12-01 00:00:00','1999-10-07 00:00:00','hoangie2k62@gmail.com','vanhoang',0,'admin','123456'),(8888,'2023-11-29 00:00:00','2023-11-29 00:00:00','1999-10-07 00:00:00','hoangie2k62@gmail.com','vanhoang',0,'superadmin','123456'),(44444444,'2023-12-01 00:00:00','2023-12-01 00:00:00','1999-10-07 00:00:00','hoangie2k62@gmail.com','vanhoang',0,'vanhoang44','123456'),(44448888,'2023-12-01 00:00:00','2023-12-01 00:00:00','1999-10-07 00:00:00','hoangie2k62@gmail.com','vanhoang',0,'vanhoang48','123456');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'ctvdatabase'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-12-06 22:37:59
