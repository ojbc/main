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
package org.ojbc.web.portal.arrest;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.web.OjbcWebConstants.ArrestType;
import org.ojbc.web.SearchFieldMetadata;
import org.ojbc.web.portal.AppProperties;
import org.ojbc.web.portal.security.OsbiUser;
import org.ojbc.web.portal.services.CodeTableService;
import org.ojbc.web.portal.services.SamlService;
import org.ojbc.web.portal.services.SearchResultConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({"arrestSearchResults", "arrestSearchRequest", "arrestDetailSearchRequest", "arrestDetail", "arrestDetailTransformed", "dispoCodeMapping", 
	"muniAmendedChargeCodeMapping", "muniFiledChargeCodeMapping", "muniAlternateSentenceMapping", "muniReasonsForDismissalMapping", 
	"submitArrestConfirmationMessage", "provisionCodeMapping", "osbiUser"})
@RequestMapping("/muniArrests")
public class MuniArrestController {
	private static final Log log = LogFactory.getLog(MuniArrestController.class);

	@Autowired
	ArrestService arrestService;
	
	@Resource
	SearchResultConverter searchResultConverter;

	@Resource
	SamlService samlService;
	
	@Resource
	CodeTableService codeTableService;
	
	@Resource
	AppProperties appProperties;
	
	@Resource
	DispositionValidator dispositionValidator;
	
    @ModelAttribute
    public void addModelAttributes(Model model) {
    	
    	log.info("Add ModelAtrributes");
		model.addAttribute("disposition", new Disposition(ArrestType.MUNI));
		
		if (!model.containsAttribute("dispoCodeMapping")) {
			Map<String, String> dispoCodeMapping = codeTableService.getMuniDispositionCodeMap(); 
					
			OsbiUser osbiUser = (OsbiUser) model.asMap().get("osbiUser"); 
			if (ArrestType.OSBI.getDescription().equals(osbiUser.getEmployerOrganizationCategory())) {
				dispoCodeMapping.put("24", "COURT DISPOSITION UNKNOWN");
			}
			model.addAttribute("dispoCodeMapping", dispoCodeMapping);
		}
		
		if (!model.containsAttribute("muniAmendedChargeCodeMapping")) {
			model.addAttribute("muniAmendedChargeCodeMapping", codeTableService.getMuniAmendedChargeCodeMap());
		}
		
		if (!model.containsAttribute("muniFiledChargeCodeMapping")) {
			model.addAttribute("muniFiledChargeCodeMapping", codeTableService.getMuniFiledChargeCodeMap());
		}
		
		if (!model.containsAttribute("muniAlternateSentenceMapping")) {
			model.addAttribute("muniAlternateSentenceMapping", codeTableService.getMuniAlternateSentenceMap());
		}
		
		if (!model.containsAttribute("muniReasonsForDismissalMapping")) {
			model.addAttribute("muniReasonsForDismissalMapping", codeTableService.getMuniReasonsForDismissalMap());
		}
		
		if (!model.containsAttribute("provisionCodeMapping")) {
			model.addAttribute("provisionCodeMapping", codeTableService.getDaProvisions());
		}
		
		if (!model.containsAttribute("submitArrestConfirmationMessage")) {
			model.addAttribute("submitArrestConfirmationMessage", appProperties.getSubmitArrestConfirmationMessage());
		}
		
		model.addAttribute("pendingChargesMapping", appProperties.getMuniPendingChargesMapping());
    	log.info("All ModelAtrributes are added.");
    }
    
	@InitBinder("disposition")
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(dispositionValidator);
	}

	@GetMapping("")
	public String defaultSearch(HttpServletRequest request, Map<String, Object> model) throws Throwable {
		ArrestSearchRequest arrestSearchRequest = (ArrestSearchRequest) model.get("arrestSearchRequest");

		if (arrestSearchRequest == null) {
			arrestSearchRequest = initializeArrestSearchRequest(model);
		}
		log.info("runing defaultSearch with " + arrestSearchRequest );
		getArrestSearchResults(request, arrestSearchRequest, model);
		return "arrest/arrests::resultsPage";
	}
	
	@GetMapping("resetSearchForm")
	public String resetSearchForm(HttpServletRequest request, Map<String, Object> model) throws Throwable {
		initializeArrestSearchRequest(model);
		return "arrest/arrests::arrestSearchForm";
	}

	private ArrestSearchRequest initializeArrestSearchRequest(Map<String, Object> model) {
		ArrestSearchRequest arrestSearchRequest = new ArrestSearchRequest();
		arrestSearchRequest.setArrestDateRangeStartDate(LocalDate.now().minusDays(appProperties.getArrestSearchDateRange()));
//		arrestSearchRequest.setArrestDateRangeStartDate(LocalDate.of(2018, 2, 1));
		arrestSearchRequest.setArrestDateRangeEndDate(LocalDate.now());
		arrestSearchRequest.setDispositionDateRangeStartDate(LocalDate.now().minusDays(appProperties.getArrestSearchDateRange()));
		arrestSearchRequest.setDispositionDateRangeEndDate(LocalDate.now());
		arrestSearchRequest.setFirstNameSearchMetadata(SearchFieldMetadata.StartsWith);
		arrestSearchRequest.setLastNameSearchMetadata(SearchFieldMetadata.StartsWith);
		arrestSearchRequest.setArrestType(ArrestType.MUNI);
		OsbiUser osbiUser = (OsbiUser) model.get("osbiUser");
		arrestSearchRequest.setAuthorizedOris(osbiUser.getOris());
		log.info("reset the search request to " + arrestSearchRequest);
		model.put("arrestSearchRequest", arrestSearchRequest);
		return arrestSearchRequest;
	}
	
	@PostMapping("/advancedSearch")
	public String advancedSearch(HttpServletRequest request, @Valid @ModelAttribute ArrestSearchRequest arrestSearchRequest, BindingResult bindingResult, 
			Map<String, Object> model) throws Throwable {
		if (bindingResult.hasErrors()) {
			log.info("has binding errors");
			log.info(bindingResult.getAllErrors());
			return "arrest/arrests::arrestSearchForm";
		}

		log.info("arrestSearchRequest:" + arrestSearchRequest );
		getArrestSearchResults(request, arrestSearchRequest, model);
		return "arrest/arrests::resultsPage";
	}

	private void getArrestSearchResults(HttpServletRequest request, ArrestSearchRequest arrestSearchRequest,
			Map<String, Object> model) throws Throwable {
		arrestSearchRequest.setFirstNameSearchMetaData(); 
		arrestSearchRequest.setLastNameSearchMetaData();
		String searchContent = arrestService.findArrests(arrestSearchRequest, samlService.getSamlAssertion(request));
		Map<String, Object> params = new HashMap<String, Object>();
		OsbiUser osbiUser = (OsbiUser) model.get("osbiUser");
		String authoritiesString = osbiUser.getAuthorities().stream()
				.map(item->item.getAuthority()).collect(Collectors.joining(","));
		params.put("authorities", authoritiesString);
		String transformedResults = searchResultConverter.convertMuniArrestSearchResult(searchContent, params);
		model.put("arrestSearchResults", searchContent); 
		model.put("arrestSearchContent", transformedResults); 
		model.put("arrestSearchRequest", arrestSearchRequest);
	}
	
	@GetMapping("/{arrestId}/charges/{chargeIds}")
	public String getArrest(HttpServletRequest request, @PathVariable String arrestId, 
			@PathVariable List<String> chargeIds, 
			Map<String, Object> model) throws Throwable {
		log.debug("chargeIds: " + chargeIds); 
		ArrestSearchRequest arrestSearchRequest = (ArrestSearchRequest) model.get("arrestSearchRequest");
		ArrestDetailSearchRequest arrestDetailSearchRequest = new ArrestDetailSearchRequest(arrestSearchRequest);
		arrestDetailSearchRequest.setArrestIdentification(arrestId);
		arrestDetailSearchRequest.setChargeIds(chargeIds);
		getArrestDetail(request, arrestDetailSearchRequest, model); 		
		return "arrest/arrestDetail::arrestDetail";
	}

	@GetMapping("/{id}/charges/{chargeIds}/hide")
	public String hideArrest(HttpServletRequest request, @PathVariable String id, 
			@PathVariable List<String> chargeIds, Map<String, Object> model) throws Throwable {
		arrestService.hideArrest(id, chargeIds, samlService.getSamlAssertion(request));
		
		ArrestSearchRequest arrestSearchrequest = (ArrestSearchRequest) model.get("arrestSearchRequest"); 
		getArrestSearchResults(request, arrestSearchrequest, model);
		return "arrest/arrests::resultsList";
	}
	
	@PostMapping("/finalize")
	public String finalizeArrest(HttpServletRequest request, 
			@RequestParam("arrestIdentification") String arrestIdentification, 
			@RequestParam("chargeIds[]") String[] chargeIds, 
			Map<String, Object> model) throws Throwable {
		String response = arrestService.finalizeArrest(arrestIdentification, chargeIds, samlService.getSamlAssertion(request));
		log.info("finalize arrest response:" + response);
		ArrestSearchRequest arrestSearchrequest = (ArrestSearchRequest) model.get("arrestSearchRequest"); 
		getArrestSearchResults(request, arrestSearchrequest, model);
		return "arrest/arrests::resultsPage";
	}
	
	@GetMapping("/{id}/charges/{chargeIds}/unhide")
	public String unhideArrest(HttpServletRequest request, @PathVariable String id, 
			@PathVariable List<String> chargeIds, Map<String, Object> model) throws Throwable {
		arrestService.unhideArrest(id, chargeIds, samlService.getSamlAssertion(request));
		
		ArrestSearchRequest arrestSearchrequest = (ArrestSearchRequest) model.get("arrestSearchRequest"); 
		getArrestSearchResults(request, arrestSearchrequest, model);
		return "arrest/arrests::resultsList";
	}
	
	@GetMapping("/summary")
	public @ResponseBody String presentArrest(Map<String, Object> model) throws Throwable {
		String arrrestDetail = (String) model.get("arrestDetail");
		String transformedArrestSummary = searchResultConverter.convertArrestSummary(arrrestDetail, ArrestType.MUNI);
		return transformedArrestSummary;
	}
	
	private void getArrestDetail(HttpServletRequest request, ArrestDetailSearchRequest arrestDetailSearchRequest, 
			Map<String, Object> model) throws Throwable {
		model.put("arrestDetailSearchRequest", arrestDetailSearchRequest);
		String searchContent = arrestService.getArrest(arrestDetailSearchRequest, samlService.getSamlAssertion(request));
		Map<String, Object> params = getAuthoritiesParam(model);
		String transformedResults = searchResultConverter.convertArrestDetail(searchContent, params);
		model.put("arrestDetail", searchContent); 
		model.put("arrestDetailTransformed", transformedResults);
	}
	
	private Map<String, Object> getAuthoritiesParam(Map<String, Object> model) {
		Map<String, Object> params = new HashMap<String, Object>();
		OsbiUser osbiUser = (OsbiUser) model.get("osbiUser");
		String authoritiesString = osbiUser.getAuthorities().stream()
				.map(item->item.getAuthority()).collect(Collectors.joining(","));
		params.put("authorities", authoritiesString);
		return params;
	}
	
	@GetMapping("/dispositionForm")
	public String getDispositionForm(HttpServletRequest request, Disposition disposition, 
			Map<String, Object> model) throws Throwable {
		
		model.put("disposition", disposition);
		return "arrest/dispositionForm::dispositionForm";
	}

	@GetMapping("/expungeDispositionForm")
	public String getExpungeDispositionForm(HttpServletRequest request, Disposition disposition, 
			Map<String, Object> model) throws Throwable {
		model.put("disposition", disposition);
		return "arrest/expungeDispositionForm::expungeDispositionForm";
	}
	
	@PostMapping("/expungeDisposition")
	public String expungeDisposition(HttpServletRequest request, @ModelAttribute Disposition disposition, BindingResult bindingResult, 
			Map<String, Object> model) throws Throwable {
		
		expunge(request, disposition, model); 
		return "arrest/arrestDetail::arrestDetail";
	}

	protected void expunge(HttpServletRequest request, Disposition disposition, Map<String, Object> model)
			throws Throwable {
		log.info("Expunge disposition" + disposition);
		arrestService.expungeDisposition(disposition, samlService.getSamlAssertion(request));
		getArrestDetail(request, (ArrestDetailSearchRequest)model.get("arrestDetailSearchRequest"), model);
	}

	@PostMapping("/saveDisposition")
	public String saveDisposition(HttpServletRequest request, @Valid @ModelAttribute Disposition disposition, BindingResult bindingResult, 
			Map<String, Object> model) throws Throwable {
		
		if (bindingResult.hasErrors()) {
			log.info("has binding errors");
			log.info(bindingResult.getAllErrors());
			return "arrest/dispositionForm::dispositionForm";
		}
		
		setCodeDescriptions(disposition, model); 
		log.info(disposition);
		arrestService.saveDisposition(disposition, samlService.getSamlAssertion(request));
//		String response = arrestService.saveDisposition(disposition, samlService.getSamlAssertion(request));
		getArrestDetail(request, (ArrestDetailSearchRequest)model.get("arrestDetailSearchRequest"), model); 
		return "arrest/arrestDetail::arrestDetail";
	}
	
	@PostMapping("/dispositions/delete")
	public String deleteDisposition(HttpServletRequest request,  Disposition disposition,
			Map<String, Object> model) throws Throwable {
		log.debug("deleting disposition " + disposition.toString());
		arrestService.deleteDisposition(disposition, samlService.getSamlAssertion(request));
//		String response = arrestService.deleteDisposition(disposition, samlService.getSamlAssertion(request));
		getArrestDetail(request, (ArrestDetailSearchRequest)model.get("arrestDetailSearchRequest"), model); 
		return "arrest/arrestDetail::arrestDetail";
	}
	
	@SuppressWarnings("unchecked")
	private void setCodeDescriptions(@Valid Disposition disposition, Map<String, Object> model) {
		Map<String, String> muniReasonsForDismissalMapping = (Map<String, String>) model.get("muniReasonsForDismissalMapping"); 
		Map<String, String> dispoCodeMapping = (Map<String, String>) model.get("dispoCodeMapping"); 
		Map<String, String> muniAmendedChargeCodeMapping = (Map<String, String>) model.get("muniAmendedChargeCodeMapping"); 
		Map<String, String> muniFiledChargeCodeMapping = (Map<String, String>) model.get("muniFiledChargeCodeMapping"); 
		Map<String, String> muniAlternateSentenceMapping = (Map<String, String>) model.get("muniAlternateSentenceMapping"); 
		
		if (disposition.getAlternateSentences()!=null && !disposition.getAlternateSentences().isEmpty()) {
			List<String> alternateSentenceDescriptions = disposition.getAlternateSentences().stream()
					.map(muniAlternateSentenceMapping::get)
					.collect(Collectors.toList());
			disposition.setAlternateSentenceDescriptions(alternateSentenceDescriptions);
		}
		disposition.setAmendedChargeDescription(muniAmendedChargeCodeMapping.get(disposition.getAmendedCharge()));
		disposition.setFiledChargeDescription(muniFiledChargeCodeMapping.get(disposition.getFiledCharge()));
		disposition.setDispositionDescription(dispoCodeMapping.get(disposition.getDispositionCode()));
		disposition.setReasonForDismissalDescripiton(muniReasonsForDismissalMapping.get(disposition.getReasonForDismissal()));
	}
	
	@PostMapping("/charges/decline")
	public String declineCharge(HttpServletRequest request,  ArrestCharge arrestCharge,
			Map<String, Object> model) throws Throwable {
		log.debug("decline charge: " + arrestCharge);
		arrestCharge.setArrestType(ArrestType.MUNI);
		arrestService.declineCharge(arrestCharge, samlService.getSamlAssertion(request));
//		String response = arrestService.deleteDisposition(disposition, samlService.getSamlAssertion(request));
		getArrestDetail(request, (ArrestDetailSearchRequest)model.get("arrestDetailSearchRequest"), model); 
		return "arrest/arrestDetail::arrestDetail";
	}

	@PostMapping("/referCharge")
	public String referCharge(HttpServletRequest request, @ModelAttribute ChargeReferral chargeReferral, BindingResult bindingResult, 
			Map<String, Object> model) throws Throwable {
		log.debug("chargeReferral: " + chargeReferral);

		@SuppressWarnings("unused")
		String response = arrestService.referCharge(chargeReferral, samlService.getSamlAssertion(request));
		//TODO check if success response. 
		ArrestDetailSearchRequest arrestDetailSearchRequest = (ArrestDetailSearchRequest)model.get("arrestDetailSearchRequest");
		arrestDetailSearchRequest.getChargeIds().removeIf(id->id.equalsIgnoreCase(chargeReferral.getArrestChargeIdentification()));
		if (arrestDetailSearchRequest.getChargeIds().size() > 0) {
			getArrestDetail(request, arrestDetailSearchRequest, model);
			return "arrest/arrestDetail::arrestDetail";
		}
		else {
			ArrestSearchRequest arrestSearchRequest = (ArrestSearchRequest) model.get("arrestSearchRequest"); 
			getArrestSearchResults(request, arrestSearchRequest, model);
			return "arrest/arrests::resultsPage";
		}
	}
	
	@PostMapping("/referArrest")
	public String referArrest(HttpServletRequest request, @ModelAttribute ArrestReferral arrestReferral, BindingResult bindingResult, 
			Map<String, Object> model) throws Throwable {
		log.debug("arrestReferral: " + arrestReferral);
		
		@SuppressWarnings("unused")
		String response = arrestService.referArrest(arrestReferral, samlService.getSamlAssertion(request));
		//TODO check if success response. 
		ArrestSearchRequest arrestSearchRequest = (ArrestSearchRequest) model.get("arrestSearchRequest"); 
		getArrestSearchResults(request, arrestSearchRequest, model);
		return "arrest/arrests::resultsList";
	}

}