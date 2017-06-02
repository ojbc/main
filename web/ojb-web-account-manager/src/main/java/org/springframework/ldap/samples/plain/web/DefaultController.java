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
package org.springframework.ldap.samples.plain.web;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.samples.plain.dao.PersonDao;
import org.springframework.ldap.samples.plain.domain.Person;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Default controller.
 * 
 */
@Controller
public class DefaultController {

	Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	private PersonDao personDao;

	@RequestMapping("/welcome.do")
	public void welcomeHandler() {
	}

	@RequestMapping("showForm.do")
	public String showForm(Model model) {
		model.addAttribute("changePasswordCommand", new ChangePasswordCommand());
		return "showForm";
	}
	
	@RequestMapping(value="updatePassword.do", method = RequestMethod.POST)
	public String updatePassword(@Valid ChangePasswordCommand command, 
            BindingResult result, Model model) {
		log.debug("update password with: " + command.toString());
		
		if (result.hasErrors()){
			return "showForm";
		}
		
		Person person = null;
		try{
			person = personDao.authenticate(command.getUserId(), command.getOldPassword());
		}
		catch(Throwable e){
			e.printStackTrace();
			result.addError(new ObjectError("changePasswordCommand", "User credential error, valid user ID and current password needed"));
			result.rejectValue("userId", null, "");
			result.rejectValue("oldPassword", null, "");
			return "showForm";
		}
			
		log.debug("Person: " + person.toString());
		
		person.setPassword(command.getConfirmNewPassword());
		
		try{
			personDao.update(person);
		}
		catch(Throwable e){
			
			String errorMessage = StringUtils.substringBeforeLast(StringUtils.substringAfterLast(e.getMessage(), ":"), "]");
			log.error("\nException Message:" + e.getMessage());
			log.error("\nException Message Detail:" + StringUtils.substringBeforeLast(StringUtils.substringAfterLast(e.getMessage(), ":"), "]"));
			result.addError(new ObjectError("changePasswordCommand", errorMessage));
			return "showForm";
		}
		return "success";
	}
}
