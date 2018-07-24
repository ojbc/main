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
CREATE TABLE IF NOT EXISTS `AuditLog` (
  id int identity not null,
  origin varchar(100),
  destination varchar(100),
  replyTo varchar(100),
  messageID varchar(100),
  federationID varchar(100),
  employerName varchar(100),
  employerSubUnitName varchar(100),
  userLastName varchar(100),
  userFirstName varchar(100),
  identityProviderID varchar(100),
  hostAddress varchar(100),
  camelContextID varchar(100),
  osgiBundleName varchar(100),
  osgiBundleVersion varchar(20),
  osgiBundleDescription varchar(100),
  soapMessage clob,
  timestamp TIMESTAMP AS CURRENT_TIMESTAMP() 
);