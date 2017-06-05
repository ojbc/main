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
CREATE schema AnalyticsDataStore;

-- Go to analytics staging database model OJBStagingModel.architect and click Tools -> Forward Engineer, select H2
-- and paste the contents below this comment.



CREATE TABLE AssessedNeed (
                AssessedNeedID IDENTITY NOT NULL,
                AssessedNeedDescription VARCHAR(30) NOT NULL,
                CONSTRAINT AssessedNeedID PRIMARY KEY (AssessedNeedID)
);


CREATE TABLE PersonSex (
                PersonSexID IDENTITY NOT NULL,
                PersonSexDescription VARCHAR(7) NOT NULL,
                CONSTRAINT PersonSexID PRIMARY KEY (PersonSexID)
);


CREATE TABLE DispositionType (
                DispositionTypeID IDENTITY NOT NULL,
                DispositionDescription VARCHAR(35) NOT NULL,
                CONSTRAINT DispositionTypeID PRIMARY KEY (DispositionTypeID)
);


CREATE TABLE PretrialService (
                PretrialServiceID IDENTITY NOT NULL,
                PretrialServiceDescription VARCHAR(80) NOT NULL,
                CONSTRAINT PretrialServiceID PRIMARY KEY (PretrialServiceID)
);


CREATE TABLE Agency (
                AgencyID IDENTITY NOT NULL,
                AgencyName VARCHAR(40) NOT NULL,
                AgencyORI VARCHAR(12) NOT NULL,
                CONSTRAINT AgencyID PRIMARY KEY (AgencyID)
);


CREATE TABLE County (
                CountyID IDENTITY NOT NULL,
                CountyName VARCHAR(30) NOT NULL,
                CONSTRAINT CountyID PRIMARY KEY (CountyID)
);


CREATE TABLE PersonRace (
                PersonRaceID IDENTITY NOT NULL,
                PersonRaceDescription VARCHAR(20) NOT NULL,
                CONSTRAINT PersonRaceID PRIMARY KEY (PersonRaceID)
);


CREATE TABLE Person (
                PersonID IDENTITY NOT NULL,
                PersonSexID INTEGER,
                PersonRaceID INTEGER,
                PersonBirthDate DATE,
                PersonUniqueIdentifier CHAR(36) NOT NULL,
                CONSTRAINT Person_pk PRIMARY KEY (PersonID)
);


CREATE TABLE PretrialServiceParticipation (
                PretrialServiceParticipationID IDENTITY NOT NULL,
                PersonID INTEGER NOT NULL,
                CountyID INTEGER,
                RiskScore INTEGER NOT NULL,
                IntakeDate DATE NOT NULL,
                RecordType CHAR(1) NOT NULL,
                ArrestingAgencyORI VARCHAR(12),
                ArrestIncidentCaseNumber VARCHAR(30) NOT NULL,
                PretrialServiceUniqueID VARCHAR(128) NOT NULL,
                Timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
                CONSTRAINT PretrialServiceParticipation_pk PRIMARY KEY (PretrialServiceParticipationID)
);
COMMENT ON COLUMN PretrialServiceParticipation.RecordType IS 'N for new record, U for update to prior record, D for delete';


CREATE TABLE PretrialServiceAssociation (
                PretrialServiceAssociationID IDENTITY NOT NULL,
                PretrialServiceID INTEGER NOT NULL,
                PretrialServiceParticipationID INTEGER NOT NULL,
                CONSTRAINT PretrialServiceAssociation_pk PRIMARY KEY (PretrialServiceAssociationID)
);


CREATE TABLE PretrialServiceNeedAssociation (
                PretrialServiceNeedAssociationID IDENTITY NOT NULL,
                AssessedNeedID INTEGER NOT NULL,
                PretrialServiceParticipationID INTEGER NOT NULL,
                CONSTRAINT PretrialServiceNeedAssociation_pk PRIMARY KEY (PretrialServiceNeedAssociationID)
);


CREATE TABLE Disposition (
                DispositionID IDENTITY NOT NULL,
                PersonID INTEGER NOT NULL,
                DispositionTypeID INTEGER NOT NULL,
                IncidentCaseNumber VARCHAR(30) NOT NULL,
                DispositionDate DATE NOT NULL,
                ArrestingAgencyORI VARCHAR(12) NOT NULL,
                SentenceTermDays NUMERIC(10,2),
                SentenceFineAmount NUMERIC(10,2),
                InitialChargeCode VARCHAR(35) NOT NULL,
                InitialChargeCode1 VARCHAR(5) NOT NULL,
                FinalChargeCode VARCHAR(35) NOT NULL,
                FinalChargeCode1 VARCHAR(5) NOT NULL,
                RecordType CHAR(1) NOT NULL,
                IsProbationViolation CHAR(1),
                IsProbationViolationOnOldCharge CHAR(1),
                RecidivismEligibilityDate DATE,
                DocketChargeNumber VARCHAR(60) NOT NULL,
                Timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
                CONSTRAINT Disposition_pk PRIMARY KEY (DispositionID)
);
COMMENT ON COLUMN Disposition.RecordType IS 'N for new record, U for update to prior record, D for delete';


CREATE TABLE Incident (
                IncidentID IDENTITY NOT NULL,
                ReportingAgencyID INTEGER NOT NULL,
                IncidentCaseNumber VARCHAR(30) NOT NULL,
                IncidentLocationLatitude NUMERIC(14,10),
                IncidentLocationLongitude NUMERIC(14,10),
                IncidentLocationStreetAddress VARCHAR(100),
                IncidentLocationTown VARCHAR(50),
                IncidentDate DATE NOT NULL,
                IncidentTime TIME NOT NULL,
                ReportingSystem VARCHAR(30) NOT NULL,
                RecordType CHAR(1) NOT NULL,
                Timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
                CONSTRAINT IncidentID PRIMARY KEY (IncidentID)
);
COMMENT ON COLUMN Incident.RecordType IS 'N for new record, U for update to previous record, D for delete';


CREATE TABLE IncidentType (
                IncidentTypeID IDENTITY NOT NULL,
                IncidentID INTEGER NOT NULL,
                IncidentDescriptionText VARCHAR(120) NOT NULL,
                CONSTRAINT IncidentTypeID PRIMARY KEY (IncidentTypeID)
);


CREATE TABLE IncidentCircumstance (
                IncidentCircumstanceID IDENTITY NOT NULL,
                IncidentID INTEGER NOT NULL,
                IncidentCircumstanceText VARCHAR(80) NOT NULL,
                CONSTRAINT IncidentCircumstance_pk PRIMARY KEY (IncidentCircumstanceID)
);


CREATE TABLE Arrest (
                ArrestID IDENTITY NOT NULL,
                PersonID INTEGER NOT NULL,
                IncidentID INTEGER NOT NULL,
                ArrestDate DATE NOT NULL,
                ArrestTime TIME NOT NULL,
                ArrestingAgencyName VARCHAR(40) NOT NULL,
                ReportingSystem VARCHAR(30) NOT NULL,
                Timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
                CONSTRAINT ArrestID PRIMARY KEY (ArrestID)
);


CREATE TABLE Charge (
                ChargeID IDENTITY NOT NULL,
                ArrestID INTEGER NOT NULL,
                OffenseDescriptionText VARCHAR(300),
                OffenseDescriptionText1 VARCHAR(300),
                CONSTRAINT Charge_pk PRIMARY KEY (ChargeID)
);


ALTER TABLE PretrialServiceNeedAssociation ADD CONSTRAINT AssessedNeed_PretrialServiceAssessedNeed_fk
FOREIGN KEY (AssessedNeedID)
REFERENCES AssessedNeed (AssessedNeedID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Person ADD CONSTRAINT PersonSex_Person_fk
FOREIGN KEY (PersonSexID)
REFERENCES PersonSex (PersonSexID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Disposition ADD CONSTRAINT DispositionType_Disposition_fk
FOREIGN KEY (DispositionTypeID)
REFERENCES DispositionType (DispositionTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PretrialServiceAssociation ADD CONSTRAINT PretrialService_PretrialServiceAssociation_fk
FOREIGN KEY (PretrialServiceID)
REFERENCES PretrialService (PretrialServiceID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Incident ADD CONSTRAINT Agency_Incident_fk
FOREIGN KEY (ReportingAgencyID)
REFERENCES Agency (AgencyID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PretrialServiceParticipation ADD CONSTRAINT County_PretrialServiceParticipation_fk
FOREIGN KEY (CountyID)
REFERENCES County (CountyID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Person ADD CONSTRAINT PersonRace_Person_fk
FOREIGN KEY (PersonRaceID)
REFERENCES PersonRace (PersonRaceID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Arrest ADD CONSTRAINT Person_Arrest_fk
FOREIGN KEY (PersonID)
REFERENCES Person (PersonID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Disposition ADD CONSTRAINT Person_Disposition_fk
FOREIGN KEY (PersonID)
REFERENCES Person (PersonID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PretrialServiceParticipation ADD CONSTRAINT Person_PretrialServiceParticipation_fk
FOREIGN KEY (PersonID)
REFERENCES Person (PersonID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PretrialServiceNeedAssociation ADD CONSTRAINT PretrialServiceParticipation_PretrialServiceAssessedNeed_fk
FOREIGN KEY (PretrialServiceParticipationID)
REFERENCES PretrialServiceParticipation (PretrialServiceParticipationID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PretrialServiceAssociation ADD CONSTRAINT PretrialServiceParticipation_PretrialServiceAssociation_fk
FOREIGN KEY (PretrialServiceParticipationID)
REFERENCES PretrialServiceParticipation (PretrialServiceParticipationID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Arrest ADD CONSTRAINT Incident_Arrest_fk
FOREIGN KEY (IncidentID)
REFERENCES Incident (IncidentID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE IncidentCircumstance ADD CONSTRAINT Incident_IncidentCircumstance_fk
FOREIGN KEY (IncidentID)
REFERENCES Incident (IncidentID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE IncidentType ADD CONSTRAINT Incident_IncidentType_fk
FOREIGN KEY (IncidentID)
REFERENCES Incident (IncidentID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Charge ADD CONSTRAINT ArrestCharge_Charge_fk
FOREIGN KEY (ArrestID)
REFERENCES Arrest (ArrestID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;