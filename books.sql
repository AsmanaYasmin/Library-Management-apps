-- phpMyAdmin SQL Dump
-- version 5.2.2
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Aug 23, 2025 at 04:19 PM
-- Server version: 8.0.42-cll-lve
-- PHP Version: 8.3.20

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `farhanat_books`
--

-- --------------------------------------------------------

--
-- Table structure for table `books`
--

CREATE TABLE `books` (
  `id` int NOT NULL,
  `title` varchar(255) NOT NULL,
  `authorname` varchar(255) NOT NULL,
  `file_url` text NOT NULL,
  `status` enum('assign','return') DEFAULT 'return'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `books`
--

INSERT INTO `books` (`id`, `title`, `authorname`, `file_url`, `status`) VALUES
(12, 'The Little Prince', 'Antoine de Saint-Exupery', 'https://farhana42.top/uploads/1755517441_The-Little-Prince-English-short-stories-for-beginners-PDF-Book.pdf', 'return'),
(13, 'Blue Moon Beach', 'Sue Murray', 'https://farhana42.top/uploads/1755517500_Blue-Moon-Beach-by-Sue-Murray-1.pdf', 'return'),
(14, 'The Mermaid', 'Hans Christian Andersen', 'https://farhana42.top/uploads/1755521252_The-Fisherman-and-His-Soul-Books-PDF-for-Elementary-Levels.pdf', 'assign'),
(15, 'Orokkhito Shadhinota ei poradhinota', 'Mejor M Jolil', 'https://farhana42.top/uploads/1755955221_অরক্ষিত স্বাধীনতাই পরাধীনতা.pdf', 'return');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `books`
--
ALTER TABLE `books`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `books`
--
ALTER TABLE `books`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
