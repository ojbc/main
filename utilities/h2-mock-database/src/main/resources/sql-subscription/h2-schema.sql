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
  timestamp TIMESTAMP AS CURRENT_TIMESTAMP() 
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
