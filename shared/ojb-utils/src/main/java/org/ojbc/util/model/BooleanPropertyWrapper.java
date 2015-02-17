package org.ojbc.util.model;

/**
 * This class wraps a property to allow it to be used in a Camel route in a way
 * that is easier to test.  It is difficult to change Camel properties in unit tests:
 * 
 * http://camel.465427.n5.nabble.com/Change-property-in-unit-test-td5758017.html
 * 
 * If not set, this property will return 'true' by default.
 * 
 * 
 * @author yogeshchawla
 *
 */
public class BooleanPropertyWrapper {
	
	public Boolean booleanProperty;
	
	public boolean sendEmailNotificationsToRecipients()
	{
		//We default to true if the property is not set
		if (booleanProperty == null)
		{
			booleanProperty = true;
		}	
		
		return booleanProperty;		
	}

	public Boolean getBooleanProperty() {
		return booleanProperty;
	}

	public void setBooleanProperty(Boolean booleanProperty) {
		this.booleanProperty = booleanProperty;
	}

}
