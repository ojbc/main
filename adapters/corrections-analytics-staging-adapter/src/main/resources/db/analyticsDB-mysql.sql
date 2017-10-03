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
drop database if exists `ojbc_community_corrections_staging`;
CREATE DATABASE `ojbc_community_corrections_staging`; 
use ojbc_community_corrections_staging;

/**
* Copy DDL from SQL PA below here.  Modify timestamps in fact tables like this:
*                `Timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
**/


CREATE TABLE ProbationTermination (
                ProbationTerminationID INT AUTO_INCREMENT NOT NULL,
                ProbationIdentification VARCHAR(100) NOT NULL,
                TerminationDate DATE NOT NULL,
                ProbationTerminationTimestamp DATETIME DEFAULT now() NOT NULL,
                PRIMARY KEY (ProbationTerminationID)
);


CREATE TABLE Supervisor (
                SupervisorID INT AUTO_INCREMENT NOT NULL,
                SupervisorGivenName VARCHAR(200) NOT NULL,
                SupervisorSurName VARCHAR(200) NOT NULL,
                SupervisorORI VARCHAR(100),
                SupervisorTimestamp DATETIME DEFAULT now()  NOT NULL,
                PRIMARY KEY (SupervisorID)
);


CREATE TABLE OtherIdentification (
                OtherIdentificationIdentificationID INT AUTO_INCREMENT NOT NULL,
                SupervisorID INT NOT NULL,
                OtherIdentificationValue VARCHAR(100) NOT NULL,
                OtherIdentificationType VARCHAR(50) NOT NULL,
                OtherIdentificationTimestamp DATETIME DEFAULT now()  NOT NULL,
                PRIMARY KEY (OtherIdentificationIdentificationID)
);

ALTER TABLE OtherIdentification COMMENT 'Supervisor Other Identification';


CREATE TABLE Probation (
                ProbationID INT AUTO_INCREMENT NOT NULL,
                SupervisorID INT NOT NULL,
                ProbationIdentification VARCHAR(100) NOT NULL,
                SupervisionPersonStatus VARCHAR(100),
                SupervisionStartDate DATE NOT NULL,
                SupervisionLevelText VARCHAR(50) NOT NULL,
                PersonUniqueIdentifier VARCHAR(50) NOT NULL,
                PersonBirthDate DATE,
                PersonRaceText VARCHAR(50),
                PersonSexText VARCHAR(50),
                SupervisionTimestamp DATETIME DEFAULT now()  NOT NULL,
                PRIMARY KEY (ProbationID)
);


CREATE TABLE ArrestAgencyID (
                ArrestAgencyID INT AUTO_INCREMENT NOT NULL,
                ArrestAgencyORI VARCHAR(50) NOT NULL,
                OrganizationName VARCHAR(200) NOT NULL,
                ArrestAgencyTimestamp DATETIME DEFAULT now() NOT NULL,
                PRIMARY KEY (ArrestAgencyID)
);


CREATE TABLE Arrest (
                ArrestID INT AUTO_INCREMENT NOT NULL,
                ArrestAgencyID INT NOT NULL,
                AttorneyGeneralCaseIndicator BOOLEAN DEFAULT false,
                RecordCategoryText VARCHAR(100),
                ArrestIdentification VARCHAR(100) NOT NULL,
                ArrestDate DATE NOT NULL,
                ArrestTime TIME NOT NULL,
                BookingDate DATE NOT NULL,
                ArrestLocationDescription VARCHAR(200),
                ArrestLocationLocaleDistrict VARCHAR(50),
                ArrestLocationLocalePoliceBeat VARCHAR(50),
                ArrestOfficialBadgeId VARCHAR(50),
                ArrestProbableCause VARCHAR(100),
                ArrestSummonsIndicator BOOLEAN,
                BookingReleaseDate DATE,
                BookingReleaseTime TIME,
                BookingSubjectCustodyTransferDate DATE,
                BookingSubjectCustodyTransferTime TIME,
                GreenboxIdentPCN VARCHAR(100) NOT NULL,
                GreenboxIdentProcessControlFlag VARCHAR(50) NOT NULL,
                GreenboxIdentOBTSActionCode VARCHAR(50) NOT NULL,
                GreenboxIdentReadOnly VARCHAR(50) NOT NULL,
                GreenboxIdentRecordType VARCHAR(50) NOT NULL,
                GreenboxIdentOBTSControlFlag VARCHAR(50) NOT NULL,
                PersonUniqueIdentifier VARCHAR(50) NOT NULL,
                PersonBirthDate DATE,
                PersonHairColorCode VARCHAR(50),
                PersonMaritalStatusText VARCHAR(50),
                PersonRaceText VARCHAR(50),
                PersonSexText VARCHAR(50),
                PersonUSCitizenIndicator BOOLEAN,
                ArrestTimestamp DATETIME DEFAULT now()  NOT NULL,
                PRIMARY KEY (ArrestID)
);


CREATE TABLE ArrestCharge (
                ArrestChargeID INT AUTO_INCREMENT NOT NULL,
                ArrestID INT NOT NULL,
                ArrestChargeIdentification VARCHAR(100) NOT NULL,
                ArrestChargeNarrative VARCHAR(200),
                ArrestChargeSeverityText VARCHAR(100),
                ArrestChargeChargeStatute VARCHAR(200),
                ArrestChargeStatuteDescriptionText VARCHAR(200),
                ArrestChargeTimestamp DATETIME DEFAULT now() NOT NULL,
                PRIMARY KEY (ArrestChargeID)
);


ALTER TABLE Probation ADD CONSTRAINT supervisor_supervision_fk
FOREIGN KEY (SupervisorID)
REFERENCES Supervisor (SupervisorID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE OtherIdentification ADD CONSTRAINT supervisor_otheridentification_fk
FOREIGN KEY (SupervisorID)
REFERENCES Supervisor (SupervisorID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Arrest ADD CONSTRAINT arrestagencyid_arrest_fk
FOREIGN KEY (ArrestAgencyID)
REFERENCES ArrestAgencyID (ArrestAgencyID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE ArrestCharge ADD CONSTRAINT arrest_arrestcharge_fk
FOREIGN KEY (ArrestID)
REFERENCES Arrest (ArrestID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;