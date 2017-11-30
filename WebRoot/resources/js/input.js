var LONG_PRESS_INTERVAL = 800; // 800ms
var pos1, pos2, time1, time2, g_width = 320;

// press operation
function pressDown() { //time = 0; 
	time1 = new Date().getTime(); 
}

function pressUp(t) {
	time2 = new Date().getTime();
	if(isLongPress(time1, time2)) execute("PRESS " + t.id.toUpperCase() + " DOWN");
	else execute("PRESS " + t.id.toUpperCase() + " DOWN_AND_UP");
}

function press(keyName) {
	execute("PRESS " + keyName);
}

// scroll screen
function start(e) {
	pos1 = getXY(e); time1 = new Date().getTime();
}

function finish(e) {
	pos2 = getXY(e); time2 = new Date().getTime();
	var cmd;
	if(isNearby(pos1, pos2)) {
		cmd = "TOUCH " + pos1.x + " " + pos1.y;
		if(isLongPress(time1, time2)) {
			//long press
			cmd += " DOWN";
		} else {
			//short press
			cmd += " DOWN_AND_UP";
		}
	} else {
		//move
		var steps = 10, ms = 1000;
		cmd = "DRAG " + pos1.x + " " + pos1.y + " " + pos2.x + " " + pos2.y + " " + steps + " " + ms;
	}
	execute(cmd);
}

function isNearby(p1, p2) {
	if(Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2)) > 10) return false;
	return true;
}

function isLongPress(t1, t2) {
	if((t2 - t1) > LONG_PRESS_INTERVAL) return true;
	return false;
}

function realPosition(x) {
	return Math.floor(x * (g_width / 330));
}

/**
used for rfb.js*/
function executeApptryCheck() {
	executeHandler("CHECK " + packageName);
}

function execute(command) {
	var action = command.substring(0, command.indexOf(" "));
	executeHandler(command);
	if(action == "CHECK" || action != "START") {
		executeHandler("CHECK " + packageName);
	}
}

var flag = false;
function executeHandler(command) {
	$.ajax({
		url: "execute/",
//		async: false,
		data: "command=" + command,
		type: "POST",
		dataType: "json",
		success: function(data) {
			switch(data.result) {
				case "PASS":
					if(!flag) {
						flag = false;
					}
					break;
				case "NOT_PASS":
					if(flag) {
						bye();
					}
					flag = true;
					break;
			}
		}
	});
}

function getXY(e) {
	e = window.event || e;
	var x = (e.offsetX === undefined) ? getOffset(e).offsetX : e.offsetX;
	var y = (e.offsetY === undefined) ? getOffset(e).offsetY : e.offsetY;
	x = realPosition(x);
	y = realPosition(y);
	var pos = {"x":x, "y":y};
	return pos;
}

function getOffset(e) {
	var target = e.target;
	if (target.offsetLeft === undefined) {
		target = target.parentNode;
	}
	var pageCoord = getPageCoord(target);
	var eventCoord = {x:window.pageXOffset + e.clientX, y:window.pageYOffset + e.clientY};
	var offset = {offsetX:eventCoord.x - pageCoord.x, offsetY:eventCoord.y - pageCoord.y};
	return offset;
}

function getPageCoord(element) {
	var coord = {x:0, y:0};
	while (element) {
		coord.x += element.offsetLeft;
		coord.y += element.offsetTop;
		element = element.offsetParent;
	}
	return coord;
}
//End
var keyName;
function keydown(e) {
	var e = e || event;
	var currKey = e.keyCode || e.which || e.charCode;
	if((currKey > 7 && currKey < 14) || (currKey > 31 && currKey < 47)) {
　 　	switch(currKey) {
    　 　	case 8:	keyName = "";	press("DEL"); 			break;
//    　 　 　 case 9: keyName = "[Tab]"; break;
    　 　 　 case 13:keyName = ""; 	press("ENTER"); 		break;
    　 　 　 case 32:keyName = ""; 	press("SPACE"); 		break;
//    　 　 　 case 33:keyName = "[PageUp]";   break;
//    　 　 　 case 34:keyName = "[PageDown]";   break;
//    　 　 　 case 35:keyName = "[End]";   break;
//    　 　 　 case 36:keyName = "[Home]";   break;
    　 　 　 case 37:keyName = ""; 	press("LEFT"); 			break;
    　 　 　 case 38:keyName = ""; 	press("UP"); 			break;
    　 　 　 case 39:keyName = "";	press("RIGHT");			break;
   　 　 　  case 40:keyName = ""; 	press("DOWN"); 			break;
//    　 　 　 case 46:keyName = ""; 	press("FORWARD_DEL");	break;
    　 　	default:keyName = ""; 	break;
		}
	}
	switch(currKey) {
		case 27: 	keyName = "";	press("BACK"); 	break;
		case 46: 	keyName = "."; 					break;
		default:	keyName = ""; 					break;
	}
}

function keypress(e) {
	var currKey = 0, CapsLock = 0, e = e || event;
	currKey = e.keyCode || e.which || e.charCode;
	CapsLock = currKey >= 65 && currKey <= 90;
	switch(currKey) {
		//屏蔽了退格、制表、回车、空格、方向键、删除键
		case 8: case 13:case 32:case 37:case 38:case 39:case 40:/*case 9:case 46:*/keyName = "";break;
		default:keyName = String.fromCharCode(currKey); break;
	}
}

function keyup(e) {
	if(keyName) {
		typeAction(keyName);
		keyName = "";
	}
}


// type
function typeAction(key) {
//	alert(rfb.keyboard.getKeysymSpecial(e));
	var cmd = "TYPE " + key;
	execute(cmd);
}

function shell(cmd) {
	var cmd = "SHELL " + cmd;
	execute(cmd);
}

document.onkeypress=keypress;
document.onkeydown =keydown;
document.onkeyup =keyup;