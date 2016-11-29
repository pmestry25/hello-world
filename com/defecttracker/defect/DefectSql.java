package com.defecttracker.defect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.defecttracker.connection.DatabaseConnection;
import com.defecttracker.user.UserObject;

public class DefectSql 
{
	/**
	 * This function is used to insert or update a defect 
	 * @param w_defectObject contains all parameters of a defect
	 * @param w_query decides whether to fire insert or update query
	 * @return defectId of the inserted or updated defect
	 * @throws SQLException whenever the insert or update fails
	 */
	public int createDefectSql(DefectObject w_defectObject, String w_query) throws SQLException 
	{
		DatabaseConnection w_dbConn = new DatabaseConnection();
		Connection conn = w_dbConn.getConnection();

		String sql;
		//converting java date into sql date
		java.sql.Date sqlDate = new java.sql.Date(w_defectObject.getW_dateIdentified().getTime());
		java.sql.Date sqlDateClosed = new java.sql.Date(w_defectObject.getW_dateClosed().getTime());
		
		//if w_query is update, update query is fired or insert query is fired
		if(w_query.equalsIgnoreCase("update"))
		{
			if(w_defectObject.getW_overallStatus() == "Closed")
			{
				sql = "update defect_spk set NAME = ?, DESCRIPTION =?, PRIORITY = ?, OVERALLSTATUS = ?, DATEIDENTIFIED= ?, SHOWSTOPPER = ?, RESOLUTION = ?, RESOLVED = ?, CURRENTUSER=? , CREATEDBY =?, SEQUENCENO = ?, DATECLOSED= ? where DEFECTID = " + w_defectObject.getW_defectId();
			} else 
			{
				sql = "update defect_spk set NAME = ?, DESCRIPTION =?, PRIORITY = ?, OVERALLSTATUS = ?, DATEIDENTIFIED= ?, SHOWSTOPPER = ?, RESOLUTION = ?, RESOLVED = ?, CURRENTUSER=? , CREATEDBY =?, SEQUENCENO = ? where DEFECTID = " + w_defectObject.getW_defectId();
			}
		} else
		{
			sql = "insert into defect_spk (DEFECTID, NAME, DESCRIPTION, PRIORITY, OVERALLSTATUS, DATEIDENTIFIED, SHOWSTOPPER, RESOLUTION, RESOLVED, CURRENTUSER, CREATEDBY, SEQUENCENO) values (defectseq.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";			
		}
		
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, w_defectObject.getW_name());
		stmt.setString(2, w_defectObject.getW_description());
		stmt.setString(3, w_defectObject.getW_priority());
		stmt.setString(4, w_defectObject.getW_overallStatus());
		stmt.setDate(5, sqlDate);
		stmt.setString(6, w_defectObject.getW_showStopper());
		stmt.setString(7, w_defectObject.getW_resolution());
		stmt.setString(8, w_defectObject.getW_resolved());
		stmt.setInt(9, w_defectObject.getW_currentUser());
		stmt.setInt(10, w_defectObject.getW_createdBy());
		stmt.setInt(11, w_defectObject.getW_sequenceNo());
	
		
		if(w_query.equalsIgnoreCase("update") && w_defectObject.getW_overallStatus() == "Closed")
		{
			stmt.setDate(12, sqlDateClosed);
		}
		stmt.executeUpdate();

		if(w_query.equalsIgnoreCase("update"))
		{
			return w_defectObject.getW_defectId();
		} else
		{
			String w_sql2 = "SELECT MAX(DEFECTID) AS CURRENT_DEFECT_ID FROM DEFECT_SPK";
			Statement w_stmt = conn.createStatement();
			ResultSet w_rs = w_stmt.executeQuery(w_sql2);
			int w_defectId = 0;
			while (w_rs.next()) 
			{
				w_defectId = w_rs.getInt("CURRENT_DEFECT_ID");
			}
			
			conn = w_dbConn.closeConnection(conn);
			return w_defectId;
		}
	}

	/**
	 * This function is used for getting the list of defects according to list type
	 * @param w_userId to get the defect according to the userId
	 * @param w_listType i.e allDefects or Inbox
	 * @return arrayList of defectObject and userObject
	 * @throws SQLException when the query to bring the list of defect fails
	 */
	public ArrayList<Object> getDefectList(int w_userId, String w_listType) throws SQLException 
	{
		DatabaseConnection w_dbConn = new DatabaseConnection();
		Connection conn = w_dbConn.getConnection();
		String w_sql = null;
		
		if(w_listType.equalsIgnoreCase("inbox"))
		{
			w_sql = "SELECT DEFECTID, NAME, PRIORITY, OVERALLSTATUS, CURRENTUSER, CREATEDBY, FIRSTNAME, LASTNAME FROM DEFECT_SPK , USER_SPK WHERE OVERALLSTATUS='Open' AND CURRENTUSER =" + w_userId +" AND defect_spk.createdby = user_spk.userid ORDER BY SHOWSTOPPER DESC, DEFECTID DESC";		
		} else 
		{
			//w_sql = "SELECT DEFECTID, NAME, PRIORITY, OVERALLSTATUS, CURRENTUSER, CREATEDBY FROM DEFECT_SPK ORDER BY DEFECTID DESC";	
			w_sql = "SELECT DEFECTID, NAME, PRIORITY, OVERALLSTATUS, CURRENTUSER, CREATEDBY ,FIRSTNAME,LASTNAME FROM defect_spk INNER JOIN USER_SPK ON defect_spk.createdby = user_spk.userid ORDER BY DEFECTID DESC";
		}
		Statement w_stmt = conn.createStatement();
		ResultSet w_rs = w_stmt.executeQuery(w_sql);

		ArrayList<Object> w_defectArray = new ArrayList<>();

		while (w_rs.next()) 
		{
			DefectObject w_defect = new DefectObject();
			UserObject w_user  = new UserObject();
			w_defect.setW_defectId(w_rs.getInt("DEFECTID"));
			w_defect.setW_name(w_rs.getString("NAME"));
			w_defect.setW_priority(w_rs.getString("PRIORITY"));
			w_defect.setW_overallStatus(w_rs.getString("OVERALLSTATUS"));
			w_defect.setW_currentUser(w_rs.getInt("CURRENTUSER"));
			w_defect.setW_createdBy(w_rs.getInt("CREATEDBY"));
			
			w_user.setW_firstName(w_rs.getString("FIRSTNAME"));
			w_user.setW_lastName(w_rs.getString("LASTNAME"));
			
			w_defectArray.add(w_defect);
			w_defectArray.add(w_user);
		}
		
		conn = w_dbConn.closeConnection(conn);
		
		return w_defectArray;
	}

	/**
	 * This function is used to get the list of developers from database
	 * @return arrayList of userObjects 
	 * @throws SQLException whenever the query to get the list of developers fails 
	 */
	public ArrayList<UserObject> developerList() throws SQLException 
	{
		DatabaseConnection w_dbConn = new DatabaseConnection();
		Connection conn = w_dbConn.getConnection();
		
		String w_sql = "SELECT USERID, LOGINID, FIRSTNAME, LASTNAME FROM USER_SPK WHERE ROLE ='Developer'";		
		
		Statement w_stmt = conn.createStatement();
		ResultSet w_rs = w_stmt.executeQuery(w_sql);
		
		ArrayList<UserObject> w_userArray = new ArrayList<UserObject>();
		
		while(w_rs.next())
		{		
			UserObject w_user = new UserObject();
			w_user.setW_userId(w_rs.getInt("USERID"));
			w_user.setW_loginId(w_rs.getString("LOGINID"));
			w_user.setW_firstName(w_rs.getString("FIRSTNAME"));
			w_user.setW_lastName(w_rs.getString("LASTNAME"));
			
			w_userArray.add(w_user);	
		}
		
		conn = w_dbConn.closeConnection(conn);
		return w_userArray;
	}

	/**
	 * This function is used to get a particular defect
	 * @param w_defectId used to get the defect
	 * @return defectObject
	 * @throws SQLException when the query to get the particular defect fails
	 */
	public DefectObject getSelectedDefectSql(int w_defectId) throws SQLException 
	{

		DatabaseConnection w_dbConn = new DatabaseConnection();
		Connection conn = w_dbConn.getConnection();
		
		String w_sql = "SELECT * FROM DEFECT_SPK WHERE DEFECTID =" + w_defectId;		
		
		Statement w_stmt = conn.createStatement();
		ResultSet w_rs = w_stmt.executeQuery(w_sql);
		
		DefectObject w_defect = new DefectObject();
		
		while(w_rs.next())
		{		
			w_defect.setW_defectId(w_rs.getInt("DEFECTID"));
			w_defect.setW_name(w_rs.getString("NAME"));
			w_defect.setW_priority(w_rs.getString("PRIORITY"));
			w_defect.setW_overallStatus(w_rs.getString("OVERALLSTATUS"));
			w_defect.setW_currentUser(w_rs.getInt("CURRENTUSER"));
			w_defect.setW_createdBy(w_rs.getInt("CREATEDBY"));
			w_defect.setW_dateIdentified(w_rs.getDate("DATEIDENTIFIED"));
			w_defect.setW_dateClosed(w_rs.getDate("DATECLOSED"));
			w_defect.setW_description(w_rs.getString("DESCRIPTION"));
			w_defect.setW_resolution(w_rs.getString("RESOLUTION"));
			w_defect.setW_resolved(w_rs.getString("RESOLVED"));
			w_defect.setW_showStopper(w_rs.getString("SHOWSTOPPER"));
		}
		conn = w_dbConn.closeConnection(conn);
		return w_defect;
	}

	/**
	 * This function getting the current user of a defect
	 * @param w_currentUser is the userId of the user
	 * @return current user name
	 * @throws SQLException whenever the query to get current user of the defect fails
	 */
	public String getCurrentUserName(int w_currentUser) throws SQLException 
	{
		DatabaseConnection w_dbConn = new DatabaseConnection();
		Connection conn = w_dbConn.getConnection();
		
		String w_sql = "SELECT * FROM USER_SPK WHERE USERID =" + w_currentUser;		
		
		Statement w_stmt = conn.createStatement();
		ResultSet w_rs = w_stmt.executeQuery(w_sql);
		
		String w_currentUserName = null;
		while(w_rs.next())
		{		
			w_currentUserName = w_rs.getString("FIRSTNAME") + " " + w_rs.getString("LASTNAME");
		}
		conn = w_dbConn.closeConnection(conn);
		return w_currentUserName;
	}
}
