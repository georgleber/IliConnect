package com.android.iliConnect.dataproviders;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.Exceptions.NetworkException;
import com.android.iliConnect.ssl.HttpsClient;

public class IliasNotifier extends AsyncTask<String, Integer, String> {


	@Override
	protected String doInBackground(String... sUrl) {

		try {
			String url = sUrl[0];
			String filePath = sUrl[1];
			// Creating HTTP client
			HttpClient httpClient = new DefaultHttpClient();

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

			// als zweites Datei per Get laden
			HttpGet get = new HttpGet(url);
			response = httpsClient.execute(get);

			HttpEntity entity = response.getEntity();
			InputStream in = entity.getContent();

			// Save the file to SD
			/*
			File path = Environment.getExternalStoragePublicDirectory(filePath);
			path.mkdirs();
			
			*/
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
				// publishing the progress....
				// publishProgress((int) (total * 100 / fileLength));
				fos.write(data, 0, count);
			}

			fos.flush();
			fos.close();
			in.close();
			try {
				MainActivity.instance.sync(null);
			} catch (NetworkException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
