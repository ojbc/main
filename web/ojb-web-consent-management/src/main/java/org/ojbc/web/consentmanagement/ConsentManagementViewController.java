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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.web.consentmanagement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ServletContextAware;

@Controller
public class ConsentManagementViewController implements ServletContextAware {
	
	private final Log log = LogFactory.getLog(ConsentManagementViewController.class);
	
	private String indexJsp;
	
	@RequestMapping("/cm")
	public ResponseEntity<String> cm(Model model, HttpServletRequest request) {
		// todo: make sure request has the saml stuff in the header
		log.info("Request: /cm");
		return buildPageResponse(model, request, false);
	}

	@RequestMapping("/cm-demo")
	public ResponseEntity<String> cm_demo(Model model, HttpServletRequest request) {
		// todo: make sure request has the saml stuff in the header
		log.info("Request: /cm-demo");
		return buildPageResponse(model, request, true);
	}

	@RequestMapping({"/cm/assets/{assetname:.+}", "/cm-demo/assets/{assetname:.+}"})
	public String assets(Model model, HttpServletRequest request, @PathVariable("assetname") String asset) {
		log.info("Request: /.../assets/" + asset);
		return asset;
	}
	
	private ResponseEntity<String> buildPageResponse(Model model, HttpServletRequest request, boolean demo) {
		String body = indexJsp;
		if (!demo) {
			log.info("In normal mode, replacing script tag in html to reference context.js");
			body = body.replaceFirst("(<!--/start-context-script/--><script src=\")(.+)(\"></script><!--/end-context-script/-->)", "$1static/context.js$3");
		} else {
			log.info("In demo mode, skipping script tag replacement, continuing to reference context-demo.js");
		}
		return new ResponseEntity<String>(body, HttpStatus.OK);
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		InputStream is = servletContext.getResourceAsStream("index.jsp");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuffer sb = new StringBuffer(1024*10);
		String line = null;
		try {
		while ((line = br.readLine()) != null) {
			sb.append(line).append("\n");
		}
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
		indexJsp = sb.toString();
	}

}
