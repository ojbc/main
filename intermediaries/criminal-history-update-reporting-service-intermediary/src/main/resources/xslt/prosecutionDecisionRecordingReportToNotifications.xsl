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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:b="http://docs.oasis-open.org/wsn/b-2" xmlns:add="http://www.w3.org/2005/08/addressing" xmlns:j="http://niem.gov/niem/domains/jxdm/4.1" xmlns:nc="http://niem.gov/niem/niem-core/2.0" xmlns:niem-xsd="http://niem.gov/niem/proxy/xsd/2.0" xmlns:s="http://niem.gov/niem/structures/2.0" xmlns:notification="http://ojbc.org/IEPD/Exchange/NotificationMessage/1.0" xmlns:notificationExt="http://ojbc.org/IEPD/Extensions/Notification/1.0" xmlns:pdr-report-doc="http://ojbc.org/IEPD/Exchange/ProsecutionDecisionRecordingReport/1.0" xmlns:pdr-report-ext="http://ojbc.org/IEPD/Extension/ProsecutionDecisionRecordingReport/1.0" xmlns:j50="http://release.niem.gov/niem/domains/jxdm/5.0/" xmlns:nc30="http://release.niem.gov/niem/niem-core/3.0/" xmlns:niem-xs="http://release.niem.gov/niem/proxy/xsd/3.0/" xmlns:s30="http://release.niem.gov/niem/structures/3.0/" xmlns:pdu="http://ojbc.org/IEPD/Extensions/ProsecutionDecisionUpdate/1.0" xmlns:nc-3.0.1="http://publication.niem.gov/niem/niem-core/3.0/1/" exclude-result-prefixes="s30 niem-xs nc30 j50 pdr-report-doc pdr-report-ext xs" version="2.0">
	<xsl:output indent="yes" method="xml"/>
	<xsl:param name="topic">{http://ojbc.org/wsn/topics}:person/ProsecutionDecisionUpdate</xsl:param>
	<xsl:param name="systemId">{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB</xsl:param>
	<xsl:template match="/">
		<xsl:apply-templates select="pdr-report-doc:ProsecutionDecisionRecordingReport"/>
	</xsl:template>
	<xsl:template match="pdr-report-doc:ProsecutionDecisionRecordingReport">
		<b:Notify>
			<b:NotificationMessage>
				<b:SubscriptionReference>
					<add:Address>http://www.ojbc.org/SubscriptionManager</add:Address>
					<!--Optional:-->
					<add:ReferenceParameters>
						<!--You may enter ANY elements at this point-->
					</add:ReferenceParameters>
					<!--Optional:-->
					<add:Metadata>
						<!--You may enter ANY elements at this point-->
					</add:Metadata>
					<!--You may enter ANY elements at this point-->
				</b:SubscriptionReference>
				<!--Optional:-->
				<b:Topic Dialect="http://docs.oasis-open.org/wsn/t-1/TopicExpression/Concrete">
					<xsl:value-of select="$topic"/>
				</b:Topic>
				<!--Optional:-->
				<b:ProducerReference>
					<add:Address>http://www.ojbc.org/ProsecutionDecisionUpdateNotificationProducer</add:Address>
					<!--Optional:-->
					<add:ReferenceParameters>
						<!--You may enter ANY elements at this point-->
					</add:ReferenceParameters>
					<!--Optional:-->
					<add:Metadata>
						<!--You may enter ANY elements at this point-->
					</add:Metadata>
					<!--You may enter ANY elements at this point-->
				</b:ProducerReference>
				<b:Message>
					<notification:NotificationMessage>
						<notificationExt:NotifyingProsecutionDecisionUpdate>
							<xsl:attribute name="s:id"><xsl:value-of select="generate-id()"/></xsl:attribute>
							<notificationExt:NotifyingActivityReportingSystemNameText><xsl:value-of select="$systemId"/></notificationExt:NotifyingActivityReportingSystemNameText>
							<notificationExt:NotifyingActivityReportingSystemURI>SystemURIHere</notificationExt:NotifyingActivityReportingSystemURI>
							<pdu:ProsecutionDecisionUpdate>
								<xsl:apply-templates select="." mode="update"/>
							</pdu:ProsecutionDecisionUpdate>
						</notificationExt:NotifyingProsecutionDecisionUpdate>
						<xsl:apply-templates select="j50:Subject/nc30:RoleOfPerson[@s30:ref]" mode="activityInvolvedPerson"/>
						<xsl:apply-templates select="nc30:Person" mode="person"/>
						<xsl:apply-templates select="nc30:Organization"/>
					</notification:NotificationMessage>
				</b:Message>
			</b:NotificationMessage>
		</b:Notify>
	</xsl:template>
	<xsl:template match="pdr-report-doc:ProsecutionDecisionRecordingReport" mode="update">
		<xsl:apply-templates select="pdr-report-ext:CycleTrackingIdentification"/>
		<xsl:apply-templates select="j50:CaseProsecutionAttorney"/>
		<xsl:apply-templates select="j50:Charge"/>
		<xsl:apply-templates select="nc30:Location"/>
		<xsl:apply-templates select="nc30:Location[@s30:ref]" mode="residence"/>
	</xsl:template>
	<xsl:template match="j50:CaseProsecutionAttorney">
		<pdu:ProsecutionAttorneyOrganizationReference>
			<xsl:variable name="prosecutionID" select="@s30:ref"/>
			<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(/pdr-report-doc:ProsecutionDecisionRecordingReport/nc30:Organization)"/></xsl:attribute>
		</pdu:ProsecutionAttorneyOrganizationReference>
	</xsl:template>
	<xsl:template match="j50:Charge">
		<pdu:Charge>
			<xsl:apply-templates select="j50:ChargeStatute"/>
			<xsl:apply-templates select="j50:ChargeTrackingIdentification"/>
			<xsl:apply-templates select="pdr-report-ext:ChargeToCourtIndicator"/>
			<xsl:apply-templates select="pdr-report-ext:EndProsecutorChargeCycleIndicator"/>
		</pdu:Charge>
	</xsl:template>
	<xsl:template match="j50:ChargeTrackingIdentification">
		<j:ChargeTrackingIdentification>
			<xsl:apply-templates select="nc30:IdentificationID"/>
		</j:ChargeTrackingIdentification>
	</xsl:template>
	<xsl:template match="j50:ChargeStatute">
		<j:ChargeStatute>
			<xsl:apply-templates select="j50:StatuteCodeIdentification"/>
			<xsl:apply-templates select="j50:StatuteText"/>
		</j:ChargeStatute>
	</xsl:template>
	<xsl:template match="j50:StatuteCodeIdentification">
		<j:StatuteCodeIdentification>
			<xsl:apply-templates select="nc30:IdentificationID"/>
		</j:StatuteCodeIdentification>
	</xsl:template>
	<xsl:template match="pdr-report-ext:ChargeToCourtIndicator">
		<pdu:ChargeToCourtIndicator>
			<xsl:value-of select="."/>
		</pdu:ChargeToCourtIndicator>
	</xsl:template>
	<xsl:template match="pdr-report-ext:EndProsecutorChargeCycleIndicator">
		<pdu:EndProsecutorChargeCycleIndicator>
			<xsl:value-of select="."/>
		</pdu:EndProsecutorChargeCycleIndicator>
	</xsl:template>
	<xsl:template match="pdr-report-ext:CycleTrackingIdentification">
		<pdu:CycleTrackingIdentification>
			<xsl:apply-templates select="nc30:IdentificationID"/>
		</pdu:CycleTrackingIdentification>
	</xsl:template>
	<xsl:template match="j50:SentenceIssuerEntity">
		<j:SentenceIssuerEntity>
			<nc30:EntityOrganization>
				<xsl:variable name="orgID" select="./nc30:EntityOrganization/@s30:ref"/>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(nc30:Organization[@s30:id=$orgID])"/></xsl:attribute>
			</nc30:EntityOrganization>
		</j:SentenceIssuerEntity>
	</xsl:template>
	<xsl:template match="j50:SentenceTerm">
		<j:SentenceTerm>
			<j:TermDuration>
				<xsl:value-of select="."/>
			</j:TermDuration>
		</j:SentenceTerm>
	</xsl:template>
	<xsl:template match="nc30:RoleOfPerson" mode="activityInvolvedPerson">
		<xsl:variable name="personID" select="@s30:ref"/>
		<nc:ActivityInvolvedPersonAssociation>
			<nc:ActivityReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(/pdr-report-doc:ProsecutionDecisionRecordingReport)"/></xsl:attribute>
			</nc:ActivityReference>
			<nc:PersonReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(/pdr-report-doc:ProsecutionDecisionRecordingReport/nc30:Person[@s30:id=$personID])"/></xsl:attribute>
			</nc:PersonReference>
		</nc:ActivityInvolvedPersonAssociation>
	</xsl:template>
	<xsl:template match="nc30:Location" mode="residence">
		<xsl:variable name="locationID" select="@s30:ref"/>
		<nc:PersonResidenceAssociation>
			<nc:PersonReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(/pdr-report-doc:ProsecutionDecisionRecordingReport/nc30:Person)"/></xsl:attribute>
			</nc:PersonReference>
			<nc:LocationReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(/pdr-report-doc:ProsecutionDecisionRecordingReport/nc30:Location[@s30:id=$locationID])"/></xsl:attribute>
			</nc:LocationReference>
		</nc:PersonResidenceAssociation>
	</xsl:template>
	<xsl:template match="nc30:Person" mode="person">
		<xsl:variable name="personID" select="@s30:ref"/>
		<j:Person>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id()"/></xsl:attribute>
			<xsl:apply-templates select="nc30:PersonBirthDate"/>
			<xsl:apply-templates select="j50:PersonEyeColorCode"/>
			<xsl:apply-templates select="j50:PersonHairColorCode"/>
			<xsl:apply-templates select="nc30:PersonHeightMeasure"/>
			<xsl:apply-templates select="nc30:PersonName"/>
			<xsl:apply-templates select="j50:PersonRaceCode"/>
			<xsl:apply-templates select="j50:PersonSexCode"/>
			<xsl:apply-templates select="nc30:PersonSSNIdentification"/>
			<xsl:apply-templates select="nc30:PersonWeighttMeasure"/>
			<j:PersonAugmentation>
				<xsl:apply-templates select="j50:PersonAugmentation/j50:DriverLicense/j50:DriverLicenseCardIdentification" mode="dl"/>
				<xsl:apply-templates select="j50:PersonAugmentation/j50:PersonStateFingerprintIdentification"/>
			</j:PersonAugmentation>	
		</j:Person>
	</xsl:template>
	<xsl:template match="j50:PersonAugmentation/j50:DriverLicense/j50:DriverLicenseCardIdentification" mode="dl">
		<nc:DriverLicense>
			<nc:DriverLicenseIdentification>
				<xsl:apply-templates select="nc30:IdentificationID"/>
			</nc:DriverLicenseIdentification>
		</nc:DriverLicense>
	</xsl:template>
	
	<xsl:template match="j50:PersonAugmentation/j50:PersonStateFingerprintIdentification">
		<j:PersonStateFingerprintIdentification>
			<xsl:apply-templates select="nc30:IdentificationID"/>
		</j:PersonStateFingerprintIdentification>
	</xsl:template>	
	
	<xsl:template match="nc30:PersonBirthDate">
		<nc:PersonBirthDate>
			<xsl:apply-templates select="nc30:Date"/>
		</nc:PersonBirthDate>
	</xsl:template>
	<xsl:template match="j50:PersonEyeColorCode">
		<nc:PersonEyeColorCode>
			<xsl:value-of select="."/>
		</nc:PersonEyeColorCode>
	</xsl:template>
	<xsl:template match="j50:PersonHairColorCode">
		<nc:PersonHairColorCode>
			<xsl:value-of select="."/>
		</nc:PersonHairColorCode>
	</xsl:template>
	<xsl:template match="nc30:PersonHeightMeasure">
		<nc:PersonHeightMeasure>
			<nc:MeasureText>
				<xsl:value-of select="nc30:MeasureValueText"/>
			</nc:MeasureText>
			<nc:MeasureUnitText>
				<xsl:value-of select="nc30:MeasureUnitText"/>
			</nc:MeasureUnitText>
		</nc:PersonHeightMeasure>
	</xsl:template>
	<xsl:template match="nc30:PersonName">
		<nc:PersonName>
			<xsl:apply-templates select="nc30:PersonGivenName"/>
			<xsl:apply-templates select="nc30:PersonMiddleName"/>
			<xsl:apply-templates select="nc30:PersonSurName"/>
		</nc:PersonName>
	</xsl:template>
	<xsl:template match="nc30:PersonGivenName">
		<nc:PersonGivenName>
			<xsl:value-of select="."/>
		</nc:PersonGivenName>
	</xsl:template>
	<xsl:template match="nc30:PersonMiddleName">
		<nc:PersonMiddleName>
			<xsl:value-of select="."/>
		</nc:PersonMiddleName>
	</xsl:template>
	<xsl:template match="nc30:PersonSurName">
		<nc:PersonSurName>
			<xsl:value-of select="."/>
		</nc:PersonSurName>
	</xsl:template>
	<xsl:template match="j50:PersonRaceCode">
		<nc:PersonRaceCode>
			<xsl:value-of select="."/>
		</nc:PersonRaceCode>
	</xsl:template>
	<xsl:template match="j50:PersonSexCode">
		<nc:PersonSexCode>
			<xsl:value-of select="."/>
		</nc:PersonSexCode>
	</xsl:template>
	<xsl:template match="nc30:PersonSSNIdentification">
		<nc:PersonSSNIdentification>
			<xsl:apply-templates select="nc30:IdentificationID"/>
		</nc:PersonSSNIdentification>
	</xsl:template>
	<xsl:template match="nc30:PersonWeightMeasure">
		<nc:PersonWeightMeasure>
			<nc:MeasureValueText>
				<xsl:value-of select="nc30:MeasureValueText"/>
			</nc:MeasureValueText>
			<nc:MeasureUnitText>
				<xsl:value-of select="nc30:MeasureUnitText"/>
			</nc:MeasureUnitText>
		</nc:PersonWeightMeasure>
	</xsl:template>
	<xsl:template match="nc30:Date">
		<nc:Date>
			<xsl:value-of select="."/>
		</nc:Date>
	</xsl:template>
	<xsl:template match="nc30:Organization">
		<j:Organization>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id()"/></xsl:attribute>
			<xsl:apply-templates select="j50:OrganizationAugmentation"/>
		</j:Organization>
	</xsl:template>
	<xsl:template match="j50:OrganizationAugmentation">
		<j:OrganizationAugmentation>
			<xsl:apply-templates select="j50:OrganizationORIIdentification"/>
		</j:OrganizationAugmentation>
	</xsl:template>
	<xsl:template match="j50:OrganizationORIIdentification">
		<j:OrganizationORIIdentification>
			<xsl:apply-templates select="nc30:IdentificationID"/>
		</j:OrganizationORIIdentification>
	</xsl:template>
	<xsl:template match="nc30:IdentificationID">
		<nc:IdentificationID><xsl:value-of select="."/></nc:IdentificationID>
	</xsl:template>
	<xsl:template match="nc30:Location">
		<xsl:variable name="LocationID" select="@s30:ref"/>
		<nc:Location>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id()"/></xsl:attribute>
			<nc:LocationAddress>
				<nc:StructuredAddress>
					<xsl:apply-templates select="nc30:Address"/>
				</nc:StructuredAddress>
			</nc:LocationAddress>
		</nc:Location>
	</xsl:template>
	<xsl:template match="nc30:Address">
			<xsl:apply-templates select="nc30:LocationStreet"/>
			<xsl:apply-templates select="nc30:LocationCityName"/>
			<xsl:apply-templates select="nc-3.0.1:LocationStateUSPostalServiceCode"/>
			<xsl:apply-templates select="nc30:LocationPostalExtensionCode"/>
	</xsl:template>
	<xsl:template match="nc30:LocationStreet">
		<nc:LocationStreet>
			<nc:StreetFullText>
				<xsl:value-of select="."/>
			</nc:StreetFullText>
		</nc:LocationStreet>
	</xsl:template>
	<xsl:template match="nc30:LocationCityName">
		<nc:LocationCityName>
			<xsl:value-of select="."/>
		</nc:LocationCityName>
	</xsl:template>
	<xsl:template match="nc-3.0.1:LocationStateUSPostalServiceCode">
		<nc:LocationStateUSPostalServiceCode>
			<xsl:value-of select="."/>
		</nc:LocationStateUSPostalServiceCode>
	</xsl:template>
	<xsl:template match="nc30:LocationPostalExtensionCode">
		<nc:LocationPostalCode>
			<xsl:value-of select="."/>
		</nc:LocationPostalCode>
	</xsl:template>
</xsl:stylesheet>
