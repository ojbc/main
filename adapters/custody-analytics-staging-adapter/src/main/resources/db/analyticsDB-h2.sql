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


CREATE TABLE Medication (MedicationID BIGINT AUTO_INCREMENT NOT NULL, GeneralProductID VARCHAR(50), ItemName VARCHAR(100));

ALTER TABLE Medication ADD CONSTRAINT medicationid PRIMARY KEY (MedicationID);

CREATE SEQUENCE Medication_MedicationID_seq;

CREATE TABLE CustodyRelease (CustodyReleaseID BIGINT AUTO_INCREMENT NOT NULL, BookingNumber VARCHAR(50) NOT NULL, ReleaseDate TIMESTAMP NOT NULL, ScheduledReleaseDate date, ReportDate TIMESTAMP NOT NULL);

ALTER TABLE CustodyRelease ADD CONSTRAINT custodyreleaseid PRIMARY KEY (CustodyReleaseID);

CREATE SEQUENCE CustodyRelease_CustodyReleaseID_seq;

CREATE TABLE Language (LanguageID INT AUTO_INCREMENT NOT NULL, Language VARCHAR(20) NOT NULL);

ALTER TABLE Language ADD CONSTRAINT languageid PRIMARY KEY (LanguageID);

CREATE SEQUENCE Language_LanguageID_seq_1_1;

CREATE TABLE EducationLevel (EducationLevelID INT AUTO_INCREMENT NOT NULL, EducationLevel VARCHAR(50) NOT NULL);

ALTER TABLE EducationLevel ADD CONSTRAINT educationlevelid PRIMARY KEY (EducationLevelID);

CREATE SEQUENCE EducationLevel_EducationLevelID_seq;

CREATE TABLE Occupation (OccupationID INT AUTO_INCREMENT NOT NULL, Occupation VARCHAR(50) NOT NULL);

ALTER TABLE Occupation ADD CONSTRAINT occupationid PRIMARY KEY (OccupationID);

CREATE SEQUENCE Occupation_OccupationID_seq_1_1;

CREATE TABLE IncomeLevel (IncomeLevelID INT AUTO_INCREMENT NOT NULL, IncomeLevel VARCHAR(50) NOT NULL);

ALTER TABLE IncomeLevel ADD CONSTRAINT incomelevelid PRIMARY KEY (IncomeLevelID);

CREATE SEQUENCE IncomeLevel_IncomeLevelID_seq_1_1;

CREATE TABLE HousingStatus (HousingStatusID INT AUTO_INCREMENT NOT NULL, HousingStatusDescription VARCHAR(50) NOT NULL);

ALTER TABLE HousingStatus ADD CONSTRAINT housingstatusid PRIMARY KEY (HousingStatusID);

CREATE SEQUENCE HousingStatus_HousingStatusID_seq_1;

CREATE TABLE Facility (FacilityID INT AUTO_INCREMENT NOT NULL, FacilityName VARCHAR(100) NOT NULL, Capacity INT NOT NULL);

ALTER TABLE Facility ADD CONSTRAINT facilityid PRIMARY KEY (FacilityID);

CREATE SEQUENCE Facility_FacilityID_seq_1;

CREATE TABLE BondType (BondTypeID INT AUTO_INCREMENT NOT NULL, BondType VARCHAR(100) NOT NULL);

ALTER TABLE BondType ADD CONSTRAINT bondtypeid PRIMARY KEY (BondTypeID);

CREATE SEQUENCE BondType_BondTypeID_seq_1_1_1_1;

CREATE TABLE BedType (BedTypeID INT AUTO_INCREMENT NOT NULL, BedTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE BedType ADD CONSTRAINT bedtypeid PRIMARY KEY (BedTypeID);

CREATE SEQUENCE BedType_BedTypeID_seq_1;

CREATE TABLE BehavioralHealthType (BehavioralHealthTypeID INT AUTO_INCREMENT NOT NULL, BehavioralHealthDescription VARCHAR(50) NOT NULL);

ALTER TABLE BehavioralHealthType ADD CONSTRAINT behavioralhealthtypeid PRIMARY KEY (BehavioralHealthTypeID);

CREATE SEQUENCE BehavioralHealthType_BehavioralHealthTypeID_seq;

CREATE TABLE PersonRace (PersonRaceID SMALLINT AUTO_INCREMENT NOT NULL, PersonRaceCode VARCHAR(10) NOT NULL, PersonRaceDescription VARCHAR(50) NOT NULL);

ALTER TABLE PersonRace ADD CONSTRAINT personraceid PRIMARY KEY (PersonRaceID);

CREATE SEQUENCE PersonRace_PersonRaceID_seq;

CREATE TABLE PersonSex (PersonSexID SMALLINT AUTO_INCREMENT NOT NULL, PersonSexCode VARCHAR(1) NOT NULL, PersonSexDescription VARCHAR(7) NOT NULL);

ALTER TABLE PersonSex ADD CONSTRAINT personsexid PRIMARY KEY (PersonSexID);

CREATE SEQUENCE PersonSex_PersonSexID_seq1;

CREATE TABLE Person (PersonID INT AUTO_INCREMENT NOT NULL, PersonUniqueIdentifier VARCHAR(36) NOT NULL, BookingSubjectNumber VARCHAR(36), PersonSSN VARCHAR(15), PersonSID VARCHAR(15), PersonEyeColor VARCHAR(20), PersonHairColor VARCHAR(20), PersonHeight VARCHAR(10), PersonHeightMeasureUnit VARCHAR(10), PersonWeight VARCHAR(10), PersonWeightMeasureUnit VARCHAR(10), RegisteredSexOffender BOOLEAN, PersonBirthDate date, LanguageID INT, PersonSexID SMALLINT NOT NULL, PersonRaceID SMALLINT NOT NULL, CreationDate TIMESTAMP DEFAULT NOW() NOT NULL);

ALTER TABLE Person ADD CONSTRAINT personid PRIMARY KEY (PersonID);

CREATE SEQUENCE Person_PersonID_seq_1;

CREATE TABLE BookingSubject (BookingSubjectID INT AUTO_INCREMENT NOT NULL, RecidivistIndicator SMALLINT DEFAULT 0 NOT NULL, PersonID INT NOT NULL, PersonAge INT, EducationLevelID INT, OccupationID INT, IncomeLevelID INT, HousingStatusID INT, MilitaryServiceStatusCode VARCHAR(20));

ALTER TABLE BookingSubject ADD CONSTRAINT bookingsubjectid PRIMARY KEY (BookingSubjectID);

CREATE SEQUENCE BookingSubject_BookingSubjectID_seq_1;

CREATE TABLE BehavioralHealthAssessment (BehavioralHealthAssessmentID INT AUTO_INCREMENT NOT NULL, PersonID INT NOT NULL, SeriousMentalIllness BOOLEAN, HighRiskNeeds BOOLEAN, CareEpisodeStartDate date, CareEpisodeEndDate date, SubstanceAbuse BOOLEAN, GeneralMentalHealthCondition BOOLEAN, MedicaidIndicator BOOLEAN, RegionalAuthorityAssignmentText VARCHAR(20));

ALTER TABLE BehavioralHealthAssessment ADD CONSTRAINT behavioralhealthassessmentid PRIMARY KEY (BehavioralHealthAssessmentID);

CREATE SEQUENCE BehavioralHealthAssessment_BehavioralHealthAssessmentID_seq;

CREATE TABLE PrescribedMedication (PrescribedMedicationID BIGINT AUTO_INCREMENT NOT NULL, BehavioralHealthAssessmentID INT NOT NULL, MedicationID BIGINT NOT NULL, MedicationDispensingDate date, MedicationDoseMeasure VARCHAR(10));

ALTER TABLE PrescribedMedication ADD CONSTRAINT prescribedmedicationid PRIMARY KEY (PrescribedMedicationID);

CREATE SEQUENCE PrescribedMedication_PrescribedMedicationID_seq;

CREATE TABLE Treatment (TreatmentID BIGINT AUTO_INCREMENT NOT NULL, BehavioralHealthAssessmentID INT NOT NULL, StartDate date, EndDate date, TreatmentCourtOrdered BOOLEAN, TreatmentText VARCHAR(100), TreatmentProvider VARCHAR(100), TreatmentActive BOOLEAN);

ALTER TABLE Treatment ADD CONSTRAINT treatmentid PRIMARY KEY (TreatmentID);

CREATE SEQUENCE Treatment_TreatmentID_seq;

CREATE TABLE BehavioralHealthEvaluation (BehavioralHealthEvaluationID BIGINT AUTO_INCREMENT NOT NULL, BehavioralHealthTypeID INT NOT NULL, BehavioralHealthAssessmentID INT NOT NULL);

ALTER TABLE BehavioralHealthEvaluation ADD CONSTRAINT behavioralhealthevaluationid PRIMARY KEY (BehavioralHealthEvaluationID);

CREATE SEQUENCE BehavioralHealthEvaluation_BehavioralHealthEvaluationID_seq;

CREATE TABLE ChargeType (ChargeTypeID INT AUTO_INCREMENT NOT NULL, ChargeType VARCHAR(100) NOT NULL);

ALTER TABLE ChargeType ADD CONSTRAINT chargetypeid PRIMARY KEY (ChargeTypeID);

CREATE SEQUENCE ChargeType_ChargeTypeID_seq;

CREATE TABLE CaseStatus (CaseStatusID INT AUTO_INCREMENT NOT NULL, CaseStatus VARCHAR(100) NOT NULL);

ALTER TABLE CaseStatus ADD CONSTRAINT statusid PRIMARY KEY (CaseStatusID);

CREATE SEQUENCE CaseStatus_CaseStatusID_seq;

CREATE TABLE Jurisdiction (JurisdictionID INT AUTO_INCREMENT NOT NULL, JurisdictionName VARCHAR(100) NOT NULL);

ALTER TABLE Jurisdiction ADD CONSTRAINT jurisdictionid PRIMARY KEY (JurisdictionID);

CREATE SEQUENCE Jurisdiction_JurisdictionID_seq_1;

CREATE TABLE CustodyStatusChange (CustodyStatusChangeID BIGINT AUTO_INCREMENT NOT NULL, ReportID VARCHAR(30) NOT NULL, BookingDate TIMESTAMP NOT NULL, CommitDate date NOT NULL, BookingNumber VARCHAR(50) NOT NULL, ScheduledReleaseDate date, ArrestLocationLatitude NUMBER(14, 10), ArrestLocationLongitude NUMBER(14, 10), BookingSubjectID INT NOT NULL, BedTypeID INT NOT NULL, CaseStatusID INT NOT NULL, JurisdictionID INT NOT NULL, FacilityID INT NOT NULL, ReportDate TIMESTAMP NOT NULL);

ALTER TABLE CustodyStatusChange ADD CONSTRAINT custodystatuschangeid PRIMARY KEY (CustodyStatusChangeID);

CREATE SEQUENCE CustodyStatusChange_CustodyStatusChangeID_seq;

CREATE TABLE CustodyStatusChangeArrest (CustodyStatusChangeArrestID BIGINT AUTO_INCREMENT NOT NULL, CustodyStatusChangeID BIGINT NOT NULL, StreetNumber VARCHAR(50), StreetName VARCHAR(150), AddressSecondaryUnit VARCHAR(150), City VARCHAR(100), State VARCHAR(10), PostalCode VARCHAR(10), ArrestLocationLatitude NUMBER(14, 10), ArrestLocationLongitude NUMBER(14, 10));

ALTER TABLE CustodyStatusChangeArrest ADD CONSTRAINT custodystatuschangearrestid PRIMARY KEY (CustodyStatusChangeArrestID);

CREATE SEQUENCE CustodyStatusChangeArrest_CustodyStatusChangeArrestID_seq_1;

CREATE TABLE Agency (AgencyID INT AUTO_INCREMENT NOT NULL, AgencyName VARCHAR(40) NOT NULL);

ALTER TABLE Agency ADD CONSTRAINT agencyid PRIMARY KEY (AgencyID);

CREATE SEQUENCE Agency_AgencyID_seq_1;

CREATE TABLE CustodyStatusChangeCharge (CustodyStatusChangeChargeID BIGINT AUTO_INCREMENT NOT NULL, CustodyStatusChangeArrestID BIGINT NOT NULL, ChargeTypeID INT NOT NULL, NextCourtDate date, NextCourtName VARCHAR(50), BondTypeID INT NOT NULL, AgencyID INT NOT NULL, BondAmount NUMBER(10, 2));

ALTER TABLE CustodyStatusChangeCharge ADD CONSTRAINT custodystatuschangechargeid PRIMARY KEY (CustodyStatusChangeChargeID);

CREATE SEQUENCE CustodyStatusChangeCharge_CustodyStatusChangeChargeID_seq;

CREATE TABLE Booking (BookingID BIGINT AUTO_INCREMENT NOT NULL, JurisdictionID INT NOT NULL, BookingReportDate TIMESTAMP NOT NULL, BookingReportID VARCHAR(30) NOT NULL, CaseStatusID INT NOT NULL, BookingDate TIMESTAMP NOT NULL, CommitDate date NOT NULL, ScheduledReleaseDate date, FacilityID INT NOT NULL, BedTypeID INT NOT NULL, BookingNumber VARCHAR(50) NOT NULL, BookingSubjectID INT NOT NULL);

ALTER TABLE Booking ADD CONSTRAINT bookingid PRIMARY KEY (BookingID);

CREATE SEQUENCE Booking_BookingID_seq;

CREATE TABLE BookingArrest (BookingArrestID BIGINT AUTO_INCREMENT NOT NULL, BookingID BIGINT NOT NULL, AddressSecondaryUnit VARCHAR(150), StreetNumber VARCHAR(50), StreetName VARCHAR(150), City VARCHAR(100), State VARCHAR(10), PostalCode VARCHAR(10), ArrestLocationLatitude NUMBER(14, 10), ArrestLocationLongitude NUMBER(14, 10));

ALTER TABLE BookingArrest ADD CONSTRAINT bookingarrestid PRIMARY KEY (BookingArrestID);

CREATE SEQUENCE BookingArrest_BookingArrestID_seq_1;

CREATE TABLE BookingCharge (BookingChargeID INT AUTO_INCREMENT NOT NULL, BookingArrestID BIGINT NOT NULL, ChargeTypeID INT NOT NULL, NextCourtName VARCHAR(50), NextCourtDate date, AgencyID INT NOT NULL, BondAmount NUMBER(10, 2), BondTypeID INT NOT NULL);

ALTER TABLE BookingCharge ADD CONSTRAINT bookingchargeid PRIMARY KEY (BookingChargeID);

CREATE SEQUENCE BookingCharge_BookingChargeID_seq;

ALTER TABLE PrescribedMedication ADD CONSTRAINT medication_prescribedmedication_fk FOREIGN KEY (MedicationID) REFERENCES Medication (MedicationID);

ALTER TABLE Person ADD CONSTRAINT language_person_fk FOREIGN KEY (LanguageID) REFERENCES Language (LanguageID);

ALTER TABLE BookingSubject ADD CONSTRAINT education_bookingsubject_fk FOREIGN KEY (EducationLevelID) REFERENCES EducationLevel (EducationLevelID);

ALTER TABLE BookingSubject ADD CONSTRAINT occupation_bookingsubject_fk FOREIGN KEY (OccupationID) REFERENCES Occupation (OccupationID);

ALTER TABLE BookingSubject ADD CONSTRAINT incomelevel_bookingsubject_fk FOREIGN KEY (IncomeLevelID) REFERENCES IncomeLevel (IncomeLevelID);

ALTER TABLE BookingSubject ADD CONSTRAINT housingstatus_bookingsubject_fk FOREIGN KEY (HousingStatusID) REFERENCES HousingStatus (HousingStatusID);

ALTER TABLE Booking ADD CONSTRAINT facility_booking_fk FOREIGN KEY (FacilityID) REFERENCES Facility (FacilityID);

ALTER TABLE CustodyStatusChange ADD CONSTRAINT facility_custody_status_change_fk FOREIGN KEY (FacilityID) REFERENCES Facility (FacilityID);

ALTER TABLE BookingCharge ADD CONSTRAINT bondtype_bookingcharge_fk FOREIGN KEY (BondTypeID) REFERENCES BondType (BondTypeID);

ALTER TABLE CustodyStatusChangeCharge ADD CONSTRAINT bondtype_custodystatuschangecharge_fk FOREIGN KEY (BondTypeID) REFERENCES BondType (BondTypeID);

ALTER TABLE Booking ADD CONSTRAINT bedtype_booking_fk FOREIGN KEY (BedTypeID) REFERENCES BedType (BedTypeID);

ALTER TABLE CustodyStatusChange ADD CONSTRAINT bedtype_custody_status_change_fk FOREIGN KEY (BedTypeID) REFERENCES BedType (BedTypeID);

ALTER TABLE BehavioralHealthEvaluation ADD CONSTRAINT behavioralhealthtype_behavioralhealthevaluation_fk FOREIGN KEY (BehavioralHealthTypeID) REFERENCES BehavioralHealthType (BehavioralHealthTypeID);

ALTER TABLE Person ADD CONSTRAINT personrace_person_fk FOREIGN KEY (PersonRaceID) REFERENCES PersonRace (PersonRaceID);

ALTER TABLE Person ADD CONSTRAINT personsex_person_fk FOREIGN KEY (PersonSexID) REFERENCES PersonSex (PersonSexID);

ALTER TABLE BehavioralHealthAssessment ADD CONSTRAINT person_behaviorhealthassessment_fk FOREIGN KEY (PersonID) REFERENCES Person (PersonID);

ALTER TABLE BookingSubject ADD CONSTRAINT person_bookingsubject_fk FOREIGN KEY (PersonID) REFERENCES Person (PersonID);

ALTER TABLE Booking ADD CONSTRAINT bookingsubject_booking_fk FOREIGN KEY (BookingSubjectID) REFERENCES BookingSubject (BookingSubjectID);

ALTER TABLE CustodyStatusChange ADD CONSTRAINT bookingsubject_custody_status_change_fk FOREIGN KEY (BookingSubjectID) REFERENCES BookingSubject (BookingSubjectID);

ALTER TABLE BehavioralHealthEvaluation ADD CONSTRAINT behavioralhealthassessment_behavioralhealthevaluation_fk FOREIGN KEY (BehavioralHealthAssessmentID) REFERENCES BehavioralHealthAssessment (BehavioralHealthAssessmentID);

ALTER TABLE Treatment ADD CONSTRAINT behavioralhealthassessment_treatment_fk FOREIGN KEY (BehavioralHealthAssessmentID) REFERENCES BehavioralHealthAssessment (BehavioralHealthAssessmentID);

ALTER TABLE PrescribedMedication ADD CONSTRAINT behavioralhealthassessment_prescribedmedication_fk FOREIGN KEY (BehavioralHealthAssessmentID) REFERENCES BehavioralHealthAssessment (BehavioralHealthAssessmentID);

ALTER TABLE BookingCharge ADD CONSTRAINT chargetype_charge_fk FOREIGN KEY (ChargeTypeID) REFERENCES ChargeType (ChargeTypeID);

ALTER TABLE CustodyStatusChangeCharge ADD CONSTRAINT chargetype_custodystatuschangecharge_fk FOREIGN KEY (ChargeTypeID) REFERENCES ChargeType (ChargeTypeID);

ALTER TABLE Booking ADD CONSTRAINT status_booking_fk FOREIGN KEY (CaseStatusID) REFERENCES CaseStatus (CaseStatusID);

ALTER TABLE CustodyStatusChange ADD CONSTRAINT casestatus_custody_status_change_fk FOREIGN KEY (CaseStatusID) REFERENCES CaseStatus (CaseStatusID);

ALTER TABLE Booking ADD CONSTRAINT jurisdiction_booking_fk FOREIGN KEY (JurisdictionID) REFERENCES Jurisdiction (JurisdictionID);

ALTER TABLE CustodyStatusChange ADD CONSTRAINT jurisdiction_custody_status_change_fk FOREIGN KEY (JurisdictionID) REFERENCES Jurisdiction (JurisdictionID);

ALTER TABLE CustodyStatusChangeArrest ADD CONSTRAINT custodystatuschange_custodystatuschangearrest_fk FOREIGN KEY (CustodyStatusChangeID) REFERENCES CustodyStatusChange (CustodyStatusChangeID);

ALTER TABLE CustodyStatusChangeCharge ADD CONSTRAINT custodystatuschangearrest_custodystatuschangecharge_fk FOREIGN KEY (CustodyStatusChangeArrestID) REFERENCES CustodyStatusChangeArrest (CustodyStatusChangeArrestID);

ALTER TABLE BookingCharge ADD CONSTRAINT agency_bookingcharge_fk FOREIGN KEY (AgencyID) REFERENCES Agency (AgencyID);

ALTER TABLE CustodyStatusChangeCharge ADD CONSTRAINT agency_custodystatuschangecharge_fk FOREIGN KEY (AgencyID) REFERENCES Agency (AgencyID);

ALTER TABLE BookingArrest ADD CONSTRAINT booking_bookingarrest_fk FOREIGN KEY (BookingID) REFERENCES Booking (BookingID);

ALTER TABLE BookingCharge ADD CONSTRAINT bookingarrest_bookingcharge_fk FOREIGN KEY (BookingArrestID) REFERENCES BookingArrest (BookingArrestID);