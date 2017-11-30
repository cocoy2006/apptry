<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>Apptry</title>
		<!-- Always force latest IE rendering engine (even in intranet) & Chrome Frame
                Remove this if you use the .htaccess -->
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<!-- Apple iOS Safari settings -->
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
		<meta name="apple-mobile-web-app-capable" content="yes" />
		<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
		
		<link rel="stylesheet" type="text/css" href="resources/css/apptry.css">
		<style type="text/css">
			body {background: url('resources/image/320_480.png'); margin: 0px;}
			.message-error {color: red;}
		</style>
	</head>

	<body>
		<div class="tap_container">
			<c:if test="${sessionScope.MESSAGE == null}">
				<div onclick="onTap()" class="tap">
					立即体验
				</div>
			</c:if>
			<c:if test="${sessionScope.MESSAGE != null}">
				<div class="message-error tap" onclick="onTap()">${sessionScope.MESSAGE}</div>
				<c:remove var="MESSAGE" scope="session" />
			</c:if>
		</div>
		<script>
			function onTap() {
				// 获取app信息
				var applicationId = self.location.hash.substr(1);
				// 返回location
				window.location = applicationId;
			}
		</script>

	</body>
</html>
