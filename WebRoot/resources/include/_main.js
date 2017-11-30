"use strict";

/**
 * äº‘è°ƒè¯•jsæ–‡ä»¶
 * 
 * @auther tangyu01(contact by molixiangpiangou@sina.cn)
 * @date 2013-08-05 
 */
var gBase64EncodeChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
var gBase64DecodeChars = [ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52,
		53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1,
		2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21,
		22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33,
		34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51,
		-1, -1, -1, -1, -1 ];
		
var sockets = {'novncStatus': null};

var globalVars = {
	status: 0,
	logId: 0,
	setErrNum: function(num) {
		var url;
		
		if(this.status == 1) {
			return;
		} else {
			
			this.status = num;
			
			url = 'ajax_hd.php?f=setUAErrorNum' + "&errno=" + num + "&log_id=" + this.logId + '&r=' + new Date().getTime();
			$.ajax({
				url:url, 
				async:false,
				success:function(data) {
				},
				dataType:'json'
			});	
		}
	},
	retry: function() {
		var url = window.location.href,
			ct;
		if(url.indexOf('ct') == -1) {
			url = url + '&ct=1&logid=' + globalVars.logId;
			window.location.href = url;
		} else {
			ct = /ct=(\d)&/.exec(url);
			ct = parseInt(ct[1]);
			if(ct && ct <= 5) {
				url = url.replace('ct=' + ct, 'ct=' + (++ct));
				window.location.href = url;
			} else {
				alert('è¿žæŽ¥æ‰‹æœºå¤±è´¥');
				$('#exitCD').click();
			}
		}
	}
}

function base64encode(str) {
    var out, i, len;
    var c1, c2, c3;
    len = str.length;
    i = 0;
    out = "";
    while(i < len) {
    c1 = str.charCodeAt(i++) & 0xff;
    if(i == len)
    {
        out += gBase64EncodeChars.charAt(c1 >> 2);
        out += gBase64EncodeChars.charAt((c1 & 0x3) << 4);
        out += "==";
        break;
    }
    c2 = str.charCodeAt(i++);
    if(i == len)
    {
        out += gBase64EncodeChars.charAt(c1 >> 2);
        out += gBase64EncodeChars.charAt(((c1 & 0x3)<< 4) | ((c2 & 0xF0) >> 4));
        out += gBase64EncodeChars.charAt((c2 & 0xF) << 2);
        out += "=";
        break;
    }
    c3 = str.charCodeAt(i++);
    out += gBase64EncodeChars.charAt(c1 >> 2);
    out += gBase64EncodeChars.charAt(((c1 & 0x3)<< 4) | ((c2 & 0xF0) >> 4));
    out += gBase64EncodeChars.charAt(((c2 & 0xF) << 2) | ((c3 & 0xC0) >>6));
    out += gBase64EncodeChars.charAt(c3 & 0x3F);
    }
    return out;
}

function base64decode(str) {
	var c1, c2, c3, c4;
	var i, len, out;

	len = str.length;
	i = 0;
	out = "";
	while (i < len) {
		/* c1 */
		do {
			c1 = gBase64DecodeChars[str.charCodeAt(i++) & 0xff];
		} while (i < len && c1 == -1);
		if (c1 == -1)
			break;

		/* c2 */
		do {
			c2 = gBase64DecodeChars[str.charCodeAt(i++) & 0xff];
		} while (i < len && c2 == -1);
		if (c2 == -1)
			break;

		out += String.fromCharCode((c1 << 2) | ((c2 & 0x30) >> 4));

		/* c3 */
		do {
			c3 = str.charCodeAt(i++) & 0xff;
			if (c3 == 61)
				return out;
			c3 = gBase64DecodeChars[c3];
		} while (i < len && c3 == -1);
		if (c3 == -1)
			break;

		out += String.fromCharCode(((c2 & 0XF) << 4) | ((c3 & 0x3C) >> 2));

		/* c4 */
		do {
			c4 = str.charCodeAt(i++) & 0xff;
			if (c4 == 61)
				return out;
			c4 = gBase64DecodeChars[c4];
		} while (i < len && c4 == -1);
		if (c4 == -1)
			break;

		out += String.fromCharCode(((c3 & 0x03) << 6) | c4);
	}
	return out;
}

/*
 * error num
 * -1ï¼š unable to init gl
 * -2 failed to connect vnc
 * -3 proxy connection
 */

function Queue(capacity) { //use for storing recent logs
	
	this.capacity = capacity;
	this.elements = new Array();
	
	Queue.prototype.enQueue = function(element) {
		if(arguments.length != 1) { //å¦‚æžœä¸æ˜¯å•å…ƒç´ å…¥é˜Ÿï¼Œè¿”å›žfalse
			return false; 
		}
		if(this.elements.length >= this.capacity) {
			this.elements.pop();
			this.elements.unshift(element);
		} else {
			this.elements.unshift(element);
		}
	}
	
	Queue.prototype.deQueue = function() {
		if(this.elements.length <= 0) {
			return false;
		}
		return this.elements.pop();
	}
	
}

function CDNovnc(options) { //class of CloudDebug Novnc
	
	var rfb = this.rfb;
	
	this.host = options.host;
	this.port = options.port ? options.port : 9001;
	this.password = options.password ? options.password : "";
	this.path = options.path ? options.path : "websockify";
	this.rfb = new RFB({'target':       $D('phoneScreen'),
					   'encrypt':      WebUtil.getQueryVar('encrypt',
								(window.location.protocol === "https:")),
					   'true_color':   WebUtil.getQueryVar('true_color', true),
					   'local_cursor': WebUtil.getQueryVar('cursor', false),
					   'shared':       WebUtil.getQueryVar('shared', true),
					   'view_only':    WebUtil.getQueryVar('view_only', false),
					   'updateState':  updateState,
					   /*'onPasswordRequired':  passwordRequired*/});
	
	this.connect = function() {
		this.rfb.connect(this.host, this.port, this.password, this.path);
	}					   
					   
					   
	function updateState(rfb, state, oldstate, msg) {
		sockets.novncStatus = state;
       	if(state == 'failed' || state == 'fatal') {
			if($('#isProxy').val()) {
				globalVars.setErrNum(-3);
				alert('è¿žæŽ¥æ‰‹æœºå¤±è´¥ï¼Œè¯·æ£€æŸ¥æ‚¨æ˜¯å¦ä½¿ç”¨äº†ä»£ç†æœåŠ¡å™¨');
				$('#exitCD').click();
			} else {
				globalVars.setErrNum(-2);
				setTimeout(globalVars.retry, 1000);
			}
       	}
       	if(state == 'normal') {
       		globalVars.setErrNum(2);
       		rfb.get_mouse().set_focused(false);
			rfb.get_keyboard().set_focused(false);
       	}
	}
}

function CDWebSocket(msgCallback, openCallback, closeCallback) { //class of websocket connection

	var socket,
		msgCallback,
		openCallback,
		closeCallback;
	
	this.msgCallback = (typeof(msgCallback) == "undefined") ?  function(msg){} : msgCallback;
	this.openCallback = (typeof(openCallback) == "undefined") ?  function(){} : openCallback;
	this.closeCallback = (typeof(closeCallback) == "undefined") ?  function(){} : closeCallback;

	msgCallback = this.msgCallback;
	openCallback = this.openCallback;
	closeCallback = this.closeCallback;
			
	function open(ip, port, handle){
		var host = "ws://" + ip + ':' + port + '/' + handle;
		try {		
			if(window.WebSocket) {
				
			} else if (window.MozWebSocket) {
				window.WebSocket = window.MozWebSocket;
			} else{
				Util.Error("Socket not supported");
			}
			socket = new WebSocket(host);
			Util.Debug('Socket Status 1: ' + socket.readyState+ ' (new)');

			socket.onopen = function(){
				openCallback();
				Util.Info('Socket Status 2: ' + socket.readyState + ' (open)');	
			}

			socket.onmessage = function(msg){
				msgCallback(msg);			
			}

			socket.onclose = function(){
				closeCallback();
				Util.Info('Socket Status 3: '+ socket.readyState + ' (Closed)');
			}			
		} catch(exception){
			Util.Error('Error' + exception);
		}	
	}
	
	this.isOpen = function(){
		if(socket && socket.readyState === WebSocket.CONNECTING ){
			return true;
		} else {
			return false;
		}
	}			
	
	function reOpen(ip, port, handle){
		if(!this.isOpen()){
			Util.Debug('reconnect wsocket.');
			open(ip, port, handle);
		}
	}

	this.connect = function(ip, port, handle){		
		open(ip, port, handle);
	}
	
	this.send = function(text){
		if( text != "" ){
			try{
				socket.send(text);
				Util.Debug('Sent: ' + base64decode(text));
			} catch(exception){
				Util.Error(' Error: ' + exception );
			}
		}
	}
	
	this.close = function(){
		Util.Debug('close socket 1');
		if (socket) {
			Util.Debug('close socket 2');
			if ((socket.readyState === WebSocket.OPEN) ||
				(socket.readyState === WebSocket.CONNECTING)) {
				Util.Info("Closing WSocket connection");
				socket.close();
			}
		}
	}
	
}

function CDWebgl(websocket) {
	
	var gl,
		shaderProgram,
		cubeVertexPositionBuffer,
		cubeVertexTextureBuffer,
		cubeVertexIndexBuffer,
		cubeVertexNormalBuffer,
		pMatrix,
		mvMatrix,
		xRot = 90,
		yRot = 0,
		zRot = 0,
		xRotTarget = 90,
		yRotTarget = 0,
		zRotTarget = 0,
		rotAngle = 15,
		lightWeight,
		bgTexture,
		canvas = document.getElementById('sensorViewport'),
		
		_construct = (function() {
			initGL(canvas);
			initShaders();
			initBuffers();
			initTexture();
			
			gl.clearColor(0.0, 0.0, 0.0, 1.0);
			gl.enable(gl.DEPTH_TEST);
			
			initRotxLight();			
			bindBtns();
		})();
	
	function initRotxLight() {
		if(sockets.novncStatus != 'normal') {
			setTimeout(initRotxLight, 1000);
		} else {
			sendRotAngle();
			websocket.send('sensor set light 10000');
			tick();
		}
	}
			
	function initGL(canvas) {
		try {
			gl = canvas.getContext('experimental-webgl');
			gl.viewportWidth = canvas.width;
			gl.viewportHeight = canvas.height;
		} catch(e) {
			//console.log(e);
			globalVars.setErrNum(-1);
		}
	}
		
	function initShaders() {
		var fragmentShader = getShader(gl, 'shader-fs'),
			vertexShader = getShader(gl, 'shader-vs');
		
		shaderProgram = gl.createProgram();
		gl.attachShader(shaderProgram, fragmentShader);
		gl.attachShader(shaderProgram, vertexShader);
		gl.linkProgram(shaderProgram);
		
		if(!gl.getProgramParameter(shaderProgram, gl.LINK_STATUS)) {
			//console.log('failed to init shader program');
			globalVars.setErrNum(-4);
		}
		
		gl.useProgram(shaderProgram);
		
		shaderProgram.vertexPositionAttribute = gl.getAttribLocation(shaderProgram, 'aVertexPosition');
		gl.enableVertexAttribArray(shaderProgram.vertexPositionAttribute);
		
		shaderProgram.vertexTextureAttribute = gl.getAttribLocation(shaderProgram, 'aVertexTexture');
		gl.enableVertexAttribArray(shaderProgram.vertexTextureAttribute);
		
		shaderProgram.vertexNormalAttribute = gl.getAttribLocation(shaderProgram, 'aVertexNormal');
		gl.enableVertexAttribArray(shaderProgram.vertexNormalAttribute);
		
		shaderProgram.pMatrixUniform = gl.getUniformLocation(shaderProgram, 'uPMatrix');
		shaderProgram.mvMatrixUniform = gl.getUniformLocation(shaderProgram, 'uMVMatrix');
		shaderProgram.samplerUniform = gl.getUniformLocation(shaderProgram, "uSampler");
		shaderProgram.nMatrixUniform = gl.getUniformLocation(shaderProgram, "uNMatrix");
		shaderProgram.ambientColorUniform = gl.getUniformLocation(shaderProgram, "uAmbientColor");
		shaderProgram.lightingDirectionUniform = gl.getUniformLocation(shaderProgram, "uLightingDirection");
		shaderProgram.directionalColorUniform = gl.getUniformLocation(shaderProgram, "uDirectionalColor");
	}
		
	function initBuffers() {
		cubeVertexPositionBuffer = gl.createBuffer();
        gl.bindBuffer(gl.ARRAY_BUFFER, cubeVertexPositionBuffer);
        var vertices = [
        	// Front face
        	-7.263, -13.263,  1.0,
             7.263, -13.263,  1.0,
             7.263,  13.263,  1.0,
            -7.263,  13.263,  1.0,
 
            // Back face
            -7.263, -13.263, -1.0,
            -7.263,  13.263, -1.0,
             7.263,  13.263, -1.0,
             7.263, -13.263, -1.0,
 
            // Top face
            -7.263,  13.263, -1.0,
            -7.263,  13.263,  1.0,
             7.263,  13.263,  1.0,
             7.263,  13.263, -1.0,
 
            // Bottom face
            -7.263, -13.263, -1.0,
             7.263, -13.263, -1.0,
             7.263, -13.263,  1.0,
            -7.263, -13.263,  1.0,
 
            // Right face
             7.263, -13.263, -1.0,
             7.263,  13.263, -1.0,
             7.263,  13.263,  1.0,
             7.263, -13.263,  1.0,
 
            // Left face
            -7.263, -13.263, -1.0,
            -7.263, -13.263,  1.0,
            -7.263,  13.263,  1.0,
            -7.263,  13.263, -1.0
        ]
        gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW);
        cubeVertexPositionBuffer.itemSize = 3;
        cubeVertexPositionBuffer.numItems = 24;
        
        cubeVertexTextureBuffer = gl.createBuffer();
        gl.bindBuffer(gl.ARRAY_BUFFER, cubeVertexTextureBuffer);
        var textureCoords = [
        	// Front face
        	0.218, 0.246,
          	0.484, 0.246,
	        0.484, 0.738,
	        0.218, 0.738,
	 
	        // Back face
	        0.879, 0.246,
	        0.879, 0.738,
	        0.611, 0.738,
	        0.611, 0.246,
	 
	        // Top face
	        0.218, 0.820,
	        0.218, 0.787,
	        0.484, 0.787,
	        0.484, 0.820,
	 
	        // Bottom face
	        0.484, 0.201,
	        0.218, 0.201,
	        0.218, 0.167,
	        0.484, 0.167,
	 
	        // Right face
	        0.570, 0.246,
	        0.570, 0.738,
	        0.533, 0.738,
	        0.533, 0.246,
	 
	        // Left face
	        0.134, 0.246,
	        0.170, 0.246,
	        0.170, 0.738,
	        0.134, 0.738,	
        ]
        gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(textureCoords), gl.STATIC_DRAW);
        cubeVertexTextureBuffer.itemSize = 2;
        cubeVertexTextureBuffer.numItems = 24;
        
        cubeVertexNormalBuffer = gl.createBuffer();
        gl.bindBuffer(gl.ARRAY_BUFFER, cubeVertexNormalBuffer);
        var vertexNormals = [
            // Front face
             0.0,  0.0,  1.0,
             0.0,  0.0,  1.0,
             0.0,  0.0,  1.0,
             0.0,  0.0,  1.0,
 
            // Back face
             0.0,  0.0, -1.0,
             0.0,  0.0, -1.0,
             0.0,  0.0, -1.0,
             0.0,  0.0, -1.0,
 
            // Top face
             0.0,  1.0,  0.0,
             0.0,  1.0,  0.0,
             0.0,  1.0,  0.0,
             0.0,  1.0,  0.0,
 
            // Bottom face
             0.0, -1.0,  0.0,
             0.0, -1.0,  0.0,
             0.0, -1.0,  0.0,
             0.0, -1.0,  0.0,
 
            // Right face
             1.0,  0.0,  0.0,
             1.0,  0.0,  0.0,
             1.0,  0.0,  0.0,
             1.0,  0.0,  0.0,
 
            // Left face
            -1.0,  0.0,  0.0,
            -1.0,  0.0,  0.0,
            -1.0,  0.0,  0.0,
            -1.0,  0.0,  0.0
        ];
        gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertexNormals), gl.STATIC_DRAW);
        cubeVertexNormalBuffer.itemSize = 3;
        cubeVertexNormalBuffer.numItems = 24;
        
        cubeVertexIndexBuffer = gl.createBuffer();
        gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, cubeVertexIndexBuffer);
        var cubeVertexIndices = [
            0, 1, 2,      0, 2, 3,    // Front face
            4, 5, 6,      4, 6, 7,    // Back face
            8, 9, 10,     8, 10, 11,  // Top face
            12, 13, 14,   12, 14, 15, // Bottom face
            16, 17, 18,   16, 18, 19, // Right face
            20, 21, 22,   20, 22, 23  // Left face
        ];
        gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(cubeVertexIndices), gl.STATIC_DRAW);
        cubeVertexIndexBuffer.itemSize = 1;
        cubeVertexIndexBuffer.numItems = 36;
	}
	
	function initTexture() {
		
		bgTexture = gl.createTexture();
		bgTexture.image = new Image();
		bgTexture.image.onload = function() {	
			handleLoadedTexture(bgTexture);
		}
		
		bgTexture.image.src = 'img/cloudDebug/phoneTexture.png';
	}

	function handleLoadedTexture(texture) {
		gl.bindTexture(gl.TEXTURE_2D, texture);
		gl.pixelStorei(gl.UNPACK_FLIP_Y_WEBGL, true);
		gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA, gl.UNSIGNED_BYTE, texture.image);
		gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.LINEAR);
		gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.LINEAR);
		gl.bindTexture(gl.TEXTURE_2D, null);
	}
	
	function getShader(gl, id) {
		var shaderScript = document.getElementById(id);
		if(!shaderScript) {
			return null;
		}
		var str = "";
		var k = shaderScript.firstChild;
		while(k) {
			if(k.nodeType == 3) {
				str += k.textContent;
			}
			k = k.nextSibling;
		}
		var shader;
		if(shaderScript.type == 'x-shader/x-fragment') {
			shader = gl.createShader(gl.FRAGMENT_SHADER);
		} else if(shaderScript.type == 'x-shader/x-vertex') {
			shader = gl.createShader(gl.VERTEX_SHADER);
		} else {
			return null;
		}
		
		gl.shaderSource(shader, str);
		gl.compileShader(shader);
		
		if(!gl.getShaderParameter(shader, gl.COMPILE_STATUS)) {
			//console.log(gl.getShaderInfoLog(shader));
			globalVars.setErrNum(-5);
			return null;
		} else {
			return shader;
		}
		
	}
	
	function tick() {
		okRequestAnimationFrame(tick);
		drawScene();
		animate();
	}
	
	function drawScene() {
		
		var lightingDirection,
			adjustedLD;
			
		gl.viewport(0, 0, gl.viewportWidth, gl.viewportHeight);
		gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);
		
		pMatrix = okMat4Proj(45, gl.viewportWidth / gl.viewportHeight, 0.1, 100);
		
		mvMatrix = new okMat4();
		mvMatrix.translate(OAK.SPACE_WORLD, 0.0, 0.0, -50.0, true);
		mvMatrix.rotX(OAK.SPACE_LOCAL, -90, true);
		mvMatrix.rotZ(OAK.SPACE_LOCAL, zRot, true);
		mvMatrix.rotX(OAK.SPACE_LOCAL, xRot, true);
		mvMatrix.rotY(OAK.SPACE_LOCAL, yRot, true);
		
		gl.bindBuffer(gl.ARRAY_BUFFER, cubeVertexPositionBuffer);
		gl.vertexAttribPointer(shaderProgram.vertexPositionAttribute, cubeVertexPositionBuffer.itemSize, gl.FLOAT, false, 0, 0);
		
		gl.bindBuffer(gl.ARRAY_BUFFER, cubeVertexTextureBuffer);
		gl.vertexAttribPointer(shaderProgram.vertexTextureAttribute, cubeVertexTextureBuffer.itemSize, gl.FLOAT, false, 0, 0);
		gl.activeTexture(gl.TEXTURE0);
		gl.bindTexture(gl.TEXTURE_2D, bgTexture);
		gl.uniform1i(shaderProgram.samplerUniform, 0);
		
		gl.bindBuffer(gl.ARRAY_BUFFER, cubeVertexNormalBuffer);
		gl.vertexAttribPointer(shaderProgram.vertexNormalAttribute, cubeVertexNormalBuffer.itemSize, gl.FLOAT, false, 0, 0);
		
		gl.uniform3f(shaderProgram.ambientColorUniform, 0.2, 0.2, 0.2);

        lightingDirection = new okVec3(-0.25, -0.25, -1.0);
        
        adjustedLD = lightingDirection.normalize(false);
        adjustedLD = okVec3MulVal(adjustedLD, -1.0);
        gl.uniform3fv(shaderProgram.lightingDirectionUniform, adjustedLD.toArray());

		lightWeight = parseInt($('#lightSensor').val()) / 10;
        gl.uniform3f(shaderProgram.directionalColorUniform, lightWeight, lightWeight, lightWeight);
		
		gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, cubeVertexIndexBuffer);
		setMatrixUniforms();
		gl.drawElements(gl.TRIANGLES, cubeVertexIndexBuffer.numItems, gl.UNSIGNED_SHORT, 0);
		
	}

	function setMatrixUniforms() {
		gl.uniformMatrix4fv(shaderProgram.pMatrixUniform, false, pMatrix.toArray());
		gl.uniformMatrix4fv(shaderProgram.mvMatrixUniform, false, mvMatrix.toArray());
		
		var normalMatrix = mvMatrix.inverse().transpose();
		gl.uniformMatrix4fv(shaderProgram.nMatrixUniform, false, normalMatrix.toArray());
	}
	
	function animate() {
		if(xRot < xRotTarget) {
			xRot++;
		} else if(xRot > xRotTarget) {
			xRot--;
		}
		if(yRot < yRotTarget) {
			yRot++;
		} else if(yRot > yRotTarget) {
			yRot--;
		}
		if(zRot < zRotTarget) {
			zRot++;
		} else if(zRot > zRotTarget) {
			zRot--;
		}
	}
	
	function bindBtns() {
		$('#xRotDown').live('click', function() {
			xRotTarget -= rotAngle;
			sendRotAngle();
		})
		$('#xRotUp').live('click', function() {
			xRotTarget += rotAngle;
			sendRotAngle();
		})
		$('#yRotDown').live('click', function() {
			yRotTarget -= rotAngle;
			sendRotAngle();
		})
		$('#yRotUp').live('click', function() {
			yRotTarget += rotAngle;
			sendRotAngle();
		})
		$('#zRotDown').live('click', function() {
			zRotTarget -= rotAngle;
			sendRotAngle();
		})
		$('#zRotUp').live('click', function() {
			zRotTarget += rotAngle;
			sendRotAngle();
		})
		$('#portrait').live('click', function() {
			//-moz-transform:rotate(-90deg); -webkit-transform:rotate(-90deg);
			xRot = 90;
			yRot = 0;
			zRot = 0;
			xRotTarget = 90;
			yRotTarget = 0;
			zRotTarget = 0;
			sendRotAngle();
			/*
			$('#canvasContainer').css('-moz-transform', 'rotate(0deg)');
			$('#canvasContainer').css('-webkit-transform', 'rotate(0deg)');*/
		})
		$('#landscape').live('click', function() {
			xRot = 0;
			yRot = -90;
			zRot = 90;
			xRotTarget = 0;
			yRotTarget = -90;
			zRotTarget = 90;
			sendRotAngle();
			/*
			$('#canvasContainer').css('-moz-transform', 'rotate(-90deg)');
			$('#canvasContainer').css('-webkit-transform', 'rotate(-90deg)');*/
		})
		$('#lightSensor').live('change', function() {
			var lightDegree = parseInt($(this).val()) / 8 * 10000,
				cmd = 'sensor set light ' + lightDegree;
			websocket.send(cmd);
		})
		/*
		$(document).keydown(function(e) {
			if(! $('#sensorViewport').is(':visible')) {
				return;
			}
			//e.preventDefault(); 
			switch(e.keyCode) {
				case 38:
					$('#xRotDown').click();
					break;
				case 40:
					$('#xRotUp').click();
					break;
				case 37:
					$('#yRotDown').click();
					break;
				case 39:
					$('#yRotUp').click();
					break;
				case 188:
					$('#zRotUp').click();
					break;
				case 190:
					$('#zRotDown').click();
					break;
				default:
					break;
			}
		})*/
	}
	
	function sendRotAngle() {
		
		var cmd = "sensor set orientation " + (360 - zRotTarget) + ":" + (-xRotTarget) + ":" + (-yRotTarget);
		websocket.send(cmd);
	}
	
	function calculateCircleAngle(angle) {
		return (angle = angle % 360) > 0 ? angle : angle + 360;
	}
}

function CDView(externalIp, phoneId) { // class of phone screen show

	var novnc,
		options = {'host': externalIp, 'path': 'websockify/' + phoneId + '/' + $('#userId').val() },
		
		_construct = (function() { //æž„é€ CDViewç±»
			novnc = new CDNovnc(options);
			bindmBtns();
			initCapture();
		})();
	
	this.connect = function() {
		novnc.connect();
		CDView.relocate();
	}
	
	this.disconnect = function() {
		novnc.rfb.disconnect();
	}
	
	function bindmBtns() {
		
		$('#mBackBtn').click(function() {
			novnc.rfb.sendKey(0xFFC1); //ä¸Žè¶…å“¥çº¦å®šçš„é”®å€¼
		})
		$('#mMenuBtn').click(function() {
			novnc.rfb.sendKey(0xFFBF);
		})
		$('#mHomeBtn').click(function() {
			novnc.rfb.sendKey(0xFFBE);
		})
		$('#mSearchBtn').click(function() {
			novnc.rfb.sendKey(0xFFC0);
		})
	}
	
	function initCapture() {
		$('#phoneScreen').mouseenter(function() {
			novnc.rfb.get_mouse().set_focused(true);
			novnc.rfb.get_keyboard().set_focused(true);
		}).mouseleave(function(e) {
			novnc.rfb.get_keyboard().set_focused(false);
			novnc.rfb.get_mouse().set_focused(false);
			//novnc.rfb.sendMouse(e, 1);
			//novnc.rfb.sendMouse(e, 0);
		});
		if($('#res').val() == '480x800') {
			$('#phoneScreen').width(480);
			$('#phoneScreen').height(800);
			$('#recommandContainer').height(852);
		}	
	}
}

CDView.relocate = function() { //ä½¿æ‰‹æœºå±å¹•åœ¨å®¹å™¨å†…å±…ä¸­, è®¾ç½®æˆé™æ€æ–¹æ³•ä½¿å…¶ä»–ç±»å¯ä»¥è°ƒç”¨
	if($('#phoneScreen').width() < $('#screenContainer').width()) {
		$('#phoneScreen').css('margin-left', ($('#screenContainer').width() - $('#phoneScreen').width()) / 2 + 'px');
	} else {
		$('#canvasScreen').css('margin-left', 0);
	}
	if($('#phoneScreen').height() < $('#screenContainer').height()) {
		$('#phoneScreen').css('margin-top', ($('#screenContainer').height() - $('#phoneScreen').height()) / 2 + 'px');
	} else {
		$('#phoneScreen').css('margin-top', 0);
	}
}

function CDCmd(externalIp, phoneId) { // class of sending command message
	
	var isInstallSuccess = false,
		installOptions = {
			beforeSend: 	 beforeSendHandler,
			uploadProgress:  uploadProgressHandler,
			complete:	     completeHandler,
			clearForm: true 
		},
		installStat = {},
		websocket = new CDWebSocket(msgCallback),
	
		_construct = (function() {
			bindBtns();
		})();
		
	this.connect = function() {
		websocket.connect(externalIp, '9001', 'adbshell/' + phoneId);
		initLogId();
	}
	
	this.disconnect = function() {
		websocket.close();
	}
	
	function initLogId() {
		var logId,
			cmd,
			url;
		if(sockets.novncStatus != 'normal') {
			setTimeout(initLogId, 1000);
		} else {
			logId = $("#logId").val();
        	cmd = "control_id:" + logId + '\n';
        	websocket.send(base64encode(cmd)); 
			globalVars.setErrNum(1);
		}
	}
	
	function bindBtns() {
		bindInstallApp();
		bindExportScreen();
		bindInstallRecommandApp();
	}
	
	function bindInstallApp() {
		$('#installApp').click(function() {
			if(checkInstallStat()) {
				$('#file').click();
			}
		})
		
		$('#file').change(function(){	
			var filename = $('#file').val(),
				tmpFilename,
				suffix; 
			if(filename){
				tmpFilename = filename.split('\\');
				filename = tmpFilename[tmpFilename.length - 1];
				tmpFilename = filename.split(".");
				suffix = tmpFilename[tmpFilename.length - 1];
				if(suffix == 'apk') {
					UploadFile(filename);
				} else {
					alert('è¯·é€‰æ‹©æ­£ç¡®çš„APPæ–‡ä»¶');
				}
			}
		});

		$('#installForm').ajaxForm(installOptions);
	}
	
	function bindExportScreen() {
		$('#exportScreen').click(function() {
			exportImg();
		})
	}
	
	function bindInstallRecommandApp() {
		$('.recommandApp').click(function() {
			if(checkInstallStat()) {
				var url = $(this).attr('url');
				$("#appurl").val(url);
				$('#installApp').addClass('disabled');
				installStat.dot = 0;
				installStat.timer = setInterval(setInstallMsg, 1000); //1ç§’ä¸€æ¬¡
				sendInstallCmd();
			}
		})
	}
	
	function exportImg() {
		 var canvas = document.getElementById('phoneScreen'),
		 	 dataURL = canvas.toDataURL('png').replace('image/png', 'image/octet-stream'),
		 	 filename = 'mtc_' + (new Date()).getTime() + '.png';
		 
		 downloadFile(dataURL, filename);
	};
	
	function downloadFile(data, filename) {
		var link = document.createElementNS('http://www.w3.org/1999/xhtml', 'a'), //åˆ›å»ºå…·æœ‰æ ‡å‡†å‘½åç©ºé—´çš„aæ ‡ç­¾
			event = document.createEvent('MouseEvents');
		link.href = data;
		link.download = filename;
		
		event.initMouseEvent('click', true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
		link.dispatchEvent(event);
	}
	
	/**
	 * æ–‡ä»¶ä¸Šä¼ å‡½æ•°
	 */
	function UploadFile(filename) {
		
		var url = "./?pname=cloudDebugCtrl&action=genSign",
			data = {
				filename: filename,
				method: 'POST'
			},
			callback;
			
		callback = function(msg){			
			var url;			
			msg = $.parseJSON(msg);
			if(msg) {
				if(msg.status == 0){
					url = msg.content;
					installOptions.url = url;
					$('#installForm').submit();
				} else {
					alert('ä¸Šä¼ å‡ºé”™:' + msg.content);
					//updateMsg(imsg,'å®‰è£…å¤±è´¥', 2);
				}
			} else {
				alert('æœªçŸ¥é”™è¯¯');
				//updateMsg(imsg,'å®‰è£…å¤±è´¥', 2);
			}
			$("#appurl").val(msg.content);
			$("#appfile").val(msg.file);
		}
		
		jQuery.ajax({
			type: 'GET',
			url: url,
			data: data,
			success: callback,
			error: function(xhr) {
				if (xhr.status != 201 && xhr.status != 200) {
					alert('èŽ·å–ä¸Šä¼ Keyå¤±è´¥,è¯·é‡è¯• error:' + xhr.responseText);
					//updateMsg(imsg,'å®‰è£…å¤±è´¥', 2);
				}	
			}
		});
	}
	
	function beforeSendHandler() {
		$('#installApp').addClass('disabled');
		installStat.dot = 0;
		installStat.timer = setInterval(setInstallMsg, 1000); //1ç§’ä¸€æ¬¡
	}
	
	function uploadProgressHandler(event, position, total, percentComplete) {
		
	}
	
	function completeHandler(xhr) {
		sendInstallCmd();
	}
	
	function setInstallMsg() {
		var i,
			msg = 'APPå®‰è£…ä¸­';
		for(i = 0; i < installStat.dot % 3 + 1; i++) {
			msg += '.';
		}
		$('#installApp').html(msg);
		installStat.dot++;
	}
	
	function setInstallRes() {
		clearInterval(installStat.timer);
		if(isInstallSuccess) {
			$('#installApp').html('å®‰è£…æˆåŠŸ');
		} else if($('#installApp').hasClass('disabled')) { //è¶…æ—¶å®‰è£…
			$('#installApp').html('å®‰è£…å¤±è´¥');
		}
		$('#installApp').removeClass('disabled');
		setTimeout(function() {
			$('#installApp').html('å®‰è£…APP');
		}, 10000);
	}
	
	function checkInstallStat() {
		if($('#installApp').hasClass('disabled')) {
			alert('æœ‰å…¶å®ƒåº”ç”¨æ­£åœ¨å®‰è£…ä¸­ï¼Œè¯·ç¨åŽ...');
			return false;
		}
		return true;
	}
	
	/*
	 * å„ç±»æŽ§åˆ¶å‘½ä»¤é€šä¿¡å‡½æ•°
	 */
	
	function sendInstallCmd() {
		var msg = 'install';
		isInstallSuccess = false;
        msg += ' ' + $("#appurl").val();
        websocket.send(base64encode(msg));
        setTimeout(setInstallRes, 180000); //å¦‚æžœ3åˆ†é’Ÿæ²¡æœ‰è¿”å›žç»“æžœï¼Œè®¤ä¸ºå®‰è£…å¤±è´¥
	}
	
	/*
	 * å›žè°ƒå‡½æ•°
	 */
	function msgCallback(msg) {
		
		var res = base64decode(msg.data);
		Util.Debug('received:' + res);
		
		if(res == "installCompleted"){
			isInstallSuccess = true;
			setInstallRes();
		} 
	};
}

function CDLog(externalIp, phoneId) { // class of recieving logs
	var websocket = new CDWebSocket(msgCallback),
		logs = new Queue(1000),
		lastLog,
		refreshTimer,
		filter,
	
		_construct = (function() {
			setFilter();
			refreshTimer = setInterval(refreshLog, 1000);
		})();
	
	this.connect = function() {
		websocket.connect(externalIp, '9001', 'adblogcat/' + phoneId);
	}
	
	this.disconnect = function() {
		websocket.close();
	}
	
	function refreshLog(flag) {
		if(!flag && lastLog && lastLog['content'] == logs.elements[0].content) {
			return; //å‡å°‘æ¸²æŸ“åˆ·æ–°ï¼Œå¦‚æžœæ—¥å¿—æ²¡æœ‰å˜åŒ–ï¼Œä¸åšæ¸²æŸ“
		}
		var table = '<table>',
			logCount = logs.elements.length,
			i,
			log;
		for(i = 0; i < logCount; i++) {
			log = logs.elements[i];
			if(filter && log['content'].toUpperCase().indexOf(filter) == -1) {
				continue;
			}
			log['label'] = log['label'].length > 20 ? log['label'].slice(0, 17) + '...' : log['label'];
			table += "<tr class='logs type" + log['type'] + "'>" +
					 "<td class='logTime'>" + log['time'] + "</td>" +
					 "<td class='logType'>" + log['type'] + "</td>" +
					 "<td class='logPid'>" + log['pid'] + "</td>" +
					 "<td class='logLabel'>" + log['label'] + "</td>" +
					 "<td class='logContentFlex'>" + log['content'] + "</td></tr>";
		}
		table += '</table>';
		$('#logContent').html(table);
		refreshLogByType();
		lastLog = logs.elements[0];
	}
	
	function handleLog(log) {
		var logPattern = /^([EWIDV])\/(.+?)\((.+?)\)\:(.+)$/i;
		log = log.match(logPattern);
		if(log) {
			return {
				time: getFormatTime(),
				type: $.trim(log[1]),
				label: $.trim(log[2]),
				pid: $.trim(log[3]),
				content: $.trim(log[4])
			}
		} else {
			return null;
		}
	}
	
	function getFormatTime() {
		var now = new Date(),
			year = now.getFullYear(),
			month = now.getMonth() + 1, 
			day = now.getDate(),
			hour = now.getHours(), 
			minute = now.getMinutes(), 
			second = now.getSeconds();
			
		return year + '-' + month + '-' + day + ' ' + hour + ':' + minute + ':' + second; 
	}
	
	function setFilter() {
		$('#logSearchKey').click(function() {
			if($.trim($(this).val()) == "" || $(this).val() == $(this).attr('default')) {
				$(this).val("");
			}
		}).blur(function() {
			if($.trim($(this).val()) == "") {
				$(this).val($(this).attr('default'))
			}
		}).keyup(function() {
			if($.trim($(this).val()) == "" || $(this).val() == $(this).attr('default')) {
				filter = null;
			} else {
				filter = $.trim($(this).val()).toUpperCase();
			}
			refreshLog(true);
		})
		
		$('#logTypeFolder').mouseenter(function() {
			$('#logTypeIcon').attr('src', './img/testJobRun/logTypeExpand.png');
			$('#logTypeSel').show();
		}).mouseleave(function() {
			$('#logTypeIcon').attr('src', './img/testJobRun/logTypeShrink.png');
			$('#logTypeSel').hide();
		})
		
		$('.logTypeOpt').click(function() {
			$('.logTypeSelected').removeClass('logTypeSelected');
			$(this).addClass('logTypeSelected');
			refreshLog(true);
		})
	}
	
	function refreshLogByType() {
		var type = $('.logTypeSelected').attr('value');
		if(type == 'A') {
			//$('.logTypeOpt').removeClass('hidden');
			$('.logs').show();
		} else {
			$('.logs').hide();
			$('.type' + type).show();
		}
	}
	/*
	 * å›žè°ƒå‡½æ•°
	 */
	function msgCallback(msg) {
		var res = base64decode(msg.data),
			log;
		
		log = handleLog(res);
		if(log) {
			logs.enQueue(log);
		}
	};
}

function CDSensor(externalIp, phoneId) { // class of sending sensor command
	var websocket = new CDWebSocket(msgCallback),
		webgl = new CDWebgl(websocket),
	
		_construct = (function() {
			bindBtns();
			initGpsMap();
			initAudio();
		})();
	
	this.connect = function() {
		websocket.connect(externalIp, '9001', 'command/' + phoneId);
	}
	
	this.disconnect = function() {
		websocket.close();
	}
	
	function bindBtns() {
		bindShakeScreen();
		bindShrinkxExpand();
		bindRecMsg();
		bindRecCall();
		bindSensorEntrance();
		bindSensorBack();
	}
	
	function bindShakeScreen() {
		$('#shakeScreen').click(function() {
			websocket.send('shake');
		})
	}
	
	function bindShrinkxExpand() {
		var target;
		$('.shrink').live('click', function() {
			target = '#' + $(this).attr('id') + 'Target';
			$('.enfoldment:visible').slideToggle();
			$('.expand').removeClass('expand').addClass('shrink');
			$(this).removeClass('shrink').addClass('expand');
			$(target).slideToggle();
		})
		$('.expand').live('click', function() {
			target = '#' + $(this).attr('id') + 'Target';
			$(target).slideToggle();
			$(this).removeClass('expand').addClass('shrink');
		})
	}
	
	function bindRecMsg() {
		$('#recMsgBtn').live('click', function() {
			var cmd,
			num = $('#msgNum').val(),
			msg = $('#msgContent').val(),
			errmsg = [];
			if(!/^\d{11}$/.test(num)) {
				errmsg.push('å‘ä»¶äººå·ç ');
			}
			if(msg == '') {
				errmsg.push('çŸ­ä¿¡å†…å®¹');
			}
			if(errmsg.length) {
				errmsg = errmsg.join('ã€') + 'è¾“å…¥æœ‰è¯¯';
				alert(errmsg);
			} else {
				cmd = 'sms send ' + num + ' ' + msg;
				websocket.send(cmd);	
			}
		})
	}
	
	function bindRecCall() {
		var cmd,
			num;
		$('#recCallSendBtn').live('click', function() {
			num = $('#callNum').val();
			if(!isPhoneNum(num)) {
				alert('æ¥ç”µå·ç è¾“å…¥æœ‰è¯¯');
			} else {
				num = num.split('-').join('');
				$('#calledNum').val(num);
				$(this).hide();
				$('#recCallCancelBtn').show();
				cmd = 'gsm call ' + num;
				websocket.send(cmd);	
			}
		})
		$('#recCallCancelBtn').live('click', function() {
			num = $('#calledNum').val();
			$('#calledNum').val(null);
			$(this).hide();
			$('#recCallSendBtn').show();
			cmd = 'gsm cancel ' + num;
			websocket.send(cmd);
		})
	}
	
	function initGpsMap() {
		var map = new BMap.Map("gpsMap", {'enableMapClick': false}),
			geocoder = new BMap.Geocoder(),  
			point = new BMap.Point();
		
		map.setZoom(15);
		map.enableScrollWheelZoom();
		geocoder.getPoint('ç™¾åº¦å¤§åŽ¦', initCallback);  
		
		map.addEventListener("click", function(e){ 	
			setPoint(e.point.lng, e.point.lat);
			map.panTo(point);
			geocoder.getLocation(point, getLocationCallback);		   
		});
		
		$('#gpsLocation').live('blur', function() {
			geocoder.getPoint($(this).val(), getPointCallback, $('#gpsCity').val() + 'å¸‚');
		}).live('keydown', function(e) {
			if(e.keyCode == 13) {
				geocoder.getPoint($(this).val(), getPointCallback, $('#gpsCity').val() + 'å¸‚');	
			}
		})
		
		function initCallback(res) {
			setPoint(res.lng, res.lat);	
			map.centerAndZoom(point, map.getZoom()); 
		}     
		
		function getPointCallback(res) {
			//todo
			if(!res) {
				showGpsStatus('error');
			} else {
				setPoint(res.lng, res.lat);	 
				map.panTo(point);  
			}                   
		}
		
		function getLocationCallback(res) {
			if(!res) {
				$('#gpsLocation').val('æœªçŸ¥åœ°ç‚¹');  
			} else {
				$('#gpsLocation').val(res.address);  
			}
		}    
		
		function setPoint(lng, lat) {
			$('#gpsLongitude').html(lng);
			$('#gpsLatitude').html(lat);
			point.lng = lng;
			point.lat = lat;
			locateGps();
		}
		
		function locateGps() {
			var cmd = "geo fix " + point.lng + " " + point.lat;
			websocket.send(cmd);
		}
	}
	
	function bindSensorEntrance() {
		$('#sensorEntrance').live('click', function() {
			$('#ctrlxRecommandContainer').hide();
			$('#sensorContainer').show();
		})
	}
	
	function bindSensorBack() {
		$('#sensorBack').live('click', function() {
			$('#sensorContainer').hide();
			$('#ctrlxRecommandContainer').show();
		})
	}
	
	function isPhoneNum(num) {
		var reg = /^(\d{11}|(\d{3,4}\-)?\d{7,8})$/;
		if(reg.test(num)) {
			return true;
		} else {
			return false;
		}
	}
	
	function initAudio() {
		var audioId = '2' + (phoneId.split('-'))[1],
		
        	audioSwf = '<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" width="100%" height="1px" id="AudioPlayer">'
	            + '<param name="movie" value="./static/flash/AudioPlayer.swf?r=[__version__]" />'
	            + '<param name="quality" value="high" />'
	            + '<param name="bgcolor" value="#ffffff" />'
	            + '<param name="allowScriptAccess" value="sameDomain" />'
	            + '<param name="allowFullScreen" value="true" />'
	            + '<param name="flashvars" value="audiourl=http://' + externalIp + ':9002/' + audioId + '.mp3" />'          
	            + '<!--[if !IE]>-->'
	            + '<object type="application/x-shockwave-flash" data="./static/flash/AudioPlayer.swf?r=[__version__]" width="100%" height="1px">'
	            + '<param name="quality" value="high" />'
	            + '<param name="bgcolor" value="#ffffff" />'
	            + '<param name="allowScriptAccess" value="sameDomain" />'
	            + '<param name="allowFullScreen" value="true" />'
	            + '<param name="flashvars" value="audiourl=http://' + externalIp + ':9002/' + audioId + '.mp3" />'  
	            + '</object>'
	            + '<!--<![endif]-->'
	            + '</object>';
        $("#audioContainer").empty().html(audioSwf);  
    }

	
	/*
	 * å›žè°ƒå‡½æ•°
	 */
	function msgCallback(msg) {
		
		var res = msg.data;
		if(res && res.indexOf('shake') != -1) {
			showShakeSucc();		
		}
		
		if(res && res.indexOf('geo fix') != -1) {
			showGpsStatus('success');
		}
		
	} 
	
	/*
	 * ä¸€äº›æ˜¾ç¤ºé€»è¾‘å‡½æ•°
	 */
	function showShakeSucc() {
		$('#shakeTag').show();
		$('#shakeTag').fadeOut(3000);
	}
	
	function showGpsStatus(status) {
		$('#gpsStatus').attr('src', './img/cloudDebug/' + status + '.png');
		$('#gpsStatus').show();
		if(status == 'success') {
			if($('#gpsStatus').is(':visible')) {
				$('#gpsStatus').fadeOut(2000);
			} else {
				$('#gpsStatus').hide();
			}
		}
	}
	
}

function CloudDebugCtrl() { //class of the whole page
	
	var externalIp = $('#externalIp').val(),
		phoneId = $('#phoneId').val(),
		cdView = new CDView(externalIp, phoneId),
		cdCmd = new CDCmd(externalIp, phoneId),
		cdLog = new CDLog(externalIp, phoneId),
		cdSensor = new CDSensor(externalIp, phoneId),
		timer,
		remainingTime = 30,
		
		_construct = (function() {
			bindExitCD();
		})();
		
	this.connect = function() {
		cdView.connect();
		cdCmd.connect();
		cdLog.connect();
		cdSensor.connect();
		setTimer();	
	}
	/*
	function connectWebsockets() {
		if(sockets.novncStatus != 'normal') {
			setTimeout(connectWebsockets, 1000);
		} else {
			cdCmd.connect();
			cdLog.connect();
			cdSensor.connect();
			setTimer();		
		}
	}*/
	
	function disconnect() {
		cdView.disconnect();
		cdCmd.disconnect();
		cdLog.disconnect();
		cdSensor.disconnect();
		clearTimer();
		window.location.href = "./?pname=cloudDebug";
	}
	
	function setTimer() {
		timer = setInterval(setRemainingTime, 60000);
	}
	
	function clearTimer() {
		clearInterval(timer);
	}
	
	function setRemainingTime() {
		remainingTime--;
		if(remainingTime > 0) {
			$('#remainingTime').html(remainingTime + 'åˆ†é’Ÿ');
		} else {
			disconnect();
			alert('æ‚¨çš„æ—¶é—´å·²ç”¨å°½ï¼Œè°¢è°¢ä½¿ç”¨ã€‚');
		}
	}
	
	function bindExitCD() {
		$('#exitCD').click(function() {
			disconnect();
		})
	}
		
}

$(document).ready(function() {
	var page = new CloudDebugCtrl();
	globalVars.logId = parseInt($('#logId').val());
	page.connect();
})
