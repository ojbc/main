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
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:ext="http://ojbc.org/IEPD/Extensions/SubscriptionSearchResults/1.0"
	xmlns:ssr="http://ojbc.org/IEPD/Exchange/SubscriptionSearchResults/1.0"
	xmlns:nc="http://niem.gov/niem/niem-core/2.0" xmlns:s="http://niem.gov/niem/structures/2.0">
		
	<xsl:output method="xml"/>			
	<xsl:param name="filterSubscriptionStatus"/>		
	<xsl:param name="currentDateTime" as="xs:dateTime"/>
	<!-- The number of days before validation due date, so a user can be warned -->
	<xsl:param name="validationDueWarningDays" as="xs:integer"/>
	<xsl:variable name="currentDateString" select="format-dateTime($currentDateTime, '[Y0001]-[M01]-[D01]')" />	 
	<xsl:variable name="currentDate" as="xs:date" select="xs:date($currentDateString)" />		
	
	<xsl:template match="/ssr:SubscriptionSearchResults">
		<ssr:SubscriptionSearchResults>
			<xsl:apply-templates select="ext:SubscriptionSearchResult" mode="copySSR"/>
			<xsl:apply-templates select="ext:SubscriptionSearchResult" mode="copyPersonNodes"/>
			<xsl:apply-templates select="ext:SubscriptionSearchResult" mode="copyContactInformationNodes"/>
			<xsl:apply-templates select="ext:SubscriptionSearchResult" mode="copyContactInformationAssociationNodes"/>
		</ssr:SubscriptionSearchResults>
	</xsl:template>
	
	<xsl:template match="ext:SubscriptionSearchResult" mode="copySSR">
		<xsl:variable name="gracePeriodStartDate" select="ext:Subscription/ext:SubscriptionGracePeriod/ext:SubscriptionGracePeriodDateRange/nc:StartDate/nc:Date"/>		
		<xsl:variable name="subjectID" select="ext:Subscription/ext:SubscriptionSubject/nc:RoleOfPersonReference/@s:ref"/>
		<xsl:variable name="subscribedEntityID" select="ext:Subscription/ext:SubscribedEntity/@s:id"/>
		<xsl:choose>
			<xsl:when test="$filterSubscriptionStatus = 'Active'">
				<xsl:if test="not($gracePeriodStartDate) or ($currentDate &lt; $gracePeriodStartDate)">
					<xsl:copy-of select="." copy-namespaces="no"/>
				</xsl:if>
			</xsl:when>
			<xsl:when test="$filterSubscriptionStatus = 'Inactive'">
				<xsl:if test="$gracePeriodStartDate and ($currentDate &gt;= $gracePeriodStartDate)">
					<xsl:copy-of select="." copy-namespaces="no"/>
				</xsl:if>
			</xsl:when>
			<xsl:when test="$filterSubscriptionStatus = 'All'">
				<xsl:copy-of select="." copy-namespaces="no"/>
			</xsl:when>
			<xsl:when test="$filterSubscriptionStatus='Expiring'">										
				<xsl:variable name="warningDate" select="xs:date($gracePeriodStartDate)  - ($validationDueWarningDays * xs:dayTimeDuration('P1D'))"/>								
				<xsl:if test="$gracePeriodStartDate and $currentDate &gt;= $warningDate and $currentDate &lt; $gracePeriodStartDate">
					 <xsl:copy-of select="." copy-namespaces="no"/>
				</xsl:if>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="ext:SubscriptionSearchResult" mode="copyPersonNodes">
		<xsl:variable name="gracePeriodStartDate" select="ext:Subscription/ext:SubscriptionGracePeriod/ext:SubscriptionGracePeriodDateRange/nc:StartDate/nc:Date"/>		
		<xsl:variable name="subjectID" select="ext:Subscription/ext:SubscriptionSubject/nc:RoleOfPersonReference/@s:ref"/>
		<xsl:variable name="subscribedEntityID" select="ext:Subscription/ext:SubscribedEntity/@s:id"/>
		<xsl:variable name="personNode" select="/ssr:SubscriptionSearchResults/ext:Person[@s:id=$subjectID]"/>
		<xsl:choose>
			<xsl:when test="$filterSubscriptionStatus = 'Active'">
				<xsl:if test="not($gracePeriodStartDate) or ($currentDate &lt; $gracePeriodStartDate)">
					<xsl:copy-of select="$personNode" copy-namespaces="no"/>
				</xsl:if>
			</xsl:when>
			<xsl:when test="$filterSubscriptionStatus = 'Inactive'">
				<xsl:if test="$gracePeriodStartDate and ($currentDate &gt;= $gracePeriodStartDate)">
					<xsl:copy-of select="$personNode" copy-namespaces="no"/>
				</xsl:if>
			</xsl:when>
			<xsl:when test="$filterSubscriptionStatus = 'All'">
				<xsl:copy-of select="$personNode" copy-namespaces="no"/>
			</xsl:when>			
			<xsl:when test="$filterSubscriptionStatus='Expiring'">
				<xsl:variable name="warningDate" select="xs:date($gracePeriodStartDate)  - ($validationDueWarningDays * xs:dayTimeDuration('P1D'))"/>
					<xsl:if test="$gracePeriodStartDate and $currentDate &gt;= $warningDate and $currentDate &lt; $gracePeriodStartDate">
						<xsl:copy-of select="$personNode" copy-namespaces="no"/>
					</xsl:if>
			</xsl:when>		
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="ext:SubscriptionSearchResult" mode="copyContactInformationNodes">
		<xsl:variable name="gracePeriodStartDate" select="ext:Subscription/ext:SubscriptionGracePeriod/ext:SubscriptionGracePeriodDateRange/nc:StartDate/nc:Date"/>	
		<xsl:variable name="subjectID" select="ext:Subscription/ext:SubscriptionSubject/nc:RoleOfPersonReference/@s:ref"/>
		<xsl:variable name="subscribedEntityID" select="ext:Subscription/ext:SubscribedEntity/@s:id"/>
		<xsl:variable name="contactInfoNode" select="/ssr:SubscriptionSearchResults/nc:ContactInformation[@s:id = /ssr:SubscriptionSearchResults/ext:SubscribedEntityContactInformationAssociation[ext:SubscribedEntityReference/@s:ref = $subscribedEntityID]/nc:ContactInformationReference/@s:ref]"/>
		<xsl:choose>
			<xsl:when test="$filterSubscriptionStatus = 'Active'">
				<xsl:if test="not($gracePeriodStartDate) or ($currentDate &lt; $gracePeriodStartDate)">
					<xsl:copy-of select="$contactInfoNode" copy-namespaces="no"/>
				</xsl:if>
			</xsl:when>
			<xsl:when test="$filterSubscriptionStatus = 'Inactive'">
				<xsl:if test="$gracePeriodStartDate and ($currentDate &gt;= $gracePeriodStartDate)">
					<xsl:copy-of select="$contactInfoNode" copy-namespaces="no"/>
				</xsl:if>
			</xsl:when>
			<xsl:when test="$filterSubscriptionStatus = 'All'">
				<xsl:copy-of select="$contactInfoNode" copy-namespaces="no"/>
			</xsl:when>		
			<xsl:when test="$filterSubscriptionStatus='Expiring'">
				<xsl:variable name="warningDate" select="xs:date($gracePeriodStartDate)  - ($validationDueWarningDays * xs:dayTimeDuration('P1D'))"/>
					<xsl:if test="$gracePeriodStartDate and $currentDate &gt;= $warningDate and $currentDate &lt; $gracePeriodStartDate">
						<xsl:copy-of select="$contactInfoNode" copy-namespaces="no"/>
					</xsl:if>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="ext:SubscriptionSearchResult" mode="copyContactInformationAssociationNodes">
		<xsl:variable name="gracePeriodStartDate" select="ext:Subscription/ext:SubscriptionGracePeriod/ext:SubscriptionGracePeriodDateRange/nc:StartDate/nc:Date"/>	
		<xsl:variable name="subjectID" select="ext:Subscription/ext:SubscriptionSubject/nc:RoleOfPersonReference/@s:ref"/>
		<xsl:variable name="subscribedEntityID" select="ext:Subscription/ext:SubscribedEntity/@s:id"/>
		<xsl:variable name="infoAssocNode" select="/ssr:SubscriptionSearchResults/ext:SubscribedEntityContactInformationAssociation[ext:SubscribedEntityReference/@s:ref = $subscribedEntityID]"/>
		<xsl:choose>
			<xsl:when test="$filterSubscriptionStatus = 'Active'">
				<xsl:if test="not($gracePeriodStartDate) or ($currentDate &lt; $gracePeriodStartDate)">
					<xsl:copy-of select="$infoAssocNode" copy-namespaces="no"/>
				</xsl:if>
			</xsl:when>
			<xsl:when test="$filterSubscriptionStatus = 'Inactive'">
				<xsl:if test="$gracePeriodStartDate and ($currentDate &gt;= $gracePeriodStartDate)">
					<xsl:copy-of select="$infoAssocNode" copy-namespaces="no"/>
				</xsl:if>
			</xsl:when>
			<xsl:when test="$filterSubscriptionStatus = 'All'">
				<xsl:copy-of select="$infoAssocNode"/>
			</xsl:when>		
			<xsl:when test="$filterSubscriptionStatus='Expiring'">
				<xsl:variable name="warningDate" select="xs:date($gracePeriodStartDate)  - ($validationDueWarningDays * xs:dayTimeDuration('P1D'))"/>
					<xsl:if test="$gracePeriodStartDate and $currentDate &gt;= $warningDate and $currentDate &lt; $gracePeriodStartDate">
						<xsl:copy-of select="$infoAssocNode" copy-namespaces="no"/>
					</xsl:if>
			</xsl:when>		
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>