package com.lumen.utility;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConnectionUtils {

	public static Connection getMyConnection() {
		Connection con = null;
		try {

			String[] credentials = getPropsAsArray();
			con = DriverManager.getConnection(credentials[0], credentials[1], credentials[2]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	public static String[] getPropsAsArray() {
		String result[] = null;
		String url = null;
		String userName = null;
		String userPassword = null;
		try {
			String fileName = "resources/DbConnection.properties";
			InputStream inStream = ConnectionUtils.class.getClassLoader().getResourceAsStream(fileName);

			Properties prop = new Properties();
			prop.load(inStream);

			url = prop.getProperty("database.url");
			userName = prop.getProperty("database.userName");
			userPassword = prop.getProperty("database.passWord");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new String[] { url, userName, userPassword };
	}

	public static Connection getConnectionFromPool() {
		Connection connection = null;
		try {
			HikariConfig config = new HikariConfig();
			String credentials[] = getPropsAsArray();
			config.setJdbcUrl(credentials[0]);
			config.setUsername(credentials[1]);
			config.setPassword(credentials[2]);

			DataSource source = new HikariDataSource(config);
			connection = source.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

}
