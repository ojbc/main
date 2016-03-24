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

CREATE TABLE supervision_bed (
                id INTEGER NOT NULL,
                category_code VARCHAR(10) NOT NULL,
                CONSTRAINT supervision_bed_pk PRIMARY KEY (id)
);


CREATE TABLE language (
                id INTEGER NOT NULL,
                description VARCHAR(50) NOT NULL,
                CONSTRAINT language_pk PRIMARY KEY (id)
);


CREATE TABLE person_race (
                id INTEGER NOT NULL,
                description VARCHAR(10) NOT NULL,
                CONSTRAINT person_race_pk PRIMARY KEY (id)
);


CREATE TABLE person_sex (
                id INTEGER NOT NULL,
                description VARCHAR(20) NOT NULL,
                CONSTRAINT person_sex_pk PRIMARY KEY (id)
);


CREATE TABLE custody_case (
                id INTEGER NOT NULL,
                court_name VARCHAR(50) NOT NULL,
                CONSTRAINT custody_case_pk PRIMARY KEY (id)
);


CREATE TABLE person (
                id INTEGER NOT NULL,
                person_sex_id INTEGER NOT NULL,
                person_race_id INTEGER NOT NULL,
                language_id INTEGER NOT NULL,
                birth_date DATE,
                education_level VARCHAR(50),
                ethnicity VARCHAR(20),
                military_experience BOOLEAN,
                given_name VARCHAR(50),
                middle_name VARCHAR(50),
                sur_name VARCHAR(50),
                resident VARCHAR(50),
                ssn INTEGER,
                state_fingerprint_id VARCHAR(50),
                socio_economic_status VARCHAR(50),
                driver_license_id INTEGER,
                driver_license_source VARCHAR(50),
                fbi_id INTEGER,
                occupation VARCHAR(50),
                CONSTRAINT person_pk PRIMARY KEY (id)
);
COMMENT ON COLUMN person.resident IS 'residence';


CREATE TABLE arrest (
                id INTEGER NOT NULL,
                agency_org_name VARCHAR(50),
                location_address VARCHAR(200),
                location_latitude FLOAT,
                location_longitude FLOAT,
                CONSTRAINT arrest_pk PRIMARY KEY (id)
);


CREATE TABLE charge (
                id INTEGER NOT NULL,
                category VARCHAR(100),
                description VARCHAR(200),
                charge_highest_indicator BOOLEAN,
                sequence_id INTEGER,
                statute_code_id INTEGER,
                statute_code_section_id INTEGER,
                bail_bond_category VARCHAR(50),
                bail_bond_status VARCHAR(50),
                bail_bond_amount FLOAT,
                next_court_date DATE,
                court_name VARCHAR(100),
                info_owning_branch VARCHAR(50),
                info_owning_org VARCHAR(50),
                CONSTRAINT charge_pk PRIMARY KEY (id)
);


CREATE TABLE detention (
                id INTEGER NOT NULL,
                supervision_bed_id INTEGER NOT NULL,
                activity_date DATE,
                supervision_custody_status VARCHAR(10),
                pretrial_category_code VARCHAR(10),
                supervision_release_date DATE,
                supervision_area_id INTEGER,
                supervision_cell_id INTEGER,
                detention_immigration_hold VARCHAR(10),
                hold_for_agency_org_name BOOLEAN,
                inmate_work_release BOOLEAN,
                inmate_worker BOOLEAN,
                CONSTRAINT detention_pk PRIMARY KEY (id)
);


CREATE TABLE booking (
                id INTEGER NOT NULL,
                person_id INTEGER NOT NULL,
                charge_id INTEGER NOT NULL,
                arrest_id INTEGER NOT NULL,
                detention_id INTEGER NOT NULL,
                custody_case_id INTEGER NOT NULL,
                activity_date DATE,
                facility_id INTEGER,
                subject_id INTEGER,
                registered_sex_offender BOOLEAN,
                CONSTRAINT booking_pk PRIMARY KEY (id)
);


ALTER TABLE detention ADD CONSTRAINT supervision_bed_detention_fk
FOREIGN KEY (supervision_bed_id)
REFERENCES supervision_bed (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE person ADD CONSTRAINT language_person_fk
FOREIGN KEY (language_id)
REFERENCES language (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE person ADD CONSTRAINT person_race_person_fk
FOREIGN KEY (person_race_id)
REFERENCES person_race (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE person ADD CONSTRAINT person_sex_person_fk
FOREIGN KEY (person_sex_id)
REFERENCES person_sex (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE booking ADD CONSTRAINT custody_case_booking_fk
FOREIGN KEY (custody_case_id)
REFERENCES custody_case (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE booking ADD CONSTRAINT person_booking_fk
FOREIGN KEY (person_id)
REFERENCES person (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE booking ADD CONSTRAINT arrest_booking_fk
FOREIGN KEY (arrest_id)
REFERENCES arrest (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE booking ADD CONSTRAINT charge_booking_fk
FOREIGN KEY (charge_id)
REFERENCES charge (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE booking ADD CONSTRAINT detention_booking_fk
FOREIGN KEY (detention_id)
REFERENCES detention (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;