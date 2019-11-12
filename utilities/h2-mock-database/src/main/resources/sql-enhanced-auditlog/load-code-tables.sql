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

insert into SEARCH_QUALIFIER_CODES(CODE_NAME) values ('startsWith');
insert into SEARCH_QUALIFIER_CODES(CODE_NAME) values ('exact');

insert into SYSTEMS_TO_SEARCH(SYSTEM_NAME, SYSTEM_URI) values ('system1','{system1}URI');
insert into SYSTEMS_TO_SEARCH(SYSTEM_NAME, SYSTEM_URI) values ('system2','{system2}URI');
insert into SYSTEMS_TO_SEARCH(SYSTEM_NAME, SYSTEM_URI) values ('system3','{system3}URI');

insert into IDENTIFICATION_SEARCH_REASON_CODE(IDENTIFICATION_REASON_CODE_DESCRIPTION) values ('reason1');
insert into IDENTIFICATION_SEARCH_REASON_CODE(IDENTIFICATION_REASON_CODE_DESCRIPTION) values ('reason2');

insert into TRIGGERING_EVENTS (TRIGGERING_EVENT) values ('ARREST');
insert into TRIGGERING_EVENTS (TRIGGERING_EVENT) values ('DEATH');
insert into TRIGGERING_EVENTS (TRIGGERING_EVENT) values ('NCIC-SOR-ENTRY');
insert into TRIGGERING_EVENTS (TRIGGERING_EVENT) values ('NCIC-SOR-MODIFICATION');
insert into TRIGGERING_EVENTS (TRIGGERING_EVENT) values ('NCIC-SOR-DELETION');
insert into TRIGGERING_EVENTS (TRIGGERING_EVENT) values ('NCIC-WARRANT-ENTRY');
insert into TRIGGERING_EVENTS (TRIGGERING_EVENT) values ('NCIC-WARRANT-MODIFICATION');
insert into TRIGGERING_EVENTS (TRIGGERING_EVENT) values ('NCIC-WARRANT-DELETION');
insert into TRIGGERING_EVENTS (TRIGGERING_EVENT) values ('DISPOSITION');

insert into SUBSCRIPTION_REASON_CODE(SUBSCRIPTION_REASON_CODE) values ('CS');
insert into SUBSCRIPTION_REASON_CODE(SUBSCRIPTION_REASON_CODE) values ('CI');
