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
<script type="text/JavaScript" src="./resources/js/jquery-1.11.1.js"></script>
<script type="text/javascript">
/* $.ajax( {
		type : "GET"
	,	url  : "getHfmp0003.do"
	,	dataType : "json"
	,	data : ""
	,	success : function (result) {
			console.log(result);
			
			$.each(result, function(key) {
				var list = result[key];
				
				var content = "<table class='tableList' style='width:100%'>";
				for (var i = 0; i < list.length; i+=2) {
					content += "<tr>";
					
					content += "<td>";
					content += "id : " + list[i].id + "<br>";
					content += "subject : " + list[i].subject + "<br>";
					content += "context : " + list[i].context;
					content += "</td>";
					
					if (i+1 >= list.length) {
						content += "<td>";
						content += " ";
						content += "</td>";
						
						content += "</tr>";
					} else {
						content += "<td>";
						content += "id : " + list[i+1].id + "<br>";
						content += "subject : " + list[i+1].subject + "<br>";
						content += "context : " + list[i+1].context;
						content += "</td>";
						
						content += "</tr>";
					}
				}
				content += "</table>";
				$("#result").html(content);
			});
		}
	}
); */
function fnClick(id) {
	location.href = "getHfmp0004.do";
}

</script>
</head>

<body>
	<%-- <img class="imgClass1" src="./resources/images/hfmp/hfmp_title.gif" alt="중소기업교류회타이틀" />
	<div id="result">
	<table class="tableList" style="width:100%">
		<c:forEach items="${result}" var="result" step="1">
			<tr>
				<td>
					<table>
						<tr>
							<td onclick="javascript:fnClick(1);">
								<img class="imgClass2" src="./resources/images/hfmp/meeting_img.png" alt="교류회이미지" />
							</td>
							<td onclick="javascript:fnClick(1);">
								ID : ${result.id}<br>
								SUBJECT : ${result.subject}<br>
								CONTEXT : ${result.context}<br>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</c:forEach>
	</table>
	</div> --%>

	<div data-role="page" id="pageone">
		<div data-role="main" class="ui-content">
			<h2>교류회</h2>
			<ul data-role="listview" data-inset="true">
				<c:forEach items="${result}" var="result" step="1">
					<li><a href="javascript:fnClick('${result.id}');">
							ID : ${result.id}<br>
							SUBJECT : ${result.subject}<br>
							CONTEXT : ${result.context}<br>
						</a>
					</li>
				</c:forEach>
			</ul>
		</div>
	</div>
</body>
</html>
