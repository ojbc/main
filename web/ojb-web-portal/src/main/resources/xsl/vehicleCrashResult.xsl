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
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:vcq-res-doc="http://ojbc.org/IEPD/Exchange/VehicleCrashQueryResults/1.0"
	xmlns:vcq-res-ext="http://ojbc.org/IEPD/Extensions/VehicleCrashQueryResultsExtension/1.0"
	xmlns:qrm="http://ojbc.org/IEPD/Extensions/QueryResultsMetadata/1.0"
	xmlns:iad="http://ojbc.org/IEPD/Extensions/InformationAccessDenial/1.0"
	xmlns:qrer="http://ojbc.org/IEPD/Extensions/QueryRequestErrorReporting/1.0"
	xmlns:me-crash-codes="http://ojbc.org/IEPD/Extensions/Maine/VehicleCrashCodes/1.0"
	xmlns:nc="http://release.niem.gov/niem/niem-core/3.0/" xmlns:j="http://release.niem.gov/niem/domains/jxdm/5.1/"
	xmlns:structures="http://release.niem.gov/niem/structures/3.0/"
	exclude-result-prefixes="#all">
	<xsl:import href="_formatters.xsl" />
	<xsl:output method="html" encoding="UTF-8" />
	<xsl:template match="/">
		<table class="detailsTable">
			<tr>
				<td colspan="8" class="detailsTitle"></td>
			</tr>
			<tr>
				<td class="padding0">
					<div id="warrants" style="overflow:auto; width:100%; height:auto">
						<xsl:apply-templates select="vcq-res-doc:VehicleCrashQueryResults" />
					</div>
				</td>
			</tr>
		</table>
	</xsl:template>
	<xsl:template match="vcq-res-doc:VehicleCrashQueryResults">
		<xsl:choose>
			<xsl:when
				test="qrm:QueryResultsMetadata/qrer:QueryRequestError | qrm:QueryResultsMetadata/iad:InformationAccessDenial">
				<xsl:apply-templates
					select="qrm:QueryResultsMetadata/qrer:QueryRequestError" />
				<xsl:apply-templates
					select="qrm:QueryResultsMetadata/iad:InformationAccessDenial" />
			</xsl:when>
			<xsl:otherwise>
				<script type="text/javascript"> $(function () {
					$("#vihicleCrashDetail").accordion({ heightStyle: "content",
					collapsible: true, activate: function( event, ui ) { var
					modalIframe = $("#modalIframe", parent.document);
					modalIframe.height(modalIframe.contents().find("body").height() +
					16); } } ); }); 
				</script>
				<div id="vihicleCrashDetail">
					<xsl:apply-templates select="vcq-res-ext:VehicleCrashReport" />
				</div>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="vcq-res-ext:VehicleCrashReport">
		<xsl:apply-templates select="j:Crash" mode="crashDetail"></xsl:apply-templates>
		<xsl:apply-templates select="j:Crash/j:IncidentAugmentation"
			mode="damagedItem" />
		<xsl:apply-templates select="j:Crash/j:CrashVehicle" />
		<xsl:apply-templates select="j:Crash/j:CrashWorkZone" />
		<xsl:apply-templates select="j:Crash/j:IncidentAugmentation"
			mode="witness" />
	</xsl:template>
	<xsl:template match="j:IncidentAugmentation" mode="witness">
		<h3>Witness</h3>
		<div>
			<table style="width:100%">
				<xsl:for-each select="j:IncidentWitness/nc:RoleOfPerson">
					<xsl:variable name="witnessId">
						<xsl:value-of select="@structures:ref" />
					</xsl:variable>
					<xsl:variable name="witnessLocationId">
						<xsl:value-of
							select="ancestor::vcq-res-ext:VehicleCrashReport/nc:PersonResidenceAssociation[nc:Person/@structures:ref = $witnessId]/nc:Location/@structures:ref" />
					</xsl:variable>
					<tr>
						<td class="detailsLabel">Name</td>
						<td>
							<xsl:apply-templates
								select="ancestor::vcq-res-ext:VehicleCrashReport/nc:Person[@structures:id=$witnessId]/nc:PersonName"
								mode="firstNameFirst" />
						</td>
						<td class="detailsLabel">Address</td>
						<td>
							<xsl:apply-templates
								select="ancestor::vcq-res-ext:VehicleCrashReport/nc:Location[@structures:id=$witnessLocationId]/nc:Address" />
						</td>
					</tr>
				</xsl:for-each>
			</table>
		</div>
	</xsl:template>
	<xsl:template match="nc:Address">
		<xsl:value-of select="nc:LocationStreet/nc:StreetFullText"></xsl:value-of>
		<xsl:value-of select="nc:LocationCityName" />
		<xsl:text>, </xsl:text>
		<xsl:value-of select="j:LocationStateNCICLISCode" />
		<xsl:text>, </xsl:text>
		<xsl:value-of select="nc:LocationPostalCode"></xsl:value-of>
	</xsl:template>
	<xsl:template match="j:CrashWorkZone">
		<h3>Work Zone</h3>
		<div>
			<table style="width:100%">
				<tr>
					<td class="detailsLabel">In Work Zone</td>
					<td>
						<xsl:value-of
							select="me-crash-codes:InOrNearWorkZoneCodeText" />
					</td>
					<td class="detailsLabel">Law Enforcement Present</td>
					<td>
						<xsl:value-of
							select="me-crash-codes:LawEnforcementPresentAtWorkZoneCodeText" />
					</td>
				</tr>
				<tr>
					<td class="detailsLabel">Workers Present</td>
					<td>
						<xsl:value-of select="me-crash-codes:WorkZoneWorkersPresentCodeText" />
					</td>
					<td class="detailsLabel">Presence Code</td>
					<td>
						<xsl:value-of select="me-crash-codes:InOrNearWorkZoneCodeText"></xsl:value-of>
					</td>
				</tr>
			</table>
		</div>
	</xsl:template>
	<xsl:template match="j:CrashVehicle">
		<xsl:variable name="vehicleId">
			<xsl:value-of select="nc:RoleOfItem/@structures:ref" />
		</xsl:variable>
		<xsl:variable name="vehicleNode"
			select="ancestor::vcq-res-ext:VehicleCrashReport/nc:Vehicle[@structures:id=$vehicleId]"
			as="node()" />
		<xsl:variable name="vehicleRegistrationId">
			<xsl:value-of
				select="ancestor::vcq-res-ext:VehicleCrashReport/j:ConveyanceRegistrationAssociation[nc:Item/@structures:ref=$vehicleId]/j:ItemRegistration/@structures:ref" />
		</xsl:variable>
		<xsl:variable name="insurancePolicyId">
			<xsl:value-of
				select="ancestor::vcq-res-ext:VehicleCrashReport/nc:ItemInsuranceAssociation[nc:Item/@structures:ref=$vehicleId]/nc:Insurance/@structures:ref" />
		</xsl:variable>
		<xsl:variable name="driverId">
			<xsl:value-of select="j:CrashDriver/nc:RoleOfPerson/@structures:ref" />
		</xsl:variable>
		<xsl:variable name="driverLicenseId">
			<xsl:value-of select="j:CrashDriver/j:DriverLicense/@structures:ref" />
		</xsl:variable>
		<h3>Crash Vehicle</h3>
		<div>
			<table style="width:100%">
				<tr>
					<td class="detailsLabel">Make</td>
					<td>
						<xsl:value-of
							select="ancestor::vcq-res-ext:VehicleCrashReport/nc:Vehicle[@structures:id=$vehicleId]/nc:ItemMakeName"></xsl:value-of>
					</td>
					<!-- Not used in the Appriss Crash Report -->
					<td class="detailsLabel">Model</td>
					<td>
						<xsl:value-of
							select="ancestor::vcq-res-ext:VehicleCrashReport/nc:Vehicle[@structures:id=$vehicleId]/j:VehicleModelCode"></xsl:value-of>
					</td>
				</tr>
				<tr>
					<td class="detailsLabel">Year</td>
					<td>
						<xsl:value-of
							select="ancestor::vcq-res-ext:VehicleCrashReport/nc:Vehicle[@structures:id=$vehicleId]/nc:ItemModelYearDate"></xsl:value-of>
					</td>
					<td class="detailsLabel">VIN</td>
					<td>
						<xsl:value-of
							select="ancestor::vcq-res-ext:VehicleCrashReport/nc:Vehicle[@structures:id=$vehicleId]/nc:VehicleIdentification/nc:IdentificationID" />
					</td>
				</tr>
				<tr>
					<td class="detailsLabel">Color</td>
					<td>
						<xsl:value-of
							select="ancestor::vcq-res-ext:VehicleCrashReport/nc:Vehicle[@structures:id=$vehicleId]/nc:ConveyanceColorPrimaryText" />
					</td>
					<td class="detailsLabel">Unit</td>
					<td>
						<xsl:value-of
							select="ancestor::vcq-res-ext:VehicleCrashReport/nc:Vehicle[@structures:id=$vehicleId]/vcq-res-ext:VehicleUnitIdentification" />
					</td>
				</tr>
				<tr>
					<td class="detailsLabel">Plate Number</td>
					<td>
						<xsl:value-of
							select="ancestor::vcq-res-ext:VehicleCrashReport/j:ConveyanceRegistration[@structures:id=$vehicleRegistrationId]/j:ConveyanceRegistrationPlateIdentification/nc:IdentificationID" />
					</td>
					<td class="detailsLabel">Plate Jurisdiction</td>
					<td>
						<xsl:value-of
							select="ancestor::vcq-res-ext:VehicleCrashReport/j:ConveyanceRegistration[@structures:id=$vehicleRegistrationId]/j:ConveyanceRegistrationPlateIdentification/nc:IdentificationJurisdiction/j:LocationStateNCICLISCode" />
					</td>
				</tr>
				<tr>
					<td class="detailsLabel">Insurance Policy #</td>
					<td>
						<xsl:value-of
							select="ancestor::vcq-res-ext:VehicleCrashReport/nc:Insurance[@structures:id=$insurancePolicyId]/nc:InsurancePolicyIdentification/nc:IdentificationID" />
					</td>
					<td class="detailsLabel">Insurance Carrier</td>
					<td>
						<xsl:value-of
							select="ancestor::vcq-res-ext:VehicleCrashReport/nc:Insurance[@structures:id=$insurancePolicyId]/nc:InsuranceCarrierName" />
					</td>
				</tr>
				<tr>
					<td class="detailsLabel">Hit and Run?</td>
					<td>
						<xsl:apply-templates select="vcq-res-ext:HitRunIndicator"
							mode="formatBooleanAsYesNo" />
					</td>
					<td class="detailsLabel">Extent of Damage</td>
					<td>
						<xsl:value-of select="me-crash-codes:VehicleDamageExtentCodeText"></xsl:value-of>
					</td>
				</tr>
				<tr>
					<td class="detailsLabel">Posted Speed</td>
					<td>
						<xsl:value-of select="me-crash-codes:PostedSpeedLimitCodeText" />
					</td>
					<!-- Not used in the Appriss Crash Report -->
					<td class="detailsLabel">Actual Speed</td>
					<td>
						<xsl:value-of select="j:CrashVehicleLegalSpeedRateMeasure" />
					</td>
				</tr>
				<tr>
					<td class="detailsLabel">Hazmat</td>
					<td>
						<xsl:apply-templates select="vcq-res-ext:HazmatPlacardIndicator"
							mode="formatBooleanAsYesNo" />
					</td>
				</tr>
				<tr>
					<td class="detailsLabel">Driver Name</td>
					<td>
						<xsl:apply-templates
							select="ancestor::vcq-res-ext:VehicleCrashReport/nc:Person[@structures:id=$driverId]/nc:PersonName"
							mode="firstNameFirst" />
					</td>
					<td class="detailsLabel">Driver License #</td>
					<td>
						<xsl:value-of
							select="ancestor::vcq-res-ext:VehicleCrashReport/nc:Person[@structures:id=$driverId]/j:PersonAugmentation/j:DriverLicense/j:DriverLicenseCardIdentification/nc:IdentificationID" />
					</td>
				</tr>
				<tr>
					<td class="detailsLabel">Alcohol Test Type</td>
					<td>
						<xsl:value-of
							select="ancestor::vcq-res-ext:VehicleCrashReport/nc:Person[@structures:id=$driverId]/me-crash-codes:AlcoholTestCategoryCodeText" />
					</td>
					<td class="detailsLabel">Alcohol Test Result</td>
					<td>
						<xsl:value-of
							select="ancestor::vcq-res-ext:VehicleCrashReport/j:PersonBloodAlcoholContentAssociation[nc:Person/@structures:ref = $driverId]/j:PersonBloodAlcoholContentNumberText" />
					</td>
				</tr>
				<tr>
					<td class="detailsLabel">Drug Test Type</td>
					<td>
						<xsl:value-of
							select="ancestor::vcq-res-ext:VehicleCrashReport/nc:Person[@structures:id=$driverId]/me-crash-codes:DrugTestCategoryCodeText" />
					</td>
					<td class="detailsLabel">Drug Test Result </td>
					<td>
						<xsl:value-of
							select="ancestor::vcq-res-ext:VehicleCrashReport/nc:Person[@structures:id=$driverId]/me-crash-codes:DrugTestResultCodeText" />
					</td>
				</tr>
				<tr>
					<td class="detailsLabel">Vehicle Occupants</td>
					<td>
						<xsl:apply-templates select="j:CrashVehicleOccupant" />
					</td>
					<td class="detailsLabel">School Bus</td>
					<td>
						<xsl:variable name="busCode">
							<xsl:value-of select="../j:CrashSchoolBusRelatedCode"></xsl:value-of>
						</xsl:variable>
						<xsl:if test="$busCode='0'">
							<xsl:value-of select="'No'" />
						</xsl:if>
						<xsl:if test="$busCode='1'">
							<xsl:value-of select="'Yes, School Bus Directly Involved'" />
						</xsl:if>
						<xsl:if test="$busCode='2'">
							<xsl:value-of select="'Yes, School Bus Indirectly Involved'" />
						</xsl:if>
					</td>
				</tr>
			</table>
		</div>
	</xsl:template>
	<xsl:template match="j:CrashVehicleOccupant">
		<xsl:for-each select="nc:RoleOfPerson">
			<xsl:if test="position()>1">
				<xsl:text>, </xsl:text>
			</xsl:if>
			<xsl:variable name="personId">
				<xsl:value-of select="@structures:ref"></xsl:value-of>
			</xsl:variable>
			<xsl:apply-templates
				select="ancestor::vcq-res-ext:VehicleCrashReport/nc:Person[@structures:id=$personId]/nc:PersonName
				| ancestor::vcq-res-ext:VehicleCrashReport/nc:EntityPerson[@structures:id=$personId]/nc:PersonName"
				mode="firstNameFirst" />
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="j:IncidentAugmentation" mode="damagedItem">
		<h3>Damaged Item(s)</h3>
		<div>
			<table style="width:100%">
				<xsl:apply-templates select="j:IncidentDamagedItem" />
			</table>
		</div>
	</xsl:template>
	<xsl:template match="j:IncidentDamagedItem">
		<tr>
			<td class="detailsLabel">Item</td>
			<td>
				<xsl:value-of select="nc:ItemDescriptionText" />
			</td>
			<td class="detailsLabel">Owner</td>
			<td>
				<xsl:apply-templates select="nc:ItemOwner" />
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="nc:ItemOwner">
		<xsl:for-each select="nc:EntityOrganization|nc:EntityPerson">
			<xsl:if test="position() > 1">
				<xsl:text>, </xsl:text>
			</xsl:if>
			<xsl:variable name="ownerId">
				<xsl:value-of select="./@structures:ref"></xsl:value-of>
			</xsl:variable>
			<xsl:choose>
				<xsl:when test="local-name()='EntityOrganization'">
					<xsl:value-of
						select="ancestor::vcq-res-ext:VehicleCrashReport/nc:EntityOrganization[@structures:id=$ownerId]/nc:OrganizationName" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of
						select="ancestor::vcq-res-ext:VehicleCrashReport/nc:EntityPerson[@structures:id=$ownerId]/nc:PersonName/nc:PersonFullName" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="j:Crash" mode="crashDetail">
		<h3>Crash Detail</h3>
		<div>
			<table style="width:100%">
				<tr>
					<td class="detailsLabel">DATE</td>
					<td>
						<xsl:apply-templates select="nc:ActivityDate/nc:DateTime"
							mode="formatDateTime" />
					</td>
					<td class="detailsLabel">Crash Report Number</td>
					<td>
						<xsl:value-of
							select="parent::vcq-res-ext:VehicleCrashReport/nc:DocumentIdentification/nc:IdentificationID" />
					</td>
				</tr>
				<tr>
					<td class="detailsLabel">Reporting Officer/Badge ID</td>
					<td>
						<xsl:value-of
							select="parent::vcq-res-ext:VehicleCrashReport/nc:Person[@structures:id=ancestor::vcq-res-ext:VehicleCrashReport/j:Crash/j:IncidentAugmentation/j:IncidentReportingOfficial/nc:RoleOfPerson/@structures:ref]/nc:PersonName/nc:PersonFullName" />
						/
						<xsl:value-of
							select="j:IncidentAugmentation/j:IncidentReportingOfficial/j:EnforcementOfficialBadgeIdentification/nc:IdentificationID" />
					</td>
					<td class="detailsLabel">Reporting ORI</td>
					<td>
						<xsl:value-of
							select="j:CrashInformationSource/j:OrganizationORIIdentification/nc:IdentificationID"></xsl:value-of>
					</td>
				</tr>
				<tr>
					<td class="detailsLabel">Location</td>
					<td>
						<xsl:value-of
							select="nc:Location/nc:Address/me-crash-codes:CityOrTownCodeText" />
						<xsl:text> </xsl:text>
						<xsl:value-of select="nc:Location/nc:AddressHighway/nc:HighwayFullText"></xsl:value-of>
					</td>
					<td class="detailsLabel">Coordinates</td>
					<td>
						<xsl:value-of
							select="nc:Location/nc:Location2DGeospatialCoordinate/nc:GeographicCoordinateLatitude/nc:LatitudeDegreeValue" />
						<xsl:text>, </xsl:text>
						<xsl:value-of
							select="nc:Location/nc:Location2DGeospatialCoordinate/nc:GeographicCoordinateLongitude/nc:LongitudeDegreeValue" />
					</td>
				</tr>
			</table>
		</div>
	</xsl:template>
	<xsl:template match="qrer:QueryRequestError">
		<span class="error">
			Vehicle Crash Query Result Error:
			<xsl:value-of select="qrer:ErrorText" />
		</span>
		<br />
	</xsl:template>
	<xsl:template match="iad:InformationAccessDenial">
		<span class="error">
			Access to System
			<xsl:value-of select="iad:InformationAccessDenyingSystemNameText" />
			Denied. Denied Reason:
			<xsl:value-of select="iad:InformationAccessDenialReasonText" />
		</span>
		<br />
	</xsl:template>
</xsl:stylesheet>