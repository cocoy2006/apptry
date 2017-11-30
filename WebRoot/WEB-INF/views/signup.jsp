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
	<script type="text/javascript" src="<%=basePath%>resources/js/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/js/md5.js"></script>
	
	<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/bootstrap-3.0.3/css/bootstrap-theme.min.css">
		<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/bootstrap-3.0.3/css/bootstrap.min.css">
	<script type="text/javascript" src="<%=basePath%>resources/bootstrap-3.0.3/js/bootstrap.min.js"></script>
		
	<script type="text/javascript" src="<%=basePath%>resources/js/user.js"></script>
	<script type="text/javascript">
	<!--
	$(document).ready(function() {
		registerTipsBind();
		registerValidation();
		$("#r_u").focus();
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
	<style type="text/css">
		body {background-color: #F5F5F5;}
		h1 {text-align: center;}
		.io-container {
			background-color:#ffffff; margin:0 auto; padding:45px; width: 660px;
			border: 1px solid #ccd0d6;
			-webkit-border-radius: 4px;
			-moz-border-radius: 4px;
			-ms-border-radius: 4px;
			-o-border-radius: 4px;
			border-radius: 4px;
		}
	</style>
</head>
<body>
	<div class="page-header">
		<h1><a href="<%=basePath%>home">Apptry</a></h1>
	</div>
	<div class="io-container">			
		<div id="registerTips" class="alert alert-danger" style="display: none;"></div>
		<form id="registerForm" class="form-horizontal">
			<div class="form-group">
				<label class="col-sm-2 control-label" for="r_u">
					* 用户名
				</label>
				<div class="col-sm-5">
					<input class="form-control" type="text" id="r_u" name="r_u" />
				</div>
				<div class="col-sm-5">
					<label id="r_u_tips"></label>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label" for="r_p">
					* 密码
				</label>
				<div class="col-sm-5">
					<input class="form-control" type=password id="r_p" name="r_p" />
				</div>
				<div class="col-sm-5">
					<label id="r_p_tips"></label>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label" for="r_p_ag">
					* 确认密码
				</label>
				<div class="col-sm-5">
					<input class="form-control" type="password" id="r_p_ag" name="r_p_ag" />
				</div>
				<div class="col-sm-5">
					<label id="r_p_ag_tips"></label>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label" for="r_email">
					* 注册邮箱
				</label>
				<div class="col-sm-5">
					<input class="form-control" type="text" id="r_email" name="r_email" />
				</div>
				<div class="col-sm-5">
					<label id="r_email_tips"></label>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button id="registerBtn" class="defaultButton btn btn-success btn-lg" onclick="register()">
						<i class="glyphicon glyphicon-pencil"></i>&nbsp;&nbsp;快速注册
					</button>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<div class="checkbox">
						<label>
							<input type="checkbox">我已阅读并同意&lt;&lt;新复在线服务协议&gt;&gt;
						</label>
					</div>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<a class="btn btn-default btn-lg btn-block" href="<%=basePath%>login">已经拥有Apptry账号？直接登录</a>
				</div>
			</div>
		</form>
	</div>
</body>