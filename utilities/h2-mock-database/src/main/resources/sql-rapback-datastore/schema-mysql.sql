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
/*
 * You need to make these changes when generating from SQL PA:
 * You need to change BINARY to MEDIUMBLOB
 * For report_timestamps, use an example like this:  REPORT_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP,
 * For creation_timestamps, use an example like this:  CREATION_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
 * 
 */

drop schema if exists rapback_datastore;
CREATE schema rapback_datastore;
use rapback_datastore;



CREATE TABLE AGENCY_EMAIL_CATEGORY (
                AGENCY_EMAIL_CATEGORY_ID INT AUTO_INCREMENT NOT NULL,
                CODE VARCHAR(50) NOT NULL,
                DESCRIPTION VARCHAR(100) NOT NULL,
                PRIMARY KEY (AGENCY_EMAIL_CATEGORY_ID)
);


CREATE TABLE TRIGGERING_EVENT (
                TRIGGERING_EVENT_ID INT AUTO_INCREMENT NOT NULL,
                TRIGGERING_EVENT_CODE VARCHAR(30) NOT NULL,
                TRIGGERING_EVENT_DESC VARCHAR(50) NOT NULL,
                PRIMARY KEY (TRIGGERING_EVENT_ID)
);


CREATE TABLE IDENTIFICATION_CATEGORY (
                IDENTIFICATION_CATEGORY_ID INT AUTO_INCREMENT NOT NULL,
                IDENTIFICATION_CATEGORY_CODE VARCHAR(10) NOT NULL,
                IDENTIFICATION_CATEGORY_DESCRIPTION VARCHAR(100) NOT NULL,
                IDENTIFICATION_CATEGORY_TYPE VARCHAR(10) NOT NULL,
                PRIMARY KEY (IDENTIFICATION_CATEGORY_ID)
);


CREATE TABLE AGENCY_PROFILE (
                AGENCY_ID INT AUTO_INCREMENT NOT NULL,
                AGENCY_ORI VARCHAR(20) NOT NULL,
                AGENCY_NAME VARCHAR(100) NOT NULL,
                FBI_SUBSCRIPTION_QUALIFICATION BOOLEAN DEFAULT false NOT NULL,
                STATE_SUBSCRIPTION_QUALIFICATION BOOLEAN DEFAULT false NOT NULL,
                FIREARMS_SUBSCRIPTION_QUALIFICATION BOOLEAN DEFAULT false NOT NULL,
                CJ_EMPLOYMENT_SUBSCRIPTION_QUALIFICATION BOOLEAN DEFAULT false NOT NULL,
                CIVIL_AGENCY_INDICATOR BOOLEAN DEFAULT false NOT NULL,
                PRIMARY KEY (AGENCY_ID)
);


CREATE TABLE AGENCY_TRIGGERING_EVENT (
                AGENCY_TRIGGERING_EVENT_ID INT AUTO_INCREMENT NOT NULL,
                AGENCY_ID INT NOT NULL,
                TRIGGERING_EVENT_ID INT NOT NULL,
                PRIMARY KEY (AGENCY_TRIGGERING_EVENT_ID)
);


CREATE TABLE SUBSCRIPTION_OWNER (
                SUBSCRIPTION_OWNER_ID INT AUTO_INCREMENT NOT NULL,
                FIRST_NAME VARCHAR(100) NOT NULL,
                LAST_NAME VARCHAR(100) NOT NULL,
                EMAIL_ADDRESS VARCHAR(100) NOT NULL,
                FEDERATION_ID VARCHAR(100) NOT NULL,
                AGENCY_ID INT NOT NULL,
                PRIMARY KEY (SUBSCRIPTION_OWNER_ID)
);


CREATE TABLE OJBC_USER (
                OJBC_USER_ID BIGINT AUTO_INCREMENT NOT NULL,
                FEDERATION_ID VARCHAR(100) NOT NULL,
                AGENCY_ID INT NOT NULL,
                SUPER_USER_INDICATOR BOOLEAN DEFAULT false NOT NULL,
                PRIMARY KEY (OJBC_USER_ID)
);


CREATE TABLE AGENCY_SUPER_USER (
                AGENCY_SUPER_USER_ID INT AUTO_INCREMENT NOT NULL,
                SUPERVISED_AGENCY INT NOT NULL,
                OJBC_USER_ID BIGINT NOT NULL,
                PRIMARY KEY (AGENCY_SUPER_USER_ID)
);


CREATE TABLE DEPARTMENT (
                DEPARTMENT_ID INT AUTO_INCREMENT NOT NULL,
                DEPARTMENT_NAME VARCHAR(50) NOT NULL,
                AGENCY_ID INT NOT NULL,
                PRIMARY KEY (DEPARTMENT_ID)
);


CREATE TABLE JOB_TITLE (
                JOB_TITLE_ID BIGINT AUTO_INCREMENT NOT NULL,
                DEPARTMENT_ID INT NOT NULL,
                TITLE_DESCRIPTION VARCHAR(50) NOT NULL,
                PRIMARY KEY (JOB_TITLE_ID)
);


CREATE TABLE JOB_TITLE_PRIVILEGE (
                JOB_TITLE_PRIVILEGE_ID INT AUTO_INCREMENT NOT NULL,
                JOB_TITLE_ID BIGINT NOT NULL,
                IDENTIFICATION_CATEGORY_ID INT NOT NULL,
                PRIMARY KEY (JOB_TITLE_PRIVILEGE_ID)
);


CREATE TABLE AGENCY_CONTACT_EMAIL (
                AGENCY_CONTACT_EMAIL_ID INT AUTO_INCREMENT NOT NULL,
                AGENCY_ID INT NOT NULL,
                AGENCY_EMAIL VARCHAR(50) NOT NULL,
                PRIMARY KEY (AGENCY_CONTACT_EMAIL_ID)
);


CREATE TABLE AGENCY_CONTACT_EMAIL_JOINER (
                AGENCY_CONTACT_EMAIL_JOINER_ID INT NOT NULL,
                AGENCY_CONTACT_EMAIL_ID INT NOT NULL,
                AGENCY_EMAIL_CATEGORY_ID INT NOT NULL,
                PRIMARY KEY (AGENCY_CONTACT_EMAIL_JOINER_ID)
);


CREATE TABLE SUBSCRIPTION_CATEGORY (
                SUBSCRIPTION_CATEGORY_CODE VARCHAR(2) NOT NULL,
                SUBSCRIPTION_CATEGORY_DESCRIPTION VARCHAR(100) NOT NULL,
                PRIMARY KEY (SUBSCRIPTION_CATEGORY_CODE)
);


CREATE TABLE RAP_BACK_SUBSCRIPTION_TERM (
                RAP_BACK_SUBSCRIPTION_TERM_CODE VARCHAR(1) NOT NULL,
                RAP_BACK_SUBSCRIPTION_TERM_DESC VARCHAR(50) NOT NULL,
                PRIMARY KEY (RAP_BACK_SUBSCRIPTION_TERM_CODE)
);


CREATE TABLE RAP_BACK_ACTIVITY_NOTIFICATION_FORMAT (
                RAP_BACK_ACTIVITY_NOTIFICATION_FORMAT_CODE VARCHAR(1) NOT NULL,
                RAP_BACK_ACTIVITY_NOTIFICATION_FORMAT_DESC VARCHAR(100) NOT NULL,
                PRIMARY KEY (RAP_BACK_ACTIVITY_NOTIFICATION_FORMAT_CODE)
);


CREATE TABLE RAP_BACK_CATEGORY (
                RAP_BACK_CATEGORY_CODE VARCHAR(2) NOT NULL,
                RAP_BACK_CATEGORY_DESCRIPTION VARCHAR(100) NOT NULL,
                PRIMARY KEY (RAP_BACK_CATEGORY_CODE)
);

ALTER TABLE RAP_BACK_CATEGORY COMMENT 'RAP_BACK_CATEGORY_CODE';


CREATE TABLE FINGER_PRINTS_TYPE (
                FINGER_PRINTS_TYPE_ID INT AUTO_INCREMENT NOT NULL,
                FINGER_PRINTS_TYPE VARCHAR(30) NOT NULL,
                PRIMARY KEY (FINGER_PRINTS_TYPE_ID)
);

ALTER TABLE FINGER_PRINTS_TYPE COMMENT 'Indicate whether it is sent to FBI or State.';

ALTER TABLE FINGER_PRINTS_TYPE MODIFY COLUMN FINGER_PRINTS_TYPE VARCHAR(30) COMMENT 'FBI or State';


CREATE TABLE RESULTS_SENDER (
                RESULTS_SENDER_ID INT AUTO_INCREMENT NOT NULL,
                RESULTS_SENDER VARCHAR(20) NOT NULL,
                PRIMARY KEY (RESULTS_SENDER_ID)
);


CREATE TABLE SUBSEQUENT_RESULTS (
                SUBSEQUENT_RESULT_ID INT AUTO_INCREMENT NOT NULL,
                TRANSACTION_NUMBER VARCHAR(100) NOT NULL,
                RAP_SHEET MEDIUMBLOB NOT NULL,
                CIVIL_SID VARCHAR(100),
                UCN VARCHAR(100),
                RESULTS_SENDER_ID INT NOT NULL,
                NOTIFICATION_INDICATOR BOOLEAN NOT NULL,
                CREATION_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                REPORT_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
                PRIMARY KEY (SUBSEQUENT_RESULT_ID)
);

CREATE INDEX subsequent_results_idx
 ON SUBSEQUENT_RESULTS
 ( TRANSACTION_NUMBER );

CREATE TABLE IDENTIFICATION_SUBJECT (
                SUBJECT_ID INT AUTO_INCREMENT NOT NULL,
                UCN VARCHAR(100),
                CRIMINAL_SID VARCHAR(50),
                CIVIL_SID VARCHAR(50),
                FIRST_NAME VARCHAR(100) NOT NULL,
                LAST_NAME VARCHAR(100) NOT NULL,
                MIDDLE_INITIAL VARCHAR(30),
                SEX_CODE VARCHAR(1),
                DOB DATE,
                PRIMARY KEY (SUBJECT_ID)
);

ALTER TABLE IDENTIFICATION_SUBJECT MODIFY COLUMN UCN VARCHAR(100) COMMENT 'FBI ID';


CREATE TABLE SUBSCRIPTION (
                ID INT AUTO_INCREMENT NOT NULL,
                TOPIC VARCHAR(100) NOT NULL,
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
                SUBSCRIPTION_OWNER_ID INT NOT NULL,
                TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
                PRIMARY KEY (ID)
);


CREATE UNIQUE INDEX constraint_index_9e
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
                SUBSCRIPTION_ID INT NOT NULL,
                CREATION_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                REPORT_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
                PRIMARY KEY (FBI_SUBSCRIPTION_ID)
);


CREATE TABLE SUBSCRIPTION_PROPERTIES (
                ID INT AUTO_INCREMENT NOT NULL,
                SUBSCRIPTIONID INT NOT NULL,
                PROPERTYNAME VARCHAR(100) NOT NULL,
                PROPERTYVALUE VARCHAR(356) NOT NULL,
                PRIMARY KEY (ID)
);


CREATE TABLE IDENTIFICATION_TRANSACTION (
                TRANSACTION_NUMBER VARCHAR(100) NOT NULL,
                SUBJECT_ID INT NOT NULL,
                OTN VARCHAR(100) NOT NULL,
                OWNER_ORI VARCHAR(20) NOT NULL,
                OWNER_PROGRAM_OCA VARCHAR(50) NOT NULL,
                IDENTIFICATION_CATEGORY VARCHAR(20) NOT NULL,
                FBI_SUBSCRIPTION_STATUS VARCHAR(20),
                ARCHIVED BOOLEAN DEFAULT false NOT NULL,
                SUBSCRIPTION_ID INT,
                AVAILABLE_FOR_SUBSCRIPTION_START_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                CREATION_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                REPORT_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
                PRIMARY KEY (TRANSACTION_NUMBER)
);

ALTER TABLE IDENTIFICATION_TRANSACTION MODIFY COLUMN OTN VARCHAR(100) COMMENT 'PersonTrackingIdentidication';

ALTER TABLE IDENTIFICATION_TRANSACTION MODIFY COLUMN OWNER_PROGRAM_OCA VARCHAR(50) COMMENT '/nc:IdentificationID';


CREATE TABLE NSOR_FIVE_YEAR_CHECK (
                NSOR_FIVE_YEAR_CHECK_ID INT AUTO_INCREMENT NOT NULL,
                TRANSACTION_NUMBER VARCHAR(100) NOT NULL,
                FIVE_YEAR_CHECK_FILE MEDIUMBLOB,
                CREATION_TIMESTAMP DATETIME DEFAULT CURRENT_TIMESTAMP() NOT NULL,
                PRIMARY KEY (NSOR_FIVE_YEAR_CHECK_ID)
);

CREATE TABLE NSOR_SEARCH_RESULT (
                NSOR_SEARCH_RESULT_ID INT AUTO_INCREMENT NOT NULL,
                SEARCH_RESULT_FILE MEDIUMBLOB,
                TRANSACTION_NUMBER VARCHAR(100) NOT NULL,
                RESULTS_SENDER_ID INT NOT NULL,
                CREATION_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                REPORT_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
                PRIMARY KEY (NSOR_SEARCH_RESULT_ID)
);

CREATE TABLE NSOR_DEMOGRAPHICS (
                NSOR_DEMOGRAPHICS_ID INT AUTO_INCREMENT NOT NULL,
                DEMOGRAPHICS_FILE MEDIUMBLOB,
                TRANSACTION_NUMBER VARCHAR(100) NOT NULL,
                RESULTS_SENDER_ID INT NOT NULL,
                CREATION_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                REPORT_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
                PRIMARY KEY (NSOR_DEMOGRAPHICS_ID)
);

CREATE TABLE CIVIL_FINGER_PRINTS (
                FINGER_PRINTS_ID INT AUTO_INCREMENT NOT NULL,
                TRANSACTION_NUMBER VARCHAR(100) NOT NULL,
                FINGER_PRINTS_FILE MEDIUMBLOB NOT NULL,
                FINGER_PRINTS_TYPE_ID INT NOT NULL,
                REPORT_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
                PRIMARY KEY (FINGER_PRINTS_ID)
);


CREATE TABLE CIVIL_INITIAL_RESULTS (
                CIVIL_INITIAL_RESULT_ID INT AUTO_INCREMENT NOT NULL,
                SEARCH_RESULT_FILE MEDIUMBLOB,
                TRANSACTION_NUMBER VARCHAR(100) NOT NULL,
                RESULTS_SENDER_ID INT NOT NULL,
                CREATION_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                REPORT_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
                PRIMARY KEY (CIVIL_INITIAL_RESULT_ID)
);

ALTER TABLE CIVIL_INITIAL_RESULTS MODIFY COLUMN SEARCH_RESULT_FILE MEDIUMBLOB COMMENT 'The first initial results indicate whethe there is a match.';


CREATE TABLE CIVIL_INITIAL_RAP_SHEET (
                CIVIL_RAPSHEET_ID INT AUTO_INCREMENT NOT NULL,
                CIVIL_INITIAL_RESULT_ID INT NOT NULL,
                RAP_SHEET MEDIUMBLOB NOT NULL,
                CREATION_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
                PRIMARY KEY (CIVIL_RAPSHEET_ID)
);


CREATE TABLE CRIMINAL_INITIAL_RESULTS (
                CRIMINAL_INITIAL_RESULT_ID INT AUTO_INCREMENT NOT NULL,
                TRANSACTION_NUMBER VARCHAR(100) NOT NULL,
                SEARCH_RESULT_FILE MEDIUMBLOB,
                RESULTS_SENDER_ID INT NOT NULL,
                REPORT_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
                CREATION_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                PRIMARY KEY (CRIMINAL_INITIAL_RESULT_ID)
);

ALTER TABLE CRIMINAL_INITIAL_RESULTS MODIFY COLUMN SEARCH_RESULT_FILE MEDIUMBLOB COMMENT 'The text file telling whether there is a match.';


CREATE TABLE SUBSCRIPTION_SUBJECT_IDENTIFIER (
                ID INT AUTO_INCREMENT NOT NULL,
                SUBSCRIPTIONID INT NOT NULL,
                IDENTIFIERNAME VARCHAR(100) NOT NULL,
                IDENTIFIERVALUE VARCHAR(256) NOT NULL,
                PRIMARY KEY (ID)
);


CREATE INDEX constraint_index_b
 ON SUBSCRIPTION_SUBJECT_IDENTIFIER
 ( SUBSCRIPTIONID ASC );

CREATE TABLE NOTIFICATION_MECHANISM (
                ID INT AUTO_INCREMENT NOT NULL,
                SUBSCRIPTIONID INT NOT NULL,
                NOTIFICATIONMECHANISMTYPE VARCHAR(20) NOT NULL,
                NOTIFICATIONADDRESS VARCHAR(256) NOT NULL,
                PRIMARY KEY (ID)
);


CREATE UNIQUE INDEX constraint_index_9
 ON NOTIFICATION_MECHANISM
 ( SUBSCRIPTIONID ASC, NOTIFICATIONADDRESS ASC );

CREATE INDEX constraint_index_94
 ON NOTIFICATION_MECHANISM
 ( SUBSCRIPTIONID ASC );

ALTER TABLE AGENCY_CONTACT_EMAIL_JOINER ADD CONSTRAINT agency_email_category_agency_contact_email_joiner_fk
FOREIGN KEY (AGENCY_EMAIL_CATEGORY_ID)
REFERENCES AGENCY_EMAIL_CATEGORY (AGENCY_EMAIL_CATEGORY_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE AGENCY_TRIGGERING_EVENT ADD CONSTRAINT triggering_event_agency_triggering_event_fk
FOREIGN KEY (TRIGGERING_EVENT_ID)
REFERENCES TRIGGERING_EVENT (TRIGGERING_EVENT_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE JOB_TITLE_PRIVILEGE ADD CONSTRAINT identification_category_job_title_privilege_fk
FOREIGN KEY (IDENTIFICATION_CATEGORY_ID)
REFERENCES IDENTIFICATION_CATEGORY (IDENTIFICATION_CATEGORY_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE AGENCY_CONTACT_EMAIL ADD CONSTRAINT agency_profile_agency_contact_email_fk
FOREIGN KEY (AGENCY_ID)
REFERENCES AGENCY_PROFILE (AGENCY_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE DEPARTMENT ADD CONSTRAINT agency_profile_department_fk
FOREIGN KEY (AGENCY_ID)
REFERENCES AGENCY_PROFILE (AGENCY_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE OJBC_USER ADD CONSTRAINT agency_profile_ojbc_user_fk
FOREIGN KEY (AGENCY_ID)
REFERENCES AGENCY_PROFILE (AGENCY_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE AGENCY_SUPER_USER ADD CONSTRAINT agency_profile_agency_super_user_fk
FOREIGN KEY (SUPERVISED_AGENCY)
REFERENCES AGENCY_PROFILE (AGENCY_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE SUBSCRIPTION_OWNER ADD CONSTRAINT agency_profile_subscription_owner_fk
FOREIGN KEY (AGENCY_ID)
REFERENCES AGENCY_PROFILE (AGENCY_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE AGENCY_TRIGGERING_EVENT ADD CONSTRAINT agency_profile_agency_triggering_event_fk
FOREIGN KEY (AGENCY_ID)
REFERENCES AGENCY_PROFILE (AGENCY_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE SUBSCRIPTION ADD CONSTRAINT subscription_owner_subscription_fk
FOREIGN KEY (SUBSCRIPTION_OWNER_ID)
REFERENCES SUBSCRIPTION_OWNER (SUBSCRIPTION_OWNER_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE AGENCY_SUPER_USER ADD CONSTRAINT ojbc_user_agency_super_user_fk
FOREIGN KEY (OJBC_USER_ID)
REFERENCES OJBC_USER (OJBC_USER_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE JOB_TITLE ADD CONSTRAINT department_title_fk
FOREIGN KEY (DEPARTMENT_ID)
REFERENCES DEPARTMENT (DEPARTMENT_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE JOB_TITLE_PRIVILEGE ADD CONSTRAINT job_title_job_title_privilege_fk
FOREIGN KEY (JOB_TITLE_ID)
REFERENCES JOB_TITLE (JOB_TITLE_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE AGENCY_CONTACT_EMAIL_JOINER ADD CONSTRAINT agency_contact_email_agency_contact_email_joiner_fk
FOREIGN KEY (AGENCY_CONTACT_EMAIL_ID)
REFERENCES AGENCY_CONTACT_EMAIL (AGENCY_CONTACT_EMAIL_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE SUBSCRIPTION ADD CONSTRAINT subscription_category_subscription_fk
FOREIGN KEY (SUBSCRIPTION_CATEGORY_CODE)
REFERENCES SUBSCRIPTION_CATEGORY (SUBSCRIPTION_CATEGORY_CODE)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE FBI_RAP_BACK_SUBSCRIPTION ADD CONSTRAINT rap_back_subscription_term_fbi_rap_back_subscription_fk
FOREIGN KEY (RAP_BACK_SUBSCRIPTION_TERM_CODE)
REFERENCES RAP_BACK_SUBSCRIPTION_TERM (RAP_BACK_SUBSCRIPTION_TERM_CODE)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE FBI_RAP_BACK_SUBSCRIPTION ADD CONSTRAINT rap_back_activity_not_format_fbi_rap_back_sub_fk
FOREIGN KEY (RAP_BACK_ACTIVITY_NOTIFICATION_FORMAT_CODE)
REFERENCES RAP_BACK_ACTIVITY_NOTIFICATION_FORMAT (RAP_BACK_ACTIVITY_NOTIFICATION_FORMAT_CODE)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE FBI_RAP_BACK_SUBSCRIPTION ADD CONSTRAINT rap_back_category_code_fbi_rap_back_subscription_fk
FOREIGN KEY (RAP_BACK_CATEGORY_CODE)
REFERENCES RAP_BACK_CATEGORY (RAP_BACK_CATEGORY_CODE)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE CIVIL_FINGER_PRINTS ADD CONSTRAINT finger_prints_type_civil_finger_prints_fk
FOREIGN KEY (FINGER_PRINTS_TYPE_ID)
REFERENCES FINGER_PRINTS_TYPE (FINGER_PRINTS_TYPE_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE CIVIL_INITIAL_RESULTS ADD CONSTRAINT results_sender_civil_initial_results_fk
FOREIGN KEY (RESULTS_SENDER_ID)
REFERENCES RESULTS_SENDER (RESULTS_SENDER_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE CRIMINAL_INITIAL_RESULTS ADD CONSTRAINT results_sender_criminal_initial_results_fk
FOREIGN KEY (RESULTS_SENDER_ID)
REFERENCES RESULTS_SENDER (RESULTS_SENDER_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE SUBSEQUENT_RESULTS ADD CONSTRAINT results_sender_subsequent_results_fk
FOREIGN KEY (RESULTS_SENDER_ID)
REFERENCES RESULTS_SENDER (RESULTS_SENDER_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE NSOR_DEMOGRAPHICS ADD CONSTRAINT results_sender_nsor_demographics_fk
FOREIGN KEY (RESULTS_SENDER_ID)
REFERENCES RESULTS_SENDER (RESULTS_SENDER_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE NSOR_SEARCH_RESULT ADD CONSTRAINT results_sender_nsor_search_result_fk
FOREIGN KEY (RESULTS_SENDER_ID)
REFERENCES RESULTS_SENDER (RESULTS_SENDER_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE IDENTIFICATION_TRANSACTION ADD CONSTRAINT fbi_rap_back_subject_identification_transaction_fk
FOREIGN KEY (SUBJECT_ID)
REFERENCES IDENTIFICATION_SUBJECT (SUBJECT_ID)
ON DELETE CASCADE
ON UPDATE CASCADE;

ALTER TABLE NOTIFICATION_MECHANISM ADD CONSTRAINT constraint_94
FOREIGN KEY (SUBSCRIPTIONID)
REFERENCES SUBSCRIPTION (ID)
ON DELETE CASCADE
ON UPDATE CASCADE;

ALTER TABLE SUBSCRIPTION_SUBJECT_IDENTIFIER ADD CONSTRAINT constraint_b
FOREIGN KEY (SUBSCRIPTIONID)
REFERENCES SUBSCRIPTION (ID)
ON DELETE CASCADE
ON UPDATE CASCADE;

ALTER TABLE IDENTIFICATION_TRANSACTION ADD CONSTRAINT subscription_identification_transaction_fk
FOREIGN KEY (SUBSCRIPTION_ID)
REFERENCES SUBSCRIPTION (ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE SUBSCRIPTION_PROPERTIES ADD CONSTRAINT subscription_subscription_properties_fk
FOREIGN KEY (SUBSCRIPTIONID)
REFERENCES SUBSCRIPTION (ID)
ON DELETE CASCADE
ON UPDATE CASCADE;

ALTER TABLE FBI_RAP_BACK_SUBSCRIPTION ADD CONSTRAINT subscription_fbi_rap_back_subscription_fk
FOREIGN KEY (SUBSCRIPTION_ID)
REFERENCES SUBSCRIPTION (ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE CRIMINAL_INITIAL_RESULTS ADD CONSTRAINT identification_transaction_criminal_initial_results_fk
FOREIGN KEY (TRANSACTION_NUMBER)
REFERENCES IDENTIFICATION_TRANSACTION (TRANSACTION_NUMBER)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE CIVIL_INITIAL_RESULTS ADD CONSTRAINT identification_transaction_civil_initial_results_fk
FOREIGN KEY (TRANSACTION_NUMBER)
REFERENCES IDENTIFICATION_TRANSACTION (TRANSACTION_NUMBER)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE CIVIL_FINGER_PRINTS ADD CONSTRAINT identification_transaction_civil_nists_fk
FOREIGN KEY (TRANSACTION_NUMBER)
REFERENCES IDENTIFICATION_TRANSACTION (TRANSACTION_NUMBER)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE NSOR_DEMOGRAPHICS ADD CONSTRAINT identification_transaction_nsor_demographics_fk
FOREIGN KEY (TRANSACTION_NUMBER)
REFERENCES IDENTIFICATION_TRANSACTION (TRANSACTION_NUMBER)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE NSOR_SEARCH_RESULT ADD CONSTRAINT identification_transaction_nsor_search_result_fk
FOREIGN KEY (TRANSACTION_NUMBER)
REFERENCES IDENTIFICATION_TRANSACTION (TRANSACTION_NUMBER)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE NSOR_FIVE_YEAR_CHECK ADD CONSTRAINT identification_transaction_nsor_five_year_check_fk
FOREIGN KEY (TRANSACTION_NUMBER)
REFERENCES IDENTIFICATION_TRANSACTION (TRANSACTION_NUMBER)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE CIVIL_INITIAL_RAP_SHEET ADD CONSTRAINT civil_initial_results_civil_initial_rap_sheet_fk
FOREIGN KEY (CIVIL_INITIAL_RESULT_ID)
REFERENCES CIVIL_INITIAL_RESULTS (CIVIL_INITIAL_RESULT_ID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;
