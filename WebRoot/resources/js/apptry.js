var TRIAL_TIME = 120; // 120s
var alreadyCostTime = 0;
function countTimer() {
//	window.setTimeout(bye, TRIAL_TIME);
	if(alreadyCostTime < TRIAL_TIME) {
		alreadyCostTime++;
		var lefttime = TRIAL_TIME - alreadyCostTime;
		$(".lefttime").text(lefttime);
		window.setTimeout(countTimer, 1000);
	} else {
		bye();
	}
}

// force phone desktop back to home page
function bye() {
	$.ajax({
		url: "logout",
		async: false,
		dataType: "json",
		success: function() {
			history.back();
//			if(self.location.hash.indexOf("#") != -1) {
//				location.href = location.href.substr(0, location.href.indexOf("#"));
//				location.reload();
//			}
		}
	});
}