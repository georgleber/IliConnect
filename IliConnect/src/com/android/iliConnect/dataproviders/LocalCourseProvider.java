package com.android.iliConnect.dataproviders;

import java.util.concurrent.ExecutionException;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.models.Authentification;
import com.android.iliConnect.models.CourseData;

public class LocalCourseProvider {
	
	// Speichert Daten, die für An-/Abmeldung von Kursen benötigt werden.
	private CourseData course = null;

	private RemoteCourseProvider prov = new RemoteCourseProvider();
	MainActivity main = new MainActivity();

	// Fürs Syncen
	private RemoteDataProvider rP = new RemoteDataProvider();
	private LocalDataProvider lp = new LocalDataProvider();
	
	private Authentification auth = new Authentification();
	
	
	// Testdaten
	private String user_id = "florian";
	private String password = "florian";
	private String url_src = "https://swe.k3mp.de/ilias/";
	
	public String joinCourse(String ref_id, String pw) throws JoinCourseException, CoursePasswordException {
		// Authnetifizierung aus XML auslesen
		auth.load();
		//course = new CourseData("join", auth.user_id, auth.password, auth.url_src, ref_id, pw);
		
		// TODO richtige User-Daten verwenden
		course = new CourseData("join", this.user_id, this.password, this.url_src, ref_id, pw);
		
		// Kursbeitritt im Hintergrund durchfuehren
		prov.execute(course);
		try {
			// Result enthaelt die Ergebnisnachricht des http-Requests. Mit get() wird auf das Ende des 
			// asnyc. Aufrufs gewartet. 
			String result = prov.get();
			
			if(result.contains("JOINED")) {
				// Sync in Background
				
				this.rP.execute(this.lp.remoteData.getSyncUrl() + "?action=sync");
				return result;
			}
			else if(result.contains("PASSWORD_NEEDED")) {
				return result;
			}
			
			// Falls andere Response-Message von Server empfangen wurde, entsprchende Eception werfen
			else if(result.contains("ALREADY_SUBSCRIBED")) {
				throw new JoinCourseException("Sie sind bereits im Kurs angemeldet.");
			}
			else if(result.contains("not a course object")) {
				throw new JoinCourseException("Der Kurs ist nicht vorhanden");
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
		return null;
	}
	
	
	public String leaveCourse(String ref_id) throws JoinCourseException {

		auth.load();
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

				this.rP.execute(this.lp.remoteData.getSyncUrl() + "?action=sync");
				return "LEFT";		
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}		
}
