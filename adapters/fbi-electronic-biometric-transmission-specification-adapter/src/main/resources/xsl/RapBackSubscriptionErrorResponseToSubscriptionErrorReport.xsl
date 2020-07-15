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
<xsl:stylesheet version="2.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ebts="http://cjis.fbi.gov/fbi_ebts/10.0"
	xmlns:itl="http://biometrics.nist.gov/standard/2011" xmlns:ansi-nist="http://niem.gov/niem/biometrics/1.0"
	xmlns:fed_subcr_error="http://ojbc.org/IEPD/Exchange/FederalSubscriptionErrorReport/1.0"
	xmlns:fed_subcr_error-ext="http://ojbc.org/IEPD/Extensions/FederalSubscriptionErrorReportExtension/1.0"
	xmlns:nc="http://niem.gov/niem/niem-core/2.0" xmlns:jxdm41="http://niem.gov/niem/domains/jxdm/4.1"
	xmlns:jxdm50="http://release.niem.gov/niem/domains/jxdm/5.0/" xmlns:s30="http://release.niem.gov/niem/structures/3.0/"
	xmlns:nc30="http://release.niem.gov/niem/niem-core/3.0/" exclude-result-prefixes="ebts itl ansi-nist jxdm41 nc">
	<xsl:output indent="yes" method="xml" omit-xml-declaration="no" />
	<xsl:param name="stateSubscriptionID" />
	<xsl:template match="/">
		<xsl:apply-templates
			select="itl:NISTBiometricInformationExchangePackage/itl:PackageDescriptiveTextRecord/itl:UserDefinedDescriptiveDetail/ebts:DomainDefinedDescriptiveFields"
			mode="main" />
	</xsl:template>
	<xsl:template match="ebts:DomainDefinedDescriptiveFields" mode="main">
		<fed_subcr_error:FederalSubscriptionErrorReport>
			<fed_subcr_error-ext:RapBackSubscriptionData>
				<xsl:apply-templates select="." mode="state" />
				<xsl:apply-templates select="." mode="domain" />
			</fed_subcr_error-ext:RapBackSubscriptionData>
		</fed_subcr_error:FederalSubscriptionErrorReport>
	</xsl:template>
	<xsl:template match="ebts:DomainDefinedDescriptiveFields" mode="state">
		<fed_subcr_error-ext:StateSubscriptionIdentification>
			<nc30:IdentificationID>
				<xsl:value-of select="$stateSubscriptionID" />
			</nc30:IdentificationID>
		</fed_subcr_error-ext:StateSubscriptionIdentification>
	</xsl:template>
	<xsl:template match="ebts:DomainDefinedDescriptiveFields" mode="domain">
		<xsl:apply-templates
			select="ebts:RecordTransactionData/ebts:TransactionResponseData/ebts:TransactionStatusText" mode="transactionStatus" />
		<xsl:apply-templates select="ebts:RecordTransactionActivity/ebts:RecordControllingAgency" />
	</xsl:template>
	<xsl:template match="ebts:TransactionStatusText" mode="transactionStatus">
		<fed_subcr_error-ext:SubscribtionTransactionStatusText>
			<xsl:value-of select="." />
		</fed_subcr_error-ext:SubscribtionTransactionStatusText>
	</xsl:template>
	<xsl:template match="ebts:RecordControllingAgency">
		<fed_subcr_error-ext:SubscribingOrganization>
			<xsl:apply-templates select="nc:OrganizationIdentification" mode="ori" />
		</fed_subcr_error-ext:SubscribingOrganization>
	</xsl:template>
	<xsl:template match="nc:OrganizationIdentification">
		<nc30:OrganizationIdentification>
			<nc30:IdentificationID>
				<xsl:value-of select="nc:IdentificationID" />
			</nc30:IdentificationID>
		</nc30:OrganizationIdentification>
	</xsl:template>
	<xsl:template match="nc:OrganizationIdentification" mode="ori">
		<jxdm50:OrganizationAugmentation>
			<jxdm50:OrganizationORIIdentification>
				<nc30:IdentificationID>
					<xsl:value-of select="nc:IdentificationID" />
				</nc30:IdentificationID>
			</jxdm50:OrganizationORIIdentification>
		</jxdm50:OrganizationAugmentation>
	</xsl:template>
	<xsl:template match="nc:IdentificationID">
		<nc30:IdentificationID>
			<xsl:value-of select="." />
		</nc30:IdentificationID>
	</xsl:template>
	<xsl:template match="nc:Date">
		<nc30:Date>
			<xsl:value-of select="." />
		</nc30:Date>
	</xsl:template>
</xsl:stylesheet>