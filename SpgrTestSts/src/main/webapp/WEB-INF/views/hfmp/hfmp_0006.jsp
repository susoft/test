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
	var companyNm = $("#companyNm").val();
	var ceoNm = $("#ceoNm").val();
	var gita1 = $("#gita1").val();
	var phone2 = $("#phone2").val();
	
	var meetingCd = $("#meetingCd").val();
	var hfmbOrganDivCd = $("#hfmbOrganDivCd").val();
	var hfmbDutyDivCd = $("#hfmbDutyDivCd").val();
	
	//$.mobile.changePage('#inputErrorPopup', {transition: 'pop', role: 'dialog'});
	if (!checkInputData(companyNm, '회원사명을 입력하세요!')) return;
	if (!checkInputData(ceoNm, '대표자명을 입력하세요!')) return;
	if (!checkInputData(gita1, '입회일자를 입력하세요!')) return;
	if (!checkInputData(phone2, '핸드폰을 입력하세요!')) return;
	
	$.ajax( {
		type : "POST"
	,	url  : "saveHfmp0006.do"
	,	dataType : "json"
	,	data : {'companyNm': companyNm, 
				'ceoNm': ceoNm,
				'gita1': gita1,
				'phone2': phone2,
				'categoryBusinessNm': $("#categoryBusinessNm").val(),
				'addr': $("#addr").val(),
				'phone1': $("#phone1").val(),
				'phone3': $("#phone3").val(),
				'email': $("#email").val(),
				'meetingCd': meetingCd,
				'hfmbOrganDivCd': hfmbOrganDivCd,
				'hfmbDutyDivCd': hfmbDutyDivCd
		}
	,	success : function (result) {
			console.log(result);
			if (result.result == "1") {
				$("#resultMessage").html('회원사가 생성되었습니다.');
			} else {
				$("#resultMessage").html('이미 등록된 회원사 입니다.');
			}
			$.mobile.changePage( "#dialog", { role: "page" } );
		}
	}
	); 
}

</script>
</head>

<body>
	<div data-role="page" id="pageone">
		<div data-role="header">
			<a href="#"
				class="ui-btn ui-corner-all ui-shadow ui-icon-home ui-btn-icon-left">Home</a>
			<h1>회원사등록</h1>
		</div>

		<div data-role="main" class="ui-content">
			<div class="ui-field-contain">
				<label for="companyNm">회사명:</label> 
				<input type="text" name="companyNm" id="companyNm"> 
				<label for="ceoNm">대표명:</label> 
				<input type="text" name="ceoNm" id="ceoNm"> 
				
				<label for="gita1">입회일자:</label> 
				<input type="date" name="gita1" id="gita1" value=""  />
				
				<label for="categoryBusinessNm">업종:</label> 
				<input type="text" name="categoryBusinessNm" id="categoryBusinessNm"> 
				
				<label for="addr">주소:</label> 
				<input type="text" name="addr" id="addr"> 
				
				<label for="phone2">핸드폰:</label> 
				<input type="text" name="phone2" id="phone2"> 
				<label for="phone1">회사전화:</label> 
				<input type="text" name="phone1" id="phone1"> 
				<label for="phone3">팩스:</label> 
				<input type="text" name="phone3" id="phone3"> 
				
				<label for="email">이메일:</label> 
				<input type="text" name="email" id="email"> 
				
				<fieldset class="ui-field-contain">
					<label for="meetingCd">교류회:</label> 
					<select name="meetingCd" id="meetingCd">
						<option value="" selected>선택</option>
						<c:forEach items="${result1}" var="result1" step="1">
							<option value="${result1.meetingCd}">${result1.meetingNm}</option>
						</c:forEach>
					</select>
				</fieldset>
				<fieldset class="ui-field-contain">
					<label for="hfmbOrganDivCd">교류회직책:</label> 
					<select name="hfmbOrganDivCd" id="hfmbOrganDivCd">
						<c:forEach items="${result}" var="result" step="1">
							<c:if test="${result.codeKey eq 'J002'}">
								<option value="${result.code}">${result.codeNm}</option>
							</c:if>
						</c:forEach>
					</select>
				</fieldset>
				<fieldset class="ui-field-contain">
					<label for="hfmbDutyDivCd">연합회직책:</label> 
					<select name="hfmbDutyDivCd" id="hfmbDutyDivCd">
						<c:forEach items="${result}" var="result" step="1">
							<c:if test="${result.codeKey eq 'J001'}">
								<option value="${result.code}">${result.codeNm}</option>
							</c:if>
						</c:forEach>
					</select>
				</fieldset>
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
			<p id="resultMessage"></p>
		</div>
	</div>
</body>
</html>
