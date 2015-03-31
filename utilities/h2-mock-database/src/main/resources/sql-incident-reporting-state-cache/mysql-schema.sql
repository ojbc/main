CREATE DATABASE `incident_reporting_state_cache`; 
use incident_reporting_state_cache;

DROP TABLE IF EXISTS `Person_Involvement_State`;
CREATE TABLE `Person_Involvement_State` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `incident_id` varchar(45) DEFAULT NULL,
  `person_involvement_hash` varchar(100) DEFAULT NULL,
  `incident_originating_system_uri` varchar(45) DEFAULT NULL,
  `person_involvement_activity` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1

