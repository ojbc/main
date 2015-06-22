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

-- Incident Type - Placeholder while waiting for code values
insert into IncidentType (IncidentTypeDescription) values ('Placeholder');

-- Agency - Placeholder while waiting for code values
insert into Agency(AgencyName, AgencyORI) values ('Placeholder Agency Name', 'ST123');
insert into Agency(AgencyName, AgencyORI) values ('Some PD', '99999');

-- Involved Drug Type - Placeholder while waiting for code values
insert into InvolvedDrug (InvolvedDrugDescription) values ('opium');
insert into InvolvedDrug (InvolvedDrugDescription) values ('other drugs');
insert into InvolvedDrug (InvolvedDrugDescription) values ('morphine');
insert into InvolvedDrug (InvolvedDrugDescription) values ('LSD');
insert into InvolvedDrug (InvolvedDrugDescription) values ('more than 3 types');
insert into InvolvedDrug (InvolvedDrugDescription) values ('unknown');
insert into InvolvedDrug (InvolvedDrugDescription) values ('other narcotics');
insert into InvolvedDrug (InvolvedDrugDescription) values ('heroin');
insert into InvolvedDrug (InvolvedDrugDescription) values ('hashish');
insert into InvolvedDrug (InvolvedDrugDescription) values ('meth/ amphetamines');
insert into InvolvedDrug (InvolvedDrugDescription) values ('PCP');
insert into InvolvedDrug (InvolvedDrugDescription) values ('barbiturates');
insert into InvolvedDrug (InvolvedDrugDescription) values ('cocaine');
insert into InvolvedDrug (InvolvedDrugDescription) values ('other depressants');
insert into InvolvedDrug (InvolvedDrugDescription) values ('other stimulants');
insert into InvolvedDrug (InvolvedDrugDescription) values ('other hallucingens');
insert into InvolvedDrug (InvolvedDrugDescription) values ('marijuana');
insert into InvolvedDrug (InvolvedDrugDescription) values ('crack cocaine');

-- OffenseType - UCR codes
insert into OffenseType (OffenseDescription) values ('09A');
insert into OffenseType (OffenseDescription) values ('09B');
insert into OffenseType (OffenseDescription) values ('09C');
insert into OffenseType (OffenseDescription) values ('100');
insert into OffenseType (OffenseDescription) values ('11A');
insert into OffenseType (OffenseDescription) values ('11B');
insert into OffenseType (OffenseDescription) values ('11C');
insert into OffenseType (OffenseDescription) values ('11D');
insert into OffenseType (OffenseDescription) values ('120');
insert into OffenseType (OffenseDescription) values ('13A');
insert into OffenseType (OffenseDescription) values ('13B');
insert into OffenseType (OffenseDescription) values ('13C');
insert into OffenseType (OffenseDescription) values ('200');
insert into OffenseType (OffenseDescription) values ('210');
insert into OffenseType (OffenseDescription) values ('220');
insert into OffenseType (OffenseDescription) values ('23C');
insert into OffenseType (OffenseDescription) values ('23D');
insert into OffenseType (OffenseDescription) values ('23E');
insert into OffenseType (OffenseDescription) values ('23F');
insert into OffenseType (OffenseDescription) values ('23G');
insert into OffenseType (OffenseDescription) values ('23H');
insert into OffenseType (OffenseDescription) values ('240');
insert into OffenseType (OffenseDescription) values ('250');
insert into OffenseType (OffenseDescription) values ('26A');
insert into OffenseType (OffenseDescription) values ('26B');
insert into OffenseType (OffenseDescription) values ('26C');
insert into OffenseType (OffenseDescription) values ('26D');
insert into OffenseType (OffenseDescription) values ('26E');
insert into OffenseType (OffenseDescription) values ('270');
insert into OffenseType (OffenseDescription) values ('280');
insert into OffenseType (OffenseDescription) values ('290');
insert into OffenseType (OffenseDescription) values ('35A');
insert into OffenseType (OffenseDescription) values ('35B');
insert into OffenseType (OffenseDescription) values ('36A');
insert into OffenseType (OffenseDescription) values ('36B');
insert into OffenseType (OffenseDescription) values ('370');
insert into OffenseType (OffenseDescription) values ('39A');
insert into OffenseType (OffenseDescription) values ('39B');
insert into OffenseType (OffenseDescription) values ('39C');
insert into OffenseType (OffenseDescription) values ('39D');
insert into OffenseType (OffenseDescription) values ('40A');
insert into OffenseType (OffenseDescription) values ('40B');
insert into OffenseType (OffenseDescription) values ('40C');
insert into OffenseType (OffenseDescription) values ('510');
insert into OffenseType (OffenseDescription) values ('520');
insert into OffenseType (OffenseDescription) values ('64A');
insert into OffenseType (OffenseDescription) values ('64B');
insert into OffenseType (OffenseDescription) values ('90A');
insert into OffenseType (OffenseDescription) values ('90B');
insert into OffenseType (OffenseDescription) values ('90C');
insert into OffenseType (OffenseDescription) values ('90D');
insert into OffenseType (OffenseDescription) values ('90E');
insert into OffenseType (OffenseDescription) values ('90F');
insert into OffenseType (OffenseDescription) values ('90G');
insert into OffenseType (OffenseDescription) values ('90H');
insert into OffenseType (OffenseDescription) values ('90I');
insert into OffenseType (OffenseDescription) values ('90J');
insert into OffenseType (OffenseDescription) values ('90Z');


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
