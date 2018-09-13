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
<xsl:stylesheet version="2.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ebts="http://cjis.fbi.gov/fbi_ebts/10.0"
	xmlns:itl="http://biometrics.nist.gov/standard/2011" xmlns:ansi-nist="http://niem.gov/niem/biometrics/1.0"
	xmlns:nc="http://niem.gov/niem/niem-core/2.0" xmlns:j="http://niem.gov/niem/domains/jxdm/4.1"
	xmlns:cht-doc="http://ojbc.org/IEPD/Exchange/CriminalHistoryTextDocument/1.0"
	xmlns:error="http://ojbc.org/IEPD/Extensions/PersonQueryErrorReporting/1.0" exclude-result-prefixes="ebts ansi-nist j itl">
	<xsl:output indent="yes" method="xml" omit-xml-declaration="no" />
	<xsl:template match="/">
		<cht-doc:CriminalHistoryTextDocument>
			<xsl:apply-templates
				select="itl:NISTBiometricInformationExchangePackage/itl:PackageDescriptiveTextRecord/itl:UserDefinedDescriptiveDetail/ebts:DomainDefinedDescriptiveFields" />
		</cht-doc:CriminalHistoryTextDocument>
	</xsl:template>
	<xsl:template match="ebts:DomainDefinedDescriptiveFields">
		<error:PersonQueryResultError>
			<xsl:apply-templates
				select="ebts:RecordTransactionData/ebts:TransactionResponseData/ebts:TransactionStatusText" />
			<xsl:apply-templates select="ebts:RecordSubject/j:PersonFBIIdentification/nc:IdentificationID" mode="ucn" />
		</error:PersonQueryResultError>
	</xsl:template>
	<xsl:template match="ebts:TransactionStatusText">
		<error:ErrorText>
			<xsl:value-of select="." />
		</error:ErrorText>
	</xsl:template>
	<xsl:template match="nc:IdentificationID" mode="ucn">
		<error:PersonRecordRequestIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="." />
			</nc:IdentificationID>
		</error:PersonRecordRequestIdentification>
	</xsl:template>
</xsl:stylesheet>