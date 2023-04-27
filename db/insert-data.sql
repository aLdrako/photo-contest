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

-- Dumping data for table photo_contest.contests: ~11 rows (approximately)
/*!40000 ALTER TABLE `contests` DISABLE KEYS */;
INSERT INTO `contests` (`id`, `title`, `category_id`, `is_invitational`, `phase1`, `phase2`, `date_created`, `is_finished`) VALUES
                                                                                                                                (1, 'Cutest things :) ', 5, 0, '2023-04-12 21:00:00', '2023-04-12 23:00:00', '2023-04-10 00:00:00', 1),
                                                                                                                                (2, 'Animation of the Year', 7, 1, '2023-04-12 23:30:00', '2023-04-13 16:00:00', '2023-04-10 00:00:00', 1),
                                                                                                                                (3, 'Universe in Focus', 3, 1, '2023-04-16 18:30:00', '2023-04-17 08:30:00', '2023-04-10 00:00:00', 1),
                                                                                                                                (4, 'Indie Games', 6, 0, '2023-04-19 00:00:00', '2023-04-19 12:00:00', '2023-04-10 00:00:00', 1),
                                                                                                                                (5, 'Blockbuster Bonanza', 8, 0, '2023-04-20 12:00:00', '2023-04-20 23:00:00', '2023-04-10 00:00:00', 1),
                                                                                                                                (6, 'Toon Time Animation', 7, 1, '2023-04-21 20:00:00', '2023-04-22 06:00:00', '2023-04-10 00:00:00', 1),
                                                                                                                                (7, 'Majestic Landscapes', 12, 1, '2023-04-23 00:00:00', '2023-04-23 12:00:00', '2023-04-10 00:00:00', 1),
                                                                                                                                (10, 'Speed Demons', 2, 0, '2023-05-01 00:00:00', '2023-05-01 22:00:00', '2023-04-10 00:00:00', 0),
                                                                                                                                (11, 'Wild and Free', 5, 0, '2023-05-04 22:00:00', '2023-05-05 18:00:00', '2023-04-10 00:00:00', 0),
                                                                                                                                (12, 'Artistic Impressions', 10, 0, '2023-05-05 08:00:00', '2023-05-06 03:00:00', '2023-04-10 00:00:00', 0),
                                                                                                                                (13, 'Impressive Space', 3, 1, '2023-05-05 20:00:00', '2023-05-06 12:00:00', '2023-04-10 00:00:00', 0);
/*!40000 ALTER TABLE `contests` ENABLE KEYS */;

-- Dumping data for table photo_contest.contests_results: ~12 rows (approximately)
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
                                                                         (7, 7, 12);
/*!40000 ALTER TABLE `contests_results` ENABLE KEYS */;

-- Dumping data for table photo_contest.cover_photos: ~8 rows (approximately)
/*!40000 ALTER TABLE `cover_photos` DISABLE KEYS */;
INSERT INTO `cover_photos` (`contest_id`, `cover_photo`) VALUES
                                                             (1, 'https://alexgo.online/Projects/PhotoContest/photos/trhUPo9SSFemJzZ.jpg'),
                                                             (3, 'https://alexgo.online/Projects/PhotoContest/photos/zBhoTt2Y9Y578Z5.jpeg'),
                                                             (4, 'https://alexgo.online/Projects/PhotoContest/photos/HpNJANsiiphW1Fc.jpg'),
                                                             (6, 'https://alexgo.online/Projects/PhotoContest/photos/hffUhKS4gsbfj5d.jpg'),
                                                             (10, 'https://alexgo.online/Projects/PhotoContest/photos/OWvEjYte1jhOQZ9.jpg'),
                                                             (11, 'https://alexgo.online/Projects/PhotoContest/photos/vhiBxq1BIAvTdyn.jpg'),
                                                             (12, 'https://alexgo.online/Projects/PhotoContest/photos/frC5cEMJfrjbwF1.jpg'),
                                                             (13, 'https://alexgo.online/Projects/PhotoContest/photos/LyJrVSBRh4FvtMK.jpg');
/*!40000 ALTER TABLE `cover_photos` ENABLE KEYS */;

-- Dumping data for table photo_contest.juries: ~31 rows (approximately)
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
                                                   (13, 13);
/*!40000 ALTER TABLE `juries` ENABLE KEYS */;

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
                                                         (13, 10),
                                                         (13, 12),
                                                         (13, 14),
                                                         (13, 15);
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

-- Dumping data for table photo_contest.photos: ~19 rows (approximately)
/*!40000 ALTER TABLE `photos` DISABLE KEYS */;
INSERT INTO `photos` (`id`, `title`, `story`, `photo`, `user_id`, `contest_id`) VALUES
                                                                                    (1, 'Nice and cute dog :)', 'This dog can swim very well', 'https://alexgo.online/Projects/PhotoContest/photos/hxrbkbEYzEmHigF.jpg', 6, 1),
                                                                                    (2, 'Green green snake', 'This snake looks dangerous', 'https://alexgo.online/Projects/PhotoContest/photos/trhUPo9SSFemJzZ.jpg', 8, 1),
                                                                                    (3, 'Look I\'m Panda ', 'Panda eating bamboo', 'https://alexgo.online/Projects/PhotoContest/photos/GLHthjvgofeHpW0.jpg', 8, 11),
                                                                                    (4, 'Very cute bird :) ', 'Bird and the nature in macro', 'https://alexgo.online/Projects/PhotoContest/photos/u1qtpM9sfMx6JYd.jpg', 12, 11),
                                                                                    (5, 'Blue blue sea and sky', 'Sea in its cold colors', 'https://alexgo.online/Projects/PhotoContest/photos/tkPNCvlf3mXETUp.jpg', 6, 7),
                                                                                    (6, 'Trees trees trees', 'Trees in warm colors of sunset', 'https://alexgo.online/Projects/PhotoContest/photos/iahPbpHTt3QjA99.jpg', 14, 7),
                                                                                    (7, 'A long logn path', 'Long path in the mountains', 'https://alexgo.online/Projects/PhotoContest/photos/CdL2hkxfNfiJCSt.jpg', 10, 7),
                                                                                    (8, 'Samurai and a girl', 'Peaceful samurai with cursed sword', 'https://alexgo.online/Projects/PhotoContest/photos/k8qgINly13NOxgD.jpg', 12, 2),
                                                                                    (9, 'Revy\'s headshot :)', 'Revy with her big gun', 'https://alexgo.online/Projects/PhotoContest/photos/ynPgl7KTw6XKgTJ.jpg', 15, 2),
                                                                                    (10, 'Dark hole in space', 'Dark hole consuming the sun', 'https://alexgo.online/Projects/PhotoContest/photos/pnViYwmX94v3LJI.jpeg', 6, 3),
                                                                                    (11, 'Earth from space', 'View of Earth from space', 'https://alexgo.online/Projects/PhotoContest/photos/CE5QuK6oPUggOCm.jpg', 6, 13),
                                                                                    (12, 'Moon in full eclipse', 'A photo of a moon eclipse', 'https://alexgo.online/Projects/PhotoContest/photos/8YBn8pkZLTkRNNQ.jpg', 8, 13),
                                                                                    (13, 'The Death Stranding', 'Death squade stranding', 'https://alexgo.online/Projects/PhotoContest/photos/5YyYofht4btSSpX.jpg', 9, 4),
                                                                                    (14, 'The BioShok Game', 'A photo of a BioShok', 'https://alexgo.online/Projects/PhotoContest/photos/bSLCHTqTX2sBIG2.jpg', 14, 4),
                                                                                    (15, 'Pinguins from Madagascar', 'White and fluffy guys', 'https://alexgo.online/Projects/PhotoContest/photos/l8wVUDAO3UMo1uu.jpg', 14, 5),
                                                                                    (18, 'Lamborghini Essenza', 'Super cars in green colors', 'https://alexgo.online/Projects/PhotoContest/photos/epXZFKoYQx6d4pp.jpg', 7, 10),
                                                                                    (19, 'Lamborghini competition', 'Cars competition in sunset', 'https://alexgo.online/Projects/PhotoContest/photos/OWvEjYte1jhOQZ9.jpg', 15, 10),
                                                                                    (20, 'Abstraction in 3D', 'Abstraction in black and red colors', 'https://alexgo.online/Projects/PhotoContest/photos/dUhdAlL19OVn2Wj.jpg', 9, 12),
                                                                                    (21, 'Painted camel and a main', 'Painted camel and a main in warm colors', 'https://alexgo.online/Projects/PhotoContest/photos/ghlXipgyrGsoZnu.jpg', 10, 1);
/*!40000 ALTER TABLE `photos` ENABLE KEYS */;

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

-- Dumping data for table photo_contest.photos_scores: ~52 rows (approximately)
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
                                                                 (21, 13, 3);
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
                                                                                                                                (3, 'Reserved1', 'Reserved1', 'reserved1', 'reserved1@mail.com', 'reserved1', '2023-04-10 00:00:00', 1, 3),
                                                                                                                                (4, 'Reserved2', 'Reserved2', 'reserved2', 'reserved2@mail.com', 'reserved2', '2023-04-10 00:00:00', 1, 0),
                                                                                                                                (5, 'Reserved3', 'Reserved3', 'reserved3', 'reserved3@mail.com', 'reserved3', '2023-04-10 00:00:00', 1, 0),
                                                                                                                                (6, 'Alexandra', 'Silcock', 'silcock', 'silcock@dailynews.com', 'silcock', '2023-04-12 12:01:03', 3, 180),
                                                                                                                                (7, 'Margot', 'Rashleig', 'rashleig', 'rashleig@yahoo.com', 'rashleig', '2023-04-12 12:01:30', 4, 1502),
                                                                                                                                (8, 'Brian', 'Mussalli', 'mussalli', 'mussalli@skynet.com', 'mussalli', '2023-04-12 12:01:46', 4, 1206),
                                                                                                                                (9, 'Orion', 'Wahner', 'owahnert', 'owahnert@tinyurl.com', 'owahnert', '2023-04-12 12:02:01', 2, 56),
                                                                                                                                (10, 'Reed', 'Flynn', 'rflynn12', 'rflynn12@php.net', 'rflynn12', '2023-04-12 12:02:19', 3, 200),
                                                                                                                                (11, 'Deleted', 'Deleted', 'deleted', 'deleted@mail.com', 'deleted', '2023-04-12 12:03:00', 1, 0),
                                                                                                                                (12, 'Tester', 'Tester', 'tester', 'tester@mail.com', 'tester', '2023-04-12 12:03:45', 3, 310),
                                                                                                                                (13, 'Alex', 'Dimov', 'alexdimov', 'alexdimov@mail.com', 'alexdimov', '2023-04-12 12:29:55', 1, 0),
                                                                                                                                (14, 'Orion', 'Spacer', 'orispace', 'orispace@gmail.com', 'orispace', '2023-04-12 12:32:53', 3, 363),
                                                                                                                                (15, 'John', 'Smith', 'smith555', 'smith555@yahoo.com', 'smith555', '2023-04-12 12:34:13', 2, 34);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
