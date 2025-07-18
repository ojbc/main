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
drop schema if exists rapback_datastore;
CREATE schema rapback_datastore;
use rapback_datastore;

CREATE TABLE TOTP_USER (
                ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                USER_NAME VARCHAR(300),
                SECRET_KEY VARCHAR(300),
                VALIDATION_CODE INTEGER,
                SCRATCH_CODES VARCHAR(300),
                DATE_CREATED TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                PRIMARY KEY (ID)
);

CREATE TABLE AGENCY_EMAIL_CATEGORY (
                AGENCY_EMAIL_CATEGORY_ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                CODE VARCHAR(50) NOT NULL,
                DESCRIPTION VARCHAR(100) NOT NULL,
                CONSTRAINT AGENCY_EMAIL_CATEGORY_ID PRIMARY KEY (AGENCY_EMAIL_CATEGORY_ID)
);


CREATE TABLE TRIGGERING_EVENT (
                TRIGGERING_EVENT_ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                TRIGGERING_EVENT_CODE VARCHAR(30) NOT NULL,
                TRIGGERING_EVENT_DESC VARCHAR(50) NOT NULL,
                CONSTRAINT TRIGGERING_EVENT_ID PRIMARY KEY (TRIGGERING_EVENT_ID)
);


CREATE TABLE IDENTIFICATION_CATEGORY (
                IDENTIFICATION_CATEGORY_ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                IDENTIFICATION_CATEGORY_CODE VARCHAR(10) NOT NULL,
                IDENTIFICATION_CATEGORY_DESCRIPTION VARCHAR(100) NOT NULL,
                IDENTIFICATION_CATEGORY_TYPE VARCHAR(10) NOT NULL,
                CONSTRAINT IDENTIFICATION_CATEGORY_ID PRIMARY KEY (IDENTIFICATION_CATEGORY_ID)
);


CREATE TABLE AGENCY_PROFILE (
                AGENCY_ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                AGENCY_ORI VARCHAR(20) NOT NULL,
                AGENCY_NAME VARCHAR(100) NOT NULL,
                FBI_SUBSCRIPTION_QUALIFICATION BOOLEAN DEFAULT false NOT NULL,
                STATE_SUBSCRIPTION_QUALIFICATION BOOLEAN DEFAULT false NOT NULL,
                FIREARMS_SUBSCRIPTION_QUALIFICATION BOOLEAN DEFAULT false NOT NULL,
                CJ_EMPLOYMENT_SUBSCRIPTION_QUALIFICATION BOOLEAN DEFAULT false NOT NULL,
                CIVIL_AGENCY_INDICATOR BOOLEAN DEFAULT false NOT NULL,
                CONSTRAINT AGENCY_ID PRIMARY KEY (AGENCY_ID)
);


CREATE TABLE AGENCY_TRIGGERING_EVENT (
                AGENCY_TRIGGERING_EVENT_ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                AGENCY_ID INTEGER NOT NULL,
                TRIGGERING_EVENT_ID INTEGER NOT NULL,
                CONSTRAINT AGENCY_TRIGGERING_EVENT_ID PRIMARY KEY (AGENCY_TRIGGERING_EVENT_ID)
);


CREATE TABLE SUBSCRIPTION_OWNER (
                SUBSCRIPTION_OWNER_ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                FIRST_NAME VARCHAR(100) NOT NULL,
                LAST_NAME VARCHAR(100) NOT NULL,
                EMAIL_ADDRESS VARCHAR(100) NOT NULL,
                FEDERATION_ID VARCHAR(100) NOT NULL,
                AGENCY_ID INTEGER NOT NULL,
                CONSTRAINT SUBSCRIPTION_OWNER_ID PRIMARY KEY (SUBSCRIPTION_OWNER_ID)
);


CREATE TABLE OJBC_USER (
                OJBC_USER_ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                FEDERATION_ID VARCHAR(100) NOT NULL,
                AGENCY_ID INTEGER NOT NULL,
                SUPER_USER_INDICATOR BOOLEAN DEFAULT false NOT NULL,
                CONSTRAINT OJBC_USER_ID PRIMARY KEY (OJBC_USER_ID)
);


CREATE TABLE AGENCY_SUPER_USER (
                AGENCY_SUPER_USER_ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                SUPERVISED_AGENCY INTEGER NOT NULL,
                OJBC_USER_ID BIGINT NOT NULL,
                CONSTRAINT AGENCY_SUPER_USER_ID PRIMARY KEY (AGENCY_SUPER_USER_ID)
);


CREATE TABLE DEPARTMENT (
                DEPARTMENT_ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                DEPARTMENT_NAME VARCHAR(50) NOT NULL,
                AGENCY_ID INTEGER NOT NULL,
                CONSTRAINT DEPARTMENT_ID PRIMARY KEY (DEPARTMENT_ID)
);


CREATE TABLE JOB_TITLE (
                JOB_TITLE_ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                DEPARTMENT_ID INTEGER NOT NULL,
                TITLE_DESCRIPTION VARCHAR(50) NOT NULL,
                CONSTRAINT JOB_TITLE_ID PRIMARY KEY (JOB_TITLE_ID)
);


CREATE TABLE JOB_TITLE_PRIVILEGE (
                JOB_TITLE_PRIVILEGE_ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                JOB_TITLE_ID BIGINT NOT NULL,
                IDENTIFICATION_CATEGORY_ID INTEGER NOT NULL,
                CONSTRAINT JOB_TITLE_PRIVILEGE_ID PRIMARY KEY (JOB_TITLE_PRIVILEGE_ID)
);


CREATE TABLE AGENCY_CONTACT_EMAIL (
                AGENCY_CONTACT_EMAIL_ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                AGENCY_ID INTEGER NOT NULL,
                AGENCY_EMAIL VARCHAR(50) NOT NULL,
                CONSTRAINT AGENCY_CONTACT_EMAIL_ID PRIMARY KEY (AGENCY_CONTACT_EMAIL_ID)
);


CREATE TABLE AGENCY_CONTACT_EMAIL_JOINER (
                AGENCY_CONTACT_EMAIL_JOINER_ID INTEGER NOT NULL,
                AGENCY_CONTACT_EMAIL_ID INTEGER NOT NULL,
                AGENCY_EMAIL_CATEGORY_ID INTEGER NOT NULL,
                CONSTRAINT AGENCY_CONTACT_EMAIL_JOINER_ID PRIMARY KEY (AGENCY_CONTACT_EMAIL_JOINER_ID)
);


CREATE TABLE SUBSCRIPTION_CATEGORY (
                SUBSCRIPTION_CATEGORY_CODE VARCHAR(2) NOT NULL,
                SUBSCRIPTION_CATEGORY_DESCRIPTION VARCHAR(100) NOT NULL,
                CONSTRAINT SUBSCRIPTION_CATEGORY_CODE PRIMARY KEY (SUBSCRIPTION_CATEGORY_CODE)
);


CREATE TABLE RAP_BACK_SUBSCRIPTION_TERM (
                RAP_BACK_SUBSCRIPTION_TERM_CODE VARCHAR(1) NOT NULL,
                RAP_BACK_SUBSCRIPTION_TERM_DESC VARCHAR(50) NOT NULL,
                CONSTRAINT RAP_BACK_SUBSCRIPTION_TERM_CODE PRIMARY KEY (RAP_BACK_SUBSCRIPTION_TERM_CODE)
);


CREATE TABLE RAP_BACK_ACTIVITY_NOTIFICATION_FORMAT (
                RAP_BACK_ACTIVITY_NOTIFICATION_FORMAT_CODE VARCHAR(1) NOT NULL,
                RAP_BACK_ACTIVITY_NOTIFICATION_FORMAT_DESC VARCHAR(100) NOT NULL,
                CONSTRAINT RAP_BACK_ACTIVITY_NOTIFICATION_FORMAT_CODE PRIMARY KEY (RAP_BACK_ACTIVITY_NOTIFICATION_FORMAT_CODE)
);


CREATE TABLE RAP_BACK_CATEGORY (
                RAP_BACK_CATEGORY_CODE VARCHAR(2) NOT NULL,
                RAP_BACK_CATEGORY_DESCRIPTION VARCHAR(100) NOT NULL,
                CONSTRAINT RAP_BACK_CATEGORY_CODE PRIMARY KEY (RAP_BACK_CATEGORY_CODE)
);
COMMENT ON TABLE RAP_BACK_CATEGORY IS 'RAP_BACK_CATEGORY_CODE';


CREATE TABLE FINGER_PRINTS_TYPE (
                FINGER_PRINTS_TYPE_ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                FINGER_PRINTS_TYPE VARCHAR(30) NOT NULL,
                CONSTRAINT FINGER_PRINTS_TYPE_pk PRIMARY KEY (FINGER_PRINTS_TYPE_ID)
);
COMMENT ON TABLE FINGER_PRINTS_TYPE IS 'Indicate whether it is sent to FBI or State.';
COMMENT ON COLUMN FINGER_PRINTS_TYPE.FINGER_PRINTS_TYPE IS 'FBI or State';


CREATE TABLE RESULTS_SENDER (
                RESULTS_SENDER_ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                RESULTS_SENDER VARCHAR(20) NOT NULL,
                CONSTRAINT RESULTS_SENDER_pk PRIMARY KEY (RESULTS_SENDER_ID)
);


CREATE TABLE SUBSEQUENT_RESULTS (
                SUBSEQUENT_RESULT_ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                TRANSACTION_NUMBER VARCHAR(100) NOT NULL,
                RAP_SHEET BINARY VARYING(1000) NOT NULL,
                CIVIL_SID VARCHAR(100),
                UCN VARCHAR(100),
                RESULTS_SENDER_ID INTEGER NOT NULL,
                NOTIFICATION_INDICATOR BOOLEAN NOT NULL,
                CREATION_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
                REPORT_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
                CONSTRAINT SUBSEQUENT_RESULTS_pk PRIMARY KEY (SUBSEQUENT_RESULT_ID)
);

CREATE TABLE IDENTIFICATION_SUBJECT (
                SUBJECT_ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                UCN VARCHAR(100),
                CRIMINAL_SID VARCHAR(50),
                CIVIL_SID VARCHAR(50),
                FIRST_NAME VARCHAR(100) NOT NULL,
                LAST_NAME VARCHAR(100) NOT NULL,
                MIDDLE_INITIAL VARCHAR(30),
                SEX_CODE VARCHAR(1),
                DOB DATE,
                CONSTRAINT IDENTIFICATION_SUBJECT_pk PRIMARY KEY (SUBJECT_ID)
);
COMMENT ON COLUMN IDENTIFICATION_SUBJECT.UCN IS 'FBI ID';


CREATE TABLE SUBSCRIPTION (
                ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                TOPIC VARCHAR(100) NOT NULL,
                STATE VARCHAR(100),
                STARTDATE DATE NOT NULL,
                ENDDATE DATE DEFAULT NULL,
                CREATIONDATE DATE NOT NULL,
                LASTVALIDATIONDATE DATE DEFAULT NULL,
                VALIDATIONDUEDATE DATE,
                SUBSCRIBINGSYSTEMIDENTIFIER VARCHAR(100) NOT NULL,
                SUBJECTNAME VARCHAR(100) NOT NULL,
                AGENCY_CASE_NUMBER VARCHAR(100),
                ACTIVE TINYINT NOT NULL,
                SUBSCRIPTION_CATEGORY_CODE VARCHAR(2),
                SUBSCRIPTION_OWNER_ID INTEGER NOT NULL,
                TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                CONSTRAINT SUBSCRIPTION_pk PRIMARY KEY (ID)
);


CREATE UNIQUE INDEX CONSTRAINT_INDEX_9E
 ON SUBSCRIPTION
 ( ID ASC );

CREATE TABLE FBI_RAP_BACK_SUBSCRIPTION (
                FBI_SUBSCRIPTION_ID VARCHAR(100) NOT NULL,
                RAP_BACK_EXPIRATION_DATE DATE NOT NULL,
                RAP_BACK_START_DATE DATE NOT NULL,
                RAP_BACK_TERM_DATE DATE,
                RAP_BACK_OPT_OUT_IN_STATE_INDICATOR BOOLEAN NOT NULL,
                UCN VARCHAR(100) NOT NULL,
                RAP_BACK_CATEGORY_CODE VARCHAR(2) NOT NULL,
                RAP_BACK_ACTIVITY_NOTIFICATION_FORMAT_CODE VARCHAR(1) NOT NULL,
                RAP_BACK_SUBSCRIPTION_TERM_CODE VARCHAR(1),
                EVENT_IDENTIFIER VARCHAR(100),
                SUBSCRIPTION_ID INTEGER NOT NULL,
                CREATION_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
                REPORT_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
                CONSTRAINT FBI_RAP_BACK_SUBSCRIPTION_pk PRIMARY KEY (FBI_SUBSCRIPTION_ID)
);


CREATE TABLE SUBSCRIPTION_PROPERTIES (
                ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                SUBSCRIPTIONID INTEGER NOT NULL,
                PROPERTYNAME VARCHAR(100) NOT NULL,
                PROPERTYVALUE VARCHAR(356) NOT NULL,
                CONSTRAINT SUBSCRIPTION_PROPERTIES_pk PRIMARY KEY (ID)
);


CREATE TABLE IDENTIFICATION_TRANSACTION (
                TRANSACTION_NUMBER VARCHAR(100) NOT NULL,
                SUBJECT_ID INTEGER NOT NULL,
                OTN VARCHAR(100) NOT NULL,
                OWNER_ORI VARCHAR(20) NOT NULL,
                OWNER_PROGRAM_OCA VARCHAR(50) NOT NULL,
                IDENTIFICATION_CATEGORY VARCHAR(20) NOT NULL,
                FBI_SUBSCRIPTION_STATUS VARCHAR(20),
                ARCHIVED BOOLEAN DEFAULT false NOT NULL,
                SUBSCRIPTION_ID INTEGER,
                AVAILABLE_FOR_SUBSCRIPTION_START_DATE TIMESTAMP DEFAULT current_timestamp() NOT NULL,
                CREATION_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
                REPORT_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
                CONSTRAINT IDENTIFICATION_TRANSACTION_pk PRIMARY KEY (TRANSACTION_NUMBER)
);
COMMENT ON COLUMN IDENTIFICATION_TRANSACTION.OTN IS 'PersonTrackingIdentidication';
COMMENT ON COLUMN IDENTIFICATION_TRANSACTION.OWNER_PROGRAM_OCA IS '/nc:IdentificationID';

CREATE TABLE NSOR_FIVE_YEAR_CHECK (
                NSOR_FIVE_YEAR_CHECK_ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                TRANSACTION_NUMBER VARCHAR(100) NOT NULL,
                FIVE_YEAR_CHECK_FILE BINARY VARYING(1000),
                CREATION_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
                CONSTRAINT NSOR_FIVE_YEAR_CHECK_ID PRIMARY KEY (NSOR_FIVE_YEAR_CHECK_ID)
);

CREATE TABLE NSOR_SEARCH_RESULT (
                NSOR_SEARCH_RESULT_ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                SEARCH_RESULT_FILE BINARY VARYING(1000),
                TRANSACTION_NUMBER VARCHAR(100) NOT NULL,
                RESULTS_SENDER_ID INTEGER NOT NULL,
                CREATION_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
                REPORT_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
                CONSTRAINT NSOR_SEARCH_RESULT_ID PRIMARY KEY (NSOR_SEARCH_RESULT_ID)
);

CREATE TABLE NSOR_DEMOGRAPHICS (
                NSOR_DEMOGRAPHICS_ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                DEMOGRAPHICS_FILE BINARY VARYING(1000),
                TRANSACTION_NUMBER VARCHAR(100) NOT NULL,
                RESULTS_SENDER_ID INTEGER NOT NULL,
                CREATION_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
                REPORT_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
                CONSTRAINT NSOR_DEMOGRAPHICS_ID PRIMARY KEY (NSOR_DEMOGRAPHICS_ID)
);

CREATE TABLE CIVIL_FINGER_PRINTS (
                FINGER_PRINTS_ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                TRANSACTION_NUMBER VARCHAR(100) NOT NULL,
                FINGER_PRINTS_FILE BINARY VARYING(1000) NOT NULL,
                FINGER_PRINTS_TYPE_ID INTEGER NOT NULL,
                REPORT_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
                CONSTRAINT CIVIL_FINGER_PRINTS_pk PRIMARY KEY (FINGER_PRINTS_ID)
);


CREATE TABLE CIVIL_INITIAL_RESULTS (
                CIVIL_INITIAL_RESULT_ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                SEARCH_RESULT_FILE BINARY VARYING(1000),
                TRANSACTION_NUMBER VARCHAR(100) NOT NULL,
                RESULTS_SENDER_ID INTEGER NOT NULL,
                CREATION_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
                REPORT_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
                CONSTRAINT CIVIL_INITIAL_RESULTS_pk PRIMARY KEY (CIVIL_INITIAL_RESULT_ID)
);
COMMENT ON COLUMN CIVIL_INITIAL_RESULTS.SEARCH_RESULT_FILE IS 'The first initial results indicate whethe there is a match.';


CREATE TABLE CIVIL_INITIAL_RAP_SHEET (
                CIVIL_RAPSHEET_ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                CIVIL_INITIAL_RESULT_ID INTEGER NOT NULL,
                RAP_SHEET BINARY VARYING(1000) NOT NULL,
                CREATION_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
                TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
                CONSTRAINT CIVIL_INITIAL_RAP_SHEET_pk PRIMARY KEY (CIVIL_RAPSHEET_ID)
);


CREATE TABLE CRIMINAL_INITIAL_RESULTS (
                CRIMINAL_INITIAL_RESULT_ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                TRANSACTION_NUMBER VARCHAR(100) NOT NULL,
                SEARCH_RESULT_FILE BINARY VARYING(1000),
                RESULTS_SENDER_ID INTEGER NOT NULL,
                CREATION_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
                REPORT_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
                CONSTRAINT CRIMINAL_INITIAL_RESULTS_pk PRIMARY KEY (CRIMINAL_INITIAL_RESULT_ID)
);
COMMENT ON COLUMN CRIMINAL_INITIAL_RESULTS.SEARCH_RESULT_FILE IS 'The text file telling whether there is a match.';


CREATE TABLE SUBSCRIPTION_SUBJECT_IDENTIFIER (
                ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                SUBSCRIPTIONID INTEGER NOT NULL,
                IDENTIFIERNAME VARCHAR(100) NOT NULL,
                IDENTIFIERVALUE VARCHAR(256) NOT NULL,
                CONSTRAINT SUBSCRIPTION_SUBJECT_IDENTIFIER_pk PRIMARY KEY (ID)
);


CREATE INDEX CONSTRAINT_INDEX_B
 ON SUBSCRIPTION_SUBJECT_IDENTIFIER
 ( SUBSCRIPTIONID ASC );

CREATE TABLE NOTIFICATION_MECHANISM (
                ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                SUBSCRIPTIONID INTEGER NOT NULL,
                NOTIFICATIONMECHANISMTYPE VARCHAR(20) NOT NULL,
                NOTIFICATIONADDRESS VARCHAR(256) NOT NULL,
                CONSTRAINT NOTIFICATION_MECHANISM_pk PRIMARY KEY (ID)
);


CREATE UNIQUE INDEX CONSTRAINT_INDEX_9
 ON NOTIFICATION_MECHANISM
 ( SUBSCRIPTIONID ASC, NOTIFICATIONADDRESS ASC );

CREATE INDEX CONSTRAINT_INDEX_94
 ON NOTIFICATION_MECHANISM
 ( SUBSCRIPTIONID ASC );

ALTER TABLE AGENCY_CONTACT_EMAIL_JOINER ADD CONSTRAINT AGENCY_EMAIL_CATEGORY_AGENCY_CONTACT_EMAIL_JOINER_fk
FOREIGN KEY (AGENCY_EMAIL_CATEGORY_ID)
REFERENCES AGENCY_EMAIL_CATEGORY (AGENCY_EMAIL_CATEGORY_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE AGENCY_TRIGGERING_EVENT ADD CONSTRAINT TRIGGERING_EVENT_AGENCY_TRIGGERING_EVENT_fk
FOREIGN KEY (TRIGGERING_EVENT_ID)
REFERENCES TRIGGERING_EVENT (TRIGGERING_EVENT_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE JOB_TITLE_PRIVILEGE ADD CONSTRAINT IDENTIFICATION_CATEGORY_JOB_TITLE_PRIVILEGE_fk
FOREIGN KEY (IDENTIFICATION_CATEGORY_ID)
REFERENCES IDENTIFICATION_CATEGORY (IDENTIFICATION_CATEGORY_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE AGENCY_CONTACT_EMAIL ADD CONSTRAINT AGENCY_PROFILE_AGENCY_CONTACT_EMAIL_fk
FOREIGN KEY (AGENCY_ID)
REFERENCES AGENCY_PROFILE (AGENCY_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE DEPARTMENT ADD CONSTRAINT AGENCY_PROFILE_DEPARTMENT_fk
FOREIGN KEY (AGENCY_ID)
REFERENCES AGENCY_PROFILE (AGENCY_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE OJBC_USER ADD CONSTRAINT AGENCY_PROFILE_OJBC_USER_fk
FOREIGN KEY (AGENCY_ID)
REFERENCES AGENCY_PROFILE (AGENCY_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE AGENCY_SUPER_USER ADD CONSTRAINT AGENCY_PROFILE_AGENCY_SUPER_USER_fk
FOREIGN KEY (SUPERVISED_AGENCY)
REFERENCES AGENCY_PROFILE (AGENCY_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE SUBSCRIPTION_OWNER ADD CONSTRAINT AGENCY_PROFILE_SUBSCRIPTION_OWNER_fk
FOREIGN KEY (AGENCY_ID)
REFERENCES AGENCY_PROFILE (AGENCY_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE AGENCY_TRIGGERING_EVENT ADD CONSTRAINT AGENCY_PROFILE_AGENCY_TRIGGERING_EVENT_fk
FOREIGN KEY (AGENCY_ID)
REFERENCES AGENCY_PROFILE (AGENCY_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE SUBSCRIPTION ADD CONSTRAINT SUBSCRIPTION_OWNER_SUBSCRIPTION_fk
FOREIGN KEY (SUBSCRIPTION_OWNER_ID)
REFERENCES SUBSCRIPTION_OWNER (SUBSCRIPTION_OWNER_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE AGENCY_SUPER_USER ADD CONSTRAINT OJBC_USER_AGENCY_SUPER_USER_fk
FOREIGN KEY (OJBC_USER_ID)
REFERENCES OJBC_USER (OJBC_USER_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE JOB_TITLE ADD CONSTRAINT DEPARTMENT_TITLE_fk
FOREIGN KEY (DEPARTMENT_ID)
REFERENCES DEPARTMENT (DEPARTMENT_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE JOB_TITLE_PRIVILEGE ADD CONSTRAINT JOB_TITLE_JOB_TITLE_PRIVILEGE_fk
FOREIGN KEY (JOB_TITLE_ID)
REFERENCES JOB_TITLE (JOB_TITLE_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE AGENCY_CONTACT_EMAIL_JOINER ADD CONSTRAINT AGENCY_CONTACT_EMAIL_AGENCY_CONTACT_EMAIL_JOINER_fk
FOREIGN KEY (AGENCY_CONTACT_EMAIL_ID)
REFERENCES AGENCY_CONTACT_EMAIL (AGENCY_CONTACT_EMAIL_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE SUBSCRIPTION ADD CONSTRAINT SUBSCRIPTION_CATEGORY_SUBSCRIPTION_fk
FOREIGN KEY (SUBSCRIPTION_CATEGORY_CODE)
REFERENCES SUBSCRIPTION_CATEGORY (SUBSCRIPTION_CATEGORY_CODE)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE FBI_RAP_BACK_SUBSCRIPTION ADD CONSTRAINT RAP_BACK_SUBSCRIPTION_TERM_FBI_RAP_BACK_SUBSCRIPTION_fk
FOREIGN KEY (RAP_BACK_SUBSCRIPTION_TERM_CODE)
REFERENCES RAP_BACK_SUBSCRIPTION_TERM (RAP_BACK_SUBSCRIPTION_TERM_CODE)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE FBI_RAP_BACK_SUBSCRIPTION ADD CONSTRAINT rap_back_activity_not_format_fbi_rap_back_sub_fk
FOREIGN KEY (RAP_BACK_ACTIVITY_NOTIFICATION_FORMAT_CODE)
REFERENCES RAP_BACK_ACTIVITY_NOTIFICATION_FORMAT (RAP_BACK_ACTIVITY_NOTIFICATION_FORMAT_CODE)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE FBI_RAP_BACK_SUBSCRIPTION ADD CONSTRAINT RAP_BACK_CATEGORY_CODE_FBI_RAP_BACK_SUBSCRIPTION_fk
FOREIGN KEY (RAP_BACK_CATEGORY_CODE)
REFERENCES RAP_BACK_CATEGORY (RAP_BACK_CATEGORY_CODE)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE CIVIL_FINGER_PRINTS ADD CONSTRAINT FINGER_PRINTS_TYPE_CIVIL_FINGER_PRINTS_fk
FOREIGN KEY (FINGER_PRINTS_TYPE_ID)
REFERENCES FINGER_PRINTS_TYPE (FINGER_PRINTS_TYPE_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE CIVIL_INITIAL_RESULTS ADD CONSTRAINT RESULTS_SENDER_CIVIL_INITIAL_RESULTS_fk
FOREIGN KEY (RESULTS_SENDER_ID)
REFERENCES RESULTS_SENDER (RESULTS_SENDER_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE CRIMINAL_INITIAL_RESULTS ADD CONSTRAINT RESULTS_SENDER_CRIMINAL_INITIAL_RESULTS_fk
FOREIGN KEY (RESULTS_SENDER_ID)
REFERENCES RESULTS_SENDER (RESULTS_SENDER_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE SUBSEQUENT_RESULTS ADD CONSTRAINT RESULTS_SENDER_SUBSEQUENT_RESULTS_fk
FOREIGN KEY (RESULTS_SENDER_ID)
REFERENCES RESULTS_SENDER (RESULTS_SENDER_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE NSOR_DEMOGRAPHICS ADD CONSTRAINT RESULTS_SENDER_NSOR_DEMOGRAPHICS_fk
FOREIGN KEY (RESULTS_SENDER_ID)
REFERENCES RESULTS_SENDER (RESULTS_SENDER_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE NSOR_SEARCH_RESULT ADD CONSTRAINT RESULTS_SENDER_NSOR_SEARCH_RESULT_fk
FOREIGN KEY (RESULTS_SENDER_ID)
REFERENCES RESULTS_SENDER (RESULTS_SENDER_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE IDENTIFICATION_TRANSACTION ADD CONSTRAINT FBI_RAP_BACK_SUBJECT_IDENTIFICATION_TRANSACTION_fk
FOREIGN KEY (SUBJECT_ID)
REFERENCES IDENTIFICATION_SUBJECT (SUBJECT_ID)
ON DELETE CASCADE
ON UPDATE CASCADE;

ALTER TABLE NOTIFICATION_MECHANISM ADD CONSTRAINT CONSTRAINT_94
FOREIGN KEY (SUBSCRIPTIONID)
REFERENCES SUBSCRIPTION (ID)
ON DELETE CASCADE
ON UPDATE CASCADE;

ALTER TABLE SUBSCRIPTION_SUBJECT_IDENTIFIER ADD CONSTRAINT CONSTRAINT_B
FOREIGN KEY (SUBSCRIPTIONID)
REFERENCES SUBSCRIPTION (ID)
ON DELETE CASCADE
ON UPDATE CASCADE;

ALTER TABLE IDENTIFICATION_TRANSACTION ADD CONSTRAINT SUBSCRIPTION_IDENTIFICATION_TRANSACTION_fk
FOREIGN KEY (SUBSCRIPTION_ID)
REFERENCES SUBSCRIPTION (ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE SUBSCRIPTION_PROPERTIES ADD CONSTRAINT SUBSCRIPTION_SUBSCRIPTION_PROPERTIES_fk
FOREIGN KEY (SUBSCRIPTIONID)
REFERENCES SUBSCRIPTION (ID)
ON DELETE CASCADE
ON UPDATE CASCADE;

ALTER TABLE FBI_RAP_BACK_SUBSCRIPTION ADD CONSTRAINT SUBSCRIPTION_FBI_RAP_BACK_SUBSCRIPTION_fk
FOREIGN KEY (SUBSCRIPTION_ID)
REFERENCES SUBSCRIPTION (ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE CRIMINAL_INITIAL_RESULTS ADD CONSTRAINT IDENTIFICATION_TRANSACTION_CRIMINAL_INITIAL_RESULTS_fk
FOREIGN KEY (TRANSACTION_NUMBER)
REFERENCES IDENTIFICATION_TRANSACTION (TRANSACTION_NUMBER)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE CIVIL_INITIAL_RESULTS ADD CONSTRAINT IDENTIFICATION_TRANSACTION_CIVIL_INITIAL_RESULTS_fk
FOREIGN KEY (TRANSACTION_NUMBER)
REFERENCES IDENTIFICATION_TRANSACTION (TRANSACTION_NUMBER)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE CIVIL_FINGER_PRINTS ADD CONSTRAINT IDENTIFICATION_TRANSACTION_CIVIL_NISTS_fk
FOREIGN KEY (TRANSACTION_NUMBER)
REFERENCES IDENTIFICATION_TRANSACTION (TRANSACTION_NUMBER)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE NSOR_DEMOGRAPHICS ADD CONSTRAINT IDENTIFICATION_TRANSACTION_NSOR_DEMOGRAPHICS_fk
FOREIGN KEY (TRANSACTION_NUMBER)
REFERENCES IDENTIFICATION_TRANSACTION (TRANSACTION_NUMBER)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE NSOR_SEARCH_RESULT ADD CONSTRAINT IDENTIFICATION_TRANSACTION_NSOR_SEARCH_RESULT_fk
FOREIGN KEY (TRANSACTION_NUMBER)
REFERENCES IDENTIFICATION_TRANSACTION (TRANSACTION_NUMBER)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE NSOR_FIVE_YEAR_CHECK ADD CONSTRAINT IDENTIFICATION_TRANSACTION_NSOR_FIVE_YEAR_CHECK_fk
FOREIGN KEY (TRANSACTION_NUMBER)
REFERENCES IDENTIFICATION_TRANSACTION (TRANSACTION_NUMBER)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE CIVIL_INITIAL_RAP_SHEET ADD CONSTRAINT CIVIL_INITIAL_RESULTS_CIVIL_INITIAL_RAP_SHEET_fk
FOREIGN KEY (CIVIL_INITIAL_RESULT_ID)
REFERENCES CIVIL_INITIAL_RESULTS (CIVIL_INITIAL_RESULT_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;