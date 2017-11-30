<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE HTML>
<html>
	<head>
		<base href="<%=basePath%>">
		<script type="text/javascript" src="<%=basePath%>resources/bootstrap-3.0.3/filestyle/bootstrap-filestyle.min.js"></script>
	</head>
	<body>
		<div class="wizard" style="display: none;">
			<ul class="steps">
				<li data-target="#step1" class="active">
					<span class="badge badge-info">1/3</span>上传APP
					<span class="chevron"></span>
				</li>
				<li data-target="#step2">
					<span class="badge">2/3</span>APP信息
					<span class="chevron"></span>
				</li>
				<li data-target="#step3">
					<span class="badge">3/3</span>立即体验
					<span class="chevron"></span>
				</li>
			</ul>
		</div>
		<div class="step-content">
			<!-- 步骤一 -->
			<div class="step-pane active" id="step1">
				<h4 style="padding: 30px 0;">
					Hi ${sessionScope.developer.username}，上传一个APP吧
					<small style="margin-left: 5px;">支持100MB以内，以.apk结尾</small>
				</h4>
				<!-- <h4>选择本地程序安装文件&nbsp;&nbsp;<small>支持100MB以内，以.apk结尾</small></h4> -->
				<form id="uploadForm" name="uploadForm" enctype="multipart/form-data" 
					target="hiddenUploadFrame" method="post">
					<input type="file" id="file" name="file" class="filestyle" onchange="newUpload(this.value)" data-input="false"
						data-buttonText="上传APP" data-buttonName="btn-success" data-size="lg" data-iconName="glyphicon glyphicon-folder-open">
				</form>
				<div class="progress" style="display: none; margin: 20px 0;">
					<div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="2"
						aria-valuemin="0" aria-valuemax="100" style="width: 2%;">
					</div>
				</div>
				<iframe name="hiddenUploadFrame" id="hiddenUploadFrame" style="display: none;"></iframe>
			</div>
			<!-- 步骤二 -->
			<div class="step-pane text-center text-success" id="step2">
				<h1>正在解析APP，请稍候...</h1>
				<div class="more" style="display: none;">
					<table class="table table-striped table-bordered">
						<tr>
							<th>APP名称</th>
							<td class="name"></td>
							<th>文件大小</th>
							<td class="size"></td>
						</tr>
						<tr>
							<th>包名</th>
							<td class="packageName"></td>
							<th>版本号</th>
							<td class="version"></td>
						</tr>
					</table>
					<div>
						<button class="btn btn-danger btn-lg" onclick="cancelApplication()">&lt;&lt;&nbsp;删除APP</button>
						<button class="btn btn-success btn-lg" onclick="newSubmit()">继续提交&nbsp;&gt;&gt;</button>
					</div>
				</div>
			</div>
			<!-- 步骤三 -->
			<div class="step-pane text-center text-success" id="step3">
				<h1>正在生成专属代码，请稍候...</h1>
				<div id="iframeCode"><pre></pre></div>
				<div id="iframeContent"></div>
			</div>
		</div>
	</body>
</html>
