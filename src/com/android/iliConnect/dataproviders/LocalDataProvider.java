package com.android.iliConnect.dataproviders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.models.Authentification;
import com.android.iliConnect.models.ClassAlias;
import com.android.iliConnect.models.Desktop;
import com.android.iliConnect.models.DesktopItem;
import com.android.iliConnect.models.Notifications;
import com.android.iliConnect.models.Settings;

import android.app.Notification;
import android.provider.MediaStore.Files;
import android.widget.Toast;

public class LocalDataProvider {

	private static LocalDataProvider instance;
	private String remoteDataFileName;
	public Settings settings;
	public Authentification auth;
	public Notifications notifications;
	public Desktop desktopItems;

	private ArrayList<ClassAlias> classAliases = new ArrayList<ClassAlias>();
	private String localDataFilename = MainActivity.context.getFilesDir().getAbsolutePath() + "/RemoteData.xml";

	public static LocalDataProvider getInstance() {
		if (instance == null)
			instance = new LocalDataProvider();
		return instance;
	}

	public void readConfig() {

		if (remoteDataFileName == null)
			remoteDataFileName = auth.url_src;

		if (classAliases.size() == 0)
			provideClassAliases();

		try {

			File configFile = new File(localDataFilename);
			while (!configFile.exists()) {
				Thread.sleep(100);
			}

			this.updateLocalData();
		} catch (InterruptedException e) {
		} catch (Exception e) {
			Toast toast = Toast.makeText(MainActivity.context, "Fehler beim Deserialisieren", 1000);
			toast.show();
		}

	}

	private void provideClassAliases() {
		this.classAliases.add(new ClassAlias("Settings", Settings.class));
		this.classAliases.add(new ClassAlias("Authentification", Authentification.class));
		this.classAliases.add(new ClassAlias("Desktop", Desktop.class));
		this.classAliases.add(new ClassAlias("Notifications", Notifications.class));
	}

	public void updateLocalData() {

		try {

			Serialization ser = new Serialization();

			BufferedReader reader = new BufferedReader(new InputStreamReader(MainActivity.context.openFileInput("RemoteData.xml")));
			String xmlContent = "";
			String line = null;
			while ((line = reader.readLine()) != null)
				xmlContent += line + "\n";
			reader.close();

			settings = (Settings) ser.deserialize(xmlContent, this.classAliases, "Settings");
			auth = (Authentification) ser.deserialize(xmlContent, this.classAliases, "Authentification");
			notifications = (Notifications) ser.deserialize(xmlContent, this.classAliases, "Notifications", true);
			desktopItems = (Desktop) ser.deserialize(xmlContent, this.classAliases, "Desktop", true);

		} catch (FileNotFoundException e) {
		} catch (Exception e) {
			Toast toast = Toast.makeText(MainActivity.context, e.getMessage(), Toast.LENGTH_LONG);
			toast.show();
		}

	}
}
