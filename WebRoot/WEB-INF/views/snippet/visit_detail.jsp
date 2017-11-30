<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE HTML>
<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/DataTables-1.10.0/examples/resources/bootstrap/3/dataTables.bootstrap.css">
<script type="text/javascript" src="<%=basePath%>resources/DataTables-1.10.0/media/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/DataTables-1.10.0/examples/resources/bootstrap/3/dataTables.bootstrap.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		$(".table").dataTable({
			"language": {
	            "lengthMenu": " 显示_MENU_条 ",
	            "zeroRecords": " 暂时没有记录 ",
	            "info": " 从_START_ 到 _END_ 条记录——总记录数为 _TOTAL_ 条", // do not work
	            "infoEmpty": " 暂时没有记录 ",
	            "infoFiltered": "(全部记录数 _MAX_  条)",
	            "search": " 模糊搜索 ",
	            "paginate": {
					"first": 	" 第一页 ",
					"previous": " 上一页 ",
					"next":     " 下一页 ",
					"last":     " 最后一页 "
	            }
	        }
		});
	});
</script>
<div class="alert alert-success" onclick="openStatisticsList()">
	<a class="btn-link">
		<label>&lt;&lt;返回</label>
		<label>${application.packageName}</label>
	</a>
</div>
<table class="table table-striped table-bordered dataTable" cellspacing="0" width="100%">
    <thead>
        <tr>
            <th>NO</th>
            <th>IP地址</th>
            <th>来访时间</th>
            <th>时长</th>
            <th style="width: 200px;">终端</th>
        </tr>
    </thead> 
    <tbody>
    <c:if test="${visits != null}">
    	<c:forEach var="visit" items="${visits}" varStatus="i">
    	<tr>
            <td>${i.index + 1}</td>
			<td>${visit.value.ipAddress}</td>
			<td>${visit.key}</td>
			<td>${visit.value.durationTime}秒</td>
			<td>${visit.value.userAgent}</td>
        </tr>
    	</c:forEach>
    </c:if>
    </tbody>
</table>