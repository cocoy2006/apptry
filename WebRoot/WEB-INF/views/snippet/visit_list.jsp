<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE HTML>
<c:if test="${fn:length(list) > 0}">
	<c:set var="length" value="${fn:length(list)}"/>
	<c:forEach var="vlc" items="${list}" varStatus="i">
	<div class="alert alert-success" onclick="openStatistisDetail(${vlc.application.id})">
		<a class="alert-link">
			<span class="badge badge-success">${i.index + 1}/${length}</span>
			<span>${vlc.application.packageName}</span>
			<span class="pull-right">总共访问:${vlc.totalClicks }/本月访问:${vlc.monthClicks }</span>
		</a>
	</div>
	</c:forEach>
</c:if>
<c:if test="${fn:length(list) <= 0}">
	<div class="disabled"><a href="javascript:void(0)">无</a></div>
</c:if>