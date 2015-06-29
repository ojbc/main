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
 
drop database if exists `AnalyticsDataStore`;
CREATE DATABASE `AnalyticsDataStore`; 
use AnalyticsDataStore;

/**
* Copy DDL from SQL PA below here.  Modify timestamps in fact tables like this:
*                `Timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
**/

CREATE TABLE InvolvedDrug (
                InvolvedDrugID INT AUTO_INCREMENT NOT NULL,
                InvolvedDrugDescription VARCHAR(40) NOT NULL,
                PRIMARY KEY (InvolvedDrugID)
);


CREATE TABLE IncidentType (
                IncidentTypeID INT AUTO_INCREMENT NOT NULL,
                IncidentTypeDescription VARCHAR(80) NOT NULL,
                PRIMARY KEY (IncidentTypeID)
);


CREATE TABLE AssessedNeed (
                AssessedNeedID INT AUTO_INCREMENT NOT NULL,
                AssessedNeedDescription VARCHAR(30) NOT NULL,
                PRIMARY KEY (AssessedNeedID)
);


CREATE TABLE PersonSex (
                PersonSexID INT AUTO_INCREMENT NOT NULL,
                PersonSexDescription VARCHAR(7) NOT NULL,
                PRIMARY KEY (PersonSexID)
);


CREATE TABLE DispositionType (
                DispositionTypeID INT AUTO_INCREMENT NOT NULL,
                DispositionDescription VARCHAR(35) NOT NULL,
                PRIMARY KEY (DispositionTypeID)
);


CREATE TABLE PretrialService (
                PretrialServiceID INT AUTO_INCREMENT NOT NULL,
                PretrialServiceDescription VARCHAR(80) NOT NULL,
                PRIMARY KEY (PretrialServiceID)
);


CREATE TABLE Agency (
                AgencyID INT AUTO_INCREMENT NOT NULL,
                AgencyName VARCHAR(40) NOT NULL,
                AgencyORI VARCHAR(12) NOT NULL,
                PRIMARY KEY (AgencyID)
);


CREATE TABLE County (
                CountyID INT AUTO_INCREMENT NOT NULL,
                CountyName VARCHAR(30) NOT NULL,
                PRIMARY KEY (CountyID)
);


CREATE TABLE PersonRace (
                PersonRaceID INT AUTO_INCREMENT NOT NULL,
                PersonRaceDescription VARCHAR(20) NOT NULL,
                PRIMARY KEY (PersonRaceID)
);


CREATE TABLE Person (
                PersonID INT AUTO_INCREMENT NOT NULL,
                PersonSexID INT,
                PersonRaceID INT,
                PersonBirthDate DATE,
                PersonUniqueIdentifier CHAR(36) NOT NULL,
                PRIMARY KEY (PersonID)
);


CREATE TABLE PretrialServiceParticipation (
                PretrialServiceParticipationID INT AUTO_INCREMENT NOT NULL,
                PersonID INT NOT NULL,
                CountyID INT NOT NULL,
                RiskScore INT NOT NULL,
                IntakeDate DATE NOT NULL,
                RecordType CHAR(1) NOT NULL,
                ArrestingAgencyORI VARCHAR(12) NOT NULL,
                ArrestIncidentCaseNumber VARCHAR(30) NOT NULL,
                `Timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                PRIMARY KEY (PretrialServiceParticipationID)
);

ALTER TABLE PretrialServiceParticipation MODIFY COLUMN RecordType CHAR(1) COMMENT 'N for new record, U for update to prior record, D for delete';


CREATE TABLE PretrialServiceAssociation (
                PretrialServiceAssociationID INT AUTO_INCREMENT NOT NULL,
                PretrialServiceID INT NOT NULL,
                PretrialServiceParticipationID INT NOT NULL,
                PRIMARY KEY (PretrialServiceAssociationID)
);


CREATE TABLE PretrialServiceNeedAssociation (
                PretrialServiceNeedAssociationID INT AUTO_INCREMENT NOT NULL,
                AssessedNeedID INT NOT NULL,
                PretrialServiceParticipationID INT NOT NULL,
                PRIMARY KEY (PretrialServiceNeedAssociationID)
);


CREATE TABLE OffenseType (
                OffenseTypeID INT AUTO_INCREMENT NOT NULL,
                OffenseDescription VARCHAR(80) NOT NULL,
                PRIMARY KEY (OffenseTypeID)
);


CREATE TABLE Disposition (
                DispositionID INT AUTO_INCREMENT NOT NULL,
                PersonID INT NOT NULL,
                DispositionTypeID INT NOT NULL,
                IncidentCaseNumber VARCHAR(30) NOT NULL,
                DispositionDate DATE NOT NULL,
                ArrestingAgencyORI VARCHAR(12) NOT NULL,
                SentenceTermDays NUMERIC(10,2),
                SentenceFineAmount NUMERIC(10,2),
                InitialChargeCode VARCHAR(35) NOT NULL,
                FinalChargeCode VARCHAR(35) NOT NULL,
                RecordType CHAR(1) NOT NULL,
                IsProbationViolation CHAR(1),
                IsProbationViolationOnOldCharge CHAR(1),
                RecidivismEligibilityDate DATE,
                `Timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                PRIMARY KEY (DispositionID)
);

ALTER TABLE Disposition MODIFY COLUMN RecordType CHAR(1) COMMENT 'N for new record, U for update to prior record, D for delete';


CREATE TABLE Incident (
                IncidentID INT AUTO_INCREMENT NOT NULL,
                ReportingAgencyID INT NOT NULL,
                IncidentCaseNumber VARCHAR(30) NOT NULL,
                IncidentTypeID INT NOT NULL,
                IncidentLocationLatitude NUMERIC(14,10),
                IncidentLocationLongitude NUMERIC(14,10),
                IncidentLocationStreetAddress VARCHAR(100),
                IncidentLocationTown VARCHAR(50),
                IncidentDate DATE NOT NULL,
                IncidentTime TIME NOT NULL,
                RecordType CHAR(1) NOT NULL,
                `Timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                PRIMARY KEY (IncidentID)
);

ALTER TABLE Incident MODIFY COLUMN RecordType CHAR(1) COMMENT 'ete';


CREATE TABLE Arrest (
                ArrestID INT AUTO_INCREMENT NOT NULL,
                PersonID INT NOT NULL,
                IncidentID INT NOT NULL,
                ArrestingAgencyID INT NOT NULL,
                ArrestDate DATE NOT NULL,
                ArrestTime TIME NOT NULL,
                ArrestDrugRelated CHAR(1) NOT NULL,
                InvolvedDrugID INT,
                `Timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                PRIMARY KEY (ArrestID)
);

ALTER TABLE Arrest MODIFY COLUMN ArrestDrugRelated CHAR(1) COMMENT 'Y if drug related, N otherwise';


CREATE TABLE Charge (
                ChargeID INT AUTO_INCREMENT NOT NULL,
                ArrestOffenseTypeID INT NOT NULL,
                ArrestID INT NOT NULL,
                PRIMARY KEY (ChargeID)
);


ALTER TABLE Arrest ADD CONSTRAINT involveddrug_arrest_fk
FOREIGN KEY (InvolvedDrugID)
REFERENCES InvolvedDrug (InvolvedDrugID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Incident ADD CONSTRAINT incidenttype_incident_fk
FOREIGN KEY (IncidentTypeID)
REFERENCES IncidentType (IncidentTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PretrialServiceNeedAssociation ADD CONSTRAINT assessedneed_pretrialserviceassessedneed_fk
FOREIGN KEY (AssessedNeedID)
REFERENCES AssessedNeed (AssessedNeedID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Person ADD CONSTRAINT personsex_person_fk
FOREIGN KEY (PersonSexID)
REFERENCES PersonSex (PersonSexID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Disposition ADD CONSTRAINT dispositiontype_disposition_fk
FOREIGN KEY (DispositionTypeID)
REFERENCES DispositionType (DispositionTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PretrialServiceAssociation ADD CONSTRAINT pretrialservice_pretrialserviceassociation_fk
FOREIGN KEY (PretrialServiceID)
REFERENCES PretrialService (PretrialServiceID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Incident ADD CONSTRAINT agency_incident_fk
FOREIGN KEY (ReportingAgencyID)
REFERENCES Agency (AgencyID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Arrest ADD CONSTRAINT agency_arrest_fk
FOREIGN KEY (ArrestingAgencyID)
REFERENCES Agency (AgencyID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PretrialServiceParticipation ADD CONSTRAINT county_pretrialserviceparticipation_fk
FOREIGN KEY (CountyID)
REFERENCES County (CountyID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Person ADD CONSTRAINT personrace_person_fk
FOREIGN KEY (PersonRaceID)
REFERENCES PersonRace (PersonRaceID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Arrest ADD CONSTRAINT person_arrest_fk
FOREIGN KEY (PersonID)
REFERENCES Person (PersonID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Disposition ADD CONSTRAINT person_disposition_fk
FOREIGN KEY (PersonID)
REFERENCES Person (PersonID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PretrialServiceParticipation ADD CONSTRAINT person_pretrialserviceparticipation_fk
FOREIGN KEY (PersonID)
REFERENCES Person (PersonID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PretrialServiceNeedAssociation ADD CONSTRAINT pretrialserviceparticipation_pretrialserviceassessedneed_fk
FOREIGN KEY (PretrialServiceParticipationID)
REFERENCES PretrialServiceParticipation (PretrialServiceParticipationID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PretrialServiceAssociation ADD CONSTRAINT pretrialserviceparticipation_pretrialserviceassociation_fk
FOREIGN KEY (PretrialServiceParticipationID)
REFERENCES PretrialServiceParticipation (PretrialServiceParticipationID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Charge ADD CONSTRAINT offensetype_charge_arrest_fk
FOREIGN KEY (ArrestOffenseTypeID)
REFERENCES OffenseType (OffenseTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Arrest ADD CONSTRAINT incident_arrest_fk
FOREIGN KEY (IncidentID)
REFERENCES Incident (IncidentID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Charge ADD CONSTRAINT arrestcharge_charge_fk
FOREIGN KEY (ArrestID)
REFERENCES Arrest (ArrestID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;