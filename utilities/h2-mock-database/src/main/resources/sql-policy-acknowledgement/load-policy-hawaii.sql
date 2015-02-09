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