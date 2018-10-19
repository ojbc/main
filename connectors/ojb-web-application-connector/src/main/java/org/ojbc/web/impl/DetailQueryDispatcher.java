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
package org.ojbc.web.impl;

import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.processor.person.query.CourtCaseQueryRequestProcessor;
import org.ojbc.processor.person.query.CriminalHistoryRequestProcessor;
import org.ojbc.processor.person.query.CustodyQueryRequestProcessor;
import org.ojbc.processor.person.query.FBICriminalHistoryRequestProcessor;
import org.ojbc.processor.person.query.FirearmRegistrationQueryRequestProcessor;
import org.ojbc.processor.person.query.FirearmsPurchaseProhibitionRequestProcessor;
import org.ojbc.processor.person.query.IncidentReportRequestProcessor;
import org.ojbc.processor.person.query.JuvenileQueryRequestProcessor;
import org.ojbc.processor.person.query.PersonToCourtCaseSearchRequestProcessor;
import org.ojbc.processor.person.query.PersonToCustodySearchRequestProcessor;
import org.ojbc.processor.person.query.PersonVehicleToIncidentSearchRequestProcessor;
import org.ojbc.processor.person.query.VehicleCrashQueryRequestProcessor;
import org.ojbc.processor.person.query.WarrantsRequestProcessor;
import org.ojbc.processor.person.query.WildlifeLicensingRequestProcessor;
import org.ojbc.web.DetailsQueryInterface;
import org.ojbc.web.OJBCWebServiceURIs;
import org.ojbc.web.model.person.query.DetailsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.w3c.dom.Element;

/**
 * The Detail Query Dispatcher will dispatch query requests to 
 * the specific implementations of those services.  We set all
 * detail query beans to required = false in case the implementer
 * has decided not to implement that query in their configuration.
 * 
 * This allows the Connector to start up without throwing dependency
 * exceptions if a profile is not specified.
 * 
 *
 */

public class DetailQueryDispatcher implements DetailsQueryInterface{

	private static final Log log = LogFactory.getLog( DetailQueryDispatcher.class );

	@Autowired(required=false)
	private WildlifeLicensingRequestProcessor wildlifeLicensingRequestProcessor;
	
	@Autowired(required=false)
	private WarrantsRequestProcessor warrantsRequestProcessor;

	@Autowired(required=false)
	private CriminalHistoryRequestProcessor criminalHistoryRequestProcessor;
	
	@Autowired(required=false)
	private FBICriminalHistoryRequestProcessor fbiCriminalHistoryRequestProcessor;
	
	@Autowired(required=false)
	private IncidentReportRequestProcessor incidentReportRequestProcessor;

	@Autowired(required=false)
	private PersonVehicleToIncidentSearchRequestProcessor personVehicleToIncidentSearchRequestProcessor;
	
	@Autowired(required=false)
	private PersonToCourtCaseSearchRequestProcessor personToCourtCaseSearchRequestProcessor;
	
	@Autowired(required=false)
	private CourtCaseQueryRequestProcessor courtCaseQueryRequestProcessor;
	
	@Autowired(required=false)
	private VehicleCrashQueryRequestProcessor vehicleCrashQueryRequestProcessor;

	@Autowired(required=false)
	private FirearmsPurchaseProhibitionRequestProcessor firearmsPurchaseProhibitionRequestProcessor;

	@Autowired(required=false)
	private PersonToCustodySearchRequestProcessor personToCustodySearchRequestProcessor;
	
	@Autowired(required=false)
	private CustodyQueryRequestProcessor custodyQueryRequestProcessor;
	
	@Autowired(required=false)
	private FirearmRegistrationQueryRequestProcessor firearmRegistrationQueryRequestProcessor;
	
	@Autowired(required=false)
	@Qualifier("juvenileCasePlanHistoryRequestProcessor")
	private JuvenileQueryRequestProcessor juvenileCasePlanHistoryRequestProcessor;

	@Autowired(required=false)
	@Qualifier("juvenileOffenseHistoryRequestProcessor")
	private JuvenileQueryRequestProcessor juvenileOffenseHistoryRequestProcessor;

	@Autowired(required=false)
	@Qualifier("juvenilePlacementHistoryRequestProcessor")
	private JuvenileQueryRequestProcessor juvenilePlacementHistoryRequestProcessor;

	@Autowired(required=false)
	@Qualifier("juvenileReferralHistoryRequestProcessor")
	private JuvenileQueryRequestProcessor juvenileReferralHistoryRequestProcessor;

	@Autowired(required=false)
	@Qualifier("juvenileHearingHistoryRequestProcessor")
	private JuvenileQueryRequestProcessor juvenileHearingHistoryRequestProcessor;
	
	@Autowired(required=false)
	@Qualifier("juvenileIntakeHistoryRequestProcessor")
	private JuvenileQueryRequestProcessor juvenileIntakeHistoryRequestProcessor;
	
	@Resource(name = "searchURIToQueryURIMap")
	private Map<String, String> searchURIToQueryURIMap;	
	
	public String invokeRequest(DetailsRequest request, String federatedQueryID, Element samlToken) throws Exception {

		log.debug("Invoking detail request in Conenctor");
		
		if (StringUtils.isEmpty(federatedQueryID)) {
			throw new IllegalStateException("Federated Query ID not set");
		}

		String requestIdSrcTxt = request.getIdentificationSourceText().trim();

		log.info("Identification Source text in request: " + StringUtils.trimToEmpty(requestIdSrcTxt));
		log.info("Identification ID in request: " + StringUtils.trimToEmpty(request.getIdentificationID()));
		
		//Check the map to see if there is a mapping of search URI to query URI
		if (searchURIToQueryURIMap != null)
		{
			if (searchURIToQueryURIMap.containsKey(requestIdSrcTxt))
			{
				request.setIdentificationSourceText(searchURIToQueryURIMap.get(requestIdSrcTxt));
				requestIdSrcTxt = searchURIToQueryURIMap.get(requestIdSrcTxt);
				log.info("Translating Identification Source text to: " + requestIdSrcTxt);
			}	
		}	

		
		if (OJBCWebServiceURIs.CRIMINAL_HISTORY.equals(requestIdSrcTxt)) {
//			if (Objects.equals(request.getQueryType(), "StateRapsheet")){
//				return criminalHistoryRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
////				return "<cht-doc:CriminalHistoryTextDocument xmlns:j=\"http://niem.gov/niem/domains/jxdm/4.1\" "
////						+ "xmlns:cht-doc=\"http://ojbc.org/IEPD/Exchange/CriminalHistoryTextDocument/1.0\" "
////						+ "xmlns:nc=\"http://niem.gov/niem/niem-core/2.0\" "
////						+ "xmlns:s=\"http://niem.gov/niem/structures/2.0\" >"
////						+ "	<cht-doc:StateCriminalHistoryRecordDocument> "
////						+ "		<cht-doc:Base64BinaryObject>VGhpcyBpcyBhIGNyaW1pbmFsIGhpc3Rvcnk=</cht-doc:Base64BinaryObject> "
////						+ "	</cht-doc:StateCriminalHistoryRecordDocument>"
////						+ "</cht-doc:CriminalHistoryTextDocument>";
//			}
			if (Objects.equals(request.getQueryType(), "FBIRapsheet")){
				return fbiCriminalHistoryRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
//				return "<cht-doc:CriminalHistoryTextDocument xmlns:j=\"http://niem.gov/niem/domains/jxdm/4.1\" "
//						+ "xmlns:cht-doc=\"http://ojbc.org/IEPD/Exchange/CriminalHistoryTextDocument/1.0\" "
//						+ "xmlns:nc=\"http://niem.gov/niem/niem-core/2.0\" "
//						+ "xmlns:s=\"http://niem.gov/niem/structures/2.0\" "
//						+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
//						+ "xsi:schemaLocation=\"http://ojbc.org/IEPD/Exchange/CriminalHistoryTextDocument/1.0 ../xsd/Criminal_History_Text_Document.xsd\">"
//						+ "		<cht-doc:FederalCriminalHistoryRecordDocument>"
//						+ "			<cht-doc:Base64BinaryObject>Q0pJU1RFU1QuDQpDUi5XVkZCSU5GMDAuQ0pJU1RFU1QuKk1SSUQyNjcxNjQuDQpUWFQNCkNSLldWRkJJTkYwMA0KMTg6NTQgMDcvMzAvMjAxNSAwMDU2MA0KMTg6NTQgMDcvMzAvMjAxNSAwMDAzMSBIWFhYWFhYWA0KVFhUDQpIRFIvMkwwMTAxOVAsTVJJRDI2NzE2NA0KQVROL0pBTkUgVEVTVCAjMTBYWFhYDQoqKioqKioqKioqKioqKioqKioqKioqICBDUklNSU5BTCBISVNUT1JZIFJFQ09SRCAgKioqKioqKioqKioqKioqKioqKioqKioNCg0KKioqKioqKioqKioqKioqKioqKioqKioqKioqKiAgSW50cm9kdWN0aW9uICAqKioqKioqKioqKioqKioqKioqKioqKioqKioqDQoNClRoaXMgcmFwIHNoZWV0IHdhcyBwcm9kdWNlZCBpbiByZXNwb25zZSB0byB0aGUgZm9sbG93aW5nIHJlcXVlc3Q6DQoNCkZCSSBOdW1iZXIgICAgICAgICAgICAgIDlYWFhYWFgNClJlcXVlc3QgSWQgICAgICAgICAgICAgIA0KUHVycG9zZSBDb2RlICAgICAgICAgICAgSQ0KQXR0ZW50aW9uICAgICAgICAgICAgICAgSkFORSBURVNUICMxMDAxMTUNCg0KVGhlIGluZm9ybWF0aW9uIGluIHRoaXMgcmFwIHNoZWV0IGlzIHN1YmplY3QgdG8gdGhlIGZvbGxvd2luZyBjYXZlYXRzOg0KDQogKFVTOyAyMDE1LTA3LTMwKQ0KIChVUzsgMjAxNS0wNy0zMCkNCiAoVVM7IDIwMTUtMDctMzApDQogKFVTOyAyMDE1LTA3LTMwKQ0KIChVUzsgMjAxNS0wNy0zMCkNClRoaXMgcmVjb3JkIGlzIGJhc2VkIG9ubHkgb24gdGhlIEZCSSBudW1iZXIgaW4geW91ciByZXF1ZXN0LVVDTjogDQo5WFhYWFhYDQpCZWNhdXNlIGFkZGl0aW9ucyBvciBkZWxldGlvbnMgbWF5IGJlIG1hZGUgYXQgYW55IHRpbWUsIGEgbmV3IA0KY29weSBzaG91bGQgYmUgcmVxdWVzdGVkIHdoZW4gbmVlZGVkIGZvciBzdWJzZXF1ZW50IHVzZS4gKFVTOyANCjIwMTUtMDctMzApDQogICAgICAgICAgICAgIFRISVMgUkVDT1JEIElTIFNVQkpFQ1QgVE8gVEhFDQogICAgICAgRk9MTE9XSU5HIFVTRSBBTkQgDQpESVNTRU1JTkFUSU9OIFJFU1RSSUNUSU9OUw0KIA0KICAgICBVTkRFUiBQUk9WSVNJT05TIFNFVCBGT1JUSCBJTiBUSVRMRSANCjI4LCBDT0RFIE9GIEZFREVSQUwNClJFR1VMQVRJT05TIChDRlIpLCBTRUNUSU9OIDUwLjEyLCBCT1RIIEdPVkVSTk1FTlRBTCANCkFORCBOT05HT1ZFUk5NRU5UQUwNCkVOVElUSUVTIEFVVEhPUklaRUQgVE8gU1VCTUlUIEZJTkdFUlBSSU5UUyBBTkQgDQpSRUNFSVZFIEZCSSBJREVOVElGSUNBVElPTg0KUkVDT1JEUyBNVVNUIE5PVElGWSBUSEUgSU5ESVZJRFVBTFMgDQpGSU5HRVJQUklOVEVEIFRIQVQgVEhFIEZJTkdFUlBSSU5UUw0KV0lMTCBCRSBVU0VEIFRPIENIRUNLIFRIRSBDUklNSU5BTCANCkhJU1RPUlkgUkVDT1JEUyBPRiBUSEUgRkJJLg0KSURFTlRJRklDQVRJT04gUkVDT1JEUyBPQlRBSU5FRCBGUk9NIFRIRSANCkZCSSBNQVkgQkUgVVNFRCBTT0xFTFkgRk9SDQpUSEUgUFVSUE9TRSBSRVFVRVNURUQgQU5EIE1BWSBOT1QgQkUgDQpESVNTRU1JTkFURUQgT1VUU0lERSBUSEUgUkVDRUlWSU5HDQpERVBBUlRNRU5ULCBSRUxBVEVEIEFHRU5DWSBPUiBPVEhFUiANCkFVVEhPUklaRUQgRU5USVRZLiAgSUYgVEhFIElORk9STUFUSU9ODQpPTiBUSEUgUkVDT1JEIElTIFVTRUQgVE8gDQpESVNRVUFMSUZZIEFOIEFQUExJQ0FOVCwgVEhFIE9GRklDSUFMIE1BS0lORyBUSEUNCkRFVEVSTUlOQVRJT04gT0YgDQpTVUlUQUJJTElUWSBGT1IgTElDRU5TSU5HIE9SIEVNUExPWU1FTlQgU0hBTEwgUFJPVklERSBUSEUNCkFQUExJQ0FOVCBUSEUgDQpPUFBPUlRVTklUWSBUTyBDT01QTEVURSwgT1IgQ0hBTExFTkdFIFRIRSBBQ0NVUkFDWSBPRiwgVEhFDQpJTkZPUk1BVElPTiANCkNPTlRBSU5FRCBJTiBUSEUgRkJJIElERU5USUZJQ0FUSU9OIFJFQ09SRC4gIFRIRSBERUNJRElORw0KT0ZGSUNJQUwgDQpTSE9VTEQgTk9UIERFTlkgVEhFIExJQ0VOU0UgT1IgRU1QTE9ZTUVOVCBCQVNFRCBPTiBUSEUNCklORk9STUFUSU9OIElOIA0KVEhFIFJFQ09SRCBVTlRJTCBUSEUgQVBQTElDQU5UIEhBUyBCRUVOIEFGRk9SREVEIEENClJFQVNPTkFCTEUgVElNRSBUTyANCkNPUlJFQ1QgT1IgQ09NUExFVEUgVEhFIElORk9STUFUSU9OLCBPUiBIQVMgREVDTElORUQgVE8NCkRPIFNPLiAgQU4gDQpJTkRJVklEVUFMIFNIT1VMRCBCRSBQUkVTVU1FRCBOT1QgR1VJTFRZIE9GIEFOWSBDSEFSR0UvQVJSRVNUDQpGT1IgV0hJQ0ggDQpUSEVSRSBJUyBOTyBGSU5BTCBESVNQT1NJVElPTiBTVEFURUQgT04gVEhFIFJFQ09SRCBPUiANCk9USEVSV0lTRQ0KREVURVJNSU5FRC4gIElGIFRIRSBBUFBMSUNBTlQgV0lTSEVTIFRPIENPUlJFQ1QgVEhFIFJFQ09SRCBBUyANCklUIEFQUEVBUlMNCklOIFRIRSBGQkkncyBDSklTIERJVklTSU9OIFJFQ09SRFMgU1lTVEVNLCBUSEUgQVBQTElDQU5UIA0KU0hPVUxEDQpCRSBBRFZJU0VEIFRIQVQgVEhFIFBST0NFRFVSRVMgVE8gQ0hBTkdFLCBDT1JSRUNUIE9SIFVQREFURSBUSEUgDQpSRUNPUkQgQVJFDQpTRVQgRk9SVEggSU4gVElUTEUgMjgsIENGUiwgU0VDVElPTiAxNi4zNC4NCiAoVVM7IDIwMTUtMDctMzApDQpUaGlzIHJlY29yZCBtdXN0IGJlIHVzZWQgb25seSBpbiBjb25qdW5jdGlvbiB3aXRoIHRoZSBjdXJyZW50IA0KDQphcHBsaWNhdGlvbiAtIGEgbmV3IHJlY29yZCBtdXN0IGJlIHJlcXVlc3RlZCBmb3IgZnV0dXJlIHVzZS4gKFVTOyANCjIwMTUtMDctMzApDQpBbGwgYXJyZXN0IGVudHJpZXMgY29udGFpbmVkIGluIHRoaXMgRkJJIHJlY29yZCBhcmUgYmFzZWQgb24gDQoNCmZpbmdlcnByaW50IGNvbXBhcmlzb25zIGFuZCBwZXJ0YWluIHRvIHRoZSBzYW1lIGluZGl2aWR1YWwuICAoVVM7IA0KMjAxNS0wNy0zMCkNClRoZSB1c2Ugb2YgdGhpcyByZWNvcmQgaXMgcmVndWxhdGVkIGJ5IGxhdy4gIEl0IGlzIHByb3ZpZGVkIGZvciANCg0Kb2ZmaWNpYWwgdXNlIG9ubHkgYW5kIG1heSBiZSB1c2VkIG9ubHkgZm9yIHRoZSBwdXJwb3NlIHJlcXVlc3RlZC4gIA0KKFVTOyAyMDE1LTA3LTMwKQ0KDQoqKioqKioqKioqKioqKioqKioqKioqKioqKiogIElERU5USUZJQ0FUSU9OICAqKioqKioqKioqKioqKioqKioqKioqKioqKioNCg0KU3ViamVjdCBOYW1lKHMpDQoNCkpFVFNPTiwgSkFORSANCkNBU0UsIFRFU1QgIChBS0EpDQpET0UsIEpBTkUgSEFXQUlJICAoQUtBKQ0KRE9FLCBTVVNBTiAgKEFLQSkNCkZMSU5UU1RPTkUsIFdJTE1BICAoQUtBKQ0KSkVUU09OLCBDT05OSUUgIChBS0EpDQpKRVRTT04sIEdFT1JHRVRURSAgKEFLQSkNClRFU1QsIEpBTkUgSEFXQUlJICAoQUtBKQ0KVEVTVCwgSkFORSBNQVJJRSAgKEFLQSkNClRFU1RFUiwgVEVTVFkgIChBS0EpDQoNClN1YmplY3QgRGVzY3JpcHRpb24NCg0KRkJJIE51bWJlciAgICAgICAgICAgICAgU3RhdGUgSWQgTnVtYmVyICAgICAgICAgICAgICAgICAgIA0KOTIyMjIwMCAgICAgICAgICAgICAgICAgVE4wOTg3NjU0MyAoVE4pICAgICAgICAgICAgICAgICAgICAgICAgVlQ5OTk5OTggKFZUKQ0KICAgICAgICAgICAgICAgICAgICAgICAgSElBNTg3MzE2NSAoSEkpDQogICAgICAgICAgICAgICAgICAgICAgICBMQTIyMjU2MzUgKExBKQ0KDQpTb2NpYWwgU2VjdXJpdHkgTnVtYmVyICAgICAgICAgICAgICAgICAgICAgICAgIA0KMDk4NzY1NDMyDQowMjczODM2ODkNCjU3NTk5NzY3Ng0KDQoNClNleCAgICAgICAgICAgICAgICAgICAgIFJhY2UgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KRmVtYWxlICAgICAgICAgICAgICAgICAgV2hpdGUNCg0KSGVpZ2h0ICAgICAgICAgICAgICAgICAgV2VpZ2h0ICAgICAgICAgICAgICAgICAgRGF0ZSBvZiBCaXJ0aA0KNScwOCIgICAgICAgICAgICAgICAgICAgMTEwICAgICAgICAgICAgICAgICAgICAgMTk2Mi0wMS0wNg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgMTk3Ny0xMC0wNg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgMTk2MS0wMS0wNg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgMTk1Ni0wMS0wMQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgMTk1MC0wMS0wMQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgMTk3OC0wNS0xNw0KDQpIYWlyIENvbG9yICAgICAgICAgICAgICBFeWUgQ29sb3IgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQpCcm93biAgICAgICAgICAgICAgICAgICBCbHVlDQoNClNjYXJzLCBNYXJrcywgYW5kIFRhdHRvb3MNCkNvZGUgICAgICAgICAgICAgICAgICAgIERlc2NyaXB0aW9uLCBDb21tZW50cywgYW5kIEltYWdlcw0KVEFUIEFCRE9NICAgICAgICAgICAgICAgLCBUQVRUT08gT04gQUJET01FTiANCkFSVCBBUk0gICAgICAgICAgICAgICAgICwgQVJUSUZJQ0lBTCBBUk0sIE5PTlNQRUNJRklDIA0KR0xBU1NFUyAgICAgICAgICAgICAgICAgLCBXRUFSUyBHTEFTU0VTIA0KVEFUIFIgQVJNICAgICAgICAgICAgICAgLCBUQVRUT08gT04gUklHSFQgQVJNIA0KDQoNClBsYWNlIG9mIEJpcnRoICAgICAgICAgIENpdGl6ZW5zaGlwICAgICAgICAgICAgICAgICAgICAgIA0KVGVubmVzc2VlICAgICAgICAgICAgICAgVW5pdGVkIFN0YXRlcw0KDQoNCg0KKioqKioqKioqKioqKioqKioqKioqKioqKiogIENSSU1JTkFMIEhJU1RPUlkgICoqKioqKioqKioqKioqKioqKioqKioqKioqDQoNCj09PT09PT09PT09PT09PT09PT09PT09PT09PT09PT0gQ3ljbGUgMDAxID09PT09PT09PT09PT09PT09PT09PT09PT09PT09PQ0KRWFybGllc3QgRXZlbnQgRGF0ZSAgICAgMTk5OS0xMS0wMQ0KLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tDQpBcnJlc3QgRGF0ZSAgICAgICAgICAgICAxOTk5LTExLTAxDQpBcnJlc3QgQ2FzZSBOdW1iZXIgICAgICANCkFycmVzdCBDYXNlIE51bWJlciAgICAgIA0KQXJyZXN0aW5nIEFnZW5jeSAgICAgICAgUkkwMDQxMDAwIFNDSVRVQVRFIFBPTElDRSBERVBUDQpDaGFyZ2UgICAgICAgICAgICAgICAgICAxDQogICAgICAgIENoYXJnZSBMaXRlcmFsICBUSEVGVCBPVkVSICQxMDANCiAgICAgICAgICAgICAgU2V2ZXJpdHkgIFVua25vd24gDQpDaGFyZ2UgICAgICAgICAgICAgICAgICAyDQogICAgICAgIENoYXJnZSBMaXRlcmFsICBNQVJJSlVBTkEgUE9TU0VTU0lPTiBVTkRFUiAxIE9aDQogICAgICAgICAgICAgIFNldmVyaXR5ICBVbmtub3duIA0KLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tDQpDb3VydCBEaXNwb3NpdGlvbiAgICAgICAoQ3ljbGUgMDAxKQ0KQ291cnQgQ2FzZSBOdW1iZXIgICAgICAgDQpDb3VydCBBZ2VuY3kgICAgICAgICAgICAgDQpDaGFyZ2UgICAgICAgICAgICAgICAgICAxDQogICAgICAgIENoYXJnZSBMaXRlcmFsICBUSEVGVCBPVkVSICQxMDANCiAgICAgICAgICAgRGlzcG9zaXRpb24gICggU0VOVEVOQ0VEIDYgTU9TIFNVUEVSVklTRUQgVy9QUk9CKQ0KQ2hhcmdlICAgICAgICAgICAgICAgICAgMg0KICAgICAgICBDaGFyZ2UgTGl0ZXJhbCAgTUFSSUpVQU5BIFBPU1NFU1NJT04gVU5ERVIgMSBPWg0KICAgICAgICAgICBEaXNwb3NpdGlvbiAgKCBGSU5FRCAkMjAwKQ0KKioqKioqKioqKioqKioqKioqKioqKioqKiAgSU5ERVggT0YgQUdFTkNJRVMgICoqKioqKioqKioqKioqKioqKioqKioqKioqDQoNCkFnZW5jeSAgICAgICAgICAgICAgICAgIFNDSVRVQVRFIFBPTElDRSBERVBUOyBSSTAwNDEwMDA7DQpBZ2VuY3kgRW1haWwgQWRkcmVzcyAgICB1bmtub3duQGxvY2FsaG9zdA0KQWRkcmVzcw0KDQogICAgICAgICAgICAgICAgICAgICAgICBIT1BFLCBSSSAwMjgzMTE4MzkgDQoNCg0KKiAqICogRU5EIE9GIFJFQ09SRCAqICogKiA=</cht-doc:Base64BinaryObject>"
//						+ "		</cht-doc:FederalCriminalHistoryRecordDocument> "
//						+ "</cht-doc:CriminalHistoryTextDocument>";
			}
			else {
				return criminalHistoryRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
			}
		
		} else if (OJBCWebServiceURIs.WARRANTS.equals(requestIdSrcTxt)) {
			return warrantsRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
		
		} else if (requestIdSrcTxt.contains(OJBCWebServiceURIs.INCIDENT_REPORT)) {
			return incidentReportRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
		
		} else if (requestIdSrcTxt.contains(OJBCWebServiceURIs.PERSON_TO_INCIDENT)) {
			return personVehicleToIncidentSearchRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
		
		} else if (requestIdSrcTxt.contains(OJBCWebServiceURIs.VEHICLE_TO_INCIDENT)) {
			return personVehicleToIncidentSearchRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
		
		} else if (OJBCWebServiceURIs.FIREARMS.equals(requestIdSrcTxt)) {
			return firearmRegistrationQueryRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
			
		} else if(requestIdSrcTxt.contains(OJBCWebServiceURIs.FIREARMS_QUERY_REQUEST_BY_FIREARM)){
			return firearmRegistrationQueryRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
			
		} else if(requestIdSrcTxt.contains(OJBCWebServiceURIs.FIREARMS_QUERY_REQUEST_BY_PERSON)){
			
			return firearmRegistrationQueryRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
			
		} else if (OJBCWebServiceURIs.COURT_CASE.equals(requestIdSrcTxt)){
			
			return personToCourtCaseSearchRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
			
		} else if (OJBCWebServiceURIs.COURT_CASE_DETAIL.equals(requestIdSrcTxt)){
			
			return courtCaseQueryRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
			
		} else if (OJBCWebServiceURIs.JAIL.equals(requestIdSrcTxt)){
			
			return personToCustodySearchRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
			
		} else if (OJBCWebServiceURIs.JAIL_DETAIL.equals(requestIdSrcTxt)){
			
			return custodyQueryRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
			
		} else if (OJBCWebServiceURIs.VEHICLE_CRASH.equals(requestIdSrcTxt)){
			
			return vehicleCrashQueryRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
			
		} else if (OJBCWebServiceURIs.FIREARMS_PURCHASE_PROHIBITION.equals(requestIdSrcTxt)) {
			
			return firearmsPurchaseProhibitionRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
			
		} else if (OJBCWebServiceURIs.WILDLIFE_LICENSING.equals(requestIdSrcTxt)) {
			return wildlifeLicensingRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
		} 
		
		else if (requestIdSrcTxt.contains(OJBCWebServiceURIs.JUVENILE_HISTORY)) {
			
			log.info("Juvenile request query type: " + request.getQueryType());
			
			if (request.getQueryType() == null){
				throw new RuntimeException("Query type required for Juvenile queries");
			}	
			else if (request.getQueryType().equalsIgnoreCase("CasePlan")){
				return juvenileCasePlanHistoryRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
			}
			else if (request.getQueryType().equalsIgnoreCase("Hearing")){
				return juvenileHearingHistoryRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
			}  
			else if (request.getQueryType().equalsIgnoreCase("Intake")){
				return juvenileIntakeHistoryRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
			}
			else if (request.getQueryType().equalsIgnoreCase("Person")||request.getQueryType().equalsIgnoreCase("Offense") ){
				return juvenileOffenseHistoryRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
			}  
			else if (request.getQueryType().equalsIgnoreCase("Placement")){
				return juvenilePlacementHistoryRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
			}  
			else if (request.getQueryType().equalsIgnoreCase("Referral")){
				return juvenileReferralHistoryRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
			}	
		}		

		throw new RuntimeException("Unknown source: " + requestIdSrcTxt);

	}

	public WarrantsRequestProcessor getWarrantsRequestProcessor() {
		return warrantsRequestProcessor;
	}

	public void setWarrantsRequestProcessor(
			WarrantsRequestProcessor warrantsRequestProcessor) {
		this.warrantsRequestProcessor = warrantsRequestProcessor;
	}

	public CriminalHistoryRequestProcessor getCriminalHistoryRequestProcessor() {
		return criminalHistoryRequestProcessor;
	}

	public void setCriminalHistoryRequestProcessor(
			CriminalHistoryRequestProcessor criminalHistoryRequestProcessor) {
		this.criminalHistoryRequestProcessor = criminalHistoryRequestProcessor;
	}

	public IncidentReportRequestProcessor getIncidentReportRequestProcessor() {
		return incidentReportRequestProcessor;
	}

	public void setIncidentReportRequestProcessor(
			IncidentReportRequestProcessor incidentReportRequestProcessor) {
		this.incidentReportRequestProcessor = incidentReportRequestProcessor;
	}

	public PersonVehicleToIncidentSearchRequestProcessor getPersonVehicleToIncidentSearchRequestProcessor() {
		return personVehicleToIncidentSearchRequestProcessor;
	}

	public void setPersonVehicleToIncidentSearchRequestProcessor(
			PersonVehicleToIncidentSearchRequestProcessor personVehicleToIncidentSearchRequestProcessor) {
		this.personVehicleToIncidentSearchRequestProcessor = personVehicleToIncidentSearchRequestProcessor;
	}

	public FirearmRegistrationQueryRequestProcessor getFirearmRegistrationQueryRequestProcessor() {
		return firearmRegistrationQueryRequestProcessor;
	}

	public void setFirearmRegistrationQueryRequestProcessor(
			FirearmRegistrationQueryRequestProcessor firearmRegistrationQueryRequestProcessor) {
		this.firearmRegistrationQueryRequestProcessor = firearmRegistrationQueryRequestProcessor;
	}

	public JuvenileQueryRequestProcessor getJuvenileCasePlanHistoryRequestProcessor() {
		return juvenileCasePlanHistoryRequestProcessor;
	}

	public void setJuvenileCasePlanHistoryRequestProcessor(
			JuvenileQueryRequestProcessor juvenileCasePlanHistoryRequestProcessor) {
		this.juvenileCasePlanHistoryRequestProcessor = juvenileCasePlanHistoryRequestProcessor;
	}

	public JuvenileQueryRequestProcessor getJuvenileOffenseHistoryRequestProcessor() {
		return juvenileOffenseHistoryRequestProcessor;
	}

	public void setJuvenileOffenseHistoryRequestProcessor(
			JuvenileQueryRequestProcessor juvenileOffenseHistoryRequestProcessor) {
		this.juvenileOffenseHistoryRequestProcessor = juvenileOffenseHistoryRequestProcessor;
	}

	public JuvenileQueryRequestProcessor getJuvenilePlacementHistoryRequestProcessor() {
		return juvenilePlacementHistoryRequestProcessor;
	}

	public void setJuvenilePlacementHistoryRequestProcessor(
			JuvenileQueryRequestProcessor juvenilePlacementHistoryRequestProcessor) {
		this.juvenilePlacementHistoryRequestProcessor = juvenilePlacementHistoryRequestProcessor;
	}

	public JuvenileQueryRequestProcessor getJuvenileReferralHistoryRequestProcessor() {
		return juvenileReferralHistoryRequestProcessor;
	}

	public void setJuvenileReferralHistoryRequestProcessor(
			JuvenileQueryRequestProcessor juvenileReferralHistoryRequestProcessor) {
		this.juvenileReferralHistoryRequestProcessor = juvenileReferralHistoryRequestProcessor;
	}

	public JuvenileQueryRequestProcessor getJuvenileHearingHistoryRequestProcessor() {
		return juvenileHearingHistoryRequestProcessor;
	}

	public void setJuvenileHearingHistoryRequestProcessor(
			JuvenileQueryRequestProcessor juvenileHearingHistoryRequestProcessor) {
		this.juvenileHearingHistoryRequestProcessor = juvenileHearingHistoryRequestProcessor;
	}

	public JuvenileQueryRequestProcessor getJuvenileIntakeHistoryRequestProcessor() {
		return juvenileIntakeHistoryRequestProcessor;
	}

	public void setJuvenileIntakeHistoryRequestProcessor(
			JuvenileQueryRequestProcessor juvenileIntakeHistoryRequestProcessor) {
		this.juvenileIntakeHistoryRequestProcessor = juvenileIntakeHistoryRequestProcessor;
	}

	public Map<String, String> getSearchURIToQueryURIMap() {
		return searchURIToQueryURIMap;
	}

	public void setSearchURIToQueryURIMap(Map<String, String> searchURIToQueryURIMap) {
		this.searchURIToQueryURIMap = searchURIToQueryURIMap;
	}

}
