drop DATABASE IF EXISTS `policy_acknowledgement`;
CREATE DATABASE `policy_acknowledgement`;
USE `policy_acknowledgement`;

--
-- Create tables for Policy Acknowledgement
--

DROP TABLE IF EXISTS `ojbc_user`;
CREATE TABLE `ojbc_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `federation_id` varchar(100) NOT NULL,
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `policy`;
CREATE TABLE `policy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `policy_uri` varchar(512) NOT NULL,
  `policy_location` varchar(512) NOT NULL,
  `active` boolean,
  `update_date` timestamp NOT NULL,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `ori`;
CREATE TABLE `ori`(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ori` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX (`ori`)
)ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `policy-ori`;
CREATE TABLE `policy_ori` (
  `ori_id` int(11) NOT NULL,
  `policy_id` int(11) NOT NULL,
  
  UNIQUE INDEX (`ori_id`, `policy_id`),
  FOREIGN KEY (`ori_id`)
  	REFERENCES `ori` (`id`),
  FOREIGN KEY (`policy_id`)
  	REFERENCES `policy` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `user_policy_acknowledgement`;
CREATE TABLE `user_policy_acknowledgement` (
  `user_id` int(11) NOT NULL,
  `policy_id` int(11) NOT NULL,
  `acknowledge_date` timestamp NOT NULL,
  
  UNIQUE INDEX (`user_id`, `policy_id`),
  FOREIGN KEY (`user_id`)
  	REFERENCES `ojbc_user` (`id`),
  FOREIGN KEY (`policy_id`)
  	REFERENCES `policy` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;