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
/*This is the default test data loaded into h2 when this is deployed */

insert into subscription(id, topic, startDate, endDate, lastValidationDate, subscribingSystemIdentifier, subscriptionOwner, subjectName, active) values ('62720','{http://ojbc.org/wsn/topics}:person/incident', '2011-10-19', NULL, '2011-10-19', '{http://demostate.gov/SystemNames/1.0}SystemC', 'OJBC:IDP:OJBC:USER:admin', 'MICHAEL Smith-Jones', '1');
insert into subscription(id, topic, startDate, endDate, lastValidationDate, subscribingSystemIdentifier, subscriptionOwner, subjectName, active) values ('62721','{http://ojbc.org/wsn/topics}:person/incident', '2011-10-19', NULL, '2011-10-19', '{http://demostate.gov/SystemNames/1.0}SystemC', 'OJBC:IDP:OJBC:USER:admin', 'MICHAEL Smith-Jones', '1');
insert into subscription(id, topic, startDate, endDate, lastValidationDate, subscribingSystemIdentifier, subscriptionOwner, subjectName, active) values ('62722','{http://ojbc.org/wsn/topics}:person/incident', '2011-10-19', NULL, '2011-10-19', '{http://demostate.gov/SystemNames/1.0}SystemC', 'OJBC:IDP:OJBC:USER:admin', 'MICHAEL Smith-Jones', '1');

insert into notification_mechanism(subscriptionId,notificationMechanismType,notificationAddress) values('62721', 'email', 'email4@email.com');
insert into notification_mechanism(subscriptionId,notificationMechanismType,notificationAddress) values('62721', 'email', 'email101@email.com');

insert into notification_mechanism(subscriptionId,notificationMechanismType,notificationAddress) values('62720', 'email', 'email100@email.com');
insert into notification_mechanism(subscriptionId,notificationMechanismType,notificationAddress) values('62720', 'email', 'email2@email.com');

insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62720', 'subscriptionQualifier', '2109639');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62720', 'dateOfBirth', '1960-10-02');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62720', 'firstName', 'MICHAEL');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62720', 'lastName', 'Smith-Jones');

insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62721', 'subscriptionQualifier', '2110217');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62721', 'dateOfBirth', '1980-06-16');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62721', 'firstName', 'MICHAEL');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62721', 'lastName', 'Jones-Smith');

insert into current_state(current_state_id , current_state ) values('1', 'Archived');
insert into current_state(current_state_id , current_state ) values('2', 'Available for subscription');
insert into current_state(current_state_id , current_state ) values('3','Subscribed');

insert into finger_prints_type(finger_prints_type_id, finger_prints_type) values('1', 'FBI'); 
insert into finger_prints_type(finger_prints_type_id, finger_prints_type) values('2', 'State'); 

insert into results_sender(results_sender_id, results_sender) values('1', 'FBI'); 
insert into results_sender(results_sender_id, results_sender) values('2', 'State'); 

insert into fbi_rap_back_subject(subject_id, ucn, civil_sid, criminal_sid, dob, first_name, last_name, middle_initial, sex_code) 
	values ('1', 'B1234568','A123457', NULL, '1990-10-12', 'Lisa', 'Simpson', 'W','F');
insert into fbi_rap_back_subject(subject_id, ucn, civil_sid, criminal_sid, dob, first_name, last_name, middle_initial, sex_code) 
	values ('2', 'B1234569','A123458', NULL, '1987-10-10', 'Bart', 'Simpson', 'C','M');

insert into IDENTIFICATION_TRANSACTION (TRANSACTION_NUMBER, SUBJECT_ID, OTN, OWNER_ORI, OWNER_PROGRAM_OCA, IDENTIFICATION_CATEGORY, CURRENT_STATE_ID) 
	values ('000001820140729014008339990', '1', '12343', '68796860', 'ID12345', 'I', '2');
insert into IDENTIFICATION_TRANSACTION (TRANSACTION_NUMBER, SUBJECT_ID, OTN, OWNER_ORI, OWNER_PROGRAM_OCA, IDENTIFICATION_CATEGORY, CURRENT_STATE_ID) 
	values ('000001820140729014008339991', '2', '12344', '68796860', 'ID12345', 'CAR', '2');
	
/*http://stackoverflow.com/questions/2607326/insert-a-blob-via-a-sql-script*/
insert into CIVIL_FINGER_PRINTS (TRANSACTION_NUMBER, FINGER_PRINTS_FILE, TRANSACTION_TYPE, FINGER_PRINTS_TYPE_ID) 
	values ('000001820140729014008339990', RAWTOHEX('FBICivilFingerPrints'), 'Transaction', '1');
insert into CIVIL_FINGER_PRINTS (TRANSACTION_NUMBER, FINGER_PRINTS_FILE, TRANSACTION_TYPE, FINGER_PRINTS_TYPE_ID) 
	values ('000001820140729014008339990', RAWTOHEX('StateCivilFingerPrints'), 'Transaction', '2');

--insert into CRIMINAL_FINGER_PRINTS (TRANSACTION_NUMBER, FINGER_PRINTS_FILE, TRANSACTION_TYPE, FINGER_PRINTS_TYPE_ID) 
--	values ('000001820140729014008339991', RAWTOHEX('FBICriminalFingerPrints'), 'Transaction', '1');
--insert into CRIMINAL_FINGER_PRINTS (TRANSACTION_NUMBER, FINGER_PRINTS_FILE, TRANSACTION_TYPE, FINGER_PRINTS_TYPE_ID) 
--	values ('000001820140729014008339991', RAWTOHEX('StateCriminalFingerPrints'), 'Transaction', '2');

insert into CIVIL_INITIAL_RESULTS (CIVIL_INITIAL_RESULT_ID,TRANSACTION_NUMBER, SEARCH_RESULT_FILE, 
	TRANSACTION_TYPE, RESULTS_SENDER_ID)
	values ('1', '000001820140729014008339990', RAWTOHEX('Match'), 'Transaction', '1');
insert into CIVIL_INITIAL_RESULTS (CIVIL_INITIAL_RESULT_ID,TRANSACTION_NUMBER, SEARCH_RESULT_FILE, 
	TRANSACTION_TYPE,  RESULTS_SENDER_ID) 
	values ('2', '000001820140729014008339990', RAWTOHEX('Match'), 'Transaction', '2');
	
insert into CIVIL_INITIAL_RAP_SHEET (CIVIL_INITIAL_RESULT_ID, RAP_SHEET, TRANSACTION_TYPE) values ('1', RAWTOHEX('FBICivilRapSheet'), 'Transaction');	
insert into CIVIL_INITIAL_RAP_SHEET (CIVIL_INITIAL_RESULT_ID, RAP_SHEET, TRANSACTION_TYPE) values ('2', RAWTOHEX('StateCivilRapSheet'), 'Transaction');	

insert into CRIMINAL_INITIAL_RESULTS (CRIMINAL_INITIAL_RESULT_ID, TRANSACTION_NUMBER, SEARCH_RESULT_FILE, TRANSACTION_TYPE, RESULTS_SENDER_ID) 
			values ('1', '000001820140729014008339991', RAWTOHEX('Match'), 'Transaction', '1');
insert into CRIMINAL_INITIAL_RESULTS (CRIMINAL_INITIAL_RESULT_ID, TRANSACTION_NUMBER, SEARCH_RESULT_FILE, TRANSACTION_TYPE, RESULTS_SENDER_ID) 
			values ('2', '000001820140729014008339991', RAWTOHEX('Match'), 'Transaction', '2');