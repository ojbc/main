package org.ojbc.web.portal.disposition;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DispostionController {

	@RequestMapping("/dispositions")
	public String welcome(Map<String, Object> model) {
		return "dispostions";
	}

}