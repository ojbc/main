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
	xmlns:iad="http://ojbc.org/IEPD/Extensions/InformationAccessDenial/1.0"
	xmlns:clq-res-doc="http://ojbc.org/IEPD/Exchange/CannabisLicenseQueryResults/1.0"
	xmlns:clq-res-ext="http://ojbc.org/IEPD/Extensions/CannabisLicenseQueryResults/1.0"
	xmlns:qrer="http://ojbc.org/IEPD/Extensions/QueryRequestErrorReporting/1.0"
	xmlns:qrm="http://ojbc.org/IEPD/Extensions/QueryResultsMetadata/1.0" xmlns:mbsc="http://www.sauper.com/MSBIOnlineServices"
	xmlns:j="http://release.niem.gov/niem/domains/jxdm/6.0/" xmlns:intel="http://release.niem.gov/niem/domains/intelligence/4.0/"
	xmlns:nc="http://release.niem.gov/niem/niem-core/4.0/" xmlns:niem-xs="http://release.niem.gov/niem/proxy/xsd/4.0/"
	xmlns:usps="http://release.niem.gov/niem/codes/usps_states/4.0/"
	xmlns:structures="http://release.niem.gov/niem/structures/4.0/" exclude-result-prefixes="#all">
	<xsl:import href="_formatters.xsl" />
	<xsl:output method="html" encoding="UTF-8" />
	<xsl:template match="/">
		<div class="card">
		  	<div class="card-header"/>
			<div class="card-body detailsTable">
				<xsl:apply-templates select="clq-res-doc:CannabisLicenseQueryResults" />
			</div>
	  	</div>
	</xsl:template>
	<xsl:template match="clq-res-doc:CannabisLicenseQueryResults">
		<xsl:choose>
			<xsl:when
				test="qrm:QueryResultsMetadata/qrer:QueryRequestError | qrm:QueryResultsMetadata/iad:InformationAccessDenial">
				<xsl:apply-templates select="qrm:QueryResultsMetadata/qrer:QueryRequestError" />
				<xsl:apply-templates select="qrm:QueryResultsMetadata/iad:InformationAccessDenial" />
			</xsl:when>
			<xsl:otherwise>
				<script type="text/javascript"> $(function () { $("#cannabisLicensing").accordion({ heightStyle: "content", collapsible: true, activate:
					function( event, ui ) { var modalIframe = $("#modalIframe", parent.document);
					modalIframe.height(modalIframe.contents().find("body").height() + 16); } } ); }); 
				</script>
				<div id="cannabisLicensing">
					<xsl:apply-templates select="clq-res-ext:CannabisLicenseQueryResult" />
				</div>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="clq-res-ext:CannabisLicenseQueryResult">
		<xsl:apply-templates select="clq-res-ext:CannabisLicense"></xsl:apply-templates>
		<xsl:apply-templates select="clq-res-ext:CannabisLicensee"></xsl:apply-templates>
		<xsl:apply-templates select="nc:Case"></xsl:apply-templates>
	</xsl:template>
	<xsl:template match="clq-res-ext:CannabisLicense">
		<h3>License Information</h3>
		<div>
			<table style="width:100%">
				<tr>
					<td class="detailsLabel">License Number</td>
					<td>
						<xsl:value-of select="clq-res-ext:LicenseIdentification/nc:IdentificationID" />
					</td>
					<td class="detailsLabel">Expiration Date</td>
					<td>
						<xsl:value-of select="clq-res-ext:LicenseExpirationDate/nc:Date" />
					</td>
				</tr>
				<tr>
					<td class="detailsLabel">License Type</td>
					<td>
						<xsl:value-of select="clq-res-ext:LicenseCategoryText" />
					</td>
					<td class="detailsLabel">Grower Indicator</td>
					<td>
						<xsl:value-of select="clq-res-ext:LicenseGrowerIndicator"></xsl:value-of>
					</td>
				</tr>
				<tr>
					<td class="detailsLabel">Card Contro lNumber</td>
					<td colspan="3">
						<xsl:value-of select="clq-res-ext:LicenseCardControlNumberID" />
					</td>
				</tr>
				<tr>
				</tr>
				<tr>
					<td class="detailsLabel">License Employer</td>
					<td>
						<xsl:value-of select="clq-res-ext:LicenseEmployer/nc:PersonName/nc:PersonFullName" />
					</td>
					<td class="detailsLabel">License Employee</td>
					<td>
						<xsl:value-of select="clq-res-ext:LicenseEmployee/nc:PersonName/nc:PersonFullName" />
					</td>
				</tr>
			</table>
		</div>
	</xsl:template>
	<xsl:template match="clq-res-ext:CannabisLicensee">
		<xsl:param name="personID" select="@structures:id" />
		<h3>Licensee</h3>
		<div>
			<table style="width:100%">
				<xsl:apply-templates select="." mode="person"></xsl:apply-templates>
				<xsl:apply-templates select="." mode="mailing_address"></xsl:apply-templates>
				<xsl:apply-templates
					select="../nc:Location[@structures:id = /clq-res-doc:CannabisLicenseQueryResults/clq-res-ext:CannabisLicenseQueryResult/nc:PersonResidenceAssociation[nc:Person/@structures:ref=$personID]/nc:Location/@structures:ref]"
					mode="residence_address"></xsl:apply-templates>
				<xsl:apply-templates
					select="../nc:Location[@structures:id = /clq-res-doc:CannabisLicenseQueryResults/clq-res-ext:CannabisLicenseQueryResult/clq-res-ext:PersonCultivationLocationAssociation[nc:Person/@structures:ref=$personID]/nc:Location/@structures:ref]"
					mode="cultivation_location"></xsl:apply-templates>
				<xsl:apply-templates
					select="../nc:Location[@structures:id = /clq-res-doc:CannabisLicenseQueryResults/clq-res-ext:CannabisLicenseQueryResult/clq-res-ext:PersonManufacturingLocationAssociation[nc:Person/@structures:ref=$personID]/nc:Location/@structures:ref]"
					mode="manufacturing_location"></xsl:apply-templates>
			</table>
		</div>
	</xsl:template>
	<xsl:template match="clq-res-ext:CannabisLicensee" mode="person">
		<tr>
			<td class="detailsLabel">First Name</td>
			<td>
				<xsl:value-of select="nc:PersonName/nc:PersonGivenName" />
			</td>
		</tr>
		<tr>
			<td class="detailsLabel">Middle Name</td>
			<td>
				<xsl:value-of select="nc:PersonName/nc:PersonMiddleName" />
			</td>
		</tr>
		<tr>
			<td class="detailsLabel">Last Name</td>
			<td>
				<xsl:value-of select="nc:PersonName/nc:PersonSurName" />
			</td>
		</tr>
		<tr>
			<td class="detailsLabel">Name Suffix</td>
			<td>
				<xsl:value-of select="nc:PersonName/nc:PersonNameSuffixText" />
			</td>
		</tr>
		<tr>
			<td class="detailsLabel">Date Of Birth</td>
			<td>
				<xsl:value-of select="nc:PersonBirthDate/nc:Date" />
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="clq-res-ext:CannabisLicensee" mode="mailing_address">
		<tr>
			<td class="detailsLabel">Mailing Address</td>
			<td>
				<xsl:apply-templates select="nc:PersonHomeContactInformation/nc:ContactMailingAddress" />
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="nc:Location" mode="residence_address">
		<tr>
			<td class="detailsLabel">Residence Address</td>
			<td>
				<xsl:apply-templates select="nc:Address" />
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="nc:Location" mode="cultivation_location">
		<tr>
			<td class="detailsLabel">Cultivation Location</td>
			<td>
				<xsl:apply-templates select="nc:Address" />
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="nc:Location" mode="manufacturing_location">
		<tr>
			<td class="detailsLabel">Manufacturing Address</td>
			<td>
				<xsl:apply-templates select="nc:Address" />
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="nc:ContactMailingAddress | nc:Address">
		<xsl:value-of select="nc:LocationStreet/nc:StreetFullText"></xsl:value-of>
		<xsl:text> </xsl:text>
		<xsl:value-of select="nc:LocationCityName" />
		<xsl:text>, </xsl:text>
		<xsl:value-of select="nc:LocationState/nc:LocationStateUSPostalServiceCode" />
		<xsl:text> </xsl:text>
		<xsl:value-of select="nc:LocationPostalCode"></xsl:value-of>
		<xsl:text> </xsl:text>
		<xsl:value-of select="nc:LocationCountry/nc:LocationCountryName"></xsl:value-of>
	</xsl:template>
	<xsl:template match="qrer:QueryRequestError">
		<span class="error">
			Profession Regulatory License Query Result Error:
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
</xsl:stylesheet>