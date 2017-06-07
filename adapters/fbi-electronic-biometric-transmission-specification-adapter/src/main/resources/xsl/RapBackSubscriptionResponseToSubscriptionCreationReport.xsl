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
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:ebts="http://cjis.fbi.gov/fbi_ebts/10.0" xmlns:itl="http://biometrics.nist.gov/standard/2011"
	xmlns:ansi-nist="http://niem.gov/niem/biometrics/1.0"
	xmlns:fed_subcr-doc="http://ojbc.org/IEPD/Exchange/FederalSubscriptionCreationReport/1.0"
	xmlns:fed_subcr-ext="http://ojbc.org/IEPD/Extensions/FederalSubscriptionCreationReportExtension/1.0"
	xmlns:nc="http://niem.gov/niem/niem-core/2.0" xmlns:jxdm41="http://niem.gov/niem/domains/jxdm/4.1"
	xmlns:jxdm50="http://release.niem.gov/niem/domains/jxdm/5.0/"
	xmlns:s30="http://release.niem.gov/niem/structures/3.0/" xmlns:nc30="http://release.niem.gov/niem/niem-core/3.0/">
	<xsl:output indent="yes" method="xml" omit-xml-declaration="no" />
	<xsl:template match="/">
		<xsl:apply-templates
			select="itl:NISTBiometricInformationExchangePackage/itl:PackageDescriptiveTextRecord/itl:UserDefinedDescriptiveDetail/ebts:DomainDefinedDescriptiveFields" />
	</xsl:template>
	<xsl:template
		match="itl:NISTBiometricInformationExchangePackage/itl:PackageDescriptiveTextRecord/itl:UserDefinedDescriptiveDetail/ebts:DomainDefinedDescriptiveFields">
		<fed_subcr-doc:FederalSubscriptionCreationReport>
			<xsl:apply-templates select="ebts:RecordRapBackData" />
			<xsl:apply-templates select="ebts:RecordSubject" />
			<xsl:apply-templates
				select="ebts:RecordTransactionData/ebts:TransactionResponseData/ebts:TransactionElectronicRapSheetText"
				mode="rapsheet" />
		</fed_subcr-doc:FederalSubscriptionCreationReport>
	</xsl:template>
	<xsl:template
		match="ebts:RecordSubject/jxdm50:PersonFBIIdentification/nc:IdentificationID">
	</xsl:template>
	<xsl:template match="ebts:RecordRapBackData">
		<fed_subcr-ext:RapBackSubscriptionData>
			<xsl:apply-templates
				select="ebts:RecordRapBackActivityNotificationFormatCode" />
			<xsl:apply-templates select="ebts:RecordRapBackAttentionText" />
			<xsl:apply-templates select="ebts:RecordRapBackCategoryCode" />
			<xsl:apply-templates select="ebts:RecordRapBackExpirationDate" />
			<xsl:apply-templates select="ebts:RecordRapBackInStateOptOutIndicator" />
			<xsl:apply-templates select="ebts:RecordRapBackSubscriptionDate" />
			<xsl:apply-templates select="ebts:RecordRapBackSubscriptionID" />
			<xsl:apply-templates select="ebts:RecordRapBackSubscriptionTerm" />
			<xsl:apply-templates select="ebts:RecordRapBackTermDate" />
			<xsl:apply-templates
				select="../ebts:RecordTransactionActivity/nc:CaseTrackingID" />
			<xsl:apply-templates
				select="../ebts:RecordTransactionActivity/ebts:RecordControllingAgency" />
			<xsl:apply-templates select="../ansi-nist:RecordForwardOrganizations" />
		</fed_subcr-ext:RapBackSubscriptionData>
	</xsl:template>
	<xsl:template match="ebts:RecordSubject">
		<xsl:apply-templates select="." mode="subject" />
		<xsl:apply-templates select="." mode="person" />
	</xsl:template>
	<xsl:template match="ebts:RecordRapBackActivityNotificationFormatCode">
		<fed_subcr-ext:RapBackActivityNotificationFormatCode>
			<xsl:value-of select="." />
		</fed_subcr-ext:RapBackActivityNotificationFormatCode>
	</xsl:template>
	<xsl:template match="ebts:RecordRapBackAttentionText">
		<fed_subcr-ext:RapBackAttentionText>
			<xsl:value-of select="." />
		</fed_subcr-ext:RapBackAttentionText>
	</xsl:template>
	<xsl:template match="ebts:RecordRapBackCategoryCode">
		<fed_subcr-ext:RapBackCategoryCode>
			<xsl:value-of select="." />
		</fed_subcr-ext:RapBackCategoryCode>
	</xsl:template>
	<xsl:template match="ebts:RecordRapBackExpirationDate">
		<fed_subcr-ext:RapBackExpirationDate>
			<xsl:apply-templates select="nc:Date" />
		</fed_subcr-ext:RapBackExpirationDate>
	</xsl:template>
	<xsl:template match="ebts:RecordRapBackInStateOptOutIndicator">
		<fed_subcr-ext:RapBackInStateOptOutIndicator>
			<xsl:value-of select="." />
		</fed_subcr-ext:RapBackInStateOptOutIndicator>
	</xsl:template>
	<xsl:template match="ebts:RecordRapBackSubscriptionDate">
		<fed_subcr-ext:RapBackSubscriptionDate>
			<xsl:apply-templates select="nc:Date" />
		</fed_subcr-ext:RapBackSubscriptionDate>
	</xsl:template>
	<xsl:template match="ebts:RecordRapBackSubscriptionID">
		<fed_subcr-ext:RapBackSubscriptionIdentification>
			<nc30:IdentificationID>
				<xsl:value-of select="." />
			</nc30:IdentificationID>
		</fed_subcr-ext:RapBackSubscriptionIdentification>
	</xsl:template>
	<xsl:template match="ebts:RecordRapBackSubscriptionTerm">
		<fed_subcr-ext:RapBackSubscriptionTermCode>
			<xsl:value-of select="." />
		</fed_subcr-ext:RapBackSubscriptionTermCode>
	</xsl:template>
	<xsl:template match="ebts:RecordRapBackTermDate">
		<fed_subcr-ext:RapBackTermDate>
			<xsl:apply-templates select="nc:Date" />
		</fed_subcr-ext:RapBackTermDate>
	</xsl:template>
	<xsl:template match="nc:CaseTrackingID">
		<fed_subcr-ext:SubscriptionRelatedCaseIdentification>
			<nc30:IdentificationID>
				<xsl:value-of select="." />
			</nc30:IdentificationID>
		</fed_subcr-ext:SubscriptionRelatedCaseIdentification>
	</xsl:template>
	<xsl:template match="ebts:RecordSubject" mode="subject">
		<jxdm50:Subject>
			<nc30:RoleOfPerson>
				<xsl:attribute name="s30:ref"><xsl:value-of select="generate-id(.)" /></xsl:attribute>
			</nc30:RoleOfPerson>
		</jxdm50:Subject>
	</xsl:template>
	<xsl:template match="ebts:RecordSubject" mode="person">
		<nc30:Person>
			<xsl:attribute name="s30:id"><xsl:value-of
				select="generate-id(/itl:NISTBiometricInformationExchangePackage/itl:PackageDescriptiveTextRecord/itl:UserDefinedDescriptiveDetail/ebts:DomainDefinedDescriptiveFields/ebts:RecordSubject)" /></xsl:attribute>
			<xsl:apply-templates select="nc:PersonBirthDate" />
			<xsl:apply-templates select="nc:PersonName" />
			<xsl:apply-templates select="." mode="augment" />
		</nc30:Person>
	</xsl:template>
	<xsl:template match="nc:PersonBirthDate">
		<nc30:PersonBirthDate>
			<xsl:apply-templates select="nc:Date" />
		</nc30:PersonBirthDate>
	</xsl:template>
	<xsl:template match="nc:PersonName">
		<nc30:PersonName>
			<xsl:apply-templates select="nc:PersonGivenName" />
			<xsl:apply-templates select="nc:PersonMiddleName" />
			<xsl:apply-templates select="nc:PersonSurName" />
		</nc30:PersonName>
	</xsl:template>
	<xsl:template match="ebts:RecordSubject" mode="augment">
		<jxdm50:PersonAugmentation>
			<xsl:apply-templates select="jxdm41:PersonFBIIdentification" />
			<xsl:if
				test="../ebts:RecordRapBackData/ebts:RecordRapBackUserDefinedElement/ebts:UserDefinedElementName=normalize-space('State Fingerprint ID')">
				<xsl:apply-templates
					select="../ebts:RecordRapBackData/ebts:RecordRapBackUserDefinedElement"
					mode="fingerPrint" />
			</xsl:if>
		</jxdm50:PersonAugmentation>
	</xsl:template>
	<xsl:template match="nc:PersonGivenName">
		<nc30:PersonGivenName>
			<xsl:value-of select="." />
		</nc30:PersonGivenName>
	</xsl:template>
	<xsl:template match="nc:PersonMiddleName">
		<nc30:PersonMiddleName>
			<xsl:value-of select="." />
		</nc30:PersonMiddleName>
	</xsl:template>
	<xsl:template match="nc:PersonSurName">
		<nc30:PersonSurName>
			<xsl:value-of select="." />
		</nc30:PersonSurName>
	</xsl:template>
	<xsl:template match="jxdm41:PersonFBIIdentification">
		<jxdm50:PersonFBIIdentification>
			<xsl:apply-templates select="nc:IdentificationID" />
		</jxdm50:PersonFBIIdentification>
	</xsl:template>
	<xsl:template match="ebts:RecordRapBackUserDefinedElement"
		mode="fingerPrint">
		<jxdm50:PersonStateFingerprintIdentification>
			<nc30:IdentificationID>
				<xsl:value-of select="ebts:UserDefinedElementText" />
			</nc30:IdentificationID>
		</jxdm50:PersonStateFingerprintIdentification>
	</xsl:template>
	<xsl:template
		match="ebts:RecordTransactionData/ebts:TransactionResponseData/ebts:TransactionElectronicRapSheetText"
		mode="rapsheet">
		<fed_subcr-ext:CriminalHistoryDocument>
			<xsl:attribute name="s30:id"><xsl:value-of select="generate-id(.)" /></xsl:attribute>
			<nc30:DocumentBinary>
				<fed_subcr-ext:Base64BinaryObject>
					<xsl:value-of select="." />
				</fed_subcr-ext:Base64BinaryObject>
			</nc30:DocumentBinary>
		</fed_subcr-ext:CriminalHistoryDocument>
	</xsl:template>
	<xsl:template match="ebts:RecordControllingAgency">
		<fed_subcr-ext:SubscribingOrganization>
			<xsl:apply-templates select="nc:OrganizationIdentification" />
		</fed_subcr-ext:SubscribingOrganization>
	</xsl:template>
	<xsl:template match="ansi-nist:RecordForwardOrganizations">
		<fed_subcr-ext:RecordForwardOrganization>
			<xsl:apply-templates select="nc:OrganizationIdentification" />
		</fed_subcr-ext:RecordForwardOrganization>
	</xsl:template>
	<xsl:template match="nc:OrganizationIdentification">
		<nc30:OrganizationIdentification>
			<nc30:IdentificationID>
				<xsl:value-of select="nc:IdentificationID" />
			</nc30:IdentificationID>
		</nc30:OrganizationIdentification>
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