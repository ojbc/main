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
DROP SCHEMA IF EXISTS publish_subscribe CASCADE;
CREATE SCHEMA publish_subscribe;
SET SCHEMA publish_subscribe;

CREATE TABLE subscription (
  id IDENTITY PRIMARY KEY,
  topic VARCHAR(100) NOT NULL,
  startDate DATE NOT NULL,
  endDate DATE DEFAULT NULL,
  lastValidationDate DATE DEFAULT NULL,
  subscribingSystemIdentifier VARCHAR(100) NOT NULL,
  subscriptionOwner VARCHAR(100) NOT NULL,
  subjectName VARCHAR(100) NOT NULL,
  active BOOLEAN NOT NULL,
  agency_case_number VARCHAR(100),
  subscription_category_code VARCHAR(2),
  timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE notification_mechanism (
  id IDENTITY PRIMARY KEY,
  subscriptionId INT NOT NULL,
  notificationMechanismType VARCHAR(20) NOT NULL,
  notificationAddress VARCHAR(256) NOT NULL,
  UNIQUE (subscriptionId, notificationAddress),
  FOREIGN KEY (subscriptionId) REFERENCES subscription(id) ON DELETE CASCADE
);

CREATE TABLE subscription_subject_identifier (
  id IDENTITY PRIMARY KEY,
  subscriptionId INT NOT NULL,
  identifierName VARCHAR(100) NOT NULL,
  identifierValue VARCHAR(256) NOT NULL,
  FOREIGN KEY (subscriptionId) REFERENCES subscription(id) ON DELETE CASCADE
);

CREATE TABLE notifications_sent (
  id IDENTITY PRIMARY KEY,
  notificationId VARCHAR(100) NOT NULL,
  notificationSubjectIdentifier VARCHAR(100) NOT NULL,
  notifyingSystem VARCHAR(100) NOT NULL
);

CREATE TABLE subscription_category (
  subscription_category_code VARCHAR(2) PRIMARY KEY,
  subscription_category_description VARCHAR(100) NOT NULL
);

ALTER TABLE subscription
  ADD CONSTRAINT subscription_category_subscription_fk
  FOREIGN KEY (subscription_category_code)
  REFERENCES subscription_category (subscription_category_code);
