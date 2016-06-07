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
                id IDENTITY NOT NULL,
                person_unique_identifier VARCHAR(100) NOT NULL,
                sex_offender BOOLEAN,
                allow_deposits BOOLEAN,
                education VARCHAR(50),
                primary_language VARCHAR(50),
                dob DATE,
                ethnicity VARCHAR(20),
                eye_color VARCHAR(40),
                hair_color VARCHAR(40),
                height INTEGER,
                weight INTEGER,
                race VARCHAR(40),
                sid VARCHAR(50),
                first_name VARCHAR(50),
                middle_name VARCHAR(50),
                last_name VARCHAR(50),
                sex VARCHAR(1),
                occupation VARCHAR(50),
                military_service BOOLEAN,
                CONSTRAINT id PRIMARY KEY (id)
);


CREATE TABLE conditions (
                id IDENTITY NOT NULL,
                person_id INTEGER NOT NULL,
                conditions_description VARCHAR(100),
                CONSTRAINT conditions_pk PRIMARY KEY (id)
);


CREATE TABLE scars_marks_tattoos (
                id IDENTITY NOT NULL,
                person_id INTEGER NOT NULL,
                scars_marks_tattoos_description VARCHAR(100),
                CONSTRAINT scars_marks_tattoos_pk PRIMARY KEY (id)
);


CREATE TABLE person_alias (
                id IDENTITY NOT NULL,
                person_id INTEGER NOT NULL,
                name_type VARCHAR(20),
                alias_last_name VARCHAR(50),
                alias_first_name VARCHAR(50),
                alias_middle VARCHAR(50),
                alias_sex VARCHAR(1),
                alias_dob DATE,
                CONSTRAINT id PRIMARY KEY (id)
);


CREATE TABLE booking (
                id IDENTITY NOT NULL,
                person_id INTEGER NOT NULL,
                booking_number INTEGER,
                booking_date DATE,
                facility VARCHAR(50),
                booking_photo VARCHAR(250),
                actual_release_datetime DATE,
                commit_date DATE,
                scheduled_release_date DATE,
                block VARCHAR(20),
                bed VARCHAR(20),
                cell VARCHAR(20),
                case_status VARCHAR(20),
                inmate_work_release_indicator BOOLEAN,
                inmate_worker_indicator BOOLEAN,
                CONSTRAINT id PRIMARY KEY (id)
);


CREATE TABLE charge (
                id IDENTITY NOT NULL,
                booking_id INTEGER NOT NULL,
                bond_amount DECIMAL(19,4),
                bond_type VARCHAR(30),
                bond_status VARCHAR(30),
                next_court_event_court_name VARCHAR(30),
                next_court_date DATE,
                charge_sequence_number INTEGER,
                charge_description VARCHAR(200),
                statute_or_ordinance_number INTEGER,
                charge_category_classification VARCHAR(200),
                arrest_location VARCHAR(200),
                arrest_agency VARCHAR(200),
                holding_for_agency BOOLEAN,
                case_jurisdiction_court VARCHAR(200),
                CONSTRAINT id PRIMARY KEY (id)
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

ALTER TABLE scars_marks_tattoos ADD CONSTRAINT person_scars_marks_tattoos_fk
FOREIGN KEY (person_id)
REFERENCES person (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE conditions ADD CONSTRAINT person_conditions_fk
FOREIGN KEY (person_id)
REFERENCES person (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE charge ADD CONSTRAINT booking_charge_fk
FOREIGN KEY (booking_id)
REFERENCES booking (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;