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

function modifyInfo(id) {
	alert(id);
}

function deleteInfo(id) {
	alert(id);
}

</script>
</head>

<body>
	<div data-role="page" id="pageone">
		<div data-role="header">
			<a href="#"
				class="ui-btn ui-corner-all ui-shadow ui-icon-home ui-btn-icon-left">Home</a>
			<h1>회원사</h1>
			<a href="#"
				class="ui-btn ui-corner-all ui-shadow ui-icon-search ui-btn-icon-left">Search</a>
		</div>
		
		<div data-role="main" class="ui-content">
			<ul data-role="listview" data-inset="true">
				<c:forEach items="${result}" var="result" step="1">
					<li><a href="#${result.companyCd}">
							<img src="./resources/photo/${result.companyCd}.jpg">
							<strong>${result.ceoNm}</strong> ${result.companyNm}<br>
							${result.categoryBusinessNm}<br>
							${result.phone1} ${result.phone2}<br>
							${result.addr}<br>
						</a>
						<a href="javascript:deleteInfo('${result.companyCd}')" data-icon="gear"></a>
					</li>
				</c:forEach>
			</ul>
		</div>

		<c:forEach items="${result}" var="result" step="1">
			<div data-role="panel" id="${result.companyCd}">
				<h2>${result.ceoNm}</h2>
				<p>${result.companyNm}</p>
				<p>${result.categoryBusinessNm}</p>
				<p>회사 : ${result.phone1}</p>
				<p>모바일 : ${result.phone2}</p>
				<p>팩스 : ${result.phone3}</p>
				<p>주소: ${result.addr}</p>
				<p>이메일: ${result.email}</p>
				<a href="#pageone" data-rel="close"
					class="ui-btn ui-btn-inline ui-shadow ui-corner-all ui-btn-b ui-icon-delete ui-btn-icon-left">닫기</a>
				<a href="javascript:modifyInfo('${result.companyCd}')" data-rel="edit"
					class="ui-btn ui-btn-inline ui-shadow ui-corner-all ui-btn-b ui-icon-edit ui-btn-icon-left">수정</a>
			</div>
		</c:forEach>
	</div>
</body>
</html>
