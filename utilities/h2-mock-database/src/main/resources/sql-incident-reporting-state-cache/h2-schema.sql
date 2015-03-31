drop schema if exists incident_reporting_state_cache;

CREATE schema incident_reporting_state_cache;

--
-- Create tables for Person Involvment State
--
CREATE TABLE `Person_Involvement_State` (
  id int(11) NOT NULL AUTO_INCREMENT,
  incident_id varchar(45) DEFAULT NULL,
  person_involvement_hash varchar(100) DEFAULT NULL,
  incident_originating_system_uri varchar(45) DEFAULT NULL,
  person_involvement_activity varchar(45) DEFAULT NULL
);
