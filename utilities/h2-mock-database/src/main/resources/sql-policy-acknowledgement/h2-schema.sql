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
  ori varchar(50) NOT NULL
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
