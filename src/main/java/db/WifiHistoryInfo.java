package db;

import lombok.Data;

@Data
public class WifiHistoryInfo {
	int ID;						 // 순서
	String lat;				   	 // X좌표
	String lnt;					 // Y좌표
	String curDate;				 // 조회일자
}
