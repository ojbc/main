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

CREATE TABLE MedicaidStatusType (MedicaidStatusTypeID INT NOT NULL, MedicaidStatusTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE MedicaidStatusType ADD CONSTRAINT medicaidstatustypeid PRIMARY KEY (MedicaidStatusTypeID);

CREATE TABLE AssessmentCategoryType (AssessmentCategoryTypeID INT NOT NULL, AssessmentCategoryTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE AssessmentCategoryType ADD CONSTRAINT assessmentcategorytypeid PRIMARY KEY (AssessmentCategoryTypeID);

CREATE TABLE BondStatusType (BondStatusTypeID INT NOT NULL, BondStatusTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE BondStatusType ADD CONSTRAINT bondstatustypeid PRIMARY KEY (BondStatusTypeID);

CREATE TABLE ChargeClassType (ChargeClassTypeID INT AUTO_INCREMENT NOT NULL, ChargeClassTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE ChargeClassType ADD CONSTRAINT chargeclasstypeid PRIMARY KEY (ChargeClassTypeID);

CREATE SEQUENCE ChargeClassType_ChargeClassTypeID_seq_2;

CREATE TABLE TreatmentStatusType (TreatmentStatusTypeID INT AUTO_INCREMENT NOT NULL, TreatmentStatusTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE TreatmentStatusType ADD CONSTRAINT treatmentstatustypeid PRIMARY KEY (TreatmentStatusTypeID);

CREATE SEQUENCE TreatmentStatusType_TreatmentStatusTypeID_seq_1;

CREATE TABLE TreatmentAdmissionReasonType (TreatmentAdmissionReasonTypeID INT AUTO_INCREMENT NOT NULL, TreatmentAdmissionReasonTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE TreatmentAdmissionReasonType ADD CONSTRAINT treatmentadmissionreasontypeid PRIMARY KEY (TreatmentAdmissionReasonTypeID);

CREATE SEQUENCE TreatmentAdmissionReasonType_TreatmentAdmissionReasonType_seq;

CREATE TABLE SexOffenderStatusType (SexOffenderStatusTypeID INT AUTO_INCREMENT NOT NULL, SexOffenderStatusTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE SexOffenderStatusType ADD CONSTRAINT sexoffenderstatustypeid PRIMARY KEY (SexOffenderStatusTypeID);

CREATE SEQUENCE SexOffenderStatusType_SexOffenderStatusTypeID_seq;

CREATE TABLE WorkReleaseStatusType (WorkReleaseStatusTypeID INT AUTO_INCREMENT NOT NULL, WorkReleaseStatusTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE WorkReleaseStatusType ADD CONSTRAINT workreleasestatustypeid PRIMARY KEY (WorkReleaseStatusTypeID);

CREATE SEQUENCE WorkReleaseStatusType_WorkReleaseStatusTypeID_seq_1;

CREATE TABLE ProgramEligibilityType (ProgramEligibilityTypeID INT AUTO_INCREMENT NOT NULL, ProgramEligibilityTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE ProgramEligibilityType ADD CONSTRAINT programeligibilitytypeid PRIMARY KEY (ProgramEligibilityTypeID);

CREATE SEQUENCE ProgramEligibilityType_ProgramEligibilityTypeID_seq_1;

CREATE TABLE DomicileStatusType (DomicileStatusTypeID INT AUTO_INCREMENT NOT NULL, DomicileStatusTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE DomicileStatusType ADD CONSTRAINT domicilestatustypeid PRIMARY KEY (DomicileStatusTypeID);

CREATE SEQUENCE DomicileStatusType_DomicileStatusTypeID_seq_1;

CREATE TABLE PersonEthnicityType (PersonEthnicityTypeID INT NOT NULL, PersonEthnicityTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE PersonEthnicityType ADD CONSTRAINT personethnicitytypeid PRIMARY KEY (PersonEthnicityTypeID);

CREATE TABLE MilitaryServiceStatusType (MilitaryServiceStatusTypeID INT AUTO_INCREMENT NOT NULL, MilitaryServiceStatusTypeDescription VARCHAR(100) NOT NULL);

ALTER TABLE MilitaryServiceStatusType ADD CONSTRAINT militaryservicestatustypeid PRIMARY KEY (MilitaryServiceStatusTypeID);

CREATE SEQUENCE MilitaryServiceStatusType_MilitaryServiceStatusTypeID_seq_1_1;

CREATE TABLE Location (LocationID INT AUTO_INCREMENT NOT NULL, AddressSecondaryUnit VARCHAR(150), StreetNumber VARCHAR(50), StreetName VARCHAR(150), City VARCHAR(100), State VARCHAR(10), PostalCode VARCHAR(10), LocationLatitude NUMBER(14, 10), LocationLongitude NUMBER(14, 10), LocationTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE Location ADD CONSTRAINT locationid PRIMARY KEY (LocationID);

CREATE SEQUENCE Location_LocationID_seq_2;

CREATE TABLE LanguageType (LanguageTypeID INT AUTO_INCREMENT NOT NULL, LanguageTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE LanguageType ADD CONSTRAINT languagetypeid PRIMARY KEY (LanguageTypeID);

CREATE SEQUENCE LanguageType_LanguageTypeID_seq;

CREATE TABLE Facility (FacilityID INT AUTO_INCREMENT NOT NULL, FacilityDescription VARCHAR(100) NOT NULL, Capacity INT DEFAULT 0 NOT NULL);

ALTER TABLE Facility ADD CONSTRAINT facilityid PRIMARY KEY (FacilityID);

CREATE SEQUENCE Facility_FacilityID_seq_1;

CREATE TABLE BondType (BondTypeID INT AUTO_INCREMENT NOT NULL, BondTypeDescription VARCHAR(100) NOT NULL);

ALTER TABLE BondType ADD CONSTRAINT bondtypeid PRIMARY KEY (BondTypeID);

CREATE SEQUENCE BondType_BondTypeID_seq_1_1_1_1;

CREATE TABLE SupervisionUnitType (SupervisionUnitTypeID INT AUTO_INCREMENT NOT NULL, SupervisionUnitTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE SupervisionUnitType ADD CONSTRAINT supervisionunittypeid PRIMARY KEY (SupervisionUnitTypeID);

CREATE SEQUENCE SupervisionUnitType_SupervisionUnitTypeID_seq;

CREATE TABLE PersonRaceType (PersonRaceTypeID INT AUTO_INCREMENT NOT NULL, PersonRaceTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE PersonRaceType ADD CONSTRAINT personracetypeid PRIMARY KEY (PersonRaceTypeID);

CREATE SEQUENCE PersonRaceType_PersonRaceTypeID_seq;

CREATE TABLE PersonSexType (PersonSexTypeID INT AUTO_INCREMENT NOT NULL, PersonSexTypeDescription VARCHAR(7) NOT NULL);

ALTER TABLE PersonSexType ADD CONSTRAINT personsextypeid PRIMARY KEY (PersonSexTypeID);

CREATE SEQUENCE PersonSexType_PersonSexTypeID_seq;

CREATE TABLE Person (PersonID INT AUTO_INCREMENT NOT NULL, PersonUniqueIdentifier VARCHAR(36) NOT NULL, PersonAgeAtBooking INT, PersonBirthDate date, EducationLevel VARCHAR(50), Occupation VARCHAR(50), LanguageTypeID INT, PersonSexTypeID INT, PersonRaceTypeID INT, PersonEthnicityTypeID INT, MilitaryServiceStatusTypeID INT, DomicileStatusTypeID INT, ProgramEligibilityTypeID INT, WorkReleaseStatusTypeID INT, SexOffenderStatusTypeID INT, PersonTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE Person ADD CONSTRAINT personid PRIMARY KEY (PersonID);

CREATE SEQUENCE Person_PersonID_seq_1_1;

CREATE TABLE BehavioralHealthAssessment (BehavioralHealthAssessmentID INT AUTO_INCREMENT NOT NULL, PersonID INT NOT NULL, SeriousMentalIllnessIndicator BOOLEAN, CareEpisodeStartDate date, CareEpisodeEndDate date, MedicaidStatusTypeID INT, EnrolledProviderName VARCHAR(100), BehavioralHealthAssessmentTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE BehavioralHealthAssessment ADD CONSTRAINT behavioralhealthassessmentid PRIMARY KEY (BehavioralHealthAssessmentID);

CREATE SEQUENCE BehavioralHealthAssessment_BehavioralHealthAssessmentID_seq;

CREATE TABLE BehavioralHealthAssessmentCategory (BehavioralHealthAssessmentCategoryID INT NOT NULL, BehavioralHealthAssessmentID INT NOT NULL, AssessmentCategoryTypeID INT NOT NULL, BehavioralHealthAssessmentCategoryTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE BehavioralHealthAssessmentCategory ADD CONSTRAINT behavioralhealthassessmentcategoryid PRIMARY KEY (BehavioralHealthAssessmentCategoryID);

CREATE TABLE PrescribedMedication (PrescribedMedicationID INT AUTO_INCREMENT NOT NULL, BehavioralHealthAssessmentID INT NOT NULL, MedicationDescription VARCHAR(80), MedicationDispensingDate date, MedicationDoseMeasure VARCHAR(10), PrescribedMedicationTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE PrescribedMedication ADD CONSTRAINT prescribedmedicationid PRIMARY KEY (PrescribedMedicationID);

CREATE SEQUENCE PrescribedMedication_PrescribedMedicationID_seq;

CREATE TABLE Treatment (TreatmentID INT AUTO_INCREMENT NOT NULL, BehavioralHealthAssessmentID INT NOT NULL, TreatmentStartDate date, TreatmentAdmissionReasonTypeID INT, TreatmentStatusTypeID INT, TreatmentProviderName VARCHAR(100), BehavioralHealthAssessmentTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE Treatment ADD CONSTRAINT treatmentid PRIMARY KEY (TreatmentID);

CREATE SEQUENCE Treatment_TreatmentID_seq;

CREATE TABLE BehavioralHealthEvaluation (BehavioralHealthEvaluationID INT AUTO_INCREMENT NOT NULL, BehavioralHealthAssessmentID INT NOT NULL, BehavioralHealthDiagnosisDescription VARCHAR(50), BehavioralHealthEvaluationTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE BehavioralHealthEvaluation ADD CONSTRAINT behavioralhealthevaluationid PRIMARY KEY (BehavioralHealthEvaluationID);

CREATE SEQUENCE BehavioralHealthEvaluation_BehavioralHealthEvaluationID_seq;

CREATE TABLE JurisdictionType (JurisdictionTypeID INT AUTO_INCREMENT NOT NULL, JurisdictionTypeDescription VARCHAR(100) NOT NULL);

ALTER TABLE JurisdictionType ADD CONSTRAINT jurisdictiontypeid PRIMARY KEY (JurisdictionTypeID);

CREATE SEQUENCE JurisdictionType_JurisdictionTypeID_seq;

CREATE TABLE Agency (AgencyID INT AUTO_INCREMENT NOT NULL, AgencyDescription VARCHAR(50) NOT NULL);

ALTER TABLE Agency ADD CONSTRAINT agencyid PRIMARY KEY (AgencyID);

CREATE SEQUENCE Agency_AgencyID_seq;

CREATE TABLE Booking (BookingID INT AUTO_INCREMENT NOT NULL, BookingNumber VARCHAR(50) NOT NULL, PersonID INT NOT NULL, BookingDate date NOT NULL, BookingTime time, ScheduledReleaseDate date, FacilityID INT, SupervisionUnitTypeID INT, InmateJailResidentIndicator BOOLEAN, BookingTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE Booking ADD CONSTRAINT bookingid PRIMARY KEY (BookingID);

CREATE SEQUENCE Booking_BookingID_seq;

CREATE TABLE CustodyRelease (CustodyReleaseID INT AUTO_INCREMENT NOT NULL, BookingID INT NOT NULL, ReleaseDate date NOT NULL, ReleaseTime time, ReleaseCondition VARCHAR(200), CustodyReleaseTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE CustodyRelease ADD CONSTRAINT custodyreleaseid PRIMARY KEY (CustodyReleaseID);

CREATE SEQUENCE CustodyRelease_CustodyReleaseID_seq;

CREATE TABLE CustodyStatusChange (CustodyStatusChangeID INT AUTO_INCREMENT NOT NULL, BookingID INT NOT NULL, PersonID INT NOT NULL, BookingDate date NOT NULL, BookingTime time, ScheduledReleaseDate date, FacilityID INT, SupervisionUnitTypeID INT, InmateJailResidentIndicator BOOLEAN, CustodyStatusChangeTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE CustodyStatusChange ADD CONSTRAINT custodystatuschangeid PRIMARY KEY (CustodyStatusChangeID);

CREATE SEQUENCE CustodyStatusChange_CustodyStatusChangeID_seq;

CREATE TABLE CustodyStatusChangeArrest (CustodyStatusChangeArrestID INT AUTO_INCREMENT NOT NULL, CustodyStatusChangeID INT NOT NULL, LocationID INT, ArrestAgencyID INT, CustodyStatusChangeArrestTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE CustodyStatusChangeArrest ADD CONSTRAINT custodystatuschangearrestid PRIMARY KEY (CustodyStatusChangeArrestID);

CREATE SEQUENCE CustodyStatusChangeArrest_CustodyStatusChangeArrestID_seq_1;

CREATE TABLE CustodyStatusChangeCharge (CustodyStatusChangeChargeID INT AUTO_INCREMENT NOT NULL, CustodyStatusChangeArrestID INT NOT NULL, ChargeCode VARCHAR(100), ChargeDisposition VARCHAR(100), AgencyID INT, BondTypeID INT, BondAmount NUMBER(10, 2), BondRemainingAmount NUMBER(10, 2), ChargeJurisdictionTypeID INT, ChargeClassTypeID INT, BondStatusTypeID INT, CustodyStatusChangeChargeTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE CustodyStatusChangeCharge ADD CONSTRAINT custodystatuschangechargeid PRIMARY KEY (CustodyStatusChangeChargeID);

CREATE SEQUENCE CustodyStatusChangeCharge_CustodyStatusChangeChargeID_seq;

CREATE TABLE BookingArrest (BookingArrestID INT AUTO_INCREMENT NOT NULL, BookingID INT NOT NULL, LocationID INT, ArrestAgencyID INT, BookingArrestTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE BookingArrest ADD CONSTRAINT bookingarrestid PRIMARY KEY (BookingArrestID);

CREATE SEQUENCE BookingArrest_BookingArrestID_seq_1;

CREATE TABLE BookingCharge (BookingChargeID INT AUTO_INCREMENT NOT NULL, BookingArrestID INT NOT NULL, ChargeCode VARCHAR(100), ChargeDisposition VARCHAR(100), AgencyID INT, BondTypeID INT, BondAmount NUMBER(10, 2), ChargeJurisdictionTypeID INT, ChargeClassTypeID INT, BondStatusTypeID INT, BookingChargeTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE BookingCharge ADD CONSTRAINT bookingchargeid PRIMARY KEY (BookingChargeID);

CREATE SEQUENCE BookingCharge_BookingChargeID_seq;

ALTER TABLE BehavioralHealthAssessment ADD CONSTRAINT medicaidstatustype_behavioralhealthassessment_fk FOREIGN KEY (MedicaidStatusTypeID) REFERENCES MedicaidStatusType (MedicaidStatusTypeID);

ALTER TABLE BehavioralHealthAssessmentCategory ADD CONSTRAINT assessmentcategorytype_behavioralhealthassessmentcategory_fk FOREIGN KEY (AssessmentCategoryTypeID) REFERENCES AssessmentCategoryType (AssessmentCategoryTypeID);

ALTER TABLE CustodyStatusChangeCharge ADD CONSTRAINT bondstatustype_custodystatuschangecharge_fk FOREIGN KEY (BondStatusTypeID) REFERENCES BondStatusType (BondStatusTypeID);

ALTER TABLE BookingCharge ADD CONSTRAINT bondstatustype_bookingcharge_fk FOREIGN KEY (BondStatusTypeID) REFERENCES BondStatusType (BondStatusTypeID);

ALTER TABLE BookingCharge ADD CONSTRAINT chargeclasstype_bookingcharge_fk FOREIGN KEY (ChargeClassTypeID) REFERENCES ChargeClassType (ChargeClassTypeID);

ALTER TABLE CustodyStatusChangeCharge ADD CONSTRAINT chargeclasstype_custodystatuschangecharge_fk FOREIGN KEY (ChargeClassTypeID) REFERENCES ChargeClassType (ChargeClassTypeID);

ALTER TABLE Treatment ADD CONSTRAINT treatmentstatustype_treatment_fk FOREIGN KEY (TreatmentStatusTypeID) REFERENCES TreatmentStatusType (TreatmentStatusTypeID);

ALTER TABLE Treatment ADD CONSTRAINT treatmentinitiationtype_treatment_fk FOREIGN KEY (TreatmentAdmissionReasonTypeID) REFERENCES TreatmentAdmissionReasonType (TreatmentAdmissionReasonTypeID);

ALTER TABLE Person ADD CONSTRAINT sexoffenderregistrationstatustype_person_fk FOREIGN KEY (SexOffenderStatusTypeID) REFERENCES SexOffenderStatusType (SexOffenderStatusTypeID);

ALTER TABLE Person ADD CONSTRAINT workreleasestatustype_person_fk FOREIGN KEY (WorkReleaseStatusTypeID) REFERENCES WorkReleaseStatusType (WorkReleaseStatusTypeID);

ALTER TABLE Person ADD CONSTRAINT programeligibilitytype_person_fk FOREIGN KEY (ProgramEligibilityTypeID) REFERENCES ProgramEligibilityType (ProgramEligibilityTypeID);

ALTER TABLE Person ADD CONSTRAINT domicilestatustype_person_fk FOREIGN KEY (DomicileStatusTypeID) REFERENCES DomicileStatusType (DomicileStatusTypeID);

ALTER TABLE Person ADD CONSTRAINT personethnicitytype_person_fk FOREIGN KEY (PersonEthnicityTypeID) REFERENCES PersonEthnicityType (PersonEthnicityTypeID);

ALTER TABLE Person ADD CONSTRAINT militaryservicestatustype_person_fk FOREIGN KEY (MilitaryServiceStatusTypeID) REFERENCES MilitaryServiceStatusType (MilitaryServiceStatusTypeID);

ALTER TABLE BookingArrest ADD CONSTRAINT location_bookingarrest_fk FOREIGN KEY (LocationID) REFERENCES Location (LocationID);

ALTER TABLE CustodyStatusChangeArrest ADD CONSTRAINT location_custodystatuschangearrest_fk FOREIGN KEY (LocationID) REFERENCES Location (LocationID);

ALTER TABLE Person ADD CONSTRAINT language_person_fk FOREIGN KEY (LanguageTypeID) REFERENCES LanguageType (LanguageTypeID);

ALTER TABLE Booking ADD CONSTRAINT facility_booking_fk FOREIGN KEY (FacilityID) REFERENCES Facility (FacilityID);

ALTER TABLE CustodyStatusChange ADD CONSTRAINT facility_custody_status_change_fk FOREIGN KEY (FacilityID) REFERENCES Facility (FacilityID);

ALTER TABLE BookingCharge ADD CONSTRAINT bondtype_bookingcharge_fk FOREIGN KEY (BondTypeID) REFERENCES BondType (BondTypeID);

ALTER TABLE CustodyStatusChangeCharge ADD CONSTRAINT bondtype_custodystatuschangecharge_fk FOREIGN KEY (BondTypeID) REFERENCES BondType (BondTypeID);

ALTER TABLE Booking ADD CONSTRAINT bedtype_booking_fk FOREIGN KEY (SupervisionUnitTypeID) REFERENCES SupervisionUnitType (SupervisionUnitTypeID);

ALTER TABLE CustodyStatusChange ADD CONSTRAINT bedtype_custody_status_change_fk FOREIGN KEY (SupervisionUnitTypeID) REFERENCES SupervisionUnitType (SupervisionUnitTypeID);

ALTER TABLE Person ADD CONSTRAINT personrace_person_fk FOREIGN KEY (PersonRaceTypeID) REFERENCES PersonRaceType (PersonRaceTypeID);

ALTER TABLE Person ADD CONSTRAINT personsex_person_fk FOREIGN KEY (PersonSexTypeID) REFERENCES PersonSexType (PersonSexTypeID);

ALTER TABLE BehavioralHealthAssessment ADD CONSTRAINT person_behaviorhealthassessment_fk FOREIGN KEY (PersonID) REFERENCES Person (PersonID);

ALTER TABLE Booking ADD CONSTRAINT person_booking_fk FOREIGN KEY (PersonID) REFERENCES Person (PersonID);

ALTER TABLE CustodyStatusChange ADD CONSTRAINT person_custodystatuschange_fk FOREIGN KEY (PersonID) REFERENCES Person (PersonID);

ALTER TABLE BehavioralHealthEvaluation ADD CONSTRAINT behavioralhealthassessment_behavioralhealthevaluation_fk FOREIGN KEY (BehavioralHealthAssessmentID) REFERENCES BehavioralHealthAssessment (BehavioralHealthAssessmentID);

ALTER TABLE Treatment ADD CONSTRAINT behavioralhealthassessment_treatment_fk FOREIGN KEY (BehavioralHealthAssessmentID) REFERENCES BehavioralHealthAssessment (BehavioralHealthAssessmentID);

ALTER TABLE PrescribedMedication ADD CONSTRAINT behavioralhealthassessment_prescribedmedication_fk FOREIGN KEY (BehavioralHealthAssessmentID) REFERENCES BehavioralHealthAssessment (BehavioralHealthAssessmentID);

ALTER TABLE BehavioralHealthAssessmentCategory ADD CONSTRAINT behavioralhealthassessment_behavioralhealthassessmentcategory_fk FOREIGN KEY (BehavioralHealthAssessmentID) REFERENCES BehavioralHealthAssessment (BehavioralHealthAssessmentID);

ALTER TABLE BookingCharge ADD CONSTRAINT jurisdictiontype_bookingcharge_fk FOREIGN KEY (ChargeJurisdictionTypeID) REFERENCES JurisdictionType (JurisdictionTypeID);

ALTER TABLE CustodyStatusChangeCharge ADD CONSTRAINT jurisdictiontype_custodystatuschangecharge_fk FOREIGN KEY (ChargeJurisdictionTypeID) REFERENCES JurisdictionType (JurisdictionTypeID);

ALTER TABLE BookingCharge ADD CONSTRAINT agency_bookingcharge_fk FOREIGN KEY (AgencyID) REFERENCES Agency (AgencyID);

ALTER TABLE CustodyStatusChangeCharge ADD CONSTRAINT agency_custodystatuschangecharge_fk FOREIGN KEY (AgencyID) REFERENCES Agency (AgencyID);

ALTER TABLE BookingArrest ADD CONSTRAINT agencytype_bookingarrest_fk FOREIGN KEY (ArrestAgencyID) REFERENCES Agency (AgencyID);

ALTER TABLE CustodyStatusChangeArrest ADD CONSTRAINT agencytype_custodystatuschangearrest_fk FOREIGN KEY (ArrestAgencyID) REFERENCES Agency (AgencyID);

ALTER TABLE BookingArrest ADD CONSTRAINT booking_bookingarrest_fk FOREIGN KEY (BookingID) REFERENCES Booking (BookingID);

ALTER TABLE CustodyStatusChange ADD CONSTRAINT booking_custodystatuschange_fk FOREIGN KEY (BookingID) REFERENCES Booking (BookingID);

ALTER TABLE CustodyRelease ADD CONSTRAINT booking_custodyrelease_fk FOREIGN KEY (BookingID) REFERENCES Booking (BookingID);

ALTER TABLE CustodyStatusChangeArrest ADD CONSTRAINT custodystatuschange_custodystatuschangearrest_fk FOREIGN KEY (CustodyStatusChangeID) REFERENCES CustodyStatusChange (CustodyStatusChangeID);

ALTER TABLE CustodyStatusChangeCharge ADD CONSTRAINT custodystatuschangearrest_custodystatuschangecharge_fk FOREIGN KEY (CustodyStatusChangeArrestID) REFERENCES CustodyStatusChangeArrest (CustodyStatusChangeArrestID);

ALTER TABLE BookingCharge ADD CONSTRAINT bookingarrest_bookingcharge_fk FOREIGN KEY (BookingArrestID) REFERENCES BookingArrest (BookingArrestID);
