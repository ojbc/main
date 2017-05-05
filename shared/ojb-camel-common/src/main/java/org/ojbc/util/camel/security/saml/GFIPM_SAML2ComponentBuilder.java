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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.util.camel.security.saml;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.common.util.StringUtils;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.common.saml.bean.AudienceRestrictionBean;
import org.apache.wss4j.common.saml.bean.SubjectBean;
import org.apache.wss4j.common.saml.bean.SubjectConfirmationDataBean;
import org.apache.wss4j.common.saml.builder.SAML2ComponentBuilder;
import org.apache.wss4j.common.saml.builder.SAML2Constants;
import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.common.SAMLObjectBuilder;
import org.opensaml.saml2.core.AudienceRestriction;
import org.opensaml.saml2.core.Condition;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.SubjectConfirmation;
import org.opensaml.saml2.core.SubjectConfirmationData;
import org.opensaml.samlext.saml2delrestrict.Delegate;
import org.opensaml.samlext.saml2delrestrict.DelegationRestrictionType;
import org.opensaml.xml.XMLObjectBuilderFactory;

public class GFIPM_SAML2ComponentBuilder {

	private static final Log log = LogFactory.getLog(GFIPM_SAML2ComponentBuilder.class);
	
    private static SAMLObjectBuilder<DelegationRestrictionType> delegationRestrictionBuilder;
    private static SAMLObjectBuilder<Delegate> delegateBuilder;
	private static SAMLObjectBuilder<Conditions> conditionsBuilder;
	private static XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();
	private static SAMLObjectBuilder<Subject> subjectBuilder;
	
    /**
     * Create an AudienceRestriction object
     * Use provided SAML2 Component Builder
     *
     * @param audienceURI of type String
     * @return an AudienceRestriction object
     */
    public static AudienceRestriction createAudienceRestriction(String audienceURI) {
    	
    	AudienceRestrictionBean arBean = new AudienceRestrictionBean();
    	
    	List<String> audList = new ArrayList<String>();
    	
    	audList.add(audienceURI);
    	arBean.setAudienceURIs(audList);
    	
    	return SAML2ComponentBuilder.createAudienceRestriction(arBean);
    }
    
    /**
     * Create a Subject.
     *
     * @param subjectBean of type SubjectBean
     * @return a Subject
     */
    @SuppressWarnings("unchecked")
    public static Subject createSaml2Subject(String inResponseTo, String recipient, DateTime notOnOrAfter, String confirmationMethodStr) 
        throws org.opensaml.xml.security.SecurityException, WSSecurityException {
        if (subjectBuilder == null) {
            subjectBuilder = (SAMLObjectBuilder<Subject>) 
                builderFactory.getBuilder(Subject.DEFAULT_ELEMENT_NAME);
        }
        Subject subject = subjectBuilder.buildObject();
        
        //NameID nameID = SAML2ComponentBuilder.createNameID(subjectBean);
        //subject.setNameID(nameID);
        
        SubjectConfirmationDataBean subjectConfirmationDataBean=new SubjectConfirmationDataBean();
        subjectConfirmationDataBean.setInResponseTo(inResponseTo);
        subjectConfirmationDataBean.setRecipient(recipient);
        subjectConfirmationDataBean.setNotAfter(notOnOrAfter);
        
        SubjectConfirmationData subjectConfData = null;
            subjectConfData = 
                SAML2ComponentBuilder.createSubjectConfirmationData(subjectConfirmationDataBean, null);
        
        if (confirmationMethodStr == null) {
            confirmationMethodStr = SAML2Constants.CONF_SENDER_VOUCHES;
        }
        SubjectConfirmation subjectConfirmation = 
            SAML2ComponentBuilder.createSubjectConfirmation(
                confirmationMethodStr, subjectConfData
            );
        
        subject.getSubjectConfirmations().add(subjectConfirmation);
        return subject;
    }
    
    /**
     * Use provided SAML2 Component Builder
     * @param method of type String
     * @param subjectConfirmationData of type SubjectConfirmationData
     * @return a SubjectConfirmation object
     */
    public static SubjectConfirmation createSubjectConfirmation(
        String method,
        SubjectConfirmationData subjectConfirmationData
    ) {
    	return SAML2ComponentBuilder.createSubjectConfirmation(method, subjectConfirmationData);
    }
    
    @SuppressWarnings("unchecked")
    private static DelegationRestrictionType createDelegateRestriction(String nameIDString, List<Delegate> existingDelegates) {
        if (delegationRestrictionBuilder == null) {
        	delegationRestrictionBuilder = (SAMLObjectBuilder<DelegationRestrictionType>) 
                builderFactory.getBuilder(DelegationRestrictionType.TYPE_NAME);        	
        }
        if (delegateBuilder == null) {
        	delegateBuilder = (SAMLObjectBuilder<Delegate>) 
                builderFactory.getBuilder(Delegate.DEFAULT_ELEMENT_NAME);
        }
       
        DelegationRestrictionType delegateRestriction = delegationRestrictionBuilder.buildObject();
        
        Delegate delegate = delegateBuilder.buildObject();
        
        DateTime delegateInstant = new DateTime();
        log.debug("Not before time: " + delegateInstant.toString("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));

        delegate.setDelegationInstant(delegateInstant);
        
        NameID nameID = createNameID(nameIDString);
        delegate.setNameID(nameID);

        //If we have existing delegates, add them to the beginning of the delegate conditions statement
        //This method must detach them from their original document to add them
        if (existingDelegates != null)
        {
        	for (Delegate existingDelegate : existingDelegates)
        	{
        		existingDelegate.detach();
        		delegateRestriction.getDelegates().add(existingDelegate);
        	}	
        }	
        
        delegateRestriction.getDelegates().add(delegate);
        
        return delegateRestriction;
    }
    
    /**
     * Create a NameID object
     * @param subject A String that will be the Name ID subject
     * @return NameID
     */
    private static NameID createNameID(String subject) {

    	SubjectBean subjectBean = new SubjectBean(subject, null, null,null);
    	return SAML2ComponentBuilder.createNameID(subjectBean);
    	
    }
    
    
    /**
     * Create a Conditions object
     *
     * @param entityIDfromAppliesToAddress a string to create an appliesTo element from
     * @param delegateNameID a string to create a new delegate element from
     * @param existingTokenConditions existing delegates to preserve
     * @return a Conditions object
     */
    @SuppressWarnings("unchecked")
    public static Conditions createConditions(String entityIDfromAppliesToAddress, String delegateNameID, Conditions existingTokenConditions) throws Exception{
        if (conditionsBuilder == null) {
            conditionsBuilder = (SAMLObjectBuilder<Conditions>) 
                builderFactory.getBuilder(Conditions.DEFAULT_ELEMENT_NAME);
        }
        
        //Within the <Conditions> element of the assertion, add a new <Delegate> element. 
        //Within the new <Delegate> element, add a <NameID> element with a value equal to the SAML 2.0 Metadata Entity ID that appears in the GFIPM CTF 
        //for the WSC that requested the assertion.58,59
        //Also, on the new <Delegate> element, add a 'DelegationInstant' attribute containing a timestamp representing the current moment in time.
        //Assertion/Conditions
        //Modify the 'NotBefore' attribute of the <Conditions> element to contain a timestamp representing the current moment in time.
        DateTime notBefore = new DateTime();
        log.debug("Not before time: " + notBefore.toString("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));

        //Modify the 'NotOnOrAfter' attribute of the <Conditions> element to contain a timestamp representing a moment in time that is N seconds in the future, 
        //where N represents length of time, in seconds, for which the new assertion will be valid.
        DateTime notOnOrAfter = notBefore.plusMinutes(5);
        log.debug("Not On or After time: " + notOnOrAfter.toString("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));

        Conditions conditions = conditionsBuilder.buildObject();

        conditions.setNotBefore(notBefore);
        conditions.setNotOnOrAfter(notOnOrAfter);

        if (!StringUtils.isEmpty(entityIDfromAppliesToAddress))
        {	  
        	AudienceRestriction audienceRestriction = GFIPM_SAML2ComponentBuilder.createAudienceRestriction(entityIDfromAppliesToAddress);
        	conditions.getAudienceRestrictions().add(audienceRestriction);
        }	  

        //It is possible, through a chain of SAML assertion delegations, for multiple <Delegate> elements to appear inside a <Conditions> element. 
        //When adding a new <Delegate> element, the ADS MUST NOT delete or modify any previously existing <Delegate> elements that already appear in the assertion.
        DelegationRestrictionType existingDelegateConditions = null;

        //Only one delegate condition is allowed, find and detach
        //http://docs.oasis-open.org/security/saml/Post2.0/sstc-saml-delegation-cs-01.html
        //A SAML authority MUST NOT include more than one <saml:Condition> element of this type within a <saml:Conditions> element of an assertion.
        if (existingTokenConditions != null)
        {
        	for (Condition condition : existingTokenConditions.getConditions())
        	{

        		String conditionSchema = condition.getSchemaType().getNamespaceURI();					
        		log.debug("Condition schema type: " + condition.getSchemaType().getNamespaceURI());

        		if (conditionSchema.equals("urn:oasis:names:tc:SAML:2.0:conditions:delegation"))
        		{
        			existingDelegateConditions = (DelegationRestrictionType)condition;
        			break;
        		}	
        	}	

        }	

        List<Delegate> existingDelegates = null;

        if (existingDelegateConditions != null)
        {
        	existingDelegates = existingDelegateConditions.getDelegates();
        }	


        if (!StringUtils.isEmpty(delegateNameID))
        {	  
        	DelegationRestrictionType delegateRestrictions = createDelegateRestriction(delegateNameID, existingDelegates);

        	conditions.getConditions().add(delegateRestrictions);
        }	  
		  
        return conditions;
    }    

}
