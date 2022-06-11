package db;
import lombok.*;

@Data
public class WifiInfo {
	private double dist;				 // 거리
	private String mgrNo;    			 // 관리번호
	private String wrdofc;   		 	 // 자치구
	private String mainNm;  		 	 // 와이파이명
	private String adres1;   		  	 // 도로명주소
	private String adres2;   		 	 // 상세주소
	private String instlFloor;		   	 // 설치위치(층)
	private String instlTy;   			 // 설치유형
	private String instlMby;  			 // 설치기관
	private String svcSe;     			 // 서비스구분
	private String cmcwr;      			 // 망종류
	private String cnstcYear;    		 // 설치년도
	private String inoutDoor; 			 // 실내외구분
	private String remars3;    			 // wifi접속환경
	private double lat;				   	 // X좌표
	private double lnt;					 // Y좌표
	private String workDttm; 			 // 작업일자
}