package com.defecttracker.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import com.defecttracker.connection.DatabaseConnection;

public class UserSql 
{
	/**
	 * This function is used to insert values into database when user is created.
	 * @param w_userObject contains all the values of user.
	 * @throws SQLException if insert query fails to execute.
	 */
	public void createUserSql(UserObject w_userObject) throws SQLException 
	{
		DatabaseConnection w_dbConn = new DatabaseConnection();
		Connection conn = w_dbConn.getConnection();
		
		//converting java date into sql date 
		java.sql.Date sqlDate = new java.sql.Date(w_userObject.getW_creationDate().getTime());
		
		String sql = "insert into user_spk values (userseq.nextval, ?, ?, ?, ?, ?, ?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, w_userObject.getW_loginId());
		stmt.setString(2, w_userObject.getW_firstName());
		stmt.setString(3, w_userObject.getW_lastName());
		stmt.setString(4, w_userObject.getW_password());
		stmt.setString(5, w_userObject.getW_role());
		stmt.setDate(6, sqlDate);
		stmt.executeUpdate();
		
		conn = w_dbConn.closeConnection(conn);
	}

	/**
	 * This is function checks if user with following parameters exists in database.
	 * @param w_logonId is the login id provided by the user
	 * @param w_password is the password provided by the user
	 * @return user object if user exists or return null
	 * @throws SQLException if the provided parameters are not present in the database
	 */
	public UserObject validateUser( String w_logonId, String w_password) throws SQLException 
	{
		DatabaseConnection w_dbConn = new DatabaseConnection();
		Connection conn = w_dbConn.getConnection();
		
		String w_sql = "SELECT USERID, LOGINID, FIRSTNAME, LASTNAME, ROLE, CREATIONDATE FROM USER_SPK WHERE LOGINID =? AND PASSWORD= ?";
		
		PreparedStatement w_stmt = conn.prepareStatement(w_sql);
		
		w_stmt.setString(1, w_logonId);
		w_stmt.setString(2, w_password);
	
		ResultSet rs = w_stmt.executeQuery();
		//To ensure only one user is returned on selection
		int w_count = 0;
		int w_uid = 0;
		String w_loginId = null;
		String w_fname = null;
		String w_lname = null;
		String w_role = null;
		Date w_creationDate = null;
		System.out.println("ghjk");
		while (rs.next()) 
		{
			w_uid = rs.getInt("USERID");
			w_loginId = rs.getString("LOGINID");			
			w_fname = rs.getString("FIRSTNAME");
			w_lname = rs.getString("LASTNAME");
			w_role = rs.getString("ROLE");
			w_creationDate = rs.getDate("CREATIONDATE");
			w_count++;
		}
		
		conn = w_dbConn.closeConnection(conn);
		if(w_count == 1)
		{
			UserObject w_loggedInUser = new UserObject();
			w_loggedInUser.setW_userId(w_uid);
			w_loggedInUser.setW_loginId(w_loginId);
			w_loggedInUser.setW_firstName(w_fname);
			w_loggedInUser.setW_lastName(w_lname);
			w_loggedInUser.setW_role(w_role);
			w_loggedInUser.setW_creationDate(w_creationDate);
				
			return w_loggedInUser;
		} else
		{
			return null;
		}
	}

	/**
	 * This function is used to get list of users from database.
	 * @return arrayList containing list of users
	 * @throws SQLException if the query to get list of users fails
	 */
	public ArrayList<UserObject> getUserList() throws SQLException 
	{
		DatabaseConnection w_dbConn = new DatabaseConnection();
		Connection conn = w_dbConn.getConnection();
		
		String w_sql = "SELECT LOGINID, FIRSTNAME, LASTNAME, ROLE, CREATIONDATE FROM USER_SPK ORDER BY CREATIONDATE DESC, USERID DESC";
		Statement w_stmt = conn.createStatement();
		ResultSet w_rs = w_stmt.executeQuery(w_sql);
		
		ArrayList<UserObject> w_userArray = new ArrayList<UserObject>();
		
		while(w_rs.next())
		{		
			UserObject w_user = new UserObject();
			w_user.setW_loginId(w_rs.getString("LOGINID"));
			w_user.setW_firstName(w_rs.getString("FIRSTNAME"));
			w_user.setW_lastName(w_rs.getString("LASTNAME"));
			w_user.setW_role(w_rs.getString("ROLE"));
			w_user.setW_creationDate(w_rs.getDate("CREATIONDATE"));
				
			w_userArray.add(w_user);	
		}
		
		conn = w_dbConn.closeConnection(conn);
		
		return w_userArray;		
	}

	/**
	 * This function is used for getting the creator of defect
	 * @param w_createdBy which is the userId of User
	 * @return name of tester who created the defect
	 * @throws Exception if the creator of defect is not obtained
	 */
	public String getDefectCreatorName(int w_createdBy) throws Exception 
	{
		DatabaseConnection w_dbConn = new DatabaseConnection();
		Connection conn = w_dbConn.getConnection();
		
		String w_sql = "SELECT FIRSTNAME, LASTNAME FROM USER_SPK WHERE USERID = " + w_createdBy;
		Statement w_stmt = conn.createStatement();
		ResultSet w_rs = w_stmt.executeQuery(w_sql);
		
		String w_creatorName = null;
		while(w_rs.next())
		{		
			w_creatorName = w_rs.getString("FIRSTNAME") + " " + w_rs.getString("LASTNAME");
		}
		
		conn = w_dbConn.closeConnection(conn);
		
		return w_creatorName;
	}

	/**
	 * This function is used to check if the loginId entered while user creation is unique. 
	 * @param w_loginId is used to query the database if it exists or not
	 * @return Unique if loginId is unique or notUnique
	 * @throws SQLException if the loginId is not unique
	 */
	public String getLoginIdStatus(String w_loginId) throws SQLException 
	{
		System.out.println("w_loginId"+ w_loginId);
		String w_loginIDStatus = "unique";
		
		DatabaseConnection w_dbConn = new DatabaseConnection();
		Connection conn = w_dbConn.getConnection();
		
		String w_sql = "SELECT * FROM USER_SPK WHERE LOGINID =" + "'" + w_loginId + "'";
		Statement w_stmt = conn.createStatement();
		ResultSet w_rs = w_stmt.executeQuery(w_sql);
		int w_loginIdCount = 0 ;
		
		while(w_rs.next())
		{	
			w_loginIdCount++;
		}
		
		if(w_loginIdCount > 0)
		{
			w_loginIDStatus = "notUnique";
			return w_loginIDStatus;
		}
		
		return w_loginIDStatus;
	}
}
