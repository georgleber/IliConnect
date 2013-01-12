package com.android.iliConnect.models.modification;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import android.widget.Toast;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.dataproviders.PersistableObject;

@Root(name = "Iliconnect")
public class AppData extends PersistableObject {

	@Element (required = false)
	public NotificationData NotificationData;

	@Override
	public void load() {
		try {
			super.deserialize(MainActivity.instance.localDataProvider.appDataFileName);
		} catch (Exception e) {
			e.printStackTrace();
			MainActivity.instance.showToast(e.getMessage());
		}
	}

	public void save() {
		try {
			super.serialize(MainActivity.instance.localDataProvider.appDataFileName);

		} catch (Exception e) {
			Toast t = Toast.makeText(MainActivity.instance, "Fehler beim speichern der Applikations-Daten, nutze Standardkonfiguration.", Toast.LENGTH_LONG);
			t.show();
			e.printStackTrace();
		}
	}

	public NotificationData getNotificationData() {
		return NotificationData;
	}
}
