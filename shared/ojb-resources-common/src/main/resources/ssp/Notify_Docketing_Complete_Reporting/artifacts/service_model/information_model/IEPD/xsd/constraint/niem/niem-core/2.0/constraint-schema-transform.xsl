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
<xsl:stylesheet version="1.0" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="xml" encoding="UTF-8" indent="yes" />
<xsl:template match="xsd:element[@ref='nc:ActivityReference' and ../../../../@name='ActivityConveyanceAssociationType']">
<xsd:element ref="nc:ActivityReference">
<xsl:attribute name="minOccurs">1</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ConveyanceReference' and ../../../../@name='ActivityConveyanceAssociationType']">
<xsd:element ref="nc:ConveyanceReference">
<xsl:attribute name="minOccurs">1</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ActivityIdentification' and ../../../../@name='ActivityType']">
<xsd:element ref="nc:ActivityIdentification">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ActivityDateRepresentation' and ../../../../@name='ActivityType']">
<xsd:element ref="nc:ActivityDateRepresentation">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ActivityDescriptionText' and ../../../../@name='ActivityType']">
<xsd:element ref="nc:ActivityDescriptionText">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ActivityIdentification' and ../../../../@name='ActivityType']">
<xsd:element ref="nc:ActivityIdentification">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ActivityStatus' and ../../../../@name='ActivityType']">
<xsd:element ref="nc:ActivityStatus">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ActivityDisposition' and ../../../../@name='ActivityType']">
<xsd:element ref="nc:ActivityDisposition">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:AddressRepresentation' and ../../../../@name='AddressType']">
<xsd:element ref="nc:AddressRepresentation">
<xsl:attribute name="minOccurs">1</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:BinaryObject' and ../../../../@name='BinaryType']">
<xsd:element ref="nc:BinaryObject">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:BinaryCaptureDate' and ../../../../@name='BinaryType']">
<xsd:element ref="nc:BinaryCaptureDate">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:BinaryCapturer' and ../../../../@name='BinaryType']">
<xsd:element ref="nc:BinaryCapturer">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:BinaryCategoryText' and ../../../../@name='BinaryType']">
<xsd:element ref="nc:BinaryCategoryText">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:BinaryDescriptionText' and ../../../../@name='BinaryType']">
<xsd:element ref="nc:BinaryDescriptionText">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:BinaryFormatStandardName' and ../../../../@name='BinaryType']">
<xsd:element ref="nc:BinaryFormatStandardName">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:BinaryLocationURI' and ../../../../@name='BinaryType']">
<xsd:element ref="nc:BinaryLocationURI">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:BinarySizeValue' and ../../../../@name='BinaryType']">
<xsd:element ref="nc:BinarySizeValue">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:BinaryCategoryText' and ../../../../@name='BinaryType']">
<xsd:element ref="nc:BinaryCategoryText">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:BiometricImage' and ../../../../@name='BiometricType']">
<xsd:element ref="nc:BiometricImage">
<xsl:attribute name="minOccurs">1</xsl:attribute>
<xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:BiometricCapturer' and ../../../../@name='BiometricType']">
<xsd:element ref="nc:BiometricCapturer">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:CapabilityDescriptionText' and ../../../../@name='CapabilityType']">
<xsd:element ref="nc:CapabilityDescriptionText">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:CaseDispositionDecisionCategoryText' and ../../../../@name='CaseDispositionType']">
<xsd:element ref="nc:CaseDispositionDecisionCategoryText">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:CaseDispositionFinalDate' and ../../../../@name='CaseDispositionType']">
<xsd:element ref="nc:CaseDispositionFinalDate">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:CaseTitleText' and ../../../../@name='CaseType']">
<xsd:element ref="nc:CaseTitleText">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:CaseCategoryText' and ../../../../@name='CaseType']">
<xsd:element ref="nc:CaseCategoryText">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:CaseTrackingID' and ../../../../@name='CaseType']">
<xsd:element ref="nc:CaseTrackingID">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:CaseDocketID' and ../../../../@name='CaseType']">
<xsd:element ref="nc:CaseDocketID">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ContactMeans' and ../../../../@name='ContactInformationType']">
<xsd:element ref="nc:ContactMeans">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ContactEntity' and ../../../../@name='ContactInformationType']">
<xsd:element ref="nc:ContactEntity">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ContactEntityDescriptionText' and ../../../../@name='ContactInformationType']">
<xsd:element ref="nc:ContactEntityDescriptionText">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ContactInformationDescriptionText' and ../../../../@name='ContactInformationType']">
<xsd:element ref="nc:ContactInformationDescriptionText">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ContactResponder' and ../../../../@name='ContactInformationType']">
<xsd:element ref="nc:ContactResponder">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ConveyanceRegistrationPlateIdentification' and ../../../../@name='ConveyanceType']">
<xsd:element ref="nc:ConveyanceRegistrationPlateIdentification">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:DNALocusCategoryText' and ../../../../@name='DNALocusType']">
<xsd:element ref="nc:DNALocusCategoryText">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:DNALocusValue' and ../../../../@name='DNALocusType']">
<xsd:element ref="nc:DNALocusValue">
<xsl:attribute name="minOccurs">1</xsl:attribute>
<xsl:attribute name="maxOccurs">2</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:DNALocus' and ../../../../@name='DNAType']">
<xsd:element ref="nc:DNALocus">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">14</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:DNAImage' and ../../../../@name='DNAType']">
<xsd:element ref="nc:DNAImage">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:StartDate' and ../../../../@name='DateRangeType']">
<xsd:element ref="nc:StartDate">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:EndDate' and ../../../../@name='DateRangeType']">
<xsd:element ref="nc:EndDate">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:DateRepresentation' and ../../../../@name='DateType']">
<xsd:element ref="nc:DateRepresentation">
<xsl:attribute name="minOccurs">1</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:DispositionDate' and ../../../../@name='DispositionType']">
<xsd:element ref="nc:DispositionDate">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:DispositionDescriptionText' and ../../../../@name='DispositionType']">
<xsd:element ref="nc:DispositionDescriptionText">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:DispositionText' and ../../../../@name='DispositionType']">
<xsd:element ref="nc:DispositionText">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:DispositionCategoryText' and ../../../../@name='DispositionType']">
<xsd:element ref="nc:DispositionCategoryText">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:DocumentApplicationName' and ../../../../@name='DocumentType']">
<xsd:element ref="nc:DocumentApplicationName">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:DocumentBinary' and ../../../../@name='DocumentType']">
<xsd:element ref="nc:DocumentBinary">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:DocumentDescriptionText' and ../../../../@name='DocumentType']">
<xsd:element ref="nc:DocumentDescriptionText">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:DocumentEffectiveDate' and ../../../../@name='DocumentType']">
<xsd:element ref="nc:DocumentEffectiveDate">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:DocumentFileControlID' and ../../../../@name='DocumentType']">
<xsd:element ref="nc:DocumentFileControlID">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:DocumentFiledDate' and ../../../../@name='DocumentType']">
<xsd:element ref="nc:DocumentFiledDate">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:DocumentIdentification' and ../../../../@name='DocumentType']">
<xsd:element ref="nc:DocumentIdentification">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:DocumentInformationCutOffDate' and ../../../../@name='DocumentType']">
<xsd:element ref="nc:DocumentInformationCutOffDate">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:DocumentPostDate' and ../../../../@name='DocumentType']">
<xsd:element ref="nc:DocumentPostDate">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:DocumentReceivedDate' and ../../../../@name='DocumentType']">
<xsd:element ref="nc:DocumentReceivedDate">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:DocumentSequenceID' and ../../../../@name='DocumentType']">
<xsd:element ref="nc:DocumentSequenceID">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:DocumentStatus' and ../../../../@name='DocumentType']">
<xsd:element ref="nc:DocumentStatus">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:DocumentLanguage' and ../../../../@name='DocumentType']">
<xsd:element ref="nc:DocumentLanguage">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:DocumentSubmitter' and ../../../../@name='DocumentType']">
<xsd:element ref="nc:DocumentSubmitter">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:DriverLicenseReference' and ../../../../@name='DriverLicenseAssociationType']">
<xsd:element ref="nc:DriverLicenseReference">
<xsl:attribute name="minOccurs">1</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PersonReference' and ../../../../@name='DriverLicenseAssociationType']">
<xsd:element ref="nc:PersonReference">
<xsl:attribute name="minOccurs">1</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:DriverLicenseIdentification' and ../../../../@name='DriverLicenseBaseType']">
<xsd:element ref="nc:DriverLicenseIdentification">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:DriverLicenseExpirationDate' and ../../../../@name='DriverLicenseBaseType']">
<xsd:element ref="nc:DriverLicenseExpirationDate">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:DriverLicenseIssueDate' and ../../../../@name='DriverLicenseBaseType']">
<xsd:element ref="nc:DriverLicenseIssueDate">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:DriverLicenseRestriction' and ../../../../@name='DriverLicenseBaseType']">
<xsd:element ref="nc:DriverLicenseRestriction">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:DriverLicenseCommercialClass' and ../../../../@name='DriverLicenseType']">
<xsd:element ref="nc:DriverLicenseCommercialClass">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:DriverLicenseWithdrawal' and ../../../../@name='DriverLicenseType']">
<xsd:element ref="nc:DriverLicenseWithdrawal">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:DriverLicenseWithdrawalEffectiveDate' and ../../../../@name='DriverLicenseWithdrawalType']">
<xsd:element ref="nc:DriverLicenseWithdrawalEffectiveDate">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:DrivingRestrictionValue' and ../../../../@name='DrivingRestrictionType']">
<xsd:element ref="nc:DrivingRestrictionValue">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:DrivingRestrictionEndDate' and ../../../../@name='DrivingRestrictionType']">
<xsd:element ref="nc:DrivingRestrictionEndDate">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:EntityRepresentation' and ../../../../@name='EntityType']">
<xsd:element ref="nc:EntityRepresentation">
<xsl:attribute name="minOccurs">1</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:FacilityIdentification' and ../../../../@name='FacilityType']">
<xsd:element ref="nc:FacilityIdentification">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:Fingerprint' and ../../../../@name='FingerprintSetType']">
<xsd:element ref="nc:Fingerprint">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:TelephoneNumberFullID' and ../../../../@name='FullTelephoneNumberType']">
<xsd:element ref="nc:TelephoneNumberFullID">
<xsl:attribute name="minOccurs">1</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:TelephoneSuffixID' and ../../../../@name='FullTelephoneNumberType']">
<xsd:element ref="nc:TelephoneSuffixID">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PersonGuardianReference' and ../../../../@name='GuardianAssociationType']">
<xsd:element ref="nc:PersonGuardianReference">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PersonDependentReference' and ../../../../@name='GuardianAssociationType']">
<xsd:element ref="nc:PersonDependentReference">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:IdentificationID' and ../../../../@name='IdentificationType']">
<xsd:element ref="nc:IdentificationID">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:IdentificationCategory' and ../../../../@name='IdentificationType']">
<xsd:element ref="nc:IdentificationCategory">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:IdentificationJurisdiction' and ../../../../@name='IdentificationType']">
<xsd:element ref="nc:IdentificationJurisdiction">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:IdentificationSourceText' and ../../../../@name='IdentificationType']">
<xsd:element ref="nc:IdentificationSourceText">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PersonParentReference' and ../../../../@name='ImmediateFamilyAssociationType']">
<xsd:element ref="nc:PersonParentReference">
<xsl:attribute name="minOccurs">1</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PersonChildReference' and ../../../../@name='ImmediateFamilyAssociationType']">
<xsd:element ref="nc:PersonChildReference">
<xsl:attribute name="minOccurs">1</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:FamilyKinshipCategory' and ../../../../@name='ImmediateFamilyAssociationType']">
<xsd:element ref="nc:FamilyKinshipCategory">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:IncidentLocation' and ../../../../@name='IncidentType']">
<xsd:element ref="nc:IncidentLocation">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:InsuranceCarrierName' and ../../../../@name='InsuranceType']">
<xsd:element ref="nc:InsuranceCarrierName">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:InsuranceActiveIndicator' and ../../../../@name='InsuranceType']">
<xsd:element ref="nc:InsuranceActiveIndicator">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:InsuranceCoverageCategory' and ../../../../@name='InsuranceType']">
<xsd:element ref="nc:InsuranceCoverageCategory">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:TelephoneCountryCodeID' and ../../../../@name='InternationalTelephoneNumberType']">
<xsd:element ref="nc:TelephoneCountryCodeID">
<xsl:attribute name="minOccurs">1</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:TelephoneNumberID' and ../../../../@name='InternationalTelephoneNumberType']">
<xsd:element ref="nc:TelephoneNumberID">
<xsl:attribute name="minOccurs">1</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:TelephoneSuffixID' and ../../../../@name='InternationalTelephoneNumberType']">
<xsd:element ref="nc:TelephoneSuffixID">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:RegistrationJurisdiction' and ../../../../@name='ItemRegistrationType']">
<xsd:element ref="nc:RegistrationJurisdiction">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ItemDescriptionText' and ../../../../@name='ItemType']">
<xsd:element ref="nc:ItemDescriptionText">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ItemOtherIdentification' and ../../../../@name='ItemType']">
<xsd:element ref="nc:ItemOtherIdentification">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ItemValue' and ../../../../@name='ItemType']">
<xsd:element ref="nc:ItemValue">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:LengthUnitCode' and ../../../../@name='LengthMeasureType']">
<xsd:element ref="nc:LengthUnitCode">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:LocationAddress' and ../../../../@name='LocationType']">
<xsd:element ref="nc:LocationAddress">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:LocationDescriptionText' and ../../../../@name='LocationType']">
<xsd:element ref="nc:LocationDescriptionText">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:LocationName' and ../../../../@name='LocationType']">
<xsd:element ref="nc:LocationName">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:MeasureValue' and ../../../../@name='MeasureType']">
<xsd:element ref="nc:MeasureValue">
<xsl:attribute name="minOccurs">1</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:MeasureUnitText' and ../../../../@name='MeasureType']">
<xsd:element ref="nc:MeasureUnitText">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:SensitivityText' and ../../../../@name='MetadataType']">
<xsd:element ref="nc:SensitivityText">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:TelephoneAreaCodeID' and ../../../../@name='NANPTelephoneNumberType']">
<xsd:element ref="nc:TelephoneAreaCodeID">
<xsl:attribute name="minOccurs">1</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:TelephoneExchangeID' and ../../../../@name='NANPTelephoneNumberType']">
<xsd:element ref="nc:TelephoneExchangeID">
<xsl:attribute name="minOccurs">1</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:TelephoneLineID' and ../../../../@name='NANPTelephoneNumberType']">
<xsd:element ref="nc:TelephoneLineID">
<xsl:attribute name="minOccurs">1</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:TelephoneSuffixID' and ../../../../@name='NANPTelephoneNumberType']">
<xsd:element ref="nc:TelephoneSuffixID">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:FinancialObligationExemptionAmount' and ../../../../@name='ObligationExemptionType']">
<xsd:element ref="nc:FinancialObligationExemptionAmount">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ObligationExemptionDescriptionText' and ../../../../@name='ObligationExemptionType']">
<xsd:element ref="nc:ObligationExemptionDescriptionText">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ObligationEndDate' and ../../../../@name='ObligationRecurrenceType']">
<xsd:element ref="nc:ObligationEndDate">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ObligationPeriodText' and ../../../../@name='ObligationRecurrenceType']">
<xsd:element ref="nc:ObligationPeriodText">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ObligationStartDate' and ../../../../@name='ObligationRecurrenceType']">
<xsd:element ref="nc:ObligationStartDate">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ObligationDue' and ../../../../@name='ObligationType']">
<xsd:element ref="nc:ObligationDue">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ObligationExemption' and ../../../../@name='ObligationType']">
<xsd:element ref="nc:ObligationExemption">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ObligationRequirementDescriptionText' and ../../../../@name='ObligationType']">
<xsd:element ref="nc:ObligationRequirementDescriptionText">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ObligationCategoryText' and ../../../../@name='ObligationType']">
<xsd:element ref="nc:ObligationCategoryText">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ObligationEntity' and ../../../../@name='ObligationType']">
<xsd:element ref="nc:ObligationEntity">
<xsl:attribute name="minOccurs">1</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ObligationRecipient' and ../../../../@name='ObligationType']">
<xsd:element ref="nc:ObligationRecipient">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ObligationRecurrence' and ../../../../@name='ObligationType']">
<xsd:element ref="nc:ObligationRecurrence">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:OrganizationReference' and ../../../../@name='OrganizationAssociationType']">
<xsd:element ref="nc:OrganizationReference">
<xsl:attribute name="minOccurs">2</xsl:attribute>
<xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:OrganizationReference' and ../../../../@name='OrganizationContactInformationAssociationType']">
<xsd:element ref="nc:OrganizationReference">
<xsl:attribute name="minOccurs">1</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ContactInformationReference' and ../../../../@name='OrganizationContactInformationAssociationType']">
<xsd:element ref="nc:ContactInformationReference">
<xsl:attribute name="minOccurs">1</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ContactInformationIsPrimaryIndicator' and ../../../../@name='OrganizationContactInformationAssociationType']">
<xsd:element ref="nc:ContactInformationIsPrimaryIndicator">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ContactInformationIsEmergencyIndicator' and ../../../../@name='OrganizationContactInformationAssociationType']">
<xsd:element ref="nc:ContactInformationIsEmergencyIndicator">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ContactInformationIsDayIndicator' and ../../../../@name='OrganizationContactInformationAssociationType']">
<xsd:element ref="nc:ContactInformationIsDayIndicator">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ContactInformationIsEveningIndicator' and ../../../../@name='OrganizationContactInformationAssociationType']">
<xsd:element ref="nc:ContactInformationIsEveningIndicator">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ContactInformationIsNightIndicator' and ../../../../@name='OrganizationContactInformationAssociationType']">
<xsd:element ref="nc:ContactInformationIsNightIndicator">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:OrganizationIdentification' and ../../../../@name='OrganizationType']">
<xsd:element ref="nc:OrganizationIdentification">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:OrganizationLocation' and ../../../../@name='OrganizationType']">
<xsd:element ref="nc:OrganizationLocation">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:OrganizationName' and ../../../../@name='OrganizationType']">
<xsd:element ref="nc:OrganizationName">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:OrganizationPrimaryContactInformation' and ../../../../@name='OrganizationType']">
<xsd:element ref="nc:OrganizationPrimaryContactInformation">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:OrganizationSubUnitName' and ../../../../@name='OrganizationType']">
<xsd:element ref="nc:OrganizationSubUnitName">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:OrganizationTaxIdentification' and ../../../../@name='OrganizationType']">
<xsd:element ref="nc:OrganizationTaxIdentification">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:OrganizationUnitName' and ../../../../@name='OrganizationType']">
<xsd:element ref="nc:OrganizationUnitName">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PersonReference' and ../../../../@name='PersonAssociationType']">
<xsd:element ref="nc:PersonReference">
<xsl:attribute name="minOccurs">2</xsl:attribute>
<xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PersonReference' and ../../../../@name='PersonContactInformationAssociationType']">
<xsd:element ref="nc:PersonReference">
<xsl:attribute name="minOccurs">1</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ContactInformationReference' and ../../../../@name='PersonContactInformationAssociationType']">
<xsd:element ref="nc:ContactInformationReference">
<xsl:attribute name="minOccurs">1</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ContactInformationIsPrimaryIndicator' and ../../../../@name='PersonContactInformationAssociationType']">
<xsd:element ref="nc:ContactInformationIsPrimaryIndicator">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ContactInformationIsHomeIndicator' and ../../../../@name='PersonContactInformationAssociationType']">
<xsd:element ref="nc:ContactInformationIsHomeIndicator">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ContactInformationIsWorkIndicator' and ../../../../@name='PersonContactInformationAssociationType']">
<xsd:element ref="nc:ContactInformationIsWorkIndicator">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ContactInformationIsEmergencyIndicator' and ../../../../@name='PersonContactInformationAssociationType']">
<xsd:element ref="nc:ContactInformationIsEmergencyIndicator">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ContactInformationIsDayIndicator' and ../../../../@name='PersonContactInformationAssociationType']">
<xsd:element ref="nc:ContactInformationIsDayIndicator">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ContactInformationIsEveningIndicator' and ../../../../@name='PersonContactInformationAssociationType']">
<xsd:element ref="nc:ContactInformationIsEveningIndicator">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ContactInformationIsNightIndicator' and ../../../../@name='PersonContactInformationAssociationType']">
<xsd:element ref="nc:ContactInformationIsNightIndicator">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:EmployeeReference' and ../../../../@name='PersonEmploymentAssociationType']">
<xsd:element ref="nc:EmployeeReference">
<xsl:attribute name="minOccurs">1</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:Employer' and ../../../../@name='PersonEmploymentAssociationType']">
<xsd:element ref="nc:Employer">
<xsl:attribute name="minOccurs">1</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:Language' and ../../../../@name='PersonLanguageType']">
<xsd:element ref="nc:Language">
<xsl:attribute name="minOccurs">1</xsl:attribute>
<xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PersonNamePrefixText' and ../../../../@name='PersonNameType']">
<xsd:element ref="nc:PersonNamePrefixText">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PersonGivenName' and ../../../../@name='PersonNameType']">
<xsd:element ref="nc:PersonGivenName">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PersonMiddleName' and ../../../../@name='PersonNameType']">
<xsd:element ref="nc:PersonMiddleName">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PersonSurName' and ../../../../@name='PersonNameType']">
<xsd:element ref="nc:PersonSurName">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PersonNameSuffixText' and ../../../../@name='PersonNameType']">
<xsd:element ref="nc:PersonNameSuffixText">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PersonMaidenName' and ../../../../@name='PersonNameType']">
<xsd:element ref="nc:PersonMaidenName">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PersonFullName' and ../../../../@name='PersonNameType']">
<xsd:element ref="nc:PersonFullName">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PersonReference' and ../../../../@name='PersonOrganizationAssociationType']">
<xsd:element ref="nc:PersonReference">
<xsl:attribute name="minOccurs">1</xsl:attribute>
<xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:OrganizationReference' and ../../../../@name='PersonOrganizationAssociationType']">
<xsd:element ref="nc:OrganizationReference">
<xsl:attribute name="minOccurs">1</xsl:attribute>
<xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PersonBirthDate' and ../../../../@name='PersonType']">
<xsd:element ref="nc:PersonBirthDate">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PersonCapability' and ../../../../@name='PersonType']">
<xsd:element ref="nc:PersonCapability">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PersonCitizenship' and ../../../../@name='PersonType']">
<xsd:element ref="nc:PersonCitizenship">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PersonDNA' and ../../../../@name='PersonType']">
<xsd:element ref="nc:PersonDNA">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PersonEthnicity' and ../../../../@name='PersonType']">
<xsd:element ref="nc:PersonEthnicity">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PersonEyeColor' and ../../../../@name='PersonType']">
<xsd:element ref="nc:PersonEyeColor">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PersonFingerprintSet' and ../../../../@name='PersonType']">
<xsd:element ref="nc:PersonFingerprintSet">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PersonHairColor' and ../../../../@name='PersonType']">
<xsd:element ref="nc:PersonHairColor">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PersonHeightMeasure' and ../../../../@name='PersonType']">
<xsd:element ref="nc:PersonHeightMeasure">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PersonLanguageEnglishIndicator' and ../../../../@name='PersonType']">
<xsd:element ref="nc:PersonLanguageEnglishIndicator">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PersonName' and ../../../../@name='PersonType']">
<xsd:element ref="nc:PersonName">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PersonOtherIdentification' and ../../../../@name='PersonType']">
<xsd:element ref="nc:PersonOtherIdentification">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PersonPhysicalFeature' and ../../../../@name='PersonType']">
<xsd:element ref="nc:PersonPhysicalFeature">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PersonPrimaryLanguage' and ../../../../@name='PersonType']">
<xsd:element ref="nc:PersonPrimaryLanguage">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PersonRace' and ../../../../@name='PersonType']">
<xsd:element ref="nc:PersonRace">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PersonSex' and ../../../../@name='PersonType']">
<xsd:element ref="nc:PersonSex">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PersonStateIdentification' and ../../../../@name='PersonType']">
<xsd:element ref="nc:PersonStateIdentification">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PersonTaxIdentification' and ../../../../@name='PersonType']">
<xsd:element ref="nc:PersonTaxIdentification">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PersonWeightMeasure' and ../../../../@name='PersonType']">
<xsd:element ref="nc:PersonWeightMeasure">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PersonUnionCategory' and ../../../../@name='PersonUnionAssociationType']">
<xsd:element ref="nc:PersonUnionCategory">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PersonUnionStatus' and ../../../../@name='PersonUnionAssociationType']">
<xsd:element ref="nc:PersonUnionStatus">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:PhysicalFeatureCategory' and ../../../../@name='PhysicalFeatureType']">
<xsd:element ref="nc:PhysicalFeatureCategory">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ActivityReference' and ../../../../@name='RelatedActivityAssociationType']">
<xsd:element ref="nc:ActivityReference">
<xsl:attribute name="minOccurs">2</xsl:attribute>
<xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ScheduleActivityText' and ../../../../@name='ScheduleDayType']">
<xsd:element ref="nc:ScheduleActivityText">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ScheduleDate' and ../../../../@name='ScheduleDayType']">
<xsd:element ref="nc:ScheduleDate">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ScheduleDayEndTime' and ../../../../@name='ScheduleDayType']">
<xsd:element ref="nc:ScheduleDayEndTime">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ScheduleDayStartTime' and ../../../../@name='ScheduleDayType']">
<xsd:element ref="nc:ScheduleDayStartTime">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:SpeedUnitCode' and ../../../../@name='SpeedMeasureType']">
<xsd:element ref="nc:SpeedUnitCode">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:StatusText' and ../../../../@name='StatusType']">
<xsd:element ref="nc:StatusText">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:StatusDate' and ../../../../@name='StatusType']">
<xsd:element ref="nc:StatusDate">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:StatusDescriptionText' and ../../../../@name='StatusType']">
<xsd:element ref="nc:StatusDescriptionText">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:StreetFullText' and ../../../../@name='StreetType']">
<xsd:element ref="nc:StreetFullText">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:StreetNumberText' and ../../../../@name='StreetType']">
<xsd:element ref="nc:StreetNumberText">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:StreetPredirectionalText' and ../../../../@name='StreetType']">
<xsd:element ref="nc:StreetPredirectionalText">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:StreetName' and ../../../../@name='StreetType']">
<xsd:element ref="nc:StreetName">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:StreetCategoryText' and ../../../../@name='StreetType']">
<xsd:element ref="nc:StreetCategoryText">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:StreetPostdirectionalText' and ../../../../@name='StreetType']">
<xsd:element ref="nc:StreetPostdirectionalText">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:StreetExtensionText' and ../../../../@name='StreetType']">
<xsd:element ref="nc:StreetExtensionText">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:AddressRecipientName' and ../../../../@name='StructuredAddressType']">
<xsd:element ref="nc:AddressRecipientName">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:AddressDeliveryPoint' and ../../../../@name='StructuredAddressType']">
<xsd:element ref="nc:AddressDeliveryPoint">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:LocationCityName' and ../../../../@name='StructuredAddressType']">
<xsd:element ref="nc:LocationCityName">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:LocationCounty' and ../../../../@name='StructuredAddressType']">
<xsd:element ref="nc:LocationCounty">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:LocationState' and ../../../../@name='StructuredAddressType']">
<xsd:element ref="nc:LocationState">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:LocationCountry' and ../../../../@name='StructuredAddressType']">
<xsd:element ref="nc:LocationCountry">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:LocationPostalCode' and ../../../../@name='StructuredAddressType']">
<xsd:element ref="nc:LocationPostalCode">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:LocationPostalExtensionCode' and ../../../../@name='StructuredAddressType']">
<xsd:element ref="nc:LocationPostalExtensionCode">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:SupervisionFacility' and ../../../../@name='SupervisionType']">
<xsd:element ref="nc:SupervisionFacility">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ItemColor' and ../../../../@name='TangibleItemType']">
<xsd:element ref="nc:ItemColor">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ItemModelYearDate' and ../../../../@name='TangibleItemType']">
<xsd:element ref="nc:ItemModelYearDate">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:ItemStyle' and ../../../../@name='TangibleItemType']">
<xsd:element ref="nc:ItemStyle">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:TelephoneNumberRepresentation' and ../../../../@name='TelephoneNumberType']">
<xsd:element ref="nc:TelephoneNumberRepresentation">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:VehicleIdentification' and ../../../../@name='VehicleType']">
<xsd:element ref="nc:VehicleIdentification">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:VehicleMakeCode' and ../../../../@name='VehicleType']">
<xsd:element ref="nc:VehicleMakeCode">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:VehicleModelCode' and ../../../../@name='VehicleType']">
<xsd:element ref="nc:VehicleModelCode">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="xsd:element[@ref='nc:WeightUnitCode' and ../../../../@name='WeightMeasureType']">
<xsd:element ref="nc:WeightUnitCode">
<xsl:attribute name="minOccurs">0</xsl:attribute>
<xsl:attribute name="maxOccurs">1</xsl:attribute>
</xsd:element>
</xsl:template>
<xsl:template match="/xsd:schema">
<xsl:copy>
<xsl:for-each select="@*">
<xsl:copy />
</xsl:for-each>
<xsl:apply-templates />
</xsl:copy>
</xsl:template>
<xsl:template match="xsd:complexType">
<xsl:copy>
<xsl:for-each select="@*">
<xsl:copy />
</xsl:for-each>
<xsl:apply-templates />
</xsl:copy>
</xsl:template>
<xsl:template match="xsd:simpleType">
<xsl:copy>
<xsl:for-each select="@*">
<xsl:copy />
</xsl:for-each>
<xsl:apply-templates />
</xsl:copy>
</xsl:template>
<xsl:template match="xsd:complexContent">
<xsl:copy>
<xsl:for-each select="@*">
<xsl:copy />
</xsl:for-each>
<xsl:apply-templates />
</xsl:copy>
</xsl:template>
<xsl:template match="xsd:extension">
<xsl:copy>
<xsl:for-each select="@*">
<xsl:copy />
</xsl:for-each>
<xsl:apply-templates />
</xsl:copy>
</xsl:template>
<xsl:template match="xsd:restriction">
<xsl:copy>
<xsl:for-each select="@*">
<xsl:copy />
</xsl:for-each>
<xsl:apply-templates />
</xsl:copy>
</xsl:template>
<xsl:template match="xsd:sequence">
<xsl:copy>
<xsl:for-each select="@*">
<xsl:copy />
</xsl:for-each>
<xsl:apply-templates />
</xsl:copy>
</xsl:template>
<xsl:template match="xsd:minInclusive">
<xsl:copy>
<xsl:for-each select="@*">
<xsl:copy />
</xsl:for-each>
<xsl:apply-templates />
</xsl:copy>
</xsl:template>
<xsl:template match="xsd:simpleContent">
<xsl:copy-of select="." />
</xsl:template>
<xsl:template match="/xsd:schema/xsd:annotation">
<xsl:copy-of select="." />
</xsl:template>
<!-- <xsl:template match="/xsd:schema/xsd:complexType">
<xsl:copy-of select="." />
</xsl:template> 
<xsl:template match="/xsd:schema/xsd:simpleType">
<xsl:copy-of select="." />
</xsl:template> -->
<xsl:template match="/xsd:attribute/xsd:annotation">
<xsl:copy-of select="." />
</xsl:template>
<xsl:template match="/xsd:attributeGroup/xsd:annotation">
<xsl:copy-of select="." />
</xsl:template>
<xsl:template match="xsd:annotation" />
<xsl:template match="xsd:import">
<xsl:copy>
<xsl:attribute name="namespace">
<xsl:value-of select="@namespace" />
</xsl:attribute>
<xsl:attribute name="schemaLocation">
<xsl:value-of select="@schemaLocation" />
</xsl:attribute>
<xsl:apply-templates />
</xsl:copy>
</xsl:template>
<xsl:template match="xsd:attribute">
<xsl:copy-of select="." />
</xsl:template>
<xsl:template match="xsd:attributeGroup">
<xsl:copy-of select="." />
</xsl:template>
<xsl:template match="xsd:element">
<xsl:copy-of select="." />
</xsl:template>
</xsl:stylesheet>
