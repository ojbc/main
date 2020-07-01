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
jQuery(function() {
   jQuery.support.placeholder = false;
   test = document.createElement('input');
   if('placeholder' in test) jQuery.support.placeholder = true;
});

$(function() {
	xhr = null;
	$.ajaxSetup ({
	      // Disable caching of AJAX responses on IE.
	      cache: false
	});
	
   ojbc.handlePlaceholders(); 
   ojbc.maskInputs();
   ojbc.handleEsc();
   $( document ).ajaxStop(function() {
	   
	   //Remove the load spinner on popup window detail retrieving
	   if ($("#modalIframeSpinner").length){
		   $("#modalIframeSpinner").hide();
	   }
   });
   
   $('#portalContent').unbind('shown.bs.modal', '#detailModal').on('shown.bs.modal', '#detailModal', function (){
		$("#modalIframeSpinner").height($(".modal-body").height()-32);
		$("#modalIframeSpinner").width($(this).find('.modal-content').width()-32);
		$("#modalIframeSpinner").show();
		$('body').addClass('modal-open'); 
   }); 

	$('#portalContent').unbind('hidden.bs.modal', '#detailModal').on('hidden.bs.modal', '#detailModal', function(){
		$('#modalIframe').attr('src', 'about:blank');
		$("#modalIframeSpinner").height(123);
		$("#modalIframe").height(123);
	});    	    	

	$('#portalContent').unbind('click', '.searchResultsTable tbody tr').on('click', '.searchResultsTable tbody tr',function() {
		$(this).siblings().removeClass("selected");
	    $(this).addClass("selected");
	});
	
  	$("#navbarCollapse").on('show.bs.collapse', function() {
  	    $('a.nav-link:not(.dropdown-toggle)').click(function() {
  	        $("#navbarCollapse").collapse('hide');
  	    });
  	    $('a.dropdown-item').click(function() {
  	    	$("#navbarCollapse").collapse('hide');
  	    });
  	});
   
   /*
    * Assuming the name is in the format of "firstName lastName"
    */
   function getLastName(name) {
       return name.slice(name.lastIndexOf(' ') + 1);;
   }

   jQuery.fn.dataTableExt.oSort['name-asc'] = function (a, b) {
       var x = getLastName(a);
       var y = getLastName(b);
       return ((x < y) ? -1 : ((x > y) ? 1 : 0));
   };

   jQuery.fn.dataTableExt.oSort['name-desc'] = function (a, b) {
       var x = getLastName(a);
       var y = getLastName(b);
       return ((x < y) ? 1 : ((x > y) ? -1 : 0));
   };
});

ojbc = {
	hideSearchBarButtons: function(){
		var searchBarIsHidden = $('#searchBar').is(':hidden');
		// hide the search bar so we can just show the subscriptions content	
		if(!searchBarIsHidden){
			// note can also use:  $('#id').hide();			
			$('#searchBarButtonDiv').css("display", "none");
		}	  

	},
	showLoadingPane: function(){
 		var loadingDiv =  $("#loadingAjaxPane");
 		var mainContentDiv = $("#mainContent");
		
 		loadingDiv.height(mainContentDiv.height());
 		loadingDiv.width(mainContentDiv.width());
 		
      	$("#loadingAjaxPane").show();          	
	},

	handleEsc:function(){
	   $("body").on("keyup", function(event){
	  	  if ( event.keyCode == 27 ) {
	  		  event.preventDefault();
	  		  event.stopPropagation();
		  	  if(xhr && xhr.readyState != 4){
	  	  		  xhr.textStatus="aborted"; 
				  xhr.abort();
		  	  }
		  	  else{
		  		document.getElementsByTagName('iframe')[0].src = "about:blank";
		  	  }
		  }
	   }); 
	},
	
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
	   $( "body" ).on( "focus", ".shortDigit", function() {
		   $(this).mask("9?99");
	   });
	},
	
   /**
    * Deal with the browsers that does not support placeholder. 
    */	
	handlePlaceholders: function(){
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
	      $('form').submit(function () {
	         $(':text.hasPlaceholder').val('');
	      });
	   }
	},
	
	clearPlaceholders: function(){
		   if(!$.support.placeholder) { 
		         $(':text.hasPlaceholder').val('');
		   }
		},
		
   /**
    * Deal with the browsers that does not support placeholder. --END 
    */	
	collectAllCheckBoxValues : function () {         
	    var allVals = "";
	    $('#sourceSystemsCheckBoxes :checked').each(function() {
	      allVals += "," + $(this).val();
	    });
	    
	    return allVals.substring(1);
	 },
	 
	displayFailMessage : function(jqXHR, textStatus, errorThrown) {
    	if (jqXHR.status == 500) {
    		bootpopup.alert(jqXHR.responseText, 'UNABLE TO PROCESS REQUEST');
    	} else if (jqXHR.status == 0) {
    		// Likely that the SAML assertion timed out -- force reload of the page
    		// Do not reload the if the user aborted the ajax request
//    		alert(jqXHR.status);
//    		alert("jqXHR.textStatus: " + jqXHR.textStatus);
    		if (jqXHR.textStatus != "aborted"){
    			window.location.reload();
    		}
    	} else {
    		bootpopup.alert('Unable to talk to Server.  Please try again later.', 'ERROR');
    	}
	},
	
	displayIncidentDetailFailMessage : function(jqXHR, textStatus, errorThrown) {
		if (jqXHR.status == 0) {
    		// Likely that the SAML assertion timed out -- force reload of the iframe's parent page
    		if (jqXHR.textStatus != "aborted"){
    			parent.location.reload();
    		}
    		else{
    			$("#loadingAjaxPane").hide();                
    			$("#modalIframeSpinner").hide();
    		}
    	} else {
    		$('#incidentDetailTabsHolder').html('<table class="detailsTable"><tr><td class="detailsTitle">UNABLE TO VIEW INCIDENT DETAILS</td></tr><tr><td><span class="error">A server-side error occurred. Please re-select the item to try again. If problem persists, contact your IT department.</span></td></tr></table>');
    	}
	},
	
	displayCustodyDetailFailMessage : function(jqXHR, textStatus, errorThrown) {
		if (jqXHR.status == 0) {
    		// Likely that the SAML assertion timed out -- force reload of the iframe's parent page
    		if (jqXHR.textStatus != "aborted"){
    			parent.location.reload();
    		}
    		else{
    			$("#modalIframeSpinner").hide();
    		}
    	} else {
    		$('#custodyDetailDataHolder').html('<table class="detailsTable"><tr><td class="detailsTitle">UNABLE TO VIEW CUSTODY DETAILS</td></tr><tr><td><span class="error">A server-side error occurred. Please re-select the item to try again. If problem persists, contact your IT department.</span></td></tr></table>');
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
	},
	
	showDetailModal : function(url, heightIncrement){
		heightIncrement = heightIncrement || 0;
		ojbc.clearErrorMessage();
        $('#modalIframe').attr('src', url);	
      	$('#detailModal').modal('show');
 		$("#modalIframe").load( function() {
 			var iframeHeight = $('iframe').contents().height() + heightIncrement; 
 			if (iframeHeight > 123){
 				$(this).css('height', iframeHeight);
 			}
			$("#modalIframeSpinner").hide();				
		});
	},
	
	clearErrorMessage : function(){
		if ( $( "#informationMessages" ).length ) {
		    $( "#informationMessages" ).html('');
		}
	},
	
	getCheckedValues: function() {
		
		var checkedValues = $('input:checkbox:checked').map(function() {
			//console.log("value:" + this.value);
		    return this.value;
		}).get();
		
		if (!String(checkedValues)) {
			return null; 
		}
		else {
			return	'[' + checkedValues + ']';
		}	  		
	}


}
