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
SET SCHEMA custody_datastore;

CREATE TABLE supervision_bed (
                id INTEGER NOT NULL,
                category_code VARCHAR(10) NOT NULL,
                CONSTRAINT supervision_bed_pk PRIMARY KEY (id)
);


CREATE TABLE occupation (
                id INTEGER NOT NULL,
                description VARCHAR(50) NOT NULL,
                CONSTRAINT occupation_pk PRIMARY KEY (id)
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


CREATE TABLE info_owning_org (
                id INTEGER NOT NULL,
                org_branch_name VARCHAR(100) NOT NULL,
                org_name VARCHAR(100) NOT NULL,
                CONSTRAINT info_owning_org_pk PRIMARY KEY (id)
);


CREATE TABLE custody_case (
                id INTEGER NOT NULL,
                court_name VARCHAR(50) NOT NULL,
                CONSTRAINT custody_case_pk PRIMARY KEY (id)
);


CREATE TABLE location (
                id INTEGER NOT NULL,
                address_full VARCHAR(200) NOT NULL,
                latitude FLOAT NOT NULL,
                longitude FLOAT NOT NULL,
                CONSTRAINT location_pk PRIMARY KEY (id)
);


CREATE TABLE person (
                id INTEGER NOT NULL,
                person_sex_id INTEGER NOT NULL,
                person_race_id INTEGER NOT NULL,
                language_id INTEGER NOT NULL,
                occupation_id INTEGER NOT NULL,
                birth_date DATE NOT NULL,
                education_level VARCHAR(50) NOT NULL,
                ethnicity VARCHAR(20) NOT NULL,
                military_experience BOOLEAN NOT NULL,
                given_name VARCHAR(50) NOT NULL,
                middle_name VARCHAR(50) NOT NULL,
                sur_name VARCHAR(50) NOT NULL,
                race_code VARCHAR(10) NOT NULL,
                resident VARCHAR(50) NOT NULL,
                ssn INTEGER NOT NULL,
                state_fingerprint_id VARCHAR(50) NOT NULL,
                socio_economic_status VARCHAR(50) NOT NULL,
                driver_license_id INTEGER NOT NULL,
                driver_license_source VARCHAR(50) NOT NULL,
                fbi_id INTEGER NOT NULL,
                CONSTRAINT person_pk PRIMARY KEY (id)
);
COMMENT ON COLUMN person.resident IS 'residence';


CREATE TABLE next_court_event (
                id INTEGER NOT NULL,
                activity_date DATE NOT NULL,
                court_name VARCHAR(100) NOT NULL,
                CONSTRAINT next_court_event_pk PRIMARY KEY (id)
);


CREATE TABLE person_criminal_history_summary (
                id INTEGER NOT NULL,
                registered_sex_offender BOOLEAN NOT NULL,
                CONSTRAINT person_criminal_history_summary_pk PRIMARY KEY (id)
);


CREATE TABLE arrest (
                id INTEGER NOT NULL,
                agency_org_name VARCHAR(50) NOT NULL,
                location_id INTEGER NOT NULL,
                CONSTRAINT arrest_pk PRIMARY KEY (id)
);


CREATE TABLE charge (
                id INTEGER NOT NULL,
                category VARCHAR(100) NOT NULL,
                description VARCHAR(200) NOT NULL,
                charge_highest_indicator BOOLEAN NOT NULL,
                sequence_id INTEGER NOT NULL,
                statute_code_id INTEGER NOT NULL,
                statute_code_section_id INTEGER NOT NULL,
                CONSTRAINT charge_pk PRIMARY KEY (id)
);


CREATE TABLE activity_charge_association (
                id INTEGER NOT NULL,
                next_court_event_id INTEGER NOT NULL,
                charge_id INTEGER NOT NULL,
                info_own_org_id INTEGER NOT NULL,
                CONSTRAINT activity_charge_association_pk PRIMARY KEY (id)
);


CREATE TABLE bail_bond (
                id INTEGER NOT NULL,
                activity_category VARCHAR(50) NOT NULL,
                activity_status VARCHAR(50) NOT NULL,
                amount FLOAT NOT NULL,
                CONSTRAINT bail_bond_pk PRIMARY KEY (id)
);
COMMENT ON COLUMN bail_bond.activity_category IS 'bond type';
COMMENT ON COLUMN bail_bond.activity_status IS 'bond status';


CREATE TABLE bail_bond_charge_association (
                id INTEGER NOT NULL,
                bail_bond_id INTEGER NOT NULL,
                charge_id INTEGER NOT NULL,
                CONSTRAINT bail_bond_charge_association_pk PRIMARY KEY (id)
);


CREATE TABLE detention (
                id INTEGER NOT NULL,
                supervision_bed_id INTEGER NOT NULL,
                activity_date DATE NOT NULL,
                supervision_custody_status VARCHAR(10) NOT NULL,
                pretrial_category_code VARCHAR(10) NOT NULL,
                supervision_release_date DATE NOT NULL,
                supervision_area_id INTEGER NOT NULL,
                supervision_cell_id INTEGER NOT NULL,
                detention_immigration_hold VARCHAR(10) NOT NULL,
                hold_for_agency_org_name BOOLEAN NOT NULL,
                inmate_work_release BOOLEAN NOT NULL,
                inmate_worker BOOLEAN NOT NULL,
                CONSTRAINT detention_pk PRIMARY KEY (id)
);


CREATE TABLE booking (
                id INTEGER NOT NULL,
                activity_date DATE NOT NULL,
                facility_id INTEGER NOT NULL,
                subject_id INTEGER NOT NULL,
                person_id INTEGER NOT NULL,
                custody_case_id INTEGER NOT NULL,
                detention_id INTEGER NOT NULL,
                arrest_id INTEGER NOT NULL,
                person_ch_sum_id INTEGER NOT NULL,
                activity_charge_assoc_id INTEGER NOT NULL,
                CONSTRAINT booking_pk PRIMARY KEY (id)
);


ALTER TABLE detention ADD CONSTRAINT supervision_bed_detention_fk
FOREIGN KEY (supervision_bed_id)
REFERENCES supervision_bed (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE person ADD CONSTRAINT occupation_person_fk
FOREIGN KEY (occupation_id)
REFERENCES occupation (id)
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

ALTER TABLE activity_charge_association ADD CONSTRAINT info_owning_org_activity_charge_association_fk
FOREIGN KEY (info_own_org_id)
REFERENCES info_owning_org (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE booking ADD CONSTRAINT custody_case_booking_fk
FOREIGN KEY (custody_case_id)
REFERENCES custody_case (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE arrest ADD CONSTRAINT location_arrest_fk
FOREIGN KEY (location_id)
REFERENCES location (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE booking ADD CONSTRAINT person_booking_fk
FOREIGN KEY (person_id)
REFERENCES person (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE activity_charge_association ADD CONSTRAINT next_court_event_activity_charge_association_fk
FOREIGN KEY (next_court_event_id)
REFERENCES next_court_event (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE booking ADD CONSTRAINT person_criminal_history_summary_booking_fk
FOREIGN KEY (person_ch_sum_id)
REFERENCES person_criminal_history_summary (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE booking ADD CONSTRAINT arrest_booking_fk
FOREIGN KEY (arrest_id)
REFERENCES arrest (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE activity_charge_association ADD CONSTRAINT charge_activity_charge_association_fk
FOREIGN KEY (charge_id)
REFERENCES charge (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE bail_bond_charge_association ADD CONSTRAINT charge_bail_bond_charge_association_fk
FOREIGN KEY (charge_id)
REFERENCES charge (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE booking ADD CONSTRAINT activity_charge_association_booking_fk
FOREIGN KEY (activity_charge_assoc_id)
REFERENCES activity_charge_association (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE bail_bond_charge_association ADD CONSTRAINT bail_bond_bail_bond_charge_association_fk
FOREIGN KEY (bail_bond_id)
REFERENCES bail_bond (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE booking ADD CONSTRAINT detention_booking_fk
FOREIGN KEY (detention_id)
REFERENCES detention (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;