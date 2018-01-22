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
<xsl:stylesheet version="1.0" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" encoding="UTF-8" indent="yes"/>
	<xsl:template match="xsd:element[@ref='nc:ActivityReference' and ../../../../@name='ActivityLocationAssociationType']">
		<xsd:element ref="nc:ActivityReference">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='nc:LocationReference' and ../../../../@name='ActivityLocationAssociationType']">
		<xsd:element ref="nc:LocationReference">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:AppellateCaseNoticeReasonText' and ../../../../@name='AppellateCaseNoticeType']">
		<xsd:element ref="j:AppellateCaseNoticeReasonText">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:AppellateCaseNotice' and ../../../../@name='AppellateCaseType']">
		<xsd:element ref="j:AppellateCaseNotice">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:AppellateCaseOriginalCase' and ../../../../@name='AppellateCaseType']">
		<xsd:element ref="j:AppellateCaseOriginalCase">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:ArrestAgency' and ../../../../@name='ArrestType']">
		<xsd:element ref="j:ArrestAgency">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:ArrestAgencyRecordIdentification' and ../../../../@name='ArrestType']">
		<xsd:element ref="j:ArrestAgencyRecordIdentification">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:ArrestBailRecommendationText' and ../../../../@name='ArrestType']">
		<xsd:element ref="j:ArrestBailRecommendationText">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:ArrestBloodAlcoholContentNumberText' and ../../../../@name='ArrestType']">
		<xsd:element ref="j:ArrestBloodAlcoholContentNumberText">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:ArrestCharge' and ../../../../@name='ArrestType']">
		<xsd:element ref="j:ArrestCharge">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:ArrestLocation' and ../../../../@name='ArrestType']">
		<xsd:element ref="j:ArrestLocation">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:ArrestOfficial' and ../../../../@name='ArrestType']">
		<xsd:element ref="j:ArrestOfficial">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:ArrestSubject' and ../../../../@name='ArrestType']">
		<xsd:element ref="j:ArrestSubject">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:ArrestWarrant' and ../../../../@name='ArrestType']">
		<xsd:element ref="j:ArrestWarrant">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:Booking' and ../../../../@name='ArrestType']">
		<xsd:element ref="j:Booking">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:BookingAgencyRecordIdentification' and ../../../../@name='BookingType']">
		<xsd:element ref="j:BookingAgencyRecordIdentification">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:CaseCharge' and ../../../../@name='CaseAugmentationType']">
		<xsd:element ref="j:CaseCharge">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:CaseCourt' and ../../../../@name='CaseAugmentationType']">
		<xsd:element ref="j:CaseCourt">
			<xsl:attribute name="minOccurs">1</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:CaseCourtEvent' and ../../../../@name='CaseAugmentationType']">
		<xsd:element ref="j:CaseCourtEvent">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:CaseDefenseAttorney' and ../../../../@name='CaseAugmentationType']">
		<xsd:element ref="j:CaseDefenseAttorney">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:CaseDefendantParty' and ../../../../@name='CaseAugmentationType']">
		<xsd:element ref="j:CaseDefendantParty">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:CaseInitiatingParty' and ../../../../@name='CaseAugmentationType']">
		<xsd:element ref="j:CaseInitiatingParty">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:CaseJudge' and ../../../../@name='CaseAugmentationType']">
		<xsd:element ref="j:CaseJudge">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:CaseLineageCase' and ../../../../@name='CaseAugmentationType']">
		<xsd:element ref="j:CaseLineageCase">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:CaseOfficial' and ../../../../@name='CaseAugmentationType']">
		<xsd:element ref="j:CaseOfficial">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:CaseOtherEntity' and ../../../../@name='CaseAugmentationType']">
		<xsd:element ref="j:CaseOtherEntity">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:CaseProsecutionAttorney' and ../../../../@name='CaseAugmentationType']">
		<xsd:element ref="j:CaseProsecutionAttorney">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:CaseRespondentAttorney' and ../../../../@name='CaseAugmentationType']">
		<xsd:element ref="j:CaseRespondentAttorney">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:CaseRespondentParty' and ../../../../@name='CaseAugmentationType']">
		<xsd:element ref="j:CaseRespondentParty">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:CaseInitiatingAttorney' and ../../../../@name='CaseAugmentationType']">
		<xsd:element ref="j:CaseInitiatingAttorney">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:CaseOfficialCaseIdentification' and ../../../../@name='CaseOfficialType']">
		<xsd:element ref="j:CaseOfficialCaseIdentification">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:CaseOfficialRoleText' and ../../../../@name='CaseOfficialType']">
		<xsd:element ref="j:CaseOfficialRoleText">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:ChargeApplicabilityText' and ../../../../@name='ChargeType']">
		<xsd:element ref="j:ChargeApplicabilityText">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:ChargeDegreeText' and ../../../../@name='ChargeType']">
		<xsd:element ref="j:ChargeDegreeText">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:ChargeDescriptionText' and ../../../../@name='ChargeType']">
		<xsd:element ref="j:ChargeDescriptionText">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:ChargeDisposition' and ../../../../@name='ChargeType']">
		<xsd:element ref="j:ChargeDisposition">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:ChargeEnhancingAllegationCharge' and ../../../../@name='ChargeType']">
		<xsd:element ref="j:ChargeEnhancingAllegationCharge">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:ChargeNCICCode' and ../../../../@name='ChargeType']">
		<xsd:element ref="j:ChargeNCICCode">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:ChargeSequenceID' and ../../../../@name='ChargeType']">
		<xsd:element ref="j:ChargeSequenceID">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:ChargeSeverityLevel' and ../../../../@name='ChargeType']">
		<xsd:element ref="j:ChargeSeverityLevel">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:ChargeSpecialAllegationText' and ../../../../@name='ChargeType']">
		<xsd:element ref="j:ChargeSpecialAllegationText">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:ChargeStatute' and ../../../../@name='ChargeType']">
		<xsd:element ref="j:ChargeStatute">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:CitationAgency' and ../../../../@name='CitationType']">
		<xsd:element ref="j:CitationAgency">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:CitationDismissalConditionIndicator' and ../../../../@name='CitationType']">
		<xsd:element ref="j:CitationDismissalConditionIndicator">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:CitationIssuingOfficial' and ../../../../@name='CitationType']">
		<xsd:element ref="j:CitationIssuingOfficial">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:CitationSubject' and ../../../../@name='CitationType']">
		<xsd:element ref="j:CitationSubject">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:CitationViolation' and ../../../../@name='CitationType']">
		<xsd:element ref="j:CitationViolation">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:CourtAppearanceDate' and ../../../../@name='CourtAppearanceType']">
		<xsd:element ref="j:CourtAppearanceDate">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:CourtEventJudge' and ../../../../@name='CourtEventType']">
		<xsd:element ref="j:CourtEventJudge">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:CourtEventSchedule' and ../../../../@name='CourtEventType']">
		<xsd:element ref="j:CourtEventSchedule">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:CourtEventSequenceID' and ../../../../@name='CourtEventType']">
		<xsd:element ref="j:CourtEventSequenceID">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:CourtOrderStatus' and ../../../../@name='CourtOrderType']">
		<xsd:element ref="j:CourtOrderStatus">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:CourtName' and ../../../../@name='CourtType']">
		<xsd:element ref="j:CourtName">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='nc:DocumentReference' and ../../../../@name='DocumentCourtAssociationType']">
		<xsd:element ref="nc:DocumentReference">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:CourtReference' and ../../../../@name='DocumentCourtAssociationType']">
		<xsd:element ref="j:CourtReference">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:DrivingAccidentSeverity' and ../../../../@name='DrivingIncidentType']">
		<xsd:element ref="j:DrivingAccidentSeverity">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:DrivingIncidentRecordedSpeedRate' and ../../../../@name='DrivingIncidentType']">
		<xsd:element ref="j:DrivingIncidentRecordedSpeedRate">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:DrivingIncidentHazMat' and ../../../../@name='DrivingIncidentType']">
		<xsd:element ref="j:DrivingIncidentHazMat">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:DrivingIncidentLaserDetectionIndicator' and ../../../../@name='DrivingIncidentType']">
		<xsd:element ref="j:DrivingIncidentLaserDetectionIndicator">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:DrivingIncidentLegalSpeedRate' and ../../../../@name='DrivingIncidentType']">
		<xsd:element ref="j:DrivingIncidentLegalSpeedRate">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:DrivingIncidentPassengerQuantityText' and ../../../../@name='DrivingIncidentType']">
		<xsd:element ref="j:DrivingIncidentPassengerQuantityText">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='nc:RoleOfPersonReference' and ../../../../@name='EnforcementOfficialType']">
		<xsd:element ref="nc:RoleOfPersonReference">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:EnforcementOfficialBadgeIdentification' and ../../../../@name='EnforcementOfficialType']">
		<xsd:element ref="j:EnforcementOfficialBadgeIdentification">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:EnforcementOfficialUnavailableSchedule' and ../../../../@name='EnforcementOfficialType']">
		<xsd:element ref="j:EnforcementOfficialUnavailableSchedule">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:EnforcementOfficialUnit' and ../../../../@name='EnforcementOfficialType']">
		<xsd:element ref="j:EnforcementOfficialUnit">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:EnforcementUnitName' and ../../../../@name='EnforcementUnitType']">
		<xsd:element ref="j:EnforcementUnitName">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:IncidentDamagedPropertyReference' and ../../../../@name='IncidentAugmentationType']">
		<xsd:element ref="j:IncidentDamagedPropertyReference">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:IncidentOfficialPresentIndicator' and ../../../../@name='IncidentAugmentationType']">
		<xsd:element ref="j:IncidentOfficialPresentIndicator">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:IncidentTrafficAccidentInvolvedIndicator' and ../../../../@name='IncidentAugmentationType']">
		<xsd:element ref="j:IncidentTrafficAccidentInvolvedIndicator">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:JudicialOfficialBarIdentification' and ../../../../@name='JudicialOfficialBarMembershipType']">
		<xsd:element ref="j:JudicialOfficialBarIdentification">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='nc:RoleOfPersonReference' and ../../../../@name='JudicialOfficialType']">
		<xsd:element ref="nc:RoleOfPersonReference">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:JudicialOfficialBarMembership' and ../../../../@name='JudicialOfficialType']">
		<xsd:element ref="j:JudicialOfficialBarMembership">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:OrganizationORIIdentification' and ../../../../@name='OrganizationAugmentationType']">
		<xsd:element ref="j:OrganizationORIIdentification">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:PersonAFISIdentification' and ../../../../@name='PersonAugmentationType']">
		<xsd:element ref="j:PersonAFISIdentification">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:PersonFBIIdentification' and ../../../../@name='PersonAugmentationType']">
		<xsd:element ref="j:PersonFBIIdentification">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:PersonPalmPrint' and ../../../../@name='PersonAugmentationType']">
		<xsd:element ref="j:PersonPalmPrint">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:PersonStateFingerprintIdentification' and ../../../../@name='PersonAugmentationType']">
		<xsd:element ref="j:PersonStateFingerprintIdentification">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='nc:PersonReference' and ../../../../@name='PersonChargeAssociationType']">
		<xsd:element ref="nc:PersonReference">
			<xsl:attribute name="minOccurs">1</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:ChargeReference' and ../../../../@name='PersonChargeAssociationType']">
		<xsd:element ref="j:ChargeReference">
			<xsl:attribute name="minOccurs">1</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:PropertyTotalDamageValue' and ../../../../@name='PropertyAugmentationType']">
		<xsd:element ref="j:PropertyTotalDamageValue">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:RegisteredOffenderIdentification' and ../../../../@name='RegisteredOffenderType']">
		<xsd:element ref="j:RegisteredOffenderIdentification">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:SentenceCharge' and ../../../../@name='SentenceType']">
		<xsd:element ref="j:SentenceCharge">
			<xsl:attribute name="minOccurs">1</xsl:attribute>
			<xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:SentenceDescriptionText' and ../../../../@name='SentenceType']">
		<xsd:element ref="j:SentenceDescriptionText">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:SentenceTerm' and ../../../../@name='SentenceType']">
		<xsd:element ref="j:SentenceTerm">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:SeverityLevelDescriptionText' and ../../../../@name='SeverityLevelType']">
		<xsd:element ref="j:SeverityLevelDescriptionText">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:SeverityLevelIdentification' and ../../../../@name='SeverityLevelType']">
		<xsd:element ref="j:SeverityLevelIdentification">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:StatuteCodeIdentification' and ../../../../@name='StatuteType']">
		<xsd:element ref="j:StatuteCodeIdentification">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:StatuteCodeSectionIdentification' and ../../../../@name='StatuteType']">
		<xsd:element ref="j:StatuteCodeSectionIdentification">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:StatuteDescriptionText' and ../../../../@name='StatuteType']">
		<xsd:element ref="j:StatuteDescriptionText">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:StatuteJurisdiction' and ../../../../@name='StatuteType']">
		<xsd:element ref="j:StatuteJurisdiction">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:StatuteLevelText' and ../../../../@name='StatuteType']">
		<xsd:element ref="j:StatuteLevelText">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:StatuteOffenseIdentification' and ../../../../@name='StatuteType']">
		<xsd:element ref="j:StatuteOffenseIdentification">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='nc:RoleOfPersonReference' and ../../../../@name='SubjectType']">
		<xsd:element ref="nc:RoleOfPersonReference">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:SubjectIdentification' and ../../../../@name='SubjectType']">
		<xsd:element ref="j:SubjectIdentification">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:SubjectSupervision' and ../../../../@name='SubjectType']">
		<xsd:element ref="j:SubjectSupervision">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:SubjectReference' and ../../../../@name='ViolatedStatuteAssociationType']">
		<xsd:element ref="j:SubjectReference">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:TermDuration' and ../../../../@name='TermType']">
		<xsd:element ref="j:TermDuration">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:VictimReference' and ../../../../@name='ViolatedStatuteAssociationType']">
		<xsd:element ref="j:VictimReference">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='nc:IncidentReference' and ../../../../@name='ViolatedStatuteAssociationType']">
		<xsd:element ref="nc:IncidentReference">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:StatuteReference' and ../../../../@name='ViolatedStatuteAssociationType']">
		<xsd:element ref="j:StatuteReference">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="xsd:element[@ref='j:WarrantExtraditionLimitation' and ../../../../@name='WarrantType']">
		<xsd:element ref="j:WarrantExtraditionLimitation">
			<xsl:attribute name="minOccurs">0</xsl:attribute>
			<xsl:attribute name="maxOccurs">1</xsl:attribute>
		</xsd:element>
	</xsl:template>
	<xsl:template match="/xsd:schema">
		<xsl:copy>
			<xsl:for-each select="@*">
				<xsl:copy/>
			</xsl:for-each>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="xsd:complexType">
		<xsl:copy>
			<xsl:for-each select="@*">
				<xsl:copy/>
			</xsl:for-each>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="xsd:simpleType">
		<xsl:copy>
			<xsl:for-each select="@*">
				<xsl:copy/>
			</xsl:for-each>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="xsd:complexContent">
		<xsl:copy>
			<xsl:for-each select="@*">
				<xsl:copy/>
			</xsl:for-each>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="xsd:extension">
		<xsl:copy>
			<xsl:for-each select="@*">
				<xsl:copy/>
			</xsl:for-each>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="xsd:restriction">
		<xsl:copy>
			<xsl:for-each select="@*">
				<xsl:copy/>
			</xsl:for-each>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="xsd:sequence">
		<xsl:copy>
			<xsl:for-each select="@*">
				<xsl:copy/>
			</xsl:for-each>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="xsd:minInclusive">
		<xsl:copy>
			<xsl:for-each select="@*">
				<xsl:copy/>
			</xsl:for-each>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="xsd:simpleContent">
		<xsl:copy-of select="."/>
	</xsl:template>
	<xsl:template match="/xsd:schema/xsd:annotation">
		<xsl:copy-of select="."/>
	</xsl:template>
	<!-- <xsl:template match="/xsd:schema/xsd:complexType">
		<xsl:copy-of select="."/>
	</xsl:template>
	<xsl:template match="/xsd:schema/xsd:simpleType">
		<xsl:copy-of select="."/>
	</xsl:template> -->
	<xsl:template match="/xsd:attribute/xsd:annotation">
		<xsl:copy-of select="."/>
	</xsl:template>
	<xsl:template match="/xsd:attributeGroup/xsd:annotation">
		<xsl:copy-of select="."/>
	</xsl:template>
	<xsl:template match="xsd:annotation"/>
	<xsl:template match="xsd:import">
		<xsl:copy>
			<xsl:attribute name="namespace"><xsl:value-of select="@namespace"/></xsl:attribute>
			<xsl:attribute name="schemaLocation"><xsl:value-of select="@schemaLocation"/></xsl:attribute>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="xsd:attribute">
		<xsl:copy-of select="."/>
	</xsl:template>
	<xsl:template match="xsd:attributeGroup">
		<xsl:copy-of select="."/>
	</xsl:template>
	<xsl:template match="xsd:element">
		<xsl:copy-of select="."/>
	</xsl:template>
</xsl:stylesheet>
