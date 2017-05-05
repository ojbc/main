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

    Copyright 2012-2015 Open Justice Broker Consortium

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
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	exclude-result-prefixes="#all">

	<xsl:import href="_formatters.xsl" />
	<xsl:output method="html" encoding="UTF-8" />
	
	<xsl:param name="rapbackValidationButtonShowingPeriod" select="30"/>
	
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
			<table class="searchResultsTable display" id="searchResultsTable">
				<thead>
					<tr>
						<th>NAME</th>
						<th>OTN</th>
						<th>ID DATE</th>
						<th>START DATE</th>
						<th>VALIDATION DUE</th>
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
			<xsl:if test="oirsr-ext:SubsequentResultsAvailableIndicator = 'true'">
				<xsl:attribute name="class">subsequentResults</xsl:attribute>
			</xsl:if>
			<td><xsl:apply-templates select="child::oirsr-ext:IdentifiedPerson/nc:PersonName" mode="primaryName"></xsl:apply-templates></td>
			<td>
				<xsl:value-of select="oirsr-ext:IdentifiedPerson/oirsr-ext:IdentifiedPersonTrackingIdentification/nc:IdentificationID"></xsl:value-of>
			</td>					
			<td>
				<xsl:apply-templates select="oirsr-ext:IdentificationReportedDate/nc:Date" mode="formatDateAsMMDDYYYY"/>
			</td>	
			<td>
				<xsl:apply-templates select="oirsr-ext:Subscription/nc:ActivityDateRange/nc:StartDate/nc:Date" mode="formatDateAsMMDDYYYY"/>
			</td>
			<xsl:variable name="validationDueDate" select="oirsr-ext:Subscription/oirsr-ext:SubscriptionValidation/oirsr-ext:SubscriptionValidationDueDate/nc:Date"/>				
			<td>
				<xsl:if test="$validationDueDate &lt; current-date()">
					<xsl:attribute name="style">color:red</xsl:attribute>
				</xsl:if>
				<xsl:apply-templates select="$validationDueDate" mode="formatDateAsMMDDYYYY"/>
			</td>
			<td>
				<xsl:if test="normalize-space(oirsr-ext:IdentificationResultStatusCode) != 'Available for Subscription'">
					<xsl:value-of select="normalize-space(oirsr-ext:IdentificationResultStatusCode)"></xsl:value-of>
				</xsl:if>
			</td>
			<td align="right" width="115px">
				<xsl:apply-templates select=".[normalize-space(oirsr-ext:IdentificationResultStatusCode) = 'Available for Subscription']" mode="unsubscribed"/>
				<xsl:apply-templates select=".[normalize-space(oirsr-ext:IdentificationResultStatusCode) = 'Subscribed']" mode="subscribed"/>
				<xsl:if test="oirsr-ext:SubsequentResultsAvailableIndicator = 'true'">
					<a href="#" class="blueIcon subsequentResultConfirmation" style="margin-right:3px" title="Subsequent Results">
						<xsl:attribute name="id">
							<xsl:value-of select="normalize-space(oirsr-ext:Subscription/oirsr-ext:SubscriptionIdentification/nc:IdentificationID)"/>
						</xsl:attribute>
						<i class="fa fa-bell fa-lg"></i>
					</a>
					<a href="{concat('../rapbacks/subsequentResults?transactionNumber=',intel:SystemIdentification/nc:IdentificationID)}" 
						class="blueIcon subsequentResults hidden">
					</a>
				</xsl:if>
				<a href="{concat('../rapbacks/initialResults?transactionNumber=',intel:SystemIdentification/nc:IdentificationID)}" 
					class="blueIcon initialResults" style="margin-right:3px" title="Initial Results"><i class="fa fa-file-text-o fa-lg"></i></a>
			</td>
		</tr>
	</xsl:template>
	
	<xsl:template match="oirsr-ext:OrganizationIdentificationResultsSearchResult" mode="unsubscribed">
<!-- Hide the Subscribe button for phase 1 -->	
<!-- 		
		<a href="#" class="blueIcon subscribe" style="margin-right:3px" title="Subscribe">
			<xsl:attribute name="id">
				<xsl:value-of select="normalize-space(intel:SystemIdentification/nc:IdentificationID)"/>
			</xsl:attribute>
			<i class="fa fa-rss fa-lg"/>
		</a>
 -->		<a href="#" class="blueIcon archive" style="margin-right:3px" title="Archive">
			<xsl:attribute name="id">
				<xsl:value-of select="normalize-space(intel:SystemIdentification/nc:IdentificationID)"/>
			</xsl:attribute>
			<i class="fa fa-archive fa-lg"></i>
		</a>
	</xsl:template>
	
	<xsl:template match="oirsr-ext:OrganizationIdentificationResultsSearchResult" mode="subscribed">
		<xsl:variable name="validationDueDate" select="oirsr-ext:Subscription/oirsr-ext:SubscriptionValidation/oirsr-ext:SubscriptionValidationDueDate/nc:Date"/>
		<xsl:if test="$validationDueDate &lt; current-date() + $rapbackValidationButtonShowingPeriod * xs:dayTimeDuration('P1D')">				
			<a href="#" class="blueIcon validate" style="margin-right:3px" title="Validate">
				<xsl:attribute name="id">
					<xsl:value-of select="normalize-space(oirsr-ext:Subscription/oirsr-ext:SubscriptionIdentification/nc:IdentificationID)"/>
				</xsl:attribute>
				<i class="fa fa-check-circle fa-lg"/>
			</a>
		</xsl:if>
		<a href="#" class="blueIcon unsubscribe" style="margin-right:3px" title="Unsubscribe">
			<xsl:attribute name="id">
				<xsl:value-of select="normalize-space(oirsr-ext:Subscription/oirsr-ext:SubscriptionIdentification/nc:IdentificationID)"/>
			</xsl:attribute>
			<i class="fa fa-times-circle fa-lg"></i>
		</a>
	</xsl:template>
	
	<xsl:template match="iad:InformationAccessDenial">
		<span class="error">
			User does not meet privilege requirements to access
			<xsl:value-of select="iad:InformationAccessDenyingSystemNameText" />. To request access, contact your IT department.
		</span>
		<br />
	</xsl:template>

	<xsl:template match="srer:SearchRequestError">
		<span class="error">
			System Name: <xsl:value-of select="nc:SystemName" />, 
			Error: <xsl:value-of select="srer:ErrorText" />
		</span>
		<br />
	</xsl:template>

	<xsl:template match="srer:SearchResultsExceedThresholdError">
		<span class="error">
			System <xsl:value-of select="../nc:SystemName" /> returned too many records, please refine your criteria.
		</span>
		<br />
	</xsl:template>
</xsl:stylesheet>
