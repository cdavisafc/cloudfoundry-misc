<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@page import="com.pivotalinitiative.solutions.cloudfoundry.*" %>
<%@page import="java.io.IOException" %>
<%@page import="java.io.IOException" %>

<%!String getProvisionedServiceName(ServletContext application) {
		return application.getInitParameter("provisioned_service_name");
	}

String statusMsg = "Echo service ready to be used!";
String clientMsg = null;
String echoMsg = null;
String svcj = null;
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		String services_json = System.getenv("VCAP_SERVICES");
                svcj = services_json;
		EchoServiceCreds creds = null;
		try {
			creds = JsonParserUtil.getServiceInstance(services_json, 
					getProvisionedServiceName(application));
		} catch (ServiceParserException e) {
			statusMsg = e.getMessage();	
		}
		
		clientMsg = request.getParameter("echo_message_area");
		if (creds != null && clientMsg != null) {
			EchoClient client = new EchoClient(creds.getPort(),
					creds.getHost());
			try {
				echoMsg = client.sendMessage(clientMsg);
			} catch (IOException e) {
				statusMsg = e.getMessage();
			}
			client.destroyConnection();
		}
	%>
	
			<div align="center">
				<h3>Echo client application</h3>
				<br> <br>
				<form method="post" action="index.jsp">
					<input type="text" name="echo_message_area" width="100px"/>
					<br> <input type="submit" value="Echo message"/>
				</form>
				<br> <hr>
				<h3>
					<%= (echoMsg != null)? "Echo from service: " + echoMsg : statusMsg %>
				</h3>
<h3>
<%= (svcj != null)? "The services description: " + svcj : "Services description: some problem" %>
</h3>
			</div>
		
</body>
</html>
