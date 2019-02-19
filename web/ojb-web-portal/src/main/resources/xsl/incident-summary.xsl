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
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:ir="http://ojbc.org/IEPD/Exchange/IncidentReport/1.0" xmlns:nc="http://niem.gov/niem/niem-core/2.0"
	xmlns:lexs="http://usdoj.gov/leisp/lexs/3.1" xmlns:lexspd="http://usdoj.gov/leisp/lexs/publishdiscover/3.1"
	xmlns:lexsdigest="http://usdoj.gov/leisp/lexs/digest/3.1" xmlns:s="http://niem.gov/niem/structures/2.0"
	xmlns:j="http://niem.gov/niem/domains/jxdm/4.0" xmlns:lexslib="http://usdoj.gov/leisp/lexs/library/3.1"
	xmlns:ndexia="http://fbi.gov/cjis/N-DEx/IncidentArrest/2.1"
	xmlns:inc-ext="http://ojbc.org/IEPD/Extensions/IncidentReportStructuredPayload/1.0"
	xmlns:ext="http://ojbc.org/IEPD/Extensions/IncidentReportStructuredPayload/1.0"
	xmlns:srer="http://ojbc.org/IEPD/Extensions/SearchRequestErrorReporting/1.0"
	xmlns:srm="http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0" xmlns:intel="http://niem.gov/niem/domains/intelligence/2.1"
	exclude-result-prefixes="#all">
	<xsl:import href="_formatters.xsl" />
	<xsl:output method="html" encoding="UTF-8" />
	<xsl:template match="/ir:IncidentReport/srm:SearchResultsMetadata">
		<xsl:apply-templates select="srer:SearchRequestError" />
	</xsl:template>
	<xsl:template match="/ir:IncidentReport/lexspd:doPublish">
		<script type="text/javascript"> $(function () { $('#incidentDetailTabs').tabs({ activate: function( event, ui ) { var modalIframe =
			$("#modalIframe", parent.document); modalIframe.height(modalIframe.contents().find("body").height() + 16); } });
			$("#modalContainer", parent.document).animate({ scrollTop: $("#incidentTab").offset().top }, 400); }); 
		</script>
		<div id="incidentDetailTabs">
			<ul>
				<li>
					<a href="#incidentTab">INCIDENT</a>
				</li>
				<li>
					<a href="#callTab">CALL INFO</a>
				</li>
			</ul>
			<div id="incidentTab">
				<xsl:call-template name="incidentTab" />
			</div>
			<div id="callTab">
				<xsl:call-template name="callTab" />
			</div>
		</div>
	</xsl:template>
	<xsl:template match="srer:SearchRequestError">
		<span class="error">
			System Name:
			<xsl:value-of select="intel:SystemName" />
			<br />
			Error:
			<xsl:value-of select="srer:ErrorText" />
		</span>
		<br />
	</xsl:template>
	<xsl:template name="incidentTab">
		<xsl:variable name="incidentStructuredLocation"
			select="//lexs:Digest/lexsdigest:EntityLocation/nc:Location[@s:id=//lexsdigest:Associations/lexsdigest:IncidentLocationAssociation[nc:ActivityReference/@s:ref=//lexsdigest:EntityActivity/nc:Activity/@s:id]/nc:LocationReference/@s:ref]/nc:LocationAddress/nc:StructuredAddress" />
		<table>
			<tr>
				<td class="incidentLabel">Incident Number:</td>
				<td>
					<xsl:value-of
						select="//lexs:Digest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText='Incident']/nc:ActivityIdentification/nc:IdentificationID" />
				</td>
				<td class="incidentLabel">Incident Nature:</td>
				<td>
				<xsl:value-of
					select="//inc-ext:IncidentReport/inc-ext:Incident[lexslib:SameAsDigestReference/@lexslib:ref=//lexs:Digest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText='Incident']/@s:id]/inc-ext:IncidentNatureText" />
				</td>
			</tr>
			<tr>
				<td class="incidentLabel">Record Security ID:</td>
				<td>
					<xsl:value-of
						select="//inc-ext:IncidentReport/inc-ext:Incident[lexslib:SameAsDigestReference/@lexslib:ref=//lexs:Digest/lexsdigest:EntityActivity/nc:Activity/@s:id]/inc-ext:IncidentRecordSecurityIdentification/nc:IdentificationID" />
				</td>
				<td class="incidentLabel"></td>
				<td></td>
			</tr>
			<tr>
				<td class="incidentLabel">Reported Date:</td>
				<td>
					<xsl:variable name="reportedDate"
						select="//lexs:Digest/lexsdigest:EntityActivity/lexsdigest:Metadata/nc:ReportedDate/nc:DateTime" />
					<xsl:value-of select="replace($reportedDate,'T',' ')" />
				</td>
				<td class="incidentLabel"></td>
				<td></td>
			</tr>
			<tr>
				<td class="incidentLabel"> Start Date/Time:</td>
				<td>
					<xsl:variable name="startDate"
						select="//lexs:Digest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText='Incident']/nc:ActivityDateRange/nc:StartDate/nc:DateTime" />
					<xsl:value-of select="replace($startDate,'T',' ')" />
				</td>
				<td class="incidentLabel">End Date/Time:</td>
				<td>
					<xsl:variable name="endDate"
						select="//lexs:Digest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText='Incident']/nc:ActivityDateRange/nc:EndDate/nc:DateTime" />
					<xsl:value-of select="replace($endDate,'T',' ')" />
				</td>
			</tr>
			<tr>
				<td class="incidentLabel">Location:</td>
				<td>
					<xsl:value-of
						select="concat($incidentStructuredLocation/nc:LocationStreet/nc:StreetFullText,' ',$incidentStructuredLocation/nc:LocationCityName,', ', $incidentStructuredLocation/nc:LocationStateName,' ',$incidentStructuredLocation/nc:LocationPostalCode)" />
				</td>
			</tr>
			<tr>
				<td class="incidentLabel">Geobase Address ID:</td>
				<td>
					<xsl:value-of
						select="//lexs:StructuredPayload/inc-ext:IncidentReport/inc-ext:Location[lexslib:SameAsDigestReference/@lexslib:ref=//lexs:Digest/lexsdigest:EntityLocation/nc:Location/@s:id]/inc-ext:SpatialReferenceIdentification/nc:IdentificationID" />
				</td>
			</tr>
			<tr>
				<td class="incidentLabel">Area Location Code:</td>
				<td>
					<xsl:value-of
						select="//lexs:StructuredPayload/inc-ext:IncidentReport/inc-ext:Location[lexslib:SameAsDigestReference/@lexslib:ref=//lexs:Digest/lexsdigest:EntityLocation/nc:Location/@s:id]/inc-ext:LocationAreaCodeText" />
				</td>
			</tr>
			<tr>
				<td class="incidentLabel">Disposition Date:</td>
				<td>
					<xsl:value-of
						select="//inc-ext:IncidentReport/inc-ext:Incident[lexslib:SameAsDigestReference/@lexslib:ref=//lexs:Digest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText='Incident']/@s:id]/inc-ext:IncidentDispositionDeclaredDate" />
				</td>
			</tr>
			<tr>
				<td class="incidentLabel">Disposition Code:</td>
				<td>
					<xsl:value-of
						select="//inc-ext:IncidentReport/inc-ext:Incident[lexslib:SameAsDigestReference/@lexslib:ref=//lexs:Digest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText='Incident']/@s:id]/inc-ext:IncidentDispositionCodeText" />
				</td>
			</tr>
			<tr>
				<td class="incidentLabel">Clearance Code:</td>
				<td>
					<xsl:value-of
						select="//inc-ext:IncidentReport/inc-ext:Incident[lexslib:SameAsDigestReference/@lexslib:ref=//lexs:Digest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText='Incident']/@s:id]/inc-ext:IncidentClearanceCodeText" />
				</td>
			</tr>
		</table>
	</xsl:template>
	<xsl:template name="callTab">
		<xsl:variable name="incidentStructuredLocation"
			select="//lexs:Digest/lexsdigest:EntityLocation/nc:Location[@s:id=//lexsdigest:Associations/lexsdigest:IncidentLocationAssociation[nc:ActivityReference/@s:ref=//lexsdigest:EntityActivity/nc:Activity/@s:id]/nc:LocationReference/@s:ref]/nc:LocationAddress/nc:StructuredAddress" />
		<table>
			<tr>
				<td class="incidentLabel">Call Receiver:</td>
				<td>
					<xsl:value-of
						select="//lexs:Digest/lexsdigest:EntityPerson/lexsdigest:Person[@s:id=//nc:ActivityInvolvedPersonAssociation[nc:PersonActivityInvolvementText='ReceivedBy']/nc:PersonReference/@s:ref]/nc:PersonName/nc:PersonFullName" />
				</td>
				<td class="incidentLabel">How Received:</td>
				<td>
					<xsl:value-of
						select="//inc-ext:IncidentReport/inc-ext:Incident[lexslib:SameAsDigestReference/@lexslib:ref=//lexs:Digest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText='Incident']/@s:id]/inc-ext:IncidentCallReceivedCodeText" />
				</td>
			</tr>
			<tr>
				<td class="incidentLabel">Long Term Call ID:</td>
				<td>
					<xsl:value-of
						select="//inc-ext:IncidentReport/inc-ext:Incident[lexslib:SameAsDigestReference/@lexslib:ref=//lexs:Digest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText='Incident']/@s:id]/inc-ext:IncidentLongTermCallIdentification/nc:IdentificationID" />
				</td>
			</tr>
			<tr>
				<td class="incidentLabel">Caller or Contact Name:</td>
				<td>
					<xsl:value-of
						select="//lexs:Digest/lexsdigest:EntityPerson/lexsdigest:Person[@s:id=//nc:ActivityInvolvedPersonAssociation[nc:PersonActivityInvolvementText='Complainant']/nc:PersonReference/@s:ref]/nc:PersonName/nc:PersonFullName" />
				</td>
				<td class="incidentLabel">Name Number:</td>
				<td>
					<xsl:value-of
						select="//lexs:StructuredPayload/inc-ext:IncidentReport/inc-ext:Person[lexslib:SameAsDigestReference/@lexslib:ref=//nc:ActivityInvolvedPersonAssociation[nc:PersonActivityInvolvementText='Complainant']/nc:PersonReference/@s:ref]/inc-ext:PersonNameRecordNumber" />
				</td>
			</tr>
			<tr>
				<td class="incidentLabel">Responding Agency Code:</td>
				<td>
					<xsl:value-of
						select="//lexs:StructuredPayload/inc-ext:IncidentReport/inc-ext:Organization[lexslib:SameAsDigestReference/@lexslib:ref=//nc:ActivityReportingOrganizationAssociation/nc:OrganizationReference/@s:ref]/inc-ext:OrganizationCodeText	" />
				</td>
				<td class="incidentLabel">Responding Officer:</td>
				<td>
					<xsl:value-of
						select="//lexs:Digest/lexsdigest:EntityPerson/lexsdigest:Person[@s:id=//j:ActivityEnforcementOfficialAssociation/nc:PersonReference/@s:ref]/nc:PersonName/nc:PersonFullName" />
				</td>
			</tr>
		</table>
	</xsl:template>
</xsl:stylesheet>