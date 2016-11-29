package com.defecttracker.defect;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import com.defecttracker.user.UserObject;
import com.defecttracker.user.UserSql;

public class DefectHelper 
{
	/**
	 * This function is used to get defect list from database for loggedIn user inbox and all defects listing
	 * @param w_loginUserDetails parameter is used to get current user name of defects in inbox
	 * @param w_listType is used to set current user attribute of defects in all defects listing
	 * @return JSONObject string of defect listing data
	 */
	public String fetchDefectList(UserObject w_loginUserDetails, String w_listType) 
	{

		DefectSql w_defectSql = new DefectSql();
		ArrayList<?> w_defectList;
		try 
		{
			w_defectList = w_defectSql.getDefectList(w_loginUserDetails.getW_userId(),w_listType);

			JSONObject w_allData = new JSONObject();

			JSONArray w_defectJsonArray = new JSONArray();
			int w_srNoCount = 0;
			for (int i = 0; i < w_defectList.size(); i=i+2) 
			{
				DefectObject w_defect = (DefectObject) w_defectList.get(i);//To get defect object from arraylist-w_defectList.
				UserObject w_user = (UserObject) w_defectList.get(i+1);//To get user object from arraylist-w_defectList.
				
				JSONObject w_defectJsonObject = new JSONObject();
				w_defectJsonObject.put("SrNo.", String.valueOf(++w_srNoCount));
				w_defectJsonObject.put("DefectID", w_defect.getW_defectId());
				w_defectJsonObject.put("Name", w_defect.getW_name());
				w_defectJsonObject.put("Priority", w_defect.getW_priority());
				w_defectJsonObject.put("Status", w_defect.getW_overallStatus());
				
				//If list type is all defects then get current user name from database otherwise current user name is same as logged in user name
				if(w_listType.equalsIgnoreCase("AllDefects"))
				{
					String w_currentUserName = w_defectSql.getCurrentUserName(w_defect.getW_currentUser());
					w_defectJsonObject.put("CurrentUser", w_currentUserName);
				}else
				{
					w_defectJsonObject.put("CurrentUser",w_loginUserDetails.getW_firstName() + " " + w_loginUserDetails.getW_lastName());					
				}
				//If current user and created by user is same then set created by attribute to current user name;
				if (w_defect.getW_currentUser() == w_defect.getW_createdBy()) 
				{					
					w_defectJsonObject.put("CreatedBy",w_loginUserDetails.getW_firstName() + " " + w_loginUserDetails.getW_lastName());
				}
				else
				{
					w_defectJsonObject.put("CreatedBy",w_user.getW_firstName() + " " + w_user.getW_lastName());
				}

				w_defectJsonArray.put(w_defectJsonObject);
			}


			w_allData.put("data", w_defectJsonArray);

			//to order column name in defect listing
			JSONArray w_userConfigArray = new JSONArray();

			JSONObject w_config = new JSONObject();
			w_config.put("name", "SrNo.");
			w_config.put("order", 1);
			w_userConfigArray.put(w_config);

			JSONObject w_config1 = new JSONObject();
			w_config1.put("name", "DefectID");
			w_config1.put("order", 2);
			w_userConfigArray.put(w_config1);

			JSONObject w_config2 = new JSONObject();
			w_config2.put("name", "Name");
			w_config2.put("order", 3);
			w_userConfigArray.put(w_config2);

			JSONObject w_config3 = new JSONObject();
			w_config3.put("name", "Priority");
			w_config3.put("order", 4);
			w_userConfigArray.put(w_config3);

			JSONObject w_config4 = new JSONObject();
			w_config4.put("name", "Status");
			w_config4.put("order", 5);
			w_userConfigArray.put(w_config4);

			JSONObject w_config5 = new JSONObject();
			w_config5.put("name", "CurrentUser");
			w_config5.put("order", 6);
			w_userConfigArray.put(w_config5);

			JSONObject w_config6 = new JSONObject();
			w_config6.put("name", "CreatedBy");
			w_config6.put("order", 7);
			w_userConfigArray.put(w_config6);

			w_allData.put("config", w_userConfigArray);

			
			return w_allData.toString();
		} catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}

	}
	
	/**
	 * This function is used to create, fields of create defect form 
	 * and if particular defect is selected pass that details of defect in json.
	 * @param w_mode to check create defect form is in edit mode or create mode
	 * @param w_defectId to get selected defect details from database on the basis of defect id
	 * @param w_user to set current user as logged in user by default
	 * @return JsonArray of Create Defect form fields
	 */

	public JSONArray createDefectHelper(String w_mode, int w_defectId,
			UserObject w_user) 
	{

		DefectSql w_defectSql = new DefectSql();

		ArrayList<UserObject> w_developerList;
		try 
		{
			w_developerList = w_defectSql.developerList();

			JSONArray w_developerListArray = new JSONArray();

			JSONObject w_currentTester = new JSONObject();

			w_currentTester.put("developerName", w_user.getW_firstName() + " " + w_user.getW_lastName());
			w_currentTester.put("developerValue", w_user.getW_userId());

			w_developerListArray.put(w_currentTester);

			for (int i = 0; i < w_developerList.size(); i++) 
			{

				UserObject w_userObject = (UserObject) w_developerList.get(i);
				JSONObject w_developerListObject = new JSONObject();

				w_developerListObject.put("developerName",w_userObject.getW_firstName() + " " + w_userObject.getW_lastName());
				w_developerListObject.put("developerValue",String.valueOf(w_userObject.getW_userId()));

				w_developerListArray.put(w_developerListObject);
			}


			DefectObject w_defectObject = w_defectSql.getSelectedDefectSql(w_defectId);

			JSONArray w_createDefectJsonArray = new JSONArray();

			JSONObject obj = new JSONObject();
			obj.put("fieldName", "Defect ID");
			obj.put("controlType", "number");
			obj.put("status", "disabled");
			obj.put("id", "defectid");

			w_createDefectJsonArray.put(obj);

			JSONObject obj1 = new JSONObject();
			obj1.put("fieldName", "Name");
			obj1.put("controlType", "text");
			obj1.put("status", "enabled");
			obj1.put("id", "name");
			obj1.put("mandatory", "yes");
			obj1.put("span", "yes");

			w_createDefectJsonArray.put(obj1);

			JSONObject obj2 = new JSONObject();
			obj2.put("fieldName", "Status");
			obj2.put("controlType", "select");
			obj2.put("status", "disabled");
			obj2.put("value", "Open,Closed");
			obj2.put("id", "status");

			w_createDefectJsonArray.put(obj2);

			JSONObject obj3 = new JSONObject();
			obj3.put("fieldName", "Priority");
			obj3.put("controlType", "select");
			obj3.put("status", "enabled");
			obj3.put("value", "Critical,High,Medium,Low");
			obj3.put("id", "priority");
			obj3.put("mandatory", "yes");

			w_createDefectJsonArray.put(obj3);

			JSONObject obj4 = new JSONObject();
			obj4.put("fieldName", "Date Identified");
			obj4.put("controlType", "date");
			obj4.put("status", "enabled");
			obj4.put("id", "dateidentified");
			obj4.put("mandatory", "yes");
			obj4.put("span", "yes");
			w_createDefectJsonArray.put(obj4);

			JSONObject obj5 = new JSONObject();
			obj5.put("fieldName", "Date Closed");
			obj5.put("controlType", "date");
			obj5.put("status", "disabled");
			obj5.put("id", "dateclosed");

			w_createDefectJsonArray.put(obj5);

			JSONObject obj6 = new JSONObject();
			obj6.put("fieldName", "Current User");
			obj6.put("controlType", "select");
			obj6.put("status", "enabled");
			obj6.put("id", "currentuser");
			obj6.put("value", w_developerListArray);

			w_createDefectJsonArray.put(obj6);

			JSONObject obj7 = new JSONObject();
			obj7.put("fieldName", "Show Stopper");
			obj7.put("controlType", "radio");
			obj7.put("status", "enabled");
			obj7.put("value", "Y,N");
			obj7.put("id", "showstopper");

			w_createDefectJsonArray.put(obj7);

			JSONObject obj8 = new JSONObject();
			obj8.put("fieldName", "Description");
			obj8.put("controlType", "text");
			obj8.put("status", "enabled");
			obj8.put("id", "description");
			obj8.put("mandatory", "yes");
			obj8.put("span", "yes");
			w_createDefectJsonArray.put(obj8);

			JSONObject obj9 = new JSONObject();
			obj9.put("fieldName", "Resolution");
			obj9.put("controlType", "text");
			obj9.put("status", "disabled");
			obj9.put("id", "resolution");
			obj9.put("span", "yes");

			w_createDefectJsonArray.put(obj9);

			JSONObject obj10 = new JSONObject();
			obj10.put("fieldName", "Resolved");
			obj10.put("controlType", "checkbox");
			obj10.put("status", "disabled");
			obj10.put("id", "resolved");

			w_createDefectJsonArray.put(obj10);

			JSONObject obj11 = new JSONObject();
			obj11.put("fieldName", "Created By");
			obj11.put("controlType", "text");
			obj11.put("status", "disabled");
			obj11.put("id", "createdby");

			if ("create".equalsIgnoreCase(w_mode)) 
			{
				obj11.put("value",w_user.getW_firstName() + " " + w_user.getW_lastName());
			}

			w_createDefectJsonArray.put(obj11);

			JSONObject obj12 = new JSONObject();
			obj12.put("fieldName", "Initiator");
			obj12.put("controlType", "text");
			obj12.put("status", "disabled");
			obj12.put("id", "initiator");

			if ("create".equalsIgnoreCase(w_mode)) 
			{
				obj12.put("value",w_user.getW_firstName() + " " + w_user.getW_lastName());
			}

			w_createDefectJsonArray.put(obj12);

			JSONObject obj13 = new JSONObject();
			obj13.put("fieldName", "Developer");
			obj13.put("controlType", "text");
			obj13.put("status", "enabled");
			obj13.put("id", "developer");

			w_createDefectJsonArray.put(obj13);

			JSONObject obj14 = new JSONObject();
			obj14.put("fieldName", "Validator");
			obj14.put("controlType", "text");
			obj14.put("status", "enabled");
			obj14.put("id", "validator");

			w_createDefectJsonArray.put(obj14);

			JSONObject obj15 = new JSONObject();
			obj15.put("fieldName", "FormName");
			obj15.put("controlType", "hidden");
			obj15.put("value", "CreateDefect");
			obj15.put("id", "formname");
			w_createDefectJsonArray.put(obj15);

			for (int i = 0; i < w_createDefectJsonArray.length(); i++) 
			{

				JSONObject w_fieldObj = w_createDefectJsonArray.getJSONObject(i);
				String w_id = w_createDefectJsonArray.getJSONObject(i).getString("id");
				w_fieldObj.put("onblur", "createDefectValidation(" + w_id + ")");

			}

			if ("edit".equalsIgnoreCase(w_mode)) 
			{

				obj.put("fieldValue",String.valueOf(w_defectObject.getW_defectId()));
				obj1.put("fieldValue", w_defectObject.getW_name());
				obj2.put("fieldValue", w_defectObject.getW_overallStatus());
				obj3.put("fieldValue", w_defectObject.getW_priority());
				obj4.put("fieldValue",String.valueOf(w_defectObject.getW_dateIdentified()));

				if ("closed".equalsIgnoreCase(w_defectObject.getW_overallStatus())) 
				{
					obj5.put("fieldValue",String.valueOf(w_defectObject.getW_dateClosed()));
				} else {
					obj5.put("fieldValue", "");
				}

				obj6.put("fieldValue", w_defectObject.getW_currentUser());
				obj7.put("fieldValue", w_defectObject.getW_showStopper());
				obj8.put("fieldValue", w_defectObject.getW_description());
				obj9.put("fieldValue", w_defectObject.getW_resolution());
				obj10.put("fieldValue", w_defectObject.getW_resolved());
				UserSql w_createdByName = new UserSql();

				String w_defectCreatorName = w_createdByName.getDefectCreatorName(w_defectObject.getW_createdBy());
				obj11.put("fieldValue", w_defectCreatorName);
				obj12.put("fieldValue", w_defectCreatorName);
				obj13.put("fieldValue", "Developer");
				obj14.put("fieldValue", "Validator");

				if ("closed".equalsIgnoreCase(w_defectObject.getW_overallStatus())|| ("Developer".equalsIgnoreCase(w_user.getW_role()))) 
				{

					for (int i = 0; i < w_createDefectJsonArray.length(); i++) 
					{

						JSONObject w_fieldObj = w_createDefectJsonArray
								.getJSONObject(i);
						w_fieldObj.put("status", "disabled");
					}

				}

				if ("open".equalsIgnoreCase(w_defectObject.getW_overallStatus())) 
				{

					obj15.put("defectName", w_defectObject.getW_name());
					if ("Developer".equalsIgnoreCase(w_user.getW_role())) 
					{
						obj9.put("status", "enabled");
						obj10.put("status", "enabled");
					} else {
						obj5.put("status", "enabled");
					}
				}

			}


			return w_createDefectJsonArray;
		} catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * This function is used to pass details of defect to database 
	 * @param w_defectDetailsJson brings the Json of Defect's details from JS
	 * @param  w_createdBy is used to set the createdBy attribute of Defect as logged in user's userId
	 * @param w_query to check whether the defect is already created or is being created
	 * @return defectId of already created defect or newly created defect
	 */
	
	public int saveDefectHelper(String w_defectDetailsJson, int w_createdBy,
			String w_query) 
	{


		JSONObject w_detailsJsonObject;
		try 
		{
			w_detailsJsonObject = new JSONObject(w_defectDetailsJson);

			JSONArray w_array = w_detailsJsonObject.getJSONArray("detailsData");

			DefectObject w_defectObject = new DefectObject();

			if (w_query.equalsIgnoreCase("update")) 
			{

				w_defectObject.setW_defectId(Integer.parseInt((w_array.getJSONObject(0).getString("defectid"))));
			
			}

			w_defectObject.setW_name((w_array.getJSONObject(0).getString("name")));
			w_defectObject.setW_overallStatus((w_array.getJSONObject(0).getString("status")));
			w_defectObject.setW_priority((w_array.getJSONObject(0).getString("priority")));

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date w_dateIdentified = formatter.parse(w_array.getJSONObject(0)
					.getString("dateidentified"));
			
			w_defectObject.setW_dateIdentified(w_dateIdentified);

			w_defectObject.setW_currentUser(Integer.parseInt((w_array.getJSONObject(0).getString("currentuser"))));
			w_defectObject.setW_showStopper((w_array.getJSONObject(0).getString("showstopper")));
			w_defectObject.setW_description(w_array.getJSONObject(0).getString("description"));
			w_defectObject.setW_resolution((w_array.getJSONObject(0).getString("resolution")));
			w_defectObject.setW_resolved((w_array.getJSONObject(0).getString("resolved")));
			w_defectObject.setW_createdBy(w_createdBy);
			w_defectObject.setW_sequenceNo(0000);

			// Passing date closed
			Date w_dateClosed = new Date();
			w_defectObject.setW_dateClosed(w_dateClosed);
			
			DefectSql w_us = new DefectSql();

			int w_defectId = w_us.createDefectSql(w_defectObject, w_query);
			return w_defectId;
		} catch (Exception e) 
		{
			e.printStackTrace();
			return 0;
		}
	}
}
