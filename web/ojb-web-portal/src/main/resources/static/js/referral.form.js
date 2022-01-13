/**
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
$(function(){
  $(".chosen-select").chosen(); 

  $("#referralForm").on("click", "input[name=targetOwnerType]", function(){
      if ($('#targetOwnerType1').prop('checked')  || $('#targetOwnerType2').prop('checked')){
          $('#submitReferral').prop('disabled', false); 
          $('#targetOri').val(''); 
          $('#targetOri').prop('disabled', true).trigger("chosen:updated");
      }
      else{
          if ($('#targetOri').val() === ''){  
        	  $('#submitReferral').prop('disabled', true);  
          }
          
          $('#targetOri').prop('disabled', false).trigger("chosen:updated");; 
      }
  });

  $("#referralForm").on("change", "#targetOri", function(){
      if ($('#targetOri').val() != ''){
          $('#submitReferral').prop('disabled', false); 
          $('input[name = targetOwnerType]').val('checked', false); 
          $('#targetOwnerType1').prop('disabled', true); 
          $('#targetOwnerType2').prop('disabled', true); 
      }
      else{
          if( !$('#targetOwnerType1').prop('checked')  && ! $('#targetOwnerType2').prop('checked')){  
        	  $('#submitReferral').prop('disabled', true);  
          }
          
          $('#targetOwnerType1').prop('disabled', false); 
          $('#targetOwnerType2').prop('disabled', false); 
      }
  });

  $('#referralForm input').keypress(function (e) {
      if (e.which == 13) {
        $('#submitReferral').click();
        return false;    
      }
  });
});
