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
<!-- 
Revision Notes:
Version 1.1		12/02/2009		Kate Silhol/Nlets	Correct XSLT to print ChargeDispositionOtherText
Version 2.0		10/22/2010		Kate Silhol/Nlets	Added 4.1 transformation logic and merged past 4.0 corrections
Version 3.0		4/11/2012			Kate Silhol/Nlets	Finalize 4.1 logic
Version 3.1		5/21/2012			Kate Silhol/Nlets	Corrected printing of rap:CaveatFormattedText
Version 3.2		6/19/2012			Kate Silhol/Nlets	Corrected all instances of j:ChargeStatute to be rap:ChargeStatute
Version 3.3		10/18/2012		Kate Silhol/Nlets	Corrected placement of Cycle loop closing to allow printing of PostSentenceAction segment
Version 3.4		11/6/2012			Kate Silhol/Nlets	Corrected printing of DocumentReceivedDate and DocumentFiledTime
Version 4.0		12/6/2012			Kate Silhol/Nlets	Allowed for both Nlets message envelopes and message header formats 
Version 4.1		1/2/2013			Kate Silhol/Nlets	Resolve extra tab in header issue
Version 4.2		10/24/2013		Kate Silhol/Nlets	Correct booking agency record ID printing, remove redundant chargeagencyrecordid printing
Version 4.3		12/17/2013		Kate Silhol/Nlets	Correct palm print and photo data printing 
Version 4.3.1		12/18/2013		Kate Silhol/Nlets	Further corrections to palm and photo and all other biometric capture organization printing
Version 4.3.2		12/19/2013		Kate Silhol/Nlets	Further corrections to palm and photo and all other biometric capture organization printing
Version 4.3.3		8/26/2014			Kate Silhol/Nlets	Correct bug in which if there are more citizenships reported than places of birth, the reported dates are not accurate. Correct printing of prosecution subject name.
Version 4.3.3		9/18/2014			Kate Silhol/Nlets	Eliminated space before carriage return after TXT
Version 4.4		11/10/2014		Kate Silhol/Nlets	Suppress printing of PersonRegisteredOffenderIndicator when inbound Indicator is sent as "false"
Version 4.5		12/9/2014			Kate Silhol/Nlets	Add BiometricCapturer option for printing under PersonDNA - now prints both BiometricCapturer and BiometricImage/BinaryCapturer. Also suppress printing of "Deceased" if PersonLivingIndicator is true 
 -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:rap="http://nlets.org/niem2/rapsheet/1.0" xmlns:ch-doc="http://ojbc.org/IEPD/Exchange/CriminalHistory/1.0" xmlns:ch-ext="http://ojbc.org/IEPD/Extensions/CriminalHistory/1.0" xmlns:j2="http://www.it.ojp.gov/jxdm/3.0" xmlns:nc="http://niem.gov/niem/niem-core/2.0" xmlns:j="http://niem.gov/niem/domains/jxdm/4.1" xmlns:ansi-nist="http://niem.gov/niem/ansi-nist/2.0" xmlns:s="http://niem.gov/niem/structures/2.0" xmlns:fs="http://niem.gov/niem/domains/familyServices/2.1" xmlns:n="http://www.nlets.org" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:n2="http://www.nlets.org/niem/1.0" xmlns:nh2="http://www.nlets.org/niem/nlets-hdr/1.0" exclude-result-prefixes="j2">
	<xsl:template match="/">
		<OJBCRapSheetTextFormat>
			<xsl:choose>
				<xsl:when test="//rap:ControlData[nc:IdentificationCategoryText='NCIChdr']/nc:IdentificationID">
					<xsl:text>HDR/</xsl:text>
					<xsl:value-of select="//rap:ControlData[nc:IdentificationCategoryText='NCIChdr']/nc:IdentificationID"/>
					<xsl:text>&#10;</xsl:text>
				</xsl:when>
				<xsl:when test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:ControlDataText">
					<xsl:text>HDR/</xsl:text>
					<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:ControlDataText"/>
					<xsl:text>&#10;</xsl:text>
				</xsl:when>
			</xsl:choose>
			<xsl:if test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:Attention">ATN/<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:Attention" disable-output-escaping="yes"/>
				<xsl:text>&#10;</xsl:text>
			</xsl:if>
			<xsl:if test="//ch-doc:CriminalHistory/ch-ext:RapSheet">
				<!-- Global Variables -->
				<xsl:variable name="posCol1">0</xsl:variable>
				<!-- Position to start Column one -->
				<xsl:variable name="posCol2">24</xsl:variable>
				<!-- Position to start Column two -->
				<xsl:variable name="posCol3">48</xsl:variable>
				<!-- Position to start Column three -->
				<xsl:variable name="posMaxWidth">72</xsl:variable>
				<!-- Position for Maximum width -->
				<xsl:variable name="amtColSpacer">2</xsl:variable>
				<!-- Amount to space between Columns -->
				<xsl:text>**********************  CRIMINAL HISTORY RECORD  ***********************&#10;&#10;</xsl:text>
				<xsl:variable name="metadataid1">
					<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/@s:metadata"/>
				</xsl:variable>
				<xsl:if test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata/nc:ReportedDate/nc:Date">
					<xsl:text>Data As Of              </xsl:text>
					<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata/nc:ReportedDate/nc:Date"/>
					<xsl:text>&#10;&#10;</xsl:text>
				</xsl:if>
				<xsl:if test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata/nc:ReportingOrganizationText">
					<xsl:text>Reporting Organization              </xsl:text>
					<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata/nc:ReportingOrganizationText"/>
					<xsl:text>&#10;&#10;</xsl:text>
				</xsl:if>
				<xsl:if test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata/nc:SourceIDText">
					<xsl:text>Reporting Organization ID              </xsl:text>
					<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata/nc:SourceIDText"/>
					<xsl:text>&#10;&#10;</xsl:text>
				</xsl:if>
				<xsl:text>****************************  Introduction  ****************************&#10;&#10;</xsl:text>
				<xsl:text>This rap sheet was produced in response to the following request:&#10;&#10;</xsl:text>
				<!-- ########## REQUESTER SECTION ########## -->
				<xsl:choose>
					<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:RapSheetPerson/nc:PersonName/nc:PersonFullName) &gt; 0">
						<xsl:text>Subject Name(s)         </xsl:text>
						<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:RapSheetPerson/nc:PersonName/nc:PersonFullName">
							<xsl:choose>
								<xsl:when test="position() = 1">
									<xsl:call-template name="wrapIn">
										<xsl:with-param name="Text" select="."/>
										<xsl:with-param name="StartPos" select="$posCol1"/>
										<xsl:with-param name="EndPos" select="$posMaxWidth"/>
										<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
										<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
										<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
										<xsl:with-param name="initialRun" select="0"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="wrapIn">
										<xsl:with-param name="Text" select="."/>
										<xsl:with-param name="StartPos" select="$posCol2"/>
										<xsl:with-param name="EndPos" select="$posMaxWidth"/>
										<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
										<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
										<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
										<xsl:with-param name="initialRun" select="0"/>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:text>&#10;</xsl:text>
						</xsl:for-each>
					</xsl:when>
				</xsl:choose>
				<!-- -->
				<xsl:choose>
					<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:RapSheetPerson/j:PersonAugmentation/j:PersonFBIIdentification/nc:IdentificationID) &gt; 0">
						<xsl:text>FBI Number              </xsl:text>
						<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:RapSheetPerson/j:PersonAugmentation/j:PersonFBIIdentification/nc:IdentificationID"/>
						<xsl:text>&#10;</xsl:text>
					</xsl:when>
				</xsl:choose>
				<!-- -->
				<xsl:choose>
					<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:RapSheetPerson/j:PersonAugmentation/j:PersonStateFingerprintIdentification/nc:IdentificationID) &gt; 0">
						<xsl:text>State Id Number         </xsl:text>
						<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:RapSheetPerson/j:PersonAugmentation/j:PersonStateFingerprintIdentification/nc:IdentificationID"/>
						<xsl:text> (</xsl:text>
						<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:RapSheetPerson/j:PersonAugmentation/j:PersonStateFingerprintIdentification/nc:IdentificationJurisdictionText"/>
						<xsl:text>)&#10;</xsl:text>
					</xsl:when>
				</xsl:choose>
				<!-- -->
				<xsl:choose>
					<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:RapSheetPerson/nc:PersonSSNIdentification/nc:IdentificationID) &gt; 0">
						<xsl:text>Social Security Number  </xsl:text>
						<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:RapSheetPerson/nc:PersonSSNIdentification/nc:IdentificationID">
							<xsl:choose>
								<xsl:when test="position() = 1">
									<xsl:call-template name="wrapIn">
										<xsl:with-param name="Text" select="."/>
										<xsl:with-param name="StartPos" select="$posCol1"/>
										<xsl:with-param name="EndPos" select="$posMaxWidth"/>
										<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
										<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
										<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
										<xsl:with-param name="initialRun" select="0"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="wrapIn">
										<xsl:with-param name="Text" select="."/>
										<xsl:with-param name="StartPos" select="$posCol2"/>
										<xsl:with-param name="EndPos" select="$posMaxWidth"/>
										<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
										<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
										<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
										<xsl:with-param name="initialRun" select="0"/>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:text>&#10;</xsl:text>
						</xsl:for-each>
					</xsl:when>
				</xsl:choose>
				<!-- -->
				<xsl:choose>
					<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:RapSheetPerson/nc:PersonBirthDate/nc:Date) &gt; 0">
						<xsl:text>Date of Birth           </xsl:text>
						<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:RapSheetPerson/nc:PersonBirthDate/nc:Date">
							<xsl:choose>
								<xsl:when test="position() = 1">
									<xsl:call-template name="wrapIn">
										<xsl:with-param name="Text" select="."/>
										<xsl:with-param name="StartPos" select="$posCol1"/>
										<xsl:with-param name="EndPos" select="$posMaxWidth"/>
										<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
										<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
										<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
										<xsl:with-param name="initialRun" select="0"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="wrapIn">
										<xsl:with-param name="Text" select="."/>
										<xsl:with-param name="StartPos" select="$posCol2"/>
										<xsl:with-param name="EndPos" select="$posMaxWidth"/>
										<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
										<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
										<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
										<xsl:with-param name="initialRun" select="0"/>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:text>&#10;</xsl:text>
						</xsl:for-each>
						<xsl:if test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:RapSheetPerson/nc:PersonBirthDate/nc:Date)=0">
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
					</xsl:when>
				</xsl:choose>
				<!-- -->
				<xsl:choose>
					<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:RapSheetPerson/j:PersonAugmentation/nc:DriverLicense/nc:DriverLicenseIdentification/nc:IdentificationID) &gt; 0">
						<xsl:text>Driver's License Number </xsl:text>
						<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:RapSheetPerson/j:PersonAugmentation/nc:DriverLicense/nc:DriverLicenseIdentification/nc:IdentificationID">
							<xsl:choose>
								<xsl:when test="position() = 1">
									<xsl:call-template name="wrapIn">
										<xsl:with-param name="Text" select="concat(nc:IdentificationID,' (',//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:RapSheetPerson/j:PersonAugmentation/nc:DriverLicense/nc:DriverLicenseIdentification/nc:IdentificationJurisdictionText,')')"/>
										<xsl:with-param name="StartPos" select="$posCol1"/>
										<xsl:with-param name="EndPos" select="$posMaxWidth"/>
										<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
										<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
										<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
										<xsl:with-param name="initialRun" select="0"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="wrapIn">
										<xsl:with-param name="Text" select="concat(nc:IdentificationID,' (',//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:RapSheetPerson/j:PersonAugmentation/nc:DriverLicense/nc:DriverLicenseIdentification/nc:IdentificationJurisdictionText,')')"/>
										<xsl:with-param name="StartPos" select="$posCol2"/>
										<xsl:with-param name="EndPos" select="$posMaxWidth"/>
										<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
										<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
										<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
										<xsl:with-param name="initialRun" select="0"/>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:text>&#10;</xsl:text>
						</xsl:for-each>
						<xsl:if test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:RapSheetPerson/j:PersonAugmentation/nc:DriverLicense/nc:DriverLicenseIdentification/nc:IdentificationID)=0">
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
					</xsl:when>
				</xsl:choose>
				<!-- -->
				<xsl:choose>
					<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:RapSheetPerson/nc:PersonOtherIdentification/nc:IdentificationID) &gt; 0">
						<xsl:text>Miscellaneous Number    </xsl:text>
						<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:RapSheetPerson/nc:PersonOtherIdentification/nc:IdentificationID">
							<xsl:variable name="i" select="position()"/>
							<xsl:choose>
								<xsl:when test="position() = 1">
									<xsl:choose>
										<xsl:when test="../nc:IdentificationJurisdictionText">
											<xsl:call-template name="wrapIn">
												<xsl:with-param name="Text" select="concat(.,' (',../nc:IdentificationJurisdictionText,'; ',../rap:IdentificationCategoryText,')')"/>
												<xsl:with-param name="StartPos" select="$posCol1"/>
												<xsl:with-param name="EndPos" select="$posMaxWidth"/>
												<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
												<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
												<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
												<xsl:with-param name="initialRun" select="0"/>
											</xsl:call-template>
										</xsl:when>
										<xsl:when test="../nc:IdentificationJurisdictionText">
											<xsl:call-template name="wrapIn">
												<xsl:with-param name="Text" select="concat(.,' (',../nc:IdentificationJurisdictionText,'; ',../rap:IdentificationCategoryText,')')"/>
												<xsl:with-param name="StartPos" select="$posCol1"/>
												<xsl:with-param name="EndPos" select="$posMaxWidth"/>
												<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
												<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
												<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
												<xsl:with-param name="initialRun" select="0"/>
											</xsl:call-template>
										</xsl:when>
									</xsl:choose>
								</xsl:when>
								<xsl:otherwise>
									<xsl:choose>
										<xsl:when test="../nc:IdentificationJurisdictionText">
											<xsl:call-template name="wrapIn">
												<xsl:with-param name="Text" select="concat(nc:IdentificationID,' (',../nc:IdentificationJurisdictionText,'; ',../rap:IdentificationCategoryText,')')"/>
												<xsl:with-param name="StartPos" select="$posCol2"/>
												<xsl:with-param name="EndPos" select="$posMaxWidth"/>
												<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
												<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
												<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
												<xsl:with-param name="initialRun" select="0"/>
											</xsl:call-template>
										</xsl:when>
										<xsl:when test="../nc:IdentificationJurisdictionText">
											<xsl:call-template name="wrapIn">
												<xsl:with-param name="Text" select="concat(nc:IdentificationID,' (',../nc:IdentificationJurisdictionText,'; ',../rap:IdentificationCategoryText,')')"/>
												<xsl:with-param name="StartPos" select="$posCol2"/>
												<xsl:with-param name="EndPos" select="$posMaxWidth"/>
												<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
												<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
												<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
												<xsl:with-param name="initialRun" select="0"/>
											</xsl:call-template>
										</xsl:when>
									</xsl:choose>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:text>&#10;</xsl:text>
						</xsl:for-each>
						<xsl:if test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:RapSheetPerson/nc:PersonOtherIdentification/nc:IdentificationID)=0">
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
					</xsl:when>
				</xsl:choose>
				<!-- -->
				<xsl:choose>
					<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:RapSheetPerson/rap:PersonSexText) &gt; 0">
						<xsl:text>Sex                     </xsl:text>
						<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:RapSheetPerson/rap:PersonSexText">
							<xsl:choose>
								<xsl:when test="position() = 1">
									<xsl:call-template name="wrapIn">
										<xsl:with-param name="Text" select="."/>
										<xsl:with-param name="StartPos" select="$posCol1"/>
										<xsl:with-param name="EndPos" select="$posMaxWidth"/>
										<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
										<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
										<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
										<xsl:with-param name="initialRun" select="0"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="wrapIn">
										<xsl:with-param name="Text" select="."/>
										<xsl:with-param name="StartPos" select="$posCol2"/>
										<xsl:with-param name="EndPos" select="$posMaxWidth"/>
										<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
										<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
										<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
										<xsl:with-param name="initialRun" select="0"/>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:text>&#10;</xsl:text>
						</xsl:for-each>
						<xsl:if test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:RapSheetPerson/rap:PersonSexText)=0">
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
					</xsl:when>
				</xsl:choose>
				<!-- -->
				<xsl:choose>
					<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:RapSheetPerson/rap:PersonRaceText) &gt; 0">
						<xsl:text>Race                    </xsl:text>
						<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:RapSheetPerson/rap:PersonRaceText">
							<xsl:choose>
								<xsl:when test="position() = 1">
									<xsl:call-template name="wrapIn">
										<xsl:with-param name="Text" select="."/>
										<xsl:with-param name="StartPos" select="$posCol1"/>
										<xsl:with-param name="EndPos" select="$posMaxWidth"/>
										<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
										<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
										<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
										<xsl:with-param name="initialRun" select="0"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="wrapIn">
										<xsl:with-param name="Text" select="."/>
										<xsl:with-param name="StartPos" select="$posCol2"/>
										<xsl:with-param name="EndPos" select="$posMaxWidth"/>
										<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
										<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
										<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
										<xsl:with-param name="initialRun" select="0"/>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:text>&#10;</xsl:text>
						</xsl:for-each>
						<xsl:if test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/j:PersonRaceText)=0">
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
					</xsl:when>
				</xsl:choose>
				<!-- -->
				<xsl:variable name="rapmetadataid">
					<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/@s:metadata"/>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/@s:metadata) &gt; 0">
						<xsl:text>Request Id              </xsl:text>
						<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$rapmetadataid]/nc:SourceIDText"/>
						<xsl:text>&#10;</xsl:text>
					</xsl:when>
				</xsl:choose>
				<!-- -->
				<xsl:choose>
					<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:PurposeCode) &gt; 0">
						<xsl:text>Purpose Code            </xsl:text>
						<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:PurposeCode"/>
						<xsl:text>&#10;</xsl:text>
					</xsl:when>
				</xsl:choose>
				<!-- -->
				<xsl:choose>
					<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:Attention) &gt; 0">
						<xsl:text>Attention               </xsl:text>
						<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:Attention" disable-output-escaping="yes"/>
						<xsl:text>&#10;</xsl:text>
					</xsl:when>
				</xsl:choose>
				<!-- -->
				<xsl:choose>
					<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/ansi-nist:TransactionControlIdentification/nc:IdentificationID) &gt; 0">
						<xsl:text>Transaction Control Number            </xsl:text>
						<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/ansi-nist:TransactionControlIdentification/nc:IdentificationID"/>
						<xsl:text>&#10;</xsl:text>
					</xsl:when>
				</xsl:choose>
				<xsl:text>&#10;</xsl:text>
				<xsl:text>The information in this rap sheet is subject to the following caveats:&#10;&#10;</xsl:text>
				<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:Caveat[nc:CaveatText]">
					<xsl:variable name="CaveatData">
						<xsl:if test="nc:CaveatText">
							<xsl:value-of select="normalize-space(nc:CaveatText)"/>
							<xsl:if test="not(. != '')">
								<xsl:text> (</xsl:text>
								<xsl:value-of select="rap:CaveatIssuingAuthorityText"/>
								<xsl:if test="rap:CaveatReferenceDate">
									<xsl:text>; </xsl:text>
									<xsl:value-of select="rap:CaveatReferenceDate"/>
								</xsl:if>
								<xsl:text>)</xsl:text>
							</xsl:if>
						</xsl:if>
					</xsl:variable>
					<xsl:call-template name="wrapIn">
						<xsl:with-param name="Text" select="$CaveatData"/>
						<xsl:with-param name="StartPos" select="$posCol1"/>
						<xsl:with-param name="EndPos" select="$posMaxWidth"/>
						<xsl:with-param name="WrapedStartPos" select="$posCol1"/>
						<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
						<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
						<xsl:with-param name="initialRun" select="0"/>
					</xsl:call-template>
					<xsl:if test="normalize-space($CaveatData) != ''">
						<xsl:text>&#10;&#10;</xsl:text>
					</xsl:if>
				</xsl:for-each>
				<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:Caveat[rap:CaveatFormattedText]">
					<xsl:variable name="CaveatData">
						<xsl:value-of select="normalize-space(rap:CaveatFormattedText)"/>
						<xsl:text>(</xsl:text>
						<xsl:value-of select="rap:CaveatIssuingAuthorityText"/>
						<xsl:if test="rap:CaveatReferenceDate">
							<xsl:text>; </xsl:text>
							<xsl:value-of select="rap:CaveatReferenceDate"/>
						</xsl:if>
						<xsl:text>)</xsl:text>
						<xsl:text>&#10;&#10;</xsl:text>
					</xsl:variable>
					<xsl:call-template name="wrapIn">
						<xsl:with-param name="Text" select="$CaveatData"/>
						<xsl:with-param name="StartPos" select="$posCol1"/>
						<xsl:with-param name="EndPos" select="$posMaxWidth"/>
						<xsl:with-param name="WrapedStartPos" select="$posCol1"/>
						<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
						<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
						<xsl:with-param name="initialRun" select="0"/>
					</xsl:call-template>
				</xsl:for-each>
				<!-- ########## Identifiction Section ########## -->
				<xsl:text>&#10;***************************  IDENTIFICATION  ***************************&#10;&#10;</xsl:text>
				<xsl:if test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonLivingIndicator) > 0">
					<xsl:variable name="metadataid">
						<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonLivingIndicator/@s:metadata"/>
					</xsl:variable>
					<xsl:if test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonLivingIndicator != 'true'">
						<xsl:text>Deceased                </xsl:text>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="concat(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonDeathDate/nc:Date,' (',//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportingOrganizationText,'; ',//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportedDate,'; ',//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:CommentText,')')"/>
							<xsl:with-param name="StartPos" select="$posCol1"/>
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
				</xsl:if>
				<xsl:if test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonName) > 0">
					<xsl:text>Subject Name(s)&#10;&#10;</xsl:text>
					<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonName">
						<!-- Name -->
						<xsl:value-of select="./nc:PersonSurName"/>
						<xsl:text>, </xsl:text>
						<xsl:if test="count(./nc:PersonNamePrefixText) > 0">
							<!-- if Prefix -->
							<xsl:value-of select="./nc:PersonNamePrefixText"/>
							<xsl:text/>
						</xsl:if>
						<xsl:if test="count(./nc:PersonGivenName) > 0">
							<!-- if First Name -->
							<xsl:value-of select="./nc:PersonGivenName"/>
							<xsl:text> </xsl:text>
							<xsl:text/>
						</xsl:if>
						<xsl:if test="count(./nc:PersonMiddleName) > 0">
							<!-- if Middle Name -->
							<xsl:value-of select="./nc:PersonMiddleName"/>
							<xsl:text> </xsl:text>
							<xsl:text/>
						</xsl:if>
						<xsl:if test="count(./nc:PersonNameSuffixText) > 0">
							<!-- if Suffix -->
							<xsl:value-of select="./nc:PersonNameSuffixText"/>
							<xsl:text/>
						</xsl:if>
						<xsl:text>&#10;</xsl:text>
					</xsl:for-each>
				</xsl:if>
				<!-- -->
				<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonAlternateName">
					<!-- Name -->
					<xsl:value-of select="./nc:PersonSurName"/>
					<xsl:text>, </xsl:text>
					<xsl:if test="count(./nc:PersonNamePrefixText) > 0">
						<!-- if Prefix -->
						<xsl:value-of select="./nc:PersonNamePrefixText"/>
						<xsl:text/>
					</xsl:if>
					<xsl:if test="count(./nc:PersonGivenName) > 0">
						<!-- if First Name -->
						<xsl:value-of select="./nc:PersonGivenName"/>
						<xsl:text> </xsl:text>
						<xsl:text/>
					</xsl:if>
					<xsl:if test="count(./nc:PersonMiddleName) > 0">
						<!-- if Middle Name -->
						<xsl:value-of select="./nc:PersonMiddleName"/>
						<xsl:text> </xsl:text>
						<xsl:text/>
					</xsl:if>
					<xsl:if test="count(./nc:PersonNameSuffixText) > 0">
						<!-- if Suffix -->
						<xsl:value-of select="./nc:PersonNameSuffixText"/>
						<xsl:text/>
					</xsl:if>
					<!--<xsl:text>&#10;</xsl:text>-->
					<!-- if Not Primary name -->
					<xsl:text> (AKA)</xsl:text>
					<xsl:text>&#10;</xsl:text>
				</xsl:for-each>
				<!-- -->
				<xsl:text>&#10;Subject Description&#10;</xsl:text>
				<!-- FBI Number/State ID Number/DOC Number -->
				<xsl:choose>
					<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/j:PersonAugmentation/j:PersonFBIIdentification/nc:IdentificationID) &gt; 0">
						<xsl:choose>
							<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/j:PersonAugmentation/j:PersonStateFingerprintIdentification/nc:IdentificationID) &gt; 0">
								<xsl:choose>
									<xsl:when test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonOtherIdentification/rap:IdentificationCategoryText = 'Correctional ID'">
										<xsl:text>&#10;FBI Number              State Id Number         DOC Number&#10;</xsl:text>
									</xsl:when>
									<xsl:when test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonCorrectionsIdentification/nc:IdentificationID">
										<xsl:text>&#10;FBI Number              State Id Number         DOC Number&#10;</xsl:text>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>&#10;FBI Number              State Id Number                   &#10;</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<xsl:otherwise>
								<xsl:choose>
									<xsl:when test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonOtherIdentification/rap:IdentificationCategoryText = 'Correctional ID'">
										<xsl:text>&#10;FBI Number                                      DOC Number&#10;</xsl:text>
									</xsl:when>
									<xsl:when test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonCorrectionsIdentification/nc:IdentificationID">
										<xsl:text>&#10;FBI Number                                      DOC Number&#10;</xsl:text>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>&#10;FBI Number                                                &#10;</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/j:PersonAugmentation/j:PersonStateFingerprintIdentification/nc:IdentificationID) &gt; 0">
								<xsl:choose>
									<xsl:when test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonOtherIdentification/rap:IdentificationCategoryText = 'Correctional ID'">
										<xsl:text>&#10;                        State Id Number         DOC Number&#10;</xsl:text>
									</xsl:when>
									<xsl:when test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonCorrectionsIdentification/nc:IdentificationID">
										<xsl:text>&#10;                        State Id Number         DOC Number&#10;</xsl:text>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>&#10;                        State Id Number                   &#10;</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<xsl:otherwise>
								<xsl:choose>
									<xsl:when test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonOtherIdentification/rap:IdentificationCategoryText = 'Correctional ID'">
										<xsl:text>&#10;                                                DOC Number&#10;</xsl:text>
									</xsl:when>
									<xsl:when test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonCorrectionsIdentification/nc:IdentificationID">
										<xsl:text>&#10;                                                DOC Number&#10;</xsl:text>
									</xsl:when>
									<xsl:otherwise/>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
				<!-- -->
				<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/j:PersonAugmentation/j:PersonFBIIdentification/nc:IdentificationID">
					<xsl:variable name="i" select="position()"/>
					<xsl:value-of select="."/>
					<xsl:choose>
						<!-- if there is a [column 2] at this index write it, else spaceover -->
						<xsl:when test="count(../../j:PersonStateFingerprintIdentification[$i]/nc:IdentificationID) &gt; 0">
							<xsl:call-template name="spaceover">
								<xsl:with-param name="amount" select="$posCol2 - (string-length(.))"/>
								<xsl:with-param name="amountWrote" select="0"/>
							</xsl:call-template>
							<xsl:value-of select="../../j:PersonStateFingerprintIdentification[$i]/nc:IdentificationID"/>
							<xsl:if test="../../j:PersonStateFingerprintIdentification[$i]/nc:IdentificationJurisdictionText">
								<xsl:text> (</xsl:text>
								<xsl:value-of select="../../j:PersonStateFingerprintIdentification[$i]/nc:IdentificationJurisdictionText"/>
								<xsl:text>)</xsl:text>
							</xsl:if>
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="spaceover">
								<xsl:with-param name="amount" select="$posCol2 - string-length(.)"/>
								<xsl:with-param name="amountWrote" select="0"/>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:choose>
						<!-- if there is a [column 3] at this index write it, else return -->
						<xsl:when test="count(../../../nc:PersonOtherIdentification[$i]/nc:IdentificationID) &gt; 0">
							<xsl:if test="../../../nc:PersonOtherIdentification[$i]/rap:IdentificationCategoryText = 'Correctional ID'">
								<!-- only do if Correctional ID -->
								<xsl:variable name="adj" select="$posCol3 - ($posCol2 + (string-length(../../j:PersonStateFingerprintIdentification/nc:IdentificationID) + 5))"/>
								<xsl:call-template name="wrapIn">
									<xsl:with-param name="Text" select="../../../nc:PersonOtherIdentification[$i]/nc:IdentificationID"/>
									<xsl:with-param name="StartPos" select="$adj"/>
									<xsl:with-param name="EndPos" select="$posMaxWidth"/>
									<xsl:with-param name="WrapedStartPos" select="$posCol3"/>
									<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
									<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
									<xsl:with-param name="initialRun" select="0"/>
								</xsl:call-template>
							</xsl:if>
						</xsl:when>
						<xsl:when test="count(../../../rap:PersonCorrectionsIdentification/nc:IdentificationID) &gt; 0">
							<!-- only do if Correctional ID -->
							<xsl:variable name="adj" select="$posCol3 - ($posCol2 + (string-length(../../j:PersonStateFingerprintIdentification/nc:IdentificationID) + 5))"/>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="../../../rap:PersonCorrectionsIdentification[$i]/nc:IdentificationID"/>
								<xsl:with-param name="StartPos" select="$adj"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol3"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise/>
					</xsl:choose>
					<xsl:if test="count(.) &lt;= count(../../j:PersonStateIdentification/nc:IdentificationID) or count(.) &lt;= count(../../../nc:PersonOtherIdentification/nc:IdentificationID) or count(.) &lt;= count(../../../rap:PersonCorrectionsIdentification/nc:IdentificationID)">
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
				</xsl:for-each>
				<!-- Check to see if there are more [column 2] or [column 3] nodes than [column 1] and write them if so -->
				<xsl:if test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/j:PersonAugmentation/j:PersonStateFingerprintIdentification/nc:IdentificationID) &gt; count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/j:PersonAugmentation/j:PersonFBIIdentification/nc:IdentificationID)">
					<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/j:PersonAugmentation/j:PersonStateFingerprintIdentification/nc:IdentificationID">
						<xsl:variable name="i" select="position()"/>
						<xsl:if test="$i &gt; count(../../j:PersonFBIIdentification/nc:IdentificationID)">
							<xsl:call-template name="spaceover">
								<xsl:with-param name="amount" select="$posCol2"/>
								<xsl:with-param name="amountWrote" select="0"/>
							</xsl:call-template>
							<xsl:value-of select="."/>
							<xsl:if test="../../j:PersonStateFingerprintIdentification[$i]/nc:IdentificationJurisdictionText">
								<xsl:text> (</xsl:text>
								<xsl:value-of select="../../j:PersonStateFingerprintIdentification[$i]/nc:IdentificationJurisdictionText"/>
								<xsl:text>)</xsl:text>
							</xsl:if>
							<xsl:if test="count(../../../nc:PersonOtherIdentification[$i]/nc:IdentificationID) &gt; 0 or count(../../../rap:PersonCorrectionsIdentification[$i]/nc:IdentificationID) &gt; 0">
								<!-- check to see if there is a [column 3] at the current index and write it if so -->
								<xsl:call-template name="spaceover">
									<xsl:with-param name="amount" select="$posCol3 - ($posCol2 + string-length(.) + 5)"/>
									<!-- the +13 is to account refDate -->
									<xsl:with-param name="amountWrote" select="0"/>
								</xsl:call-template>
								<xsl:if test="../../../nc:PersonOtherIdentification[$i]/rap:IdentificationCategoryText = 'Correctional ID'">
									<xsl:value-of select="../../../nc:PersonOtherIdentification[$i]/nc:IdentificationID"/>
								</xsl:if>
								<xsl:if test="../../../rap:PersonCorrectionsIdentification[$i]">
									<xsl:value-of select="../../../rap:PersonCorrectionsIdentification[$i]/nc:IdentificationID"/>
								</xsl:if>
							</xsl:if>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
					</xsl:for-each>
				</xsl:if>
				<!-- Check to see if there are stand alone [column 3]'s and write them if so -->
				<xsl:if test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonOtherIdentification/nc:IDTypeText) &gt; count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/j:PersonAugmentation/j:PersonStateFingerprintIdentification/nc:IdentificationID) or count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonCorrectionsIdentification) &gt; count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/j:PersonAugmentation/j:PersonStateFingerprintIdentification/nc:IdentificationID)">
					<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonOtherIdentification/nc:IdentificationID">
						<xsl:variable name="i" select="position()"/>
						<xsl:if test="$i &gt; count(../../j:PersonAugmentation/j:PersonStateFingerprintIdentification) and $i &gt; count(../../j:PersonAugmentation/j:PersonFBIIdentification/nc:IdentificationID)">
							<xsl:call-template name="spaceover">
								<xsl:with-param name="amount" select="$posCol3"/>
								<xsl:with-param name="amountWrote" select="0"/>
							</xsl:call-template>
							<xsl:if test="rap:IdentificationCategoryText = 'Correctional ID'">
								<xsl:value-of select="nc:IdentificationID"/>
							</xsl:if>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
					</xsl:for-each>
					<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonCorrectionsIdentification/nc:IdentificationID">
						<xsl:variable name="i" select="position()"/>
						<xsl:if test="$i &gt; count(../../j:PersonAugmentation/j:PersonStateFingerprintIdentification) and $i &gt; count(../../j:PersonAugmentation/j:PersonFBIIdentification/nc:IdentificationID)">
							<xsl:call-template name="spaceover">
								<xsl:with-param name="amount" select="$posCol3"/>
								<xsl:with-param name="amountWrote" select="0"/>
							</xsl:call-template>
							<xsl:value-of select="."/>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
					</xsl:for-each>
				</xsl:if>
				<xsl:text>&#10;</xsl:text>
				<!-- SSN/Driver's License Numbers -->
				<xsl:choose>
					<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonSSNIdentification/nc:IdentificationID) &gt; 0">
						<xsl:choose>
							<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/j:PersonAugmentation/nc:DriverLicense/nc:DriverLicenseIdentification/nc:IdentificationID) &gt; 0">
								<xsl:text>Social Security Number  Driver's License Number&#10;</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>Social Security Number                         &#10;</xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/j:PersonAugmentation/nc:DriverLicense/nc:DriverLicenseIdentification/nc:IdentificationID) &gt; 0">
								<xsl:text>                        Driver's License Number&#10;</xsl:text>
							</xsl:when>
							<xsl:otherwise/>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
				<!-- -->
				<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonSSNIdentification/nc:IdentificationID">
					<xsl:variable name="curPos" select="position()"/>
					<xsl:value-of select="."/>
					<xsl:choose>
						<xsl:when test="count(../../j:PersonAugmentation/nc:DriverLicense/nc:DriverLicenseIdentification/nc:IdentificationID) &gt; 0">
							<xsl:call-template name="spaceover">
								<xsl:with-param name="amount" select="$posCol2 - string-length(.)"/>
								<xsl:with-param name="amountWrote" select="0"/>
							</xsl:call-template>
							<xsl:choose>
								<xsl:when test="count(../../j:PersonAugmentation/nc:DriverLicense[position() = $curPos]/nc:DriverLicenseIdentification) &gt; 0">
									<!-- if there is a dlNumber at the same index write it. -->
									<xsl:value-of select="../../j:PersonAugmentation/nc:DriverLicense[$curPos]/nc:DriverLicenseIdentification/nc:IdentificationID"/>
									<xsl:text> (</xsl:text>
									<xsl:value-of select="../../j:PersonAugmentation/nc:DriverLicense[$curPos]/nc:DriverLicenseIdentification/nc:IdentificationIDIssuingAuthorityText"/>
									<xsl:value-of select="../../j:PersonAugmentation/nc:DriverLicense[$curPos]/nc:DriverLicenseIdentification/nc:IdentificationJurisdictionText"/>
									<xsl:text>)&#10;</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text>&#10;</xsl:text>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>&#10;</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
				<xsl:if test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonSSNIdentification/nc:IdentificationID) &gt; 0">
					<xsl:text>&#10;</xsl:text>
				</xsl:if>
				<xsl:if test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/j:PersonAugmentation/nc:DriverLicense/nc:DriverLicenseIdentification/nc:IdentificationID) &gt; count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonSSNIdentification/nc:IdentificationID)">
					<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/j:PersonAugmentation/nc:DriverLicense/nc:DriverLicenseIdentification">
						<xsl:if test="position() &gt; count(../../../../nc:PersonSSNIdentification/nc:IdentificationID)">
							<xsl:call-template name="spaceover">
								<xsl:with-param name="amount" select="$posCol2"/>
								<xsl:with-param name="amountWrote" select="0"/>
							</xsl:call-template>
							<xsl:value-of select="nc:IdentificationID"/>
							<xsl:text> (</xsl:text>
							<xsl:value-of select="nc:IDIssuingAuthorityText"/>
							<xsl:text>)</xsl:text>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
					</xsl:for-each>
				</xsl:if>
				<!-- Miscellaneous Numbers -->
				<xsl:if test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonOtherIdentification) &gt; count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonOtherIdentification[rap:IdentificationCategoryText='Correctional ID']) ">
					<xsl:text>Miscellaneous Numbers&#10;</xsl:text>
				</xsl:if>
				<!-- -->
				<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonOtherIdentification">
					<xsl:choose>
						<xsl:when test="rap:IdentificationCategoryText">
							<xsl:if test="rap:IdentificationCategoryText != 'Correctional ID'">
								<xsl:value-of select="nc:IdentificationID"/>
								<xsl:call-template name="spaceover">
									<xsl:with-param name="amount" select="$posCol2 - (string-length(nc:IdentificationID) + 1)"/>
									<xsl:with-param name="amountWrote" select="0"/>
								</xsl:call-template>
								<xsl:value-of select="rap:IdentificationCategoryText"/>
								<xsl:call-template name="spaceover">
									<xsl:with-param name="amount" select="$posCol3 - ($posCol2 + string-length(rap:IdentificationCategoryText))"/>
									<xsl:with-param name="amountWrote" select="0"/>
								</xsl:call-template>
								<xsl:value-of select="nc:IdentificationJurisdictionText"/>
								<xsl:value-of select="nc:IdentificationJurisdictionText"/>
								<xsl:text>&#10;</xsl:text>
							</xsl:if>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="nc:IdentificationID"/>
							<xsl:call-template name="spaceover">
								<xsl:with-param name="amount" select="$posCol2 - (string-length(nc:IdentificationID) + 1)"/>
								<xsl:with-param name="amountWrote" select="0"/>
							</xsl:call-template>
							<xsl:value-of select="rap:IdentificationCategoryText"/>
							<xsl:call-template name="spaceover">
								<xsl:with-param name="amount" select="$posCol3 - ($posCol2 + string-length(rap:IdentificationCategoryText))"/>
								<xsl:with-param name="amountWrote" select="0"/>
							</xsl:call-template>
							<xsl:value-of select="nc:IdentificationJurisdictionText"/>
							<xsl:value-of select="nc:IdentificationJurisdictionText"/>
							<xsl:text>&#10;</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
				<!-- Sex/Race/Skin Tone -->
				<xsl:choose>
					<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonSexText) &gt; 0">
						<xsl:choose>
							<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonRaceText) &gt; 0">
								<xsl:choose>
									<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonSkinToneText) &gt; 0">
										<xsl:text>&#10;Sex                     Race                    Skin Tone&#10;</xsl:text>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>&#10;Sex                     Race                             &#10;</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<xsl:otherwise>
								<xsl:choose>
									<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonSkinToneText) &gt; 0">
										<xsl:text>&#10;Sex                                             Skin Tone&#10;</xsl:text>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>&#10;Sex                                                      &#10;</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonRaceText) &gt; 0">
								<xsl:choose>
									<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonSkinToneText) &gt; 0">
										<xsl:text>&#10;                        Race                    Skin Tone&#10;</xsl:text>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>&#10;                        Race                             &#10;</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<xsl:otherwise>
								<xsl:choose>
									<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonSkinToneText) &gt; 0">
										<xsl:text>&#10;                                                Skin Tone&#10;</xsl:text>
									</xsl:when>
									<xsl:otherwise/>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
				<!-- -->
				<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonSexText">
					<xsl:variable name="i" select="position()"/>
					<xsl:value-of select="."/>
					<xsl:choose>
						<!-- if there is a race at this index write it, else spaceover -->
						<xsl:when test="count(../rap:PersonRaceText[$i]) &gt; 0">
							<xsl:call-template name="spaceover">
								<xsl:with-param name="amount" select="$posCol2 - string-length(.)"/>
								<xsl:with-param name="amountWrote" select="0"/>
							</xsl:call-template>
							<xsl:value-of select="../rap:PersonRaceText[$i]"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="spaceover">
								<xsl:with-param name="amount" select="$posCol2 - string-length(.)"/>
								<xsl:with-param name="amountWrote" select="0"/>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:choose>
						<!-- if there is a skin tone at this index write it, else returnr -->
						<xsl:when test="count(../rap:PersonSkinToneText[$i]) &gt; 0">
							<xsl:call-template name="spaceover">
								<xsl:with-param name="amount" select="$posCol3 - ($posCol2 + string-length(../rap:PersonRaceText[$i]))"/>
								<xsl:with-param name="amountWrote" select="0"/>
							</xsl:call-template>
							<xsl:value-of select="../rap:PersonSkinToneText[$i]"/>
						</xsl:when>
						<xsl:otherwise>
							<!-- xsl:text>&#10;</xsl:text -->
						</xsl:otherwise>
					</xsl:choose>
					<xsl:if test="count(.) &lt;= count(../rap:PersonRaceText) or count(.) &lt;= count(../rap:PersonSkinToneText)">
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
				</xsl:for-each>
				<!-- Check to see if there are more race or skinTone nodes than sex and write them if so -->
				<xsl:if test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonRaceText) &gt; count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonSexText)">
					<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonRaceText">
						<xsl:variable name="i" select="position()"/>
						<xsl:if test="$i &gt; count(../rap:PersonSexText)">
							<xsl:call-template name="spaceover">
								<xsl:with-param name="amount" select="$posCol2"/>
								<xsl:with-param name="amountWrote" select="0"/>
							</xsl:call-template>
							<xsl:value-of select="."/>
							<xsl:if test="count(../rap:PersonSkinToneText[$i]) &gt; 0">
								<!-- check to see if there is skinTone at the current index and write it if so -->
								<xsl:call-template name="spaceover">
									<xsl:with-param name="amount" select="$posCol3 - ($posCol2 + string-length(.))"/>
									<xsl:with-param name="amountWrote" select="0"/>
								</xsl:call-template>
								<xsl:value-of select="../rap:PersonSkinToneText[$i]"/>
							</xsl:if>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<!-- xsl:text>&#10;x</xsl:text -->
						<!-- moved up in the if -->
					</xsl:for-each>
				</xsl:if>
				<!-- Check to see if there are more skinTones (stand alone) than race and write them if so -->
				<xsl:if test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonSkinToneText) &gt; count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonRaceText)">
					<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonSkinToneText">
						<xsl:variable name="i" select="position()"/>
						<xsl:if test="$i &gt; count(../rap:PersonRaceText) and $i &gt; count(../rap:PersonSexText)">
							<xsl:call-template name="spaceover">
								<xsl:with-param name="amount" select="$posCol3"/>
								<xsl:with-param name="amountWrote" select="0"/>
							</xsl:call-template>
							<xsl:value-of select="."/>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
					</xsl:for-each>
				</xsl:if>
				<xsl:choose>
					<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonSexText) &gt; 0">
						<xsl:choose>
							<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonRaceText) &gt; 0">
								<xsl:choose>
									<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonSkinToneText) &gt; 0"/>
									<xsl:otherwise/>
								</xsl:choose>
							</xsl:when>
							<xsl:otherwise>
								<xsl:choose>
									<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonSkinToneText) &gt; 0"/>
									<xsl:otherwise>
										<xsl:text>&#10;</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonRaceText) &gt; 0">
								<xsl:choose>
									<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonSkinToneText) &gt; 0"/>
									<xsl:otherwise/>
								</xsl:choose>
							</xsl:when>
							<xsl:otherwise>
								<xsl:choose>
									<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonSkinToneText) &gt; 0"/>
									<xsl:otherwise/>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
				<!-- -->
				<!-- Height/Weight/DOB -->
				<xsl:choose>
					<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonHeightMeasure/nc:MeasurePointValue) &gt; 0">
						<xsl:choose>
							<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonWeightMeasure/nc:MeasurePointValue) &gt; 0">
								<xsl:choose>
									<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonBirthDate) &gt; 0">
										<xsl:text>&#10;Height                  Weight                  Date of Birth&#10;</xsl:text>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>&#10;Height                  Weight                               &#10;</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<xsl:otherwise>
								<xsl:choose>
									<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonBirthDate/nc:Date) &gt; 0">
										<xsl:text>&#10;Height                                          Date of Birth&#10;</xsl:text>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>&#10;Height                                                       &#10;</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonWeightMeasure/nc:MeasurePointValue) &gt; 0">
								<xsl:choose>
									<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonBirthDate/nc:Date) &gt; 0">
										<xsl:text>&#10;                        Weight                  Date of Birth&#10;</xsl:text>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>&#10;                        Weight                               &#10;</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<xsl:otherwise>
								<xsl:choose>
									<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonBirthDate/nc:Date) &gt; 0">
										<xsl:text>&#10;                                                Date of Birth&#10;</xsl:text>
									</xsl:when>
									<xsl:otherwise/>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
				<!-- -->
				<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonHeightMeasure/nc:MeasurePointValue">
					<xsl:variable name="i" select="position()"/>
					<xsl:variable name="metadataid">
						<xsl:value-of select="../@s:metadata"/>
					</xsl:variable>
					<xsl:variable name="HeightData">
						<xsl:value-of select="."/>
						<xsl:value-of select="../nc:LengthUnitCode"/>
						<xsl:if test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportedDate) > 0">
							<xsl:text> (</xsl:text>
							<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportedDate"/>
							<xsl:text>)</xsl:text>
						</xsl:if>
					</xsl:variable>
					<xsl:value-of select="$HeightData"/>
					<xsl:choose>
						<!-- if there is a [column 2] at this index write it, else spaceover -->
						<xsl:when test="count(../../nc:PersonWeightMeasure[$i]) &gt; 0">
							<xsl:variable name="metadataid2">
								<xsl:value-of select="../../nc:PersonWeightMeasure[$i]/@s:metadata"/>
							</xsl:variable>
							<xsl:call-template name="spaceover">
								<xsl:with-param name="amount" select="$posCol2 - (string-length($HeightData))"/>
								<xsl:with-param name="amountWrote" select="0"/>
							</xsl:call-template>
							<xsl:variable name="WeightData">
								<xsl:value-of select="../../nc:PersonWeightMeasure[$i]/nc:MeasurePointValue"/>
								<xsl:value-of select="../../nc:PersonWeightMeasure[$i]/nc:WeightUnitCode"/>
								<xsl:text/>
								<xsl:if test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid2]/nc:ReportedDate">
									<xsl:text> (</xsl:text>
									<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid2]/nc:ReportedDate"/>
									<xsl:text>)</xsl:text>
								</xsl:if>
							</xsl:variable>
							<xsl:value-of select="$WeightData"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="spaceover">
								<xsl:with-param name="amount" select="$posCol2 - string-length($HeightData)"/>
								<xsl:with-param name="amountWrote" select="0"/>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:choose>
						<!-- if there is a [column 3] at this index write it, else return -->
						<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonBirthDate[$i]) &gt; 0">
							<xsl:variable name="metadataid2">
								<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheetPerson/nc:PersonWeightMeasure[$i]/@s:metadata"/>
							</xsl:variable>
							<xsl:choose>
								<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid2]/nc:ReportedDate) &gt; 0">
									<xsl:call-template name="spaceover">
										<xsl:with-param name="amount" select="$posCol3 - ($posCol2 + string-length(//ch-doc:CriminalHistory/ch-ext:RapSheetPerson/nc:PersonWeightMeasure[$i]/nc:MeasurePointValue)+13)"/>
										<!-- the +20 is to account for pounds and refDate -->
										<xsl:with-param name="amountWrote" select="0"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="spaceover">
										<xsl:with-param name="amount" select="$posCol3 - ($posCol2 + string-length(//ch-doc:CriminalHistory/ch-ext:RapSheetPerson/nc:PersonWeightMeasure[$i]/nc:MeasurePointValue))"/>
										<xsl:with-param name="amountWrote" select="0"/>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonBirthDate[$i]/nc:Date"/>
						</xsl:when>
						<xsl:otherwise/>
					</xsl:choose>
					<xsl:if test="count(.) &lt;= count(../nc:PersonWeightMeasure/nc:MeasurePointValue) or count(.) &lt;= count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonBirthDate/nc:Date)">
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
				</xsl:for-each>
				<!-- Check to see if there are more [column 2] or [column 3] nodes than [column 1] and write them if so -->
				<xsl:if test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonWeightMeasure) &gt; count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonHeightMeasure)">
					<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonWeightMeasure">
						<xsl:variable name="metadataid2">
							<xsl:value-of select="@s:metadata"/>
						</xsl:variable>
						<xsl:variable name="i" select="position()"/>
						<xsl:if test="$i &gt; count(../nc:PersonHeightMeasure)">
							<xsl:call-template name="spaceover">
								<xsl:with-param name="amount" select="$posCol2"/>
								<xsl:with-param name="amountWrote" select="0"/>
							</xsl:call-template>
							<xsl:value-of select="."/>
							<xsl:text/>
							<xsl:if test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid2]/nc:ReportedDate">
								<xsl:text> (</xsl:text>
								<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid2]/nc:ReportedDate"/>
								<xsl:text>)</xsl:text>
							</xsl:if>
							<xsl:if test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonBirthDate[$i]/nc:Date) &gt; 0">
								<!-- check to see if there is a [column 3] at the current index and write it if so -->
								<xsl:choose>
									<xsl:when test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid2]/nc:ReportedDate">
										<xsl:call-template name="spaceover">
											<xsl:with-param name="amount" select="$posCol3 - ($posCol2 + string-length(.) + 13)"/>
											<!-- the +20 is to account for pounds and refDate -->
											<xsl:with-param name="amountWrote" select="0"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:call-template name="spaceover">
											<xsl:with-param name="amount" select="$posCol3 - ($posCol2 + string-length(.) )"/>
											<xsl:with-param name="amountWrote" select="0"/>
										</xsl:call-template>
									</xsl:otherwise>
								</xsl:choose>
								<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonBirthDate[$i]/nc:Date"/>
							</xsl:if>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
					</xsl:for-each>
				</xsl:if>
				<!-- Check to see if there are stand alone [column 3]'s and write them if so -->
				<xsl:if test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonBirthDate/nc:Date) &gt; count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonWeightMeasure/nc:MeasurePointValue)">
					<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonBirthDate/nc:Date">
						<xsl:variable name="i" select="position()"/>
						<xsl:if test="$i &gt; count(../nc:PersonWeightMeasure)+1 and $i &gt; count(../nc:PersonHeightMeasure)+1">
							<xsl:call-template name="spaceover">
								<xsl:with-param name="amount" select="$posCol3"/>
								<xsl:with-param name="amountWrote" select="0"/>
							</xsl:call-template>
							<xsl:value-of select="."/>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
					</xsl:for-each>
				</xsl:if>
				<xsl:choose>
					<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonHeightMeasure) &gt; 0">
						<xsl:choose>
							<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonWeightMeasure) &gt; 0">
								<xsl:choose>
									<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonBirthDate) &gt; 0"/>
									<xsl:otherwise/>
								</xsl:choose>
							</xsl:when>
							<xsl:otherwise>
								<xsl:choose>
									<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonBirthDate) &gt; 0"/>
									<xsl:otherwise>
										<xsl:text>&#10;</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:otherwise/>
				</xsl:choose>
				<!-- -->
				<!-- Hair Color/Eye Color/Fingerprint Patern -->
				<xsl:choose>
					<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonHairColorText) &gt; 0">
						<xsl:choose>
							<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonEyeColorText) &gt; 0">
								<xsl:choose>
									<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:PersonBiometricsAssociation/rap:PersonBiometrics/rap:PersonFingerprintSet/nc:BiometricValueText) &gt; 0">
										<xsl:text>&#10;Hair Color              Eye Color               Fingerprint Pattern&#10;</xsl:text>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>&#10;Hair Color              Eye Color                                  &#10;</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<xsl:otherwise>
								<xsl:choose>
									<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:PersonBiometricsAssociation/rap:PersonBiometrics/rap:PersonFingerprintSet/nc:BiometricValueText) &gt; 0">
										<xsl:text>&#10;Hair Color                                      Fingerprint Pattern&#10;</xsl:text>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>&#10;Hair Color                                                         &#10;</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonEyeColorText) &gt; 0">
								<xsl:choose>
									<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:PersonBiometricsAssociation/rap:PersonBiometrics/rap:PersonFingerprintSet/nc:BiometricValueText) &gt; 0">
										<xsl:text>&#10;                        Eye Color               Fingerprint Pattern&#10;</xsl:text>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>&#10;                        Eye Color                                  &#10;</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<xsl:otherwise>
								<xsl:choose>
									<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:PersonBiometricsAssociation/rap:PersonBiometrics/rap:PersonFingerprintSet/nc:BiometricValueText) &gt; 0">
										<xsl:text>&#10;                                                Fingerprint Pattern&#10;</xsl:text>
									</xsl:when>
									<xsl:otherwise/>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
				<!-- -->
				<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonHairColorText">
					<xsl:variable name="i" select="position()"/>
					<xsl:value-of select="."/>
					<xsl:variable name="metadataid">
						<xsl:value-of select="./@s:metadata"/>
					</xsl:variable>
					<xsl:if test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportedDate">
						<xsl:text> (</xsl:text>
						<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportedDate"/>
						<xsl:text>)</xsl:text>
					</xsl:if>
					<xsl:choose>
						<!-- if there is a [column 2] at this index write it, else spaceover -->
						<xsl:when test="count(../rap:PersonEyeColorText[$i]) &gt; 0">
							<xsl:variable name="metadataid2">
								<xsl:value-of select="../rap:PersonEyeColorText[$i]/@s:metadata"/>
							</xsl:variable>
							<xsl:choose>
								<xsl:when test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid2]/nc:ReportedDate">
									<xsl:call-template name="spaceover">
										<xsl:with-param name="amount" select="$posCol2 - (string-length(.) + 13)"/>
										<xsl:with-param name="amountWrote" select="0"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="spaceover">
										<xsl:with-param name="amount" select="$posCol2 - (string-length(.))"/>
										<xsl:with-param name="amountWrote" select="0"/>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:value-of select="../rap:PersonEyeColorText[$i]"/>
							<xsl:if test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid2]/nc:ReportedDate">
								<xsl:text> (</xsl:text>
								<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid2]/nc:ReportedDate"/>
								<xsl:text>)</xsl:text>
							</xsl:if>
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="spaceover">
								<xsl:with-param name="amount" select="$posCol2 - string-length(.)"/>
								<xsl:with-param name="amountWrote" select="0"/>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:choose>
						<!-- if there is a [column 3] at this index write it, else return -->
						<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:PersonBiometricsAssociation/rap:PersonBiometrics/rap:PersonFingerprintSet/nc:BiometricValueText[$i]) > 0">
							<xsl:variable name="metadataid3">
								<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:PersonBiometricsAssociation/rap:PersonBiometrics/rap:PersonFingerprintSet/nc:BiometricValueText[$i]/@s:metadata"/>
							</xsl:variable>
							<xsl:choose>
								<xsl:when test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid3]/nc:ReportedDate">
									<xsl:call-template name="spaceover">
										<xsl:with-param name="amount" select="$posCol3 - ($posCol2 + string-length(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonEyeColorText[$i]) + 13)"/>
										<!-- the +13 is to account for refDate -->
										<xsl:with-param name="amountWrote" select="0"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="spaceover">
										<xsl:with-param name="amount" select="$posCol3 - ($posCol2 + string-length(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonEyeColorText[$i]))"/>
										<xsl:with-param name="amountWrote" select="0"/>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:PersonBiometricsAssociation/rap:PersonBiometrics/rap:PersonFingerprintSet/nc:BiometricValueText[$i]"/>
							<xsl:if test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:PersonBiometricsAssociation/rap:PersonBiometrics/rap:PersonFingerprintSet/nc:BiometricEncodingMethodText[$i]">
								<xsl:value-of select="concat(' (',//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:PersonBiometricsAssociation/rap:PersonBiometrics/rap:PersonFingerprintSet/nc:BiometricEncodingMethodText[$i],')')"/>
							</xsl:if>
						</xsl:when>
						<xsl:otherwise>
							<!-- xsl:text>&#10;</xsl:text -->
						</xsl:otherwise>
					</xsl:choose>
					<xsl:if test="count(.) &lt;= count(rap:RapSheet/rap:RapSheetPerson/rap:PersonEyeColorText) or count(.) &lt;= count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:PersonBiometricsAssociation/rap:PersonBiometrics/rap:PersonFingerprintSet/rap:BiometricEncodingMethodText)">
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
					<xsl:text>&#10;</xsl:text>
				</xsl:for-each>
				<!-- Check to see if there are more [column 2] or [column 3] nodes than [column 1] and write them if so -->
				<xsl:if test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonEyeColorText) &gt; count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonHairColorText)">
					<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonEyeColorText">
						<xsl:variable name="metadataid">
							<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonEyeColorText/@s:metadata"/>
						</xsl:variable>
						<xsl:variable name="i" select="position()"/>
						<xsl:if test="$i &gt; count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonHairColorText)">
							<xsl:call-template name="spaceover">
								<xsl:with-param name="amount" select="$posCol2"/>
								<xsl:with-param name="amountWrote" select="0"/>
							</xsl:call-template>
							<xsl:value-of select="."/>
							<xsl:text> (</xsl:text>
							<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportedDate"/>
							<xsl:text>)</xsl:text>
							<xsl:if test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:PersonBiometricsAssociation/rap:PersonBiometrics/rap:PersonFingerPrintSet[$i]) &gt; 0">
								<!-- check to see if there is a [column 3] at the current index and write it if so -->
								<xsl:choose>
									<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportedDate) > 0">
										<xsl:call-template name="spaceover">
											<xsl:with-param name="amount" select="$posCol3 - ($posCol2 + string-length(../rap:PersonEyeColorText[$i]) + 13)"/>
											<!-- the +13 is to account for refDate -->
											<xsl:with-param name="amountWrote" select="0"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:call-template name="spaceover">
											<xsl:with-param name="amount" select="$posCol3 - ($posCol2 + string-length(../rap:PersonEyeColorText[$i]) + 13)"/>
											<!-- the +20 is to account for pounds and refDate -->
											<xsl:with-param name="amountWrote" select="0"/>
										</xsl:call-template>
									</xsl:otherwise>
								</xsl:choose>
								<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:PersonBiometricsAssociation/rap:PersonBiometrics/rap:PersonFingerPrintSet/nc:BiometricValueText[$i]"/>
								<xsl:if test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:PersonBiometricsAssociation/rap:PersonBiometrics/rap:PersonFingerPrintSet/nc:BiometricEncodingMethodText[$i]">
									<xsl:value-of select="concat(' (',//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:PersonBiometricsAssociation/rap:PersonBiometrics/rap:PersonFingerPrintSet/nc:BiometricEncodingMethodText[$i],')')"/>
								</xsl:if>
							</xsl:if>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
					</xsl:for-each>
				</xsl:if>
				<!-- Check to see if there are stand alone [column 3]'s and write them if so -->
				<xsl:if test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:PersonBiometricsAssociation/rap:PersonBiometrics/rap:PersonFingerPrintSet/nc:BiometricValueText) &gt; count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonEyeColorText)">
					<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:PersonBiometricsAssociation/rap:PersonBiometrics/rap:PersonFingerPrintSet/nc:BiometricValueText">
						<xsl:variable name="i" select="position()"/>
						<xsl:if test="$i &gt; count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonEyeColorText) and $i &gt; count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonHairColorText)">
							<xsl:variable name="metadataid">
								<xsl:value-of select="../rap:PersonEyeColorText/@s:metadata"/>
							</xsl:variable>
							<xsl:choose>
								<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportedDate) > 0">
									<xsl:call-template name="spaceover">
										<xsl:with-param name="amount" select="$posCol3 - ($posCol2 + string-length(../rap:PersonEyeColorText[$i]) + 13)"/>
										<!-- the +13 is to account for refDate -->
										<xsl:with-param name="amountWrote" select="0"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="spaceover">
										<xsl:with-param name="amount" select="$posCol3 - ($posCol2 + string-length(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonEyeColorText[$i]) + 13)"/>
										<!-- the +20 is to account for pounds and refDate -->
										<xsl:with-param name="amountWrote" select="0"/>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:PersonBiometricsAssociation/rap:PersonBiometrics/rap:PersonFingerPrintSet/nc:BiometricValueText[$i]"/>
							<xsl:if test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:PersonBiometricsAssociation/rap:PersonBiometrics/rap:PersonFingerPrintSet/nc:BiometricEncodingMethodText[$i]">
								<xsl:value-of select="concat(' (',//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:PersonBiometricsAssociation/rap:PersonBiometrics/rap:PersonFingerPrintSet/nc:BiometricEncodingMethodText[$i],')')"/>
							</xsl:if>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
					</xsl:for-each>
				</xsl:if>
				<!-- Scars Marks and Tattoos -->
				<xsl:if test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonPhysicalFeature) &gt; 0">
					<xsl:text>&#10;Scars, Marks, and Tattoos&#10;</xsl:text>
					<!-- -->
					<xsl:text>Code                    Description, Comments, and Images&#10;</xsl:text>
				</xsl:if>
				<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonPhysicalFeature">
					<xsl:variable name="metadataid">
						<xsl:value-of select="../@s:metadata"/>
					</xsl:variable>
					<!-- get SMT Code formatted properly and return into a variable -->
					<xsl:variable name="SMTCode">
						<xsl:call-template name="doSMTCode">
							<xsl:with-param name="codeSource" select="./nc:PhysicalFeatureCategoryCode"/>
						</xsl:call-template>
					</xsl:variable>
					<xsl:value-of select="$SMTCode"/>
					<!-- space over the correct amount to start the next column -->
					<xsl:call-template name="spaceover">
						<xsl:with-param name="amount" select="$posCol2 - (string-length($SMTCode))"/>
						<xsl:with-param name="amountWrote" select="0"/>
					</xsl:call-template>
					<!-- get SMT Description Info -->
					<xsl:choose>
						<xsl:when test="count(./nc:PhysicalFeatureDescriptionText) &gt; 0">
							<xsl:variable name="SMTDescription" select="concat(./nc:PhysicalFeatureCategoryText, ', ', ./nc:PhysicalFeatureDescriptionText, ' ',//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportedDate)"/>
							<!-- djr(19-mar-03) Write out the SMT Description Info now that it is all formatted. -->
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="$SMTDescription"/>
								<xsl:with-param name="StartPos" select="$posCol1"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:variable name="SMTDescription" select="concat(./nc:PhysicalFeatureCategoryText, ', ', ./nc:PhysicalFeatureDescriptionText, ' ', //ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportedDate)"/>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="$SMTDescription"/>
								<xsl:with-param name="StartPos" select="$posCol1"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
					<!--ADD-->
					<xsl:if test="./nc:PhysicalFeatureLocationText">
						<xsl:text>Physical Feature Location</xsl:text>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="./nc:PhysicalFeatureLocationText"/>
							<xsl:with-param name="StartPos" select="$posCol2 - (11)"/>
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
					<!--ADD-->
				</xsl:for-each>
				<!-- Blood Type/Medical Conditions -->
				<xsl:choose>
					<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonBloodTypeText) &gt; 0">
						<xsl:choose>
							<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonMedicalCondition/nc:MedicalConditionText) &gt; 0">
								<xsl:text>&#10;Blood Type              Medical Condition&#10;</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>&#10;Blood Type                               &#10;</xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonMedicalCondition/nc:MedicalConditionText) &gt; 0">
								<xsl:text>&#10;                        Medical Condition&#10;</xsl:text>
							</xsl:when>
							<xsl:otherwise/>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
				<!-- -->
				<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonBloodTypeText">
					<xsl:variable name="curPos" select="position()"/>
					<xsl:value-of select="."/>
					<xsl:choose>
						<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonMedicalCondition/nc:MedicalConditionText) &gt; 0">
							<xsl:call-template name="spaceover">
								<xsl:with-param name="amount" select="$posCol2 - string-length(.)"/>
								<xsl:with-param name="amountWrote" select="0"/>
							</xsl:call-template>
							<xsl:choose>
								<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonMedicalCondition[position() = $curPos]) &gt; 0">
									<!-- if there is a medicalCondition at the same index write it. -->
									<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonMedicalCondition/nc:MedicalConditionText[$curPos]"/>
									<xsl:variable name="metadataid">
										<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonMedicalCondition/nc:MedicalConditionText[$curPos]/@s:metadata"/>
									</xsl:variable>
									<xsl:if test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportedDate">
										<xsl:text> (</xsl:text>
										<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportedDate"/>
										<xsl:text>)</xsl:text>
									</xsl:if>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text>&#10;</xsl:text>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>&#10;</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
				<xsl:choose>
					<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonMedicalCondition) &gt; count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonBloodTypeText)">
						<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonMedicalCondition">
							<xsl:variable name="curPos" select="position()"/>
							<xsl:if test="position() &gt; count(../rap:PersonBloodTypeText)">
								<xsl:text>&#10;</xsl:text>
								<xsl:call-template name="spaceover">
									<xsl:with-param name="amount" select="$posCol2"/>
									<xsl:with-param name="amountWrote" select="0"/>
								</xsl:call-template>
								<xsl:value-of select="./nc:MedicalConditionText"/>
								<xsl:variable name="metadataid">
									<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonMedicalCondition/nc:MedicalConditionText[$curPos]/@s:metadata"/>
								</xsl:variable>
								<xsl:if test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportedDate">
									<xsl:text> (</xsl:text>
									<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportedDate"/>
									<xsl:text>)</xsl:text>
								</xsl:if>
								<xsl:text>&#10;</xsl:text>
							</xsl:if>
						</xsl:for-each>
						<xsl:text>&#10;</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>&#10;&#10;</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
				<!-- Place of Birth/Citizenship/Ethnicity -->
				<xsl:choose>
					<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonBirthLocation/nc:LocationName) &gt; 0">
						<xsl:choose>
							<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonCitizenshipText) &gt; 0">
								<xsl:choose>
									<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonEthnicityText) &gt; 0">
										<xsl:text>Place of Birth          Citizenship             Ethnicity&#10;</xsl:text>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>Place of Birth          Citizenship                      &#10;</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<xsl:otherwise>
								<xsl:choose>
									<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonEthnicityText) &gt; 0">
										<xsl:text>Place of Birth                                  Ethnicity&#10;</xsl:text>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>Place of Birth                                           &#10;</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonCitizenshipText) &gt; 0">
								<xsl:choose>
									<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonEthnicityText) &gt; 0">
										<xsl:text>                        Citizenship             Ethnicity&#10;</xsl:text>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>                        Citizenship                      &#10;</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<xsl:otherwise>
								<xsl:choose>
									<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonEthnicityText) &gt; 0">
										<xsl:text>&#10;                                                Ethnicity&#10;</xsl:text>
									</xsl:when>
									<xsl:otherwise/>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
				<!-- -->
				<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonBirthLocation/nc:LocationName">
					<xsl:variable name="i" select="position()"/>
					<xsl:value-of select="."/>
					<xsl:choose>
						<!-- if there is a [column 2] at this index write it, else spaceover -->
						<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonCitizenshipText[$i]) &gt; 0">
							<xsl:call-template name="spaceover">
								<xsl:with-param name="amount" select="$posCol2 - (string-length(.))"/>
								<xsl:with-param name="amountWrote" select="0"/>
							</xsl:call-template>
							<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonCitizenshipText[$i]"/>
							<xsl:variable name="metadataid">
								<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonCitizenshipText[$i]/@s:metadata"/>
							</xsl:variable>
							<xsl:if test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportedDate">
								<xsl:text> (</xsl:text>
								<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportedDate"/>
								<xsl:text>)</xsl:text>
							</xsl:if>
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="spaceover">
								<xsl:with-param name="amount" select="$posCol2 - string-length(.)"/>
								<xsl:with-param name="amountWrote" select="0"/>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:choose>
						<!-- if there is a [column 3] at this index write it, else return -->
						<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonEthnicityText[$i]) &gt; 0">
							<xsl:variable name="metadataid">
								<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonCitizenshipText[$i]/@s:metadata"/>
							</xsl:variable>
							<xsl:choose>
								<xsl:when test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportedDate">
									<xsl:call-template name="wrapIn">
										<xsl:with-param name="Text" select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonEthnicityText[$i]"/>
										<xsl:with-param name="StartPos" select="$posCol3 - ($posCol2 + (string-length(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonCitizenshipText[$i]) + 13))"/>
										<xsl:with-param name="EndPos" select="$posMaxWidth"/>
										<xsl:with-param name="WrapedStartPos" select="$posCol3"/>
										<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
										<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
										<xsl:with-param name="initialRun" select="0"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="wrapIn">
										<xsl:with-param name="Text" select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonEthnicityText[$i]"/>
										<xsl:with-param name="StartPos" select="$posCol3 - ($posCol2 + (string-length(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonCitizenshipText[$i])))"/>
										<xsl:with-param name="EndPos" select="$posMaxWidth"/>
										<xsl:with-param name="WrapedStartPos" select="$posCol3"/>
										<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
										<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
										<xsl:with-param name="initialRun" select="0"/>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:otherwise/>
					</xsl:choose>
					<xsl:if test="count(.) &lt;= count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonCitizenshipText) or count(.) &lt;= count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonEthnicityText)">
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
				</xsl:for-each>
				<!-- Check to see if there are more [column 2] or [column 3] nodes than [column 1] and write them if so -->
				<xsl:if test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonCitizenshipText) &gt; count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonBirthLocation/nc:LocationName)">
					<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonCitizenshipText">
						<xsl:variable name="metadataid">
							<xsl:value-of select="./@s:metadata"/>
						</xsl:variable>
						<xsl:variable name="i" select="position()"/>
						<xsl:if test="$i &gt; count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonBirthLocation/nc:LocationName)">
							<xsl:call-template name="spaceover">
								<xsl:with-param name="amount" select="$posCol2"/>
								<xsl:with-param name="amountWrote" select="0"/>
							</xsl:call-template>
							<xsl:value-of select="."/>
							<xsl:if test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportedDate">
								<xsl:text> (</xsl:text>
								<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportedDate"/>
								<xsl:text>)</xsl:text>
							</xsl:if>
							<xsl:if test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/j:PersonBirthDate[$i]) &gt; 0">
								<!-- check to see if there is a [column 3] at the current index and write it if so -->
								<xsl:choose>
									<xsl:when test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportedDate">
										<xsl:call-template name="spaceover">
											<xsl:with-param name="amount" select="$posCol3 - ($posCol2 + string-length(.) + 13)"/>
											<!-- the +13 is to account refDate -->
											<xsl:with-param name="amountWrote" select="0"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:call-template name="spaceover">
											<xsl:with-param name="amount" select="$posCol3 - ($posCol2 + string-length(.))"/>
											<!-- the +13 is to account refDate -->
											<xsl:with-param name="amountWrote" select="0"/>
										</xsl:call-template>
									</xsl:otherwise>
								</xsl:choose>
								<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonEthnicityText[$i]"/>
							</xsl:if>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
					</xsl:for-each>
				</xsl:if>
				<!-- Check to see if there are stand alone [column 3]'s and write them if so -->
				<xsl:if test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonEthnicityText) &gt; count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonCitizenshipText)">
					<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:PersonEthnicityText">
						<xsl:variable name="i" select="position()"/>
						<xsl:if test="$i &gt; count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonCitizenshipText) and $i &gt; count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonBirthLocation/nc:LocationName)">
							<xsl:call-template name="spaceover">
								<xsl:with-param name="amount" select="$posCol3"/>
								<xsl:with-param name="amountWrote" select="0"/>
							</xsl:call-template>
							<xsl:value-of select="."/>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
					</xsl:for-each>
				</xsl:if>
				<!-- Marital Status/Religion -->
				<xsl:choose>
					<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonMaritalStatusText) &gt; 0">
						<xsl:choose>
							<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonReligionText) &gt; 0">
								<xsl:text>&#10;Marital Status          Religion&#10;</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>&#10;Marital Status                  &#10;</xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonReligionText) &gt; 0">
								<xsl:text>&#10;                        Religion&#10;</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>&#10;</xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
				<!-- -->
				<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonMaritalStatusText">
					<xsl:variable name="curPos" select="position()"/>
					<xsl:value-of select="."/>
					<xsl:variable name="metadataid">
						<xsl:value-of select="./@s:metadata"/>
					</xsl:variable>
					<xsl:variable name="metadataid2">
						<xsl:value-of select="../nc:PersonReligionText/@s:metadata"/>
					</xsl:variable>
					<xsl:if test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportedDate">
						<xsl:text> (</xsl:text>
						<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportedDate"/>
						<xsl:text>)</xsl:text>
					</xsl:if>
					<xsl:choose>
						<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonReligionText) &gt; 0">
							<xsl:choose>
								<xsl:when test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid2]/nc:ReportedDate">
									<xsl:call-template name="spaceover">
										<xsl:with-param name="amount" select="$posCol2 - (string-length(.)+13)"/>
										<xsl:with-param name="amountWrote" select="0"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="spaceover">
										<xsl:with-param name="amount" select="$posCol2 - string-length(.)"/>
										<xsl:with-param name="amountWrote" select="0"/>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:choose>
								<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonReligionText[position() = $curPos]) &gt; 0">
									<!-- if there is a religion at the same index write it. -->
									<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonReligionText[$curPos]"/>
									<xsl:variable name="metadataid3">
										<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonReligionText[$curPos]/@s:metadata"/>
									</xsl:variable>
									<xsl:if test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid3]/nc:ReportedDate">
										<xsl:text> (</xsl:text>
										<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid3]/nc:ReportedDate"/>
										<xsl:text>)</xsl:text>
									</xsl:if>
									<xsl:text>&#10;&#10;</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text>&#10;&#10;</xsl:text>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>&#10;&#10;</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
				<xsl:if test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonReligionText) &gt; count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonMaritalStatusText)">
					<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonReligionText">
						<xsl:if test="position() &gt; count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/nc:PersonMaritalStatusText)">
							<xsl:call-template name="spaceover">
								<xsl:with-param name="amount" select="$posCol2"/>
								<xsl:with-param name="amountWrote" select="0"/>
							</xsl:call-template>
							<xsl:value-of select="."/>
							<xsl:variable name="metadataid">
								<xsl:value-of select="./@s:metadata"/>
							</xsl:variable>
							<xsl:if test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportedDate">
								<xsl:text> (</xsl:text>
								<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportedDate"/>
								<xsl:text>)</xsl:text>
							</xsl:if>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
					</xsl:for-each>
				</xsl:if>
				<!-- Employment -->
				<xsl:choose>
					<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/nc:PersonEmploymentAssociation) &gt; 0">
						<xsl:text>Employment&#10;</xsl:text>
					</xsl:when>
					<xsl:otherwise/>
				</xsl:choose>
				<!-- -->
				<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/nc:PersonEmploymentAssociation">
					<xsl:variable name="metadataid">
						<xsl:value-of select="./@s:metadata"/>
					</xsl:variable>
					<xsl:if test="//rap:Metadata[@s:id=$metadataid]/nc:ReportedDate">
						<!-- If there is a reference date print it -->
						<xsl:text>Employment as of        </xsl:text>
						<xsl:value-of select="//rap:Metadata[@s:id=$metadataid]/nc:ReportedDate/nc:Date"/>
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
					<xsl:text>Occupation</xsl:text>
					<xsl:call-template name="wrapIn">
						<xsl:with-param name="Text" select="./nc:EmployeeOccupationText"/>
						<xsl:with-param name="StartPos" select="$posCol2 - 10"/>
						<xsl:with-param name="EndPos" select="$posMaxWidth"/>
						<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
						<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
						<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
						<xsl:with-param name="initialRun" select="0"/>
					</xsl:call-template>
					<xsl:text>&#10;</xsl:text>
					<xsl:text>Employer</xsl:text>
					<xsl:call-template name="wrapIn">
						<xsl:with-param name="Text" select="./nc:Employer/nc:EntityOrganization/nc:OrganizationName"/>
						<!--kate: account for person employer too?-->
						<xsl:with-param name="StartPos" select="$posCol2 - 8"/>
						<xsl:with-param name="EndPos" select="$posMaxWidth"/>
						<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
						<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
						<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
						<xsl:with-param name="initialRun" select="0"/>
					</xsl:call-template>
					<xsl:text>&#10;</xsl:text>
					<xsl:variable name="metadataid2">
						<xsl:value-of select="nc:EmploymentLocationReference/@s:ref"/>
					</xsl:variable>
					<xsl:if test="//nc:Location[@s:id=$metadataid2]/nc:LocationAddress">
						<xsl:text>Location</xsl:text>
						<xsl:if test="//nc:Location[@s:id=$metadataid2]/nc:LocationAddress/nc:AddressFullText">
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="//nc:Location[@s:id=$metadataid2]/nc:LocationAddress/nc:AddressFullText"/>
								<xsl:with-param name="StartPos" select="$posCol2 - 8"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:if test="//nc:Location[@s:id=$metadataid2]/nc:LocationAddress/nc:StructuredAddress/nc:BuildingName">
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="//nc:Location[@s:id=$metadataid2]/nc:LocationAddress/nc:StructuredAddress/nc:BuildingName"/>
								<xsl:with-param name="StartPos" select="$posCol2 - 17"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
						</xsl:if>
						<xsl:text>&#10;</xsl:text>
						<xsl:if test="//nc:Location[@s:id=$metadataid2]/nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet/nc:StreetFullText">
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="//nc:Location[@s:id=$metadataid2]/nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet/nc:StreetFullText"/>
								<xsl:with-param name="StartPos" select="$posCol2"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:if test="//nc:Location[@s:id=$metadataid2]/nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet/nc:StreetNumberText">
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="concat(//nc:Location[@s:id=$metadataid2]/nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet/nc:StreetNumberText,' ',//nc:Location[@s:id=$metadataid2]/nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet/nc:StreetPredirectionalText,' ',//nc:Location[@s:id=$metadataid2]/nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet/nc:StreetName,' ',//nc:Location[@s:id=$metadataid2]/nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet/nc:StreetTypeText)"/>
								<xsl:with-param name="StartPos" select="$posCol2"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:if test="//nc:Location[@s:id=$metadataid2]/nc:LocationAddress/nc:StructuredAddress/nc:LocationStateUSPostalServiceCode">
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="concat(//nc:Location[@s:id=$metadataid2]/nc:LocationAddress/nc:StructuredAddress/nc:LocationCityName,', ',//nc:Location[@s:id=$metadataid2]/nc:LocationAddress/nc:StructuredAddress/nc:LocationStateUSPostalServiceCode,' ',//nc:Location[@s:id=$metadataid2]/nc:LocationAddress/nc:StructuredAddress/nc:LocationPostalCode,'-',//nc:Location[@s:id=$metadataid2]/nc:LocationAddress/nc:StructuredAddress/nc:LocationPostalExtensionCode,' ',//nc:Location[@s:id=$metadataid2]/nc:LocationAddress/nc:StructuredAddress/nc:LocationCountryName)"/>
								<xsl:with-param name="StartPos" select="$posCol2"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:if test="//nc:Location[@s:id=$metadataid2]/nc:LocationAddress/nc:StructuredAddress/nc:LocationCountyName">
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="concat('County:  ',//nc:Location[@s:id=$metadataid2]/nc:LocationAddress/nc:StructuredAddress/nc:LocationCountyName)"/>
								<xsl:with-param name="StartPos" select="$posCol2"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
					</xsl:if>
				</xsl:for-each>
				<!-- Residence -->
				<xsl:choose>
					<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/nc:ResidenceAssociation) &gt; 0">
						<xsl:text>Residence&#10;</xsl:text>
					</xsl:when>
					<xsl:otherwise/>
				</xsl:choose>
				<!-- -->
				<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/nc:ResidenceAssociation">
					<xsl:text>Residence as of         </xsl:text>
					<xsl:variable name="metadataid">
						<xsl:value-of select="./@s:metadata"/>
					</xsl:variable>
					<xsl:variable name="metadataid2">
						<xsl:value-of select="nc:LocationReference/@s:ref"/>
					</xsl:variable>
					<xsl:if test="count(//rap:Metadata[@s:id=$metadataid]/nc:ReportedDate) &gt; 0">
						<!-- If there is a reference date print it -->
						<xsl:value-of select="//rap:Metadata[@s:id=$metadataid]/nc:ReportedDate"/>
					</xsl:if>
					<xsl:text>&#10;</xsl:text>
					<xsl:if test="//nc:Location[@s:id=$metadataid2]/nc:LocationAddress/nc:StructuredAddress/nc:BuildingName">
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="//nc:Location[@s:id=$metadataid2]/nc:LocationAddress/nc:StructuredAddress/nc:BuildingName"/>
							<xsl:with-param name="StartPos" select="$posCol2 - 17"/>
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
					<xsl:choose>
						<xsl:when test="//nc:Location[@s:id=$metadataid2]/nc:LocationAddress/nc:AddressFullText">
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="//nc:Location[@s:id=$metadataid2]/nc:LocationAddress/nc:AddressFullText"/>
								<xsl:with-param name="StartPos" select="$posCol2"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:if test="//nc:Location[@s:id=$metadataid2]/nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet/nc:StreetFullText">
								<xsl:call-template name="wrapIn">
									<xsl:with-param name="Text" select="//nc:Location[@s:id=$metadataid2]/nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet/nc:StreetFullText"/>
									<xsl:with-param name="StartPos" select="$posCol2"/>
									<xsl:with-param name="EndPos" select="$posMaxWidth"/>
									<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
									<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
									<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
									<xsl:with-param name="initialRun" select="0"/>
								</xsl:call-template>
								<xsl:text>&#10;</xsl:text>
							</xsl:if>
							<xsl:if test="//nc:Location[@s:id=$metadataid2]/nc:LocationAddress/nc:StructuredAddress/nc:LocationSecondaryUnitText">
								<xsl:call-template name="wrapIn">
									<xsl:with-param name="Text" select="//nc:Location[@s:id=$metadataid2]/nc:LocationAddress/nc:StructuredAddress/nc:LocationSecondaryUnitText"/>
									<xsl:with-param name="StartPos" select="$posCol2"/>
									<xsl:with-param name="EndPos" select="$posMaxWidth"/>
									<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
									<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
									<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
									<xsl:with-param name="initialRun" select="0"/>
								</xsl:call-template>
								<xsl:text>&#10;</xsl:text>
							</xsl:if>
							<xsl:if test="//nc:Location[@s:id=$metadataid2]/nc:LocationAddress/nc:StructuredAddress/nc:AddressMailDeliveryUnitText">
								<xsl:call-template name="wrapIn">
									<xsl:with-param name="Text" select="//nc:Location[@s:id=$metadataid2]/nc:LocationAddress/nc:StructuredAddress/nc:AddressMailDeliveryUnitText"/>
									<xsl:with-param name="StartPos" select="$posCol2"/>
									<xsl:with-param name="EndPos" select="$posMaxWidth"/>
									<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
									<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
									<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
									<xsl:with-param name="initialRun" select="0"/>
								</xsl:call-template>
								<xsl:text>&#10;</xsl:text>
							</xsl:if>
							<xsl:if test="//nc:Location[@s:id=$metadataid2]/nc:LocationAddress/nc:StructuredAddress/nc:LocationStateUSPostalServiceCode">
								<xsl:call-template name="wrapIn">
									<xsl:with-param name="Text" select="concat(//nc:Location[@s:id=$metadataid2]/nc:LocationAddress/nc:StructuredAddress/nc:LocationCityName,', ',//nc:Location[@s:id=$metadataid2]/nc:LocationAddress/nc:StructuredAddress/nc:LocationStateUSPostalServiceCode,' ',//nc:Location[@s:id=$metadataid2]/nc:LocationAddress/nc:StructuredAddress/nc:LocationPostalCode,' ',//nc:Location[@s:id=$metadataid2]/nc:LocationAddress/nc:StructuredAddress/nc:LocationCountryName)"/>
									<xsl:with-param name="StartPos" select="$posCol2"/>
									<xsl:with-param name="EndPos" select="$posMaxWidth"/>
									<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
									<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
									<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
									<xsl:with-param name="initialRun" select="0"/>
								</xsl:call-template>
								<xsl:text>&#10;</xsl:text>
							</xsl:if>
							<xsl:if test="//nc:Location[@s:id=$metadataid2]/nc:LocationAddress/nc:StructuredAddress/nc:LocationCountyName">
								<xsl:call-template name="wrapIn">
									<xsl:with-param name="Text" select="concat('County:  ',//nc:Location[@s:id=$metadataid2]/nc:LocationAddress/nc:StructuredAddress/nc:LocationCountyName)"/>
									<xsl:with-param name="StartPos" select="$posCol2"/>
									<xsl:with-param name="EndPos" select="$posMaxWidth"/>
									<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
									<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
									<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
									<xsl:with-param name="initialRun" select="0"/>
								</xsl:call-template>
								<xsl:text>&#10;</xsl:text>
							</xsl:if>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:choose>
						<xsl:when test="count(./nc:LocationContactInformation) &gt; 0">
							<!-- if there is a telephone number, write it -->
							<xsl:for-each select="./nc:LocationContactInformation">
								<xsl:text>Telephone</xsl:text>
								<xsl:choose>
									<xsl:when test="count(./nc:ContactTelephoneNumber/nc:TelephoneNumberFullID) &gt; 0">
										<xsl:choose>
											<xsl:when test="./nc:ContactTelephoneNumber/nc:TelephoneSuffixID">
												<xsl:call-template name="wrapIn">
													<xsl:with-param name="Text" select="concat(./nc:ContactTelephoneNumber/nc:TelephoneNumberFullID, ' x', ./nc:ContactTelephoneNumber/nc:TelephoneSuffixID)"/>
													<xsl:with-param name="StartPos" select="$posCol2 - 9"/>
													<xsl:with-param name="EndPos" select="$posMaxWidth"/>
													<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
													<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
													<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
													<xsl:with-param name="initialRun" select="0"/>
													<!-- 0=True -->
												</xsl:call-template>
											</xsl:when>
											<xsl:otherwise>
												<xsl:call-template name="wrapIn">
													<xsl:with-param name="Text" select="./nc:ContactTelephoneNumber/nc:TelephoneNumberFullID"/>
													<xsl:with-param name="StartPos" select="$posCol2 - 9"/>
													<xsl:with-param name="EndPos" select="$posMaxWidth"/>
													<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
													<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
													<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
													<xsl:with-param name="initialRun" select="0"/>
													<!-- 0=True -->
												</xsl:call-template>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:when>
									<xsl:otherwise/>
								</xsl:choose>
								<xsl:text>&#10;&#10;</xsl:text>
							</xsl:for-each>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>&#10;</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
				<!-- Fingerprint Images -->
				<xsl:if test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:PersonBiometricsAssociation/rap:PersonBiometrics/rap:PersonFingerprintSet">
					<xsl:text>Fingerprint Images&#10;</xsl:text>
				</xsl:if>
				<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:PersonBiometricsAssociation/rap:PersonBiometrics/rap:PersonFingerprintSet/rap:Fingerprint">
					<xsl:if test="nc:BiometricImage/nc:BinaryCapturer">
						<xsl:text>Fingerprint Image Available  </xsl:text>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="concat(nc:BiometricImage/nc:BinaryCapturer/rap:EntityOrganization/nc:OrganizationName, ' ', nc:BiometricImage/nc:BinaryCapturer/rap:EntityOrganization/j:OrganizationAugmentation/j:OrganizationORIIdentification/nc:IdentificationID)"/>
							<xsl:with-param name="StartPos" select="$amtColSpacer"/>
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
					<xsl:if test="ansi-nist:FingerprintPositionCode">
						<xsl:text>Available Image             </xsl:text>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="ansi-nist:FingerprintPositionCode"/>
							<xsl:with-param name="StartPos" select="$amtColSpacer"/>
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
					<xsl:if test="nc:BiometricImage/nc:BinaryCaptureDate/nc:Date">
						<xsl:text>Capture Date                  </xsl:text>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="nc:BiometricImage/nc:BinaryCaptureDate/nc:Date"/>
							<xsl:with-param name="StartPos" select="$amtColSpacer"/>
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
					<xsl:if test="nc:BiometricImage/nc:BinaryLocationURI">
						<xsl:text>Download URL                  </xsl:text>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="nc:BiometricImage/nc:BinaryLocationURI"/>
							<xsl:with-param name="StartPos" select="$amtColSpacer"/>
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
					<xsl:choose>
						<xsl:when test="nc:BiometricImage/nc:BinaryBase64Object">
							<xsl:text>(Transmitted Image Suppressed: &#10;</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="concat('Type:',nc:BiometricImage/nc:BinaryCategoryText, ' Format:',nc:BiometricImage/nc:BinaryFormatID, ' Size:',nc:BiometricImage/nc:BinarySizeValue,'K')"/>
								<xsl:with-param name="StartPos" select="$amtColSpacer"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="concat('Comment:',nc:BiometricImage/nc:BinaryDescriptionText,')')"/>
								<xsl:with-param name="StartPos" select="$amtColSpacer"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>(No Fingerprint Image Transmitted  </xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="concat('&#10;Comment:',nc:BiometricImage/nc:BinaryDescriptionText)"/>
								<xsl:with-param name="StartPos" select="$amtColSpacer"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>)&#10;</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
				<!-- Palmprint Images -->
				<xsl:if test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:PersonBiometricsAssociation/rap:PersonBiometrics/j:PersonPalmPrint">
					<xsl:text>&#10;Palmprint Images&#10;</xsl:text>
				</xsl:if>
				<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:PersonBiometricsAssociation/rap:PersonBiometrics/j:PersonPalmPrint">
					<xsl:if test="nc:BiometricImage/nc:BinaryCapturer">
						<xsl:text>Palmprint Image Available    </xsl:text>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="concat(nc:BiometricImage/nc:BinaryCapturer/rap:EntityOrganization/nc:OrganizationName, ' ', nc:BiometricImage/nc:BinaryCapturer/rap:EntityOrganization/j:OrganizationAugmentation/j:OrganizationORIIdentification/nc:IdentificationID)"/>
							<xsl:with-param name="StartPos" select="$amtColSpacer"/>
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
					<xsl:if test="nc:BiometricImage/nc:BinaryCategoryText">
						<xsl:text>Available Image             </xsl:text>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="nc:BiometricImage/nc:BinaryCategoryText"/>
							<xsl:with-param name="StartPos" select="$amtColSpacer"/>
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
					<xsl:if test="nc:BiometricImage/nc:BinaryCaptureDate/nc:Date">
						<xsl:text>Capture Date                  </xsl:text>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="nc:BiometricImage/nc:BinaryCaptureDate/nc:Date"/>
							<xsl:with-param name="StartPos" select="$amtColSpacer"/>
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
					<xsl:if test="nc:BiometricImage/nc:BinaryLocationURI">
						<xsl:text>Download URL                  </xsl:text>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="nc:BiometricImage/nc:BinaryLocationURI"/>
							<xsl:with-param name="StartPos" select="$amtColSpacer"/>
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
					<xsl:choose>
						<xsl:when test="nc:BiometricImage/nc:BinaryBase64Object">
							<xsl:text>(Transmitted Image Suppressed: &#10;</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="concat('Type:',nc:BiometricImage/nc:BinaryCategoryText, ' Format:',nc:BiometricImage/nc:BinaryFormatID, ' Size:',nc:BiometricImage/nc:BinarySizeValue,'K')"/>
								<xsl:with-param name="StartPos" select="$amtColSpacer"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:if test="nc:BiometricImage/nc:BinaryDescriptionText">
								<xsl:call-template name="wrapIn">
									<xsl:with-param name="Text" select="concat('Comment:',nc:BiometricImage/nc:BinaryDescriptionText,')')"/>
									<xsl:with-param name="StartPos" select="$amtColSpacer"/>
									<xsl:with-param name="EndPos" select="$posMaxWidth"/>
									<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
									<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
									<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
									<xsl:with-param name="initialRun" select="0"/>
								</xsl:call-template>
							</xsl:if>
							<xsl:text>&#10;</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>(No Palmprint Image Transmitted  </xsl:text>
							<xsl:if test="nc:BiometricImage/nc:BinaryDescriptionText">
								<xsl:call-template name="wrapIn">
									<xsl:with-param name="Text" select="concat('&#10;Comment:',nc:BiometricImage/nc:BinaryDescriptionText)"/>
									<xsl:with-param name="StartPos" select="$amtColSpacer"/>
									<xsl:with-param name="EndPos" select="$posMaxWidth"/>
									<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
									<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
									<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
									<xsl:with-param name="initialRun" select="0"/>
								</xsl:call-template>
							</xsl:if>
							<xsl:text>)&#10;</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
				<!-- Photo Images -->
				<xsl:if test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:PersonBiometricsAssociation/rap:PersonBiometrics/nc:PersonDigitalImage">
					<xsl:text>&#10;Photo Images&#10;</xsl:text>
				</xsl:if>
				<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:PersonBiometricsAssociation/rap:PersonBiometrics/nc:PersonDigitalImage">
					<xsl:if test="nc:BiometricImage/nc:BinaryCapturer">
						<xsl:text>Photo Image Available       </xsl:text>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="concat(nc:BiometricImage/nc:BinaryCapturer/rap:EntityOrganization/nc:OrganizationName, ' ', nc:BiometricImage/nc:BinaryCapturer/rap:EntityOrganization/j:OrganizationAugmentation/j:OrganizationORIIdentification/nc:IdentificationID)"/>
							<xsl:with-param name="StartPos" select="$amtColSpacer"/>
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
					<xsl:if test="nc:BiometricImage/nc:BinaryCategoryText">
						<xsl:text>Available Image             </xsl:text>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="nc:BiometricImage/nc:BinaryCategoryText"/>
							<xsl:with-param name="StartPos" select="$amtColSpacer"/>
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
					<xsl:if test="nc:BiometricImage/nc:BinaryCaptureDate/nc:Date">
						<xsl:text>Capture Date                  </xsl:text>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="nc:BiometricImage/nc:BinaryCaptureDate/nc:Date"/>
							<xsl:with-param name="StartPos" select="$amtColSpacer"/>
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
					<xsl:if test="nc:BiometricImage/nc:BinaryLocationURI">
						<xsl:text>Download URL                  </xsl:text>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="nc:BiometricImage/nc:BinaryLocationURI"/>
							<xsl:with-param name="StartPos" select="$amtColSpacer"/>
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
					<xsl:choose>
						<xsl:when test="nc:BiometricImage/nc:BinaryBase64Object">
							<xsl:text>(Transmitted Image Suppressed: &#10;</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="concat('Type:',nc:BiometricImage/nc:BinaryTypeText, ' Format:',nc:BiometricImage/nc:BinaryFormatID, ' Size:',nc:BiometricImage/nc:BinarySizeValue,'K')"/>
								<xsl:with-param name="StartPos" select="$amtColSpacer"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:if test="nc:BiometricImage/nc:BinaryDescriptionText">
								<xsl:call-template name="wrapIn">
									<xsl:with-param name="Text" select="concat('Comment:',nc:BiometricImage/nc:BinaryDescriptionText,')')"/>
									<xsl:with-param name="StartPos" select="$amtColSpacer"/>
									<xsl:with-param name="EndPos" select="$posMaxWidth"/>
									<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
									<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
									<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
									<xsl:with-param name="initialRun" select="0"/>
								</xsl:call-template>
							</xsl:if>
							<xsl:text>&#10;</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>(No Photo Image Transmitted  </xsl:text>
							<xsl:if test="nc:BiometricImage/nc:BinaryDescriptionText">
								<xsl:call-template name="wrapIn">
									<xsl:with-param name="Text" select="concat('&#10;Comment:',nc:BiometricImage/nc:BinaryDescriptionText)"/>
									<xsl:with-param name="StartPos" select="$amtColSpacer"/>
									<xsl:with-param name="EndPos" select="$posMaxWidth"/>
									<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
									<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
									<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
									<xsl:with-param name="initialRun" select="0"/>
								</xsl:call-template>
							</xsl:if>
							<xsl:text>)&#10;</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
				<!-- Iris Images -->
				<xsl:if test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:PersonBiometricsAssociation/rap:PersonBiometrics/j:PersonIrisFeatures">
					<xsl:text>&#10;Iris Images&#10;</xsl:text>
				</xsl:if>
				<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:PersonBiometricsAssociation/rap:PersonBiometrics/j:PersonIrisFeatures">
					<xsl:if test="nc:BiometricImage/nc:BinaryCapturer">
						<xsl:text>Iris Image Available    </xsl:text>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="concat(nc:BiometricImage/nc:BinaryCapturer/rap:EntityOrganization/nc:OrganizationName, ' ', nc:BiometricImage/nc:BinaryCapturer/rap:EntityOrganization/j:OrganizationAugmentation/j:OrganizationORIID/nc:IdentificationID)"/>
							<xsl:with-param name="StartPos" select="$amtColSpacer"/>
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
					<xsl:if test="nc:BiometricImage/nc:BinaryCategoryText">
						<xsl:text>Available Image             </xsl:text>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="nc:BiometricImage/nc:BinaryCategoryText"/>
							<xsl:with-param name="StartPos" select="$amtColSpacer"/>
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
					<xsl:if test="nc:BiometricImage/nc:BinaryCaptureDate/nc:Date">
						<xsl:text>Capture Date                  </xsl:text>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="nc:BiometricImage/nc:BinaryCaptureDate/nc:Date"/>
							<xsl:with-param name="StartPos" select="$amtColSpacer"/>
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
					<xsl:if test="nc:BiometricImage/nc:BinaryLocationURI">
						<xsl:text>Download URL                  </xsl:text>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="nc:BiometricImage/nc:BinaryLocationURI"/>
							<xsl:with-param name="StartPos" select="$amtColSpacer"/>
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
					<xsl:choose>
						<xsl:when test="nc:BiometricImage/nc:BinaryBase64Object">
							<xsl:text>(Transmitted Image Suppressed: &#10;</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="concat('Type:',nc:BiometricImage/nc:BinaryCategoryText, ' Format:',nc:BiometricImage/nc:BinaryFormatID, ' Size:',nc:BiometricImage/nc:BinarySizeValue,'K')"/>
								<xsl:with-param name="StartPos" select="$amtColSpacer"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:if test="nc:BiometricImage/nc:BinaryDescriptionText">
								<xsl:call-template name="wrapIn">
									<xsl:with-param name="Text" select="concat('Comment:',nc:BiometricImage/nc:BinaryDescriptionText,')')"/>
									<xsl:with-param name="StartPos" select="$amtColSpacer"/>
									<xsl:with-param name="EndPos" select="$posMaxWidth"/>
									<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
									<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
									<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
									<xsl:with-param name="initialRun" select="0"/>
								</xsl:call-template>
							</xsl:if>
							<xsl:text>&#10;</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>(No Iris Image Transmitted  </xsl:text>
							<xsl:if test="nc:BiometricImage/nc:BinaryDescriptionText">
								<xsl:call-template name="wrapIn">
									<xsl:with-param name="Text" select="concat('&#10;Comment:',nc:BiometricImage/nc:BinaryDescriptionText)"/>
									<xsl:with-param name="StartPos" select="$amtColSpacer"/>
									<xsl:with-param name="EndPos" select="$posMaxWidth"/>
									<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
									<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
									<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
									<xsl:with-param name="initialRun" select="0"/>
								</xsl:call-template>
							</xsl:if>
							<xsl:text>)&#10;</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
				<!-- Signature Images -->
				<xsl:if test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:PersonBiometricsAssociation/rap:PersonBiometrics/j:PersonDigitizedSignatureImage">
					<xsl:text>&#10;Signature Images&#10;</xsl:text>
				</xsl:if>
				<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:PersonBiometricsAssociation/rap:PersonBiometrics/j:PersonDigitizedSignatureImage">
					<xsl:if test="nc:BiometricImage/nc:BinaryCapturer">
						<xsl:text>Signature Image Available    </xsl:text>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="concat(nc:BiometricImage/nc:BinaryCapturer/rap:EntityOrganization/nc:OrganizationName, ' ', nc:BiometricImage/nc:BinaryCapturer/rap:EntityOrganization/j:OrganizationAugmentation/j:OrganizationORIID/nc:IdentificationID)"/>
							<xsl:with-param name="StartPos" select="$amtColSpacer"/>
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
					<xsl:if test="nc:BiometricImage/nc:BinaryCategoryText">
						<xsl:text>Available Image             </xsl:text>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="nc:BiometricImage/nc:BinaryCategoryText"/>
							<xsl:with-param name="StartPos" select="$amtColSpacer"/>
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
					<xsl:if test="nc:BiometricImage/nc:BinaryCaptureDate/nc:Date">
						<xsl:text>Capture Date                  </xsl:text>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="nc:BiometricImage/nc:BinaryCaptureDate/nc:Date"/>
							<xsl:with-param name="StartPos" select="$amtColSpacer"/>
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
					<xsl:if test="nc:BiometricImage/nc:BinaryLocationURI">
						<xsl:text>Download URL                  </xsl:text>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="nc:BiometricImage/nc:BinaryLocationURI"/>
							<xsl:with-param name="StartPos" select="$amtColSpacer"/>
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
					<xsl:choose>
						<xsl:when test="nc:BiometricImage/nc:BinaryBase64Object">
							<xsl:text>(Transmitted Image Suppressed: &#10;</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="concat('Type:',nc:BiometricImage/nc:BinaryCategoryText, ' Format:',nc:BiometricImage/nc:BinaryFormatID, ' Size:',nc:BiometricImage/nc:BinarySizeValue,'K')"/>
								<xsl:with-param name="StartPos" select="$amtColSpacer"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:if test="nc:BiometricImage/nc:BinaryDescriptionText">
								<xsl:call-template name="wrapIn">
									<xsl:with-param name="Text" select="concat('Comment:',nc:BiometricImage/nc:BinaryDescriptionText,')')"/>
									<xsl:with-param name="StartPos" select="$amtColSpacer"/>
									<xsl:with-param name="EndPos" select="$posMaxWidth"/>
									<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
									<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
									<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
									<xsl:with-param name="initialRun" select="0"/>
								</xsl:call-template>
							</xsl:if>
							<xsl:text>&#10;</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>(No Iris Image Transmitted  </xsl:text>
							<xsl:if test="nc:BiometricImage/nc:BinaryDescriptionText">
								<xsl:call-template name="wrapIn">
									<xsl:with-param name="Text" select="concat('&#10;Comment:',nc:BiometricImage/nc:BinaryDescriptionText)"/>
									<xsl:with-param name="StartPos" select="$amtColSpacer"/>
									<xsl:with-param name="EndPos" select="$posMaxWidth"/>
									<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
									<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
									<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
									<xsl:with-param name="initialRun" select="0"/>
								</xsl:call-template>
							</xsl:if>
							<xsl:text>)&#10;</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
				<!-- DNA -->
				<xsl:if test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:PersonBiometricsAssociation/rap:PersonBiometrics/rap:PersonDNA">
					<xsl:text>&#10;DNA Data&#10;</xsl:text>
				</xsl:if>
				<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:PersonBiometricsAssociation/rap:PersonBiometrics/rap:PersonDNA">
					<xsl:if test="nc:BiometricCaptureDate/nc:Date">
						<xsl:text>DNA Sample Taken              </xsl:text>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="nc:BiometricCaptureDate/nc:Date"/>
							<xsl:with-param name="StartPos" select="$amtColSpacer"/>
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
					<xsl:if test="nc:BiometricImage/nc:BinaryCapturer">
						<xsl:text>DNA Information Available   </xsl:text>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="concat(nc:BiometricImage/nc:BinaryCapturer/rap:EntityOrganization/nc:OrganizationName, ' ', nc:BiometricCapturer/rap:EntityOrganization/j:OrganizationAugmentation/j:OrganizationORIIdentification/nc:IdentificationID)"/>
							<xsl:with-param name="StartPos" select="$amtColSpacer"/>
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
					<xsl:if test="nc:BiometricCapturer">
						<xsl:text>DNA Information Available   </xsl:text>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="concat(nc:BiometricCapturer/rap:EntityOrganization/nc:OrganizationName, ' ', nc:BiometricCapturer/rap:EntityOrganization/j:OrganizationAugmentation/j:OrganizationORIIdentification/nc:IdentificationID)"/>
							<xsl:with-param name="StartPos" select="$amtColSpacer"/>
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
					<xsl:choose>
						<xsl:when test="nc:DNALocus">
							<xsl:text>(Transmitted DNA Detail Suppressed</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="concat('; Encoding Method',nc:BiometricEncodingMethodText)"/>
								<xsl:with-param name="StartPos" select="$amtColSpacer"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>(No DNA Detail Transmitted  </xsl:text>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:if test="nc:BiometricDescriptionText">
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="concat('&#10;Comment:',nc:BiometricDescriptionText)"/>
							<xsl:with-param name="StartPos" select="$amtColSpacer"/>
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
					</xsl:if>
					<xsl:text>)&#10;</xsl:text>
				</xsl:for-each>
				<!-- Caution Information -->
				<xsl:if test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/j:PersonAugmentation/j:PersonFirearmSalesDisqualifiedCode) &gt; 0 or count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:SubjectCautionInformationText) &gt; 0 or count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/j:SubjectOffenderNoticeText) &gt; 0 or count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/j:PersonAugmentation/j:PersonRegisteredOffenderIndicator) &gt; 0">
					<xsl:text>&#10;Caution Information&#10;</xsl:text>
				</xsl:if>
				<!-- Firearms Disqualified -->
				<xsl:if test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/j:PersonAugmentation/j:PersonFirearmSalesDisqualifiedCode) &gt; 0">
					<xsl:variable name="metadataid">
						<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/j:PersonAugmentation/j:PersonFirearmSalesDisqualifiedCode/@s:metadata"/>
					</xsl:variable>
					<xsl:text>Firearms Disqualified Status</xsl:text>
					<xsl:call-template name="spaceover">
						<xsl:with-param name="amount" select="$posCol2 - 21"/>
						<xsl:with-param name="amountWrote" select="0"/>
					</xsl:call-template>
					<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/j:PersonAugmentation/j:PersonFirearmSalesDisqualifiedCode"/>
					<xsl:text>- </xsl:text>
					<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:CommentText"/>
					<xsl:text>&#10;</xsl:text>
				</xsl:if>
				<!-- Registered Sex Offender -->
				<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/j:PersonAugmentation/j:PersonRegisteredOffenderIndicator">
					<xsl:if test=".!='false'">
						<xsl:text>&#10;Registered Sex Offender</xsl:text>
						<xsl:variable name="metadataid">
							<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/j:PersonAugmentation/j:PersonRegisteredOffenderIndicator/@s:metadata"/>
						</xsl:variable>
						<xsl:call-template name="spaceover">
							<xsl:with-param name="amount" select="$posCol2 - 21"/>
							<xsl:with-param name="amountWrote" select="0"/>
						</xsl:call-template>
						<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportingOrganizationText"/>
						<xsl:text> </xsl:text>
						<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportedDate"/>
						<xsl:text> </xsl:text>
						<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:CommentText"/>
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
				</xsl:for-each>
				<!-- Caution -->
				<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/rap:SubjectCautionInformationText">
					<xsl:text>&#10;Caution</xsl:text>
					<xsl:variable name="metadataid">
						<xsl:value-of select="./@s:metadata"/>
					</xsl:variable>
					<xsl:variable name="CautionInfo">
						<xsl:value-of select="."/>
						<xsl:if test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportedDate or //ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportingOrganizationText">
							<xsl:text> (</xsl:text>
							<xsl:if test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportingOrganizationText">
								<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportingOrganizationText"/>
							</xsl:if>
							<xsl:if test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportedDate and //ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportingOrganizationText">
								<xsl:text>; </xsl:text>
							</xsl:if>
							<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportedDate"/>
							<xsl:text>)</xsl:text>
						</xsl:if>
					</xsl:variable>
					<!-- djr(19-mar-03) Write out the Description Info now that it is all formatted. -->
					<xsl:call-template name="wrapIn">
						<xsl:with-param name="Text" select="$CautionInfo"/>
						<xsl:with-param name="StartPos" select="$posCol2 - 7"/>
						<xsl:with-param name="EndPos" select="$posMaxWidth"/>
						<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
						<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
						<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
						<xsl:with-param name="initialRun" select="0"/>
					</xsl:call-template>
					<xsl:text>&#10;</xsl:text>
				</xsl:for-each>
				<!-- xsl:text>&#10;</xsl:text -->
				<!-- Notice -->
				<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/j:SubjectOffenderNoticeText">
					<xsl:text>&#10;Notice</xsl:text>
					<xsl:variable name="metadataid">
						<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/j:SubjectOffenderNoticeText/@s:metadata"/>
					</xsl:variable>
					<xsl:variable name="NoticeInfo">
						<xsl:value-of select="."/>
						<xsl:if test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportedDate or //ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportingOrganizationText">
							<xsl:text> (</xsl:text>
							<xsl:if test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportingOrganizationText">
								<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportingOrganizationText"/>
							</xsl:if>
							<xsl:if test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportedDate and //ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportingOrganizationText">
								<xsl:text>; </xsl:text>
							</xsl:if>
							<xsl:value-of select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid]/nc:ReportedDate"/>
							<xsl:text>)</xsl:text>
						</xsl:if>
					</xsl:variable>
					<xsl:call-template name="wrapIn">
						<xsl:with-param name="Text" select="$NoticeInfo"/>
						<xsl:with-param name="StartPos" select="$posCol2 - (6)"/>
						<xsl:with-param name="EndPos" select="$posMaxWidth"/>
						<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
						<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
						<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
						<xsl:with-param name="initialRun" select="0"/>
					</xsl:call-template>
				</xsl:for-each>
				<xsl:text>&#10;</xsl:text>
				<!-- ########## Criminal History Section ########## -->
				<xsl:text>&#10;**************************  CRIMINAL HISTORY  **************************&#10;&#10;</xsl:text>
				<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/ch-ext:RapSheetCycle">
					<xsl:sort select="./rap:CycleEarliestDate/nc:Date"/>
					<!-- dr: 17-OCT-02 sort all Cycles by earliestDate -->
					<xsl:variable name="Cycle-number" select="position()"/>
					<xsl:text>=============================== Cycle </xsl:text>
					<xsl:value-of select="format-number($Cycle-number,'#000')"/>
					<xsl:text> ==============================&#10;</xsl:text>
					<xsl:for-each select="./rap:CycleCaveat/nc:CaveatText">
						<xsl:text>Cycle Caveat</xsl:text>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="concat(.,' ',../rap:CaveatReferenceDate,' ',../rap:CaveatIssuingAuthorityText)"/>
							<xsl:with-param name="StartPos" select="$posCol2 - (12)"/>
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:for-each>
					<xsl:for-each select="./rap:CycleCaveat/nc:CaveatFormattedText">
						<xsl:text>Cycle Caveat</xsl:text>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="concat(.,' ',../rap:CaveatReferenceDate,' ',../rap:CaveatIssuingAuthorityText)"/>
							<xsl:with-param name="StartPos" select="$posCol2 - (12)"/>
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:for-each>
					<xsl:if test="./rap:CycleTrackingIdentificationID">
						<xsl:text>Tracking Number</xsl:text>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="./rap:CycleTrackingIdentificationID"/>
							<xsl:with-param name="StartPos" select="$posCol2 - (15)"/>
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
					<!-- -->
					<xsl:if test="./rap:CycleEarliestDate/nc:Date">
						<xsl:text>Earliest Event Date</xsl:text>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="./rap:CycleEarliestDate/nc:Date"/>
							<xsl:with-param name="StartPos" select="$posCol2 - (19)"/>
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
					</xsl:if>
					<xsl:if test="./rap:Incident/nc:ActivityDate/nc:Date">
						<xsl:text> Incident Date</xsl:text>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="./rap:Incident/nc:ActivityDate/nc:Date"/>
							<xsl:with-param name="StartPos" select="$posCol2 - (14)"/>
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
					</xsl:if>
					<!-- -->
					<!-- Arrest Section -->
					<xsl:for-each select="rap:Arrest">
						<!-- Print Arrest Info -->
						<xsl:text>&#10;------------------------------------------------------------------------&#10;</xsl:text>
						<xsl:variable name="linkid">
							<xsl:value-of select="./@s:id"/>
						</xsl:variable>
						<!--ADD-->
						<xsl:if test="./nc:ActivityIdentification/nc:IdentificationID">
							<xsl:text>Originating Agency Police ID</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="./nc:ActivityIdentification/nc:IdentificationID"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (11)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:if test="./nc:ActivityIdentification/nc:IdentificationJurisdictionText">
							<xsl:text>Originating Agency Jurisdiction</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="./nc:ActivityIdentification/nc:IdentificationJurisdictionText"/>
								<xsl:with-param name="StartPos" select="$posCol2 - 22"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<!--ADD-->
						<xsl:if test="./nc:ActivityDate/nc:Date">
							<xsl:text>Arrest Date</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="./nc:ActivityDate/nc:Date"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (11)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<!-- -->
						<xsl:if test="j:ArrestAgencyRecordIdentification">
							<xsl:text>Arrest Case Number</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="./j:ArrestAgencyRecordIdentification/nc:IdentificationID"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (18)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:if test="//rap:ArrestAgencyAssociation[nc:ActivityReference/@s:ref=$linkid]/j:ArrestAgencyRecordIdentification/nc:IdentificationID">
							<xsl:text>Arrest Case Number</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="//rap:ArrestAgencyAssociation[nc:ActivityReference/@s:ref=$linkid]/j:ArrestAgencyRecordIdentification/nc:IdentificationID"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (18)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<!-- -->
						<xsl:if test="//rap:ArrestAgencyAssociation[nc:ActivityReference/@s:ref=$linkid]">
							<xsl:variable name="agencyid">
								<xsl:value-of select="//rap:ArrestAgencyAssociation[nc:ActivityReference/@s:ref=$linkid]/nc:OrganizationReference/@s:ref"/>
							</xsl:variable>
							<xsl:choose>
								<xsl:when test="//rap:ArrestAgencyAssociation[nc:ActivityReference/@s:ref=$linkid]/rap:PrimaryAgencyIndicator='true'">
									<xsl:text>Arresting Agency</xsl:text>
									<xsl:call-template name="wrapIn">
										<xsl:with-param name="Text" select="concat(//rap:Agency[@s:id=$agencyid]/j:OrganizationAugmentation/j:OrganizationORIIdentification/nc:IdentificationID,' ',//rap:Agency[@s:id=$agencyid]/nc:OrganizationName, ' - Primary')"/>
										<xsl:with-param name="StartPos" select="$posCol2 - (16)"/>
										<xsl:with-param name="EndPos" select="$posMaxWidth"/>
										<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
										<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
										<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
										<xsl:with-param name="initialRun" select="0"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:when test="//rap:ArrestAgencyAssociation[nc:ActivityReference/@s:ref=$linkid]/rap:PrimaryAgencyIndicator='false'">
									<xsl:text>Arresting Agency</xsl:text>
									<xsl:call-template name="wrapIn">
										<xsl:with-param name="Text" select="concat(//rap:Agency[@s:id=$agencyid]/j:OrganizationAugmentation/j:OrganizationORIIdentification/nc:IdentificationID,' ',//rap:Agency[@s:id=$agencyid]/nc:OrganizationName, ' - Secondary')"/>
										<xsl:with-param name="StartPos" select="$posCol2 - (16)"/>
										<xsl:with-param name="EndPos" select="$posMaxWidth"/>
										<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
										<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
										<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
										<xsl:with-param name="initialRun" select="0"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text>Arresting Agency</xsl:text>
									<xsl:call-template name="wrapIn">
										<xsl:with-param name="Text" select="concat(//rap:Agency[@s:id=$agencyid]/j:OrganizationAugmentation/j:OrganizationORIIdentification/nc:IdentificationID,' ',//rap:Agency[@s:id=$agencyid]/nc:OrganizationName)"/>
										<xsl:with-param name="StartPos" select="$posCol2 - (16)"/>
										<xsl:with-param name="EndPos" select="$posMaxWidth"/>
										<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
										<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
										<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
										<xsl:with-param name="initialRun" select="0"/>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<!-- -->
						<xsl:if test="./rap:ArrestSubject">
							<xsl:text>Subject's Name</xsl:text>
						</xsl:if>
						<!-- -->
						<xsl:for-each select="./rap:ArrestSubject/rap:SubjectFullName">
							<!-- Name -->
							<xsl:variable name="i" select="position()"/>
							<xsl:choose>
								<!-- determine the correct spacing before the write of name data -->
								<xsl:when test="$i = 1">
									<xsl:call-template name="spaceover">
										<xsl:with-param name="amount" select="$posCol2 - 14"/>
										<xsl:with-param name="amountWrote" select="0"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="spaceover">
										<xsl:with-param name="amount" select="$posCol2"/>
										<xsl:with-param name="amountWrote" select="0"/>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:value-of select="."/>
							<xsl:text>&#10;</xsl:text>
						</xsl:for-each>
						<!-- Removed Dashes 09/07 -->
						<xsl:for-each select="./rap:ArrestSubject/j:SubjectIdentification">
							<xsl:text>    Offender Id Number</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="concat(nc:IdentificationID, ' ', nc:IdentificationSourceText)"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (22)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:for-each>
						<xsl:if test="./nc:ActivityCategoryText">
							<xsl:text>Arrest Type           </xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="./nc:ActivityCategoryText"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (22)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:for-each select="./nc:ActivityDescriptionText">
							<xsl:text>Comment(s)            </xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="normalize-space(.)"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (22)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:for-each>
						<xsl:for-each select="./j:ArrestCharge">
							<xsl:call-template name="PrintCharge">
								<xsl:with-param name="Charge" select="."/>
							</xsl:call-template>
						</xsl:for-each>
						<xsl:for-each select="./rap:ArrestCharge">
							<xsl:call-template name="PrintCharge">
								<xsl:with-param name="Charge" select="."/>
							</xsl:call-template>
						</xsl:for-each>
					</xsl:for-each>
					<!-- Juvenile Detainment  -->
					<xsl:for-each select="rap:JuvenileActivity">
						<!-- Print Juvenile Activity Info -->
						<xsl:text>&#10;------------------------------------------------------------------------&#10;</xsl:text>
						<xsl:variable name="linkid">
							<xsl:value-of select="./@s:id"/>
						</xsl:variable>
						<xsl:if test="./nc:ActivityDate/nc:Date">
							<xsl:text>Juvenile Detainment Date</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="./nc:ActivityDate/nc:Date"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (24)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<!-- -->
						<xsl:if test="rap:JuvenileActivityAgencyRecordIdentification">
							<xsl:text>Juvenile Detainment Case Number</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="./rap:JuvenileActivityAgencyRecordIdentification/nc:IdentificationID"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (31)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<!-- -->
						<xsl:if test="//rap:JuvenileActivityAgencyAssociation[nc:ActivityReference/@s:ref=$linkid]">
							<xsl:variable name="agencyid">
								<xsl:value-of select="//rap:JuvenileActivityAgencyAssociation[nc:ActivityReference/@s:ref=$linkid]/nc:OrganizationReference/@s:ref"/>
							</xsl:variable>
							<xsl:text>Detaining Agency</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="concat(//rap:Agency[@s:id=$agencyid]/j:OrganizationAugmentation/j:OrganizationORIIdentification/nc:IdentificationID,' ',//rap:Agency[@s:id=$agencyid]/nc:OrganizationName)"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (16)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<!-- -->
						<xsl:if test="./rap:JuvenileActivitySubject">
							<xsl:text>Subject's Name</xsl:text>
						</xsl:if>
						<!-- -->
						<xsl:for-each select="./rap:JuvenileActivitySubject/rap:SubjectFullName">
							<!-- Name -->
							<xsl:variable name="i" select="position()"/>
							<xsl:choose>
								<!-- determine the correct spacing before the write of name data -->
								<xsl:when test="$i = 1">
									<xsl:call-template name="spaceover">
										<xsl:with-param name="amount" select="$posCol2 - 14"/>
										<xsl:with-param name="amountWrote" select="0"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="spaceover">
										<xsl:with-param name="amount" select="$posCol2"/>
										<xsl:with-param name="amountWrote" select="0"/>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:value-of select="."/>
							<xsl:text>&#10;</xsl:text>
						</xsl:for-each>
						<!-- Removed Dashes 09/07 -->
						<xsl:for-each select="./rap:JuvenileActivitySubject/j:SubjectIdentification">
							<xsl:text>    Offender Id Number</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="concat(nc:IdentificationID, ' ', nc:IdentificationSourceText)"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (22)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:for-each>
						<xsl:if test="./nc:ActivityCategoryText">
							<xsl:text>Activity Type         </xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="./nc:ActivityCategoryText"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (22)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:for-each select="./nc:ActivityDescriptionText">
							<xsl:text>Comment(s)            </xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="normalize-space(.)"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (22)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:for-each>
						<xsl:for-each select="./rap:JuvenileActivityCharge">
							<xsl:call-template name="PrintCharge">
								<xsl:with-param name="Charge" select="."/>
							</xsl:call-template>
						</xsl:for-each>
					</xsl:for-each>
					<!-- Booking Section -->
					<xsl:for-each select="rap:Booking">
						<xsl:text>&#10;------------------------------------------------------------------------&#10;</xsl:text>
						<xsl:variable name="linkid">
							<xsl:value-of select="./@s:id"/>
						</xsl:variable>
						<xsl:if test="./j:BookingAgencyRecordIdentification">
							<xsl:text>Booking Case Number</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="./j:BookingAgencyRecordIdentification/nc:IdentificationID"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (19)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:if test="//rap:BookingAgencyAssociation[nc:ActivityReference/@s:ref=$linkid]">
							<xsl:variable name="agencyid">
								<xsl:value-of select="//rap:BookingAgencyAssociation[nc:ActivityReference/@s:ref=$linkid]/nc:OrganizationReference/@s:ref"/>
							</xsl:variable>
							<xsl:text>Booking Agency</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="concat(//rap:Agency[@s:id=$agencyid]/j:OrganizationAugmentation/j:OrganizationORIIdentification/nc:IdentificationID,' ',//rap:Agency[@s:id=$agencyid]/nc:OrganizationName)"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (14)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
					</xsl:for-each>
					<!-- Prosecutor Disposition Section -->
					<xsl:for-each select="rap:Prosecution">
						<xsl:text>------------------------------------------------------------------------&#10;</xsl:text>
						<xsl:text>Prosecutor Disposition  (Cycle </xsl:text>
						<xsl:value-of select="format-number($Cycle-number,'#000')"/>
						<xsl:text>)</xsl:text>
						<xsl:text>&#10;</xsl:text>
						<xsl:variable name="linkid">
							<xsl:value-of select="./@s:id"/>
						</xsl:variable>
						<xsl:if test="./nc:ActivityIdentification/nc:IdentificationID">
							<xsl:text>Prosecution ID</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="./nc:ActivityIdentification/nc:IdentificationID"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (16)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:if test="./rap:ProsecutionAgencyRecordIdentification/nc:IdentificationID">
							<xsl:text>Prosecutor Case Number</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="./rap:ProsecutionAgencyRecordIdentification/nc:IdentificationID"/>
								<xsl:with-param name="StartPos" select="$posCol2 - 22"/>
								<!-- space over to Column2 start pos -->
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:if test="./nc:ActivityDate/nc:Date">
							<xsl:text>Prosecution Date</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="./nc:ActivityDate/nc:Date"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (16)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:if test="//rap:ProsecutionAgencyAssociation[rap:ProsecutionReference/@s:ref=$linkid]">
							<xsl:variable name="agencyid">
								<xsl:value-of select="//rap:ProsecutionAgencyAssociation[rap:ProsecutionReference/@s:ref=$linkid]/nc:OrganizationReference/@s:ref"/>
							</xsl:variable>
							<xsl:text>Prosecutor Agency</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="concat(//rap:Agency[@s:id=$agencyid]/j:OrganizationAugmentation/j:OrganizationORIIdentification/nc:IdentificationID,' ',//rap:Agency[@s:id=$agencyid]/nc:OrganizationName)"/>
								<xsl:with-param name="StartPos" select="$posCol2 - 17"/>
								<!-- space over to Column2 start pos -->
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:if test="./rap:ProsecutionSubject/rap:SubjectFullName">
							<xsl:text>Subject's Name</xsl:text>
						</xsl:if>
						<xsl:for-each select="./rap:ProsecutionSubject/rap:SubjectFullName">
							<!-- Name -->
							<xsl:variable name="i" select="position()"/>
							<xsl:choose>
								<!-- determine the correct spacing before the write of name data -->
								<xsl:when test="$i = 1">
									<xsl:call-template name="spaceover">
										<xsl:with-param name="amount" select="$posCol2 - 14"/>
										<xsl:with-param name="amountWrote" select="0"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="spaceover">
										<xsl:with-param name="amount" select="$posCol2"/>
										<xsl:with-param name="amountWrote" select="0"/>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:value-of select="."/>
							<xsl:text>&#10;</xsl:text>
						</xsl:for-each>
						<xsl:for-each select="./j:ProsecutionCharge">
							<xsl:call-template name="PrintCharge">
								<xsl:with-param name="Charge" select="."/>
							</xsl:call-template>
						</xsl:for-each>
						<xsl:for-each select="./rap:ProsecutionCharge">
							<xsl:call-template name="PrintCharge">
								<xsl:with-param name="Charge" select="."/>
							</xsl:call-template>
						</xsl:for-each>
						<xsl:for-each select="nc:ActivityDescriptionText">
							<xsl:variable name="i" select="position()"/>
							<xsl:text>    Prosecution Comment</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="normalize-space(.)"/>
								<xsl:with-param name="StartPos" select="$amtColSpacer"/>
								<!-- space over to Column2 start pos -->
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:for-each>
					</xsl:for-each>
					<!-- Court Disposition Section -->
					<xsl:for-each select="rap:CourtAction">
						<xsl:variable name="linkid">
							<xsl:value-of select="./@s:id"/>
						</xsl:variable>
						<xsl:text>------------------------------------------------------------------------&#10;</xsl:text>
						<xsl:text>Court Disposition       (Cycle </xsl:text>
						<xsl:value-of select="format-number($Cycle-number,'#000')"/>
						<xsl:text>)</xsl:text>
						<xsl:text>&#10;</xsl:text>
						<!--ADD-->
						<xsl:if test="./nc:ActivityIdentification/nc:IdentificationID">
							<xsl:text>Court Action ID</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="./nc:ActivityIdentification/nc:IdentificationID"/>
								<xsl:with-param name="StartPos" select="$posCol2 - 22"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:if test="./nc:ActivityIdentification/nc:IdentificationJurisdictionText">
							<xsl:text>Court Action ID Jurisdiction</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="./nc:ActivityIdentification/nc:IdentificationJurisdictionText"/>
								<xsl:with-param name="StartPos" select="$posCol2 - 22"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<!--ADD-->
						<xsl:for-each select="rap:CourtRecordIdentification/nc:IdentificationID">
							<xsl:text>Court Case Number</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="."/>
								<xsl:with-param name="StartPos" select="$posCol2 - 17"/>
								<!-- space over to Column2 start pos -->
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:for-each>
						<xsl:if test="nc:ActivityDate/nc:Date">
							<xsl:text>Final Disposition Date</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="nc:ActivityDate/nc:Date"/>
								<xsl:with-param name="StartPos" select="$posCol2 - 22"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:if test="//rap:CourtActionCourtAssociation[nc:ActivityReference/@s:ref=$linkid]">
							<xsl:variable name="agencyid">
								<xsl:value-of select="//rap:CourtActionCourtAssociation[nc:ActivityReference/@s:ref=$linkid]/j:CourtReference/@s:ref"/>
							</xsl:variable>
							<xsl:text>Court Agency</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="concat(//rap:Court[@s:id=$agencyid]/j:OrganizationAugmentation/j:OrganizationORIIdentification/nc:IdentificationID,' ',//rap:Court[@s:id=$agencyid]/nc:OrganizationName)"/>
								<xsl:with-param name="StartPos" select="$posCol2 - 12"/>
								<!-- space over to Column2 start pos -->
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:if test="./rap:CourtActionSubject/rap:SubjectFullName">
							<xsl:text>Subject's Name</xsl:text>
						</xsl:if>
						<xsl:for-each select="./rap:CourtActionSubject/rap:SubjectFullName">
							<!-- Name -->
							<xsl:variable name="i" select="position()"/>
							<xsl:choose>
								<!-- determine the correct spacing before the write of name data -->
								<xsl:when test="$i = 1">
									<xsl:call-template name="spaceover">
										<xsl:with-param name="amount" select="$posCol2 - 14"/>
										<xsl:with-param name="amountWrote" select="0"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="spaceover">
										<xsl:with-param name="amount" select="$posCol2"/>
										<xsl:with-param name="amountWrote" select="0"/>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:value-of select="."/>
							<xsl:text>&#10;</xsl:text>
						</xsl:for-each>
						<xsl:for-each select="./j:CourtCharge">
							<xsl:call-template name="PrintCharge">
								<xsl:with-param name="Charge" select="."/>
							</xsl:call-template>
						</xsl:for-each>
						<xsl:for-each select="./rap:CourtCharge">
							<xsl:call-template name="PrintCharge">
								<xsl:with-param name="Charge" select="."/>
							</xsl:call-template>
						</xsl:for-each>
						<xsl:for-each select="rap:CourtCaseStatusText">
							<xsl:text>Court Case Status</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="."/>
								<xsl:with-param name="StartPos" select="$posCol2 - 17"/>
								<!-- space over to Column2 start pos -->
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:for-each>
						<xsl:for-each select="nc:ActivityDescriptionText">
							<xsl:variable name="i" select="position()"/>
							<xsl:text>    Court Comment</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="normalize-space(.)"/>
								<xsl:with-param name="StartPos" select="$amtColSpacer"/>
								<!-- space over to Column2 start pos -->
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:for-each>
					</xsl:for-each>
					<!-- Sentencing Section -->
					<xsl:for-each select="ch-ext:Sentencing">
						<xsl:variable name="linkid">
							<xsl:value-of select="./@s:id"/>
						</xsl:variable>
						<xsl:text>------------------------------------------------------------------------&#10;</xsl:text>
						<xsl:text>Sentencing              (Cycle </xsl:text>
						<xsl:value-of select="format-number($Cycle-number,'#000')"/>
						<xsl:text>)</xsl:text>
						<xsl:text>&#10;</xsl:text>
						<xsl:if test="./nc:ActivityDate/nc:Date">
							<xsl:text>Sentence Date</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="./nc:ActivityDate/nc:Date"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (13)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:if test="./ch-ext:Sentence/nc:ActivityDisposition/nc:DispositionDescriptionText">
							<xsl:text>Sentence Disposition</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="./ch-ext:Sentence/nc:ActivityDisposition/nc:DispositionDescriptionText"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (13)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<!-- -->
						<xsl:if test="//ch-ext:SentencingCourtAssociation[nc:ActivityReference/@s:ref=$linkid]">
							<xsl:variable name="agencyid">
								<xsl:value-of select="//ch-ext:SentencingCourtAssociation[nc:ActivityReference/@s:ref=$linkid]/j:CourtReference/@s:ref"/>
							</xsl:variable>
							<xsl:text>Sentencing Agency</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="concat(//rap:Court[@s:id=$agencyid]/j:OrganizationAugmentation/j:OrganizationORIIdentification/nc:IdentificationID,' ',//rap:Court[@s:id=$agencyid]/nc:OrganizationName)"/>
								<xsl:with-param name="StartPos" select="$posCol2 - 17"/>
								<!-- space over to Column2 start pos -->
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:if test="rap:SentencingCourtRecordIdentification">
							<xsl:text>Court Case Number</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="rap:SentencingCourtRecordIdentification/nc:IdentificationID"/>
								<xsl:with-param name="StartPos" select="$posCol2 - 17"/>
								<!-- space over to Column2 start pos -->
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<!-- -->
						<xsl:for-each select="./ch-ext:Sentence">
							<!-- Loop through the Sentencings -->
							<!-- -->
							<xsl:for-each select="j:SentenceCharge">
								<xsl:call-template name="PrintCharge">
									<xsl:with-param name="Charge" select="."/>
								</xsl:call-template>
							</xsl:for-each>
							<xsl:for-each select="rap:SentenceCharge">
								<xsl:call-template name="PrintCharge">
									<xsl:with-param name="Charge" select="."/>
								</xsl:call-template>
							</xsl:for-each>
							<xsl:for-each select="j:SupervisionAssignedTerm">
								<xsl:value-of select="nc:ActivityDescriptionText"/>
								<xsl:call-template name="wrapIn">
									<xsl:with-param name="Text" select="concat(substring(j:TermMinimumDuration,2),' to ',substring(j:TermMaximumDuration,2))"/>
									<xsl:with-param name="StartPos" select="$posCol2 - string-length(nc:ActivityDescriptionText)"/>
									<!-- space over to Column2 start pos -->
									<xsl:with-param name="EndPos" select="$posMaxWidth"/>
									<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
									<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
									<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
									<xsl:with-param name="initialRun" select="0"/>
								</xsl:call-template>
								<xsl:text>&#10;</xsl:text>
							</xsl:for-each>
							<xsl:for-each select="rap:CourtCostAmount">
								<xsl:text>Court Cost</xsl:text>
								<xsl:call-template name="wrapIn">
									<xsl:with-param name="Text" select="."/>
									<xsl:with-param name="StartPos" select="$posCol2 - 10"/>
									<!-- space over to Column2 start pos -->
									<xsl:with-param name="EndPos" select="$posMaxWidth"/>
									<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
									<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
									<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
									<xsl:with-param name="initialRun" select="0"/>
								</xsl:call-template>
								<xsl:text>&#10;</xsl:text>
							</xsl:for-each>
							<xsl:for-each select="rap:SupervisionFineAmount">
								<xsl:text>Fine Amount</xsl:text>
								<xsl:call-template name="wrapIn">
									<xsl:with-param name="Text" select="."/>
									<xsl:with-param name="StartPos" select="$posCol2 - 11"/>
									<!-- space over to Column2 start pos -->
									<xsl:with-param name="EndPos" select="$posMaxWidth"/>
									<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
									<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
									<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
									<xsl:with-param name="initialRun" select="0"/>
								</xsl:call-template>
								<xsl:text>&#10;</xsl:text>
							</xsl:for-each>
							<xsl:for-each select="j:SupervisionDisciplinaryAction/nc:DisciplinaryActionRestitution/nc:ObligationTotalAmount">
								<xsl:text>Restitution</xsl:text>
								<xsl:call-template name="wrapIn">
									<xsl:with-param name="Text" select="."/>
									<xsl:with-param name="StartPos" select="$posCol2 - 11"/>
									<!-- space over to Column2 start pos -->
									<xsl:with-param name="EndPos" select="$posMaxWidth"/>
									<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
									<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
									<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
									<xsl:with-param name="initialRun" select="0"/>
								</xsl:call-template>
								<xsl:text>&#10;</xsl:text>
							</xsl:for-each>
							<xsl:choose>
								<xsl:when test="count(j:SentenceDescriptionText) &lt; 1">
									<xsl:text>&#10;</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:for-each select="j:SentenceDescriptionText">
										<xsl:text>Sentence</xsl:text>
										<xsl:call-template name="wrapIn">
											<xsl:with-param name="Text" select="."/>
											<xsl:with-param name="StartPos" select="$posCol2 - 8"/>
											<!-- space over to Column2 start pos -->
											<xsl:with-param name="EndPos" select="$posMaxWidth"/>
											<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
											<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
											<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
											<xsl:with-param name="initialRun" select="0"/>
										</xsl:call-template>
										<xsl:text>&#10;</xsl:text>
									</xsl:for-each>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:for-each>
						<!-- End of Sentencing Loop -->
					</xsl:for-each>
					<!-- Corrections Section -->
					<xsl:choose>
						<xsl:when test="count(rap:Supervision) &gt; 0">
							<xsl:text>&#10;------------------------------------------------------------------------&#10;</xsl:text>
							<xsl:text>Corrections             (Cycle </xsl:text>
							<xsl:value-of select="format-number($Cycle-number,'#000')"/>
							<xsl:text>)</xsl:text>
							<xsl:text>&#10;</xsl:text>
						</xsl:when>
					</xsl:choose>
					<xsl:for-each select="rap:Supervision">
						<!-- Loop through the Corrections -->
						<xsl:variable name="linkid">
							<xsl:value-of select="./@s:id"/>
						</xsl:variable>
						<xsl:if test="nc:ActivityDate/nc:Date">
							<xsl:text>Supervision Date</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="nc:ActivityDate/nc:Date"/>
								<xsl:with-param name="StartPos" select="$posCol2 - 16"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:if test="//rap:SupervisionAgencyAssociation[rap:SupervisionReference/@s:ref=$linkid]">
							<xsl:variable name="agencyid">
								<xsl:value-of select="//rap:SupervisionAgencyAssociation[rap:SupervisionReference/@s:ref=$linkid]/nc:OrganizationReference/@s:ref"/>
							</xsl:variable>
							<xsl:text>Corrections Agency</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="concat(//rap:Agency[@s:id=$agencyid]/j:OrganizationAugmentation/j:OrganizationORIIdentification/nc:IdentificationID,' ',//rap:Agency[@s:id=$agencyid]/nc:OrganizationName)"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (18)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<!-- -->
						<xsl:if test="rap:SupervisionSubject/rap:SubjectFullName">
							<xsl:text>Subject's Name</xsl:text>
						</xsl:if>
						<!-- -->
						<xsl:for-each select="rap:SupervisionSubject/rap:SubjectFullName">
							<!-- Name -->
							<xsl:variable name="i" select="position()"/>
							<xsl:choose>
								<!-- determine the correct spacing before the write of name data -->
								<xsl:when test="$i = 1">
									<xsl:call-template name="spaceover">
										<xsl:with-param name="amount" select="$posCol2 - 14"/>
										<xsl:with-param name="amountWrote" select="0"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="spaceover">
										<xsl:with-param name="amount" select="$posCol2"/>
										<xsl:with-param name="amountWrote" select="0"/>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:value-of select="."/>
							<xsl:text>&#10;</xsl:text>
						</xsl:for-each>
						<xsl:if test="not(rap:SupervisionSubject/rap:SubjectFullName)">
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:for-each select="rap:SupervisionSubject/j:SubjectIdentification">
							<xsl:text>Correctional Id Number</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="concat(nc:IdentificationID, ' ', nc:IdentificationSourceText)"/>
								<xsl:with-param name="StartPos" select="$amtColSpacer"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:for-each>
						<!-- -->
						<!-- kjs -->
						<xsl:if test="rap:SupervisionAgencyRecordIdentification/nc:IdentificationID">
							<xsl:text>      Supervision Case Number</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="rap:SupervisionAgencyRecordIdentification/nc:IdentificationID"/>
								<xsl:with-param name="StartPos" select="$amtColSpacer"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:if test="rap:SupervisionCourtRecordIdentification/nc:IdentificationID">
							<xsl:text>      Court Case Number</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="rap:SupervisionCourtRecordIdentification/nc:IdentificationID"/>
								<xsl:with-param name="StartPos" select="$amtColSpacer"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<!-- -->
						<xsl:for-each select="nc:SupervisionCustodyStatus">
							<xsl:variable name="i" select="position()"/>
							<xsl:text>     Correction Action</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="nc:StatusDescriptionText"/>
								<xsl:with-param name="StartPos" select="$amtColSpacer"/>
								<!-- space over to Column2 start pos -->
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:for-each>
						<xsl:for-each select="rap:SupervisionCharge">
							<xsl:call-template name="PrintCharge">
								<xsl:with-param name="Charge" select="."/>
							</xsl:call-template>
						</xsl:for-each>
						<xsl:for-each select="nc:ActivityDescriptionText">
							<xsl:variable name="i" select="position()"/>
							<xsl:text>    Correction Comment</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="normalize-space(.)"/>
								<xsl:with-param name="StartPos" select="$amtColSpacer"/>
								<!-- space over to Column2 start pos -->
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:for-each>
						<xsl:for-each select="nc:ActivityCategoryText">
							<xsl:variable name="i" select="position()"/>
							<xsl:text>Sentence Type         </xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="normalize-space(.)"/>
								<xsl:with-param name="StartPos" select="$amtColSpacer"/>
								<!-- space over to Column2 start pos -->
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:for-each>
						<xsl:for-each select="j:ActivityReasonText">
							<xsl:variable name="i" select="position()"/>
							<xsl:text>Charge Description    </xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="normalize-space(.)"/>
								<xsl:with-param name="StartPos" select="$amtColSpacer"/>
								<!-- space over to Column2 start pos -->
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:for-each>
						<xsl:for-each select="nc:SupervisionRelease">
							<xsl:variable name="i" select="position()"/>
							<!-- 										<xsl:text>Release Type          </xsl:text>
										<xsl:call-template name="wrapIn">
											<xsl:with-param name="Text" select="."/>
											<xsl:with-param name="StartPos" select="$amtColSpacer"/>
											space over to Column2 start pos
											<xsl:with-param name="EndPos" select="$posMaxWidth"/>
											<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
											<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
											<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
											<xsl:with-param name="initialRun" select="0"/>
										</xsl:call-template>
										<xsl:text>&#10;</xsl:text>
 -->
							<xsl:text>Release Date          </xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="normalize-space(./nc:ActivityDate/nc:Date)"/>
								<xsl:with-param name="StartPos" select="$amtColSpacer"/>
								<!-- space over to Column2 start pos -->
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:for-each>
						<xsl:for-each select="j:SupervisionConditionalRelease">
							<xsl:variable name="i" select="position()"/>
							<xsl:text>Cond. Release Type    </xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="normalize-space(./nc:ActivityCategoryText)"/>
								<xsl:with-param name="StartPos" select="$amtColSpacer"/>
								<!-- space over to Column2 start pos -->
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
							<xsl:text>Cond. Release Date    </xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="normalize-space(./nc:ActivityDate/nc:Date)"/>
								<xsl:with-param name="StartPos" select="$amtColSpacer"/>
								<!-- space over to Column2 start pos -->
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:for-each>
					</xsl:for-each>
					<!-- End of Correction Loop -->
					<!-- Post Sentence Action -->
					<xsl:for-each select="rap:PostSentenceAction">
						<xsl:variable name="linkid">
							<xsl:value-of select="./@s:id"/>
						</xsl:variable>
						<xsl:text>------------------------------------------------------------------------&#10;</xsl:text>
						<xsl:text>Post Sentence Action       (Cycle </xsl:text>
						<xsl:value-of select="format-number($Cycle-number,'#000')"/>
						<xsl:text>)</xsl:text>
						<xsl:text>&#10;</xsl:text>
						<xsl:if test="nc:ActivityDate/nc:Date">
							<xsl:text>Post Sentence Event Date</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="nc:ActivityDate/nc:Date"/>
								<xsl:with-param name="StartPos" select="$posCol2 - 25"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:if test="//rap:PostSentenceActionAgencyAssociation[nc:ActivityReference/@s:ref=$linkid]">
							<xsl:variable name="agencyid">
								<xsl:value-of select="//rap:PostSentenceActionAgencyAssociation[nc:ActivityReference/@s:ref=$linkid]/nc:AgencyReference/@s:ref"/>
							</xsl:variable>
							<xsl:text>Agency</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="concat(//rap:Agency[@s:id=$agencyid]/j:OrganizationAugmentation/j:OrganizationORIIdentification/nc:IdentificationID,' ',//rap:Agency[@s:id=$agencyid]/nc:OrganizationName)"/>
								<xsl:with-param name="StartPos" select="$posCol2 - 6"/>
								<!-- space over to Column2 start pos -->
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:for-each select="rap:PostSentenceEventText">
							<xsl:text>Post Sentence Event</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="."/>
								<xsl:with-param name="StartPos" select="$posCol2 - 19"/>
								<!-- space over to Column2 start pos -->
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:for-each>
						<xsl:for-each select="nc:ActivityDescriptionText">
							<xsl:variable name="i" select="position()"/>
							<xsl:text>Post Sentence Description</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="normalize-space(.)"/>
								<xsl:with-param name="StartPos" select="$amtColSpacer"/>
								<!-- space over to Column2 start pos -->
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:for-each>
					</xsl:for-each>
				</xsl:for-each>
				<!-- End of the Cycle Loop -->
				<xsl:if test="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetNonCriminalInformation">
					<xsl:text>&#10;**********************  NON CRIMINAL INFORMATION  **********************&#10;&#10;</xsl:text>
				</xsl:if>
				<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetNonCriminalInformation">
					<xsl:for-each select="rap:FingerprintActivity">
						<!-- Print Non Criminal Info -->
						<xsl:text>&#10;------------------------------------------------------------------------&#10;</xsl:text>
						<xsl:variable name="linkid">
							<xsl:value-of select="./@s:id"/>
						</xsl:variable>
						<xsl:if test="./nc:ActivityDate/nc:Date">
							<xsl:text>Fingerprint Date</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="./nc:ActivityDate/nc:Date"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (16)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<!-- -->
						<xsl:if test="nc:ActivityDescriptionText">
							<xsl:text>Description</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="nc:ActivityDescriptionText"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (11)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<!-- -->
						<xsl:if test="nc:ActivityReasonText">
							<xsl:text>Fingerprint Reason</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="nc:ActivityReasonText"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (17)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<!-- -->
						<xsl:if test="//rap:FingerprintActivityAgencyAssociation[nc:ActivityReference/@s:ref=$linkid]">
							<xsl:variable name="agencyid">
								<xsl:value-of select="//rap:FingerprintActivityAgencyAssociation[nc:ActivityReference/@s:ref=$linkid]/nc:OrganizationReference/@s:ref"/>
							</xsl:variable>
							<xsl:text>Fingerprinting Agency</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="concat(//rap:Agency[@s:id=$agencyid]/j:OrganizationAugmentation/j:OrganizationORIIdentification/nc:IdentificationID,' ',//rap:Agency[@s:id=$agencyid]/nc:OrganizationName)"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (21)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
					</xsl:for-each>
				</xsl:for-each>
				<!-- ########## Index of Agencies ########## -->
				<xsl:text>*************************  INDEX OF AGENCIES  **************************&#10;&#10;</xsl:text>
				<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Agency">
					<xsl:text>Agency</xsl:text>
					<xsl:call-template name="wrapIn">
						<xsl:with-param name="Text" select="concat(nc:OrganizationName,'; ',j:OrganizationAugmentation/j:OrganizationORIIdentification/nc:IdentificationID,';')"/>
						<xsl:with-param name="StartPos" select="$posCol2 - 6"/>
						<!-- space over to Column2 start pos -->
						<xsl:with-param name="EndPos" select="$posMaxWidth"/>
						<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
						<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
						<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
						<xsl:with-param name="initialRun" select="0"/>
					</xsl:call-template>
					<xsl:text>&#10;</xsl:text>
					<xsl:if test="count(nc:OrganizationSubUnitName) &gt; 0">
						<xsl:text>Contact                 </xsl:text>
						<xsl:value-of select="nc:OrganizationSubUnitName"/>
						<xsl:text> Department</xsl:text>
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
					<xsl:for-each select="nc:OrganizationLocation/nc:LocationContactInformation/nc:ContactInformationDescriptionText">
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="."/>
							<xsl:with-param name="StartPos" select="$posCol2"/>
							<!-- space over to Column2 start pos -->
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:for-each>
					<!-- Telephone -->
					<!-- if there is a telephone number, write it -->
					<xsl:for-each select="nc:OrganizationLocation/nc:LocationContactInformation/nc:ContactTelephoneNumber/nc:FullTelephoneNumber">
						<xsl:text>Agency Telephone</xsl:text>
						<xsl:choose>
							<xsl:when test="count(.) &gt; 0">
								<xsl:choose>
									<xsl:when test="nc:TelephoneSuffixID">
										<xsl:call-template name="wrapIn">
											<xsl:with-param name="Text" select="concat(nc:TelephoneNumberFullID, ' x', nc:TelephoneSuffixID)"/>
											<xsl:with-param name="StartPos" select="$posCol2 - 16"/>
											<xsl:with-param name="EndPos" select="$posMaxWidth"/>
											<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
											<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
											<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
											<xsl:with-param name="initialRun" select="0"/>
											<!-- 0=True -->
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:call-template name="wrapIn">
											<xsl:with-param name="Text" select="nc:TelephoneNumberFullID"/>
											<xsl:with-param name="StartPos" select="$posCol2 - 16"/>
											<xsl:with-param name="EndPos" select="$posMaxWidth"/>
											<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
											<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
											<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
											<xsl:with-param name="initialRun" select="0"/>
											<!-- 0=True -->
										</xsl:call-template>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<!--						<xsl:otherwise>
								<xsl:call-template name="wrapIn">
									<xsl:with-param name="Text" select="."/>
									<xsl:with-param name="StartPos" select="$posCol2 - 16"/>
									<xsl:with-param name="EndPos" select="$posMaxWidth"/>
									<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
									<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
									<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
									<xsl:with-param name="initialRun" select="0"/>
								</xsl:call-template>
							</xsl:otherwise>-->
						</xsl:choose>
						<xsl:text>&#10;</xsl:text>
					</xsl:for-each>
					<!-- FAX -->
					<!-- if there is a telephone number, write it -->
					<xsl:for-each select="nc:OrganizationLocation/nc:LocationContactInformation/nc:ContactFaxNumber/nc:FullTelephoneNumber">
						<xsl:text>Agency Facsimile</xsl:text>
						<xsl:choose>
							<xsl:when test="count(.) &gt; 0">
								<xsl:choose>
									<xsl:when test="nc:TelephoneSuffixID">
										<xsl:call-template name="wrapIn">
											<xsl:with-param name="Text" select="concat(nc:TelephoneNumberFullID, ' x', nc:TelephoneSuffixID)"/>
											<xsl:with-param name="StartPos" select="$posCol2 - 16"/>
											<xsl:with-param name="EndPos" select="$posMaxWidth"/>
											<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
											<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
											<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
											<xsl:with-param name="initialRun" select="0"/>
											<!-- 0=True -->
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:call-template name="wrapIn">
											<xsl:with-param name="Text" select="nc:TelephoneNumberFullID"/>
											<xsl:with-param name="StartPos" select="$posCol2 - 16"/>
											<xsl:with-param name="EndPos" select="$posMaxWidth"/>
											<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
											<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
											<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
											<xsl:with-param name="initialRun" select="0"/>
											<!-- 0=True -->
										</xsl:call-template>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<!--	<xsl:otherwise>
								<xsl:call-template name="wrapIn">
									<xsl:with-param name="Text" select="."/>
									<xsl:with-param name="StartPos" select="$posCol2 - 16"/>
									<xsl:with-param name="EndPos" select="$posMaxWidth"/>
									<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
									<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
									<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
									<xsl:with-param name="initialRun" select="0"/>
								</xsl:call-template>
							</xsl:otherwise>-->
						</xsl:choose>
						<xsl:text>&#10;</xsl:text>
					</xsl:for-each>
					<xsl:for-each select="nc:OrganizationLocation/nc:LocationContactInformation/nc:ContactEmailID">
						<xsl:variable name="i" select="position()"/>
						<xsl:text>Agency Email Address</xsl:text>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="."/>
							<xsl:with-param name="StartPos" select="$posCol2 - 20"/>
							<!-- space over to Column2 start pos -->
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:for-each>
					<!-- xsl:if test="count(email) &lt; 1"><xsl:text>&#10;</xsl:text></xsl:if -->
					<!-- Address -->
					<xsl:for-each select="nc:OrganizationLocation">
						<xsl:if test="nc:LocationAddress">
							<xsl:text>Address</xsl:text>
						</xsl:if>
						<xsl:if test="nc:LocationAddress/nc:AddressFullText">
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="nc:LocationAddress/nc:AddressFullText"/>
								<xsl:with-param name="StartPos" select="$posCol2 - 7"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
						</xsl:if>
						<xsl:text>&#10;</xsl:text>
						<xsl:if test="./nc:LocationAddress/nc:BuildingName">
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="./nc:LocationAddress/nc:BuildingName"/>
								<xsl:with-param name="StartPos" select="$posCol2 - 7"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
						</xsl:if>
						<xsl:text>&#10;</xsl:text>
						<xsl:if test="./nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet/nc:StreetFullText">
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="./nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet/nc:StreetFullText"/>
								<xsl:with-param name="StartPos" select="$posCol2"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:if test="count(./nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet/nc:StreetFullText) = 0">
							<xsl:if test="./nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet/nc:StreetName">
								<xsl:call-template name="wrapIn">
									<xsl:with-param name="Text" select="concat(./nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet/nc:StreetNumberText,' ',./nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet/nc:StreetPredirectionalText,' ',./nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet/nc:StreetName,' ',./nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet/nc:StreetTypeText)"/>
									<xsl:with-param name="StartPos" select="$posCol2"/>
									<xsl:with-param name="EndPos" select="$posMaxWidth"/>
									<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
									<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
									<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
									<xsl:with-param name="initialRun" select="0"/>
								</xsl:call-template>
								<xsl:text>&#10;</xsl:text>
							</xsl:if>
						</xsl:if>
						<xsl:if test="./nc:LocationAddress/nc:StructuredAddress/nc:LocationStateName">
							<xsl:choose>
								<xsl:when test="./nc:LocationAddress/nc:StructuredAddress/nc:LocationPostalExtensionCode">
									<xsl:call-template name="wrapIn">
										<xsl:with-param name="Text" select="concat(./nc:LocationAddress/nc:StructuredAddress/nc:LocationCityName,', ',./nc:LocationAddress/nc:StructuredAddress/nc:LocationStateName,' ',./nc:LocationAddress/nc:StructuredAddress/nc:LocationPostalCode,'-',nc:LocationAddress/nc:StructuredAddress/nc:LocationPostalExtensionCode, ' ',./nc:LocationAddress/nc:StructuredAddress/nc:LocationCountryText)"/>
										<xsl:with-param name="StartPos" select="$posCol2"/>
										<xsl:with-param name="EndPos" select="$posMaxWidth"/>
										<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
										<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
										<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
										<xsl:with-param name="initialRun" select="0"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="wrapIn">
										<xsl:with-param name="Text" select="concat(./nc:LocationAddress/nc:StructuredAddress/nc:LocationCityName,', ',./nc:LocationAddress/nc:StructuredAddress/nc:LocationStateName,' ',./nc:LocationAddress/nc:StructuredAddress/nc:LocationPostalCode, ' ',./nc:LocationAddress/nc:StructuredAddress/nc:LocationCountryText)"/>
										<xsl:with-param name="StartPos" select="$posCol2"/>
										<xsl:with-param name="EndPos" select="$posMaxWidth"/>
										<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
										<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
										<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
										<xsl:with-param name="initialRun" select="0"/>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:if test="./nc:LocationAddress/nc:StructuredAddress/nc:LocationCountyName">
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="concat('County:  ',./nc:LocationAddress/nc:StructuredAddress/nc:LocationCountyName)"/>
								<xsl:with-param name="StartPos" select="$posCol2"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
					</xsl:for-each>
					<xsl:text>&#10;</xsl:text>
					<xsl:choose>
						<xsl:when test="position() != last()">
							<xsl:text>------------------------------------------------------------------------&#10;</xsl:text>
						</xsl:when>
						<xsl:when test="count(//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Court) > 0">
							<xsl:text>------------------------------------------------------------------------&#10;</xsl:text>
						</xsl:when>
					</xsl:choose>
				</xsl:for-each>
				<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Court">
					<xsl:text>Agency</xsl:text>
					<xsl:call-template name="wrapIn">
						<xsl:with-param name="Text" select="concat(nc:OrganizationName,'; ',j:OrganizationAugmentation/j:OrganizationORIIdentification/nc:IdentificationID,';')"/>
						<xsl:with-param name="StartPos" select="$posCol2 - 6"/>
						<!-- space over to Column2 start pos -->
						<xsl:with-param name="EndPos" select="$posMaxWidth"/>
						<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
						<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
						<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
						<xsl:with-param name="initialRun" select="0"/>
					</xsl:call-template>
					<xsl:text>&#10;</xsl:text>
					<xsl:if test="count(nc:OrganizationSubUnitName) &gt; 0">
						<xsl:text>Contact                 </xsl:text>
						<xsl:value-of select="nc:OrganizationSubUnitName"/>
						<xsl:text> Department</xsl:text>
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
					<xsl:for-each select="nc:OrganizationLocation/nc:LocationContactInformation/nc:ContactInformationDescriptionText">
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="."/>
							<xsl:with-param name="StartPos" select="$posCol2"/>
							<!-- space over to Column2 start pos -->
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:for-each>
					<!-- Telephone -->
					<xsl:choose>
						<xsl:when test="nc:OrganizationLocation/nc:LocationContactInformation/nc:ContactTelephoneNumber/nc:FullTelephoneNumber &gt; 0">
							<!-- if there is a telephone number, write it -->
							<xsl:for-each select="nc:OrganizationLocation/nc:LocationContactInformation/nc:ContactTelephoneNumber/nc:FullTelephoneNumber">
								<xsl:text>Agency Telephone</xsl:text>
								<xsl:choose>
									<xsl:when test="count(.) &gt; 0">
										<xsl:choose>
											<xsl:when test="nc:TelephoneSuffixID">
												<xsl:call-template name="wrapIn">
													<xsl:with-param name="Text" select="concat(nc:TelephoneNumberFullID, ' x', nc:TelephoneSuffixID)"/>
													<xsl:with-param name="StartPos" select="$posCol2 - 16"/>
													<xsl:with-param name="EndPos" select="$posMaxWidth"/>
													<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
													<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
													<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
													<xsl:with-param name="initialRun" select="0"/>
													<!-- 0=True -->
												</xsl:call-template>
											</xsl:when>
											<xsl:otherwise>
												<xsl:call-template name="wrapIn">
													<xsl:with-param name="Text" select="nc:TelephoneNumberFullID"/>
													<xsl:with-param name="StartPos" select="$posCol2 - 16"/>
													<xsl:with-param name="EndPos" select="$posMaxWidth"/>
													<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
													<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
													<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
													<xsl:with-param name="initialRun" select="0"/>
													<!-- 0=True -->
												</xsl:call-template>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:when>
									<!--	<xsl:otherwise>
										<xsl:call-template name="wrapIn">
											<xsl:with-param name="Text" select="."/>
											<xsl:with-param name="StartPos" select="$posCol2 - 16"/>
											<xsl:with-param name="EndPos" select="$posMaxWidth"/>
											<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
											<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
											<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
											<xsl:with-param name="initialRun" select="0"/>
										</xsl:call-template>
									</xsl:otherwise>-->
								</xsl:choose>
								<xsl:text>&#10;</xsl:text>
							</xsl:for-each>
						</xsl:when>
						<xsl:otherwise>
							<!--  xsl:text>&#10;</xsl:text -->
						</xsl:otherwise>
					</xsl:choose>
					<!-- FAX -->
					<xsl:choose>
						<xsl:when test="count(nc:OrganizationLocation/nc:LocationContactInformation/nc:ContactFaxNumber/nc:FullTelephoneNumber) &gt; 0">
							<!-- if there is a telephone number, write it -->
							<xsl:for-each select="nc:OrganizationLocation/nc:LocationContactInformation/nc:ContactFaxNumber/nc:FullTelephoneNumber">
								<xsl:text>Agency Facsimile</xsl:text>
								<xsl:choose>
									<xsl:when test="count(.) &gt; 0">
										<xsl:choose>
											<xsl:when test="nc:TelephoneSuffixID">
												<xsl:call-template name="wrapIn">
													<xsl:with-param name="Text" select="concat(nc:TelephoneNumberFullID, ' x', nc:TelephoneSuffixID)"/>
													<xsl:with-param name="StartPos" select="$posCol2 - 16"/>
													<xsl:with-param name="EndPos" select="$posMaxWidth"/>
													<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
													<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
													<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
													<xsl:with-param name="initialRun" select="0"/>
													<!-- 0=True -->
												</xsl:call-template>
											</xsl:when>
											<xsl:otherwise>
												<xsl:call-template name="wrapIn">
													<xsl:with-param name="Text" select="nc:TelephoneNumberFullID"/>
													<xsl:with-param name="StartPos" select="$posCol2 - 16"/>
													<xsl:with-param name="EndPos" select="$posMaxWidth"/>
													<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
													<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
													<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
													<xsl:with-param name="initialRun" select="0"/>
													<!-- 0=True -->
												</xsl:call-template>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:when>
									<!--			<xsl:otherwise>
										<xsl:call-template name="wrapIn">
											<xsl:with-param name="Text" select="."/>
											<xsl:with-param name="StartPos" select="$posCol2 - 16"/>
											<xsl:with-param name="EndPos" select="$posMaxWidth"/>
											<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
											<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
											<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
											<xsl:with-param name="initialRun" select="0"/>
										</xsl:call-template>
									</xsl:otherwise>-->
								</xsl:choose>
								<xsl:text>&#10;</xsl:text>
							</xsl:for-each>
						</xsl:when>
						<xsl:otherwise>
							<!-- xsl:text>&#10;</xsl:text -->
						</xsl:otherwise>
					</xsl:choose>
					<xsl:for-each select="nc:OrganizationLocation/nc:LocationContactInformation/nc:ContactEmailID">
						<xsl:variable name="i" select="position()"/>
						<xsl:text>Agency Email Address</xsl:text>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="."/>
							<xsl:with-param name="StartPos" select="$posCol2 - 20"/>
							<!-- space over to Column2 start pos -->
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:for-each>
					<!-- xsl:if test="count(email) &lt; 1"><xsl:text>&#10;</xsl:text></xsl:if -->
					<!-- Address -->
					<xsl:for-each select="./nc:OrganizationLocation">
						<xsl:if test="nc:LocationAddress">
							<xsl:text>Address</xsl:text>
						</xsl:if>
						<xsl:if test="./nc:LocationAddress/nc:AddressFullText">
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="./nc:LocationAddress/nc:AddressFullText"/>
								<xsl:with-param name="StartPos" select="$posCol2 - 7"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
						</xsl:if>
						<xsl:if test="./nc:LocationAddress/nc:BuildingName">
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="./nc:LocationAddress/nc:BuildingName"/>
								<xsl:with-param name="StartPos" select="$posCol2 - 7"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
						</xsl:if>
						<xsl:text>&#10;</xsl:text>
						<xsl:if test="./nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet/nc:StreetFullText">
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="./nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet/nc:StreetFullText"/>
								<xsl:with-param name="StartPos" select="$posCol2"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:if test="count(./nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet/nc:StreetFullText) = 0">
							<xsl:if test="./nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet/nc:StreetName">
								<xsl:call-template name="wrapIn">
									<xsl:with-param name="Text" select="concat(./nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet/nc:StreetNumberText,' ',./nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet/nc:StreetPredirectionalText,' ',./nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet/nc:StreetName,' ',./nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet/nc:StreetTypeText)"/>
									<xsl:with-param name="StartPos" select="$posCol2"/>
									<xsl:with-param name="EndPos" select="$posMaxWidth"/>
									<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
									<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
									<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
									<xsl:with-param name="initialRun" select="0"/>
								</xsl:call-template>
								<xsl:text>&#10;</xsl:text>
							</xsl:if>
						</xsl:if>
						<xsl:if test="./nc:LocationAddress/nc:StructuredAddress/nc:LocationStateName">
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="concat(./nc:LocationAddress/nc:StructuredAddress/nc:LocationCityName,', ',./nc:LocationAddress/nc:StructuredAddress/nc:LocationStateName,' ',./nc:LocationAddress/nc:StructuredAddress/nc:LocationPostalCodeID/nc:IdentificationID,' ',./nc:LocationAddress/nc:StructuredAddress/nc:LocationCountryText)"/>
								<xsl:with-param name="StartPos" select="$posCol2"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:if test="./nc:LocationAddress/nc:StructuredAddress/nc:LocationCountyName">
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="concat('County:  ',./nc:LocationAddress/nc:StructuredAddress/nc:LocationCountyName)"/>
								<xsl:with-param name="StartPos" select="$posCol2"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:text>&#10;</xsl:text>
					</xsl:for-each>
					<xsl:text>&#10;</xsl:text>
					<xsl:if test="position() != last()">
						<xsl:text>------------------------------------------------------------------------&#10;</xsl:text>
					</xsl:if>
				</xsl:for-each>
				<!-- End of Agency Index Loop -->
				<!-- Extension Fields -->
				<xsl:if test="//ch-doc:CriminalHistory/ch-ext:RapSheet/ch-ext:Order">
					<xsl:text>**********************  ADDITIONAL INFORMATION  ************************&#10;&#10;</xsl:text>
					<xsl:for-each select="//ch-doc:CriminalHistory/ch-ext:RapSheet/ch-ext:Order">
						<xsl:if test="nc:ActivityIdentification/nc:IdentificationID">
							<xsl:text>Court Order ID</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="normalize-space(./nc:ActivityIdentification/nc:IdentificationID)"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (11)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:if test="nc:ActivityIdentification/nc:IdentificationCategoryText">
							<xsl:text>Court Order ID Type</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="normalize-space(./nc:ActivityIdentification/nc:IdentificationCategoryText)"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (11)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:if test="nc:ActivityIdentification/nc:IdentificationJurisdictionText">
							<xsl:text>Court Order ID Jurisdiction</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="normalize-space(./nc:ActivityIdentification/nc:IdentificationJurisdictionText)"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (11)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:if test="nc:ActivityDate/nc:Date">
							<xsl:text>Court Order Date</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="normalize-space(./nc:ActivityDate/nc:Date)"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (11)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:if test="nc:ActivityDescriptionText">
							<xsl:text>Court Order Description</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="normalize-space(./nc:ActivityDescriptionText)"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (11)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:if test="j:CourtOrderIssuingCourt/nc:OrganizationName">
							<xsl:text>Court Order Issuing Court</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="normalize-space(./j:CourtOrderIssuingCourt/nc:OrganizationName)"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (11)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:if test="j:CourtOrderRequestDate/nc:Date">
							<xsl:text>Court Order Request Date</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="normalize-space(./j:CourtOrderRequestDate/nc:Date)"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (11)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:if test="j:CourtOrderServiceDate/nc:Date">
							<xsl:text>Court Order Service Date</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="normalize-space(./j:CourtOrderServiceDate/nc:Date)"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (11)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:if test="ch-ext:CourtCase/nc:ActivityIdentification/nc:IdentificationID">
							<xsl:text>Court Order Case ID</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="normalize-space(./ch-ext:CourtCase/nc:ActivityIdentification/nc:IdentificationID)"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (11)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:if test="ch-ext:CourtCase/nc:ActivityIdentification/nc:IdentificationCategoryText">
							<xsl:text>Court Order Case ID Type</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="normalize-space(./ch-ext:CourtCase/nc:ActivityIdentification/nc:IdentificationCategoryText)"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (11)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:if test="ch-ext:CourtCase/nc:ActivityIdentification/nc:IdentificationJurisdictionText">
							<xsl:text>Court Order Case ID Jurisdiction</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="normalize-space(./ch-ext:CourtCase/nc:ActivityIdentification/nc:IdentificationJurisdictionText)"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (11)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:if test="ch-ext:CourtCase/nc:ActivityDate/nc:Date">
							<xsl:text>Court Order Case Date</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="normalize-space(./ch-ext:CourtCase/nc:ActivityDate/nc:Date)"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (11)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:if test="ch-ext:CourtCase/nc:ActivityDescriptionText">
							<xsl:text>Court Order Case Description</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="normalize-space(./ch-ext:CourtCase/nc:ActivityDescriptionText)"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (11)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
						<xsl:if test="ch-ext:ProtectionOrderExpirationDate/nc:Date">
							<xsl:text>Protection Order Expiration Date</xsl:text>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="normalize-space(./ch-ext:ProtectionOrderExpirationDate/nc:Date)"/>
								<xsl:with-param name="StartPos" select="$posCol2 - (11)"/>
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:if>
					</xsl:for-each>
				</xsl:if>
				<xsl:text>
				* * * END OF RECORD * * * </xsl:text>
			</xsl:if>
			<xsl:if test="//n:ResponseText">
				<xsl:text>
</xsl:text>
				<xsl:value-of select="//n:ResponseText"/>
			</xsl:if>
			<xsl:if test="//n2:ResponseText">
				<xsl:text>
</xsl:text>
				<xsl:value-of select="//n2:ResponseText"/>
			</xsl:if>
		</OJBCRapSheetTextFormat>
	</xsl:template>
	<!-- ########## TEMPLATES and GLOBALS ########## -->
	<xsl:template name="textwrap">
		<!--
**************************************************************************************************
|| The below template wraps a text string at a specified position.
|| Input:
||   CurrentPos - Hold Position (wrap position on first call) + 1.
||   WrapWidth  - Wrap Width (character position of which to wrap at) + 1.
|| Explanation/Notes:
||   Before calling this template it is recommended you declare a variable
||   (ie WrapAt) and set this variable to the Position of which you want
||   the text string wrapped at.  Next set the input parameters BOTH to
||   the varaible's value plus one (ie WrapAt + 1).
|| Example Call:
||   <xsl:variable name="WrapAt" select="72"/>
||   <xsl:call-template name="textwrap">
||		<xsl:with-param name="Text" select="//ch-doc:CriminalHistory/ch-ext:RapSheet/Introduction/caveat"/>
||		<xsl:with-param name="CurrentPos" select="$WrapAt+1"/>
||		<xsl:with-param name="WrapWidth" select="$WrapAt+1"/>
||	</xsl:call-template>
**************************************************************************************************
-->
		<xsl:param name="Text"/>
		<xsl:param name="CurrentPos"/>
		<xsl:param name="WrapWidth"/>
		<xsl:choose>
			<xsl:when test="$CurrentPos &lt; 1">
				<!-- If CurrentPos = 0 it means the word is longer than the wrap width
		      so output as much as possible on one line, then pass the
		      remainder of the string on for more processing. -->
				<xsl:value-of select="substring($Text, 1, $WrapWidth - 1)"/>
				<xsl:text>&#10;</xsl:text>
				<xsl:call-template name="textwrap">
					<xsl:with-param name="Text" select="substring($Text,$WrapWidth)"/>
					<xsl:with-param name="CurrentPos" select="$WrapWidth"/>
					<xsl:with-param name="WrapWidth" select="$WrapWidth"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="string-length($Text) > ($CurrentPos - 1)">
				<!-- if input string length is greater than 72 wrap it up -->
				<xsl:choose>
					<xsl:when test="substring($Text, $CurrentPos - 1, 1) = ' '">
						<!-- return the string -->
						<xsl:value-of select="substring($Text, 1, $CurrentPos - 1)"/>
						<xsl:text>&#10;</xsl:text>
						<xsl:call-template name="textwrap">
							<xsl:with-param name="Text" select="substring($Text,$CurrentPos)"/>
							<xsl:with-param name="CurrentPos" select="$WrapWidth"/>
							<xsl:with-param name="WrapWidth" select="$WrapWidth"/>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<!-- go back a character and reEvaluate -->
						<xsl:call-template name="textwrap">
							<xsl:with-param name="Text" select="$Text"/>
							<xsl:with-param name="CurrentPos" select="$CurrentPos - 1"/>
							<xsl:with-param name="WrapWidth" select="$WrapWidth"/>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$Text"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="wrapIn">
		<!--
**************************************************************************************************
|| The below template wraps a text string at a specified position given a specified Start and
||  end position.
|| Input:
||   Text           - The inputted text to wrap: NOTE: perform a normalize-string() before to
||                     strip extra spaces and carriage returns.
||   StartPos       - The start position of where the string will start writing from.
||   EndPos         - The end position of where the string will stop writing and begin a wrap.
||   WrapedStartPos - The wrapped start position.  Where to start writing on the wrapped line.
||   CurrentPos     - Current postion (used internally).
||   WrapWidth      - The wrap width.
||   initialRun     - Initial Run (used internally).  Identifies if it is the first run of the
||                     template.  If it is the first run, the amount to space over before writing
||                     can and will be different so this parameter adjusts accordingly.
|| Explanation/Notes:
||   As mentioned in the input section, perform a normalize-string on the inputted text to remove
||    unwanted extra spaces or carriage returns.
|| Example Call:
||   <xsl:call-template name="wrapIn">
||		<xsl:with-param name="Text" select="normalize-space(./example/comments)"/>
||		<xsl:with-param name="StartPos" select="$posCol2"/>
||		<xsl:with-param name="EndPos" select="$posMaxWidth"/>
||		<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
||		<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
||		<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
||		<xsl:with-param name="initialRun" select="0"/>
||	</xsl:call-template>
**************************************************************************************************
-->
		<xsl:param name="Text"/>
		<xsl:param name="StartPos"/>
		<xsl:param name="EndPos"/>
		<xsl:param name="WrapedStartPos"/>
		<xsl:param name="CurrentPos"/>
		<xsl:param name="WrapWidth"/>
		<xsl:param name="initialRun"/>
		<xsl:variable name="l" select="$EndPos - $WrapedStartPos"/>
		<xsl:variable name="n-text" select="normalize-space($Text)"/>
		<xsl:choose>
			<xsl:when test="string-length($Text) &lt;= $l">
				<!-- if passed in string is lt or = the length we can write it -->
				<xsl:choose>
					<!-- Determine the correct spacing before writing -->
					<xsl:when test="$initialRun = 0">
						<!-- 0=True -->
						<xsl:call-template name="spaceover">
							<xsl:with-param name="amount" select="$StartPos"/>
							<!-- use StartPos (adjusted started position) -->
							<xsl:with-param name="amountWrote" select="0"/>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="spaceover">
							<xsl:with-param name="amount" select="$WrapedStartPos"/>
							<!-- if not initial run then it is a wrapped line -->
							<xsl:with-param name="amountWrote" select="0"/>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:value-of select="$Text"/>
				<!-- Write the string -->
			</xsl:when>
			<xsl:otherwise>
				<!-- else wrap the string -->
				<xsl:choose>
					<xsl:when test="$CurrentPos &lt; 1">
						<!-- If CurrentPos = 0 it means the word is longer than the wrap width
				      so output as much as possible on one line, then pass the
				      remainder of the string on for more processing. -->
						<xsl:text>&#10;</xsl:text>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="substring($Text,$WrapWidth)"/>
							<xsl:with-param name="StartPos" select="$StartPos"/>
							<xsl:with-param name="EndPos" select="$EndPos"/>
							<xsl:with-param name="WrapedStartPos" select="$WrapedStartPos"/>
							<xsl:with-param name="CurrentPos" select="$WrapWidth"/>
							<xsl:with-param name="WrapWidth" select="$WrapWidth"/>
							<xsl:with-param name="initialRun" select="1"/>
						</xsl:call-template>
					</xsl:when>
					<xsl:when test="(string-length($Text) + $WrapedStartPos) > ($CurrentPos - 1)">
						<!-- if input string length is greater than 72 wrap it up -->
						<xsl:choose>
							<xsl:when test="substring($Text, (($CurrentPos - 1)- $WrapedStartPos), 1) = ' '">
								<!-- if the last character in the string is a [space] then write the line else move back and retry -->
								<xsl:choose>
									<!-- get the correct spacing -->
									<xsl:when test="$initialRun = 0">
										<!-- 0=True -->
										<xsl:call-template name="spaceover">
											<xsl:with-param name="amount" select="$StartPos"/>
											<!-- use StartPos (adjusted started position) -->
											<xsl:with-param name="amountWrote" select="0"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:call-template name="spaceover">
											<xsl:with-param name="amount" select="$WrapedStartPos"/>
											<!-- if not initial run then it is a wrapped line -->
											<xsl:with-param name="amountWrote" select="0"/>
										</xsl:call-template>
									</xsl:otherwise>
								</xsl:choose>
								<xsl:value-of select="substring($Text, 1, ($CurrentPos - 1) - $WrapedStartPos)"/>
								<xsl:text>&#10;</xsl:text>
								<xsl:call-template name="wrapIn">
									<xsl:with-param name="Text" select="substring($Text,$CurrentPos - $WrapedStartPos)"/>
									<xsl:with-param name="StartPos" select="$StartPos"/>
									<xsl:with-param name="EndPos" select="$EndPos"/>
									<xsl:with-param name="WrapedStartPos" select="$WrapedStartPos"/>
									<xsl:with-param name="CurrentPos" select="$WrapWidth"/>
									<xsl:with-param name="WrapWidth" select="$WrapWidth"/>
									<xsl:with-param name="initialRun" select="1"/>
									<!-- set to 1 as we wrote the above line -->
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<!-- go back a character and reEvaluate -->
								<xsl:call-template name="wrapIn">
									<xsl:with-param name="Text" select="$Text"/>
									<xsl:with-param name="StartPos" select="$StartPos"/>
									<xsl:with-param name="EndPos" select="$EndPos"/>
									<xsl:with-param name="WrapedStartPos" select="$WrapedStartPos"/>
									<xsl:with-param name="CurrentPos" select="$CurrentPos - 1"/>
									<xsl:with-param name="WrapWidth" select="$WrapWidth"/>
									<xsl:with-param name="initialRun" select="$initialRun"/>
									<!-- leave at inputted value as we have not wrote a line -->
								</xsl:call-template>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$Text"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="spaceover">
		<!--
	**************************************************************************************************
	|| Adds a given number of spaces to the output. This template will write out (return) a given 
	||   number of spaces. Chage the amount to the desired number of spaces you wish to write out.
	|| Input:
	||   amount      - Number of spaces to write out.
	||   amountWrote - Hold Variable: Used to signafy amount wrote, start with 0.
	|| Example Call:
	||   <xsl:call-template name="spaceover">
	||	 	<xsl:with-param name="amount" select="10"/>
	||		<xsl:with-param name="amountWrote" select="0"/>
	||	 </xsl:call-template>
	**************************************************************************************************
	-->
		<xsl:param name="amount"/>
		<xsl:param name="amountWrote"/>
		<xsl:if test="$amountWrote &lt; $amount">
			<xsl:text> </xsl:text>
			<xsl:call-template name="spaceover">
				<xsl:with-param name="amount" select="$amount"/>
				<xsl:with-param name="amountWrote" select="$amountWrote + 1"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	<xsl:template name="assignvalue">
		<!--
	**************************************************************************************************
	|| Assigns Value to variable. 
	|| Input:
	||   flag        - variable.
	||   flagvalue   - value.
	|| Example Call:
	||   <xsl:call-template name="assignvalue">
	||	 	<xsl:with-param name="flag" select="0"/>
	||		<xsl:with-param name="flagvalue" select="1"/>
	||	 </xsl:call-template>
	**************************************************************************************************
	-->
		<xsl:param name="flag"/>
		<xsl:param name="flagvalue"/>
		<xsl:if test="$flag &lt; $flagvalue">
			<xsl:call-template name="assignvalue">
				<xsl:with-param name="flag" select="$flag + 1"/>
				<xsl:with-param name="flagvalue" select="$flagvalue"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	<xsl:template name="doAgency">
		<!--
	**************************************************************************************************
	|| Writes the appropriate 'agency' info (ori or entityName).  If future changes require structure
	||   changes to the agency write-out format it can easily be accomodated here.
	|| Input:
	||   agency: entityName, ori, entityAbbreviatedName, and entityAccronym.
	|| Example Call:
	||   <xsl:call-template name="doAgency">
	||		<xsl:with-param name="entityName" select="./agency/entityName"/>
	||		<xsl:with-param name="ori" select="./agency/ori"/>
	||		:with-param name="entityAbbreviatedName" select="./agency/entityAbbreviatedName"/>
	||		<xsl:with-param name="entityAccronym" select="./agency/entityAccronym"/>
	||	</xsl:call-template>
	**************************************************************************************************
	-->
		<xsl:param name="entityName"/>
		<xsl:param name="ori"/>
		<xsl:param name="entityAbbreviatedName"/>
		<xsl:param name="entityAccronym"/>
		<xsl:choose>
			<xsl:when test="count($ori)>0">
				<xsl:value-of select="$ori"/>
			</xsl:when>
			<xsl:when test="count($entityName)>0">
				<xsl:value-of select="$entityName"/>
			</xsl:when>
			<xsl:otherwise/>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="doSMTCode">
		<!--
	**************************************************************************************************
	|| Writes appropriate SMT Code Section.
	|| Input:
	||   codeSource
	|| Example Call:
	||   <xsl:call-template name="doSMTCode">
	||      <xsl:with-param name="codeSource" select="."/>
	||	 </xsl:call-template>
	**************************************************************************************************
	-->
		<xsl:param name="codeSource"/>
		<xsl:choose>
			<xsl:when test="string-length($codeSource)>0">
				<xsl:value-of select="$codeSource"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>Unknown Code</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="doSMTDescription">
		<!--
	**************************************************************************************************
	|| Writes the appropriate SMT Description Section.
	|| Input:
	||   referenceDate: SMT (owning agency) reference date.
	||   
	|| Example Call:
	||   <xsl:variable name="SMTDescription">
	||		<xsl:call-template name="doSMTDescription">
	||			<xsl:with-param name="referenceDate" select="@j:ReportedDate"/>
	||			<xsl:with-param name="agency" select="$SMTAgency"/>
	||			<xsl:with-param name="codeValue" select="./scarsMarksTattoosDescription/@codeValue"/>
	||			<xsl:with-param name="smtDescription" select="./scarsMarksTattoosDescription"/>
	||			<xsl:with-param name="imgReferenceDate" select="./image/@j:ReportedDate"/>
	||			<xsl:with-param name="imgAgency" select="$imgAgency"/>
	||			<xsl:with-param name="imgHref" select="./image/imageData/@href"/>
	||			<xsl:with-param name="imgComment" select="./image/comment"/>
	||		</xsl:call-template>
	**************************************************************************************************
	-->
		<xsl:param name="referenceDate"/>
		<xsl:param name="agency"/>
		<xsl:param name="codeValue"/>
		<xsl:param name="smtDescription"/>
		<xsl:param name="imgReferenceDate"/>
		<xsl:param name="imgAgency"/>
		<xsl:param name="imgHref"/>
		<xsl:param name="imgComment"/>
		<!-- Put the SMT Description info together correctly -->
		<xsl:if test="$codeValue">
			<xsl:value-of select="$codeValue"/>
			<xsl:if test="$smtDescription">
				<xsl:text>; </xsl:text>
			</xsl:if>
		</xsl:if>
		<xsl:if test="$smtDescription">
			<xsl:value-of select="$smtDescription"/>
		</xsl:if>
		<xsl:if test="$smtDescription or $codeValue">
			<xsl:text/>
		</xsl:if>
		<xsl:text>(</xsl:text>
		<xsl:value-of select="$agency"/>
		<!-- djr(19-MAR-03) agency is required -->
		<xsl:if test="$referenceDate">
			<xsl:text>; </xsl:text>
			<xsl:value-of select="$referenceDate"/>
		</xsl:if>
		<xsl:text>)</xsl:text>
		<xsl:if test="($smtDescription or $codeValue) and (string-length($imgComment)>0 or string-length($imgReferenceDate)>0 or string-length($imgAgency)>0 or string-length($imgHref)>0)">
			<xsl:text/>
			<xsl:if test="$imgComment">
				<xsl:value-of select="$imgComment"/>
				<xsl:text/>
			</xsl:if>
			<xsl:if test="$imgHref">
				<xsl:value-of select="$imgHref"/>
				<xsl:text/>
			</xsl:if>
			<xsl:if test="$imgAgency">
				<xsl:text>(</xsl:text>
				<xsl:value-of select="$imgAgency"/>
				<xsl:text>)</xsl:text>
			</xsl:if>
		</xsl:if>
	</xsl:template>
	<xsl:template name="doTypeImagesSection">
		<!--
	**************************************************************************************************
	|| Writes the appropriate Type/Comments and Images Section.  Used in the following Sections:
	||   -Fingerprint Images
	||   -Palmprint Images
	||   -Photo Images
	||   
	|| Example Call:
	||   
	**************************************************************************************************
	-->
		<xsl:param name="referenceDate"/>
		<xsl:param name="agency"/>
		<xsl:param name="Href"/>
		<xsl:param name="Comment"/>
		<!-- Put the info together correctly -->
		<xsl:if test="$Comment">
			<xsl:value-of select="$Comment"/>
			<xsl:text/>
		</xsl:if>
		<xsl:if test="$Href">
			<xsl:value-of select="$Href"/>
			<xsl:text/>
		</xsl:if>
		<xsl:text>(</xsl:text>
		<xsl:value-of select="$agency"/>
		<!-- djr(19-MAR-03) agency is required -->
		<xsl:if test="$referenceDate">
			<xsl:text>; </xsl:text>
			<xsl:value-of select="$referenceDate"/>
		</xsl:if>
		<xsl:text>)</xsl:text>
	</xsl:template>
	<xsl:template name="PrintCharge">
		<xsl:param name="Charge"/>
		<xsl:variable name="linkid">
			<xsl:value-of select="$Charge/@s:id"/>
		</xsl:variable>
		<!-- Global Variables -->
		<xsl:variable name="posCol1">0</xsl:variable>
		<!-- Position to start Column one -->
		<xsl:variable name="posCol2">24</xsl:variable>
		<!-- Position to start Column two -->
		<xsl:variable name="posCol3">48</xsl:variable>
		<!-- Position to start Column three -->
		<xsl:variable name="posMaxWidth">72</xsl:variable>
		<!-- Position for Maximum width -->
		<xsl:variable name="amtColSpacer">2</xsl:variable>
		<xsl:for-each select="$Charge">
			<xsl:if test="j:ChargeSequenceID/nc:IdentificationID">
				<xsl:text>Charge                  </xsl:text>
				<xsl:value-of select="j:ChargeSequenceID/nc:IdentificationID"/>
				<xsl:text>&#10;</xsl:text>
			</xsl:if>
			<xsl:if test="j:ChargeIdentification/nc:IdentificationID">
				<xsl:text>         Charge Number  </xsl:text>
				<xsl:value-of select="j:ChargeIdentification/nc:IdentificationID"/>
				<xsl:text>&#10;</xsl:text>
			</xsl:if>
			<xsl:if test="j:ChargeTrackingIdentification">
				<xsl:text>Charge Tracking Number  </xsl:text>
				<xsl:value-of select="j:ChargeTrackingIdentification/nc:IdentificationID"/>
				<xsl:text>&#10;</xsl:text>
			</xsl:if>
			<xsl:if test="j:ChargeText">
				<xsl:text>        Charge Literal  </xsl:text>
				<xsl:call-template name="wrapIn">
					<xsl:with-param name="Text" select="j:ChargeText"/>
					<xsl:with-param name="StartPos" select="$amtColSpacer - 2"/>
					<xsl:with-param name="EndPos" select="$posMaxWidth"/>
					<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
					<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
					<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
					<xsl:with-param name="initialRun" select="0"/>
				</xsl:call-template>
				<xsl:text>&#10;</xsl:text>
			</xsl:if>
			<!--			<xsl:if test="./rap:ArrestAgency/j:OrganizationORIID/nc:IdentificationID or ./rap:ArrestAgency/j:OrganizationName">
				<xsl:text>                Agency</xsl:text>
				<xsl:call-template name="wrapIn">
					<xsl:with-param name="Text" select="concat(./rap:ArrestAgency/j:OrganizationORIID/nc:IdentificationID,' ',./rap:ArrestAgency/j:OrganizationName)"/>
					<xsl:with-param name="StartPos" select="$amtColSpacer"/>
					<xsl:with-param name="EndPos" select="$posMaxWidth"/>
					<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
					<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
					<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
					<xsl:with-param name="initialRun" select="0"/>
				</xsl:call-template>
				<xsl:text>&#10;</xsl:text>
			</xsl:if>-->
			<xsl:for-each select="j:ChargeDescriptionText">
				<xsl:text>    Charge Description</xsl:text>
				<xsl:call-template name="wrapIn">
					<xsl:with-param name="Text" select="."/>
					<xsl:with-param name="StartPos" select="$amtColSpacer"/>
					<xsl:with-param name="EndPos" select="$posMaxWidth"/>
					<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
					<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
					<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
					<xsl:with-param name="initialRun" select="0"/>
				</xsl:call-template>
				<xsl:text>&#10;</xsl:text>
			</xsl:for-each>
			<xsl:if test="./rap:ChargeStatute/j:StatuteText or ./rap:ChargeStatute/j:StatuteCodeIdentification">
				<xsl:text>               Statute</xsl:text>
			</xsl:if>
			<xsl:for-each select="./rap:ChargeStatute">
				<xsl:if test="./j:StatuteText or ./j:StatuteCodeIdentification">
					<xsl:variable name="i" select="position()"/>
					<xsl:choose>
						<!-- determine the correct spacing and write it -->
						<xsl:when test="$i = 1">
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="concat(./j:StatuteText, ' (', ./j:StatuteCodeIdentification/nc:IdentificationID, ' ', ./j:StatuteJurisdiction/nc:LocationStateName, ')')"/>
								<xsl:with-param name="StartPos" select="$amtColSpacer"/>
								<!-- space over spacer width -->
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="wrapIn">
								<xsl:with-param name="Text" select="concat(./j:StatuteText, ' (', ./j:StatuteCodeIdentification/nc:IdentificationID, ' ', ./j:StatuteJurisdiction/nc:LocationStateName, ')')"/>
								<xsl:with-param name="StartPos" select="$posCol2"/>
								<!-- space over to Column2 start pos -->
								<xsl:with-param name="EndPos" select="$posMaxWidth"/>
								<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
								<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
								<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
								<xsl:with-param name="initialRun" select="0"/>
							</xsl:call-template>
							<xsl:text>&#10;</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:if>
			</xsl:for-each>
			<xsl:if test="./j:ChargeNCICCode">
				<xsl:text>     NCIC Offense Code  </xsl:text>
			</xsl:if>
			<xsl:for-each select="./j:ChargeNCICCode">
				<xsl:variable name="i" select="position()"/>
				<xsl:choose>
					<!-- determine the correct spacing and write it -->
					<xsl:when test="$i = 1">
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="."/>
							<xsl:with-param name="StartPos" select="$amtColSpacer - 3"/>
							<!-- space over spacer width -->
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="."/>
							<xsl:with-param name="StartPos" select="$posCol2"/>
							<!-- space over to Column2 start pos -->
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
			<xsl:if test="./j:ChargeNCICCode &lt; 1">
				<xsl:text>&#10;</xsl:text>
			</xsl:if>
			<xsl:if test="./rap:ChargeNCICText">
				<xsl:text>     NCIC Offense Text  </xsl:text>
			</xsl:if>
			<xsl:for-each select="./rap:ChargeNCICText">
				<xsl:variable name="i" select="position()"/>
				<xsl:choose>
					<!-- determine the correct spacing and write it -->
					<xsl:when test="$i = 1">
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="."/>
							<xsl:with-param name="StartPos" select="$amtColSpacer - 3"/>
							<!-- space over spacer width -->
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="."/>
							<xsl:with-param name="StartPos" select="$posCol2"/>
							<!-- space over to Column2 start pos -->
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
			<xsl:if test="./rap:ChargeNCICText &lt; 1">
				<xsl:text>&#10;</xsl:text>
			</xsl:if>
			<xsl:if test="./rap:ChargeStatute/j:StatuteOffenseIdentification/nc:IdentificationID">
				<xsl:text>    State Offense Code</xsl:text>
			</xsl:if>
			<xsl:for-each select="./rap:ChargeStatute/j:StatuteOffenseIdentification/nc:IdentificationID">
				<xsl:variable name="i" select="position()"/>
				<xsl:choose>
					<!-- determine the correct spacing and write it -->
					<xsl:when test="$i = 1">
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="."/>
							<xsl:with-param name="StartPos" select="$amtColSpacer"/>
							<!-- space over spacer width -->
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="."/>
							<xsl:with-param name="StartPos" select="$posCol2"/>
							<!-- space over to Column2 start pos -->
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
			<xsl:if test="./rap:ChargeStatute/j:StatuteOffenseID &lt; 1">
				<xsl:text>&#10;</xsl:text>
			</xsl:if>
			<xsl:if test="./rap:ChargeStatute/j:StatuteOffenseStateCodeText">
				<xsl:text>    State Offense Literal</xsl:text>
			</xsl:if>
			<xsl:for-each select="./rap:ChargeStatute/j:StatuteOffenseStateCodeText">
				<xsl:variable name="i" select="position()"/>
				<xsl:choose>
					<!-- determine the correct spacing and write it -->
					<xsl:when test="$i = 1">
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="."/>
							<xsl:with-param name="StartPos" select="$amtColSpacer"/>
							<!-- space over spacer width -->
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="."/>
							<xsl:with-param name="StartPos" select="$posCol2"/>
							<!-- space over to Column2 start pos -->
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
			<xsl:if test="./rap:ChargeStatute/j:StatuteOffenseStateCodeText &lt; 1">
				<xsl:text>&#10;</xsl:text>
			</xsl:if>
			<xsl:if test="./j:ChargeCountQuantity">
				<xsl:text>                Counts</xsl:text>
				<xsl:call-template name="wrapIn">
					<xsl:with-param name="Text" select="./j:ChargeCountQuantity"/>
					<xsl:with-param name="StartPos" select="$amtColSpacer"/>
					<xsl:with-param name="EndPos" select="$posMaxWidth"/>
					<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
					<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
					<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
					<xsl:with-param name="initialRun" select="0"/>
				</xsl:call-template>
				<xsl:text>&#10;</xsl:text>
			</xsl:if>
			<xsl:if test="./j:ChargeSeverityText">
				<xsl:text>              Severity</xsl:text>
				<xsl:call-template name="wrapIn">
					<xsl:with-param name="Text" select="concat(./j:ChargeSeverityText,' ',./rap:ChargeSeverityDescriptionText)"/>
					<xsl:with-param name="StartPos" select="$amtColSpacer"/>
					<!-- space over spacer width -->
					<xsl:with-param name="EndPos" select="$posMaxWidth"/>
					<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
					<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
					<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
					<xsl:with-param name="initialRun" select="0"/>
				</xsl:call-template>
				<xsl:text>&#10;</xsl:text>
			</xsl:if>
			<xsl:if test="./j:ChargeApplicabilityText">
				<xsl:text>       Inchoate Charge</xsl:text>
				<xsl:call-template name="wrapIn">
					<xsl:with-param name="Text" select="./j:ChargeApplicabilityText"/>
					<xsl:with-param name="StartPos" select="$amtColSpacer"/>
					<xsl:with-param name="EndPos" select="$posMaxWidth"/>
					<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
					<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
					<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
					<xsl:with-param name="initialRun" select="0"/>
				</xsl:call-template>
				<xsl:text>&#10;</xsl:text>
			</xsl:if>
			<xsl:if test="./j:ChargeSpecialAllegationText">
				<xsl:text>      Enhancing Factor</xsl:text>
			</xsl:if>
			<xsl:for-each select="./j:ChargeSpecialAllegationText">
				<xsl:variable name="i" select="position()"/>
				<xsl:choose>
					<!-- determine the correct spacing and write it -->
					<xsl:when test="$i = 1">
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="."/>
							<xsl:with-param name="StartPos" select="$amtColSpacer"/>
							<!-- space over spacer width -->
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="."/>
							<xsl:with-param name="StartPos" select="$posCol2"/>
							<!-- space over to Column2 start pos -->
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
			<xsl:if test="./j:ChargeClassification/j:ChargeReducingFactorText">
				<xsl:text>       Reducing Factor</xsl:text>
			</xsl:if>
			<xsl:for-each select="./j:ChargeClassification/j:ChargeReducingFactorText">
				<xsl:variable name="i" select="position()"/>
				<xsl:choose>
					<!-- determine the correct spacing and write it -->
					<xsl:when test="$i = 1">
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="."/>
							<xsl:with-param name="StartPos" select="$amtColSpacer"/>
							<!-- space over spacer width -->
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="."/>
							<xsl:with-param name="StartPos" select="$posCol2"/>
							<!-- space over to Column2 start pos -->
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
			<xsl:if test="./j:ChargeDisposition">
				<xsl:text>           Disposition</xsl:text>
			</xsl:if>
			<xsl:for-each select="./j:ChargeDisposition">
				<xsl:variable name="i" select="position()"/>
				<xsl:variable name="metadataid1">
					<xsl:value-of select="./@s:metadata"/>
				</xsl:variable>
				<xsl:choose>
					<!-- determine the correct spacing and write it -->
					<xsl:when test="nc:DispositionDate/nc:Date or //ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid1]/nc:CommentText">
						<xsl:choose>
							<!-- determine the correct spacing and write it -->
							<xsl:when test="$i = 1">
								<xsl:call-template name="wrapIn">
									<xsl:with-param name="Text" select="concat(j:ChargeDispositionOtherText,'(',//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid1]/nc:CommentText, ' ',nc:DispositionDate/nc:Date,'; ',nc:DispositionDescriptionText, ')')"/>
									<xsl:with-param name="StartPos" select="$amtColSpacer"/>
									<!-- space over spacer width -->
									<xsl:with-param name="EndPos" select="$posMaxWidth"/>
									<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
									<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
									<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
									<xsl:with-param name="initialRun" select="0"/>
								</xsl:call-template>
								<xsl:text>&#10;</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:call-template name="wrapIn">
									<xsl:with-param name="Text" select="concat(j:ChargeDispositionOtherText,'(',//ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[@s:id=$metadataid1]/nc:CommentText, ' ',nc:DispositionDate/nc:Date,'; ',nc:DispositionDescriptionText, ')')"/>
									<xsl:with-param name="StartPos" select="$posCol2"/>
									<!-- space over to Column2 start pos -->
									<xsl:with-param name="EndPos" select="$posMaxWidth"/>
									<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
									<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
									<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
									<xsl:with-param name="initialRun" select="0"/>
								</xsl:call-template>
								<xsl:text>&#10;</xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="nc:DispositionDescriptionText">
						<xsl:choose>
							<!-- determine the correct spacing and write it -->
							<xsl:when test="$i = 1">
								<xsl:call-template name="wrapIn">
									<xsl:with-param name="Text" select="concat(j:ChargeDispositionOtherText,'(',nc:DispositionDescriptionText, ')')"/>
									<xsl:with-param name="StartPos" select="$amtColSpacer"/>
									<!-- space over spacer width -->
									<xsl:with-param name="EndPos" select="$posMaxWidth"/>
									<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
									<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
									<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
									<xsl:with-param name="initialRun" select="0"/>
								</xsl:call-template>
								<xsl:text>&#10;</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:call-template name="wrapIn">
									<xsl:with-param name="Text" select="concat(j:ChargeDispositionOtherText,'(',nc:DispositionDescriptionText, ')')"/>
									<xsl:with-param name="StartPos" select="$posCol2"/>
									<!-- space over to Column2 start pos -->
									<xsl:with-param name="EndPos" select="$posMaxWidth"/>
									<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
									<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
									<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
									<xsl:with-param name="initialRun" select="0"/>
								</xsl:call-template>
								<xsl:text>&#10;</xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="$i = 1">
								<xsl:call-template name="wrapIn">
									<xsl:with-param name="Text" select="j:ChargeDispositionOtherText"/>
									<xsl:with-param name="StartPos" select="$amtColSpacer"/>
									<!-- space over spacer width -->
									<xsl:with-param name="EndPos" select="$posMaxWidth"/>
									<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
									<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
									<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
									<xsl:with-param name="initialRun" select="0"/>
								</xsl:call-template>
								<xsl:text>&#10;</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:call-template name="wrapIn">
									<xsl:with-param name="Text" select="j:ChargeDispositionOtherText"/>
									<xsl:with-param name="StartPos" select="$posCol2"/>
									<!-- space over to Column2 start pos -->
									<xsl:with-param name="EndPos" select="$posMaxWidth"/>
									<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
									<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
									<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
									<xsl:with-param name="initialRun" select="0"/>
								</xsl:call-template>
								<xsl:text>&#10;</xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:if test="fs:JuvenileInformalAdjustmentIndicator">
					<xsl:text>Juvenile Informal Adjustment</xsl:text>
					<xsl:call-template name="wrapIn">
						<xsl:with-param name="Text" select="fs:JuvenileInformalAdjustmentIndicator"/>
						<xsl:with-param name="StartPos" select="$posCol2 - (28)"/>
						<xsl:with-param name="EndPos" select="$posMaxWidth"/>
						<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
						<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
						<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
						<xsl:with-param name="initialRun" select="0"/>
					</xsl:call-template>
					<xsl:text>&#10;</xsl:text>
				</xsl:if>
			</xsl:for-each>
			<xsl:if test="//rap:ChargeAgencyAssociation[j:ChargeReference/@s:ref=$linkid]/rap:ChargeAgencyRecordIdentification/nc:IdentificationID">
				<xsl:text>Charge Case Number</xsl:text>
				<xsl:call-template name="wrapIn">
					<xsl:with-param name="Text" select="//rap:ChargeAgencyAssociation[j:ChargeReference/@s:ref=$linkid]/rap:ChargeAgencyRecordIdentification/nc:IdentificationID"/>
					<xsl:with-param name="StartPos" select="$posCol2 - (18)"/>
					<xsl:with-param name="EndPos" select="$posMaxWidth"/>
					<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
					<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
					<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
					<xsl:with-param name="initialRun" select="0"/>
				</xsl:call-template>
				<xsl:text>&#10;</xsl:text>
			</xsl:if>
			<!-- -->
			<xsl:if test="//rap:ChargeAgencyAssociation[j:ChargeReference/@s:ref=$linkid]">
				<xsl:variable name="agencyid">
					<xsl:value-of select="//rap:ChargeAgencyAssociation[j:ChargeReference/@s:ref=$linkid]/nc:OrganizationReference/@s:ref"/>
				</xsl:variable>
				<xsl:text>Charging Agency</xsl:text>
				<xsl:call-template name="wrapIn">
					<xsl:with-param name="Text" select="concat(//rap:Agency[@s:id=$agencyid]/j:OrganizationAugmentation/j:OrganizationORIIdentification/nc:IdentificationID,' ',//rap:Agency[@s:id=$agencyid]/nc:OrganizationName)"/>
					<xsl:with-param name="StartPos" select="$posCol2 - (15)"/>
					<xsl:with-param name="EndPos" select="$posMaxWidth"/>
					<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
					<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
					<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
					<xsl:with-param name="initialRun" select="0"/>
				</xsl:call-template>
				<xsl:text>&#10;</xsl:text>
			</xsl:if>
			<!-- -->
			<!--<xsl:if test="rap:ChargeAgencyRecordIdentification">
				<xsl:text>Charge Case Number</xsl:text>
				<xsl:call-template name="wrapIn">
					<xsl:with-param name="Text" select="./rap:ChargeAgencyRecordIdentification/nc:IdentificationID"/>
					<xsl:with-param name="StartPos" select="$posCol2 - (18)"/>
					<xsl:with-param name="EndPos" select="$posMaxWidth"/>
					<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
					<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
					<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
					<xsl:with-param name="initialRun" select="0"/>
				</xsl:call-template>
				<xsl:text>&#10;</xsl:text>
			</xsl:if>			-->
			<xsl:if test="fs:JuvenileStatusOffenseIndicator">
				<xsl:text>Juvenile Status Offense</xsl:text>
				<xsl:call-template name="wrapIn">
					<xsl:with-param name="Text" select="fs:JuvenileStatusOffenseIndicator"/>
					<xsl:with-param name="StartPos" select="$posCol2 - (23)"/>
					<xsl:with-param name="EndPos" select="$posMaxWidth"/>
					<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
					<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
					<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
					<xsl:with-param name="initialRun" select="0"/>
				</xsl:call-template>
				<xsl:text>&#10;</xsl:text>
			</xsl:if>
			<xsl:if test="j:JuvenileAsAdultIndicator">
				<xsl:text>Juvenile As Adult</xsl:text>
				<xsl:call-template name="wrapIn">
					<xsl:with-param name="Text" select="j:JuvenileAsAdultIndicator"/>
					<xsl:with-param name="StartPos" select="$posCol2 - (17)"/>
					<xsl:with-param name="EndPos" select="$posMaxWidth"/>
					<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
					<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
					<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
					<xsl:with-param name="initialRun" select="0"/>
				</xsl:call-template>
				<xsl:text>&#10;</xsl:text>
			</xsl:if>
			<xsl:if test="nc:ActivityDescriptionText">
				<xsl:text>               Comment</xsl:text>
			</xsl:if>
			<xsl:for-each select="nc:ActivityDescriptionText">
				<xsl:variable name="i" select="position()"/>
				<xsl:choose>
					<!-- determine the correct spacing and write it -->
					<xsl:when test="$i = 1">
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="normalize-space(.)"/>
							<xsl:with-param name="StartPos" select="$amtColSpacer"/>
							<!-- space over spacer width -->
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="wrapIn">
							<xsl:with-param name="Text" select="."/>
							<xsl:with-param name="StartPos" select="$posCol2"/>
							<!-- space over to Column2 start pos -->
							<xsl:with-param name="EndPos" select="$posMaxWidth"/>
							<xsl:with-param name="WrapedStartPos" select="$posCol2"/>
							<xsl:with-param name="CurrentPos" select="$posMaxWidth + 1"/>
							<xsl:with-param name="WrapWidth" select="$posMaxWidth + 1"/>
							<xsl:with-param name="initialRun" select="0"/>
						</xsl:call-template>
						<xsl:text>&#10;</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="NewTemplate">
</xsl:template>
</xsl:stylesheet>
