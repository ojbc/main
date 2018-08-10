package org.ojbc.web.portal.disposition;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.ojbc.web.SearchFieldMetadata;
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
	
	@Autowired
	DispositionService dispostionService;
	
	@Resource
	SearchResultConverter searchResultConverter;

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
	public String defaultSearch(Map<String, Object> model) throws Throwable {
		DispositionSearchRequest dispositionSearchRequest = (DispositionSearchRequest) model.get("dispositionSearchRequest");
		String searchContent = dispostionService.findDispositions(dispositionSearchRequest);
		String transformedResults = searchResultConverter.convertDispositionSearchResult(searchContent);
		model.put("dispositionSearchResults", searchContent); 
		model.put("dispositionSearchContent", transformedResults); 
		model.put("dispositionSearchRequest", dispositionSearchRequest);
		return "disposition/dispositions";
	}

	@PostMapping("/advancedSearch")
	public String advancedSearch(@Valid @ModelAttribute DispositionSearchRequest dispositionSearchRequest, BindingResult bindingResult, 
			Map<String, Object> model) throws Throwable {
		
		System.out.println("dispositionSearchRequest:" + dispositionSearchRequest );
		String searchContent = dispostionService.findDispositions(dispositionSearchRequest);
		String transformedResults = searchResultConverter.convertDispositionSearchResult(searchContent);
		model.put("dispositionSearchResults", searchContent); 
		model.put("dispositionSearchContent", transformedResults); 
		model.put("dispositionSearchRequest", dispositionSearchRequest);
		return "disposition/dispositions";
	}
	
}