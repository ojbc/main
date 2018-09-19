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
import org.ojbc.web.portal.services.SamlService;
import org.ojbc.web.portal.services.SearchResultConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({"arrestSearchResults", "arrestSearchRequest", "arrestDetail", "arrestDetailTransformed"})
@RequestMapping("/arrests")
public class ArrestController {
	private static final Log log = LogFactory.getLog(ArrestController.class);

	@Autowired
	ArrestService arrestService;
	
	@Resource
	SearchResultConverter searchResultConverter;

	@Resource
	SamlService samlService;
	
    @ModelAttribute
    public void addModelAttributes(Model model) {
    	
		ArrestSearchRequest arrestSearchRequest = new ArrestSearchRequest();
		arrestSearchRequest.setArrestDateRangeStartDate(LocalDate.now().minusDays(90));
		arrestSearchRequest.setArrestDateRangeEndDate(LocalDate.now());
		arrestSearchRequest.setFirstNameSearchMetadata(SearchFieldMetadata.StartsWith);
		arrestSearchRequest.setLastNameSearchMetadata(SearchFieldMetadata.StartsWith);
		model.addAttribute("arrestSearchRequest", arrestSearchRequest);
		model.addAttribute("disposition", new Disposition());
		model.addAttribute("searchFieldMetaData", Arrays.asList(SearchFieldMetadata.StartsWith, SearchFieldMetadata.ExactMatch));
    }
    
	@GetMapping("")
	public String defaultSearch(HttpServletRequest request, Map<String, Object> model) throws Throwable {
		ArrestSearchRequest arrestSearchRequest = (ArrestSearchRequest) model.get("arrestSearchRequest");
		String searchContent = arrestService.findArrests(arrestSearchRequest, samlService.getSamlAssertion(request));
		String transformedResults = searchResultConverter.convertArrestSearchResult(searchContent);
		model.put("arrestSearchResults", searchContent); 
		model.put("arrestSearchContent", transformedResults); 
		model.put("arrestSearchRequest", arrestSearchRequest);
		return "arrest/arrests::resultsPage";
	}

	@PostMapping("/advancedSearch")
	public String advancedSearch(HttpServletRequest request, @Valid @ModelAttribute ArrestSearchRequest arrestSearchRequest, BindingResult bindingResult, 
			Map<String, Object> model) throws Throwable {
		
		log.info("arrestSearchRequest:" + arrestSearchRequest );
		String searchContent = arrestService.findArrests(arrestSearchRequest, samlService.getSamlAssertion(request));
		String transformedResults = searchResultConverter.convertArrestSearchResult(searchContent);
		model.put("arrestSearchResults", searchContent); 
		model.put("arrestSearchContent", transformedResults); 
		model.put("arrestSearchRequest", arrestSearchRequest);
		return "arrest/arrests::resultsList";
	}
	
	@GetMapping("/{id}")
	public String getArrest(HttpServletRequest request, @PathVariable String id, Map<String, Object> model) throws Throwable {
		String searchContent = arrestService.getArrest(id, samlService.getSamlAssertion(request));
		String transformedResults = searchResultConverter.convertArrestDetail(searchContent);
		model.put("arrestDetail", searchContent); 
		model.put("arrestDetailTransformed", transformedResults); 
		return "arrest/arrestDetail::arrestDetail";
	}

	@GetMapping("/dispositionForm")
	public String getDispositionForm(HttpServletRequest request, Disposition disposition, 
			Map<String, Object> model) throws Throwable {
		
		model.put("disposition", disposition);
		return "arrest/arrestDetail::dispositionForm";
	}

	@PostMapping("/saveDisposition")
	public String saveDisposition(HttpServletRequest request, @Valid @ModelAttribute Disposition disposition, BindingResult bindingResult, 
			Map<String, Object> model) throws Throwable {
		
		if (bindingResult.hasErrors()) {
			log.info("has binding errors");
			log.info(bindingResult.getAllErrors());
			return "arrest/arrestDetail::dispositionForm";
		}
		
//		TODO add the web service call to get the arrest detail again. 
		return "arrest/arrestDetail::arrestDetail";
	}
	

}