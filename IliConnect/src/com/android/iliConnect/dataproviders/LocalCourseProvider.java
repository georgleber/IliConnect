package com.android.iliConnect.dataproviders;

import java.util.concurrent.ExecutionException;

import android.database.CursorJoiner.Result;

import com.android.iliConnect.Schreibtisch;
import com.android.iliConnect.models.CourseData;

public class LocalCourseProvider {
	
	// Speichert Daten, die f��r An-/Abmeldung von Kursen ben��tigt werden.
	private CourseData course = null;

	private RemoteCourseProvider prov = new RemoteCourseProvider();
	
	// Testdaten
	private String user_id = "lukas";
	private String password = "foobar";
	private String url_src = "http://swe.k3mp.de/ilias/";
	
	public void joinCourse(String ref_id, String pw) throws JoinCourseException, CoursePasswordException {
		course = new CourseData("join", this.user_id, this.password, this.url_src, ref_id, pw);
		
		// Kursbeitritt im Hintergrund durchf��hren
		prov.execute(course);
		try {
			// Result enth��lt die Ergebnisnachricht des http-Requests. Mit get() wird auf das Ende des 
			// asnyc. Aufrufs gewartet. 
			String result = prov.get();
			
			if(result.equals("JOINED")) {
				// TODO: wenn Benutzer dem Kurs hinzugef��gt wurde, neuen Sync. durchf��hren
				// und Kurs in localData eintragen und zu Schreibtisch wechseln.
			}
			else if(result.contains("ALREADY_SUBSCRIBED")) {
				throw new JoinCourseException("Sie sind bereits im Kurs angemeldet.");
			}
			else if(result.contains("not a course object")) {
				throw new JoinCourseException("Der Kurs ist nicht vorhanden");
			} 
			else if(result.contains("PASSWORD_NEEDED")) {
				throw new CoursePasswordException("Bitte geben Sie das Passwort des Kurses an.");
			}
			else if(result.contains("WRONG_PASSWORD")) {
				throw new CoursePasswordException("Das Passwort des Kurses ist nicht korrekt.");
			}
			else if(result.contains("PERMISSION_DENIED")) {
				throw new JoinCourseException("Zugriff verweigert");
			} 
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	
	public void leaveCourse(String ref_id) throws JoinCourseException {
		course = new CourseData("leave", this.user_id, this.password, this.url_src, ref_id, null);
		
		// Abmeldung im Hintergrund durchfuehren
		prov.execute(course);
		
		try {
			// Result enthaelt die Ergebnisnachricht des http-Requests. Mit get() wird auf das Ende des 
			// asnyc. Aufrufs gewartet. 
			String result = prov.get();
			if(result.contains("not a course object")) {
				throw new JoinCourseException("Der Kurs ist nicht vorhanden");
			}
			else if(result.contains("PERMISSION_DENIED")) {
				throw new JoinCourseException("Zugriff verweigert");
			} 
			else {
				// TODO: Kurs aus lokalen Daten entfernen, Schreibtisch updaten.
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}		
}
