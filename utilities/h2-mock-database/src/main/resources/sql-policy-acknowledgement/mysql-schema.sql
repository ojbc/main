/*
 * Unless explicitly acquired and licensed from Licensor under another license, the contents of
 * this file are subject to the Reciprocal Public License ("RPL") Version 1.5, or subsequent
 * versions as allowed by the RPL, and You may not copy or use this file in either source code
 * or executable form, except in compliance with the terms and conditions of the RPL
 *
 * All software distributed under the RPL is provided strictly on an "AS IS" basis, WITHOUT
 * WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY DISCLAIMS ALL SUCH
 * WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific language
 * governing rights and limitations under the RPL.
 *
 * http://opensource.org/licenses/RPL-1.5
 *
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
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
  `civil_ori_indicator` boolean NOT NULL, 
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