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

import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.web.security.UserOTPDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.FileTemplateResolver;

@Configuration
@PropertySources({
	@PropertySource(value = "${spring.config.additional-location}application.properties"),
    @PropertySource(value = "${spring.config.additional-location}ojbc-web-application-connector.cfg")
})
public class WebConfig implements WebMvcConfigurer {
	private static final Log log = LogFactory.getLog( WebConfig.class );
    @Autowired 
    AppProperties appProperties;
    @Autowired 
    Environment env;
    /**
     * This bean + the @PropertySources is needed to use @Value.
     * @return
     */
    @Bean  
    static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
    	PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer(); 
    	propertySourcesPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(Boolean.TRUE);
        return propertySourcesPlaceholderConfigurer;
    }    
    
    @Bean
    @Description("Thymeleaf file template resolver serving HTML 5")
    FileTemplateResolver externalTemplateResolver() {
    	
    	FileTemplateResolver templateResolver = new FileTemplateResolver();
    	
    	templateResolver.setPrefix(env.getProperty("web.external.resource.home") + "/templates/");
    	templateResolver.setCacheable(false);
    	templateResolver.setSuffix(".html");
    	templateResolver.setTemplateMode("HTML");
    	templateResolver.setCharacterEncoding("UTF-8");
    	templateResolver.setOrder(1); 
    	templateResolver.setCheckExistence(true);
    	
    	return templateResolver;
    }
    
    @Bean
    @Description("Thymeleaf classLoader template resolver serving HTML 5")
    ClassLoaderTemplateResolver templateResolver() {

    	ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();

        templateResolver.setPrefix("templates/");
        templateResolver.setCacheable(false);
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCheckExistence(true);
    	templateResolver.setOrder(2); 

        return templateResolver;
    }

    
    @Bean
    @Description("Thymeleaf template engine with Spring integration")
    SpringTemplateEngine templateEngine() {

    	SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(templateResolver());
        templateEngine.addTemplateResolver(externalTemplateResolver());
        templateEngine.addDialect(new SpringSecurityDialect());
        templateEngine.addDialect(new Java8TimeDialect());

        return templateEngine;
    }

    @Bean
    @Description("Thymeleaf view resolver")
    ViewResolver thymeleafViewResolver() {

    	ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding("UTF-8");

        return viewResolver;
    }

    @Bean
    MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
          = new ReloadableResourceBundleMessageSource();
         
        messageSource.setBasename(env.getProperty("spring.config.additional-location") + "messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
    
    @Bean
	public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }
    
    @Bean
    @ConditionalOnProperty(name = "ojbcMailSenderBean", havingValue = "ojbcMailSender")
    JavaMailSender ojbcMailSender() {
    	JavaMailSenderImpl ojbcMailSender = new JavaMailSenderImpl(); 
    	ojbcMailSender.setHost(appProperties.getMailSenderHost());
    	ojbcMailSender.setPort(appProperties.getMailSenderPort());
    	ojbcMailSender.setProtocol(appProperties.getMailSenderTransportProtocol());
    	
    	ojbcMailSender.getJavaMailProperties().put("mail.transport.protocol", appProperties.getMailSenderTransportProtocol()); 
    	ojbcMailSender.getJavaMailProperties().put("mail.smtp.auth", appProperties.getMailSenderSmtpAuth()); 
    	ojbcMailSender.getJavaMailProperties().put("mail.smtp.starttls.enable", appProperties.getMailSenderSmtpStarttlesEnable());
    	
    	if (StringUtils.isNotBlank(appProperties.getMailUserName()) && !appProperties.getMailUserName().contains("@")) {
    		ojbcMailSender.getJavaMailProperties().put("mail.smtp.user", appProperties.getMailUserName()); 
    	}
    	ojbcMailSender.getJavaMailProperties().put("mail.debug", appProperties.getMailSenderDebug());
    	
    	if (StringUtils.isNotBlank(appProperties.getMailUserName()) && appProperties.getMailUserName().contains("@")) {
    		ojbcMailSender.setUsername(appProperties.getMailUserName());
    	}
    	
    	if (StringUtils.isNotBlank(appProperties.getMailPassword())) {
    		ojbcMailSender.setPassword(appProperties.getMailPassword());
    	}

    	log.info("Created ojbcMailSender bean with the properties: " + ojbcMailSender.getJavaMailProperties());
    	log.info("ojbcMailSender host: " + ojbcMailSender.getHost());
    	log.info("ojbcMailSender port: " + ojbcMailSender.getPort());
    	return ojbcMailSender;
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
			.addResourceHandler("/resources/**")
			.addResourceLocations("/resources/","file:" + System.getProperty("web.external.resource.home") + "/");
        registry
			.addResourceHandler("/static/**")
			.addResourceLocations("/resources/static/","file:" + System.getProperty("web.external.resource.home") + "/static/");
        registry
	        .addResourceHandler("/xsl/**")
	        .addResourceLocations("/resources/xsl/","file:" + System.getProperty("web.external.resource.home") + "/xsl/");
    }
    
    @Bean
    ConcurrentHashMap<String, UserOTPDetails> otpMap(){
    	return new ConcurrentHashMap<String, UserOTPDetails>();
    }
}