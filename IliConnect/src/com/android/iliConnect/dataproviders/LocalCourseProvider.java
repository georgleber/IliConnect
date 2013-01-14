package com.android.iliConnect.dataproviders;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.os.AsyncTask;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.MainTabView;
import com.android.iliConnect.MessageBuilder;
import com.android.iliConnect.Exceptions.CoursePasswordException;
import com.android.iliConnect.Exceptions.JoinCourseException;
import com.android.iliConnect.Exceptions.NetworkException;
import com.android.iliConnect.models.Authentification;
import com.android.iliConnect.models.CourseData;

public class LocalCourseProvider {

	private CourseData course = null;

	private RemoteCourseProvider courseProv = new RemoteCourseProvider();

	private RemoteDataProvider remoteProv = MainActivity.instance.remoteDataProvider;
	private LocalDataProvider localProv = MainActivity.instance.localDataProvider;

	public String joinCourse(String ref_id, String pw) throws JoinCourseException, CoursePasswordException, NetworkException {

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
				
				if (MainActivity.instance.notificationThread.doAsynchronousTask != null) {
					MainActivity.instance.notificationThread.doAsynchronousTask.cancel();
					
					MainActivity.instance.notificationThread.startTimer();
				}
				
			}
			else if(result.contains("PASSWORD_NEEDED")) {
				return result;
			}
			else if(result.contains("JOIN_REQUEST_SENT")) {
				MessageBuilder.course_join_request_send(MainTabView.instance);
			}
			else if(result.contains("WAITING_FOR_CONFIRMATION")) {
				MessageBuilder.course_waiting_for_confirm(MainTabView.instance);
			}
			else if(result.contains("ALREADY_SUBSCRIBED")) {
				MessageBuilder.course_alreadysignedin(MainTabView.instance);
			}
			else if(result.contains("not a course object")) {
				MessageBuilder.course_notexist(MainTabView.instance,ref_id);
			} 
			else if(result.contains("WRONG_PASSWORD")) {				
				MessageBuilder.course_passwordfalse(MainTabView.instance,ref_id);
			}
			else if(result.contains("PERMISSION_DENIED")) {
				MessageBuilder.course_permissondenied(MainTabView.instance, ref_id);
			} 
			else{
				MessageBuilder.QR_error(MainTabView.instance);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public String leaveCourse(String ref_id) throws JoinCourseException, NetworkException {

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
