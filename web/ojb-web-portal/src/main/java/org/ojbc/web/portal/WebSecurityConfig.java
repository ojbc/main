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
import org.ojbc.web.security.CustomLogoutSuccessHandler;
import org.ojbc.web.security.OJBCAccessDeniedHandler;
import org.ojbc.web.security.PortalAuthenticationDetailsSource;
import org.ojbc.web.security.SamlAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesUserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig {
	@Resource
	@Lazy
	SamlService samlService;
	
	@Resource
	@Lazy
	PortalAuthenticationDetailsSource portalAuthenticationDetailsSource;
	
	@Resource
	@Lazy
	OJBCAccessDeniedHandler ojbcAccessDeniedHandler;
	
	@Resource
	@Lazy
	CustomLogoutSuccessHandler customLogoutSuccessHandler;
	
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http
        .authorizeHttpRequests(auth -> auth
            .antMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/logoutSuccess/**", "/static/**",
                    "/otp/**", "/resources/css/**", "/code/**", "/acknowlegePolicies", "/portal/leftBar", "/actuator/**", 
                    "/portal/defaultLogout", "/portal/performLogout", "/403", "/otp/inputForm", "/error", "/samlTokenInfo").permitAll()
            .anyRequest().hasAuthority("AUTHZ_PORTAL"))
        .logout(logout -> logout
//              .logoutUrl("/portal/performLogout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessHandler(customLogoutSuccessHandler)
                .deleteCookies("JSESSIONID")
                .clearAuthentication(true)
                .permitAll())
        .headers(headers -> headers
                .frameOptions()
                .sameOrigin())
        .addFilterBefore(samlAuthenticationFilter(authenticationManager),
                LogoutFilter.class)
        .exceptionHandling(handling -> handling.accessDeniedHandler(ojbcAccessDeniedHandler));
        return http.build();
    }
    
    
    @Bean
    @Lazy
    AuthenticationManager authenticationManager() throws Exception {
        final List<AuthenticationProvider> providers = new ArrayList<>(1);
        providers.add(preauthAuthProvider());
        return new ProviderManager(providers);
    }

    @Bean
    PreAuthenticatedAuthenticationProvider preauthAuthProvider() {
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
    CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        loggingFilter.setIncludeHeaders(false);
        return loggingFilter;
    }    
}