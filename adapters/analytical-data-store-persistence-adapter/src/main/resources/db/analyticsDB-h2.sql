--
-- Unless explicitly acquired and licensed from Licensor under another license, the contents of
-- this file are subject to the Reciprocal Public License ("RPL") Version 1.5, or subsequent
-- versions as allowed by the RPL, and You may not copy or use this file in either source code
-- or executable form, except in compliance with the terms and conditions of the RPL
--
-- All software distributed under the RPL is provided strictly on an "AS IS" basis, WITHOUT
-- WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY DISCLAIMS ALL SUCH
-- WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
-- PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific language
-- governing rights and limitations under the RPL.
--
-- http://opensource.org/licenses/RPL-1.5
--
-- Copyright 2012-2015 Open Justice Broker Consortium
--

CREATE schema AnalyticsDataStore;

CREATE TABLE PersonAgeRange (
                PersonAgeRangeID INTEGER NOT NULL,
                AgeRange5 VARCHAR(7) NOT NULL,
                AgeRange5Sort VARCHAR(7) NOT NULL,
                CONSTRAINT PersonAgeRange_pk PRIMARY KEY (PersonAgeRangeID)
);


CREATE TABLE IncidentType (
                IncidentTypeID IDENTITY NOT NULL,
                IncidentTypeDescription VARCHAR(80) NOT NULL,
                CONSTRAINT IncidentType_pk PRIMARY KEY (IncidentTypeID)
);


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
                DispositionDescription VARCHAR(30) NOT NULL,
                IsConviction CHAR(1) NOT NULL,
                CONSTRAINT DispositionTypeID PRIMARY KEY (DispositionTypeID)
);


CREATE TABLE RiskScore (
                RiskScoreID IDENTITY NOT NULL,
                RiskScoreDescription VARCHAR(30) NOT NULL,
                CONSTRAINT RiskScoreID PRIMARY KEY (RiskScoreID)
);


CREATE TABLE PretrialService (
                PretrialServiceID IDENTITY NOT NULL,
                PretrialServiceDescription VARCHAR(80) NOT NULL,
                IsParticipant VARCHAR(7) NOT NULL,
                CONSTRAINT PretrialServiceID PRIMARY KEY (PretrialServiceID)
);


CREATE TABLE Agency (
                AgencyID IDENTITY NOT NULL,
                AgencyName VARCHAR(40) NOT NULL,
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
                PersonSexID INTEGER NOT NULL,
                PersonRaceID INTEGER NOT NULL,
                PersonBirthDate DATE NOT NULL,
                PersonUniqueIdentifier CHAR(36) NOT NULL,
                CONSTRAINT Person_pk PRIMARY KEY (PersonID)
);


CREATE TABLE PretrialServiceParticipation (
                PretrialServiceParticipationID IDENTITY NOT NULL,
                PretrialServiceCaseNumber VARCHAR(30) NOT NULL,
                PretrialServiceID INTEGER NOT NULL,
                PersonID INTEGER NOT NULL,
                CountyID INTEGER NOT NULL,
                RiskScoreID INTEGER NOT NULL,
                AssessedNeedID INTEGER NOT NULL,
                IntakeDate DATE NOT NULL,
                RecordType CHAR(1) NOT NULL,
                CONSTRAINT PretrialServiceParticipation_pk PRIMARY KEY (PretrialServiceParticipationID)
);
COMMENT ON COLUMN PretrialServiceParticipation.RecordType IS 'N for new record, U for update to prior record, D for delete';

CREATE TABLE Population (
                PopulationID IDENTITY NOT NULL,
                Year INTEGER NOT NULL,
                CountyID INTEGER NOT NULL,
                PersonSexID INTEGER NOT NULL,
                PersonRaceID INTEGER NOT NULL,
                PersonAgeRangeID INTEGER NOT NULL,
                PopulationCount INTEGER NOT NULL,
                CONSTRAINT Population_pk PRIMARY KEY (PopulationID)
);


CREATE TABLE OffenseType (
                OffenseTypeID IDENTITY NOT NULL,
                OffenseDescription VARCHAR(80) NOT NULL,
                IsDrugOffense CHAR(1) NOT NULL,
                OffenseSeverity VARCHAR(30) NOT NULL,
                CONSTRAINT OffenseTypeID PRIMARY KEY (OffenseTypeID)
);
COMMENT ON COLUMN OffenseType.OffenseSeverity IS 'Felony, Misdemeanor, Infraction, etc.';


CREATE TABLE Disposition (
                DispositionID INTEGER NOT NULL,
                PersonID INTEGER NOT NULL,
                DispositionTypeID INTEGER NOT NULL,
                OffenseTypeID INTEGER NOT NULL,
                IncidentCaseNumber VARCHAR(30) NOT NULL,
                DispositionDate DATE NOT NULL,
                SentenceTermDays INTEGER NOT NULL,
                SentenceFineAmount NUMERIC(10,2) NOT NULL,
                RecordType CHAR(1) NOT NULL,
                IsProbationViolation CHAR(1) NOT NULL,
                RecidivismEligibilityDate DATE NOT NULL,
                CONSTRAINT Disposition_pk PRIMARY KEY (DispositionID)
);
COMMENT ON COLUMN Disposition.RecordType IS 'N for new record, U for update to prior record, D for delete';


CREATE TABLE Incident (
                IncidentID IDENTITY NOT NULL,
                ReportingAgencyID INTEGER NOT NULL,
                IncidentCaseNumber VARCHAR(30) NOT NULL,
                IncidentTypeID INTEGER NOT NULL,
                IncidentLocationLatitude NUMERIC(14,10),
                IncidentLocationLongitude NUMERIC(14,10),
                IncidentLocationStreetAddress VARCHAR(100),
                IncidentLocationTown VARCHAR(50),
                IncidentDate DATE NOT NULL,
                IncidentTime TIME NOT NULL,
                RecordType CHAR(1) NOT NULL,
                CONSTRAINT IncidentID PRIMARY KEY (IncidentID)
);
COMMENT ON COLUMN Incident.RecordType IS 'N for new record, U for update to previous record, D for delete';


CREATE TABLE Arrest (
                ArrestID IDENTITY NOT NULL,
                PersonID INTEGER NOT NULL,
                IncidentID INTEGER NOT NULL,
                ArrestingAgencyID INTEGER NOT NULL,
                ArrestLocationLatitude NUMERIC(14,10),
                ArrestLocationLongitude NUMERIC(14,10),
                ArrestDate DATE NOT NULL,
                ArrestTime TIME NOT NULL,
                ArrestDrugRelated CHAR(1) NOT NULL,
                CONSTRAINT ArrestID PRIMARY KEY (ArrestID)
);
COMMENT ON COLUMN Arrest.ArrestDrugRelated IS 'Y if drug related, N otherwise';


CREATE TABLE Charge (
                ChargeID IDENTITY NOT NULL,
                ArrestOffenseTypeID INTEGER NOT NULL,
                ArrestID INTEGER NOT NULL,
                CONSTRAINT Charge_pk PRIMARY KEY (ChargeID)
);


ALTER TABLE Population ADD CONSTRAINT AgeRange_Population_fk
FOREIGN KEY (PersonAgeRangeID)
REFERENCES PersonAgeRange (PersonAgeRangeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Incident ADD CONSTRAINT IncidentType_Incident_fk
FOREIGN KEY (IncidentTypeID)
REFERENCES IncidentType (IncidentTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PretrialServiceParticipation ADD CONSTRAINT AssessedNeed_PretrialServiceParticipation_fk
FOREIGN KEY (AssessedNeedID)
REFERENCES AssessedNeed (AssessedNeedID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Population ADD CONSTRAINT PersonSex_Population_fk
FOREIGN KEY (PersonSexID)
REFERENCES PersonSex (PersonSexID)
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

ALTER TABLE PretrialServiceParticipation ADD CONSTRAINT RiskScore_PretrialServiceParticipation_fk
FOREIGN KEY (RiskScoreID)
REFERENCES RiskScore (RiskScoreID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PretrialServiceParticipation ADD CONSTRAINT PretrialService_PretrialServiceParticipation_fk
FOREIGN KEY (PretrialServiceID)
REFERENCES PretrialService (PretrialServiceID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Incident ADD CONSTRAINT Agency_Incident_fk
FOREIGN KEY (ReportingAgencyID)
REFERENCES Agency (AgencyID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Arrest ADD CONSTRAINT Agency_Arrest_fk
FOREIGN KEY (ArrestingAgencyID)
REFERENCES Agency (AgencyID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PretrialServiceParticipation ADD CONSTRAINT County_PretrialServiceParticipation_fk
FOREIGN KEY (CountyID)
REFERENCES County (CountyID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Population ADD CONSTRAINT County_Population_fk
FOREIGN KEY (CountyID)
REFERENCES County (CountyID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Population ADD CONSTRAINT PersonRace_Population_fk
FOREIGN KEY (PersonRaceID)
REFERENCES PersonRace (PersonRaceID)
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

ALTER TABLE Charge ADD CONSTRAINT OffenseType_Charge_Arrest_fk
FOREIGN KEY (ArrestOffenseTypeID)
REFERENCES OffenseType (OffenseTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Disposition ADD CONSTRAINT OffenseType_Disposition_fk
FOREIGN KEY (OffenseTypeID)
REFERENCES OffenseType (OffenseTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Arrest ADD CONSTRAINT Incident_Arrest_fk
FOREIGN KEY (IncidentID)
REFERENCES Incident (IncidentID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Charge ADD CONSTRAINT ArrestCharge_Charge_fk
FOREIGN KEY (ArrestID)
REFERENCES Arrest (ArrestID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;