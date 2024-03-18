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
package org.ojbc.web.portal.totp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.model.TotpUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/totpUser")
public class TotpUserController {
	private final Log log = LogFactory.getLog(this.getClass());

	@Resource
	TotpUserService totpUserService;

	@GetMapping("")
	public String getUsers( Map<String, Object> model) throws Throwable {
		
		List<TotpUser> totpUsers = totpUserService.getAllTotpUsers();
		model.put("totpUsers", totpUsers);
		return "otp/totpUsers::resultsPage";
		
	}
	
	@PostMapping("/delete")
	public String finalizeArrest(HttpServletRequest request, 
			TotpUser totpUser, 
			Map<String, Object> model) throws Throwable {
		log.info("unregister the 2nd factor token for user :" + totpUser.getUserName());
		totpUserService.deleteTotpUserByUserName(totpUser.getUserName()); 
		List<TotpUser> totpUsers = totpUserService.getAllTotpUsers();
		model.put("totpUsers", totpUsers);
		return "otp/totpUsers::resultsPage";
	}
	
}