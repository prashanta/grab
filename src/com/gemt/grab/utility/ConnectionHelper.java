package com.gemt.grab.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHelper
{
	private String url;
	private static ConnectionHelper instance;

	private ConnectionHelper()
	{
    	String driver = "com.mysql.jdbc.Driver";
		try {
			Class.forName(driver).newInstance();
			url = "jdbc:mysql://localhost:3306/grab";            
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() throws SQLException {
		if (instance == null) {
			instance = new ConnectionHelper();
		}
		try {
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			return DriverManager.getConnection(instance.url, "xpd", "xpd*2016");
		} catch (SQLException e) {
			throw e;
		}
	}
	
	public static void close(Connection connection)
	{
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}