package com.android.iliConnect.dataproviders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.MainTabView;
import com.android.iliConnect.models.CourseData;
import com.android.iliConnect.ssl.HttpsClient;


import android.os.AsyncTask;

public class RemoteCourseProvider extends AsyncTask<CourseData, Integer, Object> {
		
	@Override
	protected Object doInBackground(CourseData... crs)  { 		

		// nur ersten Kurs verarbeiten
		CourseData course = crs[0];
		
		// Creating HTTP client
		HttpParams params = new BasicHttpParams();
		// Timeout für Verbindungsaufbau definieren
		HttpConnectionParams.setConnectionTimeout(params, 5000);
		HttpClient httpClient = new DefaultHttpClient(params);
		
		// mache aus http einen httpsClient
		HttpClient httpsClient = HttpsClient.createHttpsClient(httpClient);

		// Url für Request erstellen
		String url = course.getUrlSrc() + course.getApiPath() + "?action=" + course.getAction() + "&ref_id=" + course.getCourseId();

		// Creating HTTP Post
		HttpPost httpPost = new HttpPost(url);
		
						
		// Building post parameters, key and value pair
	    List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
		nameValuePair.add(new BasicNameValuePair("username", course.getUserId()));
		nameValuePair.add(new BasicNameValuePair("password", course.getPassword()));
		if(course.getCoursePw() != null) {
			nameValuePair.add(new BasicNameValuePair("course_pw", course.getCoursePw()));
		}
			
		try {
			// Url Encoding the POST parameters
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
		}
		catch (UnsupportedEncodingException e) {
			 e.printStackTrace();
		}
		
		String responseMessage = null;
		try {
			// Http-Request ausfuehren
			HttpResponse response = httpsClient.execute(httpPost);
			
			StatusLine status = response.getStatusLine();
			if(status.getStatusCode() < 200 || status.getStatusCode() > 207) {
				throw new HttpException(status.getReasonPhrase());
			}
			
			// Antwortnachricht aus Response auslesen
			responseMessage = this.getResponseMessage(response);
			
		} catch (Exception e) {
			e.printStackTrace();
			return e;
		} 			
		// Mit return wird das Ergebnis im result bereitgestellt 
		return responseMessage;
	}
	
	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);

		MainTabView.instance.update();
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
