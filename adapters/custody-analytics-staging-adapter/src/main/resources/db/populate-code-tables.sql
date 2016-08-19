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
insert into PersonSexType(PersonSexTypeID, PersonSexTypeDescription) values(1, 'M');
insert into PersonSexType(PersonSexTypeID, PersonSexTypeDescription) values(2, 'F');
insert into PersonSexType(PersonSexTypeID, PersonSexTypeDescription) values(3, 'U');

-- Person Race Codes
insert into PersonRaceType(PersonRaceTypeID, PersonRaceTypeDescription) values(1, '1');
insert into PersonRaceType(PersonRaceTypeID, PersonRaceTypeDescription) values(2, '2');
insert into PersonRaceType(PersonRaceTypeID, PersonRaceTypeDescription) values(3, '3');
insert into PersonRaceType(PersonRaceTypeID, PersonRaceTypeDescription) values(4, '4');
insert into PersonRaceType(PersonRaceTypeID, PersonRaceTypeDescription) values(5, '5');
insert into PersonRaceType(PersonRaceTypeID, PersonRaceTypeDescription) values(6, 'I');
insert into PersonRaceType(PersonRaceTypeID, PersonRaceTypeDescription) values(7, 'A');
insert into PersonRaceType(PersonRaceTypeID, PersonRaceTypeDescription) values(8, 'W');
insert into PersonRaceType(PersonRaceTypeID, PersonRaceTypeDescription) values(9, 'B');
insert into PersonRaceType(PersonRaceTypeID, PersonRaceTypeDescription) values(10, 'U');

-- PersonEthnicityTypeID
insert into PersonEthnicityType(PersonEthnicityTypeID, PersonEthnicityTypeDescription) values(1, 'N');
insert into PersonEthnicityType(PersonEthnicityTypeID, PersonEthnicityTypeDescription) values(2, 'U');
insert into PersonEthnicityType(PersonEthnicityTypeID, PersonEthnicityTypeDescription) values(3, 'H');

-- Agency - Placeholder while waiting for code values
insert into Agency(AgencyID, AgencyDescription) values (1, 'Adams County SO');
insert into Agency(AgencyID, AgencyDescription) values (2, 'Arvada PD');
insert into Agency(AgencyID, AgencyDescription) values (3, 'Brighton PD');
insert into Agency(AgencyID, AgencyDescription) values (4, 'Commerce City PD');
insert into Agency(AgencyID, AgencyDescription) values (5, 'Federal Heights PD');
insert into Agency(AgencyID, AgencyDescription) values (6, 'Northglenn PD');
insert into Agency(AgencyID, AgencyDescription) values (7, 'Thornton PD');
insert into Agency(AgencyID, AgencyDescription) values (8, 'Westminster PD');
insert into Agency(AgencyID, AgencyDescription) values (9, 'Unknown');

-- SupervisionUnitType - adding code table values
insert into SupervisionUnitType (SupervisionUnitTypeID, SupervisionUnitTypeDescription) values('1','Cell Block A');
insert into SupervisionUnitType (SupervisionUnitTypeID, SupervisionUnitTypeDescription) values('2','Cell Block B');
insert into SupervisionUnitType (SupervisionUnitTypeID, SupervisionUnitTypeDescription) values('3','Cell Block C');
insert into SupervisionUnitType (SupervisionUnitTypeID, SupervisionUnitTypeDescription) values('4','Unknown');

-- BondType
insert into BondType (BondTypeDescription) values('Cash');
insert into BondType (BondTypeDescription) values('Surety');
insert into BondType (BondTypeDescription) values('Property');
insert into BondType (BondTypeDescription) values('Deposit');
insert into BondType (BondTypeDescription) values('Unknown');

-- BondStatusType
insert into BondStatusType (BondStatusTypeID, BondStatusTypeDescription) values('1','Active');
insert into BondStatusType (BondStatusTypeID, BondStatusTypeDescription) values('2','Combined Charges');
insert into BondStatusType (BondStatusTypeID, BondStatusTypeDescription) values('3','Deceased');
insert into BondStatusType (BondStatusTypeID, BondStatusTypeDescription) values('4','Investigator Release');
insert into BondStatusType (BondStatusTypeID, BondStatusTypeDescription) values('5','No Charges');
insert into BondStatusType (BondStatusTypeID, BondStatusTypeDescription) values('6','No Probable Cause');
insert into BondStatusType (BondStatusTypeID, BondStatusTypeDescription) values('7','Unknown');

-- CaseStatus
insert into CaseStatusType (CaseStatusTypeID, CaseStatusTypeDescription) values('1','SRP');
insert into CaseStatusType (CaseStatusTypeID, CaseStatusTypeDescription) values('2','C-PR');
insert into CaseStatusType (CaseStatusTypeID, CaseStatusTypeDescription) values('3','S-PR');
insert into CaseStatusType (CaseStatusTypeID, CaseStatusTypeDescription) values('4','PRET');
insert into CaseStatusType (CaseStatusTypeID, CaseStatusTypeDescription) values('5','C-SRP');
insert into CaseStatusType (CaseStatusTypeID, CaseStatusTypeDescription) values('6','Unknown');
insert into CaseStatusType (CaseStatusTypeID, CaseStatusTypeDescription) values('7','S-SRP');
insert into CaseStatusType (CaseStatusTypeID, CaseStatusTypeDescription) values('8','SUR');
insert into CaseStatusType (CaseStatusTypeID, CaseStatusTypeDescription) values('9','Split');

-- Jurisdiction
insert into JurisdictionType (JurisdictionTypeID, JurisdictionTypeDescription) values('1','State Court');
insert into JurisdictionType (JurisdictionTypeID, JurisdictionTypeDescription) values('2','County Court');
insert into JurisdictionType (JurisdictionTypeID, JurisdictionTypeDescription) values('3','Municipal Court');
insert into JurisdictionType (JurisdictionTypeID, JurisdictionTypeDescription) values('4','Case Court');
insert into JurisdictionType (JurisdictionTypeID, JurisdictionTypeDescription) values('5','Smithville');
insert into JurisdictionType (JurisdictionTypeID, JurisdictionTypeDescription) values('9','Unknown');

-- DomicileStatusType
insert into DomicileStatusType (DomicileStatusTypeID, DomicileStatusTypeDescription) values('1','not homeless');
insert into DomicileStatusType (DomicileStatusTypeID, DomicileStatusTypeDescription) values('2','homeless');
insert into DomicileStatusType (DomicileStatusTypeID, DomicileStatusTypeDescription) values('3','Unknown');

-- ProgramEligibilityType
insert into ProgramEligibilityType (ProgramEligibilityTypeID, ProgramEligibilityTypeDescription) values('1','none');
insert into ProgramEligibilityType (ProgramEligibilityTypeID, ProgramEligibilityTypeDescription) values('2','veterans benefits');
insert into ProgramEligibilityType (ProgramEligibilityTypeID, ProgramEligibilityTypeDescription) values('3','Unknown');

-- WorkReleaseStatusType
insert into WorkReleaseStatusType (WorkReleaseStatusTypeID, WorkReleaseStatusTypeDescription) values('1','not assigned');
insert into WorkReleaseStatusType (WorkReleaseStatusTypeID, WorkReleaseStatusTypeDescription) values('2','assigned');
insert into WorkReleaseStatusType (WorkReleaseStatusTypeID, WorkReleaseStatusTypeDescription) values('3','Unknown');

-- SexOffenderStatusType
insert into SexOffenderStatusType (SexOffenderStatusTypeID, SexOffenderStatusTypeDescription) values('1','not registered');
insert into SexOffenderStatusType (SexOffenderStatusTypeID, SexOffenderStatusTypeDescription) values('2','registered');
insert into SexOffenderStatusType (SexOffenderStatusTypeID, SexOffenderStatusTypeDescription) values('3','Unknown');

-- AssessmentCategoryType
insert into AssessmentCategoryType (AssessmentCategoryTypeID, AssessmentCategoryTypeDescription) values('1','Unknown');
insert into AssessmentCategoryType (AssessmentCategoryTypeID, AssessmentCategoryTypeDescription) values('2','General Mental Health');
insert into AssessmentCategoryType (AssessmentCategoryTypeID, AssessmentCategoryTypeDescription) values('3','Substance Abuse');

-- TreatmentAdmissionReasonType
insert into TreatmentAdmissionReasonType (TreatmentAdmissionReasonTypeID, TreatmentAdmissionReasonTypeDescription) values('1','voluntary');
insert into TreatmentAdmissionReasonType (TreatmentAdmissionReasonTypeID, TreatmentAdmissionReasonTypeDescription) values('2','court ordered');
insert into TreatmentAdmissionReasonType (TreatmentAdmissionReasonTypeID, TreatmentAdmissionReasonTypeDescription) values('3','Unknown');

-- TreatmentStatusType
insert into TreatmentStatusType (TreatmentStatusTypeID, TreatmentStatusTypeDescription) values('1','inactive');
insert into TreatmentStatusType (TreatmentStatusTypeID, TreatmentStatusTypeDescription) values('2','active');
insert into TreatmentStatusType (TreatmentStatusTypeID, TreatmentStatusTypeDescription) values('3','Unknown');

-- MedicaidStatusType
insert into MedicaidStatusType (MedicaidStatusTypeID, MedicaidStatusTypeDescription) values('1','not eligible');
insert into MedicaidStatusType (MedicaidStatusTypeID, MedicaidStatusTypeDescription) values('2','eligible');
insert into MedicaidStatusType (MedicaidStatusTypeID, MedicaidStatusTypeDescription) values('3','Unknown');

-- Occupation
insert into OccupationType (OccupationTypeID, OccupationTypeDescription) values('1', 'Truck Driver');
insert into OccupationType (OccupationTypeID, OccupationTypeDescription) values('2', 'OccupationType 2');
insert into OccupationType (OccupationTypeID, OccupationTypeDescription) values('3', 'OccupationType 3');
insert into OccupationType (OccupationTypeID, OccupationTypeDescription) values('4', 'OccupationType 4');
insert into OccupationType (OccupationTypeID, OccupationTypeDescription) values('5', 'OccupationType 5');
insert into OccupationType (OccupationTypeID, OccupationTypeDescription) values('6', 'OccupationType 6');
insert into OccupationType (OccupationTypeID, OccupationTypeDescription) values('7', 'OccupationType 7');
insert into OccupationType (OccupationTypeID, OccupationTypeDescription) values('8', 'OccupationType 8');
insert into OccupationType (OccupationTypeID, OccupationTypeDescription) values('9', 'OccupationType 9');
insert into OccupationType (OccupationTypeID, OccupationTypeDescription) values('10', 'OccupationType 10');
insert into OccupationType (OccupationTypeID, OccupationTypeDescription) values('11', 'OccupationType 11');
insert into OccupationType (OccupationTypeID, OccupationTypeDescription) values('12', 'OccupationType 12');
insert into OccupationType (OccupationTypeID, OccupationTypeDescription) values('98', 'Other');
insert into OccupationType (OccupationTypeID, OccupationTypeDescription) values('99', 'Unknown');

-- EducationLevel
insert into EducationLevelType (EducationLevelTypeID, EducationLevelTypeDescription) values('1', 'High School Graduate');
insert into EducationLevelType (EducationLevelTypeID, EducationLevelTypeDescription) values('2', 'Level 2');
insert into EducationLevelType (EducationLevelTypeID, EducationLevelTypeDescription) values('3', 'Level 3');
insert into EducationLevelType (EducationLevelTypeID, EducationLevelTypeDescription) values('4', 'Level 4');
insert into EducationLevelType (EducationLevelTypeID, EducationLevelTypeDescription) values('5', 'Level 5');
insert into EducationLevelType (EducationLevelTypeID, EducationLevelTypeDescription) values('6', 'Level 6');
insert into EducationLevelType (EducationLevelTypeID, EducationLevelTypeDescription) values('7', 'Level 7');
insert into EducationLevelType (EducationLevelTypeID, EducationLevelTypeDescription) values('8', 'Other');
insert into EducationLevelType (EducationLevelTypeID, EducationLevelTypeDescription) values('9', 'Unknown');

-- Language
insert into LanguageType (LanguageTypeID, LanguageTypeDescription) values('1', 'English');
insert into LanguageType (LanguageTypeID, LanguageTypeDescription) values('2', 'LanguageType 2');
insert into LanguageType (LanguageTypeID, LanguageTypeDescription) values('3', 'LanguageType 3');
insert into LanguageType (LanguageTypeID, LanguageTypeDescription) values('98', 'Other');
insert into LanguageType (LanguageTypeID, LanguageTypeDescription) values('99', 'Unknown');

-- MilitaryServiceStatusType
insert into MilitaryServiceStatusType (MilitaryServiceStatusTypeID, MilitaryServiceStatusTypeDescription) values('1', 'ACT');
insert into MilitaryServiceStatusType (MilitaryServiceStatusTypeID, MilitaryServiceStatusTypeDescription) values('2', 'AWOL');
insert into MilitaryServiceStatusType (MilitaryServiceStatusTypeID, MilitaryServiceStatusTypeDescription) values('3', 'DHD');
insert into MilitaryServiceStatusType (MilitaryServiceStatusTypeID, MilitaryServiceStatusTypeDescription) values('4', 'HD');
insert into MilitaryServiceStatusType (MilitaryServiceStatusTypeID, MilitaryServiceStatusTypeDescription) values('5', 'MD');
insert into MilitaryServiceStatusType (MilitaryServiceStatusTypeID, MilitaryServiceStatusTypeDescription) values('6', 'NACT');
insert into MilitaryServiceStatusType (MilitaryServiceStatusTypeID, MilitaryServiceStatusTypeDescription) values('7', 'RET');
insert into MilitaryServiceStatusType (MilitaryServiceStatusTypeID, MilitaryServiceStatusTypeDescription) values('8', 'Unknown');

-- Facility
insert into Facility (FacilityDescription, Capacity) values('Adams County Jail', 3000);
insert into Facility (FacilityDescription, Capacity) values('Pima County Jail', 5000);


-- ChargeClassType
insert into ChargeClassType (ChargeClassTypeID, ChargeClassTypeDescription) values('1', 'Felony');
insert into ChargeClassType (ChargeClassTypeID, ChargeClassTypeDescription) values('2', 'Misdemeanor');
insert into ChargeClassType (ChargeClassTypeID, ChargeClassTypeDescription) values('3', 'Petty');
insert into ChargeClassType (ChargeClassTypeID, ChargeClassTypeDescription) values('4', 'Traffic');
insert into ChargeClassType (ChargeClassTypeID, ChargeClassTypeDescription) values('99', 'Unknown');
 