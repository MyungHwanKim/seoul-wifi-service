<%@page import="db.WifiHistory"%>
<%@page import="db.WifiService"%>
<%@page import="java.util.* " %>
<%@page import="db.WifiInfo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String lat_val = request.getParameter("lat_value");
	String lnt_val = request.getParameter("lnt_value");
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>와이파이 정보 구하기</title>
	<script type="text/javascript" src="http://code.jquery.com/jquery-1.7.min.js"></script>
	<script type="text/javascript">
		function self_value($lat, $lnt) {
			jQuery('#lat_value').val($lat);
			jQuery('#lnt_value').val($lnt);
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
		form
		{
			font-size: medium;
			text-align: center;
			line-height: 3;
		}
		body form #col
		{
			font-weight: 700;
			font-size: 15pt;
		}
		body form input
		{
			border-radius: 5px;
			padding: 3px;
			font-size: 15pt;
			width: 150px;
			text-align: center;
		}
		table
		{
			width: 100%;
			border-collapse: collapse;
		}
		tbody #wait
		{
			line-height: 5;
			text-align: center;
		}
		
		tbody #information
		{
			font-size: 11pt;
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
		body tr:nth-child(even) 
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
		}
		
		.btn-outline {
		    position: relative;
		    padding: 8px 20px;
		    border-radius: 5px;
		    font-family: "paybooc-Light", sans-serif;
		    box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
		    text-decoration: none;
		    font-weight: 600;
		    transition: 0.25s;
		}
		.btn-gray-outline {
		    border: 3px solid #a3a1a1;
		    color: #6e6e6e;
		}
		.btn-outline:hover {
		    letter-spacing: 1px;
		    transform: scale(1.1);
		    cursor: pointer;
		}
		.btn-gray-outline:hover {
		    background-color: #a3a1a1;
		    color: #e3dede;
		}
	</style>
	
	
</head>
<body>
	<h1>와이파이 정보 구하기</h1>
	<div id="menu">
		<a href=./>홈 &#124; </a>
		<a href=history.jsp>위치 히스토리 목록 &#124; </a>
		<a href=load-wifi.jsp>Open API 와이파이 정보 가져오기</a>
	</div>
	
	<form action="./">
		<label id="col">LAT: </label>
		<input type="text" id="lat_value" name="lat_value" step="0.0000001" 
				<%if(lat_val != null) {%>
				value=<%= lat_val %>
				<%} else {%>
				value="0.0"
				<%} %>
			 />
		<label id="col">, LNT: </label>
		<input type="text" id="lnt_value" name="lnt_value" step="0.0000001" 
				<%if(lnt_val != null) {%>
				value=<%= lnt_val %>
				<%} else {%>
				value="0.0"
				<%} %>
			 />
		<input type="hidden" id="lat" step="0.0000001" value="37.5561177" />
		<input type="hidden" id="lnt" step="0.0000001" value="126.972106" />
		
		<button class="btn-outline btn-gray-outline" type="button" onClick="self_value(lat.value, lnt.value)">내 위치 가져오기</button>
 		<button class="btn-outline btn-gray-outline" type="submit">근처 WIFI 정보 보기</button>
	</form>
	
	<table>
		<%
		WifiService wifiService = new WifiService();
		List<WifiInfo> wifiList = wifiService.nearbyWifi(lat_val, lnt_val);
		%>
		<thead>
			<tr>
				<th>거리[Km]</th>
				<th>관리번호</th>
				<th>자치구</th>
				<th>와이파이명</th>
				<th>도로명주소</th>
				<th>상세주소</th>
				<th>설치위치(층)</th>
				<th>설치유형</th>
				<th>설치기관</th>
				<th>서비스구분</th>
				<th>망종류</th>
				<th>설치년도</th>
				<th>실내외구분</th>
				<th>WIFI접속환경</th>
				<th>X좌표</th>
				<th>Y좌표</th>
				<th>작업일자</th>
			</tr>
		</thead>
		<tbody>
			<%
				if (lat_val == null && lnt_val == null) {
					
			%>
			<tr id="wait">
				<td colspan="17">위치 정보를 입력한 후에 조회해 주세요.</td>
			</tr>
			<%
				} else { 
				for (int i = 0; i < wifiList.size(); i++) {
					WifiInfo wifiInfo = wifiList.get(i);
					if (i == 0) {
						WifiHistory wifiHistory = new WifiHistory();
						wifiHistory.histInsert(lat_val, lnt_val);
					}
			%>		
			<tr id="information">
				<td><%=String.format("%.4f",Math.acos(wifiInfo.getDist()) * 6371) %></td>
				<td><%=wifiInfo.getMgrNo() %></td>
				<td><%=wifiInfo.getWrdofc() %></td>
				<td><%=wifiInfo.getMainNm() %></td>
				<td><%=wifiInfo.getAdres1() %></td>
				<td><%=wifiInfo.getAdres2() %></td>
				<td><%=wifiInfo.getInstlFloor() %></td>
				<td><%=wifiInfo.getInstlTy() %></td>
				<td><%=wifiInfo.getInstlMby() %></td>
				<td><%=wifiInfo.getSvcSe() %></td>
				<td><%=wifiInfo.getCmcwr() %></td>
				<td><%=wifiInfo.getCnstcYear() %></td>
				<td><%=wifiInfo.getInoutDoor() %></td>
				<td><%=wifiInfo.getRemars3() %></td>
				<td><%=wifiInfo.getLat() %></td>
				<td><%=wifiInfo.getLnt() %></td>
				<td><%=wifiInfo.getWorkDttm() %></td>
			</tr>
			<%} %>
			<%} %>
		</tbody>
	</table>
</body>
</html>