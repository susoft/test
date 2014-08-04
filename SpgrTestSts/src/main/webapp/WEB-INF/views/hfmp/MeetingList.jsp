<%@page session="false" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>교류회 조회</title>
<script src="http://code.jquery.com/jquery-1.10.2.min.js"></script>

<script type="text/javascript">
$(document).ready(function(){
	console.log( "ready!");
	$.ajax({
	  type: "GET",
	  url: "getHfmp0003.do",
	  data: "",
	  success:function( html ) {
		console.log( html );
	  	//$( "#results" ).append( html );
	  }
	});
});

function fnClick(meetingCd) {
	document.frm.meetingCd.value = meetingCd;
	document.frm.submit();
}

var meetingCd_temp;
var meetingNm_temp;

</script>
</head>

<body>
	<form name="frm" action="getHfmp0004.do" method="post">
		<input type=hidden name="meetingCd" />
	</form>
	<h1>교류회</h1>
	<ul>
		<c:forEach items="${result}" var="result" step="1">
			<li>
				<img src="./resources/hfmp/gita/empty_photo.png">
				<a href="javascript:fnClick('${result.meetingCd}');"> 
					<strong>${result.meetingNm}</strong> <br>
					회장 : ${result.ceoNm1} <br>
					총무 : ${result.ceoNm2} <br>
					회원수 : ${result.companyCount} <br>
				</a> 
				<a href="javascript:choosePopup('${result.meetingCd}', '${result.meetingNm}');">
				</a>
			</li>
		</c:forEach>
	</ul>
</body>
</html>
