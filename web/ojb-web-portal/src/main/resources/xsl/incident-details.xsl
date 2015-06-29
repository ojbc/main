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
	xmlns:ir="http://ojbc.org/IEPD/Exchange/IncidentReport/1.0"
	xmlns:nc="http://niem.gov/niem/niem-core/2.0"
	xmlns:lexs="http://usdoj.gov/leisp/lexs/3.1"
	xmlns:lexspd="http://usdoj.gov/leisp/lexs/publishdiscover/3.1"
	xmlns:lexsdigest="http://usdoj.gov/leisp/lexs/digest/3.1" 
	xmlns:s="http://niem.gov/niem/structures/2.0"
	xmlns:j="http://niem.gov/niem/domains/jxdm/4.0" 
	xmlns:lexslib="http://usdoj.gov/leisp/lexs/library/3.1"
	xmlns:ndexia="http://fbi.gov/cjis/N-DEx/IncidentArrest/2.1"
	xmlns:ext="http://ojbc.org/IEPD/Extensions/IncidentReportStructuredPayload/1.0"
	xmlns:srer="http://ojbc.org/IEPD/Extensions/SearchRequestErrorReporting/1.0"
    xmlns:srm="http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0"
    xmlns:intel="http://niem.gov/niem/domains/intelligence/2.1"
	exclude-result-prefixes="#all">
	
	<xsl:import href="_formatters.xsl" />
	
	<xsl:output method="html" encoding="UTF-8" />
	
	<xsl:variable name="enforcementOfficialAssociaiton" select="/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:Associations/j:ActivityEnforcementOfficialAssociation"/>
	
	<xsl:template match="/ir:IncidentReport/srm:SearchResultsMetadata">
		<xsl:apply-templates select="srer:SearchRequestError" />
	</xsl:template>

	<xsl:template match="/ir:IncidentReport/lexspd:doPublish">
		<script type="text/javascript">
			$(function () {
				$('#incidentDetailTabs').tabs({
					activate: function( event, ui ) {
						var modalIframe = $("#modalIframe", parent.document);
						modalIframe.height(modalIframe.contents().find("body").height() + 16);
					}
				});
			});
		</script>

		<div id="incidentDetailTabs">
			<ul>
				<li>
					<a href="#detailsTab">DETAILS</a>
				</li>
				<li>
					<a href="#partyTab">PARTY(S)</a>
				</li>
				<li>
					<a href="#vehicleTab">VEHICLE(S)</a>
				</li>
				<li>
					<a href="#propertyTab">PROPERTY(S)</a>
				</li>
				<li>
					<a href="#chargeOffenseTab">CHARGE(S)/OFFENSE(S)</a>
				</li>
				<li>
					<a href="#narrativeTab">NARRATIVE</a>
				</li>
				<li>
					<a href="#documentTab">DOCUMENT(S)</a>
				</li>
			</ul>

			<div id="detailsTab">
				<xsl:call-template name="detailsTab" />
			</div>
			<div id="partyTab">
				<xsl:call-template name="partyTab" />
			</div>
			<div id="vehicleTab"> 
				<xsl:call-template name="vehicleTab" />
			</div>
			<div id="propertyTab">
				<xsl:call-template name="propertyTab" />
			</div>
			<div id="chargeOffenseTab">
				<xsl:call-template name="chargeOffenseTab" />				
			</div>
			<div id="narrativeTab"> 
				<xsl:call-template name="narrativeTab" />
			</div>
			<div id="documentTab"> </div>
		</div>
	</xsl:template>
	
	<xsl:template match="srer:SearchRequestError">
		<span class="error">System Name: <xsl:value-of select="intel:SystemName" /><br/> Error: <xsl:value-of select="srer:ErrorText"/></span><br />
	</xsl:template>

	<xsl:template name="detailsTab">
<!-- 		<xsl:variable name="officerName" select="//lexs:Digest/lexsdigest:EntityPerson[lexsdigest:Person/@s:id=//lexsdigest:Associations/j:ActivityEnforcementOfficialAssociation[nc:ActivityReference/@s:ref=//lexsdigest:EntityActivity/nc:Activity/@s:id]/nc:PersonReference/@s:ref]/lexsdigest:Person/nc:PersonName/nc:PersonFullName"/> -->
<!-- 		<xsl:variable name="officerBadge" select="//lexs:Digest/lexsdigest:EntityPerson[lexsdigest:Person/@s:id=//lexsdigest:Associations/j:ActivityEnforcementOfficialAssociation[nc:ActivityReference/@s:ref=//lexsdigest:EntityActivity/nc:Activity/@s:id]/nc:PersonReference/@s:ref]/j:EnforcementOfficial/j:EnforcementOfficialBadgeIdentification/nc:IdentificationID"/> -->
		<xsl:variable name="reportingAgency" select="//lexs:Digest/lexsdigest:EntityOrganization/nc:Organization[@s:id=//lexsdigest:Associations/nc:ActivityReportingOrganizationAssociation[nc:ActivityReference/@s:ref=//lexsdigest:EntityActivity/nc:Activity/@s:id]/nc:OrganizationReference/@s:ref]/nc:OrganizationName"/>
		<xsl:variable name="officerAgency" select="//lexsdigest:EntityOrganization/nc:Organization[@s:id=//nc:PersonAssignedUnitAssociation[nc:PersonReference/@s:ref=//lexsdigest:EntityPerson/lexsdigest:Person[@s:id=../j:EnforcementOfficial/nc:RoleOfPersonReference/@s:ref]/@s:id]/nc:OrganizationReference/@s:ref]/nc:OrganizationName"/>
		<xsl:variable name="incidentStructuredLocation" select="//lexs:Digest/lexsdigest:EntityLocation/nc:Location[@s:id=//lexsdigest:Associations/lexsdigest:IncidentLocationAssociation[nc:ActivityReference/@s:ref=//lexsdigest:EntityActivity/nc:Activity/@s:id]/nc:LocationReference/@s:ref]/nc:LocationAddress/nc:StructuredAddress" />
		<table>
			<tr>
				<td class="incidentLabel">Case Number:</td>
				<td>
					<xsl:value-of select="/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:StructuredPayload/ndexia:IncidentReport/ndexia:Incident/ndexia:IncidentAugmentation/ndexia:IncidentCaseNumberText" />
				</td>
				<td class="incidentLabel">Officer:</td>
				<td>
<!-- 					<xsl:value-of select="$officerName"/> -->
<!-- 					<xsl:if test="$officerBadge"> -->
<!-- 						<xsl:if test="$officerName"><xsl:text>, </xsl:text></xsl:if> -->
<!-- 						<xsl:text>Badge #: </xsl:text><xsl:value-of select="$officerBadge" /> -->
<!-- 					</xsl:if> -->
					<xsl:apply-templates select="$enforcementOfficialAssociaiton"/>
				</td>
			</tr>
			<tr>
				<td class="incidentLabel"> Type:</td>
				<td><xsl:value-of select="//ext:IncidentReport/ext:Incident/ext:IncidentCategoryCode"/></td>
				<td class="incidentLabel"></td>
				<td></td>
			</tr>
			<tr>
				<td class="incidentLabel"> Start Date/Time:</td>
				<td>
					<xsl:variable name="startDate" select="//lexs:Digest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText='Incident']/nc:ActivityDateRange/nc:StartDate/nc:DateTime" />
					<xsl:value-of select="replace($startDate,'T',' ')" />
				</td>
				<td class="incidentLabel">End Date/Time:</td>
				<td>
					<xsl:variable name="endDate" select="//lexs:Digest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText='Incident']/nc:ActivityDateRange/nc:EndDate/nc:DateTime" />
					<xsl:value-of select="replace($endDate,'T',' ')" />
				</td>
			</tr>
			<tr>
				<td class="incidentLabel">Agency:</td>
				<td>
					<xsl:choose>
						<xsl:when test="$officerAgency">
							<xsl:value-of select="$officerAgency"/>
						</xsl:when>
						<xsl:when test="$reportingAgency">
							<xsl:value-of select="$reportingAgency"/>
						</xsl:when>
					</xsl:choose>
				</td>
				<td class="incidentLabel">Location:</td>
				<td>
					<xsl:choose>
						<xsl:when test="$incidentStructuredLocation/nc:LocationStreet/nc:StreetFullText[.!='']">
							<xsl:value-of select="concat($incidentStructuredLocation/nc:LocationStreet/nc:StreetFullText,' ',$incidentStructuredLocation/nc:LocationCityName,',', $incidentStructuredLocation/nc:LocationStateName,' ',$incidentStructuredLocation/nc:LocationPostalCode)"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="concat($incidentStructuredLocation/nc:LocationStreet/nc:StreetNumberText,' ', $incidentStructuredLocation/nc:LocationStreet/nc:StreetName,' ', $incidentStructuredLocation/nc:LocationCityName,',', $incidentStructuredLocation/nc:LocationStateName,' ',$incidentStructuredLocation/nc:LocationPostalCode)"/>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</table>
	</xsl:template>

	<xsl:template name="partyTab">
		<table>
			<tr>
				<td class="incidentLabel">Role</td>
				<td class="incidentLabel">Name</td>
				<td class="incidentLabel">DOB</td>
				<td class="incidentLabel">Race</td>
				<td class="incidentLabel">Gender</td>
			</tr>
			<xsl:apply-templates
				select="//lexs:Digest/lexsdigest:EntityPerson[j:ArrestSubject/nc:RoleOfPersonReference/@s:ref=lexsdigest:Person/@s:id]/lexsdigest:Person" />
			<xsl:apply-templates
				select="//lexs:Digest/lexsdigest:EntityPerson[j:Victim/nc:RoleOfPersonReference/@s:ref=lexsdigest:Person/@s:id]/lexsdigest:Person" />
			<xsl:apply-templates
				select="//lexs:Digest/lexsdigest:EntityPerson[j:Witness/nc:RoleOfPersonReference/@s:ref=lexsdigest:Person/@s:id]/lexsdigest:Person" />
			<xsl:apply-templates
				select="//lexs:Digest/lexsdigest:EntityPerson[j:IncidentSubject/nc:RoleOfPersonReference/@s:ref=lexsdigest:Person/@s:id]/lexsdigest:Person" />
			<xsl:apply-templates
				select="//lexs:Digest/lexsdigest:EntityPerson[j:Suspect/nc:RoleOfPersonReference/@s:ref=lexsdigest:Person/@s:id]/lexsdigest:Person" />
			<xsl:apply-templates select="//lexs:Digest/lexsdigest:EntityPerson[lexsdigest:Person/@s:id=//nc:ActivityInvolvedPersonAssociation[nc:PersonActivityInvolvementText='Complainant']/nc:PersonReference/@s:ref]/lexsdigest:Person" />
			<xsl:apply-templates select="//lexs:Digest/lexsdigest:EntityPerson[lexsdigest:OtherInvolvedPerson/nc:RoleOfPersonReference/@s:ref=lexsdigest:Person/@s:id]/lexsdigest:Person" mode="involvedPersons"/>
		</table>
	</xsl:template>
	
	<xsl:template match="//lexs:Digest/lexsdigest:EntityPerson/lexsdigest:Person" mode="involvedPersons">
		<xsl:variable name="id" select="@s:id"/>
		<xsl:variable name="involvedRole" select="//nc:ActivityInvolvedPersonAssociation[nc:PersonReference/@s:ref=$id]/nc:PersonActivityInvolvementText"/>
		<xsl:call-template name="partyRow">
			<xsl:with-param name="role"><xsl:value-of select="$involvedRole"/></xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template
		match="//lexs:Digest/lexsdigest:EntityPerson[j:Victim/nc:RoleOfPersonReference/@s:ref=lexsdigest:Person/@s:id]/lexsdigest:Person">
		<xsl:call-template name="partyRow">
			<xsl:with-param name="role">Victim</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template
		match="//lexs:Digest/lexsdigest:EntityPerson[j:Witness/nc:RoleOfPersonReference/@s:ref=lexsdigest:Person/@s:id]/lexsdigest:Person">
		<xsl:call-template name="partyRow">
			<xsl:with-param name="role">Witness</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template
		match="//lexs:Digest/lexsdigest:EntityPerson[j:IncidentSubject/nc:RoleOfPersonReference/@s:ref=lexsdigest:Person/@s:id]/lexsdigest:Person">
		<xsl:call-template name="partyRow">
			<xsl:with-param name="role">Incident Subject</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template
		match="//lexs:Digest/lexsdigest:EntityPerson[j:ArrestSubject/nc:RoleOfPersonReference/@s:ref=lexsdigest:Person/@s:id]/lexsdigest:Person">
		<xsl:call-template name="partyRow">
			<xsl:with-param name="role">Arrest Subject</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template
		match="//lexs:Digest/lexsdigest:EntityPerson[j:Suspect/nc:RoleOfPersonReference/@s:ref=lexsdigest:Person/@s:id]/lexsdigest:Person">
		<xsl:call-template name="partyRow">
			<xsl:with-param name="role">Suspect</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template
		match="//lexs:Digest/lexsdigest:EntityPerson[lexsdigest:Person/@s:id=//nc:ActivityInvolvedPersonAssociation[nc:PersonActivityInvolvementText='Complainant']/nc:PersonReference/@s:ref]/lexsdigest:Person">
		<xsl:call-template name="partyRow">
			<xsl:with-param name="role">Complainant</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="partyRow">
		<xsl:param name="role" />
		<xsl:variable name="id" select="@s:id"/>
		<tr>
			<td>
				<xsl:value-of select="$role" />
			</td>
			<td>
				<xsl:value-of
					select="nc:PersonName/nc:PersonGivenName | nc:PersonName/nc:PersonSurName" />
			</td>
			<td>
				<xsl:variable name="dateOfBirth" select="nc:PersonBirthDate/nc:Date" />
				<xsl:value-of
					select="concat(substring($dateOfBirth,6,2),'-',substring($dateOfBirth,9,2),'-',substring($dateOfBirth,3,2))" />
			</td>
			<td>
				<xsl:choose>
					<xsl:when test="//ndexia:Person/ndexia:PersonAugmentation[lexslib:SameAsDigestReference/@lexslib:ref=$id]/ndexia:PersonRaceCode">
						<xsl:value-of select="//ndexia:Person/ndexia:PersonAugmentation[lexslib:SameAsDigestReference/@lexslib:ref=$id]/ndexia:PersonRaceCode"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="formatRace" >
							<xsl:with-param name="raceCode" select="nc:PersonRaceText" />
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td>
				<xsl:call-template name="formatSex">
				<xsl:with-param name="sexCode" select="nc:PersonSexCode" />
				</xsl:call-template>
			</td>
		</tr>
	</xsl:template>
	
	<xsl:template name="vehicleTab">
		<table>
			<tr>
				<td class="incidentLabel">Type</td>
				<td class="incidentLabel">Make</td>
				<td class="incidentLabel">Model</td>
				<td class="incidentLabel">Year</td>
				<td class="incidentLabel">Color</td>
				<td class="incidentLabel">Plate #</td>
				<td class="incidentLabel">VIN</td>
			</tr>
			<xsl:apply-templates select="//lexsdigest:EntityVehicle/nc:Vehicle" />
		</table>
	</xsl:template>
	
	<xsl:template match="//lexsdigest:EntityVehicle/nc:Vehicle" >
		<xsl:variable name="vehicleID" select="@s:id"/>
		<xsl:variable name="ndexStyleCode" select="//lexs:StructuredPayload/ndexia:IncidentReport/ndexia:Vehicle/ndexia:VehicleAugmentation[lexslib:SameAsDigestReference/@lexslib:ref=$vehicleID]/ndexia:VehicleStyleCategoryCode"/>
		<tr>
			<td>
				<xsl:choose>
					<xsl:when test="$ndexStyleCode[.!='']">
						<xsl:value-of select="$ndexStyleCode"/>
					</xsl:when>
					<xsl:when test="nc:ItemCategoryText[. !='']">
						<xsl:value-of select="nc:ItemCategoryText"/>
					</xsl:when>
				</xsl:choose>
			</td>
			<td>
				<xsl:choose>
					<xsl:when test="nc:VehicleMakeCode[.!='']">
						<xsl:value-of select="nc:VehicleMakeCode" />
					</xsl:when>
					<xsl:when test="nc:ItemMakeName[.!='']">
						<xsl:value-of select="nc:ItemMakeName" />
					</xsl:when>
				</xsl:choose>
			</td>
			<td>
				<xsl:choose>
					<xsl:when test="nc:VehicleModelCode[.!='']">
						<xsl:value-of select="nc:VehicleModelCode" />
					</xsl:when>
					<xsl:when test="nc:ItemModelName[.!='']">
						<xsl:value-of select="nc:ItemModelName" />
					</xsl:when>
				</xsl:choose>
			</td>
			<td><xsl:value-of select="nc:ItemModelYearDate" /></td>
			<td><xsl:value-of select="nc:ItemColorDescriptionText" /></td>
			<td><xsl:value-of select="nc:ConveyanceRegistrationPlateIdentification/nc:IdentificationID" /></td>
			<td><xsl:value-of select="nc:VehicleIdentification/nc:IdentificationID" /></td>
		</tr>
	</xsl:template>
	<xsl:template name="propertyTab">
		<table>
			<tr>
				<td class="incidentLabel">Type</td>
				<td class="incidentLabel">Make/Brand</td>
				<td class="incidentLabel">Model</td>
				<td class="incidentLabel">Serial #</td>
				<td class="incidentLabel">Quantity</td>
				<td class="incidentLabel">Value</td>
				<td class="incidentLabel">Description</td>
			</tr>
			<xsl:apply-templates select="//lexsdigest:EntityTangibleItem/nc:TangibleItem" />
		</table>
	</xsl:template>
	
	<xsl:template match="//lexsdigest:EntityTangibleItem/nc:TangibleItem">
		<xsl:variable name="tangibleItemID" select="@s:id"/>
		<tr>
			<td><xsl:value-of select="nc:ItemCategoryText"/></td>
			<td><xsl:value-of select="nc:ItemMakeName"/></td>
			<td><xsl:value-of select="nc:ItemModelName"/></td>
			<td><xsl:value-of select="nc:ItemSerialIdentification/nc:IdentificationID"/></td>
			<td><xsl:value-of select="//ndexia:IncidentReport/ndexia:TangibleItem/ndexia:TangibleItemAugmentation[lexslib:SameAsDigestReference/@lexslib:ref=$tangibleItemID]/ndexia:ItemQuantityStatusValue/nc:ItemQuantity"/></td>
			<td><xsl:value-of select="//ndexia:IncidentReport/ndexia:TangibleItem/ndexia:TangibleItemAugmentation[lexslib:SameAsDigestReference/@lexslib:ref=$tangibleItemID]/ndexia:ItemQuantityStatusValue/ndexia:ItemValue/nc:ItemValueAmount"/></td>
			<td><xsl:value-of select="nc:ItemDescriptionText"/></td>
		</tr>
	</xsl:template>
	
	<xsl:template name="chargeOffenseTab">
		<table>
			<tr>
				<td class="incidentLabel">Code</td>
				<td class="incidentLabel">Description</td>
				<td class="incidentLabel">Status</td>
				<td class="incidentLabel"># Premises Entered</td>
				<td class="incidentLabel">Entry Method</td>
				<td class="incidentLabel">Location</td>
				<td class="incidentLabel">Bias</td>
				<td class="incidentLabel">Weapon</td>
			</tr>
<!-- 			<xsl:apply-templates select="//lexs:StructuredPayload/ndexia:IncidentReport/ndexia:Offense[ndexia:ActivityAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=//lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText='Offense']/@s:id]" /> -->
				<xsl:apply-templates select="//lexs:StructuredPayload/ndexia:IncidentReport/ndexia:Offense[ndexia:ActivityAugmentation/lexslib:SameAsDigestReference]" />
		
		</table>
		
	</xsl:template>
	
	<xsl:template match="//lexs:StructuredPayload/ndexia:IncidentReport/ndexia:Offense">
		<xsl:variable name="offenseID" select="ndexia:ActivityAugmentation/lexslib:SameAsDigestReference/@lexslib:ref"/>
		<xsl:variable name="structuredOffenseLocation" select="//lexs:Digest/lexsdigest:EntityLocation/nc:Location[@s:id=//lexsdigest:Associations/lexsdigest:OffenseLocationAssociation[nc:ActivityReference/@s:ref=//lexsdigest:EntityActivity/nc:Activity/@s:id]/nc:LocationReference/@s:ref]/nc:LocationAddress/nc:StructuredAddress" />	
		<tr>
			<td>
				<xsl:choose>
					<xsl:when test="ndexia:OffenseCode[.!='']">
						<xsl:value-of select="ndexia:OffenseCode"/>
					</xsl:when>
					<xsl:otherwise>				
						<xsl:value-of select="//ext:Offense[lexslib:SameAsDigestReference/@lexslib:ref=$offenseID]/ext:OffenseCodeText"/>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td><xsl:value-of select="ndexia:OffenseText"/></td>
			<td><xsl:value-of select="//ndexia:IncidentReport/ndexia:Incident/ndexia:ActivityStatus/ndexia:ActivityStatusAugmentation/ndexia:IncidentStatusCode"/></td>
			<td><!-- Premises entered goes in here --></td>
			<td><xsl:value-of select="ndexia:OffenseEntryPoint/ndexia:PassagePointAugmentation/ndexia:MethodOfAccessText"/></td>
			<td>
				<xsl:choose>
					<xsl:when test="$structuredOffenseLocation/nc:LocationStreet/nc:StreetFullText[.!='']">
						<xsl:value-of select="concat($structuredOffenseLocation/nc:LocationStreet/nc:StreetFullText,' ',$structuredOffenseLocation/nc:LocationCityName,',', $structuredOffenseLocation/nc:LocationStateName,' ',$structuredOffenseLocation/nc:LocationPostalCode)"/>
					</xsl:when>
					<xsl:otherwise>
							<xsl:value-of select="concat($structuredOffenseLocation/nc:LocationStreet/nc:StreetNumberText,' ', $structuredOffenseLocation/nc:LocationStreet/nc:StreetName,' ', $structuredOffenseLocation/nc:LocationCityName,',', $structuredOffenseLocation/nc:LocationStateName,' ',$structuredOffenseLocation/nc:LocationPostalCode)"/>
					</xsl:otherwise>
				</xsl:choose>			
			</td>
			<td><!-- Bias entered goes in here --></td>
			<td><xsl:value-of select="//lexsdigest:EntityTangibleItem/nc:TangibleItem[@s:id=//lexsdigest:Associations/lexsdigest:OffenseWeaponAssociation/nc:ItemReference/@s:ref]/nc:ItemDescriptionText"/></td>
		</tr>
	</xsl:template>
	
	<xsl:template name="narrativeTab" >
		<xsl:value-of select="//lexs:Digest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText='Incident']/nc:ActivityDescriptionText" />
	</xsl:template>
	
	<xsl:template match="j:ActivityEnforcementOfficialAssociation">
		<xsl:variable name="activityID" select="nc:ActivityReference/@s:ref"/>
		<xsl:variable name="officerID" select="nc:PersonReference/@s:ref"/>
		<xsl:variable name="officerName" select="/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityPerson/lexsdigest:Person[@s:id = $officerID]/nc:PersonName"/>
		<xsl:variable name="officerBadge" select="/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityPerson[lexsdigest:Person/@s:id = $officerID]/j:EnforcementOfficial/j:EnforcementOfficialBadgeIdentification/nc:IdentificationID"/>
		
		<xsl:choose>
			<xsl:when test="$officerName/nc:PersonFullName">
				<xsl:value-of select="$officerName/nc:PersonFullName"/>
			</xsl:when>
			<xsl:when test="$officerName/nc:PersonGivenName or $officerName/nc:PersonSurName">
				<xsl:value-of select="$officerName/nc:PersonGivenName"/><xsl:text> </xsl:text><xsl:value-of select="$officerName/nc:PersonSurName"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$officerName"/>
			</xsl:otherwise>
		</xsl:choose>
		
		<xsl:text> (Badge #: </xsl:text><xsl:value-of select="$officerBadge"/><xsl:text>)</xsl:text> <br/>
		
	</xsl:template>
</xsl:stylesheet>
