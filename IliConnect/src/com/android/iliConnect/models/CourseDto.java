package com.android.iliConnect.models;

public class CourseDto {

	// Beinhaltet die Action, die im Plugin ausgeführt werden soll. 
	// join für Kursanmelden und leave für Abmelden
	String action;
	
	String userId;
	
	String password;
	
	String urlSrc;
	
	String courseId;
	
	String coursePw;
	
	
	public CourseDto(String action, String userId, String password, String urlSrc, String courseId, String coursePw) {
		super();
		this.action = action;
		this.userId = userId;
		this.password = password;
		this.urlSrc = urlSrc;
		this.courseId = courseId;
		this.coursePw = coursePw;
	}
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUrlSrc() {
		return urlSrc;
	}
	public void setUrlSrc(String urlSrc) {
		this.urlSrc = urlSrc;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getCoursePw() {
		return coursePw;
	}
	public void setCoursePw(String coursePw) {
		this.coursePw = coursePw;
	}
	
	
	
	
}
