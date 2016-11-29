package com.defecttracker.user;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserHelper 
{
	/**
	 * This is used to pass the user details to UserSql
	 * @param w_detailsStringJson containing the field values filled by Admin on User Creation Form which is passed to database.
	 * @return success or fail status
	 */
	public JSONObject saveUserHelper(String w_detailsStringJson) 
	{
		JSONObject w_detailsJsonObject;
		JSONObject w_createUserStatus = new JSONObject();
		try 
		{
			w_detailsJsonObject = new JSONObject(w_detailsStringJson);

			JSONArray w_array = w_detailsJsonObject.getJSONArray("detailsData");
			//Passing loginId and password to check if format is correct.
			boolean w_validateFieldStatus = validation((w_array.getJSONObject(0).getString("loginid")), (w_array.getJSONObject(0).getString("password")));
			//setting the parameters to UserObject only if format is correct
			if(w_validateFieldStatus)
			{
				UserObject w_userObject = new UserObject();
	
				w_userObject.setW_loginId((w_array.getJSONObject(0).getString("loginid")));
				w_userObject.setW_firstName((w_array.getJSONObject(0).getString("firstName")));
				w_userObject.setW_lastName((w_array.getJSONObject(0).getString("lastName")));
				w_userObject.setW_password((w_array.getJSONObject(0).getString("password")));
				w_userObject.setW_role((w_array.getJSONObject(0).getString("role")));
	
				Date w_date = new Date();
				w_userObject.setW_creationDate(w_date);
				
				UserSql w_us = new UserSql();
				w_us.createUserSql(w_userObject);
				w_createUserStatus.put("status", "success");
				return w_createUserStatus;
			} else 
			{
				w_createUserStatus.put("status", "failed");
				return w_createUserStatus;
			}
		} catch (Exception e) 
		{
			e.printStackTrace();
			w_createUserStatus.put("status", "failed");
			return w_createUserStatus;
		}
	}

	/**
	 * This function is used to check if parameters are in correct format. 
	 * @param w_loginId is provided by the user
	 * @param w_password is provided by the user
	 * @return true if correct or false.
	 */
	private boolean validation(String w_loginId, String w_password) 
	{
		int w_atpos = w_loginId.indexOf("@");
		int w_dotpos = w_loginId.lastIndexOf(".");
		boolean w_loginIdValidationStatus = (w_atpos < 1 || w_dotpos < w_atpos + 2 || w_dotpos + 2 >= w_loginId.length());
		
		Pattern w_pattern;
		Matcher w_matcher;
		String w_passwordContent = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{4,20})";
		w_pattern = Pattern.compile(w_passwordContent);
		w_matcher = w_pattern.matcher(w_password);
		boolean w_passwordValdationStatus = !w_matcher.matches();
		
		if(w_loginIdValidationStatus || w_passwordValdationStatus)
		{
			return false;
		} else
		{
			return true;
		}
	}

	/**
	 * 	This function is used to check if user with these parameters exist. 
	 * @param w_loginId is provided by the user
	 * @param w_password is provided by the user
	 * @return UserObject if user exists or null.
	 */
	public UserObject userValidation(String w_loginId, String w_password) 
	{
		UserSql w_us = new UserSql();
		UserObject w_loginUser = null;
		try 
		{
			w_loginUser = w_us.validateUser(w_loginId, w_password);
			return w_loginUser;
		} catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 	This function is used for fetching list of all users existing in the database.
	 * @return JsonObject containing data and config array
	 */
	public String fetchUserList() 
	{
		UserSql w_us = new UserSql();
		//creating w_allData json Object for taking data and config
		JSONObject w_allData = new JSONObject();
		// changes made in the below arraylist - arguments added
		ArrayList<UserObject> w_userList;
		try 
		{
			w_userList = w_us.getUserList();
			JSONArray w_userDataArray = new JSONArray();

			for (int i = 0; i < w_userList.size(); i++) 
			{
				UserObject w_user = (UserObject) w_userList.get(i);
				JSONObject w_userJsonObject = new JSONObject();
				//putting each user in separate jsonobject
				w_userJsonObject.put("SrNo.", String.valueOf(i + 1));
				w_userJsonObject.put("Login Id", w_user.getW_loginId());
				w_userJsonObject.put("First Name", w_user.getW_firstName());
				w_userJsonObject.put("Last Name", w_user.getW_lastName());
				w_userJsonObject.put("Role", w_user.getW_role());
				w_userJsonObject.put("Creation Date", w_user.getW_creationDate().toString());

				w_userDataArray.put(w_userJsonObject);
			}
			//Putting the array of user list in a json object as data.
			w_allData.put("data", w_userDataArray);

			//creating jsonArray config 
			JSONArray w_userConfigArray = new JSONArray();
			//creating json objects containing order for ordering at UI level
			JSONObject w_config = new JSONObject();
			w_config.put("name", "SrNo.");
			w_config.put("order", 1);
			w_userConfigArray.put(w_config);

			JSONObject w_config1 = new JSONObject();
			w_config1.put("name", "Login Id");
			w_config1.put("order", 2);
			w_userConfigArray.put(w_config1);

			JSONObject w_config2 = new JSONObject();
			w_config2.put("name", "First Name");
			w_config2.put("order", 3);
			w_userConfigArray.put(w_config2);

			JSONObject w_config3 = new JSONObject();
			w_config3.put("name", "Last Name");
			w_config3.put("order", 4);
			w_userConfigArray.put(w_config3);

			JSONObject w_config4 = new JSONObject();
			w_config4.put("name", "Role");
			w_config4.put("order", 5);
			w_userConfigArray.put(w_config4);

			JSONObject w_config5 = new JSONObject();
			w_config5.put("name", "Creation Date");
			w_config5.put("order", 6);
			w_userConfigArray.put(w_config5);
			
			//adding config to w_allData jsonObject 
			w_allData.put("config", w_userConfigArray);

			return w_allData.toString();
		} catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 	This function is used for rendering the user creation form.
	 * @return JsonObject 
	 */
	public String createUserHelper() 
	{
		JSONArray w_createUserJsonArray = new JSONArray();

		JSONObject obj = new JSONObject();
		obj.put("fieldName", "User ID");
		obj.put("controlType", "text");
		obj.put("status", "disabled");
		obj.put("id", "userid");
		w_createUserJsonArray.put(obj);

		JSONObject obj2 = new JSONObject();
		obj2.put("fieldName", "First Name");
		obj2.put("controlType", "text");
		obj2.put("status", "enabled");
		obj2.put("id", "firstName");
		obj2.put("mandatory", "yes");
		obj2.put("span", "yes");
		w_createUserJsonArray.put(obj2);

		JSONObject obj3 = new JSONObject();
		obj3.put("fieldName", "Last Name");
		obj3.put("controlType", "text");
		obj3.put("status", "enabled");
		obj3.put("id", "lastName");
		obj3.put("mandatory", "yes");
		obj3.put("span", "yes");
		w_createUserJsonArray.put(obj3);

		JSONObject obj1 = new JSONObject();
		obj1.put("fieldName", "Login ID");
		obj1.put("controlType", "email");
		obj1.put("status", "enabled");
		obj1.put("id", "loginid");
		obj1.put("mandatory", "yes");
		obj1.put("span", "yes");
		w_createUserJsonArray.put(obj1);

		JSONObject obj4 = new JSONObject();
		obj4.put("fieldName", "Password");
		obj4.put("controlType", "password");
		obj4.put("status", "enabled");
		obj4.put("id", "password");
		obj4.put("mandatory", "yes");
		obj4.put("span", "yes");
		w_createUserJsonArray.put(obj4);

		JSONObject obj5 = new JSONObject();
		obj5.put("fieldName", "Role");
		obj5.put("controlType", "select");
		obj5.put("value", "Tester,Developer");
		obj5.put("status", "enabled");
		obj5.put("id", "role");
		obj5.put("mandatory", "yes");
		w_createUserJsonArray.put(obj5);

		JSONObject obj8 = new JSONObject();
		obj8.put("fieldName", "Creation Date");
		obj8.put("controlType", "date");
		obj8.put("status", "disabled");
		obj8.put("id", "creationdate");
		w_createUserJsonArray.put(obj8);

		JSONObject obj9 = new JSONObject();
		obj9.put("fieldName", "FormName");
		obj9.put("controlType", "hidden");
		obj9.put("value", "CreateUser");
		obj9.put("id", "formname");
		w_createUserJsonArray.put(obj9);

		//adding onblur key and its value to every object
		for (int i = 0; i < w_createUserJsonArray.length(); i++) 
		{
			JSONObject w_fieldObj = w_createUserJsonArray.getJSONObject(i);
			String w_id = w_createUserJsonArray.getJSONObject(i).getString("id");
			w_fieldObj.put("onblur", "createUserValidation(" + w_id + ")");
		}

		return w_createUserJsonArray.toString();
	}

	/**
	 * This function is used to check if the loginId enter by Admin while user creation is unique.
	 * @param w_loginId provided by the Admin while user creation
	 * @return Unique or null
	 */
	public String uniqueLoginIdValidation(String w_loginId) 
	{
		try 
		{
			UserSql w_us = new UserSql();

			String w_loginIDStatus = w_us.getLoginIdStatus(w_loginId);
			return w_loginIDStatus;
		} catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
}
