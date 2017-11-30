<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE HTML>
<div>
	<c:if test="${fn:length(list) > 0}">
	<ul class="list-group">
		<c:forEach var="order" items="${list}" varStatus="i">
		<!--  
		<div class="item">
			<span class="badge">${i.index + 1}</span>
			<span>${order.id}</span>
			<c:choose>
				<c:when test="${order.state == 0}">
					<span class="label label-default pull-right">等待支付</span>
				</c:when>
				<c:when test="${order.state == 1}"><span class="label label-success pull-right">支付成功</span></c:when>
				<c:when test="${order.state == 2}"><span class="label label-warning pull-right">支付失败</span></c:when>
				<c:when test="${order.state == 3}"><span class="label label-info pull-right">已取消</span></c:when>
				<c:otherwise><span>状态读取中...</span></c:otherwise>
			</c:choose>
		</div>
		-->
		<li class="list-group-item">
			<span class="label label-info">${i.index + 1}</span>
			<span>${order.id}</span>
			<c:choose>
				<c:when test="${order.state == 0}">
					<span class="label label-default pull-right">等待支付</span>
				</c:when>
				<c:when test="${order.state == 1}"><span class="label label-success pull-right">支付成功</span></c:when>
				<c:when test="${order.state == 2}"><span class="label label-warning pull-right">支付失败</span></c:when>
				<c:when test="${order.state == 3}"><span class="label label-info pull-right">已取消</span></c:when>
				<c:otherwise><span>状态读取中...</span></c:otherwise>
			</c:choose>
		</li>
		</c:forEach>
	</ul>
	</c:if>
	<c:if test="${fn:length(list) <= 0}">
		<div class="disabled"><a href="javascript:void(0)">无</a></div>
	</c:if>
</div>