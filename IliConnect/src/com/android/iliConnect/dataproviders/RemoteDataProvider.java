package com.android.iliConnect.dataproviders;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.locks.ReentrantLock;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.android.iliConnect.MainActivity;

public class RemoteDataProvider extends AsyncTask<String, Integer, String> {
	
	public ReentrantLock syncObject;
	private ProgressDialog pDialog;

	public RemoteDataProvider(){
		this.syncObject = new ReentrantLock();
	}
	public RemoteDataProvider(ProgressDialog pDialog){
		this.syncObject = new ReentrantLock();
		this.pDialog = pDialog;
	}
	
	@Override
	protected String doInBackground(String... sUrl) {
		try {
			URL url = new URL(sUrl[0]);
			String targetName = MainActivity.instance.localDataProvider.remoteDataFileName;
			if (sUrl.length > 1)
				targetName = sUrl[1];

			URLConnection connection = url.openConnection();
			connection.connect();
			// this will be useful so that you can show a typical 0-100% progress bar
			int fileLength = connection.getContentLength();

			// download the file
			InputStream input = new BufferedInputStream(url.openStream());

			OutputStream output = new FileOutputStream(MainActivity.instance.getFilesDir() + "/" + targetName);

			byte data[] = new byte[1024];
			long total = 0;
			int count;
			while ((count = input.read(data)) != -1) {
				total += count;
				// publishing the progress....
				publishProgress((int) (total * 100 / fileLength));
				output.write(data, 0, count);
			}

			output.flush();
			output.close();
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
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
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		
		
		MainActivity.instance.localDataProvider.updateLocalData();
		synchronized ( this.syncObject) {
			this.syncObject.notifyAll();
		}

		if (pDialog != null && pDialog.isShowing())
			pDialog.dismiss();
		
		
	}

}
