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
package org.ojbc.web.model.person.query;

import java.io.Serializable;
import java.lang.reflect.Field;

import org.ojbc.web.OJBCWebServiceURIs;

public class DetailsRequest implements Serializable{

    private static final long serialVersionUID = -8308338659183972879L;
    private String identificationID;
	private String identificationSourceText;
	private String queryType;
	private String activeAccordionId; 

	//Logging
	private String purpose;
	private String onBehalfOf;
	
	public String getIdentificationID() {
		return identificationID;
	}
	public void setIdentificationID(String identificationID) {
		this.identificationID = identificationID;
	}
	public String getIdentificationSourceText() {
		return identificationSourceText;
	}
	public void setIdentificationSourceText(String identificationSourceText) {
		this.identificationSourceText = identificationSourceText;
	}
	
	public String toString() {
		  StringBuilder result = new StringBuilder();
		  String newLine = System.getProperty("line.separator");

		  result.append( this.getClass().getName() );
		  result.append( " Object {" );
		  result.append(newLine);

		  //determine fields declared in this class only (no fields of superclass)
		  Field[] fields = this.getClass().getDeclaredFields();

		  //print field names paired with their values
		  for ( Field field : fields  ) {
		    result.append("  ");
		    try {
		      result.append( field.getName() );
		      result.append(": ");
		      //requires access to private field:
		      result.append( field.get(this) );
		    } catch ( IllegalAccessException ex ) {
		      System.out.println(ex);
		    }
		    result.append(newLine);
		  }
		  result.append("}");

		  return result.toString();
		}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getOnBehalfOf() {
		return onBehalfOf;
	}
	public void setOnBehalfOf(String onBehalfOf) {
		this.onBehalfOf = onBehalfOf;
	}
	public String getQueryType() {
		return queryType;
	}
	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

    public boolean isJuvenileDetailRequest() {
        return OJBCWebServiceURIs.JUVENILE_HISTORY.equals(identificationSourceText) || 
                OJBCWebServiceURIs.JUVENILE_HISTORY_SEARCH.equals(identificationSourceText);
    }
    public String getActiveAccordionId() {
        return activeAccordionId;
    }
    public void setActiveAccordionId(String activeAccordionId) {
        this.activeAccordionId = activeAccordionId;
    }

}
