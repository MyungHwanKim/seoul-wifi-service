package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class WifiHistory extends JDBCTemplate {
	public List<WifiHistoryInfo> histroyList() {
		
		List<WifiHistoryInfo> wifiHistories = new ArrayList<>();
		
		Connection connection = getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		
		try {
			
			String sql = "SELECT * "
					+ "FROM WIFI_HISTORY "
					+ "ORDER BY ID DESC "
					+ ";"
					;
			
			preparedStatement = connection.prepareStatement(sql);
			rs = preparedStatement.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt("ID");
				String x = rs.getString("LAT");
				String y = rs.getString("LNT");
				String cur = rs.getString("CURDATE");
				
				WifiHistoryInfo wifiHistoryInfo = new WifiHistoryInfo();
				wifiHistoryInfo.setID(id);
				wifiHistoryInfo.setLat(x);
				wifiHistoryInfo.setLnt(y);
				wifiHistoryInfo.setCurDate(cur);
				wifiHistories.add(wifiHistoryInfo);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(connection);
			close(preparedStatement);
			close(rs);
		}
		return wifiHistories;
	}
	
	public int getNext() {
		
		Connection connection = getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		
		try {
			
			String sql = "SELECT ID "
					+ "FROM WIFI_HISTORY "
					+ "ORDER BY ID DESC "
					;
			
			preparedStatement = connection.prepareStatement(sql);
			
			rs = preparedStatement.executeQuery();
			
			while (rs.next()) {
				return rs.getInt(1) + 1;
			}
			return 1;
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(connection);
			close(preparedStatement);
			close(rs);
		}
		return -1;
	}
	
	
	
	public void histInsert(String lat_val, String lnt_val) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT")); 
		Connection connection = getConnection();
		PreparedStatement preparedStatement = null;
		
		try {			
			String sql = "INSERT INTO WIFI_HISTORY "
					+ " (ID, LAT, LNT, CURDATE )"
					+ " VALUES "
					+ " (?, ?, ?, ?)"
					;
			
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, getNext());
			preparedStatement.setString(2, lat_val);
			preparedStatement.setString(3, lnt_val);
			preparedStatement.setString(4, sdf.format(new Date()));
			
			int affected = preparedStatement.executeUpdate();
			
			if (affected > 0) {
				System.out.println(" 저장 성공 ");
			} else {
				System.out.println(" 저장 실패 ");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(connection);
			close(preparedStatement);
		}
	}
	
	public int delete(String ID) {
		int affected = 0;
		
		Connection connection = getConnection();
		PreparedStatement preparedStatement = null;
		
		try {
			
			String sql = "DELETE FROM WIFI_HISTORY"
					+ " WHERE ID = ? ";

			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, ID.trim());
			
			affected = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			close(connection);
			close(preparedStatement);
		}
		return affected;
	}
}
