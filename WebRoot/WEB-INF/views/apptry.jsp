<%@ page language="java" import="java.util.*, molab.main.java.util.Apptry" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>Apptry</title>
		<meta charset="utf-8">
		<!-- Always force latest IE rendering engine (even in intranet) & Chrome Frame
                Remove this if you use the .htaccess -->
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<!-- Apple iOS Safari settings -->
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
		<meta name="apple-mobile-web-app-capable" content="yes" />
		<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
		
		<!-- App Start Icon  -->
		<link rel="apple-touch-startup-image" href="resources/images/screen_320x460.png" />
		<!-- For iOS devices set the icon to use if user bookmarks app on their homescreen -->
		<link rel="apple-touch-icon" href="resources/images/screen_57x57.png">
		<link rel="stylesheet" href="resources/include/base.css" title="plain">
		<link rel="stylesheet" type="text/css" href="resources/css/apptry.css">
		<style type="text/css">
			.btn {border: none; padding: 0px; position: absolute; }
			#menu {background: url('resources/image/320_480_menu.png'); width: 32px; height: 20px; left: 35px; top: 37px; }
			#menu:hover, #menu:active, #menu.active {left: 37px; top: 39px;}
			#home {background: url('resources/image/320_480_home.png'); width: 110px; height: 50px; left: 102px; top: 27px; }
			#home:hover, #home:active, #home.active {left: 104px; top: 29px;}
			#back {background: url('resources/image/320_480_back.png'); width: 32px; height: 20px; left: 246px; top: 37px; }
			#back:hover, #back:active, #back.active {left: 248px; top: 39px;}
		</style>
		<script src="resources/js/jquery.js"></script>
		<script src="resources/js/apptry.js"></script>
		<script src="resources/include/util.js"></script>
		<script src="resources/js/input.js"></script>
		<script>
			var packageName = "${packageName }";
			$(document).ready(function() {
				execute("START ${packageName } ${startActivity }");
			});
		</script>
	</head>

	<body style="margin: 0px; background: url('resources/image/320_480.png');">
		<div id="noVNC_screen" style="position: relative; left: -1px; top: 126px;">
			<div id="noVNC_status_bar" class="noVNC_status_bar"
				style="left: 102px; margin-top: 0px; position: absolute; top: -42px; width: 322px;">
				倒计时<span class="badge lefttime">120</span>秒
				<table border=0 width="100%" style="display: none;">
					<tr>
						<td>
							<div id="noVNC_status" style="position: relative; height: auto;">
								Loading
							</div>
						</td>
						<td width="1%">
							<div id="noVNC_buttons">
								<input type=button value="Send CtrlAltDel"
									id="sendCtrlAltDelButton">
							</div>
						</td>
					</tr>
				</table>
			</div>
			<div id="loading" style="width: 320px; height: 480px; left: 103px;position: absolute;background-color: white;">
				<img style="top: 140px; position: absolute; left: 82px;" 
					alt="正在准备手机" src="resources/image/loading156_95.gif">
			</div>
			<canvas id="noVNC_canvas" onMouseDown="start(event)" onMouseUp="finish(event)">
				您的浏览器不支持HTML5，推荐使用Firefox、Chrome
			</canvas>
			<div id="keys" style="position: absolute; top: 486px; left: 106px;">
				<button class="btn" id="menu" onMouseDown="pressDown()" onMouseUp="pressUp(this)"></button>
				<button class="btn" id="home" onClick="bye()"></button>
				<button class="btn" id="back" onClick="press('BACK')"></button>
			</div>
		</div>
		<script>
		/*jslint white: false */
		/*global window, $, Util, RFB, */
		// "use strict";
		var INCLUDE_URI = "resources/include/";
		// Load supporting scripts
		Util.load_scripts( [ "webutil.js", "base64.js", "websock.js", "des.js",
				"input.js", "display.js", "jsunzip.js", "_rfb.js" ]);
		
		var rfb;
		
		function passwordRequired(rfb) {
			var msg;
			msg = '<form onsubmit="return setPassword();"';
			msg += '  style="margin-bottom: 0px">';
			msg += 'Password Required: ';
			msg += '<input type=password size=10 id="password_input" class="noVNC_status">';
			msg += '<\/form>';
			$D('noVNC_status_bar').setAttribute("class", "noVNC_status_warn");
			$D('noVNC_status').innerHTML = msg;
		}
		function setPassword() {
			rfb.sendPassword($D('password_input').value);
			return false;
		}
		function sendCtrlAltDel() {
			rfb.sendCtrlAltDel();
			return false;
		}
		function updateState(rfb, state, oldstate, msg) {
			var s, sb, cad, level;
			s = $D('noVNC_status');
			sb = $D('noVNC_status_bar');
			cad = $D('sendCtrlAltDelButton');
			switch (state) {
			case 'failed':
				level = "error";
				break;
			case 'fatal':
				level = "error";
				break;
			case 'normal':
				level = "normal";
				break;
			case 'disconnected':
				level = "normal";
				break;
			case 'loaded':
				level = "normal";
				break;
			default:
				level = "warn";
				break;
			}
		
			if (state === "normal") {
				cad.disabled = false;
			} else {
				cad.disabled = true;
			}
		
			if (typeof (msg) !== 'undefined') {
				sb.setAttribute("class", "noVNC_status_" + level);
				s.innerHTML = msg;
			}
		}
		
		window.onscriptsload = function() {
			var host, port, password, path, token;
		
			$D('sendCtrlAltDelButton').style.display = "inline";
			$D('sendCtrlAltDelButton').onclick = sendCtrlAltDel;
		
			WebUtil.init_logging(WebUtil.getQueryVar('logging', 'warn'));
			document.title = unescape(WebUtil.getQueryVar('title', 'noVNC'));
			// By default, use the host and port of server that served this file
			host = WebUtil.getQueryVar('host', window.location.hostname);
            // port = WebUtil.getQueryVar('port', window.location.port);
            port = 2000;
            
			// if port == 80 (or 443) then it won't be present and should be
			// set manually
			if (!port) {
				if (window.location.protocol.substring(0, 5) == 'https') {
					port = 443;
				} else if (window.location.protocol.substring(0, 4) == 'http') {
					port = 80;
				}
			}
		
			// If a token variable is passed in, set the parameter in a cookie.
			// This is used by nova-novncproxy.
			token = WebUtil.getQueryVar('token', null);
			if (token) {
				WebUtil.createCookie('token', token, 1)
			}
			password = WebUtil.getQueryVar('password', '');
			// path = WebUtil.getQueryVar('path', 'websockify');
			path = "websockify/?token=${serialNumber }"
		
			if ((!host) || (!port)) {
				updateState('failed', "Must specify host and port in URL");
				return;
			}
		
			rfb = new RFB( {
				'target' : $D('noVNC_canvas'),
				'encrypt' : WebUtil.getQueryVar('encrypt',
						(window.location.protocol === "https:")),
				'repeaterID' : WebUtil.getQueryVar('repeaterID', ''),
				'true_color' : WebUtil.getQueryVar('true_color', true),
				'local_cursor' : WebUtil.getQueryVar('cursor', false),
				'shared' : WebUtil.getQueryVar('shared', true),
				'view_only' : WebUtil.getQueryVar('view_only', false),
				'updateState' : updateState,
				/*'onPasswordRequired' : passwordRequired*/
			});
			window.setTimeout(function() {
				$("#loading").hide(1000);
				rfb.connect(host, port, password, path);
				countTimer();
			}, 5000);
			
		};
		</script>
	</body>
</html>
