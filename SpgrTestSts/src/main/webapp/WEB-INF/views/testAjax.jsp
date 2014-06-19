<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/JavaScript" src="./resources/js/jquery-1.11.1.js"></script>
<script>
function goTest() { //v3.0
	$.ajax( {
			type : "GET"
		,	url  : "/test/test1.do"
		,	dataType : "json"
		,	data : ""
		,	success : function (result) {
				$.each(result, function(key) {
					var list = result[key];
					alert("list1 = " + list.test1);
					alert("list2 = " + list.test2);
					alert("list3 = " + list.test3);
				});
			}
	}
	);
}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>testAjax</title>
</head>
<body>
Ajax
<input type="button" value="test" onclick="goTest()">

<div id="htmlAgax"/>

</body>
</html>