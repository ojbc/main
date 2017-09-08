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

CREATE DATABASE `enhanced_auditlog`; 
use enhanced_auditlog;


CREATE TABLE NOTIFICATIONS_SENT (
                NOTIFICATIONS_SENT_ID INT AUTO_INCREMENT NOT NULL,
                SUBSCRIPTION_TYPE VARCHAR(80) NOT NULL,
                TOPIC VARCHAR NOT NULL,
                SUBSCRIPTION_IDENTIFIER INT NOT NULL,
                SUBSCRIPTION_OWNER_AGENCY VARCHAR(80),
                SUBSCRIPTION_OWNER_AGENCY_TYPE VARCHAR(80),
                SUBSCRIPTION_OWNER_EMAIL_ADDRESS VARCHAR(80),
                SUBSCRIPTION_OWNER VARCHAR(80) NOT NULL,
                SUBSCRIBING_SYSTEM_IDENTIFIER VARCHAR(80) NOT NULL,
                PRIMARY KEY (NOTIFICATIONS_SENT_ID)
);


CREATE TABLE NOTIFICATION_MECHANISM (
                NOTIFICATION_MECHANISM_ID INT AUTO_INCREMENT NOT NULL,
                NOTIFICATIONS_SENT_ID INT NOT NULL,
                NOTIFICATION_MECHANSIM VARCHAR(80) NOT NULL,
                NOTIFICATION_ADDRESS VARCHAR(80) NOT NULL,
                NOTIFICATION_RECIPIENT_TYPE VARCHAR(10) NOT NULL,
                PRIMARY KEY (NOTIFICATION_MECHANISM_ID)
);


CREATE TABLE NOTIFICATION_PROPERTIES (
                NOTIFICATION_PROPERTIES_ID INT AUTO_INCREMENT NOT NULL,
                NOTIFICATIONS_SENT_ID INT NOT NULL,
                PROPERTY_NAME VARCHAR(100) NOT NULL,
                PROPERTY_VALUE VARCHAR(100) NOT NULL,
                PRIMARY KEY (NOTIFICATION_PROPERTIES_ID)
);


ALTER TABLE NOTIFICATION_PROPERTIES ADD CONSTRAINT notifications_sent_subscription_properties_fk
FOREIGN KEY (NOTIFICATIONS_SENT_ID)
REFERENCES NOTIFICATIONS_SENT (NOTIFICATIONS_SENT_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE NOTIFICATION_MECHANISM ADD CONSTRAINT notifications_sent_email_address_fk
FOREIGN KEY (NOTIFICATIONS_SENT_ID)
REFERENCES NOTIFICATIONS_SENT (NOTIFICATIONS_SENT_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;