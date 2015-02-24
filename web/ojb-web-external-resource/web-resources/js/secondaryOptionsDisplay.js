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
