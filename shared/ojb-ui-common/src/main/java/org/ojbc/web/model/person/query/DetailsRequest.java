package org.ojbc.web.model.person.query;

import java.io.Serializable;
import java.lang.reflect.Field;

public class DetailsRequest implements Serializable{

    private static final long serialVersionUID = -8308338659183972879L;
    private String identificationID;
	private String identificationSourceText;

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
	
}
