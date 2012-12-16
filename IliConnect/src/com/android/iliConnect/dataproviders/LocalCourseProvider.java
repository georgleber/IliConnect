package com.android.iliConnect.dataproviders;

import java.util.concurrent.ExecutionException;

import android.database.CursorJoiner.Result;

import com.android.iliConnect.models.CourseData;

public class LocalCourseProvider {
	
	// Speichert Daten, die für An-/Abmeldung von Kursen benötigt werden.
	private CourseData course = null;

	private RemoteCourseProvider prov = new RemoteCourseProvider();
	
	private String user_id = "lukas";
	private String password = "foobar";
	private String url_src = "http://swe.k3mp.de/ilias/";
	
	public void joinCourse(String ref_id, String pw) {
		course = new CourseData("join", this.user_id, this.password, this.url_src, ref_id, pw);
		
		// Kursbeitritt im Hintergrund durchführen
		prov.execute(course);	
	}
	
	public void leaveCourse(String ref_id) {
		course = new CourseData("leave", this.user_id, this.password, this.url_src, ref_id, null);
		// Abmeldung im Hintergrund durchführen
		prov.execute(course);
	}
	
	// Update aktualsiert die View Schreibtishc und löscht Kurs aus XML
	public void update() {

	}
		
}
