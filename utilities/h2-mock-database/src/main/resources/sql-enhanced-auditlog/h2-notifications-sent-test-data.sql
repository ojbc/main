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

insert into NOTIFICATIONS_SENT (NOTIFICATIONS_SENT_ID, SUBSCRIPTION_TYPE, TOPIC, SUBSCRIPTION_IDENTIFIER, SUBSCRIPTION_OWNER_AGENCY, SUBSCRIPTION_OWNER_AGENCY_TYPE, SUBSCRIPTION_OWNER_EMAIL_ADDRESS, SUBSCRIPTION_OWNER, NOTIFYING_SYSTEM_NAME, SUBSCRIBING_SYSTEM_IDENTIFIER, SUBSCRIPTION_SUBJECT, timestamp) values('1', '', '{http://ojbc.org/wsn/topics}:person/rapback', '62729', 'HI123456', '', 'test1@email.com', 'STATE:IDP:AGENCY:USER:test1@email.com', 'LOTC', '{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB', 'John Doe', '2018-10-18 16:39:00');
insert into NOTIFICATIONS_SENT (NOTIFICATIONS_SENT_ID, SUBSCRIPTION_TYPE, TOPIC, SUBSCRIPTION_IDENTIFIER, SUBSCRIPTION_OWNER_AGENCY, SUBSCRIPTION_OWNER_AGENCY_TYPE, SUBSCRIPTION_OWNER_EMAIL_ADDRESS, SUBSCRIPTION_OWNER, NOTIFYING_SYSTEM_NAME, SUBSCRIBING_SYSTEM_IDENTIFIER, SUBSCRIPTION_SUBJECT, timestamp) values('2', '', '{http://ojbc.org/wsn/topics}:person/rapback', '62726', 'HI123456', '', 'test2@email.com', 'STATE:IDP:AGENCY:USER:test2@email.com', 'FBI EBTS', '{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB', 'Jane Doe','2018-10-19 13:38:38');
insert into NOTIFICATIONS_SENT (NOTIFICATIONS_SENT_ID, SUBSCRIPTION_TYPE, TOPIC, SUBSCRIPTION_IDENTIFIER, SUBSCRIPTION_OWNER_AGENCY, SUBSCRIPTION_OWNER_AGENCY_TYPE, SUBSCRIPTION_OWNER_EMAIL_ADDRESS, SUBSCRIPTION_OWNER, NOTIFYING_SYSTEM_NAME, SUBSCRIBING_SYSTEM_IDENTIFIER, SUBSCRIPTION_SUBJECT, timestamp) values('3', '', '{http://ojbc.org/wsn/topics}:person/rapback', '62725', 'HI123456', '', 'test3@email.com', 'STATE:IDP:AGENCY:USER:test3email.com', 'http://www.hawaii.gov/arrestNotificationProducer', '{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB', 'Bill Padmanabhan', '2018-10-22 11:14:46');

insert into notification_properties (NOTIFICATION_PROPERTIES_ID, NOTIFICATIONS_SENT_ID, PROPERTY_NAME, PROPERTY_VALUE) values ('1', '2', 'RAPBACK_TRIGERRING_EVENT_CODE', 'NCIC-SOR-ENTRY');