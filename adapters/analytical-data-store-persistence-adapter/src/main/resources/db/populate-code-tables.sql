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
insert into InvolvedDrug (InvolvedDrugDescription) values ('Placeholder Involved Drug');

-- OffenseType - adding a value for testing
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('Violation of a Court Order','N','Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('Resisting Officer', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('Theft from Building', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('Disorderly Conduct', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('Larceny', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('Drug_Narcotic_Marijuana_selling', 'Y', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('False Report', 'N', 'Infraction');

-- OffenseType - UCR codes
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('09A', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('09B', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('09C', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('100', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('11A', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('11B', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('11C', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('11D', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('120', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('13A', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('13B', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('13C', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('200', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('210', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('220', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('23C', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('23D', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('23E', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('23F', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('23G', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('23H', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('240', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('250', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('26A', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('26B', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('26C', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('26D', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('26E', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('270', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('280', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('290', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('35A', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('35B', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('36A', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('36B', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('370', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('39A', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('39B', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('39C', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('39D', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('40A', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('40B', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('40C', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('510', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('520', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('64A', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('64B', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('90A', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('90B', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('90C', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('90D', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('90E', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('90F', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('90G', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('90H', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('90I', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('90J', 'N', 'Infraction');
insert into OffenseType (OffenseDescription, IsDrugOffense, OffenseSeverity) values ('90Z', 'N', 'Infraction');


-- AssessedNeed - adding code table values
insert into AssessedNeed (AssessedNeedDescription) values('housing');
insert into AssessedNeed (AssessedNeedDescription) values('insurance');
insert into AssessedNeed (AssessedNeedDescription) values('medical');
insert into AssessedNeed (AssessedNeedDescription) values('mental health');
insert into AssessedNeed (AssessedNeedDescription) values('substance abuse');

-- County code value for test
insert into County (CountyName) values ('Harrison');

-- Disposition Type - placeholder while waiting for code values
insert into DispositionType (DispositionDescription, IsConviction) values ('Convicted', 'Y');
insert into DispositionType (DispositionDescription, IsConviction) values ('Probation Without Verdict','N');
insert into DispositionType (DispositionDescription, IsConviction) values ('Not Guilty by Reason of Insanity','N');
insert into DispositionType (DispositionDescription, IsConviction) values ('Acquitted','N');
insert into DispositionType (DispositionDescription, IsConviction) values ('Dismissed','N');
insert into DispositionType (DispositionDescription, IsConviction) values ('Civil Procedure','N');
insert into DispositionType (DispositionDescription, IsConviction) values ('Off Calendar','N');
insert into DispositionType (DispositionDescription, IsConviction) values ('Guilty But Mentally Ill','Y');
insert into DispositionType (DispositionDescription, IsConviction) values ('Transferred to Juvenile Court','N');
insert into DispositionType (DispositionDescription, IsConviction) values ('Mistrial','N');
insert into DispositionType (DispositionDescription, IsConviction) values ('Nolle Prosequi','N');
insert into DispositionType (DispositionDescription, IsConviction) values ('Other','N');
insert into DispositionType (DispositionDescription, IsConviction) values ('Extradited','N');
insert into DispositionType (DispositionDescription, IsConviction) values ('Not Disposition By Court','N');
insert into DispositionType (DispositionDescription, IsConviction) values ('Missing/Unknown','N');

-- PretrialService
insert into PretrialService (PretrialServiceDescription) values ('employment assistance');
insert into PretrialService (PretrialServiceDescription) values ('substance abuse treatment');
insert into PretrialService (PretrialServiceDescription) values ('behavioral health services');
