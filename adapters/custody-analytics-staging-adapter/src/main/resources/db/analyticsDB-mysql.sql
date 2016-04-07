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


CREATE TABLE Language (
                LanguageID INT AUTO_INCREMENT NOT NULL,
                Language VARCHAR(20) NOT NULL,
                PRIMARY KEY (LanguageID)
);


CREATE TABLE EducationLevel (
                EducationLevelID INT AUTO_INCREMENT NOT NULL,
                EducationLevel VARCHAR(50) NOT NULL,
                PRIMARY KEY (EducationLevelID)
);


CREATE TABLE Occupation (
                OccupationID INT AUTO_INCREMENT NOT NULL,
                Occupation VARCHAR(50) NOT NULL,
                PRIMARY KEY (OccupationID)
);


CREATE TABLE IncomeLevel (
                IncomeLevelID INT AUTO_INCREMENT NOT NULL,
                IncomeLevel VARCHAR(50) NOT NULL,
                PRIMARY KEY (IncomeLevelID)
);


CREATE TABLE HousingStatus (
                HousingStatusID INT AUTO_INCREMENT NOT NULL,
                HousingStatusDescription VARCHAR(50) NOT NULL,
                PRIMARY KEY (HousingStatusID)
);


CREATE TABLE Facility (
                FacilityID INT AUTO_INCREMENT NOT NULL,
                FacilityName VARCHAR(100) NOT NULL,
                Capacity INT NOT NULL,
                PRIMARY KEY (FacilityID)
);

ALTER TABLE Facility COMMENT 'Booking Detention Facility';


CREATE TABLE BondType (
                BondTypeID INT AUTO_INCREMENT NOT NULL,
                BondType VARCHAR(100) NOT NULL,
                PRIMARY KEY (BondTypeID)
);


CREATE TABLE BedType (
                BedTypeID INT AUTO_INCREMENT NOT NULL,
                BedTypeDescription VARCHAR(50) NOT NULL,
                PRIMARY KEY (BedTypeID)
);


CREATE TABLE PretrialStatus (
                PretrialStatusID INT AUTO_INCREMENT NOT NULL,
                PretrialStatus VARCHAR(100) NOT NULL,
                PRIMARY KEY (PretrialStatusID)
);


CREATE TABLE BehavioralHealthType (
                BehavioralHealthTypeID INT AUTO_INCREMENT NOT NULL,
                BehavioralHealthDescription VARCHAR(50) NOT NULL,
                PRIMARY KEY (BehavioralHealthTypeID)
);


CREATE TABLE PersonRace (
                PersonRaceID INT AUTO_INCREMENT NOT NULL,
                PersonRaceDescription VARCHAR(50) NOT NULL,
                PRIMARY KEY (PersonRaceID)
);


CREATE TABLE PersonSex (
                PersonSexID INT AUTO_INCREMENT NOT NULL,
                PersonSexDescription VARCHAR(7) NOT NULL,
                PRIMARY KEY (PersonSexID)
);


CREATE TABLE Person (
                PersonID INT AUTO_INCREMENT NOT NULL,
                PersonUniqueIdentifier VARCHAR(36) NOT NULL,
                PersonSexID INT,
                PersonBirthDate DATE,
                PersonRaceID INT,
                LanguageID INT,
                CreationDate DATETIME DEFAULT now() NOT NULL,
                PRIMARY KEY (PersonID)
);


CREATE TABLE BookingSubject (
                BookingSubjectID INT AUTO_INCREMENT NOT NULL,
                RecidivistIndicator SMALLINT DEFAULT 0 NOT NULL,
                PersonID INT NOT NULL,
                PersonAge INT,
                EducationLevelID INT,
                OccupationID INT,
                IncomeLevelID INT,
                HousingStatusID INT,
                PRIMARY KEY (BookingSubjectID)
);


CREATE TABLE BehavioralHealthAssessment (
                BehavioralHealthAssessmentID INT AUTO_INCREMENT NOT NULL,
                BehavioralHealthTypeID INT NOT NULL,
                EvaluationDate DATE NOT NULL,
                PersonID INT NOT NULL,
                PRIMARY KEY (BehavioralHealthAssessmentID)
);


CREATE TABLE ChargeType (
                ChargeTypeID INT AUTO_INCREMENT NOT NULL,
                ChargeType VARCHAR(100) NOT NULL,
                PRIMARY KEY (ChargeTypeID)
);


CREATE TABLE CaseStatus (
                CaseStatusID INT AUTO_INCREMENT NOT NULL,
                CaseStatus VARCHAR(100) NOT NULL,
                PRIMARY KEY (CaseStatusID)
);


CREATE TABLE Jurisdiction (
                JurisdictionID INT AUTO_INCREMENT NOT NULL,
                JurisdictionName VARCHAR(100) NOT NULL,
                PRIMARY KEY (JurisdictionID)
);


CREATE TABLE Agency (
                AgencyID INT AUTO_INCREMENT NOT NULL,
                AgencyName VARCHAR(40) NOT NULL,
                PRIMARY KEY (AgencyID)
);


CREATE TABLE Booking (
                BookingID BIGINT AUTO_INCREMENT NOT NULL,
                JurisdictionID INT NOT NULL,
                BookingReportDate DATETIME NOT NULL,
                BookingReportID VARCHAR(30) NOT NULL,
                SendingAgencyID INT NOT NULL,
                CaseStatusID INT NOT NULL,
                BookingDate DATETIME NOT NULL,
                CommitDate DATE NOT NULL,
                SupervisionReleaseDate DATETIME,
                PretrialStatusID INT NOT NULL,
                FacilityID INT NOT NULL,
                BedTypeID INT NOT NULL,
                BookingNumber VARCHAR(50) NOT NULL,
                ArrestLocationLatitude NUMERIC(14,10),
                ArrestLocationLongitude NUMERIC(14,10),
                BookingSubjectID INT NOT NULL,
                PRIMARY KEY (BookingID)
);


CREATE TABLE BookingCharge (
                BookingChargeID INT AUTO_INCREMENT NOT NULL,
                BookingID BIGINT NOT NULL,
                ChargeTypeID INT NOT NULL,
                BondAmount NUMERIC(10,2),
                BondTypeID INT,
                PRIMARY KEY (BookingChargeID)
);


ALTER TABLE Person ADD CONSTRAINT language_person_fk
FOREIGN KEY (LanguageID)
REFERENCES Language (LanguageID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE BookingSubject ADD CONSTRAINT education_bookingsubject_fk
FOREIGN KEY (EducationLevelID)
REFERENCES EducationLevel (EducationLevelID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE BookingSubject ADD CONSTRAINT occupation_bookingsubject_fk
FOREIGN KEY (OccupationID)
REFERENCES Occupation (OccupationID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE BookingSubject ADD CONSTRAINT incomelevel_bookingsubject_fk
FOREIGN KEY (IncomeLevelID)
REFERENCES IncomeLevel (IncomeLevelID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE BookingSubject ADD CONSTRAINT housingstatus_bookingsubject_fk
FOREIGN KEY (HousingStatusID)
REFERENCES HousingStatus (HousingStatusID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Booking ADD CONSTRAINT facility_booking_fk
FOREIGN KEY (FacilityID)
REFERENCES Facility (FacilityID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE BookingCharge ADD CONSTRAINT bondtype_bookingcharge_fk
FOREIGN KEY (BondTypeID)
REFERENCES BondType (BondTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Booking ADD CONSTRAINT bedtype_booking_fk
FOREIGN KEY (BedTypeID)
REFERENCES BedType (BedTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Booking ADD CONSTRAINT pretrialstatus_booking_fk
FOREIGN KEY (PretrialStatusID)
REFERENCES PretrialStatus (PretrialStatusID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE BehavioralHealthAssessment ADD CONSTRAINT healthissue_jailcustodyhealthissueassociation_fk
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

ALTER TABLE BookingCharge ADD CONSTRAINT chargetype_charge_fk
FOREIGN KEY (ChargeTypeID)
REFERENCES ChargeType (ChargeTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Booking ADD CONSTRAINT status_booking_fk
FOREIGN KEY (CaseStatusID)
REFERENCES CaseStatus (CaseStatusID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Booking ADD CONSTRAINT jurisdiction_booking_fk
FOREIGN KEY (JurisdictionID)
REFERENCES Jurisdiction (JurisdictionID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Booking ADD CONSTRAINT agency_booking_fk
FOREIGN KEY (SendingAgencyID)
REFERENCES Agency (AgencyID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE BookingCharge ADD CONSTRAINT booking_charge_fk
FOREIGN KEY (BookingID)
REFERENCES Booking (BookingID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;