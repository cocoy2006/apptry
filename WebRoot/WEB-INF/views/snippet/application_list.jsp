<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE HTML>
<c:if test="${fn:length(list) > 0}">
<ul class="list-group">
	<c:forEach var="application" items="${list}" varStatus="i">
	<li class="list-group-item">
		<span class="label label-info">${i.index + 1}</span>
		<p class="overflow" onclick="loadIframe(${application.id})">${application.name}</p>
		<button class="btn btn-xs btn-danger pull-right" onclick="del(${application.id}, '${application.packageName }', this)">删除</button>
	</li>
	</c:forEach>
</ul>
</c:if>
<c:if test="${fn:length(list) <= 0}">
	<div class="disabled"><a href="javascript:void(0)">无</a></div>
</c:if>