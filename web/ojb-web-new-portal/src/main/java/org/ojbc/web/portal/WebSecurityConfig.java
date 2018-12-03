package org.ojbc.web.portal;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.ojbc.web.portal.security.PortalAuthenticationDetailsSource;
import org.ojbc.web.portal.security.SamlAuthenticationFilter;
import org.ojbc.web.portal.services.SamlService;
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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesUserDetailsService;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Resource
	SamlService samlService;
	
	@Resource
	PortalAuthenticationDetailsSource portalAuthenticationDetailsSource;
	
	@Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.authorizeRequests()
		    .antMatchers("/arrests").hasAuthority("AUTHZ_MUNI")
		    .antMatchers("/dispositions").hasAuthority("AUTHZ_DA")
		    .antMatchers("/**").authenticated()
		    .and()
		    .sessionManagement()
		    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		    .and().securityContext()
		    .and()
		    .addFilterBefore(samlAuthenticationFilter(authenticationManager()),
		      LogoutFilter.class);
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
    		new PreAuthenticatedGrantedAuthoritiesUserDetailsService()
    	);
    	return preauthAuthProvider;
    }
    
    @Bean
    public SamlAuthenticationFilter samlAuthenticationFilter(
        final AuthenticationManager authenticationManager) {
    	SamlAuthenticationFilter filter = new SamlAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationDetailsSource(portalAuthenticationDetailsSource);
        filter.setSamlService(samlService);
        return filter;
    }   
    
    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("AUTHZ_"); 
    }
}