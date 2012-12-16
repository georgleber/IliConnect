package com.android.iliConnect.dataproviders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import com.android.iliConnect.models.CourseDto;


import android.os.AsyncTask;

public class RemoteCourseProvider extends AsyncTask<CourseDto, Integer, String> {
	
	// Pfad zum An/Abmelde Plugin
	private final String pluginPath = "IliConnect.Courses.php";
	private LocalCourseProvider prov = new LocalCourseProvider();
	
	@Override
	protected String doInBackground(CourseDto... crs)  { 		

		// nur ersten Kurs verarbeiten
		CourseDto course = crs[0];
		
		// Creating HTTP client
		HttpClient httpClient = new DefaultHttpClient();
		
		// Url für Request erstellen
		String url = course.getUrlSrc() + this.pluginPath + "/";
		url += "?action=" + course.getAction() + "&";
		if(course.getAction().equals("join") && course.getPassword() != null) {
			url += "course_pw=" + course.getCoursePw();
		}
		url += "&ref_id=" + course.getCourseId();

		// Creating HTTP Post
		HttpPost httpPost = new HttpPost(url);
				
		// Building post parameters, key and value pair
	    List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
		nameValuePair.add(new BasicNameValuePair("username", course.getUserId()));
		nameValuePair.add(new BasicNameValuePair("password", course.getPassword()));
			
		
		try {
			// Url Encoding the POST parameters
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
		}
		catch (UnsupportedEncodingException e) {
			 e.printStackTrace();
		}
		

		String responseMessage = null;
		try {
			// Http Request ausführen
			HttpResponse response = httpClient.execute(httpPost);
			
			// Anwortnachricht aus Response auslesen
			responseMessage = this.getResponseMessage(response);

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		// return String schreibt das Ergebnis in result von postExecute
		return responseMessage;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

	}
	
	// Liest den Text aus der Http Responsen aus.
	private String getResponseMessage(HttpResponse response) {
		InputStream is = null;
		try {
			is = response.getEntity().getContent();
		} catch (IllegalStateException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
					
		StringBuilder str = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				str.append(line + "\n");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
			}
		}
		return str.toString();
	}

}
