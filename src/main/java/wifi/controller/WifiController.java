package wifi.controller;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import wifi.dto.WifiInfo;
import wifi.service.WifiService;

public class WifiController {
	// 인증키
    static String key = "574b68744e6b6d683532686a666873";
    
    public long WifiController() throws Exception {
    	JSONParser jsonParser = new JSONParser();
    	long start = System.nanoTime();
    	long totalCount = 0;
    	WifiService wifiService = new WifiService();
    	List<WifiInfo> list = new ArrayList<>();

    	for (int k = 0; k < 15; k++) {
    		JSONObject json = (JSONObject) jsonParser.parse(readUrl(k));
        	JSONObject TbPublicWifiInfo = (JSONObject) json.get("TbPublicWifiInfo");
        	totalCount = (long) TbPublicWifiInfo.get("list_total_count");
        	JSONArray row = (JSONArray) TbPublicWifiInfo.get("row");

        	for (int i = 0; i < row.size(); i++) {
        		JSONObject array = (JSONObject) row.get(i);
        		WifiInfo wifiInfo = new WifiInfo();
        		wifiInfo.setMgrNo(String.valueOf(array.get("X_SWIFI_MGR_NO")));
                wifiInfo.setWrdofc(String.valueOf(array.get("X_SWIFI_WRDOFC")));
                wifiInfo.setMainNm(String.valueOf(array.get("X_SWIFI_MAIN_NM")));
                wifiInfo.setAdres1(String.valueOf(array.get("X_SWIFI_ADRES1")));
                wifiInfo.setAdres2(String.valueOf(array.get("X_SWIFI_ADRES2")));
                wifiInfo.setInstlFloor(String.valueOf(array.get("X_SWIFI_INSTL_FLOOR")));
                wifiInfo.setInstlTy(String.valueOf(array.get("X_SWIFI_INSTL_TY")));
                wifiInfo.setInstlMby(String.valueOf(array.get("X_SWIFI_INSTL_MBY")));
                wifiInfo.setSvcSe(String.valueOf(array.get("X_SWIFI_SVC_SE")));
                wifiInfo.setCmcwr(String.valueOf(array.get("X_SWIFI_CMCWR")));
                wifiInfo.setCnstcYear(String.valueOf(array.get("X_SWIFI_CNSTC_YEAR")));
                wifiInfo.setInoutDoor(String.valueOf(array.get("X_SWIFI_INOUT_DOOR")));
                wifiInfo.setRemars3(String.valueOf(array.get("X_SWIFI_REMARS3")));
                wifiInfo.setLat(Double.parseDouble(String.valueOf(array.get("LNT"))));
                wifiInfo.setLnt(Double.parseDouble(String.valueOf(array.get("LAT"))));
                wifiInfo.setWorkDttm(String.valueOf(array.get("WORK_DTTM"))); 
                list.add(wifiInfo);
        	}
    	}
    	wifiService.register(list);
    	long end = System.nanoTime();
		System.out.println("수행시간: " + (end - start) + "ns");
    	
    	return totalCount;
    }
    
    public static String readUrl(int k) throws Exception {
		int start_idx = 1 + (1000 * k);
		int end_idx = 1000 + (1000 * k);

    	StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088");
		urlBuilder.append("/" +  URLEncoder.encode(key,"UTF-8") ); 
		urlBuilder.append("/" +  URLEncoder.encode("json","UTF-8") );
		urlBuilder.append("/" + URLEncoder.encode("TbPublicWifiInfo","UTF-8")); 
		urlBuilder.append("/" + URLEncoder.encode(Integer.toString(start_idx),"UTF-8"));
		urlBuilder.append("/" + URLEncoder.encode(Integer.toString(end_idx),"UTF-8"));
		
		URL url = new URL(urlBuilder.toString());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-type", "application/xml");
		System.out.println("Response code: " + conn.getResponseCode());
		BufferedReader rd;

		if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} else {
				rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}
		
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
				sb.append(line);
		}
		rd.close();
		conn.disconnect();
		return sb.toString();
    }
}
