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
	    <div class="card-header"><h5>PERSON DETAIL</h5></div>
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
      <div class="card-header"><h5>CHARGES</h5></div>
      <div class="card-body">
         <xsl:apply-templates select="j:Arrest/j:ArrestCharge" />
      </div>
    </div>
  </xsl:template>
  <xsl:template match="j:ArrestCharge">
    <div class="card card-primary">
      <h6 class="card-header"><xsl:value-of select="j:ChargeDescriptionText" /></h6>
      <div class="card-body">
         <xsl:apply-templates select="j:ChargeDisposition" />
      </div>
    </div>
  </xsl:template>
  <xsl:template match="j:ChargeDisposition">
    <xsl:variable name="CDid" select="@structures:id" />
    <div class="card card-primary">
      <h7 class="card-header">CHARGE DISPOSITION</h7>
      <div class="card-body">
             <table class="table table-bordered table-striped">
			      <xsl:if test="nc:DispositionDate/nc:Date[. !='']">
			        <tr>
			          <td colspan="2" class="dispositionLabel">DISPOSITION DATE:</td>
			          <td colspan="6">
			            <xsl:value-of select="nc:DispositionDate/nc:Date" />
			          </td>
			        </tr>
			      </xsl:if>
			      <xsl:if test="chsres-ext:DispositionIdentification/nc:IdentificationID[. !='']">
			        <tr>
			          <td colspan="2" class="dispositionLabel">DISPOSITION ID:</td>
			          <td colspan="6">
			            <xsl:value-of select="chsres-ext:DispositionIdentification/nc:IdentificationID" />
			          </td>
			        </tr>
			      </xsl:if>
			      <xsl:if test="chsres-ext:ChargeDispositionCategoryCode[. !='']">
			        <tr>
			          <td colspan="2" class="dispositionLabel">CHARGE DISPOSITION CATEGORY CODE:</td>
			          <td colspan="6">
			            <xsl:value-of select="chsres-ext:ChargeDispositionCategoryCode" />
			          </td>
			        </tr>
			      </xsl:if>
			      <xsl:if test="chsres-ext:CourtCase/nc:ActivityIdentification/nc:IdentificationID[. !='']">
			        <tr>
			          <td colspan="2" class="dispositionLabel">COURT CASE ID:</td>
			          <td colspan="6">
			            <xsl:value-of select="chsres-ext:CourtCase/nc:ActivityIdentification/nc:IdentificationID" />
			          </td>
			        </tr>
			      </xsl:if>
			      <xsl:if test="chsres-ext:DispositionCodeText[. !='']">
			        <tr>
			          <td colspan="2" class="dispositionLabel">DISPOSITION CODE:</td>
			          <td colspan="6">
			            <xsl:value-of select="chsres-ext:DispositionCodeText" />
			          </td>
			        </tr>
			      </xsl:if>
			      <xsl:if test="chsres-ext:DispositionCodeDescriptionText[. !='']">
			        <tr>
			          <td colspan="2" class="dispositionLabel">DISPOSITION CODE DESCRIPTION:</td>
			          <td colspan="6">
			            <xsl:value-of select="chsres-ext:DispositionCodeDescriptionText" />
			          </td>
			        </tr>
			      </xsl:if>
			      <xsl:if test="chsres-ext:DispositionDismissalReasonText[. !='']">
			        <tr>
			          <td colspan="2" class="dispositionLabel">DISPOSITION DISMISSAL REASON:</td>
			          <td colspan="6">
			            <xsl:value-of select="chsres-ext:DispositionDismissalReasonText" />
			          </td>
			        </tr>
			      </xsl:if>
			      <xsl:if test="chsres-ext:DispositionDismissalReasonCodeText[. !='']">
			        <tr>
			          <td colspan="2" class="dispositionLabel">DISPOSITION DISMISSAL REASON CODE:</td>
			          <td colspan="6">
			            <xsl:value-of select="chsres-ext:DispositionDismissalReasonCodeText" />
			          </td>
			        </tr>
			      </xsl:if>
			      <xsl:if test="chsres-ext:DispositionChargeCountQuantity[. !='']">
			        <tr>
			          <td colspan="2" class="dispositionLabel">DISPOSITION CHARGE COUNT:</td>
			          <td colspan="6">
			            <xsl:value-of select="chsres-ext:DispositionChargeCountQuantity" />
			          </td>
			        </tr>
			      </xsl:if>
			    </table>
					        <tr>
		      <h4>FILED CHARGE</h4>
		    </tr>
		    <table class="filedChargeTable">
		      <xsl:if test="chsres-ext:FiledCharge/j:ChargeDescriptionText[. !='']">
		        <tr>
		          <td colspan="2" class="chargeLabel">CHARGE DESCRIPTION:</td>
		          <td colspan="6">
		            <xsl:value-of select="chsres-ext:FiledCharge/j:ChargeDescriptionText" />
		          </td>
		        </tr>
		      </xsl:if>
		      <xsl:if test="chsres-ext:FiledCharge/j:ChargeSeverityText[. !='']">
		        <tr>
		          <td colspan="2" class="chargeLabel">CHARGE SEVERITY:</td>
		          <td colspan="6">
		            <xsl:value-of select="chsres-ext:FiledCharge/j:ChargeSeverityText" />
		          </td>
		        </tr>
		      </xsl:if>
		      <xsl:if test="chsres-ext:FiledCharge/chsres-ext:ChargeMunicipalCodeText[. !='']">
		        <tr>
		          <td colspan="2" class="chargeLabel">CHARGE MUNICIPAL CODE:</td>
		          <td colspan="6">
		            <xsl:value-of select="chsres-ext:FiledCharge/chsres-ext:ChargeMunicipalCodeText" />
		          </td>
		        </tr>
		      </xsl:if>
		      <xsl:if test="chsres-ext:FiledCharge/chsres-ext:ChargeMunicipalCodeDescriptionText[. !='']">
		        <tr>
		          <td colspan="2" class="chargeLabel">CHARGE MUNICIPAL CODE DESCRIPTION:</td>
		          <td colspan="6">
		            <xsl:value-of select="chsres-ext:FiledCharge/chsres-ext:ChargeMunicipalCodeDescriptionText" />
		          </td>
		        </tr>
		      </xsl:if>
		    </table>
		    <tr>
		      <h4>AMENDED CHARGE</h4>
		    </tr>
		    <table class="amendedChargeTable">
		      <xsl:if test="chsres-ext:AmendedCharge/j:ChargeDescriptionText[. !='']">
		        <tr>
		          <td colspan="2" class="chargeLabel">CHARGE DESCRIPTION:</td>
		          <td colspan="6">
		            <xsl:value-of select="chsres-ext:AmendedCharge/j:ChargeDescriptionText" />
		          </td>
		        </tr>
		      </xsl:if>
		      <xsl:if test="chsres-ext:AmendedCharge/j:ChargeSeverityText[. !='']">
		        <tr>
		          <td colspan="2" class="chargeLabel">CHARGE SEVERITY:</td>
		          <td colspan="6">
		            <xsl:value-of select="chsres-ext:AmendedCharge/j:ChargeSeverityText" />
		          </td>
		        </tr>
		      </xsl:if>
		      <xsl:if test="chsres-ext:AmendedCharge/chsres-ext:ChargeMunicipalCodeText[. !='']">
		        <tr>
		          <td colspan="2" class="chargeLabel">CHARGE MUNICIPAL CODE:</td>
		          <td colspan="6">
		            <xsl:value-of select="chsres-ext:AmendedCharge/chsres-ext:ChargeMunicipalCodeText" />
		          </td>
		        </tr>
		      </xsl:if>
		      <xsl:if test="chsres-ext:AmendedCharge/chsres-ext:ChargeMunicipalCodeDescriptionText[. !='']">
		        <tr>
		          <td colspan="2" class="chargeLabel">CHARGE MUNICIPAL CODE DESCRIPTION:</td>
		          <td colspan="6">
		            <xsl:value-of select="chsres-ext:AmendedCharge/chsres-ext:ChargeMunicipalCodeDescriptionText" />
		          </td>
		        </tr>
		      </xsl:if>
		    </table>
		    <xsl:apply-templates
		      select="../j:ChargeSentence[@structures:id=../../../chsres-ext:DispositionSentenceAssociation[nc:Disposition/@structures:ref=$CDid]/j:Sentence/@structures:ref]" />
			    
      </div>
    </div>
  </xsl:template>
  <xsl:template match="j:ChargeSentence">
    <xsl:variable name="CSid" select="@structures:id" />
    <tr>
      <h4>SENTENCE</h4>
    </tr>
    <table class="sentenceTable">
      <xsl:if test="j:SentenceDeferredTerm/j:TermDuration[. !='']">
        <tr>
          <td colspan="2" class="sentenceLabel">DEFERRED SENTENCE DATE:</td>
          <td colspan="6">
            <xsl:value-of select="j:SentenceDeferredTerm/nc:ActivityDate/nc:Date" />
          </td>
        </tr>
        <tr>
          <td colspan="2" class="sentenceLabel">DEFERRED SENTENCE TERM:</td>
          <td colspan="6">
            <xsl:apply-templates select="j:SentenceDeferredTerm/j:TermDuration" />
          </td>
        </tr>
      </xsl:if>
      <xsl:if test="j:SentenceSuspendedTerm/j:TermDuration[. !='']">
        <tr>
          <td colspan="2" class="sentenceLabel">SUSPENDED SENTENCE TERM:</td>
          <td colspan="6">
            <xsl:apply-templates select="j:SentenceSuspendedTerm/j:TermDuration" />
          </td>
        </tr>
      </xsl:if>
      <xsl:if test="j:SentenceTerm/j:TermDuration[. !='']">
        <tr>
          <td colspan="2" class="sentenceLabel">SENTENCE CATEGORY:</td>
          <td colspan="6">
            <xsl:value-of select="j:SentenceTerm/nc:ActivityCategoryText" />
          </td>
        </tr>
        <tr>
          <td colspan="2" class="sentenceLabel">SENTENCE TERM:</td>
          <td colspan="6">
            <xsl:apply-templates select="j:SentenceTerm/j:TermDuration" />
          </td>
        </tr>
      </xsl:if>
      <xsl:if test="j:SupervisionFineAmount/nc:Amount[. !='']">
        <tr>
          <td colspan="2" class="sentenceLabel">FINE</td>
          <td colspan="6">
            <xsl:value-of select="j:SupervisionFineAmount/nc:Amount" />
          </td>
        </tr>
      </xsl:if>
      <xsl:if test="chsres-ext:FineSuspendedAmount/nc:Amount[. !='']">
        <tr>
          <td colspan="2" class="sentenceLabel">SUSPENDED FINE:</td>
          <td colspan="6">
            <xsl:value-of select="chsres-ext:FineSuspendedAmount/nc:Amount" />
          </td>
        </tr>
      </xsl:if>
      <xsl:if test="chsres-ext:AlternateSentenceCodeText[. !='']">
        <tr>
          <td colspan="2" class="sentenceLabel">ALTERNATE SENTENCE CODE:</td>
          <td colspan="6">
            <xsl:value-of select="chsres-ext:AlternateSentenceCodeText" />
          </td>
        </tr>
      </xsl:if>
      <xsl:if test="chsres-ext:AlternateSentenceCodeDescriptionText[. !='']">
        <tr>
          <td colspan="2" class="sentenceLabel">ALTERNATE SENTENCE CODE DESCRIPTION:</td>
          <td colspan="6">
            <xsl:value-of select="chsres-ext:AlternateSentenceCodeDescriptionText" />
          </td>
        </tr>
      </xsl:if>
      <xsl:if
        test="//j:Restitution[@structures:id=//j:ActivityObligationAssociation[nc:Activity/@structures:ref=$CSid]/nc:Obligation/@structures:ref]/nc:ObligationDueAmount/nc:Amount[. !='']">
        <tr>
          <td colspan="2" class="sentenceLabel">RESTITUTION AMOUNT:</td>
          <td colspan="6">
            <xsl:value-of
              select="//j:Restitution[@structures:id=//j:ActivityObligationAssociation[nc:Activity/@structures:ref=$CSid]/nc:Obligation/@structures:ref]/nc:ObligationDueAmount/nc:Amount" />
          </td>
        </tr>
      </xsl:if>
    </table>
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