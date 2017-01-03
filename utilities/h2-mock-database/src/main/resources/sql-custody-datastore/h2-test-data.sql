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

INSERT INTO person(person_unique_identifier, sex_offender, allow_deposits, education, primary_language, dob, ethnicity, eye_color, hair_color, height, weight, race, sid, first_name, middle_name, last_name, sex, occupation, military_service) VALUES
('abc123', false, true,'phd', 'francais', date '2000-01-01', 'H', 'XXX', 'BLK', 54, 150, 'A', '123', 'homer', 'jay', 'simpson', 'M', 'software engineer', 'ACT');

INSERT INTO person(person_unique_identifier, sex_offender, allow_deposits, education, primary_language, dob, ethnicity, eye_color, hair_color, height, weight, race, sid, first_name, middle_name, last_name, sex, occupation, military_service) VALUES
('abc1234', false, true,'phd', 'francais', null, 'H', null, null, null, null, null, null, 'marge', null, 'NullValues', null, 'software engineer', 'ACT');

INSERT INTO booking(person_id, booking_number, booking_date, facility, booking_photo, actual_release_datetime, commit_date, scheduled_release_date, block, bed, cell, case_status, inmate_work_release_indicator, inmate_worker_indicator, probationer_indicator, incarcerated_indicator) values
(1, '1234', date '2000-01-01', 'county jail', 'profile.jpg', date '2000-01-01', date '2000-01-01', date '2000-01-01', 'a', '23', '7', '1', true, true, true, false);

INSERT INTO arrest(booking_id, arrest_unique_identifier, arrest_agency) VALUES (1, '9191919','Chips');

INSERT INTO charge(arrest_id, bond_amount, bond_type, bond_status, next_court_event_court_name, next_court_date, charge_sequence_number, charge_description, statute_or_ordinance_number, charge_category_classification, holding_for_agency, sentence_date, case_jurisdiction_court) VALUES
(1, 499.0000, 'money', 'paid', 'judge judy', DATE '2070-01-01', 456, 'speeding', 'ordinance', 'driving', 'Walker Texas Ranger',  date '2000-01-01', 'matlock');

INSERT INTO person_alias(person_id, name_type, alias_last_name, alias_first_name, alias_middle, alias_sex, alias_dob) VALUES
(1, 'screen name', 'Simpser', 'homy', 'jay', 'M', date '2000-01-01');

INSERT INTO person_alias(person_id, name_type, alias_last_name, alias_first_name, alias_middle, alias_sex, alias_dob) VALUES
(1, 'nickname', 'Simpo', 'homie', 'jo', 'M', date '2000-01-01');

INSERT INTO scars_marks_tattoos(person_id, scars_marks_tattoos_description) VALUES
(1, 'dragon tatoo');

INSERT INTO scars_marks_tattoos(person_id, scars_marks_tattoos_description) VALUES
(1, 'lip piercing');

INSERT INTO conditions(person_id, conditions_description) VALUES
(1, 'condition 1');

INSERT INTO conditions(person_id, conditions_description) VALUES
(1, 'condition 2');