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
$.ajax( {
		type : "GET"
	,	url  : "getHfmp0003.do"
	,	dataType : "json"
	,	data : ""
	,	success : function (result) {
			console.log(result);
			
			$.each(result, function(key) {
				var list = result[key];
				
				var content = "<table>";
				for (var i = 0; i < list.length; i++) {
					content += "<tr>";
					content += "<td> id : " + list[i].id + "</td>";
					content += "<td> subject : " + list[i].subject + "</td>";
					content += "<td> context : " + list[i].context + "</td>";
					content += "</tr>";
				}
				content += "</table>";
				$("#result").html(content);
			});
		}
	}
);

</script>
</head>

<body>
	<img class="imgClass1" src="./resources/images/hfmp/hfmp_title.gif" alt="중소기업교류회타이틀" />
	<div id="result"></div>
</body>
</html>
