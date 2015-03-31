<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>OJBC Web Utilities</title>
    </head>
    <body>
        <h1>OJBC Web Utilities page</h1>
        <table>
        <tr><td><a href="saml.jsp?mode=header">View HTTP Request headers and the SAML Assertion</a></td></tr>
        <tr><td><a href="saml.jsp?mode=variable">View HTTP Request headers and the SAML Assertion (retrieved via Apache Environment Variable)</a></td></tr>
        <tr><td><a href="headers.jsp">View HTTP Request headers only</a></td></tr>
        <tr><td><a href="variables.jsp">See value of Shib Apache Environment Variable</a></td></tr>
        </table>
    </body>
</html>
