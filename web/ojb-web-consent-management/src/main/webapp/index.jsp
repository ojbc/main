<!--

    Unless explicitly acquired and licensed from Licensor under another license, the contents of
    this file are subject to the Reciprocal Public License ("RPL") Version 1.5, or subsequent
    versions as allowed by the RPL, and You may not copy or use this file in either source code
    or executable form, except in compliance with the terms and conditions of the RPL
    All software distributed under the RPL is provided strictly on an "AS IS" basis, WITHOUT
    WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY DISCLAIMS ALL SUCH
    WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
    PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific language
    governing rights and limitations under the RPL.
    
    http://opensource.org/licenses/RPL-1.5
    
    Copyright 2012-2017 Open Justice Broker Consortium
    
-->
<html lang="en">

<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Consent Management Form</title>
<link href="static/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<link href="static/DataTables/DataTables-1.10.15/css/dataTables.bootstrap.css" rel="stylesheet">
<link href="static/DataTables/Select-1.2.2/css/select.bootstrap.css" rel="stylesheet">
<link href="static/DataTables/Buttons-1.3.1/css/buttons.dataTables.min.css" rel="stylesheet">
<link href="static/ConsentManagementForm.css" rel="stylesheet">
</head>

<body>

	<div class="container-fluid" id="main-container">

		<h3>Consent Management Form</h3>
		
		<div class="row">

			<div class="col-xs-9 tablediv">
				<table id="inmate-table" class="table"></table>
			</div>
			
			<div class="col-xs-3" id="form-container">
			
				<h4>Consent Decision:</h4>
				
				<p>Patient: <span id="selected-patient-name"></span></p>
			
				<form>
  					<div class="form-group">
    					<label for="dcn">Document Control #:</label>
    					<input type="text" class="form-control" id="dcn">
  					</div>
					<div class="form-group">
						<label for="decision">Decision:</label>
						<div class="radio">
							<label> <input type="radio" name="decision" id="decisionGranted" value="Granted">Granted</label>
						</div>
						<div class="radio">
							<label> <input type="radio" name="decision" id="decisionDenied" value="Denied">Denied</label>
						</div>
					</div>
					<button type="button" class="btn btn-default" id="consent-save-button">Save</button>
				</form>
				
			</div>

		</div>
		
	</div>


	<!--/start-context-script/--><script src="static/context-local-demo.js"></script><!--/end-context-script/-->
	<script src="static/jquery/jquery-3.2.1.min.js"></script>
	<script src="static/bootstrap/js/bootstrap.min.js"></script>
	<script src="static/DataTables/DataTables-1.10.15/js/jquery.dataTables.js"></script>
	<script src="static/DataTables/DataTables-1.10.15/js/dataTables.bootstrap.js"></script>
	<script src="static/DataTables/Select-1.2.2/js/dataTables.select.js"></script>
	<script src="static/DataTables/Buttons-1.3.1/js/dataTables.buttons.min.js"></script>
	<script src="static/ConsentManagementForm.js"></script>
	
</body>

</html>