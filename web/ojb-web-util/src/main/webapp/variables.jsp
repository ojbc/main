<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.*"%>
<%@page import="java.io.*"%>
<%@page import="java.net.*"%>
<%@page import="javax.net.ssl.*"%>
<%@page import="javax.net.ssl.HttpsURLConnection"%>
<%
 /* This is a simple servlet that will display the value for the Shib-Assertion-01 Apache environment variable
 */
%>
 
<html>
<head>
<title>HTTP Request Headers Example</title>
</head>
<body>
<h1>HTTP Request Headers Received</h1>
<table>

	<tr>
		<td><%= ((String)request.getAttribute("Shib-Assertion-01")) %></td>
	</tr>
</table>
<hr />
</body>
</html>