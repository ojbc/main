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