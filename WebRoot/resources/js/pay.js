var host = location.protocol + "//" + location.host + "/apptry/";

function pay(url) {
	$.ajax({
		url: url,
		dataType: "text",
		success: function(data) {
			if(data != "") {
				window.open(data);
			}
		},
		error: function(data) {alert('服务器异常，请稍候重试.');}
	});
}

function continuePay(orderId) {
	var url = host + "order/continue/" + orderId + "/";
	pay(url);
}

function cancelOrder(orderId) {
	updateOrder(orderId, 3);
}

function updateOrder(orderId, newState) {
	var url = host + "order/update/" + orderId + "/" + newState + "/";
	$.ajax({
		url: url,
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