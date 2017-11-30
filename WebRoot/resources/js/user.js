var host = location.protocol + "//" + location.host + "/apptry/";

//function upload() {
//	var bar = $(".bar");
//	if ($("#file").val()) {
//		$("#uploadForm").attr("action", "dispatcher/upload").submit();
//		window.setTimeout(updateProgressbar, 1600);
//		bar.tooltip('show');
//		window.setTimeout(function() {
//			bar.tooltip('destroy');
//		}, 1600);
//	} else {
//		$("#file").tooltip('show');
//		window.setTimeout(function() {
//			$("#file").tooltip('destroy');
//		}, 1600);
//	}
//}

function newUpload(filename) {
	var progress = $(".progress");
	if(!filename.endsWith(".apk")) {
		alert(" 请选择.apk文件 ");
		return false;
	}
	removeAttribute("application");
	$("#uploadForm").attr("action", "dispatcher/newUpload").submit();
	$(":file").filestyle('disabled', true);
	window.setTimeout(updateProgressbar, 1600);
	progress.show();	
}

String.prototype.endsWith = function (substring) {
    var reg = new RegExp(substring + "$");
    return reg.test(this);
};

var attempts = 0;
var updateProgressbarFrequency = 1000; // 进度条的更新频率，单位ms
function updateProgressbar() {
//	var bar = $(".bar");
	var bar = $(".progress-bar");
	$.ajax( {
		url : "dispatcher/percentDone",
		async : false,
		dataType : "json",
		success : function(data) {
			if (data == null || data == "" || data == -1) {
				if (attempts++ < 3) {
					window.setTimeout(updateProgressbar, 1600);
				} else {
					alert("网络不稳定，请稍后重试.");
					window.location.reload();
				}
			} else {
				bar.attr("style", "width:" + data + "%;").html(data + "%");
				if (data < 99) {
					window.setTimeout(updateProgressbar, updateProgressbarFrequency);
				} else {
//					bar.html(" <i class='icon-ok icon-white'></i> 上传成功. ");
					window.setTimeout(function() {
						$(".wizard").wizard('next');
					}, 1600);
					checkApplication();
//					checkApplicationId();
					// observe the installation
//					window.setTimeout(observeInstallation, observeInstallationFrequency);
				}
			}
		}
	});
}

var attempts1 = 0;
var application; // json type
var checkAttributeFrequency = 10 * 1000;
function checkApplication() {
	application = hasApplication();
	if (++attempts1 >= 6) {
		$("#step2 h1").html("APP解析异常");
	} else {
		if(application) {
			$("#step2 h1").html("APP信息");
//			$("#step2 h1").append("<small>包名相同将覆盖安装</small>");
			$("#step2 .more .table .name").html(application.name);
			$("#step2 .more .table .size").html(application.size + "B");
			$("#step2 .more .table .packageName").html(application.packageName);
			$("#step2 .more .table .version").html(application.version);
			$("#step2 .more").show();
		} else {
			window.setTimeout(checkApplication, checkAttributeFrequency);
		}
	}
}

function hasApplication() {
	var result;
	$.ajax({
		url : "hasApplication/",
		async : false,
		dataType : "json",
		success : function(data) {
			result = data;
		}
	});
	return result;
}

function isApplicationExist(packageName) {
	
}

function cancelApplication() {
	$.ajax( {
		url : "removeAttribute/application/",
		async : false,
		success : function(data) {
			if(data) {
				gotoPage("user");
			}
		}
	});
}

function newSubmit() {
	$(".more button").attr("disabled", "disabled");
	$(".more button.btn-success").html("正在提交，请稍候..");
	$.ajax( {
		url : "dispatcher/newSubmit",
		dataType : "json",
		success : function(data) {
			if(data) {
				$("#step3 h1").html("专属代码<small>可嵌入至任意HTML5页面</small>");
				$("#step3 div#iframeCode pre").html(buildIframeCode(data));
				$("#step3 div#iframeContent").html(buildIframe(data));
				$(".wizard").wizard('next');
				// update apps
				openApps();
			}
		}, error: function(data) {
			$(".more button").removeAttr("disabled");
			$(".more button.btn-success").html("服务器异常，再次提交");
		}
	});
}

function removeAttribute(attr) {
	var result;
	$.ajax({
		url : "removeAttribute/" + attr + "/",
		async : false,
		success : function(data) {
			result = data;
		}
	});
	return result;
}

function buildIframeCode(applicationId) {
	var iframe = '&lt;iframe src="' + host + 'screen#'
		+ applicationId 
		+ '" scrolling="no" frameborder="no" style="height:788px; width:525px;background:transparent;"&gt;&lt;\/iframe&gt;';
	return iframe;
}

function buildIframe(applicationId) {
	var iframe = '<iframe src="' + host + 'screen#'
		+ applicationId 
		+ '" scrolling="no" frameborder="no" style="height:788px; width:525px;background:transparent;"></iframe>';
	return iframe;
}

var attempts2 = 0;
var observeInstallationFrequency = 60 * 1000; // 监控安装过程的频率，单位ms
function observeInstallation() {
	$.ajax( {
		url : "dispatcher/installation",
		async : false,
		dataType : "json",
		success : function(data) {}
	});
}

/**
	signup related start
*/
var registerTips = {
	"r_u" : "支持中文、英文、数字、'_'和'-'",
	"r_p" : "密码由5-20个字符组成",
	"r_p_ag" : "请再次确认您的密码",
	"r_email" : "请输入正确的邮箱"
}

function registerTipsBind() {
	var o_username = "";
	for(tips in registerTips) {
		var id = "#" + tips;
		var tipsId = id + "_tips";
		$(tipsId).html(registerTips[tips]).hide();
//		$(tipsId).html(registerTips[tips]).addClass("alert").hide();
		$(id).bind("focus", {tipsId: tipsId, tips: tips}, function(event) {
			$(event.data.tipsId).show();
			if("r_u" == event.data.tips) {
				o_username = $(this).val();
			}
		});
		if(tips == "r_u") {
			$(id).bind("blur", function() { // 判断用户名是否可用
				if($(this).val() != "" && $(this).val() != o_username) {
					checkUsername($(this).val());
				}
			});
		}
		$(id).bind("blur", {tipsId: tipsId}, function(event) {
			$(event.data.tipsId).hide();
		});
	}
}

function checkUsername(username) {
	var tips = $("#registerTips");
	$.ajax({
		url: host + "user/check/" + username,
		type: "POST",
		dataType: "json",
		success: function(data) {
			switch(data) {
				case 1: // SUCCESS
					tips.html("用户名可以使用.").removeClass("alert-danger").addClass("alert-info");
					break;
				case 2: // USER_EXIST
					tips.html("用户名已经被占用.").removeClass("alert-info").addClass("alert-danger");
					break;
				case 99: // ERROR_SYSTEM
					tips.html("服务器异常，请稍候重试.").removeClass("alert-info").addClass("alert-danger");
					break;
			}
			tips.show();
		}
	});
}

function registerValidation() {
	var registerForm = $("#registerForm");
	$("#r_u").val("");
	$("#r_p").val("");
	var tips = $("#registerTips");
	registerForm.validate({
		rules: {
			r_u: "required", 
			r_p: {required: true, minlength: 5, maxlength: 64},
			r_p_ag: {required: true, equalTo: "#r_p"},
			r_email: {required: true, email: true}
		},
		errorPlacement: function(error, element) {
//			element.next("span").removeClass("alert-info").addClass("alert-error");
			element.parent().parent().addClass("has-error");
		},
		submitHandler: function() {
			var signupBtn = $("#registerBtn");
			signupBtn.html("正在提交信息...").attr("disabled", "disabled");
			var md5Password = faultylabs.MD5($("#r_p").val()); 
			$("#r_p").val(md5Password);
			$("#r_p_ag").val(md5Password);
			$.ajax({
				url: host + "user/signup/",
				type: "POST",
				data: registerForm.serialize(),
				dataType: "json",
				success: function(data) {
					switch(data) {
//						case 1: case "1": // SUCCESS
//							tips.html("注册成功，请登录邮箱激活账号.").removeClass("alert-error").addClass("alert-success");
//							tips.html("注册成功，Apptry将自动跳转.").removeClass("alert-error").addClass("alert-success");
//							window.setTimeout(function() {
//								gotoPage("user");
//							}, 1500); 
//							break;
						case -2: case "-2": // USER_EXIST
							tips.html("用户名已经被占用.").removeClass("alert-info").addClass("alert-error");
							break;
						case -1: case "-1": // ERROR
							tips.html("服务器异常，请稍候重试.").removeClass("alert-info").addClass("alert-error");
							break;
						default: // developer id
							tips.html("注册成功，Apptry将自动跳转.").removeClass("alert-error").addClass("alert-success");
							window.setTimeout(function() {
								gotoPage("user");
							}, 1500);
							break;
					}
					tips.show();
					signupBtn.html("<i class='glyphicon glyphicon-pencil'></i>&nbsp;&nbsp;快速注册").removeAttr("disabled");
				}
			});
		}
	});
}

function register() {
	$("#registerForm").submit(function() {return false;});
}
/**
	signup related end
*/

/**
	pwdreset related start
*/
var pwdresetTips = {
	"username" : "请输入用户名",
	"email" : "请输入邮箱地址"
}
	
function pwdresetTipsBind() {
	for(tips in pwdresetTips) {
		var id = "#" + tips;
		var tipsId = id + "_tips";
		$(tipsId).html(pwdresetTips[tips]).hide();
//		$(tipsId).html(pwdresetTips[tips])
//			.addClass("alert").hide();
		$(id).bind("focus", {tipsId: tipsId, tips: tips}, function(event) {
			$(event.data.tipsId).show();
		});
		$(id).bind("blur", {tipsId: tipsId}, function(event) {
			$(event.data.tipsId).hide();
		});
	}
}

function pwdresetValidation() {
	var pwdresetForm = $("#pwdresetForm");
	var tips = $("#pwdresetTips");
	pwdresetForm.validate({
		rules: {
			username: "required", email: {required: true, email: true}
		},
		errorPlacement: function(error, element) {
//			element.next("span").removeClass("alert-info").addClass("alert-error");
			element.parent().parent().addClass("has-error");
		},
		submitHandler: function() {
			$.ajax({
				url: "user/pwdreset",
				type: "POST",
				data: pwdresetForm.serialize(),
				dataType: "json",
				success: function(data) {
					if(data != "") {				
						switch(data) {
							case 1: // SUCCESS
								tips.html("新的密码已经发送至您的邮箱，请查收.").removeClass("red").addClass("green");
								break;
							case 2: // USERNAME_ERROR
								tips.html("用户名有错误，请重新输入.").removeClass("green").addClass("red");
								break;
							case 3: // EMAIL_ERROR
								tips.html("邮箱地址有错误，请重新输入.").removeClass("green").addClass("red");
								break;
							case 99: // ERROR_SYSTEM
								tips.html("服务器异常，请稍候重试.").removeClass("green").addClass("red");
								break;
						}
						tips.show();
					}
				}
			});
		}
	});
}

function pwdreset() {
	$("#pwdresetForm").submit(function() {return false;});
}
/**
	pwdreset related end
*/

/**
	login related start
*/
function login() {
	var username = $("#l_u").val();
	var password = $("#l_p").val();
	var tips = $("#loginTips");
	tips.hide();
	if(!username || !password) {
		tips.html("用户和密码不能为空.").show();
		return false;
	}
	var md5Password = faultylabs.MD5($("#l_p").val()); 
	$("#l_p").val(md5Password);
	$.ajax({
		url: host + "user/login/",
		type: "POST",
		data: "l_u=" + username + "&l_p=" + md5Password,//loginForm.serialize(),
		dataType: "json",
		success: function(data) {
			if(data != "") {					
				switch(data) {
					case 0: // USER_UNACTIVED
						tips.html("用户未激活，请登录邮箱激活.").show();
						break;
					case 1: // SUCCESS
						gotoPage("user");
						break;
					case 2: // USER_NOT_EXIST
						tips.html("用户不存在.").show();
						$("#l_u").val(""); $("#l_p").val("");
						$("#l_u").focus();
						break;
					case 3: // PASSWORD_NOT_MATCH
						tips.html("密码不正确，请重新输入.").show();
						$("#l_p").val("");
						$("#l_p").focus();
						break;
					case 99: // ERROR_SYSTEM
						tips.html("服务器异常，请稍候重试.").show();
						break;
				}
			}
		},
		error: function() {
			tips.html("服务器异常，请稍候重试.").show();
		}
	});
}
/**
	login related end
*/

function load(className, url) {
	$("." + className).load(url);
}

function gotoPage(page) {
	location = host + page;
}

/**
function cancelOrder(orderId) {
	updateOrder(orderId, 3);
}

function updateOrder(orderId, newState) {
	$.ajax({
		url: host + "order/update/" + orderId + "/" + newState + "/",
		dataType: "json",
		success: function(data) {
			if(data != "") {
				if(data == 1) {
					window.location.reload();
				}
			}
		}
	});
}
*/