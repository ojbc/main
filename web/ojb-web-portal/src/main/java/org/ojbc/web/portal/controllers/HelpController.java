package org.ojbc.web.portal.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/help/*")
public class HelpController  {

    @Value("${memberContext}")
    String stateContext;

    @RequestMapping("helpLeftBar")
    public String helpLeftBar()  {
	return "help/_helpLeftBar";
    }

    @RequestMapping("introduction")	
    public String introduction()  {
	return "help/_introduction";
    }

    @RequestMapping("search")	
    public String search()  {
	return "help/_search";
    }

    @RequestMapping("search_Person")	
    public String search_Person()  {
	return "help/_search_Person";
    }

    @RequestMapping("search_Incident")	
    public String search_Incident()  {
	return "help/_search_Incident";
    }

    @RequestMapping("search_Firearm")	
    public String search_Firearm()  {
	return "help/_search_Firearm";
    }

    @RequestMapping("sources")	
    public String sources()  {
	return "help/_sources";
    }

    @RequestMapping("sources_CJIS")	
    public String sources_CJIS()  {
	return "help/_sources_CJIS";
    }

    @RequestMapping("sources_eBW")	
    public String sources_eBW()  {
	return "help/_sources_eBW";
    }

    @RequestMapping("sources_Firearms")	
    public String sources_Firearms()  {
	return "help/_sources_Firearms";
    }

    @RequestMapping("simpleSearch")	
    public String simpleSearch()  {
	return "help/_simpleSearch";
    }

    @RequestMapping("howSearchWorks")	
    public String howSearchWorks()  {
	return "help/_howSearchWorks";
    }

    @RequestMapping("subManual")	
    public String subManual()  {
	return "help/_subManual";
    }

    @RequestMapping("subManual_Arrest")	
    public String subManual_Arrest()  {
	return "help/_subManual_Arrest";
    }

    @RequestMapping("howAuthWorks")	
    public String howAuthWorks()  {
	return "help/_howAuthWorks";
    }

    @ModelAttribute("stateContext")
    public String getStateContext()  {
	return stateContext;
    }

}
