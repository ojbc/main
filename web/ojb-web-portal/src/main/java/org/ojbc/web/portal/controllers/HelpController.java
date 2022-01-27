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
package org.ojbc.web.portal.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/help")
public class HelpController  {

    @GetMapping("helpLeftBar")
    public String helpLeftBar()  {
    	return "help/helpLeftBar::helpLeftBarContent";
    }

    @GetMapping("introduction")	
    public String introduction()  {
    	return "help/introduction::introductionContent";
    }

    @GetMapping("search")	
    public String search()  {
	return "help/search::searchContent";
    }

    @GetMapping("searchPerson")	
    public String search_Person()  {
	return "help/searchPerson::searchPersonContent";
    }

    @RequestMapping("searchIncident")	
    public String search_Incident()  {
	return "help/searchIncident::searchIncidentContent";
    }

    @RequestMapping("searchFirearm")	
    public String search_Firearm()  {
	return "help/searchFirearm::searchFirearmContent";
    }

    @RequestMapping("sources")	
    public String sources()  {
	return "help/sources::sourcesContent";
    }

    @RequestMapping("sourcesCjis")	
    public String sources_CJIS()  {
	return "help/sourcesCjis::sourcesCjisContent";
    }

    @RequestMapping("sourcesEbw")	
    public String sources_eBW()  {
	return "help/sourcesEbw::sourcesEbwContent";
    }

    @RequestMapping("sourcesFirearms")	
    public String sources_Firearms()  {
	return "help/sourcesFirearms::sourcesFirearmsContent";
    }

    @RequestMapping("simpleSearch")	
    public String simpleSearch()  {
	return "help/simpleSearch::simpleSearchContent";
    }

    @RequestMapping("howSearchWorks")	
    public String howSearchWorks()  {
	return "help/howSearchWorks::howSearchWorksContent";
    }

    @RequestMapping("subManual")	
    public String subManual()  {
	return "help/subManual::subManualContent";
    }

    @RequestMapping("subManualArrest")	
    public String subManual_Arrest()  {
	return "help/subManualArrest::subManualArrestContent";
    }

    @RequestMapping("howAuthWorks")	
    public String howAuthWorks()  {
	return "help/howAuthWorks::howAuthWorksContent";
    }

}
