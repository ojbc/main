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
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:b="http://docs.oasis-open.org/wsn/b-2" 
    xmlns:add="http://www.w3.org/2005/08/addressing"
    xmlns:j="http://niem.gov/niem/domains/jxdm/4.1" 
    xmlns:j40="http://niem.gov/niem/domains/jxdm/4.0"
    xmlns:nc="http://niem.gov/niem/niem-core/2.0" 
    xmlns:niem-xsd="http://niem.gov/niem/proxy/xsd/2.0" 
    xmlns:s="http://niem.gov/niem/structures/2.0" 
    xmlns:notification="http://ojbc.org/IEPD/Exchange/NotificationMessage/1.0" 
    xmlns:notificationExt="http://ojbc.org/IEPD/Extensions/Notification/1.0"
    xmlns:ir="http://ojbc.org/IEPD/Exchange/IncidentReport/1.0"
    xmlns:lexs="http://usdoj.gov/leisp/lexs/3.1"
    xmlns:lexspd="http://usdoj.gov/leisp/lexs/publishdiscover/3.1"
    xmlns:lexsdigest="http://usdoj.gov/leisp/lexs/digest/3.1" 
    xmlns:ndexia="http://fbi.gov/cjis/N-DEx/IncidentArrest/2.1"
    xmlns:lexslib="http://usdoj.gov/leisp/lexs/library/3.1"
    xmlns:ext="http://ojbc.org/IEPD/Extensions/IncidentReportStructuredPayload/1.0"
    exclude-result-prefixes="xs ir lexs lexspd lexsdigest j40"
    version="2.0">
    
    <xsl:output indent="yes" method="xml"/>
    												 
    <xsl:variable name="lexsDataItemPackage" select="/*/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage"/>
    <xsl:variable name="lexsDigest" select="$lexsDataItemPackage/lexs:Digest"/>
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
    <xsl:param name="topic">topics:person/incident</xsl:param>
    <xsl:template match="/">
        <notifications>
            <xsl:apply-templates select="$lexsDigest/lexsdigest:EntityPerson[not(j40:EnforcementOfficial)]"/>
        </notifications>
    </xsl:template>    
    
    <xsl:template match="lexsdigest:EntityPerson">  
    	<xsl:variable name="personID" select="lexsdigest:Person/@s:id"/>  
        <wrapper>
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
                    <b:Topic Dialect="http://docs.oasis-open.org/wsn/t-1/TopicExpression/Concrete" xmlns:topics="http://ojbc.org/wsn/topics"><xsl:value-of select="normalize-space($topic)"/></b:Topic>
                    <!--Optional:-->
                    <b:ProducerReference>
                        <add:Address>http://www.ojbc.org/incidentNotificationProducer</add:Address>
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
                            <notificationExt:NotifyingIncident s:id="I001">
                            	<notificationExt:NotifyingActivityReportingSystemNameText><xsl:value-of select="/*/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataSubmitterMetadata/lexs:SystemIdentifier/lexs:SystemID"/></notificationExt:NotifyingActivityReportingSystemNameText>
                                <j:Incident>
 									<nc:ActivityIdentification>
                						<nc:IdentificationID><xsl:value-of  select="$lexsDataItemPackage/lexs:PackageMetadata/lexs:DataItemID"/></nc:IdentificationID>
            						</nc:ActivityIdentification>                               
                                    <nc:ActivityCategoryText>Law Incident</nc:ActivityCategoryText>
                                    <nc:ActivityDate>
                                    	<xsl:choose>
                                    		<xsl:when test="$incidentDateTime[ .!= '']">
                                    			<nc:DateTime><xsl:value-of select="$incidentDateTime"/></nc:DateTime>
                                    		</xsl:when>
                                    		<xsl:when test="$incidentDate [. != '']">
                                    			<nc:Date><xsl:value-of select="$incidentDate"/></nc:Date>
                                    		</xsl:when>
                                    	</xsl:choose> 
                                    </nc:ActivityDate>
                                    <xsl:if test="$lexsDigest/lexsdigest:EntityPerson/j40:EnforcementOfficial">
                                        <j:IncidentAugmentation>
                                            <xsl:apply-templates select="$lexsDigest/lexsdigest:EntityPerson/j40:EnforcementOfficial" mode="enforcementOfficialUnit"/>
                                        </j:IncidentAugmentation>
                                    </xsl:if>
                                </j:Incident>
                                <xsl:apply-templates select="/*/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText = 'Offense']" mode="Offense"/>
                            </notificationExt:NotifyingIncident>
                            <nc:ActivityInvolvedPersonAssociation>
                                <nc:ActivityReference s:ref="I001"/>
                                <nc:PersonReference>
                                    <xsl:attribute name="s:ref"><xsl:value-of select="generate-id(lexsdigest:Person)"/></xsl:attribute>
                                </nc:PersonReference>
                                <nc:PersonActivityInvolvementText>
                                	<xsl:choose>
                                		<xsl:when test="j40:ArrestSubject">Arrest Subject</xsl:when>
                                		<xsl:when test="j40:Suspect">Suspect</xsl:when>
                                		<xsl:when test="j40:Victim">Victim</xsl:when>
                                		<xsl:when test="$lexsDigest/lexsdigest:Associations/nc:ActivityInvolvedPersonAssociation[nc:PersonReference/@s:ref=$personID]/nc:PersonActivityInvolvementText">
                                			<xsl:value-of select="$lexsDigest/lexsdigest:Associations/nc:ActivityInvolvedPersonAssociation[nc:PersonReference/@s:ref=$personID]/nc:PersonActivityInvolvementText"/>
                                		</xsl:when>
                                		<xsl:otherwise>Role not specified</xsl:otherwise>
                                	</xsl:choose>
                                </nc:PersonActivityInvolvementText>
                            </nc:ActivityInvolvedPersonAssociation>
                            <j:Person>
                                <xsl:attribute name="s:id"><xsl:value-of select="generate-id(lexsdigest:Person)"/></xsl:attribute>
                                <nc:PersonBirthDate>
                                    <nc:Date><xsl:value-of  select="lexsdigest:Person/nc:PersonBirthDate/nc:Date"></xsl:value-of></nc:Date>
                                </nc:PersonBirthDate>
                                <nc:PersonName>
                                    <nc:PersonGivenName><xsl:value-of select="lexsdigest:Person/nc:PersonName/nc:PersonGivenName"/></nc:PersonGivenName>
                                    <nc:PersonSurName><xsl:value-of select="lexsdigest:Person/nc:PersonName/nc:PersonSurName"/></nc:PersonSurName>
                                </nc:PersonName>
                            </j:Person>
                            
                            <xsl:if test="$lexsDigest/lexsdigest:EntityPerson/j40:EnforcementOfficial">
                                <xsl:apply-templates select="$lexsDigest/lexsdigest:EntityPerson/j40:EnforcementOfficial" mode="enforcementOfficialPerson"/>
                            </xsl:if>    

                        </notification:NotificationMessage>
                    </b:Message>
                </b:NotificationMessage>
            </b:Notify>
        </wrapper>
    </xsl:template>   
    
    <xsl:template match="nc:Activity" mode="Offense">
    	<xsl:variable name="activityID" select="@s:id"/>
    	<j:Offense>
    		<xsl:attribute name="s:id" select="$activityID"/>
    		<xsl:apply-templates select="/*/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:StructuredPayload/ndexia:IncidentReport/ndexia:Offense[ndexia:ActivityAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$activityID]" mode="description"/>
    		<xsl:apply-templates select="/*/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:StructuredPayload/ndexia:IncidentReport/ndexia:Offense[ndexia:ActivityAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$activityID]" mode="code"/>
    		<xsl:apply-templates select="/*/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:StructuredPayload/ext:IncidentReport/ext:Offense[lexslib:SameAsDigestReference/@lexslib:ref=$activityID]"/>
    	</j:Offense>
    </xsl:template>  
    
    <xsl:template match="ndexia:Offense" mode="description">
    	<xsl:apply-templates select="ndexia:OffenseText"/>
    </xsl:template>
    
    <xsl:template match="ndexia:Offense" mode="code">
    	<xsl:apply-templates select="ndexia:OffenseCode"/>
    </xsl:template>
    
    <xsl:template match="ext:Offense">
    	<xsl:apply-templates select="ext:OffenseCodeText"/>
    </xsl:template>
    
    <xsl:template match="ndexia:OffenseText">
    	<nc:ActivityDescriptionText><xsl:value-of select="normalize-space(.)"/></nc:ActivityDescriptionText>
    </xsl:template>
    <xsl:template match="ndexia:OffenseCode">
    	<notificationExt:OffenseFBINDEXCode><xsl:value-of select="."/></notificationExt:OffenseFBINDEXCode>
    </xsl:template>
    <xsl:template match="ext:OffenseCodeText">
    	<j:OffenseCategoryText><xsl:value-of select="."/></j:OffenseCategoryText>
    </xsl:template>
    
    <xsl:template match="j40:EnforcementOfficial" mode="enforcementOfficialUnit">
        
        <xsl:variable name="enforcementOfficialID" select="preceding-sibling::lexsdigest:Person/@s:id" />
        <xsl:variable name="enforcementOfficialOrganizationID" select="$lexsDigest/lexsdigest:Associations/nc:PersonAssignedUnitAssociation/nc:OrganizationReference/@s:ref" />
        <xsl:variable name="enforcementOfficialUnit" select="$lexsDigest/lexsdigest:EntityOrganization/nc:Organization[@s:id=$enforcementOfficialOrganizationID]/nc:OrganizationName"/>

        <j:IncidentReportingOfficial>
            <nc:RoleOfPersonReference>
                <xsl:attribute name="s:ref"><xsl:value-of select="generate-id(.)"/></xsl:attribute>
            </nc:RoleOfPersonReference>
            <j:EnforcementOfficialUnit>
                <nc:OrganizationName><xsl:value-of select="$enforcementOfficialUnit"/></nc:OrganizationName>
                
                <xsl:if test="$lexsDataOwnerContactTelephone">
					<nc:OrganizationPrimaryContactInformation>
						<nc:ContactTelephoneNumber>
							<nc:FullTelephoneNumber>
								<nc:TelephoneNumberFullID>(<xsl:value-of select="$lexsDataOwnerContactTelephone/nc:TelephoneAreaCodeID"/>)<xsl:value-of select="$lexsDataOwnerContactTelephone/nc:TelephoneExchangeID"/>-<xsl:value-of select="$lexsDataOwnerContactTelephone/nc:TelephoneLineID"/></nc:TelephoneNumberFullID>
							</nc:FullTelephoneNumber>
						</nc:ContactTelephoneNumber>
					</nc:OrganizationPrimaryContactInformation>            
                </xsl:if>
                
            </j:EnforcementOfficialUnit>
        </j:IncidentReportingOfficial>
    </xsl:template>

    <xsl:template match="j40:EnforcementOfficial" mode="enforcementOfficialPerson">
        <j:Person>
            <xsl:attribute name="s:id"><xsl:value-of select="generate-id(.)"/></xsl:attribute>
            <nc:PersonName>
                <nc:PersonFullName><xsl:value-of select="preceding-sibling::lexsdigest:Person/nc:PersonName/nc:PersonFullName"/></nc:PersonFullName>
            </nc:PersonName>
        </j:Person>
    </xsl:template>
    
</xsl:stylesheet>