package com.android.iliConnect.models;

public class CourseData {

	// Beinhaltet die Action, die im Plugin ausgefuehrt werden soll. 
	// join fuer Kursanmelden und leave fuer Abmelden
	String action;
	
	String userId;
	
	String password;
	
	String urlSrc;
	
	String apiPath;
	
	String courseId;
	
	String coursePw;
	
	
	public CourseData(String action, String userId, String password, String urlSrc, String apiPath, String courseId, String coursePw) {
		super();
		this.action = action;
		this.userId = userId;
		this.password = password;
		this.urlSrc = urlSrc;
		this.apiPath = apiPath;
		this.courseId = courseId;
		this.coursePw = coursePw;
	}


	public String getAction() {
		return action;
	}


	public String getUserId() {
		return userId;
	}


	public String getPassword() {
		return password;
	}


	public String getUrlSrc() {
		return urlSrc;
	}


	public String getApiPath() {
		return apiPath;
	}


	public String getCourseId() {
		return courseId;
	}


	public String getCoursePw() {
		return coursePw;
	}	

	
}
