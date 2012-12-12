package com.android.iliConnect.dataproviders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.MainTabView;
import com.android.iliConnect.R;
import com.android.iliConnect.models.Authentification;
import com.android.iliConnect.models.ClassAlias;
import com.android.iliConnect.models.Desktop;
import com.android.iliConnect.models.Notifications;
import com.android.iliConnect.models.Settings;


import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.support.v4.app.FragmentActivity;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class LocalDataProvider {

	private static LocalDataProvider instance;

	public Settings settings = new Settings();
	public Notifications notifications = new Notifications();
	public Desktop desktopItems = new Desktop();
	public Authentification auth = new Authentification();;
	public static ArrayList<ClassAlias> classAliases = new ArrayList<ClassAlias>();
	public String remoteDataFileName = "RemoteData.xml";
	public String localDataFilename = "LocalData.xml";

	public static LocalDataProvider getInstance() {
		if (instance == null) {
			instance = new LocalDataProvider();
			classAliases.add(new ClassAlias("Settings", Settings.class));
			classAliases.add(new ClassAlias("Authentification", Authentification.class));
			classAliases.add(new ClassAlias("Desktop", Desktop.class));
			classAliases.add(new ClassAlias("Notifications", Notifications.class));
		}
		return instance;
	}

	public void init(int xmLRes) {
		File config = new File(MainActivity.context.getFilesDir() + "/" + localDataFilename);
		 
		
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
				OutputStream output = new FileOutputStream(MainActivity.context.getFilesDir() + "/" + localDataFilename);
				output.write(stringBuffer.toString().getBytes());

			} catch (Exception e) {
				Toast t = Toast.makeText(MainActivity.context, "Fehler beim Erstellen der lokalen Konfigurationsdatei.", Toast.LENGTH_LONG);
				t.show();
			}

		}
	}

	public boolean updateLocalData() {

		try {

			
			notifications.load();
			desktopItems.load();
			
			
			MainTabView.getInstance().instance.update(1);
			
			
		} catch (Exception e) {
			return false;
		}
		return true;

	}
}
