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
import java.util.concurrent.ConcurrentHashMap;

import org.ojbc.util.model.TotpUser;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("standalone")
public class TotpUserServiceStandAlone implements TotpUserService{
    private final ConcurrentHashMap<String, TotpUser> usersKeys = new ConcurrentHashMap<String, TotpUser>();

	@Override
	public Integer saveTotpUser(TotpUser totpUser) {
		usersKeys.put(totpUser.getUserName(), totpUser);
		return 1;
	}

	@Override
	public TotpUser getTotpUserByUserName(String userName) {
		return usersKeys.get(userName);
	}

	@Override
	public Integer deleteTotpUserByUserName(String userName) {
		TotpUser user = usersKeys.remove(userName);
		return user == null? 0:1; 
	}

	@Override
	public List<TotpUser> getAllTotpUsers() {
		return (List<TotpUser>) usersKeys.values();
	}

	@Override
	public Boolean isGoogleAuthUser(String email) {
		return true;
	}

	
}