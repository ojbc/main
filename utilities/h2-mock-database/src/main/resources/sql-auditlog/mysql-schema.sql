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
 * Copyright 2012-2017 Open Justice Broker Consortium
 */
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