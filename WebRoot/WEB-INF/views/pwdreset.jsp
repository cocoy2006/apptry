<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	Object obj = session.getAttribute("developer");
	if(obj != null) {
		response.sendRedirect("/" + request.getRequestURI().split("/")[1] + "/user");
		return;
	}
%>
<!DOCTYPE HTML>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>Apptry</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		
		<script type="text/javascript" src="<%=basePath%>resources/js/jquery.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/js/jquery.validate.min.js"></script>
		<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/bootstrap-3.0.3/css/bootstrap-theme.min.css">
		<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/bootstrap-3.0.3/css/bootstrap.min.css">
		<script type="text/javascript" src="<%=basePath%>resources/bootstrap-3.0.3/js/bootstrap.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/js/user.js"></script>
		<style type="text/css">
			body {background-color: #F5F5F5;}
			h1 {text-align: center;}
			.io-container {
				background-color:#ffffff; margin:0 auto; padding:45px; width: 630px;
				border: 1px solid #ccd0d6;
				-webkit-border-radius: 4px;
				-moz-border-radius: 4px;
				-ms-border-radius: 4px;
				-o-border-radius: 4px;
				border-radius: 4px;
			}
		</style>
		<script type="text/javascript">
		<!--
		$(document).ready(function() {
			pwdresetTipsBind();
			pwdresetValidation();
			$("#username").focus();
		});
		$(document).bind("keydown", function(event) {
			var button = $(".defaultButton");
	        if(event.keyCode == 13) {
	                button.click();
	                event.returnValue = false;
	        }
		});
		//-->
		</script>
	</head>

	<body>
		<div class="container">
			<c:if test="${sessionScope.MESSAGE != null}">
				<div class="alert alert-danger text-center">
					<label>${sessionScope.MESSAGE}</label>
				</div>
				<c:remove var="MESSAGE" scope="session" />
			</c:if>
			<div class="page-header">
				<h1>
					<a href="<%=basePath%>home">Apptry</a>
				</h1>
			</div>
		</div>
		<div class="io-container">			
			<div id="pwdresetTips" class="alert alert-danger" style="display: none;"></div>
			<form id="pwdresetForm" class="form-horizontal">
				<div class="form-group">
					<label class="col-sm-2 control-label" for="username">
						* 用户名
					</label>
					<div class="col-sm-5">
						<input class="form-control" type="text" id="username" name="username" />
					</div>
					<div class="col-sm-5">
						<label id="username_tips"></label>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label" for="email">
						* 邮箱
					</label>
					<div class="col-sm-5">
						<input class="form-control" type="text" id="email" name="email" />
					</div>
					<div class="col-sm-5">
						<label id="email_tips"></label>
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
						<button class="defaultButton btn btn-success btn-lg" onclick="pwdreset()">
							<i class="glyphicon glyphicon-user"></i>&nbsp;&nbsp;密码重置
						</button>
						<a href="<%=basePath%>login" class="btn-lg pull-right">返回登录</a>
					</div>
				</div>
			</form>
		</div>
	</body>
</html>
