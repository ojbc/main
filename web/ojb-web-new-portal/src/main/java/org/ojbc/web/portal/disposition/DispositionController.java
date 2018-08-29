package org.ojbc.web.portal.disposition;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.web.SearchFieldMetadata;
import org.ojbc.web.portal.services.SamlService;
import org.ojbc.web.portal.services.SearchResultConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({"dispositionSearchResults", "dispositionSearchRequest"})
@RequestMapping("/dispositions")
public class DispositionController {
	private static final Log log = LogFactory.getLog(DispositionController.class);

	@Autowired
	DispositionService dispostionService;
	
	@Resource
	SearchResultConverter searchResultConverter;
	
	@Resource
	SamlService samlService;

    @ModelAttribute
    public void addModelAttributes(Model model) {
    	
		DispositionSearchRequest dispositionSearchRequest = new DispositionSearchRequest();
		dispositionSearchRequest.setDispositionDateRangeStartDate(LocalDate.now().minusDays(90));
		dispositionSearchRequest.setDispositionDateRangeEndDate(LocalDate.now());
		dispositionSearchRequest.setFirstNameSearchMetadata(SearchFieldMetadata.StartsWith);
		dispositionSearchRequest.setLastNameSearchMetadata(SearchFieldMetadata.StartsWith);
		model.addAttribute("dispositionSearchRequest", dispositionSearchRequest);
		model.addAttribute("searchFieldMetaData", Arrays.asList(SearchFieldMetadata.StartsWith, SearchFieldMetadata.ExactMatch));
    }
    
	@GetMapping("")
	public String defaultSearch(HttpServletRequest request, Map<String, Object> model) throws Throwable {
		DispositionSearchRequest dispositionSearchRequest = (DispositionSearchRequest) model.get("dispositionSearchRequest");
		String searchContent = dispostionService.findDispositions(dispositionSearchRequest, samlService.getSamlAssertion(request));
		String transformedResults = searchResultConverter.convertDispositionSearchResult(searchContent);
		model.put("dispositionSearchResults", searchContent); 
		model.put("dispositionSearchContent", transformedResults); 
		model.put("dispositionSearchRequest", dispositionSearchRequest);
		return "disposition/dispositions::resultsPage";
	}

	@PostMapping("/advancedSearch")
	public String advancedSearch(HttpServletRequest request, @Valid @ModelAttribute DispositionSearchRequest dispositionSearchRequest, BindingResult bindingResult, 
			Map<String, Object> model) throws Throwable {
		
		log.info("dispositionSearchRequest:" + dispositionSearchRequest );
		String searchContent = dispostionService.findDispositions(dispositionSearchRequest, samlService.getSamlAssertion(request));
		String transformedResults = searchResultConverter.convertDispositionSearchResult(searchContent);
		model.put("dispositionSearchResults", searchContent); 
		model.put("dispositionSearchContent", transformedResults); 
		model.put("dispositionSearchRequest", dispositionSearchRequest);
		return "disposition/dispositions::resultsList";
	}
	
}