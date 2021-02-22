-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Feb 05, 2021 at 10:52 PM
-- Server version: 10.4.17-MariaDB
-- PHP Version: 8.0.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `xo_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `game`
--

CREATE TABLE `game` (
  `ID` int(11) NOT NULL,
  `bn1` enum('Null','x','O','X','null','o') NOT NULL,
  `bn2` enum('Null','x','O','X','null','o') NOT NULL,
  `bn3` enum('Null','x','O','X','null','o') NOT NULL,
  `bn4` enum('Null','x','O','X','null','o') NOT NULL,
  `bn5` enum('Null','x','O','X','null','o') NOT NULL,
  `bn6` enum('Null','x','O','X','null','o') NOT NULL,
  `bn7` enum('Null','x','O','X','null','o') NOT NULL,
  `bn8` enum('Null','x','O','X','null','o') NOT NULL,
  `bn9` enum('Null','x','O','X','null','o') NOT NULL,
  `playerX` varchar(60) NOT NULL,
  `playerO` varchar(60) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `game`
--

INSERT INTO `game` (`ID`, `bn1`, `bn2`, `bn3`, `bn4`, `bn5`, `bn6`, `bn7`, `bn8`, `bn9`, `playerX`, `playerO`) VALUES
(34, 'x', 'x', 'O', 'x', 'x', 'x', 'x', 'x', 'x', '', ''),
(35, 'O', 'O', 'x', 'x', 'x', 'Null', 'x', 'x', 'x', '', ''),
(47, 'O', 'O', 'x', 'x', 'x', 'Null', 'x', 'x', 'x', 'ahemreda', 'kaledAshraf');

-- --------------------------------------------------------

--
-- Table structure for table `player`
--

CREATE TABLE `player` (
  `ID` int(11) NOT NULL,
  `firstname` varchar(60) NOT NULL,
  `lastname` varchar(60) NOT NULL,
  `username` varchar(60) NOT NULL,
  `password` varchar(60) NOT NULL,
  `score` varchar(60) DEFAULT NULL,
  `state` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `player`
--

INSERT INTO `player` (`ID`, `firstname`, `lastname`, `username`, `password`, `score`, `state`) VALUES
(8, 'eman', 'soliman', 'emysoliman', '2345', NULL, NULL),
(9, 'eman', 'soliman', 'omnyamostafa', '2345', NULL, NULL),
(10, 'ahemed', 'reda', 'ahemreda', '6789', NULL, NULL),
(12, 'khaled', 'ashraf', 'kaledAshraf', '102030', NULL, NULL),
(13, 'heba', 'mostafa', 'hebamos', '123', NULL, NULL),
(14, 'eman', 'soliman', 'emansol', '123', '20', 'online');

-- --------------------------------------------------------

--
-- Table structure for table `players_record_games`
--

CREATE TABLE `players_record_games` (
  `player1_id` int(11) NOT NULL,
  `player2_id` int(11) NOT NULL,
  `game_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `players_record_games`
--

INSERT INTO `players_record_games` (`player1_id`, `player2_id`, `game_id`) VALUES
(10, 9, 35),
(13, 9, 34),
(10, 12, 47);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `game`
--
ALTER TABLE `game`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `player`
--
ALTER TABLE `player`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `players_record_games`
--
ALTER TABLE `players_record_games`
  ADD PRIMARY KEY (`player1_id`,`game_id`),
  ADD KEY `players_record_games_ibfk_2` (`player2_id`),
  ADD KEY `players_record_games_ibfk_3` (`game_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `game`
--
ALTER TABLE `game`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=48;

--
-- AUTO_INCREMENT for table `player`
--
ALTER TABLE `player`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `players_record_games`
--
ALTER TABLE `players_record_games`
  ADD CONSTRAINT `players_record_games_ibfk_1` FOREIGN KEY (`player1_id`) REFERENCES `player` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `players_record_games_ibfk_2` FOREIGN KEY (`player2_id`) REFERENCES `player` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `players_record_games_ibfk_3` FOREIGN KEY (`game_id`) REFERENCES `game` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
