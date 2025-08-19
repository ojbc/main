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
DROP SCHEMA IF EXISTS policy_acknowledgement CASCADE;

CREATE SCHEMA policy_acknowledgement;
SET SCHEMA policy_acknowledgement;

--
-- Create tables for Users with federation ID
--
CREATE TABLE ojbc_user (
  id INT AUTO_INCREMENT PRIMARY KEY,
  federation_id VARCHAR(100) NOT NULL,
  create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX idx_user_federation_id ON ojbc_user(federation_id);

CREATE TABLE policy (
  id INT AUTO_INCREMENT PRIMARY KEY,
  policy_uri VARCHAR(512) NOT NULL,
  policy_location VARCHAR(512) NOT NULL,
  active BOOLEAN,
  update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX idx_policy_uri ON policy(policy_uri);

CREATE TABLE ori (
  id INT AUTO_INCREMENT PRIMARY KEY,
  ori VARCHAR(50) NOT NULL,
  civil_ori_indicator BOOLEAN NOT NULL
);

CREATE UNIQUE INDEX idx_ori ON ori(ori);

CREATE TABLE policy_ori (
  ori_id INT NOT NULL,
  policy_id INT NOT NULL,
  
  FOREIGN KEY (ori_id) REFERENCES ori(id),
  FOREIGN KEY (policy_id) REFERENCES policy(id)
);

CREATE TABLE user_policy_acknowledgement (
  user_id INT NOT NULL,
  policy_id INT NOT NULL,
  acknowledge_date TIMESTAMP NOT NULL,

  FOREIGN KEY (user_id) REFERENCES ojbc_user(id),
  FOREIGN KEY (policy_id) REFERENCES policy(id)
);
