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
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:b="http://docs.oasis-open.org/wsn/b-2"
	xmlns:add="http://www.w3.org/2005/08/addressing"
	xmlns:topics="http://ojbc.org/wsn/topics"
	xmlns:s="http://niem.gov/niem/structures/2.0" xmlns:jxdm41="http://niem.gov/niem/domains/jxdm/4.1"
	xmlns:nc20="http://niem.gov/niem/niem-core/2.0" 
	xmlns:parole="http://ojbc.org/IEPD/Extensions/ParoleCase/1.0"
	xmlns:sm="http://ojbc.org/IEPD/Exchange/SubscriptionMessage/1.0"
	xmlns:smext="http://ojbc.org/IEPD/Extensions/Subscription/1.0"
	xmlns:pci="http://ojbc.org/IEPD/Exchange/ParoleCaseInitiation/1.0" 
	exclude-result-prefixes="s parole pci">
	<xsl:output method="xml" version="1.0" encoding="UTF-8"
		indent="yes" />

	<xsl:param name="topicExpression"/>
	<xsl:param name="fbiId"/>

	<xsl:template match="pci:ParoleCaseInitiation">
		<b:Subscribe>
			<b:ConsumerReference>
				<!-- Change to some address -->
				<add:Address>http://www.ojbc.org/OJB/SubscribeNotify</add:Address>
				<!--Optional: -->
				<add:ReferenceParameters>
					<!--You may enter ANY elements at this point -->
				</add:ReferenceParameters>
				<!--Optional: -->
				<add:Metadata>
					<!--You may enter ANY elements at this point -->
				</add:Metadata>
				<!--You may enter ANY elements at this point -->
			</b:ConsumerReference>
			<!--Optional: -->
			<b:Filter>
				<b:TopicExpression
					Dialect="http://docs.oasis-open.org/wsn/t-1/TopicExpression/Concrete"><xsl:value-of select="$topicExpression"/></b:TopicExpression>
			</b:Filter>
			<!--Optional: -->
			<b:InitialTerminationTime><xsl:value-of select="normalize-space(parole:ParoleCase/jxdm41:Supervision/jxdm41:SupervisionRelease/nc20:ActivityDate/nc20:Date)"/></b:InitialTerminationTime>
			<!--Optional: -->
			<b:SubscriptionPolicy>
				<!--You may enter ANY elements at this point -->
			</b:SubscriptionPolicy>
			<!--You may enter ANY elements at this point -->
			
			<sm:SubscriptionMessage>
				<smext:Subject>
					<xsl:apply-templates select="parole:ParoleCase/jxdm41:Supervision/nc20:SupervisionPerson/nc20:PersonBirthDate"/>
					<nc20:PersonName>
						<nc20:PersonGivenName><xsl:value-of select="parole:ParoleCase/jxdm41:Supervision/nc20:SupervisionPerson/nc20:PersonName/nc20:PersonGivenName" /></nc20:PersonGivenName>
						<nc20:PersonSurName><xsl:value-of select="parole:ParoleCase/jxdm41:Supervision/nc20:SupervisionPerson/nc20:PersonName/nc20:PersonSurName" /></nc20:PersonSurName>
						<nc20:PersonFullName>
							<xsl:value-of
								select="parole:ParoleCase/jxdm41:Supervision/nc20:SupervisionPerson/nc20:PersonName/nc20:PersonGivenName" />
							<xsl:if
								test="parole:ParoleCase/jxdm41:Supervision/nc20:SupervisionPerson/nc20:PersonName/nc20:PersonMiddleName">
								<xsl:text> </xsl:text>
								<xsl:value-of
									select="parole:ParoleCase/jxdm41:Supervision/nc20:SupervisionPerson/nc20:PersonName/nc20:PersonMiddleName" />
							</xsl:if>
							<xsl:text> </xsl:text>
							<xsl:value-of
								select="parole:ParoleCase/jxdm41:Supervision/nc20:SupervisionPerson/nc20:PersonName/nc20:PersonSurName" />
						</nc20:PersonFullName>						
					</nc20:PersonName>										
					<jxdm41:PersonAugmentation>
						<xsl:if test="$fbiId and $fbiId != ''">
							<jxdm41:PersonFBIIdentification>
								<nc20:IdentificationID><xsl:value-of select="$fbiId" /></nc20:IdentificationID>
							</jxdm41:PersonFBIIdentification>						
						</xsl:if>						
						<xsl:apply-templates select="parole:ParoleCase/jxdm41:Supervision/nc20:SupervisionPerson/nc20:PersonStateIdentification" />
					</jxdm41:PersonAugmentation>																														
				</smext:Subject>
				<nc20:ContactEmailID><xsl:value-of select="normalize-space(parole:ParoleCase/nc20:ContactInformation[@s:id='paroleOfficerEmail1']/nc20:ContactEmailID)"/></nc20:ContactEmailID>
				<smext:SystemName><xsl:value-of select="parole:SystemName"/></smext:SystemName>
				<smext:SubscriptionQualifierIdentification>
					<nc20:IdentificationID><xsl:value-of select="normalize-space(parole:ParoleCase/jxdm41:Supervision/nc20:SupervisionPerson/nc20:PersonStateIdentification/nc20:IdentificationID)"/></nc20:IdentificationID>
				</smext:SubscriptionQualifierIdentification>
		        <xsl:if test="parole:ParoleCase/jxdm41:Supervision/jxdm41:SupervisionAugmentation/jxdm41:SupervisionAssignedTerm/nc20:ActivityDate/nc20:Date">
		        	<nc20:DateRange> 
		                <nc20:StartDate>
		                   <nc20:Date><xsl:value-of select="normalize-space(parole:ParoleCase/jxdm41:Supervision/jxdm41:SupervisionAugmentation/jxdm41:SupervisionAssignedTerm/nc20:ActivityDate/nc20:Date)"/></nc20:Date>
		                </nc20:StartDate>
		                <xsl:if test="parole:ParoleCase/jxdm41:Supervision/jxdm41:SupervisionRelease/nc20:ActivityDate/nc20:Date">
		                <nc20:EndDate>
		                   <nc20:Date><xsl:value-of select="normalize-space(parole:ParoleCase/jxdm41:Supervision/jxdm41:SupervisionRelease/nc20:ActivityDate/nc20:Date)"/></nc20:Date>
		                </nc20:EndDate>
		                </xsl:if>
		            </nc20:DateRange>    
				</xsl:if>	
				<smext:CriminalSubscriptionReasonCode>CS</smext:CriminalSubscriptionReasonCode>
			</sm:SubscriptionMessage>
		</b:Subscribe>
	</xsl:template>
	<xsl:template match="nc20:PersonBirthDate">
		<nc20:PersonBirthDate>
			<nc20:Date><xsl:value-of select="nc20:Date"/></nc20:Date>
		</nc20:PersonBirthDate>
	</xsl:template>
	<xsl:template match="nc20:PersonStateIdentification">
			<jxdm41:PersonStateFingerprintIdentification><nc20:IdentificationID><xsl:value-of select="normalize-space(nc20:IdentificationID)"/></nc20:IdentificationID></jxdm41:PersonStateFingerprintIdentification>
	</xsl:template>

</xsl:stylesheet>
