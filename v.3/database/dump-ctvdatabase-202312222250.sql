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
-- Table structure for table `blog_keys`
--

DROP TABLE IF EXISTS `blog_keys`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blog_keys` (
  `id` bigint NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `blog_key` varchar(12) NOT NULL,
  `frequency` smallint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `blog_key` (`blog_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blog_keys`
--

LOCK TABLES `blog_keys` WRITE;
/*!40000 ALTER TABLE `blog_keys` DISABLE KEYS */;
INSERT INTO `blog_keys` VALUES (2827416839292,'2023-12-22 22:27:53','2023-12-22 22:28:47','LTYTHSKT',6);
/*!40000 ALTER TABLE `blog_keys` ENABLE KEYS */;
UNLOCK TABLES;

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
  `blog_key` varchar(12) COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `major_img_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `rate` double DEFAULT NULL,
  `thumbnail` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `views` int DEFAULT NULL,
  `author_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `blog_key` (`blog_key`),
  KEY `FKt8g0udj2fq40771g38t2t011n` (`author_id`),
  CONSTRAINT `FKt8g0udj2fq40771g38t2t011n` FOREIGN KEY (`author_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blogs`
--

LOCK TABLES `blogs` WRITE;
/*!40000 ALTER TABLE `blogs` DISABLE KEYS */;
INSERT INTO `blogs` VALUES (44444445,'2023-12-04 00:00:00','2023-12-04 00:00:00','first_blog','day la blog dau tien',NULL,5,'day la thumbnail cua blog dau tien','day la title cua blog dau tien',1,4444),(2825679758368,'2023-12-22 21:59:37','2023-12-22 21:59:37','LTYTHSKT1','bai viet 2',NULL,5,'thumbnail cua bai viet 2','title cua bai viet 2',0,8888),(2825985909884,'2023-12-22 22:04:36','2023-12-22 22:04:36','LTYTHSKT2','bai viet 3',NULL,5,'thumbnail cua bai viet 3','title cua bai viet 3',0,8888),(2826092901240,'2023-12-22 22:06:20','2023-12-22 22:06:20','LTYTHSKT3','bai viet 4',NULL,5,'thumbnail cua bai viet 4','title cua bai viet 4',0,8888),(2826173473813,'2023-12-22 22:07:39','2023-12-22 22:07:39','LTYTHSKT4','bai viet 5',NULL,5,'thumbnail cua bai viet 5','title cua bai viet 5',0,8888),(2826341871624,'2023-12-22 22:10:23','2023-12-22 22:10:23','LTYTHSKT5','bai viet 6',NULL,5,'thumbnail cua bai viet 6','title cua bai viet 6',0,8888),(2827414699072,'2023-12-22 22:27:51','2023-12-22 22:27:51','LTYTHSKT6','bai viet 7',NULL,5,'thumbnail cua bai viet 7','title cua bai viet 7',0,8888);
/*!40000 ALTER TABLE `blogs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blogs_tags`
--

DROP TABLE IF EXISTS `blogs_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blogs_tags` (
  `id` bigint NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `blog_id` bigint DEFAULT NULL,
  `tag_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKx3fpgfooloan6hqxab1bik23` (`blog_id`),
  KEY `FK7tmdmuehqtjx4rs23fue0pluy` (`tag_id`),
  CONSTRAINT `FK7tmdmuehqtjx4rs23fue0pluy` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`),
  CONSTRAINT `FKx3fpgfooloan6hqxab1bik23` FOREIGN KEY (`blog_id`) REFERENCES `blogs` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blogs_tags`
--

LOCK TABLES `blogs_tags` WRITE;
/*!40000 ALTER TABLE `blogs_tags` DISABLE KEYS */;
INSERT INTO `blogs_tags` VALUES (2825679796288,'2023-12-22 00:00:00','2023-12-22 00:00:00',2825679758368,2023121906),(2825679797336,'2023-12-22 00:00:00','2023-12-22 00:00:00',2825679758368,2023121903),(2825985909824,'2023-12-22 00:00:00','2023-12-22 00:00:00',2825985909884,2023121906),(2825985910204,'2023-12-22 00:00:00','2023-12-22 00:00:00',2825985909884,2023121903),(2826092900476,'2023-12-22 00:00:00','2023-12-22 00:00:00',2826092901240,2023121903),(2826092900796,'2023-12-22 00:00:00','2023-12-22 00:00:00',2826092901240,2023121906),(2826173473880,'2023-12-22 00:00:00','2023-12-22 00:00:00',2826173473813,2023121903),(2826173474114,'2023-12-22 00:00:00','2023-12-22 00:00:00',2826173473813,2023121906),(2826341872704,'2023-12-22 00:00:00','2023-12-22 00:00:00',2826341871624,2023121906),(2826341873528,'2023-12-22 00:00:00','2023-12-22 00:00:00',2826341871624,2023121903),(2827414709280,'2023-12-22 00:00:00','2023-12-22 00:00:00',2827414699072,2023121906),(2827414709312,'2023-12-22 00:00:00','2023-12-22 00:00:00',2827414699072,2023121903);
/*!40000 ALTER TABLE `blogs_tags` ENABLE KEYS */;
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
  `content` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_read` int DEFAULT NULL,
  `target_id` bigint DEFAULT NULL,
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
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag_classes`
--

DROP TABLE IF EXISTS `tag_classes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tag_classes` (
  `id` bigint NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `detail` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `priority` tinyint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag_classes`
--

LOCK TABLES `tag_classes` WRITE;
/*!40000 ALTER TABLE `tag_classes` DISABLE KEYS */;
INSERT INTO `tag_classes` VALUES (2023121901,'2023-12-19 00:00:00','2023-12-19 00:00:00','phân loại theo mục đích bài viết',0),(2023121902,'2023-12-19 00:00:00','2023-12-19 00:00:00','phân loại theo hoạt động tình nguyện',1);
/*!40000 ALTER TABLE `tag_classes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tags`
--

DROP TABLE IF EXISTS `tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tags` (
  `id` bigint NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `key` varchar(8) NOT NULL,
  `detail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `class_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `tags_fk_tagclass` (`class_id`),
  CONSTRAINT `tags_fk_tagclass` FOREIGN KEY (`class_id`) REFERENCES `tag_classes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tags`
--

LOCK TABLES `tags` WRITE;
/*!40000 ALTER TABLE `tags` DISABLE KEYS */;
INSERT INTO `tags` VALUES (2023121903,'2023-12-19 00:00:00','2023-12-19 00:00:00','LTYT','lan tỏa yêu thương',2023121901),(2023121904,'2023-12-19 00:00:00','2023-12-19 00:00:00','STU','dạy học',2023121901),(2023121905,'2023-12-19 00:00:00','2023-12-19 00:00:00','TTNV','tuyển tình nguyện viên',2023121901),(2023121906,'2023-12-19 00:00:00','2023-12-19 00:00:00','HSKT','Giúp đỡ học sinh bị khiếm thị',2023121902),(2023121907,'2023-12-19 00:00:00','2023-12-19 00:00:00','HSVC','Giúp đỡ học sinh vùng cao',2023121902),(2023121908,'2023-12-19 00:00:00','2023-12-19 00:00:00','HCKK','Giúp đỡ các trường hợp có hoàn cảnh khó khăn',2023121902),(2023121909,'2023-12-19 00:00:00','2023-12-19 00:00:00','CDMDC','Giúp đỡ nạn nhân bị chất độc màu gia cam',2023121902);
/*!40000 ALTER TABLE `tags` ENABLE KEYS */;
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

-- Dump completed on 2023-12-22 22:50:15
