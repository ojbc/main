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
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:oirsr="http://ojbc.org/IEPD/Exchange/OrganizationIdentificationResultsSearchResults/1.0"
	xmlns:oirsr-ext="http://ojbc.org/IEPD/Extensions/OrganizationIdentificationResultsSearchResults/1.0" 
	xmlns:iad="http://ojbc.org/IEPD/Extensions/InformationAccessDenial/1.0" 
	xmlns:intel="http://release.niem.gov/niem/domains/intelligence/3.0/" 
	xmlns:j="http://release.niem.gov/niem/domains/jxdm/5.0/"
	xmlns:nc="http://release.niem.gov/niem/niem-core/3.0/"
	xmlns:niem-xsd="http://niem.gov/niem/proxy/xsd/2.0" 
	xmlns:s="http://niem.gov/niem/structures/2.0" 
	xmlns:srer="http://ojbc.org/IEPD/Extensions/SearchRequestErrorReporting/1.0" 
	xmlns:srm="http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0" 
	xmlns:wsn-br="http://docs.oasis-open.org/wsn/br-2" 
	exclude-result-prefixes="#all">

	<xsl:import href="_formatters.xsl" />
	<xsl:output method="html" encoding="UTF-8" />
	
	<xsl:template match="/oirsr:OrganizationIdentificationResultsSearchResults">
		<xsl:variable name="accessDenialReasons" select="srm:SearchResultsMetadata/iad:InformationAccessDenial" />
		<xsl:variable name="requestErrors" select="srm:SearchResultsMetadata/srer:SearchRequestError" />
		<xsl:variable name="tooManyResultsErrors" select="srm:SearchResultsMetadata/srer:SearchErrors/srer:SearchResultsExceedThresholdError" />

		<xsl:apply-templates select="$accessDenialReasons" />
		<xsl:apply-templates select="$requestErrors" />
		<xsl:apply-templates select="$tooManyResultsErrors" />

		<xsl:if test="(not($tooManyResultsErrors) and not($accessDenialReasons) and not($requestErrors))">
			<xsl:variable name="containedResultCount">
				<xsl:value-of select="count(oirsr-ext:OrganizationIdentificationResultsSearchResult)"></xsl:value-of>
			</xsl:variable>
			<xsl:if test="$containedResultCount &lt; srm:SearchResultsMetadata/srm:TotalAuthorizedSearchResultsQuantity">
				<span class="hint">
					The most recent <xsl:value-of select="$containedResultCount"/> of <xsl:value-of select="srm:SearchResultsMetadata/srm:TotalAuthorizedSearchResultsQuantity"/>
					entries are loaded. Please refine your search with the RETURN TO SEARCH button.
				</span>
			</xsl:if>
			<xsl:call-template name="rapbacks"/>
		</xsl:if>
	</xsl:template>

	<xsl:template name="rapbacks">
			<table class="searchResultsTable table table-striped table-bordered nowrap" style="width:100%" id="searchResultsTable">
				<thead>
					<tr>
						<th>NAME</th>
						<th>OTN</th>
						<th>ID DATE</th>
						<th>TYPE</th>
						<th>STATUS</th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<xsl:apply-templates select="oirsr-ext:OrganizationIdentificationResultsSearchResult"/>
				</tbody>
			</table>
	</xsl:template>
	
	<xsl:template match="oirsr-ext:OrganizationIdentificationResultsSearchResult">
		<xsl:variable name="systemID" select="intel:SystemIdentifier"/>
		<xsl:variable name="rapbackId" select="intel:SystemIdentifier/nc:IdentificationID"/>
		<tr>
			<td><xsl:apply-templates select="child::oirsr-ext:IdentifiedPerson/nc:PersonName" mode="primaryName"></xsl:apply-templates></td>
			<td>
				<xsl:value-of select="oirsr-ext:IdentifiedPerson/oirsr-ext:IdentifiedPersonTrackingIdentification/nc:IdentificationID"></xsl:value-of>
			</td>	
			<td>
				<xsl:apply-templates select="oirsr-ext:IdentificationReportedDate/nc:Date" mode="formatDateAsMMDDYYYY"/>
			</td>				
			<td>
				<xsl:value-of select="oirsr-ext:CriminalIdentificationReasonCode"></xsl:value-of>
			</td>	
			<td>
				<xsl:if test="oirsr-ext:IdentificationResultStatusCode = 'Archived'">
					<xsl:text>Archived</xsl:text>
				</xsl:if>
			</td>	
			<td align="right" width="115px">
				<xsl:apply-templates select=".[normalize-space(oirsr-ext:IdentificationResultStatusCode) = 'Available for Subscription']" mode="unsubscribed"/>
				<a href="{string-join(('rapbacks/initialResults', intel:SystemIdentification/nc:IdentificationID), '/')}" 
					class="blueIcon initialResults" style="margin-right:3px" title="Initial Results"><i class="fa fa-file-alt fa-lg"></i></a>
			</td>
		</tr>
	</xsl:template>
	
	<xsl:template match="oirsr-ext:OrganizationIdentificationResultsSearchResult" mode="unsubscribed">
 		<a href="#" class="blueIcon archive" style="margin-right:3px" title="Archive">
			<xsl:attribute name="id">
				<xsl:value-of select="normalize-space(intel:SystemIdentification/nc:IdentificationID)"/>
			</xsl:attribute>
			<i class="fa fa-archive fa-lg"></i>
		</a>
	</xsl:template>
	
	<xsl:template match="iad:InformationAccessDenial">
		<div class="alert alert-warning" role="alert">
			User does not meet privilege requirements to access
			<xsl:value-of select="iad:InformationAccessDenyingSystemNameText" />. To request access, contact your IT department.
		</div>
	</xsl:template>

	<xsl:template match="srer:SearchRequestError">
		<div class="alert alert-warning" role="alert">
			System Name: <xsl:value-of select="nc:SystemName" />, 
			Error: <xsl:value-of select="srer:ErrorText" />
		</div>
	</xsl:template>

	<xsl:template match="srer:SearchResultsExceedThresholdError">
		<div class="alert alert-warning" role="alert">
			System <xsl:value-of select="../nc:SystemName" /> returned too many records, please refine your criteria.
		</div>
	</xsl:template>
</xsl:stylesheet>
