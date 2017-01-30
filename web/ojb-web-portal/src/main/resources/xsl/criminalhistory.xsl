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
    xmlns:j="http://niem.gov/niem/domains/jxdm/4.1"
    xmlns:rap="http://nlets.org/niem2/rapsheet/1.0"
    xmlns:ch-doc="http://ojbc.org/IEPD/Exchange/CriminalHistory/1.0"
    xmlns:ch-ext="http://ojbc.org/IEPD/Extensions/CriminalHistory/1.0"
    xmlns:ansi-nist="http://niem.gov/niem/ansi-nist/2.0"
    xmlns:screening="http://niem.gov/niem/domains/screening/2.0"
    xmlns:nc="http://niem.gov/niem/niem-core/2.0"
    xmlns:s="http://niem.gov/niem/structures/2.0"
    exclude-result-prefixes="#all">
	<xsl:import href="_formatters.xsl" />
	
    <xsl:output method="html" encoding="UTF-8" />

    <xsl:template match="/">
       <table>
            <tr>
                <td style="vertical-align: top;"><div class="bigPersonImage"></div></td>
                <td> 
                    <xsl:apply-templates /> 
                </td>                
            </tr>
       </table>
       <table class="detailsTable">
			<tr>
				<td colspan="8" class="detailsTitle">
					SUPERVISION
				</td>
			</tr>
       		<xsl:choose>
				<xsl:when test="//ch-ext:RapSheetCycle/rap:Supervision/nc:ActivityCategoryText[.='SUPERVISION']">
					<xsl:call-template name="supervisionTableRows" />
				</xsl:when>
				<xsl:otherwise>
					<tr>
						<td colspan="8">
							No Supervision information available
						</td>
					</tr>
				</xsl:otherwise>
		</xsl:choose>
       </table>
       <table class="detailsTable">
			<tr>
				<td colspan="8" class="detailsTitle">
					CUSTODY
				</td>
			</tr>
       		<xsl:choose>
				<xsl:when test="//ch-ext:RapSheetCycle/rap:Supervision/nc:ActivityCategoryText[.='CUSTODY']">
					<xsl:call-template name="custodyTableRows"/>
				</xsl:when>
				<xsl:otherwise>
					<tr>
						<td colspan="8">
							No Custody information available
						</td>
					</tr>
				</xsl:otherwise>
		</xsl:choose>
       </table>
       <table class="detailsTable">
			<tr>
				<td colspan="8" class="detailsTitle">
					TRO/PO
				</td>
			</tr>
			<xsl:choose>
				<xsl:when test="//ch-ext:RapSheet/ch-ext:Order">
					<xsl:call-template name="TROPOTableRows" />
				</xsl:when>
				<xsl:otherwise>
					<tr>
						<td colspan="8">
							No TRO/PO information available
						</td>
					</tr>
				</xsl:otherwise>
			</xsl:choose>
       </table>
       <table class="detailsTable"> 
             <tr>
                <td colspan="8" class="detailsTitle">CRIMINAL HISTORY</td>
            </tr>
            <tr>
            	<td id="criminalHistoryCyclesHolderTD">
            		<div id="criminalHistory" style="overflow:auto; width:100%; height:auto">
                       <xsl:call-template name="cycles" />
                    </div>
            	</td>
            </tr>
        </table>
        
    </xsl:template>
    
    <xsl:template name="TROPOTableRows">
		<tr>
			<td class="detailsLabel" style="width: 15%">Document Type</td>
			<td class="detailsLabel" style="width: 15%">Case No.</td>
			<td class="detailsLabel" style="width: 15%">Filing Agency</td>
			<td class="detailsLabel" style="width: 13%">Filed</td>
			<td class="detailsLabel" style="width: 13%">Expires</td>
			<td class="detailsLabel" style="width: 13%">Served</td>
			<td class="detailsLabel" style="width: 16%">Status</td>
			<xsl:apply-templates select="//ch-ext:RapSheet/ch-ext:Order"/>
		</tr>
	</xsl:template>
	
    <xsl:template name="supervisionTableRows">
		<tr>
			<td class="detailsLabel" style="width: 20%">Date</td>
			<td class="detailsLabel" style="width: 40%">Agency</td>
			<td class="detailsLabel" style="width: 40%">Action</td>
			<xsl:apply-templates select="//ch-ext:RapSheet/ch-ext:RapSheetCycle/rap:Supervision[nc:ActivityCategoryText='SUPERVISION']"/>
		</tr>
	</xsl:template>
	
	<xsl:template name="custodyTableRows">
		<tr>
			<td class="detailsLabel" style="width: 20%">Date</td>
			<td class="detailsLabel" style="width: 40%">Agency</td>
			<td class="detailsLabel" style="width: 40%">Action</td>
			<xsl:apply-templates select="//ch-ext:RapSheet/ch-ext:RapSheetCycle/rap:Supervision[nc:ActivityCategoryText='CUSTODY']"/>
		</tr>
	</xsl:template>
	
	<xsl:template match="ch-ext:Order">
		<tr>
			<td><xsl:value-of select="nc:ActivityCategoryText"/></td>
			<td><xsl:value-of select="ch-ext:CourtCase/nc:ActivityIdentification/nc:IdentificationID"/></td>
			<td><xsl:value-of select="j:CourtOrderIssuingCourt/nc:OrganizationName"/></td>
			<td><xsl:value-of select="nc:ActivityDate/nc:Date"/></td>
			<td><xsl:value-of select="ch-ext:ProtectionOrderExpirationDate/nc:Date"/></td>
			<td><xsl:value-of select="j:CourtOrderServiceDate/nc:Date"/></td>
			<td><xsl:value-of select="j:CourtOrderServiceDescriptionText"/></td>
		</tr>
	</xsl:template>
	
	<xsl:template match="rap:Supervision">
		<xsl:variable name="id" select="@s:id"/>
		<tr>
			<td>
				<xsl:value-of select="nc:ActivityDate/nc:Date" />
					<!-- <xsl:value-of select="$id"/> -->
			</td>
			<td>
				<xsl:value-of select="//ch-ext:RapSheet/rap:Agency[@s:id=//rap:SupervisionAgencyAssociation[rap:SupervisionReference/@s:ref=$id]/nc:OrganizationReference/@s:ref]/nc:OrganizationName" />
			</td>
			<td>
				<xsl:value-of select="nc:SupervisionCustodyStatus/nc:StatusDescriptionText" />
			</td>
		</tr>
	</xsl:template>
    
    <xsl:template name="supervisionCustodyTableRows">
    	<xsl:param name="which" />
    	
   	    <tr>
      		<td colspan="8" class="detailsTitle"><xsl:value-of select="upper-case($which)"/></td>
   		</tr>
   		<xsl:variable name="totalCount" select="count(//ch-ext:RapSheet/ch-ext:RapSheetCycle/rap:Supervision/nc:ActivityCategoryText[text()=upper-case($which)])" />
   		<xsl:choose>
    		<xsl:when test="$totalCount &gt; 0">
   		<tr>
          	<td class="detailsLabel" style="width: 20%">Date</td>
          	<td class="detailsLabel" style="width: 40%">Agency</td>
          	<td class="detailsLabel" style="width: 40%">Action</td>
      	</tr>
   			<xsl:for-each select="//ch-ext:RapSheet/ch-ext:RapSheetCycle/rap:Supervision" >
   				<xsl:if test="nc:ActivityCategoryText[text()=upper-case($which)]">
	   				<tr>
	   					<td><xsl:value-of select="nc:ActivityDate/nc:Date"/></td>
	   					<td><xsl:value-of select="//ch-ext:RapSheet/rap:Agency[@s:id=//rap:SupervisionAgencyAssociation/nc:OrganizationReference/@s:ref and //rap:SupervisionAgencyAssociation/rap:SupervisionReference/@s:ref=//rap:Supervision/@s:id]/nc:OrganizationName"/></td>
	   					<td><xsl:value-of select="nc:SupervisionCustodyStatus/nc:StatusDescriptionText"/></td>
	   				</tr>
   				</xsl:if>
   			</xsl:for-each>
   		</xsl:when>
   		<xsl:otherwise>
   			<tr>
   				<td colspan="8">No <xsl:value-of select="$which"/> information available</td>
   			</tr>
   		</xsl:otherwise>
  		</xsl:choose>
    </xsl:template>
    
    <xsl:template match="nc:PersonName" mode="constructName">
    	<xsl:if test="position() != 1">
    		<xsl:text> / </xsl:text>
        </xsl:if>	
		<xsl:value-of select="concat(nc:PersonSurName,', ',nc:PersonGivenName)" />
              
    </xsl:template>
    
    <xsl:template match="ch-doc:CriminalHistory/ch-ext:RapSheet" >
        <table class="detailsTable">
            <tr>
            	<td colspan="8" class="detailsFullName">
           			<xsl:apply-templates select="rap:RapSheetPerson/nc:PersonName" mode="constructName"/> 
           		</td>
            </tr>
            <tr>
                <td colspan="8" class="detailsTitle">SUBJECT DETAILS</td>
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">FBI#</td>
                <td colspan="2"><xsl:value-of select="rap:RapSheetPerson/j:PersonAugmentation/j:PersonFBIIdentification/nc:IdentificationID" /></td>
                <td colspan="2" class="detailsLabel">GENDER</td>
                <td colspan="2"><xsl:value-of select="rap:RapSheetPerson/rap:PersonSexText" /></td>
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">SID/ISSUER</td>
                <td colspan="1"><xsl:value-of select="rap:RapSheetPerson/j:PersonAugmentation/j:PersonStateFingerprintIdentification/nc:IdentificationID"/></td>
                <td colspan="1"><xsl:value-of select="rap:RapSheetPerson/j:PersonAugmentation/j:PersonStateFingerprintIdentification/nc:IdentificationJurisdictionText"/></td>
                <td colspan="2" class="detailsLabel">RACE</td>
                <td colspan="2"><xsl:value-of select="rap:RapSheetPerson/rap:PersonRaceText" /></td>
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">DL#/ISSUER</td>
                <td colspan="1"><xsl:value-of select="rap:RapSheetPerson/j:PersonAugmentation/nc:DriverLicense/nc:DriverLicenseIdentification/nc:IdentificationID"/></td>
                <td colspan="1"><xsl:value-of select="rap:RapSheetPerson/j:PersonAugmentation/nc:DriverLicense/nc:DriverLicenseIdentification/nc:IdentificationJurisdictionText"/></td>
                <td colspan="2" class="detailsLabel">HEIGHT</td>
                <td colspan="2">
                	<xsl:call-template name="formatHeight">
                		<xsl:with-param name="heightInInches" select="rap:RapSheetPerson/nc:PersonHeightMeasure/nc:MeasurePointValue" />
                	</xsl:call-template>
               	</td>
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">SSN</td>
                <td colspan="2">
                    <xsl:call-template name="formatSSN">
                   		<xsl:with-param name="ssn" select="rap:RapSheetPerson/nc:PersonSSNIdentification/nc:IdentificationID" />
                    </xsl:call-template>
                </td>
                <td colspan="2" class="detailsLabel">WEIGHT</td>
                <td colspan="2"><xsl:value-of select="rap:RapSheetPerson/nc:PersonWeightMeasure/nc:MeasurePointValue" /></td>
            </tr>
            <tr>
            	<td colspan="2" class="detailsLabel">HAIR COLOR</td>
                <td colspan="2"><xsl:value-of select="rap:RapSheetPerson/rap:PersonHairColorText" /></td>
                <td colspan="2" class="detailsLabel">EYE COLOR</td>
                <td colspan="2"><xsl:value-of select="rap:RapSheetPerson/rap:PersonEyeColorText" /></td>
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">DOB</td>
                
               <xsl:for-each select="rap:RapSheetPerson/nc:PersonBirthDate">
                <td colspan="2">
                	<xsl:choose>
                		<xsl:when test="nc:Date">
                			 <xsl:call-template name="formatDate">
								<xsl:with-param name="date" select="nc:Date" />
							</xsl:call-template>
                		</xsl:when>
                		<xsl:when test="nc:Year">
                			<xsl:value-of select="nc:Year"/>
                		</xsl:when>
                	</xsl:choose>
                 </td>
                </xsl:for-each>
                
                <td colspan="2" class="detailsLabel">SCARS/MARKS/TATTOOS</td>
                <td colspan="2">
                	<xsl:variable name="smtCount" select="count(rap:RapSheetPerson/nc:PersonPhysicalFeature)"/>
                	<xsl:apply-templates select="rap:RapSheetPerson/nc:PersonPhysicalFeature">
                		<xsl:with-param name="smtCount" select="$smtCount"/>
                	</xsl:apply-templates>
                </td>
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">ALIASES</td>
                <td colspan="6">
                	<xsl:variable name="aliasCount" select="count(rap:RapSheetPerson/nc:PersonAlternateName)"/>
                	<xsl:apply-templates select="rap:RapSheetPerson/nc:PersonAlternateName">
                		<xsl:with-param name="aliasCount" select="$aliasCount"/>
                	</xsl:apply-templates>
                </td>
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">RESIDENCE ADDRESS</td>
                
                 <td colspan="6">
<!--                 	<xsl:apply-templates select="nc:Location/nc:LocationAddress" mode="constructAddress"/> -->
						<xsl:apply-templates select="nc:ResidenceAssociation"/>
                 </td>
            	
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">CAUTION DETAILS</td>
                <xsl:choose>
                	<xsl:when test ="rap:RapSheetPerson/rap:SubjectCautionInformationText">
               		 <td colspan="6"><xsl:apply-templates select="rap:RapSheetPerson/rap:SubjectCautionInformationText"/></td>
               		 </xsl:when>
               		 <xsl:otherwise><td colspan="6"><xsl:text>None</xsl:text></td></xsl:otherwise>
               	</xsl:choose>
            </tr>  
        </table>
        
    </xsl:template>
	
	<xsl:template match="nc:ResidenceAssociation">
		<xsl:variable name="position" select="position()"/>
		<xsl:apply-templates select="nc:LocationReference">
			<xsl:with-param name="position" select="$position"/>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template match="nc:LocationReference">
		<xsl:param name="position"/>
		<xsl:variable name="locationID" select="@s:ref"/>
		<xsl:apply-templates select="/ch-doc:CriminalHistory/ch-ext:RapSheet/nc:Location[@s:id=$locationID]">
			<xsl:with-param name="position" select="$position"/>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template match="nc:Location">
		<xsl:param name="position"/>
		<xsl:apply-templates select="nc:LocationAddress" mode="constructAddress">
			<xsl:with-param name="position" select="$position"/>
		</xsl:apply-templates>
	</xsl:template>
    <xsl:template match="nc:LocationAddress" mode="constructAddress">
    <xsl:param name="position"/>
    		<xsl:if test="$position != 1">
    			<xsl:text> / </xsl:text>
    		</xsl:if>
 	 		<xsl:choose>
              	<xsl:when test="nc:StructuredAddress">
              		<xsl:variable name="addr" select="nc:StructuredAddress" />
              	   <xsl:value-of select="concat($addr/nc:LocationStreet,', ',$addr/nc:LocationCityName,', ',$addr/nc:LocationStateUSPostalServiceCode, ' ', $addr/nc:LocationPostalCode)"/>
              	</xsl:when>
               <xsl:when test="nc:AddressFullText"> 
               	<xsl:variable name="addr" select="nc:AddressFullText" />
               		<xsl:value-of select="$addr"/>
               </xsl:when>
          	</xsl:choose>
    </xsl:template>
    <xsl:template match="rap:SubjectCautionInformationText">
    	<xsl:value-of select="."/><br/>
    	<xsl:variable name="position" select="position()"/>
    </xsl:template>
    
	<xsl:template match="nc:PersonPhysicalFeature">
		<xsl:param name="smtCount"/>
		<xsl:variable name="position" select="position()"/>
		<xsl:value-of select="nc:PhysicalFeatureCategoryText"/><xsl:text> </xsl:text><xsl:value-of select="nc:PhysicalFeatureDescriptionText"/><xsl:text> </xsl:text><xsl:value-of select="nc:PhysicalFeatureLocationText"/>
		<xsl:if test="$position!=$smtCount">
			<xsl:text>, </xsl:text>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="nc:PersonAlternateName">
		<xsl:param name="aliasCount"/>
		<xsl:variable name="position" select="position()"/>
		<xsl:choose>
			<xsl:when test="nc:PersonFullName">
				<xsl:value-of select="nc:PersonFullName"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="nc:PersonGivenName"/><xsl:text></xsl:text><xsl:value-of select="nc:PersonSurName"/>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:if test="$position!=$aliasCount">
			<xsl:text>, </xsl:text>
		</xsl:if>
	</xsl:template>
	<xsl:template name ="cycles" >
		<script type="text/javascript" >
			$(function () {
				$("#criminalHistoryCycles").accordion({
					heightStyle: "content",			
					active: false,
  					collapsible: true,
  					activate: function( event, ui ) { 
  						var modalIframe = $("#modalIframe", parent.document);
  						modalIframe.height(modalIframe.contents().find("body").height() + 16);
  					}
				} );
			});
		</script>
		<div id="criminalHistoryCycles">
			<xsl:apply-templates select="//ch-ext:RapSheet/ch-ext:RapSheetCycle[rap:Arrest]" />
		</div>
	</xsl:template>
	
	<xsl:template match="//ch-ext:RapSheet/ch-ext:RapSheetCycle" >
		<xsl:variable name="arrestDate">
			<xsl:call-template name="formatDate">
				<xsl:with-param name="date" select="rap:Arrest[1]/nc:ActivityDate/nc:Date" />
			</xsl:call-template>
		</xsl:variable>
		<h3><xsl:value-of select="concat('CYCLE ',position(),', OTN: ',rap:Arrest[1]/j:ArrestAgencyRecordIdentification/nc:IdentificationID,', Arrest Date: ',$arrestDate)" /></h3>
		<div>
			<table>
				<tr>
					<td width="100%">
						<xsl:apply-templates select="rap:Arrest"/>
					</td>
				</tr>			
			</table>
		</div>
	</xsl:template>
	
	<xsl:template match="rap:Arrest">
		<xsl:call-template name="formatArrest">
			<xsl:with-param name="arrest" select="."/>
		</xsl:call-template>
		<xsl:apply-templates select="rap:ArrestCharge"/>
	</xsl:template>
	
	<xsl:template name="formatArrest">
		<xsl:param name="arrest"/>
		
		<p class="sectionTitle" style="font-size:150%">Arrest</p>
		<xsl:variable name="arrId" select="$arrest/@s:id"/>
		<p><span class="smallLabel">Arrest Date: </span> 
			<xsl:call-template name="formatDate">
				<xsl:with-param name="date" select="$arrest/nc:ActivityDate/nc:Date" />
			</xsl:call-template>
		</p>
		<p><span class="smallLabel">Offense Tracking Number (OTN): </span> <xsl:value-of select="$arrest/j:ArrestAgencyRecordIdentification/nc:IdentificationID" /></p>
		<p><span class="smallLabel">Arresting Agency: </span> <xsl:value-of select="//ch-ext:RapSheet/rap:Agency[@s:id=//rap:ArrestAgencyAssociation[nc:ActivityReference/@s:ref=$arrId]/nc:OrganizationReference/@s:ref]/nc:OrganizationName" /></p>
		<p><span class="smallLabel">Offense Date: </span> 
			<xsl:call-template name="formatDate">
				<xsl:with-param name="date" select="$arrest/nc:ActivityDate/nc:Date" />
			</xsl:call-template>
		</p>
	</xsl:template>
	
	<xsl:template match="rap:ArrestCharge">
		<xsl:variable name="chgid" select="j:ChargeTrackingIdentification/nc:IdentificationID"/> 
		
		<br />
		<p><span class="sectionTitle" style="font-size:125%">ARREST CHARGE #<xsl:value-of select="position()" /></span></p>
		<p><span class="smallLabel">Arrest Report Number (ARN): </span> <xsl:value-of select="j:ChargeTrackingIdentification/nc:IdentificationID" /></p>
		<p><span class="smallLabel">Arrest Charge Description: </span> <xsl:value-of select="j:ChargeDescriptionText" /></p>
		<p><span class="smallLabel">Arrest Charge Disposition: </span> <xsl:value-of select="j:ChargeDisposition/nc:DispositionDescriptionText" /></p>
		<p><span class="smallLabel">Statute: </span> <xsl:value-of select="rap:ChargeStatute/j:StatuteCodeIdentification/nc:IdentificationID" /></p>

		<xsl:variable name="prosecutionCount" select="count(../../rap:Prosecution[rap:ProsecutionCharge/j:ChargeTrackingIdentification/nc:IdentificationID[text()=$chgid]])" />
		<p class="sectionTitle">PROSECUTION</p>
		<xsl:choose>
			<xsl:when test="$prosecutionCount &gt; 0">
				<xsl:apply-templates select="../../rap:Prosecution[rap:ProsecutionCharge/j:ChargeTrackingIdentification/nc:IdentificationID[text()=$chgid]]">
					<xsl:with-param name="trackingID"><xsl:value-of select="$chgid"/></xsl:with-param>
				</xsl:apply-templates>
			</xsl:when>
			<xsl:otherwise>
				No prosecution information available.
			</xsl:otherwise>
		</xsl:choose>
		
		<xsl:variable name="courtActionCount" select="count(../../rap:CourtAction[rap:CourtCharge/j:ChargeTrackingIdentification/nc:IdentificationID[text()=$chgid]])" />
		<p class="sectionTitle">COURT ACTION</p>
		<xsl:choose>
			<xsl:when test="$courtActionCount &gt; 0">
				<xsl:apply-templates select="../../rap:CourtAction[rap:CourtCharge/j:ChargeTrackingIdentification/nc:IdentificationID[text()=$chgid]]">
					<xsl:with-param name="trackingID"><xsl:value-of select="$chgid"/></xsl:with-param>
				</xsl:apply-templates>
			</xsl:when>
			<xsl:otherwise>
				No court action information available.
			</xsl:otherwise>
		</xsl:choose>
		
		<xsl:variable name="sentencingCount" select="count(../../ch-ext:Sentencing/ch-ext:Sentence[rap:SentenceCharge/j:ChargeTrackingIdentification/nc:IdentificationID[text()=$chgid]])" />
		<p class="sectionTitle">SENTENCE</p>
		<xsl:choose>
			<xsl:when test="$sentencingCount &gt; 0">
				<xsl:apply-templates select="../../ch-ext:Sentencing/ch-ext:Sentence/rap:SentenceCharge[j:ChargeTrackingIdentification/nc:IdentificationID[text()=$chgid]]"/>
			</xsl:when>
			<xsl:otherwise>
				No sentencing information available.
				<p/>
			</xsl:otherwise>
		</xsl:choose>	
	</xsl:template>
	
	<xsl:template match="rap:Prosecution">
		<xsl:param name="trackingID"/>
		<xsl:variable name="chargeCount" select="count(rap:ProsecutionCharge[j:ChargeTrackingIdentification/nc:IdentificationID[text()=$trackingID]])"/>
		<p><span class="smallLabel">Prosecution Case Number: </span> <xsl:value-of select="rap:ProsecutionAgencyRecordIdentification/nc:IdentificationID" /></p>
		<xsl:apply-templates select="rap:ProsecutionCharge[j:ChargeTrackingIdentification/nc:IdentificationID[text()=$trackingID]]">
			<xsl:with-param name="chargeCount"><xsl:value-of select="$chargeCount"/></xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template match="rap:ProsecutionCharge">
		<xsl:param name="chargeCount"/>
		<xsl:if test="$chargeCount &gt; 1">
			<p><span class="detailsLabel">Probation Charge</span></p>
		</xsl:if>
		<p><span class="smallLabel">Arrest Report Number (ARN): </span> <xsl:value-of select="j:ChargeTrackingIdentification/nc:IdentificationID" /></p>
		<p><span class="smallLabel">Prosecution Charge Description: </span> <xsl:value-of select="j:ChargeDescriptionText" /></p>
		<p><span class="smallLabel">Prosecution Charge Disposition: </span> <xsl:value-of select="j:ChargeDisposition/nc:DispositionDescriptionText" /></p>
		<p><span class="smallLabel">Statute: </span> <xsl:value-of select="rap:ChargeStatute/j:StatuteCodeIdentification/nc:IdentificationID" /></p>
	</xsl:template>
	
	<xsl:template match="rap:CourtAction">
		<xsl:param name="trackingID"/>
		<xsl:variable name="chargeCount" select="rap:CourtCharge[j:ChargeTrackingIdentification/nc:IdentificationID[text()=$trackingID]]"/>
		<p><span class="smallLabel">Court Case Number: </span> <xsl:value-of select="rap:CourtRecordIdentification/nc:IdentificationID" /></p>
		<xsl:apply-templates select="rap:CourtCharge[j:ChargeTrackingIdentification/nc:IdentificationID[text()=$trackingID]]">
			<xsl:with-param name="chargeCount"><xsl:value-of select="$chargeCount"/></xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	
	<xsl:template match="rap:CourtCharge">
		<xsl:param name="chargeCount"/>
		<!-- inserting this conditional so an empty "Court Charge" won't appear -->
		<xsl:if test="j:ChargeDescriptionText[. !=''] or j:ChargeDisposition/nc:DispositionDescriptionText[. !=''] or rap:ChargeStatute/j:StatuteCodeIdentification/nc:IdentificationID[. != '']">
			<xsl:if test="chargeCount &gt; 1">
				<p><span class="detailsLabel">Court Charge</span></p>
			</xsl:if>
			<p><span class="smallLabel">Arrest Report Number (ARN): </span> <xsl:value-of select="j:ChargeTrackingIdentification/nc:IdentificationID" /></p>
			<p><span class="smallLabel">Charge Description: </span> <xsl:value-of select="j:ChargeDescriptionText" /></p>
			<p><span class="smallLabel">Charge Disposition: </span> <xsl:value-of select="j:ChargeDisposition/nc:DispositionDescriptionText" /></p>
			<p><span class="smallLabel">Statute: </span> <xsl:value-of select="rap:ChargeStatute/j:StatuteCodeIdentification/nc:IdentificationID" /></p>
		</xsl:if>
	</xsl:template>

	<xsl:template match="ch-ext:Sentencing/ch-ext:Sentence/rap:SentenceCharge">
		<p><span class="smallLabel">Arrest Report Number (ARN): </span> <xsl:value-of select="j:ChargeTrackingIdentification/nc:IdentificationID" /></p>
		<p><span class="smallLabel">Sentence Disposition: </span> <xsl:value-of select="../nc:ActivityDisposition/nc:DispositionDescriptionText" /></p>
		<xsl:if test="j:ChargeDescriptionText[. != ''] or j:ChargeDisposition/nc:DispositionDescriptionText[. != ''] or rap:ChargeStatute/j:StatuteCodeIdentification/nc:IdentificationID[.!='']">
			<p><span class="smallLabel">Sentence Charge Description: </span> <xsl:value-of select="j:ChargeDescriptionText" /></p>
			<p><span class="smallLabel">Sentence Charge Disposition: </span> <xsl:value-of select="j:ChargeDisposition/nc:DispositionDescriptionText" /></p>
			<p><span class="smallLabel">Statute: </span> <xsl:value-of select="rap:ChargeStatute/j:StatuteCodeIdentification/nc:IdentificationID" /></p>
		</xsl:if>
		<br/>
	</xsl:template>
 
</xsl:stylesheet>
