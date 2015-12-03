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
<!-- create arrestee ID then make sure only her residence appears -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:b="http://docs.oasis-open.org/wsn/b-2" 
	xmlns:add="http://www.w3.org/2005/08/addressing" xmlns:j="http://niem.gov/niem/domains/jxdm/4.1" 
	xmlns:j40="http://niem.gov/niem/domains/jxdm/4.0" xmlns:nc="http://niem.gov/niem/niem-core/2.0" 
	xmlns:niem-xsd="http://niem.gov/niem/proxy/xsd/2.0" xmlns:s="http://niem.gov/niem/structures/2.0" 
	xmlns:notification="http://ojbc.org/IEPD/Exchange/NotificationMessage/1.0" 
	xmlns:notificationExt="http://ojbc.org/IEPD/Extensions/Notification/1.0" xmlns:ar="http://ojbc.org/IEPD/Exchange/ArrestReport/1.0" 
	xmlns:lexs="http://usdoj.gov/leisp/lexs/3.1" xmlns:lexspd="http://usdoj.gov/leisp/lexs/publishdiscover/3.1" 
	xmlns:lexsdigest="http://usdoj.gov/leisp/lexs/digest/3.1" xmlns:ndexia="http://fbi.gov/cjis/N-DEx/IncidentArrest/2.1" 
	xmlns:lexslib="http://usdoj.gov/leisp/lexs/library/3.1" xmlns:ojbc="http://ojbc.org/IEPD/Extensions/ArrestReportStructuredPayload/1.0" xmlns:xmime="http://www.w3.org/2005/05/xmlmime" xmlns:xop="http://www.w3.org/2004/08/xop/include" xmlns:oar="http://ojbc.org/IEPD/Extensions/ArrestReportStructuredPayload/1.0" exclude-result-prefixes="xs ar lexs lexspd lexsdigest j40 oar" version="2.0">
	<xsl:output indent="yes" method="xml"/>
	<xsl:strip-space elements="*"/>
	<xsl:variable name="lexsDataItemPackage" select="/*/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage"/>
	<xsl:variable name="lexsDigest" select="$lexsDataItemPackage/lexs:Digest"/>
	<xsl:variable name="lexsAssociations" select="$lexsDigest/lexsdigest:Associations"/>
	<xsl:variable name="lexsDataOwnerContactTelephone" select="$lexsDataItemPackage/lexs:PackageMetadata/lexs:DataOwnerMetadata/lexs:DataOwnerContact/nc:ContactTelephoneNumber/nc:NANPTelephoneNumber"/>
	<xsl:variable name="incidentDateTime">
		<xsl:choose>
			<xsl:when test="$lexsDigest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText = 'Incident']/nc:ActivityDateRange/nc:StartDate/nc:DateTime">
				<xsl:value-of select="$lexsDigest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText = 'Incident']/nc:ActivityDateRange/nc:StartDate/nc:DateTime"/>
			</xsl:when>
			<xsl:when test="$lexsDigest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText = 'Incident']/nc:ActivityDate/nc:DateTime">
				<xsl:value-of select="$lexsDigest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText = 'Incident']/nc:ActivityDate/nc:DateTime"/>
			</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="incidentDate">
		<xsl:choose>
			<xsl:when test="$lexsDigest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText = 'Incident']/nc:ActivityDateRange/nc:StartDate/nc:Date">
				<xsl:value-of select="$lexsDigest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText = 'Incident']/nc:ActivityDateRange/nc:StartDate/nc:Date"/>
			</xsl:when>
			<xsl:when test="$lexsDigest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText = 'Incident']/nc:ActivityDate/nc:Date">
				<xsl:value-of select="$lexsDigest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText = 'Incident']/nc:ActivityDate/nc:Date"/>
			</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="arrestID" select="$lexsDigest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText = 'Arrest']/@s:id"/>
	<xsl:variable name="arresteeID" select="$lexsDigest/lexsdigest:EntityPerson[j40:ArrestSubject][1]/lexsdigest:Person/@s:id"/>
	<xsl:variable name="activityID">A001</xsl:variable>
	<xsl:variable name="contactInfoID">CI001</xsl:variable>
	<xsl:param name="topic">topics:person/arrest</xsl:param>
	<xsl:template match="/">
		<xsl:apply-templates select="$lexsDigest/lexsdigest:EntityPerson[(j40:ArrestSubject[1])]" mode="notifyMessage"/>
	</xsl:template>
	<xsl:template match="lexsdigest:EntityPerson" mode="notifyMessage">
		<xsl:variable name="personID" select="lexsdigest:Person/@s:id"/>
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
				<b:Topic Dialect="http://docs.oasis-open.org/wsn/t-1/TopicExpression/Concrete" xmlns:topics="http://ojbc.org/wsn/topics">
					<xsl:value-of select="normalize-space($topic)"/>
				</b:Topic>
				<!--Optional:-->
				<b:ProducerReference>
					<add:Address>http://www.ojbc.org/arrestNotificationProducer</add:Address>
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
						<notificationExt:NotifyingArrest>
							<xsl:attribute name="s:id"><xsl:value-of select="$activityID"/></xsl:attribute>
							<xsl:apply-templates select="/*/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataSubmitterMetadata" mode="reportingOrganization"/>
							<notificationExt:NotifyingActivityReportingSystemNameText>
								<xsl:value-of select="/*/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataSubmitterMetadata/lexs:SystemIdentifier/lexs:SystemID"/>
							</notificationExt:NotifyingActivityReportingSystemNameText>
							<xsl:apply-templates select="$lexsDataItemPackage/lexs:StructuredPayload/ojbc:ArrestReport/oar:RelatedFBISubscription/oar:RecordRapBackSubscriptionIdentification[nc:IdentificationID]"/>
							<xsl:apply-templates select="$lexsDataItemPackage/lexs:StructuredPayload/ojbc:ArrestReport[oar:FederalCriminalHistoryRecordDocument]"/>
							<xsl:apply-templates select="$lexsDigest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText = 'Arrest']" mode="arrest"/>
							<xsl:apply-templates select="$lexsDigest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText = 'Incident']" mode="incident"/>
							<xsl:apply-templates select="/*/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText = 'Offense']" mode="Offense"/>
						</notificationExt:NotifyingArrest>
						<nc:ActivityInvolvedPersonAssociation>
							<nc:ActivityReference>
								<xsl:attribute name="s:ref"><xsl:value-of select="$activityID"/></xsl:attribute>
							</nc:ActivityReference>
							<nc:PersonReference>
								<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(lexsdigest:Person)"/></xsl:attribute>
							</nc:PersonReference>
							<xsl:choose>
								<xsl:when test="j40:ArrestSubject">
									<nc:PersonActivityInvolvementText>Arrest Subject</nc:PersonActivityInvolvementText>
								</xsl:when>
							</xsl:choose>
						</nc:ActivityInvolvedPersonAssociation>
						<xsl:apply-templates select="." mode="person"/>
						<xsl:if test="$lexsDigest/lexsdigest:EntityPerson/j40:EnforcementOfficial">
							<xsl:apply-templates select="$lexsDigest/lexsdigest:EntityPerson/j40:EnforcementOfficial" mode="enforcementOfficialPerson"/>
						</xsl:if>
						<xsl:if test="$lexsAssociations/nc:ResidenceAssociation or $lexsAssociations/lexsdigest:PersonContactAssociation">
							<nc:ContactInformation>
								<xsl:attribute name="s:id"><xsl:value-of select="$contactInfoID"/></xsl:attribute>
								<xsl:apply-templates select="$lexsAssociations/nc:ResidenceAssociation[nc:PersonReference/@s:ref=$arresteeID]"/>
								<xsl:apply-templates select="$lexsAssociations/lexsdigest:PersonContactAssociation[nc:PersonReference/@s:ref=$arresteeID]"/>
							</nc:ContactInformation>
							<nc:PersonContactInformationAssociation>
								<nc:PersonReference>
									<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(lexsdigest:Person)"/></xsl:attribute>
								</nc:PersonReference>
								<nc:ContactInformationReference>
									<xsl:attribute name="s:ref"><xsl:value-of select="$contactInfoID"/></xsl:attribute>
								</nc:ContactInformationReference>
							</nc:PersonContactInformationAssociation>
						</xsl:if>
					</notification:NotificationMessage>
				</b:Message>
			</b:NotificationMessage>
		</b:Notify>
	</xsl:template>
	<xsl:template match="nc:ResidenceAssociation">
		<xsl:variable name="locationID" select="nc:LocationReference/@s:ref"/>
		<xsl:apply-templates select="$lexsDigest/lexsdigest:EntityLocation/nc:Location[@s:id=$locationID]" mode="residence"/>
	</xsl:template>
	<xsl:template match="lexsdigest:PersonContactAssociation">
		<xsl:apply-templates select="lexsdigest:ContactPersonTelephoneNumberReference[@s:ref = $lexsDigest/lexsdigest:EntityTelephoneNumber/lexsdigest:TelephoneNumber/@s:id]"/>
	</xsl:template>
	<xsl:template match="lexsdigest:ContactPersonTelephoneNumberReference">
		<xsl:variable name="phoneNumberRef" select="@s:ref"/>
		<nc:ContactTelephoneNumber>
			<xsl:apply-templates select="$lexsDigest/lexsdigest:EntityTelephoneNumber/lexsdigest:TelephoneNumber[@s:id=$phoneNumberRef]"/>
		</nc:ContactTelephoneNumber>
	</xsl:template>
	<xsl:template match="lexsdigest:TelephoneNumber">
		<xsl:copy-of select="nc:FullTelephoneNumber" copy-namespaces="no"/>
		<xsl:if test="nc:NANPTelephoneNumber">
			<nc:FullTelephoneNumber>
				<nc:TelephoneNumberFullID>(<xsl:value-of select="nc:NANPTelephoneNumber/nc:TelephoneAreaCodeID"/>)<xsl:value-of select="nc:NANPTelephoneNumber/nc:TelephoneExchangeID"/>-<xsl:value-of select="nc:NANPTelephoneNumber/nc:TelephoneLineID"/>
				</nc:TelephoneNumberFullID>
			</nc:FullTelephoneNumber>
		</xsl:if>
	</xsl:template>
	<xsl:template match="nc:Activity" mode="Offense">
		<xsl:variable name="activityID" select="@s:id"/>
		<j:Offense>
			<xsl:attribute name="s:id" select="$activityID"/>
			<xsl:apply-templates select="$lexsDataItemPackage/lexs:StructuredPayload/ndexia:ArrestReport/ndexia:Offense[ndexia:ActivityAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$activityID]" mode="description"/>
			<xsl:apply-templates select="$lexsDataItemPackage/lexs:StructuredPayload/ndexia:ArrestReport/ndexia:Offense[ndexia:ActivityAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$activityID]" mode="code"/>
			<xsl:apply-templates select="$lexsDataItemPackage/lexs:StructuredPayload/oar:ArrestReport/oar:Offense[lexslib:SameAsDigestReference/@lexslib:ref=$activityID]"/>
		</j:Offense>
	</xsl:template>
	<xsl:template match="ndexia:Offense" mode="description">
		<xsl:apply-templates select="ndexia:OffenseText"/>
	</xsl:template>
	<xsl:template match="ndexia:Offense" mode="code">
		<xsl:apply-templates select="ndexia:OffenseCode"/>
	</xsl:template>
	<xsl:template match="oar:Offense">
		<xsl:apply-templates select="oar:OffenseCodeText"/>
	</xsl:template>
	<xsl:template match="ndexia:OffenseText">
		<nc:ActivityDescriptionText>
			<xsl:value-of select="normalize-space(.)"/>
		</nc:ActivityDescriptionText>
	</xsl:template>
	<xsl:template match="ndexia:OffenseCode">
		<notificationExt:OffenseFBINDEXCode>
			<xsl:value-of select="."/>
		</notificationExt:OffenseFBINDEXCode>
	</xsl:template>
	<xsl:template match="oar:OffenseCodeText">
		<j:OffenseCategoryText>
			<xsl:value-of select="."/>
		</j:OffenseCategoryText>
	</xsl:template>
	<xsl:template match="oar:RecordRapBackSubscriptionIdentification">
		<notificationExt:RelatedFBISubscription>
			<notificationExt:RecordRapBackSubscriptionIdentification>
				<nc:IdentificationID>
					<xsl:value-of select="."/>
				</nc:IdentificationID>
			</notificationExt:RecordRapBackSubscriptionIdentification>
		</notificationExt:RelatedFBISubscription>
	</xsl:template>
	<xsl:template match="ojbc:ArrestReport">
		<notificationExt:CriminalHistoryRecordDocument>
			<nc:DocumentBinary>
				<notificationExt:Base64BinaryObject><xsl:value-of select="ojbc:FederalCriminalHistoryRecordDocument/nc:DocumentBinary/ojbc:Base64BinaryObject"/></notificationExt:Base64BinaryObject>
			</nc:DocumentBinary>
		</notificationExt:CriminalHistoryRecordDocument>
	</xsl:template>
	<xsl:template match="j40:EnforcementOfficial" mode="enforcementOfficialUnit">
		<xsl:variable name="enforcementOfficialID" select="preceding-sibling::lexsdigest:Person/@s:id"/>
		<xsl:variable name="enforcementOfficialOrganizationID" select="$lexsDigest/lexsdigest:Associations/nc:PersonAssignedUnitAssociation/nc:OrganizationReference/@s:ref"/>
		<xsl:variable name="enforcementOfficialUnit" select="$lexsDigest/lexsdigest:EntityOrganization/nc:Organization[@s:id=$enforcementOfficialOrganizationID]/nc:OrganizationName"/>
		<j:IncidentAugmentation>
			<j:IncidentReportingOfficial>
				<nc:RoleOfPersonReference>
					<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(.)"/></xsl:attribute>
				</nc:RoleOfPersonReference>
				<j:EnforcementOfficialUnit>
					<nc:OrganizationName>
						<xsl:value-of select="$enforcementOfficialUnit"/>
					</nc:OrganizationName>
					<xsl:if test="$lexsDataOwnerContactTelephone">
						<nc:OrganizationPrimaryContactInformation>
							<nc:ContactTelephoneNumber>
								<nc:FullTelephoneNumber>
									<nc:TelephoneNumberFullID>(<xsl:value-of select="$lexsDataOwnerContactTelephone/nc:TelephoneAreaCodeID"/>)<xsl:value-of select="$lexsDataOwnerContactTelephone/nc:TelephoneExchangeID"/>-<xsl:value-of select="$lexsDataOwnerContactTelephone/nc:TelephoneLineID"/>
									</nc:TelephoneNumberFullID>
								</nc:FullTelephoneNumber>
							</nc:ContactTelephoneNumber>
						</nc:OrganizationPrimaryContactInformation>
					</xsl:if>
				</j:EnforcementOfficialUnit>
			</j:IncidentReportingOfficial>
		</j:IncidentAugmentation>
	</xsl:template>
	<xsl:template match="j40:EnforcementOfficial" mode="enforcementOfficialPerson">
		<j:Person>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id(.)"/></xsl:attribute>
			<nc:PersonName>
				<nc:PersonFullName>
					<xsl:value-of select="preceding-sibling::lexsdigest:Person/nc:PersonName/nc:PersonFullName"/>
				</nc:PersonFullName>
			</nc:PersonName>
		</j:Person>
	</xsl:template>
	<xsl:template match="nc:Activity" mode="arrest">
		<j:Arrest>
			<xsl:copy-of select="nc:ActivityDate" copy-namespaces="no"/>
			<xsl:apply-templates select="$lexsDigest/lexsdigest:EntityOrganization/nc:Organization[@s:id = $lexsAssociations/nc:PersonAssignedUnitAssociation[nc:PersonReference/@s:ref=$lexsAssociations/lexsdigest:ArrestOfficerAssociation[nc:ActivityReference/@s:ref=$arrestID]/nc:PersonReference/@s:ref]/nc:OrganizationReference/@s:ref]" mode="arrestAgency"/>
			<xsl:apply-templates select="$lexsAssociations/lexsdigest:ArrestOfficerAssociation[nc:ActivityReference/@s:ref=$arrestID]" mode="arrestOfficial"/>
			<xsl:apply-templates select="$lexsAssociations/lexsdigest:ArrestSubjectAssociation[nc:ActivityReference/@s:ref=$arrestID]" mode="arrestSubject"/>
		</j:Arrest>
	</xsl:template>
	<xsl:template match="nc:Organization" mode="arrestAgency">
		<j:ArrestAgency>
			<xsl:copy-of select="nc:OrganizationName" copy-namespaces="no"/>
		</j:ArrestAgency>
	</xsl:template>
	<xsl:template match="lexsdigest:ArrestOfficerAssociation" mode="arrestOfficial">
		<xsl:variable name="personID" select="nc:PersonReference/@s:ref"/>
		<j:ArrestOfficial>
			<nc:RoleOfPersonReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id($lexsDigest/lexsdigest:EntityPerson[lexsdigest:Person/@s:id=$personID]/j40:EnforcementOfficial)"/></xsl:attribute>
			</nc:RoleOfPersonReference>
		</j:ArrestOfficial>
	</xsl:template>
	<xsl:template match="lexsdigest:ArrestSubjectAssociation" mode="arrestSubject">
		<xsl:variable name="personID" select="nc:PersonReference/@s:ref"/>
		<j:ArrestSubject>
			<nc:RoleOfPersonReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id($lexsDigest/lexsdigest:EntityPerson/lexsdigest:Person[@s:id=$personID])"/></xsl:attribute>
			</nc:RoleOfPersonReference>
		</j:ArrestSubject>
	</xsl:template>
	<xsl:template match="lexsdigest:EntityPerson" mode="person">
		<xsl:apply-templates select="lexsdigest:Person"/>
	</xsl:template>
	<xsl:template match="lexsdigest:Person">
		<j:Person>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id()"/></xsl:attribute>
			<xsl:copy-of select="nc:PersonAlternateName" copy-namespaces="no"/>
			<xsl:copy-of select="nc:PersonBirthDate" copy-namespaces="no"/>
			<xsl:copy-of select="nc:PersonName" copy-namespaces="no"/>
		</j:Person>
	</xsl:template>
	<xsl:template match="nc:Location" mode="residence">
		<nc:ContactMailingAddress>
			<xsl:apply-templates select="nc:LocationAddress"/>
		</nc:ContactMailingAddress>
	</xsl:template>
	<xsl:template match="nc:Location" mode="incidentLocation">
		<nc:LocationAddress>
			<xsl:apply-templates select="nc:LocationAddress"/>
		</nc:LocationAddress>
	</xsl:template>
	<xsl:template match="nc:LocationAddress">
		<xsl:apply-templates select="nc:StructuredAddress"/>
	</xsl:template>
	<xsl:template match="nc:StructuredAddress">
		<nc:StructuredAddress>
			<xsl:apply-templates select="nc:LocationStreet"/>
			<xsl:copy-of select="nc:LocationCityName[. != '']" copy-namespaces="no"/>
			<xsl:copy-of select="nc:LocationStateName[. != '']" copy-namespaces="no"/>
		</nc:StructuredAddress>
	</xsl:template>
	<xsl:template match="nc:LocationStreet">
		<nc:LocationStreet>
			<xsl:choose>
				<xsl:when test="nc:StreetNumberText or nc:StreetName">
					<xsl:copy-of select="nc:StreetNumberText[. != '']" copy-namespaces="no"/>
					<xsl:copy-of select="nc:StreetName[. != '']" copy-namespaces="no"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:copy-of select="nc:StreetFullText[. != '']" copy-namespaces="no"/>
				</xsl:otherwise>
			</xsl:choose>
		</nc:LocationStreet>
	</xsl:template>
	<xsl:template match="nc:Activity" mode="incident">
		<j:Incident>
			<xsl:apply-templates select="$lexsDataItemPackage/lexs:PackageMetadata/lexs:DataItemID" mode="incidentID"/>
			<nc:ActivityCategoryText>Law Incident</nc:ActivityCategoryText>
			<xsl:choose>
				<xsl:when test="$incidentDateTime[ .!= '']">
					<nc:ActivityDate>
						<nc:DateTime>
							<xsl:value-of select="$incidentDateTime"/>
						</nc:DateTime>
					</nc:ActivityDate>
				</xsl:when>
				<xsl:when test="$incidentDate [. != '']">
					<nc:ActivityDate>
						<nc:Date>
							<xsl:value-of select="$incidentDate"/>
						</nc:Date>
					</nc:ActivityDate>
				</xsl:when>
			</xsl:choose>
			<xsl:apply-templates select="$lexsAssociations/lexsdigest:IncidentLocationAssociation[nc:LocationReference/@s:ref=$lexsDigest/lexsdigest:EntityLocation/nc:Location/@s:id]" mode="incidentLocation"/>
			<xsl:if test="$lexsDigest/lexsdigest:EntityPerson/j40:EnforcementOfficial">
				<xsl:apply-templates select="$lexsDigest/lexsdigest:EntityPerson/j40:EnforcementOfficial" mode="enforcementOfficialUnit"/>
			</xsl:if>
		</j:Incident>
	</xsl:template>
	<xsl:template match="lexs:DataItemID" mode="incidentID">
		<nc:ActivityIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="."/>
			</nc:IdentificationID>
		</nc:ActivityIdentification>
	</xsl:template>
	<xsl:template match="lexsdigest:IncidentLocationAssociation" mode="incidentLocation">
		<xsl:variable name="locationID" select="nc:LocationReference/@s:ref"/>
		<nc:IncidentLocation>
			<xsl:apply-templates select="$lexsDigest/lexsdigest:EntityLocation/nc:Location[@s:id=$locationID]" mode="incidentLocation"/>
		</nc:IncidentLocation>
	</xsl:template>
	<xsl:template match="lexs:DataSubmitterMetadata" mode="reportingOrganization">
		<notificationExt:NotifyingActivityReportingOrganization>
			<xsl:apply-templates select="lexs:SystemIdentifier" mode="reportingOrganization"/>
		</notificationExt:NotifyingActivityReportingOrganization>
	</xsl:template>
	
	<xsl:template match="lexs:SystemIdentifier" mode="reportingOrganization">
		<xsl:copy-of select="nc:OrganizationName" copy-namespaces="no"/>
	</xsl:template>
</xsl:stylesheet>
