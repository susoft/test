<%@ page session="false" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Media Query Demos</title>
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
	width: 30%;
}

.imgClass2 {
	width: 30%;
}

/* max-width */
@media screen and (max-width: 600px) {
	.one {
		background: #F9C;
	}
}
</style>
<script type="text/JavaScript" src="./resources/js/jquery-1.11.1.js"></script>
<script type="text/javascript">

/* $(document).ready(function(){
$.ajax({
  type: "POST",
  url: "/someController",
  data: { name: "John", location: "Boston" }
  success:function( html ) {
    $( "#results" ).append( html );
  }
});
}); */

function fnClick(gubun){
	var methodVar = "get";
	var actionVar = "getHfmp0003.do";
	
	if (gubun == "3") {
		methodVar = "get";
		actionVar = "getHfmp0003.do";
		//actionVar = "hfmp_0003.do";
	} else if (gubun == "4") {
		methodVar = "post";
		actionVar = "getHfmp0004.do";
	} else if (gubun == "5") {
		methodVar = "post";
		actionVar = "regiHfmp0005.do";
	} else if (gubun == "6") {
		methodVar = "post";
		actionVar = "regiHfmp0006.do";
	}
	
	document.frm.method = methodVar;
	document.frm.action = actionVar;
	document.frm.meetingCd.value = "";
	document.frm.submit();
}
</script>
</head>

<body>
<form name="frm">
<input type=hidden name="meetingCd"/>
</form>
	<img class="imgClass1" src="./resources/images/hfmp/hfmp_title.gif" alt="중소기업교류회타이틀" />
	<div class="wrapper">
		<table class="tableList" style="width:100%">
			<tr>
				<td class="center notselected" onclick="javascript:fnClick(1);">
					<img class="imgClass1" src="./resources/images/hfmp/main/hfmp0100.png" alt="교류회소개" />
					<P>교류회소개</P>
				</td>
				<td class="center notselected" onclick="javascript:fnClick(2);">
					<img class="imgClass2" src="./resources/images/hfmp/main/hfmp0200.png" alt="교류회현황" />
					<P>교류회현황</P>
				</td>
			</tr>
			<tr>
				<td class="center notselected" onclick="javascript:fnClick(3);">
					<img class="imgClass1" src="./resources/images/hfmp/main/hfmp0300.png" alt="교류회조회" />
					<P>교류회조회</P>
				</td>
				<td class="center notselected" onclick="javascript:fnClick(4);">
					<img class="imgClass2" src="./resources/images/hfmp/main/hfmp0400.png" alt="회원사조회" />
					<P>회원사조회</P>
				</td>
			</tr>
			<tr>
				<td class="center notselected" onclick="javascript:fnClick(5);">
					<img class="imgClass1" src="./resources/images/hfmp/main/hfmp0500.png" alt="교류회등록" />
					<P>교류회등록</P>
				</td>
				<td class="center notselected" onclick="javascript:fnClick(6);">
					<img class="imgClass2" src="./resources/images/hfmp/main/hfmp0600.png" alt="회원사등록" />
					<P>회원사등록</P>
				</td>
			</tr>
		</table>
	</div>
	
	<p class="viewing-copyright">
		광주광역시 광산구 도천동 621-15호
		<BR>중소기업종합지원센터 5층
		<BR>TEL: 062-956-7120 | FAX: 062-956-7121
		<BR>Copyright 2012 © 중소기업융합광주전남연합회.
		<BR>All Rights Reserved.
	</p>
</body>
</html>
