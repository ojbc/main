<?xml version="1.0" encoding="UTF-8"?>
<!--

    Unless explicitly acquired and licensed from Licensor under another license, the contents of
    this file are subject to the Reciprocal Public License ("RPL") Version 1.5, or subsequent
    versions as allowed by the RPL, and You may not copy or use this file in either source code
    or executable form, except in compliance with the terms and conditions of the RPL

    All software distributed under the RPL is provided strictly on an "AS IS" basis, WITHOUT
    WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY DISCLAIMS ALL SUCH
    WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
    PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific language
    governing rights and limitations under the RPL.

    http://opensource.org/licenses/RPL-1.5

    Copyright 2012-2017 Open Justice Broker Consortium

-->
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xmlns:lexs="http://usdoj.gov/leisp/lexs/3.1" xmlns:j="http://niem.gov/niem/domains/jxdm/4.0"
	xmlns:nc="http://niem.gov/niem/niem-core/2.0" xmlns:lexslib="http://usdoj.gov/leisp/lexs/library/3.1"
	xmlns:s="http://niem.gov/niem/structures/2.0" xmlns:scr="http://niem.gov/niem/domains/screening/2.0"
	xmlns:inc-ext="http://ojbc.org/IEPD/Extensions/IncidentReportStructuredPayload/1.0"
	xmlns:lexsdigest="http://usdoj.gov/leisp/lexs/digest/3.1" xmlns:ndexia="http://fbi.gov/cjis/N-DEx/IncidentArrest/2.1"
	xmlns:ir-doc="http://ojbc.org/IEPD/Exchange/IncidentReport/1.0"
	xmlns:iru-doc="http://ojbc.org/IEPD/Exchange/IncidentReportUpdate/1.0"
	xmlns:lexspd="http://usdoj.gov/leisp/lexs/publishdiscover/3.1">
	<xsl:output method="xml" encoding="UTF-8" indent="yes" />
	<xsl:template match="/ir-doc:IncidentReport">
		<xsl:copy-of select="lexspd:doPublish" />
	</xsl:template>
	<xsl:template match="/iru-doc:IncidentReportUpdate">
		<xsl:copy-of select="lexspd:doPublish" />
	</xsl:template>
</xsl:stylesheet>