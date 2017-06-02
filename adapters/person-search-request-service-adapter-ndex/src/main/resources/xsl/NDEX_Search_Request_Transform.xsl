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
<xsl:stylesheet version="2.0" xmlns:psr-doc="http://ojbc.org/IEPD/Exchange/PersonSearchRequest/1.0" xmlns:psr-ext="http://ojbc.org/IEPD/Extensions/PersonSearchRequest/1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ulex="http://ulex.gov/ulex/2.0" xmlns:ulexsr="http://ulex.gov/searchretrieve/2.0" xmlns:ulexcodes="http://ulex.gov/codes/2.0" xmlns:ulexlib="http://ulex.gov/library/2.0" xmlns:j="http://niem.gov/niem/domains/jxdm/4.1" xmlns:nc="http://niem.gov/niem/niem-core/2.0" xmlns:em="http://niem.gov/niem/domains/emergencyManagement/2.0" xmlns:im="http://niem.gov/niem/domains/immigration/2.0" xmlns:scr="http://niem.gov/niem/domains/screening/2.0" xmlns:s="http://niem.gov/niem/structures/2.0" xmlns:lexs="http://lexs.gov/lexs/4.0" xmlns:wsa="http://www.w3.org/2005/08/addressing" xmlns:lexsdigest="http://lexs.gov/digest/4.0">
	<xsl:output indent="yes" method="xml" omit-xml-declaration="yes"/>
	<!-- The following is default a default value for the param that needs to be replaced -->
	<xsl:param name="fbiWSAddressingMessageID" select="'uuid:1e647974-237e-11e2-b87b-f23c91aec05e'"/>
	<xsl:template match="/psr-doc:PersonSearchRequest">
		<ulexsr:doStructuredSearchRequest>
			<xsl:apply-templates select="." mode="SSRM"/>
		</ulexsr:doStructuredSearchRequest>
	</xsl:template>
	<!--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx-->
	<xsl:template match="/psr-doc:PersonSearchRequest" mode="SSRM">
		<ulex:StructuredSearchRequestMessage>
			<xsl:apply-templates select="." mode="SRMM"/>
			<xsl:apply-templates select="." mode="SRM"/>
			<ulex:StructuredQuery>
				<xsl:apply-templates select="psr-ext:Person" mode="SQ"/>
				<xsl:apply-templates select="nc:Location/nc:LocationAddress/nc:StructuredAddress" mode="LOC"/>
			</ulex:StructuredQuery>
		</ulex:StructuredSearchRequestMessage>
	</xsl:template>
	<xsl:template match="psr-doc:PersonSearchRequest" mode="SRMM">
		<ulex:SRMessageMetadata>
			<xsl:apply-templates select="." mode="framework"/>
			<xsl:apply-templates select="." mode="impl"/>
			<xsl:apply-templates select="." mode="MDT"/>
			<xsl:apply-templates select="." mode="MID"/>
			<xsl:apply-templates select="." mode="MOM"/>
			<xsl:apply-templates select="." mode="MDI"/>
			<xsl:apply-templates select="." mode="MMDA"/>
		</ulex:SRMessageMetadata>
		<ulex:UserAssertionSAML>
			<!-- INSERT SAML ASSERTION HERE -->
		</ulex:UserAssertionSAML>
	</xsl:template>
	<xsl:template match="psr-doc:PersonSearchRequest" mode="framework">
		<ulex:ULEXFrameworkVersionText>2.0</ulex:ULEXFrameworkVersionText>
	</xsl:template>
	<xsl:template match="psr-doc:PersonSearchRequest" mode="impl">
		<ulex:ULEXImplementation>
			<ulex:ULEXImplementationVersionText>4.0</ulex:ULEXImplementationVersionText>
			<ulex:ULEXImplementationName>LEXS</ulex:ULEXImplementationName>
		</ulex:ULEXImplementation>
	</xsl:template>
	<xsl:template match="psr-doc:PersonSearchRequest" mode="MDT">
		<ulex:MessageDateTime>
			<xsl:value-of select="current-dateTime()"/>
		</ulex:MessageDateTime>
	</xsl:template>
	<xsl:template match="psr-doc:PersonSearchRequest" mode="MID">
		<wsa:MessageID>
			<xsl:value-of select="$fbiWSAddressingMessageID"/>
		</wsa:MessageID>
	</xsl:template>
	<xsl:template match="psr-doc:PersonSearchRequest" mode="MOM">
		<ulex:MessageOriginMetadata>
			<xsl:apply-templates select="psr-ext:SourceSystemNameText"/>
			<xsl:apply-templates select="psr-ext:SourceSystemNameText" mode="syscont"/>
		</ulex:MessageOriginMetadata>
	</xsl:template>
	<xsl:template match="psr-doc:PersonSearchRequest" mode="MMDA">
		<ulex:SRMessageMetadataDomainAttribute>
			<ulex:AttributeName>gfipm:ext:user:NDEx:ReasonText</ulex:AttributeName>
			<xsl:apply-templates select="psr-ext:SearchMetadata/psr-ext:SearchPurposeText"/>
			<ulex:DomainName>N-DEx</ulex:DomainName>
		</ulex:SRMessageMetadataDomainAttribute>
	</xsl:template>
	<xsl:template match="psr-doc:PersonSearchRequest" mode="SRM">
		<ulex:SearchRequestMetadata>
			<lexs:RequestedDataCategoryCode>Person</lexs:RequestedDataCategoryCode>
			<ulex:MaxItemMatchesNumeric>100</ulex:MaxItemMatchesNumeric>
			<ulex:StructuredPayloadsRequestedCode>none</ulex:StructuredPayloadsRequestedCode>
		</ulex:SearchRequestMetadata>
	</xsl:template>
	<xsl:template match="psr-ext:Person" mode="SQ">
		<xsl:call-template name="DQS-PER">
			<xsl:with-param name="element" select="nc:PersonName/nc:PersonSurName"/>
		</xsl:call-template>
		<xsl:call-template name="DQS-PER">
			<xsl:with-param name="element" select="nc:PersonName/nc:PersonGivenName"/>
		</xsl:call-template>
		<xsl:call-template name="DQS-PER">
			<xsl:with-param name="element" select="nc:PersonName/nc:PersonMiddleName"/>
		</xsl:call-template>
		<xsl:call-template name="DQS-PER">
			<xsl:with-param name="element" select="nc:PersonAlternateName/nc:PersonSurName"/>
		</xsl:call-template>
		<xsl:call-template name="DQS-PER">
			<xsl:with-param name="element" select="nc:PersonAlternateName/nc:PersonGivenName"/>
		</xsl:call-template>
		<xsl:call-template name="DQS-PER">
			<xsl:with-param name="element" select="nc:PersonAlternateName/nc:PersonMiddleName"/>
		</xsl:call-template>
		<xsl:call-template name="DQS-PER">
			<xsl:with-param name="element" select="nc:PersonBirthDate"/>
		</xsl:call-template>
		<xsl:call-template name="DQS-PER">
			<xsl:with-param name="element" select="nc:PersonEyeColorCode"/>
		</xsl:call-template>
		<xsl:call-template name="DQS-PER">
			<xsl:with-param name="element" select="nc:PersonHairColorCode"/>
		</xsl:call-template>
		<xsl:call-template name="DQS-PER">
			<xsl:with-param name="element" select="nc:PersonRaceCode"/>
		</xsl:call-template>
		<xsl:call-template name="DQS-PER">
			<xsl:with-param name="element" select="nc:PersonSexCode"/>
		</xsl:call-template>
		<xsl:call-template name="DQS-PER">
			<xsl:with-param name="element" select="nc:PersonSSNIdentification/nc:IdentificationID"/>
		</xsl:call-template>
		<xsl:call-template name="DQS-PER">
			<xsl:with-param name="element" select="nc:PersonAgeMeasure"/>
		</xsl:call-template>
		<xsl:call-template name="DQS-PER">
			<xsl:with-param name="element" select="nc:PersonHeightMeasure"/>
		</xsl:call-template>
		<xsl:call-template name="DQS-PER">
			<xsl:with-param name="element" select="nc:PersonWeightMeasure"/>
		</xsl:call-template>
		<!-- Extension, not available form N-DEX -->
		<!--xsl:call-template name="DQS-PER">
			<xsl:with-param name="element" select="psr-ext:PersonBirthDateRange"/>
		</xsl:call-template-->
		<xsl:call-template name="DQS-PER">
			<xsl:with-param name="element" select="j:PersonAugmentation/nc:DriverLicense"/>
		</xsl:call-template>
		<xsl:call-template name="DQS-PER">
			<xsl:with-param name="element" select="j:PersonAugmentation/j:PersonFBIIdentification"/>
		</xsl:call-template>
		<xsl:call-template name="DQS-PER">
			<xsl:with-param name="element" select="j:PersonAugmentation/j:PersonStateFingerprintIdentification"/>
		</xsl:call-template>
	</xsl:template>
	<xsl:template match="nc:Location/nc:LocationAddress/nc:StructuredAddress" mode="LOC">
		<xsl:call-template name="DQS-LOC">
			<xsl:with-param name="element" select="nc:LocationStreet/nc:StreetNumberText"/>
		</xsl:call-template>
		<xsl:call-template name="DQS-LOC">
			<xsl:with-param name="element" select="nc:LocationStreet/nc:StreetName"/>
		</xsl:call-template>
		<xsl:call-template name="DQS-LOC">
			<xsl:with-param name="element" select="nc:AddressSecondaryUnitText"/>
		</xsl:call-template>
		<xsl:call-template name="DQS-LOC">
			<xsl:with-param name="element" select="nc:LocationCityName"/>
		</xsl:call-template>
		<xsl:call-template name="DQS-LOC">
			<xsl:with-param name="element" select="nc:LocationStateFIPS5-2AlphaCode"/>
		</xsl:call-template>
		<xsl:call-template name="DQS-LOC">
			<xsl:with-param name="element" select="nc:LocationCountryFIPS10-4Code"/>
		</xsl:call-template>
		<xsl:call-template name="DQS-LOC">
			<xsl:with-param name="element" select="nc:LocationPostalCode"/>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="DQS-PER">
		<xsl:param name="element"/>
		<xsl:if test="$element != ''">
			<ulex:DigestQueryStatement>
				<lexs:DigestQueryField>
					<lexsdigest:EntityPerson>
						<lexsdigest:Person>
							<xsl:apply-templates select="$element"/>
						</lexsdigest:Person>
					</lexsdigest:EntityPerson>
				</lexs:DigestQueryField>
				<!--xsl:call-template name="exact">
					<xsl:with-param name="meta" select="$element/@s:metadata"/>
				</xsl:call-template-->
				<ulex:QueryMatchCode>exact</ulex:QueryMatchCode>
			</ulex:DigestQueryStatement>
		</xsl:if>
	</xsl:template>
	<xsl:template name="DQS-LOC">
		<xsl:param name="element"/>
		<xsl:if test="$element != ''">
			<ulex:DigestQueryStatement>
				<lexs:DigestQueryField>
					<lexsdigest:EntityLocation>
						<nc:Location>
							<nc:LocationAddress>
								<nc:StructuredAddress>
									<xsl:apply-templates select="$element"/>
								</nc:StructuredAddress>
							</nc:LocationAddress>
						</nc:Location>
					</lexsdigest:EntityLocation>
				</lexs:DigestQueryField>
				<!--xsl:call-template name="exact">
					<xsl:with-param name="meta" select="$element/@s:metadata"/>
				</xsl:call-template-->
				<ulex:QueryMatchCode>exact</ulex:QueryMatchCode>
			</ulex:DigestQueryStatement>
		</xsl:if>
	</xsl:template>
	<!--  BOTTOM -->
	<xsl:template match="psr-ext:SourceSystemNameText">
		<lexs:SystemIdentifier>
			<nc:OrganizationName>
				<xsl:value-of select="."/>
			</nc:OrganizationName>
		</lexs:SystemIdentifier>
	</xsl:template>
	<xsl:template match="psr-ext:SourceSystemNameText" mode="syscont">
		<lexs:SystemContact/>
	</xsl:template>
	<xsl:template match="psr-doc:PersonSearchRequest" mode="MDI">
		<lexs:MessageDestinationIdentifier>
			<lexs:OriginatingAgencyID>WVNDX0001</lexs:OriginatingAgencyID>
			<nc:OrganizationName>FBI CJIS</nc:OrganizationName>
			<lexs:SystemID>N-DEx</lexs:SystemID>
		</lexs:MessageDestinationIdentifier>
	</xsl:template>
	<xsl:template match="psr-ext:SearchMetadata/psr-ext:SearchPurposeText">
		<ulex:AttributeValueText>
			<xsl:value-of select="."/>
		</ulex:AttributeValueText>
	</xsl:template>
	<!--xsl:template name="exact">
		<xsl:param name="meta"/>
		<xsl:if test="$meta='SM001'">
			<ulex:QueryMatchCode>exact</ulex:QueryMatchCode>
		</xsl:if>
	</xsl:template-->
	<xsl:template match="nc:PersonName/nc:PersonSurName">
		<nc:PersonName>
			<nc:PersonSurName>
				<xsl:value-of select="."/>
			</nc:PersonSurName>
		</nc:PersonName>
	</xsl:template>
	<xsl:template match="nc:PersonName/nc:PersonGivenName">
		<nc:PersonName>
			<nc:PersonGivenName>
				<xsl:value-of select="."/>
			</nc:PersonGivenName>
		</nc:PersonName>
	</xsl:template>
	<xsl:template match="nc:PersonName/nc:PersonMiddleName">
		<nc:PersonName>
			<nc:PersonMiddleName>
				<xsl:value-of select="."/>
			</nc:PersonMiddleName>
		</nc:PersonName>
	</xsl:template>
	<xsl:template match="nc:PersonAlternateName/nc:PersonSurName">
		<nc:PersonAlternateName>
			<nc:PersonSurName>
				<xsl:value-of select="."/>
			</nc:PersonSurName>
		</nc:PersonAlternateName>
	</xsl:template>
	<xsl:template match="nc:PersonAlternateName/nc:PersonGivenName">
		<nc:PersonAlternateName>
			<nc:PersonGivenName>
				<xsl:value-of select="."/>
			</nc:PersonGivenName>
		</nc:PersonAlternateName>
	</xsl:template>
	<xsl:template match="nc:PersonAlternateName/nc:PersonMiddleName">
		<nc:PersonAlternateName>
			<nc:PersonMiddleName>
				<xsl:value-of select="."/>
			</nc:PersonMiddleName>
		</nc:PersonAlternateName>
	</xsl:template>
	<xsl:template match="nc:PersonBirthDate">
		<nc:PersonBirthDate>
			<nc:Date>
				<xsl:value-of select="."/>
			</nc:Date>
		</nc:PersonBirthDate>
	</xsl:template>
	<xsl:template match="nc:PersonEyeColorCode">
		<nc:PersonEyeColorCode>
			<xsl:value-of select="."/>
		</nc:PersonEyeColorCode>
	</xsl:template>
	<xsl:template match="nc:PersonHairColorCode">
		<nc:PersonHairColorCode>
			<xsl:value-of select="."/>
		</nc:PersonHairColorCode>
	</xsl:template>
	<xsl:template match="nc:PersonRaceCode">
		<nc:PersonRaceCode>
			<xsl:value-of select="."/>
		</nc:PersonRaceCode>
	</xsl:template>
	<xsl:template match="nc:PersonSexCode">
		<nc:PersonSexCode>
			<xsl:value-of select="."/>
		</nc:PersonSexCode>
	</xsl:template>
	<xsl:template match="nc:PersonSSNIdentification/nc:IdentificationID">
		<nc:PersonSSNIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="."/>
			</nc:IdentificationID>
		</nc:PersonSSNIdentification>
	</xsl:template>
	<xsl:template match="nc:PersonAgeMeasure">
		<nc:PersonAgeMeasure>
			<xsl:apply-templates select="nc:MeasureText"/>
			<xsl:apply-templates select="nc:MeasureRangeValue"/>
		</nc:PersonAgeMeasure>
	</xsl:template>
	<xsl:template match="nc:PersonHeightMeasure">
		<nc:PersonHeightMeasure>
			<xsl:apply-templates select="nc:MeasureText"/>
			<xsl:apply-templates select="nc:MeasureRangeValue"/>
			<xsl:apply-templates select="nc:LengthUnitCode"/>
		</nc:PersonHeightMeasure>
	</xsl:template>
	<xsl:template match="nc:PersonWeightMeasure">
		<nc:PersonWeightMeasure>
			<xsl:apply-templates select="nc:MeasureText"/>
			<xsl:apply-templates select="nc:MeasureRangeValue"/>
			<xsl:apply-templates select="nc:LengthUnitCode"/>
		</nc:PersonWeightMeasure>
	</xsl:template>
	<!-- Extension, not available form N-DEX -->
	<!--xsl:template match="psr-ext:PersonBirthDateRange">
		<psr-ext:PersonBirthDateRange>
			<xsl:apply-templates select="nc:StartDate"/>
			<xsl:apply-templates select="nc:EndDate"/>
		</psr-ext:PersonBirthDateRange>
	</xsl:template-->
	<xsl:template match="nc:StartDate">
		<nc:StartDate>
			<nc:Date>
				<xsl:value-of select="nc:Date"/>
			</nc:Date>
		</nc:StartDate>
	</xsl:template>
	<xsl:template match="nc:EndDate">
		<nc:EndDate>
			<nc:Date>
				<xsl:value-of select="nc:Date"/>
			</nc:Date>
		</nc:EndDate>
	</xsl:template>
	<xsl:template match="j:PersonAugmentation/nc:DriverLicense">
		<j:PersonAugmentation>
			<nc:DriverLicense>
				<xsl:apply-templates select="nc:DriverLicenseIdentification"/>
			</nc:DriverLicense>
		</j:PersonAugmentation>
	</xsl:template>
	<xsl:template match="nc:DriverLicenseIdentification">
		<nc:DriverLicenseIdentification>
			<xsl:apply-templates select="nc:IdentificationID"/>
			<xsl:apply-templates select="nc:IdentificationSourceText"/>
		</nc:DriverLicenseIdentification>
	</xsl:template>
	<xsl:template match="j:PersonAugmentation/j:PersonFBIIdentification">
		<j:PersonAugmentation>
			<j:PersonFBIIdentification>
				<xsl:apply-templates select="nc:IdentificationID"/>
			</j:PersonFBIIdentification>
		</j:PersonAugmentation>
	</xsl:template>
	<xsl:template match="j:PersonAugmentation/j:PersonStateFingerprintIdentification">
		<j:PersonAugmentation>
			<j:PersonStateFingerprintIdentification>
				<xsl:apply-templates select="nc:IdentificationID"/>
			</j:PersonStateFingerprintIdentification>
		</j:PersonAugmentation>
	</xsl:template>
	<xsl:template match="nc:LocationStreet/nc:StreetNumberText">
		<nc:LocationStreet>
			<nc:StreetNumberText>
				<xsl:value-of select="."/>
			</nc:StreetNumberText>
		</nc:LocationStreet>
	</xsl:template>
	<xsl:template match="nc:LocationStreet/nc:StreetName">
		<nc:LocationStreet>
			<nc:StreetName>
				<xsl:value-of select="."/>
			</nc:StreetName>
		</nc:LocationStreet>
	</xsl:template>
	<xsl:template match="nc:AddressSecondaryUnitText">
		<nc:AddressSecondaryUnitText>
			<xsl:value-of select="."/>
		</nc:AddressSecondaryUnitText>
	</xsl:template>
	<xsl:template match="nc:LocationCityName">
		<nc:LocationCityName>
			<xsl:value-of select="."/>
		</nc:LocationCityName>
	</xsl:template>
	<xsl:template match="nc:LocationStateFIPS5-2AlphaCode">
		<!-- Need to covnert state code to state name?-->
		<nc:LocationStateName>
			<xsl:value-of select="."/>
		</nc:LocationStateName>
	</xsl:template>
	<xsl:template match="nc:LocationCountryFIPS10-4Code">
		<!-- Need to convert country code to country name?-->
		<nc:LocationCountryName>
			<xsl:value-of select="."/>
		</nc:LocationCountryName>
	</xsl:template>
	<xsl:template match="nc:LocationPostalCode">
		<nc:LocationPostalCode>
			<xsl:value-of select="."/>
		</nc:LocationPostalCode>
	</xsl:template>
	<xsl:template match="nc:IdentificationID">
		<nc:IdentificationID>
			<xsl:value-of select="."/>
		</nc:IdentificationID>
	</xsl:template>
	<xsl:template match="nc:IdentificationSourceText">
		<nc:IdentificationSourceText>
			<xsl:value-of select="."/>
		</nc:IdentificationSourceText>
	</xsl:template>
	<xsl:template match="nc:MeasureText">
		<nc:MeasurePointValue>
			<xsl:value-of select="."/>
		</nc:MeasurePointValue>
	</xsl:template>
	<xsl:template match="nc:MeasureRangeValue">
		<nc:MeasureRangeValue>
			<xsl:apply-templates select="nc:RangeMinimumValue"/>
			<xsl:apply-templates select="nc:RangeMaximumValue"/>
		</nc:MeasureRangeValue>
	</xsl:template>
	<xsl:template match="nc:RangeMinimumValue">
		<nc:RangeMinimumValue>
			<xsl:value-of select="."/>
		</nc:RangeMinimumValue>
	</xsl:template>
	<xsl:template match="nc:RangeMaximumValue">
		<nc:RangeMaximumValue>
			<xsl:value-of select="."/>
		</nc:RangeMaximumValue>
	</xsl:template>
	<xsl:template match="nc:LengthUnitCode">
		<nc:LengthUnitCode>
			<xsl:value-of select="."/>
		</nc:LengthUnitCode>
	</xsl:template>
</xsl:stylesheet>
