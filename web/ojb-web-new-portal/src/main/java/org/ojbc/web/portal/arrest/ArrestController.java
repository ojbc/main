package org.ojbc.web.portal.arrest;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.web.SearchFieldMetadata;
import org.ojbc.web.portal.AppProperties;
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
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({"arrestSearchResults", "arrestSearchRequest", "arrestDetail", "arrestDetailTransformed", "dispoCodeMapping", 
	"muniAmendedChargeCodeMapping", "muniFiledChargeCodeMapping", "muniAlternateSentenceMapping", "muniReasonsForDismissalMapping"})
@RequestMapping("/arrests")
public class ArrestController {
	private static final Log log = LogFactory.getLog(ArrestController.class);

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
    	
		ArrestSearchRequest arrestSearchRequest = new ArrestSearchRequest();
		arrestSearchRequest.setArrestDateRangeStartDate(LocalDate.of(2018, 2, 1));
		arrestSearchRequest.setArrestDateRangeEndDate(LocalDate.of(2018, 2, 10));
//		arrestSearchRequest.setArrestDateRangeStartDate(LocalDate.now().minusDays(90));
//		arrestSearchRequest.setArrestDateRangeEndDate(LocalDate.now());
		arrestSearchRequest.setFirstNameSearchMetadata(SearchFieldMetadata.StartsWith);
		arrestSearchRequest.setLastNameSearchMetadata(SearchFieldMetadata.StartsWith);
		
		model.addAttribute("arrestSearchRequest", arrestSearchRequest);
		model.addAttribute("disposition", new Disposition());
		model.addAttribute("searchFieldMetaData", Arrays.asList(SearchFieldMetadata.StartsWith, SearchFieldMetadata.ExactMatch));
		
		model.addAttribute("dispoCodeMapping", codeTableService.getMuniDispositionCodeMap());
		model.addAttribute("muniAmendedChargeCodeMapping", codeTableService.getMuniAmendedChargeCodeMap());
		model.addAttribute("muniFiledChargeCodeMapping", codeTableService.getMuniFiledChargeCodeMap());
		model.addAttribute("muniAlternateSentenceMapping", codeTableService.getMuniAlternateSentenceMap());
		model.addAttribute("muniReasonsForDismissalMapping", codeTableService.getMuniReasonsForDismissalMap());
    }
    
	@InitBinder("disposition")
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(dispositionValidator);
	}

	@GetMapping("")
	public String defaultSearch(HttpServletRequest request, Map<String, Object> model) throws Throwable {
		ArrestSearchRequest arrestSearchRequest = (ArrestSearchRequest) model.get("arrestSearchRequest");
		getArrestSearchResults(request, arrestSearchRequest, model);
		return "arrest/arrests::resultsPage";
	}

	@PostMapping("/advancedSearch")
	public String advancedSearch(HttpServletRequest request, @Valid @ModelAttribute ArrestSearchRequest arrestSearchRequest, BindingResult bindingResult, 
			Map<String, Object> model) throws Throwable {
		
		log.info("arrestSearchRequest:" + arrestSearchRequest );
		getArrestSearchResults(request, arrestSearchRequest, model);
		return "arrest/arrests::resultsList";
	}

	private void getArrestSearchResults(HttpServletRequest request, ArrestSearchRequest arrestSearchRequest,
			Map<String, Object> model) throws Throwable {
		String searchContent = arrestService.findArrests(arrestSearchRequest, samlService.getSamlAssertion(request));
		String transformedResults = searchResultConverter.convertArrestSearchResult(searchContent);
		model.put("arrestSearchResults", searchContent); 
		model.put("arrestSearchContent", transformedResults); 
		model.put("arrestSearchRequest", arrestSearchRequest);
	}
	
	@GetMapping("/{id}")
	public String getArrest(HttpServletRequest request, @PathVariable String id, Map<String, Object> model) throws Throwable {
		getArrestDetail(request, id, model); 
		return "arrest/arrestDetail::arrestDetail";
	}

	@GetMapping("/{id}/hide")
	public String hideArrest(HttpServletRequest request, @PathVariable String id, Map<String, Object> model) throws Throwable {
		String response = arrestService.hideArrest(id, samlService.getSamlAssertion(request));
		
		ArrestSearchRequest arrestSearchrequest = (ArrestSearchRequest) model.get("arrestSearchRequest"); 
		getArrestSearchResults(request, arrestSearchrequest, model);
		return "arrest/arrests::resultsList";
	}
	
	private void getArrestDetail(HttpServletRequest request, String id, Map<String, Object> model) throws Throwable {
		String searchContent = arrestService.getArrest(id, samlService.getSamlAssertion(request));
		String transformedResults = searchResultConverter.convertArrestDetail(searchContent);
		model.put("arrestDetail", searchContent); 
		model.put("arrestDetailTransformed", transformedResults);
	}

	@GetMapping("/dispositionForm")
	public String getDispositionForm(HttpServletRequest request, Disposition disposition, 
			Map<String, Object> model) throws Throwable {
		
		model.put("disposition", disposition);
		return "arrest/dispositionForm::dispositionForm";
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
		String response = arrestService.saveDisposition(disposition, samlService.getSamlAssertion(request));
		getArrestDetail(request, disposition.getArrestIdentification(), model); 
		return "arrest/arrestDetail::arrestDetail";
	}

	@SuppressWarnings("unchecked")
	private void setCodeDescriptions(@Valid Disposition disposition, Map<String, Object> model) {
		Map<String, String> muniReasonsForDismissalMapping = (Map<String, String>) model.get("muniReasonsForDismissalMapping"); 
		Map<String, String> dispoCodeMapping = (Map<String, String>) model.get("dispoCodeMapping"); 
		Map<String, String> muniAmendedChargeCodeMapping = (Map<String, String>) model.get("muniAmendedChargeCodeMapping"); 
		Map<String, String> muniFiledChargeCodeMapping = (Map<String, String>) model.get("muniFiledChargeCodeMapping"); 
		Map<String, String> muniAlternateSentenceMapping = (Map<String, String>) model.get("muniAlternateSentenceMapping"); 
		
		disposition.setAlternateSentenceDescripiton(muniAlternateSentenceMapping.get(disposition.getAlternateSentence()));
		disposition.setAmendedChargeDescription(muniAmendedChargeCodeMapping.get(disposition.getAmendedCharge()));
		disposition.setFiledChargeDescription(muniFiledChargeCodeMapping.get(disposition.getFiledCharge()));
		disposition.setDispositionDescription(dispoCodeMapping.get(disposition.getDispositionCode()));
		disposition.setReasonForDismissalDescripiton(muniReasonsForDismissalMapping.get(disposition.getReasonForDismissal()));
	}
	

}