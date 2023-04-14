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

-- Dumping data for table photo_contest.contests: ~13 rows (approximately)
/*!40000 ALTER TABLE `contests` DISABLE KEYS */;
INSERT INTO `contests` (`id`, `title`, `category_id`, `is_invitational`, `phase1`, `phase2`, `date_created`, `is_finished`) VALUES
                                                                                                                                (1, 'Cutest things', 5, 0, '2023-04-12 21:00:00', '2023-04-12 23:00:00', '2023-04-10 00:00:00', 1),
                                                                                                                                (2, 'Anime of the Year', 7, 1, '2023-04-12 23:30:00', '2023-04-13 16:00:00', '2023-04-10 00:00:00', 1),
                                                                                                                                (3, 'Universe in Focus', 3, 1, '2023-04-16 18:30:00', '2023-04-17 08:30:00', '2023-04-10 00:00:00', 0),
                                                                                                                                (4, 'Indie Games', 6, 0, '2023-04-19 00:00:00', '2023-04-19 12:00:00', '2023-04-10 00:00:00', 0),
                                                                                                                                (5, 'Blockbuster Bonanza', 8, 0, '2023-04-21 12:00:00', '2023-04-22 00:00:00', '2023-04-10 00:00:00', 0),
                                                                                                                                (6, 'Toon Time Animation', 7, 1, '2023-04-22 15:00:00', '2023-04-23 06:00:00', '2023-04-10 00:00:00', 0),
                                                                                                                                (7, 'Majestic Landscapes', 1, 1, '2023-04-23 00:00:00', '2023-04-23 12:00:00', '2023-04-10 00:00:00', 0),
                                                                                                                                (8, 'Victory Lane', 9, 1, '2023-04-25 00:00:00', '2023-04-25 06:00:00', '2023-04-10 00:00:00', 0),
                                                                                                                                (9, 'Vintage Classics', 2, 0, '2023-04-27 03:00:00', '2023-04-27 23:00:00', '2023-04-10 00:00:00', 0),
                                                                                                                                (10, 'Speed Demons', 2, 0, '2023-05-01 00:00:00', '2023-05-01 22:00:00', '2023-04-10 00:00:00', 0),
                                                                                                                                (11, 'Wild and Free', 5, 0, '2023-05-02 06:00:00', '2023-05-02 18:00:00', '2023-04-10 00:00:00', 0),
                                                                                                                                (12, 'Artistic Impressions', 10, 0, '2023-05-04 13:00:00', '2023-05-05 08:00:00', '2023-04-10 00:00:00', 0),
                                                                                                                                (13, 'Impressive Space', 3, 0, '2023-05-05 09:00:00', '2023-05-06 06:00:00', '2023-04-10 00:00:00', 0);
/*!40000 ALTER TABLE `contests` ENABLE KEYS */;

-- Dumping data for table photo_contest.contests_results: ~5 rows (approximately)
/*!40000 ALTER TABLE `contests_results` DISABLE KEYS */;
INSERT INTO `contests_results` (`contest_id`, `photo_id`, `results`) VALUES
                                                                         (1, 1, 20),
                                                                         (1, 2, 14),
                                                                         (1, 21, 7),
                                                                         (2, 8, 9),
                                                                         (2, 9, 9);
/*!40000 ALTER TABLE `contests_results` ENABLE KEYS */;

-- Dumping data for table photo_contest.cover_photos: ~7 rows (approximately)
/*!40000 ALTER TABLE `cover_photos` DISABLE KEYS */;
INSERT INTO `cover_photos` (`contest_id`, `cover_photo`) VALUES
                                                             (3, 'https://alexgo.online/Projects/PhotoContest/photos/zBhoTt2Y9Y578Z5.jpeg'),
                                                             (4, 'https://alexgo.online/Projects/PhotoContest/photos/HpNJANsiiphW1Fc.jpg'),
                                                             (6, 'https://alexgo.online/Projects/PhotoContest/photos/hffUhKS4gsbfj5d.jpg'),
                                                             (7, 'https://alexgo.online/Projects/PhotoContest/photos/laPzi3OEbu5PZPU.jpg'),
                                                             (10, 'https://alexgo.online/Projects/PhotoContest/photos/FjDdjW0pJNXYTVM.jpeg'),
                                                             (11, 'https://alexgo.online/Projects/PhotoContest/photos/HQedRZBpiSpOtu3.jpg'),
                                                             (12, 'https://alexgo.online/Projects/PhotoContest/photos/Ylb3XbSrjtIt0yT.jpg');
/*!40000 ALTER TABLE `cover_photos` ENABLE KEYS */;

-- Dumping data for table photo_contest.juries: ~36 rows (approximately)
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
                                                   (5, 12),
                                                   (5, 13),
                                                   (6, 1),
                                                   (6, 8),
                                                   (6, 13),
                                                   (7, 1),
                                                   (7, 13),
                                                   (8, 1),
                                                   (8, 12),
                                                   (8, 13),
                                                   (9, 1),
                                                   (9, 7),
                                                   (9, 13),
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

-- Dumping data for table photo_contest.participants: ~47 rows (approximately)
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
                                                         (5, 14),
                                                         (5, 15),
                                                         (6, 6),
                                                         (6, 10),
                                                         (6, 14),
                                                         (7, 6),
                                                         (7, 10),
                                                         (7, 14),
                                                         (8, 6),
                                                         (8, 10),
                                                         (8, 15),
                                                         (9, 6),
                                                         (9, 9),
                                                         (9, 10),
                                                         (9, 14),
                                                         (9, 15),
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

-- Dumping data for table photo_contest.photos: ~21 rows (approximately)
/*!40000 ALTER TABLE `photos` DISABLE KEYS */;
INSERT INTO `photos` (`id`, `title`, `story`, `photo`, `user_id`, `contest_id`) VALUES
                                                                                    (1, 'Nice and cure dog :)', 'This dog can swim very well', 'https://alexgo.online/Projects/PhotoContest/photos/xrP98b4IS7Gn9dZ.jpg', 6, 1),
                                                                                    (2, 'Green green snake', 'This snake looks dangerous', 'https://alexgo.online/Projects/PhotoContest/photos/Jf0DLzaKZ1Oos8E.jpg', 8, 1),
                                                                                    (3, 'Serious cat in glasses :) ', 'This cat has something to say', 'https://alexgo.online/Projects/PhotoContest/photos/wmEXxoYNGRQ1nv9.jpg', 8, 11),
                                                                                    (4, 'Very cute bird :) ', 'Bird and the nature in macro', 'https://alexgo.online/Projects/PhotoContest/photos/R92SkAsckf4Rryn.jpg', 12, 11),
                                                                                    (5, 'Blue blue sea and sky', 'Sea in its cold colors', 'https://alexgo.online/Projects/PhotoContest/photos/tkPNCvlf3mXETUp.jpg', 6, 7),
                                                                                    (6, 'Trees trees trees', 'Trees in warm colors of sunset', 'https://alexgo.online/Projects/PhotoContest/photos/iahPbpHTt3QjA99.jpg', 14, 7),
                                                                                    (7, 'A long logn path', 'Long path in the mountains', 'https://alexgo.online/Projects/PhotoContest/photos/CdL2hkxfNfiJCSt.jpg', 10, 7),
                                                                                    (8, 'Samurai and a girl', 'Peaceful samurai with cursed sword', 'https://alexgo.online/Projects/PhotoContest/photos/nAnFNYHYISiZcTo.jpg', 12, 2),
                                                                                    (9, 'Revy\'s headshot :)', 'Revy with her big gun', 'https://alexgo.online/Projects/PhotoContest/photos/ynPgl7KTw6XKgTJ.jpg', 15, 2),
                                                                                    (10, 'Dark hole in space', 'Dark hole consuming the sun', 'https://alexgo.online/Projects/PhotoContest/photos/pnViYwmX94v3LJI.jpeg', 6, 3),
                                                                                    (11, 'Earth from space', 'View of Earth from space', 'https://alexgo.online/Projects/PhotoContest/photos/NVvwZqCKE79DTq4.jpg', 6, 13),
                                                                                    (12, 'Moon in full eclipse', 'A photo of a moon eclipse', 'https://alexgo.online/Projects/PhotoContest/photos/23FF0EYIOMkhDKB.jpg', 8, 13),
                                                                                    (13, 'The Death Stranding', 'Death squade stranding', 'https://alexgo.online/Projects/PhotoContest/photos/5YyYofht4btSSpX.jpg', 9, 4),
                                                                                    (14, 'The BioShok Game', 'A photo of a BioShok', 'https://alexgo.online/Projects/PhotoContest/photos/bSLCHTqTX2sBIG2.jpg', 14, 4),
                                                                                    (15, 'Pinguins from Madagascar', 'White and fluffy guys', 'https://alexgo.online/Projects/PhotoContest/photos/l8wVUDAO3UMo1uu.jpg', 14, 5),
                                                                                    (16, 'Butterfly Stroke', 'Man doing butterfly stroke', 'https://alexgo.online/Projects/PhotoContest/photos/cWVl6Cv2gdOFuQp.jpg', 10, 8),
                                                                                    (17, 'Red and black car', 'A vintage car in red and black colors', 'https://alexgo.online/Projects/PhotoContest/photos/RrLIUoKTurc5g7l.jpg', 15, 9),
                                                                                    (18, 'Lamborghini Essenza', 'Super cars in green colors', 'https://alexgo.online/Projects/PhotoContest/photos/epXZFKoYQx6d4pp.jpg', 7, 10),
                                                                                    (19, 'Lamborghini competition', 'Cars competition in sunset', 'https://alexgo.online/Projects/PhotoContest/photos/OyHXYtM8HFuKm6e.jpg', 15, 10),
                                                                                    (20, 'Abstraction in 3D', 'Abstraction in black and red colors', 'https://alexgo.online/Projects/PhotoContest/photos/dUhdAlL19OVn2Wj.jpg', 9, 12),
                                                                                    (21, 'Painted camel and a main', 'Painted camel and a main in warm colors', 'https://alexgo.online/Projects/PhotoContest/photos/4KIOROkLIwHxakA.jpg', 10, 1);
/*!40000 ALTER TABLE `photos` ENABLE KEYS */;

-- Dumping data for table photo_contest.photos_reviews_details: ~4 rows (approximately)
/*!40000 ALTER TABLE `photos_reviews_details` DISABLE KEYS */;
INSERT INTO `photos_reviews_details` (`photo_id`, `jury_id`, `comment`, `fits_category`) VALUES
                                                                                             (1, 1, 'It seems like dog is having fun', 1),
                                                                                             (1, 12, 'This dog looks very cure in water', 1),
                                                                                             (2, 13, 'Very nice picture of a snake', 1),
                                                                                             (21, 1, 'It\'s still an animal on the photo', 1),
                                                                                             (21, 12, 'Does not belong to this category', 0);
/*!40000 ALTER TABLE `photos_reviews_details` ENABLE KEYS */;

-- Dumping data for table photo_contest.photos_scores: ~53 rows (approximately)
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
                                                                 (5, 1, 3),
                                                                 (5, 13, 3),
                                                                 (6, 1, 3),
                                                                 (6, 13, 3),
                                                                 (7, 1, 3),
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
                                                                 (16, 1, 3),
                                                                 (16, 12, 3),
                                                                 (16, 13, 3),
                                                                 (17, 1, 3),
                                                                 (17, 7, 3),
                                                                 (17, 13, 3),
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
                                          (4, 'Wise and Benevolent Photo Dictator'),
                                          (5, 'WISE_AND_BENEVOLENT_PHOTO_DICTATOR');
/*!40000 ALTER TABLE `rankings` ENABLE KEYS */;

-- Dumping data for table photo_contest.users: ~15 rows (approximately)
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`id`, `first_name`, `last_name`, `username`, `email`, `password`, `join_date`, `ranking_id`, `points`) VALUES
(1, 'Admin', 'Admin', 'admin', 'admin@mail.com', 'admin', '2023-04-10 00:00:00', 1, 0),
(2, 'Anonymous', 'Anonymous', 'anonymous', 'anonymous@mail.com', 'anonymous', '2023-04-10 00:00:00', 1, 0),
(3, 'Reserved1', 'Reserved1', 'reserved1', 'reserved1@mail.com', 'reserved1', '2023-04-10 00:00:00', 1, 0),
(4, 'Reserved2', 'Reserved2', 'reserved2', 'reserved2@mail.com', 'reserved2', '2023-04-10 00:00:00', 1, 0),
(5, 'Reserved3', 'Reserved3', 'reserved3', 'reserved3@mail.com', 'reserved3', '2023-04-10 00:00:00', 1, 0),
(6, 'Alexandra', 'Silcock', 'silcock', 'silcock@dailynews.com', 'silcock', '2023-04-12 12:01:03', 1, 40),
(7, 'Margot', 'Rashleig', 'rashleig', 'rashleig@yahoo.com', 'rashleig', '2023-04-12 12:01:30', 4, 1502),
(8, 'Brian', 'Mussalli', 'mussalli', 'mussalli@skynet.com', 'mussalli', '2023-04-12 12:01:46', 1, 1206),
(9, 'Orion', 'Wahner', 'owahnert', 'owahnert@tinyurl.com', 'owahnert', '2023-04-12 12:02:01', 1, 16),
(10, 'Reed', 'Flynn', 'rflynn12', 'rflynn12@php.net', 'rflynn12', '2023-04-12 12:02:19', 1, 125),
(11, 'Deleted', 'Deleted', 'deleted', 'deleted@mail.com', 'deleted', '2023-04-12 12:03:00', 1, 0),
(12, 'Tester', 'Tester', 'tester', 'tester@mail.com', 'tester', '2023-04-12 12:03:45', 3, 299),
(13, 'Alex', 'Dimov', 'alexdimov', 'alexdimov@mail.com', 'alexdimov', '2023-04-12 12:29:55', 1, 0),
(14, 'Orion', 'Spacer', 'orispace', 'orispace@gmail.com', 'orispace', '2023-04-12 12:32:53', 3, 163),
(15, 'John', 'Smith', 'smith555', 'smith555@yahoo.com', 'smith555', '2023-04-12 12:34:13', 2, 34);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;