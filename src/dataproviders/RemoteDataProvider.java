package dataproviders;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import com.example.ilias.MainActivity;

import android.app.Service;
import android.content.Context;
import android.os.AsyncTask;

public class RemoteDataProvider extends AsyncTask<String,Integer, String>{
	
	  @Override
	    protected String doInBackground(String... sUrl) {
	        try {
	            URL url = new URL(sUrl[0]);
	            URLConnection connection = url.openConnection();
	            connection.connect();
	            // this will be useful so that you can show a typical 0-100% progress bar
	            int fileLength = connection.getContentLength();

	            // download the file
	            InputStream input = new BufferedInputStream(url.openStream());
	            OutputStream output = new FileOutputStream(MainActivity.context.getFilesDir()+"/RemoteData.xml");

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
	        super.onPreExecute();
	        MainActivity.progressDialog.show();
	    }

	    @Override
	    protected void onProgressUpdate(Integer... progress) {
	        super.onProgressUpdate(progress);
	        MainActivity.progressDialog.setProgress(progress[0]);
	    }
	    @Override
	    protected void onPostExecute(String result) {
	    	super.onPostExecute(result);
	    	MainActivity.progressDialog.dismiss();
	    	MainActivity.localDataProvider.updateLocalData();
	    }
}
