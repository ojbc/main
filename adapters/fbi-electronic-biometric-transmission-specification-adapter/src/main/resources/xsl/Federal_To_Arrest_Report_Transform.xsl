<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ebts="http://cjis.fbi.gov/fbi_ebts/10.0" xmlns:ansi-nist="http://niem.gov/niem/biometrics/1.0" xmlns:itl="http://biometrics.nist.gov/standard/2011" xmlns:nc="http://niem.gov/niem/niem-core/2.0" xmlns:j41="http://niem.gov/niem/domains/jxdm/4.1" xmlns:arrest-exch="http://ojbc.org/IEPD/Exchange/ArrestReport/1.0" xmlns:lexs="http://usdoj.gov/leisp/lexs/3.1" xmlns:lexspd="http://usdoj.gov/leisp/lexs/publishdiscover/3.1" xmlns:lexsdigest="http://usdoj.gov/leisp/lexs/digest/3.1" xmlns:s="http://niem.gov/niem/structures/2.0" xmlns:j="http://niem.gov/niem/domains/jxdm/4.0" xmlns:lexslib="http://usdoj.gov/leisp/lexs/library/3.1" xmlns:ndexia="http://fbi.gov/cjis/N-DEx/IncidentArrest/2.1">
	<xsl:output indent="yes" method="xml" omit-xml-declaration="yes"/>
	<xsl:template match="/">
		<arrest-exch:ArrestReport>
			<xsl:apply-templates select="itl:NISTBiometricInformationExchangePackage"/>
		</arrest-exch:ArrestReport>
	</xsl:template>
	<xsl:template match="itl:NISTBiometricInformationExchangePackage">
		<lexspd:doPublish>
			<lexs:PublishMessageContainer>
				<lexs:PublishMessage>
					<lexs:PDMessageMetadata>
						<lexs:LEXSVersion/>
						<xsl:apply-templates select="itl:PackageInformationRecord/ansi-nist:Transaction/ansi-nist:TransactionUTCDate/nc:DateTime"/>
						<lexs:MessageSequenceNumber>1</lexs:MessageSequenceNumber>
					</lexs:PDMessageMetadata>
					<lexs:DataSubmitterMetadata>
						<lexs:SystemIdentifier>
							<nc:OrganizationName/>
							<lexs:SystemID/>
						</lexs:SystemIdentifier>
						<lexs:SystemContact>
							<nc:PersonSurName/>
						</lexs:SystemContact>
					</lexs:DataSubmitterMetadata>
					<lexs:DataItemPackage>
						<lexs:PackageMetadata>
							<lexs:DataItemID/>
							<lexs:DataItemReferenceID/>
							<lexs:DataItemStatus/>
							<lexs:DataOwnerMetadata>
								<lexs:DataOwnerIdentifier>
									<xsl:apply-templates select="	itl:PackageDescriptiveTextRecord/itl:UserDefinedDescriptiveDetail/ebts:DomainDefinedDescriptiveFields/ebts:RecordTransactionActivity/ebts:RecordControllingAgency/nc:OrganizationIdentification/nc:IdentificationID"/>
									<nc:OrganizationName/>
									<lexs:SystemID/>
								</lexs:DataOwnerIdentifier>
								<lexs:DataOwnerContact>
									<nc:PersonSurName/>
								</lexs:DataOwnerContact>
							</lexs:DataOwnerMetadata>
							<lexs:DisseminationCriteriaValue>
								<lexs:DisseminationCriteriaDomainText/>
								<lexs:DisseminationCriteriaText/>
							</lexs:DisseminationCriteriaValue>
						</lexs:PackageMetadata>
						<xsl:apply-templates select="itl:PackageDescriptiveTextRecord/itl:UserDefinedDescriptiveDetail/ebts:DomainDefinedDescriptiveFields/ebts:RecordSubject"/>
						<lexs:StructuredPayload>
							<lexs:StructuredPayloadMetadata>
								<lexs:CommunityURI>http://www.ojbc.org</lexs:CommunityURI>
								<lexs:CommunityDescription>OJBC</lexs:CommunityDescription>
								<lexs:CommunityVersion>1.0</lexs:CommunityVersion>
							</lexs:StructuredPayloadMetadata>
							<ojbc:ArrestReport xmlns:j="http://niem.gov/niem/domains/jxdm/4.0" xmlns:s="http://niem.gov/niem/structures/2.0" xmlns:nc="http://niem.gov/niem/niem-core/2.0" xmlns:ojbc="http://ojbc.org/IEPD/Extensions/ArrestReportStructuredPayload/1.0" xmlns:xmime="http://www.w3.org/2005/05/xmlmime" xmlns:xop="http://www.w3.org/2004/08/xop/include">
								<ojbc:FederalCriminalHistoryRecordDocument>
									<ojbc:DocumentBinary xmime:contentType="text/plain"/>
									<xop:Include href="cid:http://ojbc.org/arrest/document/example"/>
								</ojbc:FederalCriminalHistoryRecordDocument>
							</ojbc:ArrestReport>
						</lexs:StructuredPayload>
					</lexs:DataItemPackage>
				</lexs:PublishMessage>
			</lexs:PublishMessageContainer>
		</lexspd:doPublish>
	</xsl:template>
	<xsl:template match="itl:PackageInformationRecord/ansi-nist:Transaction/ansi-nist:TransactionUTCDate/nc:DateTime">
		<lexs:MessageDateTime>
			<xsl:value-of select="."/>
		</lexs:MessageDateTime>
	</xsl:template>
	<xsl:template match="itl:PackageDescriptiveTextRecord/itl:UserDefinedDescriptiveDetail/ebts:DomainDefinedDescriptiveFields/ebts:RecordTransactionActivity/ebts:RecordControllingAgency/nc:OrganizationIdentification/nc:IdentificationID">
		<lexs:ORI>
			<xsl:value-of select="."/>
		</lexs:ORI>
	</xsl:template>
	<xsl:template match="itl:PackageDescriptiveTextRecord/itl:UserDefinedDescriptiveDetail/ebts:DomainDefinedDescriptiveFields/ebts:RecordSubject">
		<lexs:Digest>
			<lexsdigest:EntityPerson>
				<lexsdigest:Person>
					<xsl:attribute name="s:id"><xsl:value-of select="generate-id(.)"/></xsl:attribute>
					<xsl:apply-templates select="nc:PersonBirthDate/nc:Date"/>
					<xsl:apply-templates select="ebts:PersonName"/>
					<xsl:apply-templates select="j41:PersonFBIIdentification"/>
				</lexsdigest:Person>
				<xsl:apply-templates select="." mode="arrest_subject"/>
			</lexsdigest:EntityPerson>
		</lexs:Digest>
	</xsl:template>
	<xsl:template match="nc:PersonBirthDate/nc:Date">
		<nc:PersonBirthDate>
			<nc:Date>
				<xsl:value-of select="."/>
			</nc:Date>
		</nc:PersonBirthDate>
	</xsl:template>
	<xsl:template match="ebts:PersonName">
		<nc:PersonName>
			<nc:PersonGivenName>
				<xsl:value-of select="./nc:PersonGivenName"/>
			</nc:PersonGivenName>
			<nc:PersonMiddleName>
				<xsl:value-of select="./nc:PersonMiddleName"/>
			</nc:PersonMiddleName>
			<nc:PersonSurName>
				<xsl:value-of select="./nc:PersonlastName"/>
			</nc:PersonSurName>
		</nc:PersonName>
	</xsl:template>
	<xsl:template match="j41:PersonFBIIdentification">
		<j:PersonAugmentation>
			<j:PersonFBIIdentification>
				<nc:IdentificationID>
					<xsl:value-of select="."/>
				</nc:IdentificationID>
			</j:PersonFBIIdentification>
		</j:PersonAugmentation>
	</xsl:template>
	<xsl:template match="itl:PackageDescriptiveTextRecord/itl:UserDefinedDescriptiveDetail/ebts:DomainDefinedDescriptiveFields/ebts:RecordSubject" mode="arrest_subject">
		<j:ArrestSubject>
			<nc:RoleOfPersonReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(.)"/></xsl:attribute>
			</nc:RoleOfPersonReference>
		</j:ArrestSubject>
	</xsl:template>
</xsl:stylesheet>
