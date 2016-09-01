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

Drop schema if exists CustodyAnalyticsDataStore;

CREATE schema CustodyAnalyticsDataStore;

CREATE TABLE PUBLIC.MedicaidStatusType (MedicaidStatusTypeID INT AUTO_INCREMENT NOT NULL, MedicaidStatusTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE PUBLIC.MedicaidStatusType ADD CONSTRAINT medicaidstatustypeid PRIMARY KEY (MedicaidStatusTypeID);

CREATE SEQUENCE PUBLIC.MedicaidStatusType_MedicaidStatusTypeID_seq_1;

CREATE TABLE PUBLIC.AssessmentCategoryType (AssessmentCategoryTypeID INT AUTO_INCREMENT NOT NULL, AssessmentCategoryTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE PUBLIC.AssessmentCategoryType ADD CONSTRAINT assessmentcategorytypeid PRIMARY KEY (AssessmentCategoryTypeID);

CREATE SEQUENCE PUBLIC.AssessmentCategoryType_AssessmentCategoryTypeID_seq;

CREATE TABLE PUBLIC.BondStatusType (BondStatusTypeID INT AUTO_INCREMENT NOT NULL, BondStatusTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE PUBLIC.BondStatusType ADD CONSTRAINT bondstatustypeid PRIMARY KEY (BondStatusTypeID);

CREATE SEQUENCE PUBLIC.BondStatusType_BondStatusTypeID_seq_1_1;

CREATE TABLE PUBLIC.ChargeClassType (ChargeClassTypeID INT AUTO_INCREMENT NOT NULL, ChargeClassTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE PUBLIC.ChargeClassType ADD CONSTRAINT chargeclasstypeid PRIMARY KEY (ChargeClassTypeID);

CREATE SEQUENCE PUBLIC.ChargeClassType_ChargeClassTypeID_seq_2;

CREATE TABLE PUBLIC.TreatmentStatusType (TreatmentStatusTypeID INT AUTO_INCREMENT NOT NULL, TreatmentStatusTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE PUBLIC.TreatmentStatusType ADD CONSTRAINT treatmentstatustypeid PRIMARY KEY (TreatmentStatusTypeID);

CREATE SEQUENCE PUBLIC.TreatmentStatusType_TreatmentStatusTypeID_seq_1;

CREATE TABLE PUBLIC.TreatmentAdmissionReasonType (TreatmentAdmissionReasonTypeID INT AUTO_INCREMENT NOT NULL, TreatmentAdmissionReasonTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE PUBLIC.TreatmentAdmissionReasonType ADD CONSTRAINT treatmentadmissionreasontypeid PRIMARY KEY (TreatmentAdmissionReasonTypeID);

CREATE SEQUENCE PUBLIC.TreatmentAdmissionReasonType_TreatmentAdmissionReasonType_seq;

CREATE TABLE PUBLIC.SexOffenderStatusType (SexOffenderStatusTypeID INT AUTO_INCREMENT NOT NULL, SexOffenderStatusTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE PUBLIC.SexOffenderStatusType ADD CONSTRAINT sexoffenderstatustypeid PRIMARY KEY (SexOffenderStatusTypeID);

CREATE SEQUENCE PUBLIC.SexOffenderStatusType_SexOffenderStatusTypeID_seq;

CREATE TABLE PUBLIC.WorkReleaseStatusType (WorkReleaseStatusTypeID INT AUTO_INCREMENT NOT NULL, WorkReleaseStatusTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE PUBLIC.WorkReleaseStatusType ADD CONSTRAINT workreleasestatustypeid PRIMARY KEY (WorkReleaseStatusTypeID);

CREATE SEQUENCE PUBLIC.WorkReleaseStatusType_WorkReleaseStatusTypeID_seq_1;

CREATE TABLE PUBLIC.ProgramEligibilityType (ProgramEligibilityTypeID INT AUTO_INCREMENT NOT NULL, ProgramEligibilityTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE PUBLIC.ProgramEligibilityType ADD CONSTRAINT programeligibilitytypeid PRIMARY KEY (ProgramEligibilityTypeID);

CREATE SEQUENCE PUBLIC.ProgramEligibilityType_ProgramEligibilityTypeID_seq_1;

CREATE TABLE PUBLIC.DomicileStatusType (DomicileStatusTypeID INT AUTO_INCREMENT NOT NULL, DomicileStatusTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE PUBLIC.DomicileStatusType ADD CONSTRAINT domicilestatustypeid PRIMARY KEY (DomicileStatusTypeID);

CREATE SEQUENCE PUBLIC.DomicileStatusType_DomicileStatusTypeID_seq_1;

CREATE TABLE PUBLIC.PersonEthnicityType (PersonEthnicityTypeID INT AUTO_INCREMENT NOT NULL, PersonEthnicityTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE PUBLIC.PersonEthnicityType ADD CONSTRAINT personethnicitytypeid PRIMARY KEY (PersonEthnicityTypeID);

CREATE SEQUENCE PUBLIC.PersonEthnicityType_PersonEthnicityTypeID_seq_1;

CREATE TABLE PUBLIC.MilitaryServiceStatusType (MilitaryServiceStatusTypeID INT AUTO_INCREMENT NOT NULL, MilitaryServiceStatusTypeDescription VARCHAR(100) NOT NULL);

ALTER TABLE PUBLIC.MilitaryServiceStatusType ADD CONSTRAINT militaryservicestatustypeid PRIMARY KEY (MilitaryServiceStatusTypeID);

CREATE SEQUENCE PUBLIC.MilitaryServiceStatusType_MilitaryServiceStatusTypeID_seq_1_1;

CREATE TABLE PUBLIC.Location (LocationID INT AUTO_INCREMENT NOT NULL, AddressSecondaryUnit VARCHAR(150), StreetNumber VARCHAR(50), StreetName VARCHAR(150), City VARCHAR(100), State VARCHAR(10), PostalCode VARCHAR(10), LocationLatitude NUMBER(14, 10), LocationLongitude NUMBER(14, 10), LocationTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE PUBLIC.Location ADD CONSTRAINT locationid PRIMARY KEY (LocationID);

CREATE SEQUENCE PUBLIC.Location_LocationID_seq_2;

CREATE TABLE PUBLIC.LanguageType (LanguageTypeID INT AUTO_INCREMENT NOT NULL, LanguageTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE PUBLIC.LanguageType ADD CONSTRAINT languagetypeid PRIMARY KEY (LanguageTypeID);

CREATE SEQUENCE PUBLIC.LanguageType_LanguageTypeID_seq;

CREATE TABLE PUBLIC.Facility (FacilityID INT AUTO_INCREMENT NOT NULL, FacilityDescription VARCHAR(100) NOT NULL, Capacity INT DEFAULT 0 NOT NULL);

ALTER TABLE PUBLIC.Facility ADD CONSTRAINT facilityid PRIMARY KEY (FacilityID);

CREATE SEQUENCE PUBLIC.Facility_FacilityID_seq_1;

CREATE TABLE PUBLIC.BondType (BondTypeID INT AUTO_INCREMENT NOT NULL, BondTypeDescription VARCHAR(100) NOT NULL);

ALTER TABLE PUBLIC.BondType ADD CONSTRAINT bondtypeid PRIMARY KEY (BondTypeID);

CREATE SEQUENCE PUBLIC.BondType_BondTypeID_seq_1_1_1_1;

CREATE TABLE PUBLIC.SupervisionUnitType (SupervisionUnitTypeID INT AUTO_INCREMENT NOT NULL, SupervisionUnitTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE PUBLIC.SupervisionUnitType ADD CONSTRAINT supervisionunittypeid PRIMARY KEY (SupervisionUnitTypeID);

CREATE SEQUENCE PUBLIC.SupervisionUnitType_SupervisionUnitTypeID_seq;

CREATE TABLE PUBLIC.PersonRaceType (PersonRaceTypeID INT AUTO_INCREMENT NOT NULL, PersonRaceTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE PUBLIC.PersonRaceType ADD CONSTRAINT personracetypeid PRIMARY KEY (PersonRaceTypeID);

CREATE SEQUENCE PUBLIC.PersonRaceType_PersonRaceTypeID_seq;

CREATE TABLE PUBLIC.PersonSexType (PersonSexTypeID INT AUTO_INCREMENT NOT NULL, PersonSexTypeDescription VARCHAR(7) NOT NULL);

ALTER TABLE PUBLIC.PersonSexType ADD CONSTRAINT personsextypeid PRIMARY KEY (PersonSexTypeID);

CREATE SEQUENCE PUBLIC.PersonSexType_PersonSexTypeID_seq;

CREATE TABLE PUBLIC.Person (PersonID INT AUTO_INCREMENT NOT NULL, PersonUniqueIdentifier VARCHAR(36) NOT NULL, PersonAgeAtBooking INT, PersonBirthDate date, EducationLevel VARCHAR(50), Occupation VARCHAR(50), LanguageTypeID INT, PersonSexTypeID INT, PersonRaceTypeID INT, PersonEthnicityTypeID INT, MilitaryServiceStatusTypeID INT, DomicileStatusTypeID INT, ProgramEligibilityTypeID INT, WorkReleaseStatusTypeID INT, SexOffenderStatusTypeID INT, PersonTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE PUBLIC.Person ADD CONSTRAINT personid PRIMARY KEY (PersonID);

CREATE SEQUENCE PUBLIC.Person_PersonID_seq_1_1;

CREATE TABLE PUBLIC.BehavioralHealthAssessment (BehavioralHealthAssessmentID INT AUTO_INCREMENT NOT NULL, PersonID INT NOT NULL, SeriousMentalIllnessIndicator BOOLEAN, CareEpisodeStartDate date, CareEpisodeEndDate date, MedicaidStatusTypeID INT, EnrolledProviderName VARCHAR(100), BehavioralHealthAssessmentTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE PUBLIC.BehavioralHealthAssessment ADD CONSTRAINT behavioralhealthassessmentid PRIMARY KEY (BehavioralHealthAssessmentID);

CREATE SEQUENCE PUBLIC.BehavioralHealthAssessment_BehavioralHealthAssessmentID_seq;

CREATE TABLE PUBLIC.BehavioralHealthAssessmentCategory (BehavioralHealthAssessmentCategoryID INT AUTO_INCREMENT NOT NULL, BehavioralHealthAssessmentID INT NOT NULL, AssessmentCategoryTypeID INT NOT NULL, BehavioralHealthAssessmentCategoryTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE PUBLIC.BehavioralHealthAssessmentCategory ADD CONSTRAINT behavioralhealthassessmentcategoryid PRIMARY KEY (BehavioralHealthAssessmentCategoryID);

CREATE SEQUENCE PUBLIC.BehavioralHealthAssessmentCategory_BehavioralHealthAssessmentCategoryID_seq;

CREATE TABLE PUBLIC.PrescribedMedication (PrescribedMedicationID INT AUTO_INCREMENT NOT NULL, BehavioralHealthAssessmentID INT NOT NULL, MedicationDescription VARCHAR(80), MedicationDispensingDate date, MedicationDoseMeasure VARCHAR(10), PrescribedMedicationTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE PUBLIC.PrescribedMedication ADD CONSTRAINT prescribedmedicationid PRIMARY KEY (PrescribedMedicationID);

CREATE SEQUENCE PUBLIC.PrescribedMedication_PrescribedMedicationID_seq;

CREATE TABLE PUBLIC.Treatment (TreatmentID INT AUTO_INCREMENT NOT NULL, BehavioralHealthAssessmentID INT NOT NULL, TreatmentStartDate date, TreatmentAdmissionReasonTypeID INT, TreatmentStatusTypeID INT, TreatmentProviderName VARCHAR(100), BehavioralHealthAssessmentTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE PUBLIC.Treatment ADD CONSTRAINT treatmentid PRIMARY KEY (TreatmentID);

CREATE SEQUENCE PUBLIC.Treatment_TreatmentID_seq;

CREATE TABLE PUBLIC.BehavioralHealthEvaluation (BehavioralHealthEvaluationID INT AUTO_INCREMENT NOT NULL, BehavioralHealthAssessmentID INT NOT NULL, BehavioralHealthDiagnosisDescription VARCHAR(50), BehavioralHealthEvaluationTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE PUBLIC.BehavioralHealthEvaluation ADD CONSTRAINT behavioralhealthevaluationid PRIMARY KEY (BehavioralHealthEvaluationID);

CREATE SEQUENCE PUBLIC.BehavioralHealthEvaluation_BehavioralHealthEvaluationID_seq;

CREATE TABLE PUBLIC.JurisdictionType (JurisdictionTypeID INT AUTO_INCREMENT NOT NULL, JurisdictionTypeDescription VARCHAR(100) NOT NULL);

ALTER TABLE PUBLIC.JurisdictionType ADD CONSTRAINT jurisdictiontypeid PRIMARY KEY (JurisdictionTypeID);

CREATE SEQUENCE PUBLIC.JurisdictionType_JurisdictionTypeID_seq;

CREATE TABLE PUBLIC.Agency (AgencyID INT AUTO_INCREMENT NOT NULL, AgencyDescription VARCHAR(50) NOT NULL);

ALTER TABLE PUBLIC.Agency ADD CONSTRAINT agencyid PRIMARY KEY (AgencyID);

CREATE SEQUENCE PUBLIC.Agency_AgencyID_seq;

CREATE TABLE PUBLIC.Booking (BookingID INT AUTO_INCREMENT NOT NULL, BookingNumber VARCHAR(50) NOT NULL, PersonID INT NOT NULL, BookingDateTime TIMESTAMP, ScheduledReleaseDate date, FacilityID INT, SupervisionUnitTypeID INT, InmateJailResidentIndicator BOOLEAN, BookingTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE PUBLIC.Booking ADD CONSTRAINT bookingid PRIMARY KEY (BookingID);

CREATE SEQUENCE PUBLIC.Booking_BookingID_seq;

CREATE TABLE PUBLIC.CustodyRelease (CustodyReleaseID INT AUTO_INCREMENT NOT NULL, BookingID INT NOT NULL, ReleaseDateTime TIMESTAMP, ReleaseCondition VARCHAR(200), CustodyReleaseTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE PUBLIC.CustodyRelease ADD CONSTRAINT custodyreleaseid PRIMARY KEY (CustodyReleaseID);

CREATE SEQUENCE PUBLIC.CustodyRelease_CustodyReleaseID_seq;

CREATE TABLE PUBLIC.CustodyStatusChange (CustodyStatusChangeID INT AUTO_INCREMENT NOT NULL, BookingID INT NOT NULL, PersonID INT NOT NULL, BookingDateTime TIMESTAMP, ScheduledReleaseDate date, FacilityID INT, SupervisionUnitTypeID INT, InmateJailResidentIndicator BOOLEAN, CustodyStatusChangeTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE PUBLIC.CustodyStatusChange ADD CONSTRAINT custodystatuschangeid PRIMARY KEY (CustodyStatusChangeID);

CREATE SEQUENCE PUBLIC.CustodyStatusChange_CustodyStatusChangeID_seq;

CREATE TABLE PUBLIC.CustodyStatusChangeArrest (CustodyStatusChangeArrestID INT AUTO_INCREMENT NOT NULL, CustodyStatusChangeID INT NOT NULL, LocationID INT, ArrestAgencyID INT, CustodyStatusChangeArrestTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE PUBLIC.CustodyStatusChangeArrest ADD CONSTRAINT custodystatuschangearrestid PRIMARY KEY (CustodyStatusChangeArrestID);

CREATE SEQUENCE PUBLIC.CustodyStatusChangeArrest_CustodyStatusChangeArrestID_seq_1;

CREATE TABLE PUBLIC.CustodyStatusChangeCharge (CustodyStatusChangeChargeID INT AUTO_INCREMENT NOT NULL, CustodyStatusChangeArrestID INT NOT NULL, ChargeCode VARCHAR(100), ChargeDisposition VARCHAR(100), AgencyID INT, BondTypeID INT, BondAmount NUMBER(10, 2), BondRemainingAmount NUMBER(10, 2), ChargeJurisdictionTypeID INT, ChargeClassTypeID INT, BondStatusTypeID INT, CustodyStatusChangeChargeTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE PUBLIC.CustodyStatusChangeCharge ADD CONSTRAINT custodystatuschangechargeid PRIMARY KEY (CustodyStatusChangeChargeID);

CREATE SEQUENCE PUBLIC.CustodyStatusChangeCharge_CustodyStatusChangeChargeID_seq;

CREATE TABLE PUBLIC.BookingArrest (BookingArrestID INT AUTO_INCREMENT NOT NULL, BookingID INT NOT NULL, LocationID INT, ArrestAgencyID INT, BookingArrestTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE PUBLIC.BookingArrest ADD CONSTRAINT bookingarrestid PRIMARY KEY (BookingArrestID);

CREATE SEQUENCE PUBLIC.BookingArrest_BookingArrestID_seq_1;

CREATE TABLE PUBLIC.BookingCharge (BookingChargeID INT AUTO_INCREMENT NOT NULL, BookingArrestID INT NOT NULL, ChargeCode VARCHAR(100), ChargeDisposition VARCHAR(100), AgencyID INT, BondTypeID INT, BondAmount NUMBER(10, 2), ChargeJurisdictionTypeID INT, ChargeClassTypeID INT, BondStatusTypeID INT, BookingChargeTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE PUBLIC.BookingCharge ADD CONSTRAINT bookingchargeid PRIMARY KEY (BookingChargeID);

CREATE SEQUENCE PUBLIC.BookingCharge_BookingChargeID_seq;

ALTER TABLE PUBLIC.BehavioralHealthAssessment ADD CONSTRAINT medicaidstatustype_behavioralhealthassessment_fk FOREIGN KEY (MedicaidStatusTypeID) REFERENCES PUBLIC.MedicaidStatusType (MedicaidStatusTypeID);

ALTER TABLE PUBLIC.BehavioralHealthAssessmentCategory ADD CONSTRAINT assessmentcategorytype_behavioralhealthassessmentcategory_fk FOREIGN KEY (AssessmentCategoryTypeID) REFERENCES PUBLIC.AssessmentCategoryType (AssessmentCategoryTypeID);

ALTER TABLE PUBLIC.CustodyStatusChangeCharge ADD CONSTRAINT bondstatustype_custodystatuschangecharge_fk FOREIGN KEY (BondStatusTypeID) REFERENCES PUBLIC.BondStatusType (BondStatusTypeID);

ALTER TABLE PUBLIC.BookingCharge ADD CONSTRAINT bondstatustype_bookingcharge_fk FOREIGN KEY (BondStatusTypeID) REFERENCES PUBLIC.BondStatusType (BondStatusTypeID);

ALTER TABLE PUBLIC.BookingCharge ADD CONSTRAINT chargeclasstype_bookingcharge_fk FOREIGN KEY (ChargeClassTypeID) REFERENCES PUBLIC.ChargeClassType (ChargeClassTypeID);

ALTER TABLE PUBLIC.CustodyStatusChangeCharge ADD CONSTRAINT chargeclasstype_custodystatuschangecharge_fk FOREIGN KEY (ChargeClassTypeID) REFERENCES PUBLIC.ChargeClassType (ChargeClassTypeID);

ALTER TABLE PUBLIC.Treatment ADD CONSTRAINT treatmentstatustype_treatment_fk FOREIGN KEY (TreatmentStatusTypeID) REFERENCES PUBLIC.TreatmentStatusType (TreatmentStatusTypeID);

ALTER TABLE PUBLIC.Treatment ADD CONSTRAINT treatmentinitiationtype_treatment_fk FOREIGN KEY (TreatmentAdmissionReasonTypeID) REFERENCES PUBLIC.TreatmentAdmissionReasonType (TreatmentAdmissionReasonTypeID);

ALTER TABLE PUBLIC.Person ADD CONSTRAINT sexoffenderregistrationstatustype_person_fk FOREIGN KEY (SexOffenderStatusTypeID) REFERENCES PUBLIC.SexOffenderStatusType (SexOffenderStatusTypeID);

ALTER TABLE PUBLIC.Person ADD CONSTRAINT workreleasestatustype_person_fk FOREIGN KEY (WorkReleaseStatusTypeID) REFERENCES PUBLIC.WorkReleaseStatusType (WorkReleaseStatusTypeID);

ALTER TABLE PUBLIC.Person ADD CONSTRAINT programeligibilitytype_person_fk FOREIGN KEY (ProgramEligibilityTypeID) REFERENCES PUBLIC.ProgramEligibilityType (ProgramEligibilityTypeID);

ALTER TABLE PUBLIC.Person ADD CONSTRAINT domicilestatustype_person_fk FOREIGN KEY (DomicileStatusTypeID) REFERENCES PUBLIC.DomicileStatusType (DomicileStatusTypeID);

ALTER TABLE PUBLIC.Person ADD CONSTRAINT personethnicitytype_person_fk FOREIGN KEY (PersonEthnicityTypeID) REFERENCES PUBLIC.PersonEthnicityType (PersonEthnicityTypeID);

ALTER TABLE PUBLIC.Person ADD CONSTRAINT militaryservicestatustype_person_fk FOREIGN KEY (MilitaryServiceStatusTypeID) REFERENCES PUBLIC.MilitaryServiceStatusType (MilitaryServiceStatusTypeID);

ALTER TABLE PUBLIC.BookingArrest ADD CONSTRAINT location_bookingarrest_fk FOREIGN KEY (LocationID) REFERENCES PUBLIC.Location (LocationID);

ALTER TABLE PUBLIC.CustodyStatusChangeArrest ADD CONSTRAINT location_custodystatuschangearrest_fk FOREIGN KEY (LocationID) REFERENCES PUBLIC.Location (LocationID);

ALTER TABLE PUBLIC.Person ADD CONSTRAINT language_person_fk FOREIGN KEY (LanguageTypeID) REFERENCES PUBLIC.LanguageType (LanguageTypeID);

ALTER TABLE PUBLIC.Booking ADD CONSTRAINT facility_booking_fk FOREIGN KEY (FacilityID) REFERENCES PUBLIC.Facility (FacilityID);

ALTER TABLE PUBLIC.CustodyStatusChange ADD CONSTRAINT facility_custody_status_change_fk FOREIGN KEY (FacilityID) REFERENCES PUBLIC.Facility (FacilityID);

ALTER TABLE PUBLIC.BookingCharge ADD CONSTRAINT bondtype_bookingcharge_fk FOREIGN KEY (BondTypeID) REFERENCES PUBLIC.BondType (BondTypeID);

ALTER TABLE PUBLIC.CustodyStatusChangeCharge ADD CONSTRAINT bondtype_custodystatuschangecharge_fk FOREIGN KEY (BondTypeID) REFERENCES PUBLIC.BondType (BondTypeID);

ALTER TABLE PUBLIC.Booking ADD CONSTRAINT bedtype_booking_fk FOREIGN KEY (SupervisionUnitTypeID) REFERENCES PUBLIC.SupervisionUnitType (SupervisionUnitTypeID);

ALTER TABLE PUBLIC.CustodyStatusChange ADD CONSTRAINT bedtype_custody_status_change_fk FOREIGN KEY (SupervisionUnitTypeID) REFERENCES PUBLIC.SupervisionUnitType (SupervisionUnitTypeID);

ALTER TABLE PUBLIC.Person ADD CONSTRAINT personrace_person_fk FOREIGN KEY (PersonRaceTypeID) REFERENCES PUBLIC.PersonRaceType (PersonRaceTypeID);

ALTER TABLE PUBLIC.Person ADD CONSTRAINT personsex_person_fk FOREIGN KEY (PersonSexTypeID) REFERENCES PUBLIC.PersonSexType (PersonSexTypeID);

ALTER TABLE PUBLIC.BehavioralHealthAssessment ADD CONSTRAINT person_behaviorhealthassessment_fk FOREIGN KEY (PersonID) REFERENCES PUBLIC.Person (PersonID);

ALTER TABLE PUBLIC.Booking ADD CONSTRAINT person_booking_fk FOREIGN KEY (PersonID) REFERENCES PUBLIC.Person (PersonID);

ALTER TABLE PUBLIC.CustodyStatusChange ADD CONSTRAINT person_custodystatuschange_fk FOREIGN KEY (PersonID) REFERENCES PUBLIC.Person (PersonID);

ALTER TABLE PUBLIC.BehavioralHealthEvaluation ADD CONSTRAINT behavioralhealthassessment_behavioralhealthevaluation_fk FOREIGN KEY (BehavioralHealthAssessmentID) REFERENCES PUBLIC.BehavioralHealthAssessment (BehavioralHealthAssessmentID);

ALTER TABLE PUBLIC.Treatment ADD CONSTRAINT behavioralhealthassessment_treatment_fk FOREIGN KEY (BehavioralHealthAssessmentID) REFERENCES PUBLIC.BehavioralHealthAssessment (BehavioralHealthAssessmentID);

ALTER TABLE PUBLIC.PrescribedMedication ADD CONSTRAINT behavioralhealthassessment_prescribedmedication_fk FOREIGN KEY (BehavioralHealthAssessmentID) REFERENCES PUBLIC.BehavioralHealthAssessment (BehavioralHealthAssessmentID);

ALTER TABLE PUBLIC.BehavioralHealthAssessmentCategory ADD CONSTRAINT behavioralhealthassessment_behavioralhealthassessmentcategory_fk FOREIGN KEY (BehavioralHealthAssessmentID) REFERENCES PUBLIC.BehavioralHealthAssessment (BehavioralHealthAssessmentID);

ALTER TABLE PUBLIC.BookingCharge ADD CONSTRAINT jurisdictiontype_bookingcharge_fk FOREIGN KEY (ChargeJurisdictionTypeID) REFERENCES PUBLIC.JurisdictionType (JurisdictionTypeID);

ALTER TABLE PUBLIC.CustodyStatusChangeCharge ADD CONSTRAINT jurisdictiontype_custodystatuschangecharge_fk FOREIGN KEY (ChargeJurisdictionTypeID) REFERENCES PUBLIC.JurisdictionType (JurisdictionTypeID);

ALTER TABLE PUBLIC.BookingCharge ADD CONSTRAINT agency_bookingcharge_fk FOREIGN KEY (AgencyID) REFERENCES PUBLIC.Agency (AgencyID);

ALTER TABLE PUBLIC.CustodyStatusChangeCharge ADD CONSTRAINT agency_custodystatuschangecharge_fk FOREIGN KEY (AgencyID) REFERENCES PUBLIC.Agency (AgencyID);

ALTER TABLE PUBLIC.BookingArrest ADD CONSTRAINT agencytype_bookingarrest_fk FOREIGN KEY (ArrestAgencyID) REFERENCES PUBLIC.Agency (AgencyID);

ALTER TABLE PUBLIC.CustodyStatusChangeArrest ADD CONSTRAINT agencytype_custodystatuschangearrest_fk FOREIGN KEY (ArrestAgencyID) REFERENCES PUBLIC.Agency (AgencyID);

ALTER TABLE PUBLIC.BookingArrest ADD CONSTRAINT booking_bookingarrest_fk FOREIGN KEY (BookingID) REFERENCES PUBLIC.Booking (BookingID);

ALTER TABLE PUBLIC.CustodyStatusChange ADD CONSTRAINT booking_custodystatuschange_fk FOREIGN KEY (BookingID) REFERENCES PUBLIC.Booking (BookingID);

ALTER TABLE PUBLIC.CustodyRelease ADD CONSTRAINT booking_custodyrelease_fk FOREIGN KEY (BookingID) REFERENCES PUBLIC.Booking (BookingID);

ALTER TABLE PUBLIC.CustodyStatusChangeArrest ADD CONSTRAINT custodystatuschange_custodystatuschangearrest_fk FOREIGN KEY (CustodyStatusChangeID) REFERENCES PUBLIC.CustodyStatusChange (CustodyStatusChangeID);

ALTER TABLE PUBLIC.CustodyStatusChangeCharge ADD CONSTRAINT custodystatuschangearrest_custodystatuschangecharge_fk FOREIGN KEY (CustodyStatusChangeArrestID) REFERENCES PUBLIC.CustodyStatusChangeArrest (CustodyStatusChangeArrestID);

ALTER TABLE PUBLIC.BookingCharge ADD CONSTRAINT bookingarrest_bookingcharge_fk FOREIGN KEY (BookingArrestID) REFERENCES PUBLIC.BookingArrest (BookingArrestID);