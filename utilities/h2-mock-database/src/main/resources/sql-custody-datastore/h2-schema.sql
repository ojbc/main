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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */

DROP SCHEMA if EXISTS custody_datastore;

CREATE SCHEMA custody_datastore;

-- TODO see why other db's don't set schema
-- SET SCHEMA custody_datastore;

--generated with mysql grammar to get the auto_increment's
--warnings ignored about nullable foreign keys


CREATE TABLE person (
                id INT AUTO_INCREMENT NOT NULL,
                unique_person_id VARCHAR NOT NULL,
                sex_offender BOOLEAN NOT NULL,
                education VARCHAR(50) NOT NULL,
                primary_language VARCHAR(50) NOT NULL,
                dob DATE NOT NULL,
                ethnicity VARCHAR(20) NOT NULL,
                race VARCHAR(20) NOT NULL,
                sid VARCHAR(50) NOT NULL,
                first_name VARCHAR(50) NOT NULL,
                middle_name VARCHAR(50) NOT NULL,
                last_name VARCHAR(50) NOT NULL,
                sex VARCHAR(1) NOT NULL,
                occupation VARCHAR(50) NOT NULL,
                military_service BOOLEAN NOT NULL,
                PRIMARY KEY (id)
);


CREATE TABLE person_alias (
                id INT AUTO_INCREMENT NOT NULL,
                person_id INT NOT NULL,
                name_type VARCHAR(20) NOT NULL,
                alias_last_name VARCHAR(50) NOT NULL,
                alias_first_name VARCHAR(50) NOT NULL,
                alias_middle VARCHAR(50) NOT NULL,
                alias_sex VARCHAR(1) NOT NULL,
                alias_dob DATE NOT NULL,
                alias_name_id VARCHAR(20) NOT NULL,
                PRIMARY KEY (id)
);


CREATE TABLE booking (
                id INT AUTO_INCREMENT NOT NULL,
                person_id INT NOT NULL,
                booking_number INT NOT NULL,
                booking_date DATE NOT NULL,
                facility VARCHAR(50) NOT NULL,
                booking_photo VARCHAR(250) NOT NULL,
                actual_release_datetime DATE NOT NULL,
                commit_date DATE NOT NULL,
                scheduled_release_date DATE NOT NULL,
                block VARCHAR(20) NOT NULL,
                bed VARCHAR(20) NOT NULL,
                cell VARCHAR(20) NOT NULL,
                case_status VARCHAR(20) NOT NULL,
                inmate_work_release_indicator BOOLEAN NOT NULL,
                inmate_worker_indicator BOOLEAN NOT NULL,
                PRIMARY KEY (id)
);


CREATE TABLE charge (
                id INT AUTO_INCREMENT NOT NULL,
                booking_id INT NOT NULL,
                bond_amount DECIMAL(19,4) NOT NULL,
                bond_type VARCHAR(30) NOT NULL,
                bond_status VARCHAR(30) NOT NULL,
                next_court_event_court_name VARCHAR(30) NOT NULL,
                next_court_date DATE NOT NULL,
                charge_sequence_number INT NOT NULL,
                charge_description VARCHAR(200) NOT NULL,
                statute_or_ordinance_number INT NOT NULL,
                charge_category_classification VARCHAR(200) NOT NULL,
                arrest_location VARCHAR(200) NOT NULL,
                arrest_agency VARCHAR(200) NOT NULL,
                holding_for_agency BOOLEAN NOT NULL,
                case_jurisdiction_court VARCHAR(200) NOT NULL,
                PRIMARY KEY (id)
);


ALTER TABLE booking ADD CONSTRAINT person_booking_fk
FOREIGN KEY (person_id)
REFERENCES person (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE person_alias ADD CONSTRAINT person_person_alias_fk
FOREIGN KEY (person_id)
REFERENCES person (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE charge ADD CONSTRAINT booking_charge_fk
FOREIGN KEY (booking_id)
REFERENCES booking (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;