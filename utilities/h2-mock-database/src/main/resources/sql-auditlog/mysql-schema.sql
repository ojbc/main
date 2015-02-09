/*
 * This is MySQL specific DDL for auditing.  We need to find a permanent place for this
 * but leaving it here for now.
 */

CREATE DATABASE `Audit`; 
use Audit;

DROP TABLE IF EXISTS `AuditLog`;
CREATE TABLE `AuditLog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `origin` varchar(100) DEFAULT NULL,
  `destination` varchar(100) DEFAULT NULL,
  `replyTo` varchar(100) DEFAULT NULL,
  `messageID` varchar(100) DEFAULT NULL,
  `federationID` varchar(100) DEFAULT NULL,
  `employerName` varchar(100) DEFAULT NULL,
  `employerSubUnitName` varchar(100) DEFAULT NULL,
  `userLastName` varchar(100) DEFAULT NULL,
  `userFirstName` varchar(100) DEFAULT NULL,
  `identityProviderID` varchar(100) DEFAULT NULL,
  `hostAddress` varchar(100) DEFAULT NULL,
  `camelContextID` varchar(100) DEFAULT NULL,
  `osgiBundleName` varchar(100) DEFAULT NULL,
  `osgiBundleVersion` varchar(20) DEFAULT NULL,
  `osgiBundleDescription` varchar(100) DEFAULT NULL,
  `soapMessage` mediumtext,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1