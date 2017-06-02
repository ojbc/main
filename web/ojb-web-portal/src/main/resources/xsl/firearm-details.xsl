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
    xmlns:jxdm="http://niem.gov/niem/domains/jxdm/4.1"
    xmlns:usps-states="http://niem.gov/niem/usps_states/2.0"
    xmlns:error="http://ojbc.org/IEPD/Extensions/PersonQueryErrorReporting/1.0"
    xmlns:firearm-ext="http://ojbc.org/IEPD/Extensions/FirearmRegistrationQueryResults/1.0"
    xmlns:nc="http://niem.gov/niem/niem-core/2.0"
    xmlns:firearm="http://ojbc.org/IEPD/Exchange/FirearmRegistrationQueryResults/1.0"
    xmlns:s="http://niem.gov/niem/structures/2.0"
    xmlns:hi="http://ojbc.org/IEPD/Extensions/Hawaii/FirearmCodes/1.0"
	xmlns:demostate-codes="http://ojbc.org/IEPD/Extensions/demostate/FirearmCodes/1.0"
    exclude-result-prefixes="#all"
>
	<xsl:import href="firearm-registration-details.xsl" />
	<xsl:import href="person-firearm-registration-details.xsl" />
	<xsl:import href="_formatters.xsl" />
    <xsl:output method="html" encoding="UTF-8" />

    <xsl:template match="/">
    	<xsl:choose>
    		<xsl:when test="/firearm:FirearmRegistrationQueryResults">
    			<xsl:call-template name="FirearmRegistration"/>
    		</xsl:when>
    		<xsl:when test="/firearm:PersonFirearmRegistrationQueryResults">
    			<xsl:call-template name="PersonFirearmRegistration"/>
    		</xsl:when>
    	</xsl:choose>
   </xsl:template>
   
     <xsl:template match="nc:Person">
    	<xsl:variable name="pid" select="@s:id"/>
        <table class="detailsTable">
            <tr>
                <td colspan="8" class="detailsTitle">REGISTRANT INFORMATION</td>
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">NAME</td>
                <td colspan="2">
                	<xsl:choose>
                		<xsl:when test="nc:PersonName/nc:PersonGivenName[. !=''] or nc:PersonName/nc:PersonMiddleName[. != ''] or nc:PersonName/nc:PersonSurName[. !='']">
                			<xsl:value-of select="nc:PersonName/nc:PersonGivenName"/><xsl:text> </xsl:text><xsl:value-of select="nc:PersonName/nc:PersonMiddleName"/><xsl:text> </xsl:text><xsl:value-of select="nc:PersonName/nc:PersonSurName"/>
                		</xsl:when>
                		<xsl:when test="nc:PersonName/nc:PersonFullName[. !='']">
                			<xsl:value-of select="nc:PersonName/nc:PersonFullName" />
                		</xsl:when>
                	</xsl:choose>
                </td>
                <td colspan="2" class="detailsLabel">DOB</td>
                <td colspan="2">
                     <xsl:call-template name="formatDate">
						<xsl:with-param name="date" select="nc:PersonBirthDate/nc:Date" />
					</xsl:call-template>
                </td>
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">OCCUPATION</td>
                <td colspan="2"><xsl:value-of select="/*/nc:PersonEmploymentAssociation[nc:EmployeeReference/@s:ref]/nc:EmployeeOccupationText" /></td>
                <td colspan="2" class="detailsLabel">SEX</td>
                <td colspan="2">
	               	<xsl:call-template name="formatSex" >
	               		<xsl:with-param name="sexCode" select="nc:PersonSexCode"/>
	               	</xsl:call-template>
              	</td>
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">RES ADDRESS</td>
                <td colspan="2">   
                <xsl:choose>             
                    <xsl:when test="/*/nc:Location[@s:id=/*/nc:ResidenceAssociation[nc:PersonReference/@s:ref=$pid]/nc:LocationReference/@s:ref]/nc:LocationAddress/nc:AddressFullText">
                        <xsl:value-of select="//nc:Location/nc:LocationAddress/nc:AddressFullText"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:variable name="psAddr" select="/*/nc:Location[@s:id=/*/nc:ResidenceAssociation[nc:PersonReference/@s:ref=$pid]/nc:LocationReference/@s:ref]/nc:LocationAddress/nc:StructuredAddress" />
                    	<xsl:call-template name="DisplayAddress">
	                   		<xsl:with-param name="sAddr" select="$psAddr"/>
	                   	</xsl:call-template>
                    </xsl:otherwise> 
                </xsl:choose>
                </td>
                <td colspan="2" class="detailsLabel">RACE</td>
				<td colspan="2">
					<xsl:call-template name="formatRace" >
                    	<xsl:with-param name="raceCode" select="nc:PersonRaceCode"/>
                    </xsl:call-template>            
                </td>
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">BUS ADDRESS</td>
                <td colspan="2">   
                <xsl:choose>             
                    <xsl:when test="/*/nc:ContactInformation[@s:id = /*/nc:EmployeeContactInformationAssociation[nc:PersonReference/@s:ref=$pid]/nc:ContactInformationReference/@s:ref]/nc:ContactEntity/nc:EntityOrganization/nc:OrganizationLocation/nc:LocationAddress/nc:AddressFullText">
                        <xsl:value-of select="/*/nc:ContactInformation[@s:id = /*/nc:EmployeeContactInformationAssociation[nc:PersonReference/@s:ref=$pid]/nc:ContactInformationReference/@s:ref]/nc:ContactEntity/nc:EntityOrganization/nc:OrganizationLocation/nc:LocationAddress/nc:AddressFullText"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:variable name="psAddr" select="/*/nc:ContactInformation[@s:id = /*/nc:EmployeeContactInformationAssociation[nc:PersonReference/@s:ref=$pid]/nc:ContactInformationReference/@s:ref]/nc:ContactEntity/nc:EntityOrganization/nc:OrganizationLocation/nc:LocationAddress/nc:StructuredAddress" />
                  		<xsl:call-template name="DisplayAddress">
	                   		<xsl:with-param name="sAddr" select="$psAddr"/>
	                   	</xsl:call-template>
                    </xsl:otherwise> 
                </xsl:choose>
                </td>
                <td colspan="2" class="detailsLabel">HEIGHT</td>
                <td colspan="2"><xsl:value-of select="nc:PersonHeightMeasure/nc:MeasureText" /></td>
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">RES PHONE</td>
                <td colspan="2"><xsl:apply-templates select="/*/nc:ContactInformation[@s:id=/*/nc:PersonContactInformationAssociation[nc:PersonReference/@s:ref=$pid]/nc:ContactInformationReference/@s:ref]/nc:ContactTelephoneNumber/nc:FullTelephoneNumber/nc:TelephoneNumberFullID" /></td>
                <td colspan="2" class="detailsLabel">WEIGHT</td>
                <td colspan="2"><xsl:value-of select="nc:PersonWeightMeasure/nc:MeasureText" /></td>
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">BUS PHONE</td>
                <td colspan="2"><xsl:apply-templates select="/*/nc:ContactInformation[@s:id=/*/nc:EmployeeContactInformationAssociation[nc:PersonReference/@s:ref=$pid]/nc:ContactInformationReference/@s:ref]/nc:ContactTelephoneNumber/nc:FullTelephoneNumber/nc:TelephoneNumberFullID" /></td>
                <td colspan="2" class="detailsLabel">HAIR</td>
                <td colspan="2">
                <xsl:call-template name="formatHairColor" >
                	<xsl:with-param name="hairColorCode" select="nc:PersonHairColorCode"/>
                </xsl:call-template>
                </td>
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">SSN</td>
                <td colspan="2">
                	<xsl:variable name="ssn" select="nc:PersonSSNIdentification/nc:IdentificationID"/>
					<xsl:call-template name="formatSSN">
						<xsl:with-param name="ssn" select="$ssn"/>
					</xsl:call-template>
                </td>
                <td colspan="2" class="detailsLabel">EYES</td>
                <td colspan="2">
	                <xsl:call-template name="formatEyeColor" >
	                	<xsl:with-param name="eyeColorCode" select="nc:PersonEyeColorCode"/>
	                </xsl:call-template>
                </td>
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">PLACE OF BIRTH</td>                
                <td colspan="2">
	                <xsl:choose>             
	                    <xsl:when test="nc:PersonBirthLocation/nc:LocationAddress/nc:AddressFullText">
	                        <xsl:value-of select="nc:PersonBirthLocation/nc:LocationAddress/nc:AddressFullText"/>
	                    </xsl:when>
	                    <xsl:otherwise>
	                    <xsl:variable name="psAddr" select="nc:PersonBirthLocation/nc:LocationAddress/nc:StructuredAddress" />
	                   	<xsl:call-template name="DisplayAddress">
	                   		<xsl:with-param name="sAddr" select="$psAddr"/>
	                   	</xsl:call-template>
	                    </xsl:otherwise> 
	                </xsl:choose>
                </td>
                
                <td colspan="2" class="detailsLabel">COMPLEXION</td>
                <td colspan="2"><xsl:value-of select="nc:PersonComplexionText" /></td>
            </tr>
            <tr>
			    <td colspan="2" class="detailsLabel">DL#/ISSUER</td>
			    <td colspan="1"><xsl:value-of select="/*/nc:DriverLicense/nc:DriverLicenseIdentification/nc:IdentificationID"/></td>
			    <td colspan="1"><xsl:value-of select="/*/nc:DriverLicense/nc:DriverLicenseIdentification/nc:IdentificationSourceText"/></td>
                <td colspan="2" class="detailsLabel">STATE ID</td>
                <td colspan="2"><xsl:value-of select="nc:PersonStateIdentification/nc:IdentificationID" /></td>
            </tr>   
            <tr>
                <td colspan="2" class="detailsLabel">CURRENT CITIZENSHIP</td>
                <td colspan="2"><xsl:value-of select="nc:PersonCitizenshipText" /></td>                            
                <td colspan="2" class="detailsLabel">LEOSA</td>
                <td colspan="2"><xsl:value-of select="/*/firearm-ext:FirearmRegistrant[nc:RoleOfPersonReference/@s:ref=$pid]/firearm-ext:FirearmRegistrantLawEnforcementOfficersSafetyActIndicator"/></td>
            </tr> 			
            <tr>
            	<td colspan="2" class="detailsLabel">COMMENTS</td>
                <td colspan="2"><xsl:value-of select="/*/firearm-ext:FirearmRegistrant[nc:RoleOfPersonReference/@s:ref=$pid]/firearm-ext:FirearmRegistrantCommentsText"/></td>                
                <td colspan="2" class="detailsLabel"><!-- EMPTY --></td>
                <td colspan="2"><!-- EMPTY --></td>
            </tr>   			                   
        </table>
    </xsl:template>
    
    <xsl:template match="nc:PropertyRegistrationAssociation">
		<xsl:variable name="pRegSid" select="nc:ItemRegistrationReference/@s:ref"/>
		<xsl:variable name="pReg" select="//firearm-ext:ItemRegistration[@s:id=$pRegSid]" />
		<xsl:variable name="pRegStatusCode_HI" select="$pReg/firearm-ext:RegistrationStatus/hi:FirearmRegistrationStatusCode"/>
		<xsl:variable name="pRegStatusText" select="$pReg/firearm-ext:RegistrationStatus/firearm-ext:FirearmRegistrationStatusText"/>
		<xsl:variable name="pFirearmRef" select="nc:ItemReference/@s:ref" />
		<xsl:variable name="pFirearm" select="//firearm-ext:Firearm[@s:id=$pFirearmRef]" />
		<xsl:if test="/firearm:PersonFirearmRegistrationQueryResults">
			<h3>
				<xsl:value-of select="concat('Firearm Registration - ', $pReg/nc:RegistrationIdentification/nc:IdentificationID)" />
				<xsl:choose>
					<xsl:when test="normalize-space($pRegStatusCode_HI) != ''">
						<xsl:text> (</xsl:text>
						<xsl:call-template name="formatFirearmRegistrationStatusCode_HI">
							<xsl:with-param name="registrationStatusCode_HI" select="$pRegStatusCode_HI"></xsl:with-param>
						</xsl:call-template>
						<xsl:text>)</xsl:text>
					</xsl:when>
					<xsl:when test="normalize-space($pRegStatusText)">
						<xsl:text> (</xsl:text>
							<xsl:value-of select="$pRegStatusText"/>
						<xsl:text>)</xsl:text>
					</xsl:when>
				</xsl:choose>
			</h3>
		</xsl:if>
		<div>
			<table class="detailsTable">
				<tr>
					<td colspan="8" class="detailsTitle">REGISTRATION INFORMATION</td>
				</tr>
				<tr>
	   				<td colspan="2" class="detailsLabel">REG NO.</td>    
	   				<td colspan="6"><xsl:value-of select="$pReg/nc:RegistrationIdentification/nc:IdentificationID" /></td>     
	            </tr> 
	            <tr>
	                <td colspan="2" class="detailsLabel">COUNTY</td>
	                <td colspan="2"><xsl:value-of select="$pReg/nc:LocationCountyName" /></td>
	                <td colspan="2" class="detailsLabel">PRINTED DATE</td>
	                <td colspan="2">
	                	<xsl:call-template name="formatDate">
	                		<xsl:with-param name="date" select="$pReg/firearm-ext:RegistrationFingerprintDate/nc:Date"></xsl:with-param>
	                	</xsl:call-template>
	                 </td>
	            </tr>
	            <tr>
	                <td colspan="2" class="detailsLabel">REG DATE</td>
	                <td colspan="2">
		                <xsl:call-template name="formatDate">
							<xsl:with-param name="date" select="$pReg/nc:RegistrationEffectiveDate/nc:Date" />
						</xsl:call-template>
	                </td>
	                <td colspan="2" class="detailsLabel">STATUS</td>

	                <td colspan="2">
	                	<xsl:choose>
	                		<xsl:when test="normalize-space($pRegStatusCode_HI) != ''">
	                			<xsl:call-template name="formatFirearmRegistrationStatusCode_HI">
	                				<xsl:with-param name="registrationStatusCode_HI" select="$pRegStatusCode_HI"/>
	                			</xsl:call-template>
	                		</xsl:when>
	                		<xsl:when test="normalize-space($pRegStatusText) != ''">
	                			<xsl:value-of select="$pRegStatusText" />
	                		</xsl:when>
	                	</xsl:choose>
	                	
	                </td>
	            </tr>
	            <tr>
	                <td colspan="2" class="detailsLabel">PERMIT NO.</td>
	                <td colspan="2"><xsl:value-of select="$pReg/firearm-ext:PermitNumber" /></td>
	                <td colspan="2" class="detailsLabel">STATUS DATE</td>
	                <td colspan="2">
	                	<xsl:call-template name="formatDate">
							<xsl:with-param name="date" select="$pReg/firearm-ext:RegistrationStatus/nc:StatusDate/nc:Date" />
						</xsl:call-template>
	                </td>
	            </tr>
	            <tr>
	                <td colspan="2" class="detailsLabel">PERMIT ISSUE DATE</td>
	                <td colspan="2">
		                <xsl:call-template name="formatDate">
							<xsl:with-param name="date" select="$pReg/firearm-ext:PermitDate/nc:Date" />
						</xsl:call-template>
	                </td>
	            	<td colspan="2" class="detailsLabel">AGE AT REG</td>
	                <td colspan="2"><xsl:value-of select="$pReg/firearm-ext:AgeAtRegistration/nc:MeasureText"/></td>
	            </tr>
	            <tr>
	   				<td colspan="2" class="detailsLabel">NOTES</td>    
	   				<td colspan="6"><xsl:value-of select="$pReg/firearm-ext:RegistrationNotesText"/></td>     
	            </tr>  
	            <tr>
	            </tr>
	            <tr>
	   				<td colspan="8" class="detailsTitle">FIREARM INFORMATION</td>         
	            </tr>
	            <tr>
	            	<td colspan="2" class="detailsLabel">FACTORY SERIAL NO.</td>
	                <td colspan="2"><xsl:value-of select="$pFirearm/nc:ItemSerialIdentification/nc:IdentificationID"/></td>    
	                <td colspan="2" class="detailsLabel">GAUGE/CALIBER</td>
	                <td colspan="2">
	                	<xsl:apply-templates select ="$pFirearm/nc:FirearmGaugeText"/>
	                	<xsl:choose>
	                		<xsl:when test="normalize-space($pFirearm/hi:FirearmCaliberCode) != ''">
	                			<xsl:value-of select="$pFirearm/hi:FirearmCaliberCode"/>
	                		</xsl:when>
	                		<xsl:when test="normalize-space($pFirearm/nc:FirearmCaliberText) != ''">
	                			<xsl:value-of select="$pFirearm/nc:FirearmCaliberText"/>
	                		</xsl:when>
	                	</xsl:choose>
	          
	                </td>             
	            </tr>
	            <tr>
	            	<td colspan="2" class="detailsLabel">MAKE</td>
	                <td colspan="2">
	                	<xsl:choose>
	                		<xsl:when test="normalize-space($pFirearm/hi:FirearmMakeCode) != ''">
	                			<xsl:value-of select="$pFirearm/hi:FirearmMakeCode"/>
	                		</xsl:when>
	                		<xsl:when test="normalize-space($pFirearm/demostate-codes:FirearmMakeCode) != ''">
	                			<xsl:value-of select="$pFirearm/demostate-codes:FirearmMakeCode"/>
	                			<xsl:choose>
	                			<xsl:when test="normalize-space($pFirearm/firearm-ext:FirearmMakeText) != ''">
	                				<xsl:text> (</xsl:text><xsl:value-of select="$pFirearm/firearm-ext:FirearmMakeText"/><xsl:text>)</xsl:text>
	                			</xsl:when>
	                			</xsl:choose>
	                		</xsl:when>
	                		<xsl:when test="normalize-space($pFirearm/firearm-ext:FirearmMakeText) != ''">
	                			<xsl:value-of select="$pFirearm/firearm-ext:FirearmMakeText"/>
	                		</xsl:when>
	                	</xsl:choose>
	                </td>
		            <td colspan="2" class="detailsLabel">ACTION</td>
	                <td colspan="2"><xsl:value-of select="$pFirearm/nc:FirearmCategoryDescriptionCode"/></td>         
	            </tr>
	            <tr>
	            	<td colspan="2" class="detailsLabel">MODEL</td>
	                <td colspan="2"><xsl:value-of select="$pFirearm/nc:ItemModelName"/></td>
		            <td colspan="2" class="detailsLabel">TYPE</td>
	                <td colspan="2"><xsl:value-of select="$pFirearm/nc:FirearmCategoryCode"/></td>            
	            </tr>
	            <tr>
	            	<td colspan="2" class="detailsLabel">DATE RECEIVED</td>
	                <td colspan="2">
		                <xsl:call-template name="formatDate">
							<xsl:with-param name="date" select="$pFirearm/firearm-ext:FirearmReceivedDate/nc:Date" />
						</xsl:call-template>
					</td>
		            <td colspan="2" class="detailsLabel">BARREL LENGTH</td>
	                <td colspan="2">
	                	<xsl:value-of select="$pFirearm/nc:FirearmBarrelLengthMeasure/nc:MeasureText"/>
	                	<xsl:choose>
	                		<xsl:when test="$pFirearm/nc:FirearmBarrelLengthMeasure/nc:LengthUnitCode = 'INH' and $pFirearm/nc:FirearmBarrelLengthMeasure/nc:MeasureText[not(contains(.,'&quot;'))]">
	                			<xsl:text>"</xsl:text>
	                		</xsl:when>
	                	</xsl:choose>
	                </td>            
	            </tr>	
	            <tr>
	            	<td colspan="2" class="detailsLabel">STATUS</td>
	                <td colspan="2">
	                	<xsl:choose>
	                		<xsl:when test="normalize-space($pFirearm/firearm-ext:FirearmStatus/hi:FirearmStatusCode) != ''">
	                			<xsl:value-of select="$pFirearm/firearm-ext:FirearmStatus/hi:FirearmStatusCode"/>
	                		</xsl:when>
	                		<xsl:when test="normalize-space($pFirearm/firearm-ext:FirearmStatus/firearm-ext:FirearmStatusText) != ''">
	                			<xsl:value-of select="$pFirearm/firearm-ext:FirearmStatus/firearm-ext:FirearmStatusText"/>
	                		</xsl:when>
	                	</xsl:choose>
	                	
	                </td>
		            <td colspan="2" class="detailsLabel">STATUS DATE</td>
	                <td colspan="2">
	                	<xsl:call-template name="formatDate">
	                		<xsl:with-param name="date" select="$pFirearm/firearm-ext:FirearmStatus/nc:StatusDate/nc:Date"/>
	                	</xsl:call-template>
	                	
	                </td>            
	            </tr> 
	            <tr>
	            	<td colspan="2" class="detailsLabel">ACQUIRED FROM</td>
	                <td colspan="2"><xsl:value-of select="$pFirearm/firearm-ext:FirearmAcquisition/firearm-ext:FirearmAcquisitionSourceDescriptionText"/> </td>  
	            	<td colspan="2" class="detailsLabel">IMPORTER</td>
	                <td colspan="2"><xsl:value-of select="$pFirearm/firearm-ext:FirearmImporter"/></td>     
	            </tr>   
	            <tr>
		            <td colspan="2" class="detailsLabel">ACQUIRED FROM ADDRESS</td>
	                <td colspan="6"><xsl:value-of select="$pFirearm/firearm-ext:FirearmAcquisition/firearm-ext:FirearmAcquisitionLocationAddress/nc:AddressFullText"/> </td>            
	            </tr>         	          
	            
	           
	        </table>
      	</div>
    </xsl:template>
   <xsl:template match="nc:FirearmGaugeText">
   		<xsl:value-of select="."/>
	   	<xsl:if test="preceding-sibling::hi:FirearmCaliberCode or preceding-sibling::nc:FirearmCaliberText">
	   		<xsl:text>/</xsl:text>
	   	</xsl:if>
   		
   </xsl:template>
   <xsl:template name="DisplayAddress">
    	<xsl:param name="sAddr"/>
    	<xsl:if test="$sAddr/nc:AddressDeliveryPointText[. != '']">
    		<xsl:value-of select="$sAddr/nc:AddressDeliveryPointText"/>
    		<xsl:if test="$sAddr/nc:AddressDeliveryPointText/following-sibling::*[normalize-space(.) !='']"><xsl:text>, </xsl:text></xsl:if>
    	</xsl:if>
    	<xsl:if test="$sAddr/nc:LocationCityName[. !='']">
    		<xsl:value-of select="$sAddr/nc:LocationCityName"/>
    		<xsl:if test="$sAddr/nc:LocationCityName/following-sibling::*[normalize-space(.) !='']"><xsl:text>, </xsl:text></xsl:if>
    	</xsl:if>
    	<xsl:if test="$sAddr/nc:LocationStateName[. !='']">
    		<xsl:value-of select="$sAddr/nc:LocationStateName"/>
    		<xsl:if test="$sAddr/nc:LocationStateName/following-sibling::*[normalize-space(.) !='']"><xsl:text>, </xsl:text></xsl:if>
    	</xsl:if>
    	<xsl:if test="$sAddr/nc:LocationPostalCode[. !='']">
    		<xsl:value-of select="$sAddr/nc:LocationPostalCode"/>
    	</xsl:if>
    </xsl:template>
  </xsl:stylesheet>