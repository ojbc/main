package org.ojbc.intermediaries.sn.exception;

import java.util.ArrayList;
import java.util.List;

public class InvalidEmailAddressesException extends Exception {
	
	private static final long serialVersionUID = -3862892373028975812L;
	
	private List<String> invalidEmailAddresses;

	public InvalidEmailAddressesException(String message) {
		super(message);
	}

	public InvalidEmailAddressesException(String message, List<String> invalidEmailAddresses) {
		super(message);
		this.invalidEmailAddresses = new ArrayList<String>();
		this.invalidEmailAddresses.addAll(invalidEmailAddresses);
	}

	public List<String> getInvalidEmailAddresses() {
		return invalidEmailAddresses;
	}

	public void setInvalidEmailAddresses(List<String> invalidEmailAddresses) {
		this.invalidEmailAddresses = invalidEmailAddresses;
	}

	
}
