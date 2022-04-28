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
package org.ojbc.util.camel.processor.accesscontrol;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.message.Message;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.model.accesscontrol.AccessControlResponse;
import org.ojbc.util.model.saml.SamlAttribute;

public class OriAndIncidentAccessControlStrategy implements AccessControlStrategy{
private static final Log log = LogFactory.getLog(OriAndRoleAccessControlStrategy.class);
	
	List<String> authorizedORIs;
	
	List<String> unAuthorizedORIs;
	
	public OriAndIncidentAccessControlStrategy(List<String> authorizedORIsProperty) {
	    authorizedORIs = authorizedORIsProperty;
	}
	
	public OriAndIncidentAccessControlStrategy(List<String> authorizedORIsProperty, List<String> unAuthorizedORIsProperty) {
	    authorizedORIs = authorizedORIsProperty;
	    unAuthorizedORIs= unAuthorizedORIsProperty;
	}
	
	@Override
	public AccessControlResponse authorize(Exchange ex) {

		AccessControlResponse accessControlResponse = new AccessControlResponse();
		String employerORI = "";
		
		if (unAuthorizedORIs == null || unAuthorizedORIs.size() == 0)
		{	
			if (authorizedORIs == null || authorizedORIs.size() == 0){
				accessControlResponse.setAuthorized(false);
				accessControlResponse.setAccessControlResponseMessage("There are no authorized ORIs to query data.");
				
				return accessControlResponse;
			}
		}
		
		try{
			Message cxfMessage = ex.getIn().getHeader(CxfConstants.CAMEL_CXF_MESSAGE, Message.class);
			employerORI = SAMLTokenUtils.getSamlAttributeFromCxfMessage(cxfMessage, SamlAttribute.EmployerORI);
			
			//This allows user to only set unauthorized ORI list and does not need to set authorized list
			if (unAuthorizedORIs != null)
			{
				if (unAuthorizedORIs.contains(employerORI)) 
				{
				    accessControlResponse.setAccessControlResponseMessage("Users in the ORI: " + employerORI + 
				            " are NOT authorized to run this query. The ORI is as an unauthorized ORI.");
				    
				    accessControlResponse.setAuthorized(false);
				    
				    return accessControlResponse;
				}
				else
				{
					if (authorizedORIs==null)
					{
						accessControlResponse.setAuthorized(true);
						accessControlResponse.setAccessControlResponseMessage("Users in the ORI: " + employerORI + 
						        " are authorized to run this query.");
						
						return accessControlResponse;
					}	
				}
			}				
			
            if (isAuthorized(employerORI, cxfMessage)){
				accessControlResponse.setAuthorized(true);
				accessControlResponse.setAccessControlResponseMessage("Users in the ORI: " + employerORI + 
				        " are authorized to run this query.");
			}	
			else{
				accessControlResponse.setAuthorized(false);
				
				if (!authorizedORIs.contains(employerORI)) {
				    accessControlResponse.setAccessControlResponseMessage("Users in the ORI: " + employerORI + 
				            " are NOT authorized to run this query.");
				}else {
				    accessControlResponse.setAccessControlResponseMessage("Only Supervisors or Firearms Registration "
				            + "Records Personnels in the ORI: " + employerORI + 
				            " are authorized to run this query.");
				}
				
			}	
		} catch (Exception exception){
			accessControlResponse.setAuthorized(false);
			accessControlResponse.setAccessControlResponseMessage(exception.getMessage());
		}
		
			
		return accessControlResponse;
		
	}


    private boolean isAuthorized(String employerORI, Message cxfMessage) {
        String incidentAccessIndicator = SAMLTokenUtils.getSamlAttributeFromCxfMessage(cxfMessage, SamlAttribute.IncidentAccessIndicator);
        log.debug("IncidentAccessIndicator" + StringUtils.trimToEmpty(incidentAccessIndicator));
        
        return authorizedORIs.contains(employerORI) && BooleanUtils.toBoolean(incidentAccessIndicator);
    }
}
