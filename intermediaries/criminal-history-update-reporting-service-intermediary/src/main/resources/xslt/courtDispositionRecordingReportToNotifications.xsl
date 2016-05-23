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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:xs="http://www.w3.org/2001/XMLSchema" 
	xmlns:b="http://docs.oasis-open.org/wsn/b-2" 
	xmlns:add="http://www.w3.org/2005/08/addressing" 
	xmlns:j="http://niem.gov/niem/domains/jxdm/4.1" 
	xmlns:nc="http://niem.gov/niem/niem-core/2.0" 
	xmlns:niem-xsd="http://niem.gov/niem/proxy/xsd/2.0" 
	xmlns:s="http://niem.gov/niem/structures/2.0" 
	xmlns:notification="http://ojbc.org/IEPD/Exchange/NotificationMessage/1.0"
	 xmlns:notificationExt="http://ojbc.org/IEPD/Extensions/Notification/1.0" 
	 xmlns:cdr-report-doc="http://ojbc.org/IEPD/Exchange/CourtDispositionRecordingReport/1.0" 
	 xmlns:cdr-report-ext="http://ojbc.org/IEPD/Extension/CourtDispositionRecordingReport/1.0" 
	 xmlns:j50="http://release.niem.gov/niem/domains/jxdm/5.0/" 
	 xmlns:nc30="http://release.niem.gov/niem/niem-core/3.0/" 
	 xmlns:niem-xs="http://release.niem.gov/niem/proxy/xsd/3.0/" 
	 xmlns:s30="http://release.niem.gov/niem/structures/3.0/" 
	 xmlns:cdu="http://ojbc.org/IEPD/Extensions/CourtDispositionUpdate/1.0" 
	 xmlns:me_disp_codes="http://ojbc.org/IEPD/Extensions/Maine/DispositionCodes/1.0" 
	 exclude-result-prefixes="s30 niem-xs nc30 j50 cdr-report-doc cdr-report-ext xs" version="2.0">
	<xsl:output indent="yes" method="xml"/>
	<xsl:param name="topic">{http://ojbc.org/wsn/topics}:person/CourtDispositionUpdate</xsl:param>
	<xsl:param name="systemId">{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB</xsl:param>
	<xsl:template match="/">
		<xsl:apply-templates select="cdr-report-doc:CourtDispositionRecordingReport"/>
	</xsl:template>
	<xsl:template match="cdr-report-doc:CourtDispositionRecordingReport">
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
					<add:Address>http://www.ojbc.org/CourtDispositionUpdateNotificationProducer</add:Address>
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
						<notificationExt:NotifyingCourtDispositionUpdate>
							<notificationExt:NotifyingActivityReportingSystemNameText>
								<xsl:value-of select="$systemId"/>
							</notificationExt:NotifyingActivityReportingSystemNameText>
							<notificationExt:NotifyingActivityReportingSystemURI>SystemURIHere</notificationExt:NotifyingActivityReportingSystemURI>
							<cdu:CourtDispositionUpdate>
								<xsl:attribute name="s:id"><xsl:value-of select="generate-id()"/></xsl:attribute>
								<xsl:apply-templates select="." mode="update"/>
							</cdu:CourtDispositionUpdate>
						</notificationExt:NotifyingCourtDispositionUpdate>
						<xsl:apply-templates select="j50:Subject/nc30:RoleOfPerson[@s30:ref]" mode="activityInvolvedPerson"/>
						<xsl:apply-templates select="nc30:Person" mode="person"/>
						<xsl:apply-templates select="nc30:Organization"/>
						<xsl:apply-templates select="." mode="person-charge"/>
						<xsl:apply-templates select="." mode="activity-charge"/>
						<xsl:apply-templates select="." mode="charge-disposition"/>
					</notification:NotificationMessage>
				</b:Message>
			</b:NotificationMessage>
		</b:Notify>
	</xsl:template>
	<xsl:template match="cdr-report-doc:CourtDispositionRecordingReport" mode="update">
		<xsl:apply-templates select="cdr-report-ext:CycleTrackingIdentification"/>
		<xsl:apply-templates select="j50:Charge"/>
		<xsl:apply-templates select="j50:Sentence"/>
		<xsl:apply-templates select="nc30:Disposition"/>
	</xsl:template>
	<xsl:template match="j50:Charge">
		<j:Charge>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id(.)"/></xsl:attribute>
			<xsl:apply-templates select="j50:ChargeStatute"/>
			<xsl:apply-templates select="j50:ChargeTrackingIdentification"/>
		</j:Charge>
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
	<xsl:template match="j50:Sentence">
		<cdu:Sentence>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id(.)"/></xsl:attribute>
			<xsl:apply-templates select="nc30:ActivityDate"/>
			<xsl:apply-templates select="j50:SentenceCondition"/>
			<xsl:apply-templates select="j50:SentenceGroupingText"/>
			<xsl:apply-templates select="j50:SentenceTerm"/>
			<xsl:apply-templates select="j50:SentenceIssuerEntity"/>
			<xsl:apply-templates select="cdr-report-ext:SentenceDocketIdentification"/>
			<xsl:apply-templates select="me_disp_codes:SentenceTermCategoryCode"/>
		</cdu:Sentence>
	</xsl:template>
	<xsl:template match="nc30:Disposition">
		<cdu:Disposition>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id(.)"/></xsl:attribute>
			<xsl:apply-templates select="nc30:DispositionDate"/>
			<xsl:apply-templates select="nc30:DispositionEntity"/>
			<xsl:apply-templates select="cdr-report-ext:DispositionIdentification"/>
			<xsl:apply-templates select="cdr-report-ext:DispositionDocketIdentification"/>
			<xsl:apply-templates select="me_disp_codes:CourtActionCode"/>
		</cdu:Disposition>
	</xsl:template>
	<xsl:template match="nc30:ActivityDate">
		<nc:ActivityDate>
			<xsl:apply-templates select="nc30:Date"/>
		</nc:ActivityDate>
	</xsl:template>
	<xsl:template match="nc30:Date">
		<nc:Date>
			<xsl:value-of select="."/>
		</nc:Date>
	</xsl:template>
	<xsl:template match="nc30:DispositionDate">
		<nc:DispositionDate>
			<xsl:apply-templates select="nc30:Date"/>
		</nc:DispositionDate>
	</xsl:template>
	<xsl:template match="j50:SentenceCondition">
		<j:SentenceCondition>
			<xsl:apply-templates select="nc30:ActivityDescriptionText"/>
			<xsl:apply-templates select="nc30:ConditionDisciplinaryAction"/>
		</j:SentenceCondition>
	</xsl:template>
	<xsl:template match="nc30:ActivityDescriptionText">
		<nc:ActivityDescriptionText>
			<xsl:value-of select="."/>
		</nc:ActivityDescriptionText>
	</xsl:template>
	<xsl:template match="nc30:ConditionDisciplinaryAction">
		<nc:ConditionDisciplinaryAction>
			<xsl:apply-templates select="nc30:DisciplinaryActionFee/nc30:ObligationDueAmount/nc30:Amount"/>
			<xsl:apply-templates select="nc30:DisciplinaryActionRestitution/nc30:ObligationDueAmount/nc30:Amount"/>
		</nc:ConditionDisciplinaryAction>
	</xsl:template>
	<xsl:template match="nc30:DisciplinaryActionFee/nc30:ObligationDueAmount/nc30:Amount">
		<nc:DisciplinaryActionFee>
			<nc:ObligationDueAmount>
				<xsl:value-of select="."/>
			</nc:ObligationDueAmount>
		</nc:DisciplinaryActionFee>
	</xsl:template>
	<xsl:template match="nc30:DisciplinaryActionRestitution/nc30:ObligationDueAmount/nc30:Amount">
		<nc:DisciplinaryActionRestitution>
			<nc:ObligationDueAmount>
				<xsl:value-of select="."/>
			</nc:ObligationDueAmount>
		</nc:DisciplinaryActionRestitution>
	</xsl:template>
	<xsl:template match="j50:SentenceGroupingText">
		<j:SentenceGroupingText>
			<xsl:value-of select="."/>
		</j:SentenceGroupingText>
	</xsl:template>
	<xsl:template match="cdr-report-ext:CycleTrackingIdentification">
		<cdu:CycleTrackingIdentification>
			<xsl:apply-templates select="nc30:IdentificationID"/>
		</cdu:CycleTrackingIdentification>
	</xsl:template>
	<xsl:template match="j50:SentenceIssuerEntity">
		<cdu:SentenceCourtOrganizationReference>
			<xsl:variable name="orgID1" select="./nc30:EntityOrganization/@s30:ref"/>
			<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(../../nc30:Organization[@s30:id=$orgID1])"/></xsl:attribute>
		</cdu:SentenceCourtOrganizationReference>
	</xsl:template>
	<xsl:template match="nc30:DispositionEntity">
		<cdu:DispositionCourtOrganizationReference>
			<xsl:variable name="orgID1" select="./nc30:EntityOrganization/@s30:ref"/>
			<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(../../nc30:Organization[@s30:id=$orgID1])"/></xsl:attribute>
		</cdu:DispositionCourtOrganizationReference>
	</xsl:template>
	<xsl:template match="cdr-report-ext:DispositionIdentification">
		<cdu:DispositionIdentification>
			<xsl:apply-templates select="nc30:IdentificationID"/>
		</cdu:DispositionIdentification>
	</xsl:template>
	<xsl:template match="cdr-report-ext:DispositionDocketIdentification">
		<cdu:DispositionDocketIdentification>
			<xsl:apply-templates select="nc30:IdentificationID"/>
		</cdu:DispositionDocketIdentification>
	</xsl:template>
	<xsl:template match="j50:SentenceTerm">
		<j:SentenceTerm>
			<xsl:apply-templates select="j50:TermDuration"/>
			<xsl:apply-templates select="j50:TermLifeIndicator"/>
		</j:SentenceTerm>
	</xsl:template>
	<xsl:template match="j50:TermDuration">
		<j:TermDuration>
			<xsl:value-of select="."/>
		</j:TermDuration>
	</xsl:template>
	<xsl:template match="j50:TermLifeIndicator">
		<j:TermLifeIndicator>
			<xsl:value-of select="."/>
		</j:TermLifeIndicator>
	</xsl:template>
	<xsl:template match="cdr-report-ext:SentenceDocketIdentification">
		<cdu:SentenceDocketIdentification>
			<xsl:apply-templates select="nc30:IdentificationID"/>
		</cdu:SentenceDocketIdentification>
	</xsl:template>
	<xsl:template match="me_disp_codes:SentenceTermCategoryCode">
		<me_disp_codes:SentenceTermCategoryCode>
			<xsl:value-of select="."/>
		</me_disp_codes:SentenceTermCategoryCode>
	</xsl:template>
	<xsl:template match="me_disp_codes:CourtActionCode">
		<me_disp_codes:CourtActionCode>
			<xsl:value-of select="."/>
		</me_disp_codes:CourtActionCode>
	</xsl:template>
	<xsl:template match="cdr-report-doc:CourtDispositionRecordingReport" mode="person-charge">
		<j:PersonChargeAssociation>
			<nc:PersonReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(/cdr-report-doc:CourtDispositionRecordingReport/nc30:Person)"/></xsl:attribute>
			</nc:PersonReference>
			<j:ChargeReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(/cdr-report-doc:CourtDispositionRecordingReport/j50:Charge)"/></xsl:attribute>
			</j:ChargeReference>
		</j:PersonChargeAssociation>
	</xsl:template>
	<xsl:template match="cdr-report-doc:CourtDispositionRecordingReport" mode="activity-charge">
		<j:ActivityChargeAssociation>
			<nc:ActivityReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(/cdr-report-doc:CourtDispositionRecordingReport/j50:Sentence)"/></xsl:attribute>
			</nc:ActivityReference>
			<j:ChargeReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(/cdr-report-doc:CourtDispositionRecordingReport/j50:Charge)"/></xsl:attribute>
			</j:ChargeReference>
		</j:ActivityChargeAssociation>
	</xsl:template>
	<xsl:template match="cdr-report-doc:CourtDispositionRecordingReport" mode="charge-disposition">
		<notification:ChargeDispositionAssociation>
			<j:ChargeReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(/cdr-report-doc:CourtDispositionRecordingReport/j50:Charge)"/></xsl:attribute>
			</j:ChargeReference>
			<j:DispositionReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(/cdr-report-doc:CourtDispositionRecordingReport/nc30:Disposition)"/></xsl:attribute>
			</j:DispositionReference>
		</notification:ChargeDispositionAssociation>
	</xsl:template>
    <xsl:template match="nc30:RoleOfPerson" mode="activityInvolvedPerson">
    	<xsl:variable name="personID" select="@s30:ref"/>
    	<nc:ActivityInvolvedPersonAssociation>
			<nc:ActivityReference>
				<xsl:attribute name="s:ref">
					<xsl:value-of select="generate-id(/cdr-report-doc:CourtDispositionRecordingReport)"/>
				</xsl:attribute>
			</nc:ActivityReference>
			<nc:PersonReference>
				<xsl:attribute name="s:ref">
					<xsl:value-of select="generate-id(/cdr-report-doc:CourtDispositionRecordingReport/nc30:Person[@s30:id=$personID])"/>
				</xsl:attribute>
			</nc:PersonReference>
		</nc:ActivityInvolvedPersonAssociation>
    </xsl:template>	
	<xsl:template match="nc30:Person" mode="person">
		<xsl:variable name="personID" select="@s30:ref"/>
		<j:Person>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id()"/></xsl:attribute>
			<xsl:apply-templates select="nc30:PersonBirthDate"/>
			<xsl:apply-templates select="nc30:PersonName"/>
			<xsl:apply-templates select="j50:PersonAugmentation/j50:PersonStateFingerprintIdentification/nc30:IdentificationID" mode="sid"/>
		</j:Person>
	</xsl:template>
	<xsl:template match="nc30:PersonBirthDate">
		<nc:PersonBirthDate>
			<xsl:apply-templates select="nc30:Date"/>
		</nc:PersonBirthDate>
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
	<xsl:template match="j50:PersonAugmentation/j50:PersonStateFingerprintIdentification/nc30:IdentificationID" mode="sid">
		<j:PersonAugmentation>
			<j:PersonStateFingerprintIdentification>
				<nc:IdentificationID><xsl:apply-templates select="."/></nc:IdentificationID>
			</j:PersonStateFingerprintIdentification>
		</j:PersonAugmentation>
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
		<nc:IdentificationID>
			<xsl:value-of select="."/>
		</nc:IdentificationID>
	</xsl:template>
</xsl:stylesheet>
