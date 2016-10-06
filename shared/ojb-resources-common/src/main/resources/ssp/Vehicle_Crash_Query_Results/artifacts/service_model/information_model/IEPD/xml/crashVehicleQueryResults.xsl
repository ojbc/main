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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:vcq-res-doc="http://ojbc.org/IEPD/Exchange/VehicleCrashQueryResults/1.0"
	xmlns:me-crash-codes="http://ojbc.org/IEPD/Extensions/Maine/VehicleCrashCodes/1.0"
	xmlns:iad="http://ojbc.org/IEPD/Extensions/InformationAccessDenial/1.0"
	xmlns:qrer="http://ojbc.org/IEPD/Extensions/QueryRequestErrorReporting/1.0"
	xmlns:srm="http://ojbc.org/IEPD/Extensions/QueryResultsMetadata/1.0"
	xmlns:vcq-res-ext="http://ojbc.org/IEPD/Extensions/VehicleCrashQueryResultsExtension/1.0"
	xmlns:j="http://release.niem.gov/niem/domains/jxdm/5.1/" xmlns:nc="http://release.niem.gov/niem/niem-core/3.0/"
	xmlns:structures="http://release.niem.gov/niem/structures/3.0/"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="2.0">
	<xsl:output indent="yes" method="xml" omit-xml-declaration="yes" />
	<xsl:template match="MaineCrashReport">
		<vcq-res-doc:VehicleCrashQueryResults>
			<xsl:apply-templates select="CrashReport" />
		</vcq-res-doc:VehicleCrashQueryResults>
	</xsl:template>
	<xsl:template match="CrashReport">
		<vcq-res-ext:VehicleCrashReport>
			<xsl:apply-templates select="." mode="report" />
			<xsl:apply-templates select="." mode="crash" />
		</vcq-res-ext:VehicleCrashReport>
	</xsl:template>
	<xsl:template match="CrashReport" mode="report">
		<xsl:apply-templates select="ReportDate" />
		<xsl:apply-templates select="ReportNumber" />
		<xsl:apply-templates select="ApprovedDate" />
	</xsl:template>
	<xsl:template match="CrashReport" mode="crash">
		<j:Crash>
			<xsl:apply-templates select="CrashDate" />
			<xsl:apply-templates select="Narrative" />
			<xsl:apply-templates select="." mode="incident" />
			<xsl:apply-templates select="." mode="source" />
			<xsl:apply-templates select="." mode="workZone" />
			<xsl:apply-templates select="Units" mode="vehicle" />
			<xsl:apply-templates select="." mode="crashLocation" />
		</j:Crash>
	</xsl:template>
	<xsl:template match="CrashReport" mode="crashLocation">
		<nc:Location>
			<xsl:attribute name="structures:ref" select="generate-id(.)" />
			<xsl:apply-templates select="CityOrTown" />
		</nc:Location>
	</xsl:template>
	<xsl:template match="ReportDate">
		<nc:DocumentCreationDate>
			<xsl:call-template name="DateTime" />
		</nc:DocumentCreationDate>
	</xsl:template>
	<xsl:template match="ApprovedDate">
		<vcq-res-ext:DocumentApprovedDate>
			<xsl:call-template name="DateTime" />
		</vcq-res-ext:DocumentApprovedDate>
	</xsl:template>
	<xsl:template match="CrashReport" mode="incident">
		<j:IncidentAugmentation>
			<xsl:apply-templates select="NonVehiclePropertyDamage" />
			<xsl:apply-templates select="." mode="officer" />
			<xsl:apply-templates select="." mode="witness" />
		</j:IncidentAugmentation>
	</xsl:template>
	<xsl:template match="CrashReport" mode="source">
		<j:CrashInformationSource>
			<me-crash-codes:ReportingAgencyCode>
				<xsl:value-of select="ReportingAgency" />
			</me-crash-codes:ReportingAgencyCode>
		</j:CrashInformationSource>
	</xsl:template>
	<xsl:template match="CrashReport" mode="workZone">
		<j:CrashWorkZone>
			<xsl:apply-templates select="WorkZoneWorkersPresent" />
			<xsl:apply-templates select="WorkZonePolicePresent" />
			<xsl:apply-templates select="WorkZoneInOrNear" />
		</j:CrashWorkZone>
	</xsl:template>
	<xsl:template match="Units" mode="vehicle">
		<j:CrashVehicle>
			<nc:RoleOfItem>
				<xsl:attribute name="structures:ref" select="generate-id(.)" />
			</nc:RoleOfItem>
			<xsl:apply-templates select="Persons" mode="driver" />
			<xsl:apply-templates select="VehicleTravelDirection" />
			<xsl:apply-templates select="HazMatPlacarded" />
			<xsl:apply-templates select="ExtentOfDamage" />
			<xsl:apply-templates select="PostedSpeedLimit" />
			<xsl:apply-templates select="IsTotalDamageOverThreshold" />
			<xsl:apply-templates select="VehicleHas9orMoreSeats" />
			<xsl:apply-templates select="ExemptVehicle" />
		</j:CrashVehicle>
	</xsl:template>
	<xsl:template match="Persons" mode="driver">
		<xsl:if test="PersonType='6'">
			<j:CrashDriver>
				<nc:RoleOfPerson>
					<xsl:attribute name="structures:ref" select="generate-id(.)" />
				</nc:RoleOfPerson>
				<nc:DriverLicense>
					<xsl:attribute name="structures:ref" select="generate-id(..)" />
				</nc:DriverLicense>
				<xsl:apply-templates select="Violation1" />
				<xsl:apply-templates select="Violation2" />
				<xsl:apply-templates select="AlcoholTest" />
				<xsl:apply-templates select="DrugTest" />
				<xsl:apply-templates select="DrugTestResult" />
				<xsl:apply-templates select="AlcoholTestResultPending" />
			</j:CrashDriver>
		</xsl:if>
	</xsl:template>
	<xsl:template match="NonVehiclePropertyDamage">
		<j:IncidentDamagedItem>
			<xsl:apply-templates select="DamageDescription" />
			<xsl:apply-templates select="DamagedPropertyOwnerType" />
		</j:IncidentDamagedItem>
	</xsl:template>
	<xsl:template match="DamagedPropertyOwnerType">
		<nc:ItemOwner>
			<nc:EntityOrganization>
				<xsl:attribute name="structures:ref" select="generate-id(.)" />
			</nc:EntityOrganization>
			<nc:EntityPerson>
				<xsl:attribute name="structures:ref" select="generate-id(.)" />
			</nc:EntityPerson>
		</nc:ItemOwner>
	</xsl:template>
	<xsl:template match="CrashReport" mode="officer">
		<j:IncidentReportingOfficial>
			<nc:RoleOfPerson>
				<xsl:attribute name="structures:ref" select="generate-id(.)" />
			</nc:RoleOfPerson>
			<xsl:apply-templates select="ReportingOfficerBadgeNumber" />
		</j:IncidentReportingOfficial>
	</xsl:template>
	<xsl:template match="CrashReport" mode="witness">
		<j:IncidentWitness>
			<nc:RoleOfPerson>
				<xsl:attribute name="structures:ref" select="generate-id(..)" />
			</nc:RoleOfPerson>
		</j:IncidentWitness>
	</xsl:template>
	<xsl:template match="ReportingOfficerBadgeNumber">
		<j:EnforcementOfficialBadgeIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="." />
			</nc:IdentificationID>
		</j:EnforcementOfficialBadgeIdentification>
	</xsl:template>
	<xsl:template match="CrashDate">
		<nc:ActivityDate>
			<nc:DateTime>
				<xsl:value-of select="." />
			</nc:DateTime>
		</nc:ActivityDate>
	</xsl:template>
	<xsl:template match="Narrative">
		<j:ActivityAugmentation>
			<j:Narrative>
				<nc:CommentText>
					<xsl:value-of select="." />
				</nc:CommentText>
			</j:Narrative>
		</j:ActivityAugmentation>
	</xsl:template>
	<xsl:template match="WorkZoneWorkersPresent">
		<me-crash-codes:WorkZoneWorkersPresentCode>
			<xsl:value-of select="." />
		</me-crash-codes:WorkZoneWorkersPresentCode>
	</xsl:template>
	<xsl:template match="WorkZonePolicePresent">
		<me-crash-codes:LawEnforcementPresentAtWorkZoneCode>
			<xsl:value-of select="." />
		</me-crash-codes:LawEnforcementPresentAtWorkZoneCode>
	</xsl:template>
	<xsl:template match="WorkZoneInOrNear">
		<me-crash-codes:InOrNearWorkZoneCode>
			<xsl:value-of select="." />
		</me-crash-codes:InOrNearWorkZoneCode>
	</xsl:template>
	<xsl:template match="DamageDescription">
		<nc:ItemDescriptionText>
			<xsl:value-of select="." />
		</nc:ItemDescriptionText>
	</xsl:template>
	<xsl:template match="ReportNumber">
		<nc:DocumentIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="." />
			</nc:IdentificationID>
		</nc:DocumentIdentification>
	</xsl:template>
	<xsl:template match="Violation1">
		<vcq-res-ext:FirstViolationDescriptionText>
			<xsl:value-of select="." />
		</vcq-res-ext:FirstViolationDescriptionText>
	</xsl:template>
	<xsl:template match="Violation2">
		<vcq-res-ext:SecondViolationDescriptionText>
			<xsl:value-of select="." />
		</vcq-res-ext:SecondViolationDescriptionText>
	</xsl:template>
	<xsl:template match="AlcoholTest">
		<me-crash-codes:AlcoholTestCategoryCode>
			<xsl:value-of select="." />
		</me-crash-codes:AlcoholTestCategoryCode>
	</xsl:template>
	<xsl:template match="DrugTest">
		<me-crash-codes:DrugTestCategoryCode>
			<xsl:value-of select="." />
		</me-crash-codes:DrugTestCategoryCode>
	</xsl:template>
	<xsl:template match="DrugTestResult">
		<me-crash-codes:DrugTestResultCode>
			<xsl:value-of select="." />
		</me-crash-codes:DrugTestResultCode>
	</xsl:template>
	<xsl:template match="AlcoholTestResultPending">
		<vcq-res-ext:AlcoholTestResultsPendingIndicator>
			<xsl:value-of select="." />
		</vcq-res-ext:AlcoholTestResultsPendingIndicator>
	</xsl:template>
	<xsl:template match="VehicleTravelDirection">
		<j:TravelDirectionBeforeCrashCode>
			<xsl:value-of select="." />
		</j:TravelDirectionBeforeCrashCode>
	</xsl:template>
	<xsl:template match="HazMatPlacarded">
		<vcq-res-ext:HazmatPlacardIndicator>
			<xsl:value-of select="." />
		</vcq-res-ext:HazmatPlacardIndicator>
	</xsl:template>
	<xsl:template match="ExtentOfDamage">
		<me-crash-codes:ExtentOfDamageCode>
			<xsl:value-of select="." />
		</me-crash-codes:ExtentOfDamageCode>
	</xsl:template>
	<xsl:template match="PostedSpeedLimit">
		<me-crash-codes:PostedSpeedLimitCode>
			<xsl:value-of select="." />
		</me-crash-codes:PostedSpeedLimitCode>
	</xsl:template>
	<xsl:template match="IsTotalDamageOverThreshold">
		<vcq-res-ext:TotalDamageOverThresholdtIndicator>
			<xsl:value-of select="." />
		</vcq-res-ext:TotalDamageOverThresholdtIndicator>
	</xsl:template>
	<xsl:template match="VehicleHas9orMoreSeats">
		<vcq-res-ext:VehicleNineOrMoreSeatstIndicator>
			<xsl:value-of select="." />
		</vcq-res-ext:VehicleNineOrMoreSeatstIndicator>
	</xsl:template>
	<xsl:template match="ExemptVehicle">
		<vcq-res-ext:ExemptVehicleIndicator>
			<xsl:value-of select="." />
		</vcq-res-ext:ExemptVehicleIndicator>
	</xsl:template>
	<xsl:template match="CityOrTown">
		<nc:Address>
			<me-crash-codes:CityOrTownCode>
				<xsl:value-of select="." />
			</me-crash-codes:CityOrTownCode>
		</nc:Address>
	</xsl:template>
	<xsl:template name="DateTime">
		<nc:DateTime>
			<xsl:value-of select="." />
		</nc:DateTime>
	</xsl:template>
</xsl:stylesheet>