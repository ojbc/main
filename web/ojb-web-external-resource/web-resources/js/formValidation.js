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
