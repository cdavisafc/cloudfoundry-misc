<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@page import="org.pvtl.cassandra.*" %>
<%@page import="java.io.IOException" %>

<%!String getProvisionedServiceName(ServletContext application) {
		return application.getInitParameter("provisioned_service_name");
	}
%>
<%
String svcj = null;
String statusMsg = null;

Long rowId = null;
String detailsRetrieved = null;

String services_json = System.getenv("VCAP_SERVICES");
svcj = services_json;
ServiceCreds creds = null;
try {
	creds = JsonParserUtil.getServiceInstance(services_json,
			getProvisionedServiceName(application));
} catch (ServiceParserException e) {
	statusMsg = e.getMessage();	
}

HectorSample hector;
if (creds == null)
	 hector = new HectorSample("10.13.219.151:9160");
else
	 hector = new HectorSample(creds.getHost()+":"+creds.getPort());
String cassandraNodeIP = hector.getConnectionString();
String keyspaceName = hector.getKeyspaceName();
String columnFamilyName = hector.getColumnFamilyName();

if (request.getParameter("submit") != null) {
	String first = request.getParameter("first_name");
	String last = request.getParameter("last_name");
	String city = request.getParameter("city");
	if (first != null)
		rowId = hector.insertData(columnFamilyName, first, last, city);
}

if (request.getParameter("retrieve") != null) {
	Long row_id = Long.parseLong(request.getParameter("row_id"));
	detailsRetrieved = hector.getDetails(columnFamilyName, row_id);
}


%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Cassandra Test App</title>
</head>
<body>
<h2>Hello World!!!3</h2>


			<div align="center">
				<h3>Cassandra Test App</h3>
			</div>
				<br> <br>

			<%= (cassandraNodeIP != null)? "Connected to Cassandra server: " + cassandraNodeIP : "Cannot connect to cassandra server" %>
			<%= "Accessing keyspace " + keyspaceName + ", column family " + columnFamilyName %>

			
			<br><br>
			<div>
				<hr>
				<h3>Writing to Cassandara</h3>
				<%= (rowId != null)? "Wrote values to Cassandra with rowId " + rowId : "" %>
				<form method="post" action="index.jsp">
					<label>First Name: </label>
					<input type="text" name="first_name" width="100px"/> <br>
					<label>Last Name: </label>
					<input type="text" name="last_name" width="100px"/> <br>
					<label>City: </label>
					<input type="text" name="city" width="100px"/> <br>
					<input type="submit" name="submit" value="submit"/>
				</form>
				<br>
				<hr>
				<h3>Reading from Cassandara</h3>
				<form method="post" action="index.jsp">
					<label>Row Id: </label>
					<input type="text" name="row_id" width="100px"/> <br>
					<input type="submit" name="retrieve" value="retrieve"/>
				</form>
				<%= (detailsRetrieved != null) ? detailsRetrieved : "" %>
				<hr>
				
				<h3>
				<%= (svcj != null)? "The services description: " + svcj : "For some reason there is no services description" %>
				</h3>
				<h3>
				<%= (creds != null)? "The creds: " + creds : "For some reason creds is null" + statusMsg %>
				</h3>
				
			</div>

</body>
</html>
