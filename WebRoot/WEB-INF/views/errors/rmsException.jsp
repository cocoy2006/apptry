
<%@ page language="java" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
	<head>
		<title>Apptry异常</title>
	</head>

	<body>
		<% Exception ex = (Exception)request.getAttribute("exception"); %>
		<h2>Exception:<%=ex.getMessage() %></h2>
		<p/>
		<% ex.printStackTrace(new java.io.PrintWriter(out)); %>
	</body>
</html>