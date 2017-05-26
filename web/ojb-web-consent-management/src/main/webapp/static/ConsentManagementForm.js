$(document).ready(
		function() {
			var table = $('#example').DataTable({
				select : {
					style : "single",
					blurable : true
				},
				info : false,
				lengthChange : false,
				data : testdata,
				"columns" : [ {
					"data" : "personLastName",
					"title" : "Last Name"
				}, {
					"data" : "personFirstName",
					"title" : "First Name"
				}, {
					"data" : "personDOBString",
					"title" : "Date of Birth"
				}, {
					"data" : "bookingNumber",
					"title" : "Booking Number"
				}, {
					"data" : "nameNumber",
					"title" : "Name Number"
				} ]
			});
			table.on('select', function(e, dt, type, indexes) {
				var rowData = table.rows(indexes[0]).data()["0"];
				$("#selected-patient-name")
						.text(
								rowData.personLastName + ", "
										+ rowData.personFirstName);
			})
			table.draw();

		});