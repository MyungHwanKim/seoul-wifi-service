<%@page import="java.sql.ResultSetMetaData"%>
<%@page import="db.WifiService"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="db.Wifi"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>와이파이 정보 구하기</title>
	<style>
		.home-button, .information
		{
			text-align: center;
			color: black;
		}
		.information
		{
			font-weight: 900;
			font-size: 130%;
		}
		
	</style>
</head>
<body>
	<p class='information'>
	<% 
		Wifi wifi = new Wifi();
		out.print(wifi.Wifi());
	%>
	 개의 WIFI 정보를 정상적으로 저장하였습니다.</p>
	<div class='home-button'>
		<a href="./">홈으로 가기</a>
	</div>
</body>
</html>