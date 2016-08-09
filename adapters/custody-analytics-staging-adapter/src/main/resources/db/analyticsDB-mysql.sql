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
 
drop database if exists `CustodyAnalyticsDataStore`;
CREATE DATABASE `CustodyAnalyticsDataStore`; 
use CustodyAnalyticsDataStore;

/**
* Copy DDL from SQL PA below here.  Modify timestamps in fact tables like this:
*                `Timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
**/




CREATE TABLE Location (
                LocationID INT AUTO_INCREMENT NOT NULL,
                AddressSecondaryUnit VARCHAR(150),
                StreetNumber VARCHAR(50),
                StreetName VARCHAR(150),
                City VARCHAR(100),
                State VARCHAR(10),
                PostalCode VARCHAR(10),
                ArrestLocationLatitude NUMERIC(14,10),
                ArrestLocationLongitude NUMERIC(14,10),
                PRIMARY KEY (LocationID)
);


CREATE TABLE Medication (
                MedicationID INT AUTO_INCREMENT NOT NULL,
                GeneralProductID VARCHAR(50),
                ItemName VARCHAR(100),
                PRIMARY KEY (MedicationID)
);


CREATE TABLE LanguageType (
                LanguageTypeID INT AUTO_INCREMENT NOT NULL,
                LanguageTypeDescription VARCHAR(20) NOT NULL,
                PRIMARY KEY (LanguageTypeID)
);


CREATE TABLE EducationLevelType (
                EducationLevelTypeID INT AUTO_INCREMENT NOT NULL,
                EducationLevelTypeDescription VARCHAR(50) NOT NULL,
                PRIMARY KEY (EducationLevelTypeID)
);


CREATE TABLE OccupationType (
                OccupationTypeID INT AUTO_INCREMENT NOT NULL,
                OccupationTypeDescription VARCHAR(50) NOT NULL,
                PRIMARY KEY (OccupationTypeID)
);


CREATE TABLE IncomeLevelType (
                IncomeLevelTypeID INT AUTO_INCREMENT NOT NULL,
                IncomeLevelTypeDescription VARCHAR(50) NOT NULL,
                PRIMARY KEY (IncomeLevelTypeID)
);


CREATE TABLE HousingStatusType (
                HousingStatusTypeID INT AUTO_INCREMENT NOT NULL,
                HousingStatusTypeDescription VARCHAR(50) NOT NULL,
                PRIMARY KEY (HousingStatusTypeID)
);


CREATE TABLE Facility (
                FacilityID INT AUTO_INCREMENT NOT NULL,
                FacilityDescription VARCHAR(100) NOT NULL,
                Capacity INT NOT NULL,
                PRIMARY KEY (FacilityID)
);

ALTER TABLE Facility COMMENT 'Booking Detention Facility';


CREATE TABLE BondType (
                BondTypeID INT AUTO_INCREMENT NOT NULL,
                BondTypeDescription VARCHAR(100) NOT NULL,
                PRIMARY KEY (BondTypeID)
);


CREATE TABLE BedType (
                BedTypeID INT AUTO_INCREMENT NOT NULL,
                BedTypeDescription VARCHAR(50) NOT NULL,
                PRIMARY KEY (BedTypeID)
);


CREATE TABLE BehavioralHealthType (
                BehavioralHealthTypeID INT AUTO_INCREMENT NOT NULL,
                BehavioralHealthDescription VARCHAR(50) NOT NULL,
                PRIMARY KEY (BehavioralHealthTypeID)
);


CREATE TABLE PersonRace (
                PersonRaceID INT AUTO_INCREMENT NOT NULL,
                PersonRaceCode VARCHAR(10) NOT NULL,
                PersonRaceDescription VARCHAR(50) NOT NULL,
                PRIMARY KEY (PersonRaceID)
);


CREATE TABLE PersonSex (
                PersonSexID INT AUTO_INCREMENT NOT NULL,
                PersonSexCode VARCHAR(1) NOT NULL,
                PersonSexDescription VARCHAR(7) NOT NULL,
                PRIMARY KEY (PersonSexID)
);


CREATE TABLE Person (
                PersonID INT AUTO_INCREMENT NOT NULL,
                PersonUniqueIdentifier VARCHAR(36) NOT NULL,
                BookingSubjectNumber VARCHAR(36),
                PersonEyeColor VARCHAR(20),
                PersonHairColor VARCHAR(20),
                PersonHeight VARCHAR(10),
                PersonHeightMeasureUnit VARCHAR(10),
                PersonWeight VARCHAR(10),
                PersonWeightMeasureUnit VARCHAR(10),
                RegisteredSexOffender BOOLEAN,
                PersonBirthDate DATE,
                LanguageTypeID INT,
                CreationDate DATETIME DEFAULT now() NOT NULL,
                PersonSexID INT,
                PersonRaceID INT,
                PRIMARY KEY (PersonID)
);


CREATE TABLE BookingSubject (
                BookingSubjectID INT AUTO_INCREMENT NOT NULL,
                RecidivistIndicator SMALLINT DEFAULT 0 NOT NULL,
                PersonID INT NOT NULL,
                PersonAge INT,
                EducationLevelTypeID INT,
                OccupationTypeID INT,
                IncomeLevelTypeID INT,
                HousingStatusTypeID INT,
                MilitaryServiceStatusCode VARCHAR(20),
                PRIMARY KEY (BookingSubjectID)
);


CREATE TABLE BehavioralHealthAssessment (
                BehavioralHealthAssessmentID INT AUTO_INCREMENT NOT NULL,
                PersonID INT NOT NULL,
                SeriousMentalIllness BOOLEAN,
                HighRiskNeeds BOOLEAN,
                CareEpisodeStartDate DATE,
                CareEpisodeEndDate DATE,
                SubstanceAbuse BOOLEAN,
                GeneralMentalHealthCondition BOOLEAN,
                MedicaidIndicator BOOLEAN,
                RegionalAuthorityAssignmentText VARCHAR(20),
                PRIMARY KEY (BehavioralHealthAssessmentID)
);


CREATE TABLE PrescribedMedication (
                PrescribedMedicationID INT AUTO_INCREMENT NOT NULL,
                BehavioralHealthAssessmentID INT NOT NULL,
                MedicationID INT NOT NULL,
                MedicationDispensingDate DATE,
                MedicationDoseMeasure VARCHAR(10),
                PRIMARY KEY (PrescribedMedicationID)
);


CREATE TABLE Treatment (
                TreatmentID INT AUTO_INCREMENT NOT NULL,
                BehavioralHealthAssessmentID INT NOT NULL,
                StartDate DATE,
                EndDate DATE,
                TreatmentCourtOrdered BOOLEAN,
                TreatmentText VARCHAR(100),
                TreatmentProvider VARCHAR(100),
                TreatmentActive BOOLEAN,
                PRIMARY KEY (TreatmentID)
);


CREATE TABLE BehavioralHealthEvaluation (
                BehavioralHealthEvaluationID INT AUTO_INCREMENT NOT NULL,
                BehavioralHealthTypeID INT NOT NULL,
                BehavioralHealthAssessmentID INT NOT NULL,
                PRIMARY KEY (BehavioralHealthEvaluationID)
);


CREATE TABLE ChargeType (
                ChargeTypeID INT AUTO_INCREMENT NOT NULL,
                ChargeTypeDescription VARCHAR(100) NOT NULL,
                PRIMARY KEY (ChargeTypeID)
);


CREATE TABLE CaseStatusType (
                CaseStatusTypeID INT AUTO_INCREMENT NOT NULL,
                CaseStatusTypeDescription VARCHAR(100) NOT NULL,
                PRIMARY KEY (CaseStatusTypeID)
);


CREATE TABLE JurisdictionType (
                JurisdictionTypeID INT AUTO_INCREMENT NOT NULL,
                JurisdictionTypeDescription VARCHAR(100) NOT NULL,
                PRIMARY KEY (JurisdictionTypeID)
);


CREATE TABLE AgencyType (
                AgencyTypeID INT AUTO_INCREMENT NOT NULL,
                AgencyTypeDescription VARCHAR(50) NOT NULL,
                PRIMARY KEY (AgencyTypeID)
);


CREATE TABLE Booking (
                BookingID INT AUTO_INCREMENT NOT NULL,
                JurisdictionTypeID INT NOT NULL,
                BookingReportDate DATETIME NOT NULL,
                BookingReportID VARCHAR(30) NOT NULL,
                CaseStatusTypeID INT NOT NULL,
                BookingDate DATETIME NOT NULL,
                CommitDate DATE NOT NULL,
                ScheduledReleaseDate DATE,
                FacilityID INT NOT NULL,
                BedTypeID INT NOT NULL,
                BookingNumber VARCHAR(50) NOT NULL,
                BookingSubjectID INT NOT NULL,
                PRIMARY KEY (BookingID)
);


CREATE TABLE CustodyRelease (
                CustodyReleaseID INT AUTO_INCREMENT NOT NULL,
                BookingID INT NOT NULL,
                ReleaseDate DATETIME NOT NULL,
                ReportDate DATETIME NOT NULL,
                PRIMARY KEY (CustodyReleaseID)
);


CREATE TABLE CustodyStatusChange (
                CustodyStatusChangeID INT AUTO_INCREMENT NOT NULL,
                BookingID INT NOT NULL,
                ReportID VARCHAR(30) NOT NULL,
                BookingDate DATETIME NOT NULL,
                CommitDate DATE NOT NULL,
                ScheduledReleaseDate DATE,
                BookingSubjectID INT NOT NULL,
                BedTypeID INT,
                CaseStatusTypeID INT NOT NULL,
                JurisdictionTypeID INT NOT NULL,
                FacilityID INT NOT NULL,
                ReportDate DATETIME NOT NULL,
                PRIMARY KEY (CustodyStatusChangeID)
);


CREATE TABLE CustodyStatusChangeArrest (
                CustodyStatusChangeArrestID INT AUTO_INCREMENT NOT NULL,
                CustodyStatusChangeID INT NOT NULL,
                LocationID INT NOT NULL,
                PRIMARY KEY (CustodyStatusChangeArrestID)
);


CREATE TABLE CustodyStatusChangeCharge (
                CustodyStatusChangeChargeID INT AUTO_INCREMENT NOT NULL,
                CustodyStatusChangeArrestID INT NOT NULL,
                ChargeTypeID INT NOT NULL,
                NextCourtDate DATE,
                NextCourtName VARCHAR(50),
                BondTypeID INT NOT NULL,
                AgencyTypeID INT NOT NULL,
                BondAmount NUMERIC(10,2),
                PRIMARY KEY (CustodyStatusChangeChargeID)
);


CREATE TABLE BookingArrest (
                BookingArrestID INT AUTO_INCREMENT NOT NULL,
                BookingID INT NOT NULL,
                LocationID INT NOT NULL,
                PRIMARY KEY (BookingArrestID)
);


CREATE TABLE BookingCharge (
                BookingChargeID INT AUTO_INCREMENT NOT NULL,
                BookingArrestID INT NOT NULL,
                ChargeTypeID INT NOT NULL,
                NextCourtName VARCHAR(50),
                NextCourtDate DATE,
                AgencyTypeID INT NOT NULL,
                BondAmount NUMERIC(10,2),
                BondTypeID INT NOT NULL,
                PRIMARY KEY (BookingChargeID)
);


ALTER TABLE BookingArrest ADD CONSTRAINT location_bookingarrest_fk
FOREIGN KEY (LocationID)
REFERENCES Location (LocationID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE CustodyStatusChangeArrest ADD CONSTRAINT location_custodystatuschangearrest_fk
FOREIGN KEY (LocationID)
REFERENCES Location (LocationID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PrescribedMedication ADD CONSTRAINT medication_prescribedmedication_fk
FOREIGN KEY (MedicationID)
REFERENCES Medication (MedicationID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Person ADD CONSTRAINT language_person_fk
FOREIGN KEY (LanguageTypeID)
REFERENCES LanguageType (LanguageTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE BookingSubject ADD CONSTRAINT education_bookingsubject_fk
FOREIGN KEY (EducationLevelTypeID)
REFERENCES EducationLevelType (EducationLevelTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE BookingSubject ADD CONSTRAINT occupation_bookingsubject_fk
FOREIGN KEY (OccupationTypeID)
REFERENCES OccupationType (OccupationTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE BookingSubject ADD CONSTRAINT incomelevel_bookingsubject_fk
FOREIGN KEY (IncomeLevelTypeID)
REFERENCES IncomeLevelType (IncomeLevelTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE BookingSubject ADD CONSTRAINT housingstatus_bookingsubject_fk
FOREIGN KEY (HousingStatusTypeID)
REFERENCES HousingStatusType (HousingStatusTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Booking ADD CONSTRAINT facility_booking_fk
FOREIGN KEY (FacilityID)
REFERENCES Facility (FacilityID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE CustodyStatusChange ADD CONSTRAINT facility_custody_status_change_fk
FOREIGN KEY (FacilityID)
REFERENCES Facility (FacilityID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE BookingCharge ADD CONSTRAINT bondtype_bookingcharge_fk
FOREIGN KEY (BondTypeID)
REFERENCES BondType (BondTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE CustodyStatusChangeCharge ADD CONSTRAINT bondtype_custodystatuschangecharge_fk
FOREIGN KEY (BondTypeID)
REFERENCES BondType (BondTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Booking ADD CONSTRAINT bedtype_booking_fk
FOREIGN KEY (BedTypeID)
REFERENCES BedType (BedTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE CustodyStatusChange ADD CONSTRAINT bedtype_custody_status_change_fk
FOREIGN KEY (BedTypeID)
REFERENCES BedType (BedTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE BehavioralHealthEvaluation ADD CONSTRAINT behavioralhealthtype_behavioralhealthevaluation_fk
FOREIGN KEY (BehavioralHealthTypeID)
REFERENCES BehavioralHealthType (BehavioralHealthTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Person ADD CONSTRAINT personrace_person_fk
FOREIGN KEY (PersonRaceID)
REFERENCES PersonRace (PersonRaceID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Person ADD CONSTRAINT personsex_person_fk
FOREIGN KEY (PersonSexID)
REFERENCES PersonSex (PersonSexID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE BehavioralHealthAssessment ADD CONSTRAINT person_behaviorhealthassessment_fk
FOREIGN KEY (PersonID)
REFERENCES Person (PersonID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE BookingSubject ADD CONSTRAINT person_bookingsubject_fk
FOREIGN KEY (PersonID)
REFERENCES Person (PersonID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Booking ADD CONSTRAINT bookingsubject_booking_fk
FOREIGN KEY (BookingSubjectID)
REFERENCES BookingSubject (BookingSubjectID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE CustodyStatusChange ADD CONSTRAINT bookingsubject_custody_status_change_fk
FOREIGN KEY (BookingSubjectID)
REFERENCES BookingSubject (BookingSubjectID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE BehavioralHealthEvaluation ADD CONSTRAINT behavioralhealthassessment_behavioralhealthevaluation_fk
FOREIGN KEY (BehavioralHealthAssessmentID)
REFERENCES BehavioralHealthAssessment (BehavioralHealthAssessmentID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Treatment ADD CONSTRAINT behavioralhealthassessment_treatment_fk
FOREIGN KEY (BehavioralHealthAssessmentID)
REFERENCES BehavioralHealthAssessment (BehavioralHealthAssessmentID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PrescribedMedication ADD CONSTRAINT behavioralhealthassessment_prescribedmedication_fk
FOREIGN KEY (BehavioralHealthAssessmentID)
REFERENCES BehavioralHealthAssessment (BehavioralHealthAssessmentID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE BookingCharge ADD CONSTRAINT chargetype_charge_fk
FOREIGN KEY (ChargeTypeID)
REFERENCES ChargeType (ChargeTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE CustodyStatusChangeCharge ADD CONSTRAINT chargetype_custodystatuschangecharge_fk
FOREIGN KEY (ChargeTypeID)
REFERENCES ChargeType (ChargeTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Booking ADD CONSTRAINT status_booking_fk
FOREIGN KEY (CaseStatusTypeID)
REFERENCES CaseStatusType (CaseStatusTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE CustodyStatusChange ADD CONSTRAINT casestatus_custody_status_change_fk
FOREIGN KEY (CaseStatusTypeID)
REFERENCES CaseStatusType (CaseStatusTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Booking ADD CONSTRAINT jurisdiction_booking_fk
FOREIGN KEY (JurisdictionTypeID)
REFERENCES JurisdictionType (JurisdictionTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE CustodyStatusChange ADD CONSTRAINT jurisdiction_custody_status_change_fk
FOREIGN KEY (JurisdictionTypeID)
REFERENCES JurisdictionType (JurisdictionTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE BookingCharge ADD CONSTRAINT agency_bookingcharge_fk
FOREIGN KEY (AgencyTypeID)
REFERENCES AgencyType (AgencyTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE CustodyStatusChangeCharge ADD CONSTRAINT agency_custodystatuschangecharge_fk
FOREIGN KEY (AgencyTypeID)
REFERENCES AgencyType (AgencyTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE BookingArrest ADD CONSTRAINT booking_bookingarrest_fk
FOREIGN KEY (BookingID)
REFERENCES Booking (BookingID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE CustodyStatusChange ADD CONSTRAINT booking_custodystatuschange_fk
FOREIGN KEY (BookingID)
REFERENCES Booking (BookingID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE CustodyRelease ADD CONSTRAINT booking_custodyrelease_fk
FOREIGN KEY (BookingID)
REFERENCES Booking (BookingID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE CustodyStatusChangeArrest ADD CONSTRAINT custodystatuschange_custodystatuschangearrest_fk
FOREIGN KEY (CustodyStatusChangeID)
REFERENCES CustodyStatusChange (CustodyStatusChangeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE CustodyStatusChangeCharge ADD CONSTRAINT custodystatuschangearrest_custodystatuschangecharge_fk
FOREIGN KEY (CustodyStatusChangeArrestID)
REFERENCES CustodyStatusChangeArrest (CustodyStatusChangeArrestID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE BookingCharge ADD CONSTRAINT bookingarrest_bookingcharge_fk
FOREIGN KEY (BookingArrestID)
REFERENCES BookingArrest (BookingArrestID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;