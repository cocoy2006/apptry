<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>Apptry首页</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/bootstrap-3.0.3/css/bootstrap-theme.min.css">
		<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/bootstrap-3.0.3/css/bootstrap.min.css">
		<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/css/apptry.css">
		<script src="<%=basePath%>resources/js/jquery.js"></script>
		<style type="text/css">
			.message-error {text-align: center; color: red;}
		</style>
		<script type="text/javascript" src="<%=basePath%>resources/js/pay.js"></script>
		<script type="text/javascript">
			function tryNow(url) {
				location.href = url;
				location.reload();
			} 
		</script>
	</head>

	<body class="index">
		<c:if test="${sessionScope.MESSAGE != null}">
			<p class="message-error">${sessionScope.MESSAGE == null}</p>
			<c:remove var="MESSAGE" scope="session" />
		</c:if>
		<nav class="main navbar navbar-fixed-top" style="background-color: #32B1C2; min-height:0;">
			<div class="navbar-header">
				<a class="navbar-brand" href="#" style="padding: 10px;">Apptry</a>
			</div>
			<ul class="links">
				<li>
					<a href="<%=basePath%>home#information">行业信息</a>
				</li>
				<li>
					<a href="<%=basePath%>home#howto">业务流程</a>
				</li>
				<li>
					<a href="<%=basePath%>home#contact">联系我们</a>
				</li>
				<li>
					<c:if test="${sessionScope.developer == null}">
						<a class="btn-login" href="<%=basePath%>login/">登录</a>
					</c:if>
					<c:if test="${sessionScope.developer != null}">
						<a class="btn-login" href="<%=basePath%>user/" title="更多设置">
							${sessionScope.developer.username}
						</a>
					</c:if>
				</li>
				<li>
					<a class="btn btn-default btn-signup" href="<%=basePath%>signup/">注册</a>
				</li>
			</ul>
		</nav>
		<div class="fold hero index">
			<div class="phone">
				<iframe src="http://localhost:8080/apptry/screen#10" scrolling="no" frameborder="no"
					style="height: 788px; width: 525px;">
				</iframe>
				<!--  
				<iframe src="http://210.14.159.245/apptry/screen#8" scrolling="no" frameborder="no"
					style="height: 788px; width: 525px;">
				</iframe>
				-->
			</div>
			<div class="container">
				<div class="jumbotron" style="background-color:transparent;">
					<div class="benefit">
						<h3>通过在线体验</h3>
						<h2>提高App下载量</h2>
						<h3>
							<br>
							<p style="font-size:28px">转化为App下载的网页<br>App展示比率约为12%</p>
						</h3>
						<a class="btn btn-lg btn-success" href="home#now">体验ZAKER</a>
						<button class="btn btn-lg" disabled style="margin-left: 20px;">体验计算器</button>
					</div>
				</div>
			</div>
		</div>
		<div id="information" class="fold use-case light">
			<div class="container">
				<h2 class="pricing-intro">
					<br>
				</h2>
				<div class="sub-info">
					<a href="http://www.36kr.com/p/203698.html">通过网页模拟运行iOS应用，App.io成为应用推广最牛方式</a>
					<p>当你打开 Facebook，看到好友分享来一个 iOS 应用。点击后弹开了一个网页，而相关应用竟然在页面之中运行。</p> 
					<p>在简单浏览后，这款应用的功能和设计吸引了你，你决定将其下载到 iPhone 中。</p>		
					<p>App.io 刚刚获得了 100 多万美元融资，并且得到了一系列知名风投的支持。不过 App.io 也并非此领域唯一的玩家，</p>
					<p>同样通过网页技术渲染 iOS 应用的还有 Pieceable（已经被 Facebook 收购）。</p>			
					<a href="http://it.sohu.com/20110413/n280251177.shtml">Pieceable：让你用浏览器运行iPhone应用</a>
					<br>
					<p>让你可以在网页里运行并测试嵌入到网页中的iPhone应用。应用开发者可以把自己开发的应用程序直接发布在Pieceable中，</p>
					<p>然后Pieceable工作组成员会据此做出一个网页来演示上传的应用功能。</p>
					
				</div>				
			</div>
		</div>
		<div id="howto" class="fold use-case gray">
			<div class="container">
				<h2 class="pricing-intro">
					简洁的业务流程
				</h2>
				<div class="sub-info">
					<p>1、App开发者联系我们的业务经理，提供App的.apk文件；</p>
					<p>2、Apptry反馈一段HTML代码（iframe）；</p>
					<p>3、App开发者将HTML代码嵌入到任意页面发布即可。</p>
				</div>				
			</div>
		</div>
		<div id="contact" class="fold use-case dark">
			<div class="container">
				<h4 class="tweets-intro">在网页中展示您的App！<br>支持Android App！<br>对iOS App的支持正在开发，敬请期待！</h4>
				<h4 class="tweets-intro">E-mail：service@apptry.cn<br>
				电&nbsp;&nbsp;&nbsp;话：185&nbsp;1159&nbsp;9022</h4>
			</div>
		</div>
	</body>
</html>