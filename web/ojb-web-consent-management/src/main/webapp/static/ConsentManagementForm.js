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

const REFRESH_INTERVAL_LENGTH = 10000;
var refreshIntervalId = null;
var table = null;

$(document).ready(
		function() {
	    table = $('#inmate-table').DataTable({
		select : {
		    style : "single"
		},
		dom: 'frtiBp',
		info : false,
		lengthChange : false,
		columns : [ {
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
		} ],
		buttons: [
		            {
		                text: 'Clear Selection',
		                action: function () {
		                    table.rows().deselect();
		                    clearDecisionRecordFields();
		                    updateUIState();
		                }
		            }
		        ]
	    });
	    $('body').on('click', function() {
		updateUIState();
	    });
	    $('#dcn').on('input', function() {
		updateUIState();
	    });
	    table.on('select', function(e, dt, type, indexes) {
		var rowData = table.rows(indexes[0]).data()["0"];
		$("#selected-patient-name")
			.text(
				rowData.personLastName + ", "
					+ rowData.personFirstName);
		clearDecisionRecordFields();
	    });
	    table.draw();
	    refreshData();
	    refreshIntervalId = setInterval(refreshData, REFRESH_INTERVAL_LENGTH);
	    $("#consent-save-button").prop('disabled', true);
	    $("#consent-save-button").on('click', function() {
		    console.log('save button clicked');
		    row = table.row({selected: true}).data();
		    console.log(row);
		});
	});

clearDecisionRecordFields = function() {
    $('#dcn').val('');
    $('input[name=decision]:checked').prop('checked', false);
}

updateUIState = function() {
    rowSelected = (table.rows({selected: true}).count() > 0);
    dcnValue = $('#dcn').val();
    decisionValue = $('input[name=decision]:checked').val();
    //console.log("Selection? >> " + rowSelected + ', dcn=' + dcnValue + ', decision=' + decisionValue);
    if (!rowSelected) {
	if (refreshIntervalId == null) {
	    refreshIntervalId = setInterval(refreshData, REFRESH_INTERVAL_LENGTH);
	    //console.log('Starting refresh cycle...')
	} else {
	    //console.log('(Refresh cycle already running, doing nothing.)')
	}
    } else {
	if (refreshIntervalId != null) {
	    clearInterval(refreshIntervalId);
	    refreshIntervalId = null;
	    //console.log('...stopping refresh cycle.')
	} else {
	    //console.log('(Refresh cycle is already stopped, doing nothing.)')
	}
    }
    val = (rowSelected && dcnValue != '' && decisionValue != null);
    $("#consent-save-button").prop('disabled', !val);
}

refreshDataViaAjax = function(demodataOK) {
    $.ajax({
	url : "/ojb-web-consent-management-service/cm-api/findPendingInmates",
	headers : {
	    "demodata-ok" : demodataOK
	}
    }).done(function(data) {
	console.log(data)
	$('#inmate-table').DataTable().clear().rows.add(data).draw()
    });
}
