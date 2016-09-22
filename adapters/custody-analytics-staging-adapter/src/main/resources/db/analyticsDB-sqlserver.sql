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
 
USE master
IF EXISTS(select * from sys.databases where name='CustodyAnalyticsDataStore')
DROP DATABASE CustodyAnalyticsDataStore

CREATE DATABASE CustodyAnalyticsDataStore

Use CustodyAnalyticsDataStore

/**
* Copy DDL from SQL PA below here.  Modify timestamps in fact tables like this:
*                `Timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
**/

CREATE TABLE MedicaidStatusType (
                MedicaidStatusTypeID INT NOT NULL,
                MedicaidStatusTypeDescription VARCHAR(50) NOT NULL,
                CONSTRAINT MedicaidStatusTypeID PRIMARY KEY (MedicaidStatusTypeID)
)

CREATE TABLE AssessmentCategoryType (
                AssessmentCategoryTypeID INT NOT NULL,
                AssessmentCategoryTypeDescription VARCHAR(50) NOT NULL,
                CONSTRAINT AssessmentCategoryTypeID PRIMARY KEY (AssessmentCategoryTypeID)
)

CREATE TABLE BondStatusType (
                BondStatusTypeID INT NOT NULL,
                BondStatusTypeDescription VARCHAR(50) NOT NULL,
                CONSTRAINT BondStatusTypeID PRIMARY KEY (BondStatusTypeID)
)

CREATE TABLE ChargeClassType (
                ChargeClassTypeID INT NOT NULL,
                ChargeClassTypeDescription VARCHAR(50) NOT NULL,
                CONSTRAINT ChargeClassTypeID PRIMARY KEY (ChargeClassTypeID)
)

CREATE TABLE TreatmentStatusType (
                TreatmentStatusTypeID INT NOT NULL,
                TreatmentStatusTypeDescription VARCHAR(50) NOT NULL,
                CONSTRAINT TreatmentStatusTypeID PRIMARY KEY (TreatmentStatusTypeID)
)

CREATE TABLE TreatmentAdmissionReasonType (
                TreatmentAdmissionReasonTypeID INT NOT NULL,
                TreatmentAdmissionReasonTypeDescription VARCHAR(50) NOT NULL,
                CONSTRAINT TreatmentAdmissionReasonTypeID PRIMARY KEY (TreatmentAdmissionReasonTypeID)
)

CREATE TABLE SexOffenderStatusType (
                SexOffenderStatusTypeID INT NOT NULL,
                SexOffenderStatusTypeDescription VARCHAR(50) NOT NULL,
                CONSTRAINT SexOffenderStatusTypeID PRIMARY KEY (SexOffenderStatusTypeID)
)

CREATE TABLE WorkReleaseStatusType (
                WorkReleaseStatusTypeID INT NOT NULL,
                WorkReleaseStatusTypeDescription VARCHAR(50) NOT NULL,
                CONSTRAINT WorkReleaseStatusTypeID PRIMARY KEY (WorkReleaseStatusTypeID)
)

CREATE TABLE ProgramEligibilityType (
                ProgramEligibilityTypeID INT NOT NULL,
                ProgramEligibilityTypeDescription VARCHAR(50) NOT NULL,
                CONSTRAINT ProgramEligibilityTypeID PRIMARY KEY (ProgramEligibilityTypeID)
)

CREATE TABLE DomicileStatusType (
                DomicileStatusTypeID INT NOT NULL,
                DomicileStatusTypeDescription VARCHAR(50) NOT NULL,
                CONSTRAINT DomicileStatusTypeID PRIMARY KEY (DomicileStatusTypeID)
)

CREATE TABLE PersonEthnicityType (
                PersonEthnicityTypeID INT NOT NULL,
                PersonEthnicityTypeDescription VARCHAR(50) NOT NULL,
                CONSTRAINT PersonEthnicityTypeID PRIMARY KEY (PersonEthnicityTypeID)
)

CREATE TABLE MilitaryServiceStatusType (
                MilitaryServiceStatusTypeID INT NOT NULL,
                MilitaryServiceStatusTypeDescription VARCHAR(100) NOT NULL,
                CONSTRAINT MilitaryServiceStatusTypeID PRIMARY KEY (MilitaryServiceStatusTypeID)
)

CREATE TABLE Location (
                LocationID INT IDENTITY NOT NULL,
                AddressSecondaryUnit VARCHAR(150),
                StreetNumber VARCHAR(50),
                StreetName VARCHAR(150),
                City VARCHAR(100),
                State VARCHAR(10),
                PostalCode VARCHAR(10),
                LocationLatitude NUMERIC(14,10),
                LocationLongitude NUMERIC(14,10),
                LocationTimestamp DATETIME DEFAULT getdate() NOT NULL,
                CONSTRAINT LocationID PRIMARY KEY (LocationID)
)

CREATE TABLE LanguageType (
                LanguageTypeID INT NOT NULL,
                LanguageTypeDescription VARCHAR(50) NOT NULL,
                CONSTRAINT LanguageTypeID PRIMARY KEY (LanguageTypeID)
)

CREATE TABLE Facility (
                FacilityID INT NOT NULL,
                FacilityDescription VARCHAR(100) NOT NULL,
                Capacity INT DEFAULT 0 NOT NULL,
                CONSTRAINT FacilityID PRIMARY KEY (FacilityID)
)

-- Comment for table [Facility]: Booking Detention Facility


CREATE TABLE BondType (
                BondTypeID INT NOT NULL,
                BondTypeDescription VARCHAR(100) NOT NULL,
                CONSTRAINT BondTypeID PRIMARY KEY (BondTypeID)
)

CREATE TABLE SupervisionUnitType (
                SupervisionUnitTypeID INT NOT NULL,
                SupervisionUnitTypeDescription VARCHAR(50) NOT NULL,
                CONSTRAINT SupervisionUnitTypeID PRIMARY KEY (SupervisionUnitTypeID)
)

CREATE TABLE PersonRaceType (
                PersonRaceTypeID INT NOT NULL,
                PersonRaceTypeDescription VARCHAR(50) NOT NULL,
                CONSTRAINT PersonRaceTypeID PRIMARY KEY (PersonRaceTypeID)
)

CREATE TABLE PersonSexType (
                PersonSexTypeID INT NOT NULL,
                PersonSexTypeDescription VARCHAR(7) NOT NULL,
                CONSTRAINT PersonSexTypeID PRIMARY KEY (PersonSexTypeID)
)

CREATE TABLE Person (
                PersonID INT IDENTITY NOT NULL,
                PersonUniqueIdentifier VARCHAR(100) NOT NULL,
                PersonUniqueIdentifier2 VARCHAR(100),
                PersonAgeAtBooking INT,
                PersonBirthDate DATE,
                EducationLevel VARCHAR(50),
                Occupation VARCHAR(50),
                LanguageTypeID INT,
                PersonSexTypeID INT,
                PersonRaceTypeID INT,
                PersonEthnicityTypeID INT,
                MilitaryServiceStatusTypeID INT,
                DomicileStatusTypeID INT,
                ProgramEligibilityTypeID INT,
                WorkReleaseStatusTypeID INT,
                SexOffenderStatusTypeID INT,
                PersonTimestamp DATETIME DEFAULT getdate() NOT NULL,
                CONSTRAINT PersonID PRIMARY KEY (PersonID)
)

CREATE TABLE BehavioralHealthAssessment (
                BehavioralHealthAssessmentID INT IDENTITY NOT NULL,
                PersonID INT NOT NULL,
                SeriousMentalIllnessIndicator BIT,
                CareEpisodeStartDate DATE,
                CareEpisodeEndDate DATE,
                MedicaidStatusTypeID INT,
                EnrolledProviderName VARCHAR(100),
                BehavioralHealthAssessmentTimestamp DATETIME DEFAULT getdate() NOT NULL,
                CONSTRAINT BehavioralHealthAssessmentID PRIMARY KEY (BehavioralHealthAssessmentID)
)

CREATE TABLE BehavioralHealthAssessmentCategory (
                BehavioralHealthAssessmentCategoryID INT IDENTITY NOT NULL,
                BehavioralHealthAssessmentID INT NOT NULL,
                AssessmentCategoryTypeID INT NOT NULL,
                BehavioralHealthAssessmentCategoryTimestamp DATETIME DEFAULT getdate() NOT NULL,
                CONSTRAINT BehavioralHealthAssessmentCategoryID PRIMARY KEY (BehavioralHealthAssessmentCategoryID)
)

CREATE TABLE PrescribedMedication (
                PrescribedMedicationID INT IDENTITY NOT NULL,
                BehavioralHealthAssessmentID INT NOT NULL,
                MedicationDescription VARCHAR(80),
                MedicationDispensingDate DATE,
                MedicationDoseMeasure VARCHAR(10),
                PrescribedMedicationTimestamp DATETIME DEFAULT getdate() NOT NULL,
                CONSTRAINT PrescribedMedicationID PRIMARY KEY (PrescribedMedicationID)
)

CREATE TABLE Treatment (
                TreatmentID INT IDENTITY NOT NULL,
                BehavioralHealthAssessmentID INT NOT NULL,
                TreatmentStartDate DATE,
                TreatmentAdmissionReasonTypeID INT,
                TreatmentStatusTypeID INT,
                TreatmentProviderName VARCHAR(100),
                TreatmentTimestamp DATETIME DEFAULT getdate() NOT NULL,
                CONSTRAINT TreatmentId PRIMARY KEY (TreatmentID)
)

CREATE TABLE BehavioralHealthEvaluation (
                BehavioralHealthEvaluationID INT IDENTITY NOT NULL,
                BehavioralHealthAssessmentID INT NOT NULL,
                BehavioralHealthDiagnosisDescription VARCHAR(50),
                BehavioralHealthEvaluationTimestamp DATETIME DEFAULT getdate() NOT NULL,
                CONSTRAINT BehavioralHealthEvaluationID PRIMARY KEY (BehavioralHealthEvaluationID)
)

CREATE TABLE JurisdictionType (
                JurisdictionTypeID INT NOT NULL,
                JurisdictionTypeDescription VARCHAR(100) NOT NULL,
                CONSTRAINT JurisdictionTypeID PRIMARY KEY (JurisdictionTypeID)
)

CREATE TABLE Agency (
                AgencyID INT NOT NULL,
                AgencyDescription VARCHAR(50) NOT NULL,
                CONSTRAINT AgencyID PRIMARY KEY (AgencyID)
)

CREATE TABLE Booking (
                BookingID INT IDENTITY NOT NULL,
                BookingNumber VARCHAR(100) NOT NULL,
                PersonID INT NOT NULL,
                BookingDate DATE NOT NULL,
                BookingTime TIME,
                ScheduledReleaseDate DATE,
                FacilityID INT,
                SupervisionUnitTypeID INT,
                InmateJailResidentIndicator BIT,
                BookingTimestamp DATETIME DEFAULT getdate() NOT NULL,
                CONSTRAINT BookingID PRIMARY KEY (BookingID)
)

CREATE TABLE CustodyRelease (
                CustodyReleaseID INT IDENTITY NOT NULL,
                BookingID INT NOT NULL,
                ReleaseDate DATE NOT NULL,
                ReleaseTime TIME,
                ReleaseCondition VARCHAR(200),
                CustodyReleaseTimestamp DATETIME DEFAULT getdate() NOT NULL,
                CONSTRAINT CustodyReleaseID PRIMARY KEY (CustodyReleaseID)
)

CREATE TABLE CustodyStatusChange (
                CustodyStatusChangeID INT IDENTITY NOT NULL,
                BookingID INT NOT NULL,
                PersonID INT NOT NULL,
                BookingDate DATE NOT NULL,
                BookingTime TIME,
                ScheduledReleaseDate DATE,
                FacilityID INT,
                SupervisionUnitTypeID INT,
                InmateJailResidentIndicator BIT,
                CustodyStatusChangeTimestamp DATETIME DEFAULT getdate() NOT NULL,
                CONSTRAINT CustodyStatusChangeID PRIMARY KEY (CustodyStatusChangeID)
)

CREATE TABLE CustodyStatusChangeArrest (
                CustodyStatusChangeArrestID INT IDENTITY NOT NULL,
                CustodyStatusChangeID INT NOT NULL,
                LocationID INT,
                ArrestAgencyID INT,
                CustodyStatusChangeArrestTimestamp DATETIME DEFAULT getdate() NOT NULL,
                CONSTRAINT CustodyStatusChangeArrestID PRIMARY KEY (CustodyStatusChangeArrestID)
)

CREATE TABLE CustodyStatusChangeCharge (
                CustodyStatusChangeChargeID INT IDENTITY NOT NULL,
                CustodyStatusChangeArrestID INT NOT NULL,
                ChargeCode VARCHAR(100),
                ChargeDisposition VARCHAR(100),
                AgencyID INT,
                BondTypeID INT,
                BondAmount NUMERIC(10,2),
                BondRemainingAmount NUMERIC(10,2),
                ChargeJurisdictionTypeID INT,
                ChargeClassTypeID INT,
                BondStatusTypeID INT,
                CustodyStatusChangeChargeTimestamp DATETIME DEFAULT getdate() NOT NULL,
                CONSTRAINT CustodyStatusChangeChargeID PRIMARY KEY (CustodyStatusChangeChargeID)
)

CREATE TABLE BookingArrest (
                BookingArrestID INT IDENTITY NOT NULL,
                BookingID INT NOT NULL,
                LocationID INT,
                ArrestAgencyID INT,
                BookingArrestTimestamp DATETIME DEFAULT getdate() NOT NULL,
                CONSTRAINT BookingArrestID PRIMARY KEY (BookingArrestID)
)

CREATE TABLE BookingCharge (
                BookingChargeID INT IDENTITY NOT NULL,
                BookingArrestID INT NOT NULL,
                ChargeCode VARCHAR(100),
                ChargeDisposition VARCHAR(100),
                AgencyID INT,
                BondTypeID INT,
                BondAmount NUMERIC(10,2),
                ChargeJurisdictionTypeID INT,
                ChargeClassTypeID INT,
                BondStatusTypeID INT,
                BookingChargeTimestamp DATETIME DEFAULT getdate() NOT NULL,
                CONSTRAINT BookingChargeID PRIMARY KEY (BookingChargeID)
)

ALTER TABLE BehavioralHealthAssessment ADD CONSTRAINT MedicaidStatusType_BehavioralHealthAssessment_fk
FOREIGN KEY (MedicaidStatusTypeID)
REFERENCES MedicaidStatusType (MedicaidStatusTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE BehavioralHealthAssessmentCategory ADD CONSTRAINT AssessmentCategoryType_BehavioralHealthAssessmentCategory_fk
FOREIGN KEY (AssessmentCategoryTypeID)
REFERENCES AssessmentCategoryType (AssessmentCategoryTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE CustodyStatusChangeCharge ADD CONSTRAINT BondStatusType_CustodyStatusChangeCharge_fk
FOREIGN KEY (BondStatusTypeID)
REFERENCES BondStatusType (BondStatusTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE BookingCharge ADD CONSTRAINT BondStatusType_BookingCharge_fk
FOREIGN KEY (BondStatusTypeID)
REFERENCES BondStatusType (BondStatusTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE BookingCharge ADD CONSTRAINT ChargeClassType_BookingCharge_fk
FOREIGN KEY (ChargeClassTypeID)
REFERENCES ChargeClassType (ChargeClassTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE CustodyStatusChangeCharge ADD CONSTRAINT ChargeClassType_CustodyStatusChangeCharge_fk
FOREIGN KEY (ChargeClassTypeID)
REFERENCES ChargeClassType (ChargeClassTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE Treatment ADD CONSTRAINT TreatmentStatusType_Treatment_fk
FOREIGN KEY (TreatmentStatusTypeID)
REFERENCES TreatmentStatusType (TreatmentStatusTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE Treatment ADD CONSTRAINT TreatmentInitiationType_Treatment_fk
FOREIGN KEY (TreatmentAdmissionReasonTypeID)
REFERENCES TreatmentAdmissionReasonType (TreatmentAdmissionReasonTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE Person ADD CONSTRAINT SexOffenderRegistrationStatusType_Person_fk
FOREIGN KEY (SexOffenderStatusTypeID)
REFERENCES SexOffenderStatusType (SexOffenderStatusTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE Person ADD CONSTRAINT WorkReleaseStatusType_Person_fk
FOREIGN KEY (WorkReleaseStatusTypeID)
REFERENCES WorkReleaseStatusType (WorkReleaseStatusTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE Person ADD CONSTRAINT ProgramEligibilityType_Person_fk
FOREIGN KEY (ProgramEligibilityTypeID)
REFERENCES ProgramEligibilityType (ProgramEligibilityTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE Person ADD CONSTRAINT DomicileStatusType_Person_fk
FOREIGN KEY (DomicileStatusTypeID)
REFERENCES DomicileStatusType (DomicileStatusTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE Person ADD CONSTRAINT PersonEthnicityType_Person_fk
FOREIGN KEY (PersonEthnicityTypeID)
REFERENCES PersonEthnicityType (PersonEthnicityTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE Person ADD CONSTRAINT MilitaryServiceStatusType_Person_fk
FOREIGN KEY (MilitaryServiceStatusTypeID)
REFERENCES MilitaryServiceStatusType (MilitaryServiceStatusTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE BookingArrest ADD CONSTRAINT Location_BookingArrest_fk
FOREIGN KEY (LocationID)
REFERENCES Location (LocationID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE CustodyStatusChangeArrest ADD CONSTRAINT Location_CustodyStatusChangeArrest_fk
FOREIGN KEY (LocationID)
REFERENCES Location (LocationID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE Person ADD CONSTRAINT Language_Person_fk
FOREIGN KEY (LanguageTypeID)
REFERENCES LanguageType (LanguageTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE Booking ADD CONSTRAINT Facility_Booking_fk
FOREIGN KEY (FacilityID)
REFERENCES Facility (FacilityID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE CustodyStatusChange ADD CONSTRAINT Facility_Custody_Status_Change_fk
FOREIGN KEY (FacilityID)
REFERENCES Facility (FacilityID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE BookingCharge ADD CONSTRAINT BondType_BookingCharge_fk
FOREIGN KEY (BondTypeID)
REFERENCES BondType (BondTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE CustodyStatusChangeCharge ADD CONSTRAINT BondType_CustodyStatusChangeCharge_fk
FOREIGN KEY (BondTypeID)
REFERENCES BondType (BondTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE Booking ADD CONSTRAINT BedType_Booking_fk
FOREIGN KEY (SupervisionUnitTypeID)
REFERENCES SupervisionUnitType (SupervisionUnitTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE CustodyStatusChange ADD CONSTRAINT BedType_Custody_Status_Change_fk
FOREIGN KEY (SupervisionUnitTypeID)
REFERENCES SupervisionUnitType (SupervisionUnitTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE Person ADD CONSTRAINT PersonRace_Person_fk
FOREIGN KEY (PersonRaceTypeID)
REFERENCES PersonRaceType (PersonRaceTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE Person ADD CONSTRAINT PersonSex_Person_fk
FOREIGN KEY (PersonSexTypeID)
REFERENCES PersonSexType (PersonSexTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE BehavioralHealthAssessment ADD CONSTRAINT Person_BehaviorHealthAssessment_fk
FOREIGN KEY (PersonID)
REFERENCES Person (PersonID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE Booking ADD CONSTRAINT Person_Booking_fk
FOREIGN KEY (PersonID)
REFERENCES Person (PersonID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE CustodyStatusChange ADD CONSTRAINT Person_CustodyStatusChange_fk
FOREIGN KEY (PersonID)
REFERENCES Person (PersonID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE BehavioralHealthEvaluation ADD CONSTRAINT BehavioralHealthAssessment_BehavioralHealthEvaluation_fk
FOREIGN KEY (BehavioralHealthAssessmentID)
REFERENCES BehavioralHealthAssessment (BehavioralHealthAssessmentID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE Treatment ADD CONSTRAINT BehavioralHealthAssessment_Treatment_fk
FOREIGN KEY (BehavioralHealthAssessmentID)
REFERENCES BehavioralHealthAssessment (BehavioralHealthAssessmentID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE PrescribedMedication ADD CONSTRAINT BehavioralHealthAssessment_PrescribedMedication_fk
FOREIGN KEY (BehavioralHealthAssessmentID)
REFERENCES BehavioralHealthAssessment (BehavioralHealthAssessmentID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE BehavioralHealthAssessmentCategory ADD CONSTRAINT BehavioralHealthAssessment_BehavioralHealthAssessmentCategory_fk
FOREIGN KEY (BehavioralHealthAssessmentID)
REFERENCES BehavioralHealthAssessment (BehavioralHealthAssessmentID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE BookingCharge ADD CONSTRAINT JurisdictionType_BookingCharge_fk
FOREIGN KEY (ChargeJurisdictionTypeID)
REFERENCES JurisdictionType (JurisdictionTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE CustodyStatusChangeCharge ADD CONSTRAINT JurisdictionType_CustodyStatusChangeCharge_fk
FOREIGN KEY (ChargeJurisdictionTypeID)
REFERENCES JurisdictionType (JurisdictionTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE BookingCharge ADD CONSTRAINT Agency_BookingCharge_fk
FOREIGN KEY (AgencyID)
REFERENCES Agency (AgencyID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE CustodyStatusChangeCharge ADD CONSTRAINT Agency_CustodyStatusChangeCharge_fk
FOREIGN KEY (AgencyID)
REFERENCES Agency (AgencyID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE BookingArrest ADD CONSTRAINT AgencyType_BookingArrest_fk
FOREIGN KEY (ArrestAgencyID)
REFERENCES Agency (AgencyID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE CustodyStatusChangeArrest ADD CONSTRAINT AgencyType_CustodyStatusChangeArrest_fk
FOREIGN KEY (ArrestAgencyID)
REFERENCES Agency (AgencyID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE BookingArrest ADD CONSTRAINT Booking_BookingArrest_fk
FOREIGN KEY (BookingID)
REFERENCES Booking (BookingID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE CustodyStatusChange ADD CONSTRAINT Booking_CustodyStatusChange_fk
FOREIGN KEY (BookingID)
REFERENCES Booking (BookingID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE CustodyRelease ADD CONSTRAINT Booking_CustodyRelease_fk
FOREIGN KEY (BookingID)
REFERENCES Booking (BookingID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE CustodyStatusChangeArrest ADD CONSTRAINT CustodyStatusChange_CustodyStatusChangeArrest_fk
FOREIGN KEY (CustodyStatusChangeID)
REFERENCES CustodyStatusChange (CustodyStatusChangeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE CustodyStatusChangeCharge ADD CONSTRAINT CustodyStatusChangeArrest_CustodyStatusChangeCharge_fk
FOREIGN KEY (CustodyStatusChangeArrestID)
REFERENCES CustodyStatusChangeArrest (CustodyStatusChangeArrestID)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE BookingCharge ADD CONSTRAINT BookingArrest_BookingCharge_fk
FOREIGN KEY (BookingArrestID)
REFERENCES BookingArrest (BookingArrestID)
ON DELETE NO ACTION
ON UPDATE NO ACTION