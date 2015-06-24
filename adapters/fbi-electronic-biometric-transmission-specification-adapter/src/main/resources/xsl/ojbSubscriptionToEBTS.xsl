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
	xmlns:ebts="http://cjis.fbi.gov/fbi_ebts/10.0" 
    xmlns:ansi-nist="http://niem.gov/niem/biometrics/1.0" 
    xmlns:itl="http://biometrics.nist.gov/standard/2011" 
    xmlns:nc20="http://niem.gov/niem/niem-core/2.0" 
    xmlns:j="http://niem.gov/niem/domains/jxdm/4.1"
    xmlns:submsg-doc="http://ojbc.org/IEPD/Exchange/SubscriptionMessage/1.0"
    xmlns:submsg-ext="http://ojbc.org/IEPD/Extensions/Subscription/1.0">
	
	<xsl:output indent="yes" method="xml"/>
	<!-- These are implementation-specific parameters that must be passed in when calling this stylesheet -->
	
	<!-- This field corresponds to RBNF (2.2062) in EBTS 10.0 and indicates whether the subscriber wishes Rap Back Activity Notification 
		Format to be Pre-notification, Triggering Event, or Triggering Event with Identity History Summary. This field is mandatory in 
		establishing a Rap Back Subscription. 
	-->
	<xsl:param name="rapBackNotificatonFormat">3</xsl:param>
	
	<!-- This field corresponds to RBOO (2.2063) in EBTS 10.0 and indicates whether FBI/NGI should send notifications of events originating from 
		within the Submitter’s own state. The default value is ‘false’, NGI sending all notifications. A value of ‘true’ must be provided on all subscriptions 
		for which Submitter wishes to opt-out of in-state notifications 
	-->
	<xsl:param name="rapBackInStateOptOutIndicator">true</xsl:param>
	
	<!-- This field corresponds to RBT 2.2040 and specifies which Events will result in notifications sent to the subscriber -->
	<xsl:param name="rapBackTriggeringEvent">1</xsl:param>
	
	<xsl:template match="/submsg-doc:SubscriptionMessage">
	
		<itl:NISTBiometricInformationExchangePackage>
			<!-- Record Type 1 -->
			<itl:PackageInformationRecord>
				<ansi-nist:RecordCategoryCode>01</ansi-nist:RecordCategoryCode>
				 <ansi-nist:Transaction>
				 	<ansi-nist:TransactionDate>
				 		<nc20:Date>
				 			<xsl:value-of select="current-date()"/>
				 		</nc20:Date>
				 	</ansi-nist:TransactionDate>
				 </ansi-nist:Transaction>
			</itl:PackageInformationRecord>
			<!-- Record Type 2 -->
			<itl:PackageInformationRecord>
				<ansi-nist:RecordCategoryCode>02</ansi-nist:RecordCategoryCode>
				<itl:UserDefinedDescriptiveDetail>
					<ebts:DomainDefinedDescriptiveFields>
						<ebts:RecordRapBackData>
							<!-- in here somewhere is where we will need to add elements for rap back notification format, opt out indicator, and tiggering event -->
							<xsl:apply-templates select="submsg-ext:Subject/j:PersonAugmentation/j:PersonStateFingerprintIdentification[nc20:IdentificationID !='']"/>
						</ebts:RecordRapBackData>
						<xsl:apply-templates select="submsg-ext:Subject"/>
					</ebts:DomainDefinedDescriptiveFields>
				</itl:UserDefinedDescriptiveDetail>
			</itl:PackageInformationRecord>
		</itl:NISTBiometricInformationExchangePackage>
	</xsl:template>
	<xsl:template match="submsg-ext:Subject">
		<ebts:RecordSubject>
			<xsl:apply-templates select="nc20:PersonBirthDate"/>
			<xsl:apply-templates select="nc20:PersonName"/>
			<!-- call template to write PerosnFBIIdentification -->
		</ebts:RecordSubject>
	</xsl:template>
	<xsl:template match="nc20:PersonBirthDate">
		<xsl:copy-of select="." copy-namespaces="no"/>
	</xsl:template>
	<xsl:template match="nc20:PersonName">
		<ebts:PersonName>
			<xsl:apply-templates select="nc20:PersonGivenName"/>
			<xsl:apply-templates select="nc20:PersonSurName"/>
		</ebts:PersonName>
	</xsl:template>
	<xsl:template match="nc20:PersonGivenName">
		<xsl:copy-of select="." copy-namespaces="no"/>
	</xsl:template>
	<xsl:template match="nc20:PersonSurName">
		<xsl:copy-of select="." copy-namespaces="no"/>
	</xsl:template>
	<xsl:template match="j:PersonStateFingerprintIdentification">
		<ebts:RecordRapBackUserDefinedElement>
			<ebts:UserDefinedElementName>State Fingerprint ID</ebts:UserDefinedElementName>
			<ebts:UserDefinedElementText>
				<xsl:value-of select="normalize-space(.)"/>
			</ebts:UserDefinedElementText>
		</ebts:RecordRapBackUserDefinedElement>
	</xsl:template>
</xsl:stylesheet>