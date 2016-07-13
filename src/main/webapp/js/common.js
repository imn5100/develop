function addCommonParameter(param) {
	var mydata = '{"sessionId":"' + $('#sessionId').val() + '","version":"RMB_1.0","appKey":"54b1304dd75c4d89a97a4241563ab4bc","timestamp":"","sign":"","systemtype":"2"';

	if (param != null && param != "") {
		mydata += "," + param;
	}

	mydata += '}';

	return mydata;
}

function doSubmit(connect, mydata) {
	mydata = addCommonParameter(mydata);	
	$.ajax({
		type : 'POST',
		contentType : 'application/json',
		url : '/serviceCenter/api/' + connect,
		processData : false,
		dataType : 'text',
		data : mydata,
		success : function(data) {
			$("#dvReturnContent").remove();
			$("body").append("<div id='dvReturnContent' style='word-break:break-all;'></br>参数为：" + mydata + "</br></br>返回结果：</br>" + data + "</div>");
		},
		error : function() {
			$("#dvReturnContent").remove();
			$("body").append("<div id='dvReturnContent' style='word-break:break-all;'></br></br>参数为：" + mydata + "</br></br>返回结果：失败</div>");
		}
	});
}

var bodyWidth = 3000;
var bodyHeight =3000;
//显示遮罩，禁止页面操作
function startWait(){
	$("<div class='wrap'</div>").appendTo("body");$(".wrap").width(bodyWidth);$(".wrap").height(bodyHeight);
}
//移除遮罩
function endWait(){
	$(".wrap").remove();
}