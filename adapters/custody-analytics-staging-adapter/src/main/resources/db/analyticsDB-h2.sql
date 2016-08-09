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


CREATE TABLE Location (LocationID INT AUTO_INCREMENT NOT NULL, AddressSecondaryUnit VARCHAR(150), StreetNumber VARCHAR(50), StreetName VARCHAR(150), City VARCHAR(100), State VARCHAR(10), PostalCode VARCHAR(10), ArrestLocationLatitude NUMBER(14, 10), ArrestLocationLongitude NUMBER(14, 10));

ALTER TABLE Location ADD CONSTRAINT locationid PRIMARY KEY (LocationID);

CREATE SEQUENCE Location_LocationID_seq_2;

CREATE TABLE Medication (MedicationID INT AUTO_INCREMENT NOT NULL, GeneralProductID VARCHAR(50), ItemName VARCHAR(100));

ALTER TABLE Medication ADD CONSTRAINT medicationid PRIMARY KEY (MedicationID);

CREATE SEQUENCE Medication_MedicationID_seq;

CREATE TABLE LanguageType (LanguageTypeID INT AUTO_INCREMENT NOT NULL, LanguageTypeDescription VARCHAR(20) NOT NULL);

ALTER TABLE LanguageType ADD CONSTRAINT languagetypeid PRIMARY KEY (LanguageTypeID);

CREATE SEQUENCE LanguageType_LanguageTypeID_seq;

CREATE TABLE EducationLevelType (EducationLevelTypeID INT AUTO_INCREMENT NOT NULL, EducationLevelTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE EducationLevelType ADD CONSTRAINT educationleveltypeid PRIMARY KEY (EducationLevelTypeID);

CREATE SEQUENCE EducationLevelType_EducationLevelTypeID_seq;

CREATE TABLE OccupationType (OccupationTypeID INT AUTO_INCREMENT NOT NULL, OccupationTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE OccupationType ADD CONSTRAINT occupationtypeid PRIMARY KEY (OccupationTypeID);

CREATE SEQUENCE OccupationType_OccupationTypeID_seq;

CREATE TABLE IncomeLevelType (IncomeLevelTypeID INT AUTO_INCREMENT NOT NULL, IncomeLevelTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE IncomeLevelType ADD CONSTRAINT incomeleveltypeid PRIMARY KEY (IncomeLevelTypeID);

CREATE SEQUENCE IncomeLevelType_IncomeLevelTypeID_seq;

CREATE TABLE HousingStatusType (HousingStatusTypeID INT AUTO_INCREMENT NOT NULL, HousingStatusTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE HousingStatusType ADD CONSTRAINT housingstatustypeid PRIMARY KEY (HousingStatusTypeID);

CREATE SEQUENCE HousingStatusType_HousingStatusTypeID_seq;

CREATE TABLE Facility (FacilityID INT AUTO_INCREMENT NOT NULL, FacilityDescription VARCHAR(100) NOT NULL, Capacity INT NOT NULL);

ALTER TABLE Facility ADD CONSTRAINT facilityid PRIMARY KEY (FacilityID);

CREATE SEQUENCE Facility_FacilityID_seq_1;

CREATE TABLE BondType (BondTypeID INT AUTO_INCREMENT NOT NULL, BondTypeDescription VARCHAR(100) NOT NULL);

ALTER TABLE BondType ADD CONSTRAINT bondtypeid PRIMARY KEY (BondTypeID);

CREATE SEQUENCE BondType_BondTypeID_seq_1_1_1_1;

CREATE TABLE BedType (BedTypeID INT AUTO_INCREMENT NOT NULL, BedTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE BedType ADD CONSTRAINT bedtypeid PRIMARY KEY (BedTypeID);

CREATE SEQUENCE BedType_BedTypeID_seq_1;

CREATE TABLE BehavioralHealthType (BehavioralHealthTypeID INT AUTO_INCREMENT NOT NULL, BehavioralHealthDescription VARCHAR(50) NOT NULL);

ALTER TABLE BehavioralHealthType ADD CONSTRAINT behavioralhealthtypeid PRIMARY KEY (BehavioralHealthTypeID);

CREATE SEQUENCE BehavioralHealthType_BehavioralHealthTypeID_seq;

CREATE TABLE PersonRace (PersonRaceID INT AUTO_INCREMENT NOT NULL, PersonRaceCode VARCHAR(10) NOT NULL, PersonRaceDescription VARCHAR(50) NOT NULL);

ALTER TABLE PersonRace ADD CONSTRAINT personraceid PRIMARY KEY (PersonRaceID);

CREATE SEQUENCE PersonRace_PersonRaceID_seq;

CREATE TABLE PersonSex (PersonSexID INT AUTO_INCREMENT NOT NULL, PersonSexCode VARCHAR(1) NOT NULL, PersonSexDescription VARCHAR(7) NOT NULL);

ALTER TABLE PersonSex ADD CONSTRAINT personsexid PRIMARY KEY (PersonSexID);

CREATE SEQUENCE PersonSex_PersonSexID_seq;

CREATE TABLE Person (PersonID INT AUTO_INCREMENT NOT NULL, PersonUniqueIdentifier VARCHAR(36) NOT NULL, BookingSubjectNumber VARCHAR(36), PersonEyeColor VARCHAR(20), PersonHairColor VARCHAR(20), PersonHeight VARCHAR(10), PersonHeightMeasureUnit VARCHAR(10), PersonWeight VARCHAR(10), PersonWeightMeasureUnit VARCHAR(10), RegisteredSexOffender BOOLEAN, PersonBirthDate date, LanguageTypeID INT, CreationDate TIMESTAMP DEFAULT NOW() NOT NULL, PersonSexID INT, PersonRaceID INT);

ALTER TABLE Person ADD CONSTRAINT personid PRIMARY KEY (PersonID);

CREATE SEQUENCE Person_PersonID_seq_1;

CREATE TABLE BookingSubject (BookingSubjectID INT AUTO_INCREMENT NOT NULL, RecidivistIndicator SMALLINT DEFAULT 0 NOT NULL, PersonID INT NOT NULL, PersonAge INT, EducationLevelTypeID INT, OccupationTypeID INT, IncomeLevelTypeID INT, HousingStatusTypeID INT, MilitaryServiceStatusCode VARCHAR(20));

ALTER TABLE BookingSubject ADD CONSTRAINT bookingsubjectid PRIMARY KEY (BookingSubjectID);

CREATE SEQUENCE BookingSubject_BookingSubjectID_seq_1;

CREATE TABLE BehavioralHealthAssessment (BehavioralHealthAssessmentID INT AUTO_INCREMENT NOT NULL, PersonID INT NOT NULL, SeriousMentalIllness BOOLEAN, HighRiskNeeds BOOLEAN, CareEpisodeStartDate date, CareEpisodeEndDate date, SubstanceAbuse BOOLEAN, GeneralMentalHealthCondition BOOLEAN, MedicaidIndicator BOOLEAN, RegionalAuthorityAssignmentText VARCHAR(20));

ALTER TABLE BehavioralHealthAssessment ADD CONSTRAINT behavioralhealthassessmentid PRIMARY KEY (BehavioralHealthAssessmentID);

CREATE SEQUENCE BehavioralHealthAssessment_BehavioralHealthAssessmentID_seq;

CREATE TABLE PrescribedMedication (PrescribedMedicationID INT AUTO_INCREMENT NOT NULL, BehavioralHealthAssessmentID INT NOT NULL, MedicationID INT NOT NULL, MedicationDispensingDate date, MedicationDoseMeasure VARCHAR(10));

ALTER TABLE PrescribedMedication ADD CONSTRAINT prescribedmedicationid PRIMARY KEY (PrescribedMedicationID);

CREATE SEQUENCE PrescribedMedication_PrescribedMedicationID_seq;

CREATE TABLE Treatment (TreatmentID INT AUTO_INCREMENT NOT NULL, BehavioralHealthAssessmentID INT NOT NULL, StartDate date, EndDate date, TreatmentCourtOrdered BOOLEAN, TreatmentText VARCHAR(100), TreatmentProvider VARCHAR(100), TreatmentActive BOOLEAN);

ALTER TABLE Treatment ADD CONSTRAINT treatmentid PRIMARY KEY (TreatmentID);

CREATE SEQUENCE Treatment_TreatmentID_seq;

CREATE TABLE BehavioralHealthEvaluation (BehavioralHealthEvaluationID INT AUTO_INCREMENT NOT NULL, BehavioralHealthTypeID INT NOT NULL, BehavioralHealthAssessmentID INT NOT NULL);

ALTER TABLE BehavioralHealthEvaluation ADD CONSTRAINT behavioralhealthevaluationid PRIMARY KEY (BehavioralHealthEvaluationID);

CREATE SEQUENCE BehavioralHealthEvaluation_BehavioralHealthEvaluationID_seq;

CREATE TABLE ChargeType (ChargeTypeID INT AUTO_INCREMENT NOT NULL, ChargeTypeDescription VARCHAR(100) NOT NULL);

ALTER TABLE ChargeType ADD CONSTRAINT chargetypeid PRIMARY KEY (ChargeTypeID);

CREATE SEQUENCE ChargeType_ChargeTypeID_seq;

CREATE TABLE CaseStatusType (CaseStatusTypeID INT AUTO_INCREMENT NOT NULL, CaseStatusTypeDescription VARCHAR(100) NOT NULL);

ALTER TABLE CaseStatusType ADD CONSTRAINT casestatustypeid PRIMARY KEY (CaseStatusTypeID);

CREATE SEQUENCE CaseStatusType_CaseStatusTypeID_seq;

CREATE TABLE JurisdictionType (JurisdictionTypeID INT AUTO_INCREMENT NOT NULL, JurisdictionTypeDescription VARCHAR(100) NOT NULL);

ALTER TABLE JurisdictionType ADD CONSTRAINT jurisdictiontypeid PRIMARY KEY (JurisdictionTypeID);

CREATE SEQUENCE JurisdictionType_JurisdictionTypeID_seq;

CREATE TABLE AgencyType (AgencyTypeID INT AUTO_INCREMENT NOT NULL, AgencyTypeDescription VARCHAR(50) NOT NULL);

ALTER TABLE AgencyType ADD CONSTRAINT agencytypeid PRIMARY KEY (AgencyTypeID);

CREATE SEQUENCE AgencyType_AgencyTypeID_seq;

CREATE TABLE Booking (BookingID INT AUTO_INCREMENT NOT NULL, JurisdictionTypeID INT NOT NULL, BookingReportDate TIMESTAMP NOT NULL, BookingReportID VARCHAR(30) NOT NULL, CaseStatusTypeID INT NOT NULL, BookingDate TIMESTAMP NOT NULL, CommitDate date NOT NULL, ScheduledReleaseDate date, FacilityID INT NOT NULL, BedTypeID INT NOT NULL, BookingNumber VARCHAR(50) NOT NULL, BookingSubjectID INT NOT NULL);

ALTER TABLE Booking ADD CONSTRAINT bookingid PRIMARY KEY (BookingID);

CREATE SEQUENCE Booking_BookingID_seq;

CREATE TABLE CustodyRelease (CustodyReleaseID INT AUTO_INCREMENT NOT NULL, BookingID INT NOT NULL, ReleaseDate TIMESTAMP NOT NULL, ReportDate TIMESTAMP NOT NULL);

ALTER TABLE CustodyRelease ADD CONSTRAINT custodyreleaseid PRIMARY KEY (CustodyReleaseID);

CREATE SEQUENCE CustodyRelease_CustodyReleaseID_seq;

CREATE TABLE CustodyStatusChange (CustodyStatusChangeID INT AUTO_INCREMENT NOT NULL, BookingID INT NOT NULL, ReportID VARCHAR(30) NOT NULL, BookingDate TIMESTAMP NOT NULL, CommitDate date NOT NULL, ScheduledReleaseDate date, BookingSubjectID INT NOT NULL, BedTypeID INT, CaseStatusTypeID INT NOT NULL, JurisdictionTypeID INT NOT NULL, FacilityID INT NOT NULL, ReportDate TIMESTAMP NOT NULL);

ALTER TABLE CustodyStatusChange ADD CONSTRAINT custodystatuschangeid PRIMARY KEY (CustodyStatusChangeID);

CREATE SEQUENCE CustodyStatusChange_CustodyStatusChangeID_seq;

CREATE TABLE CustodyStatusChangeArrest (CustodyStatusChangeArrestID INT AUTO_INCREMENT NOT NULL, CustodyStatusChangeID INT NOT NULL, LocationID INT NOT NULL);

ALTER TABLE CustodyStatusChangeArrest ADD CONSTRAINT custodystatuschangearrestid PRIMARY KEY (CustodyStatusChangeArrestID);

CREATE SEQUENCE CustodyStatusChangeArrest_CustodyStatusChangeArrestID_seq_1;

CREATE TABLE CustodyStatusChangeCharge (CustodyStatusChangeChargeID INT AUTO_INCREMENT NOT NULL, CustodyStatusChangeArrestID INT NOT NULL, ChargeTypeID INT NOT NULL, NextCourtDate date, NextCourtName VARCHAR(50), BondTypeID INT NOT NULL, AgencyTypeID INT NOT NULL, BondAmount NUMBER(10, 2));

ALTER TABLE CustodyStatusChangeCharge ADD CONSTRAINT custodystatuschangechargeid PRIMARY KEY (CustodyStatusChangeChargeID);

CREATE SEQUENCE CustodyStatusChangeCharge_CustodyStatusChangeChargeID_seq;

CREATE TABLE BookingArrest (BookingArrestID INT AUTO_INCREMENT NOT NULL, BookingID INT NOT NULL, LocationID INT NOT NULL);

ALTER TABLE BookingArrest ADD CONSTRAINT bookingarrestid PRIMARY KEY (BookingArrestID);

CREATE SEQUENCE BookingArrest_BookingArrestID_seq_1;

CREATE TABLE BookingCharge (BookingChargeID INT AUTO_INCREMENT NOT NULL, BookingArrestID INT NOT NULL, ChargeTypeID INT NOT NULL, NextCourtName VARCHAR(50), NextCourtDate date, AgencyTypeID INT NOT NULL, BondAmount NUMBER(10, 2), BondTypeID INT NOT NULL);

ALTER TABLE BookingCharge ADD CONSTRAINT bookingchargeid PRIMARY KEY (BookingChargeID);

CREATE SEQUENCE BookingCharge_BookingChargeID_seq;

ALTER TABLE BookingArrest ADD CONSTRAINT location_bookingarrest_fk FOREIGN KEY (LocationID) REFERENCES Location (LocationID);

ALTER TABLE CustodyStatusChangeArrest ADD CONSTRAINT location_custodystatuschangearrest_fk FOREIGN KEY (LocationID) REFERENCES Location (LocationID);

ALTER TABLE PrescribedMedication ADD CONSTRAINT medication_prescribedmedication_fk FOREIGN KEY (MedicationID) REFERENCES Medication (MedicationID);

ALTER TABLE Person ADD CONSTRAINT language_person_fk FOREIGN KEY (LanguageTypeID) REFERENCES LanguageType (LanguageTypeID);

ALTER TABLE BookingSubject ADD CONSTRAINT education_bookingsubject_fk FOREIGN KEY (EducationLevelTypeID) REFERENCES EducationLevelType (EducationLevelTypeID);

ALTER TABLE BookingSubject ADD CONSTRAINT occupation_bookingsubject_fk FOREIGN KEY (OccupationTypeID) REFERENCES OccupationType (OccupationTypeID);

ALTER TABLE BookingSubject ADD CONSTRAINT incomelevel_bookingsubject_fk FOREIGN KEY (IncomeLevelTypeID) REFERENCES IncomeLevelType (IncomeLevelTypeID);

ALTER TABLE BookingSubject ADD CONSTRAINT housingstatus_bookingsubject_fk FOREIGN KEY (HousingStatusTypeID) REFERENCES HousingStatusType (HousingStatusTypeID);

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

ALTER TABLE Booking ADD CONSTRAINT status_booking_fk FOREIGN KEY (CaseStatusTypeID) REFERENCES CaseStatusType (CaseStatusTypeID);

ALTER TABLE CustodyStatusChange ADD CONSTRAINT casestatus_custody_status_change_fk FOREIGN KEY (CaseStatusTypeID) REFERENCES CaseStatusType (CaseStatusTypeID);

ALTER TABLE Booking ADD CONSTRAINT jurisdiction_booking_fk FOREIGN KEY (JurisdictionTypeID) REFERENCES JurisdictionType (JurisdictionTypeID);

ALTER TABLE CustodyStatusChange ADD CONSTRAINT jurisdiction_custody_status_change_fk FOREIGN KEY (JurisdictionTypeID) REFERENCES JurisdictionType (JurisdictionTypeID);

ALTER TABLE BookingCharge ADD CONSTRAINT agency_bookingcharge_fk FOREIGN KEY (AgencyTypeID) REFERENCES AgencyType (AgencyTypeID);

ALTER TABLE CustodyStatusChangeCharge ADD CONSTRAINT agency_custodystatuschangecharge_fk FOREIGN KEY (AgencyTypeID) REFERENCES AgencyType (AgencyTypeID);

ALTER TABLE BookingArrest ADD CONSTRAINT booking_bookingarrest_fk FOREIGN KEY (BookingID) REFERENCES Booking (BookingID);

ALTER TABLE CustodyStatusChange ADD CONSTRAINT booking_custodystatuschange_fk FOREIGN KEY (BookingID) REFERENCES Booking (BookingID);

ALTER TABLE CustodyRelease ADD CONSTRAINT booking_custodyrelease_fk FOREIGN KEY (BookingID) REFERENCES Booking (BookingID);

ALTER TABLE CustodyStatusChangeArrest ADD CONSTRAINT custodystatuschange_custodystatuschangearrest_fk FOREIGN KEY (CustodyStatusChangeID) REFERENCES CustodyStatusChange (CustodyStatusChangeID);

ALTER TABLE CustodyStatusChangeCharge ADD CONSTRAINT custodystatuschangearrest_custodystatuschangecharge_fk FOREIGN KEY (CustodyStatusChangeArrestID) REFERENCES CustodyStatusChangeArrest (CustodyStatusChangeArrestID);

ALTER TABLE BookingCharge ADD CONSTRAINT bookingarrest_bookingcharge_fk FOREIGN KEY (BookingArrestID) REFERENCES BookingArrest (BookingArrestID);
