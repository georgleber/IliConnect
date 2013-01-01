package com.android.iliConnect.dataproviders;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import com.android.iliConnect.MainActivity;
import com.android.iliConnect.MainTabView;
import com.android.iliConnect.models.Authentification;
import com.android.iliConnect.models.ClassAlias;
import com.android.iliConnect.models.Desktop;
import com.android.iliConnect.models.DesktopItem;
import com.android.iliConnect.models.Item;
import com.android.iliConnect.models.LocalData;
import com.android.iliConnect.models.Notifications;
import com.android.iliConnect.models.RemoteData;
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
	public String remoteDataFileName = "RemoteData.xml";
	public String localDataFilename = "LocalData.xml";
	public boolean isAvaiable = false;

	public static LocalDataProvider getInstance() {
		if (instance == null) {
			instance = new LocalDataProvider();
			instance.classAliases.add(new ClassAlias("Settings", Settings.class));
			instance.classAliases.add(new ClassAlias("Authentification", Authentification.class));
			instance.classAliases.add(new ClassAlias("Desktop", Desktop.class));
			instance.classAliases.add(new ClassAlias("Notifications", Notifications.class));

		}
		return instance;
	}

	public void init(int xmLRes) {
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

			if (new File(MainActivity.instance.getFilesDir() + "/" + remoteDataFileName).exists()) {
				// desktopItems.load();
				remoteData.load();
				desktopItems.DesktopItem = remoteData.Current.Desktop;
				notifications.Notifications = remoteData.Current.Notifications;
				isAvaiable = true;
			}
			MainActivity.instance.remoteDataProvider.synchronizeObject.notifyAll();

			// MainTabView.getInstance().instance.update(1);

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
			synchronized (download.synchronizeObject) {
				try {
					download.synchronizeObject.wait(5);
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

	public Item findItemByRefID(String refID) {
		for (Item subItem : desktopItems.DesktopItem)
			return findItem(subItem, refID);

		return null;
	}

	public Item walk(List<Item> items, String refID) {
		for (Item subItem : items)
			return findItem(subItem, refID);
		return null;
	}

	public Item findItem(Item item, String refID) {
		if (item.ref_id.equals(refID))
			return item;
		else if (item.getClass().equals(DesktopItem.class) && ((DesktopItem) item).Items != null) {
			return walk(((DesktopItem) item).Items, refID);
		} else if (item.Item != null)
			return walk(item.Item, refID);
		return item;
	}

}
