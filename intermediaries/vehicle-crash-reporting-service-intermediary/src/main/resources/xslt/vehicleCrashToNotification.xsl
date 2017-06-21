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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:b="http://docs.oasis-open.org/wsn/b-2"
	xmlns:add="http://www.w3.org/2005/08/addressing" xmlns:notification="http://ojbc.org/IEPD/Exchange/NotificationMessage/1.0"
	xmlns:notificationExt="http://ojbc.org/IEPD/Extensions/Notification/1.0"
	xmlns:vc="http://ojbc.org/IEPD/Extensions/VehicleCrash/1.0" xmlns:j="http://niem.gov/niem/domains/jxdm/4.1"
	xmlns:nc="http://niem.gov/niem/niem-core/2.0" xmlns:niem-xsd="http://niem.gov/niem/proxy/xsd/2.0"
	xmlns:s="http://niem.gov/niem/structures/2.0" xmlns:vcr-doc="http://ojbc.org/IEPD/Exchange/VehicleCrashReporting/1.0"
	xmlns:me-crash-codes="http://ojbc.org/IEPD/Extensions/Maine/VehicleCrashCodes/1.0"
	xmlns:j51="http://release.niem.gov/niem/domains/jxdm/5.1/" xmlns:nc30="http://release.niem.gov/niem/niem-core/3.0/"
	xmlns:s30="http://release.niem.gov/niem/structures/3.0/" xmlns:niem-xs="http://release.niem.gov/niem/proxy/xsd/3.0/"
	exclude-result-prefixes="s30 niem-xs nc30 j51 vcr-doc xs"
	version="2.0">
	<xsl:output indent="yes" method="xml" />
	<xsl:param name="topic">
		{http://ojbc.org/wsn/topics}:person/CourtDispositionUpdate
	</xsl:param>
	<xsl:param name="systemId">
		{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB
	</xsl:param>
	<xsl:template match="/">
	<wrapper>
		<xsl:for-each
			select="vcr-doc:VehicleCrashReport/j51:Crash/j51:CrashVehicle/j51:CrashVehicleOccupant/nc30:RoleOfPerson/@s30:ref">
			<xsl:variable name="personID" select="." />
			
				<xsl:apply-templates select="/vcr-doc:VehicleCrashReport"
					mode="wrapper">
					<xsl:with-param name="personID" select="$personID" />
				</xsl:apply-templates>
		</xsl:for-each>
	</wrapper>
	</xsl:template>
	<xsl:template match="vcr-doc:VehicleCrashReport" mode="wrapper">
		<xsl:param name="personID" />
		<b:Notify>
			<b:NotificationMessage>
				<b:SubscriptionReference>
					<add:Address>http://www.ojbc.org/SubscriptionManager</add:Address>
					<!--Optional: -->
					<add:ReferenceParameters>
						<!--You may enter ANY elements at this point -->
					</add:ReferenceParameters>
					<!--Optional: -->
					<add:Metadata>
						<!--You may enter ANY elements at this point -->
					</add:Metadata>
					<!--You may enter ANY elements at this point -->
				</b:SubscriptionReference>
				<!--Optional: -->
				<b:Topic
					Dialect="http://docs.oasis-open.org/wsn/t-1/TopicExpression/Concrete">
					<xsl:value-of select="$topic" />
				</b:Topic>
				<!--Optional: -->
				<b:ProducerReference>
					<add:Address>http://www.ojbc.org/CourtDispositionUpdateNotificationProducer
					</add:Address>
					<!--Optional: -->
					<add:ReferenceParameters>
						<!--You may enter ANY elements at this point -->
					</add:ReferenceParameters>
					<!--Optional: -->
					<add:Metadata>
						<!--You may enter ANY elements at this point -->
					</add:Metadata>
					<!--You may enter ANY elements at this point -->
				</b:ProducerReference>
				<b:Message>
					<notification:NotificationMessage>
						<notificationExt:NotifyingVehicleCrash>
							<notificationExt:NotifyingActivityReportingSystemNameText>
								<xsl:value-of select="$systemId" />
							</notificationExt:NotifyingActivityReportingSystemNameText>
							<notificationExt:NotifyingActivityReportingSystemURI>SystemURIHere
							</notificationExt:NotifyingActivityReportingSystemURI>
							<vc:VehicleCrash>
								<xsl:attribute name="s:id"><xsl:value-of
									select="generate-id()" /></xsl:attribute>
								<xsl:apply-templates select="." mode="crash">
									<xsl:with-param name="personID" select="$personID" />
								</xsl:apply-templates>
							</vc:VehicleCrash>
						</notificationExt:NotifyingVehicleCrash>
						<xsl:apply-templates
							select="j51:Crash/j51:CrashVehicle/j51:CrashVehicleOccupant/nc30:RoleOfPerson[@s30:ref=$personID]"
							mode="activityInvolvedPerson" />
						<xsl:apply-templates select="nc30:Person[@s30:id=$personID]"
							mode="person">
							<xsl:with-param name="personID" select="$personID" />
						</xsl:apply-templates>
					</notification:NotificationMessage>
				</b:Message>
			</b:NotificationMessage>
		</b:Notify>
	</xsl:template>
	<xsl:template match="vcr-doc:VehicleCrashReport" mode="crash">
		<xsl:param name="personID" />
		<xsl:apply-templates select="j51:Crash/nc30:ActivityDate" />
		<xsl:apply-templates
			select="j51:Crash/j51:CrashVehicle/j51:CrashVehicleOccupant/nc30:RoleOfPerson[@s30:ref=$personID]"
			mode="occupant">
			<xsl:with-param name="personID" select="$personID" />
		</xsl:apply-templates>
		<xsl:apply-templates
			select="j51:Crash/nc30:Location/nc30:AddressHighway/nc30:HighwayFullText" />
		<xsl:apply-templates
			select="j51:Crash/j51:CrashInformationSource/me-crash-codes:ReportingAgencyCodeText" />
		<xsl:apply-templates
			select="j51:Crash/j51:IncidentAugmentation/j51:IncidentReportingOfficial/nc30:RoleOfPerson"
			mode="official" />
		<xsl:apply-templates select="j51:Citation" />
	</xsl:template>
	<xsl:template match="nc30:ActivityDate">
		<nc:ActivityDate>
			<xsl:apply-templates select="nc30:DateTime" />
		</nc:ActivityDate>
	</xsl:template>
	<xsl:template match="nc30:Date">
		<nc:Date>
			<xsl:value-of select="." />
		</nc:Date>
	</xsl:template>
	<xsl:template match="nc30:DateTime">
		<nc:DateTime>
			<xsl:value-of select="." />
		</nc:DateTime>
	</xsl:template>
	<xsl:template match="nc30:HighwayFullText">
		<nc:Location>
			<nc:LocationHighway>
				<nc:HighwayFullText>
					<xsl:value-of select="." />
				</nc:HighwayFullText>
			</nc:LocationHighway>
		</nc:Location>
	</xsl:template>
	<xsl:template match="j51:IncidentReportingOfficial/nc30:RoleOfPerson"
		mode="official">
		<xsl:variable name="officialID" select="@s30:ref" />
		<j:IncidentAugmentation>
			<j:IncidentReportingOfficial>
				<nc:RoleOfPersonReference>
					<xsl:attribute name="s:id"><xsl:value-of
						select="$officialID" /></xsl:attribute>
				</nc:RoleOfPersonReference>
			</j:IncidentReportingOfficial>
		</j:IncidentAugmentation>
	</xsl:template>
	<xsl:template match="j51:Citation">
		<j:Citation>
			<xsl:apply-templates select="nc30:ActivityIdentification" />
		</j:Citation>
	</xsl:template>
	<xsl:template match="nc30:ActivityIdentification">
		<nc:ActivityIdentification>
			<xsl:apply-templates select="nc30:IdentificationID" />
		</nc:ActivityIdentification>
	</xsl:template>
	<xsl:template match="nc30:IdentificationID">
		<nc:IdentificationID>
			<xsl:value-of select="." />
		</nc:IdentificationID>
	</xsl:template>
	<xsl:template match="nc30:RoleOfPerson" mode="activityInvolvedPerson">
		<xsl:variable name="personID" select="@s30:ref" />
		<nc:ActivityInvolvedPersonAssociation>
			<nc:ActivityReference>
				<xsl:attribute name="s:ref"> <xsl:value-of
					select="generate-id(/vcr-doc:VehicleCrashReport)" /> 
		</xsl:attribute>
			</nc:ActivityReference>
			<nc:PersonReference>
				<xsl:attribute name="s:ref"> <xsl:value-of
					select="$personID" /> 
		</xsl:attribute>
			</nc:PersonReference>
			<nc:PersonActivityInvolvementText>
				<xsl:value-of
					select="/vcr-doc:VehicleCrashReport/nc30:Person[@s30:id=$personID]/me-crash-codes:PersonCategoryCodeText" />
			</nc:PersonActivityInvolvementText>
		</nc:ActivityInvolvedPersonAssociation>
	</xsl:template>
	<xsl:template match="me-crash-codes:ReportingAgencyCodeText">
		<me-crash-codes:ReportingAgencyCodeText>
			<xsl:value-of select="." />
		</me-crash-codes:ReportingAgencyCodeText>
	</xsl:template>
	<xsl:template match="nc30:RoleOfPerson" mode="occupant">
		<xsl:param name="personID" />
		<j:CrashVehicle>
			<j:CrashVehicleOccupant>
				<nc:RoleOfPersonReference>
					<xsl:attribute name="s:ref"><xsl:value-of
						select="$personID" /></xsl:attribute>
				</nc:RoleOfPersonReference>
			</j:CrashVehicleOccupant>
		</j:CrashVehicle>
	</xsl:template>
	<xsl:template match="nc30:Person" mode="person">
		<xsl:param name="personID" />
		<j:Person>
			<xsl:attribute name="s:id"><xsl:value-of select="$personID" /></xsl:attribute>
			<xsl:apply-templates select="nc30:PersonBirthDate" />
			<xsl:apply-templates select="nc30:PersonName" />
		</j:Person>
	</xsl:template>
	<xsl:template match="nc30:PersonBirthDate">
		<nc:PersonBirthDate>
			<xsl:apply-templates select="nc30:Date" />
		</nc:PersonBirthDate>
	</xsl:template>
	<xsl:template match="nc30:PersonName">
		<nc:PersonName>
			<xsl:apply-templates select="nc30:PersonFullName" />
			<xsl:apply-templates select="nc30:PersonGivenName" />
			<xsl:apply-templates select="nc30:PersonMiddleName" />
			<xsl:apply-templates select="nc30:PersonSurName" />
		</nc:PersonName>
	</xsl:template>
	<xsl:template match="nc30:PersonGivenName">
		<nc:PersonGivenName>
			<xsl:value-of select="." />
		</nc:PersonGivenName>
	</xsl:template>
	<xsl:template match="nc30:PersonMiddleName">
		<nc:PersonMiddleName>
			<xsl:value-of select="." />
		</nc:PersonMiddleName>
	</xsl:template>
	<xsl:template match="nc30:PersonSurName">
		<nc:PersonSurName>
			<xsl:value-of select="." />
		</nc:PersonSurName>
	</xsl:template>
</xsl:stylesheet>