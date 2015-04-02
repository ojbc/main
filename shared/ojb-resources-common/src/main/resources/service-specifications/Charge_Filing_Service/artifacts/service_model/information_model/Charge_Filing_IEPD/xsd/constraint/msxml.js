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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
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

