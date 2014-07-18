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

function checkInputData(inputValue, errorMessage) {
	if (inputValue == "") {
		$("#errorMessage").html(errorMessage);
		$("#inputErrorPopup").popup('open');
		return false;
	}
	return true;
}
function saveInfo() {
	var meetingNm = $("#companyNm").val();
	var ceoNm = $("#ceoNm").val();
	
	//$.mobile.changePage('#inputErrorPopup', {transition: 'pop', role: 'dialog'});
	if (!checkInputData(meetingNm, '회원사명을 입력하세요!')) return;
	if (!checkInputData(ceoNm, '대표자명을 입력하세요!')) return;
	
	$.ajax( {
		type : "POST"
	,	url  : "saveHfmp0006.do"
	,	dataType : "json"
	,	data : {'companyNm': companyNm,
				'ceoNm': ceoNm
				}
	,	success : function (result) {
			console.log(result);
			
			$.each(result, function(key) {
				$.mobile.changePage( "#dialog", { role: "page" } );
			});
		}
	}
	);
}

</script>
</head>

<body>
	<div data-role="page" id="pageone">
		<div data-role="main" class="ui-content">
			<div class="ui-field-contain">
				<label for="companyNm">회원사명:</label> 
				<input type="text" name="companyNm" id="companyNm"> 
				<label for="ceoNm">대표자명:</label> 
				<input type="text" name="ceoNm" id="ceoNm"> 
			</div>
			<input type="button" data-inline="true" value="저장" onclick="javascript:saveInfo()">
		</div>

		<div data-role="popup" id="inputErrorPopup">
			<div data-role="header">
				<h1>입력오류</h1>
				<a href="#" data-rel="back"
				class="ui-btn ui-corner-all ui-shadow ui-btn ui-icon-delete ui-btn-icon-notext ui-btn-right">Close</a>
			</div>
			<div data-role="main" class="ui-content">
				<p id="errorMessage"></p>
			</div>
		</div>
	</div>
	
	<div data-role="page" data-dialog="true" id="dialog">
		<div data-role="header">
			<h1>회원사 등록 결과</h1>
		</div>
		<div data-role="main" class="ui-content">
			<p>회원사가 생성되었습니다.</p>
		</div>
	</div>
</body>
</html>
