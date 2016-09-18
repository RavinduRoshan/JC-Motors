-- phpMyAdmin SQL Dump
-- version 4.1.12
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Sep 16, 2016 at 04:07 AM
-- Server version: 5.6.16
-- PHP Version: 5.5.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `jcmotors`
--

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE IF NOT EXISTS `customer` (
  `customer_id` varchar(7) NOT NULL,
  `customer_name` varchar(100) NOT NULL,
  `address` varchar(100) NOT NULL,
  `contact` varchar(10) NOT NULL,
  `email` varchar(50) NOT NULL,
  PRIMARY KEY (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`customer_id`, `customer_name`, `address`, `contact`, `email`) VALUES
('C000001', 'Nimal Gamage', '45,\nHill Street,\nNegombo.', '0775669896', 'nimal@gmail.com'),
('C000002', 'Kamal Gamage', 'No 56,\nWijerama,\nColombo 3', '0715698962', 'kamal@gmail.com'),
('C000003', 'Hemantha Wijegunaratne', 'No 25,\nHigh Level Rd,\nKegalle.', '0712563698', 'hemantha@gmail.com'),
('C000007', 'fefsdvsd', 'ngfhfg\njthjhgj', '0722423343', 'bfdbfdb'),
('C000008', 'Ravindu Roshan', '33/13,\nWelegewatta,\nMilidduwa,\nGalle.', '0773558782', 'ravinduroshan.rr@gmail.com');

-- --------------------------------------------------------

--
-- Table structure for table `dealer`
--

CREATE TABLE IF NOT EXISTS `dealer` (
  `dealer_id` varchar(7) NOT NULL,
  `dealer_name` varchar(100) NOT NULL,
  `email` varchar(50) DEFAULT NULL,
  `contact` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`dealer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dealer`
--

INSERT INTO `dealer` (`dealer_id`, `dealer_name`, `email`, `contact`) VALUES
('D000001', 'Ravindu Roshan', 'ravindu@gmail.com', '0773558782'),
('D000002', 'Kasun Perera', 'kasun@gamail.com', '0773558782'),
('D000003', 'Chinthaka Malith', 'chinthaka@gmail.com', '0775889896'),
('D000004', 'Vimal Silva', '', ''),
('D000005', 'Dhanushka Prasadh', 'dhanushka@gmail.com', '8888888888'),
('D000006', 'Dinuka Silva', '', '4444444444'),
('D000007', 'Damith Nishshanka', 'damith@gmail.com', '2222222222'),
('D000008', 'Tharindu Maduranga', 'tharindu@gmail.com', '8472349283'),
('D000009', '5ytryty', 'trtytry', '0775683222');

-- --------------------------------------------------------

--
-- Table structure for table `part`
--

CREATE TABLE IF NOT EXISTS `part` (
  `part_id` varchar(7) NOT NULL,
  `part_name` varchar(100) NOT NULL,
  `type` varchar(100) NOT NULL,
  `model` varchar(100) NOT NULL,
  `stock` varchar(20) NOT NULL,
  PRIMARY KEY (`part_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `part`
--

INSERT INTO `part` (`part_id`, `part_name`, `type`, `model`, `stock`) VALUES
('JC00001', 'Seat', 'Type 2', 'Model 1', 'Hero'),
('JC00002', 'Head Light', 'Type 1', 'Model 1', 'Other'),
('JC00003', 'Seat', 'Type 2', 'Model 1', 'Hero'),
('JC00004', 'Signal Light', 'Type 1', 'Model 1', 'Hero'),
('JC00005', 'Light', 'Type 3', 'Model 4', 'Hero'),
('JC00007', 'Head Light', 'Type 3', 'Model 4', 'Hero'),
('JC00008', 'seat', 'Type 1', 'Model 1', 'Other');

-- --------------------------------------------------------

--
-- Table structure for table `payment`
--

CREATE TABLE IF NOT EXISTS `payment` (
  `service_id` varchar(7) NOT NULL,
  `total` decimal(10,2) NOT NULL,
  `payment` decimal(10,2) NOT NULL,
  `balance` decimal(10,2) NOT NULL,
  `type` varchar(10) DEFAULT 'cash',
  `check_no` varchar(10) DEFAULT NULL,
  `date` date NOT NULL,
  `time` time NOT NULL,
  `username` varchar(50) NOT NULL,
  `next_service` varchar(20) NOT NULL,
  PRIMARY KEY (`service_id`),
  KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `quotation`
--

CREATE TABLE IF NOT EXISTS `quotation` (
  `service_id` varchar(10) NOT NULL,
  `customer_id` varchar(7) NOT NULL,
  `service_charge` decimal(10,2) NOT NULL,
  `total` decimal(10,2) NOT NULL,
  `date` date NOT NULL,
  `username` varchar(50) NOT NULL,
  PRIMARY KEY (`service_id`,`customer_id`),
  KEY `username` (`username`),
  KEY `service_id` (`service_id`),
  KEY `customer_id` (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `quotation`
--

INSERT INTO `quotation` (`service_id`, `customer_id`, `service_charge`, `total`, `date`, `username`) VALUES
('SVC000000', 'C000003', '0.00', '19504.00', '2016-03-06', 'admin'),
('SVC000001', 'C000001', '0.00', '6174.00', '2016-03-06', 'admin'),
('SVC000002', 'C000002', '450.00', '16376.00', '2016-03-08', 'admin'),
('SVC000003', 'C000002', '780.00', '6954.00', '2016-03-08', 'admin'),
('SVC000004', 'C000002', '456.00', '9766.00', '2016-03-08', 'admin'),
('SVC000005', 'C000008', '756.00', '5632.00', '2016-03-08', 'admin'),
('SVC000006', 'C000001', '100.00', '6372.00', '2016-03-18', 'admin'),
('SVC000007', 'C000003', '250.00', '24946.00', '2016-03-18', 'admin'),
('SVC000008', 'C000008', '350.00', '15344.00', '2016-03-18', 'admin'),
('SVC000009', 'C000002', '450.00', '11500.00', '2016-03-21', 'admin'),
('SVC000010', 'C000008', '550.00', '17460.48', '2016-03-21', 'admin'),
('SVC000011', 'C000008', '223.00', '49177.00', '2016-03-25', 'admin'),
('SVC000012', 'C000003', '245.00', '3381.00', '2016-03-25', 'admin'),
('SVC000013', 'C000008', '0.00', '4416.00', '2016-03-25', 'admin'),
('SVC000014', 'C000003', '0.00', '6624.00', '2016-03-25', 'admin'),
('SVC000015', 'C000003', '0.00', '17224.00', '2016-06-15', 'admin'),
('SVC000016', 'C000008', '0.00', '6174.00', '2016-06-15', 'admin');

-- --------------------------------------------------------

--
-- Table structure for table `selected_customer`
--

CREATE TABLE IF NOT EXISTS `selected_customer` (
  `customer_id` varchar(7) NOT NULL,
  `customer_name` varchar(100) NOT NULL,
  `address` varchar(100) NOT NULL,
  `contact` varchar(10) NOT NULL,
  `email` varchar(50) NOT NULL,
  PRIMARY KEY (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `selected_parts`
--

CREATE TABLE IF NOT EXISTS `selected_parts` (
  `stock_id` varchar(10) NOT NULL,
  `part_id` varchar(7) NOT NULL,
  `quantity` int(11) NOT NULL,
  `price` double NOT NULL,
  PRIMARY KEY (`stock_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `service`
--

CREATE TABLE IF NOT EXISTS `service` (
  `service_id` varchar(10) NOT NULL,
  `stock_id` varchar(10) NOT NULL,
  `quantity` int(11) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`service_id`,`stock_id`),
  KEY `stock_id` (`stock_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `service`
--

INSERT INTO `service` (`service_id`, `stock_id`, `quantity`, `price`) VALUES
('SVC000000', 'S000000003', 4, '19504.00'),
('SVC0000001', 'S000000001', 1, '6174.00'),
('SVC0000001', 'S000000004', 2, '21390.00'),
('SVC000001', 'S000000001', 1, '6174.00'),
('SVC000002', 'S000000001', 1, '6174.00'),
('SVC000002', 'S000000003', 2, '9752.00'),
('SVC000003', 'S000000001', 1, '6174.00'),
('SVC000004', 'S000000001', 1, '6174.00'),
('SVC000004', 'S000000002', 1, '3136.00'),
('SVC000005', 'S000000003', 1, '4876.00'),
('SVC000006', 'S000000002', 2, '6272.00'),
('SVC000007', 'S000000001', 4, '24696.00'),
('SVC000008', 'S000000005', 3, '14994.00'),
('SVC000009', 'S000000001', 1, '6174.00'),
('SVC000009', 'S000000003', 1, '4876.00'),
('SVC000010', 'S000000002', 1, '3136.00'),
('SVC000010', 'S000000005', 1, '4998.00'),
('SVC000010', 'S000000007', 1, '5076.00'),
('SVC000010', 'S000000013', 4, '2230.48'),
('SVC000010', 'S000000014', 3, '1470.00'),
('SVC000011', 'S000000001', 1, '6174.00'),
('SVC000011', 'S000000004', 2, '42780.00'),
('SVC000012', 'S000000002', 1, '3136.00'),
('SVC000013', 'S000000010', 1, '4416.00'),
('SVC000014', 'S000000006', 1, '6624.00'),
('SVC000015', 'S000000001', 2, '12348.00'),
('SVC000015', 'S000000003', 1, '4876.00'),
('SVC000016', 'S000000001', 1, '6174.00');

-- --------------------------------------------------------

--
-- Table structure for table `stock`
--

CREATE TABLE IF NOT EXISTS `stock` (
  `stock_id` varchar(10) NOT NULL,
  `part_id` varchar(7) NOT NULL,
  `dealer_id` varchar(7) NOT NULL,
  `cost` decimal(10,2) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `discount` decimal(10,2) NOT NULL,
  `quantity` int(11) NOT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`stock_id`),
  KEY `part_id` (`part_id`),
  KEY `dealer_id` (`dealer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `stock`
--

INSERT INTO `stock` (`stock_id`, `part_id`, `dealer_id`, `cost`, `price`, `discount`, `quantity`, `date`) VALUES
('S000000001', 'JC00001', 'D000002', '6000.00', '6300.00', '2.00', 14, '2016-02-08'),
('S000000002', 'JC00003', 'D000002', '3000.00', '3200.00', '2.00', 4, '2016-02-08'),
('S000000003', 'JC00004', 'D000002', '5000.00', '5300.00', '8.00', 5, '2016-02-08'),
('S000000004', 'JC00007', 'D000002', '20000.00', '23000.00', '7.00', 8, '2016-02-08'),
('S000000005', 'JC00007', 'D000002', '5000.00', '5100.00', '2.00', 8, '2016-02-08'),
('S000000006', 'JC00002', 'D000002', '7000.00', '7200.00', '8.00', 6, '2016-02-08'),
('S000000007', 'JC00003', 'D000002', '5000.00', '5400.00', '6.00', 12, '2016-02-09'),
('S000000008', 'JC00003', 'D000002', '5000.00', '5600.00', '3.00', 15, '2016-02-25'),
('S000000009', 'JC00003', 'D000004', '60000.00', '70000.00', '10.00', 4, '2016-02-08'),
('S000000010', 'JC00005', 'D000007', '4000.00', '4600.00', '4.00', 12, '2016-02-10'),
('S000000011', 'JC00004', 'D000008', '2300.00', '2500.00', '3.00', 15, '2016-02-10'),
('S000000012', 'JC00002', 'D000002', '4500.00', '5965.00', '3.00', 42, '2016-02-12'),
('S000000013', 'JC00003', 'D000002', '452.00', '569.00', '2.00', 45, '2016-02-14'),
('S000000014', 'JC00001', 'D000009', '450.00', '500.00', '2.00', 15, '2016-02-18'),
('S000000015', 'JC00001', 'D000001', '4560.00', '5623.00', '1.00', 0, '2016-03-01');

-- --------------------------------------------------------

--
-- Table structure for table `temp_order`
--

CREATE TABLE IF NOT EXISTS `temp_order` (
  `stock_id` varchar(10) NOT NULL,
  `part_id` varchar(7) NOT NULL,
  `quantity` int(11) NOT NULL,
  KEY `stock_id` (`stock_id`),
  KEY `part_id` (`part_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `temp_order`
--

INSERT INTO `temp_order` (`stock_id`, `part_id`, `quantity`) VALUES
('S000000001', 'JC00001', 1),
('S000000004', 'JC00007', 2);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `username` varchar(50) NOT NULL,
  `name` varchar(100) NOT NULL,
  `password` varchar(10) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`username`, `name`, `password`) VALUES
('admin', 'Ravindu Roshan', '123');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `payment`
--
ALTER TABLE `payment`
  ADD CONSTRAINT `payment_ibfk_2` FOREIGN KEY (`username`) REFERENCES `user` (`username`),
  ADD CONSTRAINT `payment_ibfk_3` FOREIGN KEY (`service_id`) REFERENCES `service` (`service_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `quotation`
--
ALTER TABLE `quotation`
  ADD CONSTRAINT `quotation_ibfk_3` FOREIGN KEY (`username`) REFERENCES `user` (`username`),
  ADD CONSTRAINT `quotation_ibfk_5` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`),
  ADD CONSTRAINT `quotation_ibfk_6` FOREIGN KEY (`service_id`) REFERENCES `service` (`service_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `selected_customer`
--
ALTER TABLE `selected_customer`
  ADD CONSTRAINT `selected_customer_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `service`
--
ALTER TABLE `service`
  ADD CONSTRAINT `service_ibfk_1` FOREIGN KEY (`stock_id`) REFERENCES `stock` (`stock_id`);

--
-- Constraints for table `stock`
--
ALTER TABLE `stock`
  ADD CONSTRAINT `stock_ibfk_1` FOREIGN KEY (`part_id`) REFERENCES `part` (`part_id`),
  ADD CONSTRAINT `stock_ibfk_2` FOREIGN KEY (`dealer_id`) REFERENCES `dealer` (`dealer_id`);

--
-- Constraints for table `temp_order`
--
ALTER TABLE `temp_order`
  ADD CONSTRAINT `temp_order_ibfk_1` FOREIGN KEY (`stock_id`) REFERENCES `stock` (`stock_id`),
  ADD CONSTRAINT `temp_order_ibfk_2` FOREIGN KEY (`part_id`) REFERENCES `part` (`part_id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
