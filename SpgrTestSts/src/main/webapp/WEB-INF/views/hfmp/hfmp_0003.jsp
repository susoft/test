<%@page session="false" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>교류회 조회</title>
<link rel="stylesheet" href="http://code.jquery.com/mobile/1.4.2/jquery.mobile-1.4.2.min.css">
<script src="http://code.jquery.com/jquery-1.10.2.min.js"></script>
<script src="http://code.jquery.com/mobile/1.4.2/jquery.mobile-1.4.2.min.js"></script>

<script type="text/javascript">
$(document).on("pagecreate","#pageone",function(){
   $(document).scroll(function() {
		console.log("► start of scroll");
		
		var documentHeight = $(document).height();
	    var scrollDifference = $(window).height() + $(window).scrollTop();
	    
	    //console.log(documentHeight + "-" +  scrollDifference);
	    
	    if (documentHeight == scrollDifference) {
	    	console.log("► End of scroll");
	    }
	});
});

function fnClick(meetingCd) {
	document.frm.meetingCd.value = meetingCd;
	document.frm.submit();
}

function choosePopup(meetingCd, meetingNm) {
	meetingCd_temp = meetingCd;
	meetingNm_temp = meetingNm;
	
	$("#meetingNm").val(meetingNm_temp);
	$("#meetingCd").val(meetingCd_temp);
	
	$("#popupMenu").popup();
	$('#popupMenu').popup('open');
}

function dialogModifyMeeting() {
	
	console.log("dialogModifyMeeting");
	
	$.mobile.changePage( "#dialogModify", { role: "dialog" } );
	
	// Dialog closed 
	$('#dialogModify').on("pagehide", function() {
		console.log($("#meetingCd").val());
		console.log($("#meetingNm").val());
		
		modifyMeeting();
	});
}

function modifyMeeting() {
	var meetingCd = $("#meetingCd").val();
	var meetingNm = $("#meetingNm").val();
	
	$.ajax( {
		type : "POST"
	,	url  : "modifyMeeting.do"
	,	dataType : "json"
	,	data : {'meetingCd': meetingCd, 'meetingNm': meetingNm}
	,	success : function (result) {
			console.log(result);
			
			if (result.result == "1") {
				$("#resultMessage").html('정삭적으로 수정되었습니다.');
			} else {
				$("#resultMessage").html('오류가 발생하였습니다.');
			}
			$.mobile.changePage( "#dialogResult", { role: "page" } );
		}
	}
	);
}

function deleteMeeting() {
	//$('#popupMenu').popup('close');
	
	var meetingCd = meetingCd_temp;
	console.log(meetingCd);
	
	$.ajax( {
		type : "POST"
	,	url  : "deleteMeeting.do"
	,	dataType : "json"
	,	data : {'meetingCd': meetingCd}
	,	success : function (result) {
			console.log(result);
			
			if (result.result == "2") {
				$("#resultMessage").html('정삭적으로 삭제되었습니다.');
			} else {
				$("#resultMessage").html('오류가 발생하였습니다.');
			}
			$.mobile.changePage( "#dialogResult", { role: "diolog" } );
		}
	}
	);
}

var meetingCd_temp;
var meetingNm_temp;

</script>
</head>

<body>
<form name="frm" action="getHfmp0004.do" method="post">
<input type=hidden name="meetingCd"/>
</form>
	<div data-role="page" id="pageone">
		<div data-role="header">
			<a href="#"
				class="ui-btn ui-corner-all ui-shadow ui-icon-home ui-btn-icon-left">Home</a>
			<h1>교류회</h1>
			<a href="#"
				class="ui-btn ui-corner-all ui-shadow ui-icon-search ui-btn-icon-left">Search</a>
		</div>
		
		<div data-role="main" class="ui-content">
			<ul data-role="listview" data-inset="true">
				<c:forEach items="${result}" var="result" step="1">
					<li><img src="./resources/hfmp/gita/empty_photo.png">
						<a href="javascript:fnClick('${result.meetingCd}');">
							<strong> ${result.meetingNm} </strong><br>
							회장 : ${result.ceoNm1}<br>
							총무 : ${result.ceoNm2}<br>
							회원수 : ${result.companyCount}<br>
						</a>
						<a href="javascript:choosePopup('${result.meetingCd}', '${result.meetingNm}');" data-rel="popup" data-icon="gear"></a>
						
					</li>
				</c:forEach>
			</ul>				
			<div data-role="popup" id="popupMenu">
				<ul data-role="listview" data-inset="true">
					<li data-role="divider" data-theme="a">교류회 관리 목록</li>
					<li><a href="javascript:dialogModifyMeeting();">수정</a></li>
					<li><a href="javascript:deleteMeeting();">삭제</a></li>
				</ul>
			</div>
		</div>
	</div>
	
	<div data-role="page" id="dialogModify" data-dialog="true">
		<div data-role="header">
			<h1>교류회 수정</h1>
		</div>

		<div data-role="main" class="ui-content">
			<label for="meetingNm">교류회명:</label> <input type="text"
				name="meetingNm" id="meetingNm"> <input type="hidden"
				name="meetingCd" id="meetingCd"> <a href="#"
				class="ui-btn ui-corner-all ui-shadow ui-btn-inline ui-btn-b ui-icon-back ui-btn-icon-right"
				data-rel="back">Go Back</a>
		</div>
	</div>

	<div data-role="page" id="dialogResult" data-dialog="true">
		<div data-role="header">
			<h1>교류회 변경 결과</h1>
		</div>
		<div data-role="main" class="ui-content">
			<p id="resultMessage"></p>
		</div>
	</div>
		
	
</body>
</html>
