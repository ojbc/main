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
/*This is the default test data loaded into h2 when this is deployed */
use rapback_datastore;

insert into subscription_category(subscription_category_code, subscription_category_description) values('CI', 'Criminal Justice Investigative');
insert into subscription_category(subscription_category_code, subscription_category_description) values('CS', 'Criminal Justice Supervision');
insert into subscription_category(subscription_category_code, subscription_category_description) values('F', 'Firearms');
insert into subscription_category(subscription_category_code, subscription_category_description) values('I', 'Volunteer, Child Care/School Employee, Non-Criminal Justice Employee and Licensing');
insert into subscription_category(subscription_category_code, subscription_category_description) values('J', 'Criminal Justice Employee');
insert into subscription_category(subscription_category_code, subscription_category_description) values('S', 'Security Clearance Information Act');

insert into identification_category(identification_category_id, identification_category_code, identification_category_description, identification_category_type) values('1', 'F', 'Firearms','CIVIL'); 
insert into identification_category(identification_category_id, identification_category_code, identification_category_description, identification_category_type) values('2', 'I', 'Volunteer, Child Care/School Employee, Non-Criminal Justice Employment and Licensing', 'CIVIL'); 
insert into identification_category(identification_category_id, identification_category_code, identification_category_description, identification_category_type) values('3', 'J', 'Criminal Justice Employment', 'CIVIL'); 
insert into identification_category(identification_category_id, identification_category_code, identification_category_description, identification_category_type) values('4', 'CAR', 'Criminal Tenprint Submission', 'CRIMINAL'); 
insert into identification_category(identification_category_id, identification_category_code, identification_category_description, identification_category_type) values('5', 'SOR', 'Sex Offender Registry', 'CRIMINAL'); 
insert into identification_category(identification_category_id, identification_category_code, identification_category_description, identification_category_type) values('6', 'S', 'Security Clearance Information Act', 'CIVIL'); 

insert into triggering_event values('1','ARREST','ARREST');
insert into triggering_event values('2','DISPOSITION','DISPOSITION');
insert into triggering_event values('3','NCIC-WARRANT','NCIC-WARRANT');
insert into triggering_event values('4','NCIC-SOR','NCIC-SOR');
insert into triggering_event values('5','DEATH','DEATH');

insert into AGENCY_PROFILE(AGENCY_ID, AGENCY_ORI, AGENCY_NAME, FBI_SUBSCRIPTION_QUALIFICATION, STATE_SUBSCRIPTION_QUALIFICATION,FIREARMS_SUBSCRIPTION_QUALIFICATION,CJ_EMPLOYMENT_SUBSCRIPTION_QUALIFICATION, CIVIL_AGENCY_INDICATOR, ) 
	values ('1', '1234567890', 'Demo Agency', true, true, true, true, false); 
insert into AGENCY_PROFILE(AGENCY_ID, AGENCY_ORI, AGENCY_NAME, FBI_SUBSCRIPTION_QUALIFICATION, STATE_SUBSCRIPTION_QUALIFICATION,FIREARMS_SUBSCRIPTION_QUALIFICATION,CJ_EMPLOYMENT_SUBSCRIPTION_QUALIFICATION, CIVIL_AGENCY_INDICATOR ) 
	values ('2', '68796860', 'Test Agency', true, false, false, false, true); 
insert into AGENCY_PROFILE(AGENCY_ID, AGENCY_ORI, AGENCY_NAME, FBI_SUBSCRIPTION_QUALIFICATION, STATE_SUBSCRIPTION_QUALIFICATION,FIREARMS_SUBSCRIPTION_QUALIFICATION,CJ_EMPLOYMENT_SUBSCRIPTION_QUALIFICATION, CIVIL_AGENCY_INDICATOR ) 
	values ('3', 'HCJDC', 'IT Agency', true, true, true, true, false); 

insert into agency_triggering_event(agency_triggering_event_id, agency_id, triggering_event_id) values(1, 1, 1);
insert into agency_triggering_event(agency_triggering_event_id, agency_id, triggering_event_id) values(2, 1, 2);
insert into agency_triggering_event(agency_triggering_event_id, agency_id, triggering_event_id) values(3, 1, 3);
insert into agency_triggering_event(agency_triggering_event_id, agency_id, triggering_event_id) values(4, 1, 4);
insert into agency_triggering_event(agency_triggering_event_id, agency_id, triggering_event_id) values(5, 2, 1);
insert into agency_triggering_event(agency_triggering_event_id, agency_id, triggering_event_id) values(6, 2, 5);
	
insert into ojbc_user(ojbc_user_id, federation_id, agency_id, super_user_indicator) values(1, 'HIJIS:IDP:HCJDC:USER:hpotter', 2, false); 
insert into ojbc_user(ojbc_user_id, federation_id, agency_id, super_user_indicator) values(2, 'HIJIS:IDP:HCJDC:USER:demouser', 1, false); 
insert into ojbc_user(ojbc_user_id, federation_id, agency_id, super_user_indicator) values(3, 'HIJIS:IDP:HCJDC:USER:superuser', 3, true);

insert into agency_super_user(supervised_agency, ojbc_user_id) values(1, 2);  
insert into agency_super_user(supervised_agency, ojbc_user_id) values(1, 1);  
insert into agency_super_user(supervised_agency, ojbc_user_id) values(2, 1);  

insert into AGENCY_CONTACT_EMAIL(AGENCY_CONTACT_EMAIL_ID , AGENCY_ID , AGENCY_EMAIL) values('1', '1', 'demo.agency@localhost'); 
insert into AGENCY_CONTACT_EMAIL(AGENCY_CONTACT_EMAIL_ID , AGENCY_ID , AGENCY_EMAIL) values('2', '1', 'demo.agency2@localhost'); 	
insert into AGENCY_CONTACT_EMAIL(AGENCY_CONTACT_EMAIL_ID , AGENCY_ID , AGENCY_EMAIL) values('3', '2', 'test.agency@localhost'); 
insert into AGENCY_CONTACT_EMAIL(AGENCY_CONTACT_EMAIL_ID , AGENCY_ID , AGENCY_EMAIL) values('4', '2', 'test.agency2@localhost'); 	

insert into AGENCY_EMAIL_CATEGORY (AGENCY_EMAIL_CATEGORY_ID, CODE, DESCRIPTION) values ('1', 'I', 'Volunteer, Child Care/School Employee, Non-Criminal Justice Employee and Licensing');
insert into AGENCY_EMAIL_CATEGORY (AGENCY_EMAIL_CATEGORY_ID, CODE, DESCRIPTION) values ('2', 'F', 'Non CJ Firearms');
insert into AGENCY_EMAIL_CATEGORY (AGENCY_EMAIL_CATEGORY_ID, CODE, DESCRIPTION) values ('3', 'J', 'CJ Employment');
insert into AGENCY_EMAIL_CATEGORY (AGENCY_EMAIL_CATEGORY_ID, CODE, DESCRIPTION) values ('4', 'S', 'S code');

insert into AGENCY_CONTACT_EMAIL_JOINER (AGENCY_CONTACT_EMAIL_JOINER_ID, AGENCY_CONTACT_EMAIL_ID, AGENCY_EMAIL_CATEGORY_ID) values ('1','1','1');
insert into AGENCY_CONTACT_EMAIL_JOINER (AGENCY_CONTACT_EMAIL_JOINER_ID, AGENCY_CONTACT_EMAIL_ID, AGENCY_EMAIL_CATEGORY_ID) values ('2','1','2');
insert into AGENCY_CONTACT_EMAIL_JOINER (AGENCY_CONTACT_EMAIL_JOINER_ID, AGENCY_CONTACT_EMAIL_ID, AGENCY_EMAIL_CATEGORY_ID) values ('3','2','2');
insert into AGENCY_CONTACT_EMAIL_JOINER (AGENCY_CONTACT_EMAIL_JOINER_ID, AGENCY_CONTACT_EMAIL_ID, AGENCY_EMAIL_CATEGORY_ID) values ('4','2','3');
insert into AGENCY_CONTACT_EMAIL_JOINER (AGENCY_CONTACT_EMAIL_JOINER_ID, AGENCY_CONTACT_EMAIL_ID, AGENCY_EMAIL_CATEGORY_ID) values ('5','3','3');
insert into AGENCY_CONTACT_EMAIL_JOINER (AGENCY_CONTACT_EMAIL_JOINER_ID, AGENCY_CONTACT_EMAIL_ID, AGENCY_EMAIL_CATEGORY_ID) values ('6','3','4');
insert into AGENCY_CONTACT_EMAIL_JOINER (AGENCY_CONTACT_EMAIL_JOINER_ID, AGENCY_CONTACT_EMAIL_ID, AGENCY_EMAIL_CATEGORY_ID) values ('7','4','4');
insert into AGENCY_CONTACT_EMAIL_JOINER (AGENCY_CONTACT_EMAIL_JOINER_ID, AGENCY_CONTACT_EMAIL_ID, AGENCY_EMAIL_CATEGORY_ID) values ('8','4','1');

insert into department(department_id, department_name, agency_id) values ('1', 'Kauai PD A&T Records', '2'); 
insert into department(department_id, department_name, agency_id) values ('2', 'Kauai PD A&T R&D', '2'); 
insert into department(department_id, department_name, agency_id) values ('3', 'Honolulu PD Records and ID Division', '1'); 
insert into department(department_id, department_name, agency_id) values ('4', 'Central Receiving Division', '1');
insert into department(department_id, department_name, agency_id) values ('5', 'Test Division', '1');

insert into job_title(job_title_id, department_id, title_description) values('1', '1', 'ID Tech1');
insert into job_title(job_title_id, department_id, title_description) values('2', '1', 'Senior Clerk');
insert into job_title(job_title_id, department_id, title_description) values('3', '1', 'Police Records Unit Supervisor');
insert into job_title(job_title_id, department_id, title_description) values('4', '1', 'Weapons Registration Clerk');
insert into job_title(job_title_id, department_id, title_description) values('5', '1', 'Police Record Clerk');
insert into job_title(job_title_id, department_id, title_description) values('6', '1', 'Police Analyst');
insert into job_title(job_title_id, department_id, title_description) values('7', '2', 'Police Sergeant');
insert into job_title(job_title_id, department_id, title_description) values('8', '3', 'Sworn Supervisors');
insert into job_title(job_title_id, department_id, title_description) values('9', '3', 'Fingerprint Examiners');
insert into job_title(job_title_id, department_id, title_description) values('10', '3', 'Fingerprint Technicians');
insert into job_title(job_title_id, department_id, title_description) values('11', '3', 'Firearms Unit (both Civilian and Sworn)');
insert into job_title(job_title_id, department_id, title_description) values('12', '3', 'ID Section clerks');
insert into job_title(job_title_id, department_id, title_description) values('13', '3', 'Senior Clerk Typist');
insert into job_title(job_title_id, department_id, title_description) values('14', '4', 'All Sworn Personnel');
insert into job_title(job_title_id, department_id, title_description) values('15', '5', 'Any');

insert into job_title_privilege(job_title_id, identification_category_id) values ('1', '4');
insert into job_title_privilege(job_title_id, identification_category_id) values ('1', '3');
insert into job_title_privilege(job_title_id, identification_category_id) values ('1', '1');
insert into job_title_privilege(job_title_id, identification_category_id) values ('1', '5');
insert into job_title_privilege(job_title_id, identification_category_id) values ('2', '4');
insert into job_title_privilege(job_title_id, identification_category_id) values ('2', '3');
insert into job_title_privilege(job_title_id, identification_category_id) values ('2', '1');
insert into job_title_privilege(job_title_id, identification_category_id) values ('2', '5');
insert into job_title_privilege(job_title_id, identification_category_id) values ('3', '1');
insert into job_title_privilege(job_title_id, identification_category_id) values ('4', '1');
insert into job_title_privilege(job_title_id, identification_category_id) values ('5', '1');
insert into job_title_privilege(job_title_id, identification_category_id) values ('6', '5');
insert into job_title_privilege(job_title_id, identification_category_id) values ('7', '3');
insert into job_title_privilege(job_title_id, identification_category_id) values ('8', '4');
insert into job_title_privilege(job_title_id, identification_category_id) values ('8', '3');
insert into job_title_privilege(job_title_id, identification_category_id) values ('8', '1');
insert into job_title_privilege(job_title_id, identification_category_id) values ('8', '5');
insert into job_title_privilege(job_title_id, identification_category_id) values ('9', '4');
insert into job_title_privilege(job_title_id, identification_category_id) values ('9', '3');
insert into job_title_privilege(job_title_id, identification_category_id) values ('9', '1');
insert into job_title_privilege(job_title_id, identification_category_id) values ('9', '5');
insert into job_title_privilege(job_title_id, identification_category_id) values ('10', '4');
insert into job_title_privilege(job_title_id, identification_category_id) values ('10', '3');
insert into job_title_privilege(job_title_id, identification_category_id) values ('10', '1');
insert into job_title_privilege(job_title_id, identification_category_id) values ('10', '5');
insert into job_title_privilege(job_title_id, identification_category_id) values ('11', '1');
insert into job_title_privilege(job_title_id, identification_category_id) values ('12', '5');
insert into job_title_privilege(job_title_id, identification_category_id) values ('13', '5');
insert into job_title_privilege(job_title_id, identification_category_id) values ('14', '4');
insert into job_title_privilege(job_title_id, identification_category_id) values ('15', '1');

insert into SUBSCRIPTION_OWNER(SUBSCRIPTION_OWNER_ID,FIRST_NAME, LAST_NAME, EMAIL_ADDRESS, FEDERATION_ID, AGENCY_ID) values ('1', 'bill', 'padmanabhan', 'bill@local.gov', 'OJBC:IDP:OJBC:USER:admin', '1');
insert into SUBSCRIPTION_OWNER(SUBSCRIPTION_OWNER_ID,FIRST_NAME, LAST_NAME, EMAIL_ADDRESS, FEDERATION_ID, AGENCY_ID) values ('2', '', '', 'admin@local.gov', 'SYSTEM', '1');

insert into subscription(id, topic, startDate, endDate, lastValidationDate, creationDate, subscribingSystemIdentifier,  subjectName, active, subscription_owner_id) values ('62720','{http://ojbc.org/wsn/topics}:person/incident', '2011-10-19', NULL, '2011-10-19', '2011-10-19','{http://demostate.gov/SystemNames/1.0}SystemC', 'MICHAEL Smith-Jones', '1','1');
insert into subscription(id, topic, startDate, endDate, lastValidationDate, creationDate, subscribingSystemIdentifier,  subjectName, active, subscription_owner_id) values ('62721','{http://ojbc.org/wsn/topics}:person/incident', '2011-10-19', NULL, '2011-10-19', '2011-10-19','{http://demostate.gov/SystemNames/1.0}SystemC', 'MICHAEL Smith-Jones', '1','1');
insert into subscription(id, topic, startDate, endDate, lastValidationDate, creationDate, subscribingSystemIdentifier,  subjectName, active, subscription_owner_id) values ('62722','{http://ojbc.org/wsn/topics}:person/incident', '2011-10-19', NULL, '2011-10-19', '2011-10-19','{http://demostate.gov/SystemNames/1.0}SystemC', 'MICHAEL Smith-Jones', '1','1');
insert into subscription(id, topic, startDate, endDate, lastValidationDate, validationdueDate, creationDate, subscribingSystemIdentifier,  subjectName, active, subscription_category_code, subscription_owner_id) values ('62723','{http://ojbc.org/wsn/topics}:person/rapback', '2014-10-15', '2015-10-15', '2014-10-15','2018-09-10', '2014-10-15', '{http://demostate.gov/SystemNames/1.0}SystemC', 'Test W Jane', '1', 'I', '1');
insert into subscription(id, topic, startDate, endDate, lastValidationDate, validationdueDate, creationDate, subscribingSystemIdentifier,  subjectName, active, subscription_category_code, subscription_owner_id) values ('62724','{http://ojbc.org/wsn/topics}:person/rapback', '2014-10-15', '2015-10-15', '2014-10-15', '2018-09-13', '2014-10-15','{http://demostate.gov/SystemNames/1.0}SystemC', 'Test W Jane', '1', 'I', '1');
insert into subscription(id, topic, startDate, endDate, lastValidationDate, validationdueDate, creationDate, subscribingSystemIdentifier,  subjectName, active, subscription_category_code, subscription_owner_id) values ('62725','{http://ojbc.org/wsn/topics}:person/rapback', '2015-09-19', '2016-10-19', '2015-09-19', '2018-09-12', '2015-09-19','{http://demostate.gov/SystemNames/1.0}SystemC', 'Lisa W Simpson', '1','I', '1');
insert into subscription(id, topic, startDate, endDate, lastValidationDate, validationdueDate, creationDate, subscribingSystemIdentifier,  subjectName, active, subscription_category_code, subscription_owner_id) values ('62726','{http://ojbc.org/wsn/topics}:person/rapback', '2015-10-16', '2016-10-19', '2015-10-16', '2018-09-11', '2015-10-16','{http://demostate.gov/SystemNames/1.0}SystemC', 'Bart Simpson', '1','I', '1');
insert into subscription(id, topic, startDate, endDate, lastValidationDate, validationdueDate, creationDate, subscribingSystemIdentifier,  subjectName, active, subscription_category_code, subscription_owner_id) values ('62727','{http://ojbc.org/wsn/topics}:person/rapback', '2015-10-16', '2018-07-14', '2015-10-16','2020-10-19', '2015-10-16', '{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB', 'El Barto', '1', 'CI','1');

insert into subscription(id, topic, startDate, endDate, lastValidationDate, creationDate, subscribingSystemIdentifier,  subjectName, active, subscription_owner_id) values ('62728','{http://ojbc.org/wsn/topics}:person/arrest', '2015-10-16', '2016-10-19', '2015-10-16', '2015-10-16','{http://demostate.gov/SystemNames/1.0}SystemC',  'PortalUnsubscribe FbiUcnTest', '1','1');
insert into subscription values (62729, '{http://ojbc.org/wsn/topics}:person/rapback', '2018-04-12', '2019-04-12', '2018-04-12', '2018-04-12', '2019-04-12', '{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB', 'James D Trombley', 'case01', '1',	'CI', '1', '2018-04-12 11:21:10.16'); 
insert into subscription values (62730, '{http://ojbc.org/wsn/topics}:person/rapback', '2018-04-12', '2019-04-12', '2018-04-12', '2018-04-12', '2019-04-12', '{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB','Mary R Billiot','case2', '1','CI','1','2018-04-12 11:22:13.45');

insert into notification_mechanism(subscriptionId,notificationMechanismType,notificationAddress) values('62721', 'email', 'email4@email.com');
insert into notification_mechanism(subscriptionId,notificationMechanismType,notificationAddress) values('62721', 'email', 'email101@email.com');
insert into notification_mechanism(subscriptionId,notificationMechanismType,notificationAddress) values('62720', 'email', 'email100@email.com');
insert into notification_mechanism(subscriptionId,notificationMechanismType,notificationAddress) values('62720', 'email', 'email2@email.com');

insert into notification_mechanism(subscriptionId,notificationMechanismType,notificationAddress) values('62723', 'email', 'email5@email.com');
insert into notification_mechanism(subscriptionId,notificationMechanismType,notificationAddress) values('62723', 'email', 'email103@email.com');

insert into notification_mechanism(subscriptionId,notificationMechanismType,notificationAddress) values('62724', 'email', 'email105@email.com');
insert into notification_mechanism(subscriptionId,notificationMechanismType,notificationAddress) values('62724', 'email', 'email6@email.com');

insert into notification_mechanism(subscriptionId,notificationMechanismType,notificationAddress) values('62725', 'email', 'email106@email.com');
insert into notification_mechanism(subscriptionId,notificationMechanismType,notificationAddress) values('62725', 'email', 'email7@email.com');

insert into notification_mechanism(subscriptionId,notificationMechanismType,notificationAddress) values('62726', 'email', 'email107@email.com');
insert into notification_mechanism(subscriptionId,notificationMechanismType,notificationAddress) values('62726', 'email', 'email8@email.com');
insert into notification_mechanism(subscriptionId,notificationMechanismType,notificationAddress) values('62729', 'email', 'ucn@ojbc.local');
insert into notification_mechanism(subscriptionId,notificationMechanismType,notificationAddress) values('62730', 'email', 'no.ucn@ojbc.local');

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
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62723', 'firstName', 'Test');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62723', 'lastName', 'Jane');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62723', 'SID', 'A123457');

insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62724', 'subscriptionQualifier', '2110224');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62724', 'dateOfBirth', '1990-10-12');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62724', 'firstName', 'Test');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62724', 'lastName', 'Jane');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62724', 'SID', 'A123458');

insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62725', 'subscriptionQualifier', '2110225');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62725', 'dateOfBirth', '1989-10-12');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62725', 'firstName', 'Lisa');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62725', 'lastName', 'Simpson');

insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62726', 'subscriptionQualifier', '2110226');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62726', 'dateOfBirth', '1987-10-10');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62726', 'firstName', 'Bart');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62726', 'lastName', 'Simpson');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62726', 'SID', 'A567890');

insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62727', 'SID', 'A123457');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62729', 'firstName', 'James');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62729', 'lastName', 'Trombley');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62729', 'subscriptionQualifier', 'ab59b6eb-de9f-4b09-869b-978be1ec7fe8');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62729', 'dateOfBirth', '1980-01-22');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62729', 'FBI_ID', '830420WX6');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62729', 'SID', 'A1643126');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62730', 'firstName', 'Mary');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62730', 'lastName', 'Billiot');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62730', 'subscriptionQualifier', 'd32f5f8c-8359-443c-869d-f1ff57d5c3b0');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62730', 'dateOfBirth', '1960-01-11');
insert into subscription_subject_identifier(subscriptionId, identifierName, identifierValue) values('62730', 'SID', 'A2588583');

insert into subscription_properties(subscriptionId, propertyName, propertyValue) values('62726','ARREST','ARREST');
insert into subscription_properties(subscriptionId, propertyName, propertyValue) values('62726','NCIC-WARRANT-ENTRY','NCIC-WARRANT-ENTRY');
insert into subscription_properties(subscriptionId, propertyName, propertyValue) values('62726','NCIC-WARRANT-MODIFICATION','NCIC-WARRANT-MODIFICATION');
insert into subscription_properties(subscriptionId, propertyName, propertyValue) values('62726','NCIC-WARRANT-DELETION','NCIC-WARRANT-DELETION');
insert into subscription_properties(subscriptionId, propertyName, propertyValue) values('62726','federalRapSheetDisclosureIndicator','true');
insert into subscription_properties(subscriptionId, propertyName, propertyValue) values('62726','federalRapSheetDisclosureAttentionDesignationText','Bill Padmanabhan');

insert into subscription_properties(subscriptionId, propertyName, propertyValue) values('62729','ARREST','ARREST');
insert into subscription_properties(subscriptionId, propertyName, propertyValue) values('62729','agencyCaseNumber','case01');
insert into subscription_properties(subscriptionId, propertyName, propertyValue) values('62730','ARREST','ARREST');
insert into subscription_properties(subscriptionId, propertyName, propertyValue) values('62730','agencyCaseNumber','case2');

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
	values ('1', 'B1234568','A123457', 'C1234567', '1990-10-12', 'Test', 'Jane', 'W','F');
insert into IDENTIFICATION_SUBJECT(subject_id, ucn, civil_sid, criminal_sid, dob, first_name, last_name, middle_initial, sex_code) 
	values ('2', 'B1234569','A123458', 'C1234568', '1987-10-10', 'Bart', 'Simpson', 'C','M');
insert into IDENTIFICATION_SUBJECT(subject_id, ucn, civil_sid, criminal_sid, dob, first_name, last_name, middle_initial, sex_code) 
	values ('3', '9222201','A123459', 'C1234569', '1989-10-12', 'Lisa', 'Simpson', 'W','F');
insert into IDENTIFICATION_SUBJECT(subject_id, ucn, civil_sid, criminal_sid, dob, first_name, last_name, middle_initial, sex_code) 
	values ('4', '9222202','A023460', 'C0234569', '1960-10-12', 'Homer', 'Simpson', '','M');
	
insert into IDENTIFICATION_SUBJECT(subject_id, ucn, civil_sid, criminal_sid, dob, first_name, last_name, middle_initial, sex_code) 
	values ('5', '1234','A123461', 'C1234570', '1989-10-12', 'Unsubscribe', 'FbiUcnTest', 'W','F');	
	

insert into IDENTIFICATION_TRANSACTION (TRANSACTION_NUMBER, SUBJECT_ID, OTN, OWNER_ORI, OWNER_PROGRAM_OCA, IDENTIFICATION_CATEGORY, ARCHIVED, Subscription_ID, AVAILABLE_FOR_SUBSCRIPTION_START_DATE, CREATION_TIMESTAMP, REPORT_TIMESTAMP) 
	values ('000001820140729014008339990', '1', '0400024', '68796860', 'ID12345', 'F', 'false', '62724', '2015-10-16', '2015-10-16', '2015-10-16');
insert into IDENTIFICATION_TRANSACTION (TRANSACTION_NUMBER, SUBJECT_ID, OTN, OWNER_ORI, OWNER_PROGRAM_OCA, IDENTIFICATION_CATEGORY, ARCHIVED, AVAILABLE_FOR_SUBSCRIPTION_START_DATE, CREATION_TIMESTAMP, REPORT_TIMESTAMP) 
	values ('000001820140729014008339991', '2', '12344', '68796860', 'ID12345', 'CAR', 'false','2015-08-01', '2015-08-01', '2015-08-01' );
insert into IDENTIFICATION_TRANSACTION (TRANSACTION_NUMBER, SUBJECT_ID, OTN, OWNER_ORI, OWNER_PROGRAM_OCA, IDENTIFICATION_CATEGORY, ARCHIVED, Subscription_ID, AVAILABLE_FOR_SUBSCRIPTION_START_DATE, CREATION_TIMESTAMP, REPORT_TIMESTAMP) 
	values ('000001820140729014008339993', '1', '0400024', '1234567890', 'ID12345', 'F', 'false', '62723', '2015-10-16', '2015-10-16', '2015-10-16');
insert into IDENTIFICATION_TRANSACTION (TRANSACTION_NUMBER, SUBJECT_ID, OTN, OWNER_ORI, OWNER_PROGRAM_OCA, IDENTIFICATION_CATEGORY, ARCHIVED, CREATION_TIMESTAMP, REPORT_TIMESTAMP) 
	values ('000001820140729014008339994', '2', '12344', '1234567890', 'ID12345', 'CAR', 'false', '2015-10-10', '2015-10-10');
insert into IDENTIFICATION_TRANSACTION (TRANSACTION_NUMBER, SUBJECT_ID, OTN, OWNER_ORI, OWNER_PROGRAM_OCA, IDENTIFICATION_CATEGORY, ARCHIVED, Subscription_ID, AVAILABLE_FOR_SUBSCRIPTION_START_DATE, CREATION_TIMESTAMP, REPORT_TIMESTAMP) 
	values ('000001820140729014008339995', '3', '0400025', '1234567890', 'ID12345', 'J', 'false', '62725', '2016-10-20', '2016-10-20', '2016-10-20');
insert into IDENTIFICATION_TRANSACTION (TRANSACTION_NUMBER, SUBJECT_ID, OTN, OWNER_ORI, OWNER_PROGRAM_OCA, IDENTIFICATION_CATEGORY, ARCHIVED, Subscription_ID, AVAILABLE_FOR_SUBSCRIPTION_START_DATE, CREATION_TIMESTAMP, REPORT_TIMESTAMP) 
	values ('000001820140729014008339996', '2', '12344', '1234567890', 'ID12345', 'F', 'false', '62726', '2016-10-20', '2016-10-20', '2016-10-20');
insert into IDENTIFICATION_TRANSACTION (TRANSACTION_NUMBER, SUBJECT_ID, OTN, OWNER_ORI, OWNER_PROGRAM_OCA, IDENTIFICATION_CATEGORY, ARCHIVED, CREATION_TIMESTAMP, REPORT_TIMESTAMP) 
	values ('000001820140729014008339997', '4', '0400026', '1234567890', 'ID12345', 'J', 'true', '2015-10-20', '2015-10-20');
	
insert into IDENTIFICATION_TRANSACTION (TRANSACTION_NUMBER, SUBJECT_ID, OTN, OWNER_ORI, OWNER_PROGRAM_OCA, IDENTIFICATION_CATEGORY, ARCHIVED, Subscription_ID, AVAILABLE_FOR_SUBSCRIPTION_START_DATE, CREATION_TIMESTAMP, REPORT_TIMESTAMP) 
	values ('000001820140729014008339998', '5', '0400025', '1234567890', 'ID12345', 'F', 'false', '62727', '2016-10-20','2016-10-20','2016-10-20');	

/*http://stackoverflow.com/questions/2607326/insert-a-blob-via-a-sql-script*/
insert into CIVIL_FINGER_PRINTS (TRANSACTION_NUMBER, FINGER_PRINTS_FILE, FINGER_PRINTS_TYPE_ID) 
	values ('000001820140729014008339990', '78DA7373F274CE2CCBCC71CBCC4B4F2D0A28CACC2B2906004B1A07A4', '1');
insert into CIVIL_FINGER_PRINTS (TRANSACTION_NUMBER, FINGER_PRINTS_FILE, FINGER_PRINTS_TYPE_ID) 
	values ('000001820140729014008339990', '78DA0B2E492C4975CE2CCBCC71CBCC4B4F2D0A28CACC2B290600638C08D4', '2');
insert into CIVIL_FINGER_PRINTS (TRANSACTION_NUMBER, FINGER_PRINTS_FILE, FINGER_PRINTS_TYPE_ID) 
	values ('000001820140729014008339993', '78DA7373F274CE2CCBCC71CBCC4B4F2D0A28CACC2B2906004B1A07A4', '1');
insert into CIVIL_FINGER_PRINTS (TRANSACTION_NUMBER, FINGER_PRINTS_FILE, FINGER_PRINTS_TYPE_ID) 
	values ('000001820140729014008339993', '78DA0B2E492C4975CE2CCBCC71CBCC4B4F2D0A28CACC2B290600638C08D4', '2');
insert into CIVIL_FINGER_PRINTS (TRANSACTION_NUMBER, FINGER_PRINTS_FILE, FINGER_PRINTS_TYPE_ID) 
	values ('000001820140729014008339995', '78DA7373F274CE2CCBCC71CBCC4B4F2D0A28CACC2B2906004B1A07A4', '1');
insert into CIVIL_FINGER_PRINTS (TRANSACTION_NUMBER, FINGER_PRINTS_FILE, FINGER_PRINTS_TYPE_ID) 
	values ('000001820140729014008339995', '78DA0B2E492C4975CE2CCBCC71CBCC4B4F2D0A28CACC2B290600638C08D4', '2');
insert into CIVIL_FINGER_PRINTS (TRANSACTION_NUMBER, FINGER_PRINTS_FILE, FINGER_PRINTS_TYPE_ID) 
	values ('000001820140729014008339996', '78DA7373F274CE2CCBCC71CBCC4B4F2D0A28CACC2B2906004B1A07A4', '1');
insert into CIVIL_FINGER_PRINTS (TRANSACTION_NUMBER, FINGER_PRINTS_FILE, FINGER_PRINTS_TYPE_ID) 
	values ('000001820140729014008339996', '78DA0B2E492C4975CE2CCBCC71CBCC4B4F2D0A28CACC2B290600638C08D4', '2');
insert into CIVIL_FINGER_PRINTS (TRANSACTION_NUMBER, FINGER_PRINTS_FILE, FINGER_PRINTS_TYPE_ID) 
	values ('000001820140729014008339997', '78DA7373F274CE2CCBCC71CBCC4B4F2D0A28CACC2B2906004B1A07A4', '1');
insert into CIVIL_FINGER_PRINTS (TRANSACTION_NUMBER, FINGER_PRINTS_FILE, FINGER_PRINTS_TYPE_ID) 
	values ('000001820140729014008339997', '78DA0B2E492C4975CE2CCBCC71CBCC4B4F2D0A28CACC2B290600638C08D4', '2');

--insert into CRIMINAL_FINGER_PRINTS (TRANSACTION_NUMBER, FINGER_PRINTS_FILE, FINGER_PRINTS_TYPE_ID) 
--	values ('000001820140729014008339991', RAWTOHEX('FBICriminalFingerPrints'), '1');
--insert into CRIMINAL_FINGER_PRINTS (TRANSACTION_NUMBER, FINGER_PRINTS_FILE, FINGER_PRINTS_TYPE_ID) 
--	values ('000001820140729014008339991', RAWTOHEX('StateCriminalFingerPrints'), '2');

insert into CIVIL_INITIAL_RESULTS (CIVIL_INITIAL_RESULT_ID,TRANSACTION_NUMBER, SEARCH_RESULT_FILE, 
	RESULTS_SENDER_ID)
	values ('1', '000001820140729014008339990', '78DAAD56DB6EDA40107D47E21FA60F2DAD14C72609101C83E40229A0042A20EAF3628FE3558CEDEEAE4368D47FEFAC3125516EE482846DADE7CC39677677D64EA81651DB0991F96D47AA5584A05629B62A0A6F94E9495969974BCE27C380146E537689C65C20BB3258A050D8C0A2255BC9BF60189B30E5C36D90C4CA08D882472B1BCE328FFB0C3A492C9308F760C6172861844B98240B169F401E2DF91F041BAAF5F4E65EB6F0A3B299B9B9B663E656CB251A9A27FEAAED283627D34BEEABB055A95AD6E70ACC13E1A368552C6D1E1C2528CA27AFFC326E553C8CC97A5E9687D03BF144A52FFABF86D0BB2E53686AC936580DF3D0320FAC6A0DAC43FBE8187E9E6B9545E8166BAE2FE2D9CCCE7436198F7EB49D0B0AEBB325E31C3A822F78CC22186652710F81C8A9703940335D509A0DEC195AD0B1B9D13738BEA76BE0D3200FB8C7144F629822135E08139459A45E294889F5F3230F2F687D7232DD348D485A9C4BD9D6FCC978D29DD222440844B280D3EF03F83A9DF4BE3D82DE51E823CBEB1E758481D2C323A697CFAC379DEDC1D01DF59ED5BB0175C7DF6D38672BA836A0DA6C1CEF049A4E4736D41A35A3D9341AF5467D3710DED870DA3B77CF765336611ED9E9BBBFDCC1C01DED5CBE67F8EF2C2BF2ECCE3AFD0FC83AE8DAE0D68E1B87D57A6D275F7A45BC69AA7260B698EBF6DA3CA09F65BD5FFF784653691D59967570B4938A71C75D2B7F3FB77B89B147ADBBDF19763BAF4CA7AF0F12CE422E41A0475D1A422681098152C9F54EF47910A0A04D0432C9848772BF5CFA12CF657AA281083CE68A5357D465169B3DBCE451446F7CDD9BE800A4B002AC4F09012691B2D8875592810C932CF2C1A36386C7190527E085E85D414603D1FF945A156A91C8AFD15F4B6344F250940BBF33142B48822D596738981A451F2F08E708298A20110B4A47A40295E078BD565B847A9B964F055209E52C8AC4638DCBDBEE7E3101F9EDE529289AD4F6BE3E33CDFCABA15CFA072B1D8289', '1');
insert into CIVIL_INITIAL_RESULTS (CIVIL_INITIAL_RESULT_ID,TRANSACTION_NUMBER, SEARCH_RESULT_FILE, 
	RESULTS_SENDER_ID) 
	values ('2', '000001820140729014008339990', '78DAAD555D6FA240147D37F13FDC3E6C782961F00B659184A25D35151BA1D9E70106992C5F81314A9BFDEF3B608D356B5DBA2D09C36438E7DE73EEDCCC68218B235D0B09F675AD606544809519190B8CEC99E41585A0B75BDA8D2842062F19DE10D1CD09FE25E280915C051CED7059FC06513CC2980F2F419A3031C0318D4A151EB61EF531986952A411B90587C6A4008BEC609DC638F90E35BAA0CF04549007D9FE2C5AF855D1A4DA9CAE49B5D5768B2FB9A95FEA1AC32E37BDA33E0BC7828CD03701DC34F7493E1650651E34967394CFBDD24D32163C9270EB7559FEA6BEC1F354D550BD070AFF37C18C4895641590227591D441721F5057ED0DE17159A97C859EB8D261C8AF46D66C67BDB27EE8DA1387CDF00E530A664E639AE00816DB82518F004FCE0B5713AA4C4F3CCC9176252D54D8DAE87F383ED335F7F9220DA887194D13B009CEBD10D6A4D846EC8382587E985F98FC43EBBB9B696459C4A525B59453CDDFC573DD196F4202419EC66033BEB317980D455E68ADB3B4110958B56CE1AA759CA9EDDCC2C2B0A657B51E4993D59D0A4B5C82AC803C52868D48B66DA9D057FAE268242A0365D08C44F62ADC4F97C64333656BEC713B33E3A7319F1B56E3F25DC9FFA6A5B867C331675F10753E51C1E80F95AE3CE837F2757F37076B1BBBD50939EAF007A1CFCB58397C47500F21D4E93552B1328D43AF7C3EB7B12189C74FDF99B998981F09F7DAE8A7EFE1CC95EA5BA7DDFA0317F7EFEF', '2');
insert into CIVIL_INITIAL_RESULTS (CIVIL_INITIAL_RESULT_ID,TRANSACTION_NUMBER, SEARCH_RESULT_FILE, 
	RESULTS_SENDER_ID)
	values ('3', '000001820140729014008339993', '78DAAD56DB6EDA40107D47E21FA60F2DAD14C72609101C83E40229A0042A20EAF3628FE3558CEDEEAE4368D47FEFAC3125516EE482846DADE7CC39677677D64EA81651DB0991F96D47AA5584A05629B62A0A6F94E9495969974BCE27C380146E537689C65C20BB3258A050D8C0A2255BC9BF60189B30E5C36D90C4CA08D882472B1BCE328FFB0C3A492C9308F760C6172861844B98240B169F401E2DF91F041BAAF5F4E65EB6F0A3B299B9B9B663E656CB251A9A27FEAAED283627D34BEEABB055A95AD6E70ACC13E1A368552C6D1E1C2528CA27AFFC326E553C8CC97A5E9687D03BF144A52FFABF86D0BB2E53686AC936580DF3D0320FAC6A0DAC43FBE8187E9E6B9545E8166BAE2FE2D9CCCE7436198F7EB49D0B0AEBB325E31C3A822F78CC22186652710F81C8A9703940335D509A0DEC195AD0B1B9D13738BEA76BE0D3200FB8C7144F629822135E08139459A45E294889F5F3230F2F687D7232DD348D485A9C4BD9D6FCC978D29DD222440844B280D3EF03F83A9DF4BE3D82DE51E823CBEB1E758481D2C323A697CFAC379DEDC1D01DF59ED5BB0175C7DF6D38672BA836A0DA6C1CEF049A4E4736D41A35A3D9341AF5467D3710DED870DA3B77CF765336611ED9E9BBBFDCC1C01DED5CBE67F8EF2C2BF2ECCE3AFD0FC83AE8DAE0D68E1B87D57A6D275F7A45BC69AA7260B698EBF6DA3CA09F65BD5FFF784653691D59967570B4938A71C75D2B7F3FB77B89B147ADBBDF19763BAF4CA7AF0F12CE422E41A0475D1A422681098152C9F54EF47910A0A04D0432C9848772BF5CFA12CF657AA281083CE68A5357D465169B3DBCE451446F7CDD9BE800A4B002AC4F09012691B2D8875592810C932CF2C1A36386C7190527E085E85D414603D1FF945A156A91C8AFD15F4B6344F250940BBF33142B48822D596738981A451F2F08E708298A20110B4A47A40295E078BD565B847A9B964F055209E52C8AC4638DCBDBEE7E3101F9EDE529289AD4F6BE3E33CDFCABA15CFA072B1D8289', '1');
insert into CIVIL_INITIAL_RESULTS (CIVIL_INITIAL_RESULT_ID,TRANSACTION_NUMBER, SEARCH_RESULT_FILE, 
	 RESULTS_SENDER_ID) 
	values ('4', '000001820140729014008339993', '78DAAD555D6FA240147D37F13FDC3E6C782961F00B659184A25D35151BA1D9E70106992C5F81314A9BFDEF3B608D356B5DBA2D09C36438E7DE73EEDCCC68218B235D0B09F675AD606544809519190B8CEC99E41585A0B75BDA8D2842062F19DE10D1CD09FE25E280915C051CED7059FC06513CC2980F2F419A3031C0318D4A151EB61EF531986952A411B90587C6A4008BEC609DC638F90E35BAA0CF04549007D9FE2C5AF855D1A4DA9CAE49B5D5768B2FB9A95FEA1AC32E37BDA33E0BC7828CD03701DC34F7493E1650651E34967394CFBDD24D32163C9270EB7559FEA6BEC1F354D550BD070AFF37C18C4895641590227591D441721F5057ED0DE17159A97C859EB8D261C8AF46D66C67BDB27EE8DA1387CDF00E530A664E639AE00816DB82518F004FCE0B5713AA4C4F3CCC9176252D54D8DAE87F383ED335F7F9220DA887194D13B009CEBD10D6A4D846EC8382587E985F98FC43EBBB9B696459C4A525B59453CDDFC573DD196F4202419EC66033BEB317980D455E68ADB3B4110958B56CE1AA759CA9EDDCC2C2B0A657B51E4993D59D0A4B5C82AC803C52868D48B66DA9D057FAE268242A0365D08C44F62ADC4F97C64333656BEC713B33E3A7319F1B56E3F25DC9FFA6A5B867C331675F10753E51C1E80F95AE3CE837F2757F37076B1BBBD50939EAF007A1CFCB58397C47500F21D4E93552B1328D43AF7C3EB7B12189C74FDF99B998981F09F7DAE8A7EFE1CC95EA5BA7DDFA0317F7EFEF', '2');
insert into CIVIL_INITIAL_RESULTS (CIVIL_INITIAL_RESULT_ID,TRANSACTION_NUMBER, SEARCH_RESULT_FILE, 
	RESULTS_SENDER_ID)
	values ('5', '000001820140729014008339995', '78DAAD556B6FA24014FDDEC4FF70F7C386DDA42C838F5A5924A16A571A854668F6F308834C9657608CBACDFEF79D418D35B5963E489821C339F79E73E732E8114B62438F080E0CBD649B9800DBE4A42F31B2668A5F9692D1B8D0BFC832E4F098E30591E705C17F641C32526880E315DE94FF4096F73016C06398A54C0E7142E38D0693A54F030C832C2DB3985C82471352824D5630CB129CFE840A5DD2BF043450AFF2F551B4E8B3A229953943572AAB8D0BBE34CF828DA1333CE7A6573460515F5211FA2AC13C2B0252F42524CC83CE0A8E0AB857BA48FB924F526EBD2ACB73EA133C4F2506716F29FCDD1033A208C91AA0AED2424A13A91D402DAD7D0DF753A172073D7095ED509C8DACBBDECCB17F19FA03878DF10A530A83822634C531DC2D4B467D023C392F5C4510991E78983DED4C5A10D8CAE83B1C1FE9B202BE4843EA6346B3145C820B3F82192997317BA320566C9F4F3CBCA2F5C5CD34F33CE6D2D24ACAA1E62FE2B9EE9C372181B0C812B8BDB1E09B3B1B7D3FC1AE29F4447B1DA58E49C8C4B28D45FB4C2CD7BC04D79ADEBB8E7D56F29E37746E34707C062A02B577DDAB45725D5B834EB723F77A72F7AADBAD47226B0D6E47537332AA859F619F3B1A9BBF4DCB32EDDA153C93FF496769603B3035BDC1F813025B430D4CB5D96A77EA954FB4C57BF7ABE22E93B938667B4D7E21F5E30E1C8FEF276A23849A9D5A2A9C81A9813772BD8FE7361724F5F9113E1EDC0D076F0C27C66701BD889650109F9FD610E112D20C12CCF8CE87D9320D7EEC5254D3EB49769FE361DEFE1D94EAFFD8B8F80F96A1210F', '1');
insert into CIVIL_INITIAL_RESULTS (CIVIL_INITIAL_RESULT_ID,TRANSACTION_NUMBER, SEARCH_RESULT_FILE, 
	 RESULTS_SENDER_ID) 
	values ('6', '000001820140729014008339995', '78DAAD555D6FA240147D37F13FDC7DD8F052C2E047ADB34842D1AE340A8DD0ECF308834E96AFC018659BFDEF3B608D35B5966E4BC23019CEB9F79C3B3733DA9AC791AEAD290974ADE065448197191D499CEEB8E21785A4B75BDA3759860C9E32B2A2F232A7E4B74C424E730C24DA92B2F80BB27C80F1009EC234E1724862169518661B9F0504CC3429D2885E81C7625A804DB7B0486392FC801A5DB03F1430A8D7D9EE24DAFAABA229B5395D536AABED96585AA641A96B9C2C85E92D0BF87A24A9087D976099E601CD4712AACC83C673810A8457B64A46924F1361BD2ECB6BEA0BBC48550DD5BBA7887F63C2A95249C680064A17291DA4F6017571EF061EE695CA67E891ABEC87FC6264CDF5168EFD53D71E056C4AB68431307316B3844470BF2938F32988E4A27035A1CAF428C21C6817D24285AD8DFE87E3135D56201659C87CC2599A804B49EEAF61418B4DC43F2888E7FBF999C93B5ADFDC4C23CB22212DA9A51C6BFE265EE8CE44135208F33406978B9D3DC36C28F24C6B9DA48D68C8AB659B54AD33B35CE30A5C6BFEE03AF645B907DED8B9C5E0981EA81D508737C34624D7B531F4077D79389407D783413312DD61B89BCC8DD9A4117E417CE1686AFC322CCBB01B57F042FE175D85C176606E78E6F40B025B630C86DAE9F6FACDCA77776B81BD8997D53139EC8807A99F57E178624F500F21D4E93752E19806066FE27A9FCF6DAC68E28B23786ADE8FCD8F847BEEF6E3777FF02AF5D5D36EFD034689F124', '2');
insert into CIVIL_INITIAL_RESULTS (CIVIL_INITIAL_RESULT_ID,TRANSACTION_NUMBER, SEARCH_RESULT_FILE, 
	RESULTS_SENDER_ID)
	values ('7', '000001820140729014008339996', '78DAAD555B6F9B30147E8F94FF70F630B1496540736704895CBA50355001D59E1D30C11A3781A324ABF6DF67934469D434A597078C65CE77BEEF3B3ED85A449358D7228C025D2BE936C640B7391E0A146FA8E497A5A0371BDA1751841C1E73B4C4E2A2C0E88F88428A0B1550BC46DBF21F88E2218C06F01866291543949078ABC2DDCA270182719696598CAFC023092EC1C26B70B204A53FA18A2EC95F0C2A28DD7C73922DFAAC6C52654ED7A4CA6AB3C1961659B0D5358A16CCF49A04341A0A8A2C7F15609115012E8682CCCD83460B161530AF64990E051FA7CC7A5596E7D027F18C8A0FFCD941D8B709A258E29255907B524B96AE65A503724B6DF7E17ECE55EE438F5869371417336BAEE7D8D62F5D7B606133B44684C0B8200949510CB7AB92121F03236785AB009CE981A539C02ED0028FAD8CBEC3F1892E33608B24243EA2244BC1C5A8F0237070B98AE91B05D162373F337945EB8B9B69E479CCA4A5959463CD5F8C67BA73D68418C2224BE06664C237D7997E3F83AE29F44C7B9D50C738A47CD942BC7D4686E35D816BCEEF5DDBBA28F9809BD823156C9F82228332E8F76A815CD752A1D3EB888381D8EBF6FAF54078A3C2DCB89BD68A7690CFFCCC8CDF86691A56EDFA5D607FD2572A583693E28D679F90D89CA86028D7AD76A75E1D7853BC77B72AEC2A59F04376547176071FB7607B6C3B79B6762D11F6D850C19BBADEC7998D254E7D767ECFC6B793F11BD3F1F159422F222514D867473544A88434830451B6F161B64A831F7B8AEAF53AC9FE5F3CBE775783545D8ECDC67F92662073', '1');
insert into CIVIL_INITIAL_RESULTS (CIVIL_INITIAL_RESULT_ID,TRANSACTION_NUMBER, SEARCH_RESULT_FILE, 
	 RESULTS_SENDER_ID) 
	values ('8', '000001820140729014008339996', '78DAAD554D6FA33010BD47CA7F981E565C8A80E6DB4B9008E96EA81AA802D59E1D3089B57C091C25B4DAFFBE366994464DB374DB03C632EFCDBC371E0DFA9A25B1A1AF090E0DBD64554C805539194B8CEC981294A564B45BFA952C430ECF395E11795910FC5BC6112305021C6F7155FE01593EC05808CF51963239C2098D2B04F79B808618AC2C2DB3985C834F13528243B6B0C8129C7E871A5DD2270208B47EBE3B89B6FEAA684A6DCED095DA6ABBC58F965958193AC34B6E7A4B43B61E4B9AAA7E936099152129C6922ACC83CE0A8E0AB957BA4AC75240526EBD2ECB5BEA2B3C4F2516F1EC29FCDB1433A208C908D481D251951B55EB81DA41DD213CCC85CA17E891ABEC97E26264DDF317AEF3D3D01F396C86B79852B00A9AD014C770B729190D08F0E4BC703541647AE4610EB40B6941606BA3FFE1F844971DF2431AD100339AA5E0115C046B58907213B30F0A62C57E7F66F30FADEF5EA699E7319796D6528E357F17CF75E7BC090944459680C7F8CD9E61361479A6B54ED2C62462E2D8C1A27526E6C2BF06CF9E3F78AE7351EE81377527085CCB074D056D341C3422799E83A037E8C9A3913CE80F86CD486487606EDEDF36422F70C0FDCCCC5FA66D9B4EE3FA5DC8FEAAA710382E97E25BB32F086C4F1198DA4DA7DB6B56871F131B9C4DB214437252F3FAA3CFCB707D7E25225AB79108D73211F8B79EFFF9CCE68AA4019FBF33EB6E6A7D24DC4BAB1FDFFBA9ABD4FF9D76EB2FAE02F086', '2');
insert into CIVIL_INITIAL_RESULTS (CIVIL_INITIAL_RESULT_ID,TRANSACTION_NUMBER, SEARCH_RESULT_FILE, 
	RESULTS_SENDER_ID)
	values ('9', '000001820140729014008339997', '78DAAD555D6F9B30147DAF94FF70F730B1496598244B1A469028C996540D5440B567074CB0C697C0519255FBEFB349A2346A9AD20F246C64CEB9F79CEB8BD1639626861E131C1A7AC5360901B629C8506264CD94A0AA24A375A17F926528E0A1C00B22CF4B82FFC83862A4D400272BBCA9FE812CEF612C848728CF981CE194261B0D6E97010D31587956E509B9049FA6A4029BACC0CD539CFD801A5DD1BF0434507BC5FA285AFC51D194DA9CA12BB5D5D6055F9AE7E1C6D0199E73D32B1AB27828A9087D96609E972129871212E64167254785DC2B5D6443292019B75E97E529F5119EA71283B8B714FE6E84195184640D505FE920A58DD4EF803A5AF70AEE6642E50E7AE02ADBA13C1B59F77CD7B17F19FA3D874DF00A530A5649539AE1046E9615A301019E9C17AE26884CF73CCC9E76262D086C6DF40D8E8F744D43BE48231A6046F30C3C82CB20069754CB84BD52102BB7CF271E5ED0FAEC669A45917069592DE550F367F15C77C19B904054E629FCBC9EC217CF1D7F3DC16E28F4447B1DA54E48C4C4B28D45FB4C9CD9D8BD046F3ABBF31CFBACE63D71E45C6BE0040CD436A8831E6A44F23C5B832E42F26020F77BFDAB6624B2D66066DE8E1BA15D1C0843E66F733A35EDC6053C93FD516369603B5C8A6F4D3E20F074A48189DA9D6EC3E289AE78F376D5E4653A17C7ECA0CD2FD47EBF05C7E7DBA97207DD461A1CCBD4C01F7BFEFB339B0B9205FC009F583723EB95E1C4F824A01FD30A4A12F0B31A625C4196438A19DFF8285F66E1B75D8A7A7A39C9EE633CCCDB7F8352FF1D5B17FF018E1A20AE', '1');
insert into CIVIL_INITIAL_RESULTS (CIVIL_INITIAL_RESULT_ID,TRANSACTION_NUMBER, SEARCH_RESULT_FILE, 
	 RESULTS_SENDER_ID) 
	values ('10', '000001820140729014008339997', '78DAAD555D6FDA30147D47E23FDC3D4C7969140718142F444A031BA94A5291547B368903D6F2A5C40858B5FF3E3B1451544AD3B591E258CE39F79E737D651B2B9E26A6B1A224328D8AEF120A7C57D091C2E9966B61552966BB657C515528E0B1204BAA2E4A4A7EAB24E6B4C440920DD9557F41550F301EC1639C675C8D49CA921D86BB75C82202769E557942AF206029ADC0A51B98E729C9BE438DAED81F0A18F47EB13D89B6FAAC685A6DCE34B4DA6ABB25961679B4330D4E16C2F486457C35527484BE2AB0C8CB8896230549F360F052A022E1952DB39112D24C58AFCBF292FA0C2F52C941BE7B8AF837269C6A52320634D0BA48EB20FD1BA02EEE5DC3FD4CAA7C821EB9DA7E282F4636FC60EEB93F4DE341C0A664431803BB6429CB4802B7EB8AB39082482E0A571364A60711E640BB901624B636FA1F8E4F7439915864310B096779063E2565B88239ADD6097FA7205EEEE767266F687D7533ADA24884B4AC9672ACF9AB78A1BB104D48212EF3147C2E76F60CB3A1C833AD759236A13197CB2E91AD33F56693F915F8CEECDEF7DC8B7A0FC4B17783C1B303D03BA00FFBA811C9F75D0C3D84D4E1501DF407D7CD48748B6166DD4D1AA1E7249486AC5F96E3586EE3025EC8FEACA930B89E9012D8D34F08EC8C3158A8D3ED352CDE8F1B07DC75BA90A7E4B0231ED4F9B80A2F103BA20B11BD461A3CDBC2104CFCE0E399AD25CD4271FE4EEDDBB1FD9E704FAD7EFCEE4F5DADBE77DAAD7F8970F06F', '2');

--NSOR Demographics compressed as 78DAF30BF60F527049CDCD4F2F4A2CC8C84C2E060033BC0639
--NSOR Demographics 1 compressed as 78DAF30BF60F527049CDCD4F2F4A2CC8C84C2E56300400409F068A
--NSOR Search Results compressed as 78DAF30BF60F52084E4D2C4ACE50084A2D2ECD292906003EE306CB
--NSOR Search Results 1 compressed as 78DAF30BF60F52084E4D2C4ACE50084A2D2ECD2929563004004CEA071C
insert into NSOR_DEMOGRAPHICS (NSOR_DEMOGRAPHICS_ID,TRANSACTION_NUMBER, DEMOGRAPHICS_FILE, 
	RESULTS_SENDER_ID)
	values ('1', '000001820140729014008339990', '78DAF30BF60F527049CDCD4F2F4A2CC8C84C2E060033BC0639', '1');
	
insert into NSOR_DEMOGRAPHICS (NSOR_DEMOGRAPHICS_ID,TRANSACTION_NUMBER, DEMOGRAPHICS_FILE, 
	RESULTS_SENDER_ID)
	values ('2', '000001820140729014008339990', '78DAF30BF60F527049CDCD4F2F4A2CC8C84C2E56300400409F068A', '1');

insert into NSOR_SEARCH_RESULT (NSOR_SEARCH_RESULT_ID,TRANSACTION_NUMBER, SEARCH_RESULT_FILE, 
	RESULTS_SENDER_ID)
	values ('1', '000001820140729014008339990', '78DAF30BF60F52084E4D2C4ACE50084A2D2ECD292906003EE306CB', '1');

insert into NSOR_SEARCH_RESULT (NSOR_SEARCH_RESULT_ID,TRANSACTION_NUMBER, SEARCH_RESULT_FILE, 
	RESULTS_SENDER_ID)
	values ('2', '000001820140729014008339990', '78DAF30BF60F52084E4D2C4ACE50084A2D2ECD2929563004004CEA071C', '1');
	
	
insert into NSOR_FIVE_YEAR_CHECK(NSOR_FIVE_YEAR_CHECK_ID,TRANSACTION_NUMBER, FIVE_YEAR_CHECK_FILE)
	values ('1', '000001820140729014008339993', '78DAF30BF60F52084E4D2C4ACE50084A2D2ECD292906003EE306CB');
	
insert into NSOR_FIVE_YEAR_CHECK (NSOR_FIVE_YEAR_CHECK_ID,TRANSACTION_NUMBER, FIVE_YEAR_CHECK_FILE)
	values ('2', '000001820140729014008339993', '78DAF30BF60F52084E4D2C4ACE50084A2D2ECD2929563004004CEA071C');
	
insert into CIVIL_INITIAL_RAP_SHEET (CIVIL_INITIAL_RESULT_ID, RAP_SHEET) values ('1', '78DAB558DB6EDB48127D37E07F68CC0E307620D9A415DD122CB02DB2657542911A5EEC78DE1889B63923915A928AD7F3F57BAA49EA6253CE22C87680D8EEAEEBA9EAAA6A1A9FA4E70BCFBF383D31DC8BDB9BF148DA634DBB30EAFD7753579A57BDBEDE7B0F1AFF8B7F48787AA20F3E74DF33AD7FD9D12EAF34BDCB34ADDB6BDED73A3A9B7C2957256B62BA975796A66BFA70D6DAA93A3DE1BE7DF989DB829115EC1FBA5632BD6B5C8C19AE9C4A9B5B6C02BB1DF78EB9C2705C93B1668677A727C764D512655264E962332FE234392A662BCB7F8C7396856B963F4651C19EC29CAD157BB46071C2B2285FA7491EB12265C563C4EED3E5327D8A93079CFC7B13E5C50712024C99BD597D8D3276B08635646E49CCE4E290E0F464B6C9D629E41BE922DA3F9140B228A2A4F4E2601DA0ABE97AB7F42382BDF769B60A150B6C2F0E5DC31FF9E6EB9FD1BC78EDCC3CFC168545AE9C616781F79151E4DB5ABFDDD1CE7FEE560978344FB30599F435CC81749A2C9FF19FB28AC04C4A30E1C473BAC96AACDB81617F00665B5847D13CDC00BC70B188C9EB9CA5195B44CBA8FC63153EB3AF117E00DAB06061F2CC8A7815B558C892E80982E6E9FA19E8A49BE582082B35B0E7E9314A40132DF03B3025E0723A4C0A067D17A55BA72787BE1E2C1FF95CE7327EF382D12761F8CC777022B6C463C7B29C5B695FB3C0138CDB26849AD2F304DD095F3A364478BE2B0DFADD035BC519D8A670D9CC756EA44727CC133E84B9FE84499BF9D2B70499376831C3310573C66C2CC0C12DA4A2B80E2C25DB6367C6D83D6F8159C9675DED42BF6AB1910331D7CE8D70EDA9B07DDC4DE4224CB31D7B7FF7F4043FA42F85C778E04F1C57FE214C7210BE4E25CC815BC29DB9D2F6BDCA35E021E48D50219626B18FA5A16C5167C0CA63D300896D3B38BA23A8E08F296FA41970CB83843DA1A46CC27D45B4AFEBF4E4565A161B09C25419644C84F159D16DAB0D0AD841BDF10823256924512E0F8DDBD18C7C2E6D081DBBCE549197777FCAEFB6FA3CC712D61D0503B90E8A59E0CE1C04D715BF07882528080BE28093C4B51F719C3A81EF41BD925EE205DF402366DCF509F916B62D45CAAF856DDC31C7658818F20161DA054205E7EE0295645CE10893A615D8F0A9925FE5670D9532E677804DF0739BF1D9CC020AA495189C313091C06FCA3F53D6AA5C36852FDC6DC202C7D3132F903E1F490B1610120C3284ED1107FE10D399E5DC912BCC9B70844AE571E9325CA83556F83AB319F23AB04914C5D2013714B6489241EC02A157B4DC3002971320E356296BCF6748321CBB0A9FB4EB58B3C6485F30756E0A439A0AFDADDF706DE20496A98207D632474BF7C40BE7469C402DA13EB44592397B0108608255FAB0F57EC23D64874008C6603429DC7449B8E7D87C84EBEDCBA9282306112ED51602A442E765C4155A24112E590A02DF41E070571DF8CAC99CDD3D63958BC8CD19CA4F3015A5BBD781B47C42170C7784BD7B2D2EB94B150AD7000A6E27D298949EB982B2CA76E8664222920A97402AE73DBFCCF3831474287B5516DF4A6F2FA784B9CBE01D36A099A0ECA874287DDF13C5A94EA0FE805C70D723CFEA68FF96339A8F1879EAED5F6CEF0E3773DA7AA1A60E361A0DB64DF0ECD71C64AD21CCC0AD0C9970E4618BED05239899BC0A455DDE1877E15C43B156A57AECEE6AB1DEBBE8D0E0F69D0EBADA60AA40EBDA6CBB287AE63C4DFEDC24E500F414178FAAADCE375946FD8B5A7CB85E2FE3793928B4AB6EF842E0AE1752FFBBDF149B2C3ADEFBF872C942C8072F746471949311451827E518A54611BA6E959610C2EACEAF2CBAC7141265EB2C8685F374B50EB338A7161E260BB68E3292544F2D79B8A25167117F8B179B7089FC68B288E6211A0DD2FB6A0EDA4E1C59F4B05986E4D9D767B60C9F28C1D47484B90F222B8FC9A8F4FE3E9EC7E1B21444E09239D554B1439CC8C9B07535CA6DA1BBA001EF75FCDE1C60D9CB8AC4BE33C07AD54C670396B35C89FF247C8FEEBC1A14511F50875AE5C0C8CEF8677E4E375F54C7137ECBA53C3CF0020F25A1DE1A5BD4557DC7C6093AEB946F4F6A3D28ACB614AFB6AF85830AE1FBBB13B2A159EFDEC994BBF29045B8A5F977DBDD9DDB6694CFB3784DA9FCD624EE1588388DDF4D87D5283EBCC2D2B45727BEAD0D07FD5EF77D879DF9F6393BB26EFC21AD013BBBF15F8D83BB3591BC3BE877F45E979D4DE41B84168739DD5E077456E573AAB2D18B7097E3E2F9B82FB547B5DD57F8F5AADF19747A83E1E949B7DF1D0EFBBD7E8F64426AF49F4676379C47ECAD05B4A355B86C20BA7D8C8B88644FA2F8E1B1682038B26F529470654771563CC2D2DFB4C12F0D9A755D6BB4481FF6AEDA9ADED67AC7613DB6F461BFDFD6B51FE4EDE93FAEB7DB53BCFA0FF16A3FCEDB1FB4B56E5BEFAB4085718637E8327D914EE2396ADA6E4E8751963E250D27A3E5466583370FB3BCC5A661F6177E502DF5C3A248D31C35EAC5F3779B10BBEB8D3A93AE56682F15AF5C850F1158314E303E3231901F2ED40CEEFB8E4393863A1734E6608846139EBE52D4C2AEAABA34E581A045CF1D6F8609107BE0BBB6388674AF81EF96A60C569F97F6B80D3AF6ED71E5F5A4B4A3BC83B3255DB63AF1773C069EB57F4749FE18AF8F81EE474912E579F412BE2089A9C7A9C297975ADE6A3C0D5F62D8DB8DE79F6F2F663CCF511B344D676F13E21D1966CB986607F18D26145504CA141D0EDB7A99DEED9FB49001E59CB2D5B2BB103B6D158D8111E5A0CA7EFF84BEA6F08728993F6FEBA8D4B4F7BA86C6E219D20F68249C39F462C0303EA3AF728F61F6D090FC7B77BA22B110D10C0D8046CA31464CBCC5D9AF3A7DCB7BD1ECA26F916A104882BF12BA93C7B55C1DD7428DF853C06D0E7B29B7D5C45C7E79D099F3C7FFA2F5E745CD4837198216E718B1E2BD8F6267DB343BAF895EC586D5278781A98F7E2EFE07269E619EB77DBCD2F170E8B1A9431F8266C22D5F12B79778428CCEFFEFB17961D158BDFF7EC5A473FE464160F4E1457C51AF3DFACC409F79BE53101AD02D13A321ED3FEEAEC5C72DA75885F192F1C502172927CE4D9948FF5AA6F370F998E60548CB53F5A1F2D884E5CC30AABA926957838EAE0F3AC3AACABE63F44FD82679553DC8CABDFF02322F8890');	
insert into CIVIL_INITIAL_RAP_SHEET (CIVIL_INITIAL_RESULT_ID, RAP_SHEET) values ('1', '78DAB558DB6EDB48127D37E07F68CC0E307620D9A415DD122CB02DB2657542911A5EEC78DE1889B63923915A928AD7F3F57BAA49EA6253CE22C87680D8EEAEEBA9EAAA6A1A9FA4E70BCFBF383D31DC8BDB9BF148DA634DBB30EAFD7753579A57BDBEDE7B0F1AFF8B7F48787AA20F3E74DF33AD7FD9D12EAF34BDCB34ADDB6BDED73A3A9B7C2957256B62BA975796A66BFA70D6DAA93A3DE1BE7DF989DB829115EC1FBA5632BD6B5C8C19AE9C4A9B5B6C02BB1DF78EB9C2705C93B1668677A727C764D512655264E962332FE234392A662BCB7F8C7396856B963F4651C19EC29CAD157BB46071C2B2285FA7491EB12265C563C4EED3E5327D8A93079CFC7B13E5C50712024C99BD597D8D3276B08635646E49CCE4E290E0F464B6C9D629E41BE922DA3F9140B228A2A4F4E2601DA0ABE97AB7F42382BDF769B60A150B6C2F0E5DC31FF9E6EB9FD1BC78EDCC3CFC168545AE9C616781F79151E4DB5ABFDDD1CE7FEE560978344FB30599F435CC81749A2C9FF19FB28AC04C4A30E1C473BAC96AACDB81617F00665B5847D13CDC00BC70B188C9EB9CA5195B44CBA8FC63153EB3AF117E00DAB06061F2CC8A7815B558C892E80982E6E9FA19E8A49BE582082B35B0E7E9314A40132DF03B3025E0723A4C0A067D17A55BA72787BE1E2C1FF95CE7327EF382D12761F8CC777022B6C463C7B29C5B695FB3C0138CDB26849AD2F304DD095F3A364478BE2B0DFADD035BC519D8A670D9CC756EA44727CC133E84B9FE84499BF9D2B70499376831C3310573C66C2CC0C12DA4A2B80E2C25DB6367C6D83D6F8159C9675DED42BF6AB1910331D7CE8D70EDA9B07DDC4DE4224CB31D7B7FF7F4043FA42F85C778E04F1C57FE214C7210BE4E25CC815BC29DB9D2F6BDCA35E021E48D50219626B18FA5A16C5167C0CA63D300896D3B38BA23A8E08F296FA41970CB83843DA1A46CC27D45B4AFEBF4E4565A161B09C25419644C84F159D16DAB0D0AD841BDF10823256924512E0F8DDBD18C7C2E6D081DBBCE549197777FCAEFB6FA3CC712D61D0503B90E8A59E0CE1C04D715BF07882528080BE28093C4B51F719C3A81EF41BD925EE205DF402366DCF509F916B62D45CAAF856DDC31C7658818F20161DA054205E7EE0295645CE10893A615D8F0A9925FE5670D9532E677804DF0739BF1D9CC020AA495189C313091C06FCA3F53D6AA5C36852FDC6DC202C7D3132F903E1F490B1610120C3284ED1107FE10D399E5DC912BCC9B70844AE571E9325CA83556F83AB319F23AB04914C5D2013714B6489241EC02A157B4DC3002971320E356296BCF6748321CBB0A9FB4EB58B3C6485F30756E0A439A0AFDADDF706DE20496A98207D632474BF7C40BE7469C402DA13EB44592397B0108608255FAB0F57EC23D64874008C6603429DC7449B8E7D87C84EBEDCBA9282306112ED51602A442E765C4155A24112E590A02DF41E070571DF8CAC99CDD3D63958BC8CD19CA4F3015A5BBD781B47C42170C7784BD7B2D2EB94B150AD7000A6E27D298949EB982B2CA76E8664222920A97402AE73DBFCCF3831474287B5516DF4A6F2FA784B9CBE01D36A099A0ECA874287DDF13C5A94EA0FE805C70D723CFEA68FF96339A8F1879EAED5F6CEF0E3773DA7AA1A60E361A0DB64DF0ECD71C64AD21CCC0AD0C9970E4618BED05239899BC0A455DDE1877E15C43B156A57AECEE6AB1DEBBE8D0E0F69D0EBADA60AA40EBDA6CBB287AE63C4DFEDC24E500F414178FAAADCE375946FD8B5A7CB85E2FE3793928B4AB6EF842E0AE1752FFBBDF149B2C3ADEFBF872C942C8072F746471949311451827E518A54611BA6E959610C2EACEAF2CBAC7141265EB2C8685F374B50EB338A7161E260BB68E3292544F2D79B8A25167117F8B179B7089FC68B288E6211A0DD2FB6A0EDA4E1C59F4B05986E4D9D767B60C9F28C1D47484B90F222B8FC9A8F4FE3E9EC7E1B21444E09239D554B1439CC8C9B07535CA6DA1BBA001EF75FCDE1C60D9CB8AC4BE33C07AD54C670396B35C89FF247C8FEEBC1A14511F50875AE5C0C8CEF8677E4E375F54C7137ECBA53C3CF0020F25A1DE1A5BD4557DC7C6093AEB946F4F6A3D28ACB614AFB6AF85830AE1FBBB13B2A159EFDEC994BBF29045B8A5F977DBDD9DDB6694CFB3784DA9FCD624EE1588388DDF4D87D5283EBCC2D2B45727BEAD0D07FD5EF77D879DF9F6393BB26EFC21AD013BBBF15F8D83BB3591BC3BE877F45E979D4DE41B84168739DD5E077456E573AAB2D18B7097E3E2F9B82FB547B5DD57F8F5AADF19747A83E1E949B7DF1D0EFBBD7E8F64426AF49F4676379C47ECAD05B4A355B86C20BA7D8C8B88644FA2F8E1B1682038B26F529470654771563CC2D2DFB4C12F0D9A755D6BB4481FF6AEDA9ADED67AC7613DB6F461BFDFD6B51FE4EDE93FAEB7DB53BCFA0FF16A3FCEDB1FB4B56E5BEFAB4085718637E8327D914EE2396ADA6E4E8751963E250D27A3E5466583370FB3BCC5A661F6177E502DF5C3A248D31C35EAC5F3779B10BBEB8D3A93AE56682F15AF5C850F1158314E303E3231901F2ED40CEEFB8E4393863A1734E6608846139EBE52D4C2AEAABA34E581A045CF1D6F8609107BE0BBB6388674AF81EF96A60C569F97F6B80D3AF6ED71E5F5A4B4A3BC83B3255DB63AF1773C069EB57F4749FE18AF8F81EE474912E579F412BE2089A9C7A9C297975ADE6A3C0D5F62D8DB8DE79F6F2F663CCF511B344D676F13E21D1966CB986607F18D26145504CA141D0EDB7A99DEED9FB49001E59CB2D5B2BB103B6D158D8111E5A0CA7EFF84BEA6F08728993F6FEBA8D4B4F7BA86C6E219D20F68249C39F462C0303EA3AF728F61F6D090FC7B77BA22B110D10C0D8046CA31464CBCC5D9AF3A7DCB7BD1ECA26F916A104882BF12BA93C7B55C1DD7428DF853C06D0E7B29B7D5C45C7E79D099F3C7FFA2F5E745CD4837198216E718B1E2BD8F6267DB343BAF895EC586D5278781A98F7E2EFE07269E619EB77DBCD2F170E8B1A9431F8266C22D5F12B79778428CCEFFEFB17961D158BDFF7EC5A473FE464160F4E1457C51AF3DFACC409F79BE53101AD02D13A321ED3FEEAEC5C72DA75885F192F1C502172927CE4D9948FF5AA6F370F998E60548CB53F5A1F2D884E5CC30AABA926957838EAE0F3AC3AACABE63F44FD82679553DC8CABDFF02322F8890');	
insert into CIVIL_INITIAL_RAP_SHEET (CIVIL_INITIAL_RESULT_ID, RAP_SHEET) values ('2', '78DA0B2E492C4975CE2CCBCC094A2C08CE484D2D010042CC0715');	
insert into CIVIL_INITIAL_RAP_SHEET (CIVIL_INITIAL_RESULT_ID, RAP_SHEET) values ('2', '78DA0B2E492C4975CE2CCBCC094A2C08CE484D2D3102004A130747');	
insert into CIVIL_INITIAL_RAP_SHEET (CIVIL_INITIAL_RESULT_ID, RAP_SHEET) values ('3', '78DAB558DB6EDB48127D37E07F68CC0E307620D9A415DD122CB02DB2657542911A5EEC78DE1889B63923915A928AD7F3F57BAA49EA6253CE22C87680D8EEAEEBA9EAAA6A1A9FA4E70BCFBF383D31DC8BDB9BF148DA634DBB30EAFD7753579A57BDBEDE7B0F1AFF8B7F48787AA20F3E74DF33AD7FD9D12EAF34BDCB34ADDB6BDED73A3A9B7C2957256B62BA975796A66BFA70D6DAA93A3DE1BE7DF989DB829115EC1FBA5632BD6B5C8C19AE9C4A9B5B6C02BB1DF78EB9C2705C93B1668677A727C764D512655264E962332FE234392A662BCB7F8C7396856B963F4651C19EC29CAD157BB46071C2B2285FA7491EB12265C563C4EED3E5327D8A93079CFC7B13E5C50712024C99BD597D8D3276B08635646E49CCE4E290E0F464B6C9D629E41BE922DA3F9140B228A2A4F4E2601DA0ABE97AB7F42382BDF769B60A150B6C2F0E5DC31FF9E6EB9FD1BC78EDCC3CFC168545AE9C616781F79151E4DB5ABFDDD1CE7FEE560978344FB30599F435CC81749A2C9FF19FB28AC04C4A30E1C473BAC96AACDB81617F00665B5847D13CDC00BC70B188C9EB9CA5195B44CBA8FC63153EB3AF117E00DAB06061F2CC8A7815B558C892E80982E6E9FA19E8A49BE582082B35B0E7E9314A40132DF03B3025E0723A4C0A067D17A55BA72787BE1E2C1FF95CE7327EF382D12761F8CC777022B6C463C7B29C5B695FB3C0138CDB26849AD2F304DD095F3A364478BE2B0DFADD035BC519D8A670D9CC756EA44727CC133E84B9FE84499BF9D2B70499376831C3310573C66C2CC0C12DA4A2B80E2C25DB6367C6D83D6F8159C9675DED42BF6AB1910331D7CE8D70EDA9B07DDC4DE4224CB31D7B7FF7F4043FA42F85C778E04F1C57FE214C7210BE4E25CC815BC29DB9D2F6BDCA35E021E48D50219626B18FA5A16C5167C0CA63D300896D3B38BA23A8E08F296FA41970CB83843DA1A46CC27D45B4AFEBF4E4565A161B09C25419644C84F159D16DAB0D0AD841BDF10823256924512E0F8DDBD18C7C2E6D081DBBCE549197777FCAEFB6FA3CC712D61D0503B90E8A59E0CE1C04D715BF07882528080BE28093C4B51F719C3A81EF41BD925EE205DF402366DCF509F916B62D45CAAF856DDC31C7658818F20161DA054205E7EE0295645CE10893A615D8F0A9925FE5670D9532E677804DF0739BF1D9CC020AA495189C313091C06FCA3F53D6AA5C36852FDC6DC202C7D3132F903E1F490B1610120C3284ED1107FE10D399E5DC912BCC9B70844AE571E9325CA83556F83AB319F23AB04914C5D2013714B6489241EC02A157B4DC3002971320E356296BCF6748321CBB0A9FB4EB58B3C6485F30756E0A439A0AFDADDF706DE20496A98207D632474BF7C40BE7469C402DA13EB44592397B0108608255FAB0F57EC23D64874008C6603429DC7449B8E7D87C84EBEDCBA9282306112ED51602A442E765C4155A24112E590A02DF41E070571DF8CAC99CDD3D63958BC8CD19CA4F3015A5BBD781B47C42170C7784BD7B2D2EB94B150AD7000A6E27D298949EB982B2CA76E8664222920A97402AE73DBFCCF3831474287B5516DF4A6F2FA784B9CBE01D36A099A0ECA874287DDF13C5A94EA0FE805C70D723CFEA68FF96339A8F1879EAED5F6CEF0E3773DA7AA1A60E361A0DB64DF0ECD71C64AD21CCC0AD0C9970E4618BED05239899BC0A455DDE1877E15C43B156A57AECEE6AB1DEBBE8D0E0F69D0EBADA60AA40EBDA6CBB287AE63C4DFEDC24E500F414178FAAADCE375946FD8B5A7CB85E2FE3793928B4AB6EF842E0AE1752FFBBDF149B2C3ADEFBF872C942C8072F746471949311451827E518A54611BA6E959610C2EACEAF2CBAC7141265EB2C8685F374B50EB338A7161E260BB68E3292544F2D79B8A25167117F8B179B7089FC68B288E6211A0DD2FB6A0EDA4E1C59F4B05986E4D9D767B60C9F28C1D47484B90F222B8FC9A8F4FE3E9EC7E1B21444E09239D554B1439CC8C9B07535CA6DA1BBA001EF75FCDE1C60D9CB8AC4BE33C07AD54C670396B35C89FF247C8FEEBC1A14511F50875AE5C0C8CEF8677E4E375F54C7137ECBA53C3CF0020F25A1DE1A5BD4557DC7C6093AEB946F4F6A3D28ACB614AFB6AF85830AE1FBBB13B2A159EFDEC994BBF29045B8A5F977DBDD9DDB6694CFB3784DA9FCD624EE1588388DDF4D87D5283EBCC2D2B45727BEAD0D07FD5EF77D879DF9F6393BB26EFC21AD013BBBF15F8D83BB3591BC3BE877F45E979D4DE41B84168739DD5E077456E573AAB2D18B7097E3E2F9B82FB547B5DD57F8F5AADF19747A83E1E949B7DF1D0EFBBD7E8F64426AF49F4676379C47ECAD05B4A355B86C20BA7D8C8B88644FA2F8E1B1682038B26F529470654771563CC2D2DFB4C12F0D9A755D6BB4481FF6AEDA9ADED67AC7613DB6F461BFDFD6B51FE4EDE93FAEB7DB53BCFA0FF16A3FCEDB1FB4B56E5BEFAB4085718637E8327D914EE2396ADA6E4E8751963E250D27A3E5466583370FB3BCC5A661F6177E502DF5C3A248D31C35EAC5F3779B10BBEB8D3A93AE56682F15AF5C850F1158314E303E3231901F2ED40CEEFB8E4393863A1734E6608846139EBE52D4C2AEAABA34E581A045CF1D6F8609107BE0BBB6388674AF81EF96A60C569F97F6B80D3AF6ED71E5F5A4B4A3BC83B3255DB63AF1773C069EB57F4749FE18AF8F81EE474912E579F412BE2089A9C7A9C297975ADE6A3C0D5F62D8DB8DE79F6F2F663CCF511B344D676F13E21D1966CB986607F18D26145504CA141D0EDB7A99DEED9FB49001E59CB2D5B2BB103B6D158D8111E5A0CA7EFF84BEA6F08728993F6FEBA8D4B4F7BA86C6E219D20F68249C39F462C0303EA3AF728F61F6D090FC7B77BA22B110D10C0D8046CA31464CBCC5D9AF3A7DCB7BD1ECA26F916A104882BF12BA93C7B55C1DD7428DF853C06D0E7B29B7D5C45C7E79D099F3C7FFA2F5E745CD4837198216E718B1E2BD8F6267DB343BAF895EC586D5278781A98F7E2EFE07269E619EB77DBCD2F170E8B1A9431F8266C22D5F12B79778428CCEFFEFB17961D158BDFF7EC5A473FE464160F4E1457C51AF3DFACC409F79BE53101AD02D13A321ED3FEEAEC5C72DA75885F192F1C502172927CE4D9948FF5AA6F370F998E60548CB53F5A1F2D884E5CC30AABA926957838EAE0F3AC3AACABE63F44FD82679553DC8CABDFF02322F8890');	
insert into CIVIL_INITIAL_RAP_SHEET (CIVIL_INITIAL_RESULT_ID, RAP_SHEET) values ('4', '78DAB558DB6EDB48127D37E07F68CC0E307620D9A415DD122CB02DB2657542911A5EEC78DE1889B63923915A928AD7F3F57BAA49EA6253CE22C87680D8EEAEEBA9EAAA6A1A9FA4E70BCFBF383D31DC8BDB9BF148DA634DBB30EAFD7753579A57BDBEDE7B0F1AFF8B7F48787AA20F3E74DF33AD7FD9D12EAF34BDCB34ADDB6BDED73A3A9B7C2957256B62BA975796A66BFA70D6DAA93A3DE1BE7DF989DB829115EC1FBA5632BD6B5C8C19AE9C4A9B5B6C02BB1DF78EB9C2705C93B1668677A727C764D512655264E962332FE234392A662BCB7F8C7396856B963F4651C19EC29CAD157BB46071C2B2285FA7491EB12265C563C4EED3E5327D8A93079CFC7B13E5C50712024C99BD597D8D3276B08635646E49CCE4E290E0F464B6C9D629E41BE922DA3F9140B228A2A4F4E2601DA0ABE97AB7F42382BDF769B60A150B6C2F0E5DC31FF9E6EB9FD1BC78EDCC3CFC168545AE9C616781F79151E4DB5ABFDDD1CE7FEE560978344FB30599F435CC81749A2C9FF19FB28AC04C4A30E1C473BAC96AACDB81617F00665B5847D13CDC00BC70B188C9EB9CA5195B44CBA8FC63153EB3AF117E00DAB06061F2CC8A7815B558C892E80982E6E9FA19E8A49BE582082B35B0E7E9314A40132DF03B3025E0723A4C0A067D17A55BA72787BE1E2C1FF95CE7327EF382D12761F8CC777022B6C463C7B29C5B695FB3C0138CDB26849AD2F304DD095F3A364478BE2B0DFADD035BC519D8A670D9CC756EA44727CC133E84B9FE84499BF9D2B70499376831C3310573C66C2CC0C12DA4A2B80E2C25DB6367C6D83D6F8159C9675DED42BF6AB1910331D7CE8D70EDA9B07DDC4DE4224CB31D7B7FF7F4043FA42F85C778E04F1C57FE214C7210BE4E25CC815BC29DB9D2F6BDCA35E021E48D50219626B18FA5A16C5167C0CA63D300896D3B38BA23A8E08F296FA41970CB83843DA1A46CC27D45B4AFEBF4E4565A161B09C25419644C84F159D16DAB0D0AD841BDF10823256924512E0F8DDBD18C7C2E6D081DBBCE549197777FCAEFB6FA3CC712D61D0503B90E8A59E0CE1C04D715BF07882528080BE28093C4B51F719C3A81EF41BD925EE205DF402366DCF509F916B62D45CAAF856DDC31C7658818F20161DA054205E7EE0295645CE10893A615D8F0A9925FE5670D9532E677804DF0739BF1D9CC020AA495189C313091C06FCA3F53D6AA5C36852FDC6DC202C7D3132F903E1F490B1610120C3284ED1107FE10D399E5DC912BCC9B70844AE571E9325CA83556F83AB319F23AB04914C5D2013714B6489241EC02A157B4DC3002971320E356296BCF6748321CBB0A9FB4EB58B3C6485F30756E0A439A0AFDADDF706DE20496A98207D632474BF7C40BE7469C402DA13EB44592397B0108608255FAB0F57EC23D64874008C6603429DC7449B8E7D87C84EBEDCBA9282306112ED51602A442E765C4155A24112E590A02DF41E070571DF8CAC99CDD3D63958BC8CD19CA4F3015A5BBD781B47C42170C7784BD7B2D2EB94B150AD7000A6E27D298949EB982B2CA76E8664222920A97402AE73DBFCCF3831474287B5516DF4A6F2FA784B9CBE01D36A099A0ECA874287DDF13C5A94EA0FE805C70D723CFEA68FF96339A8F1879EAED5F6CEF0E3773DA7AA1A60E361A0DB64DF0ECD71C64AD21CCC0AD0C9970E4618BED05239899BC0A455DDE1877E15C43B156A57AECEE6AB1DEBBE8D0E0F69D0EBADA60AA40EBDA6CBB287AE63C4DFEDC24E500F414178FAAADCE375946FD8B5A7CB85E2FE3793928B4AB6EF842E0AE1752FFBBDF149B2C3ADEFBF872C942C8072F746471949311451827E518A54611BA6E959610C2EACEAF2CBAC7141265EB2C8685F374B50EB338A7161E260BB68E3292544F2D79B8A25167117F8B179B7089FC68B288E6211A0DD2FB6A0EDA4E1C59F4B05986E4D9D767B60C9F28C1D47484B90F222B8FC9A8F4FE3E9EC7E1B21444E09239D554B1439CC8C9B07535CA6DA1BBA001EF75FCDE1C60D9CB8AC4BE33C07AD54C670396B35C89FF247C8FEEBC1A14511F50875AE5C0C8CEF8677E4E375F54C7137ECBA53C3CF0020F25A1DE1A5BD4557DC7C6093AEB946F4F6A3D28ACB614AFB6AF85830AE1FBBB13B2A159EFDEC994BBF29045B8A5F977DBDD9DDB6694CFB3784DA9FCD624EE1588388DDF4D87D5283EBCC2D2B45727BEAD0D07FD5EF77D879DF9F6393BB26EFC21AD013BBBF15F8D83BB3591BC3BE877F45E979D4DE41B84168739DD5E077456E573AAB2D18B7097E3E2F9B82FB547B5DD57F8F5AADF19747A83E1E949B7DF1D0EFBBD7E8F64426AF49F4676379C47ECAD05B4A355B86C20BA7D8C8B88644FA2F8E1B1682038B26F529470654771563CC2D2DFB4C12F0D9A755D6BB4481FF6AEDA9ADED67AC7613DB6F461BFDFD6B51FE4EDE93FAEB7DB53BCFA0FF16A3FCEDB1FB4B56E5BEFAB4085718637E8327D914EE2396ADA6E4E8751963E250D27A3E5466583370FB3BCC5A661F6177E502DF5C3A248D31C35EAC5F3779B10BBEB8D3A93AE56682F15AF5C850F1158314E303E3231901F2ED40CEEFB8E4393863A1734E6608846139EBE52D4C2AEAABA34E581A045CF1D6F8609107BE0BBB6388674AF81EF96A60C569F97F6B80D3AF6ED71E5F5A4B4A3BC83B3255DB63AF1773C069EB57F4749FE18AF8F81EE474912E579F412BE2089A9C7A9C297975ADE6A3C0D5F62D8DB8DE79F6F2F663CCF511B344D676F13E21D1966CB986607F18D26145504CA141D0EDB7A99DEED9FB49001E59CB2D5B2BB103B6D158D8111E5A0CA7EFF84BEA6F08728993F6FEBA8D4B4F7BA86C6E219D20F68249C39F462C0303EA3AF728F61F6D090FC7B77BA22B110D10C0D8046CA31464CBCC5D9AF3A7DCB7BD1ECA26F916A104882BF12BA93C7B55C1DD7428DF853C06D0E7B29B7D5C45C7E79D099F3C7FFA2F5E745CD4837198216E718B1E2BD8F6267DB343BAF895EC586D5278781A98F7E2EFE07269E619EB77DBCD2F170E8B1A9431F8266C22D5F12B79778428CCEFFEFB17961D158BDFF7EC5A473FE464160F4E1457C51AF3DFACC409F79BE53101AD02D13A321ED3FEEAEC5C72DA75885F192F1C502172927CE4D9948FF5AA6F370F998E60548CB53F5A1F2D884E5CC30AABA926957838EAE0F3AC3AACABE63F44FD82679553DC8CABDFF02322F8890');	

insert into CRIMINAL_INITIAL_RESULTS (CRIMINAL_INITIAL_RESULT_ID, TRANSACTION_NUMBER, SEARCH_RESULT_FILE, RESULTS_SENDER_ID) 
			values ('1', '000001820140729014008339991', '78DAF34D2C49CE0000059401EE', '1');
insert into CRIMINAL_INITIAL_RESULTS (CRIMINAL_INITIAL_RESULT_ID, TRANSACTION_NUMBER, SEARCH_RESULT_FILE, RESULTS_SENDER_ID) 
			values ('2', '000001820140729014008339991', '78DAF34D2C49CE0000059401EE', '2');
insert into CRIMINAL_INITIAL_RESULTS (CRIMINAL_INITIAL_RESULT_ID, TRANSACTION_NUMBER, SEARCH_RESULT_FILE, RESULTS_SENDER_ID) 
			values ('3', '000001820140729014008339994', '78DAAD556B6FA24014FDDEC4FF70F7C386DDA42C838F5A5924A16A571A854668F6F308834C9657608CBACDFEF79D418D35B5963E489821C339F79E73E732E8114B62438F080E0CBD649B9800DBE4A42F31B2668A5F9692D1B8D0BFC832E4F098E30591E705C17F641C32526880E315DE94FF4096F73016C06398A54C0E7142E38D0693A54F030C832C2DB3985C82471352824D5630CB129CFE840A5DD2BF043450AFF2F551B4E8B3A229953943572AAB8D0BBE34CF828DA1333CE7A6573460515F5211FA2AC13C2B0252F42524CC83CE0A8E0AB857BA48FB924F526EBD2ACB73EA133C4F2506716F29FCDD1033A208C91AA0AED2424A13A91D402DAD7D0DF753A172073D7095ED509C8DACBBDECCB17F19FA03878DF10A530A83822634C531DC2D4B467D023C392F5C4510991E78983DED4C5A10D8CAE83B1C1FE9B202BE4843EA6346B3145C820B3F82192997317BA320566C9F4F3CBCA2F5C5CD34F33CE6D2D24ACAA1E62FE2B9EE9C372181B0C812B8BDB1E09B3B1B7D3FC1AE29F4447B1DA58E49C8C4B28D45FB4C2CD7BC04D79ADEBB8E7D56F29E37746E34707C062A02B577DDAB45725D5B834EB723F77A72F7AADBAD47226B0D6E47537332AA859F619F3B1A9BBF4DCB32EDDA153C93FF496769603B3035BDC1F813025B430D4CB5D96A77EA954FB4C57BF7ABE22E93B938667B4D7E21F5E30E1C8FEF276A23849A9D5A2A9C81A9813772BD8FE7361724F5F9113E1EDC0D076F0C27C66701BD889650109F9FD610E112D20C12CCF8CE87D9320D7EEC5254D3EB49769FE361DEFE1D94EAFFD8B8F80F96A1210F', '1');
insert into CRIMINAL_INITIAL_RESULTS (CRIMINAL_INITIAL_RESULT_ID, TRANSACTION_NUMBER, SEARCH_RESULT_FILE, RESULTS_SENDER_ID) 
			values ('4', '000001820140729014008339994', '78DAAD555D6FA240147D37F13FDC7DD8F052C2E047ADB34842D1AE340A8DD0ECF308834E96AFC018659BFDEF3B608D35B5966E4BC23019CEB9F79C3B3733DA9AC791AEAD290974ADE065448197191D499CEEB8E21785A4B75BDA3759860C9E32B2A2F232A7E4B74C424E730C24DA92B2F80BB27C80F1009EC234E1724862169518661B9F0504CC3429D2885E81C7625A804DB7B0486392FC801A5DB03F1430A8D7D9EE24DAFAABA229B5395D536AABED96585AA641A96B9C2C85E92D0BF87A24A9087D976099E601CD4712AACC83C673810A8457B64A46924F1361BD2ECB6BEA0BBC48550DD5BBA7887F63C2A95249C680064A17291DA4F6017571EF061EE695CA67E891ABEC87FC6264CDF5168EFD53D71E056C4AB68431307316B3844470BF2938F32988E4A27035A1CAF428C21C6817D24285AD8DFE87E3135D56201659C87CC2599A804B49EEAF61418B4DC43F2888E7FBF999C93B5ADFDC4C23CB22212DA9A51C6BFE265EE8CE44135208F33406978B9D3DC36C28F24C6B9DA48D68C8AB659B54AD33B35CE30A5C6BFEE03AF645B907DED8B9C5E0981EA81D508737C34624D7B531F4077D79389407D783413312DD61B89BCC8DD9A4117E417CE1686AFC322CCBB01B57F042FE175D85C176606E78E6F40B025B630C86DAE9F6FACDCA77776B81BD8997D53139EC8807A99F57E178624F500F21D4E93752E19806066FE27A9FCF6DAC68E28B23786ADE8FCD8F847BEEF6E3777FF02AF5D5D36EFD034689F124', '2');
			
insert into fbi_rap_back_subscription(fbi_subscription_id, rap_back_category_code, rap_back_subscription_term_code, rap_back_expiration_date, rap_back_term_date, 
	rap_back_start_date, rap_back_opt_out_in_state_indicator, rap_back_activity_notification_format_code, ucn, subscription_id, report_timestamp)
values
	('fbiSubscriptionId', 'CI', '5', '2015-12-19', '2019-12-19', '2014-10-19', false, '2', 
	'123456789', '62721', {ts '2014-10-19 18:47:52.69'});			
	
insert into fbi_rap_back_subscription(fbi_subscription_id, rap_back_category_code, rap_back_subscription_term_code, rap_back_expiration_date, rap_back_term_date,
	rap_back_start_date, rap_back_opt_out_in_state_indicator, rap_back_activity_notification_format_code, ucn, subscription_id, report_timestamp)
values
	('fbiSubscriptionId_2', 'CI', '2', '2015-12-19','2016-12-19', '2014-10-19', false, '1', 
	'074644NG0', '62720',{ts '2014-10-19 18:47:52.69'});	
	
insert into fbi_rap_back_subscription(fbi_subscription_id, rap_back_category_code, rap_back_subscription_term_code, rap_back_expiration_date, rap_back_term_date, 
	rap_back_start_date, rap_back_opt_out_in_state_indicator, rap_back_activity_notification_format_code, ucn, subscription_id, report_timestamp)
values
	('fbiSubscriptionId_3', 'CI', '5', '2015-12-19', '2019-12-19', '2014-10-19', false, '2', 
	'9222201', '62726', {ts '2014-10-19 18:47:52.69'});			
	
insert into fbi_rap_back_subscription(fbi_subscription_id, rap_back_category_code, rap_back_subscription_term_code, rap_back_expiration_date, rap_back_term_date,
	rap_back_start_date, rap_back_opt_out_in_state_indicator, rap_back_activity_notification_format_code, ucn, subscription_id, report_timestamp)
values
	('UnsubscribeFbiUcnIdTest', 'CI', '2', '2015-12-19','2016-12-19', '2014-10-19', false, '1', 
	'1234', '62727', {ts '2014-10-19 18:47:52.69'});	
insert into fbi_rap_back_subscription values
	('123457', '2010-02-24', '2011-01-25', '2015-01-01', FALSE, '830420WX6', 'F', '2', 
	'5', 'eventid', '62729', {ts '2018-04-12 11:21:10.613'}, {ts '2018-04-12 11:21:10.613'});	
	
insert into subsequent_results(transaction_number, ucn, rap_sheet, results_sender_id, notification_indicator, report_timestamp) values('000001820140729014008339995', '9222201', '78DAB558DB6EDB48127D37E07F68CC0E307620D9A415DD122CB02DB2657542911A5EEC78DE1889B63923915A928AD7F3F57BAA49EA6253CE22C87680D8EEAEEBA9EAAA6A1A9FA4E70BCFBF383D31DC8BDB9BF148DA634DBB30EAFD7753579A57BDBEDE7B0F1AFF8B7F48787AA20F3E74DF33AD7FD9D12EAF34BDCB34ADDB6BDED73A3A9B7C2957256B62BA975796A66BFA70D6DAA93A3DE1BE7DF989DB829115EC1FBA5632BD6B5C8C19AE9C4A9B5B6C02BB1DF78EB9C2705C93B1668677A727C764D512655264E962332FE234392A662BCB7F8C7396856B963F4651C19EC29CAD157BB46071C2B2285FA7491EB12265C563C4EED3E5327D8A93079CFC7B13E5C50712024C99BD597D8D3276B08635646E49CCE4E290E0F464B6C9D629E41BE922DA3F9140B228A2A4F4E2601DA0ABE97AB7F42382BDF769B60A150B6C2F0E5DC31FF9E6EB9FD1BC78EDCC3CFC168545AE9C616781F79151E4DB5ABFDDD1CE7FEE560978344FB30599F435CC81749A2C9FF19FB28AC04C4A30E1C473BAC96AACDB81617F00665B5847D13CDC00BC70B188C9EB9CA5195B44CBA8FC63153EB3AF117E00DAB06061F2CC8A7815B558C892E80982E6E9FA19E8A49BE582082B35B0E7E9314A40132DF03B3025E0723A4C0A067D17A55BA72787BE1E2C1FF95CE7327EF382D12761F8CC777022B6C463C7B29C5B695FB3C0138CDB26849AD2F304DD095F3A364478BE2B0DFADD035BC519D8A670D9CC756EA44727CC133E84B9FE84499BF9D2B70499376831C3310573C66C2CC0C12DA4A2B80E2C25DB6367C6D83D6F8159C9675DED42BF6AB1910331D7CE8D70EDA9B07DDC4DE4224CB31D7B7FF7F4043FA42F85C778E04F1C57FE214C7210BE4E25CC815BC29DB9D2F6BDCA35E021E48D50219626B18FA5A16C5167C0CA63D300896D3B38BA23A8E08F296FA41970CB83843DA1A46CC27D45B4AFEBF4E4565A161B09C25419644C84F159D16DAB0D0AD841BDF10823256924512E0F8DDBD18C7C2E6D081DBBCE549197777FCAEFB6FA3CC712D61D0503B90E8A59E0CE1C04D715BF07882528080BE28093C4B51F719C3A81EF41BD925EE205DF402366DCF509F916B62D45CAAF856DDC31C7658818F20161DA054205E7EE0295645CE10893A615D8F0A9925FE5670D9532E677804DF0739BF1D9CC020AA495189C313091C06FCA3F53D6AA5C36852FDC6DC202C7D3132F903E1F490B1610120C3284ED1107FE10D399E5DC912BCC9B70844AE571E9325CA83556F83AB319F23AB04914C5D2013714B6489241EC02A157B4DC3002971320E356296BCF6748321CBB0A9FB4EB58B3C6485F30756E0A439A0AFDADDF706DE20496A98207D632474BF7C40BE7469C402DA13EB44592397B0108608255FAB0F57EC23D64874008C6603429DC7449B8E7D87C84EBEDCBA9282306112ED51602A442E765C4155A24112E590A02DF41E070571DF8CAC99CDD3D63958BC8CD19CA4F3015A5BBD781B47C42170C7784BD7B2D2EB94B150AD7000A6E27D298949EB982B2CA76E8664222920A97402AE73DBFCCF3831474287B5516DF4A6F2FA784B9CBE01D36A099A0ECA874287DDF13C5A94EA0FE805C70D723CFEA68FF96339A8F1879EAED5F6CEF0E3773DA7AA1A60E361A0DB64DF0ECD71C64AD21CCC0AD0C9970E4618BED05239899BC0A455DDE1877E15C43B156A57AECEE6AB1DEBBE8D0E0F69D0EBADA60AA40EBDA6CBB287AE63C4DFEDC24E500F414178FAAADCE375946FD8B5A7CB85E2FE3793928B4AB6EF842E0AE1752FFBBDF149B2C3ADEFBF872C942C8072F746471949311451827E518A54611BA6E959610C2EACEAF2CBAC7141265EB2C8685F374B50EB338A7161E260BB68E3292544F2D79B8A25167117F8B179B7089FC68B288E6211A0DD2FB6A0EDA4E1C59F4B05986E4D9D767B60C9F28C1D47484B90F222B8FC9A8F4FE3E9EC7E1B21444E09239D554B1439CC8C9B07535CA6DA1BBA001EF75FCDE1C60D9CB8AC4BE33C07AD54C670396B35C89FF247C8FEEBC1A14511F50875AE5C0C8CEF8677E4E375F54C7137ECBA53C3CF0020F25A1DE1A5BD4557DC7C6093AEB946F4F6A3D28ACB614AFB6AF85830AE1FBBB13B2A159EFDEC994BBF29045B8A5F977DBDD9DDB6694CFB3784DA9FCD624EE1588388DDF4D87D5283EBCC2D2B45727BEAD0D07FD5EF77D879DF9F6393BB26EFC21AD013BBBF15F8D83BB3591BC3BE877F45E979D4DE41B84168739DD5E077456E573AAB2D18B7097E3E2F9B82FB547B5DD57F8F5AADF19747A83E1E949B7DF1D0EFBBD7E8F64426AF49F4676379C47ECAD05B4A355B86C20BA7D8C8B88644FA2F8E1B1682038B26F529470654771563CC2D2DFB4C12F0D9A755D6BB4481FF6AEDA9ADED67AC7613DB6F461BFDFD6B51FE4EDE93FAEB7DB53BCFA0FF16A3FCEDB1FB4B56E5BEFAB4085718637E8327D914EE2396ADA6E4E8751963E250D27A3E5466583370FB3BCC5A661F6177E502DF5C3A248D31C35EAC5F3779B10BBEB8D3A93AE56682F15AF5C850F1158314E303E3231901F2ED40CEEFB8E4393863A1734E6608846139EBE52D4C2AEAABA34E581A045CF1D6F8609107BE0BBB6388674AF81EF96A60C569F97F6B80D3AF6ED71E5F5A4B4A3BC83B3255DB63AF1773C069EB57F4749FE18AF8F81EE474912E579F412BE2089A9C7A9C297975ADE6A3C0D5F62D8DB8DE79F6F2F663CCF511B344D676F13E21D1966CB986607F18D26145504CA141D0EDB7A99DEED9FB49001E59CB2D5B2BB103B6D158D8111E5A0CA7EFF84BEA6F08728993F6FEBA8D4B4F7BA86C6E219D20F68249C39F462C0303EA3AF728F61F6D090FC7B77BA22B110D10C0D8046CA31464CBCC5D9AF3A7DCB7BD1ECA26F916A104882BF12BA93C7B55C1DD7428DF853C06D0E7B29B7D5C45C7E79D099F3C7FFA2F5E745CD4837198216E718B1E2BD8F6267DB343BAF895EC586D5278781A98F7E2EFE07269E619EB77DBCD2F170E8B1A9431F8266C22D5F12B79778428CCEFFEFB17961D158BDFF7EC5A473FE464160F4E1457C51AF3DFACC409F79BE53101AD02D13A321ED3FEEAEC5C72DA75885F192F1C502172927CE4D9948FF5AA6F370F998E60548CB53F5A1F2D884E5CC30AABA926957838EAE0F3AC3AACABE63F44FD82679553DC8CABDFF02322F8890', '1', false, {ts '2018-05-19 18:47:52.69'});
insert into subsequent_results(transaction_number, civil_sid, rap_sheet, results_sender_id, notification_indicator, report_timestamp) values('000001820140729014008339995', 'A123459', '78DA0B2E492C4955082E4D2A4E2D2C4DCD2B51284A2D2ECD2929D603007D3709B1', '2', true, {ts '2018-04-19 18:47:52.69'});
