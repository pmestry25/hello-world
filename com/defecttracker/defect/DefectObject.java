package com.defecttracker.defect;

import java.util.Date;

public class DefectObject {

	private int w_defectId;
	private String w_name;
	private String w_description;
	private String w_priority;
	private String w_overallStatus;
	private String w_showStopper;
	private String w_resolution;
	private String w_resolved;
	private int w_currentUser;
	private int w_createdBy;
	private int w_sequenceNo;
	private Date w_dateIdentified;
	private Date w_dateClosed;

	public int getW_defectId() {
		return w_defectId;
	}

	public void setW_defectId(int w_defectId) {
		this.w_defectId = w_defectId;
	}

	public String getW_name() {
		return w_name;
	}

	public void setW_name(String w_name) {
		this.w_name = w_name;
	}

	public String getW_description() {
		return w_description;
	}

	public void setW_description(String w_description) {
		this.w_description = w_description;
	}

	public String getW_priority() {
		return w_priority;
	}

	public void setW_priority(String w_priority) {
		this.w_priority = w_priority;
	}

	public String getW_overallStatus() {
		return w_overallStatus;
	}

	public void setW_overallStatus(String w_overallStatus) {
		this.w_overallStatus = w_overallStatus;
	}

	public String getW_showStopper() {
		return w_showStopper;
	}

	public void setW_showStopper(String w_showStopper) {
		this.w_showStopper = w_showStopper;
	}

	public String getW_resolution() {
		return w_resolution;
	}

	public void setW_resolution(String w_resolution) {
		this.w_resolution = w_resolution;
	}

	public String getW_resolved() {
		return w_resolved;
	}

	public void setW_resolved(String w_resolved) {
		this.w_resolved = w_resolved;
	}

	public int getW_currentUser() {
		return w_currentUser;
	}

	public void setW_currentUser(int w_currentUser) {
		this.w_currentUser = w_currentUser;
	}

	public int getW_createdBy() {
		return w_createdBy;
	}

	public void setW_createdBy(int w_createdBy) {
		this.w_createdBy = w_createdBy;
	}

	public int getW_sequenceNo() {
		return w_sequenceNo;
	}

	public void setW_sequenceNo(int w_sequenceNo) {
		this.w_sequenceNo = w_sequenceNo;
	}

	public Date getW_dateIdentified() {
		return w_dateIdentified;
	}

	public void setW_dateIdentified(Date w_dateIdentified) {
		this.w_dateIdentified = w_dateIdentified;
	}

	public Date getW_dateClosed() {
		return w_dateClosed;
	}

	public void setW_dateClosed(Date w_dateClosed) {
		this.w_dateClosed = w_dateClosed;
	}

}


