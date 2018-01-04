//Arguments:  input-file style-file output-file replace?
//Set replace to "yes" to overwrite existing files
var out = WScript.CreateObject("Scripting.FileSystemObject"); //output
var unicode = false;
var replace = false;
if (WScript.Arguments(3)=="yes")
	replace = true;
if (out.FileExists(WScript.Arguments(0))) {
	var xml  = WScript.CreateObject("Microsoft.XMLDOM");          //input
	xml.validateOnParse=false;
	xml.load(WScript.Arguments(0));
	var xsl  = WScript.CreateObject("Microsoft.XMLDOM");          //style
	xsl.validateOnParse=false;
	xsl.load(WScript.Arguments(1));
	var hdl = out.CreateTextFile( WScript.Arguments(2), replace, unicode )
	hdl.write( xml.transformNode( xsl.documentElement ));
}

