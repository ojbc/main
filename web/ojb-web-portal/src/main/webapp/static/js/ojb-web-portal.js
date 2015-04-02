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

   ojbc.maskInputs(); 
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

	maskInputs: function(){
	   $( "#portalContent" ).on( "focus", ".mdate", function() {
		   $( this ).mask("99/99/9999");
	   });
	   $( "#portalContent" ).on( "focus", ".ssn", function() {
		   $(this).mask("999-99-9999");
	   });
	   $( "#portalContent" ).on( "focus", ".zipCode", function() {
		   $(this).mask("99999?-9999");
	   });
	   $( "#portalContent" ).on( "focus", ".year", function() {
		   $(this).mask("9999");
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
