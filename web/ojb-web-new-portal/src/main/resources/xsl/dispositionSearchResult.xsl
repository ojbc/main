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
	xmlns:chsres-doc="http://ojbc.org/IEPD/Exchange/CriminalHistorySearchResults/1.0"
	xmlns:chsres-ext="http://ojbc.org/IEPD/Extensions/CriminalHistorySearchResults/1.0"
	xmlns:j="http://release.niem.gov/niem/domains/jxdm/6.0/" xmlns:intel="http://release.niem.gov/niem/domains/intelligence/4.0/"
	xmlns:nc="http://release.niem.gov/niem/niem-core/4.0/" xmlns:ncic="http://release.niem.gov/niem/codes/fbi_ncic/4.0/"
	xmlns:niem-xs="http://release.niem.gov/niem/proxy/xsd/4.0/" xmlns:structures="http://release.niem.gov/niem/structures/4.0/"
  xmlns:iad="http://ojbc.org/IEPD/Extensions/InformationAccessDenial/1.0"
  xmlns:srm="http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0"
  xmlns:srer="http://ojbc.org/IEPD/Extensions/SearchRequestErrorReporting/1.0"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://ojbc.org/IEPD/Exchange/CriminalHistorySearchResults/1.0 ../xsd/criminal_history_search_results.xsd"
	exclude-result-prefixes="#all">

	<xsl:import href="_formatters.xsl" />
	<xsl:output method="html" encoding="UTF-8" />
	
	<xsl:template match="/chsres-doc:CriminalHistorySearchResults">
    <xsl:variable name="accessDenialReasons" select="srm:SearchResultsMetadata/iad:InformationAccessDenial" />
    <xsl:variable name="requestErrors" select="srm:SearchResultsMetadata/srer:SearchRequestError" />
    <xsl:variable name="tooManyResultsErrors" select="srm:SearchResultsMetadata/srer:SearchErrors/srer:SearchResultsExceedThresholdError" />

    <xsl:apply-templates select="$accessDenialReasons" />
    <xsl:apply-templates select="$requestErrors" />
    <xsl:apply-templates select="$tooManyResultsErrors" />
    
    <xsl:if test="(not($tooManyResultsErrors) and not($accessDenialReasons) and not($requestErrors))">
		  <xsl:call-template name="dispositions"/>
		</xsl:if>
	</xsl:template>

	<xsl:template name="dispositions">
			<table class="table table-striped table-bordered" style="width:100%" id="searchResultsTable">
				<thead>
					<tr>
						<th>OTN</th>
						<th>NAME</th>
						<th>DOB</th>
						<th>SSN</th>
						<th>DATE OF DISPOSITION</th>
						<th>CHARGE</th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<xsl:apply-templates select="chsres-ext:CriminalHistorySearchResult/j:Arrest/j:ArrestCharge" mode="row"/>
				</tbody>
			</table>
	</xsl:template>
	
	<xsl:template match="j:ArrestCharge" mode="row">
	  
		<xsl:variable name="systemID" select="ancestor::chsres-ext:CriminalHistorySearchResult/intel:SystemIdentifier"/>
		<xsl:variable name="rapbackId" select="ancestor::chsres-ext:CriminalHistorySearchResult/intel:SystemIdentifier/nc:IdentificationID"/>
		<tr>
			<td>
				<xsl:value-of select="ancestor::chsres-ext:CriminalHistorySearchResult/j:Subject[@structures:id=../j:Arrest/j:ArrestSubject/nc:RoleOfPerson/@structures:ref]/j:SubjectIdentification/nc:IdentificationID"></xsl:value-of>
			</td>					
			<td><xsl:apply-templates select="ancestor::chsres-ext:CriminalHistorySearchResult/j:Subject[@structures:id=../j:Arrest/j:ArrestSubject/nc:RoleOfPerson/@structures:ref]/nc:RoleOfPerson/nc:PersonName" mode="primaryName"></xsl:apply-templates></td>
			<td>
				<xsl:apply-templates select="ancestor::chsres-ext:CriminalHistorySearchResult/j:Subject[@structures:id=../j:Arrest/j:ArrestSubject/nc:RoleOfPerson/@structures:ref]/nc:RoleOfPerson/nc:PersonBirthDate/nc:Date" mode="formatDateAsMMDDYYYY"/>
			</td>	
			<td>
				<xsl:apply-templates select="ancestor::chsres-ext:CriminalHistorySearchResult/j:Subject[@structures:id=../j:Arrest/j:ArrestSubject/nc:RoleOfPerson/@structures:ref]/nc:RoleOfPerson/nc:PersonSSNIdentification/nc:IdentificationID"/>
			</td>
			<td>
				<xsl:apply-templates select="j:ChargeDisposition/nc:DispositionDate/nc:Date" mode="formatDateAsMMDDYYYY"/>
			</td>
			<td>
				<xsl:value-of select="j:ChargeDescriptionText"/>
			</td>
			<td align="right" width="120px">
				<i class="fas fa-edit fa-2x" title="edit" data-toggle="tooltip"></i>&#160;
				<i class="fas fa-eye-slash fa-2x" title="hide" data-toggle="tooltip"></i>&#160;
				<i class="fas fa-times-circle fa-2x" title="delete" data-toggle="tooltip"></i>
			</td>
		</tr>
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
