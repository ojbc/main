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

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.web.portal.AppProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;

@ControllerAdvice
@SessionAttributes({"showPrintButton", "sensitiveInfoAlert" })
public class GlobalControllerAdvice {
	
	@Resource
    AppProperties appProperties;
	
	private final Log log = LogFactory.getLog(this.getClass());

    @Value("${themePath:/static/css/style.css}")
    String themePath;
    
    @Value("${secondaryOptionsDisplay:false}")
    Boolean secondaryOptionsDisplay;
    
    @Value("${singleClickForDetail:false}")
    Boolean singleClickForDetail;

    @Value("${customStyleCssPath:}")
    String customStyleCssPath;
    
    @Value("${inactivityTimeout:false}")
    Boolean inactivityTimeout;
    
    @Value("${showSignOutButton:false}")
    Boolean showSignOutButton;
    
    @Value("${showPrintButton:false}")
    Boolean showPrintButton;
    
    @Value("${inactivityTimeoutInSeconds:1800}")
    String inactivityTimeoutInSeconds;
    
    private DateTimeFormatter dateTimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @ModelAttribute
    public void setupModelAttributes(Model model) {
        model.addAttribute("bannerPath", appProperties.getBannerPath());
        model.addAttribute("bannerInitial", appProperties.getBannerInitial());
        model.addAttribute("bannerFullname", appProperties.getBannerFullname());
        model.addAttribute("themePath", themePath);
        model.addAttribute("secondaryOptionsDisplay", secondaryOptionsDisplay);
        model.addAttribute("singleClickForDetail", singleClickForDetail);
        model.addAttribute("customStyleCssPath", customStyleCssPath);
        model.addAttribute("inactivityTimeout", inactivityTimeout);
        model.addAttribute("showSignOutButton", showSignOutButton);
        model.addAttribute("showPrintButton", showPrintButton);
        model.addAttribute("dateTimeformatter", dateTimeformatter);
        model.addAttribute("inactivityTimeoutInSeconds", inactivityTimeoutInSeconds);
        model.addAttribute("footerText", appProperties.getFooterText());
    }
    
    @ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
    public String handle405Exception(HttpServletRequest request, Exception ex){
    	
    	log.error("Got exception when processing the request", ex); 
    	return "/error/405";
    }
    
    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException {    
        String acceptHeader = request.getHeader("Accept");
        log.error("Got exception when processing the request", e); 
       
        String returnString = "A server-side error occurred. If problem persists, contact your IT department.";
        // If Accept header exists, check if it expects a response of type json, otherwise just return text/html
        // Use apache commons lang3 to escape json values
        if(acceptHeader.contains("application/json")) {
            // return as JSON
            String jsonString = 
                    "{\"success\": false, \"message\": \"" + returnString + "\" }";
//            String jsonString = 
//                    "{\"success\": false, \"message\": \"" + StringEscapeUtils.escapeJson(e.getMessage()) + "\" }";
        
            return jsonString;
        } else {
            //return as HTML
            response.setContentType("text/html");
//            return response.toString();
            return returnString;
        }
    }    
//    @ResponseStatus(value=HttpStatus.METHOD_NOT_ALLOWED, reason="Method Not Allowed")
//    public String handle405Exception(HttpServletRequest request, Exception ex){
//    	
//    	log.error("Got exception when processing the request", ex); 
//    	return "/error/500";
//    }
    
}