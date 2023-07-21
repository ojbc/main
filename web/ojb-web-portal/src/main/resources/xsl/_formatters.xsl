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
	xmlns:nc="http://release.niem.gov/niem/niem-core/3.0/"
	xmlns:nc20="http://niem.gov/niem/niem-core/2.0" 
	xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="#all">
	
	<xsl:template name="returnAgeFromDOB">
		<xsl:param name="dateOfBirth"/>
	    <xsl:choose>
	        <xsl:when test="month-from-date(current-date()) > month-from-date($dateOfBirth) or month-from-date(current-date()) = month-from-date($dateOfBirth) and day-from-date(current-date()) >= day-from-date($dateOfBirth)">
	            <xsl:value-of select="year-from-date(current-date()) - year-from-date($dateOfBirth)" />
	        </xsl:when>
	        <xsl:otherwise>
	            <xsl:value-of select="year-from-date(current-date()) - year-from-date($dateOfBirth) - 1" />
	        </xsl:otherwise>
	    </xsl:choose>
	</xsl:template>
	

	<xsl:template name="formatDate">
		<xsl:param name="date" />
			<xsl:if test="normalize-space($date) != ''">
				<xsl:value-of select="string-join((substring($date,6,2),substring($date,9,2),substring($date,1,4)),'/')" />
			</xsl:if>
	</xsl:template>
	
	<!-- Converts YYYY-MM-DD to MM/DD/YYYY -->
	<xsl:template match="*|@*" mode="formatDateAsMMDDYYYY">
		<xsl:value-of select="format-date(.,'[M01]/[D01]/[Y0001]')"/>
	</xsl:template>
	
	<xsl:template match="*|@*" mode="formatDateTimeAsMMDDYYYY">
		<xsl:value-of select="format-dateTime(.,'[M01]/[D01]/[Y0001]')"/>
	</xsl:template>
	
	<!-- Converts YYYY-MM-DD to MM/DD/YYYY - HH:mm -->
	<xsl:template match="*|@*" mode="formatDateTime">
		<xsl:value-of select="format-dateTime(.,'[M01]/[D01]/[Y0001] - [H01]:[m01]')"/>
	</xsl:template>
	
	<xsl:template name="formatSSN">
		<xsl:param name="ssn" />
	
		<xsl:choose>
		    <xsl:when test="not(contains($ssn, '-')) and normalize-space($ssn) !=''">
				<xsl:value-of select="concat(substring($ssn,1,3),'-',substring($ssn,4,2),'-',substring($ssn,6,4))" />
		    </xsl:when>
		    <xsl:otherwise>
				<xsl:value-of select="$ssn" />
		    </xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="formatHeight">
		<xsl:param name="heightInInches" />
		
		<xsl:variable name="heightInteger" select="number($heightInInches)"  />
		<xsl:variable name="feet" select="floor($heightInteger div 12)" />
		<xsl:variable name="inches" select="floor($heightInteger mod 12)" />
		<xsl:if test="$feet &gt;= 1" ><xsl:value-of select='concat($feet,"&apos;")' /></xsl:if>
		<xsl:if test="$feet &gt;= 1 and $inches &gt; 0" ><xsl:text> </xsl:text></xsl:if>
		<xsl:if test="$inches &gt; 0" ><xsl:value-of select="concat($inches,'&quot;')" /></xsl:if>
	</xsl:template>

	<xsl:template name="formatRace">
		<xsl:param name="raceCode" />
		<xsl:variable name="map">
			<entry key="A">Asian</entry>
			<entry key="B">Black</entry>
			<entry key="I">American Indian</entry>
			<entry key="W">White</entry>
			<entry key="U">Unknown</entry>
		</xsl:variable>
		
		<xsl:call-template name="getValueFromMapWithDefault">
			<xsl:with-param name="key" select="$raceCode" />
			<xsl:with-param name="map" select="$map" />
		</xsl:call-template>

	</xsl:template>

	<xsl:template name="formatSex">
		<xsl:param name="sexCode" />
		<xsl:variable name="map">
			<entry key="M">Male</entry>
			<entry key="F">Female</entry>
			<entry key="U">Unknown</entry>
		</xsl:variable>
		
		<xsl:call-template name="getValueFromMapWithDefault">
			<xsl:with-param name="key" select="$sexCode" />
			<xsl:with-param name="map" select="$map" />
		</xsl:call-template>
	</xsl:template>


	<xsl:template name="formatTelephoneNumber">
		<xsl:param name="tNumber" />
        <xsl:if test="$tNumber != ''">    
        	<xsl:variable name="tNumberDigitsOnly" select="replace($tNumber, '[^0-9]', '')"/>
        	
        	<xsl:choose>
        		<xsl:when test="string-length($tNumberDigitsOnly) = 10">
        			<xsl:value-of select="concat('(',substring($tNumberDigitsOnly,1,3),')',substring($tNumberDigitsOnly,4,3),'-',substring($tNumberDigitsOnly,7,4))"/>
        		</xsl:when>
        		<xsl:when test="string-length($tNumberDigitsOnly) = 11">
        			<xsl:value-of select="concat(substring($tNumberDigitsOnly,1,1),'(',substring($tNumberDigitsOnly,2,3),')',substring($tNumberDigitsOnly,5,3),'-',substring($tNumberDigitsOnly,8,4))"/>
        		</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$tNumber"/>		
				</xsl:otherwise>        	
        	</xsl:choose>
			
		</xsl:if>

	</xsl:template>

	<xsl:template name="formatHairColor">
		<xsl:param name="hairColorCode" />
		<xsl:variable name="map">
			<entry key="BLK">Black</entry>
			<entry key="BLN">Blond or Strawberry</entry>
			<entry key="BLU">Blue</entry>
			<entry key="BRO">Brown</entry>
			<entry key="GRN">Green</entry>
			<entry key="GRY">Gray or Partially Gray</entry>
			<entry key="ONG">Orange</entry>
			<entry key="PLE">Purple</entry>
			<entry key="PNK">Pink</entry>
			<entry key="RED">Red or Auburn</entry>
			<entry key="SDY">Sandy</entry>
			<entry key="WHI">White</entry>
			<entry key="XXX">Unknown or Completely Bald</entry>
		</xsl:variable>
		
		<xsl:call-template name="getValueFromMapWithDefault">
			<xsl:with-param name="key" select="$hairColorCode" />
			<xsl:with-param name="map" select="$map" />
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="formatEyeColor">
		<xsl:param name="eyeColorCode" />
		<xsl:variable name="map">
			<entry key="BLK">Black</entry>
			<entry key="BLU">Blue</entry>
			<entry key="BRO">Brown</entry>
			<entry key="GRN">Green</entry>
			<entry key="GRY">Gray</entry>
			<entry key="HAZ">Hazel</entry>
			<entry key="MAR">Maroon</entry>
			<entry key="MUL">Multicolored</entry>
			<entry key="PNK">Pink</entry>
			<entry key="XXX">Unknown</entry>
		</xsl:variable>
		
		<xsl:call-template name="getValueFromMapWithDefault">
			<xsl:with-param name="key" select="$eyeColorCode" />
			<xsl:with-param name="map" select="$map" />
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template name="formatVehicleColor">
		<xsl:param name="vehicleColorCode" />
		<xsl:variable name="map">
			<entry key="AME">Amethyst</entry>
			<entry key="BGE">Beige</entry>
			<entry key="BLK">Black</entry>
			<entry key="BLU">Blue</entry>
			<entry key="BRO">Brown</entry>
			<entry key="BRZ">Bronze</entry>
			<entry key="CAM">Camouflage</entry>
			<entry key="COM">Chrome, Stainless Steel</entry>
			<entry key="CPR">Copper</entry>
			<entry key="CRM">Cream, Ivory</entry>
			<entry key="DBL">Blue, Dark</entry>
			<entry key="DGR">Green, Dark</entry>
			<entry key="GLD">Gold</entry>
			<entry key="GRN">Green</entry>
			<entry key="GRY">Gray</entry>
			<entry key="LAV">Lavender (Purple)</entry>
			<entry key="LBL">Blue, Light</entry>
			<entry key="LGR">Green, Light</entry>
			<entry key="MAR">Maroon, Burgundy (Purple)</entry>
			<entry key="MUL/COL">Multicolored</entry>
			<entry key="MVE">Mauve</entry>
			<entry key="ONG">Orange</entry>
			<entry key="PLE">Purple</entry>
			<entry key="PNK">Pink</entry>
			<entry key="RED">Red</entry>
			<entry key="SIL">Silver</entry>
			<entry key="TAN">Tan</entry>
			<entry key="TEA">Teal</entry>
			<entry key="TPE">Taupe</entry>
			<entry key="TRQ">Turquoise (Blue)</entry>
			<entry key="WHI">White</entry>
			<entry key="YEL">Yellow</entry>
			<entry key="XXX">Unknown</entry>
		</xsl:variable>
		
		<xsl:call-template name="getValueFromMapWithDefault">
			<xsl:with-param name="key" select="$vehicleColorCode" />
			<xsl:with-param name="map" select="$map" />
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template name="formatFirearmRegistrationStatusCode_HI">
		<xsl:param name="registrationStatusCode_HI" />
		<xsl:variable name="map">
			<entry key="A">Current</entry>
			<entry key="I">Not Current</entry>
			<entry key="R">Revoked</entry>
		</xsl:variable>
		
		<xsl:call-template name="getValueFromMapWithDefault">
			<xsl:with-param name="key" select="$registrationStatusCode_HI" />
			<xsl:with-param name="map" select="$map" />
		</xsl:call-template>
	</xsl:template>


<!-- Methods below are "private" not intended to be used outside this file -->

	<xsl:template name="getValueFromMapWithDefault">
		<xsl:param name="key" />
		<xsl:param name="map" />
		
		<xsl:variable name="value" select="$map/entry[@key=normalize-space($key)]" />
		<xsl:choose>
			<xsl:when test="$value">
				<xsl:value-of select="$value" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$key" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="nc:PersonAlternateName | nc20:PersonAlternateName | nc:PersonName | nc20:PersonName">
		<xsl:choose>
			<xsl:when test="*:PersonGivenName and *:PersonSurName">
			 	<xsl:if test="*:PersonGivenName">
			 		<xsl:value-of select="concat(*:PersonGivenName, ' ')"/>
			 	</xsl:if>
			 	<xsl:if test="*:PersonMiddleName">
			 		<xsl:value-of select="concat(*:PersonMiddleName, ' ')"/>
			 	</xsl:if>
			 	<xsl:if test="*:PersonSurName">
			 		<xsl:value-of select="*:PersonSurName"/>
			 	</xsl:if>
			 	<xsl:if test="*:PersonNameSuffixText">
			 		<xsl:value-of select="' ', *:PersonNameSuffixText"/>
			 	</xsl:if>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="*:PersonFullName"/>
			</xsl:otherwise>
		</xsl:choose>
       <xsl:if test="position() != last()">
           <xsl:text>, </xsl:text>
       </xsl:if>
	</xsl:template>
	
	<xsl:template match="nc:PersonName | nc20:PersonName" mode="primaryName">
		<b>
			<xsl:choose>
				<xsl:when test="*:PersonGivenName or *:PersonMiddleName or *:PersonSurName">
					<xsl:value-of select="concat(*:PersonSurName, ', ',*:PersonGivenName)"/>
					<xsl:if test="*:PersonMiddleName">
						<xsl:value-of select="concat(' ',*:PersonMiddleName)"/>
					</xsl:if>
				</xsl:when>
				<xsl:when test="*:PersonFullName[normalize-space()]">
					<xsl:value-of select="*:PersonFullName" />
				</xsl:when>
			</xsl:choose>
		</b>
	</xsl:template>
	<xsl:template match="nc:PersonName | nc20:PersonName" mode="firstNameFirst">
		<xsl:choose>
			<xsl:when test="*:PersonGivenName or *:PersonMiddleName or *:PersonSurName">
				<xsl:value-of select="*:PersonGivenName"/><xsl:text> </xsl:text><xsl:value-of select="*:PersonMiddleName"/><xsl:text> </xsl:text><xsl:value-of select="*:PersonSurName"/>
			</xsl:when>
			<xsl:when test="*:PersonFullName[normalize-space()]">
				<xsl:value-of select="*:PersonFullName" />
			</xsl:when>
		</xsl:choose> 
	</xsl:template>

	<xsl:template match="*|@*" mode="formatBooleanAsYesNo">
		<xsl:call-template name="formatBooleanAsYesNo">
			<xsl:with-param name="value" select="."></xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="formatBooleanAsYesNo">
		<xsl:param name="value"/>
		<xsl:choose>
			<xsl:when test="$value = 'true'">
				<xsl:text>Yes</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>No</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
    <xsl:template name="parseDurationDays">
        <xsl:param name="duration" />
        <xsl:if test="normalize-space($duration) !='' and contains($duration, 'D')">
            <xsl:choose>
                <xsl:when test="contains($duration, 'M')">
                    <xsl:value-of select="substring-before(substring-after($duration, 'M'), 'D')"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:choose>
                        <xsl:when test="contains($duration, 'Y')">
                            <xsl:value-of select="substring-before(substring-after($duration, 'Y'), 'D')"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="substring-before(substring-after($duration, 'P'), 'D')"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:if>
    </xsl:template>
        
    <xsl:template name="parseDurationMonths">
        <xsl:param name="duration" />
    
        <xsl:if test="normalize-space($duration) !='' and contains($duration, 'M')">
            <xsl:choose>
                <xsl:when test="contains($duration, 'Y')">
                    <xsl:value-of select="substring-before(substring-after($duration, 'Y'), 'M')"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="substring-before(substring-after($duration, 'P'), 'M')"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:if>
    </xsl:template>
        
    <xsl:template name="getDurationDescription">
        <xsl:param name="duration" />
    
        <xsl:variable name="years" select="substring-before(substring-after($duration, 'P'), 'Y')" />
        <xsl:variable name="months">
              <xsl:call-template name="parseDurationMonths">
                <xsl:with-param name="duration" select="$duration" />
              </xsl:call-template>
        </xsl:variable>
        <xsl:variable name="days">
              <xsl:call-template name="parseDurationDays">
                <xsl:with-param name="duration" select="$duration" />
              </xsl:call-template>
        </xsl:variable>
        <xsl:if test="normalize-space($years) != ''">
            <xsl:value-of select="concat($years, ' ')"></xsl:value-of>
            <xsl:choose>
                <xsl:when test="xs:integer($years) &gt; 1">
                    <xsl:text>years</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text>year</xsl:text>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:if>
    	<xsl:if test="normalize-space($months) != ''">
    		<xsl:if test="normalize-space($years) != ''"><xsl:text> </xsl:text></xsl:if>
            <xsl:value-of select="concat($months, ' ')"></xsl:value-of>
            <xsl:choose>
                <xsl:when test="xs:integer($months) &gt; 1">
                    <xsl:text>months</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text>month</xsl:text>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:if>
    	<xsl:if test="normalize-space($days) != ''">
        	<xsl:if test="normalize-space($years) != '' or normalize-space($months) != ''"><xsl:text> </xsl:text></xsl:if>
            <xsl:value-of select="concat($days, ' ')"></xsl:value-of>
            <xsl:choose>
                <xsl:when test="xs:integer($days) &gt; 1">
                    <xsl:text>days</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text>day</xsl:text>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:if>
    </xsl:template>
        
	
</xsl:stylesheet>    