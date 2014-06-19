
//$(document).ready(function(){
//	$("#mail0").click(function(){
//		var link_str = "./mail";
//		var mail0 = $("#mail0").val();
//		$.post(link_str,
//				{
//					name: mail0
//				},
//				function(results){
//					//alert("results: " + results );
//				});
//	});
//});
	
function mail_link(pageno) { //v3.0
	$.ajax( {
			type : "POST"
		,	url  : "./mails"
		,	dataType : "json"
		,	data : ""
		,	success : function (result) {
						$.each(result, function(key) {
							var list = result[key];
							alert("length = " + list.length);
						});
					}
	}
	);
}

function schedule_link(pageno) { //v3.0
	var link_str = "./schedule";
	location.href = link_str;
}

function work_link(pageno) { //v3.0
	var link_str = "./work";
	location.href = link_str;
}

function infor_link(pageno) { //v3.0
	var link_str = "./infor";
	location.href = link_str;
}

function notice_link(pageno) { //v3.0
	var link_str = "./notice";
	location.href = link_str;
}