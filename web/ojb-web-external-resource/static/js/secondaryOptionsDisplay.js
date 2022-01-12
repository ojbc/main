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
function showForm() {
	var selopt = document.getElementById("opts").value;
	if (selopt == 0) {
		document.getElementById("f1").style.display = "none";
		document.getElementById("f2").style.display = "none";
		document.getElementById("f3").style.display = "none";
	}
	if (selopt == 1) {
		document.getElementById("f1").style.display = "block";
		document.getElementById("f2").style.display = "none";
		document.getElementById("f3").style.display = "none";
	}
	if (selopt == 2) {
		document.getElementById("f1").style.display = "none";
		document.getElementById("f2").style.display = "block";
		document.getElementById("f3").style.display = "none";
	}
	if (selopt == 3) {
		document.getElementById("f1").style.display = "none";
		document.getElementById("f2").style.display = "none";
		document.getElementById("f3").style.display = "block";
	}
}

function showFormDynamic() {
	var selopt = document.getElementById("opts").value;
	var MAX_OPTS = 3;

	for (i = 1; i <= MAX_OPTS; i++) {
		if (selopt == i) {
			document.getElementById(i).style.display = "block";
		} else {
			document.getElementById(i).style.display = "none";
		}
	}
}

function myGetDateTime() {
	var d = new Date();
	var x = document.getElementById("thisDateTime");
	x.innerHTML = d.toUTCString();
}
