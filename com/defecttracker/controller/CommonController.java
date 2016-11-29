package com.defecttracker.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import com.defecttracker.defect.DefectHelper;
import com.defecttracker.user.UserHelper;
import com.defecttracker.user.UserObject;

public class CommonController extends HttpServlet 
{
	HttpSession w_session = null;
	UserObject w_user = null;
	
	private static final long serialVersionUID = 1L;


	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		String w_action = req.getParameter("action");

		//For user login validation.If login successful redirect to base.html
		if ("login".equals(w_action)) 
		{
			String w_loginId = req.getParameter("loginid");
			String w_password = req.getParameter("password");
System.out.println("w_loginId: " + w_loginId + "  w_password: " + w_password);
			UserHelper w_userHelper = new UserHelper();
			
			w_user = w_userHelper.userValidation(w_loginId, w_password);
			if (w_user != null) 
			{
				w_session = req.getSession();//Create session
				w_session.setAttribute("userobject", w_user);//Add loggedIn user details in session object.
					
				SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
		        String w_loggedInTime = localDateFormat.format(new Date());
					
				w_session.setAttribute("loggedInTime", w_loggedInTime);
				if("bootstrap".equalsIgnoreCase(req.getParameter("bootstrap"))){
					resp.sendRedirect("base.html");
				}else{
					resp.sendRedirect("base.html");
				}			
				
			} else 
			{
				resp.sendRedirect("loginBoot.html?success=no");//If login unsuccessful redirect to login.html and success parameter to show invalid value message to user.
			}
		}
		//To pass logged in user details to userAndDefect.js to show details on header
		if ("loginUser".equalsIgnoreCase(w_action)) 
		{
			//If user session is null then directly redirect to login page 
			if(w_session != null)
			{
				JSONObject w_loginUser = new JSONObject();
				String w_userName = w_user.getW_firstName() + " " + w_user.getW_lastName();
				String w_role = w_user.getW_role();
				w_loginUser.put("name", w_userName);
				w_loginUser.put("role", w_role);			
				w_loginUser.put("loggedInTime", w_session.getAttribute("loggedInTime"));
				w_loginUser.put("loginId", w_user.getW_loginId());
				resp.getWriter().write(w_loginUser.toString());
			} else
			{
				JSONObject w_loginSession = new JSONObject();
				w_loginSession.put("pageName", "Login");
				resp.getWriter().write(w_loginSession.toString());
			}
		}
		
		//To get list to render from database and pass to userAndDefect.js for rendering
		if ("render".equals(w_action)) 
		{

			UserObject w_loginUserDetails = (UserObject) (w_session.getAttribute("userobject"));

			String w_userRole = w_loginUserDetails.getW_role();
			//If role is Admin then get user list else get defect list
			if ("Admin".equalsIgnoreCase(w_userRole)) 
			{
				UserHelper w_userHelper = new UserHelper();
				String w_userList = w_userHelper.fetchUserList();
				resp.getWriter().write(w_userList);
			} else 
			{
				DefectHelper w_defectHelper = new DefectHelper();
				String w_defectList = w_defectHelper.fetchDefectList(w_loginUserDetails,"inbox");
				resp.getWriter().write(w_defectList);
			}
		}

		//To get all defects list from database
		if ("allDefects".equals(w_action)) 
		{
			UserObject w_loginUserDetails = (UserObject) (w_session.getAttribute("userobject"));
			DefectHelper w_defectHelper = new DefectHelper();
			String w_defectList = null;
			w_defectList = w_defectHelper.fetchDefectList(w_loginUserDetails,"AllDefects");
			resp.getWriter().write(w_defectList);
		}
				
		//To get the create user form for new user creation 
		if ("createUser".equals(w_action)) 
		{
			UserHelper w_userHelper = new UserHelper();
			String w_createUser = w_userHelper.createUserHelper();
			resp.getWriter().write(w_createUser);
		}
		
		//To check entered login Id is unique or not while user creation.
		if ("uniqueLoginId".equals(w_action)) 
		{
			String w_loginId = req.getParameter("loginId");
			UserHelper w_userHelper = new UserHelper();
			String w_loginIdStatus = w_userHelper.uniqueLoginIdValidation(w_loginId);
			resp.getWriter().write(w_loginIdStatus);
		}

		//To get create defect form for new defect creation
		if ("createDefect".equals(w_action)) 
		{
			UserObject w_userObject = (UserObject) w_session.getAttribute("userobject");
			if("Admin".equalsIgnoreCase(w_userObject.getW_role()))
			{
				JSONObject w_editUser = new JSONObject();
				w_editUser.put("editUser", "editUser");
				resp.getWriter().write(w_editUser.toString());
			} else 
			{

				String w_mode = req.getParameter("mode");
				int w_defectIdInt = 0;
				//If mode create form mode is edit then get defect id of that editable defect
				if ("edit".equalsIgnoreCase(w_mode)) 
				{
					String w_defectId = req.getParameter("defectId");
					w_defectIdInt = Integer.parseInt(w_defectId);
				}

				DefectHelper w_defectHelper = new DefectHelper();

				JSONArray w_createdefect = w_defectHelper.createDefectHelper(w_mode, w_defectIdInt, w_user);
				
				resp.setStatus(200);
				resp.getWriter().write(w_createdefect.toString());
			}			
		}

		//To save details of form in database during user creation and defect creation
		if ("saveDetails".equals(w_action)) 
		{
			String w_detailsJson = req.getParameter("json");
			String w_formName = req.getParameter("formname");
			if ("CreateUser".equalsIgnoreCase(w_formName)) 
			{
				UserHelper w_userHelper = new UserHelper();
				JSONObject w_usercreationResponse = w_userHelper.saveUserHelper(w_detailsJson);
	
				resp.getWriter().write(w_usercreationResponse.toString());
			} else 
			{
				int w_defectId;
				String w_query = req.getParameter("query");
				int w_createdBy = w_user.getW_userId();
				DefectHelper w_defectHelper = new DefectHelper();
				w_defectId = w_defectHelper.saveDefectHelper(w_detailsJson, w_createdBy, w_query);
				JSONObject w_detailsFromDb = new JSONObject();
				//If defectId from database is grater than 0 then user creation is successful and pass that id into json for editable mode of newly created defect
				if(w_defectId != 0){
					w_detailsFromDb.put("defectid", w_defectId);
				} else 
				{
					w_detailsFromDb.put("status", "failed");//If defect creation failed due to some database problem
				}
				resp.getWriter().write(w_detailsFromDb.toString());
			}
		}
		//If logout button clicked then invalidate existing user session 
		if ("logout".equals(w_action)) 
		{
			w_session.invalidate();
			w_session = null;
		}
				
	}
}
