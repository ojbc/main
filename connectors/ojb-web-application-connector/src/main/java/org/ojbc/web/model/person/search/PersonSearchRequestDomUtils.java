package org.ojbc.web.model.person.search;

import org.apache.commons.lang.StringUtils;
import org.ojbc.util.helper.NIEMXMLUtils;
import org.ojbc.web.util.OJBCWebApplicationNamespaceContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PersonSearchRequestDomUtils {

	public static Element createPersonsElement(Document doc, PersonSearchRequest psr) {

		Element personElement = doc.createElementNS(OJBCWebApplicationNamespaceContext.PERSON_SEARCH_REQUEST_EXT, "Person");
		
		//<nc:PersonEyeColorCode>BLU</nc:PersonEyeColorCode>
		if (StringUtils.isNotBlank(psr.getPersonEyeColor()))
		{	
			Element personEyeColorCode = NIEMXMLUtils.createNC20Element(doc, "PersonEyeColorCode", psr.getPersonEyeColor());
			personElement.appendChild(personEyeColorCode);
		}	
		
		//<nc:PersonHairColorCode>BLK</nc:PersonHairColorCode>
		if (StringUtils.isNotBlank(psr.getPersonHairColor()))
		{	
			Element personHairColorCode = NIEMXMLUtils.createNC20Element(doc, "PersonHairColorCode", psr.getPersonHairColor());
			personElement.appendChild(personHairColorCode);
		}	

		//<nc:PersonHeightMeasure>
		//  allow range value or text value not both
		//
		//	<nc:MeasureText>150</nc:MeasureText>
		//		
		//	<nc:MeasureRangeValue>
		//		<nc:RangeMinimumValue>72</nc:RangeMinimumValue>
		//		<nc:RangeMaximumValue>75</nc:RangeMaximumValue>
		//	</nc:MeasureRangeValue>
		//	<nc:LengthUnitCode>INH</nc:LengthUnitCode>
		//</nc:PersonHeightMeasure>
		if(psr.getPersonHeightTotalInchesRangeStart() != null && psr.getPersonHeightTotalInchesRangeEnd() != null)
		{
			Element personHeightMeasureElement = NIEMXMLUtils.createMeasureRangeValueOrTextElement(doc, psr.getPersonHeightTotalInchesRangeStart(), psr.getPersonHeightTotalInchesRangeEnd(), null, "INH", null);
			personElement.appendChild(personHeightMeasureElement);
		}	

		if(psr.getPersonHeightTotalInchesRangeStart() == null && psr.getPersonHeightTotalInchesRangeEnd() == null && psr.getPersonHeightTotalInches()!=null)
		{
			Element personHeightMeasureElement = NIEMXMLUtils.createMeasureRangeValueOrTextElement(doc, null, null, psr.getPersonHeightTotalInches(), "INH", null);
			personElement.appendChild(personHeightMeasureElement);
		}	

		
		//<nc:PersonName>
		//	<nc:PersonGivenName s:metadata="SM001">M</nc:PersonGivenName>
		//	<nc:PersonMiddleName>M</nc:PersonMiddleName>
		//	<nc:PersonSurName s:metadata="SM002">Scott</nc:PersonSurName>
		//</nc:PersonName>
		Element personNameElement = NIEMXMLUtils.createPersonNameElement(doc, psr.getPersonGivenName(), psr.getPersonGivenNameMetaData(), psr.getPersonMiddleName(), psr.getPersonSurName(), psr.getPersonSurNameMetaData());
		personElement.appendChild(personNameElement);
		
		//<nc:PersonRaceCode>W</nc:PersonRaceCode>
		if (StringUtils.isNotBlank(psr.getPersonRaceCode()))
		{	
			Element personRaceCode = NIEMXMLUtils.createNC20Element(doc, "PersonRaceCode", psr.getPersonRaceCode());
			personElement.appendChild(personRaceCode);
		}	

		//<nc:PersonSexCode>M</nc:PersonSexCode>
		if (StringUtils.isNotBlank(psr.getPersonSexCode()))
		{	
			Element personSexCode = NIEMXMLUtils.createNC20Element(doc, "PersonSexCode", psr.getPersonSexCode());
			personElement.appendChild(personSexCode);
		}	

		//<nc:PersonSSNIdentification>
		//	<nc:IdentificationID>123456789</nc:IdentificationID>
		//</nc:PersonSSNIdentification>
		if (StringUtils.isNotBlank(psr.getPersonSocialSecurityNumber()))
		{	
			Element personSSNCode = NIEMXMLUtils.createPersonSSNElement(doc, psr.getPersonSocialSecurityNumber());
			personElement.appendChild(personSSNCode);
		}	
		
		//<nc:PersonWeightMeasure>
		//  allow range value or text value not both
		//
		//	<nc:MeasureText>150</nc:MeasureText>
		//
		//	<nc:MeasureRangeValue>
		//		<nc:RangeMinimumValue>200</nc:RangeMinimumValue>
		//		<nc:RangeMaximumValue>215</nc:RangeMaximumValue>
		//	</nc:MeasureRangeValue>
		//	<nc:WeightUnitCode>LBR</nc:WeightUnitCode>
		//</nc:PersonWeightMeasure>
		if(psr.getPersonWeightRangeStart() != null && psr.getPersonWeightRangeEnd() != null)
		{
			Element personWeightMeasureElement = NIEMXMLUtils.createMeasureRangeValueOrTextElement(doc, psr.getPersonWeightRangeStart(), psr.getPersonWeightRangeEnd(), null, null, "LBR");
			personElement.appendChild(personWeightMeasureElement);
		}	
		
		if(psr.getPersonWeightRangeStart() == null && psr.getPersonWeightRangeEnd() == null && psr.getPersonWeight() != null)
		{
			Element personWeightMeasureElement = NIEMXMLUtils.createMeasureRangeValueOrTextElement(doc, null, null, psr.getPersonWeight(), null, "LBR");
			personElement.appendChild(personWeightMeasureElement);
		}	

		//<ext:PersonBirthDateRange>
		//	<nc:StartDate>
		//		<nc:Date>1970-01-01</nc:Date>
		//	</nc:StartDate>
		//	<nc:EndDate>
		//		<nc:Date>1970-01-05</nc:Date>
		//	</nc:EndDate>
		//</ext:PersonBirthDateRange>
		if (psr.getPersonDateOfBirthRangeEnd() != null && psr.getPersonDateOfBirthRangeStart() != null)
		{
			Element personDateRangeElement = doc.createElementNS(OJBCWebApplicationNamespaceContext.PERSON_SEARCH_REQUEST_EXT, "PersonBirthDateRange");
			personDateRangeElement = NIEMXMLUtils.createBirthDateRange(doc, personDateRangeElement, psr.getPersonDateOfBirthRangeStart(), psr.getPersonDateOfBirthRangeEnd());

			personElement.appendChild(personDateRangeElement);
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
		//<j:PersonAugmentation>
		if (StringUtils.isNotEmpty(psr.getPersonDriversLicenseIssuer()) 
			||	StringUtils.isNotEmpty(psr.getPersonDriversLicenseNumber())
			|| 	StringUtils.isNotEmpty(psr.getPersonSID())
			||  StringUtils.isNotEmpty(psr.getPersonFBINumber())
		   )
		{
			Element personAugmentationElement = NIEMXMLUtils.createPersonAugmentation(doc, psr.getPersonDriversLicenseNumber(), psr.getPersonDriversLicenseIssuer(), psr.getPersonSID(), psr.getPersonFBINumber());
			personElement.appendChild(personAugmentationElement);
		}		
		
		
		return personElement;
	}
	
}
