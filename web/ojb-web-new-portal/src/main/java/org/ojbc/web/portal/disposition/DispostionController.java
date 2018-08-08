package org.ojbc.web.portal.disposition;
import java.time.LocalDate;
import java.util.Map;

import javax.annotation.Resource;

import org.ojbc.web.portal.services.SearchResultConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({"dispositionSearchResults", "dispositionSearchRequest"})
public class DispostionController {
	@Autowired
	DispositionService dispostionService;
	
	@Resource
	SearchResultConverter searchResultConverter;

	@GetMapping("/dispositions")
	public String welcome(Map<String, Object> model) throws Throwable {
		DispositionSearchRequest dispositionSearchRequest = new DispositionSearchRequest();
		dispositionSearchRequest.setDispositionStartDate(LocalDate.now().minusDays(90));
		dispositionSearchRequest.setDispositionEndDate(LocalDate.now());
		
		String searchContent = dispostionService.findDispositions(dispositionSearchRequest);
		String transformedResults = searchResultConverter.convertDispositionSearchResult(searchContent);
		model.put("dispositionSearchResults", searchContent); 
		model.put("dispositionSearchContent", transformedResults); 
		model.put("dispositionSearchRequest", dispositionSearchRequest);
		return "disposition/dispositions";
	}

}