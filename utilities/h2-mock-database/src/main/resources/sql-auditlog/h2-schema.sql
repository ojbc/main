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
CREATE TABLE IF NOT EXISTS AuditLog (
  id INT AUTO_INCREMENT PRIMARY KEY,
  origin VARCHAR(100),
  destination VARCHAR(100),
  replyTo VARCHAR(100),
  messageID VARCHAR(100),
  federationID VARCHAR(100),
  employerName VARCHAR(100),
  employerSubUnitName VARCHAR(100),
  userLastName VARCHAR(100),
  userFirstName VARCHAR(100),
  identityProviderID VARCHAR(100),
  hostAddress VARCHAR(100),
  camelContextID VARCHAR(100),
  osgiBundleName VARCHAR(100),
  osgiBundleVersion VARCHAR(20),
  osgiBundleDescription VARCHAR(100),
  soapMessage CLOB,
  timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);