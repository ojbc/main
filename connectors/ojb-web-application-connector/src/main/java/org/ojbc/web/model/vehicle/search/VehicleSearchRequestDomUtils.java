package org.ojbc.web.model.vehicle.search;

import org.apache.commons.lang.StringUtils;
import org.ojbc.util.helper.NIEMXMLUtils;
import org.ojbc.web.util.OJBCWebApplicationNamespaceContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class VehicleSearchRequestDomUtils {


	//<ext:Vehicle
	//	xmlns:ext="http://ojbc.org/IEPD/Extensions/VehicleSearchRequest/1.0"
	//	xmlns:nc="http://niem.gov/niem/niem-core/2.0">
	//	<nc:VehicleColorPrimaryCode>BLK</nc:VehicleColorPrimaryCode>
	//	<nc:ItemModelName>Titan</nc:ItemModelName>
	//	<nc:ConveyanceRegistrationPlateIdentification>
	//		<nc:IdentificationID>ABC123</nc:IdentificationID>
	//	</nc:ConveyanceRegistrationPlateIdentification>
	//	<nc:VehicleIdentification>
	//		<nc:IdentificationID>1234567890ABCDEFGH</nc:IdentificationID>
	//	</nc:VehicleIdentification>
	//	<ext:VehicleYearRange>
	//		<nc:StartDate>
	//			<nc:Year>2005</nc:Year>
	//		</nc:StartDate>
	//		<nc:EndDate>
	//			<nc:Year>2009</nc:Year>
	//		</nc:EndDate>
	//	</ext:VehicleYearRange>
	//	<ext:VehicleMakeCode>Nissan</ext:VehicleMakeCode>
	//</ext:Vehicle>
	public static Element createVehicleElement(Document doc, VehicleSearchRequest vsr) {

		Element vehicleElement = doc.createElementNS(OJBCWebApplicationNamespaceContext.VEHICLE_SEARCH_REQUEST_EXT, "Vehicle");
		
		if (StringUtils.isNotBlank(vsr.getVehicleColor()))
		{
			Element VehicleColorPrimaryCode = NIEMXMLUtils.createNC20Element(doc, "VehicleColorPrimaryCode", vsr.getVehicleColor());
			vehicleElement.appendChild(VehicleColorPrimaryCode);		
		}		

		if (StringUtils.isNotBlank(vsr.getVehicleModel()))
		{
			Element itemModelName = NIEMXMLUtils.createNC20Element(doc, "ItemModelName", vsr.getVehicleModel());
			vehicleElement.appendChild(itemModelName);		
		}		
		
		if (StringUtils.isNotBlank(vsr.getVehiclePlateNumber()))
		{
			Element conveyanceRegistrationPlateIdentificationElement = NIEMXMLUtils.createIdentificationElementWithParent(doc, "ConveyanceRegistrationPlateIdentification", vsr.getVehiclePlateNumber());
			vehicleElement.appendChild(conveyanceRegistrationPlateIdentificationElement);		
		}		
		
		if (StringUtils.isNotBlank(vsr.getVehicleVIN()))
		{
			Element vehicleIdentificationElement = NIEMXMLUtils.createIdentificationElementWithParent(doc, "VehicleIdentification", vsr.getVehicleVIN());
			vehicleElement.appendChild(vehicleIdentificationElement);		
		}		

		//If given a vehicle range, use start and end date to create range
		if (vsr.getVehicleYearRangeStart() != null && vsr.getVehicleYearRangeEnd() !=null)
		{
			Element vehicleYearRange = NIEMXMLUtils.createYearRangeElementWithParent(doc, "VehicleYearRange", OJBCWebApplicationNamespaceContext.VEHICLE_SEARCH_REQUEST_EXT, vsr.getVehicleYearRangeStart(), vsr.getVehicleYearRangeEnd());
			vehicleElement.appendChild(vehicleYearRange);
		}		

		//If given a vehicle start date, use start date to create range
		if (vsr.getVehicleYearRangeStart() != null && vsr.getVehicleYearRangeEnd() ==null)
		{
			Element vehicleYearRange = NIEMXMLUtils.createYearRangeElementWithParent(doc, "VehicleYearRange", OJBCWebApplicationNamespaceContext.VEHICLE_SEARCH_REQUEST_EXT, vsr.getVehicleYearRangeStart(), vsr.getVehicleYearRangeStart());
			vehicleElement.appendChild(vehicleYearRange);
		}		
		
		if (StringUtils.isNotBlank(vsr.getVehicleMake()))
		{
			Element vehicleMakeCodeElement = doc.createElementNS(OJBCWebApplicationNamespaceContext.VEHICLE_SEARCH_REQUEST_EXT, "VehicleMakeCode");
			vehicleMakeCodeElement.setTextContent(vsr.getVehicleMake());
			vehicleElement.appendChild(vehicleMakeCodeElement);		
		}		

		
		return vehicleElement;
	}	
	
	public static Element createVehicleSearchRequestElement(Document doc) {
		Element element = doc.createElementNS(OJBCWebApplicationNamespaceContext.VEHICLE_SEARCH_REQUEST, "VehicleSearchRequest");
		return element;
	}
	

}
