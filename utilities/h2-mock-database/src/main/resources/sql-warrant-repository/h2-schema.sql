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
 * Copyright 2012-2017 Open Justice Broker Consortium
 */
drop schema if exists warrant_repository;
CREATE schema warrant_repository;
use warrant_repository;


CREATE TABLE WarrantStatusType (
                WarrantStatusTypeID IDENTITY NOT NULL,
                WarrantStatusType VARCHAR(20) NOT NULL,
                WarrantStatusTypeDescription VARCHAR(50) NOT NULL,
                CONSTRAINT WarrantStatusType_pk PRIMARY KEY (WarrantStatusTypeID)
);


CREATE TABLE Person (
                PersonID IDENTITY NOT NULL,
                FirstName VARCHAR(30),
                MiddleName VARCHAR(30),
                LastName VARCHAR(28),
                NameSuffix VARCHAR(3),
                FullPersonName VARCHAR(150),
                AddressStreetFullText VARCHAR(50),
                AddressStreetName VARCHAR(15),
                AddressStreetNumber VARCHAR(15),
                AddressCity VARCHAR(15),
                AddressCounty VARCHAR(15),
                AddressState VARCHAR(2),
                AddressZip VARCHAR(10),
                SocialSecurityNumberBase VARCHAR(9),
                DateOfBirth DATE,
                PlaceOfBirth VARCHAR(2),
                PersonAge VARCHAR(2),
                OperatorLicenseNumberBase VARCHAR(20),
                OperatorLicenseStateBase VARCHAR(2),
                PersonEthnicityDescription VARCHAR(1),
                PersonEyeColorDescription VARCHAR(10),
                PersonHairColorDescription VARCHAR(10),
                PersonSexDescription VARCHAR(1),
                PersonRaceDescription VARCHAR(1),
                PersonSkinToneDescription VARCHAR(3),
                PersonHeight VARCHAR(3),
                PersonWeight VARCHAR(3),
                PersonScarsMarksTattosBase VARCHAR(3),
                PersonCitizenshipCountry VARCHAR(2),
                USCitizenshipIndicator BOOLEAN,
                PersonImmigrationAlienQueryInd BOOLEAN,
                PersonStateIdentification VARCHAR(8),
                FBIIdentificationNumber VARCHAR(9),
                MiscellaneousIDBase VARCHAR(15),
                PrisonRecordNumber VARCHAR(11),
                PersonCautionDescription VARCHAR(2),
                LastUpdateTimestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                CONSTRAINT Person_pk PRIMARY KEY (PersonID)
);


CREATE TABLE Vehicle (
                VehicleID IDENTITY NOT NULL,
                PersonID INTEGER NOT NULL,
                LicensePlateType VARCHAR(2),
                VehicleLicensePlateExpirationD VARCHAR(10),
                VehicleNonExpiringIndicator BOOLEAN DEFAULT false,
                VehicleLicensePlateNumber VARCHAR(10),
                VehicleLicenseStateCode VARCHAR(2),
                VehicleIdentificationNumber VARCHAR(20),
                VehicleYear VARCHAR(4),
                VehicleModel VARCHAR(3),
                VehicleMake VARCHAR(4),
                VehiclePrimaryColor VARCHAR(23),
                VehicleSecondaryColor VARCHAR(23),
                VehicleStyle VARCHAR(2),
                CONSTRAINT Vehicle_pk PRIMARY KEY (VehicleID)
);
COMMENT ON COLUMN Vehicle.LicensePlateType IS 'Maximum of 2 characters. NCIC Vehicular Codes.';
COMMENT ON COLUMN Vehicle.VehicleLicensePlateExpirationD IS 'Two character MM.';
COMMENT ON COLUMN Vehicle.VehicleYear IS 'CCYY';


CREATE TABLE PersonIDAdditional (
                PersonIDAdditionalID IDENTITY NOT NULL,
                PersonID INTEGER NOT NULL,
                PersonAdditionalID VARCHAR(15) NOT NULL,
                CONSTRAINT PersonIDAdditional_pk PRIMARY KEY (PersonIDAdditionalID)
);


CREATE TABLE PersonSMTAdditional (
                PersonSMTSupplementalID IDENTITY NOT NULL,
                PersonID INTEGER NOT NULL,
                PersonScarsMarksTattoos VARCHAR(3),
                CONSTRAINT PersonSMTAdditional_pk PRIMARY KEY (PersonSMTSupplementalID)
);
COMMENT ON COLUMN PersonSMTAdditional.PersonScarsMarksTattoos IS 'Maximum of 3 characters.';


CREATE TABLE PersonOLNAdditional (
                PersonOLNID IDENTITY NOT NULL,
                PersonID INTEGER NOT NULL,
                OperatorLicenseNumber VARCHAR(20),
                OperatorLicenseState VARCHAR(2),
                CONSTRAINT PersonOLNAdditional_pk PRIMARY KEY (PersonOLNID)
);


CREATE TABLE PersonSSNAdditional (
                PersonSSNID IDENTITY NOT NULL,
                PersonID INTEGER NOT NULL,
                SocialSecurityNumber VARCHAR(9),
                CONSTRAINT PersonID PRIMARY KEY (PersonSSNID)
);
COMMENT ON TABLE PersonSSNAdditional IS 'Nine additional Social Security Numbers are allowed for a person.';


CREATE TABLE PersonAlternateName (
                PersonAlternateNameID IDENTITY NOT NULL,
                PersonID INTEGER NOT NULL,
                FirstName VARCHAR(30),
                FullPersonName VARCHAR(150),
                LastName VARCHAR(28),
                MiddleName VARCHAR(30),
                NameSuffix VARCHAR(3),
                CONSTRAINT PersonAlternateName_pk PRIMARY KEY (PersonAlternateNameID)
);
COMMENT ON TABLE PersonAlternateName IS 'Need to add Person Entry Number (SYSIDNO)';


CREATE TABLE ChargeRef (
                ChargeRefID IDENTITY NOT NULL,
                PersonID INTEGER NOT NULL,
                ReportingAgencyORI VARCHAR(9) NOT NULL,
                CaseAgencyComplaintNumber VARCHAR(20),
                TransactionControlNumber VARCHAR(11),
                ReportingAgencyName VARCHAR(50) NOT NULL,
                LastUpdateTimestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                CONSTRAINT ChargeRef_pk PRIMARY KEY (ChargeRefID)
);


CREATE TABLE Warrant (
                WarrantID IDENTITY NOT NULL,
                StateWarrantRepositoryID VARCHAR(8),
                DateOfWarrant DATE,
                DateOfExpiration DATE,
                BroadcastArea VARCHAR(50),
                WarrantEntryType VARCHAR(2),
                CourtAgencyORI VARCHAR(9),
                LawEnforcementORI VARCHAR(9),
                CourtDocketNumber VARCHAR(12),
                OCAComplaintNumber VARCHAR(20),
                Operator VARCHAR(15),
                PACCCode VARCHAR(16),
                OriginalOffenseCode VARCHAR(4),
                OffenseCode VARCHAR(4),
                GeneralOffenseCharacter VARCHAR(1),
                CriminalTrackingNumber VARCHAR(12),
                Extradite BOOLEAN,
                warrantModRequestSent BOOLEAN,
                WarrantModResponseReceived BOOLEAN,
                ExtraditionLimits VARCHAR(1) DEFAULT 1,
                PickupLimits VARCHAR(1),
                BondAmount VARCHAR(8),
                LastUpdateTimestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
                ChargeRefID INTEGER NOT NULL,
                CONSTRAINT Warrant_pk PRIMARY KEY (WarrantID)
);
COMMENT ON COLUMN Warrant.DateOfExpiration IS 'Date the warrant expires, CCYY-MM-DD. Example: 2016-07-01';
COMMENT ON COLUMN Warrant.BroadcastArea IS 'broadcast area.';
COMMENT ON COLUMN Warrant.Operator IS 'e. badge, employee number, or name.';
COMMENT ON COLUMN Warrant.PACCCode IS 'Prosecuting Attorney''s Coordinating Council Code.';
COMMENT ON COLUMN Warrant.OriginalOffenseCode IS 'Charge code or PACC Code.';
COMMENT ON COLUMN Warrant.GeneralOffenseCharacter IS 'ommit';
COMMENT ON COLUMN Warrant.CriminalTrackingNumber IS 'when fingerprinting at time of arrest.';
COMMENT ON COLUMN Warrant.ExtraditionLimits IS 'cluded.';


CREATE TABLE WarrantStatus (
                WarrantStatusID IDENTITY NOT NULL,
                Operator VARCHAR(15),
                WarrantStatusTimestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
                WarrantStatusTypeID INTEGER NOT NULL,
                WarrantID INTEGER NOT NULL,
                CONSTRAINT WarrantStatus_pk PRIMARY KEY (WarrantStatusID)
);


CREATE TABLE WarrantRemarks (
                WarrantRemarksID IDENTITY NOT NULL,
                WarrantID INTEGER NOT NULL,
                WarrantRemarkText VARCHAR(250),
                CONSTRAINT WarrantRemarks_pk PRIMARY KEY (WarrantRemarksID)
);


CREATE TABLE Officer (
                OfficerID IDENTITY NOT NULL,
                ChargeRefID INTEGER NOT NULL,
                OfficerName VARCHAR(100) NOT NULL,
                OfficerBadgeNumber VARCHAR(20),
                CONSTRAINT Officer_pk PRIMARY KEY (OfficerID)
);


ALTER TABLE WarrantStatus ADD CONSTRAINT WarrantStatusType_WarrantStatus_fk
FOREIGN KEY (WarrantStatusTypeID)
REFERENCES WarrantStatusType (WarrantStatusTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE ChargeRef ADD CONSTRAINT Person_Arrest_fk
FOREIGN KEY (PersonID)
REFERENCES Person (PersonID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PersonAlternateName ADD CONSTRAINT Person_PersonAlternateName_fk
FOREIGN KEY (PersonID)
REFERENCES Person (PersonID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PersonSSNAdditional ADD CONSTRAINT Person_PersonSSNAdditional_fk
FOREIGN KEY (PersonID)
REFERENCES Person (PersonID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PersonOLNAdditional ADD CONSTRAINT Person_PersonOLNAdditional_fk
FOREIGN KEY (PersonID)
REFERENCES Person (PersonID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PersonSMTAdditional ADD CONSTRAINT Person_PersonSMTAdditional_fk
FOREIGN KEY (PersonID)
REFERENCES Person (PersonID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PersonIDAdditional ADD CONSTRAINT Person_PersonIDAdditional_fk
FOREIGN KEY (PersonID)
REFERENCES Person (PersonID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Vehicle ADD CONSTRAINT Person_Vehicle_fk
FOREIGN KEY (PersonID)
REFERENCES Person (PersonID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Officer ADD CONSTRAINT Arrest_Officer_fk
FOREIGN KEY (ChargeRefID)
REFERENCES ChargeRef (ChargeRefID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Warrant ADD CONSTRAINT ChargeRef_Warrant_fk
FOREIGN KEY (ChargeRefID)
REFERENCES ChargeRef (ChargeRefID)
ON DELETE CASCADE
ON UPDATE NO ACTION;

ALTER TABLE WarrantRemarks ADD CONSTRAINT Warrant_WarrantRemarks_fk
FOREIGN KEY (WarrantID)
REFERENCES Warrant (WarrantID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE WarrantStatus ADD CONSTRAINT Warrant_WarrantStatus_fk
FOREIGN KEY (WarrantID)
REFERENCES Warrant (WarrantID)
ON DELETE CASCADE
ON UPDATE NO ACTION;