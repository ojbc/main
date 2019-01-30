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
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:ebts="http://cjis.fbi.gov/fbi_ebts/10.0" 
	xmlns:nbio="http://niem.gov/niem/biometrics/1.0"
	xmlns:nistbio="http://biometrics.nist.gov/standard/2011" 
	xmlns:nc="http://niem.gov/niem/niem-core/2.0"
	xmlns:j="http://niem.gov/niem/domains/jxdm/4.1" xmlns:submsg-doc="http://ojbc.org/IEPD/Exchange/SubscriptionMessage/1.0"
	xmlns:submsg-ext="http://ojbc.org/IEPD/Extensions/Subscription/1.0"
	xmlns:unsubmsg-doc="http://ojbc.org/IEPD/Exchange/UnsubscriptionMessage/1.0"
	xmlns:smm="http://ojbc.org/IEPD/Exchange/SubscriptionModificationMessage/1.0"
	xmlns:b-2="http://docs.oasis-open.org/wsn/b-2" exclude-result-prefixes="submsg-doc submsg-ext unsubmsg-doc smm b-2">
	<xsl:output indent="yes" method="xml" />
	<!-- These are implementation-specific parameters that must be passed in 
		when calling this stylesheet -->
	<!-- Assumes to be string: yyyy-MM-dd -->
	<xsl:param name="rapBackTransactionDate" />
	<!-- RBNF (2.2062) -->
	<xsl:param name="rapBackNotificatonFormat" />
	<!-- RBC 2.2065 -->
	<xsl:param name="recordRapBackCategoryCode" />
	<!-- xsl:param name="rapBackDisclosureIndicator"/ -->
	<!-- RBOO (2.2063) -->
	<xsl:param name="rapBackInStateOptOutIndicator" />
	<!-- This field corresponds to RBT 2.2040 and specifies which Events will 
		result in notifications sent to the subscriber -->
	<!-- >xsl:param name="rapBackTriggeringEvent" / -->
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
	<!-- RAP 2.070 -->
	<xsl:param name="rapSheetRequestIndicator" />
	<!-- RBR 2.020 -->
	<!-- xsl:param name="rapBackRecipient"/ -->
	<!-- CRI 2.073 -->
	<xsl:param name="controllingAgencyID"
		select="/*/*/submsg-ext:SubscribingOrganization/j:OrganizationAugmentation/j:OrganizationORIIdentification/nc:IdentificationID" />
	<!-- OCA 2.009 -->
	<!-- xsl:param name="originatingAgencyCaseNumber"/ -->
	<!-- Native Scanning Resolution (NSR 1.011) -->
	<xsl:param name="nativeScanningResolution" />
	<!-- Nominal Transmitting Resolution (NTR 1.012 -->
	<xsl:param name="nominalTransmittingResolution" />
	<!-- CNT 1.003 -->
	<xsl:param name="transactionContentSummaryContentFirstRecordCategoryCode" />
	<xsl:param name="transactionContentSummaryContentRecordCountCriminal" />
	<xsl:param name="transactionContentSummaryContentRecordCountCivil" />
	<xsl:param name="civilRapBackSubscriptionTerm" />
	<xsl:template match="/">
		<xsl:apply-templates select="b-2:Subscribe" />
		<xsl:apply-templates select="b-2:Unsubscribe" />
		<xsl:apply-templates select="b-2:Modify" />
	</xsl:template>
	<xsl:template match="b-2:Subscribe">
		<xsl:variable name="action">newSubscription</xsl:variable>
		<xsl:variable name="subscriptionCategory">
			<xsl:choose>
				<xsl:when
					test="submsg-doc:SubscriptionMessage/submsg-ext:CivilSubscriptionReasonCode">civil</xsl:when>
				<xsl:when test="submsg-doc:SubscriptionMessage/submsg-ext:CriminalSubscriptionReasonCode">criminal</xsl:when>
			</xsl:choose>
		</xsl:variable>
		<xsl:apply-templates select="submsg-doc:SubscriptionMessage">
			<xsl:with-param name="action" select="$action" />
			<xsl:with-param name="subscriptionCategory" select="$subscriptionCategory" />
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template match="b-2:Unsubscribe">
		<xsl:variable name="action">cancelSubscription</xsl:variable>
		<xsl:apply-templates select="unsubmsg-doc:UnsubscriptionMessage">
			<xsl:with-param name="action" select="$action" />
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template match="b-2:Modify">
		<xsl:variable name="action">modifySubscription</xsl:variable>
		<xsl:apply-templates select="smm:SubscriptionModificationMessage">
			<xsl:with-param name="action" select="$action" />
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template match="submsg-doc:SubscriptionMessage">
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
	<xsl:template match="unsubmsg-doc:UnsubscriptionMessage">
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
	<xsl:template match="smm:SubscriptionModificationMessage">
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
				<!-- TODO: need to determine these values for civil submissions -->
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
				<!-- This determines whether we are requesting a new subscription or 
					modifying an existing one and whether it is civil or criminal -->
				<xsl:choose>
					<xsl:when
						test="$action = 'modifySubscription' or $action ='cancelSubscription'">
						<ebts:TransactionCategoryCode>RBMNT</ebts:TransactionCategoryCode>
					</xsl:when>
					<xsl:when test="$subscriptionCategory = 'civil'">
						<ebts:TransactionCategoryCode>RBSCVL</ebts:TransactionCategoryCode>
					</xsl:when>
					<xsl:when test="$subscriptionCategory = 'criminal'">
						<ebts:TransactionCategoryCode>RBSCRM</ebts:TransactionCategoryCode>
					</xsl:when>
				</xsl:choose>
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
					<xsl:choose>
						<xsl:when test="$action = 'newSubscription'">
							<nbio:RecordRapSheetRequestIndicator>
								<xsl:value-of select="$rapSheetRequestIndicator" />
							</nbio:RecordRapSheetRequestIndicator>
						</xsl:when>
					</xsl:choose>
					<ebts:RecordRapBackData>
						<ebts:RecordRapBackActivityNotificationFormatCode>
							<xsl:value-of select="$rapBackNotificatonFormat" />
						</ebts:RecordRapBackActivityNotificationFormatCode>
						<!-- RBATN 2.2070, Optional -->
						<xsl:if test="submsg-ext:FederalRapSheetDisclosure/submsg-ext:FederalRapSheetDisclosureAttentionDesignationText">
							<ebts:RecordRapBackAttentionText>
								<xsl:value-of
									select="submsg-ext:FederalRapSheetDisclosure/submsg-ext:FederalRapSheetDisclosureAttentionDesignationText" />
							</ebts:RecordRapBackAttentionText>
						</xsl:if>				
						<!--Rap Back Category RBC 2.2065 -->
						<!-- This is not allowed in RBMNT messages -->
						<xsl:choose>
							<xsl:when test="$action = 'newSubscription'">
								<xsl:apply-templates
									select="/b-2:Subscribe/submsg-doc:SubscriptionMessage/submsg-ext:CriminalSubscriptionReasonCode[. != '']"
									mode="rapBackCategory" />
								<xsl:apply-templates
									select="/b-2:Subscribe/submsg-doc:SubscriptionMessage/submsg-ext:CivilSubscriptionReasonCode[. != '']"
									mode="rapBackCategory" />
							</xsl:when>
						</xsl:choose>
						<!-- RBDI 2.2067, Optional -->
						<xsl:if test="/*/*/submsg-ext:FederalRapSheetDisclosure/submsg-ext:FederalRapSheetDisclosureIndicator[. != '']">
						<ebts:RecordRapBackDisclosureIndicator>
							<xsl:value-of
								select="/*/*/submsg-ext:FederalRapSheetDisclosure/submsg-ext:FederalRapSheetDisclosureIndicator" />
						</ebts:RecordRapBackDisclosureIndicator>
						</xsl:if>
						<!-- RBXD 2.2015 -->
						<xsl:choose>
							<xsl:when test="$action ='newSubscription'">
								<xsl:apply-templates
									select="/b-2:Subscribe/submsg-doc:SubscriptionMessage/nc:DateRange/nc:EndDate/nc:Date"
									mode="expirationDate" />
							</xsl:when>
							<xsl:when test="$action = 'modifySubscription'">
								<xsl:apply-templates
									select="/b-2:Modify/smm:SubscriptionModificationMessage/submsg-ext:SubscriptionModification/nc:DateRange/nc:EndDate/nc:Date"
									mode="expirationDate" />
							</xsl:when>
						</xsl:choose>
						<ebts:RecordRapBackInStateOptOutIndicator>
							<xsl:value-of select="$rapBackInStateOptOutIndicator" />
						</ebts:RecordRapBackInStateOptOutIndicator>
						<!-- nbio:RecordForwardOrganizations> <nc:OrganizationIdentification> 
							<nc:IdentificationID> <xsl:value-of select="$rapBackRecipient"/> </nc:IdentificationID> 
							</nc:OrganizationIdentification> </nbio:RecordForwardOrganizations -->
						<xsl:if
							test="$action = 'newSubscription' and $subscriptionCategory='civil'">
							<ebts:RecordRapBackSubscriptionTerm>
								<xsl:value-of select="$civilRapBackSubscriptionTerm" />
							</ebts:RecordRapBackSubscriptionTerm>
						</xsl:if>
						<!-- TODO: Remove wildcards -->
						<xsl:choose>
							<xsl:when test="$action = 'cancelSubscription'">
								<xsl:apply-templates
									select="/b-2:Unsubscribe/unsubmsg-doc:UnsubscriptionMessage/submsg-ext:RelatedFBISubscription"
									mode="fbiSubscriptionID" />
							</xsl:when>
							<xsl:when test="$action = 'modifySubscription'">
								<xsl:apply-templates
									select="/b-2:Modify/smm:SubscriptionModificationMessage/submsg-ext:FBISubscription/submsg-ext:SubscriptionFBIIdentification/nc:IdentificationID"
									mode="fbiSubscriptionID" />
							</xsl:when>
						</xsl:choose>
						<xsl:apply-templates
							select="submsg-ext:TriggeringEvents/submsg-ext:FederalTriggeringEventCode"
							mode="triggerCriminal" />
						<xsl:apply-templates
							select="submsg-ext:Subject/j:PersonAugmentation/j:PersonStateFingerprintIdentification[nc:IdentificationID !='']"
							mode="userDefinedElement" />
						<xsl:apply-templates
							select="submsg-ext:SubscriptionIdentification[nc:IdentificationID !='']"
							mode="userDefinedElement" />
						<xsl:apply-templates
							select="submsg-ext:SubscribingOrganization/nc:OrganizationIdentification[nc:IdentificationID !='']" mode="userDefinedElement" />
						<xsl:apply-templates
							select="submsg-ext:FingerprintIdentificationTransactionIdentification[nc:IdentificationID !='']" mode="userDefinedElement" />						
						<xsl:choose>
							<!-- This indicates that the maintenance is a "replace" -->
							<xsl:when test="$action = 'modifySubscription'">
								<ebts:TransactionRapBackMaintenanceCode>R</ebts:TransactionRapBackMaintenanceCode>
							</xsl:when>
							<xsl:when test="$action = 'cancelSubscription'">
								<ebts:TransactionRapBackMaintenanceCode>C
								</ebts:TransactionRapBackMaintenanceCode>
							</xsl:when>
							<xsl:when test="$action = 'newSubscription'" />
						</xsl:choose>
					</ebts:RecordRapBackData>
					<ebts:RecordTransactionActivity>
						<xsl:choose>
							<xsl:when test="$action = 'cancelSubscription'" />
							<xsl:when test="$action = 'modifySubscription'" />
							<xsl:when test="$action = 'newSubscription'">
								<nc:CaseTrackingID>
									<xsl:value-of
										select="/*/*/submsg-ext:SubscriptionRelatedCaseIdentification/nc:IdentificationID" />
								</nc:CaseTrackingID>
							</xsl:when>
						</xsl:choose>
						<ebts:RecordControllingAgency>
							<nc:OrganizationIdentification>
								<nc:IdentificationID>
									<xsl:value-of select="$controllingAgencyID" />
								</nc:IdentificationID>
							</nc:OrganizationIdentification>
						</ebts:RecordControllingAgency>
					</ebts:RecordTransactionActivity>
					<xsl:apply-templates select="submsg-ext:Subject" />
				</ebts:DomainDefinedDescriptiveFields>
			</nistbio:UserDefinedDescriptiveDetail>
		</nistbio:PackageDescriptiveTextRecord>
	</xsl:template>
	<xsl:template name="buildTransactionContentSummary">
		<xsl:param name="subscriptionCategory" />
		<nbio:TransactionContentSummary>
			<nbio:ContentFirstRecordCategoryCode>
				<xsl:value-of
					select="$transactionContentSummaryContentFirstRecordCategoryCode" />
			</nbio:ContentFirstRecordCategoryCode>
			<nbio:ContentRecordQuantity>
				<xsl:choose>
					<xsl:when test="$subscriptionCategory = 'civil'">
						<xsl:value-of select="$transactionContentSummaryContentRecordCountCivil" />
					</xsl:when>
					<xsl:when test="$subscriptionCategory = 'criminal'">
						<xsl:value-of
							select="$transactionContentSummaryContentRecordCountCriminal" />
					</xsl:when>
					<xsl:otherwise>
						01
					</xsl:otherwise>
				</xsl:choose>
			</nbio:ContentRecordQuantity>
			<nbio:ContentRecordSummary>
				<nbio:ImageReferenceIdentification>
					<nc:IdentificationID>00</nc:IdentificationID>
				</nbio:ImageReferenceIdentification>
				<nbio:RecordCategoryCode>02</nbio:RecordCategoryCode>
			</nbio:ContentRecordSummary>
		</nbio:TransactionContentSummary>
	</xsl:template>
	<xsl:template match="submsg-ext:Subject">
		<ebts:RecordSubject>
			<xsl:apply-templates select="nc:PersonBirthDate" />
			<xsl:apply-templates select="nc:PersonName" />
			<xsl:apply-templates select="j:PersonAugmentation" />
			<!--TODO: capture this info from the request file, need the actual capture date -->
            <ebts:PersonFingerprintSet>
                <!--DPR 2.038, Optional (Mandatory if submitting fingerprints)-->
                <nc:BiometricCaptureDate>
                    <nc:Date>1995-03-24</nc:Date>
                </nc:BiometricCaptureDate>
            </ebts:PersonFingerprintSet>			
		</ebts:RecordSubject>
	</xsl:template>
	<xsl:template match="nc:PersonBirthDate">
	    <nc:PersonBirthDate>
	       <nc:Date><xsl:value-of select="normalize-space(.)" /></nc:Date>
	    </nc:PersonBirthDate>	
	</xsl:template>
	<xsl:template match="nc:PersonName">
		<ebts:PersonName>
			<xsl:apply-templates select="nc:PersonGivenName" />
			<xsl:apply-templates select="nc:PersonSurName" />
		</ebts:PersonName>
	</xsl:template>
	<xsl:template match="nc:PersonGivenName">
		<nc:PersonGivenName><xsl:value-of select="normalize-space(.)" /></nc:PersonGivenName>
	</xsl:template>
	<xsl:template match="nc:PersonSurName">
		<nc:PersonSurName><xsl:value-of select="normalize-space(.)" /></nc:PersonSurName>
	</xsl:template>
	<xsl:template match="j:PersonAugmentation">
		<xsl:apply-templates select="j:PersonFBIIdentification" />
	</xsl:template>
	<xsl:template match="j:PersonFBIIdentification">
		<j:PersonFBIIdentification>
		   <nc:IdentificationID><xsl:value-of select="normalize-space(.)" /></nc:IdentificationID>
		</j:PersonFBIIdentification>
	</xsl:template>
	<xsl:template match="j:PersonStateFingerprintIdentification"
		mode="userDefinedElement">
		<ebts:RecordRapBackUserDefinedElement>
			<ebts:UserDefinedElementName>STATE FINGERPRINT ID</ebts:UserDefinedElementName>
			<ebts:UserDefinedElementText>
				<xsl:value-of select="normalize-space(.)" />
			</ebts:UserDefinedElementText>
		</ebts:RecordRapBackUserDefinedElement>
	</xsl:template>
	<xsl:template match="submsg-ext:SubscriptionIdentification/nc:IdentificationID"
		mode="userDefinedElement">
		<ebts:RecordRapBackUserDefinedElement>
			<ebts:UserDefinedElementName>STATE SUBSCRIPTION ID</ebts:UserDefinedElementName>
			<ebts:UserDefinedElementText>
				<xsl:value-of select="normalize-space(.)" />
			</ebts:UserDefinedElementText>
		</ebts:RecordRapBackUserDefinedElement>
	</xsl:template>
	<xsl:template match="submsg-ext:SubscribingOrganization/nc:OrganizationIdentification" mode="userDefinedElement">
		<ebts:RecordRapBackUserDefinedElement>
			<ebts:UserDefinedElementName>AGENCY OCA</ebts:UserDefinedElementName>
			<ebts:UserDefinedElementText>
				<xsl:value-of select="normalize-space(nc:IdentificationID)" />
			</ebts:UserDefinedElementText>
		</ebts:RecordRapBackUserDefinedElement>
	</xsl:template>
	<xsl:template match="submsg-ext:FingerprintIdentificationTransactionIdentification" mode="userDefinedElement">
		<ebts:RecordRapBackUserDefinedElement>
			<ebts:UserDefinedElementName>FINGERPRINT IDENTIFICATION TRANSACTION ID</ebts:UserDefinedElementName>
			<ebts:UserDefinedElementText>
				<xsl:value-of select="normalize-space(nc:IdentificationID)" />
			</ebts:UserDefinedElementText>
		</ebts:RecordRapBackUserDefinedElement>
	</xsl:template>	
	<xsl:template match="submsg-ext:CriminalSubscriptionReasonCode"
		mode="rapBackCategory">
		<ebts:RecordRapBackCategoryCode>
			<xsl:value-of select="." />
		</ebts:RecordRapBackCategoryCode>
	</xsl:template>
	<xsl:template match="submsg-ext:CivilSubscriptionReasonCode" mode="rapBackCategory">
		<ebts:RecordRapBackCategoryCode>
			<xsl:choose>
				<xsl:when test=". ='F'">
					<xsl:value-of select="'I'" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="." />
				</xsl:otherwise>
			</xsl:choose>
		</ebts:RecordRapBackCategoryCode>
	</xsl:template>
	<xsl:template match="nc:Date" mode="expirationDate">
		<ebts:RecordRapBackExpirationDate>
			<nc:Date>
				<xsl:value-of select="." />
			</nc:Date>
		</ebts:RecordRapBackExpirationDate>
	</xsl:template>
	<xsl:template match="nc:IdentificationID" mode="fbiSubscriptionID">
		<ebts:RecordRapBackSubscriptionID>
			<xsl:value-of select="." />
		</ebts:RecordRapBackSubscriptionID>
	</xsl:template>
	<xsl:template match="submsg-ext:FederalTriggeringEventCode"
		mode="triggerCriminal">
		<ebts:RecordRapBackTriggeringEventCode>
			<xsl:choose>
				<xsl:when test="normalize-space(.)='ARREST'">
					<xsl:value-of select="'1'" />
				</xsl:when>
				<xsl:when test="normalize-space(.)='DISPOSITION'">
					<xsl:value-of select="'2'" />
				</xsl:when>
				<xsl:when test="normalize-space(.)='NCIC-WARRANT-ENTRY'">
					<xsl:value-of select="'5'" />
				</xsl:when>
				<xsl:when test="normalize-space(.)='NCIC-WARRANT-MODIFICATION'">
					<xsl:value-of select="'7'" />
				</xsl:when>
				<xsl:when test="normalize-space(.)='NCIC-WARRANT-DELETION'">
					<xsl:value-of select="'6'" />
				</xsl:when>
				<xsl:when test="normalize-space(.)='NCIC-SOR-ENTRY'">
					<xsl:value-of select="'8'" />
				</xsl:when>
				<xsl:when test="normalize-space(.)='NCIC-SOR-MODIFICATION'">
					<xsl:value-of select="'10'" />
				</xsl:when>
				<xsl:when test="normalize-space(.)='NCIC-SOR-DELETION'">
					<xsl:value-of select="'9'" />
				</xsl:when>
				<xsl:when test="normalize-space(.)='DEATH'">
					<xsl:value-of select="'12'" />
				</xsl:when>
			</xsl:choose>
		</ebts:RecordRapBackTriggeringEventCode>
	</xsl:template>
</xsl:stylesheet>
