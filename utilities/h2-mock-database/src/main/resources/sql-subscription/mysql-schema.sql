CREATE DATABASE  IF NOT EXISTS `publish_subscribe`;
USE `publish_subscribe`;

--
-- Create tables for subscription notification processor
--

DROP TABLE IF EXISTS `subscription`;
CREATE TABLE `subscription` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `topic` varchar(100) NOT NULL,
  `startDate` date NOT NULL,
  `endDate` date DEFAULT NULL,
  `lastValidationDate` date DEFAULT NULL,
  `subscribingSystemIdentifier` varchar(100) NOT NULL,
  `subscriptionOwner` varchar(100) NOT NULL,
  `subjectName` varchar(100) NOT NULL,
  `active` tinyint(4) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `notification_mechanism`;
CREATE TABLE `notification_mechanism` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `subscriptionId` int(11) NOT NULL,
  `notificationMechanismType` varchar(20) NOT NULL,
  `notificationAddress` varchar(256) NOT NULL,
  
  PRIMARY KEY (`id`),
  INDEX (`subscriptionId`),
  UNIQUE INDEX (`subscriptionId`, `notificationAddress`),
  FOREIGN KEY (`subscriptionId`)
  	REFERENCES `subscription` (`id`)
  	ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `subscription_subject_identifier`;
CREATE TABLE `subscription_subject_identifier` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `subscriptionId` int(11) NOT NULL,
  `identifierName` varchar(100) NOT NULL,
  `identifierValue` varchar(256) NOT NULL,
  
  PRIMARY KEY (`id`),
  INDEX (`subscriptionId`),
  UNIQUE INDEX (`subscriptionId`, `identifierName`),
  INDEX (`identifierName`, `identifierValue`),
  FOREIGN KEY (`subscriptionId`)
  	REFERENCES `subscription` (`id`)
  	ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `notifications_sent`;
CREATE TABLE `notifications_sent` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `notificationId` varchar(100) NOT NULL,
  `notificationSubjectIdentifier` varchar(100) NOT NULL,
  `notifyingSystem` varchar(256) NOT NULL,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
