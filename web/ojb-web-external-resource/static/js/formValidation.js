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
function validateFields() {
	var valid = true;

	var selopt = document.getElementById("opts").value;
	var emptyName = document.getElementById("name").value;
	var emptyAgency = document.getElementById("userAgency").value;
	var emptyEmail = document.getElementById("email").value;
	var emptyPhone = document.getElementById("userPhone").value;
	var phoneNumberRegEx = new RegExp("[0-9]{10}");

	if (selopt == 0) {
		alert("Must select an area that this submission relates to");
		valid = false;
	} else if (emptyName.trim().length <= 0) {
		alert("Cannot leave name field blank");
		valid = false;
	} else if (emptyAgency.trim().length <= 0) {
		alert("Cannot leave agency field blank");
		valid = false;
	} else if (emptyEmail.trim().length <= 0) {
		alert("Cannot leave email field blank");
		valid = false;
	} else if (emptyPhone.trim().length <= 0) {
		alert("Cannot leave phone field blank");
		valid = false;
	} else if (!phoneNumberRegEx.test(emptyPhone)
			|| emptyPhone.trim().length != 10) {
		alert("Phone number must be in the format ##########");
		valid = false;
	}

	return valid;
}
