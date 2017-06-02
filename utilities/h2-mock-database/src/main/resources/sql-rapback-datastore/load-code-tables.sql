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

insert into identification_category(identification_category_id, identification_category_code, identification_category_description, identification_category_type) values('1', 'F', 'Firearms','CIVIL'); 
insert into identification_category(identification_category_id, identification_category_code, identification_category_description, identification_category_type) values('2', 'I', 'Volunteer, Child Care/School Employee, Non-Criminal Justice Employment and Licensing', 'CIVIL'); 
insert into identification_category(identification_category_id, identification_category_code, identification_category_description, identification_category_type) values('3', 'J', 'Criminal Justice Employment', 'CIVIL'); 
insert into identification_category(identification_category_id, identification_category_code, identification_category_description, identification_category_type) values('4', 'CAR', 'Criminal Tenprint Submission', 'CRIMINAL'); 
insert into identification_category(identification_category_id, identification_category_code, identification_category_description, identification_category_type) values('5', 'SOR', 'Sex Offender Registry', 'CRIMINAL'); 
insert into identification_category(identification_category_id, identification_category_code, identification_category_description, identification_category_type) values('6', 'S', 'Security Clearance Information Act', 'CIVIL'); 

insert into subscription_category(subscription_category_code, subscription_category_description) values('F', 'Firearms'); 
insert into subscription_category(subscription_category_code, subscription_category_description) values('I', 'Volunteer, Child Care/School Employee, Non-Criminal Justice Employment and Licensing'); 
insert into subscription_category(subscription_category_code, subscription_category_description) values('J', 'Criminal Justice Employment'); 
insert into subscription_category(subscription_category_code, subscription_category_description) values('CI', 'Criminal Justice Investigative'); 
insert into subscription_category(subscription_category_code, subscription_category_description) values('CS', 'Criminal Justice - Supervision'); 
insert into subscription_category(subscription_category_code, subscription_category_description) values('S', 'Security Clearance Information Act'); 

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
	
insert into AGENCY_PROFILE(AGENCY_ID, AGENCY_ORI, AGENCY_NAME, FBI_SUBSCRIPTION_QUALIFICATION ) values ('1', '1234567890', 'Demo Agency', 'true'); 
insert into AGENCY_PROFILE(AGENCY_ID, AGENCY_ORI, AGENCY_NAME, FBI_SUBSCRIPTION_QUALIFICATION ) values ('2', '68796860', 'Test Agency', 'true'); 

insert into AGENCY_CONTACT_EMAIL(AGENCY_CONTACT_EMAIL_ID , AGENCY_ID , AGENCY_EMAIL) values('1', '1', 'demo.agency@localhost'); 
insert into AGENCY_CONTACT_EMAIL(AGENCY_CONTACT_EMAIL_ID , AGENCY_ID , AGENCY_EMAIL) values('2', '1', 'demo.agency2@localhost'); 	
insert into AGENCY_CONTACT_EMAIL(AGENCY_CONTACT_EMAIL_ID , AGENCY_ID , AGENCY_EMAIL) values('3', '2', 'test.agency@localhost'); 
insert into AGENCY_CONTACT_EMAIL(AGENCY_CONTACT_EMAIL_ID , AGENCY_ID , AGENCY_EMAIL) values('4', '2', 'test.agency2@localhost'); 	