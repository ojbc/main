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
INSERT INTO PERSON_SEX(ID, DESCRIPTION) VALUES
(0, 'M'),
(1, 'F');

INSERT INTO PERSON_RACE(ID, DESCRIPTION) VALUES
(0, 'W'),
(1, 'B'),
(2, 'A');

INSERT INTO PUBLIC.LANGUAGE(ID, DESCRIPTION) VALUES
(0, 'EN');

INSERT INTO PUBLIC.PERSON(ID, PERSON_SEX_ID, PERSON_RACE_ID, LANGUAGE_ID, BIRTH_DATE, EDUCATION_LEVEL, ETHNICITY, MILITARY_EXPERIENCE, GIVEN_NAME, MIDDLE_NAME, SUR_NAME, RESIDENT, SSN, STATE_FINGERPRINT_ID, SOCIO_ECONOMIC_STATUS, DRIVER_LICENSE_ID, DRIVER_LICENSE_SOURCE, FBI_ID, OCCUPATION) VALUES
(0, 0, 0, 0, DATE '2000-03-24', 'HS', 'W', TRUE, 'Homer', 'Jay', 'Simpson', '123 Main St.', 123456789, '456', 'High', 123, 'FL', 123, 'Button Pusher'),
(1, 0, 0, 0, DATE '2000-03-24', 'HS', 'W', TRUE, 'Lisa', 'M', 'Simpson', '123 Main St.', 123456789, '456', 'Middle', 123, 'FL', 123, 'Student'),
(2, 0, 0, 0, DATE '2000-03-24', 'HS', 'W', FALSE, 'Bart', 'Roger', 'Simpson', '123 Main St.', 123456789, '456', 'Low', 123, 'FL', 123, 'Student');