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
<xsl:stylesheet version="2.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ebts="http://cjis.fbi.gov/fbi_ebts/10.0" xmlns:itl="http://biometrics.nist.gov/standard/2011" xmlns:ansi-nist="http://niem.gov/niem/biometrics/1.0" xmlns:fed_subcr_upd-doc="http://ojbc.org/IEPD/Exchange/FederalSubscriptionUpdateReport/1.0" xmlns:fed_subcr_upd-ext="http://ojbc.org/IEPD/Extensions/FederalSubscriptionUpdateReportExtension/1.0" xmlns:nc="http://niem.gov/niem/niem-core/2.0" xmlns:jxdm41="http://niem.gov/niem/domains/jxdm/4.1" xmlns:jxdm50="http://release.niem.gov/niem/domains/jxdm/5.0/" xmlns:s30="http://release.niem.gov/niem/structures/3.0/" xmlns:nc30="http://release.niem.gov/niem/niem-core/3.0/">
	<xsl:output indent="yes" method="xml" omit-xml-declaration="no"/>
	<xsl:template match="/">
		<xsl:apply-templates select="itl:NISTBiometricInformationExchangePackage/itl:PackageDescriptiveTextRecord/itl:UserDefinedDescriptiveDetail/ebts:DomainDefinedDescriptiveFields"/>
	</xsl:template>
	<xsl:template match="itl:NISTBiometricInformationExchangePackage/itl:PackageDescriptiveTextRecord/itl:UserDefinedDescriptiveDetail/ebts:DomainDefinedDescriptiveFields">
		<fed_subcr_upd-doc:FederalSubscriptionUpdateReport>
			<xsl:apply-templates select="ebts:RecordRapBackData"/>
			<xsl:apply-templates select="ebts:RecordSubject"/>
			<xsl:apply-templates select="ebts:RecordTransactionData/ebts:TransactionResponseData/ebts:TransactionElectronicRapSheetText" mode="rapsheet"/>
		</fed_subcr_upd-doc:FederalSubscriptionUpdateReport>
	</xsl:template>
	<xsl:template match="ebts:RecordSubject/jxdm50:PersonFBIIdentification/nc:IdentificationID">
	</xsl:template>
	<xsl:template match="ebts:RecordRapBackData">
		<fed_subcr_upd-ext:RapBackSubscriptionData>
			<xsl:apply-templates select="ebts:RecordRapBackActivityNotificationFormatCode"/>
			<!-- xsl:apply-templates select="ebts:RecordRapBackCategoryCode"/-->
			<xsl:apply-templates select="ebts:RecordRapBackExpirationDate"/>
			<xsl:apply-templates select="ebts:RecordRapBackInStateOptOutIndicator"/>
			<xsl:apply-templates select="ebts:RecordRapBackSubscriptionDate"/>
			<xsl:apply-templates select="ebts:RecordRapBackSubscriptionID"/>
			<xsl:apply-templates select="ebts:RecordRapBackSubscriptionTerm"/>
			<xsl:apply-templates select="ebts:RecordRapBackTermDate"/>
		</fed_subcr_upd-ext:RapBackSubscriptionData>
	</xsl:template>
	<xsl:template match="ebts:RecordSubject">
		<xsl:apply-templates select="." mode="subject"/>
		<xsl:apply-templates select="jxdm41:PersonFBIIdentification"/>
	</xsl:template>
	<xsl:template match="ebts:RecordRapBackActivityNotificationFormatCode">
		<fed_subcr_upd-ext:RapBackActivityNotificationFormatCode>
			<xsl:value-of select="."/>
		</fed_subcr_upd-ext:RapBackActivityNotificationFormatCode>
	</xsl:template>
	<!-- xsl:template match="ebts:RecordRapBackCategoryCode">
		<fed_subcr_upd-ext:RapBackCategoryCode>
			<xsl:value-of select="."/>
		</fed_subcr_upd-ext:RapBackCategoryCode>
	</xsl:template-->
	<xsl:template match="ebts:RecordRapBackExpirationDate">
		<fed_subcr_upd-ext:RapBackExpirationDate>
			<xsl:apply-templates select="nc:Date"/>
		</fed_subcr_upd-ext:RapBackExpirationDate>
	</xsl:template>
	<xsl:template match="ebts:RecordRapBackInStateOptOutIndicator">
		<fed_subcr_upd-ext:RapBackInStateOptOutIndicator>
			<xsl:value-of select="."/>
		</fed_subcr_upd-ext:RapBackInStateOptOutIndicator>
	</xsl:template>
	<xsl:template match="ebts:RecordRapBackSubscriptionDate">
		<fed_subcr_upd-ext:RapBackSubscriptionDate>
			<xsl:apply-templates select="nc:Date"/>
		</fed_subcr_upd-ext:RapBackSubscriptionDate>
	</xsl:template>
	<xsl:template match="ebts:RecordRapBackSubscriptionID">
		<fed_subcr_upd-ext:RapBackSubscriptionIdentification>
			<nc30:IdentificationID>
				<xsl:value-of select="."/>
			</nc30:IdentificationID>
		</fed_subcr_upd-ext:RapBackSubscriptionIdentification>
	</xsl:template>
	<xsl:template match="ebts:RecordRapBackSubscriptionTerm">
		<fed_subcr_upd-ext:RapBackSubscriptionTermCode>
			<xsl:value-of select="."/>
		</fed_subcr_upd-ext:RapBackSubscriptionTermCode>
	</xsl:template>
	<xsl:template match="ebts:RecordRapBackTermDate">
		<fed_subcr_upd-ext:RapBackTermDate>
			<xsl:apply-templates select="nc:Date"/>
		</fed_subcr_upd-ext:RapBackTermDate>
	</xsl:template>
	<xsl:template match="ebts:RecordSubject" mode="subject">
		<jxdm50:Subject>
			<nc30:RoleOfPerson>
				<xsl:attribute name="s30:ref"><xsl:value-of select="generate-id(.)"/></xsl:attribute>
			</nc30:RoleOfPerson>
		</jxdm50:Subject>
	</xsl:template>
	<xsl:template match="jxdm41:PersonFBIIdentification">
		<nc30:Person>
			<xsl:attribute name="s30:id"><xsl:value-of select="generate-id(/itl:NISTBiometricInformationExchangePackage/itl:PackageDescriptiveTextRecord/itl:UserDefinedDescriptiveDetail/ebts:DomainDefinedDescriptiveFields/ebts:RecordSubject)"/></xsl:attribute>
			<jxdm50:PersonAugmentation>
				<jxdm50:PersonFBIIdentification>
					<xsl:apply-templates select="nc:IdentificationID"/>
				</jxdm50:PersonFBIIdentification>
			</jxdm50:PersonAugmentation>
		</nc30:Person>
	</xsl:template>
	<xsl:template match="ebts:RecordTransactionData/ebts:TransactionResponseData/ebts:TransactionElectronicRapSheetText" mode="rapsheet">
		<fed_subcr_upd-ext:CriminalHistoryDocument>
			<xsl:attribute name="s30:ref"><xsl:value-of select="generate-id(.)"/></xsl:attribute>
			<nc:DocumentBinary>
				<fed_subcr_upd-ext:Base64BinaryObject>
					<xsl:value-of select="."/>
				</fed_subcr_upd-ext:Base64BinaryObject>
			</nc:DocumentBinary>
		</fed_subcr_upd-ext:CriminalHistoryDocument>
	</xsl:template>
	<xsl:template match="nc:IdentificationID">
		<nc30:IdentificationID>
			<xsl:value-of select="."/>
		</nc30:IdentificationID>
	</xsl:template>
	<xsl:template match="nc:Date">
		<nc30:Date>
			<xsl:value-of select="."/>
		</nc30:Date>
	</xsl:template>
</xsl:stylesheet>
