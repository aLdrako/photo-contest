-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.11.1-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Dumping structure for table photo_contest.categories
CREATE TABLE IF NOT EXISTS `categories` (
                                            `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                            `name` varchar(16) NOT NULL,
                                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table photo_contest.categories: ~11 rows (approximately)
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` (`id`, `name`) VALUES
                                            (1, 'No Category'),
                                            (2, 'Cars'),
                                            (3, 'Space'),
                                            (4, 'Photography'),
                                            (5, 'Animals'),
                                            (6, 'Games'),
                                            (7, 'Animation'),
                                            (8, 'Movies'),
                                            (9, 'Sports'),
                                            (10, 'Abstract'),
                                            (12, 'Nature');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;

-- Dumping structure for table photo_contest.contests
CREATE TABLE IF NOT EXISTS `contests` (
                                          `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                          `title` varchar(50) NOT NULL,
                                          `category_id` bigint(20) NOT NULL,
                                          `is_invitational` tinyint(1) NOT NULL DEFAULT 0,
                                          `phase1` datetime NOT NULL,
                                          `phase2` datetime NOT NULL,
                                          `date_created` datetime NOT NULL DEFAULT current_timestamp(),
                                          `is_finished` tinyint(1) NOT NULL DEFAULT 0,
                                          PRIMARY KEY (`id`),
                                          UNIQUE KEY `contests_pk` (`title`),
                                          UNIQUE KEY `contests_pk2` (`title`),
                                          KEY `contests_categories_id_fk` (`category_id`),
                                          CONSTRAINT `contests_categories_id_fk` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table photo_contest.contests: ~14 rows (approximately)
/*!40000 ALTER TABLE `contests` DISABLE KEYS */;
INSERT INTO `contests` (`id`, `title`, `category_id`, `is_invitational`, `phase1`, `phase2`, `date_created`, `is_finished`) VALUES
                                                                                                                                (1, 'Cutest things :) ', 5, 0, '2023-04-12 21:00:00', '2023-04-12 23:00:00', '2023-04-10 00:00:00', 1),
                                                                                                                                (2, 'Animation of the Year', 7, 1, '2023-04-12 23:30:00', '2023-04-13 16:00:00', '2023-04-10 00:00:00', 1),
                                                                                                                                (3, 'Universe in Focus', 3, 1, '2023-04-16 18:30:00', '2023-04-17 08:30:00', '2023-04-10 00:00:00', 1),
                                                                                                                                (4, 'Indie Games', 6, 0, '2023-04-19 00:00:00', '2023-04-19 12:00:00', '2023-04-10 00:00:00', 1),
                                                                                                                                (5, 'Blockbuster Bonanza', 8, 0, '2023-04-20 12:00:00', '2023-04-20 23:00:00', '2023-04-10 00:00:00', 1),
                                                                                                                                (6, 'Toon Time Animation', 7, 1, '2023-04-21 20:00:00', '2023-04-22 06:00:00', '2023-04-10 00:00:00', 1),
                                                                                                                                (7, 'Majestic Landscapes', 12, 1, '2023-04-23 00:00:00', '2023-04-23 12:00:00', '2023-04-10 00:00:00', 1),
                                                                                                                                (10, 'Speed Demons', 2, 0, '2023-05-01 00:00:00', '2023-05-01 22:00:00', '2023-04-10 00:00:00', 1),
                                                                                                                                (11, 'Wild and Free', 5, 0, '2023-05-04 22:00:00', '2023-05-05 18:00:00', '2023-04-10 00:00:00', 0),
                                                                                                                                (12, 'Artistic Impressions', 10, 0, '2023-05-05 08:00:00', '2023-05-06 03:00:00', '2023-04-10 00:00:00', 0),
                                                                                                                                (13, 'Impressive Space', 3, 1, '2023-05-05 20:00:00', '2023-05-06 12:00:00', '2023-04-10 00:00:00', 0),
                                                                                                                                (14, 'Hot Riders', 2, 0, '2023-05-06 17:58:00', '2023-05-07 16:54:00', '2023-05-03 16:55:19', 0),
                                                                                                                                (15, 'Wildlife in its beauty', 5, 1, '2023-05-05 09:30:00', '2023-05-05 20:30:00', '2023-05-03 17:35:00', 0),
                                                                                                                                (16, 'Victory Lane', 9, 0, '2023-05-04 07:00:00', '2023-05-04 22:00:00', '2023-05-03 18:00:19', 0);
/*!40000 ALTER TABLE `contests` ENABLE KEYS */;

-- Dumping structure for table photo_contest.contests_results
CREATE TABLE IF NOT EXISTS `contests_results` (
                                                  `contest_id` bigint(20) NOT NULL,
                                                  `photo_id` bigint(20) NOT NULL,
                                                  `results` int(11) DEFAULT NULL,
                                                  PRIMARY KEY (`contest_id`,`photo_id`),
                                                  KEY `contests_results_photos_id_fk` (`photo_id`),
                                                  CONSTRAINT `contests_results_contests_id_fk` FOREIGN KEY (`contest_id`) REFERENCES `contests` (`id`),
                                                  CONSTRAINT `contests_results_photos_id_fk` FOREIGN KEY (`photo_id`) REFERENCES `photos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table photo_contest.contests_results: ~14 rows (approximately)
/*!40000 ALTER TABLE `contests_results` DISABLE KEYS */;
INSERT INTO `contests_results` (`contest_id`, `photo_id`, `results`) VALUES
                                                                         (1, 1, 20),
                                                                         (1, 2, 14),
                                                                         (1, 21, 7),
                                                                         (2, 8, 9),
                                                                         (2, 9, 9),
                                                                         (3, 10, 6),
                                                                         (4, 13, 9),
                                                                         (4, 14, 9),
                                                                         (5, 15, 15),
                                                                         (7, 5, 13),
                                                                         (7, 6, 6),
                                                                         (7, 7, 12),
                                                                         (10, 18, 9),
                                                                         (10, 19, 9);
/*!40000 ALTER TABLE `contests_results` ENABLE KEYS */;

-- Dumping structure for table photo_contest.cover_photos
CREATE TABLE IF NOT EXISTS `cover_photos` (
                                              `contest_id` bigint(20) NOT NULL,
                                              `cover_photo` varchar(1000) DEFAULT NULL,
                                              PRIMARY KEY (`contest_id`),
                                              CONSTRAINT `cover_photos_contests_fk` FOREIGN KEY (`contest_id`) REFERENCES `contests` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table photo_contest.cover_photos: ~12 rows (approximately)
/*!40000 ALTER TABLE `cover_photos` DISABLE KEYS */;
INSERT INTO `cover_photos` (`contest_id`, `cover_photo`) VALUES
                                                             (1, 'https://alexgo.online/Projects/PhotoContest/photos/trhUPo9SSFemJzZ.jpg'),
                                                             (3, 'https://alexgo.online/Projects/PhotoContest/photos/zBhoTt2Y9Y578Z5.jpeg'),
                                                             (4, 'https://alexgo.online/Projects/PhotoContest/photos/HpNJANsiiphW1Fc.jpg'),
                                                             (6, 'https://alexgo.online/Projects/PhotoContest/photos/hffUhKS4gsbfj5d.jpg'),
                                                             (7, 'https://alexgo.online/Projects/PhotoContest/photos/U0f4Ibrs75a975z.jpg'),
                                                             (10, 'https://alexgo.online/Projects/PhotoContest/photos/OWvEjYte1jhOQZ9.jpg'),
                                                             (11, 'https://alexgo.online/Projects/PhotoContest/photos/vhiBxq1BIAvTdyn.jpg'),
                                                             (12, 'https://alexgo.online/Projects/PhotoContest/photos/frC5cEMJfrjbwF1.jpg'),
                                                             (13, 'https://alexgo.online/Projects/PhotoContest/photos/LyJrVSBRh4FvtMK.jpg'),
                                                             (14, 'https://alexgo.online/Projects/PhotoContest/photos/eaJa6iJIKRbOm1S.jpg'),
                                                             (15, 'https://alexgo.online/Projects/PhotoContest/photos/dK7eHghtspueyKo.jpg'),
                                                             (16, 'https://alexgo.online/Projects/PhotoContest/photos/62uOCROtFv9Wh2K.jpg');
/*!40000 ALTER TABLE `cover_photos` ENABLE KEYS */;

-- Dumping structure for table photo_contest.juries
CREATE TABLE IF NOT EXISTS `juries` (
                                        `contest_id` bigint(20) NOT NULL,
                                        `user_id` bigint(20) NOT NULL,
                                        PRIMARY KEY (`contest_id`,`user_id`),
                                        KEY `juries_users_id_fk` (`user_id`),
                                        CONSTRAINT `juries_contests_fk` FOREIGN KEY (`contest_id`) REFERENCES `contests` (`id`),
                                        CONSTRAINT `juries_users_id_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table photo_contest.juries: ~40 rows (approximately)
/*!40000 ALTER TABLE `juries` DISABLE KEYS */;
INSERT INTO `juries` (`contest_id`, `user_id`) VALUES
                                                   (1, 1),
                                                   (1, 12),
                                                   (1, 13),
                                                   (2, 1),
                                                   (2, 7),
                                                   (2, 13),
                                                   (3, 1),
                                                   (3, 13),
                                                   (4, 1),
                                                   (4, 8),
                                                   (4, 13),
                                                   (5, 1),
                                                   (5, 7),
                                                   (5, 8),
                                                   (5, 13),
                                                   (6, 1),
                                                   (6, 7),
                                                   (6, 8),
                                                   (6, 13),
                                                   (7, 1),
                                                   (7, 13),
                                                   (10, 1),
                                                   (10, 8),
                                                   (10, 13),
                                                   (11, 1),
                                                   (11, 13),
                                                   (12, 1),
                                                   (12, 13),
                                                   (13, 1),
                                                   (13, 7),
                                                   (13, 13),
                                                   (14, 1),
                                                   (14, 13),
                                                   (15, 1),
                                                   (15, 7),
                                                   (15, 8),
                                                   (15, 13),
                                                   (16, 1),
                                                   (16, 8),
                                                   (16, 13);
/*!40000 ALTER TABLE `juries` ENABLE KEYS */;

-- Dumping structure for table photo_contest.participants
CREATE TABLE IF NOT EXISTS `participants` (
                                              `contest_id` bigint(20) NOT NULL,
                                              `user_id` bigint(20) NOT NULL,
                                              PRIMARY KEY (`contest_id`,`user_id`),
                                              KEY `participants_users_id_fk` (`user_id`),
                                              CONSTRAINT `participants_contests_id_fk` FOREIGN KEY (`contest_id`) REFERENCES `contests` (`id`),
                                              CONSTRAINT `participants_users_id_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table photo_contest.participants: ~44 rows (approximately)
/*!40000 ALTER TABLE `participants` DISABLE KEYS */;
INSERT INTO `participants` (`contest_id`, `user_id`) VALUES
                                                         (1, 6),
                                                         (1, 8),
                                                         (1, 9),
                                                         (1, 10),
                                                         (2, 12),
                                                         (2, 14),
                                                         (2, 15),
                                                         (3, 6),
                                                         (3, 12),
                                                         (3, 15),
                                                         (4, 9),
                                                         (4, 14),
                                                         (4, 15),
                                                         (5, 6),
                                                         (5, 9),
                                                         (5, 12),
                                                         (5, 14),
                                                         (5, 15),
                                                         (6, 3),
                                                         (6, 6),
                                                         (6, 10),
                                                         (6, 12),
                                                         (6, 14),
                                                         (7, 6),
                                                         (7, 10),
                                                         (7, 14),
                                                         (10, 7),
                                                         (10, 10),
                                                         (10, 12),
                                                         (10, 15),
                                                         (11, 8),
                                                         (11, 9),
                                                         (11, 10),
                                                         (11, 12),
                                                         (12, 7),
                                                         (12, 9),
                                                         (12, 10),
                                                         (12, 12),
                                                         (13, 6),
                                                         (13, 8),
                                                         (14, 6),
                                                         (14, 15),
                                                         (15, 9),
                                                         (16, 15);
/*!40000 ALTER TABLE `participants` ENABLE KEYS */;

-- Dumping structure for table photo_contest.permissions
CREATE TABLE IF NOT EXISTS `permissions` (
                                             `user_id` bigint(20) NOT NULL,
                                             `is_organizer` tinyint(1) NOT NULL DEFAULT 0,
                                             `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
                                             PRIMARY KEY (`user_id`),
                                             CONSTRAINT `permissions_users_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table photo_contest.permissions: ~15 rows (approximately)
/*!40000 ALTER TABLE `permissions` DISABLE KEYS */;
INSERT INTO `permissions` (`user_id`, `is_organizer`, `is_deleted`) VALUES
                                                                        (1, 1, 0),
                                                                        (2, 0, 0),
                                                                        (3, 0, 0),
                                                                        (4, 0, 0),
                                                                        (5, 0, 0),
                                                                        (6, 0, 0),
                                                                        (7, 0, 0),
                                                                        (8, 0, 0),
                                                                        (9, 0, 0),
                                                                        (10, 0, 0),
                                                                        (11, 0, 1),
                                                                        (12, 0, 0),
                                                                        (13, 1, 0),
                                                                        (14, 0, 0),
                                                                        (15, 0, 0);
/*!40000 ALTER TABLE `permissions` ENABLE KEYS */;

-- Dumping structure for table photo_contest.photos
CREATE TABLE IF NOT EXISTS `photos` (
                                        `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                        `title` varchar(50) NOT NULL,
                                        `story` varchar(8196) NOT NULL,
                                        `photo` varchar(100) NOT NULL,
                                        `user_id` bigint(20) NOT NULL,
                                        `contest_id` bigint(20) NOT NULL,
                                        PRIMARY KEY (`id`),
                                        KEY `photos_contests_id_fk` (`contest_id`),
                                        KEY `photos_users_fk` (`user_id`),
                                        CONSTRAINT `photos_contests_id_fk` FOREIGN KEY (`contest_id`) REFERENCES `contests` (`id`),
                                        CONSTRAINT `photos_users_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table photo_contest.photos: ~24 rows (approximately)
/*!40000 ALTER TABLE `photos` DISABLE KEYS */;
INSERT INTO `photos` (`id`, `title`, `story`, `photo`, `user_id`, `contest_id`) VALUES
                                                                                    (1, 'Nice and cute dog :)', 'This dog can swim very well', 'https://alexgo.online/Projects/PhotoContest/photos/hxrbkbEYzEmHigF.jpg', 6, 1),
                                                                                    (2, 'Green green snake', 'This snake looks dangerous', 'https://alexgo.online/Projects/PhotoContest/photos/trhUPo9SSFemJzZ.jpg', 8, 1),
                                                                                    (3, 'Look I\'m Panda ', 'Panda eating bamboo', 'https://alexgo.online/Projects/PhotoContest/photos/GLHthjvgofeHpW0.jpg', 8, 11),
                                                                                    (4, 'Very cute bird :) ', 'Bird and the nature in macro', 'https://alexgo.online/Projects/PhotoContest/photos/u1qtpM9sfMx6JYd.jpg', 12, 11),
                                                                                    (5, 'Blue sea in the storm', 'Sea in its cold colors', 'https://alexgo.online/Projects/PhotoContest/photos/ZUgK2rhUnzaYJWS.jpg', 6, 7),
                                                                                    (6, 'Trees in sunset...', 'Trees in warm colors of sunset', 'https://alexgo.online/Projects/PhotoContest/photos/5q2shzaIcQi0DI0.jpg', 14, 7),
                                                                                    (7, 'A long logn path', 'Long path in the mountains', 'https://alexgo.online/Projects/PhotoContest/photos/U0f4Ibrs75a975z.jpg', 10, 7),
                                                                                    (8, 'Samurai and a girl', 'Peaceful samurai with cursed sword', 'https://alexgo.online/Projects/PhotoContest/photos/k8qgINly13NOxgD.jpg', 12, 2),
                                                                                    (9, 'Revy\'s headshot :)', 'Revy with her big gun', 'https://alexgo.online/Projects/PhotoContest/photos/ynPgl7KTw6XKgTJ.jpg', 15, 2),
                                                                                    (10, 'Dark hole in space', 'Dark hole consuming the sun', 'https://alexgo.online/Projects/PhotoContest/photos/pnViYwmX94v3LJI.jpeg', 6, 3),
                                                                                    (11, 'Earth from space', 'View of Earth from space', 'https://alexgo.online/Projects/PhotoContest/photos/CE5QuK6oPUggOCm.jpg', 6, 13),
                                                                                    (12, 'Moon in full eclipse', 'A photo of a moon eclipse', 'https://alexgo.online/Projects/PhotoContest/photos/8YBn8pkZLTkRNNQ.jpg', 8, 13),
                                                                                    (13, 'The Death Stranding', 'Death squade stranding', 'https://alexgo.online/Projects/PhotoContest/photos/5YyYofht4btSSpX.jpg', 9, 4),
                                                                                    (14, 'The BioShok Game', 'A photo of a BioShok', 'https://alexgo.online/Projects/PhotoContest/photos/bSLCHTqTX2sBIG2.jpg', 14, 4),
                                                                                    (15, 'Pinguins from Madagascar', 'White and fluffy guys', 'https://alexgo.online/Projects/PhotoContest/photos/l8wVUDAO3UMo1uu.jpg', 14, 5),
                                                                                    (18, 'Lamborghini Essenza', 'Super cars in green colors', 'https://alexgo.online/Projects/PhotoContest/photos/4BvQbivwgfdfLpg.jpg', 7, 10),
                                                                                    (19, 'Lamborghini competition', 'Cars competition in sunset', 'https://alexgo.online/Projects/PhotoContest/photos/OWvEjYte1jhOQZ9.jpg', 15, 10),
                                                                                    (20, 'Abstraction in 3D', 'Abstraction in black and red colors', 'https://alexgo.online/Projects/PhotoContest/photos/dUhdAlL19OVn2Wj.jpg', 9, 12),
                                                                                    (21, 'Painted camel and a main', 'Painted camel and a main in warm colors', 'https://alexgo.online/Projects/PhotoContest/photos/ghlXipgyrGsoZnu.jpg', 10, 1),
                                                                                    (22, 'Abstract Fractals', 'Abstract Fractals made orange and black colors with gloomy effect', 'https://alexgo.online/Projects/PhotoContest/photos/cZOrkZxG6KiHQji.jpg', 7, 12),
                                                                                    (23, 'Red classic car on the beach', 'Hot-34 enjoying the beach', 'https://alexgo.online/Projects/PhotoContest/photos/HGvAhDdL6H3C1Um.jpg', 6, 14),
                                                                                    (24, 'Jaguar Sisters playing', 'Jaguar sisters are playing. Made this photo in zoo\r\n\r\n', 'https://alexgo.online/Projects/PhotoContest/photos/K487LAG1Pr19Ott.jpg', 9, 15),
                                                                                    (26, 'Young woman boxing', 'Young woman on boxing ring in dark colors', 'https://alexgo.online/Projects/PhotoContest/photos/OEl1bBqXGTB8Ifq.jpg', 15, 16),
                                                                                    (27, 'Futuristic Super Car', 'Concept of a futuristic abstract design hyper sport car', 'https://alexgo.online/Projects/PhotoContest/photos/kH1NUcSF41zKN5V.jpg', 15, 14);
/*!40000 ALTER TABLE `photos` ENABLE KEYS */;

-- Dumping structure for table photo_contest.photos_reviews_details
CREATE TABLE IF NOT EXISTS `photos_reviews_details` (
                                                        `photo_id` bigint(20) NOT NULL,
                                                        `jury_id` bigint(20) NOT NULL,
                                                        `comment` varchar(1024) NOT NULL,
                                                        `fits_category` tinyint(1) NOT NULL,
                                                        PRIMARY KEY (`photo_id`,`jury_id`),
                                                        KEY `photos_reviews_details_users_id_fk` (`jury_id`),
                                                        CONSTRAINT `photos_reviews_details_photos_id_fk` FOREIGN KEY (`photo_id`) REFERENCES `photos` (`id`),
                                                        CONSTRAINT `photos_reviews_details_users_id_fk` FOREIGN KEY (`jury_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table photo_contest.photos_reviews_details: ~7 rows (approximately)
/*!40000 ALTER TABLE `photos_reviews_details` DISABLE KEYS */;
INSERT INTO `photos_reviews_details` (`photo_id`, `jury_id`, `comment`, `fits_category`) VALUES
                                                                                             (1, 1, 'It seems like dog is having fun', 1),
                                                                                             (1, 12, 'This dog looks very cure in water', 1),
                                                                                             (2, 13, 'Very nice picture of a snake', 1),
                                                                                             (5, 1, 'Cool and refreshing colors, this picture deserves a good scoring', 1),
                                                                                             (7, 1, 'The colors and views are astonishing', 1),
                                                                                             (21, 1, 'It\'s still an animal on the photo', 1),
                                                                                             (21, 12, 'Does not belong to this category', 0);
/*!40000 ALTER TABLE `photos_reviews_details` ENABLE KEYS */;

-- Dumping structure for table photo_contest.photos_scores
CREATE TABLE IF NOT EXISTS `photos_scores` (
                                               `photo_id` bigint(20) NOT NULL,
                                               `jury_id` bigint(20) NOT NULL,
                                               `score` int(11) NOT NULL DEFAULT 3,
                                               PRIMARY KEY (`photo_id`,`jury_id`),
                                               KEY `photos_scores_users_id_fk` (`jury_id`),
                                               CONSTRAINT `photos_scores_photos_id_fk` FOREIGN KEY (`photo_id`) REFERENCES `photos` (`id`),
                                               CONSTRAINT `photos_scores_users_id_fk` FOREIGN KEY (`jury_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table photo_contest.photos_scores: ~65 rows (approximately)
/*!40000 ALTER TABLE `photos_scores` DISABLE KEYS */;
INSERT INTO `photos_scores` (`photo_id`, `jury_id`, `score`) VALUES
                                                                 (1, 1, 8),
                                                                 (1, 12, 9),
                                                                 (1, 13, 3),
                                                                 (2, 1, 3),
                                                                 (2, 12, 3),
                                                                 (2, 13, 8),
                                                                 (3, 1, 3),
                                                                 (3, 13, 3),
                                                                 (4, 1, 3),
                                                                 (4, 13, 3),
                                                                 (5, 1, 10),
                                                                 (5, 13, 3),
                                                                 (6, 1, 3),
                                                                 (6, 13, 3),
                                                                 (7, 1, 9),
                                                                 (7, 13, 3),
                                                                 (8, 1, 3),
                                                                 (8, 7, 3),
                                                                 (8, 13, 3),
                                                                 (9, 1, 3),
                                                                 (9, 7, 3),
                                                                 (9, 13, 3),
                                                                 (10, 1, 3),
                                                                 (10, 13, 3),
                                                                 (11, 1, 3),
                                                                 (11, 7, 3),
                                                                 (11, 13, 3),
                                                                 (12, 1, 3),
                                                                 (12, 7, 3),
                                                                 (12, 13, 3),
                                                                 (13, 1, 3),
                                                                 (13, 8, 3),
                                                                 (13, 13, 3),
                                                                 (14, 1, 3),
                                                                 (14, 8, 3),
                                                                 (14, 13, 3),
                                                                 (15, 1, 3),
                                                                 (15, 7, 3),
                                                                 (15, 8, 3),
                                                                 (15, 12, 3),
                                                                 (15, 13, 3),
                                                                 (18, 1, 3),
                                                                 (18, 8, 3),
                                                                 (18, 13, 3),
                                                                 (19, 1, 3),
                                                                 (19, 8, 3),
                                                                 (19, 13, 3),
                                                                 (20, 1, 3),
                                                                 (20, 13, 3),
                                                                 (21, 1, 4),
                                                                 (21, 12, 0),
                                                                 (21, 13, 3),
                                                                 (22, 1, 3),
                                                                 (22, 13, 3),
                                                                 (23, 1, 3),
                                                                 (23, 13, 3),
                                                                 (24, 1, 3),
                                                                 (24, 7, 3),
                                                                 (24, 8, 3),
                                                                 (24, 13, 3),
                                                                 (26, 1, 3),
                                                                 (26, 8, 3),
                                                                 (26, 13, 3),
                                                                 (27, 1, 3),
                                                                 (27, 13, 3);
/*!40000 ALTER TABLE `photos_scores` ENABLE KEYS */;

-- Dumping structure for table photo_contest.rankings
CREATE TABLE IF NOT EXISTS `rankings` (
                                          `id` int(11) NOT NULL AUTO_INCREMENT,
                                          `name` varchar(50) NOT NULL,
                                          PRIMARY KEY (`id`),
                                          UNIQUE KEY `rankings_pk2` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table photo_contest.rankings: ~4 rows (approximately)
/*!40000 ALTER TABLE `rankings` DISABLE KEYS */;
INSERT INTO `rankings` (`id`, `name`) VALUES
                                          (2, 'Enthusiast'),
                                          (1, 'Junkie'),
                                          (3, 'Master'),
                                          (4, 'Wise and Benevolent Photo Dictator');
/*!40000 ALTER TABLE `rankings` ENABLE KEYS */;

-- Dumping structure for table photo_contest.users
CREATE TABLE IF NOT EXISTS `users` (
                                       `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                       `first_name` varchar(32) NOT NULL,
                                       `last_name` varchar(32) NOT NULL,
                                       `username` varchar(50) NOT NULL,
                                       `email` varchar(50) NOT NULL,
                                       `password` varchar(100) NOT NULL,
                                       `join_date` datetime DEFAULT current_timestamp(),
                                       `ranking_id` int(11) NOT NULL DEFAULT 1,
                                       `points` int(11) NOT NULL DEFAULT 0,
                                       PRIMARY KEY (`id`),
                                       UNIQUE KEY `users_pk2` (`username`),
                                       UNIQUE KEY `users_pk3` (`email`),
                                       KEY `users_rankings_id_fk` (`ranking_id`),
                                       CONSTRAINT `users_rankings_id_fk` FOREIGN KEY (`ranking_id`) REFERENCES `rankings` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table photo_contest.users: ~15 rows (approximately)
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`id`, `first_name`, `last_name`, `username`, `email`, `password`, `join_date`, `ranking_id`, `points`) VALUES
                                                                                                                                (1, 'Admin', 'Admin', 'admin', 'admin@mail.com', '$2a$10$ylhotx8CZBhi9xh/J84djeBAsmARXlzuR4YsEzMS91dz0fqHSM1Hq', '2023-04-10 00:00:00', 1, 0),
                                                                                                                                (2, 'Anonymous', 'Anonymous', 'anonymous', 'anonymous@mail.com', '$2a$10$ylhotx8CZBhi9xh/J84djeBAsmARXlzuR4YsEzMS91dz0fqHSM1Hq', '2023-04-10 00:00:00', 1, 0),
                                                                                                                                (3, 'Reserved1', 'Reserved1', 'reserved1', 'reserved1@mail.com', '$2a$10$8Np73OPF5lTIJI9SmhyAKu26f6TolHLu5gda0BqRO.tSzygPDsn4W', '2023-04-10 00:00:00', 1, 0),
                                                                                                                                (4, 'Reserved2', 'Reserved2', 'reserved2', 'reserved2@mail.com', '$2a$10$/QjvkWIVsrc1OB1q.idnkOVBOA2nGnxaLnQcnX.IBWfVv2sm6vGwy', '2023-04-10 00:00:00', 1, 0),
                                                                                                                                (5, 'Reserved3', 'Reserved3', 'reserved3', 'reserved3@mail.com', '$2a$10$g30M3YY7K.PbO8s4HiTeien2HVLrqFThG0B/V7IhXADOVquv2xmaS', '2023-04-10 00:00:00', 1, 0),
                                                                                                                                (6, 'Alexandra', 'Silcock', 'silcock', 'silcock@dailynews.com', '$2a$10$WBlyvmo/zA0FLrysYHMd6.dc9xEIdIxz9SlDHGDmb8MQiWrn7LyNy', '2023-04-12 12:01:03', 3, 561),
                                                                                                                                (7, 'Margot', 'Rashleig', 'rashleig', 'rashleig@yahoo.com', '$2a$10$zstBADfN7ypKqX0BsaK0qO3mwb1/ukvIC7/viC.Ce.JBBua9OSzNe', '2023-04-12 12:01:30', 4, 1542),
                                                                                                                                (8, 'Brian', 'Mussalli', 'mussalli', 'mussalli@skynet.com', '$2a$10$cQ1UfcnZgSr880DKJosGm.GV6KhJno7W/bSBZJoC6CBJn4JJjhOk2', '2023-04-12 12:01:46', 4, 1356),
                                                                                                                                (9, 'Orion', 'Wahner', 'owahnert', 'owahnert@tinyurl.com', '$2a$10$pACmP9yNHvo77b/.7g752.Oen7n2C/j9HXKnBDxeXmf5G.V6lZtUG', '2023-04-12 12:02:01', 2, 139),
                                                                                                                                (10, 'Reed', 'Flynn', 'rflynn12', 'rflynn12@php.net', '$2a$10$mjsvvabG3jLSkzIJpZFWqOaubumwtAz1Yzlfjru2nWcNzvobPICKO', '2023-04-12 12:02:19', 2, 75),
                                                                                                                                (11, 'Deleted', 'Deleted', 'deleted', 'deleted@mail.com', '$2a$10$OCd35EDz4gkWmv3ovx209ekNxIScst9765rHc9n9CUrs8ghB5Mu5i', '2023-04-12 12:03:00', 1, 0),
                                                                                                                                (12, 'Tester', 'Tester', 'tester', 'tester@mail.com', '$2a$10$zOWStD9aOERmoMNjHyVGDeiFbbifRq8OouJg.h5bUC6ZEKH6Win5C', '2023-04-12 12:03:45', 3, 390),
                                                                                                                                (13, 'Alex', 'Dimov', 'alexdimov', 'alexdimov@mail.com', '$2a$10$UYh.a1k6cFp7lDj0MJQ2suGgNYygeEp4/YEjeB2AAWvwrNxHtRz7C', '2023-04-12 12:29:55', 1, 0),
                                                                                                                                (14, 'Orion', 'Spacer', 'orispace', 'orispace@gmail.com', '$2a$10$XSJB8b3RB0d8OvC85.uug.1c7i8bwECmvp11Ez1PGGHVbpvk84NBC', '2023-04-12 12:32:53', 3, 584),
                                                                                                                                (15, 'John', 'Smith', 'smith555', 'smith555@yahoo.com', '$2a$10$.0CXCO.alH2AH0JnJdN33.YwTcAJze3J/I1dzxKHX4W7th4/Zcey6', '2023-04-12 12:34:13', 3, 156);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;

-- Dumping structure for table photo_contest.winners
CREATE TABLE IF NOT EXISTS `winners` (
                                         `contest_id` bigint(20) NOT NULL,
                                         `user_id` bigint(20) NOT NULL,
                                         PRIMARY KEY (`contest_id`,`user_id`),
                                         KEY `winners_users_id_fk` (`user_id`),
                                         CONSTRAINT `winners_contests_id_fk` FOREIGN KEY (`contest_id`) REFERENCES `contests` (`id`),
                                         CONSTRAINT `winners_users_id_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table photo_contest.winners: ~10 rows (approximately)
/*!40000 ALTER TABLE `winners` DISABLE KEYS */;
INSERT INTO `winners` (`contest_id`, `user_id`) VALUES
                                                    (1, 6),
                                                    (2, 12),
                                                    (2, 15),
                                                    (3, 6),
                                                    (4, 9),
                                                    (4, 14),
                                                    (5, 14),
                                                    (7, 6),
                                                    (10, 7),
                                                    (10, 15);
/*!40000 ALTER TABLE `winners` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
