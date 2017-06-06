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
DROP schema IF EXISTS policy_acknowledgement; 
DROP table IF EXISTS user_policy_acknowledgement; 
DROP table IF EXISTS ojbc_user;
DROP table IF EXISTS policy; 
DROP table IF EXISTS policy_ori; 
DROP table IF EXISTS ori; 
CREATE schema policy_acknowledgement;

--
-- Create tables for Users with federation ID
--
CREATE TABLE `ojbc_user` (
  id int(11) NOT NULL AUTO_INCREMENT,
  federation_id varchar(100) NOT NULL,
  create_date timestamp AS CURRENT_TIMESTAMP()
);

CREATE UNIQUE INDEX ON ojbc_user(federation_id); 

CREATE TABLE `policy` (
  id int(11) NOT NULL AUTO_INCREMENT,
  policy_uri varchar(512) NOT NULL,
  policy_location varchar(512) NOT NULL,
  active boolean,
  update_date timestamp NOT NULL
);
CREATE UNIQUE INDEX ON policy(policy_uri); 

CREATE TABLE `ori` (
  id int(11) NOT NULL AUTO_INCREMENT,
  ori varchar(50) NOT NULL,
  civil_ori_indicator boolean NOT NULL
);
CREATE UNIQUE INDEX ON ori(ori); 

CREATE TABLE `policy_ori` (
  ori_id int(11) NOT NULL,
  policy_id int(11) NOT NULL, 
  
  FOREIGN KEY (`ori_id`)
  	REFERENCES `ori` (`id`), 
  FOREIGN KEY (`policy_id`)
  	REFERENCES `policy` (`id`) 
);

CREATE TABLE `user_policy_acknowledgement` (
  user_id int(11) NOT NULL,
  policy_id int(11) NOT NULL, 
  acknowledge_date timestamp NOT NULL,
  
  FOREIGN KEY (`user_id`)
  	REFERENCES `ojbc_user` (`id`), 
  FOREIGN KEY (`policy_id`)
  	REFERENCES `policy` (`id`) 
) ;
