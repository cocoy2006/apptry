<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML>
<head>
	<title>Apptry</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	
	<script type="text/javascript" src="<%=basePath%>resources/js/jquery.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/js/md5.js"></script>
	
	<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/bootstrap-3.0.3/css/bootstrap-theme.min.css">
		<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/bootstrap-3.0.3/css/bootstrap.min.css">
	<script type="text/javascript" src="<%=basePath%>resources/bootstrap-3.0.3/js/bootstrap.min.js"></script>
		
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
		$("#l_u").focus();
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
	<div class="page-header">
		<h1><a href="<%=basePath%>home">Apptry</a></h1>
	</div>
	<div class="io-container">			
		<div id="loginTips" class="alert alert-danger" style="display: none;"></div>
		<div id="loginForm" class="form-horizontal">
			<div class="form-group">
				<label class="col-sm-2 control-label" for="l_u">
					* 用户名
				</label>
				<div class="col-sm-10">
					<input class="form-control" type="text" id="l_u" name="l_u" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label" for="l_p">
					* 密码
				</label>
				<div class="col-sm-10">
					<input class="form-control" type=password id="l_p" name="l_p" />
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button class="defaultButton btn btn-success btn-lg" onclick="login()">
						<i class="glyphicon glyphicon-user"></i>&nbsp;&nbsp;快速登录
					</button>
					<a href="<%=basePath%>pwdreset" class="btn-lg pull-right">忘记密码？</a>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<a class="btn btn-default btn-lg btn-block" href="<%=basePath%>signup">还没有Apptry账号？快速注册</a>
				</div>
			</div>
		</div>
	</div>
</body>