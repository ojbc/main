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
	xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:iad="http://ojbc.org/IEPD/Extensions/InformationAccessDenial/1.0"
	xmlns:intel="http://release.niem.gov/niem/domains/intelligence/4.1/" xmlns:j="http://release.niem.gov/niem/domains/jxdm/6.1/"
	xmlns:nc="http://release.niem.gov/niem/niem-core/4.0/" xmlns:ncic="http://release.niem.gov/niem/codes/fbi_ncic/4.0/"
	xmlns:niem-xs="http://release.niem.gov/niem/proxy/xsd/4.0/"
	xmlns:pdq-res-doc="http://ojbc.org/IEPD/Exchange/PersonDetailQueryResults/1.0"
	xmlns:pdq-res-ext="http://ojbc.org/IEPD/Extensions/PersonDetailQueryResultsExtension/1.0"
	xmlns:me-imc-codes="http://ojbc.org/IEPD/Extensions/Maine/IMC/Codes/1.0"
	xmlns:qrer="http://ojbc.org/IEPD/Extensions/QueryRequestErrorReporting/1.0"
	xmlns:qrm="http://ojbc.org/IEPD/Extensions/QueryResultsMetadata/1.0"
	xmlns:structures="http://release.niem.gov/niem/structures/4.0/" xmlns:usps="http://release.niem.gov/niem/codes/usps_states/4.0/"
	exclude-result-prefixes="#all">
	<xsl:import href="_formatters.xsl" />
	<xsl:output method="html" encoding="UTF-8" />
	<xsl:template match="/pdq-res-doc:PersonDetailQueryResults">
		<div id="personDetailTabs">
			<ul>
				<li>
					<a href="#personDetailTab">PERSON DETAIL</a>
				</li>
				<li>
					<a href="#personReportTab">PERSON REPORT</a>
				</li>
			</ul>
			<div id="personDetailTab">
				<xsl:apply-templates select="pdq-res-ext:PersonDetailReport" mode="personDetailTab" />
			</div>
			<div id="personReportTab">
				<xsl:apply-templates select="pdq-res-ext:PersonDetailReport" mode="personReportTab" />
			</div>
		</div>
	</xsl:template>
	<xsl:template match="pdq-res-ext:PersonDetailReport" mode="personDetailTab">
		<table>
			<tr>
				<xsl:for-each
					select="nc:Image[@structures:id=../nc:Person/nc:PersonDigitalImage/@structures:ref]/nc:BinaryBase64Object">
					<td style="vertical-align: top;">
						<xsl:element name="img">
							<xsl:attribute name="id">imageHolder</xsl:attribute>
							<xsl:attribute name="style">max-width: 180px;max-height: 250px;</xsl:attribute>
							<xsl:attribute name="src">data:image/jpeg;base64,<xsl:value-of
								select="." /></xsl:attribute>
						</xsl:element>
					</td>
				</xsl:for-each>
			</tr>
		</table>
		<table class="detailsTable">
			<tr>
				<td colspan="8" class="detailsFullName">
					<xsl:apply-templates select="nc:Person/nc:PersonName" mode="constructName" />
				</td>
			</tr>
			<tr>
				<td colspan="8" class="detailsTitle">PERSON DETAIL</td>
			</tr>
			<tr>
				<td colspan="2" class="detailsLabel">DL#/ISSUER</td>
				<td colspan="2">
					<xsl:value-of
						select="j:DriverLicense[@structures:id=../j:PersonDriverLicenseAssociation/j:DriverLicense/@structures:ref]/j:DriverLicenseIdentification/nc:IdentificationID" />
					<xsl:text> (</xsl:text>
					<xsl:value-of
						select="j:DriverLicense[@structures:id=../j:PersonDriverLicenseAssociation/j:DriverLicense/@structures:ref]/j:DriverLicenseIdentification/nc:IdentificationJurisdiction/nc:JurisdictionText" />
					<xsl:text>)</xsl:text>
				</td>
			</tr>
			<tr>
				<td colspan="2" class="detailsLabel">SSN</td>
				<td colspan="2">
					<xsl:call-template name="formatSSN">
						<xsl:with-param name="ssn" select="nc:Person/nc:PersonSSNIdentification/nc:IdentificationID" />
					</xsl:call-template>
				</td>
			</tr>
			<tr>
				<td colspan="2" class="detailsLabel">DOB</td>
				<td colspan="2">
					<xsl:value-of select="nc:Person/nc:PersonBirthDate/nc:Date" />
				</td>
			</tr>
			<tr>
				<td colspan="2" class="detailsLabel">SEX</td>
				<td colspan="2">
					<xsl:value-of select="nc:Person/j:PersonSexCode" />
				</td>
			</tr>
			<tr>
				<td colspan="2" class="detailsLabel">CONTACT INFORMATION</td>
				<td colspan="6">
					<xsl:apply-templates
						select="nc:Location[@structures:id=../nc:PersonCurrentLocationAssociation/nc:Location/@structures:ref]/nc:Address"
						mode="constructAddress" />
				</td>
			</tr>
			<tr>
				<td colspan="2" class="detailsLabel">GANG AFFILIATION</td>
				<td colspan="6">
					<xsl:value-of
						select="nc:Organization[@structures:id=../j:PersonGangAssociation/nc:Organization/@structures:ref]/nc:OrganizationName" />
				</td>
			</tr>
			<tr>
				<td colspan="2" class="detailsLabel">INDICATORS</td>
				<xsl:if test="nc:Person/pdq-res-ext:PersonSexOffenderIndicator[.='true']">
					<td colspan="6">
						<xsl:value-of select="'Sex Offender'" />
					</td>
				</xsl:if>
			</tr>
			<tr>
				<td colspan="2" class="detailsLabel"></td>
				<xsl:if test="nc:Person/pdq-res-ext:PersonCautionIndicator[.='true']">
					<td colspan="6">
						<xsl:value-of select="'Caution'" />
					</td>
				</xsl:if>
			</tr>
			<tr>
				<td colspan="2" class="detailsLabel"></td>
				<xsl:if test="nc:Person/pdq-res-ext:PersonSuicidalIndicator[.='true']">
					<td colspan="6">
						<xsl:value-of select="'Suicidal'" />
					</td>
				</xsl:if>
			</tr>
			<tr>
				<td colspan="2" class="detailsLabel">GUN PERMITS:</td>
				<td colspan="6">
					<xsl:value-of select="nc:Person/pdq-res-ext:PersonNumberOfGunPermits" />
				</td>
			</tr>
			<tr>
				<td colspan="2" class="detailsLabel">ARRESTS:</td>
				<td colspan="6">
					<xsl:value-of select="nc:Person/pdq-res-ext:PersonNumberOfArrests" />
				</td>
			</tr>
			<tr>
				<td colspan="2" class="detailsLabel">WARRANTS:</td>
				<td colspan="6">
					<xsl:value-of select="nc:Person/pdq-res-ext:PersonNumberOfWarrants" />
				</td>
			</tr>
			<tr>
				<td colspan="2" class="detailsLabel">RESTRAINING ORDERS:</td>
				<td colspan="6">
					<xsl:value-of select="nc:Person/pdq-res-ext:PersonNumberOfRestrainingOrders" />
				</td>
			</tr>
		</table>
	</xsl:template>
	<xsl:template match="pdq-res-ext:PersonDetailReport" mode="personReportTab">
		<table class="detailsTable">
			<tr>
				<td colspan="2" class="detailsLabel">PERSON REPORT:</td>
				<td rowspan="6" colspan="20">
					<pre>
						<xsl:value-of select="nc:Person/pdq-res-ext:PersonDetailInformationText" />
					</pre>
				</td>
			</tr>
		</table>
	</xsl:template>
	<xsl:template match="nc:PersonName" mode="constructName">
		<xsl:value-of select="concat(nc:PersonSurName,', ',nc:PersonGivenName,', ',nc:PersonMiddleName)" />
	</xsl:template>
	<xsl:template match="nc:Address" mode="constructAddress">
		<xsl:choose>
			<xsl:when test="nc:LocationPostalExtensionCode">
				<xsl:value-of
					select="concat(nc:LocationStreet/nc:StreetFullText,', ',nc:LocationCityName,', ',nc:LocationState/nc:LocationStateUSPostalServiceCode, ' ', nc:LocationPostalCode,'-',nc:LocationPostalExtensionCode)" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of
					select="concat(nc:LocationStreet/nc:StreetFullText,', ',nc:LocationCityName,',
				',nc:LocationState/nc:LocationStateUSPostalServiceCode, ' ', nc:LocationPostalCode)" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>