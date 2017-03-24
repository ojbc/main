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
	xmlns:fchr-doc="http://ojbc.org/IEPD/Exchange/FederalCriminalHistoryReport/1.0"
	xmlns:fchr-ext="http://ojbc.org/IEPD/Extensions/FederalCriminalHistoryReportExtension/1.0"
	xmlns:nc="http://release.niem.gov/niem/niem-core/3.0/" xmlns:j="http://release.niem.gov/niem/domains/jxdm/5.1/"
	xmlns:niem-xs="http://release.niem.gov/niem/proxy/xsd/3.0/"
	xmlns:structures="http://release.niem.gov/niem/structures/3.0/"
	xmlns:ebts="http://cjis.fbi.gov/fbi_ebts/10.0" xmlns:ansi-nist="http://niem.gov/niem/biometrics/1.0"
	xmlns:itl="http://biometrics.nist.gov/standard/2011" xmlns:nc20="http://niem.gov/niem/niem-core/2.0"
	xmlns:j41="http://niem.gov/niem/domains/jxdm/4.1"
	exclude-result-prefixes="ebts ansi-nist itl nc20 j41">
	<xsl:output indent="yes" method="xml" omit-xml-declaration="yes" />
	<xsl:param name="base64Rapsheet" />
	<xsl:template match="/itl:NISTBiometricInformationExchangePackage">
		<fchr-doc:FederalCriminalHistoryReport>
			<xsl:apply-templates select="itl:PackageDescriptiveTextRecord" />
			<xsl:apply-templates
				select="itl:PackageDescriptiveTextRecord/itl:UserDefinedDescriptiveDetail/ebts:DomainDefinedDescriptiveFields/ebts:RecordSubject" />
		</fchr-doc:FederalCriminalHistoryReport>
	</xsl:template>
	<xsl:template match="itl:PackageDescriptiveTextRecord">
		<fchr-ext:TransactionDescriptiveRecord>
			<xsl:apply-templates select="ansi-nist:RecordCategoryCode" />
			<xsl:apply-templates select="ansi-nist:ImageReferenceIdentification" />
			<xsl:apply-templates
				select="itl:UserDefinedDescriptiveDetail/ebts:DomainDefinedDescriptiveFields/ansi-nist:RecordForwardOrganizations" />
			<xsl:apply-templates
				select="itl:UserDefinedDescriptiveDetail/ebts:DomainDefinedDescriptiveFields/ebts:RecordTransactionActivity/ebts:RecordControllingAgency" />
			<xsl:apply-templates select="." mode="rapsheet" />
			<xsl:apply-templates
				select="itl:UserDefinedDescriptiveDetail/ebts:DomainDefinedDescriptiveFields/ebts:RecordRapBackData"
				mode="subscription" />
			<xsl:apply-templates
				select="itl:UserDefinedDescriptiveDetail/ebts:DomainDefinedDescriptiveFields/ebts:RecordRapBackData"
				mode="notification" />
		</fchr-ext:TransactionDescriptiveRecord>
	</xsl:template>
	<xsl:template match="ansi-nist:RecordCategoryCode">
		<fchr-ext:RecordCategoryCodeText>
			<xsl:value-of select="." />
		</fchr-ext:RecordCategoryCodeText>
	</xsl:template>
	<xsl:template match="ansi-nist:ImageReferenceIdentification">
		<fchr-ext:ImageReferenceIdentification>
			<xsl:apply-templates select="nc20:IdentificationID" />
		</fchr-ext:ImageReferenceIdentification>
	</xsl:template>
	<xsl:template match="ansi-nist:RecordForwardOrganizations">
		<fchr-ext:RecordForwardOrganization>
			<nc:EntityOrganization>
				<xsl:apply-templates select="nc20:OrganizationIdentification" />
			</nc:EntityOrganization>
		</fchr-ext:RecordForwardOrganization>
	</xsl:template>
	<xsl:template match="ebts:RecordControllingAgency">
		<fchr-ext:RecordControllingOrganization>
			<nc:EntityOrganization>
				<xsl:apply-templates select="nc20:OrganizationIdentification" />
			</nc:EntityOrganization>
		</fchr-ext:RecordControllingOrganization>
	</xsl:template>
	<xsl:template match="itl:PackageDescriptiveTextRecord"
		mode="rapsheet">
		<fchr-ext:FederalCriminalHistoryRecordDocument>
			<nc:DocumentBinary>
				<fchr-ext:Base64BinaryObject>
					<xsl:value-of select="$base64Rapsheet" />
				</fchr-ext:Base64BinaryObject>
			</nc:DocumentBinary>
		</fchr-ext:FederalCriminalHistoryRecordDocument>
	</xsl:template>
	<xsl:template match="nc20:OrganizationIdentification">
		<nc:OrganizationIdentification>
			<xsl:apply-templates select="nc20:IdentificationID" />
		</nc:OrganizationIdentification>
	</xsl:template>
	<xsl:template match="ebts:RecordRapBackData" mode="subscription">
		<fchr-ext:RapBackSubscription>
			<xsl:apply-templates select="ebts:RecordRapBackExpirationDate" />
			<xsl:apply-templates select="ebts:RecordRapBackSubscriptionDate" />
			<xsl:apply-templates select="ebts:RecordRapBackSubscriptionID" />
			<xsl:apply-templates select="ebts:RecordRapBackSubscriptionTerm" />
			<xsl:apply-templates select="ebts:RecordRapBackTermDate" />
			<xsl:apply-templates
				select="ebts:RecordRapBackUserDefinedElement/ebts:UserDefinedElementText[../ebts:UserDefinedElementName='Subscription Qualifier ID']"
				mode="subqid" />
		</fchr-ext:RapBackSubscription>
	</xsl:template>
	<xsl:template match="ebts:RecordRapBackExpirationDate">
		<fchr-ext:RapBackExpirationDate>
			<xsl:apply-templates select="nc20:Date" />
		</fchr-ext:RapBackExpirationDate>
	</xsl:template>
	<xsl:template match="ebts:RecordRapBackSubscriptionDate">
		<fchr-ext:RapBackSubscriptionDate>
			<xsl:apply-templates select="nc20:Date" />
		</fchr-ext:RapBackSubscriptionDate>
	</xsl:template>
	<xsl:template match="ebts:RecordRapBackSubscriptionID">
		<fchr-ext:RapBackSubscriptionIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="." />
			</nc:IdentificationID>
		</fchr-ext:RapBackSubscriptionIdentification>
	</xsl:template>
	<xsl:template match="ebts:RecordRapBackSubscriptionTerm">
		<fchr-ext:RapBackSubscriptionTermCode>
			<xsl:value-of select="." />
		</fchr-ext:RapBackSubscriptionTermCode>
	</xsl:template>
	<xsl:template match="ebts:RecordRapBackTermDate">
		<fchr-ext:RapBackTermDate>
			<xsl:apply-templates select="nc20:Date" />
		</fchr-ext:RapBackTermDate>
	</xsl:template>
	<xsl:template match="ebts:UserDefinedElementText" mode="subqid">
		<fchr-ext:SubscriptionQualifierIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="." />
			</nc:IdentificationID>
		</fchr-ext:SubscriptionQualifierIdentification>
	</xsl:template>
	<xsl:template match="ebts:UserDefinedElementText" mode="sid">
		<j:PersonStateFingerprintIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="." />
			</nc:IdentificationID>
		</j:PersonStateFingerprintIdentification>
	</xsl:template>
	<xsl:template match="ebts:RecordRapBackData" mode="notification">
		<fchr-ext:RapBackNotificationEvent>
			<xsl:apply-templates select="ebts:RecordRapBackActivityNotificationID" />
			<xsl:apply-templates select="ebts:RecordRapBackAttentionText" />
			<xsl:apply-templates
				select="ebts:TransactionRapBackTriggeringEvent/ebts:RapBackEventDate" />
			<xsl:apply-templates
				select="ebts:TransactionRapBackTriggeringEvent/ebts:RapBackTriggeringEventCode" />
			<xsl:apply-templates
				select="ebts:TransactionRapBackTriggeringEvent/ebts:RapBackEventText" />
		</fchr-ext:RapBackNotificationEvent>
	</xsl:template>
	<xsl:template match="ebts:RecordRapBackActivityNotificationID">
		<fchr-ext:RapBackActivityNotificationIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="." />
			</nc:IdentificationID>
		</fchr-ext:RapBackActivityNotificationIdentification>
	</xsl:template>
	<xsl:template match="ebts:RecordRapBackAttentionText">
		<fchr-ext:RapBackAttentionText>
			<xsl:value-of select="." />
		</fchr-ext:RapBackAttentionText>
	</xsl:template>
	<xsl:template match="ebts:RapBackEventDate">
		<fchr-ext:RapBackEventDate>
			<xsl:apply-templates select="nc20:Date" />
		</fchr-ext:RapBackEventDate>
	</xsl:template>
	<xsl:template match="ebts:RapBackTriggeringEventCode">
		<fchr-ext:TriggeringEvents>
			<fchr-ext:FederalTriggeringEventCode>
				<xsl:choose>
					<xsl:when test=".='1' or .='3'">
						<xsl:value-of select="'ARREST'" />
					</xsl:when>
					<xsl:when test=".='2'">
						<xsl:value-of select="'DISPOSITION'" />
					</xsl:when>
					<xsl:when test=".='5'">
						<xsl:value-of select="'NCIC-WARRANT-ENTRY'" />
					</xsl:when>
					<xsl:when test=".='7'">
						<xsl:value-of select="'NCIC-WARRANT-MODIFICATION'" />
					</xsl:when>
					<xsl:when test=".='6'">
						<xsl:value-of select="'NCIC-WARRANT-DELETION'" />
					</xsl:when>
					<xsl:when test=".='8'">
						<xsl:value-of select="'NCIC-SOR-ENTRY'" />
					</xsl:when>
					<xsl:when test=".='10'">
						<xsl:value-of select="'NCIC-SOR-MODIFICATION'" />
					</xsl:when>
					<xsl:when test=".='9'">
						<xsl:value-of select="'NCIC-SOR-DELETION'" />
					</xsl:when>
					<xsl:when test=".='12'">
						<xsl:value-of select="'DEATH'" />
					</xsl:when>
				</xsl:choose>
			</fchr-ext:FederalTriggeringEventCode>
		</fchr-ext:TriggeringEvents>
	</xsl:template>
	<xsl:template match="ebts:RapBackEventText">
		<fchr-ext:RapBackEventText>
			<xsl:value-of select="." />
		</fchr-ext:RapBackEventText>
	</xsl:template>
	<xsl:template match="ebts:RecordSubject">
		<nc:Person>
			<xsl:apply-templates select="nc20:PersonBirthDate" />
			<xsl:apply-templates select="ebts:PersonName" />
			<xsl:apply-templates select="." mode="ids" />
		</nc:Person>
	</xsl:template>
	<xsl:template match="nc20:PersonBirthDate/nc20:Date">
		<nc:PersonBirthDate>
			<nc:Date>
				<xsl:value-of select="." />
			</nc:Date>
		</nc:PersonBirthDate>
	</xsl:template>
	<xsl:template match="ebts:PersonName">
		<nc:PersonName>
			<nc:PersonGivenName>
				<xsl:value-of select="./nc20:PersonGivenName" />
			</nc:PersonGivenName>
			<nc:PersonMiddleName>
				<xsl:value-of select="./nc20:PersonMiddleName" />
			</nc:PersonMiddleName>
			<nc:PersonSurName>
				<xsl:value-of select="./nc20:PersonSurName" />
			</nc:PersonSurName>
		</nc:PersonName>
	</xsl:template>
	<xsl:template match="ebts:RecordSubject" mode="ids">
		<j:PersonAugmentation>
			<xsl:apply-templates select="./j41:PersonFBIIdentification" />
			<xsl:apply-templates
				select="../ebts:RecordRapBackData/ebts:RecordRapBackUserDefinedElement/ebts:UserDefinedElementText[../ebts:UserDefinedElementName='State Fingerprint ID']"
				mode="sid" />
		</j:PersonAugmentation>
	</xsl:template>
	<xsl:template match="j41:PersonFBIIdentification">
		<j:PersonFBIIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="." />
			</nc:IdentificationID>
		</j:PersonFBIIdentification>
	</xsl:template>
	<!-- MATCH -->
	<xsl:template match="nc20:IdentificationID">
		<nc:IdentificationID>
			<xsl:value-of select="." />
		</nc:IdentificationID>
	</xsl:template>
	<xsl:template match="nc20:Date">
		<nc:Date>
			<xsl:value-of select="." />
		</nc:Date>
	</xsl:template>
</xsl:stylesheet>