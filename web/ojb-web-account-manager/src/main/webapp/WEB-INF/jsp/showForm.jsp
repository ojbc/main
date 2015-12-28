<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Update Account Password</title>
	<style>
	.error {
		color: #ff0000;
		font-style: italic;
		font-weight: bold;
	}
	.warning {
		background-color: yellow;
	}
	</style>
</head>
<body>
	<div id="mainDiv">
		<h3>change password</h3>
		<c:url var="actionUrl" value="/updatePassword.do"/>
		<form:form modelAttribute="changePasswordCommand" action="${actionUrl}" method="Post">
			<form:errors cssClass="error" />
		    <table>
		        <tr>
		            <th>User ID:</th>
		            <td><form:input path="userId" cssErrorClass="warning"/></td>
		            <td><form:errors path="userId" cssClass="error"/></td>
		        </tr>
		        <tr>
		            <th>Current Password:</th>
		            <td><form:password path="oldPassword" cssErrorClass="warning"/></td>
		            <td><form:errors path="oldPassword" cssClass="error"/></td>
		        </tr>
		        <tr>
		            <th>New Password:</th>
		            <td><form:password path="newPassword" cssErrorClass="warning"/></td>
		            <td><form:errors path="newPassword" cssClass="error" /></td>
		        </tr>
		        <tr>
		            <th>Confirm New Password:</th>
		            <td><form:password path="confirmNewPassword" cssErrorClass="warning"/></td>
		            <td>
		            	<form:errors path="matchingPassword" cssClass="error"/>
		            	<form:errors path="confirmNewPassword" cssClass="error"/>
		            </td>
		        </tr>
		        <tr>
		            <td colspan="2">
		                <input type="submit" value="Submit"/>
		                <a href="<c:url value='/showForm.do'/>">Cancel</a>
		            </td>
		        </tr>
		    </table>
		</form:form>
	</div>
</body>
</html>
