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

import java.util.ArrayList;
import java.util.List;

import org.ojbc.util.model.TotpUser;
import org.ojbc.web.portal.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Profile({"audit-search"})
public class TotpUserRestService implements TotpUserService{
	private final WebClient webClient;

	public TotpUserRestService(WebClient.Builder webClientBuilder, @Autowired AppProperties appProperties) {
		this.webClient = webClientBuilder.baseUrl(appProperties.getTotpUserServerBaseUrl())
				.codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)) .build();
	}

	@Override
	public Integer saveTotpUser(TotpUser totpUser) {
		return this.webClient.post().uri("")
				.body(BodyInserters.fromValue(totpUser))
				.retrieve()
				.bodyToMono(Integer.class)
				.block();
	}

	@Override
	public TotpUser getTotpUserByUserName(String userName) {
		return  this.webClient.get().uri(userName)
				.retrieve()
				.bodyToMono( TotpUser.class)
				.block();
	}

	@Override
	public Integer deleteTotpUserByUserName(String userName) {
		return this.webClient.delete()
		        .uri(userName)
		        .retrieve()
		        .bodyToMono(Integer.class)
		        .block();
	}
	
	@Override
	public List<TotpUser> getAllTotpUsers() {
		return this.webClient.get()
				.uri("totpUsers")
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<List<TotpUser>>() {})
				.defaultIfEmpty(new ArrayList<TotpUser>())
				.block();
	}
	
	
}