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
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:chsres-doc="http://ojbc.org/IEPD/Exchange/CriminalHistorySearchResults/1.0"
  xmlns:chsres-ext="http://ojbc.org/IEPD/Extensions/CriminalHistorySearchResults/1.0"
  xmlns:j="http://release.niem.gov/niem/domains/jxdm/6.0/" xmlns:intel="http://release.niem.gov/niem/domains/intelligence/4.0/"
  xmlns:nc="http://release.niem.gov/niem/niem-core/4.0/" xmlns:ncic="http://release.niem.gov/niem/codes/fbi_ncic/4.0/"
  xmlns:niem-xs="http://release.niem.gov/niem/proxy/xsd/4.0/" xmlns:structures="http://release.niem.gov/niem/structures/4.0/"
  exclude-result-prefixes="#all">
  <xsl:import href="_formatters.xsl" />
  <xsl:output method="html" encoding="UTF-8" />
  <xsl:template match="/chsres-doc:CriminalHistorySearchResults">
    <div>
      <xsl:apply-templates select="chsres-ext:CriminalHistorySearchResult" mode="personDetail" />
      <xsl:apply-templates select="chsres-ext:CriminalHistorySearchResult" mode="charges" />
    </div>
  </xsl:template>
  <xsl:template match="chsres-ext:CriminalHistorySearchResult" mode="personDetail">
    <div class="card card-primary">
	    <h4 class="card-header">PERSON DETAIL</h4>
	    <div class="card-body">
		    <table class="table table-bordered table-striped">
		      <tr>
		        <th colspan="2">NAME:</th>
		        <td colspan="6">
		          <xsl:apply-templates select="j:Subject/nc:RoleOfPerson/nc:PersonName" mode="constructName" />
		        </td>
		      </tr>
		      <tr>
		        <th colspan="2">SSN:</th>
		        <td colspan="6">
		          <xsl:call-template name="formatSSN">
		            <xsl:with-param name="ssn" select="j:Subject/nc:RoleOfPerson/nc:PersonSSNIdentification/nc:IdentificationID" />
		          </xsl:call-template>
		        </td>
		      </tr>
		      <tr>
		        <th colspan="2">DOB:</th>
		        <td colspan="6">
		          <xsl:value-of select="j:Subject/nc:RoleOfPerson/nc:PersonBirthDate/nc:Date" />
		        </td>
		      </tr>
		      <tr>
		        <th colspan="2">OTN:</th>
		        <td colspan="6">
		          <xsl:value-of select="j:Subject/j:SubjectIdentification/nc:IdentificationID" />
		        </td>
		      </tr>
		      <tr>
		        <th colspan="2">ARREST DATE:</th>
		        <td colspan="6">
		          <xsl:value-of select="j:Arrest/nc:ActivityDate/nc:Date" />
		        </td>
		      </tr>
		    </table>
	    </div>
    </div>
  </xsl:template>
  <xsl:template match="chsres-ext:CriminalHistorySearchResult" mode="charges">
    <div class="card card-primary">
      <h4 class="card-header">ARREST CHARGES</h4>
      <div class="card-body">
         <xsl:apply-templates select="j:Arrest/j:ArrestCharge" />
      </div>
    </div>
  </xsl:template>
  <xsl:template match="j:ArrestCharge">
    <div class="card card-primary">
      <h5 class="card-header">CHARGE : <xsl:value-of select="j:ChargeDescriptionText" /></h5>
      <div class="card-body">
         <xsl:apply-templates select="j:ChargeDisposition" />
      </div>
    </div>
  </xsl:template>
  <xsl:template match="j:ChargeDisposition">
    <xsl:variable name="CDid" select="@structures:id" />
    <div class="card card-primary">
      <h6 class="card-header">CHARGE DISPOSITION</h6>
      <div class="card-body">
          <table class="table table-bordered table-striped">
			      <xsl:if test="nc:DispositionDate/nc:Date[. !='']">
			        <tr>
			          <th colspan="2">DISPOSITION DATE:</th>
			          <td colspan="6">
			            <xsl:value-of select="nc:DispositionDate/nc:Date" />
			          </td>
			        </tr>
			      </xsl:if>
			      <xsl:if test="chsres-ext:DispositionIdentification/nc:IdentificationID[. !='']">
			        <tr>
			          <th colspan="2">DISPOSITION ID:</th>
			          <td colspan="6">
			            <xsl:value-of select="chsres-ext:DispositionIdentification/nc:IdentificationID" />
			          </td>
			        </tr>
			      </xsl:if>
			      <xsl:if test="chsres-ext:ChargeDispositionCategoryCode[. !='']">
			        <tr>
			          <th colspan="2">CHARGE DISPOSITION CATEGORY CODE:</th>
			          <td colspan="6">
			            <xsl:value-of select="chsres-ext:ChargeDispositionCategoryCode" />
			          </td>
			        </tr>
			      </xsl:if>
			      <xsl:if test="chsres-ext:CourtCase/nc:ActivityIdentification/nc:IdentificationID[. !='']">
			        <tr>
			          <th colspan="2">COURT CASE ID:</th>
			          <td colspan="6">
			            <xsl:value-of select="chsres-ext:CourtCase/nc:ActivityIdentification/nc:IdentificationID" />
			          </td>
			        </tr>
			      </xsl:if>
			      <xsl:if test="chsres-ext:DispositionCodeText[. !='']">
			        <tr>
			          <th colspan="2">DISPOSITION CODE:</th>
			          <td colspan="6">
			            <xsl:value-of select="chsres-ext:DispositionCodeText" />
			          </td>
			        </tr>
			      </xsl:if>
			      <xsl:if test="chsres-ext:DispositionCodeDescriptionText[. !='']">
			        <tr>
			          <th colspan="2">DISPOSITION CODE DESCRIPTION:</th>
			          <td colspan="6">
			            <xsl:value-of select="chsres-ext:DispositionCodeDescriptionText" />
			          </td>
			        </tr>
			      </xsl:if>
			      <xsl:if test="chsres-ext:DispositionDismissalReasonText[. !='']">
			        <tr>
			          <th colspan="2">DISPOSITION DISMISSAL REASON:</th>
			          <td colspan="6">
			            <xsl:value-of select="chsres-ext:DispositionDismissalReasonText" />
			          </td>
			        </tr>
			      </xsl:if>
			      <xsl:if test="chsres-ext:DispositionDismissalReasonCodeText[. !='']">
			        <tr>
			          <th colspan="2">DISPOSITION DISMISSAL REASON CODE:</th>
			          <td colspan="6">
			            <xsl:value-of select="chsres-ext:DispositionDismissalReasonCodeText" />
			          </td>
			        </tr>
			      </xsl:if>
			      <xsl:if test="chsres-ext:DispositionChargeCountQuantity[. !='']">
			        <tr>
			          <th colspan="2">DISPOSITION CHARGE COUNT:</th>
			          <td colspan="6">
			            <xsl:value-of select="chsres-ext:DispositionChargeCountQuantity" />
			          </td>
			        </tr>
			      </xsl:if>
			    </table>
			    <xsl:if test="normalize-space(chsres-ext:FiledCharge/j:ChargeDescriptionText) 
			              or normalize-space(chsres-ext:FiledCharge/j:ChargeSeverityText)
			              or normalize-space(chsres-ext:FiledCharge/chsres-ext:ChargeMunicipalCodeText) 
			              or normalize-space(chsres-ext:FiledCharge/chsres-ext:ChargeMunicipalCodeDescriptionText) "> 
             <div class="card card-primary">
					      <div class="card-header">FILED CHARGE</div>
					      <div class="card-body">
				          <table class="table table-bordered table-striped">
                      <xsl:if test="chsres-ext:FiledCharge/j:ChargeDescriptionText[. !='']">
					            <tr>
					              <th colspan="2">CHARGE DESCRIPTION:</th>
					              <td colspan="6">
					                <xsl:value-of select="chsres-ext:FiledCharge/j:ChargeDescriptionText" />
					              </td>
					            </tr>
					          </xsl:if>
					          <xsl:if test="chsres-ext:FiledCharge/j:ChargeSeverityText[. !='']">
					            <tr>
					              <th colspan="2">CHARGE SEVERITY:</th>
					              <td colspan="6">
					                <xsl:value-of select="chsres-ext:FiledCharge/j:ChargeSeverityText" />
					              </td>
					            </tr>
					          </xsl:if>
					          <xsl:if test="chsres-ext:FiledCharge/chsres-ext:ChargeMunicipalCodeText[. !='']">
					            <tr>
					              <th colspan="2">CHARGE MUNICIPAL CODE:</th>
					              <td colspan="6">
					                <xsl:value-of select="chsres-ext:FiledCharge/chsres-ext:ChargeMunicipalCodeText" />
					              </td>
					            </tr>
					          </xsl:if>
					          <xsl:if test="chsres-ext:FiledCharge/chsres-ext:ChargeMunicipalCodeDescriptionText[. !='']">
					            <tr>
					              <th colspan="2">CHARGE MUNICIPAL CODE DESCRIPTION:</th>
					              <td colspan="6">
					                <xsl:value-of select="chsres-ext:FiledCharge/chsres-ext:ChargeMunicipalCodeDescriptionText" />
					              </td>
					            </tr>
					          </xsl:if>
                     </table>
                  </div>
              </div>
			    </xsl:if>
		    <xsl:if test="normalize-space(chsres-ext:AmendedCharge/j:ChargeDescriptionText) 
                    or normalize-space(chsres-ext:AmendedCharge/j:ChargeSeverityText)
                    or normalize-space(chsres-ext:AmendedCharge/chsres-ext:ChargeMunicipalCodeText) 
                    or normalize-space(chsres-ext:AmendedCharge/chsres-ext:ChargeMunicipalCodeDescriptionText) "> 
          <div class="card card-primary">
             <div class="card-header">AMENDED CHARGE</div>
             <div class="card-body">
               <table class="table table-bordered table-striped">
				          <xsl:if test="chsres-ext:AmendedCharge/j:ChargeDescriptionText[. !='']">
				            <tr>
				              <th colspan="2">CHARGE DESCRIPTION:</th>
				              <td colspan="6">
				                <xsl:value-of select="chsres-ext:AmendedCharge/j:ChargeDescriptionText" />
				              </td>
				            </tr>
				          </xsl:if>
				          <xsl:if test="chsres-ext:AmendedCharge/j:ChargeSeverityText[. !='']">
				            <tr>
				              <th colspan="2">CHARGE SEVERITY:</th>
				              <td colspan="6">
				                <xsl:value-of select="chsres-ext:AmendedCharge/j:ChargeSeverityText" />
				              </td>
				            </tr>
				          </xsl:if>
				          <xsl:if test="chsres-ext:AmendedCharge/chsres-ext:ChargeMunicipalCodeText[. !='']">
				            <tr>
				              <th colspan="2">CHARGE MUNICIPAL CODE:</th>
				              <td colspan="6">
				                <xsl:value-of select="chsres-ext:AmendedCharge/chsres-ext:ChargeMunicipalCodeText" />
				              </td>
				            </tr>
				          </xsl:if>
				          <xsl:if test="chsres-ext:AmendedCharge/chsres-ext:ChargeMunicipalCodeDescriptionText[. !='']">
				            <tr>
				              <th colspan="2">CHARGE MUNICIPAL CODE DESCRIPTION:</th>
				              <td colspan="6">
				                <xsl:value-of select="chsres-ext:AmendedCharge/chsres-ext:ChargeMunicipalCodeDescriptionText" />
				              </td>
				            </tr>
				          </xsl:if>
               </table>
             </div>
          </div>
        </xsl:if>
		    <xsl:apply-templates
		      select="../j:ChargeSentence[@structures:id=../../../chsres-ext:DispositionSentenceAssociation[nc:Disposition/@structures:ref=$CDid]/j:Sentence/@structures:ref]" />
			    
      </div>
    </div>
  </xsl:template>
  <xsl:template match="j:ChargeSentence">
    <xsl:variable name="CSid" select="@structures:id" />
    <div class="card card-primary">
       <div class="card-header">SENTENCE</div>
       <div class="card-body">
         <table class="table table-bordered table-striped">
            <xsl:if test="normalize-space(j:SentenceDeferredTerm/nc:ActivityDate/nc:Date)">
 			        <tr>
			          <th colspan="2">DEFERRED SENTENCE DATE:</th>
			          <td colspan="6">
			            <xsl:value-of select="j:SentenceDeferredTerm/nc:ActivityDate/nc:Date" />
			          </td>
			        </tr>
		        </xsl:if>
			      <xsl:if test="j:SentenceDeferredTerm/j:TermDuration[. !='']">
			        <tr>
			          <th colspan="2">DEFERRED SENTENCE TERM:</th>
			          <td colspan="6">
			            <xsl:apply-templates select="j:SentenceDeferredTerm/j:TermDuration" />
			          </td>
			        </tr>
			      </xsl:if>
			      <xsl:if test="j:SentenceSuspendedTerm/j:TermDuration[. !='']">
			        <tr>
			          <th colspan="2">SUSPENDED SENTENCE TERM:</th>
			          <td colspan="6">
			            <xsl:apply-templates select="j:SentenceSuspendedTerm/j:TermDuration" />
			          </td>
			        </tr>
			      </xsl:if>
			      <xsl:if test="j:SentenceTerm/j:TermDuration[. !='']">
			        <tr>
			          <th colspan="2">SENTENCE CATEGORY:</th>
			          <td colspan="6">
			            <xsl:value-of select="j:SentenceTerm/nc:ActivityCategoryText" />
			          </td>
			        </tr>
			        <tr>
			          <th colspan="2">SENTENCE TERM:</th>
			          <td colspan="6">
			            <xsl:apply-templates select="j:SentenceTerm/j:TermDuration" />
			          </td>
			        </tr>
			      </xsl:if>
			      <xsl:if test="j:SupervisionFineAmount/nc:Amount[. !='']">
			        <tr>
			          <th colspan="2">FINE</th>
			          <td colspan="6">
			            <xsl:value-of select="j:SupervisionFineAmount/nc:Amount" />
			          </td>
			        </tr>
			      </xsl:if>
			      <xsl:if test="chsres-ext:FineSuspendedAmount/nc:Amount[. !='']">
			        <tr>
			          <th colspan="2">SUSPENDED FINE:</th>
			          <td colspan="6">
			            <xsl:value-of select="chsres-ext:FineSuspendedAmount/nc:Amount" />
			          </td>
			        </tr>
			      </xsl:if>
			      <xsl:if test="chsres-ext:AlternateSentenceCodeText[. !='']">
			        <tr>
			          <th colspan="2">ALTERNATE SENTENCE CODE:</th>
			          <td colspan="6">
			            <xsl:value-of select="chsres-ext:AlternateSentenceCodeText" />
			          </td>
			        </tr>
			      </xsl:if>
			      <xsl:if test="chsres-ext:AlternateSentenceCodeDescriptionText[. !='']">
			        <tr>
			          <th colspan="2">ALTERNATE SENTENCE CODE DESCRIPTION:</th>
			          <td colspan="6">
			            <xsl:value-of select="chsres-ext:AlternateSentenceCodeDescriptionText" />
			          </td>
			        </tr>
			      </xsl:if>
			      <xsl:if
			        test="//j:Restitution[@structures:id=//j:ActivityObligationAssociation[nc:Activity/@structures:ref=$CSid]/nc:Obligation/@structures:ref]/nc:ObligationDueAmount/nc:Amount[. !='']">
			        <tr>
			          <th colspan="2">RESTITUTION AMOUNT:</th>
			          <td colspan="6">
			            <xsl:value-of
			              select="//j:Restitution[@structures:id=//j:ActivityObligationAssociation[nc:Activity/@structures:ref=$CSid]/nc:Obligation/@structures:ref]/nc:ObligationDueAmount/nc:Amount" />
			          </td>
			        </tr>
			      </xsl:if>
         </table>
       </div>
    </div>
  </xsl:template>
  <xsl:template match="nc:PersonName" mode="constructName">
    <xsl:value-of select="concat(nc:PersonSurName,', ',nc:PersonGivenName)" />
  </xsl:template>
  <xsl:template match="j:TermDuration">
    <xsl:if test="contains(.,'Y')">
      <xsl:variable name="years" select="translate(substring-before(.,'Y'), translate(., '0123456789', ''), '')"></xsl:variable>
      <xsl:value-of select="$years,' Years; '" />
    </xsl:if>
    <xsl:if test="contains(.,'M')">
      <xsl:choose>
        <xsl:when test="contains(.,'Y') and contains(.,'M')">
          <xsl:variable name="months"
            select="translate(substring-after(substring-before(.,'M'),'Y'), translate(., '0123456789', ''), '')"></xsl:variable>
          <xsl:value-of select="$months,' Months; '" />
        </xsl:when>
        <xsl:otherwise>
          <xsl:variable name="months" select="translate(substring-before(.,'M'), translate(., '0123456789', ''), '')"></xsl:variable>
          <xsl:value-of select="$months,' Months; '" />
        </xsl:otherwise>
      </xsl:choose>
    </xsl:if>
    <xsl:if test="contains(.,'D')">
      <xsl:choose>
        <xsl:when test="contains(.,'Y') and contains(.,'M')">
          <xsl:variable name="days"
            select="translate(substring-after(substring-before(.,'D'),'M'), translate(., '0123456789', ''), '')"></xsl:variable>
          <xsl:value-of select="$days,' Days'" />
        </xsl:when>
        <xsl:when test="contains(.,'Y') and not(contains(.,'M'))">
          <xsl:variable name="days"
            select="translate(substring-after(substring-before(.,'D'),'Y'), translate(., '0123456789', ''), '')"></xsl:variable>
          <xsl:value-of select="$days,' Days'" />
        </xsl:when>
        <xsl:when test="contains(.,'M') and not(contains(.,'Y'))">
          <xsl:variable name="days"
            select="translate(substring-after(substring-before(.,'D'),'M'), translate(., '0123456789', ''), '')"></xsl:variable>
          <xsl:value-of select="$days,' Days'" />
        </xsl:when>
        <xsl:otherwise>
          <xsl:variable name="days" select="translate(substring-before(.,'D'), translate(., '0123456789', ''), '')"></xsl:variable>
          <xsl:value-of select="$days,' Days; '" />
        </xsl:otherwise>
      </xsl:choose>
    </xsl:if>
  </xsl:template>
</xsl:stylesheet>