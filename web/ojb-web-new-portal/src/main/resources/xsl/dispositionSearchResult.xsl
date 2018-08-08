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
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://ojbc.org/IEPD/Exchange/CriminalHistorySearchResults/1.0 ../xsd/criminal_history_search_results.xsd"
	exclude-result-prefixes="#all">

	<xsl:import href="_formatters.xsl" />
	<xsl:output method="html" encoding="UTF-8" />
	
	<xsl:template match="/chsres-doc:CriminalHistorySearchResults">
		<xsl:call-template name="dispositions"/>
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
					<xsl:apply-templates select="chsres-ext:CriminalHistorySearchResult"/>
				</tbody>
			</table>
	</xsl:template>
	
	<xsl:template match="chsres-ext:CriminalHistorySearchResult">
		<xsl:variable name="systemID" select="intel:SystemIdentifier"/>
		<xsl:variable name="rapbackId" select="intel:SystemIdentifier/nc:IdentificationID"/>
		<tr>
			<td>
				<xsl:value-of select="j:Subject[@structures:id=../j:Arrest/j:ArrestSubject/nc:RoleOfPerson/@structures:ref]/j:SubjectIdentification/nc:IdentificationID"></xsl:value-of>
			</td>					
			<td><xsl:apply-templates select="j:Subject[@structures:id=../j:Arrest/j:ArrestSubject/nc:RoleOfPerson/@structures:ref]/nc:RoleOfPerson/nc:PersonName" mode="primaryName"></xsl:apply-templates></td>
			<td>
				<xsl:apply-templates select="j:Subject[@structures:id=../j:Arrest/j:ArrestSubject/nc:RoleOfPerson/@structures:ref]/nc:RoleOfPerson/nc:PersonBirthDate/nc:Date" mode="formatDateAsMMDDYYYY"/>
			</td>	
			<td>
				<xsl:apply-templates select="j:Subject[@structures:id=../j:Arrest/j:ArrestSubject/nc:RoleOfPerson/@structures:ref]/nc:RoleOfPerson/nc:PersonSSNIdentification/nc:IdentificationID"/>
			</td>
			<td>
				<xsl:apply-templates select="j:Arrest/nc:ActivityDate/nc:Date" mode="formatDateAsMMDDYYYY"/>
			</td>
			<td>
				<xsl:for-each select="j:Arrest/j:ArrestCharge/j:ChargeDescriptionText">
					<xsl:if test="position() != 1">
						<br/>
					</xsl:if>
					<xsl:value-of select="."/>
				</xsl:for-each>
			</td>
			<td align="right" width="115px">
			</td>
		</tr>
	</xsl:template>
	
 </xsl:stylesheet>
