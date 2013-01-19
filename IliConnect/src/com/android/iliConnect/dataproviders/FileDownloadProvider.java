package com.android.iliConnect.dataproviders;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.MainTabView;
import com.android.iliConnect.MessageBuilder;
import com.android.iliConnect.ssl.HttpsClient;

public class FileDownloadProvider extends AsyncTask<String, Integer, Exception> {

	ProgressDialog progressDialog;
	private Activity instance; 
	public boolean isRunning = false;
	

	public FileDownloadProvider(ProgressDialog progressDialog, Activity instance) {
		this.progressDialog = progressDialog;
		this.instance = instance;
		
	}
	
	public FileDownloadProvider() {

	}

	@Override
	protected Exception doInBackground(String... sUrl) {

		try {
			String url = sUrl[0];
			String filePath = sUrl[1];
			
			// Creating HTTP client
			HttpParams params = new BasicHttpParams();
			// Timeout für Verbindungsaufbau definieren
			HttpConnectionParams.setConnectionTimeout(params, 5000);
			HttpClient httpClient = new DefaultHttpClient(params);

			// mache aus http einen httpsClient
			HttpClient httpsClient = HttpsClient.createHttpsClient(httpClient);

			// erst Post gegenüber Login durchführen
			HttpPost post = new HttpPost(MainActivity.instance.localDataProvider.auth.url_src + "login.php");

			List<NameValuePair> postFields = new ArrayList<NameValuePair>(2);

			// Set the post fields
			postFields.add(new BasicNameValuePair("username", MainActivity.instance.localDataProvider.auth.user_id));
			postFields.add(new BasicNameValuePair("password", MainActivity.instance.localDataProvider.auth.password));
			post.setEntity(new UrlEncodedFormEntity(postFields, HTTP.UTF_8));

			HttpResponse response = null;
			response = httpsClient.execute(post);
			
			StatusLine status = response.getStatusLine();
			
			if(status.getStatusCode() < 200 || status.getStatusCode() > 207) {
				throw new HttpException(status.getReasonPhrase());
			}

			// als zweites Datei per Get laden
			HttpGet get = new HttpGet(url);
			response = httpsClient.execute(get);
			
			HttpEntity entity = response.getEntity();
			
			if(status.getStatusCode() < 200 || status.getStatusCode() > 207) {
				throw new HttpException(status.getReasonPhrase());
			}

			InputStream in = entity.getContent();

			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);

			byte data[] = new byte[1024];
			long total = 0;
			int count;
			while ((count = in.read(data)) != -1) {
				total += count;
				fos.write(data, 0, count);
			}

			fos.flush();
			fos.close();
			in.close();

		} catch (IOException e) {
			return e;
		} catch (HttpException e) {
			return e;
		}
		return null;
	}

	@Override
	protected void onPostExecute(Exception e) {
		//super.onPostExecute(e);
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
		
		if(e != null) {
			String errMsg = null;
			if(e instanceof HttpHostConnectException) {
				errMsg = "Es konnte keine Verbindung hergestellt werden.";
			}
			else {
				errMsg = "Es ist ein Fehler während des Downloads aufgetreten.";
			}
			
			if(instance != null) {
				MessageBuilder.exception_message(instance, errMsg);
			}
		}
		isRunning = false;
		synchronized (MainActivity.syncObject) {
			MainActivity.syncObject.notifyAll();
		}

	}

	@Override
	protected void onPreExecute() {
		isRunning = true;
		if (progressDialog != null) {
			progressDialog.show();
		}
		super.onPreExecute();

	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		super.onProgressUpdate(progress);
		if (progressDialog != null) {
			progressDialog.setProgress(progress[0]);
		}
	}

}
