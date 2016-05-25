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

INSERT INTO person(unique_person_id, sex_offender, education, primary_language, dob, ethnicity, race, sid, first_name, middle_name, last_name, sex, occupation, military_service) VALUES
('123', false, 'phd', 'francais', date '2000-01-01', 'a', 'a', '123', 'homer', 'jay', 'simpson', 'm', 'software engineer', false);

INSERT INTO booking(person_id, booking_number, booking_date, facility, booking_photo, actual_release_datetime, commit_date, scheduled_release_date, block, bed, cell, case_status, inmate_work_release_indicator, inmate_worker_indicator) values
(null, 1234, date '2000-01-01', 'county jail', 'profile.jpg', date '2000-01-01', date '2000-01-01', date '2000-01-01', 'a', '23', '7', '1', true, true);

INSERT INTO charge(booking_id, bond_amount, bond_type, bond_status, next_court_event_court_name, next_court_date, charge_sequence_number, charge_description, statute_or_ordinance_number, charge_category_classification, arrest_location, arrest_agency, holding_for_agency, case_jurisdiction_court) VALUES
(null, 499.0000, 'money', 'paid', 'judge judy', date '2070-01-01', 456, 'speeding', 987, 'driving', 'phoenix', 'chips', true, 'matlock');

INSERT INTO person_alias(person_id, name_type, alias_last_name, alias_first_name, alias_middle, alias_sex, alias_dob, alias_name_id) VALUES
(null, 'screen name', 'simpson', 'homer', 'jay', 'm', date '2000-01-01', '123');