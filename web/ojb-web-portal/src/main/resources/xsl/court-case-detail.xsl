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
	xmlns:ccq-res-doc="http://ojbc.org/IEPD/Exchange/CourtCaseQueryResults/1.0"
	xmlns:ccq-res-ext="http://ojbc.org/IEPD/Extensions/CourtCaseQueryResultsExtension/1.0"
	xmlns:iad="http://ojbc.org/IEPD/Extensions/InformationAccessDenial/1.0"
	xmlns:qrer="http://ojbc.org/IEPD/Extensions/QueryRequestErrorReporting/1.0"
	xmlns:srm="http://ojbc.org/IEPD/Extensions/QueryResultsMetadata/1.0"
	xmlns:intel="http://release.niem.gov/niem/domains/intelligence/3.1/"
	xmlns:j="http://release.niem.gov/niem/domains/jxdm/5.1/"
	xmlns:nc="http://release.niem.gov/niem/niem-core/3.0/"
	xmlns:structures="http://release.niem.gov/niem/structures/3.0/"
	exclude-result-prefixes="#all">
	
	<xsl:import href="_formatters.xsl" />
	
	<xsl:output method="html" encoding="UTF-8" />
	
	<xsl:variable name="enforcementOfficialAssociaiton" select="."/>
	
	<xsl:template match="/ccq-res-doc:CourtCaseQueryResults/srm:QueryResultsMetadata">
		<xsl:apply-templates select="qrer:QueryRequestError" />
		<xsl:apply-templates select="iad:InformationAccessDenial" />
	</xsl:template>

	<xsl:template match="/ccq-res-doc:CourtCaseQueryResults">
		<script type="text/javascript">
			$(function () {
		 			$('#courtCaseDetailTabs').tabs({
						activate: function( event, ui ) {
							var modalIframe = $("#modalIframe", parent.document);
							modalIframe.height(modalIframe.contents().find("body").height() + 16);
						}
					}); 
					
					$('#criminalCaseTabs').tabs();
			});
		</script>

		<div id="courtCaseDetailTabs">
			<ul class="courtCaseDetailUl">
				<li>
					<a href="#criminalCaseTab">CRIMINAL CASE</a>
				</li>
				<li>
					<a href="#partyTab">PARTY DETAIL</a>
				</li>
				<li>
					<a href="#chargeTab">CHARGE SUMMARY</a>
				</li>
				<li>
					<a href="#hearingTab">HEARING SUMMARY</a>
				</li>
				<li>
					<a href="#roaListingTab">ROA LISTING</a>
				</li>
				<li>
					<a href="#criminalWarrantTab">CRIMINAL WARRANT HISTORY</a>
				</li>
				<li>
					<a href="#criminalBondTab">CRIMINAL BOND SUMMARY</a>
				</li>
				<li>
					<a href="#victimTab">VICTIMS</a>
				</li>
				<li>
					<a href="#defenseAttorneyTab">DEFENSE ATTORNEYS</a>
				</li>
				<li>
					<a href="#prosecutorTab">PROSECUTORS</a>
				</li>
			</ul>

			<div id="criminalCaseTab">
				<p><xsl:apply-templates select="nc:Case" mode="criminalCase"/></p>	
			</div>
			<div id="partyTab">
				<p>party</p>
			</div>
			<div id="chargeTab">
				<p>charge</p>
			</div>
			<div id="hearingTab">
				<p>hearing</p>
			</div>
			<div id="roaListingTab">
				<p>roalisting</p>
			</div>
			<div id="criminalWarrantTab">
				<p>criminalWarrantTab</p>
			</div>
			<div id="criminalBondTab">
				<p>criminalBondTab</p>
			</div>
			<div id="victimTab">
				<p>victimTab</p>
			</div>
			<div id="defenseAttorneyTab">
				<p>defenseAttorneyTab</p>
			</div>
			<div id="prosecutorTab">
				<p>prosecutorTab</p>
			</div>
		</div>
	</xsl:template>
	
	<xsl:template match="qrer:QueryRequestError">
		<span class="error">System Name: <xsl:value-of select="nc:SystemName" /><br/> Error: <xsl:value-of select="qrer:ErrorText"/></span><br />
	</xsl:template>

	<xsl:template match="nc:Case" mode="criminalCase">
		<div id="criminalCaseTabs">
			<ul>
				<li>
					<a href="#caseInfoTab">Case Information</a>
				</li>
				<li>
					<a href="#defendantInfoTab">Defendant Information</a>
				</li>
			</ul>
			
			<div id="caseInfoTab">
				<p><xsl:apply-templates select="." mode="caseInfo"/></p>	
			</div>
			<div id="defendantInfoTab">
				<p><xsl:apply-templates select="." mode="defendantInfo"/></p>
			</div>
		</div>
	</xsl:template>
	
	<xsl:template match="nc:Case" mode="caseInfo">
		<table class="detailTable">
			<tr>
				<th>Judge</th>
				<td>
					<xsl:for-each select="j:CaseAugmentation/j:CaseJudge/nc:RoleOfPerson/nc:PersonName">
						<xsl:if test="position() > 1"><xsl:text>&#x0A;</xsl:text></xsl:if>
						<xsl:apply-templates select="." mode="firstNameFirst"/>
					</xsl:for-each>
				</td>
				<th>Under Advisement Date</th>
				<td><xsl:apply-templates select="ccq-res-ext:CaseAugmentation/ccq-res-ext:CaseUnderAdvisementDate/nc:Date" mode="formatDateAsMMDDYYYY"/></td>
			</tr>
			<tr>
				<th>Case Subtype</th>
				<td><xsl:value-of select="nc:CaseCategoryText"/></td>
				<th>Domestic Violence</th>
				<td><xsl:value-of select="j:CaseAugmentation/j:CaseDomesticViolenceIndicator"></xsl:value-of></td>
			</tr>
			<tr>
				<th>Court Location</th>
				<td><xsl:value-of select="j:CaseAugmentation/j:CaseCourt/j:CourtDivisionText"/></td>
				<th>Jury Verdict</th>
				<td><xsl:value-of select="ccq-res-ext:CaseAugmentation/ccq-res-ext:JuryVerdictIndicator"/></td>
			</tr>
			<tr>
				<th>Jurisdiction</th>
				<td><xsl:value-of select="j:CaseAugmentation/j:CaseCourt/j:CourtName"/></td>
				<th>Previous Case Number</th>
				<td><xsl:value-of select="j:CaseAugmentation/j:CaseLineageCase/nc:CaseTrackingID"></xsl:value-of></td>
			</tr>
			<tr>
				<th>Filing Date</th>
				<td><xsl:apply-templates select="nc:CaseFiling/child::nc:DocumentCreationDate/nc:DateTime" mode="formatDateTimeAsMMDDYYYY"/></td>
				<th>Other Agency Case Number</th>
				<td><xsl:value-of select="j:CaseAugmentation/j:CaseOtherIdentification/nc:IdentificationID"></xsl:value-of></td>
			</tr>
		</table>
	</xsl:template>
	
	<xsl:template match="nc:Case" mode="defendantInfo">
		<xsl:variable name="defendantID"><xsl:value-of select="j:CaseAugmentation/j:CaseDefendantParty/nc:EntityPerson/@structures:ref"></xsl:value-of></xsl:variable>
		<table class="detailTable">
			<tr>
				<th>Defendant</th>
				<td>
					<xsl:apply-templates select="ancestor::ccq-res-doc:CourtCaseQueryResults/nc:Person[@structures:id = $defendantID]/nc:PersonName" mode="firstNameFirst"></xsl:apply-templates>
				</td>
				<th colspan="2"/>
			</tr>
			<tr>
				<th>Next Appearance</th>
				<td>No Mapping</td>
				<th colspan="2"/>
			</tr>
			<tr>
				<th>Trial By</th>
				<td>No Mapping</td>
				<th>Money Due</th>
				<td>No Mapping</td>
			</tr>
			<tr>
				<th>Speedy Trial</th>
				<td><xsl:apply-templates select="j:CaseAugmentation/j:CaseTrial/ccq-res-ext:SpeedyTrialDate/nc:Date" mode="formatDateAsMMDDYYYY"/></td>
				<th>Extension Date</th>
				<td>No Mapping</td>
			</tr>
			<tr>
				<th>FTP Hold Indefinite</th>
				<td>No Mapping</td>
				<th>Amount Due</th>
				<td>No Mapping</td>
			</tr>
			<tr>
				<th>FTA Hold Date</th>
				<td><xsl:apply-templates select="ccq-res-ext:CaseAugmentation/ccq-res-ext:FailureToAppearHoldDate/nc:Date" mode="formatDateAsMMDDYYYY"/></td>
				<th>FTP Hold Date</th>
				<td><xsl:apply-templates select="ccq-res-ext:CaseAugmentation/ccq-res-ext:FailureToPayHoldDate/nc:Date" mode="formatDateAsMMDDYYYY"/></td>
			</tr>
			<tr>
				<th>FTC Hold Date</th>
				<td><xsl:apply-templates select="ccq-res-ext:CaseAugmentation/ccq-res-ext:FailureToComplyHoldDate/nc:Date" mode="formatDateAsMMDDYYYY"/></td>
				<th>FTPV Hold Date</th>
				<td><xsl:apply-templates select="ccq-res-ext:CaseAugmentation/ccq-res-ext:FailureToPayVictimHoldDate/nc:Date" mode="formatDateAsMMDDYYYY"/></td>
			</tr>
			<tr>
				<th>Lead Attorney</th>
				<td><xsl:apply-templates select="j:CaseAugmentation/j:CaseDefenseAttorney/nc:RoleOfPerson/nc:PersonName" mode="firstNameFirst"/></td>
				<th>Attorney Waived</th>
				<td><xsl:value-of select="j:CaseAugmentation/j:CaseDefendantSelfRepresentationIndicator"></xsl:value-of></td>
			</tr>
			<tr>
				<th>FPC Member</th>
				<td>No Mapping</td>
				<th>Custody Status</th>
				<td><xsl:value-of select="parent::ccq-res-doc:CourtCaseQueryResults/j:Detention/nc:SupervisionCustodyStatus/nc:StatusDescriptionText"/></td>
			</tr>
			<tr>
				<th>Epayments Not Allowed</th>
				<td>No Mapping</td>
				<th>Interest Start Date</th>
				<td>No Mapping</td>
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
		</table>
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
		</table>
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
		</table>
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
		</table>
		
	</xsl:template>
	
</xsl:stylesheet>
