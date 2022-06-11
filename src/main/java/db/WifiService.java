package db;

import java.sql.*;
import java.util.*;

import org.apache.jasper.tagplugins.jstl.core.ForEach;

public class WifiService extends JDBCTemplate{
	public String check(String value) {
		if (value == null) {
			return "0.0";
		}
		return value;
	}
	
	public List<WifiInfo> nearbyWifi(String lat_val, String lnt_val) {
		lat_val = check(lat_val);
		lnt_val = check(lnt_val);
		double latVal = Double.parseDouble(lat_val);
		double lntVal = Double.parseDouble(lnt_val);

		final double sinLat = Math.sin(Math.toRadians(latVal));
		final double cosLat = Math.cos(Math.toRadians(latVal));
		final double sinLnt = Math.sin(Math.toRadians(lntVal));
		final double cosLnt = Math.cos(Math.toRadians(lntVal));
		
		List<WifiInfo> wifiList = new ArrayList<>();
		Connection connection = getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		
		String sql = " SELECT *, (" + cosLat + " * " + "COS_LAT * ( COS_LNT * " + cosLnt
				+ "+ SIN_LNT *" + sinLnt + ") + " + sinLat + "* SIN_LAT) AS partial_dist "
				+ " FROM WIFI_INFO "
				+ " WHERE LAT != 0 OR LNT != 0"
				+ " ORDER BY partial_dist DESC"
				+ " LIMIT 20 ";
		try {
			
			preparedStatement = connection.prepareStatement(sql);
			
			rs = preparedStatement.executeQuery();
			
			while (rs.next()) {
				double dist = rs.getDouble("partial_dist"); 
				String mgrNo = rs.getString("X_SWIFI_MGR_NO");
				String wrdofc = rs.getString("X_SWIFI_WRDOFC");
				String mainNm = rs.getString("X_SWIFI_MAIN_NM");
				String adres1 = rs.getString("X_SWIFI_ADRES1");
				String adres2 = rs.getString("X_SWIFI_ADRES2");
				String instlFloor = rs.getString("X_SWIFI_INSTL_FLOOR");
				String instlTy = rs.getString("X_SWIFI_INSTL_TY");
				String instlMby = rs.getString("X_SWIFI_INSTL_MBY");
				String svcSe = rs.getString("X_SWIFI_SVC_SE");
				String cmcwr = rs.getString("X_SWIFI_CMCWR");
				String cnstcYear = rs.getString("X_SWIFI_CNSTC_YEAR");
				String inoutDoor = rs.getString("X_SWIFI_INOUT_DOOR");
				String remars3 = rs.getString("X_SWIFI_REMARS3");
				double lat = rs.getDouble("LAT");
				double lnt = rs.getDouble("LNT");
				String workDttm = rs.getString("WORK_DTTM");
				
				WifiInfo wifiInfo = new WifiInfo();
				wifiInfo.setDist(dist);
				wifiInfo.setMgrNo(mgrNo);
				wifiInfo.setWrdofc(wrdofc);
				wifiInfo.setMainNm(mainNm);
				wifiInfo.setAdres1(adres1);
				wifiInfo.setAdres2(adres2);
				if (instlFloor == null) { 
					wifiInfo.setInstlFloor("");
				} else {
					wifiInfo.setInstlFloor(instlFloor);
				}
				wifiInfo.setInstlTy(instlTy);
				wifiInfo.setInstlMby(instlMby);
				wifiInfo.setSvcSe(svcSe);
				wifiInfo.setCmcwr(cmcwr);
				wifiInfo.setCnstcYear(cnstcYear);
				wifiInfo.setInoutDoor(inoutDoor);
				if (remars3 == null) {
					wifiInfo.setRemars3("");
				} else {
					wifiInfo.setRemars3(remars3);
				}
				wifiInfo.setLat(lat);
				wifiInfo.setLnt(lnt);
				wifiInfo.setWorkDttm(workDttm);
				
				wifiList.add(wifiInfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(connection);
			close(rs);
			close(preparedStatement);
		}
		
		return wifiList;
	}
	
	
	/**
	 * 서울에 있는 wifi 위치 등록
	 * @param wifiInfo
	 */
	public void register(List<WifiInfo> wifiList) {
		
		Connection connection = getConnection();
		PreparedStatement preparedStatement = null;
		
		
		try {
			connection.setAutoCommit(false);
			
			String sql = "INSERT INTO WIFI_INFO "
					+ " (X_SWIFI_MGR_NO, X_SWIFI_WRDOFC, X_SWIFI_MAIN_NM, X_SWIFI_ADRES1, X_SWIFI_ADRES2 "
					+ "	, X_SWIFI_INSTL_TY, X_SWIFI_INSTL_MBY, X_SWIFI_SVC_SE, X_SWIFI_CMCWR, X_SWIFI_CNSTC_YEAR, X_SWIFI_INOUT_DOOR, LAT, LNT, WORK_DTTM, SIN_LAT, SIN_LNT, COS_LAT, COS_LNT) "
					+ " VALUES"
					+ " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); ";
			preparedStatement = connection.prepareStatement(sql);
			int cnt = 0;
			for (WifiInfo info: wifiList) {
				
				preparedStatement.setString(1, info.getMgrNo());
				preparedStatement.setString(2, info.getWrdofc());
				preparedStatement.setString(3, info.getMainNm());
				preparedStatement.setString(4, info.getAdres1());
				preparedStatement.setString(5, info.getAdres2());
				preparedStatement.setString(6, info.getInstlTy());
				preparedStatement.setString(7, info.getInstlMby());
				preparedStatement.setString(8, info.getSvcSe());
				preparedStatement.setString(9, info.getCmcwr());
				preparedStatement.setString(10, info.getCnstcYear());
				preparedStatement.setString(11, info.getInoutDoor());
				preparedStatement.setDouble(12, info.getLat());
				preparedStatement.setDouble(13, info.getLnt());
				preparedStatement.setString(14, info.getWorkDttm());
				preparedStatement.setDouble(15, Math.sin(Math.toRadians(info.getLat())));
				preparedStatement.setDouble(16, Math.sin(Math.toRadians(info.getLnt())));
				preparedStatement.setDouble(17, Math.cos(Math.toRadians(info.getLat())));
				preparedStatement.setDouble(18, Math.cos(Math.toRadians(info.getLnt())));
				cnt++;
				preparedStatement.addBatch();
				preparedStatement.clearParameters();
				if(cnt % 1000 == 0) {
					preparedStatement.executeBatch();
					preparedStatement.clearBatch();
					connection.commit();

				}
				
			}
			preparedStatement.executeBatch();
			preparedStatement.clearParameters();
			connection.commit();
			System.out.println(" 저장 성공 ");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(connection);
			close(preparedStatement);
		}
	}

	/**
	 * 서울시 wifi 위치 삭제
	 * @param wifiInfo
	 */
	public void withdraw() {
		
		Connection connection = getConnection();
		PreparedStatement preparedStatement = null;
		
		String sql = "DELETE FROM WIFI_INFO ";
		
		try {

			preparedStatement = connection.prepareStatement(sql);
			
			int affected = preparedStatement.executeUpdate();
			
			if (affected > 0) {
				System.out.println(" 삭제 성공 ");
			} else {
				System.out.println(" 삭제 실패 ");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(connection);
			close(preparedStatement);
		}
	}

}
