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

insert into ojbc_user(id, federation_id, create_date) values ('1','HIJIS:IDP:HCJDC:USER:hpotter', {ts '2010-09-17 18:47:52.69'});
insert into ojbc_user(id, federation_id, create_date) values ('2','HIJIS:IDP:HCJDC:USER:hsimpson',  {ts '2011-09-17 18:47:52.69'});

insert into policy(id,policy_uri,policy_location, active, update_date) values('1', 'http://ojbc.org/policies/privacy/hawaii/ManualSubscriptionPolicy', 'http://hijis.hawaii.gov/privacy-policies/ManualSubscriptionPolicy.html', true, {ts '2012-09-17 18:47:52.69'});
insert into policy(id,policy_uri,policy_location, active, update_date) values('2', 'http://ojbc.org/policies/privacy/hawaii/MockSubscriptionPolicy', 'http://hijis.hawaii.gov/privacy-policies/MockSubscriptionPolicy.html', true, {ts '2012-09-17 18:47:52'});
insert into policy(id,policy_uri,policy_location, active, update_date) values('3', 'http://ojbc.org/policies/privacy/hawaii/MockSubscriptionPolicy1', 'http://hijis.hawaii.gov/privacy-policies/MockSubscriptionPolicy1.html', true, {ts '2012-09-17 18:47:52'});
insert into policy(id,policy_uri,policy_location, active, update_date) values('4', 'http://ojbc.org/policies/privacy/hawaii/MockSubscriptionPolicy2', 'http://hijis.hawaii.gov/privacy-policies/MockSubscriptionPolicy2.html', true, {ts '2012-09-17 18:47:52'});
insert into policy(id,policy_uri,policy_location, active, update_date) values('5', 'http://ojbc.org/policies/privacy/hawaii/MockSubscriptionPolicy3', 'http://hijis.hawaii.gov/privacy-policies/MockSubscriptionPolicy3.html', true, {ts '2012-09-17 18:47:52'});
insert into policy(id,policy_uri,policy_location, active, update_date) values('6', 'http://ojbc.org/policies/privacy/hawaii/MockSubscriptionPolicy4', 'http://hijis.hawaii.gov/privacy-policies/MockSubscriptionPolicy4.html', true, {ts '2012-09-17 18:47:52'});
insert into policy(id,policy_uri,policy_location, active, update_date) values('7', 'http://ojbc.org/policies/privacy/hawaii/MockSubscriptionPolicy5', 'http://hijis.hawaii.gov/privacy-policies/MockSubscriptionPolicy5.html', true, {ts '2012-09-17 18:47:52'});

insert into user_policy_acknowledgement(user_id, policy_id, acknowledge_date) values ('1', '1', {ts '2010-10-17 18:47:52.69'}); 
insert into user_policy_acknowledgement(user_id, policy_id, acknowledge_date) values ('1', '2', {ts '2013-11-17 18:47:52.69'}); 
insert into user_policy_acknowledgement(user_id, policy_id, acknowledge_date) values ('2', '1', {ts '2013-11-17 18:47:52.69'}); 
insert into user_policy_acknowledgement(user_id, policy_id, acknowledge_date) values ('2', '2', {ts '2013-11-17 18:47:52.69'}); 
insert into user_policy_acknowledgement(user_id, policy_id, acknowledge_date) values ('2', '3', {ts '2013-11-17 18:47:52.69'}); 

insert into ori(id, ori, civil_ori_indicator) values ('1','H00000001', false);
insert into ori(id, ori, civil_ori_indicator) values ('2','H00000002', false);
insert into ori(id, ori, civil_ori_indicator) values ('3','H00000003', false);
insert into ori(id, ori, civil_ori_indicator) values ('4','H00000004', false);
insert into ori(id, ori, civil_ori_indicator) values ('5','H00000005', false);
insert into ori(id, ori, civil_ori_indicator) values ('6','1234567890', false);
insert into ori(id, ori, civil_ori_indicator) values ('7','NoPolicyOri1', true);

insert into policy_ori(ori_id, policy_id) values ('1','3');
insert into policy_ori(ori_id, policy_id) values ('2','4');
insert into policy_ori(ori_id, policy_id) values ('3','5');
insert into policy_ori(ori_id, policy_id) values ('4','6');
insert into policy_ori(ori_id, policy_id) values ('5','7');
insert into policy_ori(ori_id, policy_id) values ('6','7');

