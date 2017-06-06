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
	
	@RequestMapping("/{mode:.+}")
	public ResponseEntity<String> operation(Model model, HttpServletRequest request, @PathVariable String mode) {
		return buildPageResponse(model, request, mode);
	}

	private ResponseEntity<String> buildPageResponse(Model model, HttpServletRequest request, String mode) {
		log.info("Building page response, mode=" + mode);
		String body = indexJsp;
		if ("cm".equals(mode)) {
			log.info("In normal mode, replacing script tag in html to reference context.js");
			body = replaceJspModeJavascript(body, "context.js");
		} else if ("cm-demo".equals(mode)) {
			log.info("In server demo mode, replacing script tag in html to reference context-server-demo.js");
			body = replaceJspModeJavascript(body, "context-server-demo.js");
		} else {
			// todo: fail here if no SAML assertion
			log.info("In local demo mode, skipping script tag replacement, continuing to reference context-local-demo.js");
		}
		return new ResponseEntity<String>(body, HttpStatus.OK);
	}
	
	private String replaceJspModeJavascript(String body, String jsFileName) {
		return body.replaceFirst(
				"(<!--/start-context-script/--><script src=\")(.+)(\"></script><!--/end-context-script/-->)",
				"$1static/" + jsFileName + "$3");
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		InputStream is = servletContext.getResourceAsStream("/index.jsp");
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
		log.info("Read index.jsp file into cache, length=" + indexJsp.length() + " bytes.");
	}

}
