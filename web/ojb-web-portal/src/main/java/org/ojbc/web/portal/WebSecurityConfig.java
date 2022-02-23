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
package org.ojbc.web.portal;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.ojbc.web.portal.services.SamlService;
import org.ojbc.web.security.OJBCAccessDeniedHandler;
import org.ojbc.web.security.PortalAuthenticationDetailsSource;
import org.ojbc.web.security.SamlAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesUserDetailsService;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Resource
	SamlService samlService;
	
	@Resource
	PortalAuthenticationDetailsSource portalAuthenticationDetailsSource;
	
	@Resource
	OJBCAccessDeniedHandler ojbcAccessDeniedHandler;
	
	@Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/logoutSuccess/**", "/static/**",
        		"/otp/**", "/resources/css/**", "/code/**", "/acknowlegePolicies", "/portal/**", 
        		"/portal/defaultLogout", "/portal/performLogout", "/403", "/otp/inputForm", "/error");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
		    .authorizeRequests()
		    .anyRequest().hasAuthority("AUTHZ_PORTAL")
		    .and()
	    	.logout().logoutUrl("/portal/performLogout").deleteCookies("JSESSIONID").clearAuthentication(true).permitAll()
		    .and().securityContext()
		    .and()
		    .headers()
		    .frameOptions()
			.sameOrigin()
			.and()
		    .addFilterBefore(samlAuthenticationFilter(authenticationManager()),
		      LogoutFilter.class)
		    .exceptionHandling().accessDeniedHandler(ojbcAccessDeniedHandler);
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(preauthAuthProvider());
    }
    
    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        final List<AuthenticationProvider> providers = new ArrayList<>(1);
        providers.add(preauthAuthProvider());
        return new ProviderManager(providers);
    }
    
    @Bean
    public PreAuthenticatedAuthenticationProvider preauthAuthProvider() {
    	PreAuthenticatedAuthenticationProvider preauthAuthProvider =
    		new PreAuthenticatedAuthenticationProvider();
    	preauthAuthProvider.setPreAuthenticatedUserDetailsService(
    			new PreAuthenticatedGrantedAuthoritiesUserDetailsService());
    	return preauthAuthProvider;
    }
    
    public SamlAuthenticationFilter samlAuthenticationFilter(
        final AuthenticationManager authenticationManager) {
    	SamlAuthenticationFilter filter = new SamlAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationDetailsSource(portalAuthenticationDetailsSource);
        filter.setSamlService(samlService);
        filter.setCheckForPrincipalChanges(true);
        return filter;
    }   
    
    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("AUTHZ_"); 
    }
    
    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        loggingFilter.setIncludeHeaders(false);
        return loggingFilter;
    }    
}