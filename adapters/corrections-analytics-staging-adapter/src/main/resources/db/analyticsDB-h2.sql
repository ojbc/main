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
Drop schema if exists ojbc_corrections_staging;

CREATE schema ojbc_corrections_staging;


CREATE TABLE ProbationTermination (
                ProbationTerminationID IDENTITY NOT NULL,
                ProbationIdentification VARCHAR(100) NOT NULL,
                TerminationDate DATE NOT NULL,
                ProbationTerminationTimestamp TIMESTAMP DEFAULT now() NOT NULL,
                CONSTRAINT ProbationTerminationID PRIMARY KEY (ProbationTerminationID)
);


CREATE TABLE Supervisor (
                SupervisorID IDENTITY NOT NULL,
                SupervisorGivenName VARCHAR(200) NOT NULL,
                SupervisorSurName VARCHAR(200) NOT NULL,
                SupervisorORI VARCHAR(100),
                SupervisorTimestamp TIMESTAMP DEFAULT now()  NOT NULL,
                CONSTRAINT SupervisorID PRIMARY KEY (SupervisorID)
);


CREATE TABLE OtherIdentification (
                OtherIdentificationIdentificationID IDENTITY NOT NULL,
                SupervisorID INTEGER NOT NULL,
                OtherIdentificationValue VARCHAR(100) NOT NULL,
                OtherIdentificationType VARCHAR(50) NOT NULL,
                OtherIdentificationTimestamp TIMESTAMP DEFAULT now()  NOT NULL,
                CONSTRAINT OtherIdentificationIdentificationID PRIMARY KEY (OtherIdentificationIdentificationID)
);
COMMENT ON TABLE OtherIdentification IS 'Supervisor Other Identification';


CREATE TABLE Probation (
                ProbationID IDENTITY NOT NULL,
                SupervisorID INTEGER NOT NULL,
                ProbationIdentification VARCHAR(100) NOT NULL,
                SupervisionPersonStatus VARCHAR(100),
                SupervisionStartDate DATE NOT NULL,
                SupervisionLevelText VARCHAR(50) NOT NULL,
                PersonUniqueIdentifier VARCHAR(50) NOT NULL,
                PersonBirthDate DATE,
                PersonRaceText VARCHAR(50),
                PersonSexText VARCHAR(50),
                SupervisionTimestamp TIMESTAMP DEFAULT now()  NOT NULL,
                CONSTRAINT ProbationId PRIMARY KEY (ProbationID)
);


CREATE TABLE ArrestAgencyID (
                ArrestAgencyID IDENTITY NOT NULL,
                ArrestAgencyORI VARCHAR(50) NOT NULL,
                OrganizationName VARCHAR(200) NOT NULL,
                ArrestAgencyTimestamp TIMESTAMP DEFAULT now() NOT NULL,
                CONSTRAINT ArrestAgencyID PRIMARY KEY (ArrestAgencyID)
);


CREATE TABLE Arrest (
                ArrestID IDENTITY NOT NULL,
                ArrestAgencyID INTEGER NOT NULL,
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
                ArrestTimestamp TIMESTAMP DEFAULT now()  NOT NULL,
                CONSTRAINT Arrest_ID PRIMARY KEY (ArrestID)
);


CREATE TABLE ArrestCharge (
                ArrestChargeID IDENTITY NOT NULL,
                ArrestID INTEGER NOT NULL,
                ArrestChargeIdentification VARCHAR(100) NOT NULL,
                ArrestChargeNarrative VARCHAR(200),
                ArrestChargeSeverityText VARCHAR(100),
                ArrestChargeChargeStatute VARCHAR(200),
                ArrestChargeStatuteDescriptionText VARCHAR(200),
                ArrestChargeTimestamp TIMESTAMP DEFAULT now() NOT NULL,
                CONSTRAINT ArrestChargeID PRIMARY KEY (ArrestChargeID)
);


ALTER TABLE Probation ADD CONSTRAINT Supervisor_Supervision_fk
FOREIGN KEY (SupervisorID)
REFERENCES Supervisor (SupervisorID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE OtherIdentification ADD CONSTRAINT Supervisor_OtherIdentification_fk
FOREIGN KEY (SupervisorID)
REFERENCES Supervisor (SupervisorID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Arrest ADD CONSTRAINT ArrestAgencyID_Arrest_fk
FOREIGN KEY (ArrestAgencyID)
REFERENCES ArrestAgencyID (ArrestAgencyID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE ArrestCharge ADD CONSTRAINT Arrest_ArrestCharge_fk
FOREIGN KEY (ArrestID)
REFERENCES Arrest (ArrestID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;