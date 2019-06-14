package com.cinco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class Database {

	//instantiation of connection information
	public static final String url = "jdbc:mysql://cse.unl.edu/alinneman?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	public static final String username = "alinneman";
	public static final String password = "3B4MeM";

	//instantiation of logger
	public static Logger log = Logger.getLogger(Database.class);


	//method that calls the mysql driver surrounded by try catches
	@SuppressWarnings("deprecation")
	public static void classForName() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			//catching exceptions and logging them:
		} catch (InstantiationException e) {
			System.out.println("InstantiationException: ");
			e.printStackTrace();
			log.error("Could not intantiate the object", e);
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			System.out.println("IllegalAccessException: ");
			e.printStackTrace();
			log.error("Access not allowed", e);
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException: ");
			e.printStackTrace();
			log.error("Class was not found", e);
			throw new RuntimeException(e);
		}
	}

	//method that closes connections based on the status of the inputs
	public static void closeConnections(PreparedStatement ps, ResultSet rs, Connection conn) {
		try {
			if(rs != null && !rs.isClosed())
				rs.close();
			if(ps != null && !ps.isClosed())
				ps.close();
			if(conn != null && !conn.isClosed())
				conn.close();
		//catching exception and logging:
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			log.error("Database already closed", e);
			throw new RuntimeException(e);
		}
	}

	//method that returns the connection information and is surround by try/catch
	public static Connection openConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, username, password);
			return conn;
		//catching exception and logging:
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			log.error("Could not connect to database", e);
			throw new RuntimeException(e);
		}

	}
	//log getter
	public static Logger getLog() {
		return log;
	}
}
