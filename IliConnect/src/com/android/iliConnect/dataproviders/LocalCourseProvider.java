package com.android.iliConnect.dataproviders;

import java.util.concurrent.ExecutionException;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.MainTabView;
import com.android.iliConnect.models.Authentification;
import com.android.iliConnect.models.CourseData;

public class LocalCourseProvider {

	private CourseData course = null;

	private RemoteCourseProvider courseProv = new RemoteCourseProvider();

	private RemoteDataProvider remoteProv = MainActivity.instance.remoteDataProvider;
	private LocalDataProvider localProv = MainActivity.instance.localDataProvider;

	public String joinCourse(String ref_id, String pw) throws JoinCourseException, CoursePasswordException {

		course = new CourseData("join", localProv.auth.user_id, localProv.auth.password, localProv.auth.url_src, localProv.auth.api_src, ref_id, pw);
		
		// Kursbeitritt im Hintergrund durchfuehren
		courseProv.execute(course);
		try {
			// Result enthaelt die Ergebnisnachricht des http-Requests. Mit get() wird auf das Ende des 
			// asnyc. Aufrufs gewartet. 
			String result = courseProv.get();
			
			if(result.contains("JOINED")) {			
				// Nach Anmeldung, neu syncen. Durch null wird keine Sync-Meldung angezeigt
				MainActivity.instance.sync(null);
				
				// Bei erfolgreicher Anmeldung Schreibtisch anzeigen.
				MainTabView.instance.changeViewTo(3);
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
				throw new JoinCourseException("Eine Anmeldung ist nicht möglich.");
			} 
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public String leaveCourse(String ref_id) throws JoinCourseException {

		course = new CourseData("leave", localProv.auth.user_id, localProv.auth.password, localProv.auth.url_src, localProv.auth.api_src, ref_id, null);

		// Abmeldung im Hintergrund durchfuehren
		courseProv.execute(course);
		
		try {
			// Result enthaelt die Ergebnisnachricht des http-Requests. Mit get() wird auf das Ende des 
			// asnyc. Aufrufs gewartet. 
			String result = courseProv.get();

			if(result.contains("not a course object")) {
				throw new JoinCourseException("Der Kurs ist nicht vorhanden");
			}
			else if(result.contains("PERMISSION_DENIED")) {
				throw new JoinCourseException("Zugriff verweigert");
			} 
			else {
				// Nach Abmeldung, neu syncen. Durch null wird keine Sync-Meldung angezeigt
				MainActivity.instance.sync(null);
				
				MainTabView.instance.update();
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
