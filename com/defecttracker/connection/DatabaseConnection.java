package com.defecttracker.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	
	/**
	 * This function is used to establish connection
	 * @return connection object
	 * @throws SQLException if connection fails to establish
	 */
	public Connection getConnection() throws SQLException{
		
		Connection w_conn = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");  			  
			w_conn = DriverManager.getConnection("jdbc:oracle:thin:@oscar:1521:oscar","training","training");
	
			/*Class.forName("com.mysql.jdbc.Driver");
			w_conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/bugmila","root","password");*/
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}  
		return w_conn;
	}
	
	/**
	 * This function is used to close the already created connection 
	 * @param w_conn is passed so that it can be closed
	 * @return closed connection object
	 * @throws SQLException if connection fails to close
	 */
	
	public Connection closeConnection(Connection w_conn) throws SQLException {
		if (w_conn != null){
			
			w_conn.close();
		}
		return w_conn;
	}
	
}
