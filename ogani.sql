-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.34 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.5.0.6677
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for ogani
CREATE DATABASE IF NOT EXISTS `ogani` /*!40100 DEFAULT CHARACTER SET utf8mb3 */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `ogani`;

-- Dumping structure for table ogani.address
CREATE TABLE IF NOT EXISTS `address` (
  `id` int NOT NULL AUTO_INCREMENT,
  `line1` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `line2` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `city_id` int NOT NULL,
  `postal_code` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `first_name` varchar(45) DEFAULT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  `mobile` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_address_city1_idx` (`city_id`),
  KEY `FK_address_users` (`user_id`),
  CONSTRAINT `fk_address_city1` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`),
  CONSTRAINT `FK_address_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ogani.address: ~1 rows (approximately)
INSERT INTO `address` (`id`, `line1`, `line2`, `city_id`, `postal_code`, `user_id`, `first_name`, `last_name`, `mobile`) VALUES
	(1, 'Horana', 'Batuwita', 3, '12410', 2, 'Moksha', 'Dewmini', '0786213897');

-- Dumping structure for table ogani.cart
CREATE TABLE IF NOT EXISTS `cart` (
  `id` int NOT NULL AUTO_INCREMENT,
  `qty` int DEFAULT NULL,
  `product_id` int NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_cart_meal_kit1_idx` (`product_id`) USING BTREE,
  KEY `fk_cart_users1_idx` (`user_id`) USING BTREE,
  CONSTRAINT `fk_cart_meal_kit1` FOREIGN KEY (`product_id`) REFERENCES `meal_kit` (`id`),
  CONSTRAINT `fk_cart_users1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ogani.cart: ~3 rows (approximately)
INSERT INTO `cart` (`id`, `qty`, `product_id`, `user_id`) VALUES
	(12, 1, 16, 4),
	(15, 1, 18, 2),
	(16, 1, 17, 2);

-- Dumping structure for table ogani.category
CREATE TABLE IF NOT EXISTS `category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ogani.category: ~11 rows (approximately)
INSERT INTO `category` (`id`, `name`) VALUES
	(1, 'Vegetarian'),
	(2, 'Non-vagetarian'),
	(3, 'Gluten free'),
	(4, 'Keto'),
	(5, 'Fitness meals'),
	(6, 'Weight loss kits'),
	(7, 'Quick and easy'),
	(8, 'Kids friendly meals'),
	(9, 'Dried foods'),
	(10, 'Nutri drinks'),
	(11, 'Supplements');

-- Dumping structure for table ogani.city
CREATE TABLE IF NOT EXISTS `city` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ogani.city: ~7 rows (approximately)
INSERT INTO `city` (`id`, `name`) VALUES
	(1, 'Colombo'),
	(2, 'Gampaha'),
	(3, 'Horana'),
	(4, 'Anuradhapura'),
	(5, 'Jaffna'),
	(6, 'Mulative'),
	(7, 'Galle');

-- Dumping structure for table ogani.ingredients
CREATE TABLE IF NOT EXISTS `ingredients` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ogani.ingredients: ~14 rows (approximately)
INSERT INTO `ingredients` (`id`, `name`) VALUES
	(1, 'beans, peas, lentils, and soy-based foods'),
	(2, 'meat and poultry products, milk'),
	(3, 'dairy products, fish, lard, and gelatin'),
	(4, 'carrot, cabbage, potatoes and corn'),
	(5, ' quinoa, rice (both brown and white), legumes'),
	(6, 'starchy and root vegetables'),
	(7, 'chicken, spinach, avacado, olive oil'),
	(8, 'fish, broccoli, nuts'),
	(9, 'chicken, fish, or plant-based protein'),
	(10, 'dehydrated or canned veggies, spices, and sauces'),
	(11, 'fresh fruits, enchiladas, pasta dishes, and tacos'),
	(12, 'freeze-dried vegetables, dehydrated legumes, grains'),
	(13, 'cow milk or oats milk, protein powder, honey'),
	(14, 'minerals, vitamins, amino acids, herbs or botanicals, and enzymes');

-- Dumping structure for table ogani.meal_kit
CREATE TABLE IF NOT EXISTS `meal_kit` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `description` text,
  `price` double DEFAULT NULL,
  `qty` int DEFAULT NULL,
  `category_id` int NOT NULL,
  `ingredients_id` int NOT NULL,
  `product_status_id` int NOT NULL,
  `date_time` date NOT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_meal_kit_category_idx` (`category_id`),
  KEY `fk_meal_kit_product_status1_idx` (`product_status_id`),
  KEY `FK_meal_kit_ingredients` (`ingredients_id`),
  KEY `FK_meal_kit_users` (`user_id`),
  CONSTRAINT `fk_meal_kit_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`),
  CONSTRAINT `FK_meal_kit_ingredients` FOREIGN KEY (`ingredients_id`) REFERENCES `ingredients` (`id`),
  CONSTRAINT `fk_meal_kit_product_status1` FOREIGN KEY (`product_status_id`) REFERENCES `product_status` (`id`),
  CONSTRAINT `FK_meal_kit_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ogani.meal_kit: ~9 rows (approximately)
INSERT INTO `meal_kit` (`id`, `name`, `description`, `price`, `qty`, `category_id`, `ingredients_id`, `product_status_id`, `date_time`, `user_id`) VALUES
	(10, 'Vegi Max', 'Ideal for those seeking a wholesome, plant-based option, this veggie meal is rich in essential vitamins, minerals, and antioxidants, promoting overall wellbeing. Whether enjoyed as a hearty main course or a nutritious side. its a perfect choice for a healthy and fulfilling dining experience.', 1500, 8, 1, 5, 1, '2024-09-12', 2),
	(11, 'Daily Harvest Kit', 'Infused with aromatic herbs and spices, this meal offers a balance of savory and slightly sweet flavors, complemented by a hint of garlic and a touch of olive oil. Each bite delivers a satisfying crunch and a burst of natural freshness.', 2000, 9, 1, 1, 1, '2024-09-12', 2),
	(12, 'Chicken egg Combo kit', 'Perfect for those who enjoy a protein-rich diet, our non-vegetarian meals are crafted with care to ensure you receive a nutritious and delectable dining experience.', 1600, 2, 2, 9, 1, '2024-09-12', 2),
	(13, 'Avacado sea food dish', '\r\nHere\'s a description for a non-vegetarian meal:\r\n\r\nNon-Vegetarian Meal\r\n\r\nIndulge in a hearty and satisfying non-vegetarian meal that brings together rich flavors and high-quality ingredients. Our non-vegetarian meals are thoughtfully prepared to offer a balanced combination of proteins, healthy fats, and essential nutrients. Each meal features a succulent portion of expertly cooked meatâwhether it\'s tender chicken, juicy beef, or flavorful seafoodâpaired with fresh vegetables, wholesome grains, and delicious sauces.\r\n\r\nOur non-vegetarian options are designed to cater to diverse tastes and preferences, with choices ranging from classic favorites like grilled chicken with roasted vegetables to more adventurous dishes like spicy seafood paella. Enjoy a meal that not only satisfies your hunger but also delights your palate with every bite.', 1800, 6, 2, 7, 1, '2024-09-12', 2),
	(14, 'Happy kid meal', ' Fun treat like a fruit cup or a small dessert to make mealtime extra special. Every meal is designed to be fun and healthy, giving kids the energy they need to play and grow!', 2100, 2, 8, 11, 1, '2024-09-12', 2),
	(15, 'Happy kid vegy meal', 'Our baby meals are specially designed to support the nutritional needs of infants and young children. Each meal is carefully crafted to provide a balanced blend of essential vitamins, minerals, and nutrients, ensuring optimal growth and development.', 1800, 2, 8, 11, 1, '2024-09-12', 2),
	(16, 'Low carb keto meal', 'The focus is on maintaining a macronutrient ratio of approximately 70-75% fat, 20-25% protein, and 5-10% carbs. Keto meals aim to reduce blood sugar spikes and support fat loss while providing sustained energy throughout the day.', 2200, 11, 4, 6, 1, '2024-09-12', 2),
	(17, 'Combo keto pack', 'A keto meal is a low-carb, high-fat dish designed to promote ketosis, a metabolic state where the body burns fat for fuel instead of carbohydrates. Typical components of a keto meal include healthy fats (such as avocado, olive oil, and nuts), moderate amounts of protein (from sources like meat, poultry, fish, or eggs), and minimal carbohydrates (mainly from leafy greens and non-starchy vegetables). ', 2100, 11, 4, 4, 1, '2024-09-12', 2),
	(18, 'Nutri Drinks', 'The best choice for quenching your thirst. Coffee and tea, without added sweeteners, are healthy choices, too. Some beverages should be limited or consumed in moderation, including fruit juice, milk, and those made with low-calorie sweeteners, like diet drinks.', 1200, 8, 10, 13, 1, '2024-09-17', 2);

-- Dumping structure for table ogani.nutri_info
CREATE TABLE IF NOT EXISTS `nutri_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `calories` int DEFAULT NULL,
  `fat` int DEFAULT NULL,
  `protein` int DEFAULT NULL,
  `carbohdrate` int DEFAULT NULL,
  `vam` int DEFAULT NULL,
  `meal_kit_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_nutri_info_meal_kit1_idx` (`meal_kit_id`),
  CONSTRAINT `fk_nutri_info_meal_kit1` FOREIGN KEY (`meal_kit_id`) REFERENCES `meal_kit` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ogani.nutri_info: ~9 rows (approximately)
INSERT INTO `nutri_info` (`id`, `calories`, `fat`, `protein`, `carbohdrate`, `vam`, `meal_kit_id`) VALUES
	(2, 5, 6, 8, 7, 56, 10),
	(3, 5, 11, 11, 21, 34, 11),
	(4, 5, 6, 7, 6, 71, 12),
	(5, 4, 6, 4, 2, 67, 13),
	(6, 3, 4, 7, 5, 72, 14),
	(7, 5, 6, 3, 8, 81, 15),
	(8, 7, 4, 76, 7, 81, 16),
	(9, 2, 8, 5, 7, 12, 17),
	(10, 3, 5, 4, 7, 6, 18);

-- Dumping structure for table ogani.orders
CREATE TABLE IF NOT EXISTS `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date_time` date DEFAULT NULL,
  `meal_kit_id` int DEFAULT NULL,
  `order_status_id` int DEFAULT NULL,
  `address_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_order_meal_kit1_idx` (`meal_kit_id`),
  KEY `fk_order_order_status1_idx` (`order_status_id`),
  KEY `FK_orders_address` (`address_id`),
  KEY `FK_orders_users` (`user_id`),
  CONSTRAINT `fk_order_meal_kit1` FOREIGN KEY (`meal_kit_id`) REFERENCES `meal_kit` (`id`),
  CONSTRAINT `fk_order_order_status1` FOREIGN KEY (`order_status_id`) REFERENCES `order_status` (`id`),
  CONSTRAINT `FK_orders_address` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`),
  CONSTRAINT `FK_orders_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ogani.orders: ~18 rows (approximately)
INSERT INTO `orders` (`id`, `date_time`, `meal_kit_id`, `order_status_id`, `address_id`, `user_id`) VALUES
	(1, '2024-09-14', NULL, NULL, 1, 2),
	(2, '2024-09-14', NULL, NULL, 1, 2),
	(3, '2024-09-14', NULL, NULL, 1, 2),
	(4, '2024-09-14', NULL, NULL, 1, 2),
	(5, '2024-09-14', NULL, NULL, 1, 2),
	(6, '2024-09-14', NULL, NULL, 1, 2),
	(7, '2024-09-14', NULL, NULL, 1, 2),
	(8, '2024-09-15', NULL, NULL, 1, 2),
	(9, '2024-09-15', NULL, NULL, 1, 2),
	(10, '2024-09-15', NULL, NULL, 1, 2),
	(11, '2024-09-15', NULL, NULL, 1, 2),
	(12, '2024-09-15', NULL, NULL, 1, 2),
	(13, '2024-09-15', NULL, NULL, 1, 2),
	(14, '2024-09-15', NULL, NULL, 1, 2),
	(15, '2024-09-15', NULL, NULL, 1, 2),
	(16, '2024-09-15', NULL, NULL, 1, 2),
	(17, '2024-09-17', NULL, NULL, 1, 2),
	(18, '2024-09-17', NULL, NULL, 1, 2);

-- Dumping structure for table ogani.order_history
CREATE TABLE IF NOT EXISTS `order_history` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_date` date DEFAULT NULL,
  `total_price` double DEFAULT NULL,
  `orders_id` int NOT NULL,
  `users_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_order_history_users1_idx` (`users_id`),
  KEY `fk_order_history_order1_idx` (`orders_id`) USING BTREE,
  CONSTRAINT `fk_order_history_order1` FOREIGN KEY (`orders_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `fk_order_history_users1` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ogani.order_history: ~0 rows (approximately)

-- Dumping structure for table ogani.order_item
CREATE TABLE IF NOT EXISTS `order_item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` int DEFAULT NULL,
  `meal_kit_id` int DEFAULT NULL,
  `qty` int DEFAULT NULL,
  `order_status_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK__order` (`order_id`),
  KEY `FK_order_item_meal_kit` (`meal_kit_id`),
  KEY `FK_order_item_order_status` (`order_status_id`),
  CONSTRAINT `FK__order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `FK_order_item_meal_kit` FOREIGN KEY (`meal_kit_id`) REFERENCES `meal_kit` (`id`),
  CONSTRAINT `FK_order_item_order_status` FOREIGN KEY (`order_status_id`) REFERENCES `order_status` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ogani.order_item: ~13 rows (approximately)
INSERT INTO `order_item` (`id`, `order_id`, `meal_kit_id`, `qty`, `order_status_id`) VALUES
	(1, 1, 12, 3, 1),
	(2, 1, 11, 2, 1),
	(3, 1, 15, 3, 1),
	(4, 2, 15, 2, 1),
	(5, 3, 14, 1, 1),
	(6, 3, 10, 2, 1),
	(7, 3, 13, 2, 1),
	(8, 7, 13, 2, 1),
	(9, 12, 12, 2, 1),
	(10, 15, 12, 1, 1),
	(11, 16, 15, 1, 1),
	(12, 17, 17, 1, 1),
	(13, 18, 14, 3, 1);

-- Dumping structure for table ogani.order_status
CREATE TABLE IF NOT EXISTS `order_status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ogani.order_status: ~3 rows (approximately)
INSERT INTO `order_status` (`id`, `name`) VALUES
	(1, 'Peding'),
	(2, 'Delivered'),
	(3, 'Cancelled');

-- Dumping structure for table ogani.payment_status
CREATE TABLE IF NOT EXISTS `payment_status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ogani.payment_status: ~2 rows (approximately)
INSERT INTO `payment_status` (`id`, `name`) VALUES
	(1, 'Paid'),
	(2, 'Pending');

-- Dumping structure for table ogani.product_status
CREATE TABLE IF NOT EXISTS `product_status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ogani.product_status: ~2 rows (approximately)
INSERT INTO `product_status` (`id`, `name`) VALUES
	(1, 'Available'),
	(2, 'Finished');

-- Dumping structure for table ogani.users
CREATE TABLE IF NOT EXISTS `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `password` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `verification` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ogani.users: ~2 rows (approximately)
INSERT INTO `users` (`id`, `password`, `verification`, `first_name`, `last_name`, `email`) VALUES
	(2, '123', 'Verified', 'Moksha', 'Dewmini', 'dewminimoksha40@gmail.com'),
	(4, 'Methmi2009@', 'Verified', 'Methmi', 'Sathsarangi', 'kiwinumjin@gmail.com');

-- Dumping structure for table ogani.watchlist
CREATE TABLE IF NOT EXISTS `watchlist` (
  `id` int NOT NULL,
  `meal_kit_id` int NOT NULL,
  `users_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_watchlist_meal_kit1_idx` (`meal_kit_id`),
  KEY `fk_watchlist_users1_idx` (`users_id`),
  CONSTRAINT `fk_watchlist_meal_kit1` FOREIGN KEY (`meal_kit_id`) REFERENCES `meal_kit` (`id`),
  CONSTRAINT `fk_watchlist_users1` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ogani.watchlist: ~0 rows (approximately)

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
