package com.android.iliConnect.models;

import org.simpleframework.xml.Element;
import android.widget.Toast;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.dataproviders.PersistableObject;

@Element
public class Settings extends PersistableObject {
	@Element
	public boolean sync = true;
	@Element
	public int interval = 60;
	@Element
	public boolean sync_wlanonly = false;
	@Element
	public int num_notifications = 2;
	@Element
	public int level_warning = 3;
	@Element
	public int level_critical = 1;

	public Settings() {

	}

	public void load() {

		try {
			 super.deserialize(MainActivity.instance.localDataProvider.localDataFilename);
			
		} catch (Exception e) {
			Toast t = Toast.makeText(MainActivity.instance, "Fehler beim laden der Einstellungen, nutze Standardkonfiguration.", Toast.LENGTH_LONG);
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
