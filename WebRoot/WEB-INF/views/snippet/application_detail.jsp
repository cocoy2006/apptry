<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<c:if test="${application != null}">
	<table class="table table-striped table-bordered">
		<tr>
			<th>APP名称</th>
			<td>${application.name}</td>
			<th>文件大小</th>
			<td>${application.size}B</td>
		</tr>
		<tr>
			<th>包名</th>
			<td>${application.packageName}</td>
			<th>版本号</th>
			<td>${application.version}</td>
		</tr>
	</table>
	<h4>
		专属代码
		<!--  
		<div class="btn-group">
			<button id="sendBtn" class="btn" onclick="send('${application.id}', '${application.name}', '${application.version}')">
				发送至邮箱
			</button>
		</div>
		-->
	</h4>
	<pre>
&lt;iframe frameborder="no" scrolling="no" 
	src="<%=basePath%>screen#${application.id}" 
	style="height:788px;width:525px;background:transparent;"&gt;
&lt;/iframe&gt;
	</pre>
	<div>
		<iframe src="<%=basePath%>screen#${application.id}" scrolling="no" frameborder="no"
			style="height:788px;width:525px;background:transparent;">
		</iframe>
	</div>
</c:if>
<c:if test="${application == null}">
	<pre>数据异常，请刷新当前页面.</pre>
</c:if>
