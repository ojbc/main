package org.ojbc.intermediaries.sn.util;

import java.util.ArrayList;
import java.util.List;

public class EmailAddressValidatorResponse {

	private boolean areAllEmailAddressValid = true;
	private List<String> invalidEmailAddresses = new ArrayList<String>();
	
	public boolean isAreAllEmailAddressValid() {
		return areAllEmailAddressValid;
	}
	public void setAreAllEmailAddressValid(boolean areAllEmailAddressValid) {
		this.areAllEmailAddressValid = areAllEmailAddressValid;
	}
	public List<String> getInvalidEmailAddresses() {
		return invalidEmailAddresses;
	}
	public void setInvalidEmailAddresses(List<String> invalidEmailAddresses) {
		this.invalidEmailAddresses = invalidEmailAddresses;
	}
	
}
