package com.android.iliConnect.dataproviders;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import com.android.iliConnect.MainActivity;

public class RemoteDataProvider extends AsyncTask<String, Integer, Exception> {

	private List<NameValuePair> nameValuePairs;
	private ProgressDialog pDialog;

	public RemoteDataProvider() {
		
	}

	public RemoteDataProvider(ProgressDialog pDialog) {

		this.pDialog = pDialog;
	}

	public RemoteDataProvider(List<NameValuePair> nameValuePairs, ProgressDialog pDialog) {
		this.pDialog = pDialog;
		this.nameValuePairs = nameValuePairs;
	}

	public RemoteDataProvider(List<NameValuePair> nameValuePairs) {

		this.nameValuePairs = nameValuePairs;
	}

	@Override
	protected Exception doInBackground(String... sUrl) {

		try {

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(sUrl[0]);

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("username", MainActivity.instance.localDataProvider.auth.user_id));
			nameValuePairs.add(new BasicNameValuePair("password", MainActivity.instance.localDataProvider.auth.password));

			if (this.nameValuePairs != null) {
				nameValuePairs.addAll(this.nameValuePairs);
			}
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			HttpEntity entity = response.getEntity();

			if (entity != null) {
				InputStream instream = entity.getContent();

				// String result = convertStreamToString(instream);
				String targetName = MainActivity.instance.localDataProvider.remoteDataFileName;
				if (sUrl.length > 1)
					targetName = sUrl[1];

				// download the file
				InputStream input = instream;

				String s = convertStreamToString(instream);

				if (s.contains("ACCESS_DENIED"))
					throw new AuthException(s);
				
				
				BufferedWriter out = new BufferedWriter(new FileWriter(MainActivity.instance.getFilesDir() + "/" + targetName));
				out.write(s);

				out.flush();
				out.close();
				input.close();

			}

		} catch (Exception e) {
			return e;

		}
		return null;

	}

	@Override
	protected void onPreExecute() {
		MainActivity.instance.localDataProvider.isUpdating = true;
		if (pDialog != null)
			pDialog.show();
	};

	@Override
	protected void onProgressUpdate(Integer... values) {
		// increment progress bar by progress value
		if (pDialog != null)
			pDialog.setProgress(values[0]);
	}

	@Override
	protected void onPostExecute(Exception e) {
		// super.onPostExecute(result);
		if (e != null){
			MainActivity.instance.logout();
			MainActivity.instance.showToast(e.getMessage());
		}

		MainActivity.instance.localDataProvider.updateLocalData();
	
			if (pDialog != null && pDialog.isShowing())
			pDialog.dismiss();

	}

	public String convertStreamToString(InputStream inputStream) throws IOException {
		if (inputStream != null) {
			StringBuilder sb = new StringBuilder();
			String line;
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				while ((line = reader.readLine()) != null) {
					sb.append(line).append("\n");
				}
			} finally {
				inputStream.close();
			}
			return sb.toString();
		} else {
			return "";
		}
	}
}
