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
    xmlns:nc="http://niem.gov/niem/niem-core/2.0" 
    xmlns:niem-xsd="http://niem.gov/niem/proxy/xsd/2.0" 
    xmlns:s="http://niem.gov/niem/structures/2.0" 
    xmlns:notification="http://ojbc.org/IEPD/Exchange/NotificationMessage/1.0" 
    xmlns:notificationExt="http://ojbc.org/IEPD/Extensions/Notification/1.0"
    xmlns:exc="http://ojbc.org/IEPD/Exchange/CycleTrackingIdentifierAssignmentReport/1.0"
	xmlns:ext="http://ojbc.org/IEPD/Extension/CycleTrackingIdentifierAssignmentReport/1.0"
	xmlns:j50="http://release.niem.gov/niem/domains/jxdm/5.0/" 
	xmlns:nc30="http://release.niem.gov/niem/niem-core/3.0/"
	xmlns:niem-xs="http://release.niem.gov/niem/proxy/xsd/3.0/" 
	xmlns:s30="http://release.niem.gov/niem/structures/3.0/"
	xmlns:chu="http://ojbc.org/IEPD/Extensions/CriminalHistoryUpdate/1.0"
	xmlns:me="http://ojbc.org/IEPD/Extensions/Maine/ChargeCodes/1.0" 
	exclude-result-prefixes="s30 niem-xs nc30 j50 exc ext xs"
    version="2.0">
    
    <xsl:output indent="yes" method="xml"/>
    <xsl:param name="topic">{http://ojbc.org/wsn/topics}:person/criminalHistoryCycleTrackingIdentifierAssignment</xsl:param>
    <xsl:param name="systemId">{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB</xsl:param>
    
    <xsl:template match="/">
            <xsl:apply-templates select="/exc:CycleTrackingIdentifierAssignmentReport"/>
    </xsl:template>    
    
    <xsl:template match="exc:CycleTrackingIdentifierAssignmentReport">    
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
                        <add:Address>http://www.ojbc.org/criminalHistoryTrackingIdentifierAssignmentNotificationProducer</add:Address>
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
                            <notificationExt:NotifyingCriminalHistoryUpdate>
                            	<xsl:attribute name="s:id"><xsl:value-of select="generate-id()"/></xsl:attribute>                            	                            	
                            	<notificationExt:NotifyingActivityReportingSystemNameText>
                            		<xsl:value-of select="$systemId"/>
                            	</notificationExt:NotifyingActivityReportingSystemNameText>                            	
                            	<xsl:apply-templates select="ext:CycleTrackingIdentifierAssignment"/>
                            </notificationExt:NotifyingCriminalHistoryUpdate>
                            <xsl:apply-templates select="ext:CycleTrackingIdentifierAssignment/j50:Subject/nc30:RoleOfPerson[@s30:ref]" mode="activityInvolvedPerson"/>
                            <xsl:apply-templates select="nc30:Person" mode="person"/>
                            <xsl:apply-templates select="nc30:Organization"/>
                        </notification:NotificationMessage>
                    </b:Message>
                </b:NotificationMessage>
            </b:Notify>
    </xsl:template>   
    <xsl:template match="ext:CycleTrackingIdentifierAssignment">
    	<chu:CycleTrackingIdentifierAssignment>
    		<xsl:apply-templates select="nc30:ActivityDate"/>
    		<xsl:apply-templates select="ext:CycleTrackingIdentification"/>
    		<xsl:apply-templates select="j50:OriginatorOrganization[@s30:ref]"/>
    		<xsl:apply-templates select="j50:CaseProsecutionAttorney/ext:ProsecutionAttorneyOrganization[@s30:ref]"/>
    		<xsl:apply-templates select="nc30:Case"/>
    		<xsl:apply-templates select="j50:Charge"/>
    	</chu:CycleTrackingIdentifierAssignment>
    </xsl:template>  
    <xsl:template match="nc30:ActivityDate">
    	<xsl:if test="normalize-space(nc30:DateTime) != '' or normalize-space(nc30:Date) != ''">
    		<nc:ActivityDate>
    			<xsl:apply-templates select="nc30:DateTime"/>
	    		<xsl:apply-templates select="nc30:Date"/>
    		</nc:ActivityDate>
	    </xsl:if>
    </xsl:template>
        <xsl:template match="nc30:DateTime">
    	<nc:DateTime>
    		<xsl:value-of select="."/>
    	</nc:DateTime>
    </xsl:template>
        <xsl:template match="nc30:Date">
    	<nc:Date>
    		<xsl:value-of select="."/>
    	</nc:Date>
    </xsl:template>
    <xsl:template match="j50:Charge">
    	<j:Charge>
    		<xsl:attribute name="s:id"><xsl:value-of select="generate-id()"/></xsl:attribute>
    		<xsl:apply-templates select="j50:ChargeDescriptionText"/>
    		<xsl:apply-templates select="j50:ChargeEnhancingFactor"/>
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
    <xsl:template match="j50:StatuteText">
    	<j:StatuteText>
    		<xsl:value-of select="."/>
    	</j:StatuteText>
    </xsl:template>
    <xsl:template match="j50:ChargeEnhancingFactor">
    	<chu:ChargeEnhancingFactor>
    		<xsl:apply-templates select="j50:ChargeEnhancingFactorDescriptionText"/>
    		<xsl:copy-of select="me:ChargeEnhancingFactorCode" copy-namespaces="no"/>
    	</chu:ChargeEnhancingFactor>
    </xsl:template>
    <xsl:template match="j50:ChargeEnhancingFactorDescriptionText">
    	<j:ChargeEnhancingFactorDescriptionText>
    		<xsl:value-of select="."/>
    	</j:ChargeEnhancingFactorDescriptionText>
    </xsl:template>
    <xsl:template match="j50:ChargeDescriptionText">
    	<j:ChargeDescriptionText>
    		<xsl:value-of select="."/>
    	</j:ChargeDescriptionText>
    </xsl:template>
    <xsl:template match="nc30:Case">
    	<nc:Case>
    		<xsl:apply-templates select="j50:ActivityAugmentation"/>
    	</nc:Case>
    </xsl:template>
    <xsl:template match="j50:ActivityAugmentation">
    	<nc:ActivityIdentification>
    		<nc:IdentificationID>
    			<xsl:value-of select="j50:CaseNumberText"/>
    		</nc:IdentificationID>
    	</nc:ActivityIdentification>
    </xsl:template>
    <xsl:template match="ext:CycleTrackingIdentification">
    	<chu:CycleTrackingIdentification>
    		<xsl:apply-templates select="nc30:IdentificationID"/>
    	</chu:CycleTrackingIdentification>
    </xsl:template>
    <xsl:template match="ext:ProsecutionAttorneyOrganization">
    	<xsl:variable name="orgID" select="@s30:ref"/>
    	<chu:ProsecutionAttorneyOrganizationReference>
    		<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(/exc:CycleTrackingIdentifierAssignmentReport/nc30:Organization[@s30:id=$orgID])"/></xsl:attribute>
    	</chu:ProsecutionAttorneyOrganizationReference>
    </xsl:template>
    <xsl:template match="j50:OriginatorOrganization">
    	<xsl:variable name="orgID" select="@s30:ref"/>
    	<chu:OriginatorOrganizationReference>
    		<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(/exc:CycleTrackingIdentifierAssignmentReport/nc30:Organization[@s30:id=$orgID])"/></xsl:attribute>
    	</chu:OriginatorOrganizationReference>
    </xsl:template>
    <xsl:template match="nc30:RoleOfPerson" mode="activityInvolvedPerson">
    	<xsl:variable name="personID" select="@s30:ref"/>
    	<nc:ActivityInvolvedPersonAssociation>
			<nc:ActivityReference>
				<xsl:attribute name="s:ref">
					<xsl:value-of select="generate-id(/exc:CycleTrackingIdentifierAssignmentReport)"/>
				</xsl:attribute>
			</nc:ActivityReference>
			<nc:PersonReference>
				<xsl:attribute name="s:ref">
					<xsl:value-of select="generate-id(/exc:CycleTrackingIdentifierAssignmentReport/nc30:Person[@s30:id=$personID])"/>
				</xsl:attribute>
			</nc:PersonReference>
		</nc:ActivityInvolvedPersonAssociation>
    </xsl:template>
    <xsl:template match="nc30:Person" mode="person">
    	<xsl:variable name="personID" select="@s30:ref"/>
    	<j:Person>
    		<xsl:attribute name="s:id">
    			<xsl:value-of select="generate-id()"/>
    		</xsl:attribute>
    		<xsl:apply-templates select="nc30:PersonBirthDate"/>
    		<xsl:apply-templates select="nc30:PersonName"/>
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
    	<nc:PersonGivenName><xsl:value-of select="."/></nc:PersonGivenName>
    </xsl:template>
     <xsl:template match="nc30:PersonMiddleName">
    	<nc:PersonMiddleName><xsl:value-of select="."/></nc:PersonMiddleName>
    </xsl:template>
     <xsl:template match="nc30:PersonSurName">
    	<nc:PersonSurName><xsl:value-of select="."/></nc:PersonSurName>
    </xsl:template>
    <xsl:template match="nc30:Organization">
    	<j:Organization>
    		<xsl:attribute name="s:id"><xsl:value-of select="generate-id()"/></xsl:attribute>
    		<xsl:apply-templates select="nc30:OrganizationName"/>
    		<xsl:apply-templates select="j50:OrganizationAugmentation"/>
    	</j:Organization>
    </xsl:template>
    <xsl:template match="nc30:OrganizationName">
    	<nc:OrganizationName><xsl:value-of select="."/></nc:OrganizationName>
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
</xsl:stylesheet>