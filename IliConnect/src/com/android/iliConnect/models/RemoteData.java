package com.android.iliConnect.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import android.widget.Toast;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.dataproviders.PersistableObject;

@Root(name = "Iliconnect")
public class RemoteData extends PersistableObject {
	@Element
	public Current Current;

	@Override
	public void load() throws Exception {
		super.deserialize(MainActivity.instance.localDataProvider.remoteDataFileName);

		for (Item item : this.Current.Desktop.DesktopItem) {
			if (item.getType().equalsIgnoreCase("crs")) {
				Item delItem = new Item();
				delItem.title = "Abmelden";
				delItem.type = "UNSIGN";
				delItem.description = "Vom Kurs abmelden";
				item.Item.add(delItem);
			}

		}
	}

	public String getSyncUrl() {
		return MainActivity.instance.localDataProvider.auth.url_src + MainActivity.instance.localDataProvider.auth.api_src;
	}

	public void save() {

		try {
			super.serialize(MainActivity.instance.localDataProvider.remoteDataFileName);

		} catch (Exception e) {
			Toast t = Toast.makeText(MainActivity.instance, "Fehler beim speichern der Einstellungen, nutze Standardkonfiguration.", Toast.LENGTH_LONG);
			t.show();
		}
	}

	public void delete() {
		super.delete(MainActivity.instance.localDataProvider.remoteDataFileName);

	}

}
