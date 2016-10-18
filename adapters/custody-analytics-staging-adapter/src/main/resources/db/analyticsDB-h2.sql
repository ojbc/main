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

Drop schema if exists ojbc_booking_staging;

CREATE schema ojbc_booking_staging;

CREATE TABLE MedicaidStatusType (MedicaidStatusTypeID INT NOT NULL, MedicaidStatusTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE MedicaidStatusType ADD CONSTRAINT MedicaidStatusTypeID PRIMARY KEY (MedicaidStatusTypeID);

CREATE TABLE AssessmentCategoryType (AssessmentCategoryTypeID INT NOT NULL, AssessmentCategoryTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE AssessmentCategoryType ADD CONSTRAINT AssessmentCategoryTypeID PRIMARY KEY (AssessmentCategoryTypeID);

CREATE TABLE BondStatusType (BondStatusTypeID INT NOT NULL, BondStatusTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE BondStatusType ADD CONSTRAINT BondStatusTypeID PRIMARY KEY (BondStatusTypeID);

CREATE TABLE ChargeClassType (ChargeClassTypeID INT NOT NULL, ChargeClassTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE ChargeClassType ADD CONSTRAINT ChargeClassTypeID PRIMARY KEY (ChargeClassTypeID);

CREATE TABLE TreatmentStatusType (TreatmentStatusTypeID INT NOT NULL, TreatmentStatusTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE TreatmentStatusType ADD CONSTRAINT TreatmentStatusTypeID PRIMARY KEY (TreatmentStatusTypeID);

CREATE TABLE TreatmentAdmissionReasonType (TreatmentAdmissionReasonTypeID INT NOT NULL, TreatmentAdmissionReasonTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE TreatmentAdmissionReasonType ADD CONSTRAINT TreatmentAdmissionReasonTypeID PRIMARY KEY (TreatmentAdmissionReasonTypeID);

CREATE TABLE SexOffenderStatusType (SexOffenderStatusTypeID INT NOT NULL, SexOffenderStatusTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE SexOffenderStatusType ADD CONSTRAINT SexOffenderStatusTypeID PRIMARY KEY (SexOffenderStatusTypeID);

CREATE TABLE WorkReleaseStatusType (WorkReleaseStatusTypeID INT NOT NULL, WorkReleaseStatusTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE WorkReleaseStatusType ADD CONSTRAINT WorkReleaseStatusTypeID PRIMARY KEY (WorkReleaseStatusTypeID);

CREATE TABLE ProgramEligibilityType (ProgramEligibilityTypeID INT NOT NULL, ProgramEligibilityTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE ProgramEligibilityType ADD CONSTRAINT ProgramEligibilityTypeID PRIMARY KEY (ProgramEligibilityTypeID);

CREATE TABLE DomicileStatusType (DomicileStatusTypeID INT NOT NULL, DomicileStatusTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE DomicileStatusType ADD CONSTRAINT DomicileStatusTypeID PRIMARY KEY (DomicileStatusTypeID);

CREATE TABLE PersonEthnicityType (PersonEthnicityTypeID INT NOT NULL, PersonEthnicityTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE PersonEthnicityType ADD CONSTRAINT PersonEthnicityTypeID PRIMARY KEY (PersonEthnicityTypeID);

CREATE TABLE MilitaryServiceStatusType (MilitaryServiceStatusTypeID INT NOT NULL, MilitaryServiceStatusTypeDescription VARCHAR(100) NOT NULL);

ALTER TABLE MilitaryServiceStatusType ADD CONSTRAINT MilitaryServiceStatusTypeID PRIMARY KEY (MilitaryServiceStatusTypeID);

CREATE TABLE Location (LocationID INT AUTO_INCREMENT NOT NULL, AddressSecondaryUnit VARCHAR(150), StreetNumber VARCHAR(50), StreetName VARCHAR(150), City VARCHAR(100), State VARCHAR(10), PostalCode VARCHAR(10), LocationLatitude NUMBER(14, 10), LocationLongitude NUMBER(14, 10), LocationTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE Location ADD CONSTRAINT LocationID PRIMARY KEY (LocationID);

CREATE SEQUENCE Location_LocationID_seq_2;

CREATE TABLE LanguageType (LanguageTypeID INT NOT NULL, LanguageTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE LanguageType ADD CONSTRAINT LanguageTypeID PRIMARY KEY (LanguageTypeID);

CREATE TABLE Facility (FacilityID INT NOT NULL, FacilityDescription VARCHAR(100) NOT NULL, Capacity INT DEFAULT 0 NOT NULL);

ALTER TABLE Facility ADD CONSTRAINT FacilityID PRIMARY KEY (FacilityID);

CREATE TABLE BondType (BondTypeID INT NOT NULL, BondTypeDescription VARCHAR(100) NOT NULL);

ALTER TABLE BondType ADD CONSTRAINT BondTypeID PRIMARY KEY (BondTypeID);

CREATE TABLE SupervisionUnitType (SupervisionUnitTypeID INT NOT NULL, SupervisionUnitTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE SupervisionUnitType ADD CONSTRAINT SupervisionUnitTypeID PRIMARY KEY (SupervisionUnitTypeID);

CREATE TABLE PersonRaceType (PersonRaceTypeID INT NOT NULL, PersonRaceTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE PersonRaceType ADD CONSTRAINT PersonRaceTypeID PRIMARY KEY (PersonRaceTypeID);

CREATE TABLE PersonSexType (PersonSexTypeID INT NOT NULL, PersonSexTypeDescription VARCHAR(7) NOT NULL);

ALTER TABLE PersonSexType ADD CONSTRAINT PersonSexTypeID PRIMARY KEY (PersonSexTypeID);

CREATE TABLE Person (PersonID INT AUTO_INCREMENT NOT NULL, PersonUniqueIdentifier VARCHAR(100) NOT NULL, PersonUniqueIdentifier2 VARCHAR(100), PersonAgeAtBooking INT, PersonBirthDate date, EducationLevel VARCHAR(50), Occupation VARCHAR(50), LanguageTypeID INT, PersonSexTypeID INT, PersonRaceTypeID INT, PersonEthnicityTypeID INT, MilitaryServiceStatusTypeID INT, DomicileStatusTypeID INT, ProgramEligibilityTypeID INT, WorkReleaseStatusTypeID INT, SexOffenderStatusTypeID INT, PersonTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE Person ADD CONSTRAINT PersonID PRIMARY KEY (PersonID);

CREATE SEQUENCE Person_PersonID_seq_1_1;

CREATE TABLE BehavioralHealthAssessment (BehavioralHealthAssessmentID INT AUTO_INCREMENT NOT NULL, PersonID INT NOT NULL, SeriousMentalIllnessIndicator BOOLEAN, CareEpisodeStartDate date, CareEpisodeEndDate date, MedicaidStatusTypeID INT, EnrolledProviderName VARCHAR(100), BehavioralHealthAssessmentTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE BehavioralHealthAssessment ADD CONSTRAINT BehavioralHealthAssessmentID PRIMARY KEY (BehavioralHealthAssessmentID);

CREATE SEQUENCE BehavioralHealthAssessment_BehavioralHealthAssessmentID_seq;

CREATE TABLE BehavioralHealthAssessmentCategory (BehavioralHealthAssessmentCategoryID INT AUTO_INCREMENT NOT NULL, BehavioralHealthAssessmentID INT NOT NULL, AssessmentCategoryTypeID INT NOT NULL, BehavioralHealthAssessmentCategoryTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE BehavioralHealthAssessmentCategory ADD CONSTRAINT BehavioralHealthAssessmentCategoryID PRIMARY KEY (BehavioralHealthAssessmentCategoryID);

CREATE SEQUENCE BehavioralHealthAssessmentCategory_BehavioralHealthAssessmentCategoryID_seq;

CREATE TABLE PrescribedMedication (PrescribedMedicationID INT AUTO_INCREMENT NOT NULL, BehavioralHealthAssessmentID INT NOT NULL, MedicationDescription VARCHAR(80), MedicationDispensingDate date, MedicationDoseMeasure VARCHAR(10), PrescribedMedicationTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE PrescribedMedication ADD CONSTRAINT PrescribedMedicationID PRIMARY KEY (PrescribedMedicationID);

CREATE SEQUENCE PrescribedMedication_PrescribedMedicationID_seq;

CREATE TABLE Treatment (TreatmentID INT AUTO_INCREMENT NOT NULL, BehavioralHealthAssessmentID INT NOT NULL, TreatmentStartDate date, TreatmentAdmissionReasonTypeID INT, TreatmentStatusTypeID INT, TreatmentProviderName VARCHAR(100), TreatmentTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE Treatment ADD CONSTRAINT TreatmentId PRIMARY KEY (TreatmentID);

CREATE SEQUENCE Treatment_TreatmentID_seq;

CREATE TABLE BehavioralHealthEvaluation (BehavioralHealthEvaluationID INT AUTO_INCREMENT NOT NULL, BehavioralHealthAssessmentID INT NOT NULL, BehavioralHealthDiagnosisDescription VARCHAR(100), BehavioralHealthEvaluationTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE BehavioralHealthEvaluation ADD CONSTRAINT BehavioralHealthEvaluationID PRIMARY KEY (BehavioralHealthEvaluationID);

CREATE SEQUENCE BehavioralHealthEvaluation_BehavioralHealthEvaluationID_seq;

CREATE TABLE JurisdictionType (JurisdictionTypeID INT NOT NULL, JurisdictionTypeDescription VARCHAR(100) NOT NULL);

ALTER TABLE JurisdictionType ADD CONSTRAINT JurisdictionTypeID PRIMARY KEY (JurisdictionTypeID);

CREATE TABLE Agency (AgencyID INT NOT NULL, AgencyDescription VARCHAR(50) NOT NULL);

ALTER TABLE Agency ADD CONSTRAINT AgencyID PRIMARY KEY (AgencyID);

CREATE TABLE Booking (BookingID INT AUTO_INCREMENT NOT NULL, BookingNumber VARCHAR(100) NOT NULL, PersonID INT NOT NULL, BookingDate date NOT NULL, BookingTime time, ScheduledReleaseDate date, FacilityID INT, SupervisionUnitTypeID INT, InmateJailResidentIndicator BOOLEAN, InmateCurrentLocation VARCHAR(100), BookingTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE Booking ADD CONSTRAINT BookingID PRIMARY KEY (BookingID);

CREATE SEQUENCE Booking_BookingID_seq;

CREATE TABLE CustodyRelease (CustodyReleaseID INT AUTO_INCREMENT NOT NULL, BookingID INT, BookingNumber VARCHAR(100) NOT NULL, ReleaseDate date NOT NULL, ReleaseTime time, ReleaseCondition VARCHAR(200), CustodyReleaseTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE CustodyRelease ADD CONSTRAINT CustodyReleaseID PRIMARY KEY (CustodyReleaseID);

CREATE SEQUENCE CustodyRelease_CustodyReleaseID_seq;

CREATE TABLE CustodyStatusChange (CustodyStatusChangeID INT AUTO_INCREMENT NOT NULL, BookingID INT, PersonID INT NOT NULL, BookingDate date NOT NULL, BookingTime time, BookingNumber VARCHAR(100) NOT NULL, ScheduledReleaseDate date, FacilityID INT, SupervisionUnitTypeID INT, InmateJailResidentIndicator BOOLEAN, InmateCurrentLocation VARCHAR(100), CustodyStatusChangeTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE CustodyStatusChange ADD CONSTRAINT CustodyStatusChangeID PRIMARY KEY (CustodyStatusChangeID);

CREATE SEQUENCE CustodyStatusChange_CustodyStatusChangeID_seq;

CREATE TABLE CustodyStatusChangeArrest (CustodyStatusChangeArrestID INT AUTO_INCREMENT NOT NULL, CustodyStatusChangeID INT NOT NULL, LocationID INT, ArrestAgencyID INT, CustodyStatusChangeArrestTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE CustodyStatusChangeArrest ADD CONSTRAINT CustodyStatusChangeArrestID PRIMARY KEY (CustodyStatusChangeArrestID);

CREATE SEQUENCE CustodyStatusChangeArrest_CustodyStatusChangeArrestID_seq_1;

CREATE TABLE CustodyStatusChangeCharge (CustodyStatusChangeChargeID INT AUTO_INCREMENT NOT NULL, CustodyStatusChangeArrestID INT NOT NULL, ChargeCode VARCHAR(100), ChargeDisposition VARCHAR(100), AgencyID INT, BondTypeID INT, BondAmount NUMBER(10, 2), BondRemainingAmount NUMBER(10, 2), ChargeJurisdictionTypeID INT, ChargeClassTypeID INT, BondStatusTypeID INT, CustodyStatusChangeChargeTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE CustodyStatusChangeCharge ADD CONSTRAINT CustodyStatusChangeChargeID PRIMARY KEY (CustodyStatusChangeChargeID);

CREATE SEQUENCE CustodyStatusChangeCharge_CustodyStatusChangeChargeID_seq;

CREATE TABLE BookingArrest (BookingArrestID INT AUTO_INCREMENT NOT NULL, BookingID INT NOT NULL, LocationID INT, ArrestAgencyID INT, BookingArrestTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE BookingArrest ADD CONSTRAINT BookingArrestID PRIMARY KEY (BookingArrestID);

CREATE SEQUENCE BookingArrest_BookingArrestID_seq_1;

CREATE TABLE BookingCharge (BookingChargeID INT AUTO_INCREMENT NOT NULL, BookingArrestID INT NOT NULL, ChargeCode VARCHAR(100), ChargeDisposition VARCHAR(100), AgencyID INT, BondTypeID INT, BondAmount NUMBER(10, 2), ChargeJurisdictionTypeID INT, ChargeClassTypeID INT, BondStatusTypeID INT, BookingChargeTimestamp TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE BookingCharge ADD CONSTRAINT BookingChargeID PRIMARY KEY (BookingChargeID);

CREATE SEQUENCE BookingCharge_BookingChargeID_seq;

ALTER TABLE BehavioralHealthAssessment ADD CONSTRAINT MedicaidStatusType_BehavioralHealthAssessment_fk FOREIGN KEY (MedicaidStatusTypeID) REFERENCES MedicaidStatusType (MedicaidStatusTypeID);

ALTER TABLE BehavioralHealthAssessmentCategory ADD CONSTRAINT AssessmentCategoryType_BehavioralHealthAssessmentCategory_fk FOREIGN KEY (AssessmentCategoryTypeID) REFERENCES AssessmentCategoryType (AssessmentCategoryTypeID);

ALTER TABLE CustodyStatusChangeCharge ADD CONSTRAINT BondStatusType_CustodyStatusChangeCharge_fk FOREIGN KEY (BondStatusTypeID) REFERENCES BondStatusType (BondStatusTypeID);

ALTER TABLE BookingCharge ADD CONSTRAINT BondStatusType_BookingCharge_fk FOREIGN KEY (BondStatusTypeID) REFERENCES BondStatusType (BondStatusTypeID);

ALTER TABLE BookingCharge ADD CONSTRAINT ChargeClassType_BookingCharge_fk FOREIGN KEY (ChargeClassTypeID) REFERENCES ChargeClassType (ChargeClassTypeID);

ALTER TABLE CustodyStatusChangeCharge ADD CONSTRAINT ChargeClassType_CustodyStatusChangeCharge_fk FOREIGN KEY (ChargeClassTypeID) REFERENCES ChargeClassType (ChargeClassTypeID);

ALTER TABLE Treatment ADD CONSTRAINT TreatmentStatusType_Treatment_fk FOREIGN KEY (TreatmentStatusTypeID) REFERENCES TreatmentStatusType (TreatmentStatusTypeID);

ALTER TABLE Treatment ADD CONSTRAINT TreatmentInitiationType_Treatment_fk FOREIGN KEY (TreatmentAdmissionReasonTypeID) REFERENCES TreatmentAdmissionReasonType (TreatmentAdmissionReasonTypeID);

ALTER TABLE Person ADD CONSTRAINT SexOffenderRegistrationStatusType_Person_fk FOREIGN KEY (SexOffenderStatusTypeID) REFERENCES SexOffenderStatusType (SexOffenderStatusTypeID);

ALTER TABLE Person ADD CONSTRAINT WorkReleaseStatusType_Person_fk FOREIGN KEY (WorkReleaseStatusTypeID) REFERENCES WorkReleaseStatusType (WorkReleaseStatusTypeID);

ALTER TABLE Person ADD CONSTRAINT ProgramEligibilityType_Person_fk FOREIGN KEY (ProgramEligibilityTypeID) REFERENCES ProgramEligibilityType (ProgramEligibilityTypeID);

ALTER TABLE Person ADD CONSTRAINT DomicileStatusType_Person_fk FOREIGN KEY (DomicileStatusTypeID) REFERENCES DomicileStatusType (DomicileStatusTypeID);

ALTER TABLE Person ADD CONSTRAINT PersonEthnicityType_Person_fk FOREIGN KEY (PersonEthnicityTypeID) REFERENCES PersonEthnicityType (PersonEthnicityTypeID);

ALTER TABLE Person ADD CONSTRAINT MilitaryServiceStatusType_Person_fk FOREIGN KEY (MilitaryServiceStatusTypeID) REFERENCES MilitaryServiceStatusType (MilitaryServiceStatusTypeID);

ALTER TABLE BookingArrest ADD CONSTRAINT Location_BookingArrest_fk FOREIGN KEY (LocationID) REFERENCES Location (LocationID);

ALTER TABLE CustodyStatusChangeArrest ADD CONSTRAINT Location_CustodyStatusChangeArrest_fk FOREIGN KEY (LocationID) REFERENCES Location (LocationID);

ALTER TABLE Person ADD CONSTRAINT Language_Person_fk FOREIGN KEY (LanguageTypeID) REFERENCES LanguageType (LanguageTypeID);

ALTER TABLE Booking ADD CONSTRAINT Facility_Booking_fk FOREIGN KEY (FacilityID) REFERENCES Facility (FacilityID);

ALTER TABLE CustodyStatusChange ADD CONSTRAINT Facility_Custody_Status_Change_fk FOREIGN KEY (FacilityID) REFERENCES Facility (FacilityID);

ALTER TABLE BookingCharge ADD CONSTRAINT BondType_BookingCharge_fk FOREIGN KEY (BondTypeID) REFERENCES BondType (BondTypeID);

ALTER TABLE CustodyStatusChangeCharge ADD CONSTRAINT BondType_CustodyStatusChangeCharge_fk FOREIGN KEY (BondTypeID) REFERENCES BondType (BondTypeID);

ALTER TABLE Booking ADD CONSTRAINT BedType_Booking_fk FOREIGN KEY (SupervisionUnitTypeID) REFERENCES SupervisionUnitType (SupervisionUnitTypeID);

ALTER TABLE CustodyStatusChange ADD CONSTRAINT BedType_Custody_Status_Change_fk FOREIGN KEY (SupervisionUnitTypeID) REFERENCES SupervisionUnitType (SupervisionUnitTypeID);

ALTER TABLE Person ADD CONSTRAINT PersonRace_Person_fk FOREIGN KEY (PersonRaceTypeID) REFERENCES PersonRaceType (PersonRaceTypeID);

ALTER TABLE Person ADD CONSTRAINT PersonSex_Person_fk FOREIGN KEY (PersonSexTypeID) REFERENCES PersonSexType (PersonSexTypeID);

ALTER TABLE BehavioralHealthAssessment ADD CONSTRAINT Person_BehaviorHealthAssessment_fk FOREIGN KEY (PersonID) REFERENCES Person (PersonID);

ALTER TABLE Booking ADD CONSTRAINT Person_Booking_fk FOREIGN KEY (PersonID) REFERENCES Person (PersonID);

ALTER TABLE CustodyStatusChange ADD CONSTRAINT Person_CustodyStatusChange_fk FOREIGN KEY (PersonID) REFERENCES Person (PersonID);

ALTER TABLE BehavioralHealthEvaluation ADD CONSTRAINT BehavioralHealthAssessment_BehavioralHealthEvaluation_fk FOREIGN KEY (BehavioralHealthAssessmentID) REFERENCES BehavioralHealthAssessment (BehavioralHealthAssessmentID);

ALTER TABLE Treatment ADD CONSTRAINT BehavioralHealthAssessment_Treatment_fk FOREIGN KEY (BehavioralHealthAssessmentID) REFERENCES BehavioralHealthAssessment (BehavioralHealthAssessmentID);

ALTER TABLE PrescribedMedication ADD CONSTRAINT BehavioralHealthAssessment_PrescribedMedication_fk FOREIGN KEY (BehavioralHealthAssessmentID) REFERENCES BehavioralHealthAssessment (BehavioralHealthAssessmentID);

ALTER TABLE BehavioralHealthAssessmentCategory ADD CONSTRAINT BehavioralHealthAssessment_BehavioralHealthAssessmentCategory_fk FOREIGN KEY (BehavioralHealthAssessmentID) REFERENCES BehavioralHealthAssessment (BehavioralHealthAssessmentID);

ALTER TABLE BookingCharge ADD CONSTRAINT JurisdictionType_BookingCharge_fk FOREIGN KEY (ChargeJurisdictionTypeID) REFERENCES JurisdictionType (JurisdictionTypeID);

ALTER TABLE CustodyStatusChangeCharge ADD CONSTRAINT JurisdictionType_CustodyStatusChangeCharge_fk FOREIGN KEY (ChargeJurisdictionTypeID) REFERENCES JurisdictionType (JurisdictionTypeID);

ALTER TABLE BookingCharge ADD CONSTRAINT Agency_BookingCharge_fk FOREIGN KEY (AgencyID) REFERENCES Agency (AgencyID);

ALTER TABLE CustodyStatusChangeCharge ADD CONSTRAINT Agency_CustodyStatusChangeCharge_fk FOREIGN KEY (AgencyID) REFERENCES Agency (AgencyID);

ALTER TABLE BookingArrest ADD CONSTRAINT AgencyType_BookingArrest_fk FOREIGN KEY (ArrestAgencyID) REFERENCES Agency (AgencyID);

ALTER TABLE CustodyStatusChangeArrest ADD CONSTRAINT AgencyType_CustodyStatusChangeArrest_fk FOREIGN KEY (ArrestAgencyID) REFERENCES Agency (AgencyID);

ALTER TABLE BookingArrest ADD CONSTRAINT Booking_BookingArrest_fk FOREIGN KEY (BookingID) REFERENCES Booking (BookingID);

ALTER TABLE CustodyStatusChange ADD CONSTRAINT Booking_CustodyStatusChange_fk FOREIGN KEY (BookingID) REFERENCES Booking (BookingID);

ALTER TABLE CustodyRelease ADD CONSTRAINT Booking_CustodyRelease_fk FOREIGN KEY (BookingID) REFERENCES Booking (BookingID);

ALTER TABLE CustodyStatusChangeArrest ADD CONSTRAINT CustodyStatusChange_CustodyStatusChangeArrest_fk FOREIGN KEY (CustodyStatusChangeID) REFERENCES CustodyStatusChange (CustodyStatusChangeID);

ALTER TABLE CustodyStatusChangeCharge ADD CONSTRAINT CustodyStatusChangeArrest_CustodyStatusChangeCharge_fk FOREIGN KEY (CustodyStatusChangeArrestID) REFERENCES CustodyStatusChangeArrest (CustodyStatusChangeArrestID);

ALTER TABLE BookingCharge ADD CONSTRAINT BookingArrest_BookingCharge_fk FOREIGN KEY (BookingArrestID) REFERENCES BookingArrest (BookingArrestID);
