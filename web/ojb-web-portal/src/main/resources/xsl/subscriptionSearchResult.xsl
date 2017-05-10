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
	xmlns:ext="http://ojbc.org/IEPD/Extensions/SubscriptionSearchResults/1.0"
	xmlns:iad="http://ojbc.org/IEPD/Extensions/InformationAccessDenial/1.0"
	xmlns:intel="http://niem.gov/niem/domains/intelligence/2.1" xmlns:j="http://niem.gov/niem/domains/jxdm/4.1"
	xmlns:nc="http://niem.gov/niem/niem-core/2.0" xmlns:niem-xsd="http://niem.gov/niem/proxy/xsd/2.0"
	xmlns:p="http://ojbc.org/IEPD/Exchange/SubscriptionSearchResults/1.0"
	xmlns:s="http://niem.gov/niem/structures/2.0"
	xmlns:srer="http://ojbc.org/IEPD/Extensions/SearchRequestErrorReporting/1.0"
	xmlns:srm="http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0"
	xmlns:wsn-br="http://docs.oasis-open.org/wsn/br-2"
	xmlns:xsd="http://www.w3.org/2001/XMLSchema"
	exclude-result-prefixes="#all">

	<xsl:import href="_formatters.xsl" />
	<xsl:output method="html" encoding="UTF-8" />
	<xsl:param name="hrefBase"/>
	<xsl:param name="validateSubscriptionButton"/>	
	<xsl:param name="messageIfNoResults">You do not have any subscriptions.</xsl:param>
	<xsl:param name="subscriptionExpirationAlertPeriod">0</xsl:param>
	
	<!-- TODO:Pass these in from the controller class -->
 	<xsl:param name="arrestTopic">{http://ojbc.org/wsn/topics}:person/arrest</xsl:param>
 	<xsl:param name="rapbackTopic">{http://ojbc.org/wsn/topics}:person/rapback</xsl:param>
	<xsl:param name="incidentTopic">{http://ojbc.org/wsn/topics}:person/incident</xsl:param>
	<xsl:param name="chCycleTopic">{http://ojbc.org/wsn/topics}:person/criminalHistoryCycleTrackingIdentifierAssignment</xsl:param>
	
	<xsl:template match="/p:SubscriptionSearchResults">
		<xsl:variable name="totalCount" select="count(ext:SubscriptionSearchResult)" />
		<xsl:variable name="accessDenialReasons" select="srm:SearchResultsMetadata/iad:InformationAccessDenial" />
		<xsl:variable name="requestErrors" select="srm:SearchResultsMetadata/srer:SearchRequestError" />
		<xsl:variable name="tooManyResultsErrors" select="srm:SearchResultsMetadata/srer:SearchErrors/srer:SearchResultsExceedThresholdError" />

		<xsl:apply-templates select="$accessDenialReasons" />
		<xsl:apply-templates select="$requestErrors" />
		<xsl:apply-templates select="$tooManyResultsErrors" />

		<xsl:choose>
			<xsl:when test="($totalCount &gt; 0)">
				<xsl:call-template name="Subscriptions"/>
				<span id="subscriptionButtons">
					<xsl:if test="$validateSubscriptionButton='true'">
						<a id="validateLink" href="#" class="blueButton viewDetails" style="margin-right:6px">VALIDATE</a>
					</xsl:if>
					<a id="unsubscribeLink" href="#" class="blueButton viewDetails"><img style="margin-right:6px;" src="../static/images/Search%20Detail/icon-close.png"/>UNSUBSCRIBE</a>
				</span>    				
			</xsl:when>
			<xsl:otherwise>
				<xsl:if test="(($totalCount = 0) and not($tooManyResultsErrors) and not($accessDenialReasons) and not($requestErrors))">
					<xsl:value-of select="$messageIfNoResults" />  
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="Subscriptions">
			<table class="searchResultsTable display" id="searchResultsTable">
				<thead>
					<tr>
						<th></th><!-- For the checkboxes -->
						<th class="editButtonColumn"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></th>
						<th>MATCH CRITERIA</th>
						<th>NAME</th>
						<th>START DATE</th>
						<th>END DATE</th>
						<th>FBI SUBSCRIPTION</th>
						<th>EMAIL ADDRESS</th>
					</tr>
				</thead>
				<!-- Need to call Person first in order to sort on last name, first name -->
				<xsl:apply-templates select="/p:SubscriptionSearchResults/ext:Person">
					<xsl:sort select="nc:PersonName"/>
				</xsl:apply-templates>
			</table>
	</xsl:template>
	<xsl:template match="ext:Person">
		<xsl:variable name="position" select="position()-1"/>
		<xsl:variable name="personID" select="@s:id"/>
		
		<xsl:apply-templates select="../ext:SubscriptionSearchResult[ext:Subscription/ext:SubscriptionSubject/nc:RoleOfPersonReference/@s:ref=$personID]">
			<xsl:with-param name="pos" select="$position"/>
		</xsl:apply-templates>
	</xsl:template>
	
	<xsl:template match="ext:SubscriptionSearchResult">
		<xsl:param name="pos"/>
		<xsl:variable name="systemID" select="intel:SystemIdentifier"/>
		<xsl:variable name="subjectID" select="ext:Subscription/ext:SubscriptionSubject/nc:RoleOfPersonReference/@s:ref"/>
		<xsl:variable name="subscriptionTopic" select="ext:Subscription/wsn-br:Topic"/>
		<xsl:variable name="subjectPerson" select="../ext:Person[@s:id=$subjectID]"/>
		<xsl:variable name="subscribedEntity" select="ext:Subscription/ext:SubscribedEntity/@s:id"/>
		<xsl:variable name="subscriptionID" select="intel:SystemIdentifier/nc:IdentificationID"/>
		<xsl:variable name="reasonCode" select="ext:Subscription/ext:CriminalSubscriptionReasonCode"/>
		<xsl:variable name="subjectName">
			<xsl:choose>
				<xsl:when test="$subjectPerson/nc:PersonName[nc:PersonGivenName and nc:PersonSurName]">
					<xsl:value-of select="normalize-space($subjectPerson/nc:PersonName/nc:PersonGivenName)"/><xsl:text> </xsl:text><xsl:value-of select="normalize-space($subjectPerson/nc:PersonName/nc:PersonSurName)"/>
				</xsl:when>
				<xsl:when test="$subjectPerson/nc:PersonName[nc:PersonFullName]">
					<xsl:value-of select="normalize-space($subjectPerson/nc:PersonName/nc:PersonFullName)"/>
				</xsl:when>
			</xsl:choose>
		</xsl:variable>
				<tr>
					<td valign="middle">
						<!-- note value in json format, so ui can parse it -->
						<input type="checkbox" name="subscriptionRow" class="subscriptionCheckBox" value='{{"id":"{$subscriptionID}","topic":"{$subscriptionTopic}","reasonCode":"{$reasonCode}"}}'/>
					</td>				
					<td class="editButtonColumn"><a href="../subscriptions/editSubscription?identificationID={$subscriptionID}&amp;topic={$subscriptionTopic}" class="blueButton viewDetails" id="editSubscriptionLink{$subscriptionID}">EDIT</a></td>
					<td>
						<xsl:choose>
							<xsl:when test="ext:Subscription/wsn-br:Topic = $arrestTopic or ext:Subscription/wsn-br:Topic = $rapbackTopic">
								<b>SID:</b><xsl:text> </xsl:text><xsl:value-of select="normalize-space($subjectPerson/j:PersonAugmentation/j:PersonStateFingerprintIdentification/nc:IdentificationID)"/>
							</xsl:when>
							<xsl:when test="ext:Subscription/wsn-br:Topic = $incidentTopic">
								<b>Name:</b><xsl:text> </xsl:text><xsl:value-of select="normalize-space($subjectName)"/><br/>
								<b>DOB:</b><xsl:text> </xsl:text>
									<xsl:call-template name="formatDate">
										<xsl:with-param name="date" select="$subjectPerson/nc:PersonBirthDate/nc:Date"/>
									</xsl:call-template>
							</xsl:when>
							<xsl:when test="ext:Subscription/wsn-br:Topic = $chCycleTopic">
								<b>Name:</b><xsl:text> </xsl:text><xsl:value-of select="normalize-space($subjectName)"/><br/>
								<b>DOB:</b><xsl:text> </xsl:text>
									<xsl:call-template name="formatDate">
										<xsl:with-param name="date" select="$subjectPerson/nc:PersonBirthDate/nc:Date"/>
									</xsl:call-template>
							</xsl:when>
						</xsl:choose>
					</td>
					<td>
						<xsl:value-of select="normalize-space($subjectName)"/>
					</td>
					
										
					<td>
						<xsl:call-template name="formatDate">
							<xsl:with-param name="date" select="ext:Subscription/nc:ActivityDateRange/nc:StartDate/nc:Date"/>
						</xsl:call-template>
					</td>					
					<xsl:element name="td">
						<xsl:apply-templates select="ext:Subscription/nc:ActivityDateRange/nc:EndDate/nc:Date[normalize-space()]" mode="endDate"/>
					</xsl:element>

					<td>
						<xsl:apply-templates select="." mode="fbiSubscription"/>
					</td>		
									
					<td>
						<xsl:apply-templates select="/p:SubscriptionSearchResults/ext:SubscribedEntityContactInformationAssociation[ext:SubscribedEntityReference/@s:ref=$subscribedEntity]"/>
					</td>
				</tr>
	</xsl:template>
	
	<xsl:template match="ext:SubscriptionSearchResult" mode="fbiSubscription">
		<xsl:variable name="subscriptionRefId">
			<xsl:value-of select="ext:Subscription/@s:id"/>
		</xsl:variable>
		
		<xsl:variable name="fbiSubscriptionRefId">
			<xsl:value-of select="/p:SubscriptionSearchResults/ext:StateSubscriptionFBISubscriptionAssociation[ext:StateSubscriptionReference/@s:ref=$subscriptionRefId]
				/ext:FBISubscriptionReference/@s:ref"/>
		</xsl:variable>
		
		<xsl:choose>
			<xsl:when test="/p:SubscriptionSearchResults/ext:FBISubscription/@s:id = $fbiSubscriptionRefId">
				<xsl:text>true</xsl:text>				 
			</xsl:when>
			<xsl:otherwise><xsl:text>false</xsl:text></xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="ext:SubscribedEntityContactInformationAssociation">
		<xsl:variable name="contactInfo" select="nc:ContactInformationReference/@s:ref"/>
		<xsl:apply-templates select="/p:SubscriptionSearchResults/nc:ContactInformation[@s:id=$contactInfo]"/>
	</xsl:template>
	
	<xsl:template match="nc:ContactInformation">
		<xsl:apply-templates select="nc:ContactEmailID[.!='']"/>
	</xsl:template>
	
	<xsl:template match="nc:ContactEmailID">
		<xsl:value-of select="."/><br/>
	</xsl:template>
	
	<xsl:template match="iad:InformationAccessDenial">
		<span class="error">
			User does not meet privilege requirements to access
			<xsl:value-of select="iad:InformationAccessDenyingSystemNameText" />. To request access, contact your IT department.
		</span>
		<br />
	</xsl:template>

	<xsl:template match="srer:SearchRequestError">
		<span class="error">
			System Name: <xsl:value-of select="intel:SystemName" />, 
			Error: <xsl:value-of select="srer:ErrorText" />
		</span>
		<br />
	</xsl:template>

	<xsl:template match="srer:SearchResultsExceedThresholdError">
		<span class="error">
			System <xsl:value-of select="../intel:SystemName" /> returned too many records, please refine your criteria.
		</span>
		<br />
	</xsl:template>
	
	<xsl:template match="nc:Date" mode="endDate">
		
		<xsl:variable name="endDate" select="." as="xsd:date"/>
		<xsl:variable name="expirationAlertStartDate">
			<xsl:value-of select="$endDate - xsd:dayTimeDuration(string-join(('P', $subscriptionExpirationAlertPeriod, 'D'),''))"></xsl:value-of>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="$expirationAlertStartDate &lt;= current-date()">
				<xsl:attribute name="style">color:red</xsl:attribute>
				<xsl:call-template name="formatDate">
					<xsl:with-param name="date" select="."/> 
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>					
				<xsl:call-template name="formatDate">
					<xsl:with-param name="date" select="."/>
				</xsl:call-template>
			</xsl:otherwise>					
		</xsl:choose>					
	</xsl:template>
	
</xsl:stylesheet>
