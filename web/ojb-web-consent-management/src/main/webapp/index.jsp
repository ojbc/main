<html lang="en">

<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Consent Management Form</title>
<link href="static/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<link href="static/DataTables/DataTables-1.10.15/css/dataTables.bootstrap.css" rel="stylesheet">
<link href="static/DataTables/Select-1.2.2/css/select.bootstrap.css" rel="stylesheet">
<link href="static/ConsentManagementForm.css" rel="stylesheet">
</head>

<body>

	<div class="container-fluid" id="main-container">

		<h3>Consent Management Form</h3>
		
		<div class="row">

			<div class="col-xs-9 tablediv">
				<table id="example" class="table" cellspacing="0" width="100%"></table>
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
					<button type="submit" class="btn btn-default">Save</button>
				</form>
				
			</div>

		</div>

	</div>


	<script src="static/jquery/jquery-3.2.1.min.js"></script>
	<script src="static/bootstrap/js/bootstrap.min.js"></script>
	<script src="static/DataTables/DataTables-1.10.15/js/jquery.dataTables.js"></script>
	<script src="static/DataTables/DataTables-1.10.15/js/dataTables.bootstrap.js"></script>
	<script src="static/DataTables/Select-1.2.2/js/dataTables.select.js"></script>
	<script src="static/testdata.js"></script>
	<script src="static/ConsentManagementForm.js"></script>
	
</body>

</html>