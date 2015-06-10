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
--
-- Load hawaii specific data
--

USE `policy_acknowledgement`;

insert into policy(id,policy_uri,policy_location, active) values('1', 'http://ojbc.org/policies/privacy/hawaii/HIJISPrivacyPolicy', 'http://hijis.hawaii.gov/?wpfb_dl=239', true);
insert into policy(id,policy_uri,policy_location, active) values('2', 'http://ojbc.org/policies/privacy/hawaii/HawaiiCriminalJusticeDataCenterPrivacyPolicy', 'http://hijis.hawaii.gov/?wpfb_dl=13', true);
insert into policy(id,policy_uri,policy_location, active) values('3', 'http://ojbc.org/policies/privacy/hawaii/HawaiiCountyProsecutingAttorneyPrivacyPolicy', 'http://hijis.hawaii.gov/?wpfb_dl=12', true);
insert into policy(id,policy_uri,policy_location, active) values('4', 'http://ojbc.org/policies/privacy/hawaii/MauiCountyPoliceDepartmentPrivacyPolicy', 'http://hijis.hawaii.gov/?wpfb_dl=4', true);
insert into policy(id,policy_uri,policy_location, active) values('5', 'http://ojbc.org/policies/privacy/hawaii/MauiCountyProsecutingAttorneyPrivacyPolicy', 'http://hijis.hawaii.gov/?wpfb_dl=3', true);

insert into ori(id, ori) values ('2','HI002015Y');
insert into ori(id, ori) values ('3','HI001013A');
insert into ori(id, ori) values ('4','HI0050000');
insert into ori(id, ori) values ('5','HI005013A');
insert into ori(id, ori) values ('6','1234567890');

insert into policy_ori(ori_id, policy_id) values ('2','2');
insert into policy_ori(ori_id, policy_id) values ('3','3');
insert into policy_ori(ori_id, policy_id) values ('4','4');
insert into policy_ori(ori_id, policy_id) values ('5','5');
insert into policy_ori(ori_id, policy_id) values ('6','5');