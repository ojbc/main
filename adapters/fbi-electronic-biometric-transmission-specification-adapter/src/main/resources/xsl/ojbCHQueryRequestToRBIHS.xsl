<!-- Unless explicitly acquired and licensed from Licensor under another license, the contents of this file are subject to the Reciprocal 
	Public License ("RPL") Version 1.5, or subsequent versions as allowed by the RPL, and You may not copy or use this file in either source 
	code or executable form, except in compliance with the terms and conditions of the RPL All software distributed under the RPL is provided 
	strictly on an "AS IS" basis, WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY DISCLAIMS ALL SUCH WARRANTIES, 
	INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. 
	See the RPL for specific language governing rights and limitations under the RPL. http://opensource.org/licenses/RPL-1.5 Copyright 2012-2017 
	Open Justice Broker Consortium -->
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:ebts="http://cjis.fbi.gov/fbi_ebts/10.0"
	xmlns:nbio="http://niem.gov/niem/biometrics/1.0" xmlns:nistbio="http://biometrics.nist.gov/standard/2011"
	xmlns:nc="http://niem.gov/niem/niem-core/2.0" xmlns:j="http://niem.gov/niem/domains/jxdm/4.1"
	xmlns:niem-xsd="http://niem.gov/niem/proxy/xsd/2.0" xmlns:s="http://niem.gov/niem/structures/2.0"
	xmlns:fqr-doc="http://ojbc.org/IEPD/Exchange/FBIQueryRequest/1.0" exclude-result-prefixes="fqr-doc s">
	<xsl:output indent="yes" method="xml" />
	<!-- These are implementation-specific parameters that must be passed in when calling this stylesheet -->
	<!-- Assumes to be string: yyyy-MM-dd -->
	<xsl:param name="rapBackTransactionDate" />
	<!-- DAI 1.007 -->
	<xsl:param name="destinationOrganizationORI" />
	<!-- ORI 1.007 -->
	<xsl:param name="originatorOrganizationORI" />
	<!-- TCN 1.009 -->
	<xsl:param name="controlID" />
	<!-- DOM 1.013 -->
	<xsl:param name="domainVersion" />
	<xsl:param name="domainName" />
	<!-- VER 1.002 -->
	<xsl:param name="transactionMajorVersion" />
	<xsl:param name="transactionMinorVersion" />
	<!-- RBR 2.020 -->
	<xsl:param name="rapBackRecipient" />
	<!-- CRI 2.073 -->
	<xsl:param name="controllingAgencyID" />
	<!-- Native Scanning Resolution (NSR 1.011) -->
	<xsl:param name="nativeScanningResolution" />
	<!-- Nominal Transmitting Resolution (NTR 1.012 -->
	<xsl:param name="nominalTransmittingResolution" />
	<!-- CNT 1.003 -->
	<xsl:param name="transactionContentSummaryContentFirstRecordCategoryCode" />
	<xsl:param name="transactionContentSummaryContentRecordCount" />
	<xsl:template match="/">
		<xsl:apply-templates select="fqr-doc:FBIRecordRequest" />
	</xsl:template>
	<xsl:template match="fqr-doc:FBIRecordRequest">
		<xsl:param name="action" />
		<xsl:param name="subscriptionCategory" />
		<nistbio:NISTBiometricInformationExchangePackage>
			<!-- EBTS Record Type 1 -->
			<xsl:call-template name="buildType1Record">
				<xsl:with-param name="action" select="$action" />
				<xsl:with-param name="subscriptionCategory" select="$subscriptionCategory" />
			</xsl:call-template>
			<!-- EBTS Record Type 2 -->
			<xsl:call-template name="buildType2Record">
				<xsl:with-param name="action" select="$action" />
				<xsl:with-param name="subscriptionCategory" select="$subscriptionCategory" />
			</xsl:call-template>
		</nistbio:NISTBiometricInformationExchangePackage>
	</xsl:template>
	<xsl:template name="buildType1Record">
		<xsl:param name="action" />
		<xsl:param name="subscriptionCategory" />
		<nistbio:PackageInformationRecord>
			<nbio:RecordCategoryCode>01</nbio:RecordCategoryCode>
			<nbio:Transaction>
				<nbio:TransactionDate>
					<nc:Date>
						<xsl:value-of select="$rapBackTransactionDate" />
					</nc:Date>
				</nbio:TransactionDate>
				<nbio:TransactionDestinationOrganization>
					<nc:OrganizationIdentification>
						<nc:IdentificationID>
							<xsl:value-of select="$destinationOrganizationORI" />
						</nc:IdentificationID>
					</nc:OrganizationIdentification>
				</nbio:TransactionDestinationOrganization>
				<nbio:TransactionOriginatingOrganization>
					<nc:OrganizationIdentification>
						<!--ORI 1.008 -->
						<nc:IdentificationID>
							<xsl:value-of select="$originatorOrganizationORI" />
						</nc:IdentificationID>
					</nc:OrganizationIdentification>
				</nbio:TransactionOriginatingOrganization>
				<nbio:TransactionControlIdentification>
					<nc:IdentificationID>
						<xsl:value-of select="$controlID" />
					</nc:IdentificationID>
				</nbio:TransactionControlIdentification>
				<nbio:TransactionDomain>
					<nbio:DomainVersionNumberIdentification>
						<nc:IdentificationID>
							<xsl:value-of select="$domainVersion" />
						</nc:IdentificationID>
					</nbio:DomainVersionNumberIdentification>
					<nbio:TransactionDomainName>
						<xsl:value-of select="$domainName" />
					</nbio:TransactionDomainName>
				</nbio:TransactionDomain>
				<nbio:TransactionImageResolutionDetails>
					<nbio:NativeScanningResolutionValue>
						<xsl:value-of select="$nativeScanningResolution" />
					</nbio:NativeScanningResolutionValue>
					<nbio:NominalTransmittingResolutionValue>
						<xsl:value-of select="$nominalTransmittingResolution" />
					</nbio:NominalTransmittingResolutionValue>
				</nbio:TransactionImageResolutionDetails>
				<nbio:TransactionMajorVersionValue>
					<xsl:value-of select="$transactionMajorVersion" />
				</nbio:TransactionMajorVersionValue>
				<nbio:TransactionMinorVersionValue>
					<xsl:value-of select="$transactionMinorVersion" />
				</nbio:TransactionMinorVersionValue>
				<ebts:TransactionCategoryCode>RBIHS</ebts:TransactionCategoryCode>
				<xsl:call-template name="buildTransactionContentSummary">
					<xsl:with-param name="subscriptionCategory" select="$subscriptionCategory" />
				</xsl:call-template>
			</nbio:Transaction>
		</nistbio:PackageInformationRecord>
	</xsl:template>
	<xsl:template name="buildType2Record">
		<xsl:param name="action" />
		<xsl:param name="subscriptionCategory" />
		<nistbio:PackageDescriptiveTextRecord>
			<nbio:RecordCategoryCode>02</nbio:RecordCategoryCode>
			<nbio:ImageReferenceIdentification>
				<nc:IdentificationID>00</nc:IdentificationID>
			</nbio:ImageReferenceIdentification>
			<nistbio:UserDefinedDescriptiveDetail>
				<ebts:DomainDefinedDescriptiveFields>
					<nbio:RecordForwardOrganizations>
						<nc:OrganizationIdentification>
							<nc:IdentificationID>
								<xsl:value-of select="$rapBackRecipient" />
							</nc:IdentificationID>
						</nc:OrganizationIdentification>
					</nbio:RecordForwardOrganizations>
					<ebts:RecordRapBackData>
						<xsl:apply-templates select="fqr-doc:RapBackActivityNotificationIdentification/nc:IdentificationID"
							mode="notificationID" />
						<xsl:apply-templates select="fqr-doc:RapBackSubscriptionIdentification/nc:IdentificationID"
							mode="subscriptionID" />
					</ebts:RecordRapBackData>
					<ebts:RecordTransactionActivity>
						<ebts:RecordControllingAgency>
							<nc:OrganizationIdentification>
								<nc:IdentificationID>
									<xsl:value-of select="$controllingAgencyID" />
								</nc:IdentificationID>
							</nc:OrganizationIdentification>
						</ebts:RecordControllingAgency>
					</ebts:RecordTransactionActivity>
					<xsl:apply-templates select="j:Person" />
				</ebts:DomainDefinedDescriptiveFields>
			</nistbio:UserDefinedDescriptiveDetail>
		</nistbio:PackageDescriptiveTextRecord>
	</xsl:template>
	<xsl:template name="buildTransactionContentSummary">
		<xsl:param name="subscriptionCategory" />
		<nbio:TransactionContentSummary>
			<nbio:ContentFirstRecordCategoryCode>
				<xsl:value-of select="$transactionContentSummaryContentFirstRecordCategoryCode" />
			</nbio:ContentFirstRecordCategoryCode>
			<nbio:ContentRecordQuantity>
				<xsl:value-of select="$transactionContentSummaryContentRecordCount" />
			</nbio:ContentRecordQuantity>
			<nbio:ContentRecordSummary>
				<nbio:ImageReferenceIdentification>
					<nc:IdentificationID>00</nc:IdentificationID>
				</nbio:ImageReferenceIdentification>
				<nbio:RecordCategoryCode>02</nbio:RecordCategoryCode>
			</nbio:ContentRecordSummary>
		</nbio:TransactionContentSummary>
	</xsl:template>
	<xsl:template match="j:Person">
		<ebts:RecordSubject>
			<xsl:apply-templates select="j:PersonAugmentation" />
		</ebts:RecordSubject>
	</xsl:template>
	<xsl:template match="fqr-doc:RapBackActivityNotificationIdentification/nc:IdentificationID" mode="notificationID">
		<ebts:RecordRapBackActivityNotificationID>
			<xsl:value-of select="normalize-space(.)" />
		</ebts:RecordRapBackActivityNotificationID>
	</xsl:template>
	<xsl:template match="fqr-doc:RapBackSubscriptionIdentification/nc:IdentificationID" mode="subscriptionID">
		<ebts:RecordRapBackSubscriptionID>
			<xsl:value-of select="normalize-space(.)" />
		</ebts:RecordRapBackSubscriptionID>
	</xsl:template>
	<xsl:template match="j:PersonAugmentation">
		<xsl:apply-templates select="j:PersonFBIIdentification" />
	</xsl:template>
	<xsl:template match="j:PersonFBIIdentification">
		<j:PersonFBIIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="normalize-space(.)" />
			</nc:IdentificationID>
		</j:PersonFBIIdentification>
	</xsl:template>
</xsl:stylesheet>