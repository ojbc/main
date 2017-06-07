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
		} ]
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
		    //console.log('save button clicked');
		    row = table.row({selected: true}).data();
		    row.consentDocumentControlNumber=$('#dcn').val();
		    
		    if ($('input[name=decision]:checked').val() == 'Granted')
		    {	
		    	row.consentDecisionTypeID=1;
		    }

		    if ($('input[name=decision]:checked').val() == 'Denied')
		    {	
		    	row.consentDecisionTypeID=2;
		    }
		    
		    //console.log(row);
		    submitConsentDecision(row);
		});
	});

clearDecisionRecordFields = function() {
    $('#dcn').val('');
    $('input[name=decision]:checked').prop('checked', false);
    $("#selected-patient-name").text('');
}

updateUIState = function() {
    rowSelected = (table.rows({selected: true}).count() > 0);
    dcnValue = $('#dcn').val();
    decisionValue = $('input[name=decision]:checked').val();
    val = (rowSelected && dcnValue != '' && decisionValue != null);
    $("#consent-save-button").prop('disabled', !val);
    $("#status-label").html(table.rows().count() + " current inmates pending.");
}

refreshDataViaAjax = function(demodataOK) {
    $('body').addClass("loading");
	$.ajax(
			{
			    url : "/ojb-web-consent-management-service/cm-api/findPendingInmates",
			    headers : {
				"demodata-ok" : demodataOK
			    }
			}).done(function(data) {
		    var newRows = [];
		    var newConsentIds = [];
		    for (i = 0; i < data.length; i++) {
			newConsentIds.push(data[i].consentId);
		    }
		    var oldConsentIds = [];
		    var removeIndex = -1;
		    table.rows().every(function(rowIdx, tableLoop, rowLoop) {
			var rowData = this.data();
			if ($.inArray(rowData.consentId, newConsentIds) == -1) {
			    removeIndex = rowIdx;
			} else {
			    oldConsentIds.push(rowData.consentId);
			}
		    });
		    if (removeIndex != -1) {
			table.row(removeIndex).remove();
			// console.log("Removed row at index " + removeIndex);
		    }
		    for (i = 0; i < data.length; i++) {
			if ($.inArray(data[i].consentId, oldConsentIds) == -1) {
			    newRows.push(data[i]);
			}
		    }
		    // console.log("Added " + newRows.length + " new rows")
		    table.rows.add(newRows);
		    table.draw(false);
		    updateUIState();
		    $('body').removeClass("loading");
		});
}

submitConsentDecisionViaAjax = function(consentDecisionJson, demodataOK) {
    $('body').addClass("loading");
    $.ajax({
	url : "/ojb-web-consent-management-service/cm-api/recordConsentDecision",
	type: "POST",
	data: JSON.stringify(consentDecisionJson),
	contentType: "application/json",
	dataType: "json",
	processData: false,
	headers : {
	    "demodata-ok" : demodataOK
	}
    }).done(function(response) {
	refreshDataViaAjax(demodataOK);
	clearDecisionRecordFields();
        updateUIState();
        $('body').removeClass("loading");
    });
}
