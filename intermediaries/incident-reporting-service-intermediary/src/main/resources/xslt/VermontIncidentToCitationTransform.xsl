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
<xsl:stylesheet version="2.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ansi-nist="http://niem.gov/niem/ansi-nist/2.0" xmlns:j="http://niem.gov/niem/domains/jxdm/4.0" xmlns:nc="http://niem.gov/niem/niem-core/2.0" xmlns:s="http://niem.gov/niem/structures/2.0" xmlns:cfm="urn:oasis:names:tc:legalxml-courtfiling:schema:xsd:CoreFilingMessage-4.0" xmlns:ojb-cit-doc="http://ojbc.org/IEPD/Exchange/CitationCaseDocument/1.0" xmlns:ojb-cit-ext="http://ojbc.org/IEPD/Extensions/CitationCaseExtension/1.0" xmlns:ecf-cit="urn:oasis:names:tc:legalxml-courtfiling:schema:xsd:CitationCase-4.0" xmlns:xmime="http://www.w3.org/2005/05/xmlmime" xmlns:xop="http://www.w3.org/2004/08/xop/include" xmlns:ecf="urn:oasis:names:tc:legalxml-courtfiling:schema:xsd:CommonTypes-4.0" xmlns:ir-doc="http://ojbc.org/IEPD/Exchange/IncidentReport/1.0" xmlns:lexs="http://usdoj.gov/leisp/lexs/3.1" xmlns:lexspd="http://usdoj.gov/leisp/lexs/publishdiscover/3.1" xmlns:lexsdigest="http://usdoj.gov/leisp/lexs/digest/3.1" xmlns:lexslib="http://usdoj.gov/leisp/lexs/library/3.1" xmlns:ndexia="http://fbi.gov/cjis/N-DEx/IncidentArrest/2.1" xmlns:inc-ext="http://ojbc.org/IEPD/Extensions/IncidentReportStructuredPayload/1.0">
	<xsl:output indent="yes" method="xml" omit-xml-declaration="yes"/>
	<xsl:variable name="lexsDigest" select="/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest"/>
	<xsl:variable name="IncidentReport" select="/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:StructuredPayload/inc-ext:IncidentReport"/>
	<xsl:variable name="ndexiaReport" select="/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:StructuredPayload/ndexia:IncidentReport"/>
	<xsl:variable name="DIP" select="/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage"/>
	<xsl:template match="/">
		<cfm:CoreFilingMessage>
			<!-- The following elements are required by ECF DOWN -->
			<ecf:SendingMDELocationID>
				<nc:IdentificationID>91</nc:IdentificationID>
			</ecf:SendingMDELocationID>
			<ecf:SendingMDEProfileCode>MESSAGINGPROFILEID</ecf:SendingMDEProfileCode>
			<!-- The following elements are required by ECF UP -->
			<ojb-cit-doc:CitationCase>
				<!-- ECF Required elements below -->
				<j:CaseAugmentation>
					<j:CaseCourt/>
				</j:CaseAugmentation>
				<ecf:CaseAugmentation/>
				<ecf-cit:CitationCaseAugmentation>
					<j:Citation/>
				</ecf-cit:CitationCaseAugmentation>
				<!-- ECF Required elements above -->
				<ojb-cit-ext:CaseAugmentation>
					<ecf:PersonOrganizationAssociation>
						<nc:PersonReference>
							<xsl:attribute name="s:ref"><xsl:value-of select="generate-id($lexsDigest/lexsdigest:EntityPerson/j:EnforcementOfficial[@s:id=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:Associations/j:ActivityEnforcementOfficialAssociation/nc:PersonReference/@s:ref])"/></xsl:attribute>
						</nc:PersonReference>
						<nc:OrganizationReference>
							<xsl:attribute name="s:ref"><xsl:value-of select="generate-id($lexsDigest/lexsdigest:EntityOrganization/nc:Organization[@s:id=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:Associations/nc:PersonAssignedUnitAssociation/nc:OrganizationReference/@s:ref])"/></xsl:attribute>
						</nc:OrganizationReference>
						<ecf:EntityAssociationTypeCode/>
					</ecf:PersonOrganizationAssociation>
					<!-- Case Participant Organization -->
					<xsl:apply-templates select="$lexsDigest/lexsdigest:EntityOrganization/nc:Organization[@s:id=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:Associations/nc:ActivityInvolvedOrganizationAssociation/nc:OrganizationReference/@s:ref]
" mode="org"/>
					<xsl:apply-templates select="$lexsDigest/lexsdigest:EntityOrganization/nc:Organization[@s:id=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:StructuredPayload/inc-ext:IncidentReport/inc-ext:CitationDeliveryRecipientAssociation/nc:EntityOrganizationReference/@s:ref]" mode="org"/>
					<!-- Case Participant Person -->
					<xsl:apply-templates select="$lexsDigest/lexsdigest:EntityPerson/lexsdigest:Person[@s:id=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:Associations/nc:ImmediateFamilyAssociation/nc:PersonParentReference/@s:ref]" mode="parent"/>
					<xsl:apply-templates select="$lexsDigest/lexsdigest:EntityPerson/lexsdigest:Person[@s:id=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityPerson/j:EnforcementOfficial/nc:RoleOfPersonReference/@s:ref]" mode="officer"/>
					<xsl:apply-templates select="$lexsDigest/lexsdigest:EntityPerson/lexsdigest:Person[@s:id=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityPerson/j:IncidentSubject/nc:RoleOfPersonReference/@s:ref]" mode="subject"/>
				</ojb-cit-ext:CaseAugmentation>
				<ojb-cit-ext:CitationCaseAugmentation>
					<!-- j:Citation is an ECF required element -->
					<j:Citation/>
					<xsl:apply-templates select="$IncidentReport/inc-ext:Offense[lexslib:SameAsDigestReference/@lexslib:ref=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText='Offense']/@s:id]/inc-ext:ViolationOffenseCode" mode="charge"/>
					<xsl:apply-templates select="$lexsDigest/lexsdigest:EntityPerson/lexsdigest:Person[@s:id=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityPerson/j:IncidentSubject/nc:RoleOfPersonReference/@s:ref]" mode="incident_subject"/>
					<xsl:apply-templates select="$lexsDigest/lexsdigest:EntityDocument/nc:Document[@s:id= /ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:StructuredPayload/inc-ext:IncidentReport/inc-ext:Citation/lexslib:SameAsDigestReference/@lexslib:ref]" mode="citation"/>
					<xsl:apply-templates select="$lexsDigest/lexsdigest:EntityVehicle/nc:Vehicle[@s:id=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:Associations/nc:PersonConveyanceAssociation[nc:PersonReference/@s:ref=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityPerson[j:IncidentSubject]/lexsdigest:Person/@s:id]/nc:ConveyanceReference/@s:ref]" mode="vehicle"/>
					<ojb-cit-ext:Offense>
						<xsl:apply-templates select="$DIP/lexs:StructuredPayload" mode="offense"/>
					</ojb-cit-ext:Offense>
					<xsl:apply-templates select="$lexsDigest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText='Incident']" mode="driving_incident"/>
				</ojb-cit-ext:CitationCaseAugmentation>
				<xsl:apply-templates select="/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:StructuredPayload/inc-ext:IncidentReport/inc-ext:CitationDeliveryRecipientAssociation" mode="association"/>
				<xsl:apply-templates select="/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:Associations/nc:ImmediateFamilyAssociation" mode="association"/>
			</ojb-cit-doc:CitationCase>
			<!-- The following elements are required by ECF DOWN -->
			<cfm:FilingLeadDocument>
				<ecf:DocumentMetadata>
					<j:RegisterActionDescriptionText/>
					<ecf:FilingAttorneyID/>
					<ecf:FilingPartyID/>
				</ecf:DocumentMetadata>
				<ecf:DocumentRendition>
					<ecf:DocumentRenditionMetadata>
						<ecf:DocumentAttachment/>
					</ecf:DocumentRenditionMetadata>
				</ecf:DocumentRendition>
			</cfm:FilingLeadDocument>
			<!-- The following elements are required by ECF UP -->
		</cfm:CoreFilingMessage>
	</xsl:template>
	<xsl:template match="/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:StructuredPayload/inc-ext:IncidentReport/inc-ext:CitationDeliveryRecipientAssociation" mode="association">
		<ojb-cit-ext:CitationDeliveryRecipientAssociation>
			<ojb-cit-ext:CitationReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id($lexsDigest/lexsdigest:EntityDocument/nc:Document)"/></xsl:attribute>
			</ojb-cit-ext:CitationReference>
			<nc:EntityOrganizationReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id($lexsDigest/lexsdigest:EntityOrganization/nc:Organization[@s:id=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:StructuredPayload/inc-ext:IncidentReport/inc-ext:CitationDeliveryRecipientAssociation/nc:EntityOrganizationReference/@s:ref])"/></xsl:attribute>
			</nc:EntityOrganizationReference>
		</ojb-cit-ext:CitationDeliveryRecipientAssociation>
	</xsl:template>
	<xsl:template match="/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:Associations/nc:ImmediateFamilyAssociation" mode="association">
		<nc:ImmediateFamilyAssociation>
			<nc:PersonParentReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id($lexsDigest/lexsdigest:EntityPerson/lexsdigest:Person[@s:id=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:Associations/nc:ImmediateFamilyAssociation/nc:PersonParentReference/@s:ref])"/></xsl:attribute>
			</nc:PersonParentReference>
			<nc:PersonChildReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id($lexsDigest/lexsdigest:EntityPerson/lexsdigest:Person[@s:id=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityPerson/j:IncidentSubject/nc:RoleOfPersonReference/@s:ref])"/></xsl:attribute>
			</nc:PersonChildReference>
		</nc:ImmediateFamilyAssociation>
	</xsl:template>
	<xsl:template match="nc:Activity" mode="driving_incident">
		<ojb-cit-ext:DrivingIncident>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id()"/></xsl:attribute>
			<xsl:apply-templates select="nc:ActivityIdentification/nc:IdentificationID" mode="incident"/>
			<xsl:apply-templates select="nc:ActivityDate/nc:DateTime" mode="incident"/>
			<xsl:apply-templates select="nc:ActivityDescriptionText" mode="incident"/>
			<xsl:apply-templates select="$lexsDigest/lexsdigest:EntityLocation/nc:Location[@s:id=
/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:Associations/lexsdigest:IncidentLocationAssociation[nc:ActivityReference/@s:ref=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText='Incident']/@s:id]/nc:LocationReference/@s:ref]" mode="incident"/>
			<xsl:apply-templates select="$DIP/lexs:StructuredPayload/inc-ext:IncidentReport/inc-ext:DrivingIncident[lexslib:SameAsDigestReference/@lexslib:ref=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText='Incident']/@s:id]/j:DrivingIncidentRecordedSpeedRate" mode="incident"/>
			<xsl:apply-templates select="$DIP/lexs:StructuredPayload/inc-ext:IncidentReport/inc-ext:DrivingIncident[lexslib:SameAsDigestReference/@lexslib:ref=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText='Incident']/@s:id]/j:DrivingIncidentLegalSpeedRate" mode="incident"/>
			<xsl:apply-templates select="$DIP/lexs:StructuredPayload/inc-ext:IncidentReport/inc-ext:DrivingIncident[lexslib:SameAsDigestReference/@lexslib:ref=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText='Incident']/@s:id]/inc-ext:DrivingAccidentIndicator" mode="incident"/>
			<nc:IncidentVehicleAssociation>
				<nc:ActivityReference>
					<xsl:attribute name="s:ref"><xsl:value-of select="generate-id($lexsDigest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText='Incident'])"/></xsl:attribute>
				</nc:ActivityReference>
				<nc:ConveyanceReference>
					<xsl:attribute name="s:ref"><xsl:value-of select="generate-id($lexsDigest/lexsdigest:EntityVehicle/nc:Vehicle[@s:id=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:Associations/nc:PersonConveyanceAssociation[nc:PersonReference/@s:ref=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityPerson[j:IncidentSubject]/lexsdigest:Person/@s:id]/nc:ConveyanceReference/@s:ref])"/></xsl:attribute>
				</nc:ConveyanceReference>
			</nc:IncidentVehicleAssociation>
			<xsl:apply-templates select="$DIP/lexs:StructuredPayload/inc-ext:IncidentReport/inc-ext:DrivingIncident[lexslib:SameAsDigestReference/@lexslib:ref=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText='Incident']/@s:id]" mode="incident_aug"/>
		</ojb-cit-ext:DrivingIncident>
	</xsl:template>
	<xsl:template match="inc-ext:DrivingIncident" mode="incident_aug">
		<ojb-cit-ext:DrivingIncidentAugmentation>
			<xsl:apply-templates select="inc-ext:DrivingAccidentFatalityIndicator"/>
			<xsl:apply-templates select="inc-ext:SeatBeltViolationIndicator"/>
			<xsl:apply-templates select="inc-ext:CivilReliefActIndicator"/>
			<xsl:apply-templates select="inc-ext:CivilReliefActJuvenileIndicator"/>
			<xsl:apply-templates select="inc-ext:CivilReliefActOtherIndicator"/>
			<xsl:apply-templates select="inc-ext:CivilReliefActOtherText"/>
		</ojb-cit-ext:DrivingIncidentAugmentation>
	</xsl:template>
	<xsl:template match="inc-ext:DrivingAccidentFatalityIndicator">
		<ojb-cit-ext:DrivingAccidentFatalityIndicator>
			<xsl:value-of select="."/>
		</ojb-cit-ext:DrivingAccidentFatalityIndicator>
	</xsl:template>
	<xsl:template match="inc-ext:SeatBeltViolationIndicator">
		<ojb-cit-ext:SeatBeltViolationIndicator>
			<xsl:value-of select="."/>
		</ojb-cit-ext:SeatBeltViolationIndicator>
	</xsl:template>
	<xsl:template match="inc-ext:CivilReliefActIndicator">
		<ojb-cit-ext:CivilReliefActIndicator>
			<xsl:value-of select="."/>
		</ojb-cit-ext:CivilReliefActIndicator>
	</xsl:template>
	<xsl:template match="inc-ext:CivilReliefActJuvenileIndicator">
		<ojb-cit-ext:CivilReliefActJuvenileIndicator>
			<xsl:value-of select="."/>
		</ojb-cit-ext:CivilReliefActJuvenileIndicator>
	</xsl:template>
	<xsl:template match="inc-ext:CivilReliefActOtherIndicator">
		<ojb-cit-ext:CivilReliefActOtherIndicator>
			<xsl:value-of select="."/>
		</ojb-cit-ext:CivilReliefActOtherIndicator>
	</xsl:template>
	<xsl:template match="inc-ext:CivilReliefActOtherText">
		<ojb-cit-ext:CivilReliefActOtherText>
			<xsl:value-of select="."/>
		</ojb-cit-ext:CivilReliefActOtherText>
	</xsl:template>
	<xsl:template match="inc-ext:DrivingAccidentIndicator" mode="incident">
		<j:IncidentAugmentation>
			<j:IncidentTrafficAccidentInvolvedIndicator>
				<xsl:value-of select="."/>
			</j:IncidentTrafficAccidentInvolvedIndicator>
		</j:IncidentAugmentation>
	</xsl:template>
	<xsl:template match="j:DrivingIncidentRecordedSpeedRate" mode="incident">
		<j:DrivingIncidentRecordedSpeedRate>
			<xsl:apply-templates select="nc:MeasurePointValue" mode="speed"/>
			<xsl:apply-templates select="nc:MeasureUnitText" mode="speed"/>
		</j:DrivingIncidentRecordedSpeedRate>
	</xsl:template>
	<xsl:template match="j:DrivingIncidentLegalSpeedRate" mode="incident">
		<j:DrivingIncidentLegalSpeedRate>
			<xsl:apply-templates select="nc:MeasurePointValue" mode="speed"/>
			<xsl:apply-templates select="nc:MeasureUnitText" mode="speed"/>
		</j:DrivingIncidentLegalSpeedRate>
	</xsl:template>
	<xsl:template match="nc:MeasurePointValue" mode="speed">
		<nc:MeasureText>
			<xsl:value-of select="."/>
		</nc:MeasureText>
	</xsl:template>
	<xsl:template match="nc:MeasureUnitText" mode="speed">
		<nc:MeasureUnitText>
			<xsl:value-of select="."/>
		</nc:MeasureUnitText>
	</xsl:template>
	<xsl:template match="nc:Location" mode="incident">
		<nc:IncidentLocation>
			<xsl:apply-templates select="nc:LocationAddress/nc:StructuredAddress/nc:LocationCityName" mode="incident"/>
			<xsl:apply-templates select="$DIP/lexs:StructuredPayload/ndexia:IncidentReport/ndexia:Location[ndexia:LocationAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityLocation/nc:Location/@s:id]/nc:LocationHighway" mode="incident"/>
		</nc:IncidentLocation>
	</xsl:template>
	<xsl:template match="nc:LocationHighway" mode="incident">
		<nc:LocationHighway>
			<xsl:apply-templates select="nc:HighwayID" mode="incident"/>
			<xsl:apply-templates select="	nc:HighwayPositionText" mode="incident"/>
		</nc:LocationHighway>
	</xsl:template>
	<xsl:template match="nc:HighwayID" mode="incident">
		<nc:HighwayID>
			<xsl:value-of select="."/>
		</nc:HighwayID>
	</xsl:template>
	<xsl:template match="nc:HighwayPositionText" mode="incident">
		<nc:HighwayPositionText>
			<xsl:value-of select="."/>
		</nc:HighwayPositionText>
	</xsl:template>
	<xsl:template match="nc:LocationAddress/nc:StructuredAddress/nc:LocationCityName" mode="incident">
		<nc:LocationAddress>
			<nc:StructuredAddress>
				<nc:LocationCityName>
					<xsl:value-of select="."/>
				</nc:LocationCityName>
			</nc:StructuredAddress>
		</nc:LocationAddress>
	</xsl:template>
	<xsl:template match="nc:ActivityIdentification/nc:IdentificationID" mode="incident">
		<nc:ActivityIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="."/>
			</nc:IdentificationID>
		</nc:ActivityIdentification>
	</xsl:template>
	<xsl:template match="nc:ActivityDate/nc:DateTime" mode="incident">
		<nc:ActivityDate>
			<nc:DateTime>
				<xsl:value-of select="."/>
			</nc:DateTime>
		</nc:ActivityDate>
	</xsl:template>
	<xsl:template match="nc:ActivityDescriptionText" mode="incident">
		<nc:ActivityDescriptionText>
			<xsl:value-of select="."/>
		</nc:ActivityDescriptionText>
	</xsl:template>
	<xsl:template match="lexs:StructuredPayload" mode="offense">
		<xsl:apply-templates select="ndexia:IncidentReport/ndexia:Offense/j:Statute/j:StatuteCodeIdentification/nc:IdentificationID"/>
		<xsl:apply-templates select="inc-ext:IncidentReport/inc-ext:Offense/inc-ext:OffenseViolatedStatute/j:StatuteCodeIdentification/nc:IdentificationID"/>
		<xsl:apply-templates select="inc-ext:IncidentReport/inc-ext:Offense/inc-ext:DrivingOffensePoints"/>
		<xsl:apply-templates select="inc-ext:IncidentReport/inc-ext:Offense/inc-ext:OffenseFineAmountMinimum"/>
		<xsl:apply-templates select="inc-ext:IncidentReport/inc-ext:Offense/inc-ext:OffenseFineAmountMaximum"/>
		<xsl:apply-templates select="inc-ext:IncidentReport/inc-ext:Charge/inc-ext:RelatedCriminalChargeIndicator"/>
		<xsl:apply-templates select="inc-ext:IncidentReport/inc-ext:Offense/inc-ext:CFRStatute/j:StatuteCodeIdentification/nc:IdentificationID"/>
	</xsl:template>
	<xsl:template match="inc-ext:IncidentReport/inc-ext:Offense/inc-ext:CFRStatute/j:StatuteCodeIdentification/nc:IdentificationID">
		<ojb-cit-ext:CFRStatute>
			<j:StatuteCodeIdentification>
				<nc:IdentificationID>
					<xsl:value-of select="."/>
				</nc:IdentificationID>
			</j:StatuteCodeIdentification>
		</ojb-cit-ext:CFRStatute>
	</xsl:template>
	<xsl:template match="inc-ext:IncidentReport/inc-ext:Charge/inc-ext:RelatedCriminalChargeIndicator">
		<ojb-cit-ext:RelatedCriminalChargeIndicator>
			<xsl:value-of select="."/>
		</ojb-cit-ext:RelatedCriminalChargeIndicator>
	</xsl:template>
	<xsl:template match="inc-ext:IncidentReport/inc-ext:Offense/inc-ext:OffenseFineAmountMaximum">
		<ojb-cit-ext:OffenseFineAmountMaximum>
			<xsl:value-of select="."/>
		</ojb-cit-ext:OffenseFineAmountMaximum>
	</xsl:template>
	<xsl:template match="inc-ext:IncidentReport/inc-ext:Offense/inc-ext:OffenseFineAmountMinimum">
		<ojb-cit-ext:OffenseFineAmountMinimum>
			<xsl:value-of select="."/>
		</ojb-cit-ext:OffenseFineAmountMinimum>
	</xsl:template>
	<xsl:template match="inc-ext:IncidentReport/inc-ext:Offense/inc-ext:DrivingOffensePoints">
		<ojb-cit-ext:DrivingOffensePoints>
			<xsl:value-of select="."/>
		</ojb-cit-ext:DrivingOffensePoints>
	</xsl:template>
	<xsl:template match="ndexia:IncidentReport/ndexia:Offense/j:Statute/j:StatuteCodeIdentification/nc:IdentificationID">
		<j:Statute>
			<j:StatuteCodeIdentification>
				<nc:IdentificationID>
					<xsl:value-of select="."/>
				</nc:IdentificationID>
			</j:StatuteCodeIdentification>
		</j:Statute>
	</xsl:template>
	<xsl:template match="inc-ext:IncidentReport/inc-ext:Offense/inc-ext:OffenseViolatedStatute/j:StatuteCodeIdentification/nc:IdentificationID">
		<ojb-cit-ext:OffenseViolatedStatute>
			<j:StatuteCodeIdentification>
				<nc:IdentificationID>
					<xsl:value-of select="."/>
				</nc:IdentificationID>
			</j:StatuteCodeIdentification>
		</ojb-cit-ext:OffenseViolatedStatute>
	</xsl:template>
	<xsl:template match="nc:Organization" mode="org">
		<ojb-cit-ext:CaseParticipant>
			<ecf:EntityOrganization>
				<xsl:attribute name="s:id"><xsl:value-of select="generate-id()"/></xsl:attribute>
				<nc:OrganizationName>
					<xsl:value-of select="nc:OrganizationName"/>
				</nc:OrganizationName>
			</ecf:EntityOrganization>
			<ecf:CaseParticipantRoleCode/>
		</ojb-cit-ext:CaseParticipant>
	</xsl:template>
	<xsl:template match="lexsdigest:Person" mode="parent">
		<ojb-cit-ext:CaseParticipant>
			<ojb-cit-ext:EntityPerson>
				<xsl:attribute name="s:id"><xsl:value-of select="generate-id()"/></xsl:attribute>
				<xsl:apply-templates select="nc:PersonName"/>
				<ecf:PersonAugmentation>
					<xsl:apply-templates select="$lexsDigest/lexsdigest:EntityLocation/nc:Location[@s:id=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage//lexs:StructuredPayload/inc-ext:IncidentReport/nc:PersonContactInformationAssociation[nc:PersonReference/@s:ref=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:Associations/nc:ImmediateFamilyAssociation/nc:PersonParentReference/@s:ref]/nc:ContactInformationReference/@s:ref]" mode="parent"/>
				</ecf:PersonAugmentation>
			</ojb-cit-ext:EntityPerson>
			<ecf:CaseParticipantRoleCode/>
		</ojb-cit-ext:CaseParticipant>
	</xsl:template>
	<xsl:template match="lexsdigest:EntityVehicle/nc:Vehicle" mode="vehicle">
		<ojb-cit-ext:Vehicle>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id()"/></xsl:attribute>
			<xsl:apply-templates select="nc:VehicleColorPrimaryCode"/>
			<xsl:apply-templates select="nc:ItemModelYearDate"/>
			<xsl:apply-templates select="nc:VehicleStyleCode"/>
			<xsl:apply-templates select="nc:ConveyanceRegistrationPlateIdentification"/>
			<xsl:apply-templates select="$ndexiaReport/ndexia:Vehicle[ndexia:VehicleAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityVehicle/nc:Vehicle/@s:id]/nc:VehicleCMVIndicator"/>
			<xsl:apply-templates select="nc:VehicleIdentification"/>
			<xsl:apply-templates select="nc:VehicleMakeCode"/>
			<xsl:apply-templates select="$IncidentReport/inc-ext:Vehicle[lexslib:SameAsDigestReference/@lexslib:ref=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityVehicle/nc:Vehicle/@s:id]/inc-ext:VehicleHazardousMaterialIndicator"/>
		</ojb-cit-ext:Vehicle>
	</xsl:template>
	<xsl:template match="inc-ext:VehicleHazardousMaterialIndicator">
		<ojb-cit-ext:VehicleAugmentation>
			<ojb-cit-ext:VehicleHazardousMaterialIndicator>
				<xsl:value-of select="."/>
			</ojb-cit-ext:VehicleHazardousMaterialIndicator>
		</ojb-cit-ext:VehicleAugmentation>
	</xsl:template>
	<xsl:template match="nc:VehicleMakeCode">
		<nc:VehicleMakeCode>
			<xsl:value-of select="."/>
		</nc:VehicleMakeCode>
	</xsl:template>
	<xsl:template match="nc:VehicleIdentification">
		<nc:VehicleIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="."/>
			</nc:IdentificationID>
		</nc:VehicleIdentification>
	</xsl:template>
	<xsl:template match="nc:VehicleCMVIndicator">
		<nc:VehicleCMVIndicator>
			<xsl:value-of select="."/>
		</nc:VehicleCMVIndicator>
	</xsl:template>
	<xsl:template match="nc:VehicleColorPrimaryCode">
		<nc:VehicleColorPrimaryCode>
			<xsl:value-of select="."/>
		</nc:VehicleColorPrimaryCode>
	</xsl:template>
	<xsl:template match="nc:ItemModelYearDate">
		<nc:ItemModelYearDate>
			<xsl:value-of select="."/>
		</nc:ItemModelYearDate>
	</xsl:template>
	<xsl:template match="nc:VehicleStyleCode">
		<nc:VehicleStyleCode>
			<xsl:value-of select="."/>
		</nc:VehicleStyleCode>
	</xsl:template>
	<xsl:template match="nc:ConveyanceRegistrationPlateIdentification">
		<nc:ConveyanceRegistrationPlateIdentification>
			<xsl:apply-templates select="nc:IdentificationID" mode="plate"/>
			<xsl:apply-templates select="nc:IdentificationJurisdictionFIPS10-4Code" mode="plate"/>
		</nc:ConveyanceRegistrationPlateIdentification>
	</xsl:template>
	<xsl:template match="nc:IdentificationID" mode="plate">
		<nc:IdentificationID>
			<xsl:value-of select="."/>
		</nc:IdentificationID>
	</xsl:template>
	<xsl:template match="nc:IdentificationJurisdictionFIPS10-4Code" mode="plate">
		<j:IdentificationJurisdictionNCICLISCode>
			<xsl:value-of select="."/>
		</j:IdentificationJurisdictionNCICLISCode>
	</xsl:template>
	<xsl:template match="lexsdigest:Person" mode="officer">
		<ojb-cit-ext:CaseParticipant>
			<ecf:EntityPerson>
				<xsl:attribute name="s:id"><xsl:value-of select="generate-id()"/></xsl:attribute>
				<xsl:apply-templates select="nc:PersonName"/>
			</ecf:EntityPerson>
			<ecf:CaseParticipantRoleCode/>
		</ojb-cit-ext:CaseParticipant>
	</xsl:template>
	<xsl:template match="lexsdigest:Person" mode="subject">
		<ojb-cit-ext:CaseParticipant>
			<ojb-cit-ext:EntityPerson>
				<xsl:attribute name="s:id"><xsl:value-of select="generate-id()"/></xsl:attribute>
				<xsl:apply-templates select="nc:PersonBirthDate"/>
				<xsl:apply-templates select="$lexsDigest/lexsdigest:EntityLocation/nc:Location[@s:id=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:Associations/lexsdigest:PersonBirthLocationAssociation/nc:LocationReference/@s:ref]/nc:LocationAddress/nc:StructuredAddress/nc:LocationCityName" mode="birth"/>
				<xsl:apply-templates select="nc:PersonEyeColorCode"/>
				<xsl:apply-templates select="nc:PersonHairColorCode"/>
				<xsl:apply-templates select="nc:PersonHeightMeasure"/>
				<xsl:apply-templates select="nc:PersonName"/>
				<xsl:apply-templates select="nc:PersonSexCode"/>
				<xsl:apply-templates select="nc:PersonWeightMeasure"/>
				<xsl:apply-templates select="." mode="personAug"/>
			</ojb-cit-ext:EntityPerson>
			<ecf:CaseParticipantRoleCode/>
		</ojb-cit-ext:CaseParticipant>
	</xsl:template>
	<xsl:template match="lexsdigest:Person" mode="personAug">
		<ojb-cit-ext:PersonAugmentation>
			<nc:ContactInformation>
				<xsl:apply-templates select="$lexsDigest/lexsdigest:EntityLocation/nc:Location[@s:id=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage//lexs:StructuredPayload/inc-ext:IncidentReport/nc:PersonContactInformationAssociation[nc:PersonReference/@s:ref=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityPerson[j:IncidentSubject]/lexsdigest:Person/@s:id]/nc:ContactInformationReference/@s:ref]" mode="subject"/>
				<xsl:apply-templates select="$lexsDigest/lexsdigest:EntityTelephoneNumber/lexsdigest:TelephoneNumber/nc:FullTelephoneNumber[@s:id=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:Associations/lexsdigest:EntityTelephoneNumberAssociation[lexsdigest:TelephoneNumberHomeIndicator='true']
[nc:PersonReference/@s:ref=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityPerson[j:IncidentSubject]/lexsdigest:Person/@s:id]/lexsdigest:TelephoneNumberReference/@s:ref]"/>
				<xsl:apply-templates select="$lexsDigest/lexsdigest:EntityTelephoneNumber/lexsdigest:TelephoneNumber/nc:FullTelephoneNumber[@s:id=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:Associations/lexsdigest:EntityTelephoneNumberAssociation[lexsdigest:TelephoneNumberWorkIndicator='true']
[nc:PersonReference/@s:ref=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityPerson[j:IncidentSubject]/lexsdigest:Person/@s:id]/lexsdigest:TelephoneNumberReference/@s:ref]"/>
			</nc:ContactInformation>
			<xsl:apply-templates select="$lexsDigest/lexsdigest:EntityPerson/lexsdigest:Person[@s:id=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityPerson/j:IncidentSubject/nc:RoleOfPersonReference/@s:ref]/j:PersonAugmentation/nc:DriverLicense"/>
			<xsl:apply-templates select="/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:StructuredPayload/inc-ext:IncidentReport/inc-ext:Person/inc-ext:PersonFishAndWildlifeLicenseIdentification/nc:IdentificationID"/>
		</ojb-cit-ext:PersonAugmentation>
	</xsl:template>
	<xsl:template match="lexsdigest:Person" mode="incident_subject">
		<ecf-cit:CitationSubject>
			<nc:RoleOfPersonReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id($lexsDigest/lexsdigest:EntityPerson/lexsdigest:Person[@s:id=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityPerson/j:IncidentSubject/nc:RoleOfPersonReference/@s:ref])"/></xsl:attribute>
			</nc:RoleOfPersonReference>
			<xsl:apply-templates select="$IncidentReport/inc-ext:DrivingIncident[lexslib:SameAsDigestReference/@lexslib:ref=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText='Incident']/@s:id]/inc-ext:SubjectBloodAlcoholContentNumber" mode="BAC"/>
		</ecf-cit:CitationSubject>
	</xsl:template>
	<xsl:template match="nc:Document" mode="citation">
		<ojb-cit-ext:Citation>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id()"/></xsl:attribute>
			<xsl:apply-templates select="nc:DocumentIdentification/nc:IdentificationID" mode="citation"/>
			<xsl:apply-templates select="$IncidentReport/inc-ext:Citation[lexslib:SameAsDigestReference/@lexslib:ref=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityDocument/nc:Document/@s:id]/inc-ext:CitationServedDate/nc:Date" mode="citation"/>
			<xsl:apply-templates select="$lexsDigest/lexsdigest:EntityOrganization/nc:Organization[@s:id=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:Associations/nc:PersonAssignedUnitAssociation/nc:OrganizationReference/@s:ref]" mode="citation"/>
			<xsl:apply-templates select="$lexsDigest/lexsdigest:EntityPerson/j:EnforcementOfficial[@s:id=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:Associations/j:ActivityEnforcementOfficialAssociation/nc:PersonReference/@s:ref]" mode="officer"/>
			<xsl:apply-templates select="$IncidentReport/inc-ext:Citation[lexslib:SameAsDigestReference/@lexslib:ref=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityDocument/nc:Document/@s:id]" mode="citation_aug"/>
		</ojb-cit-ext:Citation>
	</xsl:template>
	<xsl:template match="nc:Organization" mode="citation">
		<j:CitationAgency>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id()"/></xsl:attribute>
			<xsl:apply-templates select="nc:OrganizationName" mode="citation"/>
			<xsl:apply-templates select="$ndexiaReport/ndexia:Organization[ndexia:OrganizationAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:Associations/nc:PersonAssignedUnitAssociation/nc:OrganizationReference/@s:ref]/j:OrganizationAugmentation/j:OrganizationORIIdentification/nc:IdentificationID" mode="ori"/>
		</j:CitationAgency>
	</xsl:template>
	<xsl:template match="j:EnforcementOfficial" mode="officer">
		<j:CitationIssuingOfficial>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id()"/></xsl:attribute>
			<nc:RoleOfPersonReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id($lexsDigest/lexsdigest:EntityPerson/lexsdigest:Person[@s:id=/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityPerson/j:EnforcementOfficial/nc:RoleOfPersonReference/@s:ref])"/></xsl:attribute>
			</nc:RoleOfPersonReference>
			<xsl:apply-templates select="j:EnforcementOfficialBadgeIdentification/nc:IdentificationID"/>
		</j:CitationIssuingOfficial>
	</xsl:template>
	<xsl:template match="inc-ext:Citation" mode="citation_aug">
		<ojb-cit-ext:CitationAugmentation>
			<xsl:apply-templates select="inc-ext:CitationWaiverAmount"/>
			<xsl:apply-templates select="inc-ext:CitationServedIndicator"/>
			<xsl:apply-templates select="inc-ext:CitationMailedIndicator"/>
		</ojb-cit-ext:CitationAugmentation>
	</xsl:template>
	<xsl:template match="j:EnforcementOfficialBadgeIdentification/nc:IdentificationID">
		<j:EnforcementOfficialBadgeIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="."/>
			</nc:IdentificationID>
		</j:EnforcementOfficialBadgeIdentification>
	</xsl:template>
	<xsl:template match="inc-ext:SubjectBloodAlcoholContentNumber" mode="BAC">
		<ecf-cit:PersonBloodAlcoholNumber>
			<xsl:value-of select="."/>
		</ecf-cit:PersonBloodAlcoholNumber>
	</xsl:template>
	<xsl:template match="nc:OrganizationName" mode="citation">
		<nc:OrganizationName>
			<xsl:value-of select="."/>
		</nc:OrganizationName>
	</xsl:template>
	<xsl:template match="nc:IdentificationID" mode="ori">
		<nc:OrganizationPrimaryContactInformation>
			<nc:ContactEntity>
				<ecf:EntityOrganization>
					<j:OrganizationAugmentation>
						<j:OrganizationORIIdentification>
							<nc:IdentificationID>
								<xsl:value-of select="."/>
							</nc:IdentificationID>
						</j:OrganizationORIIdentification>
					</j:OrganizationAugmentation>
				</ecf:EntityOrganization>
			</nc:ContactEntity>
		</nc:OrganizationPrimaryContactInformation>
	</xsl:template>
	<xsl:template match="inc-ext:ViolationOffenseCode" mode="charge">
		<j:CaseCharge>
			<j:ChargeStatute>
				<xsl:attribute name="s:id"><xsl:value-of select="generate-id()"/></xsl:attribute>
				<j:StatuteOffenseIdentification>
					<nc:IdentificationID>
						<xsl:value-of select="."/>
					</nc:IdentificationID>
				</j:StatuteOffenseIdentification>
			</j:ChargeStatute>
		</j:CaseCharge>
	</xsl:template>
	<!-- COMMENTS -->
	<xsl:template match="inc-ext:CitationWaiverAmount">
		<ojb-cit-ext:CitationWaiverAmount>
			<xsl:value-of select="."/>
		</ojb-cit-ext:CitationWaiverAmount>
	</xsl:template>
	<xsl:template match="inc-ext:CitationServedIndicator">
		<ojb-cit-ext:CitationServedIndicator>
			<xsl:value-of select="."/>
		</ojb-cit-ext:CitationServedIndicator>
	</xsl:template>
	<xsl:template match="inc-ext:CitationMailedIndicator">
		<ojb-cit-ext:CitationMailedIndicator>
			<xsl:value-of select="."/>
		</ojb-cit-ext:CitationMailedIndicator>
	</xsl:template>
	<xsl:template match="nc:PersonName">
		<nc:PersonName>
			<xsl:apply-templates select="nc:PersonGivenName"/>
			<xsl:apply-templates select="nc:PersonMiddleName"/>
			<xsl:apply-templates select="nc:PersonFullName"/>
			<xsl:apply-templates select="nc:PersonSurName"/>
		</nc:PersonName>
	</xsl:template>
	<xsl:template match="nc:PersonFullName">
		<nc:PersonFullName>
			<xsl:value-of select="."/>
		</nc:PersonFullName>
	</xsl:template>
	<xsl:template match="nc:PersonMiddleName">
		<nc:PersonMiddleName>
			<xsl:value-of select="."/>
		</nc:PersonMiddleName>
	</xsl:template>
	<xsl:template match="nc:PersonGivenName">
		<nc:PersonGivenName>
			<xsl:value-of select="."/>
		</nc:PersonGivenName>
	</xsl:template>
	<xsl:template match="nc:PersonSurName">
		<nc:PersonSurName>
			<xsl:value-of select="."/>
		</nc:PersonSurName>
	</xsl:template>
	<xsl:template match="nc:Location" mode="subject">
		<nc:ContactMailingAddress>
			<nc:StructuredAddress>
				<xsl:apply-templates select="nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet"/>
				<xsl:apply-templates select="nc:LocationAddress/nc:StructuredAddress/nc:LocationCityName"/>
				<xsl:apply-templates select="nc:LocationAddress/nc:StructuredAddress/nc:LocationStateUSPostalServiceCode"/>
				<xsl:apply-templates select="nc:LocationAddress/nc:StructuredAddress/nc:LocationPostalCode"/>
			</nc:StructuredAddress>
		</nc:ContactMailingAddress>
	</xsl:template>
	<xsl:template match="nc:Location" mode="parent">
		<nc:ContactInformation>
			<nc:ContactMailingAddress>
				<nc:StructuredAddress>
					<xsl:apply-templates select="nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet"/>
					<xsl:apply-templates select="nc:LocationAddress/nc:StructuredAddress/nc:LocationCityName"/>
					<xsl:apply-templates select="nc:LocationAddress/nc:StructuredAddress/nc:LocationStateUSPostalServiceCode"/>
					<xsl:apply-templates select="nc:LocationAddress/nc:StructuredAddress/nc:LocationPostalCode"/>
				</nc:StructuredAddress>
			</nc:ContactMailingAddress>
		</nc:ContactInformation>
	</xsl:template>
	<xsl:template match="nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet">
		<nc:LocationStreet>
			<nc:StreetFullText>
				<xsl:value-of select="nc:StreetFullText"/>
			</nc:StreetFullText>
		</nc:LocationStreet>
	</xsl:template>
	<xsl:template match="nc:LocationAddress/nc:StructuredAddress/nc:LocationCityName">
		<nc:LocationCityName>
			<xsl:value-of select="."/>
		</nc:LocationCityName>
	</xsl:template>
	<xsl:template match="nc:LocationAddress/nc:StructuredAddress/nc:LocationStateUSPostalServiceCode">
		<nc:LocationStateUSPostalServiceCode>
			<xsl:value-of select="."/>
		</nc:LocationStateUSPostalServiceCode>
	</xsl:template>
	<xsl:template match="nc:LocationAddress/nc:StructuredAddress/nc:LocationPostalCode">
		<nc:LocationPostalCode>
			<xsl:value-of select="."/>
		</nc:LocationPostalCode>
	</xsl:template>
	<xsl:template match="nc:PersonBirthDate">
		<nc:PersonBirthDate>
			<nc:Date>
				<xsl:value-of select="."/>
			</nc:Date>
		</nc:PersonBirthDate>
	</xsl:template>
	<xsl:template match="nc:LocationAddress/nc:StructuredAddress/nc:LocationCityName" mode="birth">
		<nc:PersonBirthLocation>
			<nc:LocationAddress>
				<nc:StructuredAddress>
					<xsl:apply-templates select="."/>
				</nc:StructuredAddress>
			</nc:LocationAddress>
		</nc:PersonBirthLocation>
	</xsl:template>
	<xsl:template match="nc:PersonEyeColorCode">
		<nc:PersonEyeColorCode>
			<xsl:value-of select="."/>
		</nc:PersonEyeColorCode>
	</xsl:template>
	<xsl:template match="nc:PersonHairColorCode">
		<nc:PersonHairColorCode>
			<xsl:value-of select="."/>
		</nc:PersonHairColorCode>
	</xsl:template>
	<xsl:template match="nc:PersonHeightMeasure">
		<nc:PersonHeightMeasure>
			<xsl:apply-templates select="nc:MeasurePointValue"/>
			<xsl:apply-templates select="nc:LengthUnitCode"/>
		</nc:PersonHeightMeasure>
	</xsl:template>
	<xsl:template match="nc:PersonWeightMeasure">
		<nc:PersonWeightMeasure>
			<xsl:apply-templates select="nc:MeasurePointValue"/>
			<xsl:apply-templates select="nc:WeightUnitCode"/>
		</nc:PersonWeightMeasure>
	</xsl:template>
	<xsl:template match="nc:MeasurePointValue">
		<nc:MeasureText>
			<xsl:value-of select="."/>
		</nc:MeasureText>
	</xsl:template>
	<xsl:template match="nc:LengthUnitCode">
		<nc:MeasureUnitText>
			<xsl:value-of select="."/>
		</nc:MeasureUnitText>
	</xsl:template>
	<xsl:template match="nc:WeightUnitCode">
		<nc:MeasureUnitText>
			<xsl:value-of select="."/>
		</nc:MeasureUnitText>
	</xsl:template>
	<xsl:template match="nc:PersonSexCode">
		<nc:PersonSexCode>
			<xsl:value-of select="."/>
		</nc:PersonSexCode>
	</xsl:template>
	<xsl:template match="nc:DriverLicense">
		<ojb-cit-ext:PersonDriverLicense>
			<xsl:apply-templates select="nc:DriverLicenseIdentification"/>
			<xsl:apply-templates select="/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:StructuredPayload/inc-ext:IncidentReport/inc-ext:DriverLicense/inc-ext:DriverLicenseCDLIndicator" mode="cdl"/>
		</ojb-cit-ext:PersonDriverLicense>
	</xsl:template>
	<xsl:template match="/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:StructuredPayload/inc-ext:IncidentReport/inc-ext:DriverLicense/inc-ext:DriverLicenseCDLIndicator" mode="cdl">
		<ojb-cit-ext:DriverLicenseCDLIndicator>
			<xsl:value-of select="."/>
		</ojb-cit-ext:DriverLicenseCDLIndicator>
	</xsl:template>
	<xsl:template match="/ir-doc:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:StructuredPayload/inc-ext:IncidentReport/inc-ext:Person/inc-ext:PersonFishAndWildlifeLicenseIdentification/nc:IdentificationID">
		<ojb-cit-ext:PersonFishAndWildlifeLicenseIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="."/>
			</nc:IdentificationID>
		</ojb-cit-ext:PersonFishAndWildlifeLicenseIdentification>
	</xsl:template>
	<xsl:template match="nc:DriverLicenseIdentification">
		<nc:DriverLicenseIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="nc:IdentificationID"/>
			</nc:IdentificationID>
			<j:IdentificationJurisdictionNCICLISCode>
				<xsl:value-of select="j:IdentificationJurisdictionNCICLISCode"/>
			</j:IdentificationJurisdictionNCICLISCode>
		</nc:DriverLicenseIdentification>
	</xsl:template>
	<xsl:template match="lexsdigest:Person" mode="CDL">
		<ojb-cit-ext:DriverLicenseCDLIndicator>
			<xsl:value-of select="inc-ext:DriverLicenseCDLIndicator"/>
		</ojb-cit-ext:DriverLicenseCDLIndicator>
	</xsl:template>
	<xsl:template match="nc:FullTelephoneNumber">
		<nc:ContactTelephoneNumber>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id()"/></xsl:attribute>
			<nc:FullTelephoneNumber>
				<nc:TelephoneNumberFullID>
					<xsl:value-of select="."/>
				</nc:TelephoneNumberFullID>
			</nc:FullTelephoneNumber>
		</nc:ContactTelephoneNumber>
	</xsl:template>
	<xsl:template match="nc:IdentificationID" mode="citation">
		<nc:ActivityIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="."/>
			</nc:IdentificationID>
		</nc:ActivityIdentification>
	</xsl:template>
	<xsl:template match="nc:Date" mode="citation">
		<nc:ActivityDate>
			<nc:Date>
				<xsl:value-of select="."/>
			</nc:Date>
		</nc:ActivityDate>
	</xsl:template>
</xsl:stylesheet>
