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

-- Agency - Placeholder while waiting for code values
insert into Agency(AgencyName, AgencyORI) values ('Placeholder Agency Name', 'ST123');
insert into Agency(AgencyName, AgencyORI) values ('Some PD', '99999');

-- AssessedNeed - adding code table values
insert into AssessedNeed (AssessedNeedDescription) values('housing');
insert into AssessedNeed (AssessedNeedDescription) values('insurance');
insert into AssessedNeed (AssessedNeedDescription) values('medical');
insert into AssessedNeed (AssessedNeedDescription) values('mental health');
insert into AssessedNeed (AssessedNeedDescription) values('substance abuse');

-- County code value for test
insert into County (CountyName) values ('Harrison');

-- Disposition Type - placeholder while waiting for code values
insert into DispositionType (DispositionDescription) values ('Convicted');
insert into DispositionType (DispositionDescription) values ('Probation Without Verdict');
insert into DispositionType (DispositionDescription) values ('Not Guilty by Reason of Insanity');
insert into DispositionType (DispositionDescription) values ('Acquitted');
insert into DispositionType (DispositionDescription) values ('Dismissed');
insert into DispositionType (DispositionDescription) values ('Civil Procedure');
insert into DispositionType (DispositionDescription) values ('Off Calendar');
insert into DispositionType (DispositionDescription) values ('Guilty But Mentally Ill');
insert into DispositionType (DispositionDescription) values ('Transferred to Juvenile Court');
insert into DispositionType (DispositionDescription) values ('Mistrial');
insert into DispositionType (DispositionDescription) values ('Nolle Prosequi');
insert into DispositionType (DispositionDescription) values ('Other');
insert into DispositionType (DispositionDescription) values ('Extradited');
insert into DispositionType (DispositionDescription) values ('Not Disposition By Court');
insert into DispositionType (DispositionDescription) values ('Missing/Unknown');

-- PretrialService
insert into PretrialService (PretrialServiceDescription) values ('employment assistance');
insert into PretrialService (PretrialServiceDescription) values ('substance abuse treatment');
insert into PretrialService (PretrialServiceDescription) values ('behavioral health services');
