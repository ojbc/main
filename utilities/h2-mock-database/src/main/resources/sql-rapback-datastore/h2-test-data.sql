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
insert into subscription(id, topic, startDate, endDate, lastValidationDate, subscribingSystemIdentifier, subscriptionOwner, subjectName, active) values ('62723','{http://ojbc.org/wsn/topics}:person/civilArrest', '2014-10-19', '2015-10-19', '2014-10-19', '{http://demostate.gov/SystemNames/1.0}SystemC', 'OJBC:IDP:OJBC:USER:admin', 'Lisa W Simpson', '1');
insert into subscription(id, topic, startDate, endDate, lastValidationDate, subscribingSystemIdentifier, subscriptionOwner, subjectName, active) values ('62724','{http://ojbc.org/wsn/topics}:person/civilArrest', '2014-10-19', '2015-10-19', '2014-10-19', '{http://demostate.gov/SystemNames/1.0}SystemC', 'OJBC:IDP:OJBC:USER:admin', 'Lisa W Simpson', '1');
insert into notification_mechanism(subscriptionId,notificationMechanismType,notificationAddress) values('62721', 'email', 'email4@email.com');
insert into notification_mechanism(subscriptionId,notificationMechanismType,notificationAddress) values('62721', 'email', 'email101@email.com');

insert into notification_mechanism(subscriptionId,notificationMechanismType,notificationAddress) values('62720', 'email', 'email100@email.com');
insert into notification_mechanism(subscriptionId,notificationMechanismType,notificationAddress) values('62720', 'email', 'email2@email.com');

insert into notification_mechanism(subscriptionId,notificationMechanismType,notificationAddress) values('62723', 'email', 'email5@email.com');
insert into notification_mechanism(subscriptionId,notificationMechanismType,notificationAddress) values('62723', 'email', 'email103@email.com');

insert into notification_mechanism(subscriptionId,notificationMechanismType,notificationAddress) values('62724', 'email', 'email105@email.com');
insert into notification_mechanism(subscriptionId,notificationMechanismType,notificationAddress) values('62724', 'email', 'email6@email.com');

insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62720', 'subscriptionQualifier', '2109639');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62720', 'dateOfBirth', '1960-10-02');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62720', 'firstName', 'MICHAEL');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62720', 'lastName', 'Smith-Jones');

insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62721', 'subscriptionQualifier', '2110217');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62721', 'dateOfBirth', '1980-06-16');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62721', 'firstName', 'MICHAEL');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62721', 'lastName', 'Jones-Smith');

insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62723', 'subscriptionQualifier', '2110223');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62723', 'dateOfBirth', '1990-10-12');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62723', 'firstName', 'Lisa');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62723', 'lastName', 'Simpson');

insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62724', 'subscriptionQualifier', '2110224');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62724', 'dateOfBirth', '1990-10-12');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62724', 'firstName', 'Lisa');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62724', 'lastName', 'Simpson');

insert into finger_prints_type(finger_prints_type_id, finger_prints_type) values('1', 'FBI'); 
insert into finger_prints_type(finger_prints_type_id, finger_prints_type) values('2', 'State'); 

insert into results_sender(results_sender_id, results_sender) values('1', 'FBI'); 
insert into results_sender(results_sender_id, results_sender) values('2', 'State');

insert into rap_back_category(rap_back_category_code, rap_back_category_description) values('F', 'Firearms'); 
insert into rap_back_category(rap_back_category_code, rap_back_category_description) values('I', 'Volunteer, Child Care/School Employee, Non-Criminal Justice Employment and Licensing'); 
insert into rap_back_category(rap_back_category_code, rap_back_category_description) values('J', 'Criminal Justice Employment'); 
insert into rap_back_category(rap_back_category_code, rap_back_category_description) values('CI', 'Criminal Justice Investigative'); 
insert into rap_back_category(rap_back_category_code, rap_back_category_description) values('CS', 'Criminal Justice - Supervision'); 
insert into rap_back_category(rap_back_category_code, rap_back_category_description) values('S', 'Security Clearance Information Act'); 

insert into rap_back_activity_notification_format(rap_back_activity_notification_format_code, rap_back_activity_notification_format_desc)
	values('1', 'Pre-Notification');
insert into rap_back_activity_notification_format(rap_back_activity_notification_format_code, rap_back_activity_notification_format_desc)
	values('2', 'Triggering Event');
insert into rap_back_activity_notification_format(rap_back_activity_notification_format_code, rap_back_activity_notification_format_desc)
	values('3', 'Identity History Summary to include the triggering event');
	
insert into rap_back_subscription_term(rap_back_subscription_term_code, rap_back_subscription_term_desc)
	values('2', 'Two year subscription term');
insert into rap_back_subscription_term(rap_back_subscription_term_code, rap_back_subscription_term_desc)
	values('5', 'Five year subscription term');
insert into rap_back_subscription_term(rap_back_subscription_term_code, rap_back_subscription_term_desc)
	values('L', 'Lifetime subscription term');
	
insert into IDENTIFICATION_SUBJECT(subject_id, ucn, civil_sid, criminal_sid, dob, first_name, last_name, middle_initial, sex_code) 
	values ('1', 'B1234568','A123457', 'C1234567', '1990-10-12', 'Lisa', 'Simpson', 'W','F');
insert into IDENTIFICATION_SUBJECT(subject_id, ucn, civil_sid, criminal_sid, dob, first_name, last_name, middle_initial, sex_code) 
	values ('2', 'B1234569','A123458', 'C1234568', '1987-10-10', 'Bart', 'Simpson', 'C','M');

insert into IDENTIFICATION_TRANSACTION (TRANSACTION_NUMBER, SUBJECT_ID, OTN, OWNER_ORI, OWNER_PROGRAM_OCA, IDENTIFICATION_CATEGORY, ARCHIVED, Subscription_ID) 
	values ('000001820140729014008339990', '1', '12343', '68796860', 'ID12345', 'I', 'false', '62724');
insert into IDENTIFICATION_TRANSACTION (TRANSACTION_NUMBER, SUBJECT_ID, OTN, OWNER_ORI, OWNER_PROGRAM_OCA, IDENTIFICATION_CATEGORY, ARCHIVED) 
	values ('000001820140729014008339991', '2', '12344', '68796860', 'ID12345', 'CAR', 'false');
insert into IDENTIFICATION_TRANSACTION (TRANSACTION_NUMBER, SUBJECT_ID, OTN, OWNER_ORI, OWNER_PROGRAM_OCA, IDENTIFICATION_CATEGORY, ARCHIVED, Subscription_ID) 
	values ('000001820140729014008339993', '1', '12343', '1234567890', 'ID12345', 'I', 'false', '62723');
insert into IDENTIFICATION_TRANSACTION (TRANSACTION_NUMBER, SUBJECT_ID, OTN, OWNER_ORI, OWNER_PROGRAM_OCA, IDENTIFICATION_CATEGORY, ARCHIVED) 
	values ('000001820140729014008339994', '2', '12344', '1234567890', 'ID12345', 'CAR', 'false');
	
/*http://stackoverflow.com/questions/2607326/insert-a-blob-via-a-sql-script*/
insert into CIVIL_FINGER_PRINTS (TRANSACTION_NUMBER, FINGER_PRINTS_FILE, TRANSACTION_TYPE, FINGER_PRINTS_TYPE_ID) 
	values ('000001820140729014008339990', '78DA7373F274CE2CCBCC71CBCC4B4F2D0A28CACC2B2906004B1A07A4', 'Transaction', '1');
insert into CIVIL_FINGER_PRINTS (TRANSACTION_NUMBER, FINGER_PRINTS_FILE, TRANSACTION_TYPE, FINGER_PRINTS_TYPE_ID) 
	values ('000001820140729014008339990', '78DA0B2E492C4975CE2CCBCC71CBCC4B4F2D0A28CACC2B290600638C08D4', 'Transaction', '2');
insert into CIVIL_FINGER_PRINTS (TRANSACTION_NUMBER, FINGER_PRINTS_FILE, TRANSACTION_TYPE, FINGER_PRINTS_TYPE_ID) 
	values ('000001820140729014008339993', '78DA7373F274CE2CCBCC71CBCC4B4F2D0A28CACC2B2906004B1A07A4', 'Transaction', '1');
insert into CIVIL_FINGER_PRINTS (TRANSACTION_NUMBER, FINGER_PRINTS_FILE, TRANSACTION_TYPE, FINGER_PRINTS_TYPE_ID) 
	values ('000001820140729014008339993', '78DA0B2E492C4975CE2CCBCC71CBCC4B4F2D0A28CACC2B290600638C08D4', 'Transaction', '2');

--insert into CRIMINAL_FINGER_PRINTS (TRANSACTION_NUMBER, FINGER_PRINTS_FILE, TRANSACTION_TYPE, FINGER_PRINTS_TYPE_ID) 
--	values ('000001820140729014008339991', RAWTOHEX('FBICriminalFingerPrints'), 'Transaction', '1');
--insert into CRIMINAL_FINGER_PRINTS (TRANSACTION_NUMBER, FINGER_PRINTS_FILE, TRANSACTION_TYPE, FINGER_PRINTS_TYPE_ID) 
--	values ('000001820140729014008339991', RAWTOHEX('StateCriminalFingerPrints'), 'Transaction', '2');

insert into CIVIL_INITIAL_RESULTS (CIVIL_INITIAL_RESULT_ID,TRANSACTION_NUMBER, SEARCH_RESULT_FILE, 
	TRANSACTION_TYPE, RESULTS_SENDER_ID)
	values ('1', '000001820140729014008339990', '78DA73CB2FCD4B514854F04D2C49CE00001F9C048B', 'Transaction', '1');
insert into CIVIL_INITIAL_RESULTS (CIVIL_INITIAL_RESULT_ID,TRANSACTION_NUMBER, SEARCH_RESULT_FILE, 
	TRANSACTION_TYPE,  RESULTS_SENDER_ID) 
	values ('2', '000001820140729014008339990', '78DA73CB2FCD4B514854F04D2C49CE00001F9C048B', 'Transaction', '2');
insert into CIVIL_INITIAL_RESULTS (CIVIL_INITIAL_RESULT_ID,TRANSACTION_NUMBER, SEARCH_RESULT_FILE, 
	TRANSACTION_TYPE, RESULTS_SENDER_ID)
	values ('3', '000001820140729014008339993', '78DA73CB2FCD4B514854F04D2C49CE00001F9C048B', 'Transaction', '1');
insert into CIVIL_INITIAL_RESULTS (CIVIL_INITIAL_RESULT_ID,TRANSACTION_NUMBER, SEARCH_RESULT_FILE, 
	TRANSACTION_TYPE,  RESULTS_SENDER_ID) 
	values ('4', '000001820140729014008339993', '78DA73CB2FCD4B514854F04D2C49CE00001F9C048B', 'Transaction', '2');
	
insert into CIVIL_INITIAL_RAP_SHEET (CIVIL_INITIAL_RESULT_ID, RAP_SHEET, TRANSACTION_TYPE) values ('1', '78DA7373F274CE2CCBCC094A2C08CE484D2D01002F1A05E5', 'Transaction');	
insert into CIVIL_INITIAL_RAP_SHEET (CIVIL_INITIAL_RESULT_ID, RAP_SHEET, TRANSACTION_TYPE) values ('1', '78DA7373F274CE2CCBCC094A2C08CE484D2D31020035310617', 'Transaction');	
insert into CIVIL_INITIAL_RAP_SHEET (CIVIL_INITIAL_RESULT_ID, RAP_SHEET, TRANSACTION_TYPE) values ('2', '78DA0B2E492C4975CE2CCBCC094A2C08CE484D2D010042CC0715', 'Transaction');	
insert into CIVIL_INITIAL_RAP_SHEET (CIVIL_INITIAL_RESULT_ID, RAP_SHEET, TRANSACTION_TYPE) values ('2', '78DA0B2E492C4975CE2CCBCC094A2C08CE484D2D3102004A130747', 'Transaction');	
insert into CIVIL_INITIAL_RAP_SHEET (CIVIL_INITIAL_RESULT_ID, RAP_SHEET, TRANSACTION_TYPE) values ('3', '78DA7373F274CE2CCBCC094A2C08CE484D2D01002F1A05E5', 'Transaction');	
insert into CIVIL_INITIAL_RAP_SHEET (CIVIL_INITIAL_RESULT_ID, RAP_SHEET, TRANSACTION_TYPE) values ('4', '78DA0B2E492C4975CE2CCBCC094A2C08CE484D2D010042CC0715', 'Transaction');	

insert into CRIMINAL_INITIAL_RESULTS (CRIMINAL_INITIAL_RESULT_ID, TRANSACTION_NUMBER, SEARCH_RESULT_FILE, TRANSACTION_TYPE, RESULTS_SENDER_ID) 
			values ('1', '000001820140729014008339991', '78DAF34D2C49CE0000059401EE', 'Transaction', '1');
insert into CRIMINAL_INITIAL_RESULTS (CRIMINAL_INITIAL_RESULT_ID, TRANSACTION_NUMBER, SEARCH_RESULT_FILE, TRANSACTION_TYPE, RESULTS_SENDER_ID) 
			values ('2', '000001820140729014008339991', '78DAF34D2C49CE0000059401EE', 'Transaction', '2');
insert into CRIMINAL_INITIAL_RESULTS (CRIMINAL_INITIAL_RESULT_ID, TRANSACTION_NUMBER, SEARCH_RESULT_FILE, TRANSACTION_TYPE, RESULTS_SENDER_ID) 
			values ('3', '000001820140729014008339994', '78DAF34D2C49CE0000059401EE', 'Transaction', '1');
insert into CRIMINAL_INITIAL_RESULTS (CRIMINAL_INITIAL_RESULT_ID, TRANSACTION_NUMBER, SEARCH_RESULT_FILE, TRANSACTION_TYPE, RESULTS_SENDER_ID) 
			values ('4', '000001820140729014008339994', '78DAF34D2C49CE0000059401EE', 'Transaction', '2');
			
insert into fbi_rap_back_subscription(fbi_subscription_id, rap_back_category_code, rap_back_subscription_term_code, rap_back_expiration_date, rap_back_term_date, 
	rap_back_start_date, rap_back_opt_out_in_state_indicator, rap_back_activity_notification_format_code, ucn, timestamp)
values
	('fbiSubscriptionId', 'CI', '5', '2015-12-19', '2019-12-19', '2014-10-19', false, '2', 
	'123456789', {ts '2014-10-19 18:47:52.69'});			
	
insert into fbi_rap_back_subscription(fbi_subscription_id, rap_back_category_code, rap_back_subscription_term_code, rap_back_expiration_date, rap_back_term_date,
	rap_back_start_date, rap_back_opt_out_in_state_indicator, rap_back_activity_notification_format_code, ucn, timestamp)
values
	('fbiSubscriptionId_2', 'CI', '2', '2015-12-19','2016-12-19', '2014-10-19', false, '1', 
	'074644NG0', {ts '2014-10-19 18:47:52.69'});			
	