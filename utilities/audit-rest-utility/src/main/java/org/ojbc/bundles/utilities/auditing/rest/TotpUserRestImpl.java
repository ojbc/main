package org.ojbc.bundles.utilities.auditing.rest;
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


import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.bundles.utilities.auditing.totp.TotpUserDAO;
import org.ojbc.util.model.TotpUser;
import org.springframework.stereotype.Service;

@Service
public class TotpUserRestImpl implements TotpUserRestInterface {

	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(TotpUserRestImpl.class);
	
	@Resource
	private TotpUserDAO totpUserDAO;
	
	@Override
	public Integer saveTotpUser(TotpUser totpUser) {
		Integer pkId = totpUserDAO.saveTotpUser(totpUser); 
		return pkId;
	}

	@Override
	public TotpUser getTotpUserByUserName(String userName) {
		TotpUser totpUser = totpUserDAO.getTotpUserByUserName(userName); 
		return totpUser;
	}

	@Override
	public Integer deleteTotpUserByUserName(String userName) {
		Integer deleted = totpUserDAO.deleteTotpUserByUserName(userName); 
		return deleted;
	}

	@Override
	public Response returnTotpUsers() {
		List<TotpUser> totpUsers = totpUserDAO.retrieveAllTotpUsers();
		
		return Response.status(Status.OK).entity(totpUsers).build();
	}

}