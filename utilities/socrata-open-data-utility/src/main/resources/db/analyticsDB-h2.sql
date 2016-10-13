CREATE schema ojbc_analytics;

-- Go to analytics staging database model OJBDimensionalModel.architect and click Tools -> Forward Engineer, select H2
-- and paste the contents below this comment.


CREATE TABLE ReportingSystem (
                ReportingSystemID INTEGER NOT NULL,
                ReportingSystemDescription VARCHAR(40) NOT NULL,
                CONSTRAINT ReportingSystem_pk PRIMARY KEY (ReportingSystemID)
);


CREATE TABLE IncidentType2 (
                IncidentTypeID IDENTITY NOT NULL,
                IncidentTypeDescription VARCHAR(120) NOT NULL,
                CONSTRAINT IncidentType2_pk PRIMARY KEY (IncidentTypeID)
);


CREATE TABLE LoadHistory (
                LoadID IDENTITY NOT NULL,
                LatestStagingUpdateTime TIMESTAMP NOT NULL,
                LoadStartTime TIMESTAMP NOT NULL,
                LoadEndTime TIMESTAMP,
                CONSTRAINT LoadHistory_pk PRIMARY KEY (LoadID)
);


CREATE TABLE DispositionOffenseType (
                DispositionOffenseTypeID IDENTITY NOT NULL,
                DispositionOffenseTypeDescription VARCHAR(40) NOT NULL,
                CONSTRAINT DispositionOffenseType_pk PRIMARY KEY (DispositionOffenseTypeID)
);


CREATE TABLE Region (
                RegionID IDENTITY NOT NULL,
                RegionDescription VARCHAR(40) NOT NULL,
                CONSTRAINT Region_pk PRIMARY KEY (RegionID)
);


CREATE TABLE Town (
                TownID IDENTITY NOT NULL,
                TownDescription VARCHAR(50) NOT NULL,
                CONSTRAINT Town_pk PRIMARY KEY (TownID)
);


CREATE TABLE ReadyCashOffenseCategory (
                ReadyCashOffenseCategoryID IDENTITY NOT NULL,
                ReadyCashOffenseCategoryDescription VARCHAR(40) NOT NULL,
                CONSTRAINT ReadyCashOffenseCategory_pk PRIMARY KEY (ReadyCashOffenseCategoryID)
);


CREATE TABLE InvolvedDrug (
                InvolvedDrugID IDENTITY NOT NULL,
                InvolvedDrugDescription VARCHAR(40) NOT NULL,
                CONSTRAINT InvolvedDrug_pk PRIMARY KEY (InvolvedDrugID)
);


CREATE TABLE IncidentCategory (
                IncidentCategoryID INTEGER NOT NULL,
                IncidentCategoryDescription VARCHAR(80) NOT NULL,
                CONSTRAINT IncidentCategory_pk PRIMARY KEY (IncidentCategoryID)
);


CREATE TABLE PersonAgeRange (
                PersonAgeRangeID INTEGER NOT NULL,
                AgeRange5 VARCHAR(7) NOT NULL,
                AgeRange5Sort VARCHAR(7) NOT NULL,
                CONSTRAINT PersonAgeRange_pk PRIMARY KEY (PersonAgeRangeID)
);


CREATE TABLE IncidentType (
                IncidentTypeID IDENTITY NOT NULL,
                IncidentTypeDescription VARCHAR(80) NOT NULL,
                IncidentCategoryID INTEGER NOT NULL,
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


CREATE TABLE Disposition (
                DispositionID IDENTITY NOT NULL,
                DispositionDescription VARCHAR(40) NOT NULL,
                IsConviction CHAR(1) NOT NULL,
                CONSTRAINT DispositionID PRIMARY KEY (DispositionID)
);


CREATE TABLE YesNo (
                YesNoID IDENTITY NOT NULL,
                YesNoDescription VARCHAR(10) NOT NULL,
                CONSTRAINT YesNoID PRIMARY KEY (YesNoID)
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
                RegionID INTEGER NOT NULL,
                CONSTRAINT CountyID PRIMARY KEY (CountyID)
);


CREATE TABLE PersonAge (
                PersonAgeID INTEGER NOT NULL,
                AgeInYears VARCHAR(10) NOT NULL,
                PersonAgeRangeID INTEGER NOT NULL,
                CONSTRAINT PersonAgeID PRIMARY KEY (PersonAgeID)
);


CREATE TABLE PersonRace (
                PersonRaceID IDENTITY NOT NULL,
                PersonRaceDescription VARCHAR(50) NOT NULL,
                CONSTRAINT PersonRaceID PRIMARY KEY (PersonRaceID)
);


CREATE TABLE Population (
                PopulationID IDENTITY NOT NULL,
                Year INTEGER NOT NULL,
                CountyID INTEGER NOT NULL,
                PersonSexID INTEGER NOT NULL,
                PersonRaceID INTEGER NOT NULL,
                PersonAgeRangeID INTEGER NOT NULL,
                PopulationCount INTEGER NOT NULL,
                LoadTimestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
                LoadID INTEGER NOT NULL,
                CONSTRAINT Population_pk PRIMARY KEY (PopulationID)
);


CREATE TABLE OffenseType (
                OffenseTypeID IDENTITY NOT NULL,
                OffenseDescription VARCHAR(80) NOT NULL,
                IsDrugOffense CHAR(1) NOT NULL,
                OffenseSeverity VARCHAR(30) NOT NULL,
                ReadyCashOffenseCategoryID INTEGER NOT NULL,
                CONSTRAINT OffenseTypeID PRIMARY KEY (OffenseTypeID)
);
COMMENT ON COLUMN OffenseType.OffenseSeverity IS 'Felony, Misdemeanor, Infraction, etc.';


CREATE TABLE Time (
                TimeID INTEGER NOT NULL,
                Hour INTEGER NOT NULL,
                Minute INTEGER NOT NULL,
                Second INTEGER NOT NULL,
                CONSTRAINT TimeID PRIMARY KEY (TimeID)
);


CREATE TABLE Date (
                DateID INTEGER NOT NULL,
                CalendarDate DATE NOT NULL,
                Year INTEGER NOT NULL,
                YearLabel CHAR(4) NOT NULL,
                CalendarQuarter INTEGER NOT NULL,
                Month INTEGER NOT NULL,
                MonthName VARCHAR(12) NOT NULL,
                FullMonth CHAR(7) NOT NULL,
                Day INTEGER NOT NULL,
                DayOfWeek VARCHAR(9) NOT NULL,
                DayOfWeekSort INTEGER NOT NULL,
                DateMMDDYYYY CHAR(10) NOT NULL,
                CONSTRAINT DateID PRIMARY KEY (DateID)
);


CREATE TABLE Incident (
                IncidentID IDENTITY NOT NULL,
                DateID INTEGER NOT NULL,
                TimeID INTEGER NOT NULL,
                ReportingAgencyID INTEGER NOT NULL,
                IncidentCaseNumber VARCHAR(30) NOT NULL,
                CountyID INTEGER,
                IncidentLocationLatitude NUMERIC(14,10),
                IncidentLocationLongitude NUMERIC(14,10),
                IncidentLocationStreetAddress VARCHAR(100),
                TownID INTEGER,
                IncidentDateTime TIMESTAMP,
                StagingRecordID INTEGER NOT NULL,
                LoadTimestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
                LoadID INTEGER NOT NULL,
                ReportingSystemID INTEGER NOT NULL,
                CONSTRAINT IncidentID PRIMARY KEY (IncidentID)
);


CREATE TABLE IncidentType2Association (
                IncidentTypeAssociationID IDENTITY NOT NULL,
                IncidentID INTEGER NOT NULL,
                IncidentTypeID INTEGER NOT NULL,
                CONSTRAINT IncidentType2Association_pk PRIMARY KEY (IncidentTypeAssociationID)
);


CREATE TABLE Arrest (
                ArrestID IDENTITY NOT NULL,
                IncidentID INTEGER NOT NULL,
                DateID INTEGER NOT NULL,
                TimeID INTEGER NOT NULL,
                ArresteeRaceID INTEGER NOT NULL,
                ArresteeAgeID INTEGER NOT NULL,
                ArresteeSexID INTEGER NOT NULL,
                CountyID INTEGER NOT NULL,
                ArrestingAgencyID INTEGER NOT NULL,
                ArrestDrugRelated INTEGER NOT NULL,
                SubsequentArrestID INTEGER,
                ArrestLocationLatitude NUMERIC(14,10),
                ArrestLocationLongitude NUMERIC(14,10),
                PretrialServiceEligible INTEGER NOT NULL,
                StagingPersonUniqueIdentifier VARCHAR(36) NOT NULL,
                DaysUntilNextArrest INTEGER,
                DaysUntilNextConviction INTEGER,
                SixMonthRearrestIndicator INTEGER,
                OneYearRearrestIndicator INTEGER,
                TwoYearRearrestIndicator INTEGER,
                OneYearReconvictionIndicator INTEGER,
                TwoYearReconvictionIndicator INTEGER,
                LoadTimestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
                CONSTRAINT ArrestID PRIMARY KEY (ArrestID)
);


CREATE INDEX SubsequentArrest_idx
 ON Arrest
 ( SubsequentArrestID );

CREATE TABLE Charge (
                ChargeID IDENTITY NOT NULL,
                ArrestOffenseTypeID INTEGER NOT NULL,
                ArrestChargeStatute VARCHAR(30),
                ArrestID INTEGER NOT NULL,
                SentenceTermDays INTEGER,
                SentenceFineAmount NUMERIC(10,2),
                DispositionID INTEGER,
                DispositionDateID INTEGER,
                InvolvedDrugID INTEGER,
                DispositionOffenseTypeID INTEGER,
                FiledOffenseTypeID INTEGER,
                RecidivismEligibilityDate DATE,
                LoadTimestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
                DispositionDataLoadID INTEGER,
                CONSTRAINT Charge_pk PRIMARY KEY (ChargeID)
);


CREATE TABLE PretrialServiceParticipation (
                PretrialServiceParticipationID IDENTITY NOT NULL,
                CountyID INTEGER NOT NULL,
                IntakeDateID INTEGER NOT NULL,
                RiskScoreID INTEGER,
                ArrestID INTEGER NOT NULL,
                PreChargeMonitoringIndicator INTEGER NOT NULL,
                StagingPersonUniqueIdentifier VARCHAR(36) NOT NULL,
                StagingRecordID INTEGER NOT NULL,
                LoadTimestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
                LoadID INTEGER NOT NULL,
                CONSTRAINT PretrialServiceParticipation_pk PRIMARY KEY (PretrialServiceParticipationID)
);


CREATE TABLE PretrialServiceAssociation (
                PretrialServiceAssociationID IDENTITY NOT NULL,
                PretrialServiceParticipationID INTEGER NOT NULL,
                PretrialServiceID INTEGER NOT NULL,
                CONSTRAINT PretrialServiceAssociation_pk PRIMARY KEY (PretrialServiceAssociationID)
);


CREATE TABLE PretrialAssessedNeed (
                PretrialAssessedNeedID IDENTITY NOT NULL,
                PretrialServiceParticipationID INTEGER NOT NULL,
                AssessedNeedID INTEGER NOT NULL,
                CONSTRAINT PretrialAssessedNeed_pk PRIMARY KEY (PretrialAssessedNeedID)
);


CREATE TABLE IncidentTypeAssociation (
                IncidentTypeAssociationID IDENTITY NOT NULL,
                IncidentID INTEGER NOT NULL,
                IncidentTypeID INTEGER NOT NULL,
                CONSTRAINT IncidentTypeAssociation_pk PRIMARY KEY (IncidentTypeAssociationID)
);


ALTER TABLE Incident ADD CONSTRAINT ReportingSystem_Incident_fk
FOREIGN KEY (ReportingSystemID)
REFERENCES ReportingSystem (ReportingSystemID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE IncidentType2Association ADD CONSTRAINT IncidentType2_IncidentType2Association_fk
FOREIGN KEY (IncidentTypeID)
REFERENCES IncidentType2 (IncidentTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Incident ADD CONSTRAINT LoadHistory_Incident_fk
FOREIGN KEY (LoadID)
REFERENCES LoadHistory (LoadID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PretrialServiceParticipation ADD CONSTRAINT LoadHistory_PretrialServiceParticipation_fk
FOREIGN KEY (LoadID)
REFERENCES LoadHistory (LoadID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Charge ADD CONSTRAINT LoadHistory_Charge_fk
FOREIGN KEY (DispositionDataLoadID)
REFERENCES LoadHistory (LoadID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Population ADD CONSTRAINT LoadHistory_Population_fk
FOREIGN KEY (LoadID)
REFERENCES LoadHistory (LoadID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Charge ADD CONSTRAINT DispositionOffenseType_Charge_fk
FOREIGN KEY (DispositionOffenseTypeID)
REFERENCES DispositionOffenseType (DispositionOffenseTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Charge ADD CONSTRAINT DispositionOffenseType_Charge_fk1
FOREIGN KEY (FiledOffenseTypeID)
REFERENCES DispositionOffenseType (DispositionOffenseTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE County ADD CONSTRAINT Region_County_fk
FOREIGN KEY (RegionID)
REFERENCES Region (RegionID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Incident ADD CONSTRAINT Town_Incident_fk
FOREIGN KEY (TownID)
REFERENCES Town (TownID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE OffenseType ADD CONSTRAINT ReadyCashOffenseCategory_OffenseType_fk
FOREIGN KEY (ReadyCashOffenseCategoryID)
REFERENCES ReadyCashOffenseCategory (ReadyCashOffenseCategoryID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Charge ADD CONSTRAINT InvolvedDrug_Charge_fk
FOREIGN KEY (InvolvedDrugID)
REFERENCES InvolvedDrug (InvolvedDrugID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE IncidentType ADD CONSTRAINT IncidentCategory_IncidentType_fk
FOREIGN KEY (IncidentCategoryID)
REFERENCES IncidentCategory (IncidentCategoryID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PersonAge ADD CONSTRAINT AgeRange_PersonAge_fk
FOREIGN KEY (PersonAgeRangeID)
REFERENCES PersonAgeRange (PersonAgeRangeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Population ADD CONSTRAINT AgeRange_Population_fk
FOREIGN KEY (PersonAgeRangeID)
REFERENCES PersonAgeRange (PersonAgeRangeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE IncidentTypeAssociation ADD CONSTRAINT IncidentType_IncidentTypeAssociation_fk
FOREIGN KEY (IncidentTypeID)
REFERENCES IncidentType (IncidentTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PretrialAssessedNeed ADD CONSTRAINT AssessedNeed_PretrialAssessedNeed_fk
FOREIGN KEY (AssessedNeedID)
REFERENCES AssessedNeed (AssessedNeedID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Arrest ADD CONSTRAINT PersonSex_ArrestCharge_fk
FOREIGN KEY (ArresteeSexID)
REFERENCES PersonSex (PersonSexID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Population ADD CONSTRAINT PersonSex_Population_fk
FOREIGN KEY (PersonSexID)
REFERENCES PersonSex (PersonSexID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Charge ADD CONSTRAINT Disposition_Charge_fk
FOREIGN KEY (DispositionID)
REFERENCES Disposition (DispositionID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Arrest ADD CONSTRAINT YesNo_ArrestCharge_fk
FOREIGN KEY (ArrestDrugRelated)
REFERENCES YesNo (YesNoID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Arrest ADD CONSTRAINT PretrialServiceEligible_fk
FOREIGN KEY (PretrialServiceEligible)
REFERENCES YesNo (YesNoID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PretrialServiceParticipation ADD CONSTRAINT YesNo_PretrialServiceParticipation_fk
FOREIGN KEY (PreChargeMonitoringIndicator)
REFERENCES YesNo (YesNoID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Arrest ADD CONSTRAINT YesNo_Arrest_fk
FOREIGN KEY (SixMonthRearrestIndicator)
REFERENCES YesNo (YesNoID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Arrest ADD CONSTRAINT YesNo_Arrest_fk1
FOREIGN KEY (OneYearRearrestIndicator)
REFERENCES YesNo (YesNoID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Arrest ADD CONSTRAINT YesNo_Arrest_fk2
FOREIGN KEY (TwoYearRearrestIndicator)
REFERENCES YesNo (YesNoID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Arrest ADD CONSTRAINT YesNo_Arrest_fk3
FOREIGN KEY (OneYearReconvictionIndicator)
REFERENCES YesNo (YesNoID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Arrest ADD CONSTRAINT YesNo_Arrest_fk4
FOREIGN KEY (TwoYearReconvictionIndicator)
REFERENCES YesNo (YesNoID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PretrialServiceParticipation ADD CONSTRAINT RiskScore_PretrialServiceParticipation_fk
FOREIGN KEY (RiskScoreID)
REFERENCES RiskScore (RiskScoreID)
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

ALTER TABLE Arrest ADD CONSTRAINT Agency_Arrest_fk
FOREIGN KEY (ArrestingAgencyID)
REFERENCES Agency (AgencyID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Arrest ADD CONSTRAINT County_Arrest_fk
FOREIGN KEY (CountyID)
REFERENCES County (CountyID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PretrialServiceParticipation ADD CONSTRAINT County_PretrialServiceParticipation_fk
FOREIGN KEY (CountyID)
REFERENCES County (CountyID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Incident ADD CONSTRAINT County_Incident_fk
FOREIGN KEY (CountyID)
REFERENCES County (CountyID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Population ADD CONSTRAINT County_Population_fk
FOREIGN KEY (CountyID)
REFERENCES County (CountyID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Arrest ADD CONSTRAINT PersonAge_Arrest_fk
FOREIGN KEY (ArresteeAgeID)
REFERENCES PersonAge (PersonAgeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Arrest ADD CONSTRAINT Race_Arrest_fk
FOREIGN KEY (ArresteeRaceID)
REFERENCES PersonRace (PersonRaceID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Population ADD CONSTRAINT PersonRace_Population_fk
FOREIGN KEY (PersonRaceID)
REFERENCES PersonRace (PersonRaceID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Charge ADD CONSTRAINT OffenseType_Charge_Arrest_fk
FOREIGN KEY (ArrestOffenseTypeID)
REFERENCES OffenseType (OffenseTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Incident ADD CONSTRAINT Time_Incident_fk
FOREIGN KEY (TimeID)
REFERENCES Time (TimeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Arrest ADD CONSTRAINT Time_Arrest_fk
FOREIGN KEY (TimeID)
REFERENCES Time (TimeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Incident ADD CONSTRAINT Date_Incident_fk
FOREIGN KEY (DateID)
REFERENCES Date (DateID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Arrest ADD CONSTRAINT Date_Arrest_fk
FOREIGN KEY (DateID)
REFERENCES Date (DateID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PretrialServiceParticipation ADD CONSTRAINT Date_PretrialServiceParticipation_fk
FOREIGN KEY (IntakeDateID)
REFERENCES Date (DateID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Charge ADD CONSTRAINT Date_Charge_fk
FOREIGN KEY (DispositionDateID)
REFERENCES Date (DateID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE IncidentTypeAssociation ADD CONSTRAINT Incident_IncidentTypeAssociation_fk
FOREIGN KEY (IncidentID)
REFERENCES Incident (IncidentID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Arrest ADD CONSTRAINT Incident_Arrest_fk
FOREIGN KEY (IncidentID)
REFERENCES Incident (IncidentID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE IncidentType2Association ADD CONSTRAINT Incident_IncidentType2Association_fk
FOREIGN KEY (IncidentID)
REFERENCES Incident (IncidentID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PretrialServiceParticipation ADD CONSTRAINT ArrestCharge_PretrialServiceParticipation_fk
FOREIGN KEY (ArrestID)
REFERENCES Arrest (ArrestID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Charge ADD CONSTRAINT ArrestCharge_Charge_fk
FOREIGN KEY (ArrestID)
REFERENCES Arrest (ArrestID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PretrialAssessedNeed ADD CONSTRAINT PretrialServiceParticipation_PretrialAssessedNeed_fk
FOREIGN KEY (PretrialServiceParticipationID)
REFERENCES PretrialServiceParticipation (PretrialServiceParticipationID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PretrialServiceAssociation ADD CONSTRAINT PretrialServiceParticipation_PretrialServiceAssociation_fk
FOREIGN KEY (PretrialServiceParticipationID)
REFERENCES PretrialServiceParticipation (PretrialServiceParticipationID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;