package com.android.iliConnect.models;

import android.widget.Toast;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.dataproviders.PersistableObject;

public class Settings extends PersistableObject {
	public boolean sync;
	public int interval = 30;
	public boolean sync_wlanonly;
	public int num_notifications;
	public int level_warning;
	public int level_critical;

	public Settings() {

	}

	public void load() {

		try {
			 super.deserialize(MainActivity.localDataProvider.localDataFilename);
			
		} catch (Exception e) {
			Toast t = Toast.makeText(MainActivity.context, "Fehler beim laden der Einstellungen, nutze Standardkonfiguration.", Toast.LENGTH_LONG);
			t.show();
		}

	}

	public Settings(Authentification auth, boolean sync, int interval, boolean sync_wlanonly, int num_notifications, int level_warning, int level_critical) {
		super();
		this.sync = sync;
		this.interval = interval;
		this.sync_wlanonly = sync_wlanonly;
		this.num_notifications = num_notifications;
		this.level_warning = level_warning;
		this.level_critical = level_critical;
	}

}
