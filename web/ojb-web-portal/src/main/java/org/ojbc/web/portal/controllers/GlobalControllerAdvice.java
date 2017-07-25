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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {
	
	private final Log log = LogFactory.getLog(this.getClass());

    @Value("${bannerPath:/static/images/banner/Banner.png}")
    String bannerPath;

    @Value("${themePath:/static/css/style.css}")
    String themePath;
    
    @Value("${secondaryOptionsDisplay:false}")
    Boolean secondaryOptionsDisplay;
    
    @Value("${singleClickForDetail:false}")
    Boolean singleClickForDetail;

    @Value("${customStyleCssPath:}")
    String customStyleCssPath;
    
    @ModelAttribute
    public void setupModelAttributes(Model model) {
        model.addAttribute("bannerPath", bannerPath);
        model.addAttribute("themePath", themePath);
        model.addAttribute("secondaryOptionsDisplay", secondaryOptionsDisplay);
        model.addAttribute("singleClickForDetail", singleClickForDetail);
        model.addAttribute("customStyleCssPath", customStyleCssPath);
    }
    
    @ExceptionHandler(Exception.class)
//    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason="Internal Server Error")
	public String handleAllException(HttpServletRequest request, Exception ex){
		
		log.error("Got exception when processing the request", ex); 
		return "/error/500";
	}
    
}