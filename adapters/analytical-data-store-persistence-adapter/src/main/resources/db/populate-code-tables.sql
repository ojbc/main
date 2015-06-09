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
--
-- Unless explicitly acquired and licensed from Licensor under another license, the contents of
-- this file are subject to the Reciprocal Public License ("RPL") Version 1.5, or subsequent
-- versions as allowed by the RPL, and You may not copy or use this file in either source code
-- or executable form, except in compliance with the terms and conditions of the RPL
--
-- All software distributed under the RPL is provided strictly on an "AS IS" basis, WITHOUT
-- WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY DISCLAIMS ALL SUCH
-- WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
-- PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific language
-- governing rights and limitations under the RPL.
--
-- http://opensource.org/licenses/RPL-1.5
--
-- Copyright 2012-2015 Open Justice Broker Consortium
--

-- Person Sex Codes
insert into PersonSex(PersonSexDescription) values('M');
insert into PersonSex(PersonSexDescription) values('F');
insert into PersonSex(PersonSexDescription) values('U');

-- Person Race Codes
insert into PersonRace(PersonRaceDescription) values('A');
insert into PersonRace(PersonRaceDescription) values('B');
insert into PersonRace(PersonRaceDescription) values('I');
insert into PersonRace(PersonRaceDescription) values('U');
insert into PersonRace(PersonRaceDescription) values('W');

-- Incident Type - Placeholder while waiting for code values
insert into IncidentType (IncidentTypeDescription) values ('Placeholder');

-- Agency - Placeholder while waiting for code values
insert into Agency(AgencyName, AgencyORI) values ('Placeholder Agency Name', 'ST123');
insert into Agency(AgencyName, AgencyORI) values ('Some PD', '99999');

-- Involved Drug Type - Placeholder while waiting for code values
insert into InvolvedDrug (InvolvedDrugDescription) values ('Placeholder Involved Drug');

-- OffenseType - adding a value for testing
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('Violation of a Court Order','N','Infraction');

-- AssessedNeed - adding code table values
insert into AssessedNeed (AssessedNeedDescription) values('housing');
insert into AssessedNeed (AssessedNeedDescription) values('insurance');
insert into AssessedNeed (AssessedNeedDescription) values('medical');
insert into AssessedNeed (AssessedNeedDescription) values('mental health');
insert into AssessedNeed (AssessedNeedDescription) values('substance abuse');

-- County code values for Vermont
insert into County (CountyName) values ('Addison');
insert into County (CountyName) values ('Bennington');
insert into County (CountyName) values ('Caledonia');
insert into County (CountyName) values ('Chittenden');
insert into County (CountyName) values ('Essex');
insert into County (CountyName) values ('Franklin');
insert into County (CountyName) values ('Grand Isle');
insert into County (CountyName) values ('Lamoille');
insert into County (CountyName) values ('Orange');
insert into County (CountyName) values ('Orleans');
insert into County (CountyName) values ('Rutland');
insert into County (CountyName) values ('Washington');
insert into County (CountyName) values ('Windham');
insert into County (CountyName) values ('Windsor');
