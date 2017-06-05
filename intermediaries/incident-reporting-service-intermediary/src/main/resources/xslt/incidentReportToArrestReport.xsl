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
    xmlns:j41="http://niem.gov/niem/domains/jxdm/4.1" 
    xmlns:j="http://niem.gov/niem/domains/jxdm/4.0"
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
    xmlns:ar="http://ojbc.org/IEPD/Exchange/ArrestReport/1.0"
    xmlns:oar="http://ojbc.org/IEPD/Extensions/ArrestReportStructuredPayload/1.0"
    exclude-result-prefixes="xs ir ext"
    version="2.0">
    
    <xsl:output indent="yes" method="xml"/>
    												 
    <xsl:variable name="lexsPublishMessage" select="/*/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage"/>
    <xsl:variable name="lexsDataItemPackage" select="$lexsPublishMessage/lexs:DataItemPackage"/>
    <xsl:variable name="lexsDigest" select="$lexsDataItemPackage/lexs:Digest"/>
    <xsl:variable name="lexsAssociations" select="$lexsDigest/lexsdigest:Associations"/>
    <xsl:variable name="lexsDataOwnerContactTelephone" select="$lexsDataItemPackage/lexs:PackageMetadata/lexs:DataOwnerMetadata/lexs:DataOwnerContact/nc:ContactTelephoneNumber/nc:NANPTelephoneNumber"/>
    
    <xsl:template match="/">
       <arrestReports>
       		<!-- create one arrest report per arrest subject -->
           <xsl:apply-templates select="$lexsDigest/lexsdigest:EntityPerson/lexsdigest:Person[@s:id = $lexsAssociations/lexsdigest:ArrestSubjectAssociation/nc:PersonReference/@s:ref]" mode="arrestReport"/>
       </arrestReports>
    </xsl:template>   
    
    <xsl:template match="lexsdigest:Person" mode="arrestReport">
    	<xsl:variable name="personID" select="@s:id"/>
		<xsl:variable name="arrestID" select="$lexsAssociations/lexsdigest:ArrestSubjectAssociation[nc:PersonReference/@s:ref=$personID]/nc:ActivityReference/@s:ref"/>
    	<wrapper>
    		<ar:ArrestReport>
    			<lexspd:doPublish>
    				<lexs:PublishMessageContainer>
    					<lexs:PublishMessage>
    						<xsl:copy-of select="$lexsPublishMessage/lexs:PDMessageMetadata" />
    						<xsl:copy-of select="$lexsPublishMessage/lexs:DataSubmitterMetadata" />
    						<lexs:DataItemPackage>
    							<xsl:copy-of select="$lexsDataItemPackage/lexs:PackageMetadata" />
    							<lexs:Digest>
    								<xsl:copy-of select="$lexsDigest/lexsdigest:EntityActivity[normalize-space(nc:Activity/nc:ActivityCategoryText)='Incident']" />
    								<xsl:copy-of select="$lexsDigest/lexsdigest:EntityActivity[nc:Activity/@s:id=$arrestID]" />
    								<xsl:apply-templates select="$lexsAssociations/lexsdigest:ArrestOffenseAssociation[nc:ActivityReference/@s:ref=$arrestID]" mode="offense"/>
    								<xsl:copy-of select="$lexsDigest/lexsdigest:EntityPerson[lexsdigest:Person/@s:id=$personID]" />
    								<xsl:copy-of select="$lexsDigest/lexsdigest:EntityPerson[not(j:ArrestSubject)]" />
    								<xsl:apply-templates select="$lexsDigest/lexsdigest:EntityPerson[j:ArrestSubject and lexsdigest:Person/@s:id != $personID]" mode="arrestSubjectNotOfFocus"/>
    								<xsl:copy-of select="$lexsDigest/lexsdigest:EntityVehicle" />
    								<xsl:copy-of select="$lexsDigest/lexsdigest:EntityLocation" />
    								<xsl:copy-of select="$lexsDigest/lexsdigest:EntityOrganization" />
    								<xsl:copy-of select="$lexsDigest/lexsdigest:EntityTelephoneNumber" />
    								<lexsdigest:Associations>
    									<xsl:copy-of select="$lexsAssociations/j:ActivityEnforcementOfficialAssociation" />
    									<xsl:copy-of select="$lexsAssociations/lexsdigest:ArrestOffenseAssociation[nc:ActivityReference/@s:ref=$arrestID]" />
    									<xsl:copy-of select="$lexsAssociations/lexsdigest:ArrestOfficerAssociation[nc:ActivityReference/@s:ref=$arrestID]" />
    								   	<xsl:copy-of select="$lexsAssociations/lexsdigest:ArrestSubjectAssociation[nc:ActivityReference/@s:ref=$arrestID and nc:PersonReference/@s:ref=$personID]" />
    									<xsl:copy-of select="$lexsAssociations/lexsdigest:IncidentArrestAssociation[nc:ActivityReference/@s:ref=$arrestID]" />
    								    <xsl:copy-of select="$lexsAssociations/lexsdigest:IncidentLocationAssociation" />
    									<xsl:copy-of select="$lexsAssociations/lexsdigest:IncidentSubjectPersonAssociation" />
    									<xsl:copy-of select="$lexsAssociations/lexsdigest:OffenseLocationAssociation[nc:ActivityReference/@s:ref=$lexsAssociations/lexsdigest:ArrestOffenseAssociation[nc:ActivityReference/@s:ref=$arrestID]/nc:ActivityReference/@s:ref]" />
    									<xsl:copy-of select="$lexsAssociations/lexsdigest:PersonArrestLocationAssociation[nc:PersonReference/@s:ref=$personID]" />
    									<xsl:copy-of select="$lexsAssociations/lexsdigest:PersonContactAssociation" />
    									<xsl:copy-of select="$lexsAssociations/nc:ActivityReportingOrganizationAssociation" />
    									<xsl:copy-of select="$lexsAssociations/nc:PersonAssignedUnitAssociation" />
    									<xsl:copy-of select="$lexsAssociations/nc:PersonOwnsItemAssociation" />
    									<xsl:copy-of select="$lexsAssociations/nc:ResidenceAssociation" />
    								</lexsdigest:Associations>
    							</lexs:Digest>
    							<xsl:apply-templates select="$lexsDataItemPackage/lexs:StructuredPayload">
    								<xsl:with-param name="arrestID" select="$arrestID"/>
    								<xsl:with-param name="personID" select="$personID"/>
    							</xsl:apply-templates>
    						</lexs:DataItemPackage>
    					</lexs:PublishMessage>
    				</lexs:PublishMessageContainer>
    			</lexspd:doPublish>
    		</ar:ArrestReport>
    	</wrapper>
    </xsl:template>
    <xsl:template match="lexsdigest:ArrestOffenseAssociation" mode="offense">
    	<xsl:variable name="activityID" select="nc:ActivityReference/@s:ref"/>
		<xsl:copy-of select="$lexsDigest/lexsdigest:EntityActivity[nc:Activity[normalize-space(nc:ActivityCategoryText)='Offense']/@s:id=$activityID]" />
    </xsl:template>
    <xsl:template match="lexsdigest:EntityPerson" mode="arrestSubjectNotOfFocus">
    	<lexsdigest:EntityPerson>
    		<xsl:copy-of select="lexsdigest:Metadata" />
    		<xsl:copy-of select="lexsdigest:Person" />
    		<xsl:copy-of select="j:Victim" />
    		<xsl:copy-of select="j:Suspect" />
    		<xsl:copy-of select="j:Witness" />
    	</lexsdigest:EntityPerson>
    </xsl:template>

    <xsl:template match="lexs:StructuredPayload">
    	<xsl:param name="arrestID"/>
    	<xsl:param name="personID"/>
    	<lexs:StructuredPayload>
    		<xsl:copy-of select="lexs:StructuredPayloadMetadata" />
    		<xsl:apply-templates select="ndexia:IncidentReport">
    			<xsl:with-param name="arrestID" select="$arrestID"/>
    			<xsl:with-param name="personID" select="$personID"/>
    		</xsl:apply-templates>
    		<xsl:apply-templates select="ext:IncidentReport">
    			<xsl:with-param name="arrestID" select="$arrestID"/>
    			<xsl:with-param name="personID" select="$personID"/>
    		</xsl:apply-templates>
    	</lexs:StructuredPayload>
    </xsl:template>
    <xsl:template match="ndexia:IncidentReport">
	    <xsl:param name="arrestID"/>
	    <xsl:param name="personID"/>
    	<ndexia:ArrestReport>
    		<xsl:copy-of select="ndexia:EnforcementUnit" />
    		<xsl:copy-of select="ndexia:Incident" />
    		<xsl:copy-of select="ndexia:Location" />
    		<xsl:copy-of select="ndexia:Offense[ndexia:ActivityAugmentation/lexslib:SameAsDigestReference/@lexslib:ref = $lexsAssociations/lexsdigest:ArrestOffenseAssociation[nc:ActivityReference/@s:ref = $arrestID]/nc:ActivityReference/@s:ref]" />
    		<xsl:copy-of select="ndexia:Person" />
			<xsl:copy-of select="ndexia:Subject" />
			<xsl:copy-of select="ndexia:ArrestSubject[ndexia:ArrestSubjectAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$lexsDigest/lexsdigest:EntityPerson[lexsdigest:Person/@s:id=$personID]/j:ArrestSubject/@s:id]" />
    		<xsl:copy-of select="ndexia:Victim" />
    	</ndexia:ArrestReport>
    </xsl:template>
    <xsl:template match="ext:IncidentReport">
    	<xsl:param name="arrestID"/>
	    <xsl:param name="personID"/>
	    <oar:ArrestReport>	
	    	<xsl:apply-templates select="ext:Incident"/>
	    	<xsl:apply-templates select="ext:Offense[lexslib:SameAsDigestReference/@lexslib:ref = $lexsAssociations/lexsdigest:ArrestOffenseAssociation[nc:ActivityReference/@s:ref=$arrestID]/nc:ActivityReference/@s:ref]"/>
	    </oar:ArrestReport>
    </xsl:template>
    <xsl:template match="ext:Incident">
    	<!-- TODO: Write this out but need to update the SSP first -->
    </xsl:template>
     <xsl:template match="ext:Offense">
    	<oar:Offense>
    		<xsl:apply-templates select="ext:OffenseCodeText"/>
    		<xsl:copy-of select="lexslib:SameAsDigestReference" />
    	</oar:Offense>
    </xsl:template>
    <!-- TODO: what else do we need for offense? -->
    <xsl:template match="ext:OffenseCodeText">
    	<oar:OffenseCodeText>
    		<xsl:value-of select="."/>
    	</oar:OffenseCodeText>
    </xsl:template>
</xsl:stylesheet>