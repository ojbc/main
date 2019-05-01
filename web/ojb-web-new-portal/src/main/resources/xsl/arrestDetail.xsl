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
	xmlns:chsres-doc="http://ojbc.org/IEPD/Exchange/CriminalHistorySearchResults/1.0"
	xmlns:chsres-ext="http://ojbc.org/IEPD/Extensions/CriminalHistorySearchResults/1.0"
	xmlns:j="http://release.niem.gov/niem/domains/jxdm/6.0/" xmlns:intel="http://release.niem.gov/niem/domains/intelligence/4.0/"
	xmlns:nc="http://release.niem.gov/niem/niem-core/4.0/" xmlns:ncic="http://release.niem.gov/niem/codes/fbi_ncic/4.0/"
	xmlns:niem-xs="http://release.niem.gov/niem/proxy/xsd/4.0/" xmlns:structures="http://release.niem.gov/niem/structures/4.0/"
  xmlns:iad="http://ojbc.org/IEPD/Extensions/InformationAccessDenial/1.0"
	xmlns:srm="http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0"
	xmlns:srer="http://ojbc.org/IEPD/Extensions/SearchRequestErrorReporting/1.0"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://ojbc.org/IEPD/Exchange/CriminalHistorySearchResults/1.0 ../xsd/criminal_history_search_results.xsd"
	exclude-result-prefixes="#all">

	<xsl:import href="_formatters.xsl" />
	<xsl:output method="html" encoding="UTF-8" />
	
	<xsl:template match="/chsres-doc:CriminalHistorySearchResults/chsres-ext:CriminalHistorySearchResult">
		  <xsl:call-template name="arrests"/>
	</xsl:template>

	<xsl:template name="arrests">
			<table class="table table-striped table-bordered" style="width:100%" id="searchResultsTable">
        <xsl:attribute name="arrestId">
          <xsl:value-of select="normalize-space(intel:SystemIdentification/nc:IdentificationID)"/>
        </xsl:attribute>
			
				<thead>
					<tr>
            <th>NAME
              <a href="#" data-toggle="popover" data-trigger="hover" 
                data-content="The name on the fingerprint card at the time of arrest.">
                <i class="fas fa-info-circle"></i></a>
            </th>
            <th>DOB
              <a href="#" data-toggle="popover" data-trigger="hover" 
                data-content="The date of birth on the fingerprint card at the time of arrest.">
                <i class="fas fa-info-circle"></i></a>
            </th>
            <th>SSN
              <a href="#" data-toggle="popover" data-trigger="hover" 
                data-content="The social security number on the fingerprint card at the time of arrest.">
                <i class="fas fa-info-circle"></i></a>
            </th>
						<th>OTN
              <a href="#" data-toggle="popover" data-trigger="hover" 
                data-content="Offender Tracking Number on the OSBI fingerprint card supplied by the arresting agency.  
                  OTN is used to link the filing/disposition record to the OSBI Criminal History File.  OTN consists of nine numbers and a letter. (E.G. 001234567R)">
                <i class="fas fa-info-circle"></i></a>
						</th>
            <th>DATE OF ARREST
              <a href="#" data-toggle="popover" data-trigger="hover" 
                data-content="The date on which the arrest occurred, as provided on the fingerprint card.">
                <i class="fas fa-info-circle"></i></a>
            </th>
					</tr>
				</thead>
				<tbody>
				   <tr>
				      <td><xsl:apply-templates select="j:Subject[@structures:id=../j:Arrest/j:ArrestSubject/nc:RoleOfPerson/@structures:ref]/nc:RoleOfPerson/nc:PersonName" mode="primaryName"></xsl:apply-templates></td>
				      <td>
				        <xsl:apply-templates select="j:Subject[@structures:id=../j:Arrest/j:ArrestSubject/nc:RoleOfPerson/@structures:ref]/nc:RoleOfPerson/nc:PersonBirthDate/nc:Date" mode="formatDateAsMMDDYYYY"/>
				      </td> 
				      <td>
				        <xsl:apply-templates select="j:Subject[@structures:id=../j:Arrest/j:ArrestSubject/nc:RoleOfPerson/@structures:ref]/nc:RoleOfPerson/nc:PersonSSNIdentification/nc:IdentificationID"/>
				      </td>
				      <td>
				        <xsl:value-of select="j:Subject[@structures:id=../j:Arrest/j:ArrestSubject/nc:RoleOfPerson/@structures:ref]/j:SubjectIdentification/nc:IdentificationID"></xsl:value-of>
				      </td>         
				      <td id="arrestDate">
				        <strong><xsl:apply-templates select="j:Arrest/nc:ActivityDate/nc:Date" mode="formatDateAsMMDDYYYY"/></strong>
				      </td>
           </tr>
				</tbody>
			</table>
			
      <div class="card card-primary">
         <div class="card-header"><H5>CHARGE AT ARREST</H5></div>
         <div class="card-body">
             <div id="accordion">
                <xsl:apply-templates select="j:Arrest/j:ArrestCharge"/>
             </div>             
         </div>
      </div>

	</xsl:template>
	
	<xsl:template match="j:ArrestCharge">
	   <div class="card">
      <div class="card-header">
        <a class="card-link" data-toggle="collapse">
           <xsl:attribute name="href">
              <xsl:value-of select="concat('#charge', position())"></xsl:value-of>
           </xsl:attribute>
           <xsl:value-of select="j:ChargeDescriptionText"/>
        </a>
      </div>
      <div class="collapse hscroll" data-parent="#accordion">
        <xsl:attribute name="id">
           <xsl:value-of select="concat('charge', position())"></xsl:value-of>
        </xsl:attribute>
        
        <div class="card-body">
			      <table class="table table-striped table-bordered dispositionsTable" style="width:100%">
               <xsl:attribute name="id">
                   <xsl:value-of select="normalize-space(j:ChargeIdentification/nc:IdentificationID)"/>
               </xsl:attribute>
			        <col/>
			        <col/>
			        <col/>
			        <col/>
			        <col/>
			        <col/>
			        <colgroup span="2"/>
			        <colgroup span="2"/>
			        <colgroup span="2"/>
               <col/>
               <col/>
               <col/>
               <col/>
               <col/>
			        <thead>
			          <tr>
                   <th rowspan="2">ACTION</th>
			            <th rowspan="2">DISPO DATE</th>
			            <th rowspan="2">DISPO CODE</th>
			            <th rowspan="2">COUNTS</th>
			            <th rowspan="2">COURT CASE #</th>
			            <th rowspan="2">FILED CHARGE</th>
			            <th rowspan="2">AMENDED CHARGE</th>
			            <th scope="colgroup" colspan="2" style="text-align:middle">FINE</th>
			            <th scope="colgroup" colspan="2" style="text-align:middle">JAIL</th>
			            <th scope="colgroup" colspan="2" style="text-align:middle">SUSPENDED</th>
			            <th scope="colgroup" colspan="2" style="text-align:middle">DEFERRED</th>
                   <th rowspan="2">RESTITUTION</th>
                   <th rowspan="2">ALTERNATE SENTENCE</th>
                   <th rowspan="2">REASON FOR DISMISSAL</th>
                   <th rowspan="2">PROVISION</th>
                                     
			          </tr>
			          <tr>
                   <th scope="col">AMOUNT</th>
                   <th scope="col">SUSPENDED</th>
                   <th scope="col">YEARS</th>
                   <th scope="col">DAYS</th>
                   <th scope="col">YEARS</th>
                   <th scope="col">DAYS</th>
                   <th scope="col">YEARS</th>
                   <th scope="col">DAYS</th>
			          </tr>
			        </thead>
			        <tbody>
			          <xsl:apply-templates select="j:ChargeDisposition"/>
			        </tbody>
               <tfoot>				          
	              <tr>
						      <td style="vertical-align:top; white-space: nowrap" >
						        <a href="#" class="addDisposition" style="margin-right:3px" data-content="create a new disposition to the current charge" data-toggle="popover" data-trigger="hover">
						          <i class="fas fa-plus-square fa-lg"></i>
						        </a>
						      </td>
						      <td/>
						      <td/>
						      <td/>
						      <td/>
						      <td/>
						      <td/>
						      <td/>
						      <td/>
						      <td/>
						      <td/>
						      <td/>
						      <td/>
						      <td/>
						      <td/>
						      <td/>
						      <td/>
						      <td/>
						      <td/>
						    </tr>
			         </tfoot> 
			      </table>
        </div>
      </div>
    </div>
	</xsl:template>
	
	<xsl:template match="j:ChargeDisposition">
	  <xsl:variable name="dispositionId" select="@structures:id"/>
	  <xsl:variable name="chargeSentenceId" select="ancestor::chsres-ext:CriminalHistorySearchResult/chsres-ext:DispositionSentenceAssociation[nc:Disposition/@structures:ref = $dispositionId]/j:Sentence/@structures:ref"/>
	  <xsl:variable name="restitutionId" select="ancestor::chsres-ext:CriminalHistorySearchResult/j:ActivityObligationAssociation[nc:Activity/@structures:ref=$chargeSentenceId]/nc:Obligation/@structures:ref"/>
		<tr>
      <xsl:attribute name="id">
        <xsl:value-of select="normalize-space(chsres-ext:DispositionIdentification/nc:IdentificationID)"/>
      </xsl:attribute>
      <td style="vertical-align:top; white-space: nowrap" >
        <a href="#" class="editDisposition" style="margin-right:3px" data-content="edit the recently added disposition" data-toggle="popover" data-trigger="hover" >
          <i class="fas fa-edit fa-lg"></i>
        </a>
        <a href="#" class="deleteDisposition" data-content="delete" data-toggle="popover" data-trigger="hover">
          <i class="fas fa-trash-alt fa-lg"></i>
        </a>
        <xsl:if test="chsres-ext:ChargeDispositionCategoryCode = 'deferred'">
          <a href="#" class="expungeDisposition"  data-content="expunge" data-toggle="popover" data-trigger="hover" style="margin-left:3px">
             <i class="fas fa-calendar-times fa-lg" ></i>
          </a>
        </xsl:if>
      </td>
		  <td>
		    <xsl:apply-templates select="nc:DispositionDate/nc:Date"  mode="formatDateAsMMDDYYYY"/>
		  </td>         
		  <td><xsl:value-of select="chsres-ext:DispositionCodeText"/></td>
      <td><xsl:value-of select="chsres-ext:DispositionChargeCountQuantity"/></td>
		  <td>
		    <xsl:value-of select="chsres-ext:CourtCase/nc:ActivityIdentification/nc:IdentificationID"/>
		  </td> 
		  <td>
		    <xsl:value-of select="chsres-ext:FiledCharge/chsres-ext:ChargeMunicipalCodeText"/>
		  </td>
		  <td>
		    <xsl:value-of select="chsres-ext:AmendedCharge/chsres-ext:ChargeMunicipalCodeText"/>
		  </td>
		  <td>
		    <xsl:value-of select="../j:ChargeSentence[@structures:id=$chargeSentenceId]/j:SupervisionFineAmount/nc:Amount"/>
		  </td>
		  <td>
		    <xsl:value-of select="../j:ChargeSentence[@structures:id=$chargeSentenceId]/chsres-ext:FineSuspendedAmount/nc:Amount"/>
		  </td>
		  <td>
		    <xsl:value-of select="substring-before(substring-after(../j:ChargeSentence[@structures:id=$chargeSentenceId]/j:SentenceTerm[nc:ActivityCategoryText='JAIL']/j:TermDuration, 'P'), 'Y')"/>
		  </td>
		  <td>
		    <xsl:value-of select="substring-before(substring-after(../j:ChargeSentence[@structures:id=$chargeSentenceId]/j:SentenceTerm[nc:ActivityCategoryText='JAIL']/j:TermDuration, 'Y'), 'D')"/>
		  </td>
		  <td>
		    <xsl:value-of select="substring-before(substring-after(../j:ChargeSentence[@structures:id=$chargeSentenceId]/j:SentenceSuspendedTerm/j:TermDuration, 'P'), 'Y')"/>
		  </td>
		  <td>
		    <xsl:value-of select="substring-before(substring-after(../j:ChargeSentence[@structures:id=$chargeSentenceId]/j:SentenceSuspendedTerm/j:TermDuration, 'Y'), 'D')"/>
		  </td>
		  <td>
		    <xsl:value-of select="substring-before(substring-after(../j:ChargeSentence[@structures:id=$chargeSentenceId]/j:SentenceDeferredTerm/j:TermDuration, 'P'), 'Y')"/>
		  </td>
		  <td>
		    <xsl:value-of select="substring-before(substring-after(../j:ChargeSentence[@structures:id=$chargeSentenceId]/j:SentenceDeferredTerm/j:TermDuration, 'Y'), 'D')"/>
		  </td>
		  <td>
		    <xsl:value-of select="ancestor::chsres-ext:CriminalHistorySearchResult/j:Restitution[@structures:id=$restitutionId]/nc:ObligationDueAmount/nc:Amount"/>
		  </td>
		  <td>
		    <xsl:value-of select="../j:ChargeSentence[@structures:id=$chargeSentenceId]/chsres-ext:AlternateSentenceCodeText"/>
		  </td>
		  <td>
		    <xsl:value-of select="chsres-ext:DispositionDismissalReasonCodeText"/>
		  </td>
		  <td>
		    <!-- TODO Put provision description here -->
		  </td>
		</tr>
	   
	</xsl:template>
 </xsl:stylesheet>
