package com.defecttracker.user;

import java.util.Date;

public class UserObject {

	private int w_userId;
	private String w_loginId;
	private String w_firstName;
	private String w_lastName;
	private String w_password;
	private String w_role;
	private Date w_creationDate;

	public int getW_userId() {
		return w_userId;
	}

	public void setW_userId(int w_userId) {
		this.w_userId = w_userId;
	}

	public String getW_loginId() {
		return w_loginId;
	}

	public void setW_loginId(String w_loginId) {
		this.w_loginId = w_loginId;
	}

	public String getW_firstName() {
		return w_firstName;
	}

	public void setW_firstName(String w_firstName) {
		this.w_firstName = w_firstName;
	}

	public String getW_lastName() {
		return w_lastName;
	}

	public void setW_lastName(String w_lastName) {
		this.w_lastName = w_lastName;
	}

	public String getW_password() {
		return w_password;
	}

	public void setW_password(String w_password) {
		this.w_password = w_password;
	}

	public String getW_role() {
		return w_role;
	}

	public void setW_role(String w_role) {
		this.w_role = w_role;
	}

	public Date getW_creationDate() {
		return w_creationDate;
	}

	public void setW_creationDate(Date w_creationDate) {
		this.w_creationDate = w_creationDate;
	}

}