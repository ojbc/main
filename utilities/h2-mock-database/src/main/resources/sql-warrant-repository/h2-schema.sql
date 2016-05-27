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
drop schema if exists warrant_repository;
CREATE schema warrant_repository;
use warrant_repository;

CREATE TABLE Warrant (WarrantID INT AUTO_INCREMENT NOT NULL, PersonEntryNumber VARCHAR(8), DateOfWarrant date, DateOfExpiration date, BroadcastArea VARCHAR(50), WarrantEntryType VARCHAR(2), CourtAgencyORI VARCHAR(9), LawEnforcementORI VARCHAR(9), CourtDocketNumber VARCHAR(12), OCAComplaintNumber VARCHAR(20), Operator VARCHAR(15), PACCCode VARCHAR(16), OriginalOffenseCode VARCHAR(4), OffenseCode VARCHAR(4), GeneralOffenseCharacter VARCHAR(1), CriminalTrackingNumber VARCHAR(12), Extradite BOOLEAN, ExtraditionLimits VARCHAR(1) DEFAULT '1', PickupLimits VARCHAR(1), BondAmount VARCHAR(8), WarrantStatus VARCHAR(20), WarrantStatusTimestamp TIMESTAMP, WarrantModRequestSent BOOLEAN DEFAULT FALSE, WarrantModResponseReceived BOOLEAN DEFAULT FALSE);

ALTER TABLE Warrant ADD CONSTRAINT WARRANT_PK PRIMARY KEY (WarrantID);

CREATE SEQUENCE Warrant_WarrantID_seq;

CREATE TABLE WarrantRemarks (WarrantRemarksID INT AUTO_INCREMENT NOT NULL, WarrantID INT NOT NULL, WarrantRemarkText VARCHAR(250));

ALTER TABLE WarrantRemarks ADD CONSTRAINT WARRANTREMARKS_PK PRIMARY KEY (WarrantRemarksID);

CREATE SEQUENCE WarrantRemarks_WarrantRemarksID_seq;

CREATE TABLE Person (PersonID INT AUTO_INCREMENT NOT NULL, FirstName VARCHAR(30), MiddleName VARCHAR(30), LastName VARCHAR(28), NameSuffix VARCHAR(3), FullPersonName VARCHAR(150), AddressFullText VARCHAR(24), AddressCity VARCHAR(15), AddressState VARCHAR(2), AddressZip VARCHAR(10), SocialSecurityNumberBase VARCHAR(9), DateOfBirth date, PlaceOfBirth VARCHAR(2), PersonAge VARCHAR(2), OperatorLicenseNumberBase VARCHAR(20), OperatorLicenseStateBase VARCHAR(2), PersonEthnicityDescription VARCHAR(1), PersonEyeColorDescription VARCHAR(10), PersonHairColorDescription VARCHAR(10), PersonSexDescription VARCHAR(1), PersonRaceDescription VARCHAR(1), PersonSkinToneDescription VARCHAR(3), PersonHeight VARCHAR(3), PersonWeight VARCHAR(3), PersonScarsMarksTattosBase VARCHAR(3), PersonCitizenshipCountry VARCHAR(2), USCitizenshipIndicator BOOLEAN, PersonImmigrationAlienQueryInd BOOLEAN, PersonStateIdentification VARCHAR(8), FBIIdentificationNumber VARCHAR(9), MiscellaneousIDBase VARCHAR(15), PrisonRecordNumber VARCHAR(11), PersonCautionDescription VARCHAR(2));

ALTER TABLE Person ADD CONSTRAINT PERSON_PK PRIMARY KEY (PersonID);

CREATE SEQUENCE Person_PersonID_seq;

CREATE TABLE Vehicle (VehicleID INT AUTO_INCREMENT NOT NULL, PersonID INT NOT NULL, LicensePlateType VARCHAR(2), VehicleLicensePlateExpirationD VARCHAR(10), VehicleNonExpiringIndicator BOOLEAN DEFAULT FALSE, VehicleLicensePlateNumber VARCHAR(10), VehicleLicenseStateCode VARCHAR(2), VehicleIdentificationNumber VARCHAR(20), VehicleYear VARCHAR(4), VehicleModel VARCHAR(3), VehicleMake VARCHAR(4), VehiclePrimaryColor VARCHAR(23), VehicleSecondaryColor VARCHAR(23), VehicleStyle VARCHAR(2));

ALTER TABLE Vehicle ADD CONSTRAINT VEHICLE_PK PRIMARY KEY (VehicleID);

CREATE SEQUENCE Vehicle_VehicleID_seq;

CREATE TABLE PersonIDAdditional (PersonIDAdditionalID INT AUTO_INCREMENT NOT NULL, PersonID INT NOT NULL, PersonAdditionalID VARCHAR(15) NOT NULL);

ALTER TABLE PersonIDAdditional ADD CONSTRAINT PERSONIDADDITIONAL_PK PRIMARY KEY (PersonIDAdditionalID);

CREATE SEQUENCE PersonIDAdditional_PersonIDAdditionalID_seq;

CREATE TABLE PersonSMTAdditional (PersonSMTSupplementalID INT AUTO_INCREMENT NOT NULL, PersonID INT NOT NULL, PersonScarsMarksTattoos VARCHAR(3));

ALTER TABLE PersonSMTAdditional ADD CONSTRAINT PERSONSMTADDITIONAL_PK PRIMARY KEY (PersonSMTSupplementalID);

CREATE SEQUENCE PersonSMTSupplemental_PersonID_seq;

CREATE TABLE PersonOLNAdditional (PersonOLNID INT AUTO_INCREMENT NOT NULL, PersonID INT NOT NULL, OperatorLicenseNumber VARCHAR(20), OperatorLicenseState VARCHAR(2));

ALTER TABLE PersonOLNAdditional ADD CONSTRAINT PERSONOLNADDITIONAL_PK PRIMARY KEY (PersonOLNID);

CREATE SEQUENCE PersonOLNSupplemental_PersonOLN_seq;

CREATE TABLE PersonSSNAdditional (PersonSSNID INT AUTO_INCREMENT NOT NULL, PersonID INT NOT NULL, SocialSecurityNumber VARCHAR(9));

ALTER TABLE PersonSSNAdditional ADD CONSTRAINT PERSONID PRIMARY KEY (PersonSSNID);

CREATE SEQUENCE PersonSSN_PersonID_seq;

CREATE TABLE PersonAlternateName (PersonAlternateNameID INT AUTO_INCREMENT NOT NULL, PersonID INT NOT NULL, FirstName VARCHAR(30), FullPersonName VARCHAR(150), LastName VARCHAR(28), MiddleName VARCHAR(30), NameSuffix VARCHAR(3));

ALTER TABLE PersonAlternateName ADD CONSTRAINT PERSONALTERNATENAME_PK PRIMARY KEY (PersonAlternateNameID);

CREATE SEQUENCE PersonAlternateName_PersonAlternateNameID_seq;

CREATE TABLE ChargeRef (ChargeRefID INT AUTO_INCREMENT NOT NULL, PersonID INT NOT NULL, ReportingAgencyORI VARCHAR(9) NOT NULL, CaseAgencyComplaintNumber VARCHAR(20) NOT NULL, TransactionControlNumber VARCHAR(11), ReportingAgencyName VARCHAR(50) NOT NULL);

ALTER TABLE ChargeRef ADD CONSTRAINT CHARGEREF_PK PRIMARY KEY (ChargeRefID);

CREATE SEQUENCE ChargeRef_ChargeRefID_seq;

CREATE TABLE WarrantChargeRef (WarrantChargeRefID INT AUTO_INCREMENT NOT NULL, WarrantID INT NOT NULL, ChargeRefID INT NOT NULL);

ALTER TABLE WarrantChargeRef ADD CONSTRAINT WARRANTCHARGEREF_PK PRIMARY KEY (WarrantChargeRefID);

CREATE SEQUENCE WarrantChargeRef_WarrantChargeRefID_seq;

CREATE TABLE Officer (OfficerID INT AUTO_INCREMENT NOT NULL, ChargeRefID INT NOT NULL, OfficerName VARCHAR(100) NOT NULL, OfficerBadgeNumber VARCHAR(20));

ALTER TABLE Officer ADD CONSTRAINT OFFICER_PK PRIMARY KEY (OfficerID);

CREATE SEQUENCE Officer_OfficerID_seq;

CREATE TABLE Charge (ChargeID INT AUTO_INCREMENT NOT NULL, ChargeRefID INT NOT NULL, ChargeSeverityLevel VARCHAR(50));

ALTER TABLE Charge ADD CONSTRAINT CHARGE_PK PRIMARY KEY (ChargeID);

CREATE SEQUENCE Charge_ChargeID_seq;

ALTER TABLE WarrantRemarks ADD CONSTRAINT Warrant_WarrantRemarks_fk FOREIGN KEY (WarrantID) REFERENCES Warrant (WarrantID);

ALTER TABLE WarrantChargeRef ADD CONSTRAINT Warrant_WarrantChargeRef_fk FOREIGN KEY (WarrantID) REFERENCES Warrant (WarrantID);

ALTER TABLE ChargeRef ADD CONSTRAINT Person_Arrest_fk FOREIGN KEY (PersonID) REFERENCES Person (PersonID);

ALTER TABLE PersonAlternateName ADD CONSTRAINT Person_PersonAlternateName_fk FOREIGN KEY (PersonID) REFERENCES Person (PersonID);

ALTER TABLE PersonSSNAdditional ADD CONSTRAINT Person_PersonSSNAdditional_fk FOREIGN KEY (PersonID) REFERENCES Person (PersonID);

ALTER TABLE PersonOLNAdditional ADD CONSTRAINT Person_PersonOLNAdditional_fk FOREIGN KEY (PersonID) REFERENCES Person (PersonID);

ALTER TABLE PersonSMTAdditional ADD CONSTRAINT Person_PersonSMTAdditional_fk FOREIGN KEY (PersonID) REFERENCES Person (PersonID);

ALTER TABLE PersonIDAdditional ADD CONSTRAINT Person_PersonIDAdditional_fk FOREIGN KEY (PersonID) REFERENCES Person (PersonID);

ALTER TABLE Vehicle ADD CONSTRAINT Person_Vehicle_fk FOREIGN KEY (PersonID) REFERENCES Person (PersonID);

ALTER TABLE Charge ADD CONSTRAINT Arrest_Charge_fk FOREIGN KEY (ChargeRefID) REFERENCES ChargeRef (ChargeRefID);

ALTER TABLE Officer ADD CONSTRAINT Arrest_Officer_fk FOREIGN KEY (ChargeRefID) REFERENCES ChargeRef (ChargeRefID);

ALTER TABLE WarrantChargeRef ADD CONSTRAINT ChargeRef_WarrantChargeRef_fk FOREIGN KEY (ChargeRefID) REFERENCES ChargeRef (ChargeRefID);