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
	xmlns:s="http://release.niem.gov/niem/structures/3.0/" 
	xmlns:srer="http://ojbc.org/IEPD/Extensions/SearchRequestErrorReporting/1.0" 
	xmlns:srm="http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0" 
	xmlns:wsn-br="http://docs.oasis-open.org/wsn/br-2" 
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	exclude-result-prefixes="#all">

	<xsl:import href="_formatters.xsl" />
	<xsl:output method="html" encoding="UTF-8" />
	
    <xsl:param name="purpose" />
    <xsl:param name="onBehalfOf" />
	
	<xsl:template match="/ConcealedCarrySearchResults">
		<xsl:variable name="accessDenialReasons" select="srm:SearchResultsMetadata/iad:InformationAccessDenial" />
		<xsl:variable name="requestErrors" select="srm:SearchResultsMetadata/srer:SearchRequestError" />
		<xsl:variable name="tooManyResultsErrors" select="srm:SearchResultsMetadata/srer:SearchErrors/srer:SearchResultsExceedThresholdError" />

		<xsl:apply-templates select="$accessDenialReasons" />
		<xsl:apply-templates select="$requestErrors" />
		<xsl:apply-templates select="$tooManyResultsErrors" />

		<xsl:if test="(not($tooManyResultsErrors) and not($accessDenialReasons) and not($requestErrors))">
			<xsl:call-template name="concealedCarrySearchResults"/>
		</xsl:if>
	</xsl:template>

	<xsl:template name="concealedCarrySearchResults">
			<table class="searchResultsTable table table-striped table-bordered nowrap" style="width:100%" id="searchResultsTable">
				<thead>
					<tr>
						<th>ENTITY</th>
						<th>LIC #</th>
						<th>NAME</th>
						<th>LIC DATE</th>
						<th>LIC TYPE</th>
						<th>STATUS</th>
						<th>COUNTY</th>
						<th class="d-none"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></th>
					</tr>
				</thead>
				<tbody>
				    <tr class="clickable">
				        <td>1</td>
				        <td>G123456</td>
				        <td>JOHN DOE</td>
				        <td>2023-05-05</td>
				        <td>CONCEALED</td>
				        <td>APPROVED</td>
				        <td>HONOLULU</td>
                        <td class="d-none">
                            <xsl:variable name="systemSource"><xsl:text>systemSource</xsl:text></xsl:variable>
                            <xsl:variable name="systemName"><xsl:text>systemName</xsl:text></xsl:variable>
                            <xsl:variable name="queryType"><xsl:text>Concealed Carry</xsl:text></xsl:variable>
                            <a href="{concat('concealedCarry/concealedCarryDetails?identificationID=1','&amp;systemName=',$systemName,'&amp;identificationSourceText=',$systemSource,'&amp;purpose=',$purpose,'&amp;onBehalfOf=',$onBehalfOf,'&amp;queryType=',$queryType)}" 
                                class="btn btn-primary btn-sm viewDetails" searchName='Concealed Carry Detail' 
                                    appendPersonData="{concat('personalInformation-', '1')}"
                                >DETAILS</a>
                        </td>
				    </tr>
				    <tr class="clickable">
				        <td>3</td>
				        <td>G123456</td>
				        <td>JOHN DOE</td>
				        <td>2023-05-05</td>
				        <td>CONCEALED</td>
				        <td>APPROVED</td>
				        <td>HONOLULU</td>
				        <td class="d-none">
		                    <xsl:variable name="systemSource"><xsl:text>systemSource</xsl:text></xsl:variable>
		                    <xsl:variable name="systemName"><xsl:text>systemName</xsl:text></xsl:variable>
		                    <xsl:variable name="queryType"><xsl:text>Concealed Carry</xsl:text></xsl:variable>
		                    <a href="{concat('concealedCarry/concealedCarryDetails?identificationID=1','&amp;systemName=',$systemName,'&amp;identificationSourceText=',$systemSource,'&amp;purpose=',$purpose,'&amp;onBehalfOf=',$onBehalfOf,'&amp;queryType=',$queryType)}" 
		                        class="btn btn-primary btn-sm viewDetails" searchName='{intel:SystemIdentifier/intel:SystemName} Detail' 
		                            appendPersonData="{concat('personalInformation-', '1')}"
		                        >DETAILS</a>
                        </td>
				        
				    </tr>
				</tbody>
			</table>
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
