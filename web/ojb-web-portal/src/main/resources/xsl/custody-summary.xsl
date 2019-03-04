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
	xmlns:cq-res-doc="http://ojbc.org/IEPD/Exchange/CustodyQueryResults/1.0"
	xmlns:cq-res-ext="http://ojbc.org/IEPD/Extensions/CustodyQueryResultsExtension/1.0"
	xmlns:j="http://release.niem.gov/niem/domains/jxdm/5.1/" xmlns:nc="http://release.niem.gov/niem/niem-core/3.0/"
	xmlns:ac-bkg-codes="http://ojbc.org/IEPD/Extensions/AdamsCounty/BookingCodes/1.0"
	xmlns:niem-xs="http://release.niem.gov/niem/proxy/xsd/3.0/" xmlns:structures="http://release.niem.gov/niem/structures/3.0/"
	xmlns:intel="http://release.niem.gov/niem/domains/intelligence/3.1/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:qrer="http://ojbc.org/IEPD/Extensions/QueryRequestErrorReporting/1.0"
	xmlns:qrm="http://ojbc.org/IEPD/Extensions/QueryResultsMetadata/1.0"
	xmlns:iad="http://ojbc.org/IEPD/Extensions/InformationAccessDenial/1.0" exclude-result-prefixes="#all">
	<xsl:import href="_formatters.xsl" />
	<xsl:output method="html" encoding="UTF-8" />
	<xsl:variable name="subjectId" select="//nc:Supervision/nc:SupervisionPerson/@structures:ref" />
	<xsl:template match="/">
		<table class="detailsTable">
			<tr>
				<td colspan="8" class="detailsTitle"></td>
			</tr>
			<tr>
				<td class="padding0">
					<div id="custodySummary" style="overflow:auto; width:100%; height:auto">
						<xsl:apply-templates select="cq-res-doc:CustodyQueryResults" />
					</div>
				</td>
			</tr>
		</table>
	</xsl:template>
	<xsl:template match="cq-res-doc:CustodyQueryResults">
		<xsl:choose>
			<xsl:when test="qrm:QueryResultsMetadata/qrer:QueryRequestError | qrm:QueryResultsMetadata/iad:InformationAccessDenial">
				<xsl:apply-templates select="qrm:QueryResultsMetadata/qrer:QueryRequestError" />
				<xsl:apply-templates select="qrm:QueryResultsMetadata/iad:InformationAccessDenial" />
			</xsl:when>
			<xsl:otherwise>
				<h2>CUSTODY SUMMARY</h2>
				<div id="custodySummary">
					<xsl:apply-templates select="cq-res-ext:Custody" mode="report" />
				</div>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="cq-res-ext:Custody" mode="report">
		<xsl:apply-templates select="nc:Person[@structures:id=$subjectId]" />
		<xsl:apply-templates select="nc:Supervision" />
	</xsl:template>
	<xsl:template match="nc:Person">
		<h3>Supervision Subject</h3>
		<div>
			<table style="width:100%">
				<tr>
					<td class="detailsLabel">Name:</td>
					<td>
						<xsl:apply-templates select="nc:PersonName" mode="constructName" />
					</td>
				</tr>
				<tr>
					<td class="detailsLabel">DOB:</td>
					<td>
						<xsl:value-of select="nc:PersonBirthDate/nc:Date" />
					</td>
				</tr>
			</table>
		</div>
	</xsl:template>
	<xsl:template match="nc:Supervision">
		<h3>Supervision</h3>
		<div>
			<table style="width:100%">
				<tr>
					<td class="detailsLabel">Start Date:</td>
					<td>
						<xsl:value-of select="substring-before(nc:ActivityDate/nc:DateTime,'T')" />
					</td>
				</tr>
				<tr>
					<td class="detailsLabel">Category:</td>
					<td>
						<xsl:value-of select="nc:ActivityCategoryText" />
					</td>
				</tr>			
				
				<tr>
					<td class="detailsLabel">Agency Record:</td>
					<td>
						<xsl:value-of select="nc:ActivityIdentification/nc:IdentificationID" />
					</td>
				</tr>
				<tr>
					<td class="detailsLabel">Division:</td>
					<td>
						<xsl:value-of
							select="//nc:Organization/nc:OrganizationSubUnit/nc:OrganizationName" />
					</td>
				</tr>				
				<xsl:if test="nc:SupervisionSupervisor/cq-res-ext:SupervisorCategoryText">
				<tr>
					<td class="detailsLabel">Supervisor:</td>
					<td>
						<xsl:value-of select="nc:SupervisionSupervisor/cq-res-ext:SupervisorCategoryText" />
					</td>
				</tr>
				</xsl:if>				
			</table>
		</div>
	</xsl:template>
	<xsl:template match="qrer:QueryRequestError">
		<span class="error">
			Custody Summary Query Result Error:
			<xsl:value-of select="qrer:ErrorText" />
		</span>
		<br />
	</xsl:template>
	<xsl:template match="iad:InformationAccessDenial">
		<span class="error">
			Access to System
			<xsl:value-of select="iad:InformationAccessDenyingSystemNameText" />
			Denied. Denied Reason:
			<xsl:value-of select="iad:InformationAccessDenialReasonText" />
		</span>
		<br />
	</xsl:template>
	<xsl:template match="nc:PersonName" mode="constructName">
		<xsl:value-of select="concat(nc:PersonSurName,', ',nc:PersonGivenName,', ',nc:PersonMiddleName)" />
	</xsl:template>
</xsl:stylesheet>