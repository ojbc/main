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
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xmlns:cyfs="http://release.niem.gov/niem/domains/cyfs/3.0/" 
	xmlns:nc="http://release.niem.gov/niem/niem-core/3.0/"
	xmlns:j="http://release.niem.gov/niem/domains/jxdm/5.0/" 
	xmlns:s="http://release.niem.gov/niem/structures/3.0/" 
	xmlns:cext="http://ojbc.org/IEPD/Extension/JuvenileHistory/JuvenileHistoryCommonExtension/1.0" 
	xmlns:offense-doc="http://ojbc.org/IEPD/Exchange/JuvenileHistory/JuvenileOffenseHistoryResponse/1.0" 
	xmlns:offense-ext="http://ojbc.org/IEPD/Extension/JuvenileHistory/JuvenileOffenseHistoryExtension/1.0" 
	xmlns:codes="http://ojbc.org/IEPD/Extension/JuvenileHistory/JuvenileOffenseHistoryExtension/michigan/codes/1.0" 
	xmlns:caseplan-doc="http://ojbc.org/IEPD/Exchange/JuvenileHistory/JuvenileCasePlanHistoryResponse/1.0"
	xmlns:caseplan-ext="http://ojbc.org/IEPD/Extension/JuvenileHistory/JuvenileCasePlanHistoryExtension/1.0"
	xmlns:hearing-doc="http://ojbc.org/IEPD/Exchange/JuvenileHistory/JuvenileHearingHistoryResponse/1.0"
	xmlns:hearing-ext="http://ojbc.org/IEPD/Extension/JuvenileHistory/JuvenileHearingHistoryExtension/1.0"
	xmlns:hearing-codes="http://ojbc.org/IEPD/Extension/JuvenileHistory/JuvenileHearingHistoryExtension/michigan/codes/1.0"
	xmlns:intake-doc="http://ojbc.org/IEPD/Exchange/JuvenileHistory/JuvenileIntakeHistoryResponse/1.0"
	xmlns:intake-ext="http://ojbc.org/IEPD/Extension/JuvenileHistory/JuvenileIntakeHistoryExtension/1.0"
	xmlns:intake-codes="http://ojbc.org/IEPD/Extension/JuvenileHistory/JuvenileIntakeHistoryExtension/michigan/codes/1.0"
	xmlns:placement-doc="http://ojbc.org/IEPD/Exchange/JuvenileHistory/JuvenilePlacementHistoryResponse/1.0"
	xmlns:placement-ext="http://ojbc.org/IEPD/Extension/JuvenileHistory/JuvenilePlacementHistoryExtension/1.0"
	xmlns:placement-codes="http://ojbc.org/IEPD/Extension/JuvenileHistory/JuvenilePlacementHistoryExtension/michigan/codes/1.0"
	xmlns:referral-doc="http://ojbc.org/IEPD/Exchange/JuvenileHistory/JuvenileReferralHistoryResponse/1.0"
	xmlns:referral-ext="http://ojbc.org/IEPD/Extension/JuvenileHistory/JuvenileReferralHistoryExtension/1.0"
	xmlns:referral-codes="http://ojbc.org/IEPD/Extension/JuvenileHistory/JuvenileReferralHistoryExtension/michigan/codes/1.0"
	exclude-result-prefixes="#all"
	>
	<xsl:import href="_formatters.xsl" />
	<xsl:output method="html" encoding="UTF-8" />
	
	<xsl:param name="queryTypes">
		<xsl:text>OFFENSE|REFERRAL|INTAKE|HEARING|CASE PLAN|PLACEMENT</xsl:text>
	</xsl:param>
	<xsl:param name="activeAccordionId">false</xsl:param>
	<xsl:template match="/">
		<div style="width:100%;height:100%;display:none;" id="modalIframeSpinner"></div>
		
		<span class="hint">
			<xsl:text>Last Updated: </xsl:text>
			<xsl:variable name="lastUpdatedDate" 
				select="offense-doc:JuvenileOffenseHistoryResponse/offense-ext:JuvenileOffenseHistory/cext:JuvenileInformationAvailabilityMetadata/nc:LastUpdatedDate
				|caseplan-doc:JuvenileCasePlanHistoryResponse/caseplan-ext:JuvenileCasePlanHistory/cext:JuvenileInformationAvailabilityMetadata/nc:LastUpdatedDate
				|hearing-doc:JuvenileHearingHistoryResponse/hearing-ext:JuvenileHearingHistory/cext:JuvenileInformationAvailabilityMetadata/nc:LastUpdatedDate
				|intake-doc:JuvenileIntakeHistoryResponse/intake-ext:JuvenileIntakeHistory/cext:JuvenileInformationAvailabilityMetadata/nc:LastUpdatedDate
				|placement-doc:JuvenilePlacementHistoryResponse/placement-ext:JuvenilePlacementHistory/cext:JuvenileInformationAvailabilityMetadata/nc:LastUpdatedDate
				|referral-doc:JuvenileReferralHistoryResponse/referral-ext:JuvenileIntakeHistory/cext:JuvenileInformationAvailabilityMetadata/nc:LastUpdatedDate
				"/>
			<xsl:value-of select="format-date($lastUpdatedDate,'[MNn] [D1], [Y]')"/>
		</span>
		<table style="width:100%">
			<tr>
				<td> 
					<xsl:apply-templates select="offense-doc:JuvenileOffenseHistoryResponse/offense-ext:JuvenileOffenseHistory " mode="personInfo"/> 
					<xsl:apply-templates select="caseplan-doc:JuvenileCasePlanHistoryResponse/caseplan-ext:JuvenileCasePlanHistory" mode="personInfo"/> 
					<xsl:apply-templates select="hearing-doc:JuvenileHearingHistoryResponse/hearing-ext:JuvenileHearingHistory" mode="personInfo"/> 
					<xsl:apply-templates select="intake-doc:JuvenileIntakeHistoryResponse/intake-ext:JuvenileIntakeHistory" mode="personInfo"/> 
					<xsl:apply-templates select="placement-doc:JuvenilePlacementHistoryResponse/placement-ext:JuvenilePlacementHistory" mode="personInfo"/> 
					<xsl:apply-templates select="referral-doc:JuvenileReferralHistoryResponse/referral-ext:JuvenileReferralHistory" mode="personInfo"/> 
				</td>                
			</tr>
		</table>
		<div class="buttonBar full">
			<xsl:variable name="responseType">
				<xsl:value-of select="upper-case(substring-before(substring-after(/child::node()/cext:JuvenileHistoryCategoryCode, 'Juvenile'), 'History'))"></xsl:value-of>
			</xsl:variable>
			<xsl:variable name="id"><xsl:value-of select="child::node()/cext:JuvenileHistoryQueryCriteria/cext:JuvenileInformationRecordID/nc:IdentificationID"/></xsl:variable>
			<xsl:variable name="identificationSourceText"><xsl:value-of select="child::node()/cext:JuvenileHistoryQueryCriteria/cext:JuvenileInformationRecordID/nc:IdentificationSourceText"/></xsl:variable>
			<xsl:for-each select="tokenize($queryTypes, '\|')">
				<xsl:variable name="queryType"><xsl:value-of select="replace(., ' ','')"/></xsl:variable>
				<xsl:variable name="isActiveTab" select="upper-case($responseType) eq $queryType"/>
				<div class="buttonBarItem">
					<xsl:element name="a">
						<xsl:attribute name="class">
							<xsl:text>buttonBarButton full </xsl:text>
							<xsl:if test="$isActiveTab">
								<xsl:text>active</xsl:text>
							</xsl:if>
						</xsl:attribute>
						<xsl:attribute name="href">
							<xsl:choose>
								<xsl:when test="$isActiveTab">#</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="concat('../people/searchDetails?identificationID=',$id , '&amp;systemName=Juvenile History' , '&amp;identificationSourceText=',$identificationSourceText,'&amp;queryType=',$queryType)"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:attribute>
						<xsl:value-of select="."></xsl:value-of>
					</xsl:element>
				</div>
			</xsl:for-each>
		</div>
		<table  class="detailsTable">
			<tr><td>
				<xsl:apply-templates select="//cext:JuvenileInformationAvailabilityCode[.='NOT FOUND']" mode="informationNotFound"/> 
				<xsl:apply-templates select="offense-doc:JuvenileOffenseHistoryResponse/offense-ext:JuvenileOffenseHistory[cext:JuvenileInformationAvailabilityCode='FOUND']" mode="details"/> 
				<xsl:apply-templates select="caseplan-doc:JuvenileCasePlanHistoryResponse/caseplan-ext:JuvenileCasePlanHistory[cext:JuvenileInformationAvailabilityCode='FOUND']" mode="details"/> 
				<xsl:apply-templates select="hearing-doc:JuvenileHearingHistoryResponse/hearing-ext:JuvenileHearingHistory[cext:JuvenileInformationAvailabilityCode='FOUND']" mode="details"/>
				<xsl:apply-templates select="intake-doc:JuvenileIntakeHistoryResponse/intake-ext:JuvenileIntakeHistory[cext:JuvenileInformationAvailabilityCode='FOUND']" mode="details"/>
				<xsl:apply-templates select="placement-doc:JuvenilePlacementHistoryResponse/placement-ext:JuvenilePlacementHistory[cext:JuvenileInformationAvailabilityCode='FOUND']" mode="details"/>
				<xsl:apply-templates select="referral-doc:JuvenileReferralHistoryResponse/referral-ext:JuvenileReferralHistory[cext:JuvenileInformationAvailabilityCode='FOUND']" mode="details"/>
				
				<xsl:if test="//cext:JuvenileInformationAvailabilityCode='FOUND'">
					<xsl:choose>
						<xsl:when test="not($activeAccordionId)">
							<script type="text/javascript" >
								$(function () {
									$("#detailList").accordion({
										active:"false",
										heightStyle: "content",			
										collapsible: true,
										activate: function( event, ui ) { 
											var modalIframe = $("#modalIframe", parent.document);
											modalIframe.height(modalIframe.contents().find("body").height() + 16);
										}
									});
								});
							</script>
						</xsl:when>
						<xsl:otherwise>
							<script type="text/javascript" >
								$(function () {
									$("#detailList").accordion({
										active:$("<xsl:value-of select="concat('h3#',$activeAccordionId)"/>")	,
										heightStyle: "content",			
										collapsible: true,
										activate: function( event, ui ) { 
											var modalIframe = $("#modalIframe", parent.document);
											modalIframe.height(modalIframe.contents().find("body").height() + 16);
										}
									});
									
									$("<xsl:value-of select="concat('#',$activeAccordionId)"/>").click();
								});
							</script>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:if>
			</td></tr>
		</table>
	</xsl:template>
	<xsl:template match="cext:JuvenileInformationAvailabilityCode" mode="informationNotFound">
		<span class="hint">Information not available</span>
	</xsl:template>
		
	<xsl:template match="referral-ext:JuvenileReferralHistory" mode="details">
		<div id="detailList">
			<xsl:apply-templates select="nc:Referral"></xsl:apply-templates>
		</div>
	</xsl:template>
	<xsl:template match="nc:Referral">
		<xsl:variable name="id">
			<xsl:value-of select="child::referral-ext:ReferralAugmentation/cext:JuvenileInformationRecordID/nc:IdentificationID"/>
		</xsl:variable>
		<xsl:element name="h3">
			<xsl:attribute name="id">
				<xsl:value-of select="$id"></xsl:value-of>
			</xsl:attribute>
			REFERRAL <xsl:value-of select="$id"/>
		</xsl:element>
		<div>
			<table class="detailsTable">
				<tr>
					<td class="detailsLabel">Type of Referral</td>
					<td>
						<xsl:value-of select="referral-ext:ReferralAugmentation/referral-codes:ReferralCategoryCode"/>
					</td>
					<td class="detailsLabel">Referral Organization</td>
					<td>
						<xsl:value-of select="nc:ReferralIssuer/nc:EntityOrganization/nc:OrganizationName"/>
					</td>
				</tr>
				<tr>
					<td class="detailsLabel">Referral Source</td>
					<td>
						<xsl:value-of select="nc:ReferralIssuer/nc:EntityOrganization/referral-codes:ReferralIssuerCategoryCode"></xsl:value-of>
					</td>
					<td class="detailsLabel">Referral Date</td>
					<td>
						<xsl:apply-templates select="nc:ActivityDate/nc:Date" mode="formatDateAsMMDDYYYY"/>						
					</td>
				</tr>
				<tr>
					<td class="detailsLabel">Referral Name</td>
					<td>
						<xsl:apply-templates mode="firstNameFirst" select="nc:ReferralIssuer/nc:EntityPerson/nc:PersonName"></xsl:apply-templates>
					</td>
					<td class="detailsLabel">Related Record</td>
					<td>
						<xsl:apply-templates select="child::referral-ext:ReferralAugmentation/cext:RelatedJuvenileHistoryRecords/cext:RelatedJuvenileHistoryRecord"/>
					</td>
					
				</tr>
			</table>
		</div>
	</xsl:template>
	<xsl:template match="placement-ext:JuvenilePlacementHistory" mode="details">
		<div id="detailList">
			<xsl:apply-templates select="cyfs:JuvenilePlacement"></xsl:apply-templates>
		</div>
	</xsl:template>
	<xsl:template match="cyfs:JuvenilePlacement">
		<xsl:variable name="id">
			<xsl:value-of select="child::placement-ext:JuvenilePlacementAugmentation/cext:JuvenileInformationRecordID/nc:IdentificationID"></xsl:value-of>
		</xsl:variable>
		<xsl:element name="h3">
			<xsl:attribute name="id">
				<xsl:value-of select="$id"/>
			</xsl:attribute>
			<xsl:value-of select="placement-codes:PlacementCategoryCode"></xsl:value-of><xsl:text> </xsl:text><xsl:apply-templates select="nc:ActivityDateRange/nc:StartDate/nc:Date" mode="formatDateAsMMDDYYYY"/>
		</xsl:element>
		<div>
			<table class="detailsTable">
				<tr>
					<td class="detailsLabel">Placement Organization</td>
					<td>
						<xsl:value-of select="placement-ext:JuvenilePlacementAugmentation/cyfs:JuvenilePlacementFacilityAssociation/cyfs:PlacementFacility/nc:FacilityName"/>
					</td>
					<td class="detailsLabel">Placement Start Date</td>
					<td>	
						<xsl:apply-templates select="nc:ActivityDateRange/nc:StartDate/nc:Date" mode="formatDateAsMMDDYYYY"/>
					</td>
				</tr>
				<tr>
					<td class="detailsLabel">Placement Location</td>
					<td>
						<xsl:variable name="locationId" select="placement-ext:JuvenilePlacementAugmentation/cyfs:JuvenilePlacementFacilityAssociation/cyfs:PlacementFacility/nc:FacilityLocation/@s:ref"></xsl:variable>
						<xsl:apply-templates select="preceding-sibling::nc:Location[@s:id = $locationId]/nc:Address" mode="cityAndState"></xsl:apply-templates>
					</td>
					<td class="detailsLabel">Placement End Date</td>
					<td>
						<xsl:apply-templates select="nc:ActivityDateRange/nc:EndDate/nc:Date" mode="formatDateAsMMDDYYYY"/>						
					</td>
				</tr>
				<tr>
					<td class="detailsLabel">Placement Security</td>
					<td>
						<xsl:value-of select="placement-ext:JuvenilePlacementAugmentation/cyfs:JuvenilePlacementFacilityAssociation/cyfs:PlacementFacility/placement-codes:FacilitySecurityCode"></xsl:value-of>
					</td>
					<td class="detailsLabel">Placement Type</td>
					<td>
						<xsl:value-of select="placement-codes:PlacementCategoryCode"></xsl:value-of>						
					</td>
				</tr>
				<tr>
					<td class="detailsLabel">Related Record</td>
					<td colspan="3">
						<xsl:apply-templates select="child::placement-ext:JuvenilePlacementAugmentation/cext:RelatedJuvenileHistoryRecords/cext:RelatedJuvenileHistoryRecord"/>
					</td>
				</tr>
			</table>
		</div>
	</xsl:template>
	
	<xsl:template match="intake-ext:JuvenileIntakeHistory" mode="details">
		<div id="detailList">
			<xsl:apply-templates select="cyfs:JuvenileIntakeAssessment"></xsl:apply-templates>
		</div>
	</xsl:template>
	<xsl:template match="cyfs:JuvenileIntakeAssessment">
		<xsl:variable name="id">
			<xsl:value-of select="child::intake-ext:JuvenileAssessmentAugmentation/cext:JuvenileInformationRecordID/nc:IdentificationID"></xsl:value-of>
		</xsl:variable>
		<xsl:element name="h3">
			<xsl:attribute name="id">
				<xsl:value-of select="$id"/>
			</xsl:attribute>
			INTAKE <xsl:value-of select="$id"/>
		</xsl:element>
		<div>
			<xsl:variable name="intakeTypes">
				<xsl:value-of select="string-join(intake-ext:JuvenileAssessmentAugmentation/intake-codes:JuvenileIntakeAssessmentCategoryCode, ', ')"/>
			</xsl:variable>
			<table class="detailsTable">
				<tr>
					<td class="detailsLabel">Intake Screening</td>
					<td>
						<xsl:call-template name="formatBooleanAsYesNo">
							<xsl:with-param name="value" select="string(contains($intakeTypes, 'Screening'))"></xsl:with-param>
						</xsl:call-template>
					</td>
					<td class="detailsLabel">Intake Other Process</td>
					<td>						
						<xsl:call-template name="formatBooleanAsYesNo">
							<xsl:with-param name="value" select="string(contains($intakeTypes, 'Other'))"></xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
				<tr>
					<td class="detailsLabel">Intake Assessment</td>
					<td>
						<xsl:call-template name="formatBooleanAsYesNo">
							<xsl:with-param name="value" select="string(contains($intakeTypes, 'Assessment'))"></xsl:with-param>
						</xsl:call-template>
					</td>
					<td class="detailsLabel">Intake Outcome</td>
					<td>
						<xsl:value-of select="string-join(
							(cyfs:AssessmentRecommendation/cyfs:AssessmentRecommendedCourseOfAction/intake-ext:AssessmentRecommendedCourseOfActionAugmentation/intake-codes:AssessmentRecommendedCourseOfActionCode), ', ')"/>						
					</td>
				</tr>
				<tr>
					<td class="detailsLabel">Intake Interview</td>
					<td>
						<xsl:call-template name="formatBooleanAsYesNo">
							<xsl:with-param name="value" select="string(contains($intakeTypes, 'Interview'))"></xsl:with-param>
						</xsl:call-template>
					</td>
					<td class="detailsLabel">Intake Assessment Date</td>
					<td>
						<xsl:apply-templates select="nc:ActivityDate/nc:Date" mode="formatDateAsMMDDYYYY"/>						
					</td>
				</tr>
				<tr>
					<td class="detailsLabel">Related Record</td>
					<td colspan="3">
						<xsl:apply-templates select="child::intake-ext:JuvenileAssessmentAugmentation/cext:RelatedJuvenileHistoryRecords/cext:RelatedJuvenileHistoryRecord"/>
					</td>
				</tr>
			</table>
		</div>
	</xsl:template>
	<xsl:template match="offense-ext:JuvenileOffenseHistory" mode="details">
		<div id="detailList">
			<xsl:apply-templates select="j:OffenseChargeAssociation"></xsl:apply-templates>
		</div>
	</xsl:template>
	<xsl:template match="caseplan-ext:JuvenileCasePlanHistory" mode="details">
		<div id="detailList">
			<xsl:apply-templates select="caseplan-ext:CasePlan"></xsl:apply-templates>
		</div>
	</xsl:template>
	<xsl:template match="j:OffenseChargeAssociation">
		<xsl:variable name="id">
			<xsl:value-of select="child::j:Offense/offense-ext:OffenseAugmentation/cext:JuvenileInformationRecordID/nc:IdentificationID"></xsl:value-of>
		</xsl:variable>
		<xsl:element name="h3">
			<xsl:attribute name="id">
				<xsl:value-of select="$id"/>
			</xsl:attribute>
			<xsl:value-of select="j:Offense/j:OffenseName |j:Offesne/j:OffenseDesignation"/><xsl:text> </xsl:text><xsl:call-template name="formatDate"><xsl:with-param name="date" select="j:Charge/j:ChargeFilingDate/nc:Date"></xsl:with-param></xsl:call-template>
			
		</xsl:element>
		<div>
			<table class="detailsTable">
				<tr>
					<td class="detailsLabel">PACC Code</td>
					<td><xsl:value-of select="j:Offense/codes:OffensePACCCode"></xsl:value-of></td>
					<td class="detailsLabel">Adjudication Type</td>
					<td><xsl:value-of select="j:Charge/j:ChargeVerdict/offense-ext:VerdictAugmentation/codes:ChargeVerdictCode"></xsl:value-of></td>
				</tr>
				<tr>
					<td class="detailsLabel">Description</td>
					<td><xsl:value-of select="j:Offense/j:OffenseName |j:Offesne/j:OffenseDesignation"></xsl:value-of></td>
					<td class="detailsLabel">Adjudication Date</td>
					<td>
						<xsl:call-template name="formatDate">
							<xsl:with-param name="date" select="j:Charge/j:ChargeVerdict/j:VerdictDispositionDate/nc:Date"> </xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
				<tr>
					<td class="detailsLabel">Date</td>
					<td>
						<xsl:call-template name="formatDate">
							<xsl:with-param name="date" select="j:Charge/j:ChargeFilingDate/nc:Date"> </xsl:with-param>
						</xsl:call-template>
					</td>
					<td class="detailsLabel">Dispositional Option</td>
					<td>
						<xsl:value-of select="string-join(j:Charge/j:ChargeSanction/offense-ext:SanctionAugmentation/codes:DispositionSanctionCode,', ')"></xsl:value-of>
					</td>
				</tr>
				<tr>
					<td class="detailsLabel">Location</td>
					<td>
						<xsl:variable name="offenseId" select="j:Offense/@s:id"></xsl:variable>
						<xsl:variable name="locationId"><xsl:value-of select="following-sibling::j:OffenseLocationAssociation[j:Offense/@s:ref =$offenseId]/nc:Location/@s:ref"/></xsl:variable>
						<xsl:apply-templates select="preceding-sibling::nc:Location[@s:id = $locationId]/nc:Address" mode="cityAndState"></xsl:apply-templates>
					</td>
					<td class="detailsLabel">Related Record</td>
					<td>
						<xsl:apply-templates select="j:Offense/offense-ext:OffenseAugmentation/cext:RelatedJuvenileHistoryRecords/cext:RelatedJuvenileHistoryRecord"/>
					</td>
				</tr>
			</table>
		</div>
	</xsl:template>
	
	<xsl:template match="cext:RelatedJuvenileHistoryRecord">
		<xsl:choose>
			<xsl:when test="cext:RelatedJuvenileHistoryRecordNotSupportedIndicator">
				<xsl:if test="position() != 1"><br/></xsl:if>
				<xsl:text>Linkage to </xsl:text>
				<xsl:value-of select="cext:JuvenileHistoryCategoryCode"/>
				<xsl:text> not supported</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:variable name="id">
					<xsl:value-of select="preceding::cext:JuvenileHistoryQueryCriteria/cext:JuvenileInformationRecordID/nc:IdentificationID"/>
				</xsl:variable>
				<xsl:variable name="identificationSourceText">
					<xsl:value-of select="preceding::cext:JuvenileHistoryQueryCriteria/cext:JuvenileInformationRecordID/nc:IdentificationSourceText"/>
				</xsl:variable>
				<xsl:variable name="queryType">
					<xsl:value-of select="normalize-space(substring-before(substring-after(cext:JuvenileHistoryCategoryCode, 'Juvenile'),'History'))"></xsl:value-of>
				</xsl:variable>
				<xsl:variable name="relatedRecordId">
					<xsl:value-of select="cext:JuvenileInformationRecordID/nc:IdentificationID"></xsl:value-of>
				</xsl:variable>
				
				<xsl:if test="normalize-space($relatedRecordId)">
					<xsl:if test="position() != 1"><br/></xsl:if>
					<xsl:element name="a">
						<xsl:attribute name="href">
							<xsl:value-of select="concat('../people/searchDetails?identificationID=',$id,'&amp;systemName=Juvenile History' , '&amp;identificationSourceText=', $identificationSourceText, '&amp;queryType=',$queryType,'&amp;activeAccordionId=',$relatedRecordId)"/>
						</xsl:attribute>
						<xsl:value-of select="$queryType"/><xsl:text> ID: </xsl:text><xsl:value-of select="$relatedRecordId"></xsl:value-of>
					</xsl:element>
				</xsl:if>
				
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="caseplan-ext:CasePlan">
		<xsl:variable name="id">
			<xsl:value-of select="child::caseplan-ext:CasePlanAugmentation/cext:JuvenileInformationRecordID/nc:IdentificationID"></xsl:value-of>
		</xsl:variable>
		<xsl:element name="h3">
			<xsl:attribute name="id">
				<xsl:value-of select="$id"/>
			</xsl:attribute>
			CASE PLAN <xsl:value-of select="$id"/>
		</xsl:element>
		<div>
			<table class="detailsTable">
				<tr>
					<td class="detailsLabel">Case Plan</td>
					<td><xsl:value-of select="caseplan-ext:CasePlanIndicator"></xsl:value-of></td>
					<td class="detailsLabel">Assessement</td>
					<td><xsl:value-of select="caseplan-ext:AssessmentIndicator"></xsl:value-of></td>
				</tr>
				<tr>
					<td class="detailsLabel">Related Record</td>
					<td colspan="3">
						<xsl:apply-templates select="child::caseplan-ext:CasePlanAugmentation/cext:RelatedJuvenileHistoryRecords/cext:RelatedJuvenileHistoryRecord"></xsl:apply-templates>
					</td>
				</tr>
			</table>
		</div>
	</xsl:template>
	<xsl:template match="hearing-ext:JuvenileHearingHistory" mode="details">
		<div id="detailList">
			<xsl:apply-templates select="cyfs:CourtCase/j:CaseAugmentation/j:CaseHearing"></xsl:apply-templates>
		</div>
	</xsl:template>
	<xsl:template match="j:CaseHearing">
		<xsl:variable name="id">
			<xsl:value-of select="child::hearing-ext:CourtEventAugmentation/cext:JuvenileInformationRecordID/nc:IdentificationID"></xsl:value-of>
		</xsl:variable>
		<xsl:element name="h3">
			<xsl:attribute name="id">
				<xsl:value-of select="$id"/>
			</xsl:attribute>
			HEARING <xsl:value-of select="$id"/>
		</xsl:element>
		<div>
			<table class="detailsTable">
				<tr>
					<td class="detailsLabel">Hearing Type</td>
					<td><xsl:value-of select="hearing-ext:CourtEventAugmentation/hearing-codes:HearingCategoryCode"></xsl:value-of></td>
					<td class="detailsLabel">Probation Violation</td>
					<td><xsl:apply-templates select="hearing-ext:CourtEventAugmentation/hearing-ext:ProbationViolationIndicator" 
						mode="formatBooleanAsYesNo"/></td>
					<td class="detailsLabel">Contempt of Court</td>
					<td><xsl:apply-templates select="hearing-ext:CourtEventAugmentation/hearing-ext:ContemptOfCourtIndicator" 
						mode="formatBooleanAsYesNo"/></td>
				</tr>
				<tr>
					<td class="detailsLabel">Hearing Date</td>
					<td><xsl:apply-templates select="nc:ActivityDate/nc:Date" 
						mode="formatDateAsMMDDYYYY"/></td>
					<td class="detailsLabel">Probation Violation Date</td>
					<td>
						<xsl:if test="hearing-ext:CourtEventAugmentation/hearing-ext:ProbationViolationIndicator">
							<xsl:apply-templates select="nc:ActivityDate/nc:Date" 
								mode="formatDateAsMMDDYYYY"/>
						</xsl:if>
					</td>
					<td class="detailsLabel">Contempt of Court Date</td>
					<td>						
						<xsl:if test="hearing-ext:CourtEventAugmentation/hearing-ext:ContemptOfCourtIndicator">
							<xsl:apply-templates select="nc:ActivityDate/nc:Date" 
								mode="formatDateAsMMDDYYYY"/>
						</xsl:if>
					</td>
				</tr>
				<tr>
					<td class="detailsLabel">Hearing Outcome</td>
					<td colspan="5">
						<xsl:value-of select="string-join(hearing-ext:CourtEventAugmentation/hearing-codes:HearingDispositionCode, ', ')"/>
					</td>
				</tr>
				<tr>
					<td class="detailsLabel">Related Record</td>
					<td colspan="5">
						<xsl:apply-templates select="child::hearing-ext:CourtEventAugmentation/cext:RelatedJuvenileHistoryRecords/cext:RelatedJuvenileHistoryRecord"></xsl:apply-templates>
					</td>
				</tr>
			</table>
		</div>
	</xsl:template>
	<xsl:template match="offense-ext:JuvenileOffenseHistory
		|caseplan-ext:JuvenileCasePlanHistory
		|hearing-ext:JuvenileHearingHistory
		|placement-ext:JuvenilePlacementHistory
		|referral-ext:JuvenileReferralHistory
		|intake-ext:JuvenileIntakeHistory" 
		mode="personInfo">
		<xsl:variable name="pid" select="cyfs:ParentChildAssociation/cyfs:Child/@s:ref"/>
		<xsl:variable name="parentId" select="cyfs:ParentChildAssociation/cyfs:Parent/@s:ref"/>
		<xsl:variable name="childLocationId" select="nc:PersonResidenceAssociation[nc:Person/@s:ref=$pid]/nc:Location/@s:ref"/>
		<table class="detailsTable">
			<tr>
				<td class="detailsLabel">NAME</td>
				<td>
					<xsl:apply-templates select="nc:Person[@s:id=$pid]/nc:PersonName[j:PersonNameCategoryCode='provided']" mode="firstNameFirst"/>
				</td>
				<td class="detailsLabel">ADDRESS</td>
				<td>
					<xsl:apply-templates select="nc:Location[@s:id = $childLocationId]/nc:Address" mode="street"></xsl:apply-templates>
				</td>
				<td class="detailsLabel">SOURCE ORG</td>
				<td>
					<xsl:value-of select="cext:JuvenileInformationAvailabilityMetadata/cext:JuvenileInformationOwnerOrganization/nc:OrganizationName"></xsl:value-of>
				</td>
			</tr>
			<tr>
				<td class="detailsLabel">ALIAS</td>
				<td>
					<xsl:apply-templates select="nc:Person[@s:id=$pid]/nc:PersonName[j:PersonNameCategoryCode='alias']" mode="firstNameFirst"/>
				</td>
				<td class="detailsLabel">CITY</td>
				<td><xsl:value-of select="nc:Location[@s:id = $childLocationId]/nc:Address/nc:LocationCityName" /></td>
				<td class="detailsLabel">SOURCE DIVISION</td>
				<td>
					<xsl:value-of select="cext:JuvenileInformationAvailabilityMetadata/cext:JuvenileInformationOwnerOrganization/nc:OrganizationBranchName"></xsl:value-of>
				</td>
			</tr>
			<tr>
				<td class="detailsLabel">FILE NUMBER</td>
				<td> </td>
				<td class="detailsLabel">STATE</td>
				<td><xsl:value-of select="nc:Location[@s:id = $childLocationId]/nc:Address/nc:LocationStateFIPS5-2AlphaCode
					|nc:Location[@s:id = $childLocationId]/nc:Address/nc:LocationCanadianProvinceCode" /></td>
				<td class="detailsLabel"></td>
				<td></td>
			</tr>
			<tr>
				<td class="detailsLabel">PARENT(S)</td>
				<td>
					<xsl:apply-templates select="nc:Person[@s:id=$parentId]/nc:PersonName[j:PersonNameCategoryCode='provided']"/>
				</td>
				<td class="detailsLabel">ZIP</td>
				<td><xsl:value-of select="nc:Location[@s:id = $childLocationId]/nc:Address/nc:LocationPostalCode" /></td>
				<td class="detailsLabel"></td>
				<td></td>
			</tr>
		</table>
	</xsl:template>
	
	<xsl:template match="nc:Address" mode="street">
		<xsl:if test="nc:AddressSecondaryUnitText[normalize-space()]">
			<xsl:value-of select="nc:AddressSecondaryUnitText[normalize-space()]"></xsl:value-of>
			<xsl:text> </xsl:text>
		</xsl:if>
		<xsl:choose>
			<xsl:when test="nc:LocationStreet/nc:StreetFullText[normalize-space()]">
				<xsl:value-of select="nc:LocationStreet/nc:StreetFullText"></xsl:value-of>
			</xsl:when>
			<xsl:when test="nc:StreetNumberText[normalize-space()] or nc:StreetName[normalize-space()] or nc:StreetCategoryText[normalize-space()]">
				<xsl:value-of select="nc:StreetNumberText"></xsl:value-of>
				<xsl:if test="nc:StreetName[normalize-space()]">
					<xsl:text> </xsl:text><xsl:value-of select="normalize-space(nc:StreetName)"></xsl:value-of>
				</xsl:if>
				<xsl:if test="nc:StreetCategoryText[normalize-space()]">
					<xsl:text> </xsl:text><xsl:value-of select="normalize-space(nc:StreetCategoryText)"></xsl:value-of>
				</xsl:if>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="nc:Address" mode="cityAndState">
		<xsl:value-of select="string-join((nc:LocationCityName, nc:LocationStateFIPS5-2AlphaCode | nc:LocationCanadianProvinceCode), ', ')"></xsl:value-of>
	</xsl:template>
	
</xsl:stylesheet>