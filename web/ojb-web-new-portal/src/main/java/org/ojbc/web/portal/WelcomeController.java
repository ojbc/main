package org.ojbc.web.portal;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.web.portal.services.SamlService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WelcomeController {
	private static final Log log = LogFactory.getLog(WelcomeController.class);

	// inject via application.properties
	@Value("${welcome.message:test}")
	private String message = "Hello World";
	
	@Resource
	SamlService samlService;

	@RequestMapping("/")
	public String welcome(HttpServletRequest request, Map<String, Object> model) throws Exception {
		
		log.info("in welcome");
		samlService.getSamlAssertion(request);
		model.put("message", this.message);
		return "index";
	}

}