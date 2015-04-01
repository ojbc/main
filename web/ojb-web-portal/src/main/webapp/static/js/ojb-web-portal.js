jQuery(function() {
   jQuery.support.placeholder = false;
   test = document.createElement('input');
   if('placeholder' in test) jQuery.support.placeholder = true;
});

$(function() {
	$.ajaxSetup ({
	      // Disable caching of AJAX responses on IE.
	      cache: false
	});
	
   /*
    * Deal with the browsers that does not support placeholder. 
    */	
   if(!$.support.placeholder) { 
      var active = document.activeElement;
      $(':text').focus(function () {
         if ( $(this).attr('placeholder') && $(this).val() == $(this).attr('placeholder')) {
            $(this).val('').removeClass('hasPlaceholder');
         }
      }).blur(function () {
         if ($(this).attr('placeholder') && ($(this).val() == '' || $(this).val() == $(this).attr('placeholder'))) {
            $(this).val($(this).attr('placeholder')).addClass('hasPlaceholder');
         }
      });
      $(':text').blur();
      $(active).focus();
      $('form:eq(0)').submit(function () {
         $(':text.hasPlaceholder').val('');
      });
   }

});

ojbc = {
	clearDefaultValue : function (cssSelector,defaultValue){
	     if($(cssSelector).val() == defaultValue){
	          $(cssSelector).val('');
	     }
	},
	  
	initializeInputWithDefaultValue : function (cssSelector,defaultValue){
	   if($(cssSelector).val() == ""){
	     $(cssSelector).val(defaultValue);
	   }
	   $(cssSelector).focus(
	      function(){
	          if($(cssSelector).val() == defaultValue){
	              $(this).val('');
	          }
	      });
	  $(cssSelector).focusout(
	      function(){
	         if($(cssSelector).val() == ""){
	              $(cssSelector).val(defaultValue);
	          }
	      });
	},
	
	collectAllCheckBoxValues : function () {         
	    var allVals = "";
	    $('#sourceSystemsCheckBoxes :checked').each(function() {
	      allVals += "," + $(this).val();
	    });
	    
	    return allVals.substring(1);
	 },
	 
	displayFailMessage : function(jqXHR, textStatus, errorThrown) {
    	if (jqXHR.status == 500) {
			var exceptionPart = "<b>exception</b>";
	    	var excStartIndex = jqXHR.responseText.indexOf(exceptionPart) + exceptionPart.length;
	    	var excEndIndex = jqXHR.responseText.indexOf('<b>root cause</b>');
	    	var errorHeader = "<span class='error'>A server-side error occurred while processing your request. Details below:</span>";
	    	$('#portalContent').html(errorHeader + jqXHR.responseText.substring(excStartIndex,excEndIndex));
    	} else if (jqXHR.status == 0) {
    		// Likely that the SAML assertion timed out -- force reload of the page
    		window.location.reload();
    	} else {
    		$('#portalContent').html("<span class='error'>Unable to talk to Server.  Please try again later.</span>");
    	}
	},
	
	displayIncidentDetailFailMessage : function(jqXHR, textStatus, errorThrown) {
		if (jqXHR.status == 0) {
    		// Likely that the SAML assertion timed out -- force reload of the iframe's parent page
			parent.location.reload();
    	} else {
    		$('#incidentDetailTabsHolder').html('<table class="detailsTable"><tr><td class="detailsTitle">UNABLE TO VIEW INCIDENT DETAILS</td></tr><tr><td><span class="error">A server-side error occurred. Please re-select the item to try again. If problem persists, contact your IT department.</span></td></tr></table>');
    	}
	},
	
	displayComingSoonMessage : function() {
		$('#portalContent').html("<span class='error'>Coming soon!</span>");
	},
	
	populatePortalContentFromUrl : function(url) {
		$.get(url, function(data) {					
		      $('#portalContent').html(data);
		    }).fail(ojbc.displayFailMessage);
		return false;
	},

   populateLeftBarContentFromUrl : function(url) {
	    $.post(url, function(data) {	    	
	    	$('#filterColumn').html(data);
	    }).fail(ojbc.displayFailMessage);
	    return false;
	},
		
	clearAllDefaultValues_incident : function () {
		ojbc.clearDefaultValue('#advanceSearch\\.incidentNumber','Incident #');
		ojbc.clearDefaultValue('#advanceSearch\\.incidentNature','Nature');
	    
		ojbc.clearDefaultValue('#advanceSearch\\.incidentDateRangeStart','From');
		ojbc.clearDefaultValue('#advanceSearch\\.incidentDateRangeEnd','To');
	},
	
	initializeAllInputWithDefaultValue_incident : function (){
		ojbc.initializeInputWithDefaultValue('#advanceSearch\\.incidentNumber','Incident #');
		ojbc.initializeInputWithDefaultValue('#advanceSearch\\.incidentNature','Nature');

		ojbc.initializeInputWithDefaultValue('#advanceSearch\\.incidentDateRangeStart','From');
		ojbc.initializeInputWithDefaultValue('#advanceSearch\\.incidentDateRangeEnd','To');
	},
	
	clearAllDefaultValues_vehicle : function () {
		ojbc.clearDefaultValue('#advanceSearch\\.vehicleModel','Model');
		ojbc.clearDefaultValue('#advanceSearch\\.vehiclePlateNumber','Plate #');
		ojbc.clearDefaultValue('#advanceSearch\\.vehicleVIN','VIN');
	    
		ojbc.clearDefaultValue('#advanceSearch\\.vehicleYearRangeStart','From');
		ojbc.clearDefaultValue('#advanceSearch\\.vehicleYearRangeEnd','To');
	},
	
	initializeAllInputWithDefaultValue_vehicle : function (){
		ojbc.initializeInputWithDefaultValue('#advanceSearch\\.vehicleModel','Model');
		ojbc.initializeInputWithDefaultValue('#advanceSearch\\.vehiclePlateNumber','Plate #');
		ojbc.initializeInputWithDefaultValue('#advanceSearch\\.vehicleVIN','VIN');
		
		ojbc.initializeInputWithDefaultValue('#advanceSearch\\.vehicleYearRangeStart','From');
		ojbc.initializeInputWithDefaultValue('#advanceSearch\\.vehicleYearRangeEnd','To');
	},
	
	clearSearchPurposeFields : function() {
		$('#purposeSelect').val('');
		$('#onBehalfOf').val('');
		ojbc.initializeInputWithDefaultValue('#onBehalfOf','On behalf of');
	},
	
	clearSearchResultsFilterFields : function() {
		$('#demogErrors').html("");  
		$('#filterAgeRangeStart').val('');	
		$('#filterAgeRangeEnd').val('');
		$('#filterHeightInFeet').val('');
		$('#filterHeightInInches').val(''); 	
		$('#filterHeightTolerance').val('');
		$('#filterWeight').val('');  	
		$('#filterWeightTolerance').val('');
		$('#filterPersonRaceCode').prop('selectedIndex',0);					
		$('#filterPersonEyeColor').prop('selectedIndex',0);					
		$('#filterPersonHairColor').prop('selectedIndex',0);
	}
	
}
