<%@page import="db.WifiHistoryInfo"%>
<%@page import="db.WifiHistory"%>
<%@page import="java.util.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	WifiHistory wifiHistory = new WifiHistory();
	List<WifiHistoryInfo> histroyList = wifiHistory.histroyList();
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>와이파이 정보 구하기</title>
	<script type="text/javascript">
		function del(ID) {
			location.href="delete.jsp?id=" + ID;
		}
	</script>
	<style>
		h1
		{
			text-align: center;
		}
		#menu
		{
			text-align: center;
		}
		#menu a
		{
			color: black;
		}
		div
		{
			line-height: 2.5;
		}
		table
		{
			width: 100%;
			border-collapse: collapse;
		}
		tbody tr #del
		{
			text-align: center;
		}
		th 
		{
			padding: 8px;
			padding-top: 12px;
			padding-bottom: 12px;
			text-align: center;
			background-color: #696969;
			color: white;
		}
		tr:nth-child(even) 
		{
			background-color: #f2f2f2;
		}
		td 
		{
			font-weight: 700;
		}
		
		th, td
		{
			border: 1px solid #ddd;
			padding: 8px;
		}
	
	</style>
</head>
<body>
	<h1>위치 히스토리 목록</h1>
	<div id="menu">
		<a href=./>홈 &#124; </a>
		<a href=history.jsp>위치 히스토리 목록 &#124; </a>
		<a href=load-wifi.jsp>Open API 와이파이 정보 가져오기</a>
	</div>

	<table>
		<thead>
			<tr>
				<th>ID</th>
				<th>X좌표</th>
				<th>Y좌표</th>
				<th>조회일자</th>
				<th>비고</th>
			</tr>
		</thead>
		<tbody>
			<%
				for (int i = 0; i < histroyList.size(); i++) {
					WifiHistoryInfo historyInfo = histroyList.get(i);				
			%>
			<tr>
				<td><%=historyInfo.getID() %></td>
				<td><%=historyInfo.getLat() %></td>
				<td><%=historyInfo.getLnt() %></td>
				<td><%=historyInfo.getCurDate() %></td>
				<td id="del"><input type="button" value="삭제" onclick="del('<%=historyInfo.getID()%>');"></td>
			</tr>
			<%} %>
		</tbody>
	</table>
</body>
</html>