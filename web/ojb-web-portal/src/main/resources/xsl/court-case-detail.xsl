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
	xmlns:cyfs="http://release.niem.gov/niem/domains/cyfs/3.1/"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
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
					var modalIframe = $("#modalIframe", parent.document);
					modalIframe.height(modalIframe.contents().find("body").height() + 16);
					
					$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
						var modalIframe = $("#modalIframe", parent.document);
						modalIframe.height(modalIframe.contents().find("body").height() + 16);
					})
					
					$('#criminalCaseTabs').tabs();
					$('#partyTabs').tabs();
					$('.detailDataTable').DataTable({
 						"dom": 'rt',
 						responsive: true, 
 						colReorder: true 
					});
			});
		</script>

		<ul class="nav nav-tabs" id="myTab" role="tablist">
		  <li class="nav-item">
		    <a class="nav-link active" id="criminalCaseTab" data-toggle="tab" href="#criminalCase" role="tab" aria-controls="criminalCase" aria-selected="true">CRIMINAL CASE</a>
		  </li>
		  <li class="nav-item">
		    <a class="nav-link" id="partyTab" data-toggle="tab" href="#party" role="tab" aria-controls="party" aria-selected="false">PARTY DETAIL</a>
		  </li>
		  <li class="nav-item">
		    <a class="nav-link" id="chargeTab" data-toggle="tab" href="#charge" role="tab" aria-controls="charge" aria-selected="false">CHARGE SUMMARY</a>
		  </li>
		  <li class="nav-item">
		    <a class="nav-link" id="hearingTab" data-toggle="tab" href="#hearing" role="tab" aria-controls="hearing" aria-selected="false">HEARING SUMMARY</a>
		  </li>
		  <li class="nav-item">
		    <a class="nav-link" id="roaListingTab" data-toggle="tab" href="#roaListing" role="tab" aria-controls="roaListing" aria-selected="false">ROA LISTING</a>
		  </li>
		  <li class="nav-item">
		    <a class="nav-link" id="criminalWarrantTab" data-toggle="tab" href="#criminalWarrant" role="tab" aria-controls="criminalWarrant" aria-selected="false">CRIMINAL WARRANT</a>
		  </li>
		  <li class="nav-item">
		    <a class="nav-link" id="criminalBondTab" data-toggle="tab" href="#criminalBond" role="tab" aria-controls="criminalBond" aria-selected="false">CRIMINAL BOND</a>
		  </li>
		  <li class="nav-item">
		    <a class="nav-link" id="victimTab" data-toggle="tab" href="#victim" role="tab" aria-controls="victim" aria-selected="false">VICTIMS</a>
		  </li>
		  <li class="nav-item">
		    <a class="nav-link" id="defenseAttorneyTab" data-toggle="tab" href="#defenseAttorney" role="tab" aria-controls="defenseAttorney" aria-selected="false">DEFENSE ATTORNEYS</a>
		  </li>
		  <li class="nav-item">
		    <a class="nav-link" id="prosecutorTab" data-toggle="tab" href="#prosecutor" role="tab" aria-controls="prosecutor" aria-selected="false">PROSECUTORS</a>
		  </li>
		</ul>
		<div class="tab-content" id="myTabContent">
		  <div class="tab-pane fade show active" id="criminalCase" role="tabpanel" aria-labelledby="criminalCaseTab">
		  	<p><xsl:apply-templates select="nc:Case" mode="criminalCase"/></p>	
		  </div>
		  <div class="tab-pane fade" id="party" role="tabpanel" aria-labelledby="partyTab">
		  	<p><xsl:apply-templates select="." mode="party"/></p>
		  </div>
		  <div class="tab-pane fade" id="charge" role="tabpanel" aria-labelledby="chargeTab">
		  	<p><xsl:apply-templates select="nc:Case" mode="chargeSummary"/></p>
		  </div>
		  <div class="tab-pane fade" id="hearing" role="tabpanel" aria-labelledby="hearingTab">
		  	<p><xsl:apply-templates select="nc:Case/j:CaseAugmentation" mode="hearing"/></p>
		  </div>
		  <div class="tab-pane fade" id="roaListing" role="tabpanel" aria-labelledby="roaListingTab">
		  	<p><xsl:apply-templates select="nc:Case/j:CaseAugmentation" mode="ROA"/></p>
		  </div>
		  <div class="tab-pane fade" id="criminalWarrant" role="tabpanel" aria-labelledby="criminalWarrantTab">
		  	<p><xsl:apply-templates select="." mode="warrants"/></p>
		  </div>
		  <div class="tab-pane fade" id="criminalBond" role="tabpanel" aria-labelledby="criminalBondTab">
		  	<p><xsl:apply-templates select="." mode="bonds"/></p>
		  </div>
		  <div class="tab-pane fade" id="victim" role="tabpanel" aria-labelledby="victimTab">
		  	<p><xsl:apply-templates select="." mode="victims"/></p>
		  </div>
		  <div class="tab-pane fade" id="defenseAttorney" role="tabpanel" aria-labelledby="defenseAttorneyTab">
		  	<p><xsl:apply-templates select="nc:Case/j:CaseAugmentation/j:CaseDefenseAttorney"></xsl:apply-templates></p>
		  </div>
		  <div class="tab-pane fade" id="prosecutor" role="tabpanel" aria-labelledby="prosecutorTab">
		  	<p><xsl:apply-templates select="nc:Case/j:CaseAugmentation" mode="prosecutors"></xsl:apply-templates></p>
		  </div>
		</div>
	</xsl:template>
	
	<xsl:template match="qrer:QueryRequestError">
		<span class="error">System Name: <xsl:value-of select="nc:SystemName" /><br/> Error: <xsl:value-of select="qrer:ErrorText"/></span><br />
	</xsl:template>
	
	<xsl:template match="j:CaseAugmentation" mode="prosecutors">
		<table class="detailDataTable table table-striped table-bordered mt-2 nowrap" style="width:100%">
			<thead>
				<tr>
					<th>Attorney Name</th>
					<th>Attorney Bar Number</th>
					<th>Attorney Type</th>
				</tr>
			</thead>
			<tbody>
				<xsl:apply-templates select="j:CaseProsecutionAttorney"/>
			</tbody>
		</table>
	</xsl:template>
	
	<xsl:template match="j:CaseProsecutionAttorney">
		<tr>
			<td><xsl:value-of select="nc:RoleOfPerson/nc:PersonName/nc:PersonFullName"/></td>
			<td><xsl:value-of select="j:JudicialOfficialBarMembership/j:JudicialOfficialBarIdentification/nc:IdentificationID"/></td>
			<td><xsl:value-of select="j:JudicialOfficialCategoryText"/></td>
		</tr>
	</xsl:template>
	
	<xsl:template match="j:CaseDefenseAttorney">
		<table class="detailTable table table-striped table-bordered mt-2 nowrap" style="width:100%">
			<tr>
				<th>
					<label>Lead Attorney: </label>
					<xsl:value-of select="nc:RoleOfPerson/nc:PersonName/nc:PersonFullName" />
				</th>
			</tr>
		</table>
	</xsl:template>
	
	<xsl:template match="ccq-res-doc:CourtCaseQueryResults" mode="victims">
		<table class="detailDataTable table table-striped table-bordered mt-2 nowrap" style="width:100%">
			<thead>
				<tr>
					<th>Victim Name</th>
					<th>Victim Restitution</th>
					<th>Restitution Paid</th>
					<th>Balance</th>
				</tr>
			</thead>
			<tbody>
				<xsl:apply-templates select="j:Victim"/>
			</tbody>
		</table>
	</xsl:template>
	
	<xsl:template match="j:Victim">
		<xsl:variable name="personVictimId"><xsl:value-of select="nc:RoleOfPerson/@structures:id"/></xsl:variable>
		<xsl:variable name="restitutionId"><xsl:value-of select="/ccq-res-doc:CourtCaseQueryResults/j:ObligationPersonAssociation[contains(string-join(nc:Person/@structures:ref,'|'), $personVictimId)]/nc:Obligation/@structures:ref"/></xsl:variable>
		<tr>
			<td><xsl:value-of select="nc:RoleOfPerson/nc:PersonName/nc:PersonFullName"/></td>
			<td><xsl:value-of select="j:VictimSeeksRestitutionDescriptionText"/></td>
			<td><xsl:value-of select="parent::ccq-res-doc:CourtCaseQueryResults/nc:DisciplinaryActionRestitution[@structures:id=$restitutionId]/nc:ObligationPaidAmount/nc:Amount"/></td>
			<td><xsl:value-of select="parent::ccq-res-doc:CourtCaseQueryResults/nc:DisciplinaryActionRestitution[@structures:id=$restitutionId]/nc:ObligationDueAmount/nc:Amount"/></td>
		</tr>	
	</xsl:template>
	
	<xsl:template match="ccq-res-doc:CourtCaseQueryResults" mode="bonds">
		<table class="detailDataTable table table-striped table-bordered mt-2 nowrap" style="width:100%">
			<thead>
				<tr>
					<th>Bond Type</th>
					<th>Bond Number</th>
					<th>Bond Amount</th>
					<th>Bonding Agency</th>
				</tr>
			</thead>
			<tbody>
				<xsl:apply-templates select="j:BailBond"/>
			</tbody>
		</table>
	</xsl:template>
	
	<xsl:template match="j:BailBond">
		<tr>
			<td><xsl:value-of select="nc:ActivityCategoryText"/></td>
			<td><xsl:value-of select="nc:ActivityIdentification/nc:IdentificationID"/></td>
			<td><xsl:value-of select="j:BailBondAmount/nc:Amount"/></td>
			<td><xsl:value-of select="j:BailBondIssuerEntity/nc:EntityOrganization/nc:OrganizationName"/></td>
		</tr>	
	</xsl:template>
	
	<xsl:template match="ccq-res-doc:CourtCaseQueryResults" mode="warrants">
		<table class="detailDataTable table table-striped table-bordered mt-2 nowrap" style="width:100%">
			<thead>
				<tr>
					<th>Warrant Number</th>
					<th>Warrant Type</th>
					<th>Issue Date</th>
					<th>Description</th>
				</tr>
			</thead>
			<tbody>
				<xsl:apply-templates select="j:Warrant"/>
			</tbody>
		</table>
	</xsl:template>
	
	<xsl:template match="j:Warrant">
		<tr>
			<td><xsl:value-of select="nc:ActivityIdentification/nc:IdentificationID"/></td>
			<td><xsl:value-of select="j:WarrantCategoryText"/></td>
			<td><xsl:apply-templates select="j:CourtOrderIssuingDate/nc:DateTime" mode="formatDateTimeAsMMDDYYYY"/></td>
			<td><xsl:value-of select="nc:ActivityDescriptionText"/></td>
		</tr>	
	</xsl:template>
	
	<xsl:template match="j:CaseAugmentation" mode="ROA">
		<table class="detailDataTable table table-striped table-bordered mt-2 nowrap" style="width:100%">
			<thead>
				<tr>
					<th>Court Event ID</th>
					<th>Event Date</th>
					<th>Event Name</th>
					<th>Event Description</th>
					<th>Judge Name</th>
					<th>Comments</th>
				</tr>
			</thead>
			<tbody>
				<xsl:apply-templates select="j:CaseCourtEvent"/>
			</tbody>
		</table>
	</xsl:template>
	
	<xsl:template match="j:CaseCourtEvent">
		<tr>
			<td><xsl:value-of select="nc:ActivityIdentification/nc:IdentificationID"></xsl:value-of></td>
			<td><xsl:apply-templates select="nc:ActivityDate/nc:Date" mode="formatDateAsMMDDYYYY"/></td>
			<td><xsl:value-of select="nc:ActivityName"/></td>
			<td><xsl:value-of select="nc:ActivityDescriptionText"/></td>
			<td><xsl:value-of select="j:CourtEventJudge/nc:RoleOfPerson/nc:PersonName/nc:PersonFullName"/></td>
			<td><xsl:value-of select="ccq-res-ext:CourtEventCommentsText"/></td>
		</tr>
	</xsl:template>
	
	<xsl:template match="j:CaseAugmentation" mode="hearing">
		<table class="detailDataTable table table-striped table-bordered mt-2 nowrap" style="width:100%">
			<thead>
				<tr>
					<th>Hearing Type</th>
					<th>Hearing Reason</th>
					<th>Court Case #</th>
					<th>Judge</th>
					<th>Court Room</th>
					<th>Start Date</th>
					<th>End Date</th>
					<th>Hearing Minutes</th>
					<th>Result</th>
					<th>Comments</th>
				</tr>
			</thead>
			<tbody>
				<xsl:apply-templates select="j:CaseHearing"/>
			</tbody>
		</table>
	</xsl:template>

	<xsl:template match="j:CaseHearing">
		<tr>
			<td><xsl:value-of select="nc:ActivityCategoryText"/></td>
			<td><xsl:value-of select="nc:ActivityReasonText"/></td>
			<td><xsl:value-of select="nc:ActivityName"/></td>
			<td><xsl:value-of select="j:CourtEventJudge/nc:RoleOfPerson/nc:PersonName/nc:PersonFullName"/></td>
			<td><xsl:value-of select="j:CourtEventAppearance/j:CourtAppearanceCourt/j:CourtName"/></td>
			<td><xsl:apply-templates select="nc:ActivityDateRange/nc:StartDate/nc:DateTime" mode="formatDateTime"/></td>
			<td><xsl:apply-templates select="nc:ActivityDateRange/nc:EndDate/nc:DateTime" mode="formatDateTime"/></td>
			<td><xsl:value-of select="ccq-res-ext:CourtEventCommentsText"/></td>
			<td><xsl:value-of select="nc:ActivityDisposition/nc:DispositionDescriptionText"/></td>
			<td><xsl:value-of select="nc:ActivityDescriptionText"/></td>
		</tr>	
	</xsl:template>
	
	<xsl:template match="nc:Case" mode="chargeSummary">
		<table class="detailDataTable table table-striped table-bordered mt-2 nowrap" style="width:100%">
			<thead>
				<tr>
					<th>Charge</th>
					<th>Count</th>
					<th>Statute Number</th>
					<th>Statute Description</th>
					<th>Charge Description</th>
					<th>Charge Date</th>
				</tr>
			</thead>
			<tbody>
				<xsl:apply-templates select="j:CaseAugmentation/j:CaseCharge"></xsl:apply-templates>
			</tbody>
		</table>
		<xsl:if test="j:CaseAugmentation/j:CaseAmendedCharge[normalize-space()]">
			<pre>
				
				
				
			</pre>
			<table class="detailDataTable table table-striped table-bordered mt-2 nowrap" style="width:100%">
				<thead>
					<tr>
						<th>Amended Charge Count</th>
						<th>Amended Statute</th>
						<th>Amended Statute Description</th>
						<th>Amended Charge Description</th>
						<th>Amended Date</th>
					</tr>
				</thead>
				<tbody>
					<xsl:apply-templates select="j:CaseAugmentation/j:CaseAmendedCharge"></xsl:apply-templates>
				</tbody>
			</table>
		</xsl:if>
	</xsl:template>
		
	<xsl:template match="j:CaseCharge">
		<tr>
			<td><xsl:value-of select="j:ChargeSequenceID"/></td>
			<td><xsl:value-of select="j:ChargeCountQuantity"/></td>
			<td><xsl:value-of select="j:ChargeStatute/j:StatuteCodeIdentification/nc:IdentificationID"/></td>
			<td><xsl:value-of select="j:ChargeStatute/j:StatuteCodeIdentification/nc:IdentificationCategoryDescriptionText"/></td>
			<td><xsl:value-of select="j:ChargeDescriptionText"/></td>
			<td><xsl:apply-templates select="j:ChargeFilingDate/nc:Date" mode="formatDateAsMMDDYYYY"/></td>
		</tr>
	</xsl:template>
	
	<xsl:template match="j:CaseAmendedCharge">
		<tr>
			<td><xsl:value-of select="j:ChargeCountQuantity"/></td>
			<td><xsl:value-of select="j:ChargeStatute/j:StatuteCodeIdentification/nc:IdentificationID"/></td>
			<td><xsl:value-of select="j:ChargeStatute/j:StatuteCodeIdentification/nc:IdentificationCategoryDescriptionText"/></td>
			<td><xsl:value-of select="j:ChargeDescriptionText"/></td>
			<td><xsl:apply-templates select="j:ChargeFilingDate/nc:Date" mode="formatDateAsMMDDYYYY"/></td>
		</tr>
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
				<th>
					<label>Judge: </label>
					<xsl:for-each select="j:CaseAugmentation/j:CaseJudge/nc:RoleOfPerson/nc:PersonName">
						<xsl:if test="position() > 1"><xsl:text>&#x0A;</xsl:text></xsl:if>
						<xsl:apply-templates select="." mode="firstNameFirst"/>
					</xsl:for-each>
				</th>
				<th>
					<label>Under Advisement Date: </label>
					<xsl:apply-templates select="ccq-res-ext:CaseAugmentation/ccq-res-ext:CaseUnderAdvisementDate/nc:Date" mode="formatDateAsMMDDYYYY"/>
				</th>
			</tr>
			<tr>
				<th>
					<label>Case Type: </label>
					<xsl:value-of select="nc:CaseGeneralCategoryText"/></th>
				<th>
					<label>Domestic Violence: </label>
					<xsl:value-of select="j:CaseAugmentation/j:CaseDomesticViolenceIndicator"></xsl:value-of></th>
			</tr>
			<tr>
				<th>
					<label>Case Subtype: </label>
					<xsl:value-of select="nc:CaseCategoryText"/></th>
				<th><label>Jury Verdict: </label>
				<xsl:value-of select="ccq-res-ext:CaseAugmentation/ccq-res-ext:JuryVerdictIndicator"/></th>
			</tr>
			<tr>
				<th>
					<label>Court Location: </label>
					<xsl:value-of select="j:CaseAugmentation/j:CaseCourt/j:CourtDivisionText"/>
				</th>
				<th><label>Previous Case Number: </label>
				<xsl:value-of select="j:CaseAugmentation/j:CaseLineageCase/nc:CaseTrackingID"></xsl:value-of></th>
			</tr>
			<tr>
				<th><label>Jurisdiction: </label>
				<xsl:value-of select="j:CaseAugmentation/j:CaseCourt/j:OrganizationAugmentation/j:OrganizationJurisdiction/nc:JurisdictionText"/></th>
				<th><label>Other Agency Case Number: </label>
				<xsl:value-of select="j:CaseAugmentation/j:CaseOtherIdentification/nc:IdentificationID"></xsl:value-of></th>
			</tr>
			<tr>
				<th colspan='2'><label>Filing Date: </label>
				<xsl:apply-templates select="nc:CaseFiling/child::nc:DocumentCreationDate/nc:DateTime" mode="formatDateTimeAsMMDDYYYY"/></th>
			</tr>
		</table>
	</xsl:template>
	
	<xsl:template match="nc:Case" mode="defendantInfo">
		<xsl:variable name="defendantID"><xsl:value-of select="j:CaseAugmentation/j:CaseDefendantParty/nc:EntityPerson/@structures:ref"></xsl:value-of></xsl:variable>
		<table class="detailTable">
			<tr>
				<th><label>Defendant: </label>
				
					<xsl:apply-templates select="ancestor::ccq-res-doc:CourtCaseQueryResults/nc:Person[@structures:id = $defendantID]/nc:PersonName" mode="firstNameFirst"></xsl:apply-templates>
				</th>
				<th colspan="2"/>
			</tr>
			<tr>
				<th>
					<label>Next Appearance: </label>
					<xsl:apply-templates select="j:CaseAugmentation/j:CaseCourtEvent/nc:ActivityDate/nc:Date" mode="formatDateAsMMDDYYYY"/>
				</th>
				<th colspan="2"/>
			</tr>
			<tr>
				<th><label>Trial By: </label>
					<xsl:value-of select="j:CaseAugmentation/j:CaseTrial/ccq-res-ext:TrialByText"></xsl:value-of></th>
				<th><label>Custody Status: </label>
					<xsl:value-of select="parent::ccq-res-doc:CourtCaseQueryResults/j:Detention/nc:SupervisionCustodyStatus/nc:StatusDescriptionText"/></th>
			</tr>
			<tr>
				<th><label>Speedy Trial: </label>
				<xsl:apply-templates select="j:CaseAugmentation/j:CaseTrial/ccq-res-ext:SpeedyTrialDate/nc:Date" mode="formatDateAsMMDDYYYY"/></th>
				<th><label>FTP Hold Indefinite: </label>
					<xsl:value-of select="ccq-res-ext:CaseAugmentation/ccq-res-ext:FailureToPayHoldIndefiniteIndicator"></xsl:value-of></th>
			</tr>
			<tr>
				<th><label>FTA Hold Date: </label>
				<xsl:apply-templates select="ccq-res-ext:CaseAugmentation/ccq-res-ext:FailureToAppearHoldDate/nc:Date" mode="formatDateAsMMDDYYYY"/></th>
				<th><label>FTP Hold Date: </label>
				<xsl:apply-templates select="ccq-res-ext:CaseAugmentation/ccq-res-ext:FailureToPayHoldDate/nc:Date" mode="formatDateAsMMDDYYYY"/></th>
			</tr>
			<tr>
				<th><label>FTC Hold Date: </label>
				<xsl:apply-templates select="ccq-res-ext:CaseAugmentation/ccq-res-ext:FailureToComplyHoldDate/nc:Date" mode="formatDateAsMMDDYYYY"/></th>
				<th><label>FTPV Hold Date: </label>
				<xsl:apply-templates select="ccq-res-ext:CaseAugmentation/ccq-res-ext:FailureToPayVictimHoldDate/nc:Date" mode="formatDateAsMMDDYYYY"/></th>
			</tr>
			<tr>
				<th><label>Lead Attorney: </label>
				<xsl:apply-templates select="j:CaseAugmentation/j:CaseDefenseAttorney/nc:RoleOfPerson/nc:PersonName" mode="firstNameFirst"/></th>
				<th><label>Attorney Waived: </label>
				<xsl:value-of select="j:CaseAugmentation/j:CaseDefendantSelfRepresentationIndicator"></xsl:value-of></th>
			</tr>
		</table>
	</xsl:template>
	
	<xsl:template match="ccq-res-doc:CourtCaseQueryResults" mode="party">
		<xsl:variable name="defendantID"><xsl:value-of select="nc:Case/j:CaseAugmentation/j:CaseDefendantParty/nc:EntityPerson/@structures:ref"/></xsl:variable>
		<div id="partyTabs">
			<ul>
				<li>
					<a href="#partyInfoTab">Party Information</a>
				</li>
				<li>
					<a href="#licenseInfoTab">License Information</a>
				</li>
				<li>
					<a href="#idAndPaymentInfoTab">ID And Payment Information</a>
				</li>
				<li>
					<a href="#commentsTab">Comments</a>
				</li>
			</ul>
			
			<div id="partyInfoTab">
				<p><xsl:apply-templates select="nc:Person[@structures:id = $defendantID]" mode="partyInfo"/></p>	
			</div>
			<div id="licenseInfoTab">
				<p><xsl:apply-templates select="nc:Person[@structures:id = $defendantID]/j:PersonAugmentation/j:DriverLicense"/></p>
			</div>
			<div id="idAndPaymentInfoTab">
				<p><xsl:apply-templates select="nc:Person[@structures:id = $defendantID]" mode="idAndPaymentInfo"/></p>
			</div>
			<div id="commentsTab">
				<p><xsl:apply-templates select="nc:Person[@structures:id = $defendantID]" mode="partyComments"/></p>
			</div>
		</div>
	</xsl:template>
	
	<xsl:template match="nc:Person" mode="partyInfo">
		<xsl:variable name="personId"><xsl:value-of select="@structures:id"></xsl:value-of></xsl:variable>
		<xsl:variable name="locationId">
			<xsl:value-of select="parent::ccq-res-doc:CourtCaseQueryResults/nc:PersonResidenceAssociation[nc:Person/@structures:ref=$personId]/nc:Location/@structures:ref"/></xsl:variable>
		<xsl:variable name="contactInfoId">
			<xsl:value-of select="parent::ccq-res-doc:CourtCaseQueryResults/nc:ContactInformationAssociation[nc:ContactEntity/@structures:ref=$personId]/nc:ContactInformation/@structures:ref"/></xsl:variable>
		
		<table class="detailTable">
			<tr>
				<th colspan="2">
					<label>State ID: </label>
					<xsl:value-of select="j:PersonAugmentation/j:PersonStateFingerprintIdentification/nc:IdentificationID"/>
				</th>
			</tr>
			<tr>
				<th><label>Last Name: </label>
					<xsl:value-of select="nc:PersonName/nc:PersonSurName"/>
				</th>
				<th><label>Company: </label>
					<xsl:value-of select="preceding-sibling::nc:Case/j:CaseAugmentation/j:CaseDefendantParty/nc:EntityOrganization/nc:OrganizationName"></xsl:value-of>
				</th>
			</tr>
			<tr>
				<th><label>First Name: </label>
					<xsl:value-of select="nc:PersonName/nc:PersonGivenName"/></th>
				<th colspan="2"/>		
			</tr>
			<tr>
				<th>
					<label>Middle Name: </label>
					<xsl:value-of select="nc:PersonName/nc:PersonMiddleName"/></th>
				<th>
					<label>SSN: </label>
					<xsl:value-of select="nc:PersonSSNIdentification/nc:IdentificationID"></xsl:value-of>
				</th>
			</tr>
			<tr>
				<th>
					<label>Suffix: </label>
					<xsl:value-of select="nc:PersonName/nc:PersonNameSuffixText"></xsl:value-of>	
				</th>
				<th>
					<label>DOB: </label>
					<xsl:apply-templates select="nc:PersonBirthDate/nc:Date" mode="formatDateAsMMDDYYYY"></xsl:apply-templates>
				</th>
			</tr>
			<tr>
				<th><label>Sex: </label>
					<xsl:value-of select="j:PersonSexCode"/>
				</th>
				<th><label>Eyes: </label>
					<xsl:value-of select="j:PersonEyeColorCode"/></th>
			</tr>
			<tr>
				<th><label>Hair: </label>
					<xsl:value-of select="j:PersonHairColorCode"/></th>
				<th><label>Weight: </label>
					<xsl:value-of select="nc:PersonWeightMeasure/child::nc:MeasureValueText"></xsl:value-of>
					<xsl:text> </xsl:text>
					<xsl:value-of select="nc:PersonWeightMeasure/child::nc:MeasureUnitText"></xsl:value-of>
				</th>
			</tr>
				<th><label>Height: </label>
					<xsl:value-of select="nc:PersonHeightMeasure/nc:MeasureValueText"/><xsl:text> </xsl:text>
					<xsl:value-of select="nc:PersonHeightMeasure/nc:MeasureUnitText"></xsl:value-of>
				</th>
				<th><label>Language: </label>
					<xsl:value-of select="nc:PersonPrimaryLanguage/nc:LanguageName"></xsl:value-of>
				</th>
			<tr>
				<th><label>Race: </label>
					<xsl:value-of select="nc:PersonEthnicityText"></xsl:value-of>	
				</th>
				<th colspan="2"><label>Juvenile: </label>
					<xsl:variable name="sortedCitationDates">
	         		 	<xsl:perform-sort select="parent::ccq-res-doc:CourtCaseQueryResults/j:Citation/j:CitationViolation/nc:ActivityDate/nc:Date">
					     <xsl:sort select="."/>
					  </xsl:perform-sort>
       				 </xsl:variable>
       				 <xsl:variable name="latestCitationDate" select="$sortedCitationDates/nc:Date[last()]"/>
					<xsl:call-template name="getJuvenileIndicator">
						<xsl:with-param name="birthDate" select="nc:PersonBirthDate/nc:Date"/>
						<xsl:with-param name="violationDate" select="$latestCitationDate"/>					
					</xsl:call-template>	
				</th>
			</tr>
		</table>
		
		<xsl:if test="$locationId">
			<pre>
				
			</pre>
			<table class="detailDataTable table table-striped table-bordered mt-2 nowrap" style="width:100%">
				<thead>
					<tr>
						<th>Address Type</th>
						<th>Address</th>
						<th>Address Line 2</th>
						<th>City</th>
						<th>State</th>
						<th>Zip</th>
					</tr>
				</thead>
				<tbody>
					<xsl:apply-templates select="parent::ccq-res-doc:CourtCaseQueryResults/nc:Location[contains(string-join($locationId, '|'), @structures:id)]"/>
				</tbody>
			</table>
		</xsl:if>
		
		<xsl:if test="$contactInfoId">
			<pre>
				
			</pre>
			<table class="detailDataTable table table-striped table-bordered mt-2 nowrap" style="width:100%">
				<thead>
					<tr>
						<th>Telephone #</th>
						<th>Type of #</th>
						<th>Electronic Contact Type</th>
						<th>Electronic Contact</th>
					</tr>
				</thead>
				<tbody>
					<xsl:apply-templates select="parent::ccq-res-doc:CourtCaseQueryResults/nc:ContactInformation[contains(string-join($contactInfoId, '|'), @structures:id)]"/>
				</tbody>
			</table>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="nc:Location">
		<tr>
			<td><xsl:value-of select="nc:LocationCategoryText"/></td>
			<td><xsl:value-of select="nc:Address/nc:LocationStreet/nc:StreetFullText"/></td>
			<td><xsl:value-of select="nc:Address/nc:AddressSecondaryUnitText"/></td>
			<td><xsl:value-of select="nc:Address/nc:LocationCityName"/></td>
			<td><xsl:value-of select="nc:Address/nc:LocationStateName"/></td>
			<td><xsl:value-of select="nc:Address/nc:LocationPostalCode"/></td>
		</tr>
	</xsl:template>
	
	<xsl:template match="nc:ContactInformation">
		<tr>
			<td><xsl:value-of select="nc:ContactTelephoneNumber/nc:FullTelephoneNumber/nc:TelephoneNumberFullID"/></td>
			<td><xsl:value-of select="nc:ContactTelephoneNumber/nc:TelephoneNumberCategoryText"/></td>
			<td><xsl:value-of select="ccq-res-ext:ElectronicContactIdentification/nc:IdentificationCategoryDescriptionText"/></td>
			<td><xsl:value-of select="ccq-res-ext:ElectronicContactIdentification/nc:IdentificationID"/></td>
		</tr>
	</xsl:template>
	
	<xsl:template match="j:DriverLicense">
		<table class="detailTable">
			<tr>
				<th>
					<label>License Number: </label>
					<xsl:value-of select="j:DriverLicenseIdentification/nc:IdentificationID"/>
				</th>
				<th>
					<label>License State: </label>
					<xsl:value-of select="j:DriverLicenseIdentification/nc:IdentificationSourceText"/>
				</th>
				<th>
					<label>Commercial: </label>
					<xsl:value-of select="j:DriverLicenseCommercialClassText"/>
				</th>
			</tr>
			<tr>
				<th><label>License Class: </label>
					<xsl:value-of select="j:DriverLicenseNonCommercialClassText"/>
				</th>
				<th><label>License Issued: </label>
					<xsl:apply-templates select="j:DriverLicenseIssueDate/nc:DateTime" mode="formatDateTimeAsMMDDYYYY"></xsl:apply-templates>
				</th>
				<th><label>License Expires: </label>
					<xsl:apply-templates select="j:DriverLicenseExpirationDate/nc:DateTime" mode="formatDateTimeAsMMDDYYYY"></xsl:apply-templates>
				</th>
			</tr>
		</table>
	</xsl:template>
	
	<xsl:template match="nc:Person" mode="idAndPaymentInfo">
		<table class="detailTable">
			<tr>
				<th colspan="3">
					<label>Party ID: </label>
					<xsl:value-of select="intel:PersonAugmentation/intel:PersonSystemIdentification/nc:IdentificationID"/>
				</th>
			</tr>
			<tr>
				<th><label>Juvenile ID: </label>
					<xsl:value-of select="cyfs:PersonAugmentation/cyfs:StudentIdentifiation/nc:IdentificationID"></xsl:value-of>
				</th>
				<th><label>Inmate ID: </label>
					<xsl:value-of select="ccq-res-ext:PersonInmateIdentification/nc:IdentificationID"></xsl:value-of>
				</th>
				<th><label>Other ID: </label>
					<xsl:value-of select="nc:PersonOtherIdentification/nc:IdentificationID"></xsl:value-of>
				</th>
			</tr>
		</table>
	</xsl:template>
	
	<xsl:template match="nc:Person" mode="partyComments">
		<table class="detailTable">
			<tr>
				<th>
					<label>Body Marks: </label>
					<xsl:value-of select="nc:PersonPhysicalFeature/nc:PhysicalFeatureDescriptionText"></xsl:value-of>
				</th>
			</tr>
			<tr>
				<th>
					<label>Clerk Comments: </label>
					<xsl:value-of select="parent::ccq-res-doc:CourtCaseQueryResults/nc:Case/j:CaseAugmentation/j:CaseCourtEvent/ccq-res-ext:CommentsForCourtClerk"></xsl:value-of>
				</th>
			</tr>
		</table>
	</xsl:template>
		
	<xsl:template name="getJuvenileIndicator">
		<xsl:param name="birthDate"/>
		<xsl:param name="violationDate"/>
		
		<xsl:variable name="age" as="xs:integer">
			<xsl:choose>
				<xsl:when test="month-from-date($violationDate) > month-from-date($birthDate) or month-from-date($violationDate) = month-from-date($birthDate) 
					and day-from-date($violationDate) >= day-from-date($birthDate)">
					<xsl:value-of select="year-from-date($violationDate) - year-from-date($birthDate)" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="year-from-date($violationDate) - year-from-date($birthDate) - 1" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		
		<xsl:value-of select="$age &lt; xs:integer(18)"/>
	</xsl:template>
</xsl:stylesheet>
