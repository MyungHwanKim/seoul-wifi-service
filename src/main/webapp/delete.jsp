<%@page import="wifi.service.WifiHistoryService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>와이파이 정보 구하기</title>
</head>
<body>
	<%
	WifiHistoryService wifiHistory = new WifiHistoryService();
		int affected = wifiHistory.delete(request.getParameter("id"));
		String message = "삭제되지 않았습니다.";
		if (affected > 0) {
			message = "삭제되었습니다.";
		}
	%>
	
	<script>
		alert("<%=message %>");
		location.href="history.jsp";
	</script>
</body>
</html>