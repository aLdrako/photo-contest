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

-- Dumping data for table photo_contest.categories: ~10 rows (approximately)
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` (`id`, `name`) VALUES
                                            (1, 'Nature'),
                                            (2, 'Cars'),
                                            (3, 'Space'),
                                            (4, 'Photography'),
                                            (5, 'Animals'),
                                            (6, 'Games'),
                                            (7, 'Animation'),
                                            (8, 'Movies'),
                                            (9, 'Sports'),
                                            (10, 'Abstract');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;

-- Dumping data for table photo_contest.contests: ~10 rows (approximately)
/*!40000 ALTER TABLE `contests` DISABLE KEYS */;
INSERT INTO `contests` (`id`, `title`, `category_id`, `is_invitational`, `phase1`, `phase2`, `date_created`) VALUES
                                                                                                                 (1, 'Speed Demons', 2, 0, '2023-05-01 00:00:00', '2023-05-01 23:59:59', '2023-04-12 12:43:44'),
                                                                                                                 (2, 'Majestic Landscapes', 1, 1, '2023-04-23 00:00:00', '2023-04-23 12:00:00', '2023-04-12 13:13:55'),
                                                                                                                 (3, 'Vintage Classics', 2, 0, '2023-04-27 03:00:00', '2023-04-27 23:59:59', '2023-04-12 13:16:13'),
                                                                                                                 (4, 'Universe in Focus', 3, 1, '2023-04-16 18:30:00', '2023-04-17 08:30:00', '2023-04-12 13:18:18'),
                                                                                                                 (5, 'Wild and Free', 5, 0, '2023-05-02 06:00:00', '2023-05-02 18:00:00', '2023-04-12 13:20:08'),
                                                                                                                 (6, 'Indie Games', 6, 0, '2023-04-19 00:00:00', '2023-04-19 12:00:00', '2023-04-12 13:21:18'),
                                                                                                                 (7, 'Toon Time Animation', 7, 1, '2023-04-22 15:00:00', '2023-04-23 06:00:00', '2023-04-12 13:25:07'),
                                                                                                                 (8, 'Blockbuster Bonanza', 8, 0, '2023-04-21 12:00:00', '2023-04-22 00:00:00', '2023-04-12 13:25:40'),
                                                                                                                 (9, 'Victory Lane', 9, 1, '2023-04-25 00:00:00', '2023-04-25 06:00:00', '2023-04-12 13:26:02'),
                                                                                                                 (10, 'Artistic Impressions', 10, 0, '2023-05-04 13:00:00', '2023-05-05 08:00:00', '2023-04-12 13:29:18');
/*!40000 ALTER TABLE `contests` ENABLE KEYS */;

-- Dumping data for table photo_contest.cover_photos: ~7 rows (approximately)
/*!40000 ALTER TABLE `cover_photos` DISABLE KEYS */;
INSERT INTO `cover_photos` (`contest_id`, `cover_photo`) VALUES
                                                             (1, 'https://alexgo.online/Projects/PhotoContest/photos/FjDdjW0pJNXYTVM.jpeg'),
                                                             (2, 'https://alexgo.online/Projects/PhotoContest/photos/laPzi3OEbu5PZPU.jpg'),
                                                             (4, 'https://alexgo.online/Projects/PhotoContest/photos/zBhoTt2Y9Y578Z5.jpeg'),
                                                             (5, 'https://alexgo.online/Projects/PhotoContest/photos/HQedRZBpiSpOtu3.jpg'),
                                                             (6, 'https://alexgo.online/Projects/PhotoContest/photos/HpNJANsiiphW1Fc.jpg'),
                                                             (7, 'https://alexgo.online/Projects/PhotoContest/photos/hffUhKS4gsbfj5d.jpg'),
                                                             (10, 'https://alexgo.online/Projects/PhotoContest/photos/Ylb3XbSrjtIt0yT.jpg');
/*!40000 ALTER TABLE `cover_photos` ENABLE KEYS */;

-- Dumping data for table photo_contest.juries: ~29 rows (approximately)
/*!40000 ALTER TABLE `juries` DISABLE KEYS */;
INSERT INTO `juries` (`contest_id`, `user_id`) VALUES
                                                   (1, 1),
                                                   (1, 13),
                                                   (2, 1),
                                                   (2, 7),
                                                   (2, 8),
                                                   (2, 12),
                                                   (2, 13),
                                                   (3, 1),
                                                   (3, 12),
                                                   (3, 13),
                                                   (4, 1),
                                                   (4, 7),
                                                   (4, 13),
                                                   (5, 1),
                                                   (5, 13),
                                                   (6, 1),
                                                   (6, 7),
                                                   (6, 8),
                                                   (6, 13),
                                                   (7, 1),
                                                   (7, 13),
                                                   (8, 1),
                                                   (8, 12),
                                                   (8, 13),
                                                   (9, 1),
                                                   (9, 12),
                                                   (9, 13),
                                                   (10, 1),
                                                   (10, 13);
/*!40000 ALTER TABLE `juries` ENABLE KEYS */;

-- Dumping data for table photo_contest.participants: ~33 rows (approximately)
/*!40000 ALTER TABLE `participants` DISABLE KEYS */;
INSERT INTO `participants` (`contest_id`, `user_id`) VALUES
                                                         (1, 10),
                                                         (1, 12),
                                                         (1, 14),
                                                         (1, 15),
                                                         (2, 6),
                                                         (2, 10),
                                                         (2, 15),
                                                         (3, 6),
                                                         (3, 10),
                                                         (3, 15),
                                                         (4, 10),
                                                         (4, 12),
                                                         (4, 15),
                                                         (5, 6),
                                                         (5, 7),
                                                         (5, 10),
                                                         (5, 15),
                                                         (6, 6),
                                                         (6, 9),
                                                         (6, 12),
                                                         (7, 6),
                                                         (7, 9),
                                                         (7, 12),
                                                         (7, 15),
                                                         (8, 6),
                                                         (8, 15),
                                                         (9, 6),
                                                         (9, 8),
                                                         (9, 9),
                                                         (9, 15),
                                                         (10, 6),
                                                         (10, 7),
                                                         (10, 10);
/*!40000 ALTER TABLE `participants` ENABLE KEYS */;

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

-- Dumping data for table photo_contest.photos: ~0 rows (approximately)
/*!40000 ALTER TABLE `photos` DISABLE KEYS */;
/*!40000 ALTER TABLE `photos` ENABLE KEYS */;

-- Dumping data for table photo_contest.photos_reviews_details: ~0 rows (approximately)
/*!40000 ALTER TABLE `photos_reviews_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `photos_reviews_details` ENABLE KEYS */;

-- Dumping data for table photo_contest.photos_scores: ~0 rows (approximately)
/*!40000 ALTER TABLE `photos_scores` DISABLE KEYS */;
/*!40000 ALTER TABLE `photos_scores` ENABLE KEYS */;

-- Dumping data for table photo_contest.rankings: ~4 rows (approximately)
/*!40000 ALTER TABLE `rankings` DISABLE KEYS */;
INSERT INTO `rankings` (`id`, `name`) VALUES
                                          (2, 'Enthusiast'),
                                          (1, 'Junkie'),
                                          (3, 'Master'),
                                          (4, 'Wise and Benevolent Photo Dictator');
/*!40000 ALTER TABLE `rankings` ENABLE KEYS */;

-- Dumping data for table photo_contest.users: ~15 rows (approximately)
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`id`, `first_name`, `last_name`, `username`, `email`, `password`, `join_date`, `ranking_id`, `points`) VALUES
                                                                                                                                (1, 'Admin', 'Admin', 'admin', 'admin@mail.com', 'admin', '2023-04-10 00:00:00', 1, 0),
                                                                                                                                (2, 'Anonymous', 'Anonymous', 'anonymous', 'anonymous@mail.com', 'anonymous', '2023-04-10 00:00:00', 1, 0),
                                                                                                                                (3, 'Reserved1', 'Reserved1', 'reserved1', 'reserved1@mail.com', 'reserved1', '2023-04-10 00:00:00', 1, 0),
                                                                                                                                (4, 'Reserved2', 'Reserved2', 'reserved2', 'reserved2@mail.com', 'reserved2', '2023-04-10 00:00:00', 1, 0),
                                                                                                                                (5, 'Reserved3', 'Reserved3', 'reserved3', 'reserved3@mail.com', 'reserved3', '2023-04-10 00:00:00', 1, 0),
                                                                                                                                (6, 'Alexandra', 'Silcock', 'asilcock0', 'asilcock0@dailynews.com', 'asilcock0', '2023-04-12 12:01:03', 1, 14),
                                                                                                                                (7, 'Margot', 'Rashleigh', 'mrashleigh1', 'mrashleigh1@yahoo.com', 'mrashleigh1', '2023-04-12 12:01:30', 4, 1502),
                                                                                                                                (8, 'Brian', 'Mussalli', 'bmussallij', 'bmussallij@skynet.com', 'bmussallij', '2023-04-12 12:01:46', 4, 1203),
                                                                                                                                (9, 'Orion', 'Wahner', 'owahnert', 'owahnert@tinyurl.com', 'owahnert', '2023-04-12 12:02:01', 1, 7),
                                                                                                                                (10, 'Reed', 'Flynn', 'rflynn12', 'rflynn12@php.net', 'rflynn12', '2023-04-12 12:02:19', 2, 110),
                                                                                                                                (11, 'Deleted', 'Deleted', 'deleted', 'deleted@mail.com', 'deleted', '2023-04-12 12:03:00', 1, 0),
                                                                                                                                (12, 'Tester', 'Tester', 'tester', 'tester@mail.com', 'tester', '2023-04-12 12:03:45', 3, 208),
                                                                                                                                (13, 'Alex', 'Dimov', 'alexdimov', 'alexdimov@mail.com', 'alexdimov', '2023-04-12 12:29:55', 1, 0),
                                                                                                                                (14, 'Orion', 'Spacer', 'orispace', 'orispace@gmail.com', 'orispace', '2023-04-12 12:32:53', 2, 150),
                                                                                                                                (15, 'John', 'Smith', 'smith555', 'smith555@yahoo.com', 'smith555', '2023-04-12 12:34:13', 1, 41);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
