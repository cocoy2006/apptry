var host = location.protocol + "//" + location.host + "/apptry/";

function loadIframe(applicationId) {
	var url = host + "application/" + applicationId + "/";
	load('main', url);
}

var sendBtnStatus = new Array();
sendBtnStatus["INIT"] = "发送到注册邮箱";
sendBtnStatus["SENDING"] = "邮件正在发送，请稍候...";
sendBtnStatus["ERROR"] = "尝试再次发送";

function send(applicationId, packageName, version) {
	var url = host + "sendIframe/";
	var sendBtn = $("#sendBtn");
	sendBtn.html(sendBtnStatus["SENDING"]).attr("disabled", "disabled");
	$.ajax({
		url: url,
		data: "applicationId=" + applicationId + "&packageName=" + packageName + "&version=" + version,
		type: "POST",
		dataType: "json",
		success: function(data) {
			switch(data) {
				case 1:
					alert("发送成功，请查收.");
					sendBtn.html(sendBtnStatus["INIT"]);
					break;
				case 99:
					alert("服务器异常，请稍候重试.");
					sendBtn.html(sendBtnStatus["ERROR"]);
					break;
			}
			sendBtn.removeAttr("disabled");
		}, error: function(data) {
			alert("网络异常，请稍候重试.");
			sendBtn.html(sendBtnStatus["ERROR"]);
			sendBtn.removeAttr("disabled");
		}
	});
}

function del(applicationId, packageName, that) {
	if(confirm("确定删除吗?")) {
		var url = host + "application/remove/" + applicationId + "/" + packageName + "/";
		$.ajax({
			url: url,
			async: false,
			beforeSend: function() {
				$(that).html("删除中...").attr("disabled", "disabled");
			},
			dataType: "json",
			success: function(data) {
				if(data == 1) {
					console.info("SUCCESS");
				} else {
					console.info(data);
				}
			}
		});
		location.reload();
		return true;
	} else {
		return false;
	}
}