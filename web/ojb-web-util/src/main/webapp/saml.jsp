<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*" %>
<%@ page import="org.ojbc.web.*" %>
<%
 /* This is a simple servlet that will display the incoming HTTP headers as well as the SAML assertion.  
 */
%>
 
<html>
	
	<head>
		<title>HTTP Request Headers Example</title>
	</head>
	
	<body>
		<h1>HTTP Request Headers Received</h1>
		<table>
	
			<%
			for (Enumeration<String> e = request.getHeaderNames(); e.hasMoreElements();) {
				String name = e.nextElement();
				String value = request.getHeader(name);
			%>
				<tr>
					<td><%= name %></td>
					<td><%= value %></td>
				</tr>
			<%
			}
			%>
		</table>
		
		<hr/>
		
		<%
		
			String mode = request.getParameter(ShibbolethSamlAssertionRetriever.MODE_KEY);
			if (mode == null)
			{
			    mode = ShibbolethSamlAssertionRetriever.DEFAULT_MODE;
			}
		
		%>
		
		<h1>SAML Assertion Extracted from <%= ShibbolethSamlAssertionRetriever.HEADER_MODE.equals(mode) ? "HTTP Header" : "Apache Environment Variable" %></h1>
		
		<pre><%= ShibbolethSamlAssertionRetriever.retrieveAssertion(request) %></pre> 

	</body>
	
</html>