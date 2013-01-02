package com.android.iliConnect.dataproviders;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import org.xmlpull.v1.XmlPullParser;
import com.android.iliConnect.MainActivity;
import com.android.iliConnect.MainTabView;
import com.android.iliConnect.models.Authentification;
import com.android.iliConnect.models.ClassAlias;
import com.android.iliConnect.models.Desktop;
import com.android.iliConnect.models.Item;
import com.android.iliConnect.models.LocalData;
import com.android.iliConnect.models.Notifications;
import com.android.iliConnect.models.RemoteData;
import com.android.iliConnect.models.Results;
import com.android.iliConnect.models.Settings;

import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.widget.Toast;

public class LocalDataProvider {

	private static LocalDataProvider instance;

	public Settings settings = new Settings();
	public Notifications notifications = new Notifications();

	public LocalData localdata = new LocalData();
	public RemoteData remoteData = new RemoteData();
	public Desktop desktopItems = new Desktop();
	public Authentification auth = new Authentification();;
	public ArrayList<ClassAlias> classAliases = new ArrayList<ClassAlias>();
	public Results results = new Results();
	
	public String searchDataFileName = "SearchData.xml";
	public String remoteDataFileName = "RemoteData.xml";
	public String localDataFilename = "LocalData.xml";
	
	public static boolean isAvaiable = false;
	public boolean isUpdating = false;
	public ReentrantLock syncObject;
	
	
	public static LocalDataProvider getInstance() {
		if (instance == null) {
			instance = new LocalDataProvider();
	
		}
		return instance;
	}

	public void init(int xmLRes) {
		
		syncObject = new ReentrantLock();
		
		File config = new File(MainActivity.instance.getFilesDir() + "/" + localDataFilename);
		if (!config.exists()) {
			XmlResourceParser xpp = MainActivity.currentActivity.getResources().getXml(xmLRes);
			StringBuffer stringBuffer = new StringBuffer();
			try {
				xpp.next();
				int eventType = xpp.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {

					if (eventType == XmlPullParser.START_TAG) {
						stringBuffer.append("<" + xpp.getName() + ">");
					} else if (eventType == XmlPullParser.END_TAG) {
						stringBuffer.append("</" + xpp.getName() + ">");
					} else if (eventType == XmlPullParser.TEXT) {
						stringBuffer.append(xpp.getText());
					}
					eventType = xpp.next();
				}
				OutputStream output = new FileOutputStream(MainActivity.instance.getFilesDir() + "/" + localDataFilename);
				output.write(stringBuffer.toString().getBytes());

			} catch (Exception e) {
				Toast t = Toast.makeText(MainActivity.instance, "Fehler beim Erstellen der lokalen Konfigurationsdatei.", Toast.LENGTH_LONG);
				t.show();
			}

		}

	}

	public boolean updateLocalData() {
		try {
			
				// desktopItems.load();
				remoteData.load();


				desktopItems.DesktopItem = remoteData.Current.Desktop.DesktopItem;
				notifications.Notifications = remoteData.Current.Notifications;
				isAvaiable = true;
				isUpdating = false;
			
			if(new File(MainActivity.instance.getFilesDir() + "/" + searchDataFileName).exists())
				results.load();
			
			 MainTabView.getInstance().update();
			
		} catch (Exception e) {
			return false;
		}
		return true;

	}

	public void openFileOrDownload(String refID) {

		File file = new File(MainActivity.instance.getFilesDir() + "/" + refID);
		if (!file.exists()) {
			RemoteDataProvider download = new RemoteDataProvider();
			download.execute(new String[] { auth.url_src + "webdav.php?ref_id=" + refID, refID });
			synchronized (MainActivity.instance.localDataProvider.syncObject) {
				try {
					MainActivity.instance.localDataProvider.syncObject.wait(100);
				} catch (InterruptedException e) {

				}
			}
		}
		if (file.exists()) {
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(android.content.Intent.ACTION_VIEW);
			// Setting up the data and the type for the intent

			intent.setData(Uri.fromFile(file));

			// will start the activtiy found by android or show a dialog to select one
			MainActivity.instance.startActivity(intent);
		} else {
			Toast t = Toast.makeText(MainActivity.instance, "Download Fehlgeschlagen", Toast.LENGTH_LONG);
			t.show();
		}

	}

}
