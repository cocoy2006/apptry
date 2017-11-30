<%@ page language="java" import="java.util.*, molab.main.java.entity.T_Developer" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	Object obj = session.getAttribute("developer");
	T_Developer developer = null;
	if(obj != null) {
		developer = (T_Developer) obj;
	} else {
		response.sendRedirect("/" + request.getRequestURI().split("/")[1] + "/home");
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
		<!-- <script type="text/javascript" src="<%=basePath%>resources/js/jquery.cookie.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/jquery.ui/js/jquery-ui-1.8.16.custom.min.js"></script> -->
		<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/bootstrap-3.0.3/css/bootstrap-theme.min.css">
		<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/bootstrap-3.0.3/css/bootstrap.min.css">
		<script type="text/javascript" src="<%=basePath%>resources/bootstrap-3.0.3/js/bootstrap.min.js"></script>
		<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/bootstrap-3.0.3/wizard/wizard.css">
		<script type="text/javascript" src="<%=basePath%>resources/bootstrap-3.0.3/wizard/wizard.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/js/user.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/js/pay.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/js/apps.js"></script>
		<style type="text/css">
			body {background-color: #F5F5F5;}
			h1, h2, h3 {text-align: center;}
			.overflow {cursor: pointer;
				display: inline-block;
			    margin: 0;
			    overflow: hidden;
			    text-overflow: ellipsis;
			    white-space: nowrap;
			    width: 204px;}
		</style>
		<script type="text/javascript">
			$(document).ready(function() {
				load("main", "dashboard");
			});
			function openApps() {
				var url = "<%=basePath%>user/loadApplications/<%=developer.getId() %>/";
				load("apps", url);
			}
			function openOrders() {
				var url = "<%=basePath%>order/loadAll/<%=developer.getId() %>/";
				load("orders", url);
			}
			function openUpload() {
				$(".main").load("dashboard", function() {
					$("#file").click();
				});
			}
			function openStatisticsList() {
				var url = "<%=basePath%>visit/list/<%=developer.getId() %>/";
				$(".main").load(url);
			}
			function openStatistisDetail(id) {
				var url = "<%=basePath%>visit/detail/" + id + "/";
				$(".main").load(url);
			}
			function openBuy() {
				$(".main").load("buy");
			}
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
				<h1 style="margin: 0 15px;">
					<a href="<%=basePath%>home">Apptry</a>
					<a href="<%=basePath%>user/logout" class="btn btn-warning btn-lg pull-right">退出登录</a>
				</h1>
			</div>
			<div class="col-md-4 pull-left">
				<div class="panel panel-primary">
					<div class="panel-heading"></div>
					<div class="panel-body">
						剩余点击次数&nbsp;<span class="label label-primary"><%=developer.getLeftClicks() %></span>
						<a class="btn btn-xs btn-link" href="javascript:void(0)" onclick="openStatisticsList()">统计报表</a>
						<a class="pull-right" href="javascript:void(0)" onclick="openBuy()">
							<i class="glyphicon glyphicon-shopping-cart"></i>购买
						</a>
					</div>
				</div>
				<div class="panel panel-primary">
					<div class="panel-heading">
						<b>我的APP</b>
						<small>
							点击可查看详情
						</small>
						<a class="pull-right" href="javascript:void(0)" onclick="openUpload()" style="color: #FFFFFF;">
							<i class="glyphicon glyphicon-upload"></i>上传
						</a>
					</div>
					<div class="apps"></div>
					<script type="text/javascript">openApps();</script>
				</div>
				<div class="panel panel-primary">
					<div class="panel-heading">
						<b>我的订单</b>
						<a class="pull-right" target="_blank" style="color: #FFFFFF;" 
							href="http://shop108024094.taobao.com/index.htm?spm=2013.1.w5002-5143967157.2.JwofvZ">
							<i class="glyphicon glyphicon-user"></i>联系客服
						</a>
					</div>
					<div class="orders"></div>
					<script type="text/javascript">openOrders();</script>
				</div>
				<div class="panel panel-primary">
					<div class="panel-heading">
						<b>我的管理</b>
					</div>
					<ul class="list-group">
						<li class="list-group-item">
							密码修改
						</li>
					</ul>
				</div>
			</div>
			<div class="col-md-8 pull-left">
				<div class="panel panel-primary">
					<div class="panel-heading"></div>
					<div class="panel-body main"></div>
				</div>
			</div>
		</div>
	</body>
</html>
