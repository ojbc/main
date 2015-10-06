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
drop schema if exists publish_subscribe;
CREATE schema publish_subscribe;

--
-- Create tables for subscription notification processor
--
CREATE TABLE `subscription` (
  id int(11) NOT NULL AUTO_INCREMENT,
  topic varchar(100) NOT NULL,
  startDate date NOT NULL,
  endDate date DEFAULT NULL,
  lastValidationDate date DEFAULT NULL,
  subscribingSystemIdentifier varchar(100) NOT NULL,
  subscriptionOwner varchar(100) NOT NULL,
  subjectName varchar(100) NOT NULL,
  active tinyint(4) NOT NULL,
  timestamp TIMESTAMP AS CURRENT_TIMESTAMP(), 
  AGENCY_CASE_NUMBER VARCHAR(100)
);

CREATE TABLE `notification_mechanism` (
  id int(11) NOT NULL AUTO_INCREMENT,
  subscriptionId int(11) NOT NULL,
  notificationMechanismType varchar(20) NOT NULL,
  notificationAddress varchar(256) NOT NULL,
  UNIQUE KEY (`subscriptionId`, `notificationAddress`),
  FOREIGN KEY (subscriptionId)
  	REFERENCES subscription (id)
  	ON DELETE CASCADE
);

CREATE TABLE `subscription_subject_identifier` (
  id int(11) NOT NULL AUTO_INCREMENT,
  subscriptionId int(11) NOT NULL,
  identifierName varchar(100) NOT NULL,
  identifierValue varchar(256) NOT NULL,
  
  FOREIGN KEY (`subscriptionId`)
  	REFERENCES `subscription` (`id`)
  	ON DELETE CASCADE
) ;

CREATE TABLE `notifications_sent` (
  id int(11) NOT NULL AUTO_INCREMENT,
  notificationId varchar(100) NOT NULL,
  notificationSubjectIdentifier varchar(100) NOT NULL,
  notifyingSystem varchar(100) NOT NULL
);
