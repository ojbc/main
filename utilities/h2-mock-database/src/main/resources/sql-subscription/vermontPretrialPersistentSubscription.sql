#Vermont pre trial services have a wildcard match for arrest notifications
#The notifications are sent to a pre-trial service email address
#This SQL file has the insert queries needed to make the subscription and should be run manually at the client site

#We use a start date of January 1, 2000 for the subscription start date
#The subject name is 'all arrestees'
INSERT INTO `publish_subscribe`.`subscription`
(`topic`,
`startDate`,
`lastValidationDate`,
`subscribingSystemIdentifier`,
`subscriptionOwner`,
`subjectName`,
`active`)
VALUES
(
'{http://ojbc.org/wsn/topics}:person/arrest',
'2000-01-01',
'2000-01-01',
'{http://vjiss.vermont.gov/PreTrial/1.0}Pre-TrialServices',
'SYSTEM',
'All Arrestees',
1
);

#Replace the 'subscriptionID' below with the subscription ID returned by the subscription row insert
#Also replace the email address with the one specified by the customer
INSERT INTO `publish_subscribe`.`notification_mechanism`
(
`subscriptionId`,
`notificationMechanismType`,
`notificationAddress`)
VALUES
(
157665,
'email',
'testimap@localhost'
);

#Replace the 'subscriptionID' below with the subscription ID returned by the subscription row insert
INSERT INTO `publish_subscribe`.`subscription_subject_identifier`
(
`subscriptionId`,
`identifierName`,
`identifierValue`)
VALUES
(
157665,
'SID',
'*');