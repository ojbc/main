/*
 * Unless explicitly acquired and licensed from Licensor under another license, the contents of
 * this file are subject to the Reciprocal Public License ("RPL") Version 1.5, or subsequent
 * versions as allowed by the RPL, and You may not copy or use this file in either source code
 * or executable form, except in compliance with the terms and conditions of the RPL
 *
 * All software distributed under the RPL is provided strictly on an "AS IS" basis, WITHOUT
 * WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY DISCLAIMS ALL SUCH
 * WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific language
 * governing rights and limitations under the RPL.
 *
 * http://opensource.org/licenses/RPL-1.5
 *
 * Copyright 2012-2017 Open Justice Broker Consortium
 */
package org.ojbc.util.helper;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.util.xml.namespaces.NIEMNamespaces;
import org.ojbc.util.xml.namespaces.OJBNamespaces;
import org.ojbc.web.SearchFieldMetadata;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class NIEMXMLUtils {
		
	//        <nc:LastUpdatedDate>
	//            <nc:DateTime>2012-06-20T12:20:00</nc:DateTime>
	//        </nc:LastUpdatedDate>
	public static Element createElementDateTime(Document doc, String wrapperElementName, DateTime dateTime)
	{
		Element dateTimeWrapperElement = doc.createElementNS(NIEMNamespaces.NC_20_NS, wrapperElementName);
		
		Element dateTimeElement = doc.createElementNS(NIEMNamespaces.NC_20_NS, "DateTime");
		dateTimeElement.setTextContent(dateTime.toString("yyyy-MM-dd'T'HH:mm:ss"));
		
		dateTimeWrapperElement.appendChild(dateTimeElement);
		
		return dateTimeWrapperElement;
	}

	//    <nc:PersonBirthDate>
	//    	<nc:Date>1957-04-01</nc:Date>
	//    </nc:PersonBirthDate>
	public static Element createElementDate(Document doc, String wrapperElementName, DateTime dateTime)
	{
		Element dateWrapperElement = doc.createElementNS(NIEMNamespaces.NC_20_NS, wrapperElementName);
		
		Element datelement = XmlUtils.appendElement(dateWrapperElement, OjbcNamespaceContext.NS_NC, "Date");
		datelement.setTextContent(dateTime.toString("yyyy-MM-dd"));
		
		dateWrapperElement.appendChild(datelement);
		
		return dateWrapperElement;
	}	

	//		<nc:StartDate>
	//			<nc:Year>2005</nc:Year>
	//		</nc:StartDate>
	public static Element createElementDateYear(Document doc, String wrapperElementName, Integer year)
	{
		Element dateWrapperElement = doc.createElementNS(NIEMNamespaces.NC_20_NS, wrapperElementName);
		
		Element datelement = doc.createElementNS(NIEMNamespaces.NC_20_NS, "Year");
		datelement.setTextContent(year.toString());
		
		dateWrapperElement.appendChild(datelement);
		
		return dateWrapperElement;
	}	

	public static Element createBirthDateRange(Document doc, Element parentElement, DateTime personDateOfBirthRangeStart, DateTime personDateOfBirthRangeEnd)
	{
		Element personDateOfBirthRangeStartElement = createElementDate(doc, "StartDate", personDateOfBirthRangeStart);
		Element personDateOfBirthRangeEndElement = createElementDate(doc, "EndDate", personDateOfBirthRangeEnd);

		parentElement.appendChild(personDateOfBirthRangeStartElement);
		parentElement.appendChild(personDateOfBirthRangeEndElement);
		
		return parentElement;
	}
	
	public static Element createNC20Element(Document doc, String elementName, String elementValue)
	{
		Element returnElement = doc.createElementNS(NIEMNamespaces.NC_20_NS, elementName);
		returnElement.setTextContent(elementValue);
		return returnElement;
	}
	
	public static Element createMeasureRangeValueOrTextElement(Document doc,
			Integer rangeStart,
			Integer rangeEnd, 
			Integer measureTextValue,
			String lengthCode,
			String weightCode) {
		
		Element heightOrWeightElement = null;
		
		if (StringUtils.isNotBlank(lengthCode))
		{
			heightOrWeightElement = doc.createElementNS(NIEMNamespaces.NC_20_NS, "PersonHeightMeasure");
		}

		if (StringUtils.isNotBlank(weightCode))
		{
			heightOrWeightElement = doc.createElementNS(NIEMNamespaces.NC_20_NS, "PersonWeightMeasure");
		}

		Element measureRangeValueElement = null;
		Element measureTextElement = null;
		
		if (rangeStart != null && rangeEnd != null)
		{	
			measureRangeValueElement = doc.createElementNS(NIEMNamespaces.NC_20_NS, "MeasureRangeValue");
			
			Element rangeMinimumElement = doc.createElementNS(NIEMNamespaces.NC_20_NS, "RangeMinimumValue");
			rangeMinimumElement.setTextContent(String.valueOf(rangeStart));
			measureRangeValueElement.appendChild(rangeMinimumElement);
			
			Element rangeMaximumElement = doc.createElementNS(NIEMNamespaces.NC_20_NS, "RangeMaximumValue");
			rangeMaximumElement.setTextContent(String.valueOf(rangeEnd));
			measureRangeValueElement.appendChild(rangeMaximumElement);
			
			heightOrWeightElement.appendChild(measureRangeValueElement);
		}
		
		if (measureTextValue != null)
		{
			measureTextElement = doc.createElementNS(NIEMNamespaces.NC_20_NS, "MeasureText");
			measureTextElement.setTextContent(String.valueOf(measureTextValue));
			
			heightOrWeightElement.appendChild(measureTextElement);
		}	
		
		if (StringUtils.isNotBlank(lengthCode))
		{
			Element lengthUnitCode = doc.createElementNS(NIEMNamespaces.NC_20_NS, "LengthUnitCode");
			lengthUnitCode.setTextContent(lengthCode);
			heightOrWeightElement.appendChild(lengthUnitCode);
		}	
		
		if (StringUtils.isNotBlank(weightCode))
		{
			Element weightUnitCode = doc.createElementNS(NIEMNamespaces.NC_20_NS, "WeightUnitCode");
			weightUnitCode.setTextContent(weightCode);
			heightOrWeightElement.appendChild(weightUnitCode);
		}
		
		return heightOrWeightElement;
		
	}
	
	public static Element createPersonNameElement(Document doc, String personGivenName,
			SearchFieldMetadata personGivenNameMetaData,
			String personMiddleName, String personSurName,
			SearchFieldMetadata personSurNameMetaData) {

		//Set Person Name
		Element personNameElement = doc.createElementNS(NIEMNamespaces.NC_20_NS, "PersonName");

		if (StringUtils.isNotBlank(personGivenName))
		{	
			Element personGivenNameElement = doc.createElementNS(NIEMNamespaces.NC_20_NS, "PersonGivenName");
			personGivenNameElement.setTextContent(personGivenName);
			personNameElement.appendChild(personGivenNameElement);
			
			if (personGivenNameMetaData == SearchFieldMetadata.ExactMatch)
			{
				personGivenNameElement.setAttributeNS(NIEMNamespaces.STRUCT_NS, "metadata", "SM001");
			}
			if (personGivenNameMetaData == SearchFieldMetadata.StartsWith)
			{
				personGivenNameElement.setAttributeNS(NIEMNamespaces.STRUCT_NS, "metadata", "SM002");
			}	
			
		}

		if (StringUtils.isNotBlank(personMiddleName))
		{	
			Element personMiddleNameElement = doc.createElementNS(NIEMNamespaces.NC_20_NS, "PersonMiddleName");
			personMiddleNameElement.setTextContent(personMiddleName);
			personNameElement.appendChild(personMiddleNameElement);
		}

		if (StringUtils.isNotBlank(personSurName))
		{	
			Element personSurNameElement = doc.createElementNS(NIEMNamespaces.NC_20_NS, "PersonSurName");
			personSurNameElement.setTextContent(personSurName);
			personNameElement.appendChild(personSurNameElement);
			
			if (personSurNameMetaData == SearchFieldMetadata.ExactMatch)
			{
				personSurNameElement.setAttributeNS(NIEMNamespaces.STRUCT_NS, "metadata", "SM001");
			}
			if (personSurNameMetaData == SearchFieldMetadata.StartsWith)
			{
				personSurNameElement.setAttributeNS(NIEMNamespaces.STRUCT_NS, "metadata", "SM002");
			}	
			
		}
		
		return personNameElement;
	}	

	public static Element createPersonsSearchRequestElement(Document doc, String id)
	{
		Element element = doc.createElementNS(OJBNamespaces.PERSON_SEARCH_REQUEST, "PersonSearchRequest");
		element.setAttributeNS(NIEMNamespaces.STRUCT_NS, "id", "SM003");
		
		return element;
	}

	//<nc:PersonSSNIdentification>
	//	<nc:IdentificationID>123456789</nc:IdentificationID>
	//</nc:PersonSSNIdentification>
	public static Element createPersonSSNElement(Document doc,
			String personSocialSecurityNumber) {
		Element personSSNElement = doc.createElementNS(NIEMNamespaces.NC_20_NS, "PersonSSNIdentification");
		Element	identificationIDElement  = doc.createElementNS(NIEMNamespaces.NC_20_NS, "IdentificationID");
		identificationIDElement.setTextContent(personSocialSecurityNumber.trim());
		
		personSSNElement.appendChild(identificationIDElement);
		
		return personSSNElement;
	}

	//<j:PersonAugmentation>
	//	<nc:DriverLicense>
	//		<nc:DriverLicenseIdentification>
	//			<nc:IdentificationID>12345</nc:IdentificationID>
	//			<nc:IdentificationSourceText>VT</nc:IdentificationSourceText>
	//		</nc:DriverLicenseIdentification>
	//	</nc:DriverLicense>
	//	<j:PersonFBIIdentification>
	//		<nc:IdentificationID>12345</nc:IdentificationID>
	//	</j:PersonFBIIdentification>
	//	<j:PersonStateFingerprintIdentification>
	//		<nc:IdentificationID>12345</nc:IdentificationID>
	//	</j:PersonStateFingerprintIdentification>
	//</j:PersonAugmentation>
	public static Element createPersonAugmentation(Document doc,
	String personDriversLicenseNumber,
	String personDriversLicenseIssuer,
	String personSID,
	String personFBINumber)
	{
		Element personAugmentationElement = doc.createElementNS(NIEMNamespaces.JXDM_41_NS, "PersonAugmentation");

		if (StringUtils.isNotBlank(personDriversLicenseNumber) || StringUtils.isNotBlank(personDriversLicenseIssuer))
		{	
			Element driversLicenseElement = doc.createElementNS(NIEMNamespaces.NC_20_NS, "DriverLicense");
			
			Element driversLicenseIdentificationElement = doc.createElementNS(NIEMNamespaces.NC_20_NS, "DriverLicenseIdentification");
			
			if (StringUtils.isNotBlank(personDriversLicenseNumber))
			{		
				Element identificationIDElement = doc.createElementNS(NIEMNamespaces.NC_20_NS, "IdentificationID");
				identificationIDElement.setTextContent(personDriversLicenseNumber);
				
				driversLicenseIdentificationElement.appendChild(identificationIDElement);
			}	
				
			if (StringUtils.isNotBlank(personDriversLicenseIssuer))
			{	
				Element identificationSourceTextElement = doc.createElementNS(NIEMNamespaces.NC_20_NS, "IdentificationSourceText");
				identificationSourceTextElement.setTextContent(personDriversLicenseIssuer);
			
				driversLicenseIdentificationElement.appendChild(identificationSourceTextElement);
			}
				
			driversLicenseElement.appendChild(driversLicenseIdentificationElement);
			
			personAugmentationElement.appendChild(driversLicenseElement);
		}	

		if (StringUtils.isNotBlank(personFBINumber))
		{	
			Element fbiNumberElement = doc.createElementNS(NIEMNamespaces.JXDM_41_NS, "PersonFBIIdentification");
			
			Element identificationIDElement = doc.createElementNS(NIEMNamespaces.NC_20_NS, "IdentificationID");
			identificationIDElement.setTextContent(personFBINumber);
			fbiNumberElement.appendChild(identificationIDElement);
			
			personAugmentationElement.appendChild(fbiNumberElement);
		}	
		
		if (StringUtils.isNotBlank(personSID))
		{
			Element personSIDElement = doc.createElementNS(NIEMNamespaces.JXDM_41_NS, "PersonStateFingerprintIdentification");
			
			Element identificationIDElement = doc.createElementNS(NIEMNamespaces.NC_20_NS, "IdentificationID");
			identificationIDElement.setTextContent(personSID);
			personSIDElement.appendChild(identificationIDElement);
			
			personAugmentationElement.appendChild(personSIDElement);
			
		}

		return personAugmentationElement;
	}
	
	//<ext:SearchMetadata s:id="SM001">
	//	<ext:SearchQualifierCode>exact</ext:SearchQualifierCode>
	//</ext:SearchMetadata>
	//<ext:SearchMetadata s:id="SM002">
	//	<ext:SearchQualifierCode>startsWith</ext:SearchQualifierCode>
	//</ext:SearchMetadata>

	public static Element createSearchMetaData(Document doc, String searchFieldMetaDataNamespace, SearchFieldMetadata searchFieldMetadata)
	{
		Element searchFieldMetadataElement = doc.createElementNS(searchFieldMetaDataNamespace, "SearchMetadata");
		
		if (searchFieldMetadata != null) {
		    switch(searchFieldMetadata) {
		    case ExactMatch:
		        searchFieldMetadataElement.setAttributeNS(NIEMNamespaces.STRUCT_NS, "id", "SM001");
		        break; 
		    case StartsWith:
		        searchFieldMetadataElement.setAttributeNS(NIEMNamespaces.STRUCT_NS, "id", "SM002");
		        break;
            default:
                break; 
		    }
		}

		Element searchFieldQualifierCodeElement = doc.createElementNS(searchFieldMetaDataNamespace, "SearchQualifierCode");
		
		if (searchFieldMetadata != null) {
		    searchFieldQualifierCodeElement.setTextContent(searchFieldMetadata.getMetadata());
		}
		
		searchFieldMetadataElement.appendChild(searchFieldQualifierCodeElement);
		
		return searchFieldMetadataElement;
	}
	
	//<ext:SearchMetadata s:id="SM003">
	//	<ext:SearchRequestOnBehalfOfText>John Doe</ext:SearchRequestOnBehalfOfText>
	//	<ext:SearchPurposeText>Criminal Justice</ext:SearchPurposeText>
	//</ext:SearchMetadata>
	
	public static Element createSearchMetaDataPurposeOnBehalfOf(Document doc, String searchFieldMetaDataNamespace, String id, String onBehalfOfText, String searchPurposeText)
	{
		Element searchFieldMetadataElement = doc.createElementNS(searchFieldMetaDataNamespace, "SearchMetadata");
		searchFieldMetadataElement.setAttributeNS(NIEMNamespaces.STRUCT_NS, "id", id);
		
		if (StringUtils.isNotBlank(onBehalfOfText))
		{
			Element searchRequestOnBehalfOfTextElement = doc.createElementNS(searchFieldMetaDataNamespace, "SearchRequestOnBehalfOfText");
			searchRequestOnBehalfOfTextElement.setTextContent(onBehalfOfText);
			searchFieldMetadataElement.appendChild(searchRequestOnBehalfOfTextElement);
		}	
				
		if (StringUtils.isNotBlank(searchPurposeText))
		{
			Element searchPurposeTextElement = doc.createElementNS(searchFieldMetaDataNamespace, "SearchPurposeText");
			searchPurposeTextElement.setTextContent(searchPurposeText);
			searchFieldMetadataElement.appendChild(searchPurposeTextElement);
		}
		
		return searchFieldMetadataElement;
	}

	public static Element createSourceSystemElement(Document doc, String sourceSystemNamespace,
			String sourceSystemName) {
		
		Element sourceSystemElement = doc.createElementNS(sourceSystemNamespace, "SourceSystemNameText");
		sourceSystemElement.setTextContent(sourceSystemName);
		return sourceSystemElement;
		
	} 
	
	//For Example
	//	<nc:ConveyanceRegistrationPlateIdentification>
	//		<nc:IdentificationID>ABC123</nc:IdentificationID>
	//	</nc:ConveyanceRegistrationPlateIdentification>
	public static Element createIdentificationElementWithParent(Document doc, String parentElementName, String identificationID) {
		Element parentElement = doc.createElementNS(NIEMNamespaces.NC_20_NS, parentElementName);
		Element	identificationIDElement  = doc.createElementNS(NIEMNamespaces.NC_20_NS, "IdentificationID");
		identificationIDElement.setTextContent(identificationID.trim());
		
		parentElement.appendChild(identificationIDElement);
		
		return parentElement;
	}
	
	//For Example
	//	<nc:ItemSerialIdentification>
	//		<nc:IdentificationID s:metadata="SM001">12345</nc:IdentificationID>
	//	</nc:ItemSerialIdentification>
	public static Element createIdentificationElementWithStructureAttrAndParent(Document doc, String parentElementName, String identificationID, String structureAttrName, String structureAttrValue) {
		Element parentElement = doc.createElementNS(NIEMNamespaces.NC_20_NS, parentElementName);
		Element	identificationIDElement  = doc.createElementNS(NIEMNamespaces.NC_20_NS, "IdentificationID");
		identificationIDElement.setTextContent(identificationID.trim());
		identificationIDElement.setAttributeNS(NIEMNamespaces.STRUCT_NS, structureAttrName, structureAttrValue);
		
		parentElement.appendChild(identificationIDElement);
		
		return parentElement;
	}

	//  For Example
	//	<ext:VehicleYearRange>
	//		<nc:StartDate>
	//			<nc:Year>2005</nc:Year>
	//		</nc:StartDate>
	//		<nc:EndDate>
	//			<nc:Year>2009</nc:Year>
	//		</nc:EndDate>
	//	</ext:VehicleYearRange>
	public static Element createYearRangeElementWithParent(Document doc,
			String parentElementName, String parentElementNamespace,
			Integer yearRangeStart, Integer yearRangeEnd) {
		
		Element parentElement = doc.createElementNS(parentElementNamespace, parentElementName);
		
		Element startYearElement = createElementDateYear(doc, "StartDate", yearRangeStart);
		Element endYearElement = createElementDateYear(doc, "EndDate", yearRangeEnd);
		
		parentElement.appendChild(startYearElement);
		parentElement.appendChild(endYearElement);
		
		return parentElement;
	}
	
	// For example
    // <Incident xmlns:ns0="http://niem.gov/niem/structures/2.0" ns0:id="I001" xmlns="http://ojbc.org/IEPD/Extensions/IncidentSearchRequest/1.0">
    // </Incident>
	public static Element createIncidentElement(Document doc, String structureId) {
		Element incidentElement = doc.createElementNS(OJBNamespaces.INCIDENT_SEARCH_REQUEST_EXT, "Incident");
		incidentElement.setAttributeNS(NIEMNamespaces.STRUCT_NS, "id", structureId);
		
		return incidentElement;
	}
	
	// For example
    // <Location xmlns:ns0="http://niem.gov/niem/structures/2.0" ns0:id="L001" xmlns="http://niem.gov/niem/niem-core/2.0">
    // </Location>
	public static Element createLocationElement(Document doc, String structureId) {
		Element locationElement = doc.createElementNS(NIEMNamespaces.NC_20_NS, "Location");
		locationElement.setAttributeNS(NIEMNamespaces.STRUCT_NS, "id", structureId);
		
		return locationElement;
	}
	
	// For example
    // <LocationAddress>
	// 		<StructuredAddress xmlns="http://ojbc.org/IEPD/Extensions/IncidentSearchRequest/1.0">
	// 		</StructuredAddress>
	// </LocationAddress>
	public static Element createLocationStructuredAddressElement(Document doc, String structuredAddressElementNamespace) {
		Element locationAddressElement = doc.createElementNS(NIEMNamespaces.NC_20_NS, "LocationAddress");
		
		Element structuredAddressElement = doc.createElementNS(structuredAddressElementNamespace, "StructuredAddress");
		
		locationAddressElement.appendChild(structuredAddressElement);
		
		return locationAddressElement;
	}

	// For Example
	//	<nc:ActivityDateRange>
	//		<nc:StartDate>
	//			<nc:DateTime>2012-04-01T12:00:00</nc:DateTime>
	//		</nc:StartDate>
	//		<nc:EndDate>
	//			<nc:DateTime>2012-04-01T12:00:00</nc:DateTime>
	//		</nc:EndDate>
	//	</nc:ActivityDateRange>
	public static Element createNC20DateRangeElementWithParent(Document doc, String parentElementName, 
			DateTime dateStart, DateTime dateEnd) {
		
		Element parentElement = doc.createElementNS(NIEMNamespaces.NC_20_NS, parentElementName);
		
		if (dateStart != null)
		{	
			Element startDateElement = createElementDateTime(doc, "StartDate", dateStart);
			parentElement.appendChild(startDateElement);
		}

		if (dateEnd != null)
		{	
			Element endDateElement = createElementDateTime(doc, "EndDate", dateEnd);
			parentElement.appendChild(endDateElement);
		}	
		
		
		return parentElement;
	}
	
	// For Example
	//	<nc:ActivityDateRange>
	//		<nc:StartDate>
	//			<nc:Date>2012-04-01</nc:Date>
	//		</nc:StartDate>
	//		<nc:EndDate>
	//			<nc:DateT>2012-04-01</nc:Date>
	//		</nc:EndDate>
	//	</nc:ActivityDateRange>
	public static Element createNC20DateOnlyRangeElementWithParent(Document doc, String parentElementName, 
			DateTime dateStart, DateTime dateEnd) {
		
		Element parentElement = doc.createElementNS(OjbcNamespaceContext.NS_NC, OjbcNamespaceContext.NS_PREFIX_NC + ":" + parentElementName);
		
		if (dateStart != null)
		{	
			Element startDateElement = createElementDate(doc, "StartDate", dateStart);
			startDateElement.setPrefix("nc");
			parentElement.appendChild(startDateElement);
		}

		if (dateEnd != null)
		{	
			Element endDateElement = createElementDate(doc, "EndDate", dateEnd);
			endDateElement.setPrefix("nc");
			parentElement.appendChild(endDateElement);
		}	
		
		
		return parentElement;
	}	

	// For Example
	//	<j:ActivityLocationAssociation>
	//		<nc:ActivityReference s:ref="I001" />
	//		<nc:LocationReference s:ref="L001" />
	//	</j:ActivityLocationAssociation>
	public static Element createActivityLocationAssociationElement(
			Document doc, String activityRef, String locationRef) {
		
		Element parentElement = doc.createElementNS(NIEMNamespaces.JXDM_41_NS, "ActivityLocationAssociation");
		
		Element activityReferenceElement = doc.createElementNS(NIEMNamespaces.NC_20_NS, "ActivityReference");
		activityReferenceElement.setAttributeNS(NIEMNamespaces.STRUCT_NS, "ref", activityRef);
		
		Element locationReferenceElement = doc.createElementNS(NIEMNamespaces.NC_20_NS, "LocationReference");
		locationReferenceElement.setAttributeNS(NIEMNamespaces.STRUCT_NS, "ref", locationRef);
		
		parentElement.appendChild(activityReferenceElement);
		parentElement.appendChild(locationReferenceElement);
		
		return parentElement;
	}
	
	// For Example
	// <ext:Firearm xmlns:ns0="http://niem.gov/niem/structures/2.0" ns0:id="FIREARM" xmlns="http://ojbc.org/IEPD/Extensions/FirearmSearchRequest/1.0">
	// </ext:Firearm>
	public static Element createFirearmElement(Document doc, String structureId) {
		Element firearmElement = doc.createElementNS(OJBNamespaces.FIREARM_SEARCH_REQUEST_EXT, "Firearm");
		firearmElement.setAttributeNS(NIEMNamespaces.STRUCT_NS, "id", structureId);
		
		return firearmElement;
	}
	
	// For Example
	// <ItemRegistration xmlns:ns0="http://niem.gov/niem/structures/2.0" ns0:id="REGISTRATION" xmlns="http://ojbc.org/IEPD/Extensions/FirearmSearchRequest/1.0">
	//    <RegistrationIdentification xmlns="http://niem.gov/niem/niem-core/2.0">
	//        <IdentificationID>Registration Number</IdentificationID>
	//    </RegistrationIdentification>
	//    <LocationCountyName xmlns="http://niem.gov/niem/niem-core/2.0">County</LocationCountyName>
	// </ItemRegistration>
	public static Element createFirearmItemRegistration(Document doc, String structureId, String registrationId, String countyName, Boolean currentRegIndicator) {
		Element itemRegistrationElement = doc.createElementNS(OJBNamespaces.FIREARM_SEARCH_REQUEST_EXT, "ItemRegistration");
		itemRegistrationElement.setAttributeNS(NIEMNamespaces.STRUCT_NS, "id", structureId);
		
		if (StringUtils.isNotBlank(registrationId)) {
			Element registrationIdElement = createIdentificationElementWithParent(doc, "RegistrationIdentification", registrationId);
			itemRegistrationElement.appendChild(registrationIdElement);
		}
		
		if (StringUtils.isNotBlank(countyName)) {
			Element countyNameElement = createNC20Element(doc, "LocationCountyName", countyName);
			itemRegistrationElement.appendChild(countyNameElement);
		}
		
		if (currentRegIndicator != null) {
			Element currentRegIndicatorElement = doc.createElementNS(OJBNamespaces.FIREARM_SEARCH_REQUEST_EXT, "CurrentRegistrationIndicator");
			currentRegIndicatorElement.setTextContent(currentRegIndicator.toString());
			itemRegistrationElement.appendChild(currentRegIndicatorElement);
		}
		
		return itemRegistrationElement;
	}
	
	// For Example
	//	<nc:PropertyRegistrationAssociation>
	//		<nc:ActivityReference s:ref="REGISTRATION" />
	//		<nc:LocationReference s:ref="FIREARM" />
	//	</nc:PropertyRegistrationAssociation>
	public static Element createPropertyRegistrationAssociationElement(
			Document doc, String itemRegistrationRef, String itemRef) {
		
		Element parentElement = doc.createElementNS(NIEMNamespaces.NC_20_NS, "PropertyRegistrationAssociation");
		
		Element itemRegistrationReferenceElement = doc.createElementNS(NIEMNamespaces.NC_20_NS, "ItemRegistrationReference");
		itemRegistrationReferenceElement.setAttributeNS(NIEMNamespaces.STRUCT_NS, "ref", itemRegistrationRef);
		
		Element itemReferenceElement = doc.createElementNS(NIEMNamespaces.NC_20_NS, "ItemReference");
		itemReferenceElement.setAttributeNS(NIEMNamespaces.STRUCT_NS, "ref", itemRef);
		
		parentElement.appendChild(itemRegistrationReferenceElement);
		parentElement.appendChild(itemReferenceElement);
		
		return parentElement;
	}
	
	// For example
	//  <SearchMetadata xmlns:ns0="http://niem.gov/niem/structures/2.0" ns0:id="SM002" xmlns="http://ojbc.org/IEPD/Extensions/PersonSearchRequest/1.0">
	//			<SearchQualifierCode>partial</SearchQualifierCode>
	//	</SearchMetadata>
	public static Element createFirearmSearchMetaData(Document doc, String searchFieldMetaDataNamespace, SearchFieldMetadata searchFieldMetadata)
	{
		Element searchFieldMetadataElement = doc.createElementNS(searchFieldMetaDataNamespace, "SearchMetadata");
		
		if (searchFieldMetadata == SearchFieldMetadata.ExactMatch)
		{	
			searchFieldMetadataElement.setAttributeNS(NIEMNamespaces.STRUCT_NS, "id", "SM001");
		}	

		if (searchFieldMetadata == SearchFieldMetadata.Partial)
		{	
			searchFieldMetadataElement.setAttributeNS(NIEMNamespaces.STRUCT_NS, "id", "SM002");
		}	

		Element searchFieldQualifierCodeElement = doc.createElementNS(searchFieldMetaDataNamespace, "SearchQualifierCode");
		searchFieldQualifierCodeElement.setTextContent(searchFieldMetadata.getMetadata());
		
		searchFieldMetadataElement.appendChild(searchFieldQualifierCodeElement);
		
		return searchFieldMetadataElement;
	}
}

