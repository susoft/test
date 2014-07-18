<%@page session="false" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Media Query Demos</title>
<link rel="stylesheet" href="http://code.jquery.com/mobile/1.4.2/jquery.mobile-1.4.2.min.css">
<script src="http://code.jquery.com/jquery-1.10.2.min.js"></script>
<script src="http://code.jquery.com/mobile/1.4.2/jquery.mobile-1.4.2.min.js"></script>

<style type="text/css">
body {
	font-size: 0.95em;
	color: #333;
	font-family: "맑은 고딕";
}
.wrapper {
	/* border: solid 1px #666;	 */
	padding: 5px 10px;
	margin: 50px 20px 20px 20px;
}
.viewing-copyright {
	font-size: 0.75em;
	color: #666;
	text-align: center;
}

table.tableList { table-layout:fixed; border-top:2px #EA0000 solid; border-bottom:1px #CFCFCF solid; }
table.tableList td.center { text-align:center; }
table.tableList td.selected { background-color:pink; }
table.tableList td.notselected {  }

.imgClass1 {
	width: 40%;
}

.imgClass2 {
	width: 100%;
}

/* max-width */
@media screen and (max-width: 600px) {
	.one {
		background: #F9C;
	}
}
</style>
<script type="text/javascript">
function fnClick(meetingCd) {
	document.frm.meetingCd.value = meetingCd;
	document.frm.submit();
}

function choosePopup(meetingCd, meetingNm) {
	meetingCd_temp = meetingCd;
	meetingNm_temp = meetingNm;
	
	$("#popupMenu").popup();
	$('#popupMenu').popup('open');
}

function modifyMeeting(meetingCd, meetingNm) {
	
	$("#meetingNm").val(meetingNm);
	$("#meetingCd").val(meetingCd);
	
	$.mobile.changePage( "#dialogModify", { role: "dialog" } );
}

function saveInfo() {
	var meetingCd = $("#meetingCd").val();
	var meetingNm = $("#meetingNm").val();
	$("#meetingNm").val(meetingNm);
	$("#meetingCd").val(meetingCd);
	
	$("#dialogModify").popup();
	$('#dialogModify').popup('close');
	
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
			//$.mobile.changePage( "#dialog", { role: "page" } );
		}
	}
	);
}

function deleteMeeting() {
	$('#popupMenu').popup('close');
	
	var meetingCd = meetingCd_temp;
	console.log(meetingCd);
	
	$.ajax( {
		type : "POST"
	,	url  : "deletMeeting.do"
	,	dataType : "json"
	,	data : {'meetingCd': meetingCd}
	,	success : function (result) {
			console.log(result);
			
			if (result.result == "1") {
				$("#resultMessage").html('정삭적으로 삭제되었습니다.');
			} else {
				$("#resultMessage").html('오류가 발생하였습니다.');
			}
			$.mobile.changePage( "#dialog", { role: "diolog" } );
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
						<a href="javascript:choosePopup('${result.meetingCd}', '${result.meetingNm}');" data-icon="gear"></a>
					</li>
				</c:forEach>
			</ul>
		</div>

		<div data-role="popup" id="popupMenu">
			<ul data-role="listview" data-inset="true">
				<li data-role="divider" data-theme="a">교류회 관리 목록</li>
				<li><a href="#" onclick="javascript:modifyMeeting();">수정</a></li>
				<li><a href="#" onclick="javascript:deleteMeeting();">삭제</a></li>
			</ul>
		</div>

		<div data-role="page" data-dialog="true" id="dialogResult">
			<div data-role="header">
				<h1>교류회 변경 결과</h1>
			</div>
			<div data-role="main" class="ui-content">
				<p id="resultMessage"></p>
			</div>
		</div>
		
		<div data-role="page" data-dialog="true" id="dialogModify">
			<div data-role="header">
				<a href="#"
					class="ui-btn ui-corner-all ui-shadow ui-icon-home ui-btn-icon-left">Home</a>
				<h1>교류회수정</h1>
			</div>
			<div data-role="main" class="ui-content">
				<div class="ui-field-contain">
					<label for="meetingNm">교류회명:</label> 
					<input type="text" name="meetingNm" id="meetingNm">
					<input type="hidden" name="meetingCd" id="meetingCd">
				</div>
				<input type="button" data-inline="true" value="저장"
					onclick="javascript:saveInfo()">
			</div>
		</div>
		
	</div>
</body>
</html>
